package twilightforest.network;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ColorParticleOption;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import twilightforest.TwilightForestMod;
import twilightforest.init.TFParticleType;

import java.util.Random;

public record SpawnFallenLeafFromPacket(BlockPos pos, Vec3 motion) implements CustomPacketPayload {

	public static final Type<SpawnFallenLeafFromPacket> TYPE = new Type<>(TwilightForestMod.prefix("spawn_fallen_leaf"));
	public static final StreamCodec<RegistryFriendlyByteBuf, SpawnFallenLeafFromPacket> STREAM_CODEC = CustomPacketPayload.codec(SpawnFallenLeafFromPacket::write, SpawnFallenLeafFromPacket::new);

	public SpawnFallenLeafFromPacket(RegistryFriendlyByteBuf buf) {
		this(buf.readBlockPos(), new Vec3(buf.readDouble(), buf.readDouble(), buf.readDouble()));
	}

	public void write(RegistryFriendlyByteBuf buf) {
		buf.writeBlockPos(this.pos());
		buf.writeDouble(this.motion().x());
		buf.writeDouble(this.motion().y());
		buf.writeDouble(this.motion().z());
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}

	// Client-side handler moved to SpawnFallenLeafFromPacketClientHandler
}
