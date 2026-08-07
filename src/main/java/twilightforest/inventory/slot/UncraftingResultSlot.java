package twilightforest.inventory.slot;

import net.minecraft.core.NonNullList;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.ResultSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import twilightforest.inventory.InventoryUtil;
import twilightforest.inventory.UncraftingContainer;
import twilightforest.inventory.UncraftingMenu;

import java.util.HashMap;
import java.util.Map;

public class UncraftingResultSlot extends ResultSlot {

	private final Player player;
	private final Container inputSlot;
	private final UncraftingContainer uncraftingMatrix;
	private final CraftingContainer assemblyMatrix;
	private final Map<Integer, ItemStack> tempRemainderMap;

	public UncraftingResultSlot(Player player, Container input, Container uncraftingMatrix, Container assemblyMatrix, Container result, int slotIndex, int x, int y) {
		super(player, (CraftingContainer) assemblyMatrix, result, slotIndex, x, y);
		this.player = player;
		this.inputSlot = input;
		this.uncraftingMatrix = (UncraftingContainer) uncraftingMatrix;
		this.assemblyMatrix = (CraftingContainer) assemblyMatrix;
		this.tempRemainderMap = new HashMap<>();
	}

	@Override
	public void onTake(Player player, ItemStack stack) {
		//clear the temp map, just in case
		this.tempRemainderMap.clear();

		boolean combined;

		// If the input slot has an item, this is always a TF uncrafting-table specific operation
		// (enchantment transfer / recrafting), regardless of whether assembly matches a recipe.
		// In this case combined=true so XP is charged and input item is consumed.
		if (!this.inputSlot.getItem(0).isEmpty()) {
			combined = true;
		} else {
			// Input is empty - assembly matrix used as a standalone crafting grid.
			// Check if assembly alone can produce the item (regular crafting, no XP cost).
			combined = true;
			if (player.level() instanceof ServerLevel serverLevel) {
				RecipeManager recipeManager = serverLevel.recipeAccess();
				for (RecipeHolder<?> recipe : recipeManager.getRecipes()) {
					if (recipe.value() instanceof CraftingRecipe craftingRecipe && craftingRecipe.matches(this.assemblyMatrix.asCraftInput(), player.level())) {
						if (ItemStack.isSameItemSameComponents(craftingRecipe.assemble(this.assemblyMatrix.asCraftInput()), stack)) {
							combined = false;
							break;
						}
					}
				}
			}
		}
		
		if (net.minecraft.SharedConstants.IS_RUNNING_IN_IDE) {
			twilightforest.TwilightForestMod.LOGGER.info("[UncraftingResultSlot] onTake: combined={}, recraftingCost={}, inputSlot={}", 
				combined, this.uncraftingMatrix.recraftingCost, !this.inputSlot.getItem(0).isEmpty());
		}

		if (combined) {
			// charge the player before the stacks empty
			if (this.uncraftingMatrix.recraftingCost > 0) {
				if (net.minecraft.SharedConstants.IS_RUNNING_IN_IDE) {
					twilightforest.TwilightForestMod.LOGGER.info("[UncraftingResultSlot] Charging {} XP for recrafting", this.uncraftingMatrix.recraftingCost);
				}
				this.player.giveExperienceLevels(-this.uncraftingMatrix.recraftingCost);
			} else if (net.minecraft.SharedConstants.IS_RUNNING_IN_IDE) {
				twilightforest.TwilightForestMod.LOGGER.info("[UncraftingResultSlot] recraftingCost is 0, no XP charged");
			}

			// if we are using a combined recipe, wipe the uncrafting matrix and decrement the input appropriately
			for (int i = 0; i < this.uncraftingMatrix.getContainerSize(); i++) {
				if (this.assemblyMatrix.getItem(i).isEmpty()) {
					if (!UncraftingMenu.isMarked(this.uncraftingMatrix.getItem(i))) {
						this.uncraftingMatrix.setItem(i, ItemStack.EMPTY);
					} else {
						//if we have an ingredient in the grid and one in the uncrafting matrix, copy the uncrafting matrix item for later
						this.tempRemainderMap.put(i, this.uncraftingMatrix.getItem(i));
					}
				}
			}
			this.inputSlot.removeItem(0, this.uncraftingMatrix.numberOfInputItems);
		}

		//VanillaCopy of the super method, but altered to work with the assembly matrix
		this.checkTakeAchievements(stack);

		CraftingInput.Positioned positioned = this.assemblyMatrix.asPositionedCraftInput();
		CraftingInput input = positioned.input();
		int i = positioned.left();
		int j = positioned.top();
		NonNullList<ItemStack> remainingItems = getRemainingItems(input, player.level());

		for (int k = 0; k < input.height(); k++) {
			for (int l = 0; l < input.width(); l++) {
				int index = l + i + (k + j) * this.assemblyMatrix.getWidth();
				ItemStack currentStack = this.assemblyMatrix.getItem(index);
				ItemStack remainingStack = remainingItems.get(l + k * input.width());
				if (!currentStack.isEmpty()) {
					this.assemblyMatrix.removeItem(index, 1);
					currentStack = this.assemblyMatrix.getItem(index);
				}

				if (!remainingStack.isEmpty()) {
					if (currentStack.isEmpty()) {
						this.assemblyMatrix.setItem(index, remainingStack);
					} else if (!ItemStack.isSameItemSameComponents(currentStack, remainingStack)) {
						InventoryUtil.giveItemToPlayer(this.player, remainingStack);
					}
				}
			}
		}
		//add all remainders to the crafting grid. This prevents any extra items from being deleted during the recrafting process.
		if (!this.tempRemainderMap.isEmpty()) {
			this.tempRemainderMap.forEach(this.assemblyMatrix::setItem);
		}
	}

	private static NonNullList<ItemStack> getRemainingItems(CraftingInput input, Level level) {
		if (level instanceof ServerLevel serverLevel) {
			return serverLevel.recipeAccess()
				.getRecipeFor(RecipeType.CRAFTING, input, serverLevel)
				.map(recipe -> recipe.value().getRemainingItems(input))
				.orElseGet(() -> copyAllInputItems(input));
		}
		return CraftingRecipe.defaultCraftingReminder(input);
	}

	private static NonNullList<ItemStack> copyAllInputItems(CraftingInput input) {
		NonNullList<ItemStack> result = NonNullList.withSize(input.size(), ItemStack.EMPTY);
		for (int slot = 0; slot < result.size(); slot++) {
			result.set(slot, input.getItem(slot));
		}
		return result;
	}
}
