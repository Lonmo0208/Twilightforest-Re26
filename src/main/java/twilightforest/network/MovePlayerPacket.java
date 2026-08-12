package twilightforest.network;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import twilightforest.TwilightForestMod;

public record MovePlayerPacket(double motionX, double motionY, double motionZ) implements CustomPacketPayload {

	public static final Type<MovePlayerPacket> TYPE = new Type<>(TwilightForestMod.prefix("move_player"));
	public static final StreamCodec<RegistryFriendlyByteBuf, MovePlayerPacket> STREAM_CODEC = CustomPacketPayload.codec(MovePlayerPacket::write, MovePlayerPacket::new);

	public MovePlayerPacket(FriendlyByteBuf buf) {
		this(buf.readDouble(), buf.readDouble(), buf.readDouble());
	}

	public void write(FriendlyByteBuf buf) {
		buf.writeDouble(this.motionX());
		buf.writeDouble(this.motionY());
		buf.writeDouble(this.motionZ());
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}

	// No client-side push needed - Entity.push() on server already syncs via needsSync
	// This handler exists only for registration compatibility
	public static void handle(MovePlayerPacket message, ClientPlayNetworking.Context context) {
		// Push is handled by entity sync (needsSync=true in Entity.push())
		// Do NOT apply push here again - it causes double velocity on client
	}
}
