package twilightforest.client.event;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.player.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.ClientInput;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.player.AvatarRenderer;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.PlayerModelPart;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.phys.Vec2;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import twilightforest.beanification.Component;
import twilightforest.TwilightForestMod;
import twilightforest.client.model.TFModelLayers;
import twilightforest.config.TFConfig;
import twilightforest.init.*;
import twilightforest.init.custom.TravellersModifiersManager;
import twilightforest.item.travellers_gear.TravellersArmorBeltItem;
import twilightforest.item.travellers_gear.TravellersGearLogic;
import twilightforest.item.travellers_gear.modifiers.TravellersModifier;
import twilightforest.network.*;
import twilightforest.tags.TFItemTags;

import java.lang.reflect.Field;

@Component
@Environment(EnvType.CLIENT)
public class TravellersClientEvents {

	private static boolean isZoomKeyHeld(Player player) {
		return TFKeyBinds.ZOOM_KEY.isDown() && !player.isScoping();
	}

	// TODO: Port to Fabric - Event registration (these methods need to be called from Fabric event callbacks)
	private void setup() {
	}

	// Reflection-based helper to set the protected moveVector field on ClientInput
	private static final Field MOVE_VECTOR_FIELD;

	static {
		try {
			MOVE_VECTOR_FIELD = ClientInput.class.getDeclaredField("moveVector");
			MOVE_VECTOR_FIELD.setAccessible(true);
		} catch (NoSuchFieldException e) {
			throw new RuntimeException("Failed to find ClientInput.moveVector field", e);
		}
	}

	private static void setMoveVector(ClientInput input, Vec2 vec) {
		try {
			MOVE_VECTOR_FIELD.set(input, vec);
		} catch (IllegalAccessException e) {
			throw new RuntimeException("Failed to set ClientInput.moveVector", e);
		}
	}

	private void handleAgileRanger(LocalPlayer localPlayer) {
		if (localPlayer == null)
			return;
		ItemStack leggingsStack = localPlayer.getItemBySlot(EquipmentSlot.LEGS);
		Float agileRangerModifier = leggingsStack.get(TFDataComponents.AGILE_RANGER_MODIFIER);
		if (!TravellersModifiersManager.isModifierActive(localPlayer, leggingsStack, TravellersModifiersManager.AGILE_RANGER_MODIFIER) || agileRangerModifier == null)
			return;
		ItemStack stack = localPlayer.getUseItem();
		boolean isLegalItem = (stack.getItem() instanceof ProjectileWeaponItem || stack.is(TFItemTags.TRAVELLERS_AGILE_RANGER_WHITELISTED)) && !stack.is(TFItemTags.TRAVELLERS_AGILE_RANGER_BLACKLISTED);
		if (localPlayer.isUsingItem() && !localPlayer.isPassenger() && isLegalItem) {
			ClientInput input = localPlayer.input;
			// In 26.1.2, ClientInput.moveVector is protected -- use reflection to modify it
			Vec2 old = input.getMoveVector();
			setMoveVector(input, new Vec2(old.x * agileRangerModifier, old.y * agileRangerModifier));
		}
	}

