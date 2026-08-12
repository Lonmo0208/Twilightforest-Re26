package twilightforest.world.components.processors;

import org.jetbrains.annotations.Nullable;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import twilightforest.init.custom.WoodPalettes;
import twilightforest.util.woods.WoodPalette;

public final class WoodPaletteSwizzle implements StructureProcessor {
	private final Holder<WoodPalette> targetPalette;
	private final Holder<WoodPalette> replacementPalette;

	public static final MapCodec<WoodPaletteSwizzle> MAP_CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(
		WoodPalettes.CODEC.fieldOf("target_palette").forGetter(s -> s.targetPalette),
		WoodPalettes.CODEC.fieldOf("replacement_palette").forGetter(s -> s.replacementPalette)
	).apply(instance, WoodPaletteSwizzle::new));

	public WoodPaletteSwizzle(Holder<WoodPalette> targetPalette, Holder<WoodPalette> replacementPalette) {
		this.targetPalette = targetPalette;
		this.replacementPalette = replacementPalette;
	}

	@Nullable

	@Override
	public StructureTemplate.StructureBlockInfo processBlock(LevelReader worldIn, BlockPos pos, BlockPos piecepos, BlockPos templateRelativePos, StructureTemplate.StructureBlockInfo blockInfo, StructurePlaceSettings settings) {
		return this.replacementPalette.value().modifyBlockWithType(this.targetPalette.value(), blockInfo);
	}

	@Nullable

	@Override
	public MapCodec<? extends StructureProcessor> codec() {
		return MAP_CODEC;
	}
}
