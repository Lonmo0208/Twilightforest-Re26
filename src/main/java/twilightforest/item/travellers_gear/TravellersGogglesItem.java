package twilightforest.item.travellers_gear;

import net.minecraft.core.component.DataComponents;
import net.minecraft.network.protocol.Packet;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.saveddata.maps.MapDecoration;
import net.minecraft.world.level.saveddata.maps.MapDecorationTypes;
import net.minecraft.world.level.saveddata.maps.MapId;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import org.jspecify.annotations.Nullable;
import twilightforest.components.item.ItemDisplayContents;
import twilightforest.init.TFDataComponents;
import twilightforest.init.custom.TravellersModifiersManager;
import twilightforest.item.MagicMapItem;
import twilightforest.mixin.MapItemSavedDataMixin;

import java.util.Optional;

public class TravellersGogglesItem extends TravellersArmorItem {

	public TravellersGogglesItem(int insertableModifierSlots, Properties properties) {
		super(insertableModifierSlots, properties);
	}

	@Override
	public Optional<TooltipComponent> getTooltipImage(ItemStack stack) {
		TooltipDisplay display = stack.getOrDefault(DataComponents.TOOLTIP_DISPLAY, TooltipDisplay.DEFAULT);
		if (display.hideTooltip())
			return Optional.empty();
		ItemDisplayContents contents = stack.get(TFDataComponents.ITEM_DISPLAY);
		if (contents == null)
			return Optional.empty();
		boolean hasAnyItem = false;
		for (ItemStack s : contents.items()) {
			if (!s.isEmpty()) {
				hasAnyItem = true;
				break;
			}
		}
		return hasAnyItem ? Optional.of(new Tooltip(contents)) : Optional.empty();
	}

	@Override
	public boolean overrideStackedOnOther(ItemStack stack, Slot slot, ClickAction action, Player player) {
		if (stack.getCount() != 1 || action != ClickAction.SECONDARY)
			return false;

		ItemDisplayContents contents = stack.get(TFDataComponents.ITEM_DISPLAY);
		if (contents == null)
			return false;

		ItemDisplayContents.Mutable mutableContents = new ItemDisplayContents.Mutable(contents);
		ItemStack itemstack = slot.getItem();
		if (itemstack.isEmpty()) {
			ItemStack removedStack = mutableContents.removeFirstFree(slot);
			if (removedStack != null) {
				slot.safeInsert(removedStack);
				this.playRemoveOneSound(player);
			}
		} else if (canFitInsideContainerItems(itemstack)) {
			if (mutableContents.trySwap(SlotAccess.of(slot::getItem, slot::set), player))
				this.playInsertSound(player);
		}

		stack.set(TFDataComponents.ITEM_DISPLAY, mutableContents.toImmutable());
		return true;
	}

	@Override
	public boolean overrideOtherStackedOnMe(ItemStack stack, ItemStack other, Slot slot, ClickAction action, Player player, SlotAccess access) {
		if (stack.getCount() != 1 || action != ClickAction.SECONDARY || !slot.allowModification(player))
			return false;

		ItemDisplayContents contents = stack.get(TFDataComponents.ITEM_DISPLAY);
		if (contents == null)
			return false;

		ItemDisplayContents.Mutable mutableContents = new ItemDisplayContents.Mutable(contents);
		if (other.isEmpty()) {
			ItemStack itemstack = mutableContents.removeFirstFree(null);
			if (itemstack != null) {
				this.playRemoveOneSound(player);
				access.set(itemstack);
			}
		} else {
			if (mutableContents.trySwap(access, player))
				this.playInsertSound(player);
		}

		stack.set(TFDataComponents.ITEM_DISPLAY, mutableContents.toImmutable());
		return true;
	}

