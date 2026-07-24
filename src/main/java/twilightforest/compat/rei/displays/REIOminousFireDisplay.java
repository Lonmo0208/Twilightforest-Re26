package twilightforest.compat.rei.displays;

import com.mojang.serialization.codecs.RecordCodecBuilder;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.display.DisplaySerializer;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Holder;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import twilightforest.compat.RecipeViewerConstants;
import twilightforest.compat.rei.TFREIClientPlugin;
import twilightforest.compat.rei.categories.REIOminousFireCategory;
import twilightforest.init.TFBlocks;
import twilightforest.util.entities.EntityRenderingUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class REIOminousFireDisplay extends BasicDisplay {

	public static final DisplaySerializer<REIOminousFireDisplay> SERIALIZER = DisplaySerializer.of(
		RecordCodecBuilder.mapCodec(instance -> instance.group(
			EntryIngredient.codec().listOf().fieldOf("inputs").forGetter(REIOminousFireDisplay::getInputEntries),
			EntryIngredient.codec().listOf().fieldOf("outputs").forGetter(REIOminousFireDisplay::getOutputEntries)
		).apply(instance, REIOminousFireDisplay::new)),
		StreamCodec.composite(
			EntryIngredient.streamCodec().apply(ByteBufCodecs.list()),
			REIOminousFireDisplay::getInputEntries,
			EntryIngredient.streamCodec().apply(ByteBufCodecs.list()),
			REIOminousFireDisplay::getOutputEntries,
			REIOminousFireDisplay::new
		)
	);

	private REIOminousFireDisplay(List<EntryIngredient> inputs, List<EntryIngredient> outputs) {
		super(inputs, outputs);
	}

	@Nullable
	public static REIOminousFireDisplay of(RecipeViewerConstants.OminousFireInfo recipe) {
		List<EntryIngredient> inputs = new ArrayList<>();
		List<EntryIngredient> outputs = new ArrayList<>();

		getEntity(recipe.input(), Minecraft.getInstance().level).ifPresent(entity -> {
			inputs.add(EntryIngredients.of(TFREIClientPlugin.ENTITY_DEFINITION, List.of(entity)));
			SpawnEggItem inputEgg = SpawnEggItem.byId(entity.getType()).map(Holder::value).map(item -> (SpawnEggItem) item).orElse(null);
			if (inputEgg != null) {
				inputs.add(EntryIngredients.of(inputEgg));
			}
		});

		inputs.add(EntryIngredients.of(TFREIClientPlugin.BLOCKSTATE_DEFINITION, List.of(TFBlocks.OMINOUS_FIRE.get().defaultBlockState())));

		getEntity(recipe.output(), Minecraft.getInstance().level).ifPresent(entity -> {
			outputs.add(EntryIngredients.of(TFREIClientPlugin.ENTITY_DEFINITION, List.of(entity)));
			SpawnEggItem outputEgg = SpawnEggItem.byId(entity.getType()).map(Holder::value).map(item -> (SpawnEggItem) item).orElse(null);
			if (outputEgg != null) {
				outputs.add(EntryIngredients.of(outputEgg));
			}
		});

		if (!inputs.isEmpty() && !outputs.isEmpty()) {
			return new REIOminousFireDisplay(inputs, outputs);
		}

		return null;
	}

	public static Optional<Entity> getEntity(EntityType<?> type, @Nullable Level level) {
		return Optional.ofNullable(EntityRenderingUtil.fetchEntity(type, level));
	}

	@Override
	public CategoryIdentifier<?> getCategoryIdentifier() {
		return REIOminousFireCategory.OMINOUS_FIRE;
	}

	@Override
	public DisplaySerializer<? extends Display> getSerializer() {
		return SERIALIZER;
	}
}
