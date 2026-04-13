package com.nred.nuclearcraft.compat.recipe_viewer;

import com.nred.nuclearcraft.multiblock.fisson.FissionPlacement;
import com.nred.nuclearcraft.radiation.RadiationHelper;
import com.nred.nuclearcraft.recipe.BasicRecipe;
import com.nred.nuclearcraft.recipe.DecayGeneratorRecipe;
import com.nred.nuclearcraft.recipe.RadiationScrubberRecipe;
import com.nred.nuclearcraft.recipe.RecipeStats;
import com.nred.nuclearcraft.recipe.exchanger.CondenserRecipe;
import com.nred.nuclearcraft.recipe.exchanger.HeatExchangerRecipe;
import com.nred.nuclearcraft.recipe.fission.*;
import com.nred.nuclearcraft.recipe.machine.MultiblockDistillerRecipe;
import com.nred.nuclearcraft.recipe.machine.MultiblockElectrolyzerRecipe;
import com.nred.nuclearcraft.recipe.machine.MultiblockInfiltratorRecipe;
import com.nred.nuclearcraft.recipe.turbine.TurbineRecipe;
import com.nred.nuclearcraft.util.DataMapHelper;
import com.nred.nuclearcraft.util.NCMath;
import com.nred.nuclearcraft.util.UnitHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static com.nred.nuclearcraft.NuclearcraftNeohaul.MODID;
import static com.nred.nuclearcraft.config.NCConfig.*;
import static com.nred.nuclearcraft.registration.DataMapTypeRegistration.INFILTRATOR_PRESSURE_DATA;

public class RecipeViewerImpl {
    public static Function<FluidStack, Component> INFILTRATOR_PRESSURE_FLUID_TOOLTIP = (fluidStack) -> Component.translatable(MODID + ".recipe_viewer.infiltrator_pressure_fluid_efficiency", Component.literal(NCMath.pcDecimalPlaces(DataMapHelper.getData(fluidStack, INFILTRATOR_PRESSURE_DATA).efficiency(), 1)).withStyle(ChatFormatting.WHITE)).withStyle(ChatFormatting.LIGHT_PURPLE);
    public static Function<FluidStack, Component> CONDENSER_DISSIPATION_TOOLTIP = (fluidStack) -> Component.translatable(MODID + ".recipe_viewer.condenser_dissipation_fluid_temp", Component.literal(fluidStack.getFluidType().getTemperature() + "K").withStyle(ChatFormatting.WHITE)).withStyle(ChatFormatting.AQUA);

    public abstract static class RecipeViewer<R extends BasicRecipe> {
        public final R recipe;

        public RecipeViewer(R recipe) {
            this.recipe = recipe;
        }

        public abstract List<Component> progressTooltips(int x, int y);

        public abstract int getProgressTime();
    }

    public static class FissionEmergencyCoolingRecipeViewer extends RecipeViewer<FissionEmergencyCoolingRecipe> {
        public FissionEmergencyCoolingRecipeViewer(FissionEmergencyCoolingRecipe recipe) {
            super(recipe);
        }

        public List<Component> progressTooltips(int x, int y) {
            ArrayList<Component> list = new ArrayList<>(1);
            list.add(Component.translatable(MODID + ".recipe_viewer.fission_emergency_cooling_heating_required", Component.literal(UnitHelper.prefix(recipe.getEmergencyCoolingHeatPerInputMB(), 5, "H")).withStyle(ChatFormatting.WHITE)).withStyle(ChatFormatting.YELLOW));
            return list;
        }

        public int getProgressTime() {
            return NCMath.toInt(640.0 / recipe.getEmergencyCoolingHeatPerInputMB());
        }
    }

    public static class DecayGeneratorRecipeViewer extends RecipeViewer<DecayGeneratorRecipe> {
        public DecayGeneratorRecipeViewer(DecayGeneratorRecipe recipe) {
            super(recipe);
        }

