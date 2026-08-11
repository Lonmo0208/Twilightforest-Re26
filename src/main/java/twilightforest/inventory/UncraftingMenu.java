package twilightforest.inventory;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import java.util.Optional;
import net.minecraft.core.Holder;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.recipebook.ServerPlaceRecipe;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.StackedItemContents;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.item.equipment.Equippable;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import twilightforest.TwilightForestMod;
import twilightforest.config.TFConfig;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFMenuTypes;
import twilightforest.init.TFRecipes;
import twilightforest.inventory.slot.AssemblySlot;
import twilightforest.inventory.slot.UncraftingResultSlot;
import twilightforest.inventory.slot.UncraftingSlot;
import twilightforest.item.recipe.UncraftingRecipe;
import twilightforest.tags.TFItemTags;
import twilightforest.util.TFItemStackUtils;

import java.util.*;
import java.util.stream.Collectors;

public class UncraftingMenu extends RecipeBookMenu {

	private static final String TAG_MARKER = "TwilightForestMarker";

	// Inaccessible grid, for uncrafting logic
	private final UncraftingContainer uncraftingMatrix = new UncraftingContainer(this);
	// Accessible grid, for actual crafting
	public final CraftingContainer assemblyMatrix = new TransientCraftingContainer(this, 3, 3);
	// Inaccessible grid, for recrafting logic
	private final CraftingContainer combineMatrix = new TransientCraftingContainer(this, 3, 3);

	// Input slot for uncrafting
	public final Container tinkerInput = new UncraftingInputContainer(this);
	// Crafting Output
	private final ResultContainer tinkerResult = new ResultContainer();

	private final ContainerLevelAccess positionData;
	private final Level level;
	private final Player player;

	// Conflict resolution
	public int unrecipeInCycle = 0;
	public int ingredientsInCycle = 0;
	public int recipeInCycle = 0;

	// Store the currently selected recipe for use later down the line.
	// Currently used for determining if the recipe is an uncrafting one and for determining custom costs
	@Nullable
	public Recipe<?> storedGhostRecipe = null;

	// Prevent infinite recursion when updating combineMatrix
	private boolean isUpdatingCombineMatrix = false;

	public static UncraftingMenu fromNetwork(int id, Inventory inventory) {
		return new UncraftingMenu(id, inventory, inventory.player.level(), ContainerLevelAccess.NULL);
	}

	public UncraftingMenu(int id, Inventory inventory, Level level, ContainerLevelAccess positionData) {
		super(TFMenuTypes.UNCRAFTING, id);

		this.positionData = positionData;
		this.level = level;
		this.player = inventory.player;

		this.addSlot(new Slot(this.tinkerInput, 0, 13, 35));
		this.addSlot(new UncraftingResultSlot(inventory.player, this.tinkerInput, this.uncraftingMatrix, this.assemblyMatrix, this.tinkerResult, 0, 147, 35));

		int invX;
		int invY;

		for (invX = 0; invX < 3; ++invX) {
			for (invY = 0; invY < 3; ++invY) {
				this.addSlot(new UncraftingSlot(inventory.player, this.tinkerInput, this.uncraftingMatrix, this.assemblyMatrix, invY + invX * 3, 300000 + invY * 18, 17 + invX * 18));
			}
		}
		for (invX = 0; invX < 3; ++invX) {
			for (invY = 0; invY < 3; ++invY) {
				this.addSlot(new AssemblySlot(this.assemblyMatrix, invY + invX * 3, 62 + invY * 18, 17 + invX * 18));
			}
		}

		for (invX = 0; invX < 3; ++invX) {
			for (invY = 0; invY < 9; ++invY) {
				this.addSlot(new Slot(inventory, invY + invX * 9 + 9, 8 + invY * 18, 84 + invX * 18));
			}
		}

		for (invX = 0; invX < 9; ++invX) {
			this.addSlot(new Slot(inventory, invX, 8 + invX * 18, 142));
		}

		this.slotsChanged(this.assemblyMatrix);

		// Sync initial costs to client (server-side only)
		if (!this.level.isClientSide() && this.player instanceof net.minecraft.server.level.ServerPlayer serverPlayer) {
			twilightforest.network.SyncUncraftingCostsPacket.send(serverPlayer, this.uncraftingMatrix.uncraftingCost, this.uncraftingMatrix.recraftingCost);
		}

		if (net.minecraft.SharedConstants.IS_RUNNING_IN_IDE) {
			// Debug slot listing
			NonNullList<Slot> slots = this.slots;

			StringJoiner joiner = new StringJoiner(",\n", "Uncrafting Menu Slots:\n", "(" + slots.size() + " total slots)");

			for (Slot slot : this.slots) {
				joiner.add("[index " + slot.index + ": " + slot.getClass().getName() + " (container slot: " + slot.getContainerSlot() + ")]");
			}

			TwilightForestMod.LOGGER.info(joiner.toString());
		}
	}

