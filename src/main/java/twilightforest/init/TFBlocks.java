package twilightforest.init;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.block.entity.BlockEntityTypes;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import twilightforest.TwilightForestMod;
import twilightforest.block.*;
import twilightforest.enums.BlockLoggingEnum;
import twilightforest.enums.BossVariant;
import twilightforest.enums.FireJetVariant;
import twilightforest.enums.TowerDeviceVariant;
import twilightforest.loot.TFLootTables;
import twilightforest.util.woods.TFWoodTypes;
import twilightforest.world.components.feature.trees.growers.TFTreeGrowers;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EquipmentSlot;

public class TFBlocks {

public static final TFPortalBlock TWILIGHT_PORTAL = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("twilight_portal")),
		new TFPortalBlock(BlockBehaviour.Properties.of().pushReaction(PushReaction.BLOCK).strength(-1.0F).sound(SoundType.GLASS).lightLevel((state) -> 11).noCollision().noOcclusion().noLootTable().setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("twilight_portal"))))
	);

	//misc.
public static final Block HEDGE = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("hedge")),
		new HedgeBlock(BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).pushReaction(PushReaction.DESTROY).sound(SoundType.GRASS).strength(2.0F, 6.0F).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("hedge"))))
	);
public static final MasonJarBlock MASON_JAR = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("mason_jar")),
		new MasonJarBlock(BlockBehaviour.Properties.of().noOcclusion().randomTicks().sound(SoundType.DECORATED_POT).strength(0.3F, 3.0F).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("mason_jar"))))
	);
public static final JarBlock FIREFLY_JAR = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("firefly_jar")),
		new FireflyJarBlock(BlockBehaviour.Properties.of().lightLevel((state) -> 15).noOcclusion().sound(SoundType.DECORATED_POT).strength(0.3F, 3.0F).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("firefly_jar"))))
	);
public static final Block FIREFLY_SPAWNER = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("firefly_spawner")),
		new FireflySpawnerBlock(BlockBehaviour.Properties.of().lightLevel((state) -> 15).noOcclusion().sound(SoundType.DECORATED_POT).strength(0.3F, 3.0F).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("firefly_spawner"))))
	);
public static final JarBlock CICADA_JAR = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("cicada_jar")),
		new CicadaJarBlock(BlockBehaviour.Properties.of().noOcclusion().randomTicks().sound(SoundType.DECORATED_POT).strength(0.3F, 3.0F).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("cicada_jar"))))
	);
public static final Block MOSS_PATCH = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("moss_patch")),
		new MossPatchBlock(BlockBehaviour.Properties.of().ignitedByLava().instabreak().mapColor(MapColor.PLANT).noCollision().noOcclusion().pushReaction(PushReaction.DESTROY).sound(SoundType.MOSS).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("moss_patch"))))
	);
public static final Block MAYAPPLE = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("mayapple")),
		new MayappleBlock(BlockBehaviour.Properties.of().ignitedByLava().instabreak().mapColor(MapColor.PLANT).noCollision().noOcclusion().pushReaction(PushReaction.DESTROY).sound(SoundType.GRASS).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("mayapple"))))
	);
public static final Block CLOVER_PATCH = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("clover_patch")),
		new PatchBlock(BlockBehaviour.Properties.of().ignitedByLava().noCollision().noOcclusion().instabreak().mapColor(MapColor.PLANT).pushReaction(PushReaction.DESTROY).sound(SoundType.GRASS).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("clover_patch"))))
	);
public static final Block FIDDLEHEAD = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("fiddlehead")),
		new FiddleheadBlock(BlockBehaviour.Properties.of().ignitedByLava().instabreak().mapColor(MapColor.PLANT).noCollision().noOcclusion().pushReaction(PushReaction.DESTROY).replaceable().sound(SoundType.GRASS).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("fiddlehead"))))
	);
public static final Block MUSHGLOOM = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("mushgloom")),
		new MushgloomBlock(BlockBehaviour.Properties.of().instabreak().lightLevel((state) -> 3).noCollision().noOcclusion().mapColor(MapColor.PLANT).pushReaction(PushReaction.DESTROY).sound(SoundType.FUNGUS).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("mushgloom"))))
	);
public static final Block TORCHBERRY_PLANT = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("torchberry_plant")),
		new TorchberryPlantBlock(BlockBehaviour.Properties.of().ignitedByLava().instabreak().lightLevel(value -> value.getValue(TorchberryPlantBlock.HAS_BERRIES) ? 7 : 1).noCollision().mapColor(MapColor.PLANT).pushReaction(PushReaction.DESTROY).sound(SoundType.HANGING_ROOTS).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("torchberry_plant"))))
	);
public static final Block ROOT_STRAND = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("root_strand")),
		new RootStrandBlock(BlockBehaviour.Properties.of().ignitedByLava().instabreak().mapColor(MapColor.PLANT).noCollision().noOcclusion().pushReaction(PushReaction.DESTROY).sound(SoundType.HANGING_ROOTS).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("root_strand"))))
	);
public static final Block FALLEN_LEAVES = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("fallen_leaves")),
		new FallenLeavesBlock(BlockBehaviour.Properties.of().ignitedByLava().instabreak().mapColor(MapColor.PLANT).noCollision().noOcclusion().replaceable().pushReaction(PushReaction.DESTROY).sound(SoundType.AZALEA_LEAVES).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("fallen_leaves"))))
	);
public static final Block ROOT_BLOCK = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("root_block")),
		new Block(BlockBehaviour.Properties.of().ignitedByLava().mapColor(MapColor.WOOD).sound(SoundType.WOOD).strength(2.0F, 3.0F).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("root_block"))))
	);
public static final Block LIVEROOT_BLOCK = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("liveroot_block")),
		new LiverootBlock(BlockBehaviour.Properties.of().ignitedByLava().mapColor(MapColor.COLOR_LIGHT_GREEN).sound(SoundType.WOOD).strength(2.0F, 3.0F).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("liveroot_block"))))
	);
public static final Block UNCRAFTING_TABLE = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("uncrafting_table")),
		new UncraftingTableBlock(BlockBehaviour.Properties.of().ignitedByLava().mapColor(MapColor.FIRE).sound(SoundType.WOOD).strength(2.5F).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("uncrafting_table"))))
	);
public static final Block SMOKER = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("smoker")),
		new TFSmokerBlock(BlockBehaviour.Properties.of().mapColor(MapColor.GRASS).sound(SoundType.GRASS).strength(1.5F, 6.0F).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("smoker"))))
	);
public static final Block ENCASED_SMOKER = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("encased_smoker")),
		new EncasedSmokerBlock(BlockBehaviour.Properties.of().ignitedByLava().mapColor(MapColor.SAND).requiresCorrectToolForDrops().sound(SoundType.WOOD).strength(1.5F, 6.0F).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("encased_smoker"))))
	);
public static final Block FIRE_JET = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("fire_jet")),
		new FireJetBlock(BlockBehaviour.Properties.of().lightLevel((state) -> state.getValue(FireJetBlock.STATE) != FireJetVariant.FLAME ? 0 : 15).mapColor(MapColor.GRASS).randomTicks().sound(SoundType.GRASS).strength(1.5F, 6.0F).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("fire_jet"))))
	);
public static final Block ENCASED_FIRE_JET = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("encased_fire_jet")),
		new EncasedFireJetBlock(BlockBehaviour.Properties.of().ignitedByLava().lightLevel((state) -> state.getValue(FireJetBlock.STATE) != FireJetVariant.FLAME ? 0 : 15).mapColor(MapColor.SAND).requiresCorrectToolForDrops().sound(SoundType.WOOD).strength(1.5F, 6.0F).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("encased_fire_jet"))))
	);
public static final Block FIREFLY = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("firefly")),
		new FireflyBlock(BlockBehaviour.Properties.of().instabreak().lightLevel((state) -> 15).noCollision().pushReaction(PushReaction.DESTROY).sound(SoundType.SLIME_BLOCK).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("firefly"))))
	);
public static final Block CICADA = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("cicada")),
		new CicadaBlock(BlockBehaviour.Properties.of().instabreak().noCollision().pushReaction(PushReaction.DESTROY).sound(SoundType.SLIME_BLOCK).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("cicada"))))
	);
public static final Block MOONWORM = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("moonworm")),
		new MoonwormBlock(BlockBehaviour.Properties.of().forceSolidOff().instabreak().lightLevel((state) -> 14).noCollision().pushReaction(PushReaction.DESTROY).sound(SoundType.SLIME_BLOCK).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("moonworm"))))
	);
public static final Block HUGE_LILY_PAD = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("huge_lily_pad")),
		new HugeLilyPadBlock(BlockBehaviour.Properties.of().instabreak().mapColor(MapColor.PLANT).pushReaction(PushReaction.DESTROY).sound(SoundType.LILY_PAD).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("huge_lily_pad"))))
	);
public static final Block HUGE_WATER_LILY = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("huge_water_lily")),
		new HugeWaterLilyBlock(BlockBehaviour.Properties.of().instabreak().mapColor(MapColor.PLANT).noCollision().pushReaction(PushReaction.DESTROY).sound(SoundType.LILY_PAD).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("huge_water_lily"))))
	);
public static final Block SLIDER = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("slider")),
		new SliderBlock(BlockBehaviour.Properties.of().mapColor(MapColor.DIRT).noLootTable().noOcclusion().randomTicks().strength(2.0F, 10.0F).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("slider"))))
	);
public static final Block IRON_LADDER = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("iron_ladder")),
		new IronLadderBlock(BlockBehaviour.Properties.of().forceSolidOff().noOcclusion().pushReaction(PushReaction.DESTROY).requiresCorrectToolForDrops().sound(SoundType.METAL).strength(5.0F, 6.0F).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("iron_ladder"))))
	);
public static final Block ROPE = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("rope")),
		new RopeBlock(BlockBehaviour.Properties.of().forceSolidOff().noOcclusion().pushReaction(PushReaction.DESTROY).sound(SoundType.WOOL).strength(0.3F, 3.0F).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("rope"))))
	);
public static final TransparentBlock CANOPY_WINDOW = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("canopy_window")),
		new TransparentBlock(BlockBehaviour.Properties.of().instrument(NoteBlockInstrument.HAT).strength(0.3F).sound(SoundType.GLASS).noOcclusion().isValidSpawn((pState, pLevel, pPos, pValue) -> false).isRedstoneConductor((pState, pLevel, pPos) -> false).isSuffocating((pState, pLevel, pPos) -> false).isViewBlocking((pState, pLevel, pPos) -> false).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("canopy_window"))))
	);
public static final IronBarsBlock CANOPY_WINDOW_PANE = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("canopy_window_pane")),
		new IronBarsBlock(BlockBehaviour.Properties.of().instrument(NoteBlockInstrument.HAT).strength(0.3F).sound(SoundType.GLASS).noOcclusion().setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("canopy_window_pane"))))
	);
public static final Block SINISTER_SPAWNER = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("sinister_spawner")),
		new SinisterSpawnerBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SPAWNER).noLootTable().setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("sinister_spawner"))))
	);
public static final Block BRAZIER = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("brazier")),
		new BrazierBlock(BlockBehaviour.Properties.of().sound(SoundType.WOOD).lightLevel(state -> state.getValue(BrazierBlock.HALF) == DoubleBlockHalf.UPPER ? state.getValue(BrazierBlock.LIGHT).getLight() : 0).pushReaction(PushReaction.DESTROY).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("brazier"))))
	);

	// bushes
public static final Block IRON_OREBERRY_BUSH = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("iron_oreberry_bush")),
		new OreBerryBushBlock(false, TFLootTables.IRON_OREBERRY_BUSH_DROPS, BlockBehaviour.Properties.of().sound(SoundType.METAL).destroyTime(0.4F).randomTicks().dynamicShape().noOcclusion().setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("iron_oreberry_bush"))))
	);
public static final Block GOLD_OREBERRY_BUSH = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("gold_oreberry_bush")),
		new OreBerryBushBlock(false, TFLootTables.GOLD_OREBERRY_BUSH_DROPS, BlockBehaviour.Properties.of().sound(SoundType.METAL).destroyTime(0.4F).randomTicks().dynamicShape().noOcclusion().setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("gold_oreberry_bush"))))
	);
public static final Block COPPER_OREBERRY_BUSH = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("copper_oreberry_bush")),
		new OreBerryBushBlock(false, TFLootTables.COPPER_OREBERRY_BUSH_DROPS, BlockBehaviour.Properties.of().sound(SoundType.METAL).destroyTime(0.4F).randomTicks().dynamicShape().noOcclusion().setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("copper_oreberry_bush"))))
	);
public static final Block ESSENCE_OREBERRY_BUSH = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("essence_oreberry_bush")),
		new OreBerryBushBlock(true, TFLootTables.ESSENCE_BERRY_BUSH_DROPS, BlockBehaviour.Properties.of().sound(SoundType.METAL).destroyTime(0.4F).randomTicks().dynamicShape().noOcclusion().setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("essence_oreberry_bush"))))
	);
public static final Block RASPBERRY_BUSH = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("raspberry_bush")),
		new BerryBushBlock(TFLootTables.RASPBERRY_BUSH_DROPS, BlockBehaviour.Properties.of().sound(SoundType.GRASS).destroyTime(0.4F).randomTicks().dynamicShape().noOcclusion().setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("raspberry_bush"))))
	);
public static final Block BLUEBERRY_BUSH = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("blueberry_bush")),
		new BerryBushBlock(TFLootTables.BLUEBERRY_BUSH_DROPS, BlockBehaviour.Properties.of().sound(SoundType.GRASS).destroyTime(0.4F).randomTicks().dynamicShape().noOcclusion().setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("blueberry_bush"))))
	);
public static final Block BLACKBERRY_BUSH = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("blackberry_bush")),
		new BerryBushBlock(TFLootTables.BLACKBERRY_BUSH_DROPS, BlockBehaviour.Properties.of().sound(SoundType.GRASS).destroyTime(0.4F).randomTicks().dynamicShape().noOcclusion().setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("blackberry_bush"))))
	);
public static final Block MALOBERRY_BUSH = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("maloberry_bush")),
		new BerryBushBlock(TFLootTables.MALOBERRY_BUSH_DROPS, BlockBehaviour.Properties.of().sound(SoundType.GRASS).destroyTime(0.4F).randomTicks().dynamicShape().noOcclusion().setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("maloberry_bush"))))
	);
public static final Block BLIGHTBERRY_BUSH = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("blightberry_bush")),
		new DarkTowerBerryBushBlock(TFLootTables.BLIGHTBERRY_BUSH_DROPS, BlockBehaviour.Properties.of().sound(SoundType.GRASS).destroyTime(0.4F).randomTicks().dynamicShape().noOcclusion().setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("blightberry_bush"))))
	);
public static final Block DUSKBERRY_BUSH = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("duskberry_bush")),
		new DarkTowerBerryBushBlock(TFLootTables.DUSKBERRY_BUSH_DROPS, BlockBehaviour.Properties.of().sound(SoundType.GRASS).destroyTime(0.4F).randomTicks().dynamicShape().noOcclusion().setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("duskberry_bush"))))
	);
public static final Block SKYBERRY_BUSH = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("skyberry_bush")),
		new DarkTowerBerryBushBlock(TFLootTables.SKYBERRY_BUSH_DROPS, BlockBehaviour.Properties.of().sound(SoundType.GRASS).destroyTime(0.4F).randomTicks().dynamicShape().noOcclusion().setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("skyberry_bush"))))
	);
public static final Block STINGBERRY_BUSH = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("stingberry_bush")),
		new DarkTowerBerryBushBlock(TFLootTables.STINGBERRY_BUSH_DROPS, BlockBehaviour.Properties.of().sound(SoundType.GRASS).destroyTime(0.4F).randomTicks().dynamicShape().noOcclusion().setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("stingberry_bush"))))
	);

	//naga courtyard
public static final Block NAGASTONE_HEAD = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("nagastone_head")),
		new TFHorizontalBlock(BlockBehaviour.Properties.of().instrument(NoteBlockInstrument.BASEDRUM).mapColor(MapColor.STONE).requiresCorrectToolForDrops().sound(SoundType.STONE).strength(1.5F, 6.0F).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("nagastone_head"))))
	);
public static final Block NAGASTONE = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("nagastone")),
		new NagastoneBlock(BlockBehaviour.Properties.of().instrument(NoteBlockInstrument.BASEDRUM).mapColor(MapColor.STONE).requiresCorrectToolForDrops().sound(SoundType.STONE).strength(1.5F, 6.0F).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("nagastone"))))
	);
public static final Block SPIRAL_BRICKS = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("spiral_bricks")),
		new SpiralBrickBlock(BlockBehaviour.Properties.of().instrument(NoteBlockInstrument.BASEDRUM).mapColor(MapColor.STONE).noOcclusion().requiresCorrectToolForDrops().sound(SoundType.STONE).strength(1.5F, 6.0F).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("spiral_bricks"))))
	);
public static final Block ETCHED_NAGASTONE = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("etched_nagastone")),
		new EtchedNagastoneBlock(BlockBehaviour.Properties.of().instrument(NoteBlockInstrument.BASEDRUM).mapColor(MapColor.STONE).requiresCorrectToolForDrops().sound(SoundType.STONE).strength(1.5F, 6.0F).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("etched_nagastone"))))
	);
public static final Block NAGASTONE_PILLAR = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("nagastone_pillar")),
		new DirectionalRotatedPillarBlock(BlockBehaviour.Properties.of().instrument(NoteBlockInstrument.BASEDRUM).mapColor(MapColor.STONE).requiresCorrectToolForDrops().sound(SoundType.STONE).strength(1.5F, 6.0F).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("nagastone_pillar"))))
	);
public static final StairBlock NAGASTONE_STAIRS_LEFT = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("nagastone_stairs_left")),
		new StairBlock(ETCHED_NAGASTONE.defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(ETCHED_NAGASTONE).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("nagastone_stairs_left"))))
	);
public static final StairBlock NAGASTONE_STAIRS_RIGHT = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("nagastone_stairs_right")),
		new StairBlock(ETCHED_NAGASTONE.defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(ETCHED_NAGASTONE).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("nagastone_stairs_right"))))
	);
public static final Block MOSSY_ETCHED_NAGASTONE = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("mossy_etched_nagastone")),
		new EtchedNagastoneBlock(BlockBehaviour.Properties.of().instrument(NoteBlockInstrument.BASEDRUM).mapColor(MapColor.STONE).requiresCorrectToolForDrops().sound(SoundType.STONE).strength(1.5F, 6.0F).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("mossy_etched_nagastone"))))
	);
public static final Block MOSSY_NAGASTONE_PILLAR = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("mossy_nagastone_pillar")),
		new DirectionalRotatedPillarBlock(BlockBehaviour.Properties.of().instrument(NoteBlockInstrument.BASEDRUM).mapColor(MapColor.STONE).requiresCorrectToolForDrops().sound(SoundType.STONE).strength(1.5F, 6.0F).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("mossy_nagastone_pillar"))))
	);
public static final StairBlock MOSSY_NAGASTONE_STAIRS_LEFT = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("mossy_nagastone_stairs_left")),
		new StairBlock(MOSSY_ETCHED_NAGASTONE.defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(MOSSY_ETCHED_NAGASTONE).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("mossy_nagastone_stairs_left"))))
	);
public static final StairBlock MOSSY_NAGASTONE_STAIRS_RIGHT = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("mossy_nagastone_stairs_right")),
		new StairBlock(MOSSY_ETCHED_NAGASTONE.defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(MOSSY_ETCHED_NAGASTONE).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("mossy_nagastone_stairs_right"))))
	);
public static final Block CRACKED_ETCHED_NAGASTONE = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("cracked_etched_nagastone")),
		new EtchedNagastoneBlock(BlockBehaviour.Properties.of().instrument(NoteBlockInstrument.BASEDRUM).mapColor(MapColor.STONE).requiresCorrectToolForDrops().sound(SoundType.STONE).strength(1.5F, 6.0F).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("cracked_etched_nagastone"))))
	);
public static final Block CRACKED_NAGASTONE_PILLAR = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("cracked_nagastone_pillar")),
		new DirectionalRotatedPillarBlock(BlockBehaviour.Properties.of().instrument(NoteBlockInstrument.BASEDRUM).mapColor(MapColor.STONE).requiresCorrectToolForDrops().sound(SoundType.STONE).strength(1.5F, 6.0F).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("cracked_nagastone_pillar"))))
	);
public static final StairBlock CRACKED_NAGASTONE_STAIRS_LEFT = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("cracked_nagastone_stairs_left")),
		new StairBlock(CRACKED_ETCHED_NAGASTONE.defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(CRACKED_ETCHED_NAGASTONE).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("cracked_nagastone_stairs_left"))))
	);
public static final StairBlock CRACKED_NAGASTONE_STAIRS_RIGHT = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("cracked_nagastone_stairs_right")),
		new StairBlock(CRACKED_ETCHED_NAGASTONE.defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(CRACKED_ETCHED_NAGASTONE).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("cracked_nagastone_stairs_right"))))
	);

	//lich tower
public static final RotatedPillarBlock TWISTED_STONE = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("twisted_stone")),
		new RotatedPillarBlock(BlockBehaviour.Properties.of().instrument(NoteBlockInstrument.BASEDRUM).mapColor(MapColor.STONE).requiresCorrectToolForDrops().sound(SoundType.STONE).strength(1.5F, 6.0F).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("twisted_stone"))))
	);
public static final Block TWISTED_STONE_PILLAR = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("twisted_stone_pillar")),
		new WallPillarBlock(12, 16, BlockBehaviour.Properties.of().instrument(NoteBlockInstrument.BASEDRUM).mapColor(MapColor.STONE).noOcclusion().requiresCorrectToolForDrops().sound(SoundType.STONE).strength(1.5F, 6.0F).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("twisted_stone_pillar"))))
	);
public static final Block SKULL_CHEST = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("skull_chest")),
		new SkullChestBlock(BlockBehaviour.Properties.of().lightLevel(state -> state.getValue(BlockLoggingEnum.MULTILOGGED) == BlockLoggingEnum.LAVA ? 15 : 0).mapColor(MapColor.COLOR_LIGHT_GRAY).noOcclusion().pushReaction(PushReaction.BLOCK).requiresCorrectToolForDrops().sound(SoundType.NETHERITE_BLOCK).strength(3.0F, 100.0F).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("skull_chest"))))
	);
public static final Block KEEPSAKE_CASKET = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("keepsake_casket")),
		new KeepsakeCasketBlock(BlockBehaviour.Properties.of().lightLevel(state -> state.getValue(BlockLoggingEnum.MULTILOGGED) == BlockLoggingEnum.LAVA ? 15 : 0).mapColor(MapColor.COLOR_BLACK).noOcclusion().pushReaction(PushReaction.BLOCK).requiresCorrectToolForDrops().sound(SoundType.NETHERITE_BLOCK).strength(5.0F, 1200.0F).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("keepsake_casket"))))
	);
public static final RotatedPillarBlock BOLD_STONE_PILLAR = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("bold_stone_pillar")),
		new RotatedPillarBlock(BlockBehaviour.Properties.of().instrument(NoteBlockInstrument.BASEDRUM).mapColor(MapColor.STONE).requiresCorrectToolForDrops().sound(SoundType.STONE).strength(1.5F, 6.0F).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("bold_stone_pillar"))))
	);
public static final Block CHISELED_CANOPY_BOOKSHELF = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("chiseled_canopy_bookshelf")),
		new ChiseledCanopyShelfBlock(BlockBehaviour.Properties.of().ignitedByLava().instrument(NoteBlockInstrument.BASS).mapColor(MapColor.COLOR_BROWN).sound(SoundType.CHISELED_BOOKSHELF).strength(2.5F).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("chiseled_canopy_bookshelf"))))
	);
public static final Block CANDELABRA = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("candelabra")),
		new CandelabraBlock(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).strength(1.5F).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("candelabra"))))
	);
public static final AbstractSkullCandleBlock ZOMBIE_SKULL_CANDLE = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("zombie_skull_candle")),
		new SkullCandleBlock(SkullBlock.Types.ZOMBIE, BlockBehaviour.Properties.ofFullCopy(Blocks.ZOMBIE_HEAD).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("zombie_skull_candle"))))
	);
public static final AbstractSkullCandleBlock ZOMBIE_WALL_SKULL_CANDLE = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("zombie_wall_skull_candle")),
		new WallSkullCandleBlock(SkullBlock.Types.ZOMBIE, BlockBehaviour.Properties.of().strength(1.0F).pushReaction(PushReaction.DESTROY).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("zombie_wall_skull_candle"))))
	);
public static final AbstractSkullCandleBlock SKELETON_SKULL_CANDLE = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("skeleton_skull_candle")),
		new SkullCandleBlock(SkullBlock.Types.SKELETON, BlockBehaviour.Properties.ofFullCopy(Blocks.SKELETON_SKULL).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("skeleton_skull_candle"))))
	);
public static final AbstractSkullCandleBlock SKELETON_WALL_SKULL_CANDLE = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("skeleton_wall_skull_candle")),
		new WallSkullCandleBlock(SkullBlock.Types.SKELETON, BlockBehaviour.Properties.of().strength(1.0F).pushReaction(PushReaction.DESTROY).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("skeleton_wall_skull_candle"))))
	);
public static final AbstractSkullCandleBlock WITHER_SKELE_SKULL_CANDLE = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("wither_skele_skull_candle")),
		new SkullCandleBlock(SkullBlock.Types.WITHER_SKELETON, BlockBehaviour.Properties.ofFullCopy(Blocks.WITHER_SKELETON_SKULL).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("wither_skele_skull_candle"))))
	);
public static final AbstractSkullCandleBlock WITHER_SKELE_WALL_SKULL_CANDLE = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("wither_skele_wall_skull_candle")),
		new WallSkullCandleBlock(SkullBlock.Types.WITHER_SKELETON, BlockBehaviour.Properties.of().strength(1.0F).pushReaction(PushReaction.DESTROY).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("wither_skele_wall_skull_candle"))))
	);
public static final AbstractSkullCandleBlock CREEPER_SKULL_CANDLE = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("creeper_skull_candle")),
		new SkullCandleBlock(SkullBlock.Types.CREEPER, BlockBehaviour.Properties.ofFullCopy(Blocks.CREEPER_HEAD).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("creeper_skull_candle"))))
	);
public static final AbstractSkullCandleBlock CREEPER_WALL_SKULL_CANDLE = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("creeper_wall_skull_candle")),
		new WallSkullCandleBlock(SkullBlock.Types.CREEPER, BlockBehaviour.Properties.of().strength(1.0F).pushReaction(PushReaction.DESTROY).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("creeper_wall_skull_candle"))))
	);
public static final AbstractSkullCandleBlock PLAYER_SKULL_CANDLE = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("player_skull_candle")),
		new SkullCandleBlock(SkullBlock.Types.PLAYER, BlockBehaviour.Properties.ofFullCopy(Blocks.PLAYER_HEAD).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("player_skull_candle"))))
	);
public static final AbstractSkullCandleBlock PLAYER_WALL_SKULL_CANDLE = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("player_wall_skull_candle")),
		new WallSkullCandleBlock(SkullBlock.Types.PLAYER, BlockBehaviour.Properties.of().strength(1.0F).pushReaction(PushReaction.DESTROY).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("player_wall_skull_candle"))))
	);
public static final AbstractSkullCandleBlock PIGLIN_SKULL_CANDLE = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("piglin_skull_candle")),
		new SkullCandleBlock(SkullBlock.Types.PIGLIN, BlockBehaviour.Properties.ofFullCopy(Blocks.PIGLIN_HEAD).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("piglin_skull_candle"))))
	);
public static final AbstractSkullCandleBlock PIGLIN_WALL_SKULL_CANDLE = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("piglin_wall_skull_candle")),
		new WallSkullCandleBlock(SkullBlock.Types.PIGLIN, BlockBehaviour.Properties.of().strength(1.0F).pushReaction(PushReaction.DESTROY).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("piglin_wall_skull_candle"))))
	);
public static final WroughtIronFenceBlock WROUGHT_IRON_FENCE = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("wrought_iron_fence")),
		new WroughtIronFenceBlock(BlockBehaviour.Properties.of().strength(8.0F, 20.0F).sound(SoundType.METAL).requiresCorrectToolForDrops().noOcclusion().setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("wrought_iron_fence"))))
	);