        public List<Component> progressTooltips(int x, int y) {
            ArrayList<Component> list = new ArrayList<>(2);

            list.add(Component.translatable(MODID + ".recipe_viewer.decay_gen_lifetime", Component.literal(UnitHelper.applyTimeUnitShort(recipe.getDecayGeneratorLifetime(), 3, 1))).withStyle(ChatFormatting.WHITE).withStyle(ChatFormatting.GREEN));
            list.add(Component.translatable(MODID + ".recipe_viewer.decay_gen_power", Component.literal(UnitHelper.prefix(recipe.getDecayGeneratorPower(), 5, "RF/t")).withStyle(ChatFormatting.WHITE)).withStyle(ChatFormatting.LIGHT_PURPLE));


            double radiation = recipe.getDecayGeneratorRadiation();
            if (radiation > 0D) {
                list.add(Component.translatable(MODID + ".recipe_viewer.decay_gen_radiation", RadiationHelper.radsColoredPrefix(radiation, true)).withStyle(ChatFormatting.GOLD));
            }

            return list;
        }

        public int getProgressTime() {
            return NCMath.toInt(20D * recipe.getDecayGeneratorLifetime());
        }
    }

    public static class CondenserRecipeViewer extends RecipeViewer<CondenserRecipe> {
        public CondenserRecipeViewer(CondenserRecipe recipe) {
            super(recipe);
        }

        public List<Component> progressTooltips(int x, int y) {
            ArrayList<Component> list = new ArrayList<>(3);

            list.add(Component.translatable(MODID + ".recipe_viewer.condenser_fluid_temp_in", Component.literal(recipe.getCondenserInputTemperature() + "K").withStyle(ChatFormatting.WHITE)).withStyle(ChatFormatting.RED));
            list.add(Component.translatable(MODID + ".recipe_viewer.condenser_fluid_temp_out", Component.literal(recipe.getCondenserOutputTemperature() + "K").withStyle(ChatFormatting.WHITE)).withStyle(ChatFormatting.AQUA));
            list.add(Component.translatable(MODID + ".recipe_viewer.condenser_cooling_required", Component.literal(UnitHelper.prefix(recipe.getCondenserCoolingRequired(), 5, "H")).withStyle(ChatFormatting.WHITE)).withStyle(ChatFormatting.BLUE));

            double flowDirectionBonus = recipe.getCondenserFlowDirectionBonus();
            if (flowDirectionBonus != 0D) {
                int preferredFlowDirection = recipe.getCondenserPreferredFlowDirection();
                list.add(Component.translatable(MODID + ".recipe_viewer." + (preferredFlowDirection == 0 ? "condenser_horizontal_bonus" : (preferredFlowDirection > 0 ? "condenser_upward_bonus" : "condenser_downward_bonus")), Component.literal(NCMath.pcDecimalPlaces(flowDirectionBonus, 1)).withStyle(ChatFormatting.WHITE)).withStyle(ChatFormatting.LIGHT_PURPLE));
            }

            return list;
        }

        public int getProgressTime() {
            return NCMath.toInt(400D * recipe.getCondenserCoolingRequired());
        }
    }

    public static class TurbineRecipeViewer extends RecipeViewer<TurbineRecipe> {
        public TurbineRecipeViewer(TurbineRecipe recipe) {
            super(recipe);
        }

        public List<Component> progressTooltips(int x, int y) {
            ArrayList<Component> list = new ArrayList<>(2);
            list.addAll(List.of(
                    Component.translatable(MODID + ".recipe_viewer.turbine_energy_density", Component.literal(NCMath.decimalPlaces(recipe.getTurbinePowerPerMB(), 2) + " RF/mB").withStyle(ChatFormatting.WHITE)).withStyle(ChatFormatting.LIGHT_PURPLE),
                    Component.translatable(MODID + ".recipe_viewer.turbine_expansion", Component.literal(NCMath.pcDecimalPlaces(recipe.getTurbineExpansionLevel(), 1)).withStyle(ChatFormatting.WHITE)).withStyle(ChatFormatting.GRAY)));
            if (recipe.getTurbineSpinUpMultiplier() != 1.0)
                list.add(Component.translatable(MODID + ".recipe_viewer.turbine_spin_up_multiplier", Component.literal(NCMath.pcDecimalPlaces(recipe.getTurbineSpinUpMultiplier(), 1)).withStyle(ChatFormatting.WHITE)).withStyle(ChatFormatting.GREEN));
            return list;
        }

