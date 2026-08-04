package twilightforest.client.renderer.armor;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import twilightforest.TwilightForestMod;
import twilightforest.client.model.TFModelLayers;
import twilightforest.client.model.armor.TFArmorModel;
import twilightforest.client.model.armor.TravellersWingsModel;
import twilightforest.init.TFDataComponents;
import twilightforest.init.TFKeyBinds;
import twilightforest.init.custom.TravellersModifiersManager;

public class TravellersArmorRenderer extends TFArmorRenderer {

	@Override
	protected HumanoidModel<HumanoidRenderState> createModel(HumanoidRenderState state, ItemStack stack, EquipmentSlot slot, HumanoidModel<HumanoidRenderState> contextModel) {
		ModelPart root = switch (slot) {
			case HEAD -> getModelPart(TFModelLayers.TRAVELLERS_ARMOR_HELMET);
			case CHEST -> {
				ModelPart chestLayer = getModelPart(TFModelLayers.TRAVELLERS_ARMOR_CHEST_GLOVES);
				chestLayer.getAllParts().forEach(part -> part.skipDraw = true);
				boolean hasChestplate = stack.has(TFDataComponents.TRAVELLERS_HAS_CHESTPLATE);
				boolean hasGloves = stack.has(TFDataComponents.TRAVELLERS_HAS_GLOVES);
				chestLayer.getChild("body").skipDraw = !hasChestplate;
				chestLayer.getChild("left_arm").skipDraw = !hasGloves;
				chestLayer.getChild("right_arm").skipDraw = !hasGloves;
				yield chestLayer;
			}
			case LEGS -> {
				ModelPart leggingsLayer = getModelPart(TFModelLayers.TRAVELLERS_ARMOR_LEGGINGS);
				leggingsLayer.getAllParts().forEach(part -> part.skipDraw = true);
				boolean hasWings = stack.has(TFDataComponents.TRAVELLERS_HAS_WINGS);
				boolean hasBelt = stack.has(TFDataComponents.TRAVELLERS_HAS_BELT);
				TravellersWingsModel.skipBelt(leggingsLayer, !hasBelt);
				TravellersWingsModel.skipWings(leggingsLayer, !hasWings);
				yield leggingsLayer;
			}
			case FEET -> getModelPart(TFModelLayers.TRAVELLERS_ARMOR_BOOTS);
			default -> throw new IllegalArgumentException("Unexpected armor slot: " + slot + ": " + stack);
		};

		return slot == EquipmentSlot.LEGS ? new TravellersWingsModel(root) : new TFArmorModel(root);
	}

	@Override
	protected void setupModelAnimations(HumanoidRenderState state, ItemStack stack, EquipmentSlot slot, HumanoidModel<HumanoidRenderState> model) {
		if (model instanceof TravellersWingsModel wingsModel) {
			float partialTick = Minecraft.getInstance().getDeltaTracker().getGameTimeDeltaPartialTick(false);
			wingsModel.setupModelAnimations(state, partialTick);
		}
	}

	@Override
	protected void renderModel(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int light, ItemStack stack, EquipmentSlot slot, HumanoidModel<HumanoidRenderState> model, HumanoidRenderState state) {
		Identifier texture;
		if (slot != EquipmentSlot.LEGS && isUsingZoom()) {
			texture = TwilightForestMod.prefix("textures/models/armor/travellers_layer_1_down.png");
		} else {
			texture = armorTexture("travellers", slot, false);
		}
		renderTinted(poseStack, submitNodeCollector, light, stack, model, state, texture, 0xFFFFFFFF);
	}

	private static boolean isUsingZoom() {
		var player = Minecraft.getInstance().player;
		if (player == null) return false;
		if (player.isScoping()) return false;
		if (!TFKeyBinds.ZOOM_KEY.isDown()) return false;
		var headStack = player.getItemBySlot(EquipmentSlot.HEAD);
		if (headStack.isEmpty()) return false;
		return TravellersModifiersManager.isModifierActive(player, headStack, TravellersModifiersManager.ZOOM_ABILITY);
	}
}