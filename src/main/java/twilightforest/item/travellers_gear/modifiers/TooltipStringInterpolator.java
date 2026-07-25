package twilightforest.item.travellers_gear.modifiers;


import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

import java.util.Map;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public abstract class TooltipStringInterpolator {
	private static final Pattern VAR_PATTERN = Pattern.compile("\\$\\{([^}]+)}");

	public static MutableComponent render(String translatableKey) {
		String text = Component.translatable(translatableKey).getString();
		Matcher m = VAR_PATTERN.matcher(text);
		MutableComponent result = Component.empty();
		int lastEnd = 0;
		while (m.find()) {
			if (m.start() > lastEnd) {
				result.append(text.substring(lastEnd, m.start()));
			}
			String var = m.group(1);
			result.append(resolveVariable(var));
			lastEnd = m.end();
		}
		if (lastEnd < text.length()) {
			result.append(text.substring(lastEnd));
		}
		return result;
	}

	private static MutableComponent resolveVariable(String var) {
		int slash = var.indexOf('/');
		if (slash < 0)
			throw new IllegalArgumentException("Missing / in variable: " + var);
		String ns = var.substring(0, slash);
		String arg = var.substring(slash + 1);
		Map<String, Function<String, MutableComponent>> twoPart = Map.of(
			"tfkeybinds", TooltipStringInterpolator::resolveTFKeybind);
		Function<String, MutableComponent> resolver = twoPart.get(ns);
		if (resolver == null) {
			throw new IllegalArgumentException("Unknown namespace: " + ns);
		}
		return resolver.apply(arg);
	}

	private static MutableComponent resolveTFKeybind(String keyString) {
		return Component.keybind(keyString);
	}
}
