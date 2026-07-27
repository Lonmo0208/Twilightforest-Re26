package twilightforest.init.custom;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.Registry;
import twilightforest.TFRegistries;
import twilightforest.TwilightForestMod;
import twilightforest.item.travellers_gear.modifiers.*;

public class TravellersModifierTypes {

	public static final MapCodec<TravellersEntryModifier> ATTRIBUTE_ENTRY = TravellersEntryModifier.CODEC;
	public static final MapCodec<BuiltinTravellersComponentModifier> BUILTIN = BuiltinTravellersComponentModifier.CODEC;
	public static final MapCodec<TravellersComponentModifier> COMPONENT = TravellersComponentModifier.CODEC;
	public static final MapCodec<TransferableComponentModifier> TRANSFERABLE_COMPONENT = TransferableComponentModifier.CODEC;

	public static void init() {
		Registry.register(TFRegistries.TRAVELLERS_MODIFIER_TYPE, TwilightForestMod.prefix("attribute"), ATTRIBUTE_ENTRY);
		Registry.register(TFRegistries.TRAVELLERS_MODIFIER_TYPE, TwilightForestMod.prefix("builtin"), BUILTIN);
		Registry.register(TFRegistries.TRAVELLERS_MODIFIER_TYPE, TwilightForestMod.prefix("component"), COMPONENT);
		Registry.register(TFRegistries.TRAVELLERS_MODIFIER_TYPE, TwilightForestMod.prefix("transferable_component"), TRANSFERABLE_COMPONENT);
	}
}