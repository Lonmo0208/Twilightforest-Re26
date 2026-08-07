package twilightforest.events;

import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

/**
 * Fabric-compatible event stub classes to replace Neoforge events.
 * These are placeholder classes that will be wired to Fabric callbacks later.
 */
public class FabricEvents {

	// Base class for cancelable events
	public static class CancelableEvent {
		private boolean canceled = false;
		public boolean isCanceled() { return canceled; }
		public void setCanceled(boolean canceled) { this.canceled = canceled; }
	}

	public static class PlayerInteractEvent {
		private final Player player;
		private final InteractionHand hand;
		private final BlockPos pos;
		private final BlockHitResult hitVec;
		private InteractionResult cancellationResult = InteractionResult.PASS;

		public PlayerInteractEvent(Player player, InteractionHand hand, BlockPos pos, BlockHitResult hitVec) {
			this.player = player;
			this.hand = hand;
			this.pos = pos;
			this.hitVec = hitVec;
		}

		public Player getEntity() { return player; }
		public InteractionHand getHand() { return hand; }
		public BlockPos getPos() { return pos; }
		public BlockHitResult getHitVec() { return hitVec; }
		public Level getLevel() { return player.level(); }
		public BlockState getBlockState() { return player.level().getBlockState(pos); }
		public ItemStack getItemStack() { return player.getItemInHand(hand); }
		public InteractionResult getCancellationResult() { return cancellationResult; }
		public void setCancellationResult(InteractionResult result) { this.cancellationResult = result; }
		public boolean isCanceled() { return cancellationResult != InteractionResult.PASS; }
		public void setCanceled(boolean canceled) { if (canceled && this.cancellationResult == InteractionResult.PASS) this.cancellationResult = InteractionResult.FAIL; }

		public static class RightClickBlock extends PlayerInteractEvent {
			public RightClickBlock(Player player, InteractionHand hand, BlockPos pos, BlockHitResult hitVec) {
				super(player, hand, pos, hitVec);
			}
		}

		public static class RightClickItem extends PlayerInteractEvent {
			public RightClickItem(Player player, InteractionHand hand) {
				super(player, hand, BlockPos.ZERO, null);
			}
		}

		public static class LeftClickEmpty extends PlayerInteractEvent {
			public LeftClickEmpty(Player player) {
				super(player, InteractionHand.MAIN_HAND, BlockPos.ZERO, null);
			}
		}
	}

	public static class PlayerTickEvent {
		private final Player player;

		public PlayerTickEvent(Player player) {
			this.player = player;
		}

		public Player getEntity() { return player; }

		public static class Pre extends PlayerTickEvent {
			public Pre(Player player) { super(player); }
		}

		public static class Post extends PlayerTickEvent {
			public Post(Player player) { super(player); }
		}
	}

	public static class EntityTickEvent {
		private final Entity entity;

		public EntityTickEvent(Entity entity) {
			this.entity = entity;
		}

		public Entity getEntity() { return entity; }

		public static class Post extends EntityTickEvent {
			public Post(Entity entity) { super(entity); }
		}
	}

	public static class LivingDamageEvent {
		private final LivingEntity entity;
		private final DamageSource source;
		private final float amount;

		public LivingDamageEvent(LivingEntity entity, DamageSource source, float amount) {
			this.entity = entity;
			this.source = source;
			this.amount = amount;
		}

		public LivingEntity getEntity() { return entity; }
		public DamageSource getSource() { return source; }
		public float getAmount() { return amount; }
		public boolean isCanceled() { return false; }
		public void setCanceled(boolean canceled) {}

			public float getOriginalDamage() { return amount; }

		public static class Post extends LivingDamageEvent {
			public Post(LivingEntity entity, DamageSource source, float amount) {
				super(entity, source, amount);
			}
		}
	}

	public static class GrindstoneEvent {
		private final ItemStack top;
		private final ItemStack bottom;
		private final Player player;

		public GrindstoneEvent(ItemStack top, ItemStack bottom, Player player) {
			this.top = top;
			this.bottom = bottom;
			this.player = player;
		}

