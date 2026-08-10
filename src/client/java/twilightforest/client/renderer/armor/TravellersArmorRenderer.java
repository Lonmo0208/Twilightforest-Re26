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

		// Deep copy the entire ModelPart tree so we can safely modify skipDraw
		// without affecting the shared cached model (critical: submitModel is deferred!)
		ModelPart copiedRoot = deepCopyModelPart(sharedRoot);

		// Match the NeoForge reference 1:1 — control visibility through skipDraw
		// only, never touch `visible`. Using `visible = false` on the parent would
		// prevent Minecraft's ModelPart.visit() from ever descending into children,
		// which means belt buckle / wing cubes (grandchildren of copiedRoot under
		// body → buckle/wingBaseLeft etc.) would be silently skipped no matter
		// what skipDraw says on the individual child nodes.
		switch (slot) {
			case HEAD -> {
				// Helmet only exposes head + hat; every other child is hidden via
				// skipDraw. Head is always drawn here.
				copiedRoot.getAllParts().forEach(p -> p.skipDraw = true);
				setSkipDrawTree(copiedRoot, false, "head");
				setSkipDrawTree(copiedRoot, false, "head", "hat");
			}
			case CHEST -> {
				copiedRoot.getAllParts().forEach(p -> p.skipDraw = true);
				boolean hasChestplate = stack.has(TFDataComponents.TRAVELLERS_HAS_CHESTPLATE);
				boolean hasGloves = stack.has(TFDataComponents.TRAVELLERS_HAS_GLOVES);
				copiedRoot.getChild("body").skipDraw = !hasChestplate;
				copiedRoot.getChild("left_arm").skipDraw = !hasGloves;
				copiedRoot.getChild("right_arm").skipDraw = !hasGloves;
			}
			case LEGS -> {
				copiedRoot.getAllParts().forEach(p -> p.skipDraw = true);
				boolean hasWings = stack.has(TFDataComponents.TRAVELLERS_HAS_WINGS);
				boolean hasBelt = stack.has(TFDataComponents.TRAVELLERS_HAS_BELT)
					|| stack.has(TFDataComponents.SWAP_HOTBAR_MODIFIER);

				// Legs are the base leggings cylinders, always shown.
				copiedRoot.getChild("right_leg").skipDraw = false;
				copiedRoot.getChild("left_leg").skipDraw = false;

				// Belt and wings live inside copiedRoot.body. Skip helpers expect
				// the leggings ROOT as the first argument and internally resolve
				// body → buckle/wingBaseLeft themselves (so we mirror NeoForge's
				// exact layout).
				TravellersWingsModel.skipBelt(copiedRoot, !hasBelt);
				TravellersWingsModel.skipWings(copiedRoot, !hasWings);
			}
			case FEET -> {
				copiedRoot.getAllParts().forEach(p -> p.skipDraw = true);
				copiedRoot.getChild("right_leg").skipDraw = false;
				copiedRoot.getChild("left_leg").skipDraw = false;
			}
			default -> { }
		}

		// `visible` on the root must stay true — otherwise nothing is traversed.
		copiedRoot.visible = true;

		return slot == EquipmentSlot.LEGS ? new TravellersWingsModel(copiedRoot) : new TFArmorModel(copiedRoot);
	}

	/**
	 * Shorthand helper: set skipDraw on every part of a chain of named children
	 * (e.g. {@code setSkipDrawTree(root, false, "head", "hat")} makes head visible
	 * and also every part under head.hat). If any name in the chain doesn't exist
	 * the call is silently a no-op so GUI / PIP render states with incomplete
	 * ModelPart trees don't throw.
	 */
	private static void setSkipDrawTree(ModelPart root, boolean skip, String firstChild, String... restChildren) {
		ModelPart cur = root;
		if (!cur.hasChild(firstChild)) return;
		cur = cur.getChild(firstChild);
		cur.getAllParts().forEach(p -> p.skipDraw = skip);
		for (String name : restChildren) {
			if (!cur.hasChild(name)) return;
			cur = cur.getChild(name);
			cur.getAllParts().forEach(p -> p.skipDraw = skip);
		}
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
