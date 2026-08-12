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
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import twilightforest.beanification.Component;
import twilightforest.beanification.PostConstruct;
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
import twilightforest.util.TFEntityExtensions;

import java.lang.reflect.Field;

@Component
@Environment(EnvType.CLIENT)
public class TravellersClientEvents {

	private static volatile boolean setupInvoked = false;

	
	private boolean lastJumpKeyDown = false;
	
	
	private boolean hasDoneFirstGroundJump = false;
	
	
	
	private boolean lastOnGround = false;

	private static boolean isZoomKeyHeld(Player player) {
		return TFKeyBinds.ZOOM_KEY.isDown() && !player.isScoping();
	}

	private static boolean safeBoolAttachment(Player player, AttachmentType<Boolean> type) {
		Boolean val = player.getAttached(type);
		return val != null && val;
	}

	private static int safeIntAttachment(Player player, AttachmentType<Integer> type) {
		Integer val = player.getAttached(type);
		return val != null ? val : 0;
	}

	private static float safeFloatAttachment(Player player, AttachmentType<Float> type) {
		Float val = player.getAttached(type);
		return val != null ? val : 0.0F;
	}

	@PostConstruct
	public void setup() {
		// Guard against double-registration: BeanContext may call @PostConstruct via
		// classpath scanning while TwilightForestClient.onInitializeClient() also
		// invokes setup() manually. Either one wins, the other becomes a no-op.
		if (setupInvoked) return;
		synchronized (TravellersClientEvents.class) {
			if (setupInvoked) return;
			setupInvoked = true;
		}
		// Register per-frame client tick handling for all traveler's gear keybinds and modifiers
		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			LocalPlayer localPlayer = client.player;
			if (localPlayer == null) return;

			boolean nowOnGround = localPlayer.onGround();

			// 閳光偓閳光偓閳光偓閳光偓閳光偓閳光偓閳光偓閳光偓閳光偓閳光偓閳光偓閳光偓閳光偓閳光偓閳光偓閳光偓閳光偓閳光偓閳光偓閳光偓閳光偓閳光偓閳光偓閳光偓閳光偓閳光偓閳光偓閳光偓閳光偓閳光偓閳光偓閳光偓閳光偓閳光偓閳光偓閳光偓閳光偓閳光偓閳光偓閳光偓閳光偓閳光偓閳光偓閳光偓閳光偓閳光偓閳光偓閳光偓閳光偓閳光偓閳光偓閳光偓閳光偓閳光偓閳光偓閳光偓閳光偓
			
			
			
			
			
			// 閳光偓閳光偓閳光偓閳光偓閳光偓閳光偓閳光偓閳光偓閳光偓閳光偓閳光偓閳光偓閳光偓閳光偓閳光偓閳光偓閳光偓閳光偓閳光偓閳光偓閳光偓閳光偓閳光偓閳光偓閳光偓閳光偓閳光偓閳光偓閳光偓閳光偓閳光偓閳光偓閳光偓閳光偓閳光偓閳光偓閳光偓閳光偓閳光偓閳光偓閳光偓閳光偓閳光偓閳光偓閳光偓閳光偓閳光偓閳光偓閳光偓閳光偓閳光偓閳光偓閳光偓閳光偓閳光偓閳光偓閳光偓

			
			Boolean hasDoubleJump = null;
			boolean doubleJumpModifierActive = TravellersModifiersManager.isModifierActive(localPlayer, TravellersModifiersManager.DOUBLE_JUMP_MODIFIER);
			if (!doubleJumpModifierActive) {
				hasDoubleJump = false;
			} else if (nowOnGround || localPlayer.isInLiquid() || localPlayer.onClimbable()) {
				hasDoubleJump = true;
			}
			boolean currentHasDoubleJump = safeBoolAttachment(localPlayer, TFDataAttachments.HAS_DOUBLE_JUMP);
			if (hasDoubleJump != null && hasDoubleJump != currentHasDoubleJump) {
				localPlayer.setAttached(TFDataAttachments.HAS_DOUBLE_JUMP, hasDoubleJump);
				localPlayer.setAttached(TFDataAttachments.DOUBLE_JUMP_VALIDATOR, 0);
				AttributeInstance instance = localPlayer.getAttribute(Attributes.SAFE_FALL_DISTANCE);
				if (instance != null) {
					instance.removeModifier(TFAttributeModifiers.TRAVELLERS_DOUBLE_JUMP_SAFE_FALL_DISTANCE);
				}
			}

			// 閳烘劏鏅查埡鎰ㄦ櫜閳烘劏鏅查埡鎰ㄦ櫜閳烘劏鏅查埡鎰ㄦ櫜閳烘劏鏅查埡鎰ㄦ櫜閳烘劏鏅查埡鎰ㄦ櫜閳烘劏鏅查埡鎰ㄦ櫜閳烘劏鏅查埡鎰ㄦ櫜閳烘劏鏅查埡鎰ㄦ櫜閳烘劏鏅查埡鎰ㄦ櫜閳烘劏鏅查埡鎰ㄦ櫜閳烘劏鏅查埡鎰ㄦ櫜閳烘劏鏅查埡鎰ㄦ櫜閳烘劏鏅查埡鎰ㄦ櫜閳烘劏鏅查埡鎰ㄦ櫜閳?
			
			
			
			
			
			
			
			
			
			
			// 閳烘劏鏅查埡鎰ㄦ櫜閳烘劏鏅查埡鎰ㄦ櫜閳烘劏鏅查埡鎰ㄦ櫜閳烘劏鏅查埡鎰ㄦ櫜閳烘劏鏅查埡鎰ㄦ櫜閳烘劏鏅查埡鎰ㄦ櫜閳烘劏鏅查埡鎰ㄦ櫜閳烘劏鏅查埡鎰ㄦ櫜閳烘劏鏅查埡鎰ㄦ櫜閳烘劏鏅查埡鎰ㄦ櫜閳烘劏鏅查埡鎰ㄦ櫜閳烘劏鏅查埡鎰ㄦ櫜閳烘劏鏅查埡鎰ㄦ櫜閳烘劏鏅查埡鎰ㄦ櫜閳?
			this.handleDoubleJump();

			
			
			if (this.lastOnGround && !nowOnGround) {
				if (!this.hasDoneFirstGroundJump)
					this.hasDoneFirstGroundJump = true;
			}
			
			if (nowOnGround || localPlayer.isInLiquid() || localPlayer.onClimbable()) {
				if (this.hasDoneFirstGroundJump)
					this.hasDoneFirstGroundJump = false;
			}
			this.lastOnGround = nowOnGround;

			// Other keybinds and state updates
			this.cycleItemDisplayMap();
			this.swapHotbar();
			this.toggleRedThreadVision();

			// Per-frame state updates
			this.updateZoomState();
			this.updateGradualGlideState();

			// Note: moveVector modifiers (Agile Ranger, Straight Ahead, Glide-Sneak
			// speed-up) now run from KeyboardInputMixin at TAIL of tick(). Applying
			// them here was too late: aiStep() had already consumed the vector.

			// Sidestep impulse tracking + double-tap detection
			this.handleSidestep(localPlayer);

			// Stealth effect
			this.handleStealth();

			// Water walking 閳?client side must ALSO lift/snap the local player so the
			// client physics and server physics agree. If only the server runs this,
			// the client keeps applying gravity (velY=-0.0784) and sends underwater
			// position packets, fighting the server's teleportTo corrections 閳?jitter.
			TravellersGearLogic.waterWalkingTick(localPlayer);

			// Gradual glide 閳?must run on BOTH client AND server so that the client's
			// prediction (what the player actually sees on screen) is slowed by the
			// same 0.8333 multiplier the server applies. If we skip the client side:
			//   1) Player's local render shows full-speed free fall 閳?no perceived effect
			//   2) Client's move packets report the un-slowed position back to server
			//      which then overwrites the server-side setDeltaMovement correction
			// This is the exact mirror of TravellersGearEvents END_SERVER_TICK path.
			TravellersGearLogic.travellersWingsGradualGlide(localPlayer);
		});
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
		Vec2 old = input.getMoveVector();
		setMoveVector(input, new Vec2((float)(old.x / multiplier), old.y));
	}

	private void speedUpControlledWhileSneaking(LocalPlayer localPlayer) {
		if (localPlayer == null || !safeBoolAttachment(localPlayer, TFDataAttachments.IS_GRADUALLY_GLIDING) || !localPlayer.isShiftKeyDown())
			return;
		Vec2 old = localPlayer.input.getMoveVector();
		setMoveVector(localPlayer.input, new Vec2(old.x / 0.2F, old.y / 0.2F));
	}

	private void handleSidestep(LocalPlayer localPlayer) {
		if (localPlayer == null || !localPlayer.onGround())
			return;

		ClientInput input = localPlayer.input;
		float leftImpulse = input.getMoveVector().x;
		boolean lastImpulseZero = safeFloatAttachment(localPlayer, TFDataAttachments.LAST_HORIZONTAL_IMPULSE) == 0;
		boolean sameImpulseDirection = Math.signum(safeFloatAttachment(localPlayer, TFDataAttachments.LAST_NON_ZERO_HORIZONTAL_IMPULSE)) == Math.signum(leftImpulse);
		int currentTime = localPlayer.tickCount;
		int lastWalkingTime = safeIntAttachment(localPlayer, TFDataAttachments.LAST_HORIZONTAL_WALKING_TIME);
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

	private void handleStealth() {
		if (Minecraft.getInstance().level == null)
			return;
		for (Entity entity : Minecraft.getInstance().level.entitiesForRendering()) {
			if (!(entity instanceof Player player)) continue;
			TravellersGearLogic.travellersStealth(player, player1 -> player1.setInvisible(true));
		}
	}

	private void handleDoubleJump() {
		if (!(Minecraft.getInstance().player instanceof LocalPlayer localPlayer)) {
			return;
		}
		if (Minecraft.getInstance().gui.screen() != null) {
			return;
		}
		
		
		
		boolean keyDown = Minecraft.getInstance().options.keyJump.isDown();
		boolean edgePress = keyDown && !this.lastJumpKeyDown;
		this.lastJumpKeyDown = keyDown;  
		if (!edgePress) {
			return;
		}

		
		
		if (localPlayer.onGround() || localPlayer.isInLiquid() || localPlayer.onClimbable()) {
			return;
		}
		if (!this.hasDoneFirstGroundJump)
			return;

		int lastJumpKeyPressTime = safeIntAttachment(localPlayer, TFDataAttachments.LAST_JUMP_KEY_PRESS_TIME);
		localPlayer.setAttached(TFDataAttachments.LAST_JUMP_KEY_PRESS_TIME, localPlayer.tickCount);

		boolean avoidCreativeFly = localPlayer.getAbilities().mayfly && localPlayer.tickCount - lastJumpKeyPressTime <= 6;
		if (avoidCreativeFly)
			return;

		boolean isModifierActive = TravellersModifiersManager.isModifierActive(localPlayer, TravellersModifiersManager.DOUBLE_JUMP_MODIFIER);
		if (!isModifierActive)
			return;

		if (TravellersGearLogic.performDoubleJump(localPlayer)) {
			ClientPlayNetworking.send(new PerformDoubleJumpPacket());
		}
	}

	private void updateZoomState() {
		LocalPlayer player = Minecraft.getInstance().player;
		if (player == null) return;
		boolean wasUsingZoom = safeBoolAttachment(player, TFDataAttachments.IS_USING_GOGGLES_ZOOM_MODIFIER);
		ItemStack headStack = player.getItemBySlot(EquipmentSlot.HEAD);
		Float zoomModifier = headStack.get(TFDataComponents.ZOOM_ABILITY_MODIFIER);
		boolean isUsingZoom = isZoomKeyHeld(player) && TravellersModifiersManager.isModifierActive(player, headStack, TravellersModifiersManager.ZOOM_ABILITY) && zoomModifier != null;
		if (isUsingZoom == wasUsingZoom)
			return;

		player.setAttached(TFDataAttachments.IS_USING_GOGGLES_ZOOM_MODIFIER, isUsingZoom);
		player.playSound(isUsingZoom ? TFSounds.GOGGLES_ZOOM_IN : TFSounds.GOGGLES_ZOOM_OUT);
		ClientPlayNetworking.send(new GogglesZoomPacket(isUsingZoom, player.getUUID()));
	}

	private void updateGradualGlideState() {
		LocalPlayer player = Minecraft.getInstance().player;
		if (player == null) return;
		boolean wasGraduallyGliding = safeBoolAttachment(player, TFDataAttachments.IS_GRADUALLY_GLIDING);
		boolean shiftHeld = player.isShiftKeyDown();
		double velY = player.getKnownMovement().y();
		// NeoForge official three conditions, plus one Fabric-specific tolerance:
		//   1) manualDefault == shiftHeld  (Shift-toggle matches manual/auto mode)
		//   2) velY < 0                     (actually falling, not rising/apex)
		//   3) truly airborne               (NOT on solid ground 閳?but with a
		//                                     tolerance for Fabric-side false
		//                                     positives: water-walking code and
		//                                     1-frame collision lag both set
		//                                     onGround=true even when velY is
		//                                     already the full -0.0784 gravity
		//                                     value. If velY <= -0.05 we trust
		//                                     the physics and ignore the flag.)
		boolean trustOnGroundFalse = !player.onGround() || velY <= -0.05D;
		boolean isGraduallyGliding = TFConfig.manualTravellersWingsGradualGlideDefault == shiftHeld
			&& velY < 0
			&& trustOnGroundFalse;

		if (isGraduallyGliding == wasGraduallyGliding)
			return;

		player.setAttached(TFDataAttachments.IS_GRADUALLY_GLIDING, isGraduallyGliding);
		ClientPlayNetworking.send(new GradualGlidePacket(isGraduallyGliding, player.getUUID()));
	}

	private void cycleItemDisplayMap() {
		if (!(Minecraft.getInstance().player instanceof LocalPlayer localPlayer) || !TFKeyBinds.ITEM_DISPLAY_MAP_CYCLE_KEY.consumeClick())
			return;
		ClientPlayNetworking.send(CycleMapSlotPacket.INSTANCE);
	}

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

	private void toggleRedThreadVision() {
		this.toggleBooleanDataAttachment(TFKeyBinds.RED_THREAD_VISION_KEY.consumeClick(), TravellersModifiersManager.RED_THREAD_VISION_MODIFIER, TFDataAttachments.TRAVELLERS_GOGGLES_RED_THREAD_VISION);
	}

	private void toggleBooleanDataAttachment(boolean pressed, ResourceKey<TravellersModifier> modifier, AttachmentType<Boolean> attachment) {
		if (!pressed)
			return;

		Player player = Minecraft.getInstance().player;
		if (player == null || !TravellersModifiersManager.isModifierActive(player, modifier))
			return;

		boolean current = safeBoolAttachment(player, attachment);
		player.setAttached(attachment, !current);
	}

	public double slowZoomSensitivity(boolean cinematicCameraEnabled, double mouseSensitivity) {
		Player player = Minecraft.getInstance().player;
		if (cinematicCameraEnabled || player == null)
			return mouseSensitivity;

		ItemStack headStack = player.getItemBySlot(EquipmentSlot.HEAD);
		Float zoomModifier = headStack.get(TFDataComponents.ZOOM_ABILITY_MODIFIER);
		if (zoomModifier == null || !isZoomKeyHeld(player))
			return mouseSensitivity;

		double mod = 0.5D - 1 / (6 * mouseSensitivity);
		double fovMod = zoomModifier + 0.05F;
		return mod * mouseSensitivity / fovMod;
	}

	public boolean renderGlovesInFirstPerson(AbstractClientPlayer player, HumanoidArm arm, SubmitNodeCollector collector, PoseStack poseStack, int packedLight) {
		if (TFConfig.firstPersonGloveOverlay) {
			ItemStack chestStack = player.getItemBySlot(EquipmentSlot.CHEST);
			if (chestStack.has(TFDataComponents.TRAVELLERS_HAS_GLOVES) && !chestStack.has(TFDataComponents.EMPERORS_CLOTH)) {
				boolean rightArm = arm == HumanoidArm.RIGHT;

				AvatarRenderer<AbstractClientPlayer> renderer = Minecraft.getInstance().getEntityRenderDispatcher().getPlayerRenderer(player);
				PlayerModel playerModel = renderer.getModel();
				ModelPart armPart = rightArm ? playerModel.rightArm : playerModel.leftArm;
				armPart.resetPose();
				armPart.visible = true;
				playerModel.leftSleeve.visible = player.isModelPartShown(PlayerModelPart.LEFT_SLEEVE);
				playerModel.rightSleeve.visible = player.isModelPartShown(PlayerModelPart.RIGHT_SLEEVE);
				playerModel.leftArm.zRot = -0.1F;
				playerModel.rightArm.zRot = 0.1F;

				Identifier skinTexture = player.getSkin().body().texturePath();

				collector.submitModelPart(
					armPart,
					poseStack,
					RenderTypes.entityTranslucent(skinTexture),
					packedLight,
					OverlayTexture.NO_OVERLAY,
					null
				);

				boolean slim = playerModel.slim;
				ModelPart gloveRoot = Minecraft.getInstance().getEntityModels().bakeLayer(
					slim ? TFModelLayers.TRAVELLERS_ARMOR_CHEST_GLOVES_SLIM : TFModelLayers.TRAVELLERS_ARMOR_CHEST_GLOVES
				);
				gloveRoot.getAllParts().forEach(part -> part.skipDraw = true);
				String armName = rightArm ? "right_arm" : "left_arm";
				ModelPart gloveArmPart = gloveRoot.getChild(armName);
				gloveArmPart.skipDraw = false;
				gloveArmPart.visible = true;

				gloveArmPart.xRot = armPart.xRot;
				gloveArmPart.yRot = armPart.yRot;
				gloveArmPart.zRot = armPart.zRot;

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
