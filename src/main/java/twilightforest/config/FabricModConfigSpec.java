package twilightforest.config;

import com.google.gson.*;
import net.minecraft.resources.Identifier;
import twilightforest.TwilightForestMod;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Fabric-compatible replacement for Neoforge's FabricModConfigSpec.
 * Uses Gson for JSON config file persistence.
 */
public class FabricModConfigSpec {

	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

	private final String configName;
	private final Map<String, ConfigValue<?>> values = new LinkedHashMap<>();
	private Path configPath;

	public FabricModConfigSpec(String configName) {
		this.configName = configName;
	}

	void registerValue(String path, ConfigValue<?> value) {
		values.put(path, value);
	}

	public String getConfigName() {
		return configName;
	}

	public void load(Path configDir) {
		this.configPath = configDir.resolve(configName + ".json");
		if (Files.exists(configPath)) {
			try (Reader reader = Files.newBufferedReader(configPath)) {
				JsonObject json = GSON.fromJson(reader, JsonObject.class);
				if (json != null) {
					for (Map.Entry<String, JsonElement> entry : json.entrySet()) {
						ConfigValue<?> cv = values.get(entry.getKey());
						if (cv != null) {
							cv.fromJson(entry.getValue());
						}
					}
				}
			} catch (IOException e) {
				TwilightForestMod.LOGGER.error("Failed to load config: {}", configName, e);
			}
		}
		save(); // Save defaults if file doesn't exist
	}

	public void save() {
		if (configPath == null) return;
		try {
			Files.createDirectories(configPath.getParent());
			JsonObject json = new JsonObject();
			for (Map.Entry<String, ConfigValue<?>> entry : values.entrySet()) {
				json.add(entry.getKey(), entry.getValue().toJson());
			}
			try (Writer writer = Files.newBufferedWriter(configPath)) {
				GSON.toJson(json, writer);
			}
		} catch (IOException e) {
			TwilightForestMod.LOGGER.error("Failed to save config: {}", configName, e);
		}
	}

	public boolean isLoaded() {
		return configPath != null;
	}

	// --- Builder ---
	public static class Builder {
		private final FabricModConfigSpec spec;
		private final Stack<String> pathStack = new Stack<>();
		private String currentComment = null;
		private String currentTranslation = null;
		private boolean worldRestart = false;

		public Builder(FabricModConfigSpec spec) {
			this.spec = spec;
		}

		public Builder comment(String comment) {
			this.currentComment = comment;
			return this;
		}

		public Builder translation(String key) {
			this.currentTranslation = key;
			return this;
		}

		public Builder worldRestart() {
			this.worldRestart = true;
			return this;
		}

		public Builder push(String name) {
			pathStack.push(name);
			return this;
		}

		public Builder pop() {
			if (!pathStack.isEmpty()) pathStack.pop();
			return this;
		}

		private String fullPath(String name) {
			if (pathStack.isEmpty()) return name;
			return String.join(".", pathStack) + "." + name;
		}

		public BooleanValue define(String name, boolean defaultValue) {
			String path = fullPath(name);
			BooleanValue value = new BooleanValue(path, defaultValue);
			spec.registerValue(path, value);
			reset();
			return value;
		}

		public IntValue defineInRange(String name, int defaultValue, int min, int max) {
			String path = fullPath(name);
			IntValue value = new IntValue(path, defaultValue);
			spec.registerValue(path, value);
			reset();
			return value;
		}

		public DoubleValue defineInRange(String name, double defaultValue, double min, double max) {
			String path = fullPath(name);
			DoubleValue value = new DoubleValue(path, defaultValue);
			spec.registerValue(path, value);
			reset();
			return value;
		}

		public <T extends Enum<T>> EnumValue<T> defineEnum(String name, T defaultValue) {
			String path = fullPath(name);
			EnumValue<T> value = new EnumValue<>(path, defaultValue);
			spec.registerValue(path, value);
			reset();
			return value;
		}