		public ItemStack getTopItem() { return top; }
		public ItemStack getBottomItem() { return bottom; }
		public Player getPlayer() { return player; }
		public boolean isCanceled() { return false; }
		public void setCanceled(boolean canceled) {}

		public static class OnPlaceItem extends GrindstoneEvent {
			public OnPlaceItem(ItemStack top, ItemStack bottom, Player player) {
				super(top, bottom, player);
			}

			private ItemStack output = ItemStack.EMPTY;
			public void setOutput(ItemStack output) { this.output = output; }
			public ItemStack getOutput() { return output; }
		}

		public static class OnTakeItem extends GrindstoneEvent {
			private final ItemStack result;

			public OnTakeItem(ItemStack top, ItemStack bottom, ItemStack result, Player player) {
				super(top, bottom, player);
				this.result = result;
			}

			public ItemStack getResult() { return result; }
		}
	}

	public static class AdvancementEvent {
		private final AdvancementHolder advancement;
		private final Player player;

		public AdvancementEvent(AdvancementHolder advancement, Player player) {
			this.advancement = advancement;
			this.player = player;
		}

		public AdvancementHolder getAdvancement() { return advancement; }
		public Player getEntity() { return player; }
		public boolean isCanceled() { return false; }
		public void setCanceled(boolean canceled) {}

		public static class AdvancementEarnEvent extends AdvancementEvent {
			public AdvancementEarnEvent(AdvancementHolder advancement, Player player) {
				super(advancement, player);
			}
		}
	}

	public static class LivingEvent {
		private final LivingEntity entity;

		public LivingEvent(LivingEntity entity) {
			this.entity = entity;
		}

		public LivingEntity getEntity() { return entity; }
		public boolean isCanceled() { return false; }
		public void setCanceled(boolean canceled) {}

		public static class LivingJumpEvent extends LivingEvent {
			public LivingJumpEvent(LivingEntity entity) { super(entity); }
		}
	}

	public static class PlayerEvent {
		private final Player player;

		public PlayerEvent(Player player) {
			this.player = player;
		}

		public Player getEntity() { return player; }
		public boolean isCanceled() { return false; }
		public void setCanceled(boolean canceled) {}

		public static class PlayerLoggedInEvent extends PlayerEvent {
			public PlayerLoggedInEvent(Player player) { super(player); }
		}

		public static class PlayerRespawnEvent extends PlayerEvent {
			private final boolean conqueredEnd;
			public PlayerRespawnEvent(Player player, boolean conqueredEnd) {
				super(player);
				this.conqueredEnd = conqueredEnd;
			}
			public boolean isEndConquered() { return conqueredEnd; }
		}

		public static class ItemCraftedEvent extends PlayerEvent {
			private final ItemStack crafting;
			private final net.minecraft.world.Container inventory;
			public ItemCraftedEvent(Player player, ItemStack crafting) {
				super(player);
				this.crafting = crafting;
				this.inventory = null;
			}
			public ItemStack getCrafting() { return crafting; }
			public net.minecraft.world.Container getInventory() { return inventory; }
		}

		public static class Clone extends PlayerEvent {
			private final Player original;
			private final boolean wasDeath;
			public Clone(Player player, Player original, boolean wasDeath) {
				super(player);
				this.original = original;
				this.wasDeath = wasDeath;
			}
			public Player getOriginal() { return original; }
			public boolean isWasDeath() { return wasDeath; }
		}
	}

	public static class LivingIncomingDamageEvent {
		private final LivingEntity entity;
		private final DamageSource source;
		private float amount;
		private boolean canceled;

		public LivingIncomingDamageEvent(LivingEntity entity, DamageSource source, float amount) {
			this.entity = entity;
			this.source = source;
			this.amount = amount;
		}

		public LivingEntity getEntity() { return entity; }
		public DamageSource getSource() { return source; }
		public float getAmount() { return amount; }
		public void setAmount(float amount) { this.amount = amount; }
		public boolean isCanceled() { return canceled; }
		public void setCanceled(boolean canceled) { this.canceled = canceled; }
	}

