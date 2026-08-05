package twilightforest.events;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

import twilightforest.beanification.Component;
import twilightforest.beanification.PostConstruct;
import twilightforest.entity.IHostileMount;
import twilightforest.init.TFDamageTypes;
import twilightforest.init.TFDataAttachments;
import twilightforest.util.TFEntityExtensions;

@Component
public class HostileMountEvents {

	public static volatile boolean allowDismount = false;

	@PostConstruct
	private void setup() {
		// All handlers are implemented via mixins:
		// - handleMountDamage               → LivingEntityMixin → EntityEvents.handleMountDamage()
		// - preventTeleportingOffHostileMounts → EntityTeleportMixin
		// - preventMountDismount            → EntityStopRidingMixin
		// - preventHostilMountCrouching     → LivingEntityMixin → EntityEvents.tickShields()
	}


	private void handleMountDamage(FabricEvents.LivingIncomingDamageEvent event) {
		LivingEntity living = event.getEntity();
		DamageSource damageSource = event.getSource();
		// lets not make the player take suffocation damage if riding something
		if (living instanceof Player && isRidingUnfriendly(living) && damageSource.is(DamageTypes.IN_WALL)) {
			event.setCanceled(true);
		}

		if (damageSource.is(DamageTypes.FALL) && ((TFEntityExtensions) living).twilightforest$getData(TFDataAttachments.YETI_THROWING).getThrown()) {
			float amount = event.getAmount();
			event.setCanceled(true);
			living.hurt(TFDamageTypes.getEntityDamageSource(living.level(), TFDamageTypes.YEETED, ((TFEntityExtensions) living).twilightforest$getData(TFDataAttachments.YETI_THROWING).getThrower()), amount);
		}
	}

	private void preventTeleportingOffHostileMounts(FabricEvents.EntityTeleportEvent event) {
		// if our grabbed target tries to teleport dont let them
		if (event.getEntity() instanceof LivingEntity living && isRidingUnfriendly(living)) {
			event.setCanceled(true);
		}
	}

	public static void hostileDismount(Entity rider) {
		HostileMountEvents.allowDismount = true;
		rider.stopRiding();
		HostileMountEvents.allowDismount = false;
	}

	private void preventMountDismount(FabricEvents.EntityMountEvent event) {
		if (!event.getLevel().isClientSide() &&
			!event.isMounting() && event.getEntityBeingMounted().isAlive() &&
			event.getEntityMounting() instanceof Player player && player.isAlive() &&
			isRidingUnfriendly(player) && !allowDismount && !player.getAbilities().invulnerable)
			event.setCanceled(true);
	}

	private void preventHostilMountCrouching(FabricEvents.EntityTickEvent.Post event) {
		if (event.getEntity() instanceof IHostileMount)
			event.getEntity().getPassengers().forEach(e -> e.setShiftKeyDown(false));
	}

	public static boolean isRidingUnfriendly(LivingEntity entity) {
		return entity.isPassenger() && entity.getVehicle() instanceof IHostileMount;
	}
}
