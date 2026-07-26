package twilightforest.datagen.data.tags;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.entity.EntityTypes;
import net.neoforged.neoforge.common.Tags;
import twilightforest.datagen.data.tags.compat.ModdedEntityTypeTagGenerator;
import twilightforest.init.TFEntities;
import twilightforest.tags.TFEntityTypeTags;

import java.util.concurrent.CompletableFuture;

public class EntityTypeTagGenerator extends ModdedEntityTypeTagGenerator {

	public EntityTypeTagGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> provider) {
		super(output, provider);
	}

	@Override
	protected void addTags(HolderLookup.Provider provider) {
		super.addTags(provider);
		this.tag(EntityTypeTags.SKELETONS).add(TFEntities.SKELETON_DRUID.getKey(), TFEntities.LICH.getKey(), TFEntities.KNIGHT_PHANTOM.getKey());
		this.tag(EntityTypeTags.ZOMBIES).add(TFEntities.LICH_MINION.getKey(), TFEntities.LOYAL_ZOMBIE.getKey(), TFEntities.RISING_ZOMBIE.getKey());
		this.tag(EntityTypeTags.ARROWS).add(TFEntities.ICE_ARROW.getKey(), TFEntities.SEEKER_ARROW.getKey());
		this.tag(EntityTypeTags.FREEZE_HURTS_EXTRA_TYPES).add(TFEntities.FIRE_BEETLE.getKey());
		this.tag(EntityTypeTags.FROG_FOOD).add(TFEntities.MAZE_SLIME.getKey());

		this.tag(TFEntityTypeTags.BOSSES).add(
			TFEntities.NAGA.getKey(),
			TFEntities.LICH.getKey(),
			TFEntities.MINOSHROOM.getKey(),
			TFEntities.HYDRA.getKey(),
			TFEntities.KNIGHT_PHANTOM.getKey(),
			TFEntities.UR_GHAST.getKey(),
			TFEntities.ALPHA_YETI.getKey(),
			TFEntities.SNOW_QUEEN.getKey(),
			TFEntities.PLATEAU_BOSS.getKey()
		);

		this.tag(EntityTypeTags.IMPACT_PROJECTILES).add(
			TFEntities.NATURE_BOLT.getKey(),
			TFEntities.LICH_BOLT.getKey(),
			TFEntities.WAND_BOLT.getKey(),
			TFEntities.LICH_BOMB.getKey(),
			TFEntities.MOONWORM_SHOT.getKey(),
			TFEntities.SLIME_BLOB.getKey(),
			TFEntities.THROWN_WEP.getKey(),
			TFEntities.THROWN_ICE.getKey(),
			TFEntities.FALLING_ICE.getKey(),
			TFEntities.ICE_SNOWBALL.getKey(),
			TFEntities.CHAIN_BLOCK.getKey()
		);

		this.tag(EntityTypeTags.POWDER_SNOW_WALKABLE_MOBS).add(
			TFEntities.PENGUIN.getKey(),
			TFEntities.STABLE_ICE_CORE.getKey(),
			TFEntities.UNSTABLE_ICE_CORE.getKey(),
			TFEntities.SNOW_GUARDIAN.getKey(),
			TFEntities.ICE_CRYSTAL.getKey()
		).add(
			TFEntities.RAVEN.getKey(),
			TFEntities.SQUIRREL.getKey(),
			TFEntities.DWARF_RABBIT.getKey(),
			TFEntities.TINY_BIRD.getKey(),
			TFEntities.KOBOLD.getKey(),
			TFEntities.DEATH_TOME.getKey(),
			TFEntities.MOSQUITO_SWARM.getKey(),
			TFEntities.TOWERWOOD_BORER.getKey()
		);

		this.tag(EntityTypeTags.FREEZE_IMMUNE_ENTITY_TYPES).add(
			TFEntities.PENGUIN.getKey(),
			TFEntities.STABLE_ICE_CORE.getKey(),
			TFEntities.UNSTABLE_ICE_CORE.getKey(),
			TFEntities.SNOW_GUARDIAN.getKey(),
			TFEntities.ICE_CRYSTAL.getKey()
		).add(
			TFEntities.WRAITH.getKey(),
			TFEntities.KNIGHT_PHANTOM.getKey(),
			TFEntities.WINTER_WOLF.getKey(),
			TFEntities.YETI.getKey()
		).addTag(TFEntityTypeTags.BOSSES);

		this.tag(EntityTypeTags.FALL_DAMAGE_IMMUNE).add(
			TFEntities.NAGA.getKey(),
			TFEntities.SQUIRREL.getKey(),
			TFEntities.WRAITH.getKey(),
			TFEntities.CARMINITE_GOLEM.getKey(),
			TFEntities.DEATH_TOME.getKey(),
			TFEntities.UR_GHAST.getKey(),
			TFEntities.CARMINITE_GHASTLING.getKey(),
			TFEntities.KNIGHT_PHANTOM.getKey(),
			TFEntities.SNOW_QUEEN.getKey(),
			TFEntities.PENGUIN.getKey(),
			TFEntities.RAVEN.getKey(),
			TFEntities.SNOW_GUARDIAN.getKey(),
			TFEntities.STABLE_ICE_CORE.getKey(),
			TFEntities.MOSQUITO_SWARM.getKey(),
			TFEntities.UNSTABLE_ICE_CORE.getKey(),
			TFEntities.ICE_CRYSTAL.getKey(),
			TFEntities.CARMINITE_GHASTGUARD.getKey(),
			TFEntities.TINY_BIRD.getKey());

		this.tag(TFEntityTypeTags.LICH_POPPABLES)
			.addTag(EntityTypeTags.SKELETONS)
			.add(EntityTypes.ZOMBIE.builtInRegistryHolder().key(), EntityTypes.ENDERMAN.builtInRegistryHolder().key(), EntityTypes.SPIDER.builtInRegistryHolder().key(), EntityTypes.CREEPER.builtInRegistryHolder().key(), TFEntities.SWARM_SPIDER.getKey())
			.remove(Tags.EntityTypes.BOSSES);

		this.tag(TFEntityTypeTags.LIFEDRAIN_DROPS_NO_FLESH).addTag(EntityTypeTags.SKELETONS).addTag(EntityTypeTags.FROG_FOOD).add(
			EntityTypes.BLAZE.builtInRegistryHolder().key(),
			EntityTypes.BREEZE.builtInRegistryHolder().key(),
			EntityTypes.IRON_GOLEM.builtInRegistryHolder().key(),
			EntityTypes.PHANTOM.builtInRegistryHolder().key(),
			EntityTypes.SHULKER.builtInRegistryHolder().key(),
			EntityTypes.SKELETON_HORSE.builtInRegistryHolder().key(),
			EntityTypes.SNOW_GOLEM.builtInRegistryHolder().key(),
			EntityTypes.VEX.builtInRegistryHolder().key(),
			EntityTypes.WITHER.builtInRegistryHolder().key(),
			TFEntities.CARMINITE_GOLEM.getKey(),
			TFEntities.DEATH_TOME.getKey(),
			TFEntities.ICE_CRYSTAL.getKey(),
			TFEntities.KNIGHT_PHANTOM.getKey(),
			TFEntities.LICH.getKey(),
			TFEntities.MOSQUITO_SWARM.getKey(),
			TFEntities.SNOW_GUARDIAN.getKey(),
			TFEntities.STABLE_ICE_CORE.getKey(),
			TFEntities.UNSTABLE_ICE_CORE.getKey(),
			TFEntities.WRAITH.getKey());

		// These entities forcefully take players from the entity they're riding
		this.tag(TFEntityTypeTags.RIDES_OBSTRUCT_SNATCHING).add(TFEntities.PINCH_BEETLE.getKey(), TFEntities.YETI.getKey(), TFEntities.ALPHA_YETI.getKey());

		this.tag(TFEntityTypeTags.DONT_KILL_BUGS).add(TFEntities.MOONWORM_SHOT.getKey());

		this.tag(TFEntityTypeTags.SORTABLE_ENTITIES).add(
			EntityTypes.CHEST_MINECART.builtInRegistryHolder().key(),
			EntityTypes.HOPPER_MINECART.builtInRegistryHolder().key(),
			EntityTypes.LLAMA.builtInRegistryHolder().key(),
			EntityTypes.TRADER_LLAMA.builtInRegistryHolder().key(),
			EntityTypes.DONKEY.builtInRegistryHolder().key(),
			EntityTypes.MULE.builtInRegistryHolder().key());

		this.tag(TFEntityTypeTags.MULTIPLAYER_INCLUSIVE_ENTITIES).add(
			TFEntities.NAGA.getKey(),
			TFEntities.LICH.getKey(),
			TFEntities.MINOSHROOM.getKey(),
			TFEntities.HYDRA.getKey(),
			TFEntities.UR_GHAST.getKey(),
			TFEntities.ALPHA_YETI.getKey(),
			TFEntities.SNOW_QUEEN.getKey(),
			TFEntities.PLATEAU_BOSS.getKey()
		);

		this.tag(Tags.EntityTypes.BOSSES).addTag(TFEntityTypeTags.BOSSES);
		this.tag(EntityTypeTags.ARTHROPOD).add(
			TFEntities.CARMINITE_BROODLING.getKey(),
			TFEntities.FIRE_BEETLE.getKey(),
			TFEntities.HEDGE_SPIDER.getKey(),
			TFEntities.HELMET_CRAB.getKey(),
			TFEntities.KING_SPIDER.getKey(),
			TFEntities.PINCH_BEETLE.getKey(),
			TFEntities.SLIME_BEETLE.getKey(),
			TFEntities.SWARM_SPIDER.getKey(),
			TFEntities.TOWERWOOD_BORER.getKey());
		this.tag(EntityTypeTags.UNDEAD).add(TFEntities.WRAITH.getKey());
		this.tag(EntityTypeTags.IMMUNE_TO_OOZING).add(TFEntities.MAZE_SLIME.getKey());
		this.tag(EntityTypeTags.IMMUNE_TO_INFESTED).add(TFEntities.TOWERWOOD_BORER.getKey());
		this.tag(EntityTypeTags.REDIRECTABLE_PROJECTILE).add(TFEntities.HYDRA_MORTAR.getKey(), TFEntities.LICH_BOLT.getKey());
		this.tag(TFEntityTypeTags.LICH_DEFLECTS_PHASE_2).add(TFEntities.WAND_BOLT.getKey(), TFEntities.LICH_BOLT.getKey(), TFEntities.LICH_BOMB.getKey());
	}


	@Override
	public String getName() {
		return "Twilight Forest Entity Tags";
	}
}