	public static class LivingDeathEvent {
		private final LivingEntity entity;
		private final DamageSource source;

		public LivingDeathEvent(LivingEntity entity, DamageSource source) {
			this.entity = entity;
			this.source = source;
		}

		public LivingEntity getEntity() { return entity; }
		public DamageSource getSource() { return source; }
		public boolean isCanceled() { return false; }
		public void setCanceled(boolean canceled) {}
	}

	public static class BreakBlockEvent {
		private final Level level;
		private final BlockPos pos;
		private final BlockState state;
		private final Player player;
		private boolean canceled;

		public BreakBlockEvent(Level level, BlockPos pos, BlockState state, Player player) {
			this.level = level;
			this.pos = pos;
			this.state = state;
			this.player = player;
		}

		public Level getLevel() { return level; }
		public BlockPos getPos() { return pos; }
		public BlockState getState() { return state; }
		public Player getPlayer() { return player; }
		public boolean isCanceled() { return canceled; }
		public void setCanceled(boolean canceled) { this.canceled = canceled; }
	}

	public static class ProjectileImpactEvent {
		private final Entity projectile;
		private final net.minecraft.world.phys.HitResult hitResult;

		public ProjectileImpactEvent(Entity projectile, net.minecraft.world.phys.HitResult hitResult) {
			this.projectile = projectile;
			this.hitResult = hitResult;
		}

		public Entity getProjectile() { return projectile; }
		public net.minecraft.world.phys.HitResult getRayTraceResult() { return hitResult; }
		public boolean isCanceled() { return false; }
		public void setCanceled(boolean canceled) {}
	}

	public static class AttackEntityEvent {
		private final Player player;
		private final Entity target;

		public AttackEntityEvent(Player player, Entity target) {
			this.player = player;
			this.target = target;
		}

		public Player getEntity() { return player; }
		public Entity getTarget() { return target; }
		public boolean isCanceled() { return false; }
		public void setCanceled(boolean canceled) {}
	}

	public static class FinalizeSpawnEvent {
		private final LivingEntity entity;
		private final Level level;
		private final double x, y, z;
		private final net.minecraft.world.DifficultyInstance difficulty;

		public FinalizeSpawnEvent(LivingEntity entity, Level level, double x, double y, double z) {
			this.entity = entity;
			this.level = level;
			this.x = x;
			this.y = y;
			this.z = z;
			this.difficulty = level instanceof net.minecraft.server.level.ServerLevel sl ? sl.getCurrentDifficultyAt(entity.blockPosition()) : new net.minecraft.world.DifficultyInstance(net.minecraft.world.Difficulty.NORMAL, 0L, 0L, 0.0F);
		}

		public LivingEntity getEntity() { return entity; }
		public Level getLevel() { return level; }
		public double getX() { return x; }
		public double getY() { return y; }
		public double getZ() { return z; }
		public net.minecraft.world.DifficultyInstance getDifficulty() { return difficulty; }
		public boolean isCanceled() { return false; }
		public void setCanceled(boolean canceled) {}
	}

	public static class PlayerSpawnPhantomsEvent {
		private final Player player;
		private int phantomsToSpawn;
		private Result result = Result.DEFAULT;

		public PlayerSpawnPhantomsEvent(Player player, int phantomsToSpawn) {
			this.player = player;
			this.phantomsToSpawn = phantomsToSpawn;
		}

		public Player getEntity() { return player; }
		public int getPhantomsToSpawn() { return phantomsToSpawn; }
		public void setPhantomsToSpawn(int phantomsToSpawn) { this.phantomsToSpawn = phantomsToSpawn; }
		public Result getResult() { return result; }
		public void setResult(Result result) { this.result = result; }
		public boolean isCanceled() { return result == Result.DENY; }
		public void setCanceled(boolean canceled) { this.result = canceled ? Result.DENY : Result.DEFAULT; }

		public enum Result {
			DEFAULT,
			ALLOW,
			DENY
		}
	}

	public static class OnDatapackSyncEvent {
		private final Player player;
		private final net.minecraft.server.players.PlayerList playerList;

