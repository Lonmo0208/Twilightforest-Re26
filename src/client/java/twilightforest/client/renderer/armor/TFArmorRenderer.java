package twilightforest.client.renderer.armor;

import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.OrderedSubmitNodeCollector;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import twilightforest.TwilightForestMod;
import twilightforest.init.TFItems;

import java.util.HashMap;
import java.util.Map;

public abstract class TFArmorRenderer implements ArmorRenderer {
	private static final Map<ModelLayerLocation, ModelPart> ARMOR_MODELS = new HashMap<>();

	protected static ModelPart getModelPart(ModelLayerLocation layerLocation) {
		return ARMOR_MODELS.computeIfAbsent(layerLocation, layer -> Minecraft.getInstance().getEntityModels().bakeLayer(layer));
	}

	public static void resetModelCache() {
		ARMOR_MODELS.clear();
	}

	public static void bootstrap() {
		ArmorRenderer.register(new ArcticArmorRenderer(),
				(ItemLike) TFItems.ARCTIC_HELMET, (ItemLike) TFItems.ARCTIC_CHESTPLATE, (ItemLike) TFItems.ARCTIC_LEGGINGS, (ItemLike) TFItems.ARCTIC_BOOTS);
		ArmorRenderer.register(new TFSimpleArmorRenderer(twilightforest.client.model.armor.FieryArmorModel::new, twilightforest.client.model.TFModelLayers.FIERY_ARMOR_INNER, twilightforest.client.model.TFModelLayers.FIERY_ARMOR_OUTER, "fiery"),
				(ItemLike) TFItems.FIERY_HELMET, (ItemLike) TFItems.FIERY_CHESTPLATE, (ItemLike) TFItems.FIERY_LEGGINGS, (ItemLike) TFItems.FIERY_BOOTS);
		ArmorRenderer.register(new TFSimpleArmorRenderer(twilightforest.client.model.armor.TFArmorModel::new, twilightforest.client.model.TFModelLayers.KNIGHTMETAL_ARMOR_INNER, twilightforest.client.model.TFModelLayers.KNIGHTMETAL_ARMOR_OUTER, "knightmetal"),
				(ItemLike) TFItems.KNIGHTMETAL_HELMET, (ItemLike) TFItems.KNIGHTMETAL_CHESTPLATE, (ItemLike) TFItems.KNIGHTMETAL_LEGGINGS, (ItemLike) TFItems.KNIGHTMETAL_BOOTS);
		ArmorRenderer.register(new TFSimpleArmorRenderer(twilightforest.client.model.armor.TFArmorModel::new, twilightforest.client.model.TFModelLayers.PHANTOM_ARMOR_INNER, twilightforest.client.model.TFModelLayers.PHANTOM_ARMOR_OUTER, "phantom"),
				(ItemLike) TFItems.PHANTOM_HELMET, (ItemLike) TFItems.PHANTOM_CHESTPLATE, (ItemLike) TFItems.PHANTOM_LEGGINGS, (ItemLike) TFItems.PHANTOM_BOOTS);
		ArmorRenderer.register(new TFSimpleArmorRenderer(twilightforest.client.model.armor.YetiArmorModel::new, twilightforest.client.model.TFModelLayers.YETI_ARMOR_INNER, twilightforest.client.model.TFModelLayers.YETI_ARMOR_OUTER, "yeti"),
				(ItemLike) TFItems.YETI_HELMET, (ItemLike) TFItems.YETI_CHESTPLATE, (ItemLike) TFItems.YETI_LEGGINGS, (ItemLike) TFItems.YETI_BOOTS);
		ArmorRenderer.register(new TravellersArmorRenderer(),
				(ItemLike) TFItems.TRAVELLERS_GOGGLES, (ItemLike) TFItems.TRAVELLERS_VEST, (ItemLike) TFItems.TRAVELLERS_GLOVES, (ItemLike) TFItems.TRAVELLERS_WINGS, (ItemLike) TFItems.TRAVELLERS_BELT, (ItemLike) TFItems.TRAVELLERS_BOOTS);
	}

	@Override
	public final void render(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, ItemStack stack, HumanoidRenderState humanoidRenderState, EquipmentSlot slot, int light, HumanoidModel<HumanoidRenderState> contextModel) {
		HumanoidModel<HumanoidRenderState> model = this.createModel(humanoidRenderState, stack, slot, contextModel);
		this.setupModelAnimations(humanoidRenderState, stack, slot, model);
		this.renderModel(poseStack, submitNodeCollector, light, stack, slot, model, humanoidRenderState);
	}

	protected abstract HumanoidModel<HumanoidRenderState> createModel(HumanoidRenderState state, ItemStack stack, EquipmentSlot slot, HumanoidModel<HumanoidRenderState> contextModel);

	protected void setupModelAnimations(HumanoidRenderState state, ItemStack stack, EquipmentSlot slot, HumanoidModel<HumanoidRenderState> model) {
	}

	protected void renderModel(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int light, ItemStack stack, EquipmentSlot slot, HumanoidModel<HumanoidRenderState> model, HumanoidRenderState state) {
		Identifier texture = armorTexture(this.texturePrefix(stack, slot, state), slot, false);
		renderTinted(poseStack, submitNodeCollector, light, stack, model, state, texture, 0xFFFFFFFF);
	}

	protected String texturePrefix(ItemStack stack, EquipmentSlot slot, HumanoidRenderState state) {
		return "ironwood";
	}

	protected static Identifier armorTexture(String prefix, EquipmentSlot slot, boolean overlay) {
		int layer = slot == EquipmentSlot.LEGS ? 2 : 1;
		return TwilightForestMod.prefix("textures/models/armor/" + prefix + "_layer_" + layer + (overlay ? "_overlay" : "") + ".png");
	}

	protected static void renderTinted(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int light, ItemStack stack, HumanoidModel<HumanoidRenderState> model, HumanoidRenderState state, Identifier texture, int color) {
		RenderType renderType = RenderTypes.armorCutoutNoCull(texture);
		OrderedSubmitNodeCollector orderedCollector = submitNodeCollector.order(0);
		ArmorRenderer.submitTransformCopyingModel(model, state, model, state, false, orderedCollector, poseStack, renderType, light, OverlayTexture.NO_OVERLAY, color, null, state.outlineColor, null);
		if (stack.hasFoil()) {
			ArmorRenderer.submitTransformCopyingModel(model, state, model, state, false, submitNodeCollector.order(1), poseStack, RenderTypes.armorEntityGlint(), light, OverlayTexture.NO_OVERLAY, color, null, state.outlineColor, null);
		}
	}
}