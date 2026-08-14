package twilightforest.util.woods;

import net.fabricmc.fabric.api.object.builder.v1.block.type.BlockSetTypeBuilder;
import net.fabricmc.fabric.api.object.builder.v1.block.type.WoodTypeBuilder;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.WoodType;
import twilightforest.TwilightForestMod;

public class TFWoodTypes {

	public static final BlockSetType TWILIGHT_OAK_SET = BlockSetTypeBuilder.copyOf(BlockSetType.OAK).register(TwilightForestMod.prefix("twilight_oak"));
	public static final BlockSetType CANOPY_WOOD_SET = BlockSetTypeBuilder.copyOf(BlockSetType.OAK).register(TwilightForestMod.prefix("canopy"));
	public static final BlockSetType MANGROVE_WOOD_SET = BlockSetTypeBuilder.copyOf(BlockSetType.OAK).register(TwilightForestMod.prefix("mangrove"));
	public static final BlockSetType DARK_WOOD_SET = BlockSetTypeBuilder.copyOf(BlockSetType.OAK).register(TwilightForestMod.prefix("dark"));
	public static final BlockSetType TIME_WOOD_SET = BlockSetTypeBuilder.copyOf(BlockSetType.OAK).register(TwilightForestMod.prefix("time"));
	public static final BlockSetType TRANSFORMATION_WOOD_SET = BlockSetTypeBuilder.copyOf(BlockSetType.OAK).register(TwilightForestMod.prefix("transformation"));
	public static final BlockSetType MINING_WOOD_SET = BlockSetTypeBuilder.copyOf(BlockSetType.OAK).register(TwilightForestMod.prefix("mining"));
	public static final BlockSetType SORTING_WOOD_SET = BlockSetTypeBuilder.copyOf(BlockSetType.OAK).register(TwilightForestMod.prefix("sorting"));

	public static final WoodType TWILIGHT_OAK_WOOD_TYPE = WoodTypeBuilder.copyOf(WoodType.OAK).register(TwilightForestMod.prefix("twilight_oak"), TWILIGHT_OAK_SET);
	public static final WoodType CANOPY_WOOD_TYPE = WoodTypeBuilder.copyOf(WoodType.OAK).register(TwilightForestMod.prefix("canopy"), CANOPY_WOOD_SET);
	public static final WoodType MANGROVE_WOOD_TYPE = WoodTypeBuilder.copyOf(WoodType.OAK).register(TwilightForestMod.prefix("mangrove"), MANGROVE_WOOD_SET);
	public static final WoodType DARK_WOOD_TYPE = WoodTypeBuilder.copyOf(WoodType.OAK).register(TwilightForestMod.prefix("dark"), DARK_WOOD_SET);
	public static final WoodType TIME_WOOD_TYPE = WoodTypeBuilder.copyOf(WoodType.OAK).register(TwilightForestMod.prefix("time"), TIME_WOOD_SET);
	public static final WoodType TRANSFORMATION_WOOD_TYPE = WoodTypeBuilder.copyOf(WoodType.OAK).register(TwilightForestMod.prefix("transformation"), TRANSFORMATION_WOOD_SET);
	public static final WoodType MINING_WOOD_TYPE = WoodTypeBuilder.copyOf(WoodType.OAK).register(TwilightForestMod.prefix("mining"), MINING_WOOD_SET);
	public static final WoodType SORTING_WOOD_TYPE = WoodTypeBuilder.copyOf(WoodType.OAK).register(TwilightForestMod.prefix("sorting"), SORTING_WOOD_SET);
}