public static final RotatedPillarBlock TERRORCOTTA_ARCS = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("terrorcotta_arcs")),
		new RotatedPillarBlock(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(1.5F, 6.0F).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("terrorcotta_arcs"))))
	);
public static final GlazedTerracottaBlock TERRORCOTTA_CURVES = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("terrorcotta_curves")),
		new GlazedTerracottaBlock(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(1.5F, 6.0F).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("terrorcotta_curves"))))
	);
public static final BinaryRotatedBlock TERRORCOTTA_LINES = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("terrorcotta_lines")),
		new BinaryRotatedBlock(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(1.5F, 6.0F).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("terrorcotta_lines"))))
	);
public static final CarpetBlock CORONATION_CARPET = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("coronation_carpet")),
		new WoolCarpetBlock(DyeColor.RED, BlockBehaviour.Properties.ofFullCopy(Blocks.CARPET.pick(DyeColor.RED)).isValidSpawn(Blocks::always).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("coronation_carpet"))))
	);

	//ominous
public static final OminousFireBlock OMINOUS_FIRE = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("ominous_fire")),
		new OminousFireBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_PURPLE).replaceable().noCollision().instabreak().lightLevel((state) -> 15).sound(SoundType.WOOL).pushReaction(PushReaction.DESTROY).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("ominous_fire"))))
	);
public static final OminousCandleBlock OMINOUS_CANDLE = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("ominous_candle")),
		new OminousCandleBlock(Blocks.CANDLE, BlockBehaviour.Properties.of().setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("ominous_candle")))
		.mapColor(MapColor.SAND)
		.noOcclusion()
		.strength(0.1F)
		.sound(SoundType.CANDLE)
		.lightLevel(state -> 2 * state.getValue(OminousCandleBlock.CANDLES))
		.pushReaction(PushReaction.DESTROY))
	);
public static final OminousCandleBlock OMINOUS_WHITE_CANDLE = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("ominous_white_candle")),
		new OminousCandleBlock(Blocks.DYED_CANDLE.pick(DyeColor.WHITE), BlockBehaviour.Properties.of().setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("ominous_white_candle")))
		.mapColor(MapColor.WOOL)
		.noOcclusion()
		.strength(0.1F)
		.sound(SoundType.CANDLE)
		.lightLevel(state -> 2 * state.getValue(OminousCandleBlock.CANDLES))
		.pushReaction(PushReaction.DESTROY))
	);
public static final OminousCandleBlock OMINOUS_ORANGE_CANDLE = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("ominous_orange_candle")),
		new OminousCandleBlock(Blocks.DYED_CANDLE.pick(DyeColor.ORANGE), BlockBehaviour.Properties.of().setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("ominous_orange_candle")))
		.mapColor(MapColor.COLOR_ORANGE)
		.noOcclusion()
		.strength(0.1F)
		.sound(SoundType.CANDLE)
		.lightLevel(state -> 2 * state.getValue(OminousCandleBlock.CANDLES))
		.pushReaction(PushReaction.DESTROY))
	);
public static final OminousCandleBlock OMINOUS_MAGENTA_CANDLE = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("ominous_magenta_candle")),
		new OminousCandleBlock(Blocks.DYED_CANDLE.pick(DyeColor.MAGENTA), BlockBehaviour.Properties.of().setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("ominous_magenta_candle")))
		.mapColor(MapColor.COLOR_MAGENTA)
		.noOcclusion()
		.strength(0.1F)
		.sound(SoundType.CANDLE)
		.lightLevel(state -> 2 * state.getValue(OminousCandleBlock.CANDLES))
		.pushReaction(PushReaction.DESTROY))
	);
public static final OminousCandleBlock OMINOUS_LIGHT_BLUE_CANDLE = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("ominous_light_blue_candle")),
		new OminousCandleBlock(Blocks.DYED_CANDLE.pick(DyeColor.LIGHT_BLUE), BlockBehaviour.Properties.of().setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("ominous_light_blue_candle")))
		.mapColor(MapColor.COLOR_LIGHT_BLUE)
		.noOcclusion()
		.strength(0.1F)
		.sound(SoundType.CANDLE)
		.lightLevel(state -> 2 * state.getValue(OminousCandleBlock.CANDLES))
		.pushReaction(PushReaction.DESTROY))
	);
public static final OminousCandleBlock OMINOUS_YELLOW_CANDLE = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("ominous_yellow_candle")),
		new OminousCandleBlock(Blocks.DYED_CANDLE.pick(DyeColor.YELLOW), BlockBehaviour.Properties.of().setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("ominous_yellow_candle")))
		.mapColor(MapColor.COLOR_YELLOW)
		.noOcclusion()
		.strength(0.1F)
		.sound(SoundType.CANDLE)
		.lightLevel(state -> 2 * state.getValue(OminousCandleBlock.CANDLES))
		.pushReaction(PushReaction.DESTROY))
	);
public static final OminousCandleBlock OMINOUS_LIME_CANDLE = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("ominous_lime_candle")),
		new OminousCandleBlock(Blocks.DYED_CANDLE.pick(DyeColor.LIME), BlockBehaviour.Properties.of().setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("ominous_lime_candle")))
		.mapColor(MapColor.COLOR_LIGHT_GREEN)
		.noOcclusion()
		.strength(0.1F)
		.sound(SoundType.CANDLE)
		.lightLevel(state -> 2 * state.getValue(OminousCandleBlock.CANDLES))
		.pushReaction(PushReaction.DESTROY))
	);
public static final OminousCandleBlock OMINOUS_PINK_CANDLE = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("ominous_pink_candle")),
		new OminousCandleBlock(Blocks.DYED_CANDLE.pick(DyeColor.PINK), BlockBehaviour.Properties.of().setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("ominous_pink_candle")))
		.mapColor(MapColor.COLOR_PINK)
		.noOcclusion()
		.strength(0.1F)
		.sound(SoundType.CANDLE)
		.lightLevel(state -> 2 * state.getValue(OminousCandleBlock.CANDLES))
		.pushReaction(PushReaction.DESTROY))
	);
public static final OminousCandleBlock OMINOUS_GRAY_CANDLE = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("ominous_gray_candle")),
		new OminousCandleBlock(Blocks.DYED_CANDLE.pick(DyeColor.GRAY), BlockBehaviour.Properties.of().setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("ominous_gray_candle")))
		.mapColor(MapColor.COLOR_GRAY)
		.noOcclusion()
		.strength(0.1F)
		.sound(SoundType.CANDLE)
		.lightLevel(state -> 2 * state.getValue(OminousCandleBlock.CANDLES))
		.pushReaction(PushReaction.DESTROY))
	);
public static final OminousCandleBlock OMINOUS_LIGHT_GRAY_CANDLE = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("ominous_light_gray_candle")),
		new OminousCandleBlock(Blocks.DYED_CANDLE.pick(DyeColor.LIGHT_GRAY), BlockBehaviour.Properties.of().setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("ominous_light_gray_candle")))
		.mapColor(MapColor.COLOR_LIGHT_GRAY)
		.noOcclusion()
		.strength(0.1F)
		.sound(SoundType.CANDLE)
		.lightLevel(state -> 2 * state.getValue(OminousCandleBlock.CANDLES))
		.pushReaction(PushReaction.DESTROY))
	);
public static final OminousCandleBlock OMINOUS_CYAN_CANDLE = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("ominous_cyan_candle")),
		new OminousCandleBlock(Blocks.DYED_CANDLE.pick(DyeColor.CYAN), BlockBehaviour.Properties.of().setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("ominous_cyan_candle")))
		.mapColor(MapColor.COLOR_CYAN)
		.noOcclusion()
		.strength(0.1F)
		.sound(SoundType.CANDLE)
		.lightLevel(state -> 2 * state.getValue(OminousCandleBlock.CANDLES))
		.pushReaction(PushReaction.DESTROY))
	);
public static final OminousCandleBlock OMINOUS_PURPLE_CANDLE = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("ominous_purple_candle")),
		new OminousCandleBlock(Blocks.DYED_CANDLE.pick(DyeColor.PURPLE), BlockBehaviour.Properties.of().setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("ominous_purple_candle")))
		.mapColor(MapColor.COLOR_PURPLE)
		.noOcclusion()
		.strength(0.1F)
		.sound(SoundType.CANDLE)
		.lightLevel(state -> 2 * state.getValue(OminousCandleBlock.CANDLES))
		.pushReaction(PushReaction.DESTROY))
	);
public static final OminousCandleBlock OMINOUS_BLUE_CANDLE = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("ominous_blue_candle")),
		new OminousCandleBlock(Blocks.DYED_CANDLE.pick(DyeColor.BLUE), BlockBehaviour.Properties.of().setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("ominous_blue_candle")))
		.mapColor(MapColor.COLOR_BLUE)
		.noOcclusion()
		.strength(0.1F)
		.sound(SoundType.CANDLE)
		.lightLevel(state -> 2 * state.getValue(OminousCandleBlock.CANDLES))
		.pushReaction(PushReaction.DESTROY))
	);
public static final OminousCandleBlock OMINOUS_BROWN_CANDLE = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("ominous_brown_candle")),
		new OminousCandleBlock(Blocks.DYED_CANDLE.pick(DyeColor.BROWN), BlockBehaviour.Properties.of().setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("ominous_brown_candle")))
		.mapColor(MapColor.COLOR_BROWN)
		.noOcclusion()
		.strength(0.1F)
		.sound(SoundType.CANDLE)
		.lightLevel(state -> 2 * state.getValue(OminousCandleBlock.CANDLES))
		.pushReaction(PushReaction.DESTROY))
	);
public static final OminousCandleBlock OMINOUS_GREEN_CANDLE = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("ominous_green_candle")),
		new OminousCandleBlock(Blocks.DYED_CANDLE.pick(DyeColor.GREEN), BlockBehaviour.Properties.of().setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("ominous_green_candle")))
		.mapColor(MapColor.COLOR_GREEN)
		.noOcclusion()
		.strength(0.1F)
		.sound(SoundType.CANDLE)
		.lightLevel(state -> 2 * state.getValue(OminousCandleBlock.CANDLES))
		.pushReaction(PushReaction.DESTROY))
	);
public static final OminousCandleBlock OMINOUS_RED_CANDLE = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("ominous_red_candle")),
		new OminousCandleBlock(Blocks.DYED_CANDLE.pick(DyeColor.RED), BlockBehaviour.Properties.of().setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("ominous_red_candle")))
		.mapColor(MapColor.COLOR_RED)
		.noOcclusion()
		.strength(0.1F)
		.sound(SoundType.CANDLE)
		.lightLevel(state -> 2 * state.getValue(OminousCandleBlock.CANDLES))
		.pushReaction(PushReaction.DESTROY))
	);
public static final OminousCandleBlock OMINOUS_BLACK_CANDLE = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("ominous_black_candle")),
		new OminousCandleBlock(Blocks.DYED_CANDLE.pick(DyeColor.BLACK), BlockBehaviour.Properties.of().setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("ominous_black_candle")))
		.mapColor(MapColor.COLOR_BLACK)
		.noOcclusion()
		.strength(0.1F)
		.sound(SoundType.CANDLE)
		.lightLevel(state -> 2 * state.getValue(OminousCandleBlock.CANDLES))
		.pushReaction(PushReaction.DESTROY))
	);

	//labyrinth
public static final Block MAZESTONE = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("mazestone")),
		new Block(BlockBehaviour.Properties.of().instrument(NoteBlockInstrument.BASEDRUM).mapColor(MapColor.STONE).requiresCorrectToolForDrops().sound(SoundType.STONE).strength(100.0F, 5.0F).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("mazestone"))))
	);
public static final Block MAZESTONE_BRICK = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("mazestone_brick")),
		new Block(BlockBehaviour.Properties.ofFullCopy(MAZESTONE).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("mazestone_brick"))))
	);
public static final Block CUT_MAZESTONE = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("cut_mazestone")),
		new Block(BlockBehaviour.Properties.ofFullCopy(MAZESTONE).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("cut_mazestone"))))
	);
public static final Block DECORATIVE_MAZESTONE = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("decorative_mazestone")),
		new Block(BlockBehaviour.Properties.ofFullCopy(MAZESTONE).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("decorative_mazestone"))))
	);
public static final Block CRACKED_MAZESTONE = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("cracked_mazestone")),
		new Block(BlockBehaviour.Properties.ofFullCopy(MAZESTONE).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("cracked_mazestone"))))
	);
public static final Block MOSSY_MAZESTONE = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("mossy_mazestone")),
		new Block(BlockBehaviour.Properties.ofFullCopy(MAZESTONE).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("mossy_mazestone"))))
	);
public static final Block MAZESTONE_MOSAIC = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("mazestone_mosaic")),
		new Block(BlockBehaviour.Properties.ofFullCopy(MAZESTONE).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("mazestone_mosaic"))))
	);
public static final Block MAZESTONE_BORDER = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("mazestone_border")),
		new Block(BlockBehaviour.Properties.ofFullCopy(MAZESTONE).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("mazestone_border"))))
	);
public static final Block RED_THREAD = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("red_thread")),
		new RedThreadBlock(BlockBehaviour.Properties.of().instabreak().mapColor(MapColor.FIRE).isValidSpawn(TFBlocks::noSpawning).noCollision().noOcclusion().pushReaction(PushReaction.DESTROY).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("red_thread"))))
	);
public static final Block MAZE_SLIME_BLOCK = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("maze_slime_block")),
		new MazeSlimeBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SLIME_BLOCK).mapColor(MapColor.STONE).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("maze_slime_block"))))
	);

	//stronghold
public static final Block STRONGHOLD_SHIELD = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("stronghold_shield")),
		new StrongholdShieldBlock(BlockBehaviour.Properties.of().noLootTable().mapColor(MapColor.STONE).pushReaction(PushReaction.BLOCK).requiresCorrectToolForDrops().sound(SoundType.METAL).strength(-1.0F, 6000000.0F).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("stronghold_shield"))))
	);
public static final Block TROPHY_PEDESTAL = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("trophy_pedestal")),
		new TrophyPedestalBlock(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).pushReaction(PushReaction.BLOCK).requiresCorrectToolForDrops().sound(SoundType.STONE).strength(2.0F, 2000.0F).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("trophy_pedestal"))))
	);
public static final Block UNDERBRICK = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("underbrick")),
		new Block(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).requiresCorrectToolForDrops().sound(SoundType.DEEPSLATE_BRICKS).strength(1.5F, 6.0F).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("underbrick"))))
	);
public static final Block MOSSY_UNDERBRICK = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("mossy_underbrick")),
		new Block(BlockBehaviour.Properties.ofFullCopy(UNDERBRICK).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("mossy_underbrick"))))
	);
public static final Block CRACKED_UNDERBRICK = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("cracked_underbrick")),
		new Block(BlockBehaviour.Properties.ofFullCopy(UNDERBRICK).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("cracked_underbrick"))))
	);
public static final Block UNDERBRICK_FLOOR = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("underbrick_floor")),
		new Block(BlockBehaviour.Properties.ofFullCopy(UNDERBRICK).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("underbrick_floor"))))
	);

	//dark tower
public static final Block TOWERWOOD = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("towerwood")),
		new Block(BlockBehaviour.Properties.of().ignitedByLava().instrument(NoteBlockInstrument.BASS).mapColor(MapColor.COLOR_ORANGE).strength(40.0F, 6.0F).sound(SoundType.WOOD).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("towerwood"))))
	);
public static final Block ENCASED_TOWERWOOD = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("encased_towerwood")),
		new Block(BlockBehaviour.Properties.ofFullCopy(TOWERWOOD).mapColor(MapColor.SAND).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("encased_towerwood"))))
	);
public static final Block CRACKED_TOWERWOOD = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("cracked_towerwood")),
		new Block(BlockBehaviour.Properties.ofFullCopy(TOWERWOOD).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("cracked_towerwood"))))
	);
public static final Block MOSSY_TOWERWOOD = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("mossy_towerwood")),
		new Block(BlockBehaviour.Properties.ofFullCopy(TOWERWOOD).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("mossy_towerwood"))))
	);
public static final Block INFESTED_TOWERWOOD = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("infested_towerwood")),
		new InfestedTowerwoodBlock(BlockBehaviour.Properties.ofFullCopy(TOWERWOOD).instrument(NoteBlockInstrument.FLUTE).noLootTable().strength(2.0F, 6.0F).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("infested_towerwood"))))
	);
public static final Block REAPPEARING_BLOCK = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("reappearing_block")),
		new ReappearingBlock(BlockBehaviour.Properties.of().forceSolidOn().lightLevel((state) -> 4).mapColor(MapColor.SAND).pushReaction(PushReaction.BLOCK).requiresCorrectToolForDrops().sound(SoundType.WOOD).strength(10.0F, 35.0F).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("reappearing_block"))))
	);
public static final Block VANISHING_BLOCK = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("vanishing_block")),
		new VanishingBlock(BlockBehaviour.Properties.of().lightLevel((state) -> state.getValue(VanishingBlock.ACTIVE) ? 4 : 0).mapColor(MapColor.SAND).pushReaction(PushReaction.BLOCK).sound(SoundType.WOOD).strength(10.0F, 35.0F).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("vanishing_block"))))
	);
public static final Block UNBREAKABLE_VANISHING_BLOCK = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("unbreakable_vanishing_block")),
		new VanishingBlock(BlockBehaviour.Properties.ofFullCopy(VANISHING_BLOCK).noLootTable().strength(-1.0F, 6000000.0F).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("unbreakable_vanishing_block"))))
	);
public static final Block LOCKED_VANISHING_BLOCK = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("locked_vanishing_block")),
		new LockedVanishingBlock(BlockBehaviour.Properties.of().pushReaction(PushReaction.BLOCK).mapColor(MapColor.SAND).sound(SoundType.WOOD).strength(-1.0F, 2000.0F).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("locked_vanishing_block"))))
	);
public static final Block CARMINITE_BUILDER = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("carminite_builder")),
		new BuilderBlock(BlockBehaviour.Properties.of().lightLevel((state) -> state.getValue(BuilderBlock.STATE) == TowerDeviceVariant.BUILDER_ACTIVE ? 4 : 0).mapColor(MapColor.SAND).pushReaction(PushReaction.BLOCK).requiresCorrectToolForDrops().sound(SoundType.WOOD).strength(10.0F, 6.0F).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("carminite_builder"))))
	);
public static final Block BUILT_BLOCK = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("built_block")),
		new TranslucentBuiltBlock(BlockBehaviour.Properties.of().noLootTable().noOcclusion().pushReaction(PushReaction.BLOCK).sound(SoundType.WOOD).strength(50.0F, 2000.0F).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("built_block"))))
	);
public static final Block ANTIBUILDER = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("antibuilder")),
		new AntibuilderBlock(BlockBehaviour.Properties.of().lightLevel((state) -> 10).noLootTable().pushReaction(PushReaction.BLOCK).mapColor(MapColor.SAND).requiresCorrectToolForDrops().sound(SoundType.WOOD).strength(10.0F, 6.0F).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("antibuilder"))))
	);
public static final Block ANTIBUILT_BLOCK = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("antibuilt_block")),
		new Block(BlockBehaviour.Properties.of().noLootTable().noOcclusion().pushReaction(PushReaction.BLOCK).sound(SoundType.WOOD).strength(0.3F, 2000.0F).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("antibuilt_block"))))
	);
public static final GhastTrapBlock GHAST_TRAP = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("ghast_trap")),
		new GhastTrapBlock(BlockBehaviour.Properties.of().lightLevel((state) -> state.getValue(GhastTrapBlock.ACTIVE) ? 15 : 0).mapColor(MapColor.SAND).pushReaction(PushReaction.BLOCK).requiresCorrectToolForDrops().sound(SoundType.WOOD).strength(10.0F, 6.0F).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("ghast_trap"))))
	);
public static final Block CARMINITE_REACTOR = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("carminite_reactor")),
		new CarminiteReactorBlock(BlockBehaviour.Properties.of().lightLevel((state) -> state.getValue(CarminiteReactorBlock.ACTIVE) ? 15 : 0).mapColor(MapColor.SAND).pushReaction(PushReaction.BLOCK).requiresCorrectToolForDrops().sound(SoundType.WOOD).strength(10.0F, 6.0F).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("carminite_reactor"))))
	);
public static final Block REACTOR_DEBRIS = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("reactor_debris")),
		new ReactorDebrisBlock(BlockBehaviour.Properties.of().noLootTable().noOcclusion().pushReaction(PushReaction.BLOCK).sound(SoundType.ANCIENT_DEBRIS).strength(0.3F, 2000.0F).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("reactor_debris"))))
	);
public static final Block FAKE_GOLD = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("fake_gold")),
		new Block(BlockBehaviour.Properties.of().noLootTable().pushReaction(PushReaction.BLOCK).sound(SoundType.METAL).strength(50.0F, 2000.0F).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("fake_gold"))))
	);
public static final Block FAKE_DIAMOND = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("fake_diamond")),
		new Block(BlockBehaviour.Properties.of().noLootTable().pushReaction(PushReaction.BLOCK).sound(SoundType.METAL).strength(50.0F, 2000.0F).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("fake_diamond"))))
	);
public static final Block EXPERIMENT_115 = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("experiment_115")),
		new Experiment115Block(BlockBehaviour.Properties.of().noLootTable().pushReaction(PushReaction.DESTROY).randomTicks().sound(SoundType.WOOL).strength(0.5F).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("experiment_115"))))
	);

	//aurora palace
public static final Block AURORA_BLOCK = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("aurora_block")),
		new AuroraBrickBlock(BlockBehaviour.Properties.of().instrument(NoteBlockInstrument.CHIME).mapColor(MapColor.ICE).strength(10.0F, 6.0F).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("aurora_block"))))
	);
public static final RotatedPillarBlock AURORA_PILLAR = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("aurora_pillar")),
		new AuroraPillarBlock(BlockBehaviour.Properties.of().instrument(NoteBlockInstrument.CHIME).mapColor(MapColor.ICE).requiresCorrectToolForDrops().strength(2.0F, 6.0F).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("aurora_pillar"))))
	);
public static final Block AURORA_SLAB = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("aurora_slab")),
		new AuroraSlabBlock(BlockBehaviour.Properties.of().instrument(NoteBlockInstrument.CHIME).mapColor(MapColor.ICE).requiresCorrectToolForDrops().strength(2.0F, 6.0F).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("aurora_slab"))))
	);
public static final Block AURORALIZED_GLASS = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("auroralized_glass")),
		new AuroralizedGlassBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("auroralized_glass"))))
	);

	//highlands/thornlands
public static final Block BROWN_THORNS = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("brown_thorns")),
		new ThornsBlock(BlockBehaviour.Properties.of().noLootTable().mapColor(MapColor.PODZOL).pushReaction(PushReaction.BLOCK).sound(SoundType.WOOD).strength(50.0F, 2000.0F).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("brown_thorns"))))
	);
public static final Block GREEN_THORNS = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("green_thorns")),
		new ThornsBlock(BlockBehaviour.Properties.of().noLootTable().mapColor(MapColor.PLANT).pushReaction(PushReaction.BLOCK).sound(SoundType.WOOD).strength(50.0F, 2000.0F).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("green_thorns"))))
	);
public static final Block BURNT_THORNS = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("burnt_thorns")),
		new BurntThornsBlock(BlockBehaviour.Properties.of().instabreak().noLootTable().mapColor(MapColor.STONE).pushReaction(PushReaction.DESTROY).sound(SoundType.SAND).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("burnt_thorns"))))
	);
public static final Block THORN_ROSE = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("thorn_rose")),
		new ThornRoseBlock(BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).noCollision().pushReaction(PushReaction.DESTROY).sound(SoundType.GRASS).strength(10.0F, 0.0F).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("thorn_rose"))))
	);
public static final Block THORN_LEAVES = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("thorn_leaves")),
		new SpecialStemLeavesBlock(state -> state.is(TFBlocks.BROWN_THORNS) || state.is(TFBlocks.GREEN_THORNS), BlockBehaviour.Properties.of().ignitedByLava().mapColor(MapColor.PLANT).noOcclusion().pushReaction(PushReaction.DESTROY).randomTicks().sound(SoundType.AZALEA_LEAVES).strength(0.2F).isSuffocating((state, getter, pos) -> false).isViewBlocking((state, getter, pos) -> false).isRedstoneConductor((state, level, pos) -> false).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("thorn_leaves"))))
	);
public static final Block BEANSTALK_LEAVES = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("beanstalk_leaves")),
		new SpecialStemLeavesBlock(state -> state.is(TFBlocks.HUGE_STALK), BlockBehaviour.Properties.of().ignitedByLava().mapColor(MapColor.PLANT).noOcclusion().pushReaction(PushReaction.DESTROY).randomTicks().sound(SoundType.AZALEA_LEAVES).strength(0.2F).isSuffocating((state, getter, pos) -> false).isViewBlocking((state, getter, pos) -> false).isRedstoneConductor((state, level, pos) -> false).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("beanstalk_leaves"))))
	);
public static final Block DEADROCK = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("deadrock")),
		new Block(BlockBehaviour.Properties.of().instrument(NoteBlockInstrument.BASEDRUM).mapColor(MapColor.STONE).requiresCorrectToolForDrops().sound(SoundType.STONE).strength(100.0F, 6000000.0F).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("deadrock"))))
	);
public static final Block CRACKED_DEADROCK = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("cracked_deadrock")),
		new Block(BlockBehaviour.Properties.ofFullCopy(DEADROCK).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("cracked_deadrock"))))
	);
public static final Block WEATHERED_DEADROCK = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("weathered_deadrock")),
		new Block(BlockBehaviour.Properties.ofFullCopy(DEADROCK).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("weathered_deadrock"))))
	);
public static final Block TROLLSTEINN = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("trollsteinn")),
		new TrollsteinnBlock(BlockBehaviour.Properties.of().instrument(NoteBlockInstrument.BASEDRUM).mapColor(MapColor.STONE).randomTicks().requiresCorrectToolForDrops().sound(SoundType.STONE).strength(2.0F, 6.0F).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("trollsteinn"))))
	);

public static final Block WISPY_CLOUD = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("wispy_cloud")),
		new WispyCloudBlock(Biome.Precipitation.NONE, BlockBehaviour.Properties.of().instrument(NoteBlockInstrument.HAT).mapColor(MapColor.SNOW).noOcclusion().pushReaction(PushReaction.DESTROY).replaceable().sound(SoundType.WOOL).strength(0.3F, 0.0F).forceSolidOff().setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("wispy_cloud"))))
	);
public static final Block FLUFFY_CLOUD = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("fluffy_cloud")),
		new CloudBlock(null, BlockBehaviour.Properties.of().instrument(NoteBlockInstrument.HAT).mapColor(MapColor.ICE).pushReaction(PushReaction.DESTROY).sound(SoundType.WOOL).strength(0.8F, 0.0F).randomTicks().setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("fluffy_cloud"))))
	);
public static final Block RAINY_CLOUD = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("rainy_cloud")),
		new CloudBlock(Biome.Precipitation.RAIN, BlockBehaviour.Properties.of().instrument(NoteBlockInstrument.HAT).mapColor(MapColor.ICE).pushReaction(PushReaction.DESTROY).sound(SoundType.WOOL).strength(0.8F, 0.0F).randomTicks().setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("rainy_cloud"))))
	);
public static final Block SNOWY_CLOUD = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("snowy_cloud")),
		new CloudBlock(Biome.Precipitation.SNOW, BlockBehaviour.Properties.of().instrument(NoteBlockInstrument.HAT).mapColor(MapColor.ICE).pushReaction(PushReaction.DESTROY).sound(SoundType.WOOL).strength(0.8F, 0.0F).randomTicks().setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("snowy_cloud"))))
	);

public static final Block GIANT_COBBLESTONE = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("giant_cobblestone")),
		new GiantBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.COBBLESTONE).pushReaction(PushReaction.BLOCK).requiresCorrectToolForDrops().strength(128.0F, 50.0F).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("giant_cobblestone"))))
	);
public static final Block GIANT_LOG = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("giant_log")),
		new GiantBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS).pushReaction(PushReaction.BLOCK).requiresCorrectToolForDrops().strength(128.0F, 30.0F).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("giant_log"))))
	);