        public int getProgressTime() {
            return 1200;
        }
    }

    public static class SolidFissionRecipeViewer extends RecipeViewer<SolidFissionRecipe> {
        public SolidFissionRecipeViewer(SolidFissionRecipe recipe) {
            super(recipe);
        }

        public List<Component> progressTooltips(int x, int y) {
            ArrayList<Component> list = new ArrayList<>(4);

            list.add(Component.translatable(MODID + ".info.fission_fuel.base_time", Component.literal(UnitHelper.applyTimeUnitShort(recipe.getFissionFuelTime(), 3)).withStyle(ChatFormatting.WHITE)).withStyle(ChatFormatting.GREEN));
            list.add(Component.translatable(MODID + ".info.fission_fuel.base_heat", Component.literal(UnitHelper.prefix(recipe.getFissionFuelHeat(), 5, "H/t")).withStyle(ChatFormatting.WHITE)).withStyle(ChatFormatting.YELLOW));
            list.add(Component.translatable(MODID + ".info.fission_fuel.base_efficiency", Component.literal(NCMath.pcDecimalPlaces(recipe.getFissionFuelEfficiency(), 1)).withStyle(ChatFormatting.WHITE)).withStyle(ChatFormatting.LIGHT_PURPLE));
            list.add(Component.translatable(MODID + ".info.fission_fuel.criticality", Component.literal(recipe.getFissionFuelCriticality() + " N/t").withStyle(ChatFormatting.WHITE)).withStyle(ChatFormatting.RED));

            if (fission_decay_mechanics) {
                list.add(Component.translatable(MODID + ".info.fission_fuel.decay_factor", Component.literal(NCMath.pcDecimalPlaces(recipe.getFissionFuelDecayFactor(), 1)).withStyle(ChatFormatting.WHITE)).withStyle(ChatFormatting.GRAY));
            }
            if (recipe.getFissionFuelSelfPriming()) {
                list.add(Component.translatable(MODID + ".info.fission_fuel.self_priming").withStyle(ChatFormatting.DARK_AQUA));
            }

            double radiation = recipe.getFissionFuelRadiation();
            if (radiation > 0D) {
                list.add(Component.translatable(MODID + ".recipe_viewer.radiation_per_flux", RadiationHelper.radsColoredPrefix(radiation, true)).withStyle(ChatFormatting.GOLD));
            }

            return list;
        }

        public int getProgressTime() {
            return NCMath.toInt(recipe.getFissionFuelTime() * 16.0);
        }
    }

    public static class SaltFissionRecipeViewer extends RecipeViewer<SaltFissionRecipe> {
        public SaltFissionRecipeViewer(SaltFissionRecipe recipe) {
            super(recipe);
        }

