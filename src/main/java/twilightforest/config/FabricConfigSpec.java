package twilightforest.config;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class FabricConfigSpec {

	public static class BooleanValue {
		private boolean value;

		public BooleanValue(boolean defaultValue) {
			this.value = defaultValue;
		}

		public boolean get() {
			return value;
		}

		public void set(boolean value) {
			this.value = value;
		}
	}

	public static class IntValue {
		private int value;

		public IntValue(int defaultValue) {
			this.value = defaultValue;
		}

		public int get() {
			return value;
		}

		public void set(int value) {
			this.value = value;
		}
	}

	public static class DoubleValue {
		private double value;

		public DoubleValue(double defaultValue) {
			this.value = defaultValue;
		}

		public double get() {
			return value;
		}

		public void set(double value) {
			this.value = value;
		}
	}

	public static class ConfigValue<T> {
		private T value;

		public ConfigValue(T defaultValue) {
			this.value = defaultValue;
		}

		public T get() {
			return value;
		}

		public void set(T value) {
			this.value = value;
		}
	}

	public static class EnumValue<T extends Enum<T>> {
		private T value;

		public EnumValue(T defaultValue) {
			this.value = defaultValue;
		}

		public T get() {
			return value;
		}

		public void set(T value) {
			this.value = value;
		}
	}

	public static class Builder {

		public Builder comment(String comment) {
			return this;
		}

		public Builder translation(String translation) {
			return this;
		}

		public Builder push(String name) {
			return this;
		}

		public Builder pop() {
			return this;
		}

		public Builder worldRestart() {
			return this;
		}

		public BooleanValue define(String path, boolean defaultValue) {
			return new BooleanValue(defaultValue);
		}

		public ConfigValue<String> define(String path, String defaultValue) {
			return new ConfigValue<>(defaultValue);
		}

		public IntValue defineInRange(String path, int defaultValue, int min, int max) {
			return new IntValue(defaultValue);
		}

		public DoubleValue defineInRange(String path, double defaultValue, double min, double max) {
			return new DoubleValue(defaultValue);
		}

		public <T extends Enum<T>> EnumValue<T> defineEnum(String path, T defaultValue) {
			return new EnumValue<>(defaultValue);
		}

		public ConfigValue<List<? extends String>> defineListAllowEmpty(String path, List<? extends String> defaultValue, Supplier<String> defaultElementSupplier, Predicate<Object> validator) {
			return new ConfigValue<>(new ArrayList<>(defaultValue));
		}
	}
}