	@Override
	public void slotsChanged(Container inventory) {
		// Prevent infinite recursion when updating combineMatrix
		if (this.isUpdatingCombineMatrix) {
			return;
		}

		if (inventory == this.tinkerInput) {
			if (!this.level.isClientSide()) {
				// Empty whole grid to start with
				this.uncraftingMatrix.clearContent();

				// See if there is a recipe for the input
				ItemStack inputStack = tinkerInput.getItem(0);
				Recipe<?>[] recipes = getRecipesFor(inputStack, this.level);

				int size = recipes.length;

				if (size > 0 && !inputStack.is(TFItemTags.BANNED_UNCRAFTABLES)) {
					Recipe<?> recipe = recipes[Math.floorMod(this.unrecipeInCycle, size)];
					this.storedGhostRecipe = recipe;
					ItemStack[] recipeItems = this.getIngredients(recipe);

					if (recipe instanceof ShapedRecipe rec) {
						int recipeWidth = rec.getWidth();
						int recipeHeight = rec.getHeight();

						// Set uncrafting grid
						for (int invY = 0; invY < recipeHeight; invY++) {
							for (int invX = 0; invX < recipeWidth; invX++) {
								int index = invX + invY * recipeWidth;
								if (index >= recipeItems.length) continue;

								ItemStack ingredient = normalizeIngredient(recipeItems[index].copy());
								this.uncraftingMatrix.setItem(invX + invY * 3, ingredient);
							}
						}
					} else {
						for (int i = 0; i < this.uncraftingMatrix.getContainerSize(); i++) {
							if (i < recipeItems.length) {
								ItemStack ingredient = normalizeIngredient(recipeItems[i].copy());
								this.uncraftingMatrix.setItem(i, ingredient);
							}
						}
					}

					// Mark the appropriate number of damaged components
					if (inputStack.isDamaged()) {
						int damagedParts = this.countDamagedParts(inputStack);

						for (int i = 0; i < 9 && damagedParts > 0; i++) {
							ItemStack stack = this.uncraftingMatrix.getItem(i);
							if (isDamageableComponent(stack)) {
								markStack(stack);
								damagedParts--;
							}
						}
					}

					// Mark banned items
					for (int i = 0; i < 9; i++) {
						ItemStack ingredient = this.uncraftingMatrix.getItem(i);
						if (isIngredientProblematic(ingredient)) {
							markStack(ingredient);
						}
					}

					// Store number of items this recipe produces
					this.uncraftingMatrix.numberOfInputItems = recipe instanceof UncraftingRecipe uncraftingRecipe ? uncraftingRecipe.getCount() : ((CraftingRecipe)recipe).assemble(CraftingInput.EMPTY).getCount();

					// Only set uncraftingCost if assemblyMatrix is empty (pure uncrafting mode)
					if (this.assemblyMatrix.isEmpty()) {
						this.uncraftingMatrix.uncraftingCost = this.calculateUncraftingCost();
					}
					// Do NOT reset recraftingCost here - it will be recalculated in Phase 2 if needed

					if (net.minecraft.SharedConstants.IS_RUNNING_IN_IDE) {
						TwilightForestMod.LOGGER.info("[UncraftingMenu] tinkerInput changed: uncraftingCost={}, recraftingCost={}, recipeType={}, assemblyEmpty={}",
							this.uncraftingMatrix.uncraftingCost, this.uncraftingMatrix.recraftingCost, recipe.getType(), this.assemblyMatrix.isEmpty());
					}
				} else {
					// No valid recipe found or item is banned
					this.storedGhostRecipe = null;
					this.uncraftingMatrix.numberOfInputItems = 0;
					
					// Always clear the result and costs when tinkerInput becomes empty
					// This prevents enchanted items from appearing after the input is removed
					this.tinkerResult.setItem(0, ItemStack.EMPTY);
					this.uncraftingMatrix.uncraftingCost = 0;
					this.uncraftingMatrix.recraftingCost = 0;
					
					// If assemblyMatrix still has items, recalculate as pure crafting mode
					if (!this.assemblyMatrix.isEmpty()) {
						this.chooseRecipe(this.assemblyMatrix.asCraftInput());
					}
				}
			} else {
				// Client-side: clear recipe-dependent state
				this.storedGhostRecipe = null;
				this.uncraftingMatrix.numberOfInputItems = 0;
			}
		}

		// Phase 2: Recrafting logic - triggered when BOTH matrices have items
		// The key fix: recrafting must work regardless of WHICH container changed first
		boolean bothMatricesHaveItems = !this.tinkerInput.isEmpty() && !this.assemblyMatrix.isEmpty();
		
		if (bothMatricesHaveItems) {
			if (!this.level.isClientSide()) {
				// Recrafting mode: clear previous result
				this.tinkerResult.setItem(0, ItemStack.EMPTY);
				this.uncraftingMatrix.recraftingCost = 0;

				// Both matrices have items - merge them and try recrafting
				if (!this.uncraftingMatrix.isEmpty()) {
					// Set flag to prevent infinite recursion
					this.isUpdatingCombineMatrix = true;
					try {
						// Merge assemblyMatrix and uncraftingMatrix into combineMatrix
						for (int i = 0; i < 9; i++) {
							ItemStack assembly = this.assemblyMatrix.getItem(i);
							ItemStack uncrafting = this.uncraftingMatrix.getItem(i);

							if (!assembly.isEmpty()) {
								this.combineMatrix.setItem(i, assembly);
							} else if (!uncrafting.isEmpty() && !isMarked(uncrafting)) {
								this.combineMatrix.setItem(i, uncrafting);
							} else {
								this.combineMatrix.setItem(i, ItemStack.EMPTY);
							}
						}
					} finally {
						this.isUpdatingCombineMatrix = false;
					}

					// Use combined matrix to find the crafting recipe
					this.chooseRecipe(this.combineMatrix.asCraftInput());

					ItemStack input = this.tinkerInput.getItem(0);
					ItemStack result = this.tinkerResult.getItem(0);

					if (net.minecraft.SharedConstants.IS_RUNNING_IN_IDE) {
						TwilightForestMod.LOGGER.info("[UncraftingMenu] recrafting check: input={}, result={}, isEmpty={}, triggeredBy={}",
							input.getItem(), result.getItem(), result.isEmpty(), inventory == this.tinkerInput ? "tinkerInput" : "assemblyMatrix");
					}

					if (!result.isEmpty() && isValidMatchForInput(input, result)) {
						// Transfer enchantments from input to result
						if (result.isEnchantable()) {
							// Store copy of input enchants
							ItemEnchantments.Mutable enchants = new ItemEnchantments.Mutable(input.getOrDefault(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY));
							// Add all resulting item enchants to the list (preserves pre-existing enchants on new item)
							if (result.has(DataComponents.ENCHANTMENTS)) {
								Objects.requireNonNull(result.get(DataComponents.ENCHANTMENTS)).entrySet().forEach(enchantment -> enchants.set(enchantment.getKey(), enchantment.getIntValue()));
							}
							// Remove any incompatible enchants
							enchants.removeIf(holder -> !holder.value().isSupportedItem(result));

							// Remove enchantments and replace with filtered list
							result.remove(DataComponents.ENCHANTMENTS);
							EnchantmentHelper.setEnchantments(result, enchants.toImmutable());
						}

						this.tinkerResult.setItem(0, result);
						this.uncraftingMatrix.uncraftingCost = 0;
						this.uncraftingMatrix.recraftingCost = this.calculateRecraftingCost();

						if (net.minecraft.SharedConstants.IS_RUNNING_IN_IDE) {
							TwilightForestMod.LOGGER.info("[UncraftingMenu] recrafting success: recraftingCost={}, inputEnchants={}, outputEnchants={}", 
								this.uncraftingMatrix.recraftingCost, 
								input.getEnchantments().size(), 
								result.getEnchantments().size());
						}
					} else if (net.minecraft.SharedConstants.IS_RUNNING_IN_IDE) {
						if (result.isEmpty()) {
							TwilightForestMod.LOGGER.info("[UncraftingMenu] recrafting failed: no matching recipe found for combined matrix");
						} else {
							TwilightForestMod.LOGGER.info("[UncraftingMenu] recrafting failed: isValidMatchForInput returned false for input={}, result={}",
								input.getItem(), result.getItem());
						}
						// Recrafting failed, show uncrafting cost instead
						this.uncraftingMatrix.uncraftingCost = this.calculateUncraftingCost();
					}
				}
			} else {
				// Client-side: calculate costs locally for instant UI feedback
				ItemStack input = this.tinkerInput.getItem(0);
				ItemStack result = this.tinkerResult.getItem(0);

				if (!input.isEmpty() && !this.assemblyMatrix.isEmpty()) {
					if (!result.isEmpty()) {
						this.uncraftingMatrix.recraftingCost = this.calculateRecraftingCost();
						this.uncraftingMatrix.uncraftingCost = 0;
					}
				}
			}
		}

		// Phase 3: Handle pure crafting (assemblyMatrix changes when tinkerInput is empty)
		if (inventory == this.assemblyMatrix && this.tinkerInput.isEmpty()) {
			if (!this.level.isClientSide()) {
				// Pure crafting mode: display the output
				this.chooseRecipe(this.assemblyMatrix.asCraftInput());
				this.uncraftingMatrix.uncraftingCost = 0;
				this.uncraftingMatrix.recraftingCost = 0;
			}
		}

		// Phase 4: Handle pure uncrafting (tinkerInput changes when assemblyMatrix is empty)
		if (inventory == this.tinkerInput && this.assemblyMatrix.isEmpty() && !this.tinkerInput.isEmpty()) {
			if (!this.level.isClientSide()) {
				this.uncraftingMatrix.uncraftingCost = this.calculateUncraftingCost();
				this.uncraftingMatrix.recraftingCost = 0;
			}
		}

		// Old code removed - see new Phase 2-4 logic above
		if (false) {
		if (inventory == this.assemblyMatrix) {
			if (!this.level.isClientSide()) {
				if (this.tinkerInput.isEmpty()) {
					// Pure crafting mode: display the output
					this.chooseRecipe(this.assemblyMatrix.asCraftInput());
					this.uncraftingMatrix.uncraftingCost = 0;
					this.uncraftingMatrix.recraftingCost = 0;
				} else {
					// Recrafting mode: clear previous result
					this.tinkerResult.setItem(0, ItemStack.EMPTY);
					this.uncraftingMatrix.recraftingCost = 0;

					// Both matrices have items - merge them and try recrafting
					if (!this.uncraftingMatrix.isEmpty() && !this.assemblyMatrix.isEmpty()) {
						// Merge assemblyMatrix and uncraftingMatrix into combineMatrix
						// This combines new materials (assemblyMatrix) with old equipment materials (uncraftingMatrix)
						for (int i = 0; i < 9; i++) {
							ItemStack assembly = this.assemblyMatrix.getItem(i);
							ItemStack uncrafting = this.uncraftingMatrix.getItem(i);

							if (!assembly.isEmpty()) {
								this.combineMatrix.setItem(i, assembly);
							} else if (!uncrafting.isEmpty() && !isMarked(uncrafting)) {
								this.combineMatrix.setItem(i, uncrafting);
							} else {
								this.combineMatrix.setItem(i, ItemStack.EMPTY);
							}
						}

						// Use combined matrix to find the crafting recipe
						this.chooseRecipe(this.combineMatrix.asCraftInput());

						ItemStack input = this.tinkerInput.getItem(0);
						ItemStack result = this.tinkerResult.getItem(0);

						if (net.minecraft.SharedConstants.IS_RUNNING_IN_IDE) {
							TwilightForestMod.LOGGER.info("[UncraftingMenu] recrafting check: input={}, result={}, isEmpty={}",
								input.getItem(), result.getItem(), result.isEmpty());
						}

						if (!result.isEmpty() && isValidMatchForInput(input, result)) {
							// Transfer enchantments from input to result
							if (result.isEnchantable()) {
								// Store copy of input enchants
								ItemEnchantments.Mutable enchants = new ItemEnchantments.Mutable(input.getOrDefault(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY));
								// Add all resulting item enchants to the list (preserves pre-existing enchants on new item)
								if (result.has(DataComponents.ENCHANTMENTS)) {
									Objects.requireNonNull(result.get(DataComponents.ENCHANTMENTS)).entrySet().forEach(enchantment -> enchants.set(enchantment.getKey(), enchantment.getIntValue()));
								}
								// Remove any incompatible enchants
								enchants.removeIf(holder -> !holder.value().isSupportedItem(result));

								// Remove enchantments and replace with filtered list
								result.remove(DataComponents.ENCHANTMENTS);
								EnchantmentHelper.setEnchantments(result, enchants.toImmutable());
							}

							this.tinkerResult.setItem(0, result);
							this.uncraftingMatrix.uncraftingCost = 0;
							this.uncraftingMatrix.recraftingCost = this.calculateRecraftingCost();

							if (net.minecraft.SharedConstants.IS_RUNNING_IN_IDE) {
								TwilightForestMod.LOGGER.info("[UncraftingMenu] recrafting success: recraftingCost={}, inputEnchants={}, outputEnchants={}",
									this.uncraftingMatrix.recraftingCost,
									input.getEnchantments().size(),
									result.getEnchantments().size());
							}
						} else if (net.minecraft.SharedConstants.IS_RUNNING_IN_IDE) {
							if (result.isEmpty()) {
								TwilightForestMod.LOGGER.info("[UncraftingMenu] recrafting failed: no matching recipe found for combined matrix");
							} else {
								TwilightForestMod.LOGGER.info("[UncraftingMenu] recrafting failed: isValidMatchForInput returned false for input={}, result={}",
									input.getItem(), result.getItem());
							}
							// Recrafting failed, show uncrafting cost instead
							this.uncraftingMatrix.uncraftingCost = this.calculateUncraftingCost();
						}
					} else if (this.assemblyMatrix.isEmpty()) {
						// Only uncraftingMatrix has items (pure uncrafting mode)
						this.uncraftingMatrix.uncraftingCost = this.calculateUncraftingCost();
						this.uncraftingMatrix.recraftingCost = 0;
					}
				}
			} else {
				// Client-side: calculate costs locally for instant UI feedback
				ItemStack input = this.tinkerInput.getItem(0);
				ItemStack result = this.tinkerResult.getItem(0);

				if (!input.isEmpty() && !this.assemblyMatrix.isEmpty()) {
					// Attempt local calculation
					if (!result.isEmpty()) {
						this.uncraftingMatrix.recraftingCost = this.calculateRecraftingCost();
						this.uncraftingMatrix.uncraftingCost = 0;
					}
				} else if (input.isEmpty()) {
					// Pure crafting mode
					this.uncraftingMatrix.uncraftingCost = 0;
					this.uncraftingMatrix.recraftingCost = 0;
				}
			}
		}
		} // Close if(false) block for old code

		if (!this.level.isClientSide() && this.player instanceof net.minecraft.server.level.ServerPlayer serverPlayer) {
			if (net.minecraft.SharedConstants.IS_RUNNING_IN_IDE) {
				TwilightForestMod.LOGGER.info("[UncraftingMenu] Syncing costs to client: uncraftingCost={}, recraftingCost={}",
					this.uncraftingMatrix.uncraftingCost, this.uncraftingMatrix.recraftingCost);
			}
			twilightforest.network.SyncUncraftingCostsPacket.send(serverPlayer, this.uncraftingMatrix.uncraftingCost, this.uncraftingMatrix.recraftingCost);
		}
	}

