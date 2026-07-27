package twilightforest.item.recipe;

import com.mojang.serialization.MapCodec;

/**
 * Fabric-compatible replacement for NeoForge's ICondition.
 */
public interface ICondition {
    MapCodec<? extends ICondition> codec();

    boolean test(IContext context);

    interface IContext {
        // Marker interface for condition context
    }
}