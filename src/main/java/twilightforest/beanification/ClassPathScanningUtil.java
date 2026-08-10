package twilightforest.beanification;

import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Utility class for scanning the classpath to find classes annotated with @Component.
 * Scans within the "twilightforest" package hierarchy.
 */
public class ClassPathScanningUtil {

	private static final Logger LOGGER = LoggerFactory.getLogger(ClassPathScanningUtil.class);

	// -----------------------------------------------------------------------------------------------
	// NEVER load these packages on the server. They either:
	//   - only compile against net.minecraft.client.* classes (missing from server JVM classpath)
	//   - reference rendering GUIs (JEI/REI/EMI/Curios) that pull in Minecraft client types
	// Loading any class here on a dedicated server will throw NoClassDefFoundError and crash entrypoint.
	// -----------------------------------------------------------------------------------------------
	private static final List<String> BLACKLIST_PREFIXES;

	static {
		List<String> prefixes = new ArrayList<>();
		// On a dedicated server, skip the entire client source-set root.
		// On the client, we MUST allow twilightforest.client.* so that @Component classes
		// like TravellersClientEvents, ClientGameEvents, FoliageColorHandler etc. get
		// properly instantiated by BeanContext (otherwise their @PostConstruct tick
		// listeners never register and features like V-key hotbar swap silently break).
		if (FabricLoader.getInstance().getEnvironmentType() != EnvType.CLIENT) {
			prefixes.add("twilightforest.client.");
		}
		// Inactive / disabled code that still gets compiled because it's under src/main/java/disabled.
		// This contains JEI/REI/EMI client plugins, Curios renderers, CastleGuardian entity models, etc.
		prefixes.add("twilightforest.disabled.");
		// ASM hooks / utility modules that should not be wired via DI (and may pull client internals)
		prefixes.add("twilightforest.asmhooks.");
		prefixes.add("twilightforest.util.multiparts.");
		// Mixin classes are never beans, and scanning them risks early loading of their targets.
		prefixes.add("twilightforest.mixin.");
		BLACKLIST_PREFIXES = List.copyOf(prefixes);
	}

	// Some class-names don't live under the client packages but still import client classes
	// (e.g. a compat bridge under twilightforest.compat.* that uses GuiGraphics).
	// This suffix list catches common client-only inner classes before Class.forName is called.
	private static final List<String> BLACKLIST_SUFFIXES = List.of(
		"Renderer",
		"RenderState",
		"Model",
		"RendererMixin",
		"EntityRendererMixin",
		"BlockEntityRendererMixin",
		"ItemStackRenderState",
		"RenderLayerParent",
		"GuiGraphics"
	);

	public static Set<Class<?>> findComponentClasses(String scanPackage) {
		Set<Class<?>> componentClasses = new HashSet<>();

		try {
			String packagePath = scanPackage.replace('.', '/');
			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			if (classLoader == null) {
				classLoader = ClassPathScanningUtil.class.getClassLoader();
			}

			Enumeration<URL> resources = classLoader.getResources(packagePath);
			Set<String> visited = new HashSet<>();

			while (resources.hasMoreElements()) {
				URL resource = resources.nextElement();
				String protocol = resource.getProtocol();

				if ("file".equals(protocol)) {
					try {
						File file = Path.of(resource.toURI()).toFile();
						scanFileDirectory(file, scanPackage, componentClasses, visited);
					} catch (URISyntaxException e) {
						LOGGER.warn("Failed to parse file URI: {}", resource, e);
					}
				} else if ("jar".equals(protocol)) {
					String jarPath = resource.getPath();
					int separatorIndex = jarPath.indexOf("!");
					if (separatorIndex > 0) {
						String jarFileUri = jarPath.substring(0, separatorIndex);
						URI jarUri = URI.create("jar:" + jarFileUri + "!/");
						try (FileSystem jarFs = FileSystems.newFileSystem(jarUri, Map.of())) {
							Path jarPackagePath = jarFs.getPath(packagePath);
							if (Files.exists(jarPackagePath)) {
								scanJarPath(jarPackagePath, scanPackage, componentClasses, visited);
							}
						} catch (FileSystemAlreadyExistsException e) {
							try (FileSystem jarFs = FileSystems.getFileSystem(jarUri)) {
								Path jarPackagePath = jarFs.getPath(packagePath);
								if (Files.exists(jarPackagePath)) {
									scanJarPath(jarPackagePath, scanPackage, componentClasses, visited);
								}
							}
						}
					}
				}
			}

			scanClassPathDirectories(scanPackage, componentClasses, visited);
		} catch (IOException e) {
			LOGGER.error("Failed to scan classpath for @Component classes", e);
		}

		return componentClasses;
	}