        @Override
        public List<Component> progressTooltips(int x, int y) {
            ArrayList<Component> list = new ArrayList<>(4);

            list.add(Component.translatable(MODID + ".info.fission_fuel.base_time", Component.literal(UnitHelper.applyTimeUnitShort(recipe.getSaltFissionFuelTime(), 3)).withStyle(ChatFormatting.WHITE)).withStyle(ChatFormatting.GREEN));
            list.add(Component.translatable(MODID + ".info.fission_fuel.base_heat", Component.literal(UnitHelper.prefix(recipe.getFissionFuelHeat(), 5, "H/t")).withStyle(ChatFormatting.WHITE)).withStyle(ChatFormatting.YELLOW));
            list.add(Component.translatable(MODID + ".info.fission_fuel.base_efficiency", Component.literal(NCMath.pcDecimalPlaces(recipe.getFissionFuelEfficiency(), 1)).withStyle(ChatFormatting.WHITE)).withStyle(ChatFormatting.LIGHT_PURPLE));
            list.add(Component.translatable(MODID + ".info.fission_fuel.criticality", Component.literal(recipe.getFissionFuelCriticality() + " N/t").withStyle(ChatFormatting.WHITE)).withStyle(ChatFormatting.RED));

            if (fission_decay_mechanics) {
                list.add(Component.translatable(MODID + ".info.fission_fuel.decay_factor", Component.literal(NCMath.pcDecimalPlaces(recipe.getFissionFuelDecayFactor(), 1)).withStyle(ChatFormatting.WHITE)).withStyle(ChatFormatting.GRAY));
            }
            if (recipe.getFissionFuelSelfPriming()) {
                list.add(Component.translatable(MODID + ".info.fission_fuel.self_priming").withStyle(ChatFormatting.DARK_AQUA));
            }

            double radiation = recipe.getFissionFuelRadiation();
            if (radiation > 0D) {
                list.add(Component.translatable(MODID + ".recipe_viewer.radiation_per_flux", RadiationHelper.radsColoredPrefix(radiation, true)).withStyle(ChatFormatting.GOLD));
            }

            return list;
        }

        @Override
        public int getProgressTime() {
            return NCMath.toInt(recipe.getSaltFissionFuelTime() * 9.0);
        }
    }

    public static class FissionCoolantHeaterRecipeViewer extends RecipeViewer<FissionCoolantHeaterRecipe> {
        public FissionCoolantHeaterRecipeViewer(FissionCoolantHeaterRecipe recipe) {
            super(recipe);
        }

        @Override
        public List<Component> progressTooltips(int x, int y) {
            ArrayList<Component> list = new ArrayList<>(2);

            list.add(Component.translatable(MODID + ".recipe_viewer.coolant_heater_rate", Component.literal(UnitHelper.prefix(recipe.getCoolantHeaterCoolingRate(), 5, "H/t")).withStyle(ChatFormatting.WHITE)).withStyle(ChatFormatting.BLUE));

            String coolantHeaterInfo = FissionPlacement.TOOLTIP_MAP.getOrDefault(recipe.getCoolantHeaterPlacementRule(), "");
            if (!coolantHeaterInfo.isEmpty()) {
                list.add(Component.literal(coolantHeaterInfo).withStyle(ChatFormatting.AQUA));
            }
            return list;
        }

        @Override
        public int getProgressTime() {
            return 2000;
        }
    }

    public static class RadiationScrubberRecipeViewer extends RecipeViewer<RadiationScrubberRecipe> {
        public RadiationScrubberRecipeViewer(RadiationScrubberRecipe recipe) {
            super(recipe);
        }

        @Override
        public List<Component> progressTooltips(int x, int y) {
            ArrayList<Component> list = new ArrayList<>(3);
            list.add(Component.translatable(MODID + ".recipe_viewer.scrubber_process_time", Component.literal(UnitHelper.applyTimeUnitShort(recipe.getScrubberProcessTime(), 3)).withStyle(ChatFormatting.WHITE)).withStyle(ChatFormatting.GREEN));
            list.add(Component.translatable(MODID + ".recipe_viewer.scrubber_process_power", Component.literal(UnitHelper.prefix(recipe.getScrubberProcessPower(), 5, "RF/t")).withStyle(ChatFormatting.WHITE)).withStyle(ChatFormatting.LIGHT_PURPLE));
            list.add(Component.translatable(MODID + ".recipe_viewer.scrubber_process_efficiency", Component.literal(NCMath.pcDecimalPlaces(recipe.getScrubberProcessEfficiency(), 1)).withStyle(ChatFormatting.WHITE)).withStyle(ChatFormatting.RED));
            return list;
        }

