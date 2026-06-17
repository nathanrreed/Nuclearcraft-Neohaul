package com.nred.nuclearcraft.compat.jei;

import com.nred.nuclearcraft.compat.recipe_viewer.RecipeViewerImpl.*;
import com.nred.nuclearcraft.datamap.ElectrolyzerElectrolyteData;
import com.nred.nuclearcraft.helpers.GuiHelper;
import com.nred.nuclearcraft.recipe.CollectorRecipe;
import com.nred.nuclearcraft.recipe.DecayGeneratorRecipe;
import com.nred.nuclearcraft.recipe.RadiationScrubberRecipe;
import com.nred.nuclearcraft.recipe.exchanger.CondenserRecipe;
import com.nred.nuclearcraft.recipe.exchanger.HeatExchangerRecipe;
import com.nred.nuclearcraft.recipe.fission.*;
import com.nred.nuclearcraft.recipe.machine.MultiblockDistillerRecipe;
import com.nred.nuclearcraft.recipe.machine.MultiblockElectrolyzerRecipe;
import com.nred.nuclearcraft.recipe.machine.MultiblockInfiltratorRecipe;
import com.nred.nuclearcraft.recipe.processor.*;
import com.nred.nuclearcraft.recipe.turbine.TurbineRecipe;
import com.nred.nuclearcraft.util.NCMath;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.material.Fluid;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.nred.nuclearcraft.NuclearcraftNeohaul.MODID;
import static com.nred.nuclearcraft.registration.DataMapTypeRegistration.ELECTROLYZER_ELECTROLYTE_DATA;
import static com.nred.nuclearcraft.registration.RecipeTypeRegistration.*;

public class JeiRecipeViewerImpl {
    public static class JeiTurbineCategory extends JeiRecipeViewerCategory<TurbineRecipe> {
        public JeiTurbineCategory(IGuiHelper helper) {
            super(helper, "turbine", TurbineRecipeViewer.class, TURBINE_RECIPE_TYPE.get());
        }
    }

    public static class JeiFissionVentCategory extends JeiRecipeViewerCategory<FissionHeatingRecipe> {
        public JeiFissionVentCategory(IGuiHelper helper) {
            super(helper, "fission_heating", FissionVentRecipeViewer.class, FISSION_HEATING_RECIPE_TYPE.get());
        }
    }

    public static class JeiFissionIrradiatorCategory extends JeiRecipeViewerCategory<FissionIrradiatorRecipe> {
        public JeiFissionIrradiatorCategory(IGuiHelper helper) {
            super(helper, "fission_irradiator", FissionIrradiatorRecipeViewer.class, FISSION_IRRADIATOR_RECIPE_TYPE.get());
        }
    }

    public static class JeiGasCoolingCategory extends JeiRecipeViewerCategory<PebbleFissionCoolerRecipe> {
        public JeiGasCoolingCategory(IGuiHelper helper) {
            super(helper, "gas_cooling", PebbleFissionCoolerRecipeViewer.class, COOLER_RECIPE_TYPE.get());
        }

        @Override
        public void setRecipe(IRecipeLayoutBuilder builder, RecipeHolder<PebbleFissionCoolerRecipe> holder, IFocusGroup focuses) {
            super.setRecipe(builder, holder, focuses);
            int[] iconStackXY = categoryInfo.getItemInputStackXY().getFirst();
            builder.addInputSlot(iconStackXY[0] - categoryInfo.getRecipeViewerBackgroundX(), iconStackXY[1] - categoryInfo.getRecipeViewerBackgroundY()).addItemStack(holder.value().getCooler());
        }
    }

    public static class JeiPebbleFissionCategory extends JeiRecipeViewerCategory<PebbleFissionRecipe> {
        public JeiPebbleFissionCategory(IGuiHelper helper) {
            super(helper, "pebble_fission", PebbleFissionRecipeViewer.class, PEBBLE_FISSION_RECIPE_TYPE.get());
        }
    }

    public static class JeiSolidFissionCategory extends JeiRecipeViewerCategory<SolidFissionRecipe> {
        public JeiSolidFissionCategory(IGuiHelper helper) {
            super(helper, "solid_fission", SolidFissionRecipeViewer.class, SOLID_FISSION_RECIPE_TYPE.get());
        }
    }

    public static class JeiSaltCoolingCategory extends JeiRecipeViewerCategory<FissionCoolantHeaterRecipe> {
        public JeiSaltCoolingCategory(IGuiHelper helper) {
            super(helper, "salt_cooling", FissionCoolantHeaterRecipeViewer.class, COOLANT_HEATER_RECIPE_TYPE.get());
        }

