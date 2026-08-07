package twilightforest.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import twilightforest.TwilightForestMod;

public class FortificationShieldPacket implements CustomPacketPayload {

	public static final Type<FortificationShieldPacket> TYPE = new Type<>(TwilightForestMod.prefix("fortification_shield"));
	public static final StreamCodec<RegistryFriendlyByteBuf, FortificationShieldPacket> STREAM_CODEC = CustomPacketPayload.codec(FortificationShieldPacket::write, FortificationShieldPacket::new);

	public final int entityId;
	public final int temporaryShields;
	public final int permanentShields;

	public FortificationShieldPacket(int entityId, int temporaryShields, int permanentShields) {
		this.entityId = entityId;
		this.temporaryShields = temporaryShields;
		this.permanentShields = permanentShields;
	}

	public FortificationShieldPacket(RegistryFriendlyByteBuf buf) {
		this.entityId = buf.readInt();
		this.temporaryShields = buf.readInt();
		this.permanentShields = buf.readInt();
	}

	public void write(RegistryFriendlyByteBuf buf) {
		buf.writeInt(this.entityId);
		buf.writeInt(this.temporaryShields);
		buf.writeInt(this.permanentShields);
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}