	private static int countNonEmpty(Container container) {
		int count = 0;
		for (int i = 0; i < container.getContainerSize(); i++) {
			if (!container.getItem(i).isEmpty()) {
				count++;
			}
		}
		return count;
	}

	public static void markStack(ItemStack stack) {
		TFItemStackUtils.addInfoTag(stack, TAG_MARKER);
	}

	public static boolean isMarked(ItemStack stack) {
		return TFItemStackUtils.hasInfoTag(stack, TAG_MARKER);
	}

	//might be handy one day
	@SuppressWarnings("unused")
	public static void unmarkStack(ItemStack stack) {
		TFItemStackUtils.clearInfoTag(stack, TAG_MARKER);
	}

	public static boolean isIngredientProblematic(ItemStack ingredient) {
		return (!ingredient.isEmpty() && ingredient.getItem().getCraftingRemainder() != null) || ingredient.is(Items.BARRIER);
	}

	private static ItemStack normalizeIngredient(ItemStack ingredient) {
		if (ingredient.getCount() > 1) {
			ingredient.setCount(1);
		}
		return ingredient;
	}

	private static Recipe<?>[] getRecipesFor(ItemStack inputStack, Level world) {

		List<Recipe<?>> recipes = new ArrayList<>();

		if (!inputStack.isEmpty()) {
			if (world instanceof ServerLevel serverLevel) {
				RecipeManager recipeManager = serverLevel.recipeAccess();

				// First pass: find UncraftingRecipe matches (using isItemStackAnIngredient)
				for (RecipeHolder<?> recipe : recipeManager.getRecipes()) {
					if (recipe.value() instanceof UncraftingRecipe uncraftingRecipe
							&& recipe.value().getType() == TFRecipes.UNCRAFTING_RECIPE
							&& uncraftingRecipe.isItemStackAnIngredient(inputStack)) {
						if (TFConfig.reverseRecipeBlacklist == TFConfig.disableUncraftingRecipes.contains(recipe.id().toString())) {
							if (TFConfig.flipUncraftingModIdList == TFConfig.blacklistedUncraftingModIds.contains(recipe.id().identifier().getNamespace())) {
								recipes.add(uncraftingRecipe);
							}
						}
					}
				}

				// Second pass: find regular crafting recipe matches
				for (RecipeHolder<?> recipe : recipeManager.getRecipes()) {
					if (!(recipe.value() instanceof CraftingRecipe craftingRecipe)) continue;
					if (!isRecipeSupported(craftingRecipe)) continue;
					if (craftingRecipe instanceof UncraftingRecipe) continue; // Already handled above

					// Check dimensions
					if (craftingRecipe instanceof ShapedRecipe shapedRecipe) {
						if (shapedRecipe.getWidth() > 3 || shapedRecipe.getHeight() > 3) continue;
					}

					// Check ingredients are not empty
					if (craftingRecipe.placementInfo().ingredients().isEmpty()) continue;

					// Get the output item and match against input
					ItemStack output = craftingRecipe.assemble(CraftingInput.EMPTY);
					if (!matches(inputStack, output)) continue;

					if (TFConfig.reverseRecipeBlacklist == TFConfig.disableUncraftingRecipes.contains(recipe.id().toString())) {
						if (TFConfig.flipUncraftingModIdList == TFConfig.blacklistedUncraftingModIds.contains(recipe.id().identifier().getNamespace())) {
							recipes.add(craftingRecipe);
						}
					}
				}
			}
		}

		return recipes.toArray(new Recipe<?>[0]);
	}