        @Override
        public void setRecipe(IRecipeLayoutBuilder builder, RecipeHolder<FissionCoolantHeaterRecipe> holder, IFocusGroup focuses) {
            super.setRecipe(builder, holder, focuses);
            int[] iconStackXY = categoryInfo.getItemInputStackXY().getFirst();
            builder.addInputSlot(iconStackXY[0] - categoryInfo.getRecipeViewerBackgroundX(), iconStackXY[1] - categoryInfo.getRecipeViewerBackgroundY()).addItemStack(holder.value().getHeater());
        }
    }

    public static class JeiSaltFissionCategory extends JeiRecipeViewerCategory<SaltFissionRecipe> {
        public JeiSaltFissionCategory(IGuiHelper helper) {
            super(helper, "salt_fission", SaltFissionRecipeViewer.class, SALT_FISSION_RECIPE_TYPE.get());
        }
    }

    public static class JeiFissionEmergencyCoolingCategory extends JeiRecipeViewerCategory<FissionEmergencyCoolingRecipe> {
        public JeiFissionEmergencyCoolingCategory(IGuiHelper helper) {
            super(helper, "fission_emergency_cooling", FissionEmergencyCoolingRecipeViewer.class, FISSION_EMERGENCY_COOLING_RECIPE_TYPE.get());
        }
    }

    public static class JeiMultiblockDistillerCategory extends JeiRecipeViewerCategory<MultiblockDistillerRecipe> {
        public JeiMultiblockDistillerCategory(IGuiHelper helper) {
            super(helper, "multiblock_distiller", MultiblockDistillerRecipeViewer.class, MULTIBLOCK_DISTILLER_RECIPE_TYPE.get());
        }
    }

    public static class JeiMultiblockElectrolyzerCategory extends JeiRecipeViewerCategory<MultiblockElectrolyzerRecipe> {
        protected final int electrolyteX;
        protected final int electrolyteY;
        protected final int electrolyteW;
        protected final int electrolyteH;

        public JeiMultiblockElectrolyzerCategory(IGuiHelper helper) {
            super(helper, "multiblock_electrolyzer", MultiblockElectrolyzerRecipeViewer.class, MULTIBLOCK_ELECTROLYZER_RECIPE_TYPE.get());

            electrolyteX = 64 - categoryInfo.getRecipeViewerBackgroundX();
            electrolyteY = 41 - categoryInfo.getRecipeViewerBackgroundY();
            electrolyteW = 16;
            electrolyteH = 16;
        }

        @Override
        public void draw(RecipeHolder<MultiblockElectrolyzerRecipe> holder, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
            super.draw(holder, recipeSlotsView, guiGraphics, mouseX, mouseY);

            List<Map.Entry<ResourceKey<Fluid>, ElectrolyzerElectrolyteData>> electrolytes = BuiltInRegistries.FLUID.getDataMap(ELECTROLYZER_ELECTROLYTE_DATA).entrySet().stream().filter(e -> e.getValue().group().equals(holder.value().getElectrolyteGroup())).toList();

            int x1 = electrolyteX - 1;
            int x2 = electrolyteX + electrolyteW;
            int y2 = electrolyteY + electrolyteH + 1;

            boolean display_tooltips = mouseX > x1 && mouseY > electrolyteY && mouseX < x2 && mouseY < y2;
            if (!electrolytes.isEmpty()) {
                Map.Entry<ResourceKey<Fluid>, ElectrolyzerElectrolyteData> electrolyte = getEnumerationElement(electrolytes, 1000L);
                Fluid current_fluid = BuiltInRegistries.FLUID.get(electrolyte.getKey());
                GuiHelper.renderGuiFluid(guiGraphics, current_fluid, electrolyteX, electrolyteY, electrolyteW, electrolyteH, 255);
                if (display_tooltips) {
                    guiGraphics.renderTooltip(font, List.of(
                            Component.translatable(MODID + ".recipe_viewer.electrolyte", Component.translatable(current_fluid.getFluidType().getDescriptionId()).withStyle(ChatFormatting.WHITE)).withStyle(ChatFormatting.AQUA),
                            Component.translatable(MODID + ".recipe_viewer.electrolyte_efficiency", Component.literal(NCMath.pcDecimalPlaces(electrolyte.getValue().efficiency(), 1)).withStyle(ChatFormatting.WHITE)).withStyle(ChatFormatting.LIGHT_PURPLE)
                    ), Optional.empty(), (int) mouseX, (int) mouseY);
                }
            } else {
                if (display_tooltips) {
                    guiGraphics.renderTooltip(font, Component.translatable(MODID + ".recipe_viewer.electrolyte", ChatFormatting.WHITE + "null").withStyle(ChatFormatting.AQUA), (int) mouseX, (int) mouseY);
                }
            }
        }
    }

