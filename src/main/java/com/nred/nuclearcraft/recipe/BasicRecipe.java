package com.nred.nuclearcraft.recipe;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.crafting.SizedIngredient;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;

import java.util.Arrays;
import java.util.List;

import static com.nred.nuclearcraft.util.StreamCodecsHelper.SIZED_FLUID_INGREDIENT_LIST_STREAM_CODEC;
import static com.nred.nuclearcraft.util.StreamCodecsHelper.SIZED_ITEM_INGREDIENT_LIST_STREAM_CODEC;

public class BasicRecipe implements IRecipe, Recipe<BasicRecipeInput> {
    public List<SizedIngredient> itemIngredients, itemProducts;
    public List<SizedFluidIngredient> fluidIngredients, fluidProducts;

    public BasicRecipe(List<SizedIngredient> itemIngredients, List<SizedFluidIngredient> fluidIngredients, List<SizedIngredient> itemProducts, List<SizedFluidIngredient> fluidProducts) {
        this.itemIngredients = itemIngredients;
        this.fluidIngredients = fluidIngredients;
        this.itemProducts = itemProducts;
        this.fluidProducts = fluidProducts;
    }

    @Override
    public List<SizedIngredient> getItemIngredients() {
        return itemIngredients;
    }

    @Override
    public List<SizedFluidIngredient> getFluidIngredients() {
        return fluidIngredients;
    }

    @Override
    public List<SizedIngredient> getItemProducts() {
        return itemProducts;
    }

    @Override
    public List<SizedFluidIngredient> getFluidProducts() {
        return fluidProducts;
    }

    public static MapCodec<BasicRecipe> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
            SizedIngredient.FLAT_CODEC.listOf().fieldOf("itemIngredients").forGetter(BasicRecipe::getItemIngredients),
            SizedFluidIngredient.FLAT_CODEC.listOf().fieldOf("fluidIngredients").forGetter(BasicRecipe::getFluidIngredients),
            SizedIngredient.FLAT_CODEC.listOf().fieldOf("itemProducts").forGetter(BasicRecipe::getItemProducts),
            SizedFluidIngredient.FLAT_CODEC.listOf().fieldOf("fluidProducts").forGetter(BasicRecipe::getFluidProducts)
    ).apply(inst, BasicRecipe::new));

    public static StreamCodec<RegistryFriendlyByteBuf, BasicRecipe> STREAM_CODEC = StreamCodec.composite(
            SIZED_ITEM_INGREDIENT_LIST_STREAM_CODEC, BasicRecipe::getItemIngredients,
            SIZED_FLUID_INGREDIENT_LIST_STREAM_CODEC, BasicRecipe::getFluidIngredients,
            SIZED_ITEM_INGREDIENT_LIST_STREAM_CODEC, BasicRecipe::getItemProducts,
            SIZED_FLUID_INGREDIENT_LIST_STREAM_CODEC, BasicRecipe::getFluidProducts,
            BasicRecipe::new
    );

//    @Override
//    public RecipeMatchResult matchInputs(List<ItemStack> itemInputs, List<Tank> fluidInputs) {
//        return RecipeHelper.matchIngredients(IngredientSorption.INPUT, itemIngredients, fluidIngredients, itemInputs, fluidInputs, isShapeless);
//    }
//
//    @Override
//    public RecipeMatchResult matchOutputs(List<ItemStack> itemOutputs, List<Tank> fluidOutputs) {
//        return RecipeHelper.matchIngredients(IngredientSorption.OUTPUT, itemProducts, fluidProducts, itemOutputs, fluidOutputs, isShapeless);
//    }
//
//    @Override
//    public RecipeMatchResult matchIngredients(List<SizedIngredient> itemIngredientsIn, List<SizedFluidIngredient> fluidIngredientsIn) {
//        return RecipeHelper.matchIngredients(IngredientSorption.INPUT, itemIngredients, fluidIngredients, itemIngredientsIn, fluidIngredientsIn, isShapeless);
//    }
//
//    @Override
//    public RecipeMatchResult matchProducts(List<SizedIngredient> itemProductsIn, List<SizedFluidIngredient> fluidProductsIn) {
//        return RecipeHelper.matchIngredients(IngredientSorption.OUTPUT, itemProducts, fluidProducts, itemProductsIn, fluidProductsIn, isShapeless);
//    }

    @Override
    public boolean matches(BasicRecipeInput input, Level level) {
        return RecipeHelper.matchIngredients(IngredientSorption.INPUT, itemIngredients, fluidIngredients, input.itemIngredients(), input.fluidIngredients()).isMatch;
    }

    @Override
    public ItemStack assemble(BasicRecipeInput input, HolderLookup.Provider registries) {
        return itemProducts.isEmpty() ? ItemStack.EMPTY : Arrays.stream(itemProducts.getFirst().getItems()).findFirst().orElse(ItemStack.EMPTY).copy();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider registries) {
        return itemProducts.isEmpty() ? ItemStack.EMPTY : Arrays.stream(itemProducts.getFirst().getItems()).findFirst().orElse(ItemStack.EMPTY).copy();
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return null; // TODO!
    }

    @Override
    public RecipeType<?> getType() {
        return null;
    }

    // Recipe Extras

    // Processors TODO

    public double getProcessTimeMultiplier() {
        return 0;// (double) extras.get(0);
    }

    public double getProcessPowerMultiplier() {
        return 0;// (double) extras.get(1);
    }

    public double getBaseProcessTime(double defaultProcessTime) {
        return 0;// getProcessTimeMultiplier() * defaultProcessTime;
    }

    public double getBaseProcessPower(double defaultProcessPower) {
        return 0;// getProcessPowerMultiplier() * defaultProcessPower;
    }

    public double getBaseProcessRadiation() {
        return 0;//  (double) extras.get(2);
    }