	private static boolean isRecipeSupported(Recipe<?> recipe) {
		return TFConfig.allowShapelessUncrafting ? recipe instanceof CraftingRecipe : recipe instanceof ShapedRecipe;
	}

	private static boolean matches(ItemStack input, ItemStack output) {
		return input.is(output.getItem()) && input.getCount() >= 1;
	}

	@SuppressWarnings({"unchecked", "rawtypes"})
	private static RecipeHolder<CraftingRecipe>[] getRecipesFor(CraftingInput input, Level level) {
		if (level instanceof ServerLevel serverLevel) {
			RecipeManager recipeManager = serverLevel.recipeAccess();
			List<RecipeHolder<CraftingRecipe>> result = new ArrayList<>();
			for (RecipeHolder<?> holder : recipeManager.getRecipes()) {
				if (holder.value() instanceof CraftingRecipe craftingRecipe && craftingRecipe.matches(input, level)) {
					result.add((RecipeHolder<CraftingRecipe>) holder);
				}
			}
			if (net.minecraft.SharedConstants.IS_RUNNING_IN_IDE) {
				TwilightForestMod.LOGGER.info("[UncraftingMenu] getRecipesFor input={}, found={} recipes", input, result.size());
			}
			return result.toArray(new RecipeHolder[0]);
		}
		return new RecipeHolder[0];
	}

