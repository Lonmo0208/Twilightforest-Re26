package twilightforest.neoforge.reg;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;

import java.util.Map;

/**
 * Utility to look up registries by their ResourceKey.
 * In vanilla/Fabric, BuiltInRegistries doesn't have a REGISTRY field like NeoForge.
 */
public class RegistryUtil {

    @SuppressWarnings("unchecked")
    public static <T> Registry<T> getRegistry(ResourceKey<? extends Registry<T>> key) {
        var keyId = key.identifier();
        if (keyId.equals(Registries.BLOCK.identifier())) return (Registry<T>) BuiltInRegistries.BLOCK;
        if (keyId.equals(Registries.ITEM.identifier())) return (Registry<T>) BuiltInRegistries.ITEM;
        if (keyId.equals(Registries.ENTITY_TYPE.identifier())) return (Registry<T>) BuiltInRegistries.ENTITY_TYPE;
        if (keyId.equals(Registries.MOB_EFFECT.identifier())) return (Registry<T>) BuiltInRegistries.MOB_EFFECT;
        if (keyId.equals(Registries.SOUND_EVENT.identifier())) return (Registry<T>) BuiltInRegistries.SOUND_EVENT;
        if (keyId.equals(Registries.PARTICLE_TYPE.identifier())) return (Registry<T>) BuiltInRegistries.PARTICLE_TYPE;
        if (keyId.equals(Registries.BLOCK_ENTITY_TYPE.identifier())) return (Registry<T>) BuiltInRegistries.BLOCK_ENTITY_TYPE;
        if (keyId.equals(Registries.CUSTOM_STAT.identifier())) return (Registry<T>) BuiltInRegistries.CUSTOM_STAT;
        if (keyId.equals(Registries.RECIPE_TYPE.identifier())) return (Registry<T>) BuiltInRegistries.RECIPE_TYPE;
        if (keyId.equals(Registries.RECIPE_SERIALIZER.identifier())) return (Registry<T>) BuiltInRegistries.RECIPE_SERIALIZER;
        if (keyId.equals(Registries.ATTRIBUTE.identifier())) return (Registry<T>) BuiltInRegistries.ATTRIBUTE;
        if (keyId.equals(Registries.STAT_TYPE.identifier())) return (Registry<T>) BuiltInRegistries.STAT_TYPE;
        if (keyId.equals(Registries.POTION.identifier())) return (Registry<T>) BuiltInRegistries.POTION;
        if (keyId.equals(Registries.FLUID.identifier())) return (Registry<T>) BuiltInRegistries.FLUID;
        if (keyId.equals(Registries.GAME_EVENT.identifier())) return (Registry<T>) BuiltInRegistries.GAME_EVENT;
        if (keyId.equals(Registries.STRUCTURE_PIECE.identifier())) return (Registry<T>) BuiltInRegistries.STRUCTURE_PIECE;
        return null;
    }
}
