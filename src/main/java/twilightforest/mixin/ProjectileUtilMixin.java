package twilightforest.mixin;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import com.mojang.logging.LogUtils;
import org.slf4j.Logger;
import twilightforest.entity.TFPart;
import twilightforest.util.TFEntityExtensions;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/**
 * Fabric multipart ray-trace fix.
 *
 * <p>NeoForge upstream provides multipart hit detection via {@code Entity.getParts()}
 * + a NeoForge patch on {@code ProjectileUtil} / vanilla ray-trace that iterates an
 * owner's sub-entities. Fabric has no equivalent, so without this mixin a player's
 * crosshair / projectile / arrow ray-trace never touches a TFPart (HydraHead,
 * HydraNeck, NagaSegment, …) — it always resolves to the multipart owner's main
 * bounding box. Effects observed before this fix:
 *   • Hydra appears invulnerable because hits land on the Hydra main entity, which
 *     rejects all non-BYPASSES_INVULNERABILITY damage in Hydra#hurt.
 *   • Naga segments past the head are not attackable — only the head's bbox is
 *     hit-tested.
 *   • Arrows fly through visible Hydra heads / Naga body without registering hits.
 *
 * <p>Strategy: redirect the inner {@code level.getEntities(shooter, box, filter)}
 * call inside each public {@code getEntityHitResult} overload so the returned list
 * also contains every TFPart belonging to each owner returned by vanilla.
 */
@Mixin(ProjectileUtil.class)
public abstract class ProjectileUtilMixin {

	private static final Logger LOGGER = LogUtils.getLogger();
	private static int debugCounter = 0;

	/**
	 * Server-side projectile / arrow ray-trace path.
	 * {@code getEntityHitResult(Level, Entity, Vec3, Vec3, AABB, Predicate, float)}
	 */
	@Redirect(method = "getEntityHitResult(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/phys/Vec3;Lnet/minecraft/world/phys/Vec3;Lnet/minecraft/world/phys/AABB;Ljava/util/function/Predicate;F)Lnet/minecraft/world/phys/EntityHitResult;",
		at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;getEntities(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/phys/AABB;Ljava/util/function/Predicate;)Ljava/util/List;"))
	private static List<Entity> twilightforest$includeMultipartPartsWithLevel(Level level, Entity shooter, AABB box, Predicate<Entity> filter) {
		return twilightforest$augmentEntitiesWithParts(level, shooter, box, filter);
	}

	/**
	 * Melee / crosshair path. {@code Entity#pick} → {@code GameRenderer#pick} call
	 * this overload (no leading {@code Level} parameter, takes {@code double}
	 * distance instead of {@code float}). Without this redirect, hovering the
	 * crosshair over a Hydra head or Naga body segment resolves to the multipart
	 * owner's main bounding box (which is invulnerable / off-screen), so the
	 * resulting {@code ServerboundInteractPacket} never targets a part.
	 */
	@Redirect(method = "getEntityHitResult(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/phys/Vec3;Lnet/minecraft/world/phys/Vec3;Lnet/minecraft/world/phys/AABB;Ljava/util/function/Predicate;D)Lnet/minecraft/world/phys/EntityHitResult;",
		at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;getEntities(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/phys/AABB;Ljava/util/function/Predicate;)Ljava/util/List;"))
	private static List<Entity> twilightforest$includeMultipartPartsNoLevel(Level level, Entity shooter, AABB box, Predicate<Entity> filter) {
		return twilightforest$augmentEntitiesWithParts(level, shooter, box, filter);
	}

	/**
	 * 26.1.2 melee / sweeping attack / piercing projectile path.
	 * {@code AttackRange.getHitEntitiesAlong()} → {@code getManyEntityHitResult}
	 * is the new melee hit detection path. Without this redirect, melee swings
	 * and multi-target attacks never register hits on TFPart entities.
	 */
	@Redirect(method = "getManyEntityHitResult(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/phys/Vec3;Lnet/minecraft/world/phys/Vec3;Lnet/minecraft/world/phys/AABB;Ljava/util/function/Predicate;FLnet/minecraft/world/level/ClipContext$Block;Z)Ljava/util/Collection;",
		at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;getEntities(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/phys/AABB;Ljava/util/function/Predicate;)Ljava/util/List;"))
	private static List<Entity> twilightforest$includeMultipartPartsMany(Level level, Entity shooter, AABB box, Predicate<Entity> filter) {
		return twilightforest$augmentEntitiesWithParts(level, shooter, box, filter);
	}

	private static List<Entity> twilightforest$augmentEntitiesWithParts(Level level, Entity shooter, AABB box, Predicate<Entity> filter) {
		List<Entity> original = level.getEntities(shooter, box, filter);
		// Also query for non-pickable multipart entity owners (e.g. Hydra with isPickable()=false).
		// These are excluded by the pickable filter, but their parts must still be reachable
		// by the ray-trace so the client can send attack packets targeting the parts.
		List<Entity> nonPickableMultiparts = level.getEntities(shooter, box, entity ->
			!entity.isPickable() && entity instanceof TFEntityExtensions ext && ext.isMultipartEntity());
		// Query for ALL multipart entity owners (both pickable and non-pickable) with an
		// expanded box. This is necessary for entities like Naga whose body segments can be
		// far from the head (up to 24 blocks away). The head may be outside the ray-trace box
		// when the player attacks a body segment, but we still need to find the parts.
		// Expand by 30 blocks to cover the maximum Naga body length (12 segments * 2 blocks).
		List<Entity> allMultiparts = level.getEntities(shooter, box.inflate(30.0D), entity ->
			entity instanceof TFEntityExtensions ext && ext.isMultipartEntity());
		List<Entity> augmented = null;
		// Combine all three lists for part discovery
		List<Entity> allOwners = new ArrayList<>(original);
		allOwners.addAll(nonPickableMultiparts);
		allOwners.addAll(allMultiparts);
		for (Entity ent : allOwners) {
			if (ent instanceof TFEntityExtensions ext) {
				Entity[] parts = ext.getParts();
				if (parts == null) continue;
				for (Entity part : parts) {
					if (!(part instanceof TFPart<?> tfPart)) continue;
					if (!tfPart.isAlive() || !tfPart.isPickable()) continue;
					if (filter != null && !filter.test(tfPart)) continue;
					if (!tfPart.getBoundingBox().intersects(box)) continue;
					if (augmented == null) augmented = new ArrayList<>(original);
					augmented.add(tfPart);
				}
			}
		}
		if (augmented != null && debugCounter++ % 20 == 0) {
			LOGGER.info("[TF Debug] ProjectileUtilMixin: added {} TFPart(s) to entity pick list (total entities: {} -> {})",
				augmented.size() - original.size(), original.size(), augmented.size());
		}
		return augmented != null ? augmented : original;
	}
}