	private void chooseRecipe(CraftingInput input) {

		RecipeHolder<CraftingRecipe>[] recipes = getRecipesFor(input, this.level);

		if (recipes.length == 0) {
			this.tinkerResult.setItem(0, ItemStack.EMPTY);
			return;
		}

		RecipeHolder<CraftingRecipe> recipe = recipes[Math.floorMod(this.recipeInCycle, recipes.length)];

		if (recipe != null) {
			this.tinkerResult.setRecipeUsed(recipe);
			this.tinkerResult.setItem(0, recipe.value().assemble(input));
		} else {
			this.tinkerResult.setItem(0, ItemStack.EMPTY);
		}
	}

	/**
	 * Checks if the result is a valid match for the input. Currently, only accepts armor or tools that are the same type as the input
	 */
	private static boolean isValidMatchForInput(ItemStack inputStack, ItemStack resultStack) {
		// Check by equipment slot using EQUIPPABLE component
		Equippable inputEquip = inputStack.get(DataComponents.EQUIPPABLE);
		Equippable resultEquip = resultStack.get(DataComponents.EQUIPPABLE);
		if (inputEquip != null && resultEquip != null) {
			// Both are in the same equipment slot - allow transfer
			// This covers all tools (MAINHAND), all armor (HEAD/CHEST/LEGS/FEET), etc.
			if (inputEquip.slot() == resultEquip.slot()) {
				return true;
			}
		}

		// Fallback: check specific item tags for items that may not have EQUIPPABLE component
		if (inputStack.is(ItemTags.PICKAXES) && resultStack.is(ItemTags.PICKAXES)) {
			return true;
		}
		if (inputStack.is(ItemTags.AXES) && resultStack.is(ItemTags.AXES)) {
			return true;
		}
		if (inputStack.is(ItemTags.SHOVELS) && resultStack.is(ItemTags.SHOVELS)) {
			return true;
		}
		if (inputStack.is(ItemTags.HOES) && resultStack.is(ItemTags.HOES)) {
			return true;
		}
		if (inputStack.is(ItemTags.SWORDS) && resultStack.is(ItemTags.SWORDS)) {
			return true;
		}
		if (inputStack.is(ItemTags.SPEARS) && resultStack.is(ItemTags.SPEARS)) {
			return true;
		}
		// Armor tags
		if (inputStack.is(ItemTags.FOOT_ARMOR) && resultStack.is(ItemTags.FOOT_ARMOR)) {
			return true;
		}
		if (inputStack.is(ItemTags.LEG_ARMOR) && resultStack.is(ItemTags.LEG_ARMOR)) {
			return true;
		}
		if (inputStack.is(ItemTags.CHEST_ARMOR) && resultStack.is(ItemTags.CHEST_ARMOR)) {
			return true;
		}
		if (inputStack.is(ItemTags.HEAD_ARMOR) && resultStack.is(ItemTags.HEAD_ARMOR)) {
			return true;
		}
		// Bows
		if (inputStack.is(Items.BOW) && resultStack.is(Items.BOW)) {
			return true;
		}
		// Crossbows
		if (inputStack.is(Items.CROSSBOW) && resultStack.is(Items.CROSSBOW)) {
			return true;
		}
		// Fishing rods
		if (inputStack.is(Items.FISHING_ROD) && resultStack.is(Items.FISHING_ROD)) {
			return true;
		}
		// Mace
		if (inputStack.is(Items.MACE) && resultStack.is(Items.MACE)) {
			return true;
		}

		return false;
	}

