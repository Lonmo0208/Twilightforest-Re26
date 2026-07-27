package twilightforest.world.components.structures.util;

import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.TerrainAdjustment;

/**
 * Fabric-compatible replacement for NeoForge's PieceBeardifierModifier.
 * Allows individual structure pieces to override the structure-wide terrain adjustment.
 */
public interface PieceBeardifierModifier {
	BoundingBox getBeardifierBox();

	TerrainAdjustment getTerrainAdjustment();

	int getGroundLevelDelta();
}