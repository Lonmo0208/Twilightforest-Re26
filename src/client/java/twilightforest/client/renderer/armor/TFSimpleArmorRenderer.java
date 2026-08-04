package twilightforest.client.renderer.armor;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import twilightforest.client.model.armor.TFArmorModel;

import java.util.function.Function;

public class TFSimpleArmorRenderer extends TFArmorRenderer {
	protected final Function<ModelPart, TFArmorModel> createModelInstance;
	protected final ModelLayerLocation innerArmorModel;
	protected final ModelLayerLocation outerArmorModel;

	public TFSimpleArmorRenderer(Function<ModelPart, TFArmorModel> createModelInstance, ModelLayerLocation innerLayerLocation, ModelLayerLocation outerLayerLocation) {
		this.createModelInstance = createModelInstance;
		this.innerArmorModel = innerLayerLocation;
		this.outerArmorModel = outerLayerLocation;
	}

	@Override
	protected HumanoidModel<HumanoidRenderState> createModel(HumanoidRenderState state, ItemStack stack, EquipmentSlot slot, HumanoidModel<HumanoidRenderState> contextModel) {
		ModelLayerLocation layerLocation = slot == EquipmentSlot.LEGS ? innerArmorModel : outerArmorModel;
		return createModelInstance.apply(getModelPart(layerLocation));
	}
}