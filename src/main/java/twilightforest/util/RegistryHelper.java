package twilightforest.util;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import twilightforest.TwilightForestMod;

import java.util.function.Supplier;

/**
 * Fabric-compatible registry helper to replace NeoForge's DeferredRegister.
 * Uses immediate Registry.register() calls instead of deferred registration.
 */
public class RegistryHelper {

	public static Block registerBlock(String name, Supplier<Block> factory) {
		return Registry.register(
			BuiltInRegistries.BLOCK,
			TwilightForestMod.prefix(name),
			factory.get()
		);
	}

	public static Item registerItem(String name, Supplier<Item> factory) {
		return Registry.register(
			BuiltInRegistries.ITEM,
			TwilightForestMod.prefix(name),
			factory.get()
		);
	}

	public static <T> T register(Registry<T> registry, String name, Supplier<T> factory) {
		return Registry.register(
			registry,
			TwilightForestMod.prefix(name),
			factory.get()
		);
	}

	@SuppressWarnings("unchecked")
	public static <T> T register(ResourceKey<? extends Registry<T>> registryKey, String name, Supplier<T> factory) {
		Registry<T> registry = (Registry<T>) BuiltInRegistries.REGISTRY.get(registryKey.identifier()).map(Holder.Reference::value).orElse(null);
		if (registry == null) {
			throw new IllegalStateException("Registry not found: " + registryKey.identifier());
		}
		return Registry.register(registry, TwilightForestMod.prefix(name), factory.get());
	}
}