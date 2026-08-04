package twilightforest.client.model.item;

import net.minecraft.client.renderer.block.dispatch.ModelState;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.cuboid.FaceBakery;
import net.minecraft.client.resources.model.cuboid.CuboidFace;
import net.minecraft.client.resources.model.geometry.BakedQuad;
import net.minecraft.client.resources.model.geometry.QuadCollection;
import net.minecraft.client.renderer.texture.SpriteContents;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.core.Direction;
import org.joml.Vector3f;

/**
 * Helper for generating item layer quads from a material sprite.
 * This replaces the need for the private ItemModelGenerator.ItemLayerKey.
 */
public class ItemModelLayerHelper {

	public static QuadCollection computeItemLayer(ModelBaker baker, Material.Baked material, ModelState modelState, int layerIndex) {
		ModelBaker.SharedOperationKey<QuadCollection> key = new ModelBaker.SharedOperationKey<>() {
			@Override
			public QuadCollection compute(ModelBaker modelBakery) {
				QuadCollection.Builder builder = new QuadCollection.Builder();
				BakedQuad.MaterialInfo materialInfo = modelBakery.interner()
					.materialInfo(BakedQuad.MaterialInfo.of(material, material.sprite().transparency(), layerIndex, true, 0));
				bakeExtrudedSprite(builder, modelBakery.interner(), modelState, materialInfo);
				return builder.build();
			}
		};
		return baker.compute(key);
	}

	private static void bakeExtrudedSprite(QuadCollection.Builder builder, ModelBaker.Interner interner, ModelState modelState, BakedQuad.MaterialInfo materialInfo) {
		Vector3f from = new Vector3f(0.0F, 0.0F, 7.5F);
		Vector3f to = new Vector3f(16.0F, 16.0F, 8.5F);

		CuboidFace.UVs southUVs = new CuboidFace.UVs(0.0F, 0.0F, 16.0F, 16.0F);
		CuboidFace.UVs northUVs = new CuboidFace.UVs(16.0F, 0.0F, 0.0F, 16.0F);

		builder.addUnculledFace(FaceBakery.bakeQuad(interner, from, to, southUVs, com.mojang.math.Quadrant.R0, materialInfo, Direction.SOUTH, modelState, null));
		builder.addUnculledFace(FaceBakery.bakeQuad(interner, from, to, northUVs, com.mojang.math.Quadrant.R0, materialInfo, Direction.NORTH, modelState, null));

		bakeSideFaces(builder, interner, modelState, materialInfo);
	}

	private static void bakeSideFaces(QuadCollection.Builder builder, ModelBaker.Interner interner, ModelState modelState, BakedQuad.MaterialInfo materialInfo) {
		var sprite = materialInfo.sprite().contents();
		float xScale = 16.0F / sprite.width();
		float yScale = 16.0F / sprite.height();

		for (SideFace sideFace : getSideFaces(sprite)) {
			float x = sideFace.x();
			float y = sideFace.y();
			SideDirection sideDirection = sideFace.facing();
			float u0 = x + 0.1F;
			float u1 = x + 1.0F - 0.1F;
			float v0;
			float v1;
			if (sideDirection.isHorizontal()) {
				v0 = y + 0.1F;
				v1 = y + 1.0F - 0.1F;
			} else {
				v0 = y + 1.0F - 0.1F;
				v1 = y + 0.1F;
			}

			float startX = x;
			float startY = y;
			float endX = x;
			float endY = y;
			switch (sideDirection) {
				case UP -> endX = x + 1.0F;
				case DOWN -> {
					endX = x + 1.0F;
					startY = y + 1.0F;
					endY = y + 1.0F;
				}
				case LEFT -> endY = y + 1.0F;
				case RIGHT -> {
					startX = x + 1.0F;
					endX = x + 1.0F;
					endY = y + 1.0F;
				}
			}

			startX *= xScale;
			endX *= xScale;
			startY *= yScale;
			endY *= yScale;
			startY = 16.0F - startY;
			endY = 16.0F - endY;

			CuboidFace.UVs uvs = new CuboidFace.UVs(u0 * xScale, v0 * yScale, u1 * xScale, v1 * yScale);
			Vector3f from = new Vector3f();
			Vector3f to = new Vector3f();

			switch (sideDirection) {
				case UP -> { from.set(startX, startY, 7.5F); to.set(endX, startY, 8.5F); }
				case DOWN -> { from.set(startX, endY, 7.5F); to.set(endX, endY, 8.5F); }
				case LEFT -> { from.set(startX, startY, 7.5F); to.set(startX, endY, 8.5F); }
				case RIGHT -> { from.set(endX, startY, 7.5F); to.set(endX, endY, 8.5F); }
			}

			builder.addUnculledFace(FaceBakery.bakeQuad(interner, from, to, uvs, com.mojang.math.Quadrant.R0, materialInfo, sideDirection.getDirection(), modelState, null));
		}
	}

	private static java.util.Collection<SideFace> getSideFaces(SpriteContents sprite) {
		int width = sprite.width();
		int height = sprite.height();
		java.util.Set<SideFace> sideFaces = new java.util.HashSet<>();
		sprite.getUniqueFrames().forEach(frame -> {
			for (int y = 0; y < height; y++) {
				for (int x = 0; x < width; x++) {
					boolean thisOpaque = !isTransparent(sprite, frame, x, y, width, height);
					if (thisOpaque) {
						checkTransition(SideDirection.UP, sideFaces, sprite, frame, x, y, width, height);
						checkTransition(SideDirection.DOWN, sideFaces, sprite, frame, x, y, width, height);
						checkTransition(SideDirection.LEFT, sideFaces, sprite, frame, x, y, width, height);
						checkTransition(SideDirection.RIGHT, sideFaces, sprite, frame, x, y, width, height);
					}
				}
			}
		});
		return sideFaces;
	}

	private static void checkTransition(SideDirection facing, java.util.Set<SideFace> sideFaces, SpriteContents sprite, int frame, int x, int y, int width, int height) {
		if (isTransparent(sprite, frame, x - facing.direction.getStepX(), y - facing.direction.getStepY(), width, height)) {
			sideFaces.add(new SideFace(facing, x, y));
		}
	}

	private static boolean isTransparent(SpriteContents sprite, int frame, int x, int y, int width, int height) {
		return x >= 0 && y >= 0 && x < width && y < height ? sprite.isTransparent(frame, x, y) : true;
	}

	private enum SideDirection {
		UP(Direction.UP),
		DOWN(Direction.DOWN),
		LEFT(Direction.EAST),
		RIGHT(Direction.WEST);

		private final Direction direction;

		SideDirection(Direction direction) {
			this.direction = direction;
		}

		public Direction getDirection() {
			return this.direction;
		}

		public boolean isHorizontal() {
			return this == DOWN || this == UP;
		}
	}

	private record SideFace(SideDirection facing, int x, int y) {
	}
}