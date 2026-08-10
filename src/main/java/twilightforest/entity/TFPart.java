package twilightforest.entity;

import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import twilightforest.TwilightForestMod;
import twilightforest.network.UpdateTFMultipartPacket;
import twilightforest.util.TFEntityExtensions;

import java.util.Objects;

public abstract class TFPart<T extends Entity> extends Entity {

	public static final Identifier RENDERER = TwilightForestMod.prefix("noop");

	private final T parentEntity;

	public T getParent() {
		return this.parentEntity;
	}

	protected EntityDimensions realSize = EntityDimensions.fixed(1F, 1F);

	protected int newPosRotationIncrements;
	protected double interpTargetX;
	protected double interpTargetY;
	protected double interpTargetZ;
	protected double interpTargetYaw;
	protected double interpTargetPitch;
	public float renderYawOffset;
	public float prevRenderYawOffset;

	public int deathTime;
	public int hurtTime;

	public TFPart(T parent, EntityType<?> type, Level level) {
		super(type, level);
		this.parentEntity = parent;
	}

	public Identifier renderer() {
		return RENDERER;
	}

	public void setPositionAndRotationDirect(double x, double y, double z, float yaw, float pitch, int posRotationIncrements) {
		this.interpTargetX = x;
		this.interpTargetY = y;
		this.interpTargetZ = z;
		this.interpTargetYaw = yaw;
		this.interpTargetPitch = pitch;
		this.newPosRotationIncrements = posRotationIncrements;
	}

	@Override
	public void tick() {
		updateLastPos();
		super.tick();
		if (this.newPosRotationIncrements > 0) {
			double d0 = this.getX() + (this.interpTargetX - this.getX()) / (double) this.newPosRotationIncrements;
			double d2 = this.getY() + (this.interpTargetY - this.getY()) / (double) this.newPosRotationIncrements;
			double d4 = this.getZ() + (this.interpTargetZ - this.getZ()) / (double) this.newPosRotationIncrements;
			double d6 = Mth.wrapDegrees(this.interpTargetYaw - (double) this.getYRot());
			this.setYRot((float) ((double) this.getYRot() + d6 / (double) this.newPosRotationIncrements));
			this.setXRot((float) ((double) this.getXRot() + (this.interpTargetPitch - (double) this.getXRot()) / (double) this.newPosRotationIncrements));
			--this.newPosRotationIncrements;
			this.setPos(d0, d2, d4);
			this.setRot(this.getYRot(), this.getXRot());
		}

		while (getYRot() - this.yRotO < -180F) this.yRotO -= 360F;
		while (getYRot() - this.yRotO >= 180F) this.yRotO += 360F;

		while (this.renderYawOffset - this.prevRenderYawOffset < -180F) this.prevRenderYawOffset -= 360F;
		while (this.renderYawOffset - this.prevRenderYawOffset >= 180F) this.prevRenderYawOffset += 360F;

		while (getXRot() - this.xRotO < -180F) this.xRotO -= 360F;
		while (getXRot() - this.xRotO >= 180F) this.xRotO += 360F;
	}

	public final void updateLastPos() {
		this.snapTo(this.getX(), this.getY(), this.getZ());
		this.yRotO = this.getYRot();
		this.xRotO = this.getXRot();
		this.tickCount++;
	}

	protected void setSize(EntityDimensions size) {
		this.realSize = size;
		this.dimensions = size;
		this.refreshDimensions();
	}

	@Override
	public boolean isCurrentlyGlowing() {
		return this.getParent().isCurrentlyGlowing();
	}

	@Override
	public boolean isInvisible() {
		return this.getParent().isInvisible();
	}

	@Override
	public EntityDimensions getDimensions(Pose pose) {
		return this.realSize;
	}

	@Override
	public boolean isPickable() {
		return true;
	}

	@Override
	public boolean isAttackable() {
		return true;
	}

	@Override
	public InteractionResult interact(Player player, InteractionHand hand, Vec3 location) {
		return this.getParent().interact(player, hand, location);
	}

	@Override
	public void setId(int id) {
		super.setId(id + 1);
	}

	public UpdateTFMultipartPacket.PartDataHolder writeData() {
		return new UpdateTFMultipartPacket.PartDataHolder(
			this.getX(),
			this.getY(),
			this.getZ(),
			this.getYRot(),
			this.getXRot(),
			this.dimensions.width(),
			this.dimensions.height(),
			this.dimensions.fixed(),
			getEntityData().packDirty());

	}

	public void readData(UpdateTFMultipartPacket.PartDataHolder data) {
		Vec3 vec = new Vec3(data.x(), data.y(), data.z());
		// CRITICAL: setOldPosAndRot() MUST be called BEFORE setPos()/setRot().
		// Minecraft's render pipeline does a linear interpolation between
		// (xOld,yOld,zOld) [setOldPosAndRot saves the CURRENT x/y/z here]
		// and (x,y,z) [setPos writes the NEW packet x/y/z here] using
		// partialTick. If we call setOldPosAndRot AFTER setPos, then
		// xOld == x, yOld == y, zOld == z, the lerp factor collapses to 0,
		// and every 50ms packet teleports the part instead of smoothly
		// gliding — exactly the "jittery/shaking" bug reported for the
		// Naga body, Hydra neck/head, and Snow Queen ice shields.
		this.setOldPosAndRot();
		this.setPos(vec.x(), vec.y(), vec.z());
		this.setRot(data.yRot(), data.xRot());
		// Reset interpolation counter to prevent tick()'s interpolation logic
		// from overwriting the position set by readData(), which causes jitter
		this.newPosRotationIncrements = 0;
		final float w = data.width();
		final float h = data.height();
		this.setSize(data.fixed() ? EntityDimensions.fixed(w, h) : EntityDimensions.scalable(w, h));
		if (data.data() != null)
			getEntityData().assignValues(data.data());
		this.refreshDimensions();
	}

	public static void assignPartIDs(Entity parent) {
		Entity[] parts = ((TFEntityExtensions) parent).twilightforest$getParts();
		for (int i = 0, partsLength = Objects.requireNonNull(parts).length; i < partsLength; i++) {
			Entity part = parts[i];
			part.setId(parent.getId() + i);
		}
	}
}
