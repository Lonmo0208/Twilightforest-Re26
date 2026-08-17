package twilightforest.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import twilightforest.TwilightForestMod;
import twilightforest.config.TFConfig;

import java.util.ArrayList;
import java.util.List;

public record SyncUncraftingTableConfigPacket(
	double uncraftingMultiplier, double repairingMultiplier,
	boolean allowShapeless, boolean disableIngredientSwitching, boolean disabledUncrafting, boolean disabledTable,
	List<? extends String> disabledRecipes, boolean flipRecipeList,
	List<? extends String> disabledModids, boolean flipModidList) implements CustomPacketPayload {

	public static final Type<SyncUncraftingTableConfigPacket> TYPE = new Type<>(TwilightForestMod.prefix("sync_uncrafting_config"));
	public static final StreamCodec<RegistryFriendlyByteBuf, SyncUncraftingTableConfigPacket> STREAM_CODEC = CustomPacketPayload.codec(SyncUncraftingTableConfigPacket::write, SyncUncraftingTableConfigPacket::new);

	public SyncUncraftingTableConfigPacket(FriendlyByteBuf buf) {
		this(buf.readDouble(), buf.readDouble(),
			buf.readBoolean(), buf.readBoolean(), buf.readBoolean(), buf.readBoolean(),
			readStringList(buf), buf.readBoolean(),
			readStringList(buf), buf.readBoolean());
	}

	private static List<String> readStringList(FriendlyByteBuf buf) {
		List<String> list = new ArrayList<>();
		buf.readWithCount(b -> list.add(b.readUtf()));
		return list;
	}

	public void write(FriendlyByteBuf buf) {
		buf.writeDouble(this.uncraftingMultiplier());
		buf.writeDouble(this.repairingMultiplier());
		buf.writeBoolean(this.allowShapeless());
		buf.writeBoolean(this.disableIngredientSwitching());
		buf.writeBoolean(this.disabledUncrafting());
		buf.writeBoolean(this.disabledTable());
		writeStringList(buf, this.disabledRecipes());
		buf.writeBoolean(this.flipRecipeList());
		writeStringList(buf, this.disabledModids());
		buf.writeBoolean(this.flipModidList());
	}

	private static void writeStringList(FriendlyByteBuf buf, List<? extends String> list) {
		buf.writeVarInt(list.size());
		list.forEach(buf::writeUtf);
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}

	// Client-side handler moved to SyncUncraftingTableConfigPacketClientHandler
}
