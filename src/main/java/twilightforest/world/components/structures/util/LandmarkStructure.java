package twilightforest.world.components.structures.util;

import com.mojang.datafixers.Products;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;
import net.minecraft.world.level.saveddata.maps.MapDecorationType;
import org.jetbrains.annotations.Nullable;
import twilightforest.world.components.biomesources.TFBiomeProvider;
import twilightforest.world.components.structures.TFStructureComponentTemplate;

import java.util.Comparator;
import java.util.Optional;

// Landmark structure without progression lock; Hollow Hills/Hedge Maze/Naga Courtyard/Quest Grove
public abstract class LandmarkStructure extends Structure implements DecorationClearance {

	protected static <S extends LandmarkStructure> Products.P3<RecordCodecBuilder.Mu<S>, Optional<DecorationConfig>, Boolean, Optional<Holder<MapDecorationType>>> landmarkCodecNoSettings(RecordCodecBuilder.Instance<S> instance) {
		return instance.group(
			DecorationConfig.CODEC.optionalFieldOf(DecorationClearance.CODEC_NAME).forGetter(s -> s.decorationConfig),
			Codec.BOOL.optionalFieldOf("center_in_chunk", true).forGetter(s -> s.centerInChunk),
			BuiltInRegistries.MAP_DECORATION_TYPE.holderByNameCodec().optionalFieldOf("structure_icon").forGetter(s -> s.structureIcon)
		);
	}

	protected static <S extends LandmarkStructure> Products.P4<RecordCodecBuilder.Mu<S>, Optional<DecorationConfig>, Boolean, Optional<Holder<MapDecorationType>>, StructureSettings> landmarkCodec(RecordCodecBuilder.Instance<S> instance) {
		return instance.group(
			DecorationConfig.CODEC.optionalFieldOf(DecorationClearance.CODEC_NAME).forGetter(s -> s.decorationConfig),
			Codec.BOOL.optionalFieldOf("center_in_chunk", true).forGetter(s -> s.centerInChunk),
			BuiltInRegistries.MAP_DECORATION_TYPE.holderByNameCodec().optionalFieldOf("structure_icon").forGetter(s -> s.structureIcon),
			Structure.settingsCodec(instance)
		);
	}

	protected final Optional<DecorationConfig> decorationConfig;
	protected final boolean centerInChunk;
	protected Optional<Holder<MapDecorationType>> structureIcon;

	public LandmarkStructure(Optional<DecorationConfig> decorationConfig, boolean centerInChunk, Optional<Holder<MapDecorationType>> structureIcon, StructureSettings structureSettings) {
		super(structureSettings);
		this.decorationConfig = decorationConfig;
		this.centerInChunk = centerInChunk;
		this.structureIcon = structureIcon;
	}

	protected Structure.GenerationStub getStructurePieceGenerationStubFunction(StructurePiece startingPiece, GenerationContext context, int x, int y, int z) {
		return new GenerationStub(new BlockPos(x, y, z), structurePiecesBuilder -> {
			this.generateFromStartingPiece(startingPiece, context, structurePiecesBuilder);

			structurePiecesBuilder.pieces.sort(Comparator.comparing(piece -> piece instanceof SortablePiece sortable ? sortable.getSortKey() : 0));

			structurePiecesBuilder.pieces.stream()
				.filter(TFStructureComponentTemplate.class::isInstance)
				.map(TFStructureComponentTemplate.class::cast)
				.forEach(t -> t.LAZY_TEMPLATE_LOADER.run());
		});
	}

	protected void generateFromStartingPiece(StructurePiece startingPiece, GenerationContext context, StructurePiecesBuilder structurePiecesBuilder) {
		structurePiecesBuilder.addPiece(startingPiece);
		startingPiece.addChildren(startingPiece, structurePiecesBuilder, context.random());
	}

	/**
	 * Calculates the block-level spawn position for a given chunk, anchored to the
	 * center of the biome-grid tile so that the structure spawns exactly where
	 * the biome was validated in {@link #findValidGenerationPoint}.
	 * Prevents off-center generation in key biomes like FINAL_PLATEAU / HIGHLANDS.
	 */
	protected static int[] tileCenterBlockPos(ChunkPos chunkPos) {
		int biomeX = (Math.round(chunkPos.x() / 16F) << 6) + 2;
		int biomeZ = (Math.round(chunkPos.z() / 16F) << 6) + 2;
		return new int[]{ biomeX * 4, biomeZ * 4 };
	}