	public int getUncraftingCost() {
		return this.uncraftingMatrix.uncraftingCost;
	}

	public int getRecraftingCost() {
		return this.uncraftingMatrix.recraftingCost;
	}

	/**
	 * Called from client-side network handler to sync cost values from server.
	 */
	public void setClientCosts(int uncraftingCost, int recraftingCost) {
		this.uncraftingMatrix.uncraftingCost = uncraftingCost;
		this.uncraftingMatrix.recraftingCost = recraftingCost;
	}

	/**
	 * Calculate the cost of uncrafting, if any. Return 0 if uncrafting is not available at this time
	 */
	private int calculateUncraftingCost() {
		// we don't want to display anything if there is anything in the assembly grid
		int cost;
		if ((!TFConfig.disableUncraftingOnly || this.storedGhostRecipe instanceof UncraftingRecipe) && this.assemblyMatrix.isEmpty()) {
			cost = this.storedGhostRecipe instanceof UncraftingRecipe recipe ? recipe.getCost() : (int) Math.round(countDamageableParts(this.uncraftingMatrix) * TFConfig.uncraftingXpCostMultiplier);
		} else {
			cost = 0;
		}
		if (net.minecraft.SharedConstants.IS_RUNNING_IN_IDE) {
			TwilightForestMod.LOGGER.info("[UncraftingMenu] calculateUncraftingCost: storedGhostRecipe={}, assemblyMatrix.isEmpty={}, cost={}",
				this.storedGhostRecipe, this.assemblyMatrix.isEmpty(), cost);
		}
		return cost;
	}

	/**
	 * Return the cost of recrafting, if any.  Return 0 if recrafting is not available at this time
	 */
	private int calculateRecraftingCost() {
		ItemStack input = this.tinkerInput.getItem(0);
		ItemStack output = this.tinkerResult.getItem(0);

		if (input.isEmpty() || output.isEmpty()) {
			if (net.minecraft.SharedConstants.IS_RUNNING_IN_IDE) {
				TwilightForestMod.LOGGER.info("[UncraftingMenu] calculateRecraftingCost: input or output empty, returning 0");
			}
			return 0;
		}

		int cost = 0;

		if (!ItemStack.isSameItem(input, output)) {
			// add each ingredient being used to the cost if recrafting
			cost += this.assemblyMatrix.getItems().stream().filter(stack -> !stack.isEmpty()).toList().size();
		}

		// look at the input's enchantments and total them up
		int enchantCost = countTotalEnchantmentCost(input);
		cost += enchantCost;

		// broken pieces cost
		int damagedCost = (1 + this.countDamagedParts(input)) * output.getEnchantments().size();
		cost += damagedCost;

		// minimum cost of 1 if we're even calling this part
		cost = Math.max(1, cost);

		int finalCost = (int) Math.round(cost * TFConfig.repairingXpCostMultiplier);

		if (net.minecraft.SharedConstants.IS_RUNNING_IN_IDE) {
			TwilightForestMod.LOGGER.info("[UncraftingMenu] calculateRecraftingCost: enchantCost={}, damagedCost={}, baseCost={}, multiplier={}, finalCost={}, inputEnchants={}, outputEnchants={}",
				enchantCost, damagedCost, cost, TFConfig.repairingXpCostMultiplier, finalCost,
				input.getEnchantments().size(), output.getEnchantments().size());
		}

		return finalCost;
	}

