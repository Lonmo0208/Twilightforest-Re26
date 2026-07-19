package twilightforest.mixin;

import net.minecraft.client.renderer.block.BlockModelResolver;
import net.minecraft.client.renderer.entity.SulfurCubeRenderer;
import net.minecraft.client.renderer.entity.state.SulfurCubeRenderState;
import net.minecraft.world.entity.monster.cubemob.SulfurCube;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import twilightforest.loot.modifiers.GiantToolGroupingModifier;
import twilightforest.tags.TFItemTags;

import java.util.HashMap;
import java.util.Map;

@Mixin(SulfurCubeRenderer.class)
public class SulfurCubeRendererMixin {

	@Final
	@Shadow
	private BlockModelResolver blockModelResolver;

	@Unique
	private static final Map<Item, Block> tf$giantToNormalBlock = new HashMap<>();

	static {
		for (Map.Entry<Block, Item> entry : GiantToolGroupingModifier.CONVERSIONS.entrySet()) {
			tf$giantToNormalBlock.put(entry.getValue(), entry.getKey());
		}
	}

	@Inject(method = "extractRenderState*", at = @At("TAIL"))
	private void tf$onExtractRenderState(SulfurCube entity, SulfurCubeRenderState state, float partialTicks, CallbackInfo ci) {
		ItemStack bodyItem = entity.getBodyArmorItem();
		if (!bodyItem.isEmpty() && bodyItem.is(TFItemTags.SULFUR_CUBE_GIANT_BLOCKS) && bodyItem.getItem() instanceof BlockItem) {
			Block normalBlock = tf$giantToNormalBlock.get(bodyItem.getItem());
			if (normalBlock != null) {
				BlockState normalState = normalBlock.defaultBlockState();
				this.blockModelResolver.update(state.containedBlock, normalState, SulfurCubeRenderer.BLOCK_DISPLAY_CONTEXT);
			}
		}
	}
}
