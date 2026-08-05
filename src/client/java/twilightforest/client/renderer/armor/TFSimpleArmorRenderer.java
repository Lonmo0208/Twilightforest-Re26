package twilightforest.client.renderer.armor;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import twilightforest.client.model.armor.TFArmorModel;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class TFSimpleArmorRenderer extends TFArmorRenderer {
	protected final Function<ModelPart, TFArmorModel> createModelInstance;
	protected final ModelLayerLocation innerArmorModel;
	protected final ModelLayerLocation outerArmorModel;
	protected final String texturePrefix;

	public TFSimpleArmorRenderer(Function<ModelPart, TFArmorModel> createModelInstance, ModelLayerLocation innerLayerLocation, ModelLayerLocation outerLayerLocation) {
		this(createModelInstance, innerLayerLocation, outerLayerLocation, null);
	}

	public TFSimpleArmorRenderer(Function<ModelPart, TFArmorModel> createModelInstance, ModelLayerLocation innerLayerLocation, ModelLayerLocation outerLayerLocation, String texturePrefix) {
		this.createModelInstance = createModelInstance;
		this.innerArmorModel = innerLayerLocation;
		this.outerArmorModel = outerLayerLocation;
		this.texturePrefix = texturePrefix;
	}

	@Override
	protected HumanoidModel<HumanoidRenderState> createModel(HumanoidRenderState state, ItemStack stack, EquipmentSlot slot, HumanoidModel<HumanoidRenderState> contextModel) {
		ModelLayerLocation layerLocation = slot == EquipmentSlot.LEGS ? innerArmorModel : outerArmorModel;
		ModelPart sharedRoot = getModelPart(layerLocation);

		// Deep copy the entire ModelPart tree so we can safely modify visible
		// without affecting the shared cached model (critical: submitModel is deferred!)
		ModelPart copiedRoot = deepCopyModelPart(sharedRoot);

		// Hide everything first
		copiedRoot.getAllParts().forEach(p -> p.visible = false);

		// Show only the parts for this equipment slot
		ModelPart head = copiedRoot.getChild("head");
		ModelPart hat = head.getChild("hat");
		ModelPart body = copiedRoot.getChild("body");
		ModelPart rightArm = copiedRoot.getChild("right_arm");
		ModelPart leftArm = copiedRoot.getChild("left_arm");
		ModelPart rightLeg = copiedRoot.getChild("right_leg");
		ModelPart leftLeg = copiedRoot.getChild("left_leg");

		switch (slot) {
			case HEAD -> {
				head.getAllParts().forEach(p -> p.visible = true);
				hat.getAllParts().forEach(p -> p.visible = true);
			}
			case CHEST -> {
				body.getAllParts().forEach(p -> p.visible = true);
				rightArm.getAllParts().forEach(p -> p.visible = true);
				leftArm.getAllParts().forEach(p -> p.visible = true);
			}
			case LEGS -> {
				body.getAllParts().forEach(p -> p.visible = true);
				rightLeg.getAllParts().forEach(p -> p.visible = true);
				leftLeg.getAllParts().forEach(p -> p.visible = true);
			}
			case FEET -> {
				rightLeg.getAllParts().forEach(p -> p.visible = true);
				leftLeg.getAllParts().forEach(p -> p.visible = true);
			}
			default -> { }
		}

		copiedRoot.visible = true;

		return createModelInstance.apply(copiedRoot);
	}

	@Override
	protected String texturePrefix(ItemStack stack, EquipmentSlot slot, HumanoidRenderState state) {
		return this.texturePrefix != null ? this.texturePrefix : super.texturePrefix(stack, slot, state);
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

			// Cube is effectively immutable so sharing references is safe
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
