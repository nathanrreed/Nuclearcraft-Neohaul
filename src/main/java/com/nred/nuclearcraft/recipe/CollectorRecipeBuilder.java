package com.nred.nuclearcraft.recipe;

import com.nred.nuclearcraft.block.collector.MACHINE_LEVEL;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.FluidStack;

public class CollectorRecipeBuilder extends SimpleRecipeBuilder {
    private final ItemStack itemResult;
    private final FluidStack fluidResult;
    private final MACHINE_LEVEL level;
    private final double rate;

    public CollectorRecipeBuilder(ItemStack itemResult, FluidStack fluidResult, MACHINE_LEVEL level, double rate) {
        super(itemResult);
        this.itemResult = itemResult;
        this.fluidResult = fluidResult;
        this.level = level;
        this.rate = rate;
    }

    public CollectorRecipeBuilder(ItemStack itemResult, MACHINE_LEVEL level, double rate) {
        super(itemResult);
        this.itemResult = itemResult;
        this.fluidResult = FluidStack.EMPTY;
        this.level = level;
        this.rate = rate;
    }

    public CollectorRecipeBuilder(FluidStack fluidResult, MACHINE_LEVEL level) {
        super(ItemStack.EMPTY);
        this.itemResult = ItemStack.EMPTY;
        this.fluidResult = fluidResult;
        this.level = level;
        this.rate = fluidResult.getAmount();
    }

    @Override
    public void save(RecipeOutput output, ResourceLocation key) {
        Advancement.Builder advancement = output.advancement()
                .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(key))
                .rewards(AdvancementRewards.Builder.recipe(key))
                .requirements(AdvancementRequirements.Strategy.OR);

        CollectorRecipe recipe = new CollectorRecipe(this.itemResult, this.fluidResult, level, rate);

        output.accept(key, recipe, advancement.build(key.withPrefix("recipes/")));
    }
}