		public OnDatapackSyncEvent(Player player) {
			this.player = player;
			this.playerList = null;
		}

		public OnDatapackSyncEvent(net.minecraft.server.players.PlayerList playerList) {
			this.player = null;
			this.playerList = playerList;
		}

		@Nullable
		public Player getPlayer() { return player; }
		public net.minecraft.server.players.PlayerList getPlayerList() { return playerList; }
		public boolean isCanceled() { return false; }
		public void setCanceled(boolean canceled) {}
	}

	public static class EntityJoinLevelEvent {
		private final Entity entity;
		private final Level level;

		public EntityJoinLevelEvent(Entity entity, Level level) {
			this.entity = entity;
			this.level = level;
		}

		public Entity getEntity() { return entity; }
		public Level getLevel() { return level; }
		public boolean isCanceled() { return false; }
		public void setCanceled(boolean canceled) {}
	}

	public static class EntityTeleportEvent {
		private final LivingEntity entity;
		private double targetX, targetY, targetZ;

		public EntityTeleportEvent(LivingEntity entity, double targetX, double targetY, double targetZ) {
			this.entity = entity;
			this.targetX = targetX;
			this.targetY = targetY;
			this.targetZ = targetZ;
		}

		public LivingEntity getEntity() { return entity; }
		public double getTargetX() { return targetX; }
		public double getTargetY() { return targetY; }
		public double getTargetZ() { return targetZ; }
		public void setTargetX(double x) { this.targetX = x; }
		public void setTargetY(double y) { this.targetY = y; }
		public void setTargetZ(double z) { this.targetZ = z; }
		public boolean isCanceled() { return false; }
		public void setCanceled(boolean canceled) {}
	}

	public static class EntityMountEvent {
		private final Entity entityMounting;
		private final Entity entityBeingMounted;
		private final Level level;
		private final boolean isMounting;

		public EntityMountEvent(Entity entityMounting, Entity entityBeingMounted, Level level, boolean isMounting) {
			this.entityMounting = entityMounting;
			this.entityBeingMounted = entityBeingMounted;
			this.level = level;
			this.isMounting = isMounting;
		}

		public Entity getEntityMounting() { return entityMounting; }
		public Entity getEntityBeingMounted() { return entityBeingMounted; }
		public Level getLevel() { return level; }
		public boolean isMounting() { return isMounting; }
		public boolean isDismounting() { return !isMounting; }
		public boolean isCanceled() { return false; }
		public void setCanceled(boolean canceled) {}
	}

	public static class LivingEquipmentChangeEvent {
		private final LivingEntity entity;
		private final net.minecraft.world.entity.EquipmentSlot slot;
		private final ItemStack from;
		private final ItemStack to;

		public LivingEquipmentChangeEvent(LivingEntity entity, net.minecraft.world.entity.EquipmentSlot slot, ItemStack from, ItemStack to) {
			this.entity = entity;
			this.slot = slot;
			this.from = from;
			this.to = to;
		}

		public LivingEntity getEntity() { return entity; }
		public net.minecraft.world.entity.EquipmentSlot getSlot() { return slot; }
		public ItemStack getFrom() { return from; }
		public ItemStack getTo() { return to; }
		public boolean isCanceled() { return false; }
		public void setCanceled(boolean canceled) {}
	}

	public static class LivingFallEvent {
		private final LivingEntity entity;
		private float distance;
		private float damageMultiplier;

		public LivingFallEvent(LivingEntity entity, float distance, float damageMultiplier) {
			this.entity = entity;
			this.distance = distance;
			this.damageMultiplier = damageMultiplier;
		}

		public LivingEntity getEntity() { return entity; }
		public float getDistance() { return distance; }
		public void setDistance(float distance) { this.distance = distance; }
		public float getDamageMultiplier() { return damageMultiplier; }
		public void setDamageMultiplier(float multiplier) { this.damageMultiplier = multiplier; }
		public boolean isCanceled() { return false; }
		public void setCanceled(boolean canceled) {}
	}

	public static class ItemAttributeModifierEvent {
		private final ItemStack itemStack;
		private final java.util.List<net.minecraft.world.item.component.ItemAttributeModifiers.Entry> modifiers = new java.util.ArrayList<>();

