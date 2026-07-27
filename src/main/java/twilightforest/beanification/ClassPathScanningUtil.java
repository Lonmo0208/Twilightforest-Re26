package twilightforest.beanification;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.*;
import java.util.*;

/**
 * Utility class for scanning the classpath to find classes annotated with @Component.
 * Scans within the "twilightforest" package hierarchy.
 */
public class ClassPathScanningUtil {

	private static final Logger LOGGER = LoggerFactory.getLogger(ClassPathScanningUtil.class);

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
				scanFileDirectory(file, packageName + "." + file.getName(), result, visited);
			} else if (file.getName().endsWith(".class")) {
				String className = packageName + "." + file.getName().replace(".class", "");
				loadAndCheckComponent(className, result, visited);
			}
		}
	}

	private static void scanJarPath(Path dir, String packageName, Set<Class<?>> result, Set<String> visited) throws IOException {
		try (var stream = Files.walk(dir)) {
			stream.filter(path -> path.toString().endsWith(".class"))
				.forEach(path -> {
					String relativePath = path.toString().replace('\\', '/');
					String className = relativePath.replace('/', '.').replace(".class", "");
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

	private static void loadAndCheckComponent(String className, Set<Class<?>> result, Set<String> visited) {
		if (visited.contains(className)) return;
		visited.add(className);
		if (className.contains(".mixin.")) return;

		try {
			Class<?> clazz = Class.forName(className, false, Thread.currentThread().getContextClassLoader());
			if (clazz.isAnnotationPresent(Component.class)) {
				result.add(clazz);
			}
		} catch (Throwable e) {
		}
	}
}