    public static class JeiMultiblockInfiltratorCategory extends JeiRecipeViewerCategory<MultiblockInfiltratorRecipe> {
        public JeiMultiblockInfiltratorCategory(IGuiHelper helper) {
            super(helper, "multiblock_infiltrator", MultiblockInfiltratorRecipeViewer.class, MULTIBLOCK_INFILTRATOR_RECIPE_TYPE.get());
        }
    }

    public static class JeiCollectorCategory extends JeiRecipeViewerCategory<CollectorRecipe> {
        public JeiCollectorCategory(IGuiHelper helper) {
            super(helper, "collector", CollectorRecipeViewer.class, COLLECTOR_RECIPE_TYPE.get());
        }

        @Override
        public void setRecipe(IRecipeLayoutBuilder builder, RecipeHolder<CollectorRecipe> holder, IFocusGroup focuses) {
            super.setRecipe(builder, holder, focuses);
            int[] iconStackXY = categoryInfo.getItemInputStackXY().getFirst();
            builder.addInputSlot(iconStackXY[0] - categoryInfo.getRecipeViewerBackgroundX(), iconStackXY[1] - categoryInfo.getRecipeViewerBackgroundY()).addItemStack(holder.value().getToastSymbol());
        }
    }

    public static class JeiHeatExchangerCategory extends JeiRecipeViewerCategory<HeatExchangerRecipe> {
        public JeiHeatExchangerCategory(IGuiHelper helper) {
            super(helper, "heat_exchanger", HeatExchangerRecipeViewer.class, HEAT_EXCHANGER_RECIPE_TYPE.get());
        }
    }

    public static class JeiCondenserCategory extends JeiRecipeViewerCategory<CondenserRecipe> {
        public JeiCondenserCategory(IGuiHelper helper) {
            super(helper, "condenser", CondenserRecipeViewer.class, CONDENSER_RECIPE_TYPE.get());
        }
    }

    public static class JeiDecayGeneratorCategory extends JeiRecipeViewerCategory<DecayGeneratorRecipe> {
        public JeiDecayGeneratorCategory(IGuiHelper helper) {
            super(helper, "decay_generator", DecayGeneratorRecipeViewer.class, DECAY_GENERATOR_RECIPE_TYPE.get());
        }
    }

    public static class JeiRadiationScrubberCategory extends JeiRecipeViewerCategory<RadiationScrubberRecipe> {
        public JeiRadiationScrubberCategory(IGuiHelper helper) {
            super(helper, "radiation_scrubber", RadiationScrubberRecipeViewer.class, RADIATION_SCRUBBER_RECIPE_TYPE.get());
        }
    }

    public static class JeiAlloyFurnaceCategory extends JeiRecipeViewerCategory<AlloyFurnaceRecipe> {
        public JeiAlloyFurnaceCategory(IGuiHelper helper) {
            super(helper, "alloy_furnace", AlloyFurnaceRecipeViewer.class, ALLOY_FURNACE_RECIPE_TYPE.get());
        }
    }

    public static class JeiAssemblerCategory extends JeiRecipeViewerCategory<AssemblerRecipe> {
        public JeiAssemblerCategory(IGuiHelper helper) {
            super(helper, "assembler", AssemblerRecipeViewer.class, ASSEMBLER_RECIPE_TYPE.get());
        }
    }

    public static class JeiCentrifugeCategory extends JeiRecipeViewerCategory<CentrifugeRecipe> {
        public JeiCentrifugeCategory(IGuiHelper helper) {
            super(helper, "centrifuge", CentrifugeRecipeViewer.class, CENTRIFUGE_RECIPE_TYPE.get());
        }
    }

    public static class JeiChemicalReactorCategory extends JeiRecipeViewerCategory<ChemicalReactorRecipe> {
        public JeiChemicalReactorCategory(IGuiHelper helper) {
            super(helper, "chemical_reactor", ChemicalReactorRecipeViewer.class, CHEMICAL_REACTOR_RECIPE_TYPE.get());
        }
    }

    public static class JeiCrystallizerCategory extends JeiRecipeViewerCategory<CrystallizerRecipe> {
        public JeiCrystallizerCategory(IGuiHelper helper) {
            super(helper, "crystallizer", CrystallizerRecipeViewer.class, CRYSTALLIZER_RECIPE_TYPE.get());
        }
    }

    public static class JeiDecayHastenerCategory extends JeiRecipeViewerCategory<DecayHastenerRecipe> {
        public JeiDecayHastenerCategory(IGuiHelper helper) {
            super(helper, "decay_hastener", DecayHastenerRecipeViewer.class, DECAY_HASTENER_RECIPE_TYPE.get());
        }
    }

    public static class JeiElectricFurnaceCategory extends JeiRecipeViewerCategory<ElectricFurnaceRecipe> {
        public JeiElectricFurnaceCategory(IGuiHelper helper) {
            super(helper, "electric_furnace", ElectricFurnaceRecipeViewer.class, ELECTRIC_FURNACE_RECIPE_TYPE.get());
        }
    }