public static final Block GIANT_LEAVES = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("giant_leaves")),
		new GiantLeavesBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_LEAVES).noOcclusion().pushReaction(PushReaction.BLOCK).requiresCorrectToolForDrops().sound(SoundType.AZALEA_LEAVES).strength(0.2F * 64.0F, 15.0F).isSuffocating((state, getter, pos) -> false).isViewBlocking((state, getter, pos) -> false).isValidSpawn(TFBlocks::noSpawning).isRedstoneConductor((state, level, pos) -> false).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("giant_leaves"))))
	);
public static final Block GIANT_OBSIDIAN = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("giant_obsidian")),
		new GiantBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OBSIDIAN).pushReaction(PushReaction.BLOCK).requiresCorrectToolForDrops().strength(50.0F * 64.0F * 64.0F, 2000.0F * 64.0F * 64.0F).isValidSpawn(TFBlocks::noSpawning).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("giant_obsidian"))))
	);
public static final Block UBEROUS_SOIL = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("uberous_soil")),
		new UberousSoilBlock(BlockBehaviour.Properties.of().mapColor(MapColor.DIRT).sound(SoundType.GRAVEL).strength(0.6F).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("uberous_soil"))))
	);
public static final RotatedPillarBlock HUGE_STALK = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("huge_stalk")),
		new RotatedPillarBlock(BlockBehaviour.Properties.of().ignitedByLava().instrument(NoteBlockInstrument.BASS).mapColor(MapColor.PLANT).sound(SoundType.STEM).strength(1.5F, 3.0F).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("huge_stalk"))))
	);
public static final Block BEANSTALK_GROWER = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("beanstalk_grower")),
		new GrowingBeanstalkBlock(BlockBehaviour.Properties.of().noCollision().noLootTable().noOcclusion().strength(-1.0F, 6000000.0F).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("beanstalk_grower"))))
	);
public static final Block HUGE_MUSHGLOOM = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("huge_mushgloom")),
		new HugeMushroomBlock(BlockBehaviour.Properties.of().ignitedByLava().lightLevel((state) -> 5).mapColor(MapColor.COLOR_ORANGE).sound(SoundType.SHROOMLIGHT).strength(0.2F).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("huge_mushgloom"))))
	);
public static final Block HUGE_MUSHGLOOM_STEM = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("huge_mushgloom_stem")),
		new HugeMushroomBlock(BlockBehaviour.Properties.of().ignitedByLava().lightLevel((state) -> 5).mapColor(MapColor.COLOR_ORANGE).sound(SoundType.NYLIUM).strength(0.2F).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("huge_mushgloom_stem"))))
	);
public static final Block TROLLVIDR = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("trollvidr")),
		new TrollRootBlock(BlockBehaviour.Properties.of().instabreak().mapColor(MapColor.PLANT).noCollision().pushReaction(PushReaction.DESTROY).sound(SoundType.FLOWERING_AZALEA).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("trollvidr"))))
	);
public static final Block UNRIPE_TROLLBER = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("unripe_trollber")),
		new UnripeTorchClusterBlock(BlockBehaviour.Properties.of().instabreak().mapColor(MapColor.PLANT).noCollision().pushReaction(PushReaction.DESTROY).randomTicks().sound(SoundType.FLOWERING_AZALEA).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("unripe_trollber"))))
	);
public static final Block TROLLBER = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("trollber")),
		new TrollRootBlock(BlockBehaviour.Properties.of().instabreak().lightLevel((state) -> 15).mapColor(MapColor.PLANT).noCollision().pushReaction(PushReaction.DESTROY).sound(SoundType.FLOWERING_AZALEA).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("trollber"))))
	);

	//plateau castle
public static final Block CASTLE_BRICK = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("castle_brick")),
		new Block(BlockBehaviour.Properties.of().instrument(NoteBlockInstrument.BASEDRUM).mapColor(MapColor.QUARTZ).requiresCorrectToolForDrops().sound(SoundType.DEEPSLATE_TILES).strength(100.0F, 50.0F).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("castle_brick"))))
	);
public static final Block WORN_CASTLE_BRICK = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("worn_castle_brick")),
		new Block(BlockBehaviour.Properties.ofFullCopy(CASTLE_BRICK).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("worn_castle_brick"))))
	);
public static final Block CRACKED_CASTLE_BRICK = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("cracked_castle_brick")),
		new Block(BlockBehaviour.Properties.ofFullCopy(CASTLE_BRICK).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("cracked_castle_brick"))))
	);
public static final Block CASTLE_ROOF_TILE = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("castle_roof_tile")),
		new Block(BlockBehaviour.Properties.ofFullCopy(CASTLE_BRICK).mapColor(MapColor.COLOR_GRAY).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("castle_roof_tile"))))
	);
public static final Block MOSSY_CASTLE_BRICK = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("mossy_castle_brick")),
		new Block(BlockBehaviour.Properties.ofFullCopy(CASTLE_BRICK).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("mossy_castle_brick"))))
	);
public static final Block THICK_CASTLE_BRICK = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("thick_castle_brick")),
		new Block(BlockBehaviour.Properties.ofFullCopy(CASTLE_BRICK).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("thick_castle_brick"))))
	);
public static final Block ENCASED_CASTLE_BRICK_PILLAR = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("encased_castle_brick_pillar")),
		new RotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(CASTLE_BRICK).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("encased_castle_brick_pillar"))))
	);
public static final Block ENCASED_CASTLE_BRICK_TILE = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("encased_castle_brick_tile")),
		new Block(BlockBehaviour.Properties.ofFullCopy(CASTLE_BRICK).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("encased_castle_brick_tile"))))
	);
public static final Block BOLD_CASTLE_BRICK_PILLAR = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("bold_castle_brick_pillar")),
		new RotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(CASTLE_BRICK).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("bold_castle_brick_pillar"))))
	);
public static final Block BOLD_CASTLE_BRICK_TILE = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("bold_castle_brick_tile")),
		new Block(BlockBehaviour.Properties.ofFullCopy(CASTLE_BRICK).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("bold_castle_brick_tile"))))
	);
public static final StairBlock CASTLE_BRICK_STAIRS = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("castle_brick_stairs")),
		new StairBlock(CASTLE_BRICK.defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(CASTLE_BRICK).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("castle_brick_stairs"))))
	);
public static final StairBlock WORN_CASTLE_BRICK_STAIRS = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("worn_castle_brick_stairs")),
		new StairBlock(WORN_CASTLE_BRICK.defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(WORN_CASTLE_BRICK).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("worn_castle_brick_stairs"))))
	);
public static final StairBlock CRACKED_CASTLE_BRICK_STAIRS = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("cracked_castle_brick_stairs")),
		new StairBlock(CRACKED_CASTLE_BRICK.defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(CRACKED_CASTLE_BRICK).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("cracked_castle_brick_stairs"))))
	);
public static final StairBlock MOSSY_CASTLE_BRICK_STAIRS = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("mossy_castle_brick_stairs")),
		new StairBlock(MOSSY_CASTLE_BRICK.defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(MOSSY_CASTLE_BRICK).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("mossy_castle_brick_stairs"))))
	);
public static final StairBlock ENCASED_CASTLE_BRICK_STAIRS = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("encased_castle_brick_stairs")),
		new StairBlock(ENCASED_CASTLE_BRICK_PILLAR.defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(ENCASED_CASTLE_BRICK_PILLAR).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("encased_castle_brick_stairs"))))
	);
public static final StairBlock BOLD_CASTLE_BRICK_STAIRS = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("bold_castle_brick_stairs")),
		new StairBlock(BOLD_CASTLE_BRICK_PILLAR.defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(BOLD_CASTLE_BRICK_PILLAR).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("bold_castle_brick_stairs"))))
	);
public static final Block PINK_CASTLE_RUNE_BRICK = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("pink_castle_rune_brick")),
		new Block(BlockBehaviour.Properties.ofFullCopy(CASTLE_BRICK).mapColor(DyeColor.MAGENTA).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("pink_castle_rune_brick"))))
	);
public static final Block BLUE_CASTLE_RUNE_BRICK = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("blue_castle_rune_brick")),
		new Block(BlockBehaviour.Properties.ofFullCopy(CASTLE_BRICK).mapColor(DyeColor.LIGHT_BLUE).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("blue_castle_rune_brick"))))
	);
public static final Block YELLOW_CASTLE_RUNE_BRICK = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("yellow_castle_rune_brick")),
		new Block(BlockBehaviour.Properties.ofFullCopy(CASTLE_BRICK).mapColor(DyeColor.YELLOW).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("yellow_castle_rune_brick"))))
	);
public static final Block VIOLET_CASTLE_RUNE_BRICK = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("violet_castle_rune_brick")),
		new Block(BlockBehaviour.Properties.ofFullCopy(CASTLE_BRICK).mapColor(DyeColor.PURPLE).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("violet_castle_rune_brick"))))
	);
public static final Block VIOLET_FORCE_FIELD = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("violet_force_field")),
		new ForceFieldBlock(BlockBehaviour.Properties.of().lightLevel((state) -> 2).mapColor(DyeColor.PURPLE).noLootTable().noOcclusion().pushReaction(PushReaction.BLOCK).strength(-1.0F, 3600000.8F).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("violet_force_field"))))
	);
public static final Block PINK_FORCE_FIELD = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("pink_force_field")),
		new ForceFieldBlock(BlockBehaviour.Properties.of().lightLevel((state) -> 2).mapColor(DyeColor.MAGENTA).noLootTable().noOcclusion().pushReaction(PushReaction.BLOCK).strength(-1.0F, 3600000.8F).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("pink_force_field"))))
	);
public static final Block ORANGE_FORCE_FIELD = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("orange_force_field")),
		new ForceFieldBlock(BlockBehaviour.Properties.of().lightLevel((state) -> 2).mapColor(DyeColor.ORANGE).noLootTable().noOcclusion().pushReaction(PushReaction.BLOCK).strength(-1.0F, 3600000.8F).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("orange_force_field"))))
	);
public static final Block GREEN_FORCE_FIELD = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("green_force_field")),
		new ForceFieldBlock(BlockBehaviour.Properties.of().lightLevel((state) -> 2).mapColor(DyeColor.GREEN).noLootTable().noOcclusion().pushReaction(PushReaction.BLOCK).strength(-1.0F, 3600000.8F).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("green_force_field"))))
	);
public static final Block BLUE_FORCE_FIELD = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("blue_force_field")),
		new ForceFieldBlock(BlockBehaviour.Properties.of().lightLevel((state) -> 2).mapColor(DyeColor.LIGHT_BLUE).noLootTable().noOcclusion().pushReaction(PushReaction.BLOCK).strength(-1.0F, 3600000.8F).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("blue_force_field"))))
	);
public static final Block CINDER_FURNACE = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("cinder_furnace")),
		new CinderFurnaceBlock(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).requiresCorrectToolForDrops().strength(7.0F).lightLevel((state) -> 15).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("cinder_furnace"))))
	);
public static final RotatedPillarBlock CINDER_LOG = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("cinder_log")),
		new RotatedPillarBlock(BlockBehaviour.Properties.of().ignitedByLava().instrument(NoteBlockInstrument.BASS).mapColor(MapColor.COLOR_GRAY).strength(1.0F).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("cinder_log"))))
	);
public static final Block CINDER_WOOD = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("cinder_wood")),
		new RotatedPillarBlock(BlockBehaviour.Properties.of().ignitedByLava().instrument(NoteBlockInstrument.BASS).mapColor(MapColor.COLOR_GRAY).strength(1.0F).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("cinder_wood"))))
	);
public static final Block YELLOW_CASTLE_DOOR = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("yellow_castle_door")),
		new CastleDoorBlock(BlockBehaviour.Properties.of().forceSolidOn().mapColor((state) -> state.getValue(CastleDoorBlock.VANISHED) ? MapColor.NONE : DyeColor.YELLOW.getMapColor()).pushReaction(PushReaction.BLOCK).requiresCorrectToolForDrops().sound(SoundType.DEEPSLATE_TILES).strength(100.0F, 100.0F).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("yellow_castle_door"))))
	);
public static final Block VIOLET_CASTLE_DOOR = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("violet_castle_door")),
		new CastleDoorBlock(BlockBehaviour.Properties.of().forceSolidOn().mapColor((state) -> state.getValue(CastleDoorBlock.VANISHED) ? MapColor.NONE : DyeColor.LIGHT_BLUE.getMapColor()).pushReaction(PushReaction.BLOCK).requiresCorrectToolForDrops().sound(SoundType.DEEPSLATE_TILES).strength(100.0F, 100.0F).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("violet_castle_door"))))
	);
public static final Block PINK_CASTLE_DOOR = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("pink_castle_door")),
		new CastleDoorBlock(BlockBehaviour.Properties.of().forceSolidOn().mapColor((state) -> state.getValue(CastleDoorBlock.VANISHED) ? MapColor.NONE : DyeColor.MAGENTA.getMapColor()).pushReaction(PushReaction.BLOCK).requiresCorrectToolForDrops().sound(SoundType.DEEPSLATE_TILES).strength(100.0F, 100.0F).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("pink_castle_door"))))
	);
public static final Block BLUE_CASTLE_DOOR = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("blue_castle_door")),
		new CastleDoorBlock(BlockBehaviour.Properties.of().forceSolidOn().mapColor((state) -> state.getValue(CastleDoorBlock.VANISHED) ? MapColor.NONE : DyeColor.PURPLE.getMapColor()).pushReaction(PushReaction.BLOCK).requiresCorrectToolForDrops().sound(SoundType.DEEPSLATE_TILES).strength(100.0F, 100.0F).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("blue_castle_door"))))
	);

	//mini structures
public static final Block TWILIGHT_PORTAL_MINIATURE_STRUCTURE = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("twilight_portal_miniature_structure")),
		new MiniatureStructureBlock(BlockBehaviour.Properties.of().noCollision().noOcclusion().requiresCorrectToolForDrops().strength(0.75F).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("twilight_portal_miniature_structure"))))
	);
public static final Block HEDGE_MAZE_MINIATURE_STRUCTURE = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("hedge_maze_miniature_structure")),
		new MiniatureStructureBlock(BlockBehaviour.Properties.ofFullCopy(TWILIGHT_PORTAL_MINIATURE_STRUCTURE).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("hedge_maze_miniature_structure"))))
	);
public static final Block HOLLOW_HILL_MINIATURE_STRUCTURE = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("hollow_hill_miniature_structure")),
		new MiniatureStructureBlock(BlockBehaviour.Properties.ofFullCopy(TWILIGHT_PORTAL_MINIATURE_STRUCTURE).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("hollow_hill_miniature_structure"))))
	);
public static final Block QUEST_GROVE_MINIATURE_STRUCTURE = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("quest_grove_miniature_structure")),
		new MiniatureStructureBlock(BlockBehaviour.Properties.ofFullCopy(TWILIGHT_PORTAL_MINIATURE_STRUCTURE).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("quest_grove_miniature_structure"))))
	);
public static final Block MUSHROOM_TOWER_MINIATURE_STRUCTURE = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("mushroom_tower_miniature_structure")),
		new MiniatureStructureBlock(BlockBehaviour.Properties.ofFullCopy(TWILIGHT_PORTAL_MINIATURE_STRUCTURE).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("mushroom_tower_miniature_structure"))))
	);
public static final Block NAGA_COURTYARD_MINIATURE_STRUCTURE = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("naga_courtyard_miniature_structure")),
		new MiniatureStructureBlock(BlockBehaviour.Properties.ofFullCopy(TWILIGHT_PORTAL_MINIATURE_STRUCTURE).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("naga_courtyard_miniature_structure"))))
	);
public static final Block LICH_TOWER_MINIATURE_STRUCTURE = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("lich_tower_miniature_structure")),
		new MiniatureStructureBlock(BlockBehaviour.Properties.ofFullCopy(TWILIGHT_PORTAL_MINIATURE_STRUCTURE).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("lich_tower_miniature_structure"))))
	);
public static final Block MINOTAUR_LABYRINTH_MINIATURE_STRUCTURE = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("minotaur_labyrinth_miniature_structure")),
		new MiniatureStructureBlock(BlockBehaviour.Properties.ofFullCopy(TWILIGHT_PORTAL_MINIATURE_STRUCTURE).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("minotaur_labyrinth_miniature_structure"))))
	);
public static final Block HYDRA_LAIR_MINIATURE_STRUCTURE = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("hydra_lair_miniature_structure")),
		new MiniatureStructureBlock(BlockBehaviour.Properties.ofFullCopy(TWILIGHT_PORTAL_MINIATURE_STRUCTURE).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("hydra_lair_miniature_structure"))))
	);
public static final Block GOBLIN_STRONGHOLD_MINIATURE_STRUCTURE = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("goblin_stronghold_miniature_structure")),
		new MiniatureStructureBlock(BlockBehaviour.Properties.ofFullCopy(TWILIGHT_PORTAL_MINIATURE_STRUCTURE).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("goblin_stronghold_miniature_structure"))))
	);
public static final Block DARK_TOWER_MINIATURE_STRUCTURE = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("dark_tower_miniature_structure")),
		new MiniatureStructureBlock(BlockBehaviour.Properties.ofFullCopy(TWILIGHT_PORTAL_MINIATURE_STRUCTURE).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("dark_tower_miniature_structure"))))
	);
public static final Block YETI_CAVE_MINIATURE_STRUCTURE = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("yeti_cave_miniature_structure")),
		new MiniatureStructureBlock(BlockBehaviour.Properties.ofFullCopy(TWILIGHT_PORTAL_MINIATURE_STRUCTURE).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("yeti_cave_miniature_structure"))))
	);
public static final Block AURORA_PALACE_MINIATURE_STRUCTURE = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("aurora_palace_miniature_structure")),
		new MiniatureStructureBlock(BlockBehaviour.Properties.ofFullCopy(TWILIGHT_PORTAL_MINIATURE_STRUCTURE).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("aurora_palace_miniature_structure"))))
	);
public static final Block TROLL_CAVE_COTTAGE_MINIATURE_STRUCTURE = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("troll_cave_cottage_miniature_structure")),
		new MiniatureStructureBlock(BlockBehaviour.Properties.ofFullCopy(TWILIGHT_PORTAL_MINIATURE_STRUCTURE).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("troll_cave_cottage_miniature_structure"))))
	);
public static final Block FINAL_CASTLE_MINIATURE_STRUCTURE = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("final_castle_miniature_structure")),
		new MiniatureStructureBlock(BlockBehaviour.Properties.ofFullCopy(TWILIGHT_PORTAL_MINIATURE_STRUCTURE).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("final_castle_miniature_structure"))))
	);

	//storage blocks
public static final Block KNIGHTMETAL_BLOCK = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("knightmetal_block")),
		new KnightmetalBlock(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).requiresCorrectToolForDrops().sound(SoundType.NETHERITE_BLOCK).strength(5.0F, 40.0F).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("knightmetal_block"))))
	);
public static final Block IRONWOOD_BLOCK = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("ironwood_block")),
		new Block(BlockBehaviour.Properties.of().ignitedByLava().mapColor(MapColor.WOOD).sound(SoundType.WOOD).strength(5.0F, 6.0F).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("ironwood_block"))))
	);
public static final Block FIERY_BLOCK = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("fiery_block")),
		new FieryBlock(BlockBehaviour.Properties.of().mapColor(MapColor.TERRACOTTA_BLACK).noOcclusion().requiresCorrectToolForDrops().sound(SoundType.METAL).strength(5.0F, 6.0F).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("fiery_block"))))
	);
public static final Block STEELEAF_BLOCK = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("steeleaf_block")),
		new Block(BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).sound(SoundType.NETHERITE_BLOCK).strength(5.0F, 6.0F).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("steeleaf_block"))))
	);
public static final Block ARCTIC_FUR_BLOCK = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("arctic_fur_block")),
		new ArcticFurBlock(BlockBehaviour.Properties.of().ignitedByLava().mapColor(MapColor.WOOL).sound(SoundType.WOOL).strength(0.8F).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("arctic_fur_block"))))
	);
public static final Block CARMINITE_BLOCK = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("carminite_block")),
		new Block(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_RED).strength(1.5F, 10.0F).requiresCorrectToolForDrops().sound(SoundType.METAL).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("carminite_block"))))
	);

	//boss trophies and spawners
public static final Block NAGA_BOSS_SPAWNER = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("naga_boss_spawner")),
		new BossSpawnerBlock(BossVariant.NAGA, BlockBehaviour.Properties.of().mapColor(MapColor.STONE).noLootTable().sound(SoundType.METAL).noOcclusion().strength(-1.0F, 3600000.8F).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("naga_boss_spawner"))))
	);
public static final Block LICH_BOSS_SPAWNER = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("lich_boss_spawner")),
		new BossSpawnerBlock(BossVariant.LICH, BlockBehaviour.Properties.of().mapColor(MapColor.STONE).noLootTable().sound(SoundType.METAL).noOcclusion().strength(-1.0F, 3600000.8F).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("lich_boss_spawner"))))
	);
public static final Block HYDRA_BOSS_SPAWNER = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("hydra_boss_spawner")),
		new BossSpawnerBlock(BossVariant.HYDRA, BlockBehaviour.Properties.of().mapColor(MapColor.STONE).noLootTable().sound(SoundType.METAL).noOcclusion().strength(-1.0F, 3600000.8F).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("hydra_boss_spawner"))))
	);
public static final Block UR_GHAST_BOSS_SPAWNER = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("ur_ghast_boss_spawner")),
		new BossSpawnerBlock(BossVariant.UR_GHAST, BlockBehaviour.Properties.of().mapColor(MapColor.STONE).noLootTable().sound(SoundType.METAL).noOcclusion().strength(-1.0F, 3600000.8F).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("ur_ghast_boss_spawner"))))
	);
public static final Block KNIGHT_PHANTOM_BOSS_SPAWNER = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("knight_phantom_boss_spawner")),
		new BossSpawnerBlock(BossVariant.KNIGHT_PHANTOM, BlockBehaviour.Properties.of().mapColor(MapColor.STONE).noLootTable().sound(SoundType.METAL).noOcclusion().strength(-1.0F, 3600000.8F).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("knight_phantom_boss_spawner"))))
	);
public static final Block SNOW_QUEEN_BOSS_SPAWNER = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("snow_queen_boss_spawner")),
		new BossSpawnerBlock(BossVariant.SNOW_QUEEN, BlockBehaviour.Properties.of().mapColor(MapColor.STONE).noLootTable().sound(SoundType.METAL).noOcclusion().strength(-1.0F, 3600000.8F).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("snow_queen_boss_spawner"))))
	);
public static final Block MINOSHROOM_BOSS_SPAWNER = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("minoshroom_boss_spawner")),
		new BossSpawnerBlock(BossVariant.MINOSHROOM, BlockBehaviour.Properties.of().mapColor(MapColor.STONE).noLootTable().sound(SoundType.METAL).noOcclusion().strength(-1.0F, 3600000.8F).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("minoshroom_boss_spawner"))))
	);
public static final Block ALPHA_YETI_BOSS_SPAWNER = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("alpha_yeti_boss_spawner")),
		new BossSpawnerBlock(BossVariant.ALPHA_YETI, BlockBehaviour.Properties.of().mapColor(MapColor.STONE).noLootTable().sound(SoundType.METAL).noOcclusion().strength(-1.0F, 3600000.8F).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("alpha_yeti_boss_spawner"))))
	);
public static final Block FINAL_BOSS_BOSS_SPAWNER = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("final_boss_boss_spawner")),
		new BossSpawnerBlock(BossVariant.FINAL_BOSS, BlockBehaviour.Properties.of().mapColor(MapColor.STONE).noLootTable().sound(SoundType.METAL).noOcclusion().strength(-1.0F, 3600000.8F).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("final_boss_boss_spawner"))))
	);
public static final TrophyBlock NAGA_TROPHY = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("naga_trophy")),
		new TrophyBlock(BossVariant.NAGA, 5, BlockBehaviour.Properties.of().instabreak().setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("naga_trophy"))))
	);
public static final TrophyBlock LICH_TROPHY = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("lich_trophy")),
		new TrophyBlock(BossVariant.LICH, 6, BlockBehaviour.Properties.of().instabreak().setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("lich_trophy"))))
	);
public static final TrophyBlock HYDRA_TROPHY = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("hydra_trophy")),
		new TrophyBlock(BossVariant.HYDRA, 12, BlockBehaviour.Properties.of().instabreak().setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("hydra_trophy"))))
	);
public static final TrophyBlock UR_GHAST_TROPHY = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("ur_ghast_trophy")),
		new TrophyBlock(BossVariant.UR_GHAST, 13, BlockBehaviour.Properties.of().instabreak().setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("ur_ghast_trophy"))))
	);
public static final TrophyBlock KNIGHT_PHANTOM_TROPHY = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("knight_phantom_trophy")),
		new TrophyBlock(BossVariant.KNIGHT_PHANTOM, 8, BlockBehaviour.Properties.of().instabreak().setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("knight_phantom_trophy"))))
	);
public static final TrophyBlock SNOW_QUEEN_TROPHY = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("snow_queen_trophy")),
		new TrophyBlock(BossVariant.SNOW_QUEEN, 14, BlockBehaviour.Properties.of().instabreak().setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("snow_queen_trophy"))))
	);
public static final TrophyBlock MINOSHROOM_TROPHY = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("minoshroom_trophy")),
		new TrophyBlock(BossVariant.MINOSHROOM, 7, BlockBehaviour.Properties.of().instabreak().setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("minoshroom_trophy"))))
	);
public static final TrophyBlock ALPHA_YETI_TROPHY = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("alpha_yeti_trophy")),
		new TrophyBlock(BossVariant.ALPHA_YETI, 9, BlockBehaviour.Properties.of().instabreak().setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("alpha_yeti_trophy"))))
	);
public static final TrophyBlock QUEST_RAM_TROPHY = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("quest_ram_trophy")),
		new TrophyBlock(BossVariant.QUEST_RAM, 1, BlockBehaviour.Properties.of().instabreak().setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("quest_ram_trophy"))))
	);
public static final TrophyWallBlock NAGA_WALL_TROPHY = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("naga_wall_trophy")),
		new TrophyWallBlock(BossVariant.NAGA, BlockBehaviour.Properties.of().instabreak().pushReaction(PushReaction.DESTROY).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("naga_wall_trophy"))))
	);
public static final TrophyWallBlock LICH_WALL_TROPHY = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("lich_wall_trophy")),
		new TrophyWallBlock(BossVariant.LICH, BlockBehaviour.Properties.of().instabreak().pushReaction(PushReaction.DESTROY).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("lich_wall_trophy"))))
	);
public static final TrophyWallBlock HYDRA_WALL_TROPHY = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("hydra_wall_trophy")),
		new TrophyWallBlock(BossVariant.HYDRA, BlockBehaviour.Properties.of().instabreak().pushReaction(PushReaction.DESTROY).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("hydra_wall_trophy"))))
	);
public static final TrophyWallBlock UR_GHAST_WALL_TROPHY = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("ur_ghast_wall_trophy")),
		new TrophyWallBlock(BossVariant.UR_GHAST, BlockBehaviour.Properties.of().instabreak().pushReaction(PushReaction.DESTROY).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("ur_ghast_wall_trophy"))))
	);
public static final TrophyWallBlock KNIGHT_PHANTOM_WALL_TROPHY = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("knight_phantom_wall_trophy")),
		new TrophyWallBlock(BossVariant.KNIGHT_PHANTOM, BlockBehaviour.Properties.of().instabreak().pushReaction(PushReaction.DESTROY).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("knight_phantom_wall_trophy"))))
	);
public static final TrophyWallBlock SNOW_QUEEN_WALL_TROPHY = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("snow_queen_wall_trophy")),
		new TrophyWallBlock(BossVariant.SNOW_QUEEN, BlockBehaviour.Properties.of().instabreak().pushReaction(PushReaction.DESTROY).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("snow_queen_wall_trophy"))))
	);
public static final TrophyWallBlock MINOSHROOM_WALL_TROPHY = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("minoshroom_wall_trophy")),
		new TrophyWallBlock(BossVariant.MINOSHROOM, BlockBehaviour.Properties.of().instabreak().pushReaction(PushReaction.DESTROY).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("minoshroom_wall_trophy"))))
	);
