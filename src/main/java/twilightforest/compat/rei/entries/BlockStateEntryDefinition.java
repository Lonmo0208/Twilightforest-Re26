package twilightforest.compat.rei.entries;

import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.entry.renderer.EntryRenderer;
import me.shedaniel.rei.api.client.gui.widgets.Tooltip;
import me.shedaniel.rei.api.client.gui.widgets.TooltipContext;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.entry.EntrySerializer;
import me.shedaniel.rei.api.common.entry.EntryStack;
import me.shedaniel.rei.api.common.entry.comparison.ComparisonContext;
import me.shedaniel.rei.api.common.entry.type.EntryDefinition;
import me.shedaniel.rei.api.common.entry.type.EntryType;
import me.shedaniel.rei.api.client.gui.compat.GuiGraphics;
import com.mojang.serialization.Codec;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import twilightforest.TwilightForestMod;
import twilightforest.compat.RecipeViewerConstants;

import java.util.Objects;
import java.util.stream.Stream;

public class BlockStateEntryDefinition implements EntryDefinition<BlockState>, EntrySerializer<BlockState> {

	public static EntryType<BlockState> BLOCKSTATE = EntryType.deferred(TwilightForestMod.prefix("blockstate"));

	private final EntryRenderer<BlockState> renderer;

	public BlockStateEntryDefinition() {
		this.renderer = new FlatBlockRenderer();
	}

	@Override
	public Class<BlockState> getValueType() {
		return BlockState.class;
	}

	@Override
	public EntryType<BlockState> getType() {
		return BLOCKSTATE;
	}

	@Override
	public EntryRenderer<BlockState> getRenderer() {
		return renderer;
	}

	@Override
	@Nullable
	public Identifier getIdentifier(EntryStack<BlockState> entry, BlockState value) {
		return BuiltInRegistries.BLOCK.getKey(value.getBlock());
	}

	@Override
	public boolean isEmpty(EntryStack<BlockState> entry, BlockState value) {
		return value.isEmpty();
	}

	@Override
	public BlockState copy(EntryStack<BlockState> entry, BlockState value) {
		return value;
	}

	@Override
	public BlockState normalize(EntryStack<BlockState> entry, BlockState value) {
		return value;
	}

	@Override
	public BlockState wildcard(EntryStack<BlockState> entry, BlockState value) {
		return value.getBlock().defaultBlockState();
	}

	@Nullable
	@Override
	public ItemStack cheatsAs(EntryStack<BlockState> entry, BlockState value) {
		return new ItemStack(value.getBlock().asItem());
	}

	@Nullable
	@Override
	//TODO...?
	public BlockState add(BlockState o1, BlockState o2) {
		return o1;
	}

	@Override
	public long hash(EntryStack<BlockState> entry, BlockState value, ComparisonContext context) {
		return Objects.hash(value.getBlock(), value.getProperties());
	}

	@Override
	public boolean equals(BlockState o1, BlockState o2, ComparisonContext context) {
		if (o1.getBlock() != o2.getBlock())
			return false;
		return o1.getProperties().equals(o2.getProperties());
	}

	@Override
	@Nullable
	public EntrySerializer<BlockState> getSerializer() {
		return this;
	}

	@Override
	public Component asFormattedText(EntryStack<BlockState> entry, BlockState value) {
		return Component.literal(value.toString());
	}

	@Override
	public Stream<? extends TagKey<?>> getTagsFor(EntryStack<BlockState> entry, BlockState value) {
		return value.getBlock().builtInRegistryHolder().tags();
	}

	@Override
	public Codec<BlockState> codec() {
		return BlockState.CODEC;
	}

	@Override
	public StreamCodec<RegistryFriendlyByteBuf, BlockState> streamCodec() {
		StreamCodec<io.netty.buffer.ByteBuf, BlockState> codec = ByteBufCodecs.idMapper(Block.BLOCK_STATE_REGISTRY);
		return StreamCodec.of(
			(buf, state) -> codec.encode(buf, state),
			buf -> codec.decode(buf)
		);
	}

	public static class FlatBlockRenderer implements EntryRenderer<BlockState> {

		@Override
		public void render(EntryStack<BlockState> entry, GuiGraphics graphics, Rectangle bounds, int mouseX, int mouseY, float delta) {
			RecipeViewerConstants.renderFlatBlock(graphics.pose(), entry.getValue(), new Vec3(bounds.getX(), bounds.getY(), 201.0D), 20.0F);
		}

		@Override
		public @Nullable Tooltip getTooltip(EntryStack<BlockState> entry, TooltipContext context) {
			return Tooltip.create(new ItemStack(entry.getValue().getBlock()).getTooltipLines(context.vanillaContext(), null, context.getFlag()));
		}
	}
}