    public static class JeiElectrolyzerCategory extends JeiRecipeViewerCategory<ElectrolyzerRecipe> {
        public JeiElectrolyzerCategory(IGuiHelper helper) {
            super(helper, "electrolyzer", ElectrolyzerRecipeViewer.class, ELECTROLYZER_RECIPE_TYPE.get());
        }
    }

    public static class JeiFluidEnricherCategory extends JeiRecipeViewerCategory<FluidEnricherRecipe> {
        public JeiFluidEnricherCategory(IGuiHelper helper) {
            super(helper, "fluid_enricher", FluidEnricherRecipeViewer.class, FLUID_ENRICHER_RECIPE_TYPE.get());
        }
    }

    public static class JeiFluidExtractorCategory extends JeiRecipeViewerCategory<FluidExtractorRecipe> {
        public JeiFluidExtractorCategory(IGuiHelper helper) {
            super(helper, "fluid_extractor", FluidExtractorRecipeViewer.class, FLUID_EXTRACTOR_RECIPE_TYPE.get());
        }
    }

    public static class JeiFluidInfuserCategory extends JeiRecipeViewerCategory<FluidInfuserRecipe> {
        public JeiFluidInfuserCategory(IGuiHelper helper) {
            super(helper, "fluid_infuser", FluidInfuserRecipeViewer.class, FLUID_INFUSER_RECIPE_TYPE.get());
        }
    }

    public static class JeiFluidMixerCategory extends JeiRecipeViewerCategory<FluidMixerRecipe> {
        public JeiFluidMixerCategory(IGuiHelper helper) {
            super(helper, "fluid_mixer", FluidMixerRecipeViewer.class, FLUID_MIXER_RECIPE_TYPE.get());
        }
    }

    public static class JeiFuelReprocessorCategory extends JeiRecipeViewerCategory<FuelReprocessorRecipe> {
        public JeiFuelReprocessorCategory(IGuiHelper helper) {
            super(helper, "fuel_reprocessor", FuelReprocessorRecipeViewer.class, FUEL_REPROCESSOR_RECIPE_TYPE.get());
        }
    }

    public static class JeiIngotFormerCategory extends JeiRecipeViewerCategory<IngotFormerRecipe> {
        public JeiIngotFormerCategory(IGuiHelper helper) {
            super(helper, "ingot_former", IngotFormerRecipeViewer.class, INGOT_FORMER_RECIPE_TYPE.get());
        }
    }

    public static class JeiManufactoryCategory extends JeiRecipeViewerCategory<ManufactoryRecipe> {
        public JeiManufactoryCategory(IGuiHelper helper) {
            super(helper, "manufactory", ManufactoryRecipeViewer.class, MANUFACTORY_RECIPE_TYPE.get());
        }
    }

    public static class JeiMelterCategory extends JeiRecipeViewerCategory<MelterRecipe> {
        public JeiMelterCategory(IGuiHelper helper) {
            super(helper, "melter", MelterRecipeViewer.class, MELTER_RECIPE_TYPE.get());
        }
    }

    public static class JeiPressurizerCategory extends JeiRecipeViewerCategory<PressurizerRecipe> {
        public JeiPressurizerCategory(IGuiHelper helper) {
            super(helper, "pressurizer", PressurizerRecipeViewer.class, PRESSURIZER_RECIPE_TYPE.get());
        }
    }

    public static class JeiRockCrusherCategory extends JeiRecipeViewerCategory<RockCrusherRecipe> {
        public JeiRockCrusherCategory(IGuiHelper helper) {
            super(helper, "rock_crusher", RockCrusherRecipeViewer.class, ROCK_CRUSHER_RECIPE_TYPE.get());
        }
    }

    public static class JeiSeparatorCategory extends JeiRecipeViewerCategory<SeparatorRecipe> {
        public JeiSeparatorCategory(IGuiHelper helper) {
            super(helper, "separator", SeparatorRecipeViewer.class, SEPARATOR_RECIPE_TYPE.get());
        }
    }

    public static class JeiSupercoolerCategory extends JeiRecipeViewerCategory<SupercoolerRecipe> {
        public JeiSupercoolerCategory(IGuiHelper helper) {
            super(helper, "supercooler", SupercoolerRecipeViewer.class, SUPERCOOLER_RECIPE_TYPE.get());
        }
    }

    public static class JeiProcessorCategoryDyn extends JeiRecipeViewerCategory<ProcessorRecipeDyn> {
        public JeiProcessorCategoryDyn(IGuiHelper helper, String name) {
            super(helper, name, ProcessorRecipeViewerDyn.class, PROCESSOR_RECIPE_DYN_TYPES.get(name).get());
        }
    }
}