package twilightforest.init;

import com.mojang.authlib.GameProfile;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.minecraft.core.UUIDUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.Entity;
import twilightforest.TwilightForestMod;
import twilightforest.components.entity.*;
import twilightforest.util.Codecs;

import java.util.function.Supplier;

public class TFDataAttachments {

	/**
	 * Safe attachment accessor with defensive fallback.
	 * <p>
	 * Even though every AttachmentType in this class declares an {@code initializer(...)},
	 * Fabric can still return {@code null} during certain early lifecycle events
	 * (e.g. {@code ServerPlayerEvents.PlayerLoggedInEvent} for brand-new players, or
	 * mid-way through {@code COPY_FROM} / {@code AFTER_RESPAWN} transitions).
	 * <p>
	 * This helper guarantees a non-null value and writes the fallback back to the
	 * entity so subsequent calls stay consistent.
	 *
	 * @param entity          the entity to read the attachment from
	 * @param type            the registered attachment type
	 * @param defaultSupplier supplier for a fresh default instance (matches the type's declared initializer)
	 * @param <A>             attachment value type
	 * @param <E>             entity subtype
	 * @return a non-null attachment value (either the stored one or the created default)
	 */
	public static <A, E extends Entity> A getOrCreate(E entity, AttachmentType<A> type, Supplier<A> defaultSupplier) {
		A a = entity.getAttached(type);
		if (a == null) {
			a = defaultSupplier.get();
			entity.setAttached(type, a);
		}
		return a;
	}

	public static final AttachmentType<Boolean> FEATHER_FAN = AttachmentRegistry.create(TwilightForestMod.prefix("feather_fan_falling"), builder ->
		builder.initializer(() -> false).persistent(Codec.BOOL.fieldOf("feather_fan_falling").codec()).syncWith(ByteBufCodecs.BOOL, (entity, target) -> true));

	public static final AttachmentType<PotionFlaskTrackingAttachment> FLASK_DOSES = AttachmentRegistry.create(TwilightForestMod.prefix("flask_doses"), builder ->
		builder.initializer(PotionFlaskTrackingAttachment::new).persistent(PotionFlaskTrackingAttachment.CODEC.codec()));

	public static final AttachmentType<FortificationShieldAttachment> FORTIFICATION_SHIELDS = AttachmentRegistry.create(TwilightForestMod.prefix("fortification_shields"), builder ->
		builder.initializer(FortificationShieldAttachment::new).persistent(FortificationShieldAttachment.CODEC.codec()).syncWith(FortificationShieldAttachment.STREAM_CODEC, (entity, target) -> true));

	public static final AttachmentType<GiantPickaxeMiningAttachment> GIANT_PICKAXE_MINING = AttachmentRegistry.create(TwilightForestMod.prefix("giant_pickaxe_mining"), builder ->
		builder.initializer(GiantPickaxeMiningAttachment::new));

	public static final AttachmentType<YetiThrowAttachment> YETI_THROWING = AttachmentRegistry.create(TwilightForestMod.prefix("yeti_throwing"), builder ->
		builder.initializer(YetiThrowAttachment::new));

	public static final AttachmentType<MultiplayerInclusivityAttachment> MULTIPLAYER_FIGHT = AttachmentRegistry.create(TwilightForestMod.prefix("multiplayer_fight"), builder ->
		builder.initializer(MultiplayerInclusivityAttachment::new));

	public static final AttachmentType<TFPortalAttachment> TF_PORTAL_COOLDOWN = AttachmentRegistry.create(TwilightForestMod.prefix("tf_portal_cooldown"), builder ->
		builder.initializer(TFPortalAttachment::new));

	public static final AttachmentType<SmashBlocksEnchantmentAttachment> SMASH_BLOCKS = AttachmentRegistry.create(TwilightForestMod.prefix("smash_blocks"), builder ->
		builder.initializer(SmashBlocksEnchantmentAttachment::new).persistent(SmashBlocksEnchantmentAttachment.CODEC.codec()));