		public StringValue define(String name, String defaultValue) {
			String path = fullPath(name);
			StringValue value = new StringValue(path, defaultValue);
			spec.registerValue(path, value);
			reset();
			return value;
		}

		public <T> ListValue<T> defineListAllowEmpty(String name, List<T> defaultValue, Supplier<T> defaultElement, Predicate<Object> validator) {
			String path = fullPath(name);
			@SuppressWarnings("unchecked")
			ListValue<T> value = new ListValue<>(path, (List<T>) new ArrayList<>(defaultValue));
			spec.registerValue(path, value);
			reset();
			return value;
		}

		private void reset() {
			this.currentComment = null;
			this.currentTranslation = null;
			this.worldRestart = false;
		}
	}

	// --- Config value types ---
	public abstract static class ConfigValue<T> {
		protected final String path;
		protected T value;
		protected final T defaultValue;

		public ConfigValue(String path, T defaultValue) {
			this.path = path;
			this.defaultValue = defaultValue;
			this.value = defaultValue;
		}

		public T get() {
			return value;
		}

		public void set(T value) {
			this.value = value;
		}

		public String getPath() {
			return path;
		}

		abstract void fromJson(JsonElement json);
		abstract JsonElement toJson();
	}

	public static class BooleanValue extends ConfigValue<Boolean> {
		public BooleanValue(String path, boolean defaultValue) {
			super(path, defaultValue);
		}

		@Override
		void fromJson(JsonElement json) {
			value = json.getAsBoolean();
		}

		@Override
		JsonElement toJson() {
			return new JsonPrimitive(value);
		}
	}

	public static class IntValue extends ConfigValue<Integer> {
		public IntValue(String path, int defaultValue) {
			super(path, defaultValue);
		}

		@Override
		void fromJson(JsonElement json) {
			value = json.getAsInt();
		}

		@Override
		JsonElement toJson() {
			return new JsonPrimitive(value);
		}
	}

	public static class DoubleValue extends ConfigValue<Double> {
		public DoubleValue(String path, double defaultValue) {
			super(path, defaultValue);
		}

		@Override
		void fromJson(JsonElement json) {
			value = json.getAsDouble();
		}

		@Override
		JsonElement toJson() {
			return new JsonPrimitive(value);
		}
	}

	public static class StringValue extends ConfigValue<String> {
		public StringValue(String path, String defaultValue) {
			super(path, defaultValue);
		}

		@Override
		void fromJson(JsonElement json) {
			value = json.getAsString();
		}

		@Override
		JsonElement toJson() {
			return new JsonPrimitive(value);
		}
	}

	public static class EnumValue<T extends Enum<T>> extends ConfigValue<T> {
		private final Class<T> enumClass;

		@SuppressWarnings("unchecked")
		public EnumValue(String path, T defaultValue) {
			super(path, defaultValue);
			this.enumClass = (Class<T>) defaultValue.getClass();
		}

		@Override
		void fromJson(JsonElement json) {
			try {
				value = Enum.valueOf(enumClass, json.getAsString());
			} catch (IllegalArgumentException e) {
				value = defaultValue;
			}
		}

		@Override
		JsonElement toJson() {
			return new JsonPrimitive(value.name());
		}
	}

	public static class ListValue<T> extends ConfigValue<List<T>> {
		public ListValue(String path, List<T> defaultValue) {
			super(path, new ArrayList<>(defaultValue));
		}

		@Override
		@SuppressWarnings("unchecked")
		void fromJson(JsonElement json) {
			value.clear();
			if (json.isJsonArray()) {
				for (JsonElement e : json.getAsJsonArray()) {
					value.add((T) e.getAsString());
				}
			}
		}

		@Override
		JsonElement toJson() {
			JsonArray arr = new JsonArray();
			for (T item : value) {
				arr.add(String.valueOf(item));
			}
			return arr;
		}
	}
}