package twilightforest.entity.monster;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gamerules.GameRules;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFSounds;

public class UnstableIceCore extends BaseIceMob {

	private static final float EXPLOSION_RADIUS = 1;

	public UnstableIceCore(EntityType<? extends UnstableIceCore> type, Level world) {
		super(type, world);
	}

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(0, new FloatGoal(this));
		this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.0D, false));
		this.goalSelector.addGoal(2, new WaterAvoidingRandomStrollGoal(this, 1.0D));
		this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 8.0F));
		this.goalSelector.addGoal(3, new RandomLookAroundGoal(this));
		this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
		this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
	}

	public static AttributeSupplier.Builder registerAttributes() {
		return Monster.createMonsterAttributes()
			.add(Attributes.MOVEMENT_SPEED, 0.23D)
			.add(Attributes.ATTACK_DAMAGE, 3.0D);
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return TFSounds.ICE_CORE_AMBIENT;
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource source) {
		return TFSounds.ICE_CORE_HURT;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return TFSounds.ICE_CORE_DEATH;
	}

	@Override
	protected void tickDeath() {
		++this.deathTime;

		if (this.deathTime == 60) { // delay until 3 seconds
			if (this.level() instanceof ServerLevel server) {
				boolean mobGriefing = server.getGameRules().get(GameRules.MOB_GRIEFING);
				this.level().explode(this, this.getX(), this.getY(), this.getZ(), UnstableIceCore.EXPLOSION_RADIUS, Level.ExplosionInteraction.MOB);

				if (mobGriefing) {
					this.transformBlocks();
				}
			}
			// Fake to trigger super's behaviour
			this.deathTime = 19;
			super.tickDeath();
			this.deathTime = 60;
		}
	}

	private void transformBlocks() {
		int range = 4;

		for (int dx = -range; dx <= range; dx++) {
			for (int dy = -range; dy <= range; dy++) {
				for (int dz = -range; dz <= range; dz++) {
					double distance = Math.sqrt(dx * dx + dy * dy + dz * dz);

					float randRange = range + (this.getRandom().nextFloat() - this.getRandom().nextFloat()) * 2.0F;

					if (distance < randRange) {
						this.transformBlock(this.blockPosition().offset(dx, dy, dz));
					}
				}
			}
		}
	}

	private void transformBlock(BlockPos pos) {
		BlockState state = this.level().getBlockState(pos);
		Block block = state.getBlock();

		if (block.getExplosionResistance() < 8F && state.getDestroySpeed(this.level(), pos) >= 0) {
			// do appropriate transformation into aurora-palace themed blocks
			if (this.shouldTransformGlass(state, pos)) {
				this.level().setBlockAndUpdate(pos, TFBlocks.AURORALIZED_GLASS.defaultBlockState());
			} else if (this.shouldTransformClay(state, pos)) {
				// randomly pick between the aurora brick and the aurora pillar
				Block auroraBlock = this.getRandom().nextBoolean() ? TFBlocks.AURORA_BLOCK : TFBlocks.AURORA_PILLAR;
				this.level().setBlockAndUpdate(pos, auroraBlock.defaultBlockState());
			}
		}
	}

	private boolean shouldTransformClay(BlockState state, BlockPos pos) {
		return !state.isAir() && state.isRedstoneConductor(this.level(), pos);
	}

	private boolean shouldTransformGlass(BlockState state, BlockPos pos) {
		return !state.isAir() && isBlockNormalBounds(state, pos) && (!state.isSolid() || state.is(BlockTags.LEAVES) || state.is(Blocks.ICE) || state.is(TFBlocks.AURORA_BLOCK));
	}

	private boolean isBlockNormalBounds(BlockState state, BlockPos pos) {
		return Block.isShapeFullBlock(state.getShape(this.level(), pos));
	}

	@Override
	public int getMaxSpawnClusterSize() {
		return 2;
	}
}