public static final TrophyWallBlock ALPHA_YETI_WALL_TROPHY = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("alpha_yeti_wall_trophy")),
		new TrophyWallBlock(BossVariant.ALPHA_YETI, BlockBehaviour.Properties.of().instabreak().pushReaction(PushReaction.DESTROY).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("alpha_yeti_wall_trophy"))))
	);
public static final TrophyWallBlock QUEST_RAM_WALL_TROPHY = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("quest_ram_wall_trophy")),
		new TrophyWallBlock(BossVariant.QUEST_RAM, BlockBehaviour.Properties.of().instabreak().pushReaction(PushReaction.DESTROY).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("quest_ram_wall_trophy"))))
	);

	// TODO Enumify all of the dang tree stuff

	//all tree related stuff
public static final BanisterBlock OAK_BANISTER = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("oak_banister")),
		new BanisterBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("oak_banister"))))
	);
public static final BanisterBlock SPRUCE_BANISTER = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("spruce_banister")),
		new BanisterBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SPRUCE_PLANKS).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("spruce_banister"))))
	);
public static final BanisterBlock BIRCH_BANISTER = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("birch_banister")),
		new BanisterBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.BIRCH_PLANKS).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("birch_banister"))))
	);
public static final BanisterBlock JUNGLE_BANISTER = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("jungle_banister")),
		new BanisterBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.JUNGLE_PLANKS).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("jungle_banister"))))
	);
public static final BanisterBlock ACACIA_BANISTER = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("acacia_banister")),
		new BanisterBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.ACACIA_PLANKS).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("acacia_banister"))))
	);
public static final BanisterBlock DARK_OAK_BANISTER = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("dark_oak_banister")),
		new BanisterBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.DARK_OAK_PLANKS).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("dark_oak_banister"))))
	);
public static final BanisterBlock CRIMSON_BANISTER = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("crimson_banister")),
		new BanisterBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.CRIMSON_PLANKS).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("crimson_banister"))))
	);
public static final BanisterBlock WARPED_BANISTER = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("warped_banister")),
		new BanisterBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.WARPED_PLANKS).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("warped_banister"))))
	);
public static final BanisterBlock VANGROVE_BANISTER = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("vangrove_banister")),
		new BanisterBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.MANGROVE_PLANKS).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("vangrove_banister"))))
	);
public static final BanisterBlock BAMBOO_BANISTER = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("bamboo_banister")),
		new BanisterBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.BAMBOO_PLANKS).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("bamboo_banister"))))
	);
public static final BanisterBlock CHERRY_BANISTER = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("cherry_banister")),
		new BanisterBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.CHERRY_PLANKS).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("cherry_banister"))))
	);
public static final BanisterBlock PALE_OAK_BANISTER = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("pale_oak_banister")),
		new BanisterBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.PALE_OAK_PLANKS).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("pale_oak_banister"))))
	);

public static final DryingRackBlock OAK_DRYING_RACK = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("oak_drying_rack")),
		new DryingRackBlock(copyAndScaleProperties(Blocks.OAK_SLAB, 0.5F).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("oak_drying_rack"))))
	);
public static final DryingRackBlock SPRUCE_DRYING_RACK = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("spruce_drying_rack")),
		new DryingRackBlock(copyAndScaleProperties(Blocks.SPRUCE_SLAB, 0.5F).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("spruce_drying_rack"))))
	);
public static final DryingRackBlock BIRCH_DRYING_RACK = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("birch_drying_rack")),
		new DryingRackBlock(copyAndScaleProperties(Blocks.BIRCH_SLAB, 0.5F).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("birch_drying_rack"))))
	);
public static final DryingRackBlock JUNGLE_DRYING_RACK = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("jungle_drying_rack")),
		new DryingRackBlock(copyAndScaleProperties(Blocks.JUNGLE_SLAB, 0.5F).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("jungle_drying_rack"))))
	);
public static final DryingRackBlock ACACIA_DRYING_RACK = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("acacia_drying_rack")),
		new DryingRackBlock(copyAndScaleProperties(Blocks.ACACIA_SLAB, 0.5F).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("acacia_drying_rack"))))
	);
public static final DryingRackBlock DARK_OAK_DRYING_RACK = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("dark_oak_drying_rack")),
		new DryingRackBlock(copyAndScaleProperties(Blocks.DARK_OAK_SLAB, 0.5F).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("dark_oak_drying_rack"))))
	);
public static final DryingRackBlock CRIMSON_DRYING_RACK = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("crimson_drying_rack")),
		new DryingRackBlock(copyAndScaleProperties(Blocks.CRIMSON_SLAB, 0.5F).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("crimson_drying_rack"))))
	);
public static final DryingRackBlock WARPED_DRYING_RACK = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("warped_drying_rack")),
		new DryingRackBlock(copyAndScaleProperties(Blocks.WARPED_SLAB, 0.5F).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("warped_drying_rack"))))
	);
public static final DryingRackBlock VANGROVE_DRYING_RACK = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("vangrove_drying_rack")),
		new DryingRackBlock(copyAndScaleProperties(Blocks.MANGROVE_SLAB, 0.5F).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("vangrove_drying_rack"))))
	);
public static final DryingRackBlock BAMBOO_DRYING_RACK = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("bamboo_drying_rack")),
		new DryingRackBlock(copyAndScaleProperties(Blocks.BAMBOO_SLAB, 0.5F).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("bamboo_drying_rack"))))
	);
public static final DryingRackBlock CHERRY_DRYING_RACK = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("cherry_drying_rack")),
		new DryingRackBlock(copyAndScaleProperties(Blocks.CHERRY_SLAB, 0.5F).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("cherry_drying_rack"))))
	);
public static final DryingRackBlock PALE_OAK_DRYING_RACK = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("pale_oak_drying_rack")),
		new DryingRackBlock(copyAndScaleProperties(Blocks.CHERRY_SLAB, 0.5F).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("pale_oak_drying_rack"))))
	);

	public static final BlockBehaviour.Properties TWILIGHT_OAK_LOG_PROPS = logProperties(MapColor.WOOD, MapColor.PODZOL).strength(2.0F).sound(SoundType.WOOD);
	public static final BlockBehaviour.Properties CANOPY_LOG_PROPS = logProperties(MapColor.PODZOL, MapColor.COLOR_BROWN).strength(2.0F).sound(SoundType.WOOD);
	public static final BlockBehaviour.Properties MANGROVE_LOG_PROPS = logProperties(MapColor.DIRT, MapColor.PODZOL).strength(2.0F).sound(SoundType.WOOD);
	public static final BlockBehaviour.Properties DARK_LOG_PROPS = logProperties(MapColor.COLOR_BROWN, MapColor.STONE).strength(2.0F).sound(SoundType.WOOD);
	public static final BlockBehaviour.Properties TIME_LOG_PROPS = logProperties(MapColor.DIRT, MapColor.PODZOL).strength(2.0F).sound(SoundType.WOOD);
	public static final BlockBehaviour.Properties TRANSFORMATION_LOG_PROPS = logProperties(MapColor.WOOD, MapColor.PODZOL).strength(2.0F).sound(SoundType.WOOD);
	public static final BlockBehaviour.Properties MINING_LOG_PROPS = logProperties(MapColor.SAND, MapColor.QUARTZ).strength(2.0F).sound(SoundType.WOOD);
	public static final BlockBehaviour.Properties SORTING_LOG_PROPS = logProperties(MapColor.PODZOL, MapColor.COLOR_BROWN).strength(2.0F).sound(SoundType.WOOD);

	public static final BlockBehaviour.Properties TWILIGHT_OAK_BARK_PROPS = logProperties(MapColor.PODZOL).strength(2.0F).sound(SoundType.WOOD);
	public static final BlockBehaviour.Properties CANOPY_BARK_PROPS = logProperties(MapColor.COLOR_BROWN).strength(2.0F).sound(SoundType.WOOD);
	public static final BlockBehaviour.Properties MANGROVE_BARK_PROPS = logProperties(MapColor.PODZOL).strength(2.0F).sound(SoundType.WOOD);
	public static final BlockBehaviour.Properties DARK_BARK_PROPS = logProperties(MapColor.STONE).strength(2.0F).sound(SoundType.WOOD);
	public static final BlockBehaviour.Properties TIME_BARK_PROPS = logProperties(MapColor.PODZOL).strength(2.0F).sound(SoundType.WOOD);
	public static final BlockBehaviour.Properties TRANSFORMATION_BARK_PROPS = logProperties(MapColor.PODZOL).strength(2.0F).sound(SoundType.WOOD);
	public static final BlockBehaviour.Properties MINING_BARK_PROPS = logProperties(MapColor.QUARTZ).strength(2.0F).sound(SoundType.WOOD);
	public static final BlockBehaviour.Properties SORTING_BARK_PROPS = logProperties(MapColor.COLOR_BROWN).strength(2.0F).sound(SoundType.WOOD);

	public static final BlockBehaviour.Properties TWILIGHT_OAK_STRIPPED_PROPS = logProperties(MapColor.WOOD).strength(2.0F).sound(SoundType.WOOD);
	public static final BlockBehaviour.Properties CANOPY_STRIPPED_PROPS = logProperties(MapColor.PODZOL).strength(2.0F).sound(SoundType.WOOD);
	public static final BlockBehaviour.Properties MANGROVE_STRIPPED_PROPS = logProperties(MapColor.DIRT).strength(2.0F).sound(SoundType.WOOD);
	public static final BlockBehaviour.Properties DARK_STRIPPED_PROPS = logProperties(MapColor.COLOR_BROWN).strength(2.0F).sound(SoundType.WOOD);
	public static final BlockBehaviour.Properties TIME_STRIPPED_PROPS = logProperties(MapColor.DIRT).strength(2.0F).sound(SoundType.WOOD);
	public static final BlockBehaviour.Properties TRANSFORMATION_STRIPPED_PROPS = logProperties(MapColor.WOOD).strength(2.0F).sound(SoundType.WOOD);
	public static final BlockBehaviour.Properties MINING_STRIPPED_PROPS = logProperties(MapColor.SAND).strength(2.0F).sound(SoundType.WOOD);
	public static final BlockBehaviour.Properties SORTING_STRIPPED_PROPS = logProperties(MapColor.PODZOL).strength(2.0F).sound(SoundType.WOOD);

public static final RotatedPillarBlock TWILIGHT_OAK_LOG = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("twilight_oak_log")),
		new RotatedPillarBlock(TWILIGHT_OAK_LOG_PROPS.setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("twilight_oak_log"))))
	);
public static final RotatedPillarBlock CANOPY_LOG = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("canopy_log")),
		new RotatedPillarBlock(CANOPY_LOG_PROPS.setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("canopy_log"))))
	);
public static final RotatedPillarBlock MANGROVE_LOG = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("mangrove_log")),
		new RotatedPillarBlock(MANGROVE_LOG_PROPS.setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("mangrove_log"))))
	);
public static final RotatedPillarBlock DARK_LOG = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("dark_log")),
		new RotatedPillarBlock(DARK_LOG_PROPS.setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("dark_log"))))
	);
public static final RotatedPillarBlock TIME_LOG = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("time_log")),
		new RotatedPillarBlock(TIME_LOG_PROPS.setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("time_log"))))
	);
public static final RotatedPillarBlock TRANSFORMATION_LOG = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("transformation_log")),
		new RotatedPillarBlock(TRANSFORMATION_LOG_PROPS.setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("transformation_log"))))
	);
public static final RotatedPillarBlock MINING_LOG = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("mining_log")),
		new RotatedPillarBlock(MINING_LOG_PROPS.setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("mining_log"))))
	);
public static final RotatedPillarBlock SORTING_LOG = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("sorting_log")),
		new RotatedPillarBlock(SORTING_LOG_PROPS.setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("sorting_log"))))
	);

public static final HorizontalHollowLogBlock HOLLOW_TWILIGHT_OAK_LOG_HORIZONTAL = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("hollow_twilight_oak_log_horizontal")),
		new HorizontalHollowLogBlock(TWILIGHT_OAK_BARK_PROPS.setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("hollow_twilight_oak_log_horizontal"))))
	);
public static final HorizontalHollowLogBlock HOLLOW_CANOPY_LOG_HORIZONTAL = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("hollow_canopy_log_horizontal")),
		new HorizontalHollowLogBlock(CANOPY_BARK_PROPS.setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("hollow_canopy_log_horizontal"))))
	);
public static final HorizontalHollowLogBlock HOLLOW_MANGROVE_LOG_HORIZONTAL = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("hollow_mangrove_log_horizontal")),
		new HorizontalHollowLogBlock(MANGROVE_BARK_PROPS.setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("hollow_mangrove_log_horizontal"))))
	);
public static final HorizontalHollowLogBlock HOLLOW_DARK_LOG_HORIZONTAL = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("hollow_dark_log_horizontal")),
		new HorizontalHollowLogBlock(DARK_BARK_PROPS.setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("hollow_dark_log_horizontal"))))
	);
public static final HorizontalHollowLogBlock HOLLOW_TIME_LOG_HORIZONTAL = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("hollow_time_log_horizontal")),
		new HorizontalHollowLogBlock(TIME_BARK_PROPS.setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("hollow_time_log_horizontal"))))
	);
public static final HorizontalHollowLogBlock HOLLOW_TRANSFORMATION_LOG_HORIZONTAL = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("hollow_transformation_log_horizontal")),
		new HorizontalHollowLogBlock(TRANSFORMATION_BARK_PROPS.setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("hollow_transformation_log_horizontal"))))
	);
public static final HorizontalHollowLogBlock HOLLOW_MINING_LOG_HORIZONTAL = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("hollow_mining_log_horizontal")),
		new HorizontalHollowLogBlock(MINING_BARK_PROPS.setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("hollow_mining_log_horizontal"))))
	);
public static final HorizontalHollowLogBlock HOLLOW_SORTING_LOG_HORIZONTAL = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("hollow_sorting_log_horizontal")),
		new HorizontalHollowLogBlock(SORTING_BARK_PROPS.setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("hollow_sorting_log_horizontal"))))
	);

	public static VerticalHollowLogBlock HOLLOW_TWILIGHT_OAK_LOG_VERTICAL;
	public static VerticalHollowLogBlock HOLLOW_CANOPY_LOG_VERTICAL;
	public static VerticalHollowLogBlock HOLLOW_MANGROVE_LOG_VERTICAL;
	public static VerticalHollowLogBlock HOLLOW_DARK_LOG_VERTICAL;
	public static VerticalHollowLogBlock HOLLOW_TIME_LOG_VERTICAL;
	public static VerticalHollowLogBlock HOLLOW_TRANSFORMATION_LOG_VERTICAL;
	public static VerticalHollowLogBlock HOLLOW_MINING_LOG_VERTICAL;
	public static VerticalHollowLogBlock HOLLOW_SORTING_LOG_VERTICAL;

	public static ClimbableHollowLogBlock HOLLOW_TWILIGHT_OAK_LOG_CLIMBABLE;
	public static ClimbableHollowLogBlock HOLLOW_CANOPY_LOG_CLIMBABLE;
	public static ClimbableHollowLogBlock HOLLOW_MANGROVE_LOG_CLIMBABLE;
	public static ClimbableHollowLogBlock HOLLOW_DARK_LOG_CLIMBABLE;
	public static ClimbableHollowLogBlock HOLLOW_TIME_LOG_CLIMBABLE;
	public static ClimbableHollowLogBlock HOLLOW_TRANSFORMATION_LOG_CLIMBABLE;
	public static ClimbableHollowLogBlock HOLLOW_MINING_LOG_CLIMBABLE;
	public static ClimbableHollowLogBlock HOLLOW_SORTING_LOG_CLIMBABLE;

public static final HorizontalHollowLogBlock HOLLOW_OAK_LOG_HORIZONTAL = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("hollow_oak_log_horizontal")),
		new HorizontalHollowLogBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_WOOD).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("hollow_oak_log_horizontal"))))
	);
public static final HorizontalHollowLogBlock HOLLOW_SPRUCE_LOG_HORIZONTAL = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("hollow_spruce_log_horizontal")),
		new HorizontalHollowLogBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SPRUCE_WOOD).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("hollow_spruce_log_horizontal"))))
	);
public static final HorizontalHollowLogBlock HOLLOW_BIRCH_LOG_HORIZONTAL = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("hollow_birch_log_horizontal")),
		new HorizontalHollowLogBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.BIRCH_WOOD).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("hollow_birch_log_horizontal"))))
	);
public static final HorizontalHollowLogBlock HOLLOW_JUNGLE_LOG_HORIZONTAL = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("hollow_jungle_log_horizontal")),
		new HorizontalHollowLogBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.JUNGLE_WOOD).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("hollow_jungle_log_horizontal"))))
	);
public static final HorizontalHollowLogBlock HOLLOW_ACACIA_LOG_HORIZONTAL = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("hollow_acacia_log_horizontal")),
		new HorizontalHollowLogBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.ACACIA_WOOD).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("hollow_acacia_log_horizontal"))))
	);
public static final HorizontalHollowLogBlock HOLLOW_DARK_OAK_LOG_HORIZONTAL = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("hollow_dark_oak_log_horizontal")),
		new HorizontalHollowLogBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.DARK_OAK_WOOD).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("hollow_dark_oak_log_horizontal"))))
	);
public static final HorizontalHollowLogBlock HOLLOW_CRIMSON_STEM_HORIZONTAL = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("hollow_crimson_stem_horizontal")),
		new HorizontalHollowLogBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.CRIMSON_HYPHAE).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("hollow_crimson_stem_horizontal"))))
	);
public static final HorizontalHollowLogBlock HOLLOW_WARPED_STEM_HORIZONTAL = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("hollow_warped_stem_horizontal")),
		new HorizontalHollowLogBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.WARPED_HYPHAE).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("hollow_warped_stem_horizontal"))))
	);
public static final HorizontalHollowLogBlock HOLLOW_VANGROVE_LOG_HORIZONTAL = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("hollow_vangrove_log_horizontal")),
		new HorizontalHollowLogBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.MANGROVE_WOOD).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("hollow_vangrove_log_horizontal"))))
	);
public static final HorizontalHollowLogBlock HOLLOW_CHERRY_LOG_HORIZONTAL = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("hollow_cherry_log_horizontal")),
		new HorizontalHollowLogBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.CHERRY_WOOD).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("hollow_cherry_log_horizontal"))))
	);
public static final HorizontalHollowLogBlock HOLLOW_PALE_OAK_LOG_HORIZONTAL = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("hollow_pale_oak_log_horizontal")),
		new HorizontalHollowLogBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.PALE_OAK_WOOD).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("hollow_pale_oak_log_horizontal"))))
	);

	public static VerticalHollowLogBlock HOLLOW_OAK_LOG_VERTICAL;
	public static VerticalHollowLogBlock HOLLOW_SPRUCE_LOG_VERTICAL;
	public static VerticalHollowLogBlock HOLLOW_BIRCH_LOG_VERTICAL;
	public static VerticalHollowLogBlock HOLLOW_JUNGLE_LOG_VERTICAL;
	public static VerticalHollowLogBlock HOLLOW_ACACIA_LOG_VERTICAL;
	public static VerticalHollowLogBlock HOLLOW_DARK_OAK_LOG_VERTICAL;
	public static VerticalHollowLogBlock HOLLOW_CRIMSON_STEM_VERTICAL;
	public static VerticalHollowLogBlock HOLLOW_WARPED_STEM_VERTICAL;
	public static VerticalHollowLogBlock HOLLOW_VANGROVE_LOG_VERTICAL;
	public static VerticalHollowLogBlock HOLLOW_CHERRY_LOG_VERTICAL;
	public static VerticalHollowLogBlock HOLLOW_PALE_OAK_LOG_VERTICAL;

	public static ClimbableHollowLogBlock HOLLOW_OAK_LOG_CLIMBABLE;
	public static ClimbableHollowLogBlock HOLLOW_SPRUCE_LOG_CLIMBABLE;
	public static ClimbableHollowLogBlock HOLLOW_BIRCH_LOG_CLIMBABLE;
	public static ClimbableHollowLogBlock HOLLOW_JUNGLE_LOG_CLIMBABLE;
	public static ClimbableHollowLogBlock HOLLOW_ACACIA_LOG_CLIMBABLE;
	public static ClimbableHollowLogBlock HOLLOW_DARK_OAK_LOG_CLIMBABLE;
	public static ClimbableHollowLogBlock HOLLOW_CRIMSON_STEM_CLIMBABLE;
	public static ClimbableHollowLogBlock HOLLOW_WARPED_STEM_CLIMBABLE;
	public static ClimbableHollowLogBlock HOLLOW_VANGROVE_LOG_CLIMBABLE;
	public static ClimbableHollowLogBlock HOLLOW_CHERRY_LOG_CLIMBABLE;
	public static ClimbableHollowLogBlock HOLLOW_PALE_OAK_LOG_CLIMBABLE;

public static final RotatedPillarBlock STRIPPED_TWILIGHT_OAK_LOG = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("stripped_twilight_oak_log")),
		new RotatedPillarBlock(TWILIGHT_OAK_STRIPPED_PROPS.setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("stripped_twilight_oak_log"))))
	);
public static final RotatedPillarBlock STRIPPED_CANOPY_LOG = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("stripped_canopy_log")),
		new RotatedPillarBlock(CANOPY_STRIPPED_PROPS.setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("stripped_canopy_log"))))
	);
public static final RotatedPillarBlock STRIPPED_MANGROVE_LOG = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("stripped_mangrove_log")),
		new RotatedPillarBlock(MANGROVE_STRIPPED_PROPS.setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("stripped_mangrove_log"))))
	);
public static final RotatedPillarBlock STRIPPED_DARK_LOG = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("stripped_dark_log")),
		new RotatedPillarBlock(DARK_STRIPPED_PROPS.setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("stripped_dark_log"))))
	);
public static final RotatedPillarBlock STRIPPED_TIME_LOG = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("stripped_time_log")),
		new RotatedPillarBlock(TIME_STRIPPED_PROPS.setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("stripped_time_log"))))
	);
public static final RotatedPillarBlock STRIPPED_TRANSFORMATION_LOG = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("stripped_transformation_log")),
		new RotatedPillarBlock(TRANSFORMATION_STRIPPED_PROPS.setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("stripped_transformation_log"))))
	);
public static final RotatedPillarBlock STRIPPED_MINING_LOG = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("stripped_mining_log")),
		new RotatedPillarBlock(MINING_STRIPPED_PROPS.setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("stripped_mining_log"))))
	);
