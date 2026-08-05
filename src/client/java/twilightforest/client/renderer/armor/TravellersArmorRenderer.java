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

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TravellersArmorRenderer extends TFArmorRenderer {

	@Override
	protected HumanoidModel<HumanoidRenderState> createModel(HumanoidRenderState state, ItemStack stack, EquipmentSlot slot, HumanoidModel<HumanoidRenderState> contextModel) {
		ModelPart sharedRoot = switch (slot) {
			case HEAD -> getModelPart(TFModelLayers.TRAVELLERS_ARMOR_HELMET);
			case CHEST -> getModelPart(TFModelLayers.TRAVELLERS_ARMOR_CHEST_GLOVES);
			case LEGS -> getModelPart(TFModelLayers.TRAVELLERS_ARMOR_LEGGINGS);
			case FEET -> getModelPart(TFModelLayers.TRAVELLERS_ARMOR_BOOTS);
			default -> throw new IllegalArgumentException("Unexpected armor slot: " + slot + ": " + stack);
		};

		// Deep copy the entire ModelPart tree so we can safely modify visible/skipDraw
		// without affecting the shared cached model (critical: submitModel is deferred!)
		ModelPart copiedRoot = deepCopyModelPart(sharedRoot);

		// Hide everything first (visible=false completely skips the part AND all its children)
		copiedRoot.getAllParts().forEach(p -> p.visible = false);

		// Show only the parts relevant to this equipment slot
		switch (slot) {
			case HEAD -> {
				ModelPart head = copiedRoot.getChild("head");
				head.getAllParts().forEach(p -> p.visible = true);
				head.getChild("hat").getAllParts().forEach(p -> p.visible = true);
			}
			case CHEST -> {
				boolean hasChestplate = stack.has(TFDataComponents.TRAVELLERS_HAS_CHESTPLATE);
				boolean hasGloves = stack.has(TFDataComponents.TRAVELLERS_HAS_GLOVES);
				if (hasChestplate) {
					copiedRoot.getChild("body").getAllParts().forEach(p -> p.visible = true);
				}
				if (hasGloves) {
					copiedRoot.getChild("left_arm").getAllParts().forEach(p -> p.visible = true);
					copiedRoot.getChild("right_arm").getAllParts().forEach(p -> p.visible = true);
				}
			}
			case LEGS -> {
				boolean hasWings = stack.has(TFDataComponents.TRAVELLERS_HAS_WINGS);
				boolean hasBelt = stack.has(TFDataComponents.TRAVELLERS_HAS_BELT);
				// Always show the leg parts (they're the leggings base)
				copiedRoot.getChild("right_leg").getAllParts().forEach(p -> p.visible = true);
				copiedRoot.getChild("left_leg").getAllParts().forEach(p -> p.visible = true);
				// Belt and wings are children of body; handle via skipDraw on the model
				if (hasWings || hasBelt) {
					// Show body for belt/wings
					copiedRoot.getChild("body").visible = true;
				}
				// Use skipDraw to selectively hide belt/wings within the body
				ModelPart body = copiedRoot.getChild("body");
				if (hasBelt) {
					TravellersWingsModel.skipBelt(body, false);
				} else {
					TravellersWingsModel.skipBelt(body, true);
				}
				if (hasWings) {
					TravellersWingsModel.skipWings(body, false);
				} else {
					TravellersWingsModel.skipWings(body, true);
				}
			}
			case FEET -> {
				copiedRoot.getChild("right_leg").getAllParts().forEach(p -> p.visible = true);
				copiedRoot.getChild("left_leg").getAllParts().forEach(p -> p.visible = true);
			}
			default -> { }
		}

		copiedRoot.visible = true;

		return slot == EquipmentSlot.LEGS ? new TravellersWingsModel(copiedRoot) : new TFArmorModel(copiedRoot);
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

	private static ModelPart deepCopyModelPart(ModelPart source) {
		try {
			Field cubesField = ModelPart.class.getDeclaredField("cubes");
			Field childrenField = ModelPart.class.getDeclaredField("children");
			cubesField.setAccessible(true);
			childrenField.setAccessible(true);

			@SuppressWarnings("unchecked")
			List<ModelPart.Cube> cubes = (List<ModelPart.Cube>) cubesField.get(source);
			@SuppressWarnings("unchecked")
			Map<String, ModelPart> children = (Map<String, ModelPart>) childrenField.get(source);

			List<ModelPart.Cube> newCubes = new ArrayList<>(cubes);
			Map<String, ModelPart> newChildren = new HashMap<>();
			for (Map.Entry<String, ModelPart> entry : children.entrySet()) {
				newChildren.put(entry.getKey(), deepCopyModelPart(entry.getValue()));
			}

			Constructor<ModelPart> constructor = ModelPart.class.getDeclaredConstructor(List.class, Map.class);
			constructor.setAccessible(true);
			ModelPart copy = constructor.newInstance(newCubes, newChildren);

			copy.x = source.x;
			copy.y = source.y;
			copy.z = source.z;
			copy.xRot = source.xRot;
			copy.yRot = source.yRot;
			copy.zRot = source.zRot;
			copy.xScale = source.xScale;
			copy.yScale = source.yScale;
			copy.zScale = source.zScale;
			copy.visible = source.visible;
			copy.skipDraw = source.skipDraw;
			copy.setInitialPose(source.getInitialPose());

			return copy;
		} catch (Exception e) {
			throw new RuntimeException("Failed to deep copy ModelPart", e);
		}
	}
}
