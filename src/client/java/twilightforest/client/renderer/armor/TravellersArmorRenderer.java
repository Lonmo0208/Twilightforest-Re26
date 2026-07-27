package twilightforest.client.renderer.armor;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.player.PlayerModel;
import net.minecraft.client.resources.model.EquipmentClientInfo;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;
import twilightforest.TwilightForestMod;
import twilightforest.client.model.TFModelLayers;
import twilightforest.client.model.armor.TFArmorModel;
import twilightforest.client.model.armor.TravellersWingsModel;
import twilightforest.init.TFDataAttachments;
import twilightforest.init.TFDataComponents;
import twilightforest.util.TFEntityExtensions;
import twilightforest.init.custom.TravellersModifiersManager;

public class TravellersArmorRenderer extends TFArmorRenderer {

	public TravellersArmorRenderer() {
		super(TFModelLayers.TRAVELLERS_ARMOR_HELMET, TFModelLayers.TRAVELLERS_ARMOR_CHEST_GLOVES, TFModelLayers.TRAVELLERS_ARMOR_CHEST_GLOVES_SLIM, TFModelLayers.TRAVELLERS_ARMOR_LEGGINGS, TFModelLayers.TRAVELLERS_ARMOR_BOOTS);
	}

	@Nullable
	public Identifier getArmorTexture(ItemStack stack, EquipmentClientInfo.LayerType type, EquipmentClientInfo.Layer layer, Identifier def) {
		LivingEntity entity = Minecraft.getInstance().player;
		if (type != EquipmentClientInfo.LayerType.HUMANOID_LEGGINGS && entity != null && ((TFEntityExtensions) entity).getData(() -> TFDataAttachments.IS_USING_GOGGLES_ZOOM_MODIFIER)) {
			return TwilightForestMod.prefix("textures/models/armor/travellers_layer_1_down.png");
		}
		return def;
	}

	@SuppressWarnings("rawtypes")
	public Model<?> getHumanoidArmorModel(ItemStack stack, EquipmentClientInfo.LayerType layerType, Model model) {
		if (stack.has(DataComponents.EQUIPPABLE)) {
			EquipmentSlot slot = stack.get(DataComponents.EQUIPPABLE).slot();
			ModelPart root = switch (slot) {
				case HEAD -> {
					ModelPart headLayer = this.getModelPart(TFModelLayers.TRAVELLERS_ARMOR_HELMET);
					headLayer.getAllParts().forEach(part -> part.skipDraw = true);
					headLayer.getChild("head").skipDraw = false;
					yield headLayer;
				}
				case CHEST -> {
					ModelPart chestLayer = this.getModelPart(this.isModelSlim(model) ? TFModelLayers.TRAVELLERS_ARMOR_CHEST_GLOVES_SLIM : TFModelLayers.TRAVELLERS_ARMOR_CHEST_GLOVES);
					chestLayer.getAllParts().forEach(part -> part.skipDraw = true);
					boolean hasChestplate = stack.has(TFDataComponents.TRAVELLERS_HAS_CHESTPLATE);
					boolean hasGloves = stack.has(TFDataComponents.TRAVELLERS_HAS_GLOVES);
					chestLayer.getChild("body").skipDraw = !hasChestplate;
					chestLayer.getChild("left_arm").skipDraw = !hasGloves;
					chestLayer.getChild("right_arm").skipDraw = !hasGloves;

					yield chestLayer;
				}
				case LEGS -> {
					ModelPart leggingsLayer = this.getModelPart(TFModelLayers.TRAVELLERS_ARMOR_LEGGINGS);
					leggingsLayer.getAllParts().forEach(part -> part.skipDraw = true);
					boolean hasWings = stack.has(TFDataComponents.TRAVELLERS_HAS_WINGS);
					boolean hasBelt = stack.has(TFDataComponents.TRAVELLERS_HAS_BELT) || TravellersModifiersManager.hasTravellersModifier(Minecraft.getInstance().level.registryAccess(), stack, TravellersModifiersManager.SWAP_HOTBAR_MODIFIER);

					TravellersWingsModel.skipBelt(leggingsLayer, !hasBelt);
					TravellersWingsModel.skipWings(leggingsLayer, !hasWings);

					yield leggingsLayer;
				}
				case FEET -> {
					ModelPart bootsLayer = this.getModelPart(TFModelLayers.TRAVELLERS_ARMOR_BOOTS);
					bootsLayer.getAllParts().forEach(part -> part.skipDraw = true);
					bootsLayer.getChild("right_leg").skipDraw = false;
					bootsLayer.getChild("left_leg").skipDraw = false;
					yield bootsLayer;
				}
				default -> null;
			};


			if (slot == EquipmentSlot.LEGS) {
				return new TravellersWingsModel(root);
			} else if (root != null) {
				return new TFArmorModel(root);
			}
		}
		return null;
	}

	@SuppressWarnings("rawtypes")
	public void setupModelAnimations(LivingEntity livingEntity, ItemStack itemStack, EquipmentSlot equipmentSlot, Model model, float limbSwing, float limbSwingAmount, float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {
		if (model instanceof TravellersWingsModel wingsModel)
			wingsModel.setupModelAnimations(livingEntity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
	}

	private boolean isModelSlim(Model<?> model) {
		if (model instanceof PlayerModel player) return player.slim;
		return false;
	}
}