public static final RotatedPillarBlock STRIPPED_SORTING_LOG = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("stripped_sorting_log")),
		new RotatedPillarBlock(SORTING_STRIPPED_PROPS.setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("stripped_sorting_log"))))
	);

	static {
		HOLLOW_TWILIGHT_OAK_LOG_VERTICAL = Registry.register(BuiltInRegistries.BLOCK, ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("hollow_twilight_oak_log_vertical")), new VerticalHollowLogBlock(() -> HOLLOW_TWILIGHT_OAK_LOG_CLIMBABLE, TWILIGHT_OAK_STRIPPED_PROPS.setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("hollow_twilight_oak_log_vertical")))));
		HOLLOW_CANOPY_LOG_VERTICAL = Registry.register(BuiltInRegistries.BLOCK, ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("hollow_canopy_log_vertical")), new VerticalHollowLogBlock(() -> HOLLOW_CANOPY_LOG_CLIMBABLE, CANOPY_STRIPPED_PROPS.setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("hollow_canopy_log_vertical")))));
		HOLLOW_MANGROVE_LOG_VERTICAL = Registry.register(BuiltInRegistries.BLOCK, ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("hollow_mangrove_log_vertical")), new VerticalHollowLogBlock(() -> HOLLOW_MANGROVE_LOG_CLIMBABLE, MANGROVE_STRIPPED_PROPS.setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("hollow_mangrove_log_vertical")))));
		HOLLOW_DARK_LOG_VERTICAL = Registry.register(BuiltInRegistries.BLOCK, ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("hollow_dark_log_vertical")), new VerticalHollowLogBlock(() -> HOLLOW_DARK_LOG_CLIMBABLE, DARK_STRIPPED_PROPS.setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("hollow_dark_log_vertical")))));
		HOLLOW_TIME_LOG_VERTICAL = Registry.register(BuiltInRegistries.BLOCK, ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("hollow_time_log_vertical")), new VerticalHollowLogBlock(() -> HOLLOW_TIME_LOG_CLIMBABLE, TIME_STRIPPED_PROPS.setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("hollow_time_log_vertical")))));
		HOLLOW_TRANSFORMATION_LOG_VERTICAL = Registry.register(BuiltInRegistries.BLOCK, ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("hollow_transformation_log_vertical")), new VerticalHollowLogBlock(() -> HOLLOW_TRANSFORMATION_LOG_CLIMBABLE, TRANSFORMATION_STRIPPED_PROPS.setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("hollow_transformation_log_vertical")))));
		HOLLOW_MINING_LOG_VERTICAL = Registry.register(BuiltInRegistries.BLOCK, ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("hollow_mining_log_vertical")), new VerticalHollowLogBlock(() -> HOLLOW_MINING_LOG_CLIMBABLE, MINING_STRIPPED_PROPS.setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("hollow_mining_log_vertical")))));
		HOLLOW_SORTING_LOG_VERTICAL = Registry.register(BuiltInRegistries.BLOCK, ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("hollow_sorting_log_vertical")), new VerticalHollowLogBlock(() -> HOLLOW_SORTING_LOG_CLIMBABLE, SORTING_STRIPPED_PROPS.setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("hollow_sorting_log_vertical")))));

		HOLLOW_TWILIGHT_OAK_LOG_CLIMBABLE = Registry.register(BuiltInRegistries.BLOCK, ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("hollow_twilight_oak_log_climbable")), new ClimbableHollowLogBlock(() -> HOLLOW_TWILIGHT_OAK_LOG_VERTICAL, TWILIGHT_OAK_STRIPPED_PROPS.setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("hollow_twilight_oak_log_climbable")))));
		HOLLOW_CANOPY_LOG_CLIMBABLE = Registry.register(BuiltInRegistries.BLOCK, ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("hollow_canopy_log_climbable")), new ClimbableHollowLogBlock(() -> HOLLOW_CANOPY_LOG_VERTICAL, CANOPY_STRIPPED_PROPS.setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("hollow_canopy_log_climbable")))));
		HOLLOW_MANGROVE_LOG_CLIMBABLE = Registry.register(BuiltInRegistries.BLOCK, ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("hollow_mangrove_log_climbable")), new ClimbableHollowLogBlock(() -> HOLLOW_MANGROVE_LOG_VERTICAL, MANGROVE_STRIPPED_PROPS.setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("hollow_mangrove_log_climbable")))));
		HOLLOW_DARK_LOG_CLIMBABLE = Registry.register(BuiltInRegistries.BLOCK, ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("hollow_dark_log_climbable")), new ClimbableHollowLogBlock(() -> HOLLOW_DARK_LOG_VERTICAL, DARK_STRIPPED_PROPS.setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("hollow_dark_log_climbable")))));
		HOLLOW_TIME_LOG_CLIMBABLE = Registry.register(BuiltInRegistries.BLOCK, ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("hollow_time_log_climbable")), new ClimbableHollowLogBlock(() -> HOLLOW_TIME_LOG_VERTICAL, TIME_STRIPPED_PROPS.setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("hollow_time_log_climbable")))));
		HOLLOW_TRANSFORMATION_LOG_CLIMBABLE = Registry.register(BuiltInRegistries.BLOCK, ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("hollow_transformation_log_climbable")), new ClimbableHollowLogBlock(() -> HOLLOW_TRANSFORMATION_LOG_VERTICAL, TRANSFORMATION_STRIPPED_PROPS.setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("hollow_transformation_log_climbable")))));
		HOLLOW_MINING_LOG_CLIMBABLE = Registry.register(BuiltInRegistries.BLOCK, ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("hollow_mining_log_climbable")), new ClimbableHollowLogBlock(() -> HOLLOW_MINING_LOG_VERTICAL, MINING_STRIPPED_PROPS.setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("hollow_mining_log_climbable")))));
		HOLLOW_SORTING_LOG_CLIMBABLE = Registry.register(BuiltInRegistries.BLOCK, ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("hollow_sorting_log_climbable")), new ClimbableHollowLogBlock(() -> HOLLOW_SORTING_LOG_VERTICAL, SORTING_STRIPPED_PROPS.setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("hollow_sorting_log_climbable")))));

		HOLLOW_OAK_LOG_VERTICAL = Registry.register(BuiltInRegistries.BLOCK, ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("hollow_oak_log_vertical")), new VerticalHollowLogBlock(() -> HOLLOW_OAK_LOG_CLIMBABLE, BlockBehaviour.Properties.ofFullCopy(Blocks.STRIPPED_OAK_WOOD).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("hollow_oak_log_vertical")))));
		HOLLOW_SPRUCE_LOG_VERTICAL = Registry.register(BuiltInRegistries.BLOCK, ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("hollow_spruce_log_vertical")), new VerticalHollowLogBlock(() -> HOLLOW_SPRUCE_LOG_CLIMBABLE, BlockBehaviour.Properties.ofFullCopy(Blocks.STRIPPED_SPRUCE_WOOD).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("hollow_spruce_log_vertical")))));
		HOLLOW_BIRCH_LOG_VERTICAL = Registry.register(BuiltInRegistries.BLOCK, ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("hollow_birch_log_vertical")), new VerticalHollowLogBlock(() -> HOLLOW_BIRCH_LOG_CLIMBABLE, BlockBehaviour.Properties.ofFullCopy(Blocks.STRIPPED_BIRCH_WOOD).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("hollow_birch_log_vertical")))));
		HOLLOW_JUNGLE_LOG_VERTICAL = Registry.register(BuiltInRegistries.BLOCK, ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("hollow_jungle_log_vertical")), new VerticalHollowLogBlock(() -> HOLLOW_JUNGLE_LOG_CLIMBABLE, BlockBehaviour.Properties.ofFullCopy(Blocks.STRIPPED_JUNGLE_WOOD).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("hollow_jungle_log_vertical")))));
		HOLLOW_ACACIA_LOG_VERTICAL = Registry.register(BuiltInRegistries.BLOCK, ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("hollow_acacia_log_vertical")), new VerticalHollowLogBlock(() -> HOLLOW_ACACIA_LOG_CLIMBABLE, BlockBehaviour.Properties.ofFullCopy(Blocks.STRIPPED_ACACIA_WOOD).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("hollow_acacia_log_vertical")))));
		HOLLOW_DARK_OAK_LOG_VERTICAL = Registry.register(BuiltInRegistries.BLOCK, ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("hollow_dark_oak_log_vertical")), new VerticalHollowLogBlock(() -> HOLLOW_DARK_OAK_LOG_CLIMBABLE, BlockBehaviour.Properties.ofFullCopy(Blocks.STRIPPED_DARK_OAK_WOOD).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("hollow_dark_oak_log_vertical")))));
		HOLLOW_CRIMSON_STEM_VERTICAL = Registry.register(BuiltInRegistries.BLOCK, ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("hollow_crimson_stem_vertical")), new VerticalHollowLogBlock(() -> HOLLOW_CRIMSON_STEM_CLIMBABLE, BlockBehaviour.Properties.ofFullCopy(Blocks.STRIPPED_CRIMSON_HYPHAE).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("hollow_crimson_stem_vertical")))));
		HOLLOW_WARPED_STEM_VERTICAL = Registry.register(BuiltInRegistries.BLOCK, ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("hollow_warped_stem_vertical")), new VerticalHollowLogBlock(() -> HOLLOW_WARPED_STEM_CLIMBABLE, BlockBehaviour.Properties.ofFullCopy(Blocks.STRIPPED_WARPED_HYPHAE).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("hollow_warped_stem_vertical")))));
		// wanna see a funny crash? Use () -> BlockBehaviour.Properties.ofFullCopy(Blocks.STRIPPED_MANGROVE_WOOD) instead of the BlockBehaviour.Properties.of(...)
		// I still legit have no idea why it happens but it does
		HOLLOW_VANGROVE_LOG_VERTICAL = Registry.register(BuiltInRegistries.BLOCK, ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("hollow_vangrove_log_vertical")), new VerticalHollowLogBlock(() -> HOLLOW_VANGROVE_LOG_CLIMBABLE, BlockBehaviour.Properties.of().ignitedByLava().mapColor(MapColor.COLOR_RED).strength(2.0F).sound(SoundType.WOOD).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("hollow_vangrove_log_vertical")))));
		HOLLOW_CHERRY_LOG_VERTICAL = Registry.register(BuiltInRegistries.BLOCK, ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("hollow_cherry_log_vertical")), new VerticalHollowLogBlock(() -> HOLLOW_CHERRY_LOG_CLIMBABLE, BlockBehaviour.Properties.ofFullCopy(Blocks.STRIPPED_CHERRY_WOOD).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("hollow_cherry_log_vertical")))));
		HOLLOW_PALE_OAK_LOG_VERTICAL = Registry.register(BuiltInRegistries.BLOCK, ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("hollow_pale_oak_log_vertical")), new VerticalHollowLogBlock(() -> HOLLOW_PALE_OAK_LOG_CLIMBABLE, BlockBehaviour.Properties.ofFullCopy(Blocks.STRIPPED_PALE_OAK_WOOD).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("hollow_pale_oak_log_vertical")))));

		HOLLOW_OAK_LOG_CLIMBABLE = Registry.register(BuiltInRegistries.BLOCK, ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("hollow_oak_log_climbable")), new ClimbableHollowLogBlock(() -> HOLLOW_OAK_LOG_VERTICAL, BlockBehaviour.Properties.ofFullCopy(Blocks.STRIPPED_OAK_WOOD).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("hollow_oak_log_climbable")))));
		HOLLOW_SPRUCE_LOG_CLIMBABLE = Registry.register(BuiltInRegistries.BLOCK, ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("hollow_spruce_log_climbable")), new ClimbableHollowLogBlock(() -> HOLLOW_SPRUCE_LOG_VERTICAL, BlockBehaviour.Properties.ofFullCopy(Blocks.STRIPPED_SPRUCE_WOOD).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("hollow_spruce_log_climbable")))));
		HOLLOW_BIRCH_LOG_CLIMBABLE = Registry.register(BuiltInRegistries.BLOCK, ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("hollow_birch_log_climbable")), new ClimbableHollowLogBlock(() -> HOLLOW_BIRCH_LOG_VERTICAL, BlockBehaviour.Properties.ofFullCopy(Blocks.STRIPPED_BIRCH_WOOD).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("hollow_birch_log_climbable")))));
		HOLLOW_JUNGLE_LOG_CLIMBABLE = Registry.register(BuiltInRegistries.BLOCK, ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("hollow_jungle_log_climbable")), new ClimbableHollowLogBlock(() -> HOLLOW_JUNGLE_LOG_VERTICAL, BlockBehaviour.Properties.ofFullCopy(Blocks.STRIPPED_JUNGLE_WOOD).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("hollow_jungle_log_climbable")))));
		HOLLOW_ACACIA_LOG_CLIMBABLE = Registry.register(BuiltInRegistries.BLOCK, ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("hollow_acacia_log_climbable")), new ClimbableHollowLogBlock(() -> HOLLOW_ACACIA_LOG_VERTICAL, BlockBehaviour.Properties.ofFullCopy(Blocks.STRIPPED_ACACIA_WOOD).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("hollow_acacia_log_climbable")))));
		HOLLOW_DARK_OAK_LOG_CLIMBABLE = Registry.register(BuiltInRegistries.BLOCK, ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("hollow_dark_oak_log_climbable")), new ClimbableHollowLogBlock(() -> HOLLOW_DARK_OAK_LOG_VERTICAL, BlockBehaviour.Properties.ofFullCopy(Blocks.STRIPPED_DARK_OAK_WOOD).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("hollow_dark_oak_log_climbable")))));
		HOLLOW_CRIMSON_STEM_CLIMBABLE = Registry.register(BuiltInRegistries.BLOCK, ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("hollow_crimson_stem_climbable")), new ClimbableHollowLogBlock(() -> HOLLOW_CRIMSON_STEM_VERTICAL, BlockBehaviour.Properties.ofFullCopy(Blocks.STRIPPED_CRIMSON_HYPHAE).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("hollow_crimson_stem_climbable")))));
		HOLLOW_WARPED_STEM_CLIMBABLE = Registry.register(BuiltInRegistries.BLOCK, ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("hollow_warped_stem_climbable")), new ClimbableHollowLogBlock(() -> HOLLOW_WARPED_STEM_VERTICAL, BlockBehaviour.Properties.ofFullCopy(Blocks.STRIPPED_WARPED_HYPHAE).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("hollow_warped_stem_climbable")))));
		HOLLOW_VANGROVE_LOG_CLIMBABLE = Registry.register(BuiltInRegistries.BLOCK, ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("hollow_vangrove_log_climbable")), new ClimbableHollowLogBlock(() -> HOLLOW_VANGROVE_LOG_VERTICAL, BlockBehaviour.Properties.of().ignitedByLava().mapColor(MapColor.COLOR_RED).strength(2.0F).sound(SoundType.WOOD).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("hollow_vangrove_log_climbable")))));
		HOLLOW_CHERRY_LOG_CLIMBABLE = Registry.register(BuiltInRegistries.BLOCK, ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("hollow_cherry_log_climbable")), new ClimbableHollowLogBlock(() -> HOLLOW_CHERRY_LOG_VERTICAL, BlockBehaviour.Properties.ofFullCopy(Blocks.STRIPPED_CHERRY_WOOD).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("hollow_cherry_log_climbable")))));
		HOLLOW_PALE_OAK_LOG_CLIMBABLE = Registry.register(BuiltInRegistries.BLOCK, ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("hollow_pale_oak_log_climbable")), new ClimbableHollowLogBlock(() -> HOLLOW_PALE_OAK_LOG_VERTICAL, BlockBehaviour.Properties.ofFullCopy(Blocks.STRIPPED_PALE_OAK_WOOD).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("hollow_pale_oak_log_climbable")))));
	}

public static final RotatedPillarBlock TWILIGHT_OAK_WOOD = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("twilight_oak_wood")),
		new RotatedPillarBlock(TWILIGHT_OAK_BARK_PROPS.setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("twilight_oak_wood"))))
	);
public static final RotatedPillarBlock CANOPY_WOOD = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("canopy_wood")),
		new RotatedPillarBlock(CANOPY_BARK_PROPS.setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("canopy_wood"))))
	);
public static final RotatedPillarBlock MANGROVE_WOOD = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("mangrove_wood")),
		new RotatedPillarBlock(MANGROVE_BARK_PROPS.setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("mangrove_wood"))))
	);
public static final RotatedPillarBlock DARK_WOOD = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("dark_wood")),
		new RotatedPillarBlock(DARK_BARK_PROPS.setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("dark_wood"))))
	);
public static final RotatedPillarBlock TIME_WOOD = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("time_wood")),
		new RotatedPillarBlock(TIME_BARK_PROPS.setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("time_wood"))))
	);
public static final RotatedPillarBlock TRANSFORMATION_WOOD = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("transformation_wood")),
		new RotatedPillarBlock(TRANSFORMATION_BARK_PROPS.setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("transformation_wood"))))
	);
public static final RotatedPillarBlock MINING_WOOD = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("mining_wood")),
		new RotatedPillarBlock(MINING_BARK_PROPS.setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("mining_wood"))))
	);
public static final RotatedPillarBlock SORTING_WOOD = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("sorting_wood")),
		new RotatedPillarBlock(SORTING_BARK_PROPS.setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("sorting_wood"))))
	);

public static final RotatedPillarBlock STRIPPED_TWILIGHT_OAK_WOOD = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("stripped_twilight_oak_wood")),
		new RotatedPillarBlock(TWILIGHT_OAK_STRIPPED_PROPS.setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("stripped_twilight_oak_wood"))))
	);
public static final RotatedPillarBlock STRIPPED_CANOPY_WOOD = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("stripped_canopy_wood")),
		new RotatedPillarBlock(CANOPY_STRIPPED_PROPS.setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("stripped_canopy_wood"))))
	);
public static final RotatedPillarBlock STRIPPED_MANGROVE_WOOD = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("stripped_mangrove_wood")),
		new RotatedPillarBlock(MANGROVE_STRIPPED_PROPS.setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("stripped_mangrove_wood"))))
	);
public static final RotatedPillarBlock STRIPPED_DARK_WOOD = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("stripped_dark_wood")),
		new RotatedPillarBlock(DARK_STRIPPED_PROPS.setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("stripped_dark_wood"))))
	);
public static final RotatedPillarBlock STRIPPED_TIME_WOOD = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("stripped_time_wood")),
		new RotatedPillarBlock(TIME_STRIPPED_PROPS.setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("stripped_time_wood"))))
	);
public static final RotatedPillarBlock STRIPPED_TRANSFORMATION_WOOD = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("stripped_transformation_wood")),
		new RotatedPillarBlock(TRANSFORMATION_STRIPPED_PROPS.setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("stripped_transformation_wood"))))
	);
public static final RotatedPillarBlock STRIPPED_MINING_WOOD = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("stripped_mining_wood")),
		new RotatedPillarBlock(MINING_STRIPPED_PROPS.setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("stripped_mining_wood"))))
	);
public static final RotatedPillarBlock STRIPPED_SORTING_WOOD = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("stripped_sorting_wood")),
		new RotatedPillarBlock(SORTING_STRIPPED_PROPS.setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("stripped_sorting_wood"))))
	);

public static final Block TIME_LOG_CORE = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("time_log_core")),
		new TimeLogCoreBlock(TIME_LOG_PROPS.setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("time_log_core"))))
	);
public static final Block TRANSFORMATION_LOG_CORE = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("transformation_log_core")),
		new TransLogCoreBlock(TRANSFORMATION_LOG_PROPS.setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("transformation_log_core"))))
	);
public static final Block MINING_LOG_CORE = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("mining_log_core")),
		new MineLogCoreBlock(MINING_LOG_PROPS.setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("mining_log_core"))))
	);
public static final Block SORTING_LOG_CORE = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("sorting_log_core")),
		new SortLogCoreBlock(SORTING_LOG_PROPS.setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("sorting_log_core"))))
	);

public static final Block MANGROVE_ROOT = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("mangrove_root")),
		new Block(BlockBehaviour.Properties.of().ignitedByLava().instrument(NoteBlockInstrument.BASS).mapColor(MapColor.STONE).sound(SoundType.WOOD).strength(2.0F).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("mangrove_root"))))
	);

public static final Block TWILIGHT_OAK_LEAVES = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("twilight_oak_leaves")),
		new TintedParticleLeavesBlock(0.01F, BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).ignitedByLava().pushReaction(PushReaction.DESTROY).strength(0.2F).randomTicks().noOcclusion().sound(SoundType.AZALEA_LEAVES).isSuffocating((state, getter, pos) -> false).isViewBlocking((state, getter, pos) -> false).isRedstoneConductor((state, level, pos) -> false).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("twilight_oak_leaves"))))
	);
public static final Block CANOPY_LEAVES = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("canopy_leaves")),
		new TintedParticleLeavesBlock(0.01F, BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).ignitedByLava().pushReaction(PushReaction.DESTROY).strength(0.2F).randomTicks().noOcclusion().sound(SoundType.AZALEA_LEAVES).isSuffocating((state, getter, pos) -> false).isViewBlocking((state, getter, pos) -> false).isRedstoneConductor((state, level, pos) -> false).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("canopy_leaves"))))
	);
public static final Block MANGROVE_LEAVES = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("mangrove_leaves")),
		new TintedParticleLeavesBlock(0.01F, BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).ignitedByLava().pushReaction(PushReaction.DESTROY).strength(0.2F).randomTicks().noOcclusion().sound(SoundType.AZALEA_LEAVES).isSuffocating((state, getter, pos) -> false).isViewBlocking((state, getter, pos) -> false).isRedstoneConductor((state, level, pos) -> false).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("mangrove_leaves"))))
	);
public static final Block DARK_LEAVES = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("dark_leaves")),
		new DarkLeavesBlock(BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).ignitedByLava().pushReaction(PushReaction.DESTROY).strength(2.0F, 10.0F).sound(SoundType.AZALEA_LEAVES).isSuffocating((state, getter, pos) -> false).isViewBlocking((state, getter, pos) -> false).isRedstoneConductor((state, level, pos) -> false).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("dark_leaves"))))
	);
public static final Block HARDENED_DARK_LEAVES = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("hardened_dark_leaves")),
		new HardenedDarkLeavesBlock(BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).ignitedByLava().pushReaction(PushReaction.DESTROY).strength(2.0F, 10.0F).sound(SoundType.AZALEA_LEAVES).isValidSpawn(TFBlocks::noSpawning).isRedstoneConductor((state, level, pos) -> false).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("hardened_dark_leaves"))))
	);
public static final Block RAINBOW_OAK_LEAVES = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("rainbow_oak_leaves")),
		new TintedParticleLeavesBlock(0.01F, BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).ignitedByLava().pushReaction(PushReaction.DESTROY).strength(0.2F).randomTicks().noOcclusion().sound(SoundType.AZALEA_LEAVES).isSuffocating((state, getter, pos) -> false).isViewBlocking((state, getter, pos) -> false).isRedstoneConductor((state, level, pos) -> false).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("rainbow_oak_leaves"))))
	);
public static final Block TIME_LEAVES = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("time_leaves")),
		new TintedParticleLeavesBlock(0.01F, BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).ignitedByLava().pushReaction(PushReaction.DESTROY).strength(0.2F).sound(SoundType.AZALEA_LEAVES).randomTicks().noOcclusion().isSuffocating((state, getter, pos) -> false).isViewBlocking((state, getter, pos) -> false).isRedstoneConductor((state, level, pos) -> false).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("time_leaves"))))
	);
public static final Block TRANSFORMATION_LEAVES = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("transformation_leaves")),
		new TransformationLeavesBlock(BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).ignitedByLava().pushReaction(PushReaction.DESTROY).strength(0.2F).sound(SoundType.AZALEA_LEAVES).randomTicks().noOcclusion().isSuffocating((state, getter, pos) -> false).isViewBlocking((state, getter, pos) -> false).isRedstoneConductor((state, level, pos) -> false).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("transformation_leaves"))))
	);
public static final Block MINING_LEAVES = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("mining_leaves")),
		new TintedParticleLeavesBlock(0.01F, BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).ignitedByLava().pushReaction(PushReaction.DESTROY).strength(0.2F).sound(SoundType.AZALEA_LEAVES).randomTicks().noOcclusion().isSuffocating((state, getter, pos) -> false).isViewBlocking((state, getter, pos) -> false).isRedstoneConductor((state, level, pos) -> false).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("mining_leaves"))))
	);
public static final Block SORTING_LEAVES = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("sorting_leaves")),
		new TintedParticleLeavesBlock(0.01F, BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).ignitedByLava().pushReaction(PushReaction.DESTROY).strength(0.2F).sound(SoundType.AZALEA_LEAVES).randomTicks().noOcclusion().isSuffocating((state, getter, pos) -> false).isViewBlocking((state, getter, pos) -> false).isRedstoneConductor((state, level, pos) -> false).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("sorting_leaves"))))
	);

public static final SaplingBlock TWILIGHT_OAK_SAPLING = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("twilight_oak_sapling")),
		new TFSaplingBlock(TFTreeGrowers.TWILIGHT_OAK, BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).pushReaction(PushReaction.DESTROY).instabreak().sound(SoundType.GRASS).noCollision().randomTicks().setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("twilight_oak_sapling"))),
			new TFSaplingBlock.TreePlacement(
				() -> {
					// secondaryChance 0.1 : 90% normal oak, 10% large oak
					if (net.minecraft.util.RandomSource.create().nextFloat() < 0.1F) {
						return TFSaplingBlock.LARGE_TWILIGHT_OAK_TREE.get();
					}
					return TFSaplingBlock.TWILIGHT_OAK_TREE.get();
				},
				() -> {
					// 2x2 mega sapling variant: 50/50 forest vs savannah mega oak
					net.minecraft.util.RandomSource r = net.minecraft.util.RandomSource.create();
					if (r.nextFloat() < 0.5F) return TFSaplingBlock.MEGA_TWILIGHT_OAK.get();
					return TFSaplingBlock.SAVANNAH_MEGA_OAK.get();
				}
			))
	);
public static final SaplingBlock CANOPY_SAPLING = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("canopy_sapling")),
		new TFSaplingBlock(TFTreeGrowers.CANOPY, BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).pushReaction(PushReaction.DESTROY).instabreak().sound(SoundType.GRASS).noCollision().randomTicks().setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("canopy_sapling"))),
			new TFSaplingBlock.TreePlacement(TFSaplingBlock.CANOPY_TREE, TFSaplingBlock.MEGA_CANOPY_TREE))
	);
public static final SaplingBlock MANGROVE_SAPLING = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("mangrove_sapling")),
		new MangroveSaplingBlock(TFTreeGrowers.MANGROVE, BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).pushReaction(PushReaction.DESTROY).instabreak().sound(SoundType.GRASS).noCollision().randomTicks().setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("mangrove_sapling"))))
	);
public static final SaplingBlock DARKWOOD_SAPLING = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("darkwood_sapling")),
		new TFSaplingBlock(TFTreeGrowers.DARK, BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).pushReaction(PushReaction.DESTROY).instabreak().sound(SoundType.GRASS).noCollision().randomTicks().setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("darkwood_sapling"))),
			TFSaplingBlock.TreePlacement.single(TFSaplingBlock.DARKWOOD_TREE))
	);
public static final SaplingBlock HOLLOW_OAK_SAPLING = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("hollow_oak_sapling")),
		new HollowOakSaplingBlock(BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).pushReaction(PushReaction.DESTROY).instabreak().sound(SoundType.GRASS).noCollision().randomTicks().setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("hollow_oak_sapling"))))
	);
public static final SaplingBlock TIME_SAPLING = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("time_sapling")),
		new TFSaplingBlock(TFTreeGrowers.TIME, BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).pushReaction(PushReaction.DESTROY).instabreak().sound(SoundType.GRASS).noCollision().randomTicks().setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("time_sapling"))),
			TFSaplingBlock.TreePlacement.single(TFSaplingBlock.TIME_TREE))
	);
public static final SaplingBlock TRANSFORMATION_SAPLING = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("transformation_sapling")),
		new TFSaplingBlock(TFTreeGrowers.TRANSFORMATION, BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).pushReaction(PushReaction.DESTROY).instabreak().sound(SoundType.GRASS).noCollision().randomTicks().setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("transformation_sapling"))),
			TFSaplingBlock.TreePlacement.single(TFSaplingBlock.TRANSFORMATION_TREE))
	);
public static final SaplingBlock MINING_SAPLING = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("mining_sapling")),
		new TFSaplingBlock(TFTreeGrowers.MINING, BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).pushReaction(PushReaction.DESTROY).instabreak().sound(SoundType.GRASS).noCollision().randomTicks().setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("mining_sapling"))),
			TFSaplingBlock.TreePlacement.single(TFSaplingBlock.MINING_TREE))
	);
public static final SaplingBlock SORTING_SAPLING = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("sorting_sapling")),
		new TFSaplingBlock(TFTreeGrowers.SORTING, BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).pushReaction(PushReaction.DESTROY).instabreak().sound(SoundType.GRASS).noCollision().randomTicks().setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("sorting_sapling"))),
			TFSaplingBlock.TreePlacement.single(TFSaplingBlock.SORTING_TREE))
	);
public static final SaplingBlock RAINBOW_OAK_SAPLING = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("rainbow_oak_sapling")),
		new TFSaplingBlock(TFTreeGrowers.RAINBOW_OAK, BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).pushReaction(PushReaction.DESTROY).instabreak().sound(SoundType.GRASS).noCollision().randomTicks().setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("rainbow_oak_sapling"))),
			new TFSaplingBlock.TreePlacement(
				() -> {
					// 10% tall rainbow oak chance, matching the secondaryChance=0.1 of the grower
					if (net.minecraft.util.RandomSource.create().nextFloat() < 0.1F) {
						return TFSaplingBlock.LARGE_RAINBOW_OAK_TREE.get();
					}
					return TFSaplingBlock.RAINBOW_OAK_TREE.get();
				},
				null
			))
	);

public static final Block TWILIGHT_OAK_PLANKS = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("twilight_oak_planks")),
		new Block(BlockBehaviour.Properties.of().ignitedByLava().instrument(NoteBlockInstrument.BASS).mapColor(MapColor.WOOD).strength(2.0F, 3.0F).sound(SoundType.WOOD).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("twilight_oak_planks"))))
	);
public static final StairBlock TWILIGHT_OAK_STAIRS = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("twilight_oak_stairs")),
		new StairBlock(TWILIGHT_OAK_PLANKS.defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(TWILIGHT_OAK_PLANKS).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("twilight_oak_stairs"))))
	);
public static final Block TWILIGHT_OAK_SLAB = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("twilight_oak_slab")),
		new SlabBlock(BlockBehaviour.Properties.ofFullCopy(TWILIGHT_OAK_PLANKS).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("twilight_oak_slab"))))
	);
public static final Block TWILIGHT_OAK_BUTTON = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("twilight_oak_button")),
		new ButtonBlock(TFWoodTypes.TWILIGHT_OAK_SET, 30, BlockBehaviour.Properties.ofFullCopy(TWILIGHT_OAK_PLANKS).noCollision().strength(0.5F).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("twilight_oak_button"))))
	);
public static final Block TWILIGHT_OAK_FENCE = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("twilight_oak_fence")),
		new FenceBlock(BlockBehaviour.Properties.ofFullCopy(TWILIGHT_OAK_PLANKS).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("twilight_oak_fence"))))
	);
public static final Block TWILIGHT_OAK_GATE = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("twilight_oak_gate")),
		new FenceGateBlock(TFWoodTypes.TWILIGHT_OAK_WOOD_TYPE, BlockBehaviour.Properties.ofFullCopy(TWILIGHT_OAK_PLANKS).forceSolidOn().setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("twilight_oak_gate"))))
	);
public static final Block TWILIGHT_OAK_PLATE = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("twilight_oak_pressure_plate")),
		new PressurePlateBlock(TFWoodTypes.TWILIGHT_OAK_SET, BlockBehaviour.Properties.ofFullCopy(TWILIGHT_OAK_PLANKS).forceSolidOn().noCollision().strength(0.5F).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("twilight_oak_pressure_plate"))))
	);
public static final DoorBlock TWILIGHT_OAK_DOOR = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("twilight_oak_door")),
		new DoorBlock(TFWoodTypes.TWILIGHT_OAK_SET, BlockBehaviour.Properties.ofFullCopy(TWILIGHT_OAK_PLANKS).strength(3.0F).sound(SoundType.WOOD).noOcclusion().setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("twilight_oak_door"))))
	);
public static final TrapDoorBlock TWILIGHT_OAK_TRAPDOOR = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("twilight_oak_trapdoor")),
		new TrapDoorBlock(TFWoodTypes.TWILIGHT_OAK_SET, BlockBehaviour.Properties.ofFullCopy(TWILIGHT_OAK_PLANKS).strength(3.0F).noOcclusion().setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("twilight_oak_trapdoor"))))
	);
public static final StandingSignBlock TWILIGHT_OAK_SIGN = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("twilight_oak_sign")),
		new StandingSignBlock(TFWoodTypes.TWILIGHT_OAK_WOOD_TYPE, BlockBehaviour.Properties.ofFullCopy(TWILIGHT_OAK_PLANKS).strength(3.0F).noOcclusion().noCollision().setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("twilight_oak_sign"))))
	);
public static final WallSignBlock TWILIGHT_WALL_SIGN = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("twilight_wall_sign")),
		new WallSignBlock(TFWoodTypes.TWILIGHT_OAK_WOOD_TYPE, BlockBehaviour.Properties.ofFullCopy(TWILIGHT_OAK_PLANKS).strength(3.0F).noOcclusion().noCollision().setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("twilight_wall_sign"))))
	);
public static final CeilingHangingSignBlock TWILIGHT_OAK_HANGING_SIGN = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("twilight_oak_hanging_sign")),
		new CeilingHangingSignBlock(TFWoodTypes.TWILIGHT_OAK_WOOD_TYPE, BlockBehaviour.Properties.ofFullCopy(TWILIGHT_OAK_PLANKS).noCollision().strength(1.0F).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("twilight_oak_hanging_sign"))))
	);
