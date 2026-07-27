package twilightforest.util.datamaps;

import com.mojang.serialization.Codec;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Fabric-compatible replacement for NeoForge's DataMapType.
 * Stores data attached to registry entries, loaded from JSON data files.
 */
public class DataMapType<R, T> {

	private final Identifier id;
	private final ResourceKey<Registry<R>> registryKey;
	private final Codec<T> codec;
	private final Map<ResourceKey<R>, T> dataMap = new ConcurrentHashMap<>();

	private DataMapType(Identifier id, ResourceKey<Registry<R>> registryKey, Codec<T> codec) {
		this.id = id;
		this.registryKey = registryKey;
		this.codec = codec;
	}

	public Identifier id() {
		return id;
	}

	public ResourceKey<Registry<R>> registryKey() {
		return registryKey;
	}

	public Codec<T> codec() {
		return codec;
	}

	@Nullable
	public T get(ResourceKey<R> key) {
		return dataMap.get(key);
	}

	public void add(ResourceKey<R> key, T value) {
		dataMap.put(key, value);
	}

	public void clear() {
		dataMap.clear();
	}

	@Nullable
	public static <R, T> T getData(Holder<R> holder, DataMapType<R, T> dataMapType) {
		return holder.unwrapKey().map(dataMapType::get).orElse(null);
	}

	public static <R, T> Builder<R, T> builder(Identifier id, ResourceKey<Registry<R>> registryKey, Codec<T> codec) {
		return new Builder<>(id, registryKey, codec);
	}

	public static class Builder<R, T> {
		private final Identifier id;
		private final ResourceKey<Registry<R>> registryKey;
		private final Codec<T> codec;

		private Builder(Identifier id, ResourceKey<Registry<R>> registryKey, Codec<T> codec) {
			this.id = id;
			this.registryKey = registryKey;
			this.codec = codec;
		}

		public Builder<R, T> synced(Codec<T> codec, boolean network) {
			return this;
		}

		public DataMapType<R, T> build() {
			return new DataMapType<>(id, registryKey, codec);
		}
	}
}