		public ItemAttributeModifierEvent(ItemStack itemStack) {
			this.itemStack = itemStack;
		}

		public ItemStack getItemStack() { return itemStack; }
		public boolean isCanceled() { return false; }
		public void setCanceled(boolean canceled) {}
		public void clearModifiers() { this.modifiers.clear(); }
		public void replaceModifier(net.minecraft.core.Holder<net.minecraft.world.entity.ai.attributes.Attribute> attribute, net.minecraft.world.entity.ai.attributes.AttributeModifier modifier, net.minecraft.world.entity.EquipmentSlotGroup slot) {
			this.modifiers.add(new net.minecraft.world.item.component.ItemAttributeModifiers.Entry(attribute, modifier, slot));
		}
		public net.minecraft.world.item.component.ItemAttributeModifiers build() {
			return new net.minecraft.world.item.component.ItemAttributeModifiers(java.util.List.copyOf(this.modifiers));
		}
	}

	public static class ArmorHurtEvent {
		private final LivingEntity entity;
		private final DamageSource source;
		private final float amount;
		private final java.util.EnumMap<EquipmentSlot, Integer> damageMap = new java.util.EnumMap<>(EquipmentSlot.class);

		public ArmorHurtEvent(LivingEntity entity, DamageSource source, float amount) {
			this.entity = entity;
			this.source = source;
			this.amount = amount;
		}

		public LivingEntity getEntity() { return entity; }
		public DamageSource getSource() { return source; }
		public float getAmount() { return amount; }
		public boolean isCanceled() { return false; }
		public void setCanceled(boolean canceled) {}
		public java.util.Map<EquipmentSlot, Integer> getArmorMap() { return damageMap; }
		public ItemStack getArmorItemStack(EquipmentSlot slot) { return entity.getItemBySlot(slot); }
		public int getNewDamage(EquipmentSlot slot) { return damageMap.getOrDefault(slot, 0); }
		public void setNewDamage(EquipmentSlot slot, int damage) { damageMap.put(slot, damage); }
	}

	public static class AnvilUpdateEvent {
		private final ItemStack left;
		private final ItemStack right;
		private final ItemStack output;
		private final int cost;
		private final int materialCost;
		private final Player player;

		public AnvilUpdateEvent(ItemStack left, ItemStack right, ItemStack output, int cost, int materialCost, Player player) {
			this.left = left;
			this.right = right;
			this.output = output;
			this.cost = cost;
			this.materialCost = materialCost;
			this.player = player;
		}

		public ItemStack getLeft() { return left; }
		public ItemStack getRight() { return right; }
		public ItemStack getOutput() { return output; }
		public int getCost() { return cost; }
		public int getMaterialCost() { return materialCost; }
		public Player getPlayer() { return player; }
		public boolean isCanceled() { return false; }
		public void setCanceled(boolean canceled) {}
	}

	public static class TagsUpdatedEvent {
		private final net.minecraft.core.RegistryAccess registryAccess;
		private final boolean fromClientPacket;

		public TagsUpdatedEvent(net.minecraft.core.RegistryAccess registryAccess, boolean fromClientPacket) {
			this.registryAccess = registryAccess;
			this.fromClientPacket = fromClientPacket;
		}

		public net.minecraft.core.RegistryAccess getRegistryAccess() { return registryAccess; }
		public boolean isCanceled() { return false; }
		public void setCanceled(boolean canceled) {}
	}

	public static class LevelEvent {
		private final Level level;

		public LevelEvent(Level level) {
			this.level = level;
		}

		public Level getLevel() { return level; }
		public boolean isCanceled() { return false; }
		public void setCanceled(boolean canceled) {}

		public static class PotentialSpawns extends LevelEvent {
			private final net.minecraft.world.entity.MobCategory category;
			private final java.util.List<net.minecraft.world.level.biome.MobSpawnSettings.SpawnerData> spawns;
			private final BlockPos pos;