public static final WallHangingSignBlock TWILIGHT_OAK_WALL_HANGING_SIGN = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("twilight_oak_wall_hanging_sign")),
		new WallHangingSignBlock(TFWoodTypes.TWILIGHT_OAK_WOOD_TYPE, BlockBehaviour.Properties.ofFullCopy(TWILIGHT_OAK_PLANKS).noCollision().strength(1.0F).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("twilight_oak_wall_hanging_sign"))))
	);
public static final BanisterBlock TWILIGHT_OAK_BANISTER = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("twilight_oak_banister")),
		new BanisterBlock(BlockBehaviour.Properties.ofFullCopy(TWILIGHT_OAK_PLANKS).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("twilight_oak_banister"))))
	);
public static final DryingRackBlock TWILIGHT_OAK_DRYING_RACK = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("twilight_oak_drying_rack")),
		new DryingRackBlock(copyAndScaleProperties(TWILIGHT_OAK_SLAB, 0.5F).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("twilight_oak_drying_rack"))))
	);

public static final Block CANOPY_PLANKS = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("canopy_planks")),
		new Block(BlockBehaviour.Properties.of().ignitedByLava().instrument(NoteBlockInstrument.BASS).mapColor(MapColor.PODZOL).strength(2.0F, 3.0F).sound(SoundType.WOOD).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("canopy_planks"))))
	);
public static final StairBlock CANOPY_STAIRS = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("canopy_stairs")),
		new StairBlock(CANOPY_PLANKS.defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(CANOPY_PLANKS).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("canopy_stairs"))))
	);
public static final Block CANOPY_SLAB = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("canopy_slab")),
		new SlabBlock(BlockBehaviour.Properties.ofFullCopy(CANOPY_PLANKS).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("canopy_slab"))))
	);
public static final Block CANOPY_BUTTON = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("canopy_button")),
		new ButtonBlock(TFWoodTypes.CANOPY_WOOD_SET, 30, BlockBehaviour.Properties.ofFullCopy(CANOPY_PLANKS).noCollision().strength(0.5F).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("canopy_button"))))
	);
public static final Block CANOPY_FENCE = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("canopy_fence")),
		new FenceBlock(BlockBehaviour.Properties.ofFullCopy(CANOPY_PLANKS).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("canopy_fence"))))
	);
public static final Block CANOPY_GATE = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("canopy_gate")),
		new FenceGateBlock(TFWoodTypes.CANOPY_WOOD_TYPE, BlockBehaviour.Properties.ofFullCopy(CANOPY_PLANKS).forceSolidOn().setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("canopy_gate"))))
	);
public static final Block CANOPY_PLATE = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("canopy_pressure_plate")),
		new PressurePlateBlock(TFWoodTypes.CANOPY_WOOD_SET, BlockBehaviour.Properties.ofFullCopy(CANOPY_PLANKS).forceSolidOn().noCollision().strength(0.5F).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("canopy_pressure_plate"))))
	);
public static final DoorBlock CANOPY_DOOR = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("canopy_door")),
		new DoorBlock(TFWoodTypes.CANOPY_WOOD_SET, BlockBehaviour.Properties.ofFullCopy(CANOPY_PLANKS).strength(3.0F).noOcclusion().setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("canopy_door"))))
	);
public static final TrapDoorBlock CANOPY_TRAPDOOR = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("canopy_trapdoor")),
		new TrapDoorBlock(TFWoodTypes.CANOPY_WOOD_SET, BlockBehaviour.Properties.of().ignitedByLava().mapColor(MapColor.SAND).strength(3.0F).sound(SoundType.WOOD).noOcclusion().setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("canopy_trapdoor"))))
	);
public static final StandingSignBlock CANOPY_SIGN = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("canopy_sign")),
		new StandingSignBlock(TFWoodTypes.CANOPY_WOOD_TYPE, BlockBehaviour.Properties.ofFullCopy(CANOPY_PLANKS).strength(1.0F).noOcclusion().noCollision().setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("canopy_sign"))))
	);
public static final WallSignBlock CANOPY_WALL_SIGN = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("canopy_wall_sign")),
		new WallSignBlock(TFWoodTypes.CANOPY_WOOD_TYPE, BlockBehaviour.Properties.ofFullCopy(CANOPY_PLANKS).strength(1.0F).noOcclusion().noCollision().setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("canopy_wall_sign"))))
	);
public static final Block CANOPY_BOOKSHELF = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("canopy_bookshelf")),
		new Block(BlockBehaviour.Properties.ofFullCopy(CANOPY_PLANKS).strength(1.5F).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("canopy_bookshelf"))))
	);
public static final CeilingHangingSignBlock CANOPY_HANGING_SIGN = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("canopy_hanging_sign")),
		new CeilingHangingSignBlock(TFWoodTypes.CANOPY_WOOD_TYPE, BlockBehaviour.Properties.ofFullCopy(CANOPY_PLANKS).noCollision().strength(1.0F).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("canopy_hanging_sign"))))
	);
public static final WallHangingSignBlock CANOPY_WALL_HANGING_SIGN = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("canopy_wall_hanging_sign")),
		new WallHangingSignBlock(TFWoodTypes.CANOPY_WOOD_TYPE, BlockBehaviour.Properties.ofFullCopy(CANOPY_PLANKS).noCollision().strength(1.0F).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("canopy_wall_hanging_sign"))))
	);
public static final BanisterBlock CANOPY_BANISTER = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("canopy_banister")),
		new BanisterBlock(BlockBehaviour.Properties.ofFullCopy(CANOPY_PLANKS).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("canopy_banister"))))
	);
public static final DryingRackBlock CANOPY_DRYING_RACK = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("canopy_drying_rack")),
		new DryingRackBlock(copyAndScaleProperties(CANOPY_SLAB, 0.5F).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("canopy_drying_rack"))))
	);

public static final Block MANGROVE_PLANKS = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("mangrove_planks")),
		new Block(BlockBehaviour.Properties.of().ignitedByLava().instrument(NoteBlockInstrument.BASS).mapColor(MapColor.DIRT).strength(2.0F, 3.0F).sound(SoundType.WOOD).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("mangrove_planks"))))
	);
public static final StairBlock MANGROVE_STAIRS = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("mangrove_stairs")),
		new StairBlock(MANGROVE_PLANKS.defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(MANGROVE_PLANKS).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("mangrove_stairs"))))
	);
public static final Block MANGROVE_SLAB = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("mangrove_slab")),
		new SlabBlock(BlockBehaviour.Properties.ofFullCopy(MANGROVE_PLANKS).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("mangrove_slab"))))
	);
public static final Block MANGROVE_BUTTON = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("mangrove_button")),
		new ButtonBlock(TFWoodTypes.MANGROVE_WOOD_SET, 30, BlockBehaviour.Properties.ofFullCopy(MANGROVE_PLANKS).noCollision().strength(0.5F).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("mangrove_button"))))
	);
public static final Block MANGROVE_FENCE = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("mangrove_fence")),
		new FenceBlock(BlockBehaviour.Properties.ofFullCopy(MANGROVE_PLANKS).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("mangrove_fence"))))
	);
public static final Block MANGROVE_GATE = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("mangrove_gate")),
		new FenceGateBlock(TFWoodTypes.MANGROVE_WOOD_TYPE, BlockBehaviour.Properties.ofFullCopy(MANGROVE_PLANKS).forceSolidOn().setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("mangrove_gate"))))
	);
public static final Block MANGROVE_PLATE = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("mangrove_pressure_plate")),
		new PressurePlateBlock(TFWoodTypes.MANGROVE_WOOD_SET, BlockBehaviour.Properties.ofFullCopy(MANGROVE_PLANKS).forceSolidOn().noCollision().strength(0.5F).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("mangrove_pressure_plate"))))
	);
public static final DoorBlock MANGROVE_DOOR = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("mangrove_door")),
		new DoorBlock(TFWoodTypes.MANGROVE_WOOD_SET, BlockBehaviour.Properties.ofFullCopy(MANGROVE_PLANKS).strength(3.0F).noOcclusion().setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("mangrove_door"))))
	);
public static final TrapDoorBlock MANGROVE_TRAPDOOR = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("mangrove_trapdoor")),
		new TrapDoorBlock(TFWoodTypes.MANGROVE_WOOD_SET, BlockBehaviour.Properties.ofFullCopy(MANGROVE_PLANKS).strength(3.0F).noOcclusion().setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("mangrove_trapdoor"))))
	);
public static final StandingSignBlock MANGROVE_SIGN = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("mangrove_sign")),
		new StandingSignBlock(TFWoodTypes.MANGROVE_WOOD_TYPE, BlockBehaviour.Properties.ofFullCopy(MANGROVE_PLANKS).strength(1.0F).noOcclusion().noCollision().setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("mangrove_sign"))))
	);
public static final WallSignBlock MANGROVE_WALL_SIGN = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("mangrove_wall_sign")),
		new WallSignBlock(TFWoodTypes.MANGROVE_WOOD_TYPE, BlockBehaviour.Properties.ofFullCopy(MANGROVE_PLANKS).strength(1.0F).noOcclusion().noCollision().setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("mangrove_wall_sign"))))
	);
public static final CeilingHangingSignBlock MANGROVE_HANGING_SIGN = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("mangrove_hanging_sign")),
		new CeilingHangingSignBlock(TFWoodTypes.MANGROVE_WOOD_TYPE, BlockBehaviour.Properties.ofFullCopy(MANGROVE_PLANKS).noCollision().strength(1.0F).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("mangrove_hanging_sign"))))
	);
public static final WallHangingSignBlock MANGROVE_WALL_HANGING_SIGN = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("mangrove_wall_hanging_sign")),
		new WallHangingSignBlock(TFWoodTypes.MANGROVE_WOOD_TYPE, BlockBehaviour.Properties.ofFullCopy(MANGROVE_PLANKS).noCollision().strength(1.0F).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("mangrove_wall_hanging_sign"))))
	);
public static final BanisterBlock MANGROVE_BANISTER = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("mangrove_banister")),
		new BanisterBlock(BlockBehaviour.Properties.ofFullCopy(MANGROVE_PLANKS).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("mangrove_banister"))))
	);
public static final DryingRackBlock MANGROVE_DRYING_RACK = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("mangrove_drying_rack")),
		new DryingRackBlock(copyAndScaleProperties(MANGROVE_SLAB, 0.5F).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("mangrove_drying_rack"))))
	);

public static final Block DARK_PLANKS = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("dark_planks")),
		new Block(BlockBehaviour.Properties.of().ignitedByLava().instrument(NoteBlockInstrument.BASS).mapColor(MapColor.COLOR_ORANGE).strength(2.0F, 3.0F).sound(SoundType.WOOD).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("dark_planks"))))
	);
public static final StairBlock DARK_STAIRS = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("dark_stairs")),
		new StairBlock(DARK_PLANKS.defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(DARK_PLANKS).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("dark_stairs"))))
	);
public static final Block DARK_SLAB = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("dark_slab")),
		new SlabBlock(BlockBehaviour.Properties.ofFullCopy(DARK_PLANKS).sound(SoundType.WOOD).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("dark_slab"))))
	);
public static final Block DARK_BUTTON = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("dark_button")),
		new ButtonBlock(TFWoodTypes.DARK_WOOD_SET, 30, BlockBehaviour.Properties.ofFullCopy(DARK_PLANKS).noCollision().strength(0.5F).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("dark_button"))))
	);
public static final Block DARK_FENCE = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("dark_fence")),
		new FenceBlock(BlockBehaviour.Properties.ofFullCopy(DARK_PLANKS).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("dark_fence"))))
	);
public static final Block DARK_GATE = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("dark_gate")),
		new FenceGateBlock(TFWoodTypes.DARK_WOOD_TYPE, BlockBehaviour.Properties.ofFullCopy(DARK_PLANKS).forceSolidOn().setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("dark_gate"))))
	);
public static final Block DARK_PLATE = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("dark_pressure_plate")),
		new PressurePlateBlock(TFWoodTypes.DARK_WOOD_SET, BlockBehaviour.Properties.ofFullCopy(DARK_PLANKS).forceSolidOn().noCollision().strength(0.5F).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("dark_pressure_plate"))))
	);
public static final DoorBlock DARK_DOOR = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("dark_door")),
		new DoorBlock(TFWoodTypes.DARK_WOOD_SET, BlockBehaviour.Properties.ofFullCopy(DARK_PLANKS).strength(3.0F).sound(SoundType.WOOD).noOcclusion().setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("dark_door"))))
	);
public static final TrapDoorBlock DARK_TRAPDOOR = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("dark_trapdoor")),
		new TrapDoorBlock(TFWoodTypes.DARK_WOOD_SET, BlockBehaviour.Properties.ofFullCopy(DARK_PLANKS).strength(3.0F).noOcclusion().setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("dark_trapdoor"))))
	);
public static final StandingSignBlock DARK_SIGN = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("dark_sign")),
		new StandingSignBlock(TFWoodTypes.DARK_WOOD_TYPE, BlockBehaviour.Properties.ofFullCopy(DARK_PLANKS).strength(1.0F).noOcclusion().noCollision().setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("dark_sign"))))
	);
public static final WallSignBlock DARK_WALL_SIGN = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("dark_wall_sign")),
		new WallSignBlock(TFWoodTypes.DARK_WOOD_TYPE, BlockBehaviour.Properties.ofFullCopy(DARK_PLANKS).strength(1.0F).noOcclusion().noCollision().setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("dark_wall_sign"))))
	);
public static final CeilingHangingSignBlock DARK_HANGING_SIGN = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("dark_hanging_sign")),
		new CeilingHangingSignBlock(TFWoodTypes.DARK_WOOD_TYPE, BlockBehaviour.Properties.ofFullCopy(DARK_PLANKS).noCollision().strength(1.0F).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("dark_hanging_sign"))))
	);
public static final WallHangingSignBlock DARK_WALL_HANGING_SIGN = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("dark_wall_hanging_sign")),
		new WallHangingSignBlock(TFWoodTypes.DARK_WOOD_TYPE, BlockBehaviour.Properties.ofFullCopy(DARK_PLANKS).noCollision().strength(1.0F).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("dark_wall_hanging_sign"))))
	);
public static final BanisterBlock DARK_BANISTER = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("dark_banister")),
		new BanisterBlock(BlockBehaviour.Properties.ofFullCopy(DARK_PLANKS).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("dark_banister"))))
	);
public static final DryingRackBlock DARK_DRYING_RACK = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("dark_drying_rack")),
		new DryingRackBlock(copyAndScaleProperties(DARK_SLAB, 0.5F).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("dark_drying_rack"))))
	);

public static final Block TIME_PLANKS = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("time_planks")),
		new Block(BlockBehaviour.Properties.of().ignitedByLava().instrument(NoteBlockInstrument.BASS).mapColor(MapColor.DIRT).strength(2.0F, 3.0F).sound(SoundType.WOOD).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("time_planks"))))
	);
public static final StairBlock TIME_STAIRS = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("time_stairs")),
		new StairBlock(TIME_PLANKS.defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(TIME_PLANKS).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("time_stairs"))))
	);
public static final Block TIME_SLAB = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("time_slab")),
		new SlabBlock(BlockBehaviour.Properties.ofFullCopy(TIME_PLANKS).sound(SoundType.WOOD).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("time_slab"))))
	);
public static final Block TIME_BUTTON = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("time_button")),
		new ButtonBlock(TFWoodTypes.TIME_WOOD_SET, 30, BlockBehaviour.Properties.ofFullCopy(TIME_PLANKS).noCollision().strength(0.5F).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("time_button"))))
	);
public static final Block TIME_FENCE = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("time_fence")),
		new FenceBlock(BlockBehaviour.Properties.ofFullCopy(TIME_PLANKS).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("time_fence"))))
	);
public static final Block TIME_GATE = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("time_gate")),
		new FenceGateBlock(TFWoodTypes.TIME_WOOD_TYPE, BlockBehaviour.Properties.ofFullCopy(TIME_PLANKS).forceSolidOn().setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("time_gate"))))
	);
public static final Block TIME_PLATE = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("time_pressure_plate")),
		new PressurePlateBlock(TFWoodTypes.TIME_WOOD_SET, BlockBehaviour.Properties.ofFullCopy(TIME_PLANKS).forceSolidOn().noCollision().strength(0.5F).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("time_pressure_plate"))))
	);
public static final DoorBlock TIME_DOOR = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("time_door")),
		new DoorBlock(TFWoodTypes.TIME_WOOD_SET, BlockBehaviour.Properties.ofFullCopy(TIME_PLANKS).strength(3.0F).sound(SoundType.WOOD).noOcclusion().setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("time_door"))))
	);
public static final TrapDoorBlock TIME_TRAPDOOR = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("time_trapdoor")),
		new TrapDoorBlock(TFWoodTypes.TIME_WOOD_SET, BlockBehaviour.Properties.ofFullCopy(TIME_PLANKS).strength(3.0F).noOcclusion().setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("time_trapdoor"))))
	);
public static final StandingSignBlock TIME_SIGN = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("time_sign")),
		new StandingSignBlock(TFWoodTypes.TIME_WOOD_TYPE, BlockBehaviour.Properties.ofFullCopy(TIME_PLANKS).strength(1.0F).noOcclusion().noCollision().setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("time_sign"))))
	);
public static final WallSignBlock TIME_WALL_SIGN = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("time_wall_sign")),
		new WallSignBlock(TFWoodTypes.TIME_WOOD_TYPE, BlockBehaviour.Properties.ofFullCopy(TIME_PLANKS).strength(1.0F).noOcclusion().noCollision().setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("time_wall_sign"))))
	);
public static final CeilingHangingSignBlock TIME_HANGING_SIGN = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("time_hanging_sign")),
		new CeilingHangingSignBlock(TFWoodTypes.TIME_WOOD_TYPE, BlockBehaviour.Properties.ofFullCopy(TIME_PLANKS).noCollision().strength(1.0F).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("time_hanging_sign"))))
	);
public static final WallHangingSignBlock TIME_WALL_HANGING_SIGN = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("time_wall_hanging_sign")),
		new WallHangingSignBlock(TFWoodTypes.TIME_WOOD_TYPE, BlockBehaviour.Properties.ofFullCopy(TIME_PLANKS).noCollision().strength(1.0F).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("time_wall_hanging_sign"))))
	);
public static final BanisterBlock TIME_BANISTER = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("time_banister")),
		new BanisterBlock(BlockBehaviour.Properties.ofFullCopy(TIME_PLANKS).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("time_banister"))))
	);
public static final DryingRackBlock TIME_DRYING_RACK = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("time_drying_rack")),
		new DryingRackBlock(copyAndScaleProperties(TIME_SLAB, 0.5F).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("time_drying_rack"))))
	);

public static final Block TRANSFORMATION_PLANKS = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("transformation_planks")),
		new Block(BlockBehaviour.Properties.of().ignitedByLava().instrument(NoteBlockInstrument.BASS).mapColor(MapColor.WOOD).strength(2.0F, 3.0F).sound(SoundType.WOOD).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("transformation_planks"))))
	);
public static final StairBlock TRANSFORMATION_STAIRS = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("transformation_stairs")),
		new StairBlock(TRANSFORMATION_PLANKS.defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(TRANSFORMATION_PLANKS).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("transformation_stairs"))))
	);
public static final Block TRANSFORMATION_SLAB = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("transformation_slab")),
		new SlabBlock(BlockBehaviour.Properties.ofFullCopy(TRANSFORMATION_PLANKS).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("transformation_slab"))))
	);
public static final Block TRANSFORMATION_BUTTON = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("transformation_button")),
		new ButtonBlock(TFWoodTypes.TRANSFORMATION_WOOD_SET, 30, BlockBehaviour.Properties.ofFullCopy(TRANSFORMATION_PLANKS).noCollision().strength(0.5F).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("transformation_button"))))
	);
public static final Block TRANSFORMATION_FENCE = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("transformation_fence")),
		new FenceBlock(BlockBehaviour.Properties.ofFullCopy(TRANSFORMATION_PLANKS).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("transformation_fence"))))
	);
public static final Block TRANSFORMATION_GATE = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("transformation_gate")),
		new FenceGateBlock(TFWoodTypes.TRANSFORMATION_WOOD_TYPE, BlockBehaviour.Properties.ofFullCopy(TRANSFORMATION_PLANKS).forceSolidOn().setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("transformation_gate"))))
	);
public static final Block TRANSFORMATION_PLATE = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("transformation_pressure_plate")),
		new PressurePlateBlock(TFWoodTypes.TRANSFORMATION_WOOD_SET, BlockBehaviour.Properties.ofFullCopy(TRANSFORMATION_PLANKS).forceSolidOn().noCollision().strength(0.5F).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("transformation_pressure_plate"))))
	);
public static final DoorBlock TRANSFORMATION_DOOR = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("transformation_door")),
		new DoorBlock(TFWoodTypes.TRANSFORMATION_WOOD_SET, BlockBehaviour.Properties.ofFullCopy(TRANSFORMATION_PLANKS).strength(3.0F).noOcclusion().setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("transformation_door"))))
	);
public static final TrapDoorBlock TRANSFORMATION_TRAPDOOR = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("transformation_trapdoor")),
		new TrapDoorBlock(TFWoodTypes.TRANSFORMATION_WOOD_SET, BlockBehaviour.Properties.ofFullCopy(TRANSFORMATION_PLANKS).strength(3.0F).noOcclusion().setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("transformation_trapdoor"))))
	);
public static final StandingSignBlock TRANSFORMATION_SIGN = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("transformation_sign")),
		new StandingSignBlock(TFWoodTypes.TRANSFORMATION_WOOD_TYPE, BlockBehaviour.Properties.ofFullCopy(TRANSFORMATION_PLANKS).strength(1.0F).noOcclusion().noCollision().setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("transformation_sign"))))
	);
public static final WallSignBlock TRANSFORMATION_WALL_SIGN = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("transformation_wall_sign")),
		new WallSignBlock(TFWoodTypes.TRANSFORMATION_WOOD_TYPE, BlockBehaviour.Properties.ofFullCopy(TRANSFORMATION_PLANKS).strength(1.0F).noOcclusion().noCollision().setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("transformation_wall_sign"))))
	);
public static final CeilingHangingSignBlock TRANSFORMATION_HANGING_SIGN = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("transformation_hanging_sign")),
		new CeilingHangingSignBlock(TFWoodTypes.TRANSFORMATION_WOOD_TYPE, BlockBehaviour.Properties.ofFullCopy(TRANSFORMATION_PLANKS).noCollision().strength(1.0F).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("transformation_hanging_sign"))))
	);
public static final WallHangingSignBlock TRANSFORMATION_WALL_HANGING_SIGN = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("transformation_wall_hanging_sign")),
		new WallHangingSignBlock(TFWoodTypes.TRANSFORMATION_WOOD_TYPE, BlockBehaviour.Properties.ofFullCopy(TRANSFORMATION_PLANKS).noCollision().strength(1.0F).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("transformation_wall_hanging_sign"))))
	);
public static final BanisterBlock TRANSFORMATION_BANISTER = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("transformation_banister")),
		new BanisterBlock(BlockBehaviour.Properties.ofFullCopy(TRANSFORMATION_PLANKS).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("transformation_banister"))))
	);
public static final DryingRackBlock TRANSFORMATION_DRYING_RACK = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("transformation_drying_rack")),
		new DryingRackBlock(copyAndScaleProperties(TRANSFORMATION_SLAB, 0.5F).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("transformation_drying_rack"))))
	);

public static final Block MINING_PLANKS = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("mining_planks")),
		new Block(BlockBehaviour.Properties.of().ignitedByLava().instrument(NoteBlockInstrument.BASS).mapColor(MapColor.SAND).strength(2.0F, 3.0F).sound(SoundType.WOOD).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("mining_planks"))))
	);
public static final StairBlock MINING_STAIRS = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("mining_stairs")),
		new StairBlock(MINING_PLANKS.defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(MINING_PLANKS).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("mining_stairs"))))
	);
public static final Block MINING_SLAB = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("mining_slab")),
		new SlabBlock(BlockBehaviour.Properties.ofFullCopy(MINING_PLANKS).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("mining_slab"))))
	);
public static final Block MINING_BUTTON = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("mining_button")),
		new ButtonBlock(TFWoodTypes.MINING_WOOD_SET, 30, BlockBehaviour.Properties.ofFullCopy(MINING_PLANKS).noCollision().strength(0.5F).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("mining_button"))))
	);
public static final Block MINING_FENCE = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("mining_fence")),
		new FenceBlock(BlockBehaviour.Properties.ofFullCopy(MINING_PLANKS).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("mining_fence"))))
	);
public static final Block MINING_GATE = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("mining_gate")),
		new FenceGateBlock(TFWoodTypes.MINING_WOOD_TYPE, BlockBehaviour.Properties.ofFullCopy(MINING_PLANKS).forceSolidOn().setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("mining_gate"))))
	);
public static final Block MINING_PLATE = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("mining_pressure_plate")),
		new PressurePlateBlock(TFWoodTypes.MINING_WOOD_SET, BlockBehaviour.Properties.ofFullCopy(MINING_PLANKS).forceSolidOn().noCollision().strength(0.5F).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("mining_pressure_plate"))))
	);
public static final DoorBlock MINING_DOOR = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("mining_door")),
		new DoorBlock(TFWoodTypes.MINING_WOOD_SET, BlockBehaviour.Properties.ofFullCopy(MINING_PLANKS).strength(3.0F).noOcclusion().setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("mining_door"))))
	);
public static final TrapDoorBlock MINING_TRAPDOOR = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("mining_trapdoor")),
		new TrapDoorBlock(TFWoodTypes.MINING_WOOD_SET, BlockBehaviour.Properties.ofFullCopy(MINING_PLANKS).strength(3.0F).noOcclusion().setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("mining_trapdoor"))))
	);
public static final StandingSignBlock MINING_SIGN = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("mining_sign")),
		new StandingSignBlock(TFWoodTypes.MINING_WOOD_TYPE, BlockBehaviour.Properties.ofFullCopy(MINING_PLANKS).strength(1.0F).noOcclusion().noCollision().setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("mining_sign"))))
	);
public static final WallSignBlock MINING_WALL_SIGN = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("mining_wall_sign")),
		new WallSignBlock(TFWoodTypes.MINING_WOOD_TYPE, BlockBehaviour.Properties.ofFullCopy(MINING_PLANKS).strength(1.0F).noOcclusion().noCollision().setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("mining_wall_sign"))))
	);
public static final CeilingHangingSignBlock MINING_HANGING_SIGN = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("mining_hanging_sign")),
		new CeilingHangingSignBlock(TFWoodTypes.MINING_WOOD_TYPE, BlockBehaviour.Properties.ofFullCopy(MINING_PLANKS).noCollision().strength(1.0F).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("mining_hanging_sign"))))
	);
public static final WallHangingSignBlock MINING_WALL_HANGING_SIGN = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("mining_wall_hanging_sign")),
		new WallHangingSignBlock(TFWoodTypes.MINING_WOOD_TYPE, BlockBehaviour.Properties.ofFullCopy(MINING_PLANKS).noCollision().strength(1.0F).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("mining_wall_hanging_sign"))))
	);
public static final BanisterBlock MINING_BANISTER = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("mining_banister")),
		new BanisterBlock(BlockBehaviour.Properties.ofFullCopy(MINING_PLANKS).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("mining_banister"))))
	);
public static final DryingRackBlock MINING_DRYING_RACK = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("mining_drying_rack")),
		new DryingRackBlock(copyAndScaleProperties(MINING_SLAB, 0.5F).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("mining_drying_rack"))))
	);

public static final Block SORTING_PLANKS = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("sorting_planks")),
		new Block(BlockBehaviour.Properties.of().ignitedByLava().instrument(NoteBlockInstrument.BASS).mapColor(MapColor.PODZOL).strength(2.0F, 3.0F).sound(SoundType.WOOD).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("sorting_planks"))))
	);
public static final StairBlock SORTING_STAIRS = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("sorting_stairs")),
		new StairBlock(SORTING_PLANKS.defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(SORTING_PLANKS).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("sorting_stairs"))))
	);
public static final Block SORTING_SLAB = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("sorting_slab")),
		new SlabBlock(BlockBehaviour.Properties.ofFullCopy(SORTING_PLANKS).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("sorting_slab"))))
	);
