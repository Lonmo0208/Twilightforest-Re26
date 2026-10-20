package twilightforest.compat.c2me;

// Adapted from tf-c2me-compat (MIT); see THIRD_PARTY_NOTICES.md.

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * C2ME's OpenCL module hard-crashes world creation when the world's density
 * functions contain types it cannot compile (e.g. TF's TerrainDensityRouter)
 * unless the {@code openclAccel.allowIncompatibilityFallback} config flag is
 * enabled. Because users often run multiple game instances with separate
 * config files, we force the flag programmatically (in-memory via Unsafe for
 * this session, and in the config file for future sessions) instead of asking
 * users to edit c2me.toml in every instance.
 */
public final class OpenclFallbackEnabler {

	private static final Logger LOGGER = LoggerFactory.getLogger(OpenclFallbackEnabler.class);

	private OpenclFallbackEnabler() {
	}

	public static void enable(Path configDir) {
		forceInMemoryFlag();
		patchConfigFile(configDir);
	}

	/** Flip C2ME's static final config field for the current session. */
	private static void forceInMemoryFlag() {
		try {
			Class<?> config = Class.forName("com.ishland.c2me.opts.accel.opencl.common.Config");
			Field field = config.getDeclaredField("allowIncompatibilityFallback");
			sun.misc.Unsafe unsafe = unsafe();
			long offset = unsafe.staticFieldOffset(field);
			unsafe.putBoolean(config, offset, true);
			LOGGER.info("[TF-C2ME-Compat] Forced openclAccel.allowIncompatibilityFallback = true for this session");
		} catch (ClassNotFoundException e) {
			// OpenCL module not installed — nothing to do
		} catch (Throwable t) {
			LOGGER.warn("[TF-C2ME-Compat] Could not force OpenCL fallback flag; if the OpenCL module is installed and worldgen codegen fails, the game may crash. Install/verify config manually.", t);
		}
	}

	private static sun.misc.Unsafe unsafe() throws Exception {
		Field f = sun.misc.Unsafe.class.getDeclaredField("theUnsafe");
		f.setAccessible(true);
		return (sun.misc.Unsafe) f.get(null);
	}

	/** Persist the flag (and GPU device permission) into c2me.toml for future launches. */
	private static void patchConfigFile(Path configDir) {
		try {
			Path config = configDir.resolve("c2me.toml");
			if (!Files.exists(config)) {
				return;
			}
			String content = Files.readString(config);
			String patched = content;
			patched = setKey(patched, "allowIncompatibilityFallback", "true");
			patched = setKey(patched, "allowGPUDevices", "true");
			if (!patched.equals(content)) {
				Files.writeString(config, patched);
				LOGGER.info("[TF-C2ME-Compat] Patched c2me.toml OpenCL fallback flags for future launches");
			}
		} catch (Throwable t) {
			LOGGER.warn("[TF-C2ME-Compat] Failed to patch c2me.toml", t);
		}
	}

	/** The [openclAccel] section header, matched as a whole line so comments or
	 * values mentioning the name cannot be mistaken for the real section. */
	private static final Pattern OPENCL_SECTION = Pattern.compile("(?m)^\\[openclAccel\\][ \\t]*(?:\\r?\\n|$)");
	private static final Pattern ANY_SECTION = Pattern.compile("(?m)^\\[");

	/**
	 * Set {@code key = true} inside the [openclAccel] section. Handles the
	 * quoted-expression values C2ME uses ("default", "false", "true") as well
	 * as bare booleans. Appends the key to the section if absent; creates the
	 * section at EOF if that is missing too.
	 */
	private static String setKey(String content, String key, String value) {
		Matcher header = OPENCL_SECTION.matcher(content);
		int headerStart = -1;
		int headerEnd = -1;
		while (header.find()) {
			headerStart = header.start();
			headerEnd = header.end();
		}
		if (headerStart < 0) {
			return content + "\n[openclAccel]\n\t" + key + " = " + value + "\n";
		}
		// Bound the section by the next TOML header (or EOF) so a key of the
		// same name in another section is never rewritten.
		Matcher nextSection = ANY_SECTION.matcher(content);
		int sectionEnd = content.length();
		if (nextSection.find(headerEnd)) {
			sectionEnd = nextSection.start();
		}
		Matcher keyLine = Pattern.compile("(?m)^([ \\t]*)(" + Pattern.quote(key) + ")[ \\t]*=.*$")
				.matcher(content);
		if (keyLine.find(headerStart) && keyLine.end() <= sectionEnd) {
			return content.substring(0, keyLine.start()) + keyLine.group(1) + key + " = " + value
					+ content.substring(keyLine.end());
		}
		// Key absent from the section: insert at the end of the section.
		String prefix = content.substring(0, sectionEnd);
		if (!prefix.endsWith("\n")) {
			prefix += "\n";
		}
		return prefix + "\t" + key + " = " + value + "\n" + content.substring(sectionEnd);
	}
}
