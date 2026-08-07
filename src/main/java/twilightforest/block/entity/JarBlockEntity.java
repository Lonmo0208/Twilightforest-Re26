package twilightforest.block.entity;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Util;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jspecify.annotations.Nullable;
import twilightforest.TwilightForestMod;
import twilightforest.block.JarBlock;
import twilightforest.components.item.JarLid;
import twilightforest.init.TFBlockEntities;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFDataComponents;
import twilightforest.util.HolidayEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BooleanSupplier;

import static net.minecraft.world.level.block.entity.DecoratedPotBlockEntity.WobbleStyle;

public class JarBlockEntity extends BlockEntity {
	public static final Codec<Item> ITEM_CODEC = BuiltInRegistries.ITEM.byNameCodec();
	public static final Map<Item, BooleanSupplier> REGISTERED_LOG_LIDS = new HashMap<>();
	public static final String TAG_LID = "lid";
	public static final Identifier JAR_LID = TwilightForestMod.prefix("jar_lid");
	public static final int EVENT_POT_WOBBLES = 1;

	public static void addLid(Item item, BooleanSupplier supplier) {
		REGISTERED_LOG_LIDS.put(item, supplier);
	}

	public static void addLid(Item item) {
		addLid(item, () -> true);
	}

	private static boolean initialized = false;
	private static HolidayEvent holidayEventInstance;

	/**
	 * Ensure all jar lids are registered explicitly. Called from TwilightForestMod.onInitialize()
	 * so we are 100% sure REGISTERED_LOG_LIDS is populated, independent of bean context wiring.
	 */
	public static void ensureLidsRegistered(HolidayEvent holidayEvent) {
		if (initialized) return;
		initialized = true;
		holidayEventInstance = holidayEvent;

		// Twilight Forest log variants
		addLid(TFBlocks.MANGROVE_LOG.asItem());
		addLid(TFBlocks.CANOPY_LOG.asItem());
		addLid(TFBlocks.DARK_LOG.asItem());
		addLid(TFBlocks.MINING_LOG.asItem());
		addLid(TFBlocks.SORTING_LOG.asItem());
		addLid(TFBlocks.TIME_LOG.asItem());
		addLid(TFBlocks.TRANSFORMATION_LOG.asItem());
		addLid(TFBlocks.TWILIGHT_OAK_LOG.asItem());

		// Vanilla log variants
		addLid(net.minecraft.world.item.Items.ACACIA_LOG);
		addLid(net.minecraft.world.item.Items.BIRCH_LOG);
		addLid(net.minecraft.world.item.Items.CHERRY_LOG);
		addLid(net.minecraft.world.item.Items.DARK_OAK_LOG);
		addLid(net.minecraft.world.item.Items.JUNGLE_LOG);
		addLid(net.minecraft.world.item.Items.MANGROVE_LOG);
		addLid(net.minecraft.world.item.Items.OAK_LOG);
		addLid(net.minecraft.world.item.Items.SPRUCE_LOG);
		addLid(net.minecraft.world.item.Items.CRIMSON_STEM);
		addLid(net.minecraft.world.item.Items.WARPED_STEM);

		// Stripped TF logs
		addLid(TFBlocks.STRIPPED_MANGROVE_LOG.asItem());
		addLid(TFBlocks.STRIPPED_CANOPY_LOG.asItem());
		addLid(TFBlocks.STRIPPED_DARK_LOG.asItem());
		addLid(TFBlocks.STRIPPED_MINING_LOG.asItem());
		addLid(TFBlocks.STRIPPED_SORTING_LOG.asItem());
		addLid(TFBlocks.STRIPPED_TIME_LOG.asItem());
		addLid(TFBlocks.STRIPPED_TRANSFORMATION_LOG.asItem());
		addLid(TFBlocks.STRIPPED_TWILIGHT_OAK_LOG.asItem());

		// Stripped vanilla logs/stems
		addLid(net.minecraft.world.item.Items.STRIPPED_ACACIA_LOG);
		addLid(net.minecraft.world.item.Items.STRIPPED_BIRCH_LOG);
		addLid(net.minecraft.world.item.Items.STRIPPED_CHERRY_LOG);
		addLid(net.minecraft.world.item.Items.STRIPPED_DARK_OAK_LOG);
		addLid(net.minecraft.world.item.Items.STRIPPED_JUNGLE_LOG);
		addLid(net.minecraft.world.item.Items.STRIPPED_MANGROVE_LOG);
		addLid(net.minecraft.world.item.Items.STRIPPED_OAK_LOG);
		addLid(net.minecraft.world.item.Items.STRIPPED_SPRUCE_LOG);
		addLid(net.minecraft.world.item.Items.STRIPPED_CRIMSON_STEM);
		addLid(net.minecraft.world.item.Items.STRIPPED_WARPED_STEM);

		// Cinder log + bamboo variants
		addLid(TFBlocks.CINDER_LOG.asItem());
		addLid(net.minecraft.world.item.Items.BAMBOO_BLOCK);
		addLid(net.minecraft.world.item.Items.STRIPPED_BAMBOO_BLOCK);

		// Pumpkin (Halloween week only)
		addLid(net.minecraft.world.item.Items.PUMPKIN, () -> holidayEventInstance != null && holidayEventInstance.isHalloweenWeek());
	}

