package twilightforest.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.DeathScreen;
import twilightforest.ClientProxy;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ColorParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import twilightforest.init.TFItems;
import twilightforest.init.TFParticleType;
import twilightforest.item.LifedrainScepterItem;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.client.multiplayer.ClientAdvancements;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.Identifier;
import twilightforest.util.PlayerHelper;

/**
 * Initializes the client proxy fields with real implementations.
 * Called from the client entry point.
 */
public class ClientProxyInitializer {

	private static final BiomeMapGenerator biomeMapGenerator = new BiomeMapGenerator();

	public static void init() {
		ClientProxy.uberousSoilAnimator = (state, level, pos, rand) -> {
			if (Minecraft.getInstance().player != null && Minecraft.getInstance().player.isHolding(TFItems.MAGIC_BEANS)) {
				for (int i = 0; i < 2; i++) {
					level.addParticle(ParticleTypes.HAPPY_VILLAGER, pos.getX() + rand.nextDouble(), pos.getY() + 1.25D, pos.getZ() + rand.nextDouble(), 0.0D, 0.0D, 0.0D);
				}
			}
		};

		ClientProxy.lifedrainTrailRenderer = new ClientProxy.LifedrainTrailRenderer() {
			@Override
			public void makeRedMagicTrail(Level level, LivingEntity source, Vec3 target) {
				Vec3 handPos = getPlayerHandPos(source, Minecraft.getInstance().getDeltaTracker().getGameTimeDeltaPartialTick(false));
				double distance = handPos.distanceTo(target);

				for (double i = 0; i <= distance * 3; i++) {
					Vec3 particlePos = handPos.subtract(target).scale(i / (distance * 3));
					particlePos = handPos.subtract(particlePos);
					float r = 1.0F;
					float g = 0.5F;
					float b = 0.5F;
					level.addParticle(ColorParticleOption.create(TFParticleType.MAGIC_EFFECT, r, g, b), particlePos.x(), particlePos.y(), particlePos.z(), 0.0D, 0.0D, 0.0D);
				}
			}

			@Override
			public Vec3 getPlayerHandPos(LivingEntity living, float partialTicks) {
				float armSwing = Mth.sin(Mth.sqrt(living.getAttackAnim(partialTicks)) * (float) Math.PI);
				int invert = living.getMainArm() == HumanoidArm.RIGHT ? 1 : -1;
				if (!(living.getMainHandItem().getItem() instanceof LifedrainScepterItem)) invert = -invert;

				Minecraft minecraft = Minecraft.getInstance();
				if (minecraft.options.getCameraType().isFirstPerson() && living == minecraft.player) {
					float fov = minecraft.options.fov().get();
					double viewBobbingScale = 960.0 / fov;
					Vec3 vec3 = minecraft.getEntityRenderDispatcher()
						.camera
						.getNearPlane(fov)
						.getPointOnPlane((float) invert * 0.525F, -0.1F)
						.scale(viewBobbingScale)
						.yRot(armSwing * 0.5F)
						.xRot(-armSwing * 0.7F);
					return living.getEyePosition(partialTicks).add(vec3);
				} else {
					float yRot = Mth.lerp(partialTicks, living.yBodyRotO, living.yBodyRot) * Mth.DEG_TO_RAD;
					double sin = Mth.sin(yRot);
					double cos = Mth.cos(yRot);
					float scale = living.getScale();
					double offset = (double) invert * 0.35 * (double) scale;
					double factor = 0.8 * (double) scale;
					float crouch = living.isCrouching() ? -0.1875F : 0.0F;
					return living.getEyePosition(partialTicks).add(-cos * offset - sin * factor, (double) crouch - 0.45 * (double) scale, -sin * offset + cos * factor);
				}
			}
		};

		ClientProxy.portalAttachmentHandler = player -> {
			if (player instanceof LocalPlayer local) {
				Minecraft minecraft = Minecraft.getInstance();
				if (minecraft.screen != null && !minecraft.screen.isPauseScreen() && !(minecraft.screen instanceof DeathScreen)) {
					if (minecraft.screen instanceof AbstractContainerScreen) local.closeContainer();
					minecraft.setScreen(null);
				}
				return true;
			}
			return false;
		};

		ClientProxy.biomeMapGenerator = (source, w, h, show) -> biomeMapGenerator.createMap(source, w, h, show);

		ClientProxy.advancementChecker = new ClientProxy.AdvancementChecker() {
			@Override
			public Object getAdvancement(Object player, Object advancementLocation) {
				if (player instanceof LocalPlayer localPlayer) {
					ClientAdvancements manager = localPlayer.connection.getAdvancements();
					return manager.get((Identifier) advancementLocation);
				}
				return null;
			}

			@Override
			public boolean doesPlayerHaveRequiredAdvancement(Object player, Object holder) {
				if (player instanceof LocalPlayer local) {
					ClientAdvancements manager = local.connection.getAdvancements();
					if (holder == null) return false;
					AdvancementProgress progress = manager.progress.get((AdvancementHolder) holder);
					return progress != null && progress.isDone();
				}
				return false;
			}

			@Override
			@SuppressWarnings("unchecked")
			public boolean playerHasRequiredAdvancements(Object player, Iterable<Object> requiredAdvancements) {
				if (player instanceof LocalPlayer local) {
					ClientAdvancements manager = local.connection.getAdvancements();
					for (Object advancementLocation : requiredAdvancements) {
						AdvancementHolder adv = manager.get((Identifier) advancementLocation);
						if (adv == null) return false;
						AdvancementProgress progress = manager.progress.get(adv);
						if (progress == null || !progress.isDone()) return false;
					}
					return true;
				}
				return false;
			}
		};
	}
}