        @Override
        public int getProgressTime() {
            return NCMath.toInt(recipe.getScrubberProcessTime() * 1.2);
        }
    }

    public static class MultiblockInfiltratorRecipeViewer extends RecipeViewer<MultiblockInfiltratorRecipe> {
        public MultiblockInfiltratorRecipeViewer(MultiblockInfiltratorRecipe recipe) {
            super(recipe);
        }

        @Override
        public List<Component> progressTooltips(int x, int y) {
            ArrayList<Component> list = new ArrayList<>(2);

            list.add(Component.translatable(MODID + ".tooltip.process_time", Component.literal(UnitHelper.applyTimeUnitShort(recipe.getBaseProcessTime(machine_infiltrator_time), 3)).withStyle(ChatFormatting.WHITE)).withStyle(ChatFormatting.GREEN));
            list.add(Component.translatable(MODID + ".tooltip.process_power", Component.literal(UnitHelper.prefix(recipe.getBaseProcessPower(machine_infiltrator_power), 5, "RF/t")).withStyle(ChatFormatting.WHITE)).withStyle(ChatFormatting.LIGHT_PURPLE));

            double heatingBonus = recipe.getInfiltratorHeatingFactor();
            if (heatingBonus != 0D) {
                list.add(Component.translatable(MODID + ".recipe_viewer.infiltrator_heating_factor", Component.literal(NCMath.pcDecimalPlaces(heatingBonus, 1)).withStyle(ChatFormatting.WHITE)).withStyle(ChatFormatting.RED));
            }

            double radiation = recipe.getBaseProcessRadiation();
            if (radiation > 0D) {
                list.add(Component.translatable(MODID + ".tooltip.base_process_radiation", RadiationHelper.radsColoredPrefix(radiation, true)).withStyle(ChatFormatting.GOLD));
            }

            return list;
        }

        @Override
        public int getProgressTime() {
            return NCMath.toInt(4D * recipe.getBaseProcessTime(machine_infiltrator_time));
        }
    }

    public static class MultiblockElectrolyzerRecipeViewer extends RecipeViewer<MultiblockElectrolyzerRecipe> {
        public MultiblockElectrolyzerRecipeViewer(MultiblockElectrolyzerRecipe recipe) {
            super(recipe);
        }

        public static final ScreenRectangle electrolyte = new ScreenRectangle(50, 13, 18, 18);

        @Override
        public List<Component> progressTooltips(int x, int y) {
            if (electrolyte.containsPoint(x, y)) return List.of();
            ArrayList<Component> list = new ArrayList<>(3);

            list.add(Component.translatable(MODID + ".tooltip.process_time", Component.literal(UnitHelper.applyTimeUnitShort(recipe.getBaseProcessTime(machine_electrolyzer_time), 3)).withStyle(ChatFormatting.WHITE)).withStyle(ChatFormatting.GREEN));
            list.add(Component.translatable(MODID + ".tooltip.process_power", Component.literal(UnitHelper.prefix(recipe.getBaseProcessPower(machine_electrolyzer_power), 5, "RF/t")).withStyle(ChatFormatting.WHITE)).withStyle(ChatFormatting.LIGHT_PURPLE));

            double radiation = recipe.getBaseProcessRadiation();
            if (radiation > 0D) {
                list.add(Component.translatable(MODID + ".tooltip.base_process_radiation", RadiationHelper.radsColoredPrefix(radiation, true)).withStyle(ChatFormatting.GOLD));
            }

            return list;
        }

        @Override
        public int getProgressTime() {
            return NCMath.toInt(80D * recipe.getBaseProcessTime(machine_electrolyzer_time));
        }
    }

    public static class FissionVentRecipeViewer extends RecipeViewer<FissionHeatingRecipe> {
        public FissionVentRecipeViewer(FissionHeatingRecipe recipe) {
            super(recipe);
        }

