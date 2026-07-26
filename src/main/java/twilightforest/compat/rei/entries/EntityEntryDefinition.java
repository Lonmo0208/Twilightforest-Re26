package twilightforest.compat.rei.entries;

import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.entry.renderer.EntryRenderer;
import me.shedaniel.rei.api.client.gui.compat.GuiGraphics;
import me.shedaniel.rei.api.client.gui.widgets.Tooltip;
import me.shedaniel.rei.api.client.gui.widgets.TooltipContext;
import me.shedaniel.rei.api.common.entry.EntrySerializer;
import me.shedaniel.rei.api.common.entry.EntryStack;
import me.shedaniel.rei.api.common.entry.comparison.ComparisonContext;
import me.shedaniel.rei.api.common.entry.type.EntryDefinition;
import me.shedaniel.rei.api.common.entry.type.EntryType;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.util.ProblemReporter;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.livingblock.LivingBlock;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.TagValueInput;
import net.minecraft.world.level.storage.TagValueOutput;
import org.jetbrains.annotations.Nullable;
import twilightforest.TwilightForestMod;
import twilightforest.util.entities.EntityRenderingUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public class EntityEntryDefinition implements EntryDefinition<Entity> {

	public static EntryType<Entity> ENTITY_TYPE = EntryType.deferred(TwilightForestMod.prefix("entity"));

	private final EntryRenderer<Entity> renderer;

	public EntityEntryDefinition() {
		this.renderer = new EntityRenderer();
	}

	@Override
	public Class<Entity> getValueType() {
		return Entity.class;
	}

	@Override
	public EntryType<Entity> getType() {
		return ENTITY_TYPE;
	}

	@Override
	public EntryRenderer<Entity> getRenderer() {
		return this.renderer;
	}

	@Override
	public @Nullable Identifier getIdentifier(EntryStack<Entity> entry, Entity value) {
		return BuiltInRegistries.ENTITY_TYPE.getKey(value.getType());
	}

	@Override
	public boolean isEmpty(EntryStack<Entity> entry, Entity value) {
		return false;
	}

	@Override
	public Entity copy(EntryStack<Entity> entry, Entity value) {
		String string = value.getEncodeId();
		TagValueOutput output = TagValueOutput.createWithoutContext(ProblemReporter.DISCARDING);

		if (string != null) {
			output.putString("id", string);
			value.saveWithoutId(output);
		}

		Entity entity = value.getType().create(Minecraft.getInstance().level, EntitySpawnReason.LOAD);

		entity.load(TagValueInput.create(ProblemReporter.DISCARDING, Minecraft.getInstance().level.registryAccess(), output.buildResult()));

		return entity;
	}

	@Override
	public Entity normalize(EntryStack<Entity> entry, Entity value) {
		return this.copy(entry, value);
	}

	@Override
	public Entity wildcard(EntryStack<Entity> entry, Entity value) {
		return value.getType().create(Minecraft.getInstance().level, EntitySpawnReason.LOAD);
	}

	@Override
	public @Nullable ItemStack cheatsAs(EntryStack<Entity> entry, Entity value) {
		SpawnEggItem egg = SpawnEggItem.byId(value.getType()).map(Holder::value).map(item -> (SpawnEggItem) item).orElse(null);
		if (egg != null) {
			return new ItemStack(egg);
		}
		return EntryDefinition.super.cheatsAs(entry, value);
	}

	@Override
	public @Nullable Entity add(Entity o1, Entity o2) {
		TagValueOutput output = TagValueOutput.createWithoutContext(ProblemReporter.DISCARDING);
		o1.saveWithoutId(output);
		o2.saveWithoutId(output);
		Entity copy = o1.getType().create(Minecraft.getInstance().level, EntitySpawnReason.LOAD);
		copy.load(TagValueInput.create(ProblemReporter.DISCARDING, Minecraft.getInstance().level.registryAccess(), output.buildResult()));
		return copy;
	}

	@Override
	public long hash(EntryStack<Entity> entry, Entity value, ComparisonContext context) {
		int code = 1;
		code = 31 * code + System.identityHashCode(value);
		code = 31 * code + Long.hashCode(EntityComparatorImpl.INSTANCE.hashOf(context, value));
		return code;
	}

	@Override
	public boolean equals(Entity o1, Entity o2, ComparisonContext context) {
		if (o1 != o2)
			return false;
		return EntityComparatorImpl.INSTANCE.hashOf(context, o1) == EntityComparatorImpl.INSTANCE.hashOf(context, o2);
	}

	@Override
	public @Nullable EntrySerializer<Entity> getSerializer() {
		return null;
	}

	@Override
	public Component asFormattedText(EntryStack<Entity> entry, Entity value) {
		return this.asFormattedText(entry, value, TooltipContext.of(Item.TooltipContext.EMPTY));
	}

	@Override
	public Component asFormattedText(EntryStack<Entity> entry, Entity value, TooltipContext context) {
		return value.getType().getDescription();
	}

	@Override
	public Stream<? extends TagKey<?>> getTagsFor(EntryStack<Entity> entry, Entity value) {
		return value.getType().builtInRegistryHolder().tags();
	}

	public static class EntityRenderer implements EntryRenderer<Entity> {

		@Override
		public void render(EntryStack<Entity> entry, GuiGraphics graphics, Rectangle bounds, int mouseX, int mouseY, float delta) {
			if (!entry.isEmpty()) {
				graphics.pose().pushMatrix();
				graphics.pose().translate((float) bounds.getX(), (float) bounds.getY());
				EntityRenderingUtil.renderEntity(graphics, entry.getValue().getType(), 32);
				graphics.pose().popMatrix();
			}
		}

		@Override
		@Nullable
		public Tooltip getTooltip(EntryStack<Entity> entry, TooltipContext context) {
			if (entry.isEmpty()) return null;
			Tooltip tooltip = Tooltip.create();
			EntityRenderingUtil.getMobTooltip(entry.getValue().getType()).forEach(tooltip::add);
			return tooltip;
		}
	}

	public static class LivingBlockRenderer implements EntryRenderer<Entity> {
		private final float bobOffs;

		public LivingBlockRenderer() {
			this.bobOffs = RandomSource.create().nextFloat() * (float) Math.PI * 2.0F;
		}

		@Override
		public void render(EntryStack<Entity> entry, GuiGraphics graphics, Rectangle bounds, int mouseX, int mouseY, float delta) {
			ItemStack item = ((LivingBlock) entry.getValue()).getItemStack();

			if (!entry.isEmpty()) {
				Level level = Minecraft.getInstance().level;

				graphics.pose().pushMatrix();
				graphics.pose().translate((float) bounds.getX(), (float) bounds.getY());

				if (level != null) {
					try {
						EntityRenderingUtil.renderLivingBlock(graphics, item, level, this.bobOffs);
					} catch (Exception e) {
						TwilightForestMod.LOGGER.error("Error drawing item in REI!", e);
					}
				}

				graphics.pose().popMatrix();
			}
		}

		@Override
		@Nullable
		public Tooltip getTooltip(EntryStack<Entity> entry, TooltipContext context) {
			ItemStack item = ((LivingBlock) entry.getValue()).getItemStack();

			List<Component> tooltip = new ArrayList<>();

			tooltip.add(item.getHoverName());

			if (context.getFlag().isAdvanced()) {
				tooltip.add(Component.literal(Objects.requireNonNull(BuiltInRegistries.ITEM.getKey(item.getItem())).toString()).withStyle(ChatFormatting.DARK_GRAY));
			}

			return Tooltip.create(context.getPoint(), tooltip);
		}
	}
}