	@Override
	public void inventoryTick(ItemStack stack, ServerLevel level, Entity owner, @Nullable EquipmentSlot slot) {
		if (slot != EquipmentSlot.HEAD)
			return;
		if (level.isClientSide() || !TravellersModifiersManager.isModifierActive(owner, TravellersModifiersManager.ITEM_DISPLAY_MODIFIER))
			return;

		ItemDisplayContents contents = stack.get(TFDataComponents.ITEM_DISPLAY);
		if (contents == null || contents.isEmpty())
			return;

		int mapSlot = contents.findActiveMapSlot();
		if (mapSlot == -1)
			return;

		ItemStack map = contents.items().get(mapSlot);
		if (map.isEmpty())
			return;

		if (!(map.getItem() instanceof MapItem mapItem))
			return;

		ServerPlayer player = (ServerPlayer) owner;

		MapItemSavedData data;
		if (mapItem instanceof MagicMapItem magicMapItem) {
			data = magicMapItem.getCustomMapData(map, level);
		} else {
			data = MapItem.getSavedData(map, level);
		}

		if (data == null)
			return;

		MapItemSavedDataMixin dataAccessor = (MapItemSavedDataMixin) data;

		data.getHoldingPlayer(player);

		if (!data.locked && dataAccessor.getDimension() == level.dimension() && dataAccessor.isTrackingPosition()) {
			String playerName = player.getPlainTextName();
			int scale = 1 << dataAccessor.getScale();
			float xDeltaFromCenter = (float) (player.getX() - dataAccessor.getCenterX()) / scale;
			float yDeltaFromCenter = (float) (player.getZ() - dataAccessor.getCenterZ()) / scale;
			boolean insideMap = xDeltaFromCenter >= -63.0F && yDeltaFromCenter >= -63.0F && xDeltaFromCenter <= 63.0F && yDeltaFromCenter <= 63.0F;

			if (insideMap || dataAccessor.isUnlimitedTracking()) {
				byte rotation = calculateRotation(player.getYRot());
				byte clampedX = clampMapCoordinate(xDeltaFromCenter);
				byte clampedY = clampMapCoordinate(yDeltaFromCenter);

				MapDecoration decoration = new MapDecoration(
					MapDecorationTypes.PLAYER, clampedX, clampedY, rotation, Optional.empty()
				);
				MapDecoration existingDecoration = dataAccessor.getDecorationsField().get(playerName);
				if (existingDecoration == null || !existingDecoration.equals(decoration)) {
					dataAccessor.getDecorationsField().put(playerName, decoration);
					if (decoration.type().value().trackCount()) {
						dataAccessor.setTrackedDecorationCount(dataAccessor.getTrackedDecorationCount() + 1);
					}
					if (existingDecoration != null && existingDecoration.type().value().trackCount()) {
						dataAccessor.setTrackedDecorationCount(dataAccessor.getTrackedDecorationCount() - 1);
					}
					dataAccessor.tf$setDecorationsDirty();
				}
			}
		}

		if (!data.locked) {
			if (mapItem instanceof MagicMapItem magicMapItem) {
				magicMapItem.update(level, owner, data);
			}
		}

		MapId mapId = map.get(DataComponents.MAP_ID);
		if (mapId != null) {
			Packet<?> packet = data.getUpdatePacket(mapId, player);
			if (packet != null) {
				// Always send the returned packet (updates map texture on client)
				// For magic maps, getUpdatePacket also sent MagicMapPacket internally
				player.connection.send(packet);
			}
		}
	}

	private static byte clampMapCoordinate(float deltaFromCenter) {
		if (deltaFromCenter <= -63.0F) {
			return -128;
		} else {
			return deltaFromCenter >= 63.0F ? 127 : (byte) (deltaFromCenter * 2.0F + 0.5);
		}
	}

	// This matches the vanilla MapItemSavedData.calculateRotation method
	private static byte calculateRotation(double yRot) {
		double adjustedYRot = yRot < 0.0 ? yRot - 8.0 : yRot + 8.0;
		return (byte) (adjustedYRot * 16.0 / 360.0);
	}

	public boolean isGazeDisguise(ItemStack stack, Player player, @Nullable LivingEntity entity) {
		return entity instanceof EnderMan && TravellersModifiersManager.isModifierActive(player, TravellersModifiersManager.ALL_NIGHT_GOGGLES_MODIFIER);
	}

	private void playRemoveOneSound(Entity entity) {
		entity.playSound(SoundEvents.BUNDLE_REMOVE_ONE, 0.8F, 0.8F + entity.level().getRandom().nextFloat() * 0.4F);
	}

	private void playInsertSound(Entity entity) {
		entity.playSound(SoundEvents.BUNDLE_INSERT, 0.8F, 0.8F + entity.level().getRandom().nextFloat() * 0.4F);
	}

	public record Tooltip(ItemDisplayContents contents) implements TooltipComponent {}
}
