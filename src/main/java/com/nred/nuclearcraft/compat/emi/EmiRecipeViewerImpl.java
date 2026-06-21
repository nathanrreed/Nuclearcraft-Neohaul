package com.nred.nuclearcraft.compat.emi;

import com.nred.nuclearcraft.compat.emi.part.TankWithAmount;
import com.nred.nuclearcraft.compat.recipe_viewer.RecipeViewerImpl.*;
import com.nred.nuclearcraft.datamap.ElectrolyzerElectrolyteData;
import com.nred.nuclearcraft.recipe.CollectorRecipe;
import com.nred.nuclearcraft.recipe.DecayGeneratorRecipe;
import com.nred.nuclearcraft.recipe.RadiationScrubberRecipe;
import com.nred.nuclearcraft.recipe.exchanger.CondenserRecipe;
import com.nred.nuclearcraft.recipe.exchanger.HeatExchangerRecipe;
import com.nred.nuclearcraft.recipe.fission.*;
import com.nred.nuclearcraft.recipe.machine.*;
import com.nred.nuclearcraft.recipe.processor.*;
import com.nred.nuclearcraft.recipe.turbine.TurbineRecipe;
import com.nred.nuclearcraft.util.NCMath;
import dev.emi.emi.api.neoforge.NeoForgeEmiIngredient;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.crafting.FluidIngredient;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.nred.nuclearcraft.NuclearcraftNeohaul.MODID;
import static com.nred.nuclearcraft.compat.emi.ModEmiPlugin.*;
import static com.nred.nuclearcraft.registration.DataMapTypeRegistration.ELECTROLYZER_ELECTROLYTE_DATA;

public class EmiRecipeViewerImpl {
    public static class EmiTurbineRecipe extends EmiRecipeViewerRecipe {
        public EmiTurbineRecipe(ResourceLocation id, TurbineRecipe recipe) {
            super("turbine", EMI_TURBINE_CATEGORY, id, new TurbineRecipeViewer(recipe));
        }
    }

    public static class EmiFissionVentRecipe extends EmiRecipeViewerRecipe {
        public EmiFissionVentRecipe(ResourceLocation id, FissionHeatingRecipe recipe) {
            super("fission_heating", EMI_VENT_CATEGORY, id, new FissionVentRecipeViewer(recipe));
        }
    }

    public static class EmiFissionIrradiatorRecipe extends EmiRecipeViewerRecipe {
        public EmiFissionIrradiatorRecipe(ResourceLocation id, FissionIrradiatorRecipe recipe) {
            super("fission_irradiator", EMI_IRRADIATOR_CATEGORY, id, new FissionIrradiatorRecipeViewer(recipe));
        }
    }

    public static class EmiGasCoolingRecipe extends EmiRecipeViewerRecipe {
        public EmiGasCoolingRecipe(ResourceLocation id, PebbleFissionCoolerRecipe recipe) {
            super("gas_cooling", EMI_PEBBLE_COOLER_CATEGORY, id, new PebbleFissionCoolerRecipeViewer(recipe));
            this.inputs.add(EmiStack.of(recipe.getCooler()));
        }
    }

    public static class EmiPebbleFissionRecipe extends EmiRecipeViewerRecipe {
        public EmiPebbleFissionRecipe(ResourceLocation id, PebbleFissionRecipe recipe) {
            super("pebble_fission", EMI_PEBBLE_FISSION_CATEGORY, id, new PebbleFissionRecipeViewer(recipe));
        }
    }

    public static class EmiSolidFissionRecipe extends EmiRecipeViewerRecipe {
        public EmiSolidFissionRecipe(ResourceLocation id, SolidFissionRecipe recipe) {
            super("solid_fission", EMI_SOLID_FISSION_CATEGORY, id, new SolidFissionRecipeViewer(recipe));
        }
    }

    public static class EmiSaltCoolingRecipe extends EmiRecipeViewerRecipe {
        public EmiSaltCoolingRecipe(ResourceLocation id, FissionCoolantHeaterRecipe recipe) {
            super("salt_cooling", EMI_SALT_FISSION_CATEGORY, id, new FissionCoolantHeaterRecipeViewer(recipe));
            this.inputs.add(EmiStack.of(recipe.getHeater()));
        }
    }

    public static class EmiFissionEmergencyCoolingRecipe extends EmiRecipeViewerRecipe {
        public EmiFissionEmergencyCoolingRecipe(ResourceLocation id, FissionEmergencyCoolingRecipe recipe) {
            super("fission_emergency_cooling", EMI_EMERGENCY_COOLING_CATEGORY, id, new FissionEmergencyCoolingRecipeViewer(recipe));
        }
    }

