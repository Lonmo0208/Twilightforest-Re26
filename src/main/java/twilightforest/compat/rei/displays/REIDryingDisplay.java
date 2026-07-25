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
import twilightforest.compat.rei.TFREIClientPlugin;
import twilightforest.compat.rei.categories.REIDryingCategory;
import twilightforest.init.TFBlocks;
import twilightforest.item.recipe.DryingRecipe;

import java.util.List;

public class REIDryingDisplay extends BasicDisplay {

	public static final DisplaySerializer<REIDryingDisplay> SERIALIZER = DisplaySerializer.of(
		RecordCodecBuilder.mapCodec(instance -> instance.group(
			EntryIngredient.codec().listOf().fieldOf("inputs").forGetter(REIDryingDisplay::getInputEntries),
			EntryIngredient.codec().listOf().fieldOf("outputs").forGetter(REIDryingDisplay::getOutputEntries),
			Codec.INT.fieldOf("drying_time").forGetter(REIDryingDisplay::getDryingTime)
		).apply(instance, REIDryingDisplay::new)),
		StreamCodec.composite(
			EntryIngredient.streamCodec().apply(ByteBufCodecs.list()),
			REIDryingDisplay::getInputEntries,
			EntryIngredient.streamCodec().apply(ByteBufCodecs.list()),
			REIDryingDisplay::getOutputEntries,
			ByteBufCodecs.INT,
			REIDryingDisplay::getDryingTime,
			REIDryingDisplay::new
		)
	);

	private final int dryingTime;

	public REIDryingDisplay(List<EntryIngredient> inputs, List<EntryIngredient> outputs, int dryingTime) {
		super(inputs, outputs);
		this.dryingTime = dryingTime;
	}

	public static REIDryingDisplay of(DryingRecipe recipe) {
		List<EntryIngredient> inputs = List.of(EntryIngredients.ofIngredient(recipe.getInput()), EntryIngredient.of(EntryStack.of(TFREIClientPlugin.BLOCKSTATE_DEFINITION, TFBlocks.OAK_DRYING_RACK.get().defaultBlockState())));
		List<EntryIngredient> outputs = List.of(EntryIngredients.of(recipe.getResult()));
		return new REIDryingDisplay(inputs, outputs, recipe.getDryingTime());
	}

	public int getDryingTime() {
		return this.dryingTime;
	}

	@Override
	public CategoryIdentifier<?> getCategoryIdentifier() {
		return REIDryingCategory.DRYING;
	}

	@Override
	public DisplaySerializer<? extends Display> getSerializer() {
		return SERIALIZER;
	}
}