	public Item lid = TFBlocks.TWILIGHT_OAK_LOG.asItem();
	public long wobbleStartedAtTick;
	@Nullable
	public WobbleStyle lastWobbleStyle;

	public JarBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
		super(type, pos, blockState);
		if (blockState.getBlock() instanceof JarBlock jarBlock) this.lid = jarBlock.getDefaultLid();
	}

	public JarBlockEntity(BlockPos pos, BlockState state) {
		this(TFBlockEntities.JAR, pos, state);
	}

	@Override
	protected void saveAdditional(ValueOutput output) {
		super.saveAdditional(output);
		output.store(TAG_LID, ITEM_CODEC, this.lid);
	}

	@Override
	protected void loadAdditional(ValueInput input) {
		super.loadAdditional(input);
		this.lid = input.read(TAG_LID, ITEM_CODEC).orElse(TFBlocks.TWILIGHT_OAK_LOG.asItem());
	}

	public ItemStack getJarAsItem() {
		return Util.make(this.getBlockState().getBlock().asItem().getDefaultInstance(), jar -> jar.applyComponents(this.collectComponents()));
	}

	@Override
	public ClientboundBlockEntityDataPacket getUpdatePacket() {
		return ClientboundBlockEntityDataPacket.create(this);
	}

	@Override
	public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
		return this.saveCustomOnly(registries);
	}

	@Override
	protected void collectImplicitComponents(DataComponentMap.Builder builder) {
		super.collectImplicitComponents(builder);
		builder.set(TFDataComponents.JAR_LID, new JarLid(this.lid));
	}

	@Override
	protected void applyImplicitComponents(DataComponentGetter components) {
		super.applyImplicitComponents(components);
		this.lid = components.getOrDefault(TFDataComponents.JAR_LID, new JarLid(TFBlocks.TWILIGHT_OAK_LOG.asItem())).lid();
	}

	@Override
	public void removeComponentsFromTag(ValueOutput output) {
		super.removeComponentsFromTag(output);
		output.discard(TAG_LID);
	}

	public void wobble(WobbleStyle style) {
		if (this.level != null && !this.level.isClientSide()) {
			this.level.blockEvent(this.getBlockPos(), this.getBlockState().getBlock(), EVENT_POT_WOBBLES, style.ordinal());
		}
	}

	@Override
	public boolean triggerEvent(int id, int type) {
		if (this.level != null && id == EVENT_POT_WOBBLES && type >= 0 && type < WobbleStyle.values().length) {
			this.wobbleStartedAtTick = this.level.getGameTime();
			this.lastWobbleStyle = WobbleStyle.values()[type];
			return true;
		} else {
			return super.triggerEvent(id, type);
		}
	}
}