        @Override
        public List<Component> progressTooltips(int x, int y) {
            ArrayList<Component> list = new ArrayList<>(1);
            list.add(Component.translatable(MODID + ".recipe_viewer.fission_heating_required", Component.literal(UnitHelper.prefix(recipe.getFissionHeatingHeatPerInputMB(), 5, "H")).withStyle(ChatFormatting.WHITE)).withStyle(ChatFormatting.YELLOW));
            return list;
        }

        @Override
        public int getProgressTime() {
            return NCMath.toInt(recipe.getFissionHeatingHeatPerInputMB() * 4.0);
        }
    }

    public static class MultiblockDistillerRecipeViewer extends RecipeViewer<MultiblockDistillerRecipe> {
        public MultiblockDistillerRecipeViewer(MultiblockDistillerRecipe recipe) {
            super(recipe);
        }

        @Override
        public List<Component> progressTooltips(int x, int y) {
            ArrayList<Component> list = new ArrayList<>(3);

            list.add(Component.translatable(MODID + ".tooltip.process_time", Component.literal(UnitHelper.applyTimeUnitShort(recipe.getBaseProcessTime(machine_distiller_time), 3))).withStyle(ChatFormatting.WHITE).withStyle(ChatFormatting.GREEN));
            list.add(Component.translatable(MODID + ".tooltip.process_power", Component.literal(UnitHelper.prefix(recipe.getBaseProcessPower(machine_distiller_power), 5, "RF/t")).withStyle(ChatFormatting.WHITE)).withStyle(ChatFormatting.LIGHT_PURPLE));
            list.add(Component.translatable(MODID + ".recipe_viewer.distiller_sieve_tray_count", Component.literal(recipe.getDistillerSieveTrayCount() + "").withStyle(ChatFormatting.WHITE)).withStyle(ChatFormatting.GRAY));

            double radiation = recipe.getBaseProcessRadiation();
            if (radiation > 0D) {
                list.add(Component.translatable(MODID + ".tooltip.base_process_radiation", RadiationHelper.radsColoredPrefix(radiation, true)).withStyle(ChatFormatting.GOLD));
            }

            return list;
        }

        @Override
        public int getProgressTime() {
            return NCMath.toInt(20D * recipe.getBaseProcessTime(machine_distiller_time));
        }
    }

    public static class HeatExchangerRecipeViewer extends RecipeViewer<HeatExchangerRecipe> {
        public HeatExchangerRecipeViewer(HeatExchangerRecipe recipe) {
            super(recipe);
        }

        @Override
        public List<Component> progressTooltips(int x, int y) {
            ArrayList<Component> list = new ArrayList<>(3);

            boolean heating = recipe.getHeatExchangerIsHeating();
            list.add(Component.translatable(MODID + ".recipe_viewer.exchanger_fluid_temp_in", Component.literal(recipe.getHeatExchangerInputTemperature() + "K").withStyle(ChatFormatting.WHITE)).withStyle((heating ? ChatFormatting.AQUA : ChatFormatting.RED)));
            list.add(Component.translatable(MODID + ".recipe_viewer.exchanger_fluid_temp_out", Component.literal(recipe.getHeatExchangerOutputTemperature() + "K").withStyle(ChatFormatting.WHITE)).withStyle((heating ? ChatFormatting.RED : ChatFormatting.AQUA)));
            list.add(Component.translatable(MODID + ".recipe_viewer." + (heating ? "exchanger_heating_required" : "exchanger_cooling_required"), Component.literal(UnitHelper.prefix(recipe.getHeatExchangerHeatDifference(), 5, "H")).withStyle(ChatFormatting.WHITE)).withStyle((heating ? ChatFormatting.GOLD : ChatFormatting.BLUE)));

            double flowDirectionBonus = recipe.getHeatExchangerFlowDirectionBonus();
            if (flowDirectionBonus != 0D) {
                int preferredFlowDirection = recipe.getHeatExchangerPreferredFlowDirection();
                list.add(Component.translatable(MODID + ".recipe_viewer." + (preferredFlowDirection == 0 ? "exchanger_horizontal_bonus" : (preferredFlowDirection > 0 ? "exchanger_upward_bonus" : "exchanger_downward_bonus")), Component.literal(NCMath.pcDecimalPlaces(flowDirectionBonus, 1)).withStyle(ChatFormatting.WHITE)).withStyle(ChatFormatting.LIGHT_PURPLE));
            }
            return list;
        }

