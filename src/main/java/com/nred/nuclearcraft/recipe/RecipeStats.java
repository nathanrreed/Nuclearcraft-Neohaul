package com.nred.nuclearcraft.recipe;

import com.nred.nuclearcraft.block_entity.processor.info.ProcessorMenuInfo;
import com.nred.nuclearcraft.handler.SizedChanceFluidIngredient;
import com.nred.nuclearcraft.handler.TileContainerInfo;
import com.nred.nuclearcraft.handler.TileInfoHandler;
import com.nred.nuclearcraft.recipe.NCRecipes.BasicProcessorRecipeHandler;
import com.nred.nuclearcraft.recipe.fission.FissionModeratorRecipe;
import com.nred.nuclearcraft.util.NCMath;

import java.util.List;

import static com.nred.nuclearcraft.config.NCConfig.*;

public class RecipeStats {
    private static int decay_generator_max_power;
    private static int fission_max_moderator_line_flux;
    private static double block_mutation_threshold;
    private static double block_purification_threshold;

    public static void init() {
        setBasicProcessorMaxStats();
        setScrubberMaxStats();
        setDecayGeneratorMaxPower();
        setFissionMaxModeratorLineFlux();
        setBlockMutationThreshold();
        setBlockPurificationThreshold();
    }

    private static void setBasicProcessorMaxStats() {
        for (TileContainerInfo<?> info : TileInfoHandler.TILE_CONTAINER_INFO_MAP.values()) {
            if (info instanceof ProcessorMenuInfo<?, ?, ?> processorInfo) {
                if (processorInfo.getRecipeHandler() instanceof BasicProcessorRecipeHandler processorRecipeHandler) {
                    List<ProcessorRecipe> recipeList = processorRecipeHandler.getRecipeList();
                    if (recipeList.isEmpty()) {
                        processorInfo.maxBaseProcessTime = processor_time_multiplier * processorInfo.getDefaultProcessTime();
                        processorInfo.maxBaseProcessPower = processor_power_multiplier * processorInfo.getDefaultProcessPower();
                    } else {
                        double maxProcessTimeMultiplier = 1D, maxProcessPowerMultiplier = 0D;
                        int maxFluidInputSize = 0, maxFluidOutputSize = 0;

                        for (ProcessorRecipe recipe : processorRecipeHandler.getRecipeList()) {
                            maxProcessTimeMultiplier = Math.max(maxProcessTimeMultiplier, recipe.getProcessTimeMultiplier());
                            maxProcessPowerMultiplier = Math.max(maxProcessPowerMultiplier, recipe.getProcessPowerMultiplier());

                            for (SizedChanceFluidIngredient ingredient : recipe.getFluidIngredients()) {
                                maxFluidInputSize = Math.max(maxFluidInputSize, ingredient.amount());
                            }

                            for (SizedChanceFluidIngredient ingredient : recipe.getFluidProducts()) {
                                maxFluidOutputSize = Math.max(maxFluidOutputSize, ingredient.amount());
                            }
                        }

                        processorInfo.maxBaseProcessTime = maxProcessTimeMultiplier * processor_time_multiplier * processorInfo.getDefaultProcessTime();
                        processorInfo.maxBaseProcessPower = maxProcessPowerMultiplier * processor_power_multiplier * processorInfo.getDefaultProcessPower();

                        processorInfo.inputTankCapacity = Math.max(processorInfo.inputTankCapacity, 2 * maxFluidInputSize);
                        processorInfo.outputTankCapacity = Math.max(processorInfo.outputTankCapacity, 2 * maxFluidOutputSize);
                    }
                }
            }
        }
    }

    private static void setScrubberMaxStats() {
        ProcessorMenuInfo<?, ?, ?> info = TileInfoHandler.getProcessorContainerInfo("radiation_scrubber");
        info.maxBaseProcessTime = 1D;
        info.maxBaseProcessPower = 0D;
        for (BasicRecipe recipe : info.getRecipeHandler().getRecipeList()) {
            info.maxBaseProcessTime = Math.max(info.maxBaseProcessTime, ((RadiationScrubberRecipe) recipe).getScrubberProcessTime());
            info.maxBaseProcessPower = Math.max(info.maxBaseProcessPower, ((RadiationScrubberRecipe) recipe).getScrubberProcessPower());
        }
    }

    public static int getDecayGeneratorMaxPower() {
        return decay_generator_max_power;
    }

    private static void setDecayGeneratorMaxPower() {
        double max = 0D;
        for (DecayGeneratorRecipe recipe : NCRecipes.decay_generator.getRecipeList()) {
            if (recipe != null) {
                max = Math.max(max, recipe.getDecayGeneratorPower());
            }
        }
        decay_generator_max_power = NCMath.toInt(machine_update_rate * max);
    }

    public static int getFissionMaxModeratorLineFlux() {
        return fission_max_moderator_line_flux;
    }

    private static void setFissionMaxModeratorLineFlux() {
        fission_max_moderator_line_flux = 0;
        for (FissionModeratorRecipe recipe : NCRecipes.fission_moderator.getRecipeList()) {
            if (recipe != null) {
                fission_max_moderator_line_flux = Math.max(fission_max_moderator_line_flux, recipe.getFissionModeratorFluxFactor());
            }
        }
        fission_max_moderator_line_flux *= fission_neutron_reach;
    }

    public static double getBlockMutationThreshold() {
        return block_mutation_threshold;
    }

    private static void setBlockMutationThreshold() {
        block_mutation_threshold = Double.MAX_VALUE;
        for (BasicRecipe basicRecipe : NCRecipes.radiation_block_mutation.getRecipeList()) {
            if (basicRecipe instanceof RadiationBlockMutationRecipe recipe) {
                block_mutation_threshold = Math.min(block_mutation_threshold, recipe.getBlockMutationThreshold());
            }
        }
    }

    public static double getBlockPurificationThreshold() {
        return block_purification_threshold;
    }

    private static void setBlockPurificationThreshold() {
        block_purification_threshold = 0D;
        for (BasicRecipe basicRecipe : NCRecipes.radiation_block_purification.getRecipeList()) {
            if (basicRecipe instanceof RadiationBlockPurificationRecipe recipe) {
                block_purification_threshold = Math.max(block_purification_threshold, recipe.getPurificationThreshold());
            }
        }
    }
}