	private static int countTotalEnchantmentCost(ItemStack stack) {
		int count = 0;

		for (Object2IntMap.Entry<Holder<Enchantment>> entry : stack.getEnchantments().entrySet()) {
			Enchantment ench = entry.getKey().value();
			int level = entry.getIntValue();

			if (level > 0) {
				count += getWeightModifier(ench) * level;
				count += 1;
			}
		}

		return count;
	}

	private static int getWeightModifier(Enchantment ench) {
		return switch (ench.getWeight()) {
			case 1 -> 8;
			case 2 -> 4;
			case 3, 4, 5 -> 2;
			default -> 1;
		};
	}

	@Override
	public void clicked(int slotNum, int mouseButton, ContainerInput containerInput, Player player) {
		// if the player is trying to take an item out of the assembly grid, and the assembly grid is empty, take the item from the uncrafting grid.
		if (slotNum > 0 && this.getSlotContainer(slotNum) == this.assemblyMatrix
			&& player.containerMenu.getCarried().isEmpty() && !this.slots.get(slotNum).hasItem()) {

			// is the assembly matrix empty?
			if (this.assemblyMatrix.isEmpty() && (containerInput != ContainerInput.SWAP || player.getInventory().getItem(mouseButton).isEmpty())) {
				slotNum -= 9;
			}
		}

		// if the player is trying to take the result item and they don't have the XP to pay for it, reject them
		if (slotNum > 0 && this.getSlotContainer(slotNum) == this.tinkerResult
			&& this.calculateRecraftingCost() > player.experienceLevel && !player.getAbilities().instabuild) {

			return;
		}

		if (slotNum > 0 && this.getSlotContainer(slotNum) == this.uncraftingMatrix) {

			// don't allow uncrafting normal recipes if the server option is turned off
			if (TFConfig.disableUncraftingOnly && !(this.storedGhostRecipe instanceof UncraftingRecipe)) {
				return;
			}

			// similarly, reject uncrafting if they can't do that either
			if (this.calculateUncraftingCost() > player.experienceLevel && !player.getAbilities().instabuild) {
				return;
			}

			// finally, don't give them damaged goods
			ItemStack stackInSlot = this.slots.get(slotNum).getItem();
			if (stackInSlot.isEmpty() || isMarked(stackInSlot)) {
				return;
			}
		}

		super.clicked(slotNum, mouseButton, containerInput, player);

		// just trigger this event whenever the input slot is clicked for any reason
		if (slotNum == 0 && this.getSlotContainer(slotNum) == this.tinkerInput) {
			this.slotsChanged(this.tinkerInput);
		}
	}

	@NotNull
	private Container getSlotContainer(int slotNum) {
		return this.slots.get(slotNum).container;
	}

	/**
	 * Should the specified item count for taking damage?
	 */
	public static boolean isDamageableComponent(ItemStack stack) {
		return !stack.isEmpty() && !stack.is(TFItemTags.UNCRAFTING_IGNORES_COST);
	}

	/**
	 * Count how many items in an inventory can take damage
	 */
	public static int countDamageableParts(Container matrix) {
		int count = matrix.getContainerSize();
		for (int i = 0; i < matrix.getContainerSize(); i++) {

			if (isIngredientProblematic(matrix.getItem(i)) || isMarked(matrix.getItem(i)) || !isDamageableComponent(matrix.getItem(i))) {
				count--;
			}
		}
		return count;
	}

	/**
	 * Determine, based on the item damage, how many parts are damaged.  We're already
	 * assuming that the item is loaded into the uncrafting matrix.
	 */
	private int countDamagedParts(ItemStack input) {
		int totalMax4 = Math.max(4, countDamageableParts(this.uncraftingMatrix));
		float damage = (float) input.getDamageValue() / (float) input.getMaxDamage();
		return (int) Math.ceil(totalMax4 * damage);
	}

	/**
	 * Called to transfer a stack from one inventory to the other e.g. when shift clicking.
	 */
	@Override
	public ItemStack quickMoveStack(Player player, int slotNum) {
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = this.slots.get(slotNum);
		//noinspection ConstantConditions
		if (slot != null && slot.hasItem()) {
			ItemStack itemstack1 = slot.getItem();
			itemstack = itemstack1.copy();
			if (slotNum == 0) {
				if (!this.moveItemStackTo(itemstack1, 20, 56, false)) {
					return ItemStack.EMPTY;
				}
				slot.onQuickCraft(itemstack1, itemstack);
			} else if (slotNum == 1) {
				this.positionData.execute((p_39378_, p_39379_) -> itemstack1.getItem().onCraftedBy(itemstack1, player));
				if (!this.moveItemStackTo(itemstack1, 20, 56, true)) {
					return ItemStack.EMPTY;
				}
				slot.onQuickCraft(itemstack1, itemstack);
			} else if (slotNum >= 20 && slotNum < 56) {
				if (!this.moveItemStackTo(itemstack1, 0, 1, false)) {
					return ItemStack.EMPTY;
				}
			} else if (slot.container == this.assemblyMatrix) {
				if (!this.moveItemStackTo(itemstack1, 20, 56, false)) {
					return ItemStack.EMPTY;
				}
			} else {
				if (this.moveItemStackTo(itemstack1, 20, 56, false)) {
					slot.onTake(player, itemstack1);
					return ItemStack.EMPTY;
				}
			}
			if (itemstack1.isEmpty()) {
				slot.set(ItemStack.EMPTY);
			} else {
				slot.setChanged();
			}
			if (itemstack1.getCount() == itemstack.getCount()) {
				return ItemStack.EMPTY;
			}
			slot.onTake(player, itemstack1);
			if (slotNum == 1) {
				player.drop(itemstack1, false);
			}
		}
		return itemstack;
	}

