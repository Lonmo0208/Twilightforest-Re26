package twilightforest.mixin;

import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import twilightforest.network.PacketDistributor;
import twilightforest.network.UpdateTFMultipartPacket;
import twilightforest.util.TFEntityExtensions;

import java.util.function.Consumer;

/**
 * Fabric equivalent of the NeoForge ASM transformer {@code SendDirtyEntityDataTransformer}.
 * Injects multipart entity data sync so that multipart entity data (position, rotation,
 * dimensions) is synchronized to the client for rendering TFPart entities like
 * NagaSegment, HydraHead, HydraNeck, and SnowQueenIceShield.
 * <p>
 * Two injection points:
 * <ul>
 *   <li>{@link ServerEntity#sendPairingData} - initial sync when a player starts tracking the entity</li>
 *   <li>{@link ServerEntity#sendDirtyEntityData} - subsequent syncs when entity data changes</li>
 * </ul>
 */
@Mixin(ServerEntity.class)
public class ServerEntityMixin {

	@Shadow
	private Entity entity;

	/**
	 * Send initial multipart entity data when a player starts tracking this entity.
	 * Without this, the client creates TFPart entities but they have no position/rotation
	 * data until the first sendDirtyEntityData call, resulting in invisible parts.
	 */
	@Inject(method = "sendPairingData", at = @At("TAIL"))
	private void tf$sendInitialMultipartEntityData(ServerPlayer player, Consumer<Packet<ClientGamePacketListener>> broadcast, CallbackInfo ci) {
		if (this.entity instanceof TFEntityExtensions extensions && extensions.isMultipartEntity()) {
			PacketDistributor.sendToPlayer(player, new UpdateTFMultipartPacket(this.entity));
		}
	}

	@Inject(method = "sendDirtyEntityData", at = @At("HEAD"))
	private void tf$sendDirtyMultipartEntityData(CallbackInfo ci) {
		if (this.entity instanceof TFEntityExtensions extensions && extensions.isMultipartEntity()) {
			PacketDistributor.sendToPlayersTrackingEntity(this.entity, new UpdateTFMultipartPacket(this.entity));
		}
	}
}