	private void handleStraightAhead(LocalPlayer localPlayer) {
		if (localPlayer == null)
			return;
		ItemStack bootsStack = localPlayer.getItemBySlot(EquipmentSlot.FEET);
		Double multiplier = bootsStack.get(TFDataComponents.STRAIGHT_AHEAD_MULTIPLIER);
		AttributeInstance attributeInstance = localPlayer.getAttributes().getInstance(Attributes.MOVEMENT_SPEED);
		if (attributeInstance == null)
			return;

		ClientInput input = localPlayer.input;
		if (!TravellersModifiersManager.isModifierActive(localPlayer, bootsStack, TravellersModifiersManager.STRAIGHT_AHEAD_MODIFIER) || multiplier == null || input.getMoveVector().y <= 0)
			multiplier = 1D;
		attributeInstance.addOrUpdateTransientModifier(new AttributeModifier(TFAttributeModifiers.STRAIGHT_AHEAD_ATTRIBUTE_MODIFIER_LOCATION, multiplier - 1, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
		// In 26.1.2, ClientInput.moveVector is protected -- use reflection to modify it
		Vec2 old = input.getMoveVector();
		setMoveVector(input, new Vec2((float)(old.x / multiplier), old.y));
	}

	private void speedUpControlledWhileSneaking(LocalPlayer localPlayer) {
		if (localPlayer == null || !localPlayer.getAttached(TFDataAttachments.IS_GRADUALLY_GLIDING) || !localPlayer.isShiftKeyDown())
			return;
		// In 26.1.2, ClientInput.moveVector is protected -- use reflection to modify it
		Vec2 old = localPlayer.input.getMoveVector();
		setMoveVector(localPlayer.input, new Vec2(old.x / 0.2F, old.y / 0.2F));
	}

	private void handleSidestep(LocalPlayer localPlayer) {
		if (localPlayer == null || !localPlayer.onGround())
			return;

		ClientInput input = localPlayer.input;
		float leftImpulse = input.getMoveVector().x;
		boolean lastImpulseZero = localPlayer.getAttached(TFDataAttachments.LAST_HORIZONTAL_IMPULSE) == 0;
		boolean sameImpulseDirection = Math.signum(localPlayer.getAttached(TFDataAttachments.LAST_NON_ZERO_HORIZONTAL_IMPULSE)) == Math.signum(leftImpulse);
		int currentTime = localPlayer.tickCount;
		int lastWalkingTime = localPlayer.getAttached(TFDataAttachments.LAST_HORIZONTAL_WALKING_TIME);
		boolean hasDoubleTapped = currentTime - lastWalkingTime < 4;

		if (lastImpulseZero && sameImpulseDirection && hasDoubleTapped && leftImpulse != 0) {
			boolean isLeftSidestep = leftImpulse > 0;
			if (TravellersGearLogic.tryPerformSidestep(localPlayer, isLeftSidestep)) {
				ClientPlayNetworking.send(new PerformSidestepPacket(isLeftSidestep));
			}
		}

		localPlayer.setAttached(TFDataAttachments.LAST_HORIZONTAL_IMPULSE, leftImpulse);
		if (leftImpulse != 0) {
			localPlayer.setAttached(TFDataAttachments.LAST_HORIZONTAL_WALKING_TIME, currentTime);
			localPlayer.setAttached(TFDataAttachments.LAST_NON_ZERO_HORIZONTAL_IMPULSE, leftImpulse);
		}
	}

	// Legacy NeoForge event-based methods removed -- use the Fabric-compatible versions above

	// TODO: Wire these methods into Fabric event callbacks (ClientTickEvents, etc.)

	/**
	 * Called per frame from ClientTickEvents (or equivalent) to apply stealth effect.
	 */
	private void handleStealth() {
		if (Minecraft.getInstance().level == null)
			return;
		for (Entity entity : Minecraft.getInstance().level.entitiesForRendering()) {
			if (!(entity instanceof Player player)) continue;
			TravellersGearLogic.travellersStealth(player, player1 -> player1.setInvisible(true));
		}
	}

	/**
	 * Called per frame from ClientTickEvents (or equivalent) to detect double-jump key press.
	 * Uses KeyMapping.consumeClick() to detect fresh presses (Fabric-compatible pattern).
	 */
	private void handleDoubleJump() {
		if (!(Minecraft.getInstance().player instanceof LocalPlayer localPlayer)
			|| !Minecraft.getInstance().options.keyJump.consumeClick()
			|| Minecraft.getInstance().screen != null)
			return;
		int lastJumpKeyPressTime = localPlayer.getAttached(TFDataAttachments.LAST_JUMP_KEY_PRESS_TIME);
		localPlayer.setAttached(TFDataAttachments.LAST_JUMP_KEY_PRESS_TIME, localPlayer.tickCount);
		boolean avoidCreativeFly = localPlayer.getAbilities().mayfly && localPlayer.tickCount - lastJumpKeyPressTime <= 6;
		if (!avoidCreativeFly && TravellersModifiersManager.isModifierActive(localPlayer, TravellersModifiersManager.DOUBLE_JUMP_MODIFIER)) {
			if (TravellersGearLogic.performDoubleJump(localPlayer)) {
				ClientPlayNetworking.send(new PerformDoubleJumpPacket());
			}
		}
	}

	/**
	 * Called when the FOV modifier is computed (via Fabric FovModifierCallback or similar).
	 * Returns the modified FOV modifier.
	 */
	private float updateZoomState(float currentFovModifier) {
		LocalPlayer player = Minecraft.getInstance().player;
		if (player == null) return currentFovModifier;
		boolean wasUsingZoom = player.getAttached(TFDataAttachments.IS_USING_GOGGLES_ZOOM_MODIFIER);
		ItemStack headStack = player.getItemBySlot(EquipmentSlot.HEAD);
		Float zoomModifier = headStack.get(TFDataComponents.ZOOM_ABILITY_MODIFIER);
		boolean isUsingZoom = isZoomKeyHeld(player) && TravellersModifiersManager.isModifierActive(player, headStack, TravellersModifiersManager.ZOOM_ABILITY) && zoomModifier != null;
		float result = currentFovModifier;
		if (isUsingZoom)
			result = currentFovModifier * zoomModifier;
		if (isUsingZoom == wasUsingZoom)
			return result;

		player.setAttached(TFDataAttachments.IS_USING_GOGGLES_ZOOM_MODIFIER, isUsingZoom);
		player.playSound(isUsingZoom ? TFSounds.GOGGLES_ZOOM_IN : TFSounds.GOGGLES_ZOOM_OUT);
		ClientPlayNetworking.send(new GogglesZoomPacket(isUsingZoom, player.getUUID()));
		return result;
	}

	/**
	 * Called per frame from ClientTickEvents (or equivalent) to update gradual glide state.
	 */
	private void updateGradualGlideState() {
		LocalPlayer player = Minecraft.getInstance().player;
		if (player == null) return;
		boolean wasGraduallyGliding = player.getAttached(TFDataAttachments.IS_GRADUALLY_GLIDING);
		boolean shiftHeld = player.isShiftKeyDown();
		boolean isGraduallyGliding = TFConfig.manualTravellersWingsGradualGlideDefault == shiftHeld && player.getKnownMovement().y() < 0 && !player.onGround();
		if (isGraduallyGliding == wasGraduallyGliding)
			return;

		player.setAttached(TFDataAttachments.IS_GRADUALLY_GLIDING, isGraduallyGliding);
		ClientPlayNetworking.send(new GradualGlidePacket(isGraduallyGliding, player.getUUID()));
	}

	/**
	 * Called from a key input callback or ClientTickEvents when ITEM_DISPLAY_MAP_CYCLE_KEY is pressed.
	 */
	private void cycleItemDisplayMap() {
		if (!(Minecraft.getInstance().player instanceof LocalPlayer localPlayer) || !TFKeyBinds.ITEM_DISPLAY_MAP_CYCLE_KEY.consumeClick())
			return;
		ClientPlayNetworking.send(CycleMapSlotPacket.INSTANCE);
	}

	/**
	 * Called from a key input callback or ClientTickEvents when SWAP_HOTBAR_KEY is pressed.
	 */
	private void swapHotbar() {
		if (!TFKeyBinds.SWAP_HOTBAR_KEY.consumeClick())
			return;
		Player player = Minecraft.getInstance().player;
		if (!(player instanceof LocalPlayer localPlayer)) return;
		ItemStack legArmor = localPlayer.getItemBySlot(EquipmentSlot.LEGS);
		ItemContainerContents containerContents = legArmor.get(DataComponents.CONTAINER);
		if (!TravellersArmorBeltItem.hasSwapHotbar(player, legArmor) || containerContents == null)
			return;
		ClientPlayNetworking.send(SwapHotbarPacket.INSTANCE);
	}

	/**
	 * Called from a key input callback or ClientTickEvents when RED_THREAD_VISION_KEY is pressed.
	 */
	private void toggleRedThreadVision() {
		this.toggleBooleanDataAttachment(TFKeyBinds.RED_THREAD_VISION_KEY.consumeClick(), TravellersModifiersManager.RED_THREAD_VISION_MODIFIER, TFDataAttachments.TRAVELLERS_GOGGLES_RED_THREAD_VISION);
	}

	private void toggleBooleanDataAttachment(boolean pressed, ResourceKey<TravellersModifier> modifier, AttachmentType<Boolean> attachment) {
		if (!pressed)
			return;

		Player player = Minecraft.getInstance().player;
		if (player == null || !TravellersModifiersManager.isModifierActive(player, modifier))
			return;

		boolean current = player.getAttached(attachment);
		player.setAttached(attachment, !current);
	}

	/**
	 * Called when the mouse sensitivity is computed (via a Fabric callback or mixin).
	 * Returns the modified mouse sensitivity.
	 */
	private double slowZoomSensitivity(boolean cinematicCameraEnabled, double mouseSensitivity) {
		Player player = Minecraft.getInstance().player;
		if (cinematicCameraEnabled || player == null)
			return mouseSensitivity;

		ItemStack headStack = player.getItemBySlot(EquipmentSlot.HEAD);
		Float zoomModifier = headStack.get(TFDataComponents.ZOOM_ABILITY_MODIFIER);
		if (zoomModifier == null || !isZoomKeyHeld(player))
			return mouseSensitivity;

		// vanilla math for turning is (m * 0.6 + 0.2)³ * 8; where m is the mouse sensitivity
		// vanilla spyglasses avoid using the "* 8" part, so we probably want to as well
		// the mod value to reverse that was borrowed from IE since they also have zoom functionality
		// we can then divide by our zoom modifier (and add 0.05 to slow it down slightly) to set the sensitivity to a reasonable value when zooming
		double mod = 0.5D - 1 / (6 * mouseSensitivity);
		double fovMod = zoomModifier + 0.05F;
		return mod * mouseSensitivity / fovMod;
	}

	/**
	 * Called to render traveller gloves over the first-person arm.
	 * Returns true if the gloves were rendered (cancelling the default arm render).
	 */
	private boolean renderGlovesInFirstPerson(AbstractClientPlayer player, HumanoidArm arm, SubmitNodeCollector collector, PoseStack poseStack, int packedLight) {
		if (TFConfig.firstPersonGloveOverlay) {
			ItemStack chestStack = player.getItemBySlot(EquipmentSlot.CHEST);
			if (chestStack.has(TFDataComponents.TRAVELLERS_HAS_GLOVES) && !chestStack.has(TFDataComponents.EMPERORS_CLOTH)) {
				boolean rightArm = arm == HumanoidArm.RIGHT;

				// Get player model for arm pose setup (same as AvatarRenderer.renderHand)
				AvatarRenderer<AbstractClientPlayer> renderer = Minecraft.getInstance().getEntityRenderDispatcher().getPlayerRenderer(player);
				PlayerModel playerModel = renderer.getModel();
				ModelPart armPart = rightArm ? playerModel.rightArm : playerModel.leftArm;
				armPart.resetPose();
				armPart.visible = true;
				playerModel.leftSleeve.visible = player.isModelPartShown(PlayerModelPart.LEFT_SLEEVE);
				playerModel.rightSleeve.visible = player.isModelPartShown(PlayerModelPart.RIGHT_SLEEVE);
				playerModel.leftArm.zRot = -0.1F;
				playerModel.rightArm.zRot = 0.1F;

				// Get the player's skin texture
				Identifier skinTexture = player.getSkin().body().texturePath();

				// Render the player's arm skin FIRST
				collector.submitModelPart(
					armPart,
					poseStack,
					RenderTypes.entityTranslucent(skinTexture),
					packedLight,
					OverlayTexture.NO_OVERLAY,
					null
				);

				// Get the glove model part from the baked armor model layer
				boolean slim = playerModel.slim;
				ModelPart gloveRoot = Minecraft.getInstance().getEntityModels().bakeLayer(
					slim ? TFModelLayers.TRAVELLERS_ARMOR_CHEST_GLOVES_SLIM : TFModelLayers.TRAVELLERS_ARMOR_CHEST_GLOVES
				);
				gloveRoot.getAllParts().forEach(part -> part.skipDraw = true);
				String armName = rightArm ? "right_arm" : "left_arm";
				ModelPart gloveArmPart = gloveRoot.getChild(armName);
				gloveArmPart.skipDraw = false;
				gloveArmPart.visible = true;

				// Copy the pose from the vanilla arm to the glove arm
				gloveArmPart.xRot = armPart.xRot;
				gloveArmPart.yRot = armPart.yRot;
				gloveArmPart.zRot = armPart.zRot;

				// Render the glove ON TOP of the arm skin
				Identifier gloveLocation = TwilightForestMod.prefix("textures/models/armor/travellers_layer_1.png");
				collector.submitModelPart(
					gloveArmPart,
					poseStack,
					RenderTypes.armorCutoutNoCull(gloveLocation),
					packedLight,
					OverlayTexture.NO_OVERLAY,
					null
				);
				return true;
			}
		}
		return false;
	}
}
