package twilightforest.mixin;

import java.util.List;
import net.minecraft.world.entity.livingblock.LivingBlockType;
import net.minecraft.world.entity.livingblock.LivingBlockTypeRegistry;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(LivingBlockTypeRegistry.class)
public class MixinLivingBlockTypeRegistry {

    @Redirect(method = "get(Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/entity/livingblock/LivingBlockType;",
        at = @At(value = "INVOKE", target = "Ljava/util/List;get(I)Ljava/lang/Object;"))
    private Object twilightforest$safeListGet(List<?> list, int index) {
        if (index >= 0 && index < list.size()) {
            return list.get(index);
        }
        return null;
    }
}