//
//    // Passive Collector
//
//    public String getCollectorProductionRate() {
//        return (String) extras.get(0);
//    }
//
//    // Decay Generator
//
//    public double getDecayGeneratorLifetime() {
//        return (double) extras.get(0);
//    }
//
//    public double getDecayGeneratorPower() {
//        return (double) extras.get(1);
//    }
//
//    public double getDecayGeneratorRadiation() {
//        return (double) extras.get(2);
//    }
//
//    // Placement Rule
//
//    public String getPlacementRuleID() {
//        return (String) extras.get(0);
//    }
//
//    // Diaphragm
//
//    public double getMachineDiaphragmEfficiency() {
//        return (double) extras.get(0);
//    }
//
//    public double getMachineDiaphragmContactFactor() {
//        return (double) extras.get(1);
//    }
//
//    // Sieve Assembly
//
//    public double getMachineSieveAssemblyEfficiency() {
//        return (double) extras.get(0);
//    }
//
//    // Electrolyzer
//

//    public ElectrolyzerElectrolyteRecipeHandler getElectrolyzerElectrolyteRecipeHandler() { TODO
//        return NCRecipes.multiblock_electrolyzer.electrolyteRecipeHandlerMap.get(extras.get(3));
//    }
//
//    public double getElectrolyzerElectrolyteEfficiency() {
//        return (double) extras.get(0);
//    }
//
//    public double getElectrolyzerElectrodeEfficiency() {
//        return (double) extras.get(0);
//    }
//
//    // Distiller
//
//    public long getDistillerSieveTrayCount() {
//        long liquidProductCount = getFluidProducts().stream().filter(x -> {
//            FluidStack stack = x.getStack();
//            Fluid fluid = stack == null ? null : stack.getFluid();
//            return fluid != null && fluid.getFluidType().getDensity(stack) > 0;
//        }).count();
//
//        return Math.max(0L, liquidProductCount - 1L);
//    }
//
//    // Infiltrator
//
//    public double getInfiltratorHeatingFactor() {
//        return (double) extras.get(3);
//    }
//
//    public double getInfiltratorPressureFluidEfficiency() {
//        return (double) extras.get(0);
//    }

//    // Fission Reflector
//
//    public double getFissionReflectorEfficiency() {
//        return (double) extras.get(0);
//    }
//
//    public double getFissionReflectorReflectivity() {
//        return 0;//(double) extras.get(1);
//    }

    // Fission Irradiator
    public long getIrradiatorFluxRequired() {
        return 0;//(long) extras.get(0);
    }

    public double getIrradiatorHeatPerFlux() {
        return 0;//(double) extras.get(1);
    }

    public double getIrradiatorProcessEfficiency() {
        return 0;//(double) extras.get(2);
    }

    public long getIrradiatorMinFluxPerTick() {
        return 0;//(long) extras.get(3);
    }

    public long getIrradiatorMaxFluxPerTick() {
        return 0;//(long) extras.get(4);
    }

    public double getIrradiatorBaseProcessRadiation() {
        return 0;//(double) extras.get(5);
    }

    // Fission Heating

    public int getFissionHeatingHeatPerInputMB() {
        return 0;//(int) extras.get(0);
    }

    // Fission Emergency Cooling

    public double getEmergencyCoolingHeatPerInputMB() {
        return 0;// (double) extras.get(0);
    }

    // Coolant Heater
    public int getCoolantHeaterCoolingRate() {
        return 0;//(int) extras.get(0);
    }

    public String getCoolantHeaterPlacementRule() {
        return "";// (String) extras.get(1);
    }

    public String[] getCoolantHeaterJEIInfo() {
//        String rule = FissionPlacement.TOOLTIP_MAP.get(getCoolantHeaterPlacementRule());
//        if (rule != null) {
//            return FontRenderHelper.wrapString(rule, InfoHelper.MAXIMUM_TEXT_WIDTH);
//        }
        return new String[]{};
    }
