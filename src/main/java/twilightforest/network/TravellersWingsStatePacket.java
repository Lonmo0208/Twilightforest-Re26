package twilightforest.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import twilightforest.TwilightForestMod;
import twilightforest.components.entity.TravellersWingsAttachment;
import twilightforest.init.TFDataAttachments;
import twilightforest.util.TFEntityExtensions;

public class TravellersWingsStatePacket implements CustomPacketPayload {

	public static final Type<TravellersWingsStatePacket> TYPE = new Type<>(TwilightForestMod.prefix("travellers_wings_state"));
	public static final StreamCodec<RegistryFriendlyByteBuf, TravellersWingsStatePacket> STREAM_CODEC = CustomPacketPayload.codec(TravellersWingsStatePacket::write, TravellersWingsStatePacket::new);

	public final int entityId;
	public final TravellersWingsAttachment.WingState state;
	public final boolean sidestepLeft;
	public final int doubleJumpTimer;
	public final int sidestepTimer;

	public TravellersWingsStatePacket(int entityId, TravellersWingsAttachment.WingState state, boolean sidestepLeft, int doubleJumpTimer, int sidestepTimer) {
		this.entityId = entityId;
		this.state = state;
		this.sidestepLeft = sidestepLeft;
		this.doubleJumpTimer = doubleJumpTimer;
		this.sidestepTimer = sidestepTimer;
	}

	public TravellersWingsStatePacket(int entityId, TravellersWingsAttachment.WingState state) {
		this(entityId, state, false, 0, 0);
	}

	public TravellersWingsStatePacket(RegistryFriendlyByteBuf buf) {
		this.entityId = buf.readInt();
		this.state = buf.readEnum(TravellersWingsAttachment.WingState.class);
		this.sidestepLeft = buf.readBoolean();
		this.doubleJumpTimer = buf.readInt();
		this.sidestepTimer = buf.readInt();
	}

	// Client-side handler moved to TravellersWingsStatePacketClientHandler

	public void write(RegistryFriendlyByteBuf buf) {
		buf.writeInt(this.entityId);
		buf.writeEnum(this.state);
		buf.writeBoolean(this.sidestepLeft);
		buf.writeInt(this.doubleJumpTimer);
		buf.writeInt(this.sidestepTimer);
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
