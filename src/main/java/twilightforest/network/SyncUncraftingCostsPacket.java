package twilightforest.network;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import twilightforest.TwilightForestMod;

public record SyncUncraftingCostsPacket(int uncraftingCost, int recraftingCost) implements CustomPacketPayload {

	public static final Type<SyncUncraftingCostsPacket> TYPE = new Type<>(TwilightForestMod.prefix("sync_uncrafting_costs"));
	public static final StreamCodec<RegistryFriendlyByteBuf, SyncUncraftingCostsPacket> STREAM_CODEC = CustomPacketPayload.codec(SyncUncraftingCostsPacket::write, SyncUncraftingCostsPacket::new);

	public SyncUncraftingCostsPacket(FriendlyByteBuf buf) {
		this(buf.readInt(), buf.readInt());
	}

	public void write(FriendlyByteBuf buf) {
		buf.writeInt(this.uncraftingCost());
		buf.writeInt(this.recraftingCost());
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}

	public static void send(ServerPlayer player, int uncraftingCost, int recraftingCost) {
		ServerPlayNetworking.send(player, new SyncUncraftingCostsPacket(uncraftingCost, recraftingCost));
	}
}
