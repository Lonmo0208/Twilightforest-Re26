package twilightforest.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.game.ClientboundMapItemDataPacket;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.maps.MapId;
import twilightforest.TwilightForestMod;
import twilightforest.item.mapdata.TFMagicMapData;

import java.util.List;

// Rewraps vanilla ClientboundMapItemDataPacket to sync conquered status of structures
public record MagicMapPacket(ClientboundMapItemDataPacket inner, List<String> conqueredStructures) implements CustomPacketPayload {

	public static final Type<MagicMapPacket> TYPE = new Type<>(TwilightForestMod.prefix("magic_map"));
	public static final StreamCodec<RegistryFriendlyByteBuf, MagicMapPacket> STREAM_CODEC = StreamCodec.composite(
		ClientboundMapItemDataPacket.STREAM_CODEC, MagicMapPacket::inner,
		ByteBufCodecs.STRING_UTF8.apply(ByteBufCodecs.list()), MagicMapPacket::conqueredStructures,
		MagicMapPacket::new);

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}

	/**
	 * Converts this custom payload back to a vanilla packet for the standard update packet pipeline.
	 * The custom data (conqueredStructures) is sent separately through the Fabric custom payload channel.
	 */
	public ClientboundMapItemDataPacket toVanillaClientbound() {
		return this.inner;
	}

	// Client-side handler moved to MagicMapPacketClientHandler
}