	public static final AttachmentType<GameProfile> ZOMBIFIED_PLAYER = AttachmentRegistry.create(TwilightForestMod.prefix("zombified_player"), builder ->
		builder.initializer(() -> UUIDUtil.createOfflineProfile("GizmoTheMoonPig")).persistent(Codecs.SIMPLE_GAME_PROFILE.codec()));

	public static final AttachmentType<Unit> LEASH_PATHFINDER_OVERRIDE = AttachmentRegistry.create(TwilightForestMod.prefix("leashed_pathfinder_override"), builder ->
		builder.initializer(() -> Unit.INSTANCE).persistent(MapCodec.unit(Unit.INSTANCE).codec()));

	public static final AttachmentType<Unit> BANISHED_TO_TWILIGHT_FOREST = AttachmentRegistry.create(TwilightForestMod.prefix("twilightforest_banished"), builder ->
		builder.initializer(() -> Unit.INSTANCE).persistent(MapCodec.unit(Unit.INSTANCE).codec()).copyOnDeath());

	public static final AttachmentType<TravellersWingsAttachment> TRAVELLERS_WINGS = AttachmentRegistry.create(TwilightForestMod.prefix("travellers_wings"), builder ->
		builder.initializer(TravellersWingsAttachment::new));

	public static final AttachmentType<TravellersWingsAnimAttachment> TRAVELLERS_WINGS_ANIM = AttachmentRegistry.create(TwilightForestMod.prefix("travellers_wings_anim"), builder ->
		builder.initializer(TravellersWingsAnimAttachment::new));

	public static final AttachmentType<Boolean> IS_USING_GOGGLES_ZOOM_MODIFIER = AttachmentRegistry.create(TwilightForestMod.prefix("is_using_goggles_zoom_modifier"), builder ->
		builder.initializer(() -> false).persistent(Codec.BOOL.fieldOf("zooming").codec()).syncWith(ByteBufCodecs.BOOL, (entity, target) -> true));

	public static final AttachmentType<Boolean> TRAVELLERS_GOGGLES_RED_THREAD_VISION = AttachmentRegistry.create(TwilightForestMod.prefix("travellers_goggles_red_thread_vision"), builder ->
		builder.initializer(() -> true).persistent(Codec.BOOL.fieldOf("red_thread_vision").codec()));

	public static final AttachmentType<Long> LAST_TICK_WATER_WALKING = AttachmentRegistry.create(TwilightForestMod.prefix("last_tick_water_walking"), builder ->
		builder.initializer(() -> 0L).persistent(Codec.LONG.fieldOf("last_water_walking_tick").codec()));

	public static final AttachmentType<Long> LAST_TICK_WATER_WALK_LOG = AttachmentRegistry.create(TwilightForestMod.prefix("last_tick_water_walk_log"), builder ->
		builder.initializer(() -> -1L));

	public static final AttachmentType<Boolean> HAS_DOUBLE_JUMP = AttachmentRegistry.create(TwilightForestMod.prefix("has_double_jump"), builder ->
		builder.initializer(() -> false).persistent(Codec.BOOL.fieldOf("double_jump").codec()));

	public static final AttachmentType<Integer> DOUBLE_JUMP_VALIDATOR = AttachmentRegistry.create(TwilightForestMod.prefix("double_jump_validator"), builder ->
		builder.initializer(() -> 0).persistent(Codec.INT.fieldOf("double_jump_count").codec()));

	public static final AttachmentType<Integer> DOUBLE_JUMP_VALIDATOR_LAST_CHECK = AttachmentRegistry.create(TwilightForestMod.prefix("double_jump_validator_last_check"), builder ->
		builder.initializer(() -> 0).persistent(Codec.INT.fieldOf("last_double_jump_count").codec()));

