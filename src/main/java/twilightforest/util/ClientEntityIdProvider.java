package twilightforest.util;

import java.util.concurrent.atomic.AtomicInteger;

import net.minecraft.world.entity.Entity;

/**
 * 26.3: {@code ClientLevel.addEntity()} no longer auto-assigns an entity id, so client-only
 * local entities (ProtectionBox, CharmEffect) must be given a unique id before being added.
 * Vanilla server entity ids are non-negative, so a negative monotonic counter never collides.
 */
public final class ClientEntityIdProvider {
	private static final AtomicInteger NEXT_ID = new AtomicInteger();

	private ClientEntityIdProvider() {
	}

	public static void assignLocalId(Entity entity) {
		entity.setId(NEXT_ID.decrementAndGet());
	}
}