	@Override
	public void removed(Player player) {
		super.removed(player);
		this.positionData.execute((world, pos) -> {
			this.clearContainer(player, this.assemblyMatrix);
			this.clearContainer(player, this.tinkerInput);
		});
	}

	private ItemStack[] getIngredients(Recipe<?> recipe) {
		List<Optional<Ingredient>> optionalIngredients;
		if (recipe instanceof ShapedRecipe shapedRecipe) {
			// 26.1: ShapedRecipe ingredients are List<Optional<Ingredient>> — empty slot = Optional.empty()
			// Never construct Ingredient.of() with no args, that throws "Ingredients can't be empty"
			optionalIngredients = shapedRecipe.getIngredients();
		} else {
			// Shapeless / other recipes return Ingredient list — wrap every entry as Optional.present
			optionalIngredients = recipe.placementInfo().ingredients().stream()
					.map(Optional::of)
					.collect(Collectors.<Optional<Ingredient>>toList());
		}
		ItemStack[] stacks = new ItemStack[optionalIngredients.size()];

		for (int i = 0; i < optionalIngredients.size(); i++) {
			Optional<Ingredient> opt = optionalIngredients.get(i);
			if (opt.isEmpty()) {
				// shaped recipe "empty" slot — nothing to uncraft / display here
				stacks[i] = ItemStack.EMPTY;
			} else {
				ItemStack[] matchingStacks = opt.get().items().map(h -> new ItemStack(h.value())).filter(s -> !s.is(TFItemTags.BANNED_UNCRAFTING_INGREDIENTS)).toArray(ItemStack[]::new);
				stacks[i] = matchingStacks.length > 0 ? matchingStacks[Math.floorMod(this.ingredientsInCycle, matchingStacks.length)] : ItemStack.EMPTY;
			}
		}

		return stacks;
	}

	@Override
	public boolean stillValid(Player player) {
		return !TFConfig.disableEntireTable && stillValid(this.positionData, player, TFBlocks.UNCRAFTING_TABLE);
	}

	@Override
	public void fillCraftSlotsStackedContents(StackedItemContents stackedContents) {
		this.assemblyMatrix.fillStackedContents(stackedContents);
	}

	public void clearCraftingContent() {
		this.tinkerInput.clearContent();
		this.assemblyMatrix.clearContent();
		this.tinkerResult.clearContent();
	}

	public int getResultSlotIndex() {
		return 1; // tinkerResult slot
	}

	public int getGridWidth() {
		return this.assemblyMatrix.getWidth();
	}

	public int getGridHeight() {
		return this.assemblyMatrix.getHeight();
	}

	public int getSize() {
		return 20;
	}

	@Override
	public RecipeBookType getRecipeBookType() {
		return RecipeBookType.CRAFTING;
	}

	public boolean shouldMoveToInventory(int slot) {
		return slot == 0 || (11 <= slot && slot <= 19);
	}

	public boolean recipeMatches(RecipeHolder<Recipe<RecipeInput>> recipeHolder) {
		return recipeHolder.value().matches(this.assemblyMatrix.asCraftInput(), this.player.level());
	}

	@Override
	@SuppressWarnings("unchecked")
	public RecipeBookMenu.PostPlaceAction handlePlacement(boolean useMaxItems, boolean allowDroppingItemsToClear, RecipeHolder<?> recipe, ServerLevel level, Inventory inventory) {
		return ServerPlaceRecipe.placeRecipe(
			new ServerPlaceRecipe.CraftingMenuAccess<CraftingRecipe>() {
				@Override
				public void fillCraftSlotsStackedContents(StackedItemContents stackedContents) {
					UncraftingMenu.this.fillCraftSlotsStackedContents(stackedContents);
				}

				@Override
				public void clearCraftingContent() {
					UncraftingMenu.this.clearCraftingContent();
				}

				@Override
				public boolean recipeMatches(RecipeHolder<CraftingRecipe> holder) {
					return holder.value().matches(UncraftingMenu.this.assemblyMatrix.asCraftInput(), UncraftingMenu.this.player.level());
				}
			},
			UncraftingMenu.this.getGridWidth(),
			UncraftingMenu.this.getGridHeight(),
			UncraftingMenu.this.slots.subList(11, 20),
			UncraftingMenu.this.slots.subList(11, 20),
			inventory,
			(RecipeHolder<CraftingRecipe>) recipe,
			useMaxItems,
			allowDroppingItemsToClear
		);
	}
}
