package com.nred.nuclearcraft.recipe.turbine;

import com.nred.nuclearcraft.recipe.SimpleRecipeBuilder;
import com.nred.nuclearcraft.recipe.base_types.ProcessorRecipeBuilder;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;

public class TurbineRecipeBuilder extends SimpleRecipeBuilder {
    private final SizedFluidIngredient fluidInput;
    private final SizedFluidIngredient fluidResult;
    private final double powerPerMb;
    private final double expansionLevel;
    private final double spinUpMultiplier;
    private final ParticleOptions particle;
    private final double particleSpeedMult;

    public TurbineRecipeBuilder(SizedFluidIngredient fluidInput, SizedFluidIngredient fluidResult, double power_per_mb, double expansion_level, double spin_up_multiplier, ParticleOptions particle, double particle_speed_mult) {
        super(ItemStack.EMPTY);
        this.fluidInput = fluidInput;
        this.fluidResult = fluidResult;
        this.powerPerMb = power_per_mb;
        this.expansionLevel = expansion_level;
        this.spinUpMultiplier = spin_up_multiplier;
        this.particle = particle;
        this.particleSpeedMult = particle_speed_mult;
    }

    @Override
    public void save(RecipeOutput recipeOutput) {
        save(recipeOutput, ProcessorRecipeBuilder.getDefaultRecipeId(fluidInput, fluidResult));
    }

    @Override
    public void save(RecipeOutput output, ResourceLocation key) {
        Advancement.Builder advancement = output.advancement()
                .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(key))
                .rewards(AdvancementRewards.Builder.recipe(key))
                .requirements(AdvancementRequirements.Strategy.OR);

        TurbineRecipe recipe = new TurbineRecipe(this.fluidInput, this.fluidResult, powerPerMb, expansionLevel, spinUpMultiplier, particle, particleSpeedMult);

        output.accept(key, recipe, advancement.build(key.withPrefix("recipes/")));
    }
}