//
//    // Heat Exchanger
//
//    public double getHeatExchangerHeatDifference() {
//        return (double) extras.get(0);
//    }
//
//    public int getHeatExchangerInputTemperature() {
//        return (int) extras.get(1);
//    }
//
//    public int getHeatExchangerOutputTemperature() {
//        return (int) extras.get(2);
//    }
//
//    public boolean getHeatExchangerIsHeating() {
//        int inputTemperature = getHeatExchangerInputTemperature(), outputTemperature = getHeatExchangerOutputTemperature();
//        if (inputTemperature == outputTemperature) {
//            return (boolean) extras.get(3);
//        } else {
//            return inputTemperature < outputTemperature;
//        }
//    }
//
//    public int getHeatExchangerPreferredFlowDirection() {
//        return (int) extras.get(4);
//    }
//
//    public double getHeatExchangerFlowDirectionBonus() {
//        return (double) extras.get(5);
//    }
//
//    public double getHeatExchangerFlowDirectionMultiplier(Vec3i flowDir) {
//        int preferredFlowDirection = getHeatExchangerPreferredFlowDirection();
//        double flowDirectionBonus = getHeatExchangerFlowDirectionBonus();
//
//		/*if (preferredFlowDirection == 0) {
//			return Math.max(0D, 1D + flowDirectionBonus * Math.sqrt(flowDir.x * flowDir.x + flowDir.z * flowDir.z));
//		}
//		else if (preferredFlowDirection > 0) {
//			return Math.max(0D, 1D + flowDirectionBonus * flowDir.y);
//		}
//		else {
//			return Math.max(0D, 1D - flowDirectionBonus * flowDir.y);
//		}*/
//
//        double flowProjection = preferredFlowDirection == 0 ? Math.sqrt(flowDir.getX() * flowDir.getX() + flowDir.getZ() * flowDir.getZ()) : (preferredFlowDirection > 0 ? flowDir.getY() : -flowDir.getY());
//        return Math.max(0D, 1D + flowDirectionBonus * (flowProjection > 0.5D ? 1D : (flowProjection > -0.5D ? 0D : -1D)));
//    }
//
//    // Condenser
//
//    public double getCondenserCoolingRequired() {
//        return (double) extras.get(0);
//    }
//
//    public int getCondenserInputTemperature() {
//        return (int) extras.get(1);
//    }
//
//    public int getCondenserOutputTemperature() {
//        return (int) extras.get(2);
//    }
//
//    public int getCondenserPreferredFlowDirection() {
//        return (int) extras.get(3);
//    }
//
//    public double getCondenserFlowDirectionBonus() {
//        return (double) extras.get(4);
//    }
//
//    public double getCondenserFlowDirectionMultiplier(Vec3i flowDir) {
//        int preferredFlowDirection = getCondenserPreferredFlowDirection();
//        double flowDirectionBonus = getCondenserFlowDirectionBonus();
//        double flowProjection = preferredFlowDirection == 0 ? Math.sqrt(flowDir.getX() * flowDir.getX() + flowDir.getZ() * flowDir.getZ()) : (preferredFlowDirection > 0 ? flowDir.getY() : -flowDir.getY());
//        return Math.max(0D, 1D + flowDirectionBonus * (flowProjection > 0.5D ? 1D : (flowProjection > -0.5D ? 0D : -1D)));
//    }
//
//    public int getCondenserDissipationFluidTemperature() {
//        return (int) extras.get(0);
//    }
//
//    // Turbine
//
//    public double getTurbinePowerPerMB() {
//        return 0;//(double) extras.get(0);
//    }
//
//    public double getTurbineExpansionLevel() {
//        return 0;// (double) extras.get(1);
//    }
//
//    public double getTurbineSpinUpMultiplier() {
//        return 0;// turbine_spin_up_multiplier_global * (double) extras.get(2);
//    }
//
//    public String getTurbineParticleEffect() {
////        ParticleType<?> particle = BuiltInRegistries.PARTICLE_TYPE.get(ResourceLocation.parse((String) extras.get(3)));
//        return "";// particle == null ? "cloud" : (String) extras.get(3);
//    }
//
//    public double getTurbineParticleSpeedMultiplier() {
//        return 0;// (double) extras.get(4);
//    }
//
//    // Fusion
//
//    public double getFusionComboTime() {
//        return fusion_fuel_time_multiplier * (double) extras.get(0);
//    }
//
//    public double getFusionComboHeat() {
//        return fusion_fuel_heat_multiplier * (double) extras.get(1);
//    }
//
//    public double getFusionComboOptimalTemperature() {
//        return (double) extras.get(2);
//    }
//
//    public double getFusionComboRadiation() {
//        return fusion_fuel_radiation_multiplier * (double) extras.get(3);
//    }
//
//    // Radiation Scrubber
//
//    public long getScrubberProcessTime() {
//        return (long) extras.get(0);
//    }
//
//    public long getScrubberProcessPower() {
//        return (long) extras.get(1);
//    }
//
//    public double getScrubberProcessEfficiency() {
//        return (double) extras.get(2);
//    }
//
//    // Radiation Block Mutations
//
//    public double getBlockMutationThreshold() {
//        return (double) extras.get(0);
//    }
}