	private static void scanFileDirectory(File dir, String packageName, Set<Class<?>> result, Set<String> visited) {
		if (dir == null || !dir.exists()) return;
		File[] files = dir.listFiles();
		if (files == null) return;

		for (File file : files) {
			if (file.isDirectory()) {
				// Skip the whole directory subtree early if the package prefix is blacklisted.
				// This avoids traversing disabled/client/ directories entirely.
				String nextPackage = packageName + "." + file.getName();
				if (isBlacklistedPrefix(nextPackage + ".")) continue;
				scanFileDirectory(file, nextPackage, result, visited);
			} else if (file.getName().endsWith(".class")) {
				String className = packageName + "." + file.getName().replace(".class", "");
				loadAndCheckComponent(className, result, visited);
			}
		}
	}

	private static void scanJarPath(Path dir, String packageName, Set<Class<?>> result, Set<String> visited) throws IOException {
		String baseDirNormalized = dir.toString().replace('\\', '/');
		try (var stream = Files.walk(dir)) {
			stream.filter(path -> path.toString().endsWith(".class"))
				.forEach(path -> {
					String relativePath = path.toString().replace('\\', '/');
					// Strip the jar-internal absolute prefix so we are left with "a/b/C.class"
					// relative to the scan package root. Otherwise className starts with a stray dot.
					if (relativePath.startsWith(baseDirNormalized)) {
						int stripLen = baseDirNormalized.length();
						if (stripLen < relativePath.length() && relativePath.charAt(stripLen) == '/') stripLen++;
						relativePath = relativePath.substring(stripLen);
					} else if (relativePath.startsWith("/")) {
						relativePath = relativePath.substring(1);
					}
					String className = relativePath.replace('/', '.').replace(".class", "");
					// Prepend the scan package prefix unless the relative path already contains it.
					if (!className.startsWith(packageName)) {
						className = packageName + "." + className;
					}
					loadAndCheckComponent(className, result, visited);
				});
		}
	}

	private static void scanClassPathDirectories(String scanPackage, Set<Class<?>> result, Set<String> visited) {
		String classPath = System.getProperty("java.class.path");
		if (classPath == null) return;

		String[] entries = classPath.split(File.pathSeparator);
		String packagePath = scanPackage.replace('.', File.separatorChar);

		for (String entry : entries) {
			File entryFile = new File(entry);
			if (entryFile.isDirectory()) {
				File packageDir = new File(entryFile, packagePath);
				if (packageDir.exists()) {
					scanFileDirectory(packageDir, scanPackage, result, visited);
				}
			}
		}
	}

	private static boolean isBlacklistedPrefix(String className) {
		for (String prefix : BLACKLIST_PREFIXES) {
			if (className.startsWith(prefix)) return true;
		}
		return false;
	}

	private static boolean isBlacklistedSuffix(String className) {
		for (String suffix : BLACKLIST_SUFFIXES) {
			// className is "foo.bar.Renderer" → strip package → match on "Renderer"
			int lastDot = className.lastIndexOf('.');
			String shortName = lastDot >= 0 ? className.substring(lastDot + 1) : className;
			if (shortName.startsWith(suffix) || shortName.endsWith(suffix)) return true;
		}
		return false;
	}

	private static void loadAndCheckComponent(String className, Set<Class<?>> result, Set<String> visited) {
		if (visited.contains(className)) return;
		visited.add(className);

		// Quick-reject on package prefix first (before any class lookup happens).
		if (isBlacklistedPrefix(className)) return;

		// Quick-reject on class name suffix for classes that aren't in the client packages
		// but still have rendering / GUI-ey names.
		if (isBlacklistedSuffix(className)) return;

		// Synthetic / anonymous / lambdas are never beans and may carry weird class-loading edges.
		if (className.indexOf('$') >= 0) return;

		try {
			Class<?> clazz = Class.forName(className, false, Thread.currentThread().getContextClassLoader());
			if (clazz.isAnnotationPresent(Component.class)) {
				result.add(clazz);
			}
		} catch (Throwable e) {
			// Swallow any class-loading problem (ClassNotFoundException, NoClassDefFoundError,
			// UnsatisfiedLinkError, VerifyError, ExceptionInInitializerError, etc.)
			// so one bad class never aborts the scan. Always warn loudly so misconfigurations
			// can still be spotted in the log on a dedicated server.
			LOGGER.warn("Skipping non-loadable class during @Component scan: {} ({})", className, e.toString());
		}
	}
}