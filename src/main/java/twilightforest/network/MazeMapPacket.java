package twilightforest.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.game.ClientboundMapItemDataPacket;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.maps.MapId;
import twilightforest.TwilightForestMod;
import twilightforest.item.mapdata.TFMazeMapData;

// Rewraps vanilla ClientboundMapItemDataPacket to properly add our own data
public record MazeMapPacket(ClientboundMapItemDataPacket inner, boolean ore, int yCenter) implements CustomPacketPayload {

	public static final Type<MazeMapPacket> TYPE = new Type<>(TwilightForestMod.prefix("maze_map"));

	public static final StreamCodec<RegistryFriendlyByteBuf, MazeMapPacket> STREAM_CODEC = StreamCodec.composite(
		ClientboundMapItemDataPacket.STREAM_CODEC, MazeMapPacket::inner,
		ByteBufCodecs.BOOL, MazeMapPacket::ore,
		ByteBufCodecs.INT, MazeMapPacket::yCenter,
		MazeMapPacket::new
	);

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}

	/**
	 * Converts this custom payload back to a vanilla packet for the standard update packet pipeline.
	 * The custom data (ore, yCenter) is sent separately through the Fabric custom payload channel.
	 */
	public ClientboundMapItemDataPacket toVanillaClientbound() {
		return this.inner;
	}

	// Client-side handler moved to MazeMapPacketClientHandler
}