    public static class EmiMultiblockDistillerRecipe extends EmiRecipeViewerRecipe {
        public EmiMultiblockDistillerRecipe(ResourceLocation id, MultiblockDistillerRecipe recipe) {
            super("multiblock_distiller", EMI_MULTIBLOCK_DISTILLER_CATEGORY, id, new MultiblockDistillerRecipeViewer(recipe));
        }
    }

    public static class EmiMultiblockElectrolyzerRecipe extends EmiRecipeViewerRecipe {
        private final String group;

        public EmiMultiblockElectrolyzerRecipe(ResourceLocation id, MultiblockElectrolyzerRecipe recipe) {
            super("multiblock_electrolyzer", EMI_MULTIBLOCK_ELECTROLYZER_CATEGORY, id, new MultiblockElectrolyzerRecipeViewer(recipe));
            this.group = recipe.getElectrolyteGroup();
        }

        @Override
        public void addWidgets(WidgetHolder widgets) {
            super.addWidgets(widgets);

            Map<ResourceKey<Fluid>, ElectrolyzerElectrolyteData> electrolytes = BuiltInRegistries.FLUID.getDataMap(ELECTROLYZER_ELECTROLYTE_DATA).entrySet().stream().filter(e -> e.getValue().group().equals(this.group)).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
            List<EmiIngredient> electrolyteIngredients = electrolytes.keySet().stream().map(fluidResourceKey -> NeoForgeEmiIngredient.of(FluidIngredient.single(Objects.requireNonNull(BuiltInRegistries.FLUID.get(fluidResourceKey))))).toList();
            widgets.add(new TankWithAmount(EmiIngredient.of(electrolyteIngredients), MultiblockElectrolyzerRecipeViewer.electrolyte.left() - 1, MultiblockElectrolyzerRecipeViewer.electrolyte.top() - 1) {
                @Override
                public List<ClientTooltipComponent> getTooltip(int mouseX, int mouseY) {
                    int current = (int) (System.currentTimeMillis() / 1000 % this.stack.getEmiStacks().size());
                    Fluid fluid = (Fluid) this.stack.getEmiStacks().get(current).getKey();

                    String name = fluid.getFluidType().getDescriptionId();

                    return List.of(
                            ClientTooltipComponent.create(Component.translatable(MODID + ".recipe_viewer.electrolyte", Component.translatable(name).withStyle(ChatFormatting.WHITE)).withStyle(ChatFormatting.AQUA).getVisualOrderText()),
                            ClientTooltipComponent.create(Component.translatable(MODID + ".recipe_viewer.electrolyte_efficiency", Component.literal(NCMath.pcDecimalPlaces(electrolytes.get(BuiltInRegistries.FLUID.getResourceKey(fluid).get()).efficiency(), 1)).withStyle(ChatFormatting.WHITE)).withStyle(ChatFormatting.LIGHT_PURPLE).getVisualOrderText())
                    );
                }

                @Override
                public boolean mouseClicked(int mouseX, int mouseY, int button) {
                    return false;
                }

                @Override
                public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
                    return false;
                }
            }).drawBack(false);
        }
    }

    public static class EmiMultiblockInfiltratorRecipe extends EmiRecipeViewerRecipe {
        public EmiMultiblockInfiltratorRecipe(ResourceLocation id, MultiblockInfiltratorRecipe recipe) {
            super("multiblock_infiltrator", EMI_MULTIBLOCK_INFILTRATOR_CATEGORY, id, new MultiblockInfiltratorRecipeViewer(recipe));
        }
    }
    public static class EmiMultiblockDecayPoolRecipe extends EmiRecipeViewerRecipe {
        public EmiMultiblockDecayPoolRecipe(ResourceLocation id, MultiblockDecayPoolRecipe recipe) {
            super("multiblock_decay_pool", EMI_MULTIBLOCK_DECAY_POOL_CATEGORY, id, new MultiblockDecayPoolRecipeViewer(recipe));
        }
    }
    public static class EmiDecayPoolHeatSourceRecipe extends EmiRecipeViewerRecipe {
        public EmiDecayPoolHeatSourceRecipe(ResourceLocation id, DecayPoolHeatSourceRecipe recipe) {
            super("decay_pool_heat_source", EMI_DECAY_POOL_HEAT_SOURCE_CATEGORY, id, new DecayPoolHeatSourceRecipeViewer(recipe));
        }
    }

    public static class EmiSaltFissionRecipe extends EmiRecipeViewerRecipe {
        public EmiSaltFissionRecipe(ResourceLocation id, SaltFissionRecipe recipe) {
            super("salt_fission", EMI_SALT_COOLING_CATEGORY, id, new SaltFissionRecipeViewer(recipe));
        }
    }

    public static class EmiCollectorRecipe extends EmiRecipeViewerRecipe {
        public EmiCollectorRecipe(ResourceLocation id, CollectorRecipe recipe) {
            super("collector", EMI_COLLECTOR_CATEGORY, id, new CollectorRecipeViewer(recipe));
            this.inputs.add(EmiStack.of(recipe.getToastSymbol()));
        }
    }

    public static class EmiHeatExchangerRecipe extends EmiRecipeViewerRecipe {
        public EmiHeatExchangerRecipe(ResourceLocation id, HeatExchangerRecipe recipe) {
            super("heat_exchanger", EMI_HEAT_EXCHANGER_CATEGORY, id, new HeatExchangerRecipeViewer(recipe));
        }
    }

    public static class EmiCondenserRecipe extends EmiRecipeViewerRecipe {
        public EmiCondenserRecipe(ResourceLocation id, CondenserRecipe recipe) {
            super("condenser", EMI_CONDENSER_CATEGORY, id, new CondenserRecipeViewer(recipe));
        }
    }

    public static class EmiDecayGeneratorRecipe extends EmiRecipeViewerRecipe {
        public EmiDecayGeneratorRecipe(ResourceLocation id, DecayGeneratorRecipe recipe) {
            super("decay_generator", EMI_DECAY_GENERATOR_CATEGORY, id, new DecayGeneratorRecipeViewer(recipe));
        }
    }

    public static class EmiRadiationScrubberRecipe extends EmiRecipeViewerRecipe {
        public EmiRadiationScrubberRecipe(ResourceLocation id, RadiationScrubberRecipe recipe) {
            super("radiation_scrubber", EMI_RADIATION_SCRUBBER_CATEGORY, id, new RadiationScrubberRecipeViewer(recipe));
        }
    }

    public static class EmiAlloyFurnaceRecipe extends EmiRecipeViewerRecipe {
        public EmiAlloyFurnaceRecipe(ResourceLocation id, AlloyFurnaceRecipe recipe) {
            super("alloy_furnace", EMI_PROCESSOR_CATEGORIES.get("alloy_furnace"), id, new AlloyFurnaceRecipeViewer(recipe));
        }
    }

    public static class EmiAssemblerRecipe extends EmiRecipeViewerRecipe {
        public EmiAssemblerRecipe(ResourceLocation id, AssemblerRecipe recipe) {
            super("assembler", EMI_PROCESSOR_CATEGORIES.get("assembler"), id, new AssemblerRecipeViewer(recipe));
        }
    }

    public static class EmiCentrifugeRecipe extends EmiRecipeViewerRecipe {
        public EmiCentrifugeRecipe(ResourceLocation id, CentrifugeRecipe recipe) {
            super("centrifuge", EMI_PROCESSOR_CATEGORIES.get("centrifuge"), id, new CentrifugeRecipeViewer(recipe));
        }
    }

    public static class EmiChemicalReactorRecipe extends EmiRecipeViewerRecipe {
        public EmiChemicalReactorRecipe(ResourceLocation id, ChemicalReactorRecipe recipe) {
            super("chemical_reactor", EMI_PROCESSOR_CATEGORIES.get("chemical_reactor"), id, new ChemicalReactorRecipeViewer(recipe));
        }
    }

    public static class EmiCrystallizerRecipe extends EmiRecipeViewerRecipe {
        public EmiCrystallizerRecipe(ResourceLocation id, CrystallizerRecipe recipe) {
            super("crystallizer", EMI_PROCESSOR_CATEGORIES.get("crystallizer"), id, new CrystallizerRecipeViewer(recipe));
        }
    }

    public static class EmiDecayHastenerRecipe extends EmiRecipeViewerRecipe {
        public EmiDecayHastenerRecipe(ResourceLocation id, DecayHastenerRecipe recipe) {
            super("decay_hastener", EMI_PROCESSOR_CATEGORIES.get("decay_hastener"), id, new DecayHastenerRecipeViewer(recipe));
        }
    }

    public static class EmiElectricFurnaceRecipe extends EmiRecipeViewerRecipe {
        public EmiElectricFurnaceRecipe(ResourceLocation id, ElectricFurnaceRecipe recipe) {
            super("electric_furnace", EMI_PROCESSOR_CATEGORIES.get("electric_furnace"), id, new ElectricFurnaceRecipeViewer(recipe));
        }
    }

    public static class EmiElectrolyzerRecipe extends EmiRecipeViewerRecipe {
        public EmiElectrolyzerRecipe(ResourceLocation id, ElectrolyzerRecipe recipe) {
            super("electrolyzer", EMI_PROCESSOR_CATEGORIES.get("electrolyzer"), id, new ElectrolyzerRecipeViewer(recipe));
        }
    }

    public static class EmiFluidEnricherRecipe extends EmiRecipeViewerRecipe {
        public EmiFluidEnricherRecipe(ResourceLocation id, FluidEnricherRecipe recipe) {
            super("fluid_enricher", EMI_PROCESSOR_CATEGORIES.get("fluid_enricher"), id, new FluidEnricherRecipeViewer(recipe));
        }
    }

    public static class EmiFluidExtractorRecipe extends EmiRecipeViewerRecipe {
        public EmiFluidExtractorRecipe(ResourceLocation id, FluidExtractorRecipe recipe) {
            super("fluid_extractor", EMI_PROCESSOR_CATEGORIES.get("fluid_extractor"), id, new FluidExtractorRecipeViewer(recipe));
        }
    }

    public static class EmiFluidInfuserRecipe extends EmiRecipeViewerRecipe {
        public EmiFluidInfuserRecipe(ResourceLocation id, FluidInfuserRecipe recipe) {
            super("fluid_infuser", EMI_PROCESSOR_CATEGORIES.get("fluid_infuser"), id, new FluidInfuserRecipeViewer(recipe));
        }
    }

    public static class EmiFluidMixerRecipe extends EmiRecipeViewerRecipe {
        public EmiFluidMixerRecipe(ResourceLocation id, FluidMixerRecipe recipe) {
            super("fluid_mixer", EMI_PROCESSOR_CATEGORIES.get("fluid_mixer"), id, new FluidMixerRecipeViewer(recipe));
        }
    }

    public static class EmiFuelReprocessorRecipe extends EmiRecipeViewerRecipe {
        public EmiFuelReprocessorRecipe(ResourceLocation id, FuelReprocessorRecipe recipe) {
            super("fuel_reprocessor", EMI_PROCESSOR_CATEGORIES.get("fuel_reprocessor"), id, new FuelReprocessorRecipeViewer(recipe));
        }
    }

    public static class EmiIngotFormerRecipe extends EmiRecipeViewerRecipe {
        public EmiIngotFormerRecipe(ResourceLocation id, IngotFormerRecipe recipe) {
            super("ingot_former", EMI_PROCESSOR_CATEGORIES.get("ingot_former"), id, new IngotFormerRecipeViewer(recipe));
        }
    }

    public static class EmiManufactoryRecipe extends EmiRecipeViewerRecipe {
        public EmiManufactoryRecipe(ResourceLocation id, ManufactoryRecipe recipe) {
            super("manufactory", EMI_PROCESSOR_CATEGORIES.get("manufactory"), id, new ManufactoryRecipeViewer(recipe));
        }
    }

    public static class EmiMelterRecipe extends EmiRecipeViewerRecipe {
        public EmiMelterRecipe(ResourceLocation id, MelterRecipe recipe) {
            super("melter", EMI_PROCESSOR_CATEGORIES.get("melter"), id, new MelterRecipeViewer(recipe));
        }
    }

    public static class EmiPressurizerRecipe extends EmiRecipeViewerRecipe {
        public EmiPressurizerRecipe(ResourceLocation id, PressurizerRecipe recipe) {
            super("pressurizer", EMI_PROCESSOR_CATEGORIES.get("pressurizer"), id, new PressurizerRecipeViewer(recipe));
        }
    }

    public static class EmiRockCrusherRecipe extends EmiRecipeViewerRecipe {
        public EmiRockCrusherRecipe(ResourceLocation id, RockCrusherRecipe recipe) {
            super("rock_crusher", EMI_PROCESSOR_CATEGORIES.get("rock_crusher"), id, new RockCrusherRecipeViewer(recipe));
        }
    }

    public static class EmiSeparatorRecipe extends EmiRecipeViewerRecipe {
        public EmiSeparatorRecipe(ResourceLocation id, SeparatorRecipe recipe) {
            super("separator", EMI_PROCESSOR_CATEGORIES.get("separator"), id, new SeparatorRecipeViewer(recipe));
        }
    }

    public static class EmiSupercoolerRecipe extends EmiRecipeViewerRecipe {
        public EmiSupercoolerRecipe(ResourceLocation id, SupercoolerRecipe recipe) {
            super("supercooler", EMI_PROCESSOR_CATEGORIES.get("supercooler"), id, new SupercoolerRecipeViewer(recipe));
        }
    }

    public static class EmiProcessorRecipeDyn extends EmiRecipeViewerRecipe {
        public EmiProcessorRecipeDyn(ResourceLocation id, ProcessorRecipeDyn recipe, String name) {
            super(name, EMI_PROCESSOR_CATEGORIES_DYN.get(name), id, new ProcessorRecipeViewerDyn(recipe, name));
        }
    }
}