        @Override
        public int getProgressTime() {
            return NCMath.toInt(400D * recipe.getHeatExchangerHeatDifference());
        }
    }

    public static class FissionIrradiatorRecipeViewer extends RecipeViewer<FissionIrradiatorRecipe> {
        public FissionIrradiatorRecipeViewer(FissionIrradiatorRecipe recipe) {
            super(recipe);
        }

        @Override
        public List<Component> progressTooltips(int x, int y) {
            ArrayList<Component> list = new ArrayList<>(2);

            list.add(Component.translatable(MODID + ".recipe_viewer.irradiator_flux_required", Component.literal(UnitHelper.prefix(recipe.getIrradiatorFluxRequired(), 5, "N")).withStyle(ChatFormatting.WHITE)).withStyle(ChatFormatting.RED));
            double heatPerFlux = recipe.getIrradiatorHeatPerFlux();
            if (heatPerFlux > 0D) {
                list.add(Component.translatable(MODID + ".recipe_viewer.irradiator_heat_per_flux", Component.literal(UnitHelper.prefix(heatPerFlux, 5, "H/N")).withStyle(ChatFormatting.WHITE)).withStyle(ChatFormatting.YELLOW));
            }
            double efficiency = recipe.getIrradiatorProcessEfficiency();
            if (efficiency > 0D) {
                list.add(Component.translatable(MODID + ".recipe_viewer.irradiator_process_efficiency", Component.literal(NCMath.pcDecimalPlaces(efficiency, 1)).withStyle(ChatFormatting.WHITE)).withStyle(ChatFormatting.LIGHT_PURPLE));
            }
            long minFluxPerTick = recipe.getIrradiatorMinFluxPerTick(), maxFluxPerTick = recipe.getIrradiatorMaxFluxPerTick();
            if (minFluxPerTick > 0 || (maxFluxPerTick >= 0 && maxFluxPerTick < Long.MAX_VALUE)) {
                if (minFluxPerTick <= 0) {
                    list.add(Component.translatable(MODID + ".recipe_viewer.irradiator_valid_flux_maximum", Component.literal(minFluxPerTick + " N/t").withStyle(ChatFormatting.WHITE)).withStyle(ChatFormatting.RED));
                } else if (maxFluxPerTick < 0 || maxFluxPerTick == Long.MAX_VALUE) {
                    list.add(Component.translatable(MODID + ".recipe_viewer.irradiator_valid_flux_minimum", Component.literal(maxFluxPerTick + " N/t").withStyle(ChatFormatting.WHITE)).withStyle(ChatFormatting.RED));
                } else {
                    list.add(Component.translatable(MODID + ".recipe_viewer.irradiator_valid_flux_range", Component.literal(minFluxPerTick + " - " + maxFluxPerTick + " N/t").withStyle(ChatFormatting.WHITE)).withStyle(ChatFormatting.RED));
                }
            }
            double radiation = recipe.getIrradiatorBaseProcessRadiation() / RecipeStats.getFissionMaxModeratorLineFlux();
            if (radiation > 0D) {
                list.add(Component.translatable(MODID + ".recipe_viewer.radiation_per_flux", Component.literal(RadiationHelper.getRadiationTextColor(radiation) + UnitHelper.prefix(radiation, 3, "Rad/N")).withStyle(ChatFormatting.WHITE)).withStyle(ChatFormatting.GOLD));
            }
            return list;
        }

        @Override
        public int getProgressTime() {
            return NCMath.toInt(recipe.getIrradiatorFluxRequired() / 80);
        }
    }
}