	public static final AttachmentType<Double> TEMPORARY_SAVED_STRAIGHT_AHEAD = AttachmentRegistry.create(TwilightForestMod.prefix("temporary_saved_straight_ahead"), builder ->
		builder.initializer(() -> 1D).persistent(Codec.DOUBLE.fieldOf("straight_ahead").codec()));

	public static final AttachmentType<Long> LAST_DAMAGE_ARMOR_TIME = AttachmentRegistry.create(TwilightForestMod.prefix("last_damage_armor_time"), builder ->
		builder.initializer(() -> 0L).persistent(Codec.LONG.fieldOf("last_armor_damage_timestamp").codec()));

	public static final AttachmentType<Integer> LAST_JUMP_KEY_PRESS_TIME = AttachmentRegistry.create(TwilightForestMod.prefix("last_jump_key_press_time"), builder ->
		builder.initializer(() -> 0).persistent(Codec.INT.fieldOf("last_jump_key_press").codec()));

	public static final AttachmentType<Float> LAST_HORIZONTAL_IMPULSE = AttachmentRegistry.create(TwilightForestMod.prefix("last_horizontal_impulse"), builder ->
		builder.initializer(() -> 0F).persistent(Codec.FLOAT.fieldOf("last_horizontal_impulse").codec()));

	public static final AttachmentType<Float> LAST_NON_ZERO_HORIZONTAL_IMPULSE = AttachmentRegistry.create(TwilightForestMod.prefix("last_non_horizontal_impulse"), builder ->
		builder.initializer(() -> 0F).persistent(Codec.FLOAT.fieldOf("last_non_horizontal_impulse").codec()));

	public static final AttachmentType<Integer> LAST_HORIZONTAL_WALKING_TIME = AttachmentRegistry.create(TwilightForestMod.prefix("last_horizontal_walking_time"), builder ->
		builder.initializer(() -> 0).persistent(Codec.INT.fieldOf("last_horizontal_walk_time").codec()));

	public static final AttachmentType<Integer> SIDESTEP_VALIDATOR = AttachmentRegistry.create(TwilightForestMod.prefix("sidestep_validator"), builder ->
		builder.initializer(() -> 0).persistent(Codec.INT.fieldOf("side_step_count").codec()));

	public static final AttachmentType<Integer> SIDESTEP_VALIDATOR_LAST_CHECK = AttachmentRegistry.create(TwilightForestMod.prefix("sidestep_validator_last_check"), builder ->
		builder.initializer(() -> 0).persistent(Codec.INT.fieldOf("last_side_step_count").codec()));

	public static final AttachmentType<Boolean> IS_GRADUALLY_GLIDING = AttachmentRegistry.create(TwilightForestMod.prefix("is_gradually_gliding"), builder ->
		builder.initializer(() -> false).persistent(Codec.BOOL.fieldOf("gliding").codec()).syncWith(ByteBufCodecs.BOOL, (entity, target) -> true));

	public static final AttachmentType<SlimySolesAttachment> SLIMY_SOLES_BOUNCE_INFO = AttachmentRegistry.create(TwilightForestMod.prefix("slimy_soles_bounce_info"), builder ->
		builder.initializer(SlimySolesAttachment::new).persistent(SlimySolesAttachment.CODEC.codec()));

	public static final AttachmentType<Boolean> ENDER_BOW_ARROW = AttachmentRegistry.create(TwilightForestMod.prefix("ender_bow_arrow"), builder ->
		builder.initializer(() -> false).persistent(Codec.BOOL.fieldOf("ender_bow_arrow").codec()));

	
	
	public static final AttachmentType<CompoundTag> TF_PERSISTENT_DATA = AttachmentRegistry.create(TwilightForestMod.prefix("persistent_data"), builder ->
		builder.initializer(CompoundTag::new).persistent(CompoundTag.CODEC.fieldOf("tf_data").codec()));
}