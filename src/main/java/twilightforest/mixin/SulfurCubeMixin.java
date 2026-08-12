package twilightforest.mixin;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.cubemob.SulfurCube;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import twilightforest.tags.TFItemTags;

@Mixin(SulfurCube.class)
public class SulfurCubeMixin {

	@Unique
	private static final int GIANT_SIZE = 8;
	@Unique
	private static final int NORMAL_SIZE = 2;

	@Inject(method = "equipItem", at = @At("RETURN"))
	private void tf$onEquipItem(ItemStack heldItem, CallbackInfoReturnable<Boolean> cir) {
		if (cir.getReturnValue()) {
			this.tf$syncSizeWithBodyItem();
		}
	}

	@Inject(method = "pickUpItem", at = @At("TAIL"))
	private void tf$onPickUpItem(ServerLevel level, ItemEntity entity, CallbackInfo ci) {
		this.tf$syncSizeWithBodyItem();
	}

	@Inject(method = "shear", at = @At("HEAD"))
	private void tf$onShear(ServerLevel level, SoundSource soundSource, ItemStack tool, CallbackInfo ci) {
		SulfurCube cube = (SulfurCube) (Object) this;
		cube.setSize(NORMAL_SIZE, true);
	}

	@Unique
	private void tf$syncSizeWithBodyItem() {
		SulfurCube cube = (SulfurCube) (Object) this;
		if (cube.hasBodyItem()) {
			if (cube.getBodyArmorItem().is(TFItemTags.SULFUR_CUBE_GIANT_BLOCKS)) {
				cube.setSize(GIANT_SIZE, true);
			} else {
				cube.setSize(NORMAL_SIZE, true);
			}
		}
	}
}