	/**
	 * Generates the structure at a specific block position, used by subclasses and
	 * wrappers that need to control the spawn coordinates independently of the chunk.
	 */
	protected Optional<GenerationStub> generateAt(GenerationContext context, ChunkPos chunkPos, int x, int y, int z) {
		return Optional
			.ofNullable(this.makeFirstPiece(context, chunkPos, x, y, z))
			.map(piece -> this.getStructurePieceGenerationStubFunction(piece, context, x, y, z));
	}

	/**
	 * Delegating helper for subclasses that wrap another structure's first piece logic.
	 * Calls {@link #getFirstPiece} with the correct random seed.
	 */
	@Nullable
	public StructurePiece makeFirstPiece(GenerationContext context, ChunkPos chunkPos, int x, int y, int z) {
		return this.getFirstPiece(context, RandomSource.create(context.seed() + chunkPos.x() * 25117L + chunkPos.z() * 151121L), chunkPos, x, y, z);
	}

	// TODO Refactor findGenerationPoint to merge usecases for getFirstPiece and getStructurePieceGenerationStubFunction
	@Override
	public Optional<GenerationStub> findGenerationPoint(GenerationContext context) {
		ChunkPos chunkPos = context.chunkPos();
		int[] pos = tileCenterBlockPos(chunkPos);
		int x = pos[0];
		int z = pos[1];
		int y = this.adjustForTerrain(context, x, z);

		return this.generateAt(context, chunkPos, x, y, z);
	}

	/**
	 * Public entry point for tile-based generation, used by wrapped structures
	 * that need to delegate to another structure's generation logic while keeping
	 * the biome-validation-aligned spawn position.
	 */
	public Optional<GenerationStub> generateAtTileCenter(GenerationContext context) {
		ChunkPos chunkPos = context.chunkPos();
		int[] pos = tileCenterBlockPos(chunkPos);
		int x = pos[0];
		int z = pos[1];
		int y = this.adjustForTerrain(context, x, z);

		return this.generateAt(context, chunkPos, x, y, z);
	}

	public final Optional<Holder<MapDecorationType>> getMapIcon() {
		return this.structureIcon;
	}

	@Nullable
	protected abstract StructurePiece getFirstPiece(GenerationContext context, RandomSource random, ChunkPos chunkPos, int x, int y, int z);

	@Override
	public boolean isSurfaceDecorationsAllowed() {
		return this.decorationConfig.map(DecorationConfig::surfaceDecorations).orElse(true);
	}

	@Override
	public boolean isUndergroundDecoAllowed() {
		return this.decorationConfig.map(DecorationConfig::undergroundDecorations).orElse(true);
	}

	@Override
	public boolean isGrassDecoAllowed() {
		return this.decorationConfig.map(DecorationConfig::vegetation).orElse(true);
	}

	@Override
	public boolean shouldAdjustToTerrain() {
		return this.decorationConfig.map(DecorationConfig::adjustElevation).orElse(false);
	}

	@Override
	public float chunkClearanceRadius() {
		return this.decorationConfig.map(DecorationConfig::chunkClearanceRadius).orElse(1.0F);
	}

	@Override
	public Optional<GenerationStub> findValidGenerationPoint(GenerationContext context) {
		if (!(context.biomeResolver() instanceof TFBiomeProvider twilightBiomeProvider))
			return super.findValidGenerationPoint(context);

		ChunkPos chunkPos = context.chunkPos();
		// set biomeX and biomeZ to center of the biome-grid tile.
		// Otherwise some tightly-fitting biomes like Highlands vs Thornlands may fail the Troll-Clouds structure generation
		int[] pos = tileCenterBlockPos(chunkPos);
		int biomeX = pos[0] / 4;
		int biomeZ = pos[1] / 4;

		Holder<Biome> biomeAt = twilightBiomeProvider.getMainBiome(biomeX, biomeZ);

		return context.validBiome().test(biomeAt) ? this.findGenerationPoint(context) : Optional.empty();
	}
}