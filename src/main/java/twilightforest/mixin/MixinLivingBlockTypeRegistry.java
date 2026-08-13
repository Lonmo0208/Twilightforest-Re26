package twilightforest.mixin;

import java.util.List;
import net.minecraft.world.entity.livingblock.LivingBlockTypeRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * Guards against {@code IndexOutOfBoundsException} in {@code LivingBlockTypeRegistry#get}.
 * <p>
 * Vanilla builds its internal item→type lookup list sized to the vanilla item registry at
 * class-init time. Mod items registered afterwards (e.g. Twilight Forest containers) can have
 * an {@code Item.getId} that exceeds the list size, so {@code itemLookup.get(id)} would throw.
 * This redirect returns {@code null} for out-of-range indices, letting vanilla fall through to
 * its default-type logic. Mirrors NeoForge's 26w14a {@code MixinLivingBlockTypeRegistry}.
 */
@Mixin(LivingBlockTypeRegistry.class)
public class MixinLivingBlockTypeRegistry {

	@Redirect(method = "get(Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/entity/livingblock/LivingBlockType;", at = @At(value = "INVOKE", target = "Ljava/util/List;get(I)Ljava/lang/Object;"))
	private Object tf$safeListGet(List<?> list, int index) {
		if (index >= 0 && index < list.size()) {
			return list.get(index);
		}
		return null;
	}
}