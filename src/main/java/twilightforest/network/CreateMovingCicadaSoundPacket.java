package twilightforest.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import twilightforest.TwilightForestMod;

public record CreateMovingCicadaSoundPacket(int entityID) implements CustomPacketPayload {

	public static final Type<CreateMovingCicadaSoundPacket> TYPE = new Type<>(TwilightForestMod.prefix("create_cicada_sound"));
	public static final StreamCodec<RegistryFriendlyByteBuf, CreateMovingCicadaSoundPacket> STREAM_CODEC = CustomPacketPayload.codec(CreateMovingCicadaSoundPacket::write, CreateMovingCicadaSoundPacket::new);

	public CreateMovingCicadaSoundPacket(FriendlyByteBuf buf) {
		this(buf.readInt());
	}

	public void write(FriendlyByteBuf buf) {
		buf.writeInt(this.entityID());
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}

	// Client-side handler moved to CreateMovingCicadaSoundPacketClientHandler
}
