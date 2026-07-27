package twilightforest.mixin;

import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(AbstractFurnaceBlockEntity.class)
public interface AbstractFurnaceBlockEntityMixin {

        @Accessor("cookingTimer")
        int getCookingTimer();

        @Accessor("cookingTimer")
        void setCookingTimer(int value);

        @Accessor("cookingTotalTime")
        int getCookingTotalTime();

        @Accessor("cookingTotalTime")
        void setCookingTotalTime(int value);

        @Accessor("litTimeRemaining")
        int getLitTimeRemaining();

        @Accessor("litTimeRemaining")
        void setLitTimeRemaining(int value);

        @Accessor("litTotalTime")
        int getLitTotalTime();

        @Accessor("litTotalTime")
        void setLitTotalTime(int value);

        @Accessor("quickCheck")
        RecipeManager.CachedCheck<SingleRecipeInput, ? extends AbstractCookingRecipe> getQuickCheck();
}