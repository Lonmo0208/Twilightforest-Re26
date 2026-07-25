package twilightforest.compat.jei.renderers;

import mezz.jei.api.gui.builder.ITooltipBuilder;
import mezz.jei.api.ingredients.IIngredientRenderer;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import twilightforest.TwilightForestMod;
import twilightforest.compat.jei.FakeLivingBlock;
import twilightforest.util.entities.EntityRenderingUtil;

import java.util.List;
import java.util.Objects;

public class FakeLivingBlockRenderer implements IIngredientRenderer<FakeLivingBlock> {

	private final float bobOffs;
	private final int size;

	public FakeLivingBlockRenderer(int size) {
		this.bobOffs = RandomSource.create().nextFloat() * (float) Math.PI * 2.0F;
		this.size = size;
	}

	@Override
	public int getWidth() {
		return this.size;
	}

	@Override
	public int getHeight() {
		return this.size;
	}

	@Override
	public void render(GuiGraphicsExtractor graphics, @Nullable FakeLivingBlock item) {
		Level level = Minecraft.getInstance().level;
		if (item != null && level != null) {
			try {
				EntityRenderingUtil.renderLivingBlock(graphics, item.stack(), level, this.bobOffs);
			} catch (Exception e) {
				TwilightForestMod.LOGGER.error("Error drawing item in JEI!", e);
			}
		}
	}

	@SuppressWarnings("removal") //we are absolutely forced to use this
	@Override
	public List<Component> getTooltip(FakeLivingBlock item, TooltipFlag flag) {
		return List.of();
	}

	@Override
	public void getTooltip(ITooltipBuilder tooltip, FakeLivingBlock item, TooltipFlag flag) {
		tooltip.add(item.stack().getHoverName());
		if (flag.isAdvanced()) {
			tooltip.add(Component.literal(Objects.requireNonNull(BuiltInRegistries.ITEM.getKey(item.stack().getItem())).toString()).withStyle(ChatFormatting.DARK_GRAY));
		}
	}
}