package twilightforest.compat.rei.displays;

import com.mojang.serialization.Codec;
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
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import twilightforest.compat.RecipeViewerConstants;
import twilightforest.compat.rei.TFREIClientPlugin;
import twilightforest.compat.rei.categories.REITransformationPowderCategory;
import twilightforest.util.entities.EntityRenderingUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class REITransformationPowderDisplay extends BasicDisplay {

	public static final DisplaySerializer<REITransformationPowderDisplay> SERIALIZER = DisplaySerializer.of(
		RecordCodecBuilder.mapCodec(instance -> instance.group(
			EntryIngredient.codec().listOf().fieldOf("inputs").forGetter(REITransformationPowderDisplay::getInputEntries),
			EntryIngredient.codec().listOf().fieldOf("outputs").forGetter(REITransformationPowderDisplay::getOutputEntries),
			Codec.BOOL.fieldOf("isReversible").forGetter(d -> d.isReversible)
		).apply(instance, REITransformationPowderDisplay::new)),
		StreamCodec.composite(
			EntryIngredient.streamCodec().apply(ByteBufCodecs.list()),
			REITransformationPowderDisplay::getInputEntries,
			EntryIngredient.streamCodec().apply(ByteBufCodecs.list()),
			REITransformationPowderDisplay::getOutputEntries,
			ByteBufCodecs.BOOL,
			d -> d.isReversible,
			REITransformationPowderDisplay::new
		)
	);

	public final boolean isReversible;

	private REITransformationPowderDisplay(List<EntryIngredient> inputs, List<EntryIngredient> outputs, boolean reversible) {
		super(inputs, outputs);
		this.isReversible = reversible;
	}

	@Nullable
	public static REITransformationPowderDisplay of(RecipeViewerConstants.TransformationPowderInfo recipe) {
		List<EntryIngredient> inputs = new ArrayList<>();
		List<EntryIngredient> outputs = new ArrayList<>();

		getEntity(recipe.input(), Minecraft.getInstance().level).ifPresent(entity -> {
			inputs.add(EntryIngredients.of(TFREIClientPlugin.ENTITY_DEFINITION, List.of(entity)));
			SpawnEggItem inputEgg = SpawnEggItem.byId(entity.getType()).map(Holder::value).map(item -> (SpawnEggItem) item).orElse(null);
			if (inputEgg != null) {
				inputs.add(EntryIngredients.of(inputEgg));
			}
		});

		getEntity(recipe.output(), Minecraft.getInstance().level).ifPresent(entity -> {
			outputs.add(EntryIngredients.of(TFREIClientPlugin.ENTITY_DEFINITION, List.of(entity)));
			SpawnEggItem outputEgg = SpawnEggItem.byId(entity.getType()).map(Holder::value).map(item -> (SpawnEggItem) item).orElse(null);
			if (outputEgg != null) {
				outputs.add(EntryIngredients.of(outputEgg));
			}
		});

		if (!inputs.isEmpty() && !outputs.isEmpty()) {
			if (recipe.reversible()) {
				inputs.addAll(outputs);
				outputs.addAll(inputs);
			}

			return new REITransformationPowderDisplay(inputs, outputs, recipe.reversible());
		}

		return null;
	}

	public static Optional<Entity> getEntity(EntityType<?> type, @Nullable Level level) {
		return Optional.ofNullable(EntityRenderingUtil.fetchEntity(type, level));
	}

	@Override
	public CategoryIdentifier<?> getCategoryIdentifier() {
		return REITransformationPowderCategory.TRANSFORMATION;
	}

	@Override
	public DisplaySerializer<? extends Display> getSerializer() {
		return SERIALIZER;
	}
}