public static final Block SORTING_BUTTON = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("sorting_button")),
		new ButtonBlock(TFWoodTypes.SORTING_WOOD_SET, 30, BlockBehaviour.Properties.ofFullCopy(SORTING_PLANKS).noCollision().strength(0.5F).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("sorting_button"))))
	);
public static final Block SORTING_FENCE = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("sorting_fence")),
		new FenceBlock(BlockBehaviour.Properties.ofFullCopy(SORTING_PLANKS).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("sorting_fence"))))
	);
public static final Block SORTING_GATE = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("sorting_gate")),
		new FenceGateBlock(TFWoodTypes.SORTING_WOOD_TYPE, BlockBehaviour.Properties.ofFullCopy(SORTING_PLANKS).forceSolidOn().setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("sorting_gate"))))
	);
public static final Block SORTING_PLATE = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("sorting_pressure_plate")),
		new PressurePlateBlock(TFWoodTypes.SORTING_WOOD_SET, BlockBehaviour.Properties.ofFullCopy(SORTING_PLANKS).forceSolidOn().noCollision().strength(0.5F).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("sorting_pressure_plate"))))
	);
public static final DoorBlock SORTING_DOOR = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("sorting_door")),
		new DoorBlock(TFWoodTypes.SORTING_WOOD_SET, BlockBehaviour.Properties.ofFullCopy(SORTING_PLANKS).strength(3.0F).noOcclusion().setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("sorting_door"))))
	);
public static final TrapDoorBlock SORTING_TRAPDOOR = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("sorting_trapdoor")),
		new TrapDoorBlock(TFWoodTypes.SORTING_WOOD_SET, BlockBehaviour.Properties.ofFullCopy(SORTING_PLANKS).strength(3.0F).noOcclusion().setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("sorting_trapdoor"))))
	);
public static final StandingSignBlock SORTING_SIGN = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("sorting_sign")),
		new StandingSignBlock(TFWoodTypes.SORTING_WOOD_TYPE, BlockBehaviour.Properties.ofFullCopy(SORTING_PLANKS).strength(1.0F).noOcclusion().noCollision().setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("sorting_sign"))))
	);
public static final WallSignBlock SORTING_WALL_SIGN = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("sorting_wall_sign")),
		new WallSignBlock(TFWoodTypes.SORTING_WOOD_TYPE, BlockBehaviour.Properties.ofFullCopy(SORTING_PLANKS).strength(1.0F).noOcclusion().noCollision().setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("sorting_wall_sign"))))
	);
public static final CeilingHangingSignBlock SORTING_HANGING_SIGN = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("sorting_hanging_sign")),
		new CeilingHangingSignBlock(TFWoodTypes.SORTING_WOOD_TYPE, BlockBehaviour.Properties.ofFullCopy(SORTING_PLANKS).noCollision().strength(1.0F).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("sorting_hanging_sign"))))
	);
public static final WallHangingSignBlock SORTING_WALL_HANGING_SIGN = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("sorting_wall_hanging_sign")),
		new WallHangingSignBlock(TFWoodTypes.SORTING_WOOD_TYPE, BlockBehaviour.Properties.ofFullCopy(SORTING_PLANKS).noCollision().strength(1.0F).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("sorting_wall_hanging_sign"))))
	);
public static final BanisterBlock SORTING_BANISTER = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("sorting_banister")),
		new BanisterBlock(BlockBehaviour.Properties.ofFullCopy(SORTING_PLANKS).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("sorting_banister"))))
	);
public static final DryingRackBlock SORTING_DRYING_RACK = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("sorting_drying_rack")),
		new DryingRackBlock(copyAndScaleProperties(SORTING_SLAB, 0.5F).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("sorting_drying_rack"))))
	);

public static final TFChestBlock TWILIGHT_OAK_CHEST = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("twilight_oak_chest")),
		new TFChestBlock(BlockBehaviour.Properties.ofFullCopy(TWILIGHT_OAK_PLANKS).strength(2.5F).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("twilight_oak_chest"))))
	);
public static final TFChestBlock CANOPY_CHEST = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("canopy_chest")),
		new TFChestBlock(BlockBehaviour.Properties.ofFullCopy(CANOPY_PLANKS).strength(2.5F).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("canopy_chest"))))
	);
public static final TFChestBlock MANGROVE_CHEST = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("mangrove_chest")),
		new TFChestBlock(BlockBehaviour.Properties.ofFullCopy(MANGROVE_PLANKS).strength(2.5F).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("mangrove_chest"))))
	);
public static final TFChestBlock DARK_CHEST = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("dark_chest")),
		new TFChestBlock(BlockBehaviour.Properties.ofFullCopy(DARK_PLANKS).strength(2.5F).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("dark_chest"))))
	);
public static final TFChestBlock TIME_CHEST = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("time_chest")),
		new TFChestBlock(BlockBehaviour.Properties.ofFullCopy(TIME_PLANKS).strength(2.5F).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("time_chest"))))
	);
public static final TFChestBlock TRANSFORMATION_CHEST = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("transformation_chest")),
		new TFChestBlock(BlockBehaviour.Properties.ofFullCopy(TRANSFORMATION_PLANKS).strength(2.5F).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("transformation_chest"))))
	);
public static final TFChestBlock MINING_CHEST = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("mining_chest")),
		new TFChestBlock(BlockBehaviour.Properties.ofFullCopy(MINING_PLANKS).strength(2.5F).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("mining_chest"))))
	);
public static final TFChestBlock SORTING_CHEST = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("sorting_chest")),
		new TFChestBlock(BlockBehaviour.Properties.ofFullCopy(SORTING_PLANKS).strength(2.5F).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("sorting_chest"))))
	);

public static final TFTrappedChestBlock TWILIGHT_OAK_TRAPPED_CHEST = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("twilight_oak_trapped_chest")),
		new TFTrappedChestBlock(BlockBehaviour.Properties.ofFullCopy(TWILIGHT_OAK_PLANKS).strength(2.5F).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("twilight_oak_trapped_chest"))))
	);
public static final TFTrappedChestBlock CANOPY_TRAPPED_CHEST = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("canopy_trapped_chest")),
		new TFTrappedChestBlock(BlockBehaviour.Properties.ofFullCopy(CANOPY_PLANKS).strength(2.5F).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("canopy_trapped_chest"))))
	);
public static final TFTrappedChestBlock MANGROVE_TRAPPED_CHEST = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("mangrove_trapped_chest")),
		new TFTrappedChestBlock(BlockBehaviour.Properties.ofFullCopy(MANGROVE_PLANKS).strength(2.5F).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("mangrove_trapped_chest"))))
	);
public static final TFTrappedChestBlock DARK_TRAPPED_CHEST = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("dark_trapped_chest")),
		new TFTrappedChestBlock(BlockBehaviour.Properties.ofFullCopy(DARK_PLANKS).strength(2.5F).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("dark_trapped_chest"))))
	);
public static final TFTrappedChestBlock TIME_TRAPPED_CHEST = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("time_trapped_chest")),
		new TFTrappedChestBlock(BlockBehaviour.Properties.ofFullCopy(TIME_PLANKS).strength(2.5F).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("time_trapped_chest"))))
	);
public static final TFTrappedChestBlock TRANSFORMATION_TRAPPED_CHEST = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("transformation_trapped_chest")),
		new TFTrappedChestBlock(BlockBehaviour.Properties.ofFullCopy(TRANSFORMATION_PLANKS).strength(2.5F).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("transformation_trapped_chest"))))
	);
public static final TFTrappedChestBlock MINING_TRAPPED_CHEST = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("mining_trapped_chest")),
		new TFTrappedChestBlock(BlockBehaviour.Properties.ofFullCopy(MINING_PLANKS).strength(2.5F).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("mining_trapped_chest"))))
	);
public static final TFTrappedChestBlock SORTING_TRAPPED_CHEST = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("sorting_trapped_chest")),
		new TFTrappedChestBlock(BlockBehaviour.Properties.ofFullCopy(SORTING_PLANKS).strength(2.5F).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("sorting_trapped_chest"))))
	);

	//Flower Pots
public static final FlowerPotBlock POTTED_TWILIGHT_OAK_SAPLING = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("potted_twilight_oak_sapling")),
		new FlowerPotBlock(TWILIGHT_OAK_SAPLING, BlockBehaviour.Properties.ofFullCopy(Blocks.FLOWER_POT).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("potted_twilight_oak_sapling"))))
	);
public static final FlowerPotBlock POTTED_CANOPY_SAPLING = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("potted_canopy_sapling")),
		new FlowerPotBlock(CANOPY_SAPLING, BlockBehaviour.Properties.ofFullCopy(Blocks.FLOWER_POT).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("potted_canopy_sapling"))))
	);
public static final FlowerPotBlock POTTED_MANGROVE_SAPLING = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("potted_mangrove_sapling")),
		new FlowerPotBlock(MANGROVE_SAPLING, BlockBehaviour.Properties.ofFullCopy(Blocks.FLOWER_POT).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("potted_mangrove_sapling"))))
	);
public static final FlowerPotBlock POTTED_DARKWOOD_SAPLING = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("potted_darkwood_sapling")),
		new FlowerPotBlock(DARKWOOD_SAPLING, BlockBehaviour.Properties.ofFullCopy(Blocks.FLOWER_POT).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("potted_darkwood_sapling"))))
	);
public static final FlowerPotBlock POTTED_HOLLOW_OAK_SAPLING = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("potted_hollow_oak_sapling")),
		new FlowerPotBlock(HOLLOW_OAK_SAPLING, BlockBehaviour.Properties.ofFullCopy(Blocks.FLOWER_POT).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("potted_hollow_oak_sapling"))))
	);
public static final FlowerPotBlock POTTED_RAINBOW_OAK_SAPLING = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("potted_rainbow_oak_sapling")),
		new FlowerPotBlock(RAINBOW_OAK_SAPLING, BlockBehaviour.Properties.ofFullCopy(Blocks.FLOWER_POT).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("potted_rainbow_oak_sapling"))))
	);
public static final FlowerPotBlock POTTED_TIME_SAPLING = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("potted_time_sapling")),
		new FlowerPotBlock(TIME_SAPLING, BlockBehaviour.Properties.ofFullCopy(Blocks.FLOWER_POT).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("potted_time_sapling"))))
	);
public static final FlowerPotBlock POTTED_TRANSFORMATION_SAPLING = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("potted_transformation_sapling")),
		new FlowerPotBlock(TRANSFORMATION_SAPLING, BlockBehaviour.Properties.ofFullCopy(Blocks.FLOWER_POT).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("potted_transformation_sapling"))))
	);
public static final FlowerPotBlock POTTED_MINING_SAPLING = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("potted_mining_sapling")),
		new FlowerPotBlock(MINING_SAPLING, BlockBehaviour.Properties.ofFullCopy(Blocks.FLOWER_POT).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("potted_mining_sapling"))))
	);
public static final FlowerPotBlock POTTED_SORTING_SAPLING = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("potted_sorting_sapling")),
		new FlowerPotBlock(SORTING_SAPLING, BlockBehaviour.Properties.ofFullCopy(Blocks.FLOWER_POT).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("potted_sorting_sapling"))))
	);
public static final FlowerPotBlock POTTED_MAYAPPLE = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("potted_mayapple")),
		new FlowerPotBlock(MAYAPPLE, BlockBehaviour.Properties.ofFullCopy(Blocks.FLOWER_POT).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("potted_mayapple"))))
	);
public static final FlowerPotBlock POTTED_FIDDLEHEAD = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("potted_fiddlehead")),
		new FlowerPotBlock(FIDDLEHEAD, BlockBehaviour.Properties.ofFullCopy(Blocks.FLOWER_POT).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("potted_fiddlehead"))))
	);
public static final FlowerPotBlock POTTED_MUSHGLOOM = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("potted_mushgloom")),
		new FlowerPotBlock(MUSHGLOOM, BlockBehaviour.Properties.ofFullCopy(Blocks.FLOWER_POT).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("potted_mushgloom"))))
	);
public static final FlowerPotBlock POTTED_THORN = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("potted_thorn")),
		new SpecialFlowerPotBlock(BROWN_THORNS, BlockBehaviour.Properties.ofFullCopy(Blocks.FLOWER_POT).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("potted_thorn"))))
	);
public static final FlowerPotBlock POTTED_GREEN_THORN = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("potted_green_thorn")),
		new SpecialFlowerPotBlock(GREEN_THORNS, BlockBehaviour.Properties.ofFullCopy(Blocks.FLOWER_POT).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("potted_green_thorn"))))
	);
