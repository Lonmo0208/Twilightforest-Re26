package twilightforest.mixin;

import net.minecraft.client.player.ClientInput;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.phys.Vec2;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import twilightforest.init.TFAttributeModifiers;
import twilightforest.init.TFDataAttachments;
import twilightforest.init.TFDataComponents;
import twilightforest.init.custom.TravellersModifiersManager;
import twilightforest.tags.TFItemTags;

import java.lang.reflect.Field;

@Environment(EnvType.CLIENT)
@Mixin(net.minecraft.client.player.KeyboardInput.class)
public abstract class KeyboardInputMixin extends ClientInput {

	@Unique
	private static final Field MOVE_VECTOR_FIELD;
	static {
		try {
			MOVE_VECTOR_FIELD = ClientInput.class.getDeclaredField("moveVector");
			MOVE_VECTOR_FIELD.setAccessible(true);
		} catch (NoSuchFieldException e) {
			throw new RuntimeException("Failed to find ClientInput.moveVector field", e);
		}
	}

	@Unique
	private static void setMoveVector(ClientInput input, Vec2 vec) {
		try {
			MOVE_VECTOR_FIELD.set(input, vec);
		} catch (IllegalAccessException e) {
			throw new RuntimeException("Failed to set ClientInput.moveVector", e);
		}
	}

	@Unique
	private static boolean safeBoolAttachment(Player player) {
		Boolean val = player.getAttached(TFDataAttachments.IS_GRADUALLY_GLIDING);
		return val != null && val;
	}

	@Unique
	private static float safeFloatAttachment(Player player, AttachmentType<Float> type) {
		Float val = player.getAttached(type);
		return val != null ? val : 0.0F;
	}

	/**
	 * Called immediately after vanilla recalculates this.moveVector from key presses.
	 * This timing is critical: the next step in LocalPlayer.tick() will invoke aiStep()
	 * which reads this.moveVector to produce actual movement. Applying modifiers here
	 * guarantees that agile ranger / straight ahead / glide-sneak speed-up take effect
	 * on the very same tick (modifying from END_CLIENT_TICK was too late because
	 * aiStep() had already consumed the vector before our handler ran).
	 */
	@Inject(method = "tick", at = @At("TAIL"))
	private void tf$applyTravellersGearMovementModifiers(CallbackInfo ci) {
		if (!(net.minecraft.client.Minecraft.getInstance().player instanceof LocalPlayer localPlayer))
			return;

		this.applyAgileRanger(localPlayer);
		this.applyStraightAhead(localPlayer);
		this.applySpeedUpControlledWhileSneaking(localPlayer);
	}

	@Unique
	private void applyAgileRanger(LocalPlayer localPlayer) {
		ItemStack leggingsStack = localPlayer.getItemBySlot(EquipmentSlot.LEGS);
		Float agileRangerModifier = leggingsStack.get(TFDataComponents.AGILE_RANGER_MODIFIER);
		if (!TravellersModifiersManager.isModifierActive(localPlayer, leggingsStack, TravellersModifiersManager.AGILE_RANGER_MODIFIER) || agileRangerModifier == null)
			return;
		ItemStack stack = localPlayer.getUseItem();
		boolean isLegalItem = (stack.getItem() instanceof ProjectileWeaponItem || stack.is(TFItemTags.TRAVELLERS_AGILE_RANGER_WHITELISTED)) && !stack.is(TFItemTags.TRAVELLERS_AGILE_RANGER_BLACKLISTED);
		if (localPlayer.isUsingItem() && !localPlayer.isPassenger() && isLegalItem) {
			Vec2 old = this.getMoveVector();
			setMoveVector(this, new Vec2(old.x * agileRangerModifier, old.y * agileRangerModifier));
		}
	}

	@Unique
	private void applyStraightAhead(LocalPlayer localPlayer) {
		ItemStack bootsStack = localPlayer.getItemBySlot(EquipmentSlot.FEET);
		Double multiplier = bootsStack.get(TFDataComponents.STRAIGHT_AHEAD_MULTIPLIER);
		AttributeInstance attributeInstance = localPlayer.getAttributes().getInstance(Attributes.MOVEMENT_SPEED);
		if (attributeInstance == null)
			return;

		if (!TravellersModifiersManager.isModifierActive(localPlayer, bootsStack, TravellersModifiersManager.STRAIGHT_AHEAD_MODIFIER) || multiplier == null || this.getMoveVector().y <= 0)
			multiplier = 1D;
		attributeInstance.addOrUpdateTransientModifier(new AttributeModifier(TFAttributeModifiers.STRAIGHT_AHEAD_ATTRIBUTE_MODIFIER_LOCATION, multiplier - 1, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
		Vec2 old = this.getMoveVector();
		setMoveVector(this, new Vec2((float)(old.x / multiplier), old.y));
	}

	@Unique
	private void applySpeedUpControlledWhileSneaking(LocalPlayer localPlayer) {
		if (!safeBoolAttachment(localPlayer) || !localPlayer.isShiftKeyDown())
			return;
		Vec2 old = this.getMoveVector();
		setMoveVector(this, new Vec2(old.x / 0.2F, old.y / 0.2F));
	}
}