			public PotentialSpawns(Level level, net.minecraft.world.entity.MobCategory category, java.util.List<net.minecraft.world.level.biome.MobSpawnSettings.SpawnerData> spawns, BlockPos pos) {
				super(level);
				this.category = category;
				this.spawns = spawns;
				this.pos = pos;
			}

			public net.minecraft.world.entity.MobCategory getMobCategory() { return category; }
			public java.util.List<net.minecraft.world.level.biome.MobSpawnSettings.SpawnerData> getSpawnerDataList() { return spawns; }
			public BlockPos getPos() { return pos; }
			public void addSpawnerData(net.minecraft.world.level.biome.MobSpawnSettings.SpawnerData data) { spawns.add(data); }
			public void removeSpawnerData(net.minecraft.world.level.biome.MobSpawnSettings.SpawnerData data) { spawns.remove(data); }
			public boolean isCanceled() { return false; }
			public void setCanceled(boolean canceled) {}
		}
	}

	public static class ExplosionEvent {
		private final Level level;
		private final net.minecraft.world.level.Explosion explosion;

		public ExplosionEvent(Level level, net.minecraft.world.level.Explosion explosion) {
			this.level = level;
			this.explosion = explosion;
		}

		public Level getLevel() { return level; }
		public net.minecraft.world.level.Explosion getExplosion() { return explosion; }
		public boolean isCanceled() { return false; }
		public void setCanceled(boolean canceled) {}

		public static class Detonate extends ExplosionEvent {
			private final java.util.List<Entity> affectedEntities;

			public Detonate(Level level, net.minecraft.world.level.Explosion explosion, java.util.List<Entity> affectedEntities) {
				super(level, explosion);
				this.affectedEntities = affectedEntities;
			}

			public java.util.List<Entity> getAffectedEntities() { return affectedEntities; }
			public boolean isCanceled() { return false; }
			public void setCanceled(boolean canceled) {}
		}
	}

	public static class BlockEvent {
		private final Level level;
		private final BlockPos pos;
		private final BlockState state;

		public BlockEvent(Level level, BlockPos pos, BlockState state) {
			this.level = level;
			this.pos = pos;
			this.state = state;
		}

		public Level getLevel() { return level; }
		public BlockPos getPos() { return pos; }
		public BlockState getState() { return state; }
		public boolean isCanceled() { return false; }
		public void setCanceled(boolean canceled) {}

		public static class EntityMultiPlaceEvent extends BlockEvent {
			private final java.util.List<BlockState> states;
			private final java.util.List<BlockPos> positions;
			private final Entity entity;

			public EntityMultiPlaceEvent(java.util.List<BlockState> states, java.util.List<BlockPos> positions, BlockState state, Level level, BlockPos pos, Entity entity) {
				super(level, pos, state);
				this.states = states;
				this.positions = positions;
				this.entity = entity;
			}

			public java.util.List<BlockState> getReplacedBlockStates() { return states; }
			public java.util.List<BlockPos> getReplacedBlockPositions() { return positions; }
			public Entity getEntity() { return entity; }
			public boolean isCanceled() { return false; }
			public void setCanceled(boolean canceled) {}

			// BlockSnapshot is a NeoForge-only class, not available in Fabric
			// public java.util.List<...> getReplacedBlockSnapshots() { ... }
		}
	}

	public static class MobEffectEvent {
		private final LivingEntity entity;
		private final net.minecraft.world.effect.MobEffectInstance effectInstance;

		public MobEffectEvent(LivingEntity entity, net.minecraft.world.effect.MobEffectInstance effectInstance) {
			this.entity = entity;
			this.effectInstance = effectInstance;
		}

		public LivingEntity getEntity() { return entity; }
		public net.minecraft.world.effect.MobEffectInstance getEffectInstance() { return effectInstance; }
		public boolean isCanceled() { return false; }
		public void setCanceled(boolean canceled) {}

		public static class Applicable extends MobEffectEvent {
			public Applicable(LivingEntity entity, net.minecraft.world.effect.MobEffectInstance effectInstance) {
				super(entity, effectInstance);
			}

			public boolean getResult() { return true; }
			public void setResult(boolean result) {}
		}
	}
}