public static final FlowerPotBlock POTTED_DEAD_THORN = Registry.register(
		BuiltInRegistries.BLOCK,
		ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("potted_dead_thorn")),
		new SpecialFlowerPotBlock(BURNT_THORNS, BlockBehaviour.Properties.ofFullCopy(Blocks.FLOWER_POT).setId(ResourceKey.create(Registries.BLOCK, TwilightForestMod.prefix("potted_dead_thorn"))))
	);

	private static BlockBehaviour.Properties logProperties(MapColor color) {
		return BlockBehaviour.Properties.of().ignitedByLava().instrument(NoteBlockInstrument.BASS).mapColor(color);
	}

	private static BlockBehaviour.Properties logProperties(MapColor top, MapColor side) {
		return BlockBehaviour.Properties.of().ignitedByLava().instrument(NoteBlockInstrument.BASS).mapColor((state) -> state.getValue(RotatedPillarBlock.AXIS) == Direction.Axis.Y ? top : side);
	}

	public static BlockBehaviour.Properties copyAndScaleProperties(BlockBehaviour blockBehaviour, float scale) {
		BlockBehaviour.Properties properties = BlockBehaviour.Properties.ofFullCopy(blockBehaviour);
		return properties.destroyTime(blockBehaviour.defaultDestroyTime() * scale);
	}

	private static boolean noSpawning(BlockState pState, BlockGetter pLevel, BlockPos pPos, EntityType<?> pValue) {
		return false;
	}

	public static void init() {

		// Register BlockItems for blocks that need them
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("acacia_banister"), new BlockItem(ACACIA_BANISTER, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("acacia_banister")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("acacia_drying_rack"), new BlockItem(ACACIA_DRYING_RACK, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("acacia_drying_rack")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("alpha_yeti_boss_spawner"), new BlockItem(ALPHA_YETI_BOSS_SPAWNER, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("alpha_yeti_boss_spawner")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("antibuilder"), new BlockItem(ANTIBUILDER, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("antibuilder")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("arctic_fur_block"), new BlockItem(ARCTIC_FUR_BLOCK, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("arctic_fur_block")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("auroralized_glass"), new BlockItem(AURORALIZED_GLASS, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("auroralized_glass")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("aurora_block"), new BlockItem(AURORA_BLOCK, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("aurora_block")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("aurora_palace_miniature_structure"), new BlockItem(AURORA_PALACE_MINIATURE_STRUCTURE, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("aurora_palace_miniature_structure")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("aurora_pillar"), new BlockItem(AURORA_PILLAR, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("aurora_pillar")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("aurora_slab"), new BlockItem(AURORA_SLAB, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("aurora_slab")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("bamboo_banister"), new BlockItem(BAMBOO_BANISTER, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("bamboo_banister")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("bamboo_drying_rack"), new BlockItem(BAMBOO_DRYING_RACK, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("bamboo_drying_rack")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("beanstalk_leaves"), new BlockItem(BEANSTALK_LEAVES, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("beanstalk_leaves")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("birch_banister"), new BlockItem(BIRCH_BANISTER, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("birch_banister")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("birch_drying_rack"), new BlockItem(BIRCH_DRYING_RACK, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("birch_drying_rack")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("blackberry_bush"), new BlockItem(BLACKBERRY_BUSH, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("blackberry_bush")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("blightberry_bush"), new BlockItem(BLIGHTBERRY_BUSH, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("blightberry_bush")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("blueberry_bush"), new BlockItem(BLUEBERRY_BUSH, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("blueberry_bush")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("blue_castle_door"), new BlockItem(BLUE_CASTLE_DOOR, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("blue_castle_door")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("blue_castle_rune_brick"), new BlockItem(BLUE_CASTLE_RUNE_BRICK, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("blue_castle_rune_brick")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("blue_force_field"), new BlockItem(BLUE_FORCE_FIELD, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("blue_force_field")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("bold_castle_brick_pillar"), new BlockItem(BOLD_CASTLE_BRICK_PILLAR, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("bold_castle_brick_pillar")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("bold_castle_brick_stairs"), new BlockItem(BOLD_CASTLE_BRICK_STAIRS, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("bold_castle_brick_stairs")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("bold_castle_brick_tile"), new BlockItem(BOLD_CASTLE_BRICK_TILE, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("bold_castle_brick_tile")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("bold_stone_pillar"), new BlockItem(BOLD_STONE_PILLAR, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("bold_stone_pillar")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("brazier"), new BlockItem(BRAZIER, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("brazier")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("brown_thorns"), new BlockItem(BROWN_THORNS, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("brown_thorns")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("burnt_thorns"), new BlockItem(BURNT_THORNS, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("burnt_thorns")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("candelabra"), new BlockItem(CANDELABRA, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("candelabra")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("canopy_banister"), new BlockItem(CANOPY_BANISTER, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("canopy_banister")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("canopy_bookshelf"), new BlockItem(CANOPY_BOOKSHELF, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("canopy_bookshelf")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("canopy_button"), new BlockItem(CANOPY_BUTTON, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("canopy_button")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("canopy_chest"), new BlockItem(CANOPY_CHEST, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("canopy_chest")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("canopy_door"), new BlockItem(CANOPY_DOOR, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("canopy_door")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("canopy_drying_rack"), new BlockItem(CANOPY_DRYING_RACK, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("canopy_drying_rack")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("canopy_fence"), new BlockItem(CANOPY_FENCE, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("canopy_fence")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("canopy_gate"), new BlockItem(CANOPY_GATE, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("canopy_gate")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("canopy_leaves"), new BlockItem(CANOPY_LEAVES, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("canopy_leaves")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("canopy_log"), new BlockItem(CANOPY_LOG, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("canopy_log")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("canopy_planks"), new BlockItem(CANOPY_PLANKS, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("canopy_planks")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("canopy_pressure_plate"), new BlockItem(CANOPY_PLATE, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("canopy_pressure_plate")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("canopy_sapling"), new BlockItem(CANOPY_SAPLING, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("canopy_sapling")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("canopy_slab"), new BlockItem(CANOPY_SLAB, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("canopy_slab")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("canopy_stairs"), new BlockItem(CANOPY_STAIRS, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("canopy_stairs")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("canopy_trapdoor"), new BlockItem(CANOPY_TRAPDOOR, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("canopy_trapdoor")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("canopy_trapped_chest"), new BlockItem(CANOPY_TRAPPED_CHEST, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("canopy_trapped_chest")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("canopy_window"), new BlockItem(CANOPY_WINDOW, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("canopy_window")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("canopy_window_pane"), new BlockItem(CANOPY_WINDOW_PANE, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("canopy_window_pane")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("canopy_wood"), new BlockItem(CANOPY_WOOD, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("canopy_wood")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("carminite_block"), new BlockItem(CARMINITE_BLOCK, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("carminite_block")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("carminite_builder"), new BlockItem(CARMINITE_BUILDER, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("carminite_builder")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("carminite_reactor"), new BlockItem(CARMINITE_REACTOR, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("carminite_reactor")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("castle_brick"), new BlockItem(CASTLE_BRICK, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("castle_brick")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("castle_brick_stairs"), new BlockItem(CASTLE_BRICK_STAIRS, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("castle_brick_stairs")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("castle_roof_tile"), new BlockItem(CASTLE_ROOF_TILE, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("castle_roof_tile")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("cherry_banister"), new BlockItem(CHERRY_BANISTER, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("cherry_banister")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("cherry_drying_rack"), new BlockItem(CHERRY_DRYING_RACK, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("cherry_drying_rack")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("chiseled_canopy_bookshelf"), new BlockItem(CHISELED_CANOPY_BOOKSHELF, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("chiseled_canopy_bookshelf")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("cicada"), new BlockItem(CICADA, new Item.Properties().useBlockDescriptionPrefix().equippable(EquipmentSlot.HEAD).setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("cicada")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("cinder_furnace"), new BlockItem(CINDER_FURNACE, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("cinder_furnace")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("cinder_log"), new BlockItem(CINDER_LOG, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("cinder_log")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("cinder_wood"), new BlockItem(CINDER_WOOD, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("cinder_wood")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("clover_patch"), new BlockItem(CLOVER_PATCH, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("clover_patch")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("copper_oreberry_bush"), new BlockItem(COPPER_OREBERRY_BUSH, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("copper_oreberry_bush")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("coronation_carpet"), new BlockItem(CORONATION_CARPET, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("coronation_carpet")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("cracked_castle_brick"), new BlockItem(CRACKED_CASTLE_BRICK, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("cracked_castle_brick")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("cracked_castle_brick_stairs"), new BlockItem(CRACKED_CASTLE_BRICK_STAIRS, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("cracked_castle_brick_stairs")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("cracked_deadrock"), new BlockItem(CRACKED_DEADROCK, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("cracked_deadrock")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("cracked_etched_nagastone"), new BlockItem(CRACKED_ETCHED_NAGASTONE, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("cracked_etched_nagastone")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("cracked_mazestone"), new BlockItem(CRACKED_MAZESTONE, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("cracked_mazestone")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("cracked_nagastone_pillar"), new BlockItem(CRACKED_NAGASTONE_PILLAR, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("cracked_nagastone_pillar")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("cracked_nagastone_stairs_left"), new BlockItem(CRACKED_NAGASTONE_STAIRS_LEFT, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("cracked_nagastone_stairs_left")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("cracked_nagastone_stairs_right"), new BlockItem(CRACKED_NAGASTONE_STAIRS_RIGHT, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("cracked_nagastone_stairs_right")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("cracked_towerwood"), new BlockItem(CRACKED_TOWERWOOD, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("cracked_towerwood")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("cracked_underbrick"), new BlockItem(CRACKED_UNDERBRICK, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("cracked_underbrick")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("crimson_banister"), new BlockItem(CRIMSON_BANISTER, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("crimson_banister")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("crimson_drying_rack"), new BlockItem(CRIMSON_DRYING_RACK, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("crimson_drying_rack")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("cut_mazestone"), new BlockItem(CUT_MAZESTONE, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("cut_mazestone")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("darkwood_sapling"), new BlockItem(DARKWOOD_SAPLING, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("darkwood_sapling")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("dark_banister"), new BlockItem(DARK_BANISTER, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("dark_banister")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("dark_button"), new BlockItem(DARK_BUTTON, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("dark_button")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("dark_chest"), new BlockItem(DARK_CHEST, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("dark_chest")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("dark_door"), new BlockItem(DARK_DOOR, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("dark_door")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("dark_drying_rack"), new BlockItem(DARK_DRYING_RACK, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("dark_drying_rack")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("dark_fence"), new BlockItem(DARK_FENCE, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("dark_fence")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("dark_gate"), new BlockItem(DARK_GATE, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("dark_gate")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("dark_leaves"), new BlockItem(DARK_LEAVES, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("dark_leaves")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("dark_log"), new BlockItem(DARK_LOG, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("dark_log")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("dark_oak_banister"), new BlockItem(DARK_OAK_BANISTER, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("dark_oak_banister")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("dark_oak_drying_rack"), new BlockItem(DARK_OAK_DRYING_RACK, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("dark_oak_drying_rack")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("dark_planks"), new BlockItem(DARK_PLANKS, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("dark_planks")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("dark_pressure_plate"), new BlockItem(DARK_PLATE, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("dark_pressure_plate")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("dark_slab"), new BlockItem(DARK_SLAB, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("dark_slab")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("dark_stairs"), new BlockItem(DARK_STAIRS, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("dark_stairs")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("dark_tower_miniature_structure"), new BlockItem(DARK_TOWER_MINIATURE_STRUCTURE, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("dark_tower_miniature_structure")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("dark_trapdoor"), new BlockItem(DARK_TRAPDOOR, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("dark_trapdoor")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("dark_trapped_chest"), new BlockItem(DARK_TRAPPED_CHEST, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("dark_trapped_chest")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("dark_wood"), new BlockItem(DARK_WOOD, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("dark_wood")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("deadrock"), new BlockItem(DEADROCK, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("deadrock")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("decorative_mazestone"), new BlockItem(DECORATIVE_MAZESTONE, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("decorative_mazestone")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("duskberry_bush"), new BlockItem(DUSKBERRY_BUSH, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("duskberry_bush")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("encased_castle_brick_pillar"), new BlockItem(ENCASED_CASTLE_BRICK_PILLAR, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("encased_castle_brick_pillar")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("encased_castle_brick_stairs"), new BlockItem(ENCASED_CASTLE_BRICK_STAIRS, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("encased_castle_brick_stairs")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("encased_castle_brick_tile"), new BlockItem(ENCASED_CASTLE_BRICK_TILE, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("encased_castle_brick_tile")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("encased_fire_jet"), new BlockItem(ENCASED_FIRE_JET, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("encased_fire_jet")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("encased_smoker"), new BlockItem(ENCASED_SMOKER, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("encased_smoker")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("encased_towerwood"), new BlockItem(ENCASED_TOWERWOOD, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("encased_towerwood")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("essence_oreberry_bush"), new BlockItem(ESSENCE_OREBERRY_BUSH, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("essence_oreberry_bush")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("etched_nagastone"), new BlockItem(ETCHED_NAGASTONE, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("etched_nagastone")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("fiddlehead"), new BlockItem(FIDDLEHEAD, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("fiddlehead")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("fiery_block"), new BlockItem(FIERY_BLOCK, new Item.Properties().fireResistant().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("fiery_block")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("final_boss_boss_spawner"), new BlockItem(FINAL_BOSS_BOSS_SPAWNER, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("final_boss_boss_spawner")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("final_castle_miniature_structure"), new BlockItem(FINAL_CASTLE_MINIATURE_STRUCTURE, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("final_castle_miniature_structure")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("firefly"), new BlockItem(FIREFLY, new Item.Properties().useBlockDescriptionPrefix().equippable(EquipmentSlot.HEAD).setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("firefly")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("firefly_spawner"), new BlockItem(FIREFLY_SPAWNER, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("firefly_spawner")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("fire_jet"), new BlockItem(FIRE_JET, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("fire_jet")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("fluffy_cloud"), new BlockItem(FLUFFY_CLOUD, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("fluffy_cloud")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("ghast_trap"), new BlockItem(GHAST_TRAP, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("ghast_trap")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("giant_cobblestone"), new BlockItem(GIANT_COBBLESTONE, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("giant_cobblestone")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("giant_leaves"), new BlockItem(GIANT_LEAVES, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("giant_leaves")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("giant_log"), new BlockItem(GIANT_LOG, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("giant_log")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("giant_obsidian"), new BlockItem(GIANT_OBSIDIAN, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("giant_obsidian")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("goblin_stronghold_miniature_structure"), new BlockItem(GOBLIN_STRONGHOLD_MINIATURE_STRUCTURE, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("goblin_stronghold_miniature_structure")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("gold_oreberry_bush"), new BlockItem(GOLD_OREBERRY_BUSH, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("gold_oreberry_bush")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("green_force_field"), new BlockItem(GREEN_FORCE_FIELD, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("green_force_field")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("green_thorns"), new BlockItem(GREEN_THORNS, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("green_thorns")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("hedge"), new BlockItem(HEDGE, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("hedge")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("hedge_maze_miniature_structure"), new BlockItem(HEDGE_MAZE_MINIATURE_STRUCTURE, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("hedge_maze_miniature_structure")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("hollow_hill_miniature_structure"), new BlockItem(HOLLOW_HILL_MINIATURE_STRUCTURE, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("hollow_hill_miniature_structure")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("hollow_oak_sapling"), new BlockItem(HOLLOW_OAK_SAPLING, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("hollow_oak_sapling")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("huge_mushgloom"), new BlockItem(HUGE_MUSHGLOOM, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("huge_mushgloom")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("huge_mushgloom_stem"), new BlockItem(HUGE_MUSHGLOOM_STEM, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("huge_mushgloom_stem")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("huge_stalk"), new BlockItem(HUGE_STALK, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("huge_stalk")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("hydra_boss_spawner"), new BlockItem(HYDRA_BOSS_SPAWNER, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("hydra_boss_spawner")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("hydra_lair_miniature_structure"), new BlockItem(HYDRA_LAIR_MINIATURE_STRUCTURE, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("hydra_lair_miniature_structure")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("infested_towerwood"), new BlockItem(INFESTED_TOWERWOOD, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("infested_towerwood")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("ironwood_block"), new BlockItem(IRONWOOD_BLOCK, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("ironwood_block")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("iron_ladder"), new BlockItem(IRON_LADDER, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("iron_ladder")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("iron_oreberry_bush"), new BlockItem(IRON_OREBERRY_BUSH, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("iron_oreberry_bush")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("jungle_banister"), new BlockItem(JUNGLE_BANISTER, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("jungle_banister")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("jungle_drying_rack"), new BlockItem(JUNGLE_DRYING_RACK, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("jungle_drying_rack")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("knightmetal_block"), new BlockItem(KNIGHTMETAL_BLOCK, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("knightmetal_block")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("knight_phantom_boss_spawner"), new BlockItem(KNIGHT_PHANTOM_BOSS_SPAWNER, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("knight_phantom_boss_spawner")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("lich_boss_spawner"), new BlockItem(LICH_BOSS_SPAWNER, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("lich_boss_spawner")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("lich_tower_miniature_structure"), new BlockItem(LICH_TOWER_MINIATURE_STRUCTURE, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("lich_tower_miniature_structure")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("liveroot_block"), new BlockItem(LIVEROOT_BLOCK, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("liveroot_block")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("locked_vanishing_block"), new BlockItem(LOCKED_VANISHING_BLOCK, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("locked_vanishing_block")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("maloberry_bush"), new BlockItem(MALOBERRY_BUSH, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("maloberry_bush")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("mangrove_banister"), new BlockItem(MANGROVE_BANISTER, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("mangrove_banister")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("mangrove_button"), new BlockItem(MANGROVE_BUTTON, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("mangrove_button")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("mangrove_chest"), new BlockItem(MANGROVE_CHEST, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("mangrove_chest")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("mangrove_door"), new BlockItem(MANGROVE_DOOR, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("mangrove_door")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("mangrove_drying_rack"), new BlockItem(MANGROVE_DRYING_RACK, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("mangrove_drying_rack")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("mangrove_fence"), new BlockItem(MANGROVE_FENCE, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("mangrove_fence")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("mangrove_gate"), new BlockItem(MANGROVE_GATE, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("mangrove_gate")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("mangrove_leaves"), new BlockItem(MANGROVE_LEAVES, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("mangrove_leaves")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("mangrove_log"), new BlockItem(MANGROVE_LOG, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("mangrove_log")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("mangrove_planks"), new BlockItem(MANGROVE_PLANKS, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("mangrove_planks")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("mangrove_pressure_plate"), new BlockItem(MANGROVE_PLATE, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("mangrove_pressure_plate")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("mangrove_root"), new BlockItem(MANGROVE_ROOT, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("mangrove_root")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("mangrove_sapling"), new BlockItem(MANGROVE_SAPLING, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("mangrove_sapling")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("mangrove_slab"), new BlockItem(MANGROVE_SLAB, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("mangrove_slab")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("mangrove_stairs"), new BlockItem(MANGROVE_STAIRS, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("mangrove_stairs")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("mangrove_trapdoor"), new BlockItem(MANGROVE_TRAPDOOR, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("mangrove_trapdoor")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("mangrove_trapped_chest"), new BlockItem(MANGROVE_TRAPPED_CHEST, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("mangrove_trapped_chest")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("mangrove_wood"), new BlockItem(MANGROVE_WOOD, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("mangrove_wood")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("mayapple"), new BlockItem(MAYAPPLE, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("mayapple")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("mazestone"), new BlockItem(MAZESTONE, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("mazestone")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("mazestone_border"), new BlockItem(MAZESTONE_BORDER, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("mazestone_border")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("mazestone_brick"), new BlockItem(MAZESTONE_BRICK, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("mazestone_brick")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("mazestone_mosaic"), new BlockItem(MAZESTONE_MOSAIC, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("mazestone_mosaic")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("maze_slime_block"), new BlockItem(MAZE_SLIME_BLOCK, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("maze_slime_block")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("mining_banister"), new BlockItem(MINING_BANISTER, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("mining_banister")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("mining_button"), new BlockItem(MINING_BUTTON, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("mining_button")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("mining_chest"), new BlockItem(MINING_CHEST, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("mining_chest")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("mining_door"), new BlockItem(MINING_DOOR, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("mining_door")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("mining_drying_rack"), new BlockItem(MINING_DRYING_RACK, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("mining_drying_rack")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("mining_fence"), new BlockItem(MINING_FENCE, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("mining_fence")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("mining_gate"), new BlockItem(MINING_GATE, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("mining_gate")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("mining_leaves"), new BlockItem(MINING_LEAVES, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("mining_leaves")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("mining_log"), new BlockItem(MINING_LOG, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("mining_log")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("mining_log_core"), new BlockItem(MINING_LOG_CORE, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("mining_log_core")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("mining_planks"), new BlockItem(MINING_PLANKS, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("mining_planks")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("mining_pressure_plate"), new BlockItem(MINING_PLATE, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("mining_pressure_plate")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("mining_sapling"), new BlockItem(MINING_SAPLING, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("mining_sapling")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("mining_slab"), new BlockItem(MINING_SLAB, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("mining_slab")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("mining_stairs"), new BlockItem(MINING_STAIRS, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("mining_stairs")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("mining_trapdoor"), new BlockItem(MINING_TRAPDOOR, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("mining_trapdoor")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("mining_trapped_chest"), new BlockItem(MINING_TRAPPED_CHEST, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("mining_trapped_chest")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("mining_wood"), new BlockItem(MINING_WOOD, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("mining_wood")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("minoshroom_boss_spawner"), new BlockItem(MINOSHROOM_BOSS_SPAWNER, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("minoshroom_boss_spawner")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("minotaur_labyrinth_miniature_structure"), new BlockItem(MINOTAUR_LABYRINTH_MINIATURE_STRUCTURE, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("minotaur_labyrinth_miniature_structure")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("moonworm"), new BlockItem(MOONWORM, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("moonworm")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("mossy_castle_brick"), new BlockItem(MOSSY_CASTLE_BRICK, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("mossy_castle_brick")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("mossy_castle_brick_stairs"), new BlockItem(MOSSY_CASTLE_BRICK_STAIRS, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("mossy_castle_brick_stairs")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("mossy_etched_nagastone"), new BlockItem(MOSSY_ETCHED_NAGASTONE, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("mossy_etched_nagastone")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("mossy_mazestone"), new BlockItem(MOSSY_MAZESTONE, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("mossy_mazestone")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("mossy_nagastone_pillar"), new BlockItem(MOSSY_NAGASTONE_PILLAR, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("mossy_nagastone_pillar")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("mossy_nagastone_stairs_left"), new BlockItem(MOSSY_NAGASTONE_STAIRS_LEFT, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("mossy_nagastone_stairs_left")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("mossy_nagastone_stairs_right"), new BlockItem(MOSSY_NAGASTONE_STAIRS_RIGHT, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("mossy_nagastone_stairs_right")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("mossy_towerwood"), new BlockItem(MOSSY_TOWERWOOD, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("mossy_towerwood")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("mossy_underbrick"), new BlockItem(MOSSY_UNDERBRICK, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("mossy_underbrick")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("moss_patch"), new BlockItem(MOSS_PATCH, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("moss_patch")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("mushgloom"), new BlockItem(MUSHGLOOM, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("mushgloom")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("mushroom_tower_miniature_structure"), new BlockItem(MUSHROOM_TOWER_MINIATURE_STRUCTURE, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("mushroom_tower_miniature_structure")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("nagastone"), new BlockItem(NAGASTONE, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("nagastone")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("nagastone_head"), new BlockItem(NAGASTONE_HEAD, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("nagastone_head")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("nagastone_pillar"), new BlockItem(NAGASTONE_PILLAR, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("nagastone_pillar")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("nagastone_stairs_left"), new BlockItem(NAGASTONE_STAIRS_LEFT, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("nagastone_stairs_left")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("nagastone_stairs_right"), new BlockItem(NAGASTONE_STAIRS_RIGHT, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("nagastone_stairs_right")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("naga_boss_spawner"), new BlockItem(NAGA_BOSS_SPAWNER, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("naga_boss_spawner")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("naga_courtyard_miniature_structure"), new BlockItem(NAGA_COURTYARD_MINIATURE_STRUCTURE, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("naga_courtyard_miniature_structure")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("oak_banister"), new BlockItem(OAK_BANISTER, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("oak_banister")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("oak_drying_rack"), new BlockItem(OAK_DRYING_RACK, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("oak_drying_rack")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("orange_force_field"), new BlockItem(ORANGE_FORCE_FIELD, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("orange_force_field")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("pale_oak_banister"), new BlockItem(PALE_OAK_BANISTER, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("pale_oak_banister")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("pale_oak_drying_rack"), new BlockItem(PALE_OAK_DRYING_RACK, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("pale_oak_drying_rack")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("pink_castle_door"), new BlockItem(PINK_CASTLE_DOOR, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("pink_castle_door")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("pink_castle_rune_brick"), new BlockItem(PINK_CASTLE_RUNE_BRICK, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("pink_castle_rune_brick")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("pink_force_field"), new BlockItem(PINK_FORCE_FIELD, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("pink_force_field")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("quest_grove_miniature_structure"), new BlockItem(QUEST_GROVE_MINIATURE_STRUCTURE, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("quest_grove_miniature_structure")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("rainbow_oak_leaves"), new BlockItem(RAINBOW_OAK_LEAVES, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("rainbow_oak_leaves")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("rainbow_oak_sapling"), new BlockItem(RAINBOW_OAK_SAPLING, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("rainbow_oak_sapling")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("rainy_cloud"), new BlockItem(RAINY_CLOUD, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("rainy_cloud")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("raspberry_bush"), new BlockItem(RASPBERRY_BUSH, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("raspberry_bush")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("reappearing_block"), new BlockItem(REAPPEARING_BLOCK, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("reappearing_block")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("red_thread"), new BlockItem(RED_THREAD, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("red_thread")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("root_block"), new BlockItem(ROOT_BLOCK, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("root_block")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("root_strand"), new BlockItem(ROOT_STRAND, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("root_strand")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("sinister_spawner"), new BlockItem(SINISTER_SPAWNER, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("sinister_spawner")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("skull_chest"), new BlockItem(SKULL_CHEST, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("skull_chest")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("skyberry_bush"), new BlockItem(SKYBERRY_BUSH, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("skyberry_bush")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("slider"), new BlockItem(SLIDER, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("slider")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("smoker"), new BlockItem(SMOKER, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("smoker")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("snowy_cloud"), new BlockItem(SNOWY_CLOUD, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("snowy_cloud")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("snow_queen_boss_spawner"), new BlockItem(SNOW_QUEEN_BOSS_SPAWNER, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("snow_queen_boss_spawner")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("sorting_banister"), new BlockItem(SORTING_BANISTER, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("sorting_banister")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("sorting_button"), new BlockItem(SORTING_BUTTON, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("sorting_button")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("sorting_chest"), new BlockItem(SORTING_CHEST, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("sorting_chest")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("sorting_door"), new BlockItem(SORTING_DOOR, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("sorting_door")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("sorting_drying_rack"), new BlockItem(SORTING_DRYING_RACK, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("sorting_drying_rack")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("sorting_fence"), new BlockItem(SORTING_FENCE, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("sorting_fence")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("sorting_gate"), new BlockItem(SORTING_GATE, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("sorting_gate")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("sorting_leaves"), new BlockItem(SORTING_LEAVES, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("sorting_leaves")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("sorting_log"), new BlockItem(SORTING_LOG, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("sorting_log")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("sorting_log_core"), new BlockItem(SORTING_LOG_CORE, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("sorting_log_core")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("sorting_planks"), new BlockItem(SORTING_PLANKS, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("sorting_planks")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("sorting_pressure_plate"), new BlockItem(SORTING_PLATE, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("sorting_pressure_plate")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("sorting_sapling"), new BlockItem(SORTING_SAPLING, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("sorting_sapling")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("sorting_slab"), new BlockItem(SORTING_SLAB, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("sorting_slab")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("sorting_stairs"), new BlockItem(SORTING_STAIRS, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("sorting_stairs")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("sorting_trapdoor"), new BlockItem(SORTING_TRAPDOOR, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("sorting_trapdoor")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("sorting_trapped_chest"), new BlockItem(SORTING_TRAPPED_CHEST, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("sorting_trapped_chest")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("sorting_wood"), new BlockItem(SORTING_WOOD, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("sorting_wood")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("spiral_bricks"), new BlockItem(SPIRAL_BRICKS, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("spiral_bricks")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("spruce_banister"), new BlockItem(SPRUCE_BANISTER, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("spruce_banister")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("spruce_drying_rack"), new BlockItem(SPRUCE_DRYING_RACK, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("spruce_drying_rack")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("steeleaf_block"), new BlockItem(STEELEAF_BLOCK, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("steeleaf_block")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("stingberry_bush"), new BlockItem(STINGBERRY_BUSH, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("stingberry_bush")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("stripped_canopy_log"), new BlockItem(STRIPPED_CANOPY_LOG, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("stripped_canopy_log")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("stripped_canopy_wood"), new BlockItem(STRIPPED_CANOPY_WOOD, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("stripped_canopy_wood")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("stripped_dark_log"), new BlockItem(STRIPPED_DARK_LOG, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("stripped_dark_log")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("stripped_dark_wood"), new BlockItem(STRIPPED_DARK_WOOD, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("stripped_dark_wood")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("stripped_mangrove_log"), new BlockItem(STRIPPED_MANGROVE_LOG, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("stripped_mangrove_log")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("stripped_mangrove_wood"), new BlockItem(STRIPPED_MANGROVE_WOOD, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("stripped_mangrove_wood")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("stripped_mining_log"), new BlockItem(STRIPPED_MINING_LOG, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("stripped_mining_log")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("stripped_mining_wood"), new BlockItem(STRIPPED_MINING_WOOD, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("stripped_mining_wood")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("stripped_sorting_log"), new BlockItem(STRIPPED_SORTING_LOG, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("stripped_sorting_log")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("stripped_sorting_wood"), new BlockItem(STRIPPED_SORTING_WOOD, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("stripped_sorting_wood")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("stripped_time_log"), new BlockItem(STRIPPED_TIME_LOG, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("stripped_time_log")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("stripped_time_wood"), new BlockItem(STRIPPED_TIME_WOOD, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("stripped_time_wood")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("stripped_transformation_log"), new BlockItem(STRIPPED_TRANSFORMATION_LOG, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("stripped_transformation_log")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("stripped_transformation_wood"), new BlockItem(STRIPPED_TRANSFORMATION_WOOD, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("stripped_transformation_wood")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("stripped_twilight_oak_log"), new BlockItem(STRIPPED_TWILIGHT_OAK_LOG, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("stripped_twilight_oak_log")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("stripped_twilight_oak_wood"), new BlockItem(STRIPPED_TWILIGHT_OAK_WOOD, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("stripped_twilight_oak_wood")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("stronghold_shield"), new BlockItem(STRONGHOLD_SHIELD, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("stronghold_shield")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("terrorcotta_arcs"), new BlockItem(TERRORCOTTA_ARCS, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("terrorcotta_arcs")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("terrorcotta_curves"), new BlockItem(TERRORCOTTA_CURVES, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("terrorcotta_curves")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("terrorcotta_lines"), new BlockItem(TERRORCOTTA_LINES, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("terrorcotta_lines")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("thick_castle_brick"), new BlockItem(THICK_CASTLE_BRICK, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("thick_castle_brick")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("thorn_leaves"), new BlockItem(THORN_LEAVES, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("thorn_leaves")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("thorn_rose"), new BlockItem(THORN_ROSE, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("thorn_rose")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("time_banister"), new BlockItem(TIME_BANISTER, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("time_banister")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("time_button"), new BlockItem(TIME_BUTTON, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("time_button")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("time_chest"), new BlockItem(TIME_CHEST, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("time_chest")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("time_door"), new BlockItem(TIME_DOOR, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("time_door")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("time_drying_rack"), new BlockItem(TIME_DRYING_RACK, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("time_drying_rack")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("time_fence"), new BlockItem(TIME_FENCE, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("time_fence")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("time_gate"), new BlockItem(TIME_GATE, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("time_gate")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("time_leaves"), new BlockItem(TIME_LEAVES, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("time_leaves")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("time_log"), new BlockItem(TIME_LOG, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("time_log")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("time_log_core"), new BlockItem(TIME_LOG_CORE, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("time_log_core")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("time_planks"), new BlockItem(TIME_PLANKS, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("time_planks")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("time_pressure_plate"), new BlockItem(TIME_PLATE, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("time_pressure_plate")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("time_sapling"), new BlockItem(TIME_SAPLING, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("time_sapling")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("time_slab"), new BlockItem(TIME_SLAB, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("time_slab")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("time_stairs"), new BlockItem(TIME_STAIRS, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("time_stairs")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("time_trapdoor"), new BlockItem(TIME_TRAPDOOR, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("time_trapdoor")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("time_trapped_chest"), new BlockItem(TIME_TRAPPED_CHEST, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("time_trapped_chest")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("time_wood"), new BlockItem(TIME_WOOD, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("time_wood")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("torchberry_plant"), new BlockItem(TORCHBERRY_PLANT, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("torchberry_plant")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("towerwood"), new BlockItem(TOWERWOOD, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("towerwood")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("transformation_banister"), new BlockItem(TRANSFORMATION_BANISTER, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("transformation_banister")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("transformation_button"), new BlockItem(TRANSFORMATION_BUTTON, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("transformation_button")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("transformation_chest"), new BlockItem(TRANSFORMATION_CHEST, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("transformation_chest")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("transformation_door"), new BlockItem(TRANSFORMATION_DOOR, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("transformation_door")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("transformation_drying_rack"), new BlockItem(TRANSFORMATION_DRYING_RACK, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("transformation_drying_rack")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("transformation_fence"), new BlockItem(TRANSFORMATION_FENCE, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("transformation_fence")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("transformation_gate"), new BlockItem(TRANSFORMATION_GATE, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("transformation_gate")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("transformation_leaves"), new BlockItem(TRANSFORMATION_LEAVES, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("transformation_leaves")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("transformation_log"), new BlockItem(TRANSFORMATION_LOG, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("transformation_log")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("transformation_log_core"), new BlockItem(TRANSFORMATION_LOG_CORE, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("transformation_log_core")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("transformation_planks"), new BlockItem(TRANSFORMATION_PLANKS, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("transformation_planks")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("transformation_pressure_plate"), new BlockItem(TRANSFORMATION_PLATE, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("transformation_pressure_plate")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("transformation_sapling"), new BlockItem(TRANSFORMATION_SAPLING, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("transformation_sapling")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("transformation_slab"), new BlockItem(TRANSFORMATION_SLAB, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("transformation_slab")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("transformation_stairs"), new BlockItem(TRANSFORMATION_STAIRS, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("transformation_stairs")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("transformation_trapdoor"), new BlockItem(TRANSFORMATION_TRAPDOOR, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("transformation_trapdoor")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("transformation_trapped_chest"), new BlockItem(TRANSFORMATION_TRAPPED_CHEST, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("transformation_trapped_chest")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("transformation_wood"), new BlockItem(TRANSFORMATION_WOOD, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("transformation_wood")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("trollber"), new BlockItem(TROLLBER, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("trollber")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("trollsteinn"), new BlockItem(TROLLSTEINN, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("trollsteinn")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("trollvidr"), new BlockItem(TROLLVIDR, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("trollvidr")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("troll_cave_cottage_miniature_structure"), new BlockItem(TROLL_CAVE_COTTAGE_MINIATURE_STRUCTURE, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("troll_cave_cottage_miniature_structure")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("trophy_pedestal"), new BlockItem(TROPHY_PEDESTAL, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("trophy_pedestal")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("twilight_oak_banister"), new BlockItem(TWILIGHT_OAK_BANISTER, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("twilight_oak_banister")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("twilight_oak_button"), new BlockItem(TWILIGHT_OAK_BUTTON, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("twilight_oak_button")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("twilight_oak_chest"), new BlockItem(TWILIGHT_OAK_CHEST, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("twilight_oak_chest")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("twilight_oak_door"), new BlockItem(TWILIGHT_OAK_DOOR, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("twilight_oak_door")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("twilight_oak_drying_rack"), new BlockItem(TWILIGHT_OAK_DRYING_RACK, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("twilight_oak_drying_rack")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("twilight_oak_fence"), new BlockItem(TWILIGHT_OAK_FENCE, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("twilight_oak_fence")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("twilight_oak_gate"), new BlockItem(TWILIGHT_OAK_GATE, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("twilight_oak_gate")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("twilight_oak_leaves"), new BlockItem(TWILIGHT_OAK_LEAVES, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("twilight_oak_leaves")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("twilight_oak_log"), new BlockItem(TWILIGHT_OAK_LOG, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("twilight_oak_log")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("twilight_oak_planks"), new BlockItem(TWILIGHT_OAK_PLANKS, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("twilight_oak_planks")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("twilight_oak_pressure_plate"), new BlockItem(TWILIGHT_OAK_PLATE, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("twilight_oak_pressure_plate")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("twilight_oak_sapling"), new BlockItem(TWILIGHT_OAK_SAPLING, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("twilight_oak_sapling")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("twilight_oak_slab"), new BlockItem(TWILIGHT_OAK_SLAB, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("twilight_oak_slab")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("twilight_oak_stairs"), new BlockItem(TWILIGHT_OAK_STAIRS, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("twilight_oak_stairs")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("twilight_oak_trapdoor"), new BlockItem(TWILIGHT_OAK_TRAPDOOR, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("twilight_oak_trapdoor")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("twilight_oak_trapped_chest"), new BlockItem(TWILIGHT_OAK_TRAPPED_CHEST, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("twilight_oak_trapped_chest")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("twilight_oak_wood"), new BlockItem(TWILIGHT_OAK_WOOD, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("twilight_oak_wood")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("twilight_portal_miniature_structure"), new BlockItem(TWILIGHT_PORTAL_MINIATURE_STRUCTURE, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("twilight_portal_miniature_structure")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("twisted_stone"), new BlockItem(TWISTED_STONE, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("twisted_stone")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("twisted_stone_pillar"), new BlockItem(TWISTED_STONE_PILLAR, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("twisted_stone_pillar")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("uberous_soil"), new BlockItem(UBEROUS_SOIL, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("uberous_soil")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("uncrafting_table"), new BlockItem(UNCRAFTING_TABLE, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("uncrafting_table")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("underbrick"), new BlockItem(UNDERBRICK, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("underbrick")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("underbrick_floor"), new BlockItem(UNDERBRICK_FLOOR, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("underbrick_floor")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("unripe_trollber"), new BlockItem(UNRIPE_TROLLBER, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("unripe_trollber")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("ur_ghast_boss_spawner"), new BlockItem(UR_GHAST_BOSS_SPAWNER, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("ur_ghast_boss_spawner")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("vangrove_banister"), new BlockItem(VANGROVE_BANISTER, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("vangrove_banister")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("vangrove_drying_rack"), new BlockItem(VANGROVE_DRYING_RACK, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("vangrove_drying_rack")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("vanishing_block"), new BlockItem(VANISHING_BLOCK, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("vanishing_block")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("violet_castle_door"), new BlockItem(VIOLET_CASTLE_DOOR, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("violet_castle_door")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("violet_castle_rune_brick"), new BlockItem(VIOLET_CASTLE_RUNE_BRICK, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("violet_castle_rune_brick")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("violet_force_field"), new BlockItem(VIOLET_FORCE_FIELD, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("violet_force_field")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("warped_banister"), new BlockItem(WARPED_BANISTER, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("warped_banister")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("warped_drying_rack"), new BlockItem(WARPED_DRYING_RACK, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("warped_drying_rack")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("weathered_deadrock"), new BlockItem(WEATHERED_DEADROCK, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("weathered_deadrock")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("wispy_cloud"), new BlockItem(WISPY_CLOUD, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("wispy_cloud")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("worn_castle_brick"), new BlockItem(WORN_CASTLE_BRICK, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("worn_castle_brick")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("worn_castle_brick_stairs"), new BlockItem(WORN_CASTLE_BRICK_STAIRS, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("worn_castle_brick_stairs")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("yellow_castle_door"), new BlockItem(YELLOW_CASTLE_DOOR, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("yellow_castle_door")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("yellow_castle_rune_brick"), new BlockItem(YELLOW_CASTLE_RUNE_BRICK, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("yellow_castle_rune_brick")))));
		Registry.register(BuiltInRegistries.ITEM, TwilightForestMod.prefix("yeti_cave_miniature_structure"), new BlockItem(YETI_CAVE_MINIATURE_STRUCTURE, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, TwilightForestMod.prefix("yeti_cave_miniature_structure")))));

		// Register all sign blocks with BlockEntityType for proper rendering
		BlockEntityTypes.SIGN.addValidBlock(TWILIGHT_OAK_SIGN);
		BlockEntityTypes.SIGN.addValidBlock(TWILIGHT_WALL_SIGN);
		BlockEntityTypes.HANGING_SIGN.addValidBlock(TWILIGHT_OAK_HANGING_SIGN);
		BlockEntityTypes.HANGING_SIGN.addValidBlock(TWILIGHT_OAK_WALL_HANGING_SIGN);

		BlockEntityTypes.SIGN.addValidBlock(CANOPY_SIGN);
		BlockEntityTypes.SIGN.addValidBlock(CANOPY_WALL_SIGN);
		BlockEntityTypes.HANGING_SIGN.addValidBlock(CANOPY_HANGING_SIGN);
		BlockEntityTypes.HANGING_SIGN.addValidBlock(CANOPY_WALL_HANGING_SIGN);

		BlockEntityTypes.SIGN.addValidBlock(MANGROVE_SIGN);
		BlockEntityTypes.SIGN.addValidBlock(MANGROVE_WALL_SIGN);
		BlockEntityTypes.HANGING_SIGN.addValidBlock(MANGROVE_HANGING_SIGN);
		BlockEntityTypes.HANGING_SIGN.addValidBlock(MANGROVE_WALL_HANGING_SIGN);

		BlockEntityTypes.SIGN.addValidBlock(DARK_SIGN);
		BlockEntityTypes.SIGN.addValidBlock(DARK_WALL_SIGN);
		BlockEntityTypes.HANGING_SIGN.addValidBlock(DARK_HANGING_SIGN);
		BlockEntityTypes.HANGING_SIGN.addValidBlock(DARK_WALL_HANGING_SIGN);

		BlockEntityTypes.SIGN.addValidBlock(TIME_SIGN);
		BlockEntityTypes.SIGN.addValidBlock(TIME_WALL_SIGN);
		BlockEntityTypes.HANGING_SIGN.addValidBlock(TIME_HANGING_SIGN);
		BlockEntityTypes.HANGING_SIGN.addValidBlock(TIME_WALL_HANGING_SIGN);

		BlockEntityTypes.SIGN.addValidBlock(TRANSFORMATION_SIGN);
		BlockEntityTypes.SIGN.addValidBlock(TRANSFORMATION_WALL_SIGN);
		BlockEntityTypes.HANGING_SIGN.addValidBlock(TRANSFORMATION_HANGING_SIGN);
		BlockEntityTypes.HANGING_SIGN.addValidBlock(TRANSFORMATION_WALL_HANGING_SIGN);

		BlockEntityTypes.SIGN.addValidBlock(MINING_SIGN);
		BlockEntityTypes.SIGN.addValidBlock(MINING_WALL_SIGN);
		BlockEntityTypes.HANGING_SIGN.addValidBlock(MINING_HANGING_SIGN);
		BlockEntityTypes.HANGING_SIGN.addValidBlock(MINING_WALL_HANGING_SIGN);

		BlockEntityTypes.SIGN.addValidBlock(SORTING_SIGN);
		BlockEntityTypes.SIGN.addValidBlock(SORTING_WALL_SIGN);
		BlockEntityTypes.HANGING_SIGN.addValidBlock(SORTING_HANGING_SIGN);
		BlockEntityTypes.HANGING_SIGN.addValidBlock(SORTING_WALL_HANGING_SIGN);
	}
}

