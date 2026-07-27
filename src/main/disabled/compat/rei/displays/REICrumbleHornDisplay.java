package twilightforest.compat.rei.displays;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.display.DisplaySerializer;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.entry.EntryStack;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import twilightforest.compat.rei.TFREIClientPlugin;
import twilightforest.compat.rei.categories.REICrumbleHornCategory;

import java.util.List;

public class REICrumbleHornDisplay extends BasicDisplay {

	public static final DisplaySerializer<REICrumbleHornDisplay> SERIALIZER = DisplaySerializer.of(
		RecordCodecBuilder.mapCodec(instance -> instance.group(
			EntryIngredient.codec().listOf().fieldOf("inputs").forGetter(REICrumbleHornDisplay::getInputEntries),
			EntryIngredient.codec().listOf().fieldOf("outputs").forGetter(REICrumbleHornDisplay::getOutputEntries),
			Codec.BOOL.fieldOf("isResultAir").forGetter(d -> d.isResultAir)
		).apply(instance, REICrumbleHornDisplay::new)),
		StreamCodec.composite(
			EntryIngredient.streamCodec().apply(ByteBufCodecs.list()),
			REICrumbleHornDisplay::getInputEntries,
			EntryIngredient.streamCodec().apply(ByteBufCodecs.list()),
			REICrumbleHornDisplay::getOutputEntries,
			ByteBufCodecs.BOOL,
			d -> d.isResultAir,
			REICrumbleHornDisplay::new
		)
	);

	public final boolean isResultAir;

	public REICrumbleHornDisplay(List<EntryIngredient> inputs, List<EntryIngredient> outputs, boolean isResultAir) {
		super(inputs, outputs);
		this.isResultAir = isResultAir;
	}

	public static REICrumbleHornDisplay of(Block input, Block output) {
		boolean isResultAir = output.defaultBlockState().isAir();
		List<EntryIngredient> inputs = List.of(EntryIngredients.of(input));
		List<EntryIngredient> outputs = isResultAir
			? List.of(EntryIngredient.of(EntryStack.of(TFREIClientPlugin.ENTITY_DEFINITION, TFREIClientPlugin.createItemEntity(new ItemStack(input)))))
			: List.of(EntryIngredients.of(output));
		return new REICrumbleHornDisplay(inputs, outputs, isResultAir);
	}

	@Override
	public CategoryIdentifier<?> getCategoryIdentifier() {
		return REICrumbleHornCategory.CRUMBLE_HORN;
	}

	@Override
	public DisplaySerializer<? extends Display> getSerializer() {
		return SERIALIZER;
	}
}
