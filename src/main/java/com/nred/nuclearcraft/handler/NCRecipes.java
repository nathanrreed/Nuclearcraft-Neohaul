package com.nred.nuclearcraft.handler;

import com.nred.nuclearcraft.recipe.BasicRecipe;
import com.nred.nuclearcraft.recipe.CollectorRecipe;
import com.nred.nuclearcraft.recipe.DecayGeneratorRecipe;
import com.nred.nuclearcraft.recipe.fission.*;
import com.nred.nuclearcraft.recipe.turbine.TurbineRecipe;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.material.Fluid;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public class NCRecipes {
    private static boolean initialized = false;

    private static final Object2ObjectMap<String, BasicRecipeHandler> RECIPE_HANDLER_MAP = new Object2ObjectOpenHashMap<>();

    public NCRecipes() {
        registerRecipes();
    }

    public static void putHandler(BasicRecipeHandler handler) {
        RECIPE_HANDLER_MAP.put(handler.getName(), handler);
    }

    @SuppressWarnings("unchecked")
    public static <T extends BasicRecipeHandler> T getHandler(String name) {
        return (T) RECIPE_HANDLER_MAP.get(name);
    }

    public static Collection<BasicRecipeHandler> getHandlers() {
        return RECIPE_HANDLER_MAP.values();
    }

    public static List<? extends BasicRecipe> getRecipeList(String name) {
        return getHandler(name).recipeList;
    }

    public static List<Set<ResourceKey<Fluid>>> getValidFluids(String name) {
        return getHandler(name).validFluids;
    }

    public void registerRecipes() {
        if (initialized) {
            return;
        }
        initialized = true;

//        RadSources.init(); TODO

//        putHandler(new ManufactoryRecipes());
//        putHandler(new SeparatorRecipes());
//        putHandler(new DecayHastenerRecipes());
//        putHandler(new FuelReprocessorRecipes());
//        putHandler(new AlloyFurnaceRecipes());
//        putHandler(new InfuserRecipes());
//        putHandler(new MelterRecipes());
//        putHandler(new SupercoolerRecipes());
//        putHandler(new ElectrolyzerRecipes());
//        putHandler(new AssemblerRecipes());
//        putHandler(new IngotFormerRecipes());
//        putHandler(new PressurizerRecipes());
//        putHandler(new ChemicalReactorRecipes());
//        putHandler(new SaltMixerRecipes());
//        putHandler(new CrystallizerRecipes());
//        putHandler(new EnricherRecipes());
//        putHandler(new ExtractorRecipes());
//        putHandler(new CentrifugeRecipes());
//        putHandler(new RockCrusherRecipes());
//        putHandler(new ElectricFurnaceRecipes());
        putHandler(new CollectorRecipes());
        putHandler(new DecayGeneratorRecipes());
//        putHandler(new MachineDiaphragmRecipes());
//        putHandler(new MachineSieveAssemblyRecipes());
//        putHandler(new MultiblockElectrolyzerRecipes());
//        putHandler(new ElectrolyzerCathodeRecipes());
//        putHandler(new ElectrolyzerAnodeRecipes());
//        putHandler(new MultiblockDistillerRecipes());
//        putHandler(new MultiblockInfiltratorRecipes());
//        putHandler(new InfiltratorPressureFluidRecipes());
        putHandler(new FissionModeratorRecipes());
        putHandler(new FissionReflectorRecipes());
        putHandler(new FissionIrradiatorRecipes());
        putHandler(new PebbleFissionRecipes());
        putHandler(new SolidFissionRecipes());
        putHandler(new FissionHeatingRecipes());
        putHandler(new SaltFissionRecipes());
        putHandler(new CoolantHeaterRecipes());
        putHandler(new FissionEmergencyCoolingRecipes());
//        putHandler(new HeatExchangerRecipes());
//        putHandler(new CondenserRecipes());
//        putHandler(new CondenserDissipationFluidRecipes());
        putHandler(new TurbineRecipes());
//        putHandler(new RadiationScrubberRecipes());
//        putHandler(new RadiationBlockMutation());
//        putHandler(new RadiationBlockPurification());

        registerShortcuts();
//
//        CraftingRecipeHandler.registerCraftingRecipes();
//        FurnaceRecipeHandler.registerFurnaceRecipes();
//        GameRegistry.registerFuelHandler(new FurnaceFuelHandler());
//
//        for (RegistrationInfo info : CTRegistration.INFO_LIST) {
//            info.recipeInit();
//        }
    }

    //    public static ManufactoryRecipes manufactory;
//    public static SeparatorRecipes separator;
//    public static DecayHastenerRecipes decay_hastener;
//    public static FuelReprocessorRecipes fuel_reprocessor;
//    public static AlloyFurnaceRecipes alloy_furnace;
//    public static InfuserRecipes infuser;
//    public static MelterRecipes melter;
//    public static SupercoolerRecipes supercooler;
//    public static ElectrolyzerRecipes electrolyzer;
//    public static AssemblerRecipes assembler;
//    public static IngotFormerRecipes ingot_former;
//    public static PressurizerRecipes pressurizer;
//    public static ChemicalReactorRecipes chemical_reactor;
//    public static SaltMixerRecipes salt_mixer;
//    public static CrystallizerRecipes crystallizer;
//    public static EnricherRecipes enricher;
//    public static ExtractorRecipes extractor;
//    public static CentrifugeRecipes centrifuge;
//    public static RockCrusherRecipes rock_crusher;
//    public static ElectricFurnaceRecipes electric_furnace;
    public static CollectorRecipes collector;
    public static DecayGeneratorRecipes decay_generator;
    //    public static MachineDiaphragmRecipes machine_diaphragm;
//    public static MachineSieveAssemblyRecipes machine_sieve_assembly;
//    public static MultiblockElectrolyzerRecipes multiblock_electrolyzer;
//    public static ElectrolyzerCathodeRecipes electrolyzer_cathode;
//    public static ElectrolyzerAnodeRecipes electrolyzer_anode;
//    public static MultiblockDistillerRecipes multiblock_distiller;
//    public static MultiblockInfiltratorRecipes multiblock_infiltrator;
//    public static InfiltratorPressureFluidRecipes infiltrator_pressure_fluid;
    public static FissionModeratorRecipes fission_moderator;
    public static FissionReflectorRecipes fission_reflector;
    public static FissionIrradiatorRecipes fission_irradiator;
    public static PebbleFissionRecipes pebble_fission;
    public static SolidFissionRecipes solid_fission;
    public static FissionHeatingRecipes fission_heating;
    public static SaltFissionRecipes salt_fission;
    public static CoolantHeaterRecipes coolant_heater;
    public static FissionEmergencyCoolingRecipes fission_emergency_cooling;
    //    public static HeatExchangerRecipes heat_exchanger;
//    public static CondenserRecipes condenser;
//    public static CondenserDissipationFluidRecipes condenser_dissipation_fluid;
    public static TurbineRecipes turbine;
//    public static RadiationScrubberRecipes radiation_scrubber;
//    public static RadiationBlockMutation radiation_block_mutation;
//    public static RadiationBlockPurification radiation_block_purification;

    public void registerShortcuts() {
//        manufactory = getHandler("manufactory");
//        separator = getHandler("separator");
//        decay_hastener = getHandler("decay_hastener");
//        fuel_reprocessor = getHandler("fuel_reprocessor");
//        alloy_furnace = getHandler("alloy_furnace");
//        infuser = getHandler("infuser");
//        melter = getHandler("melter");
//        supercooler = getHandler("supercooler");
//        electrolyzer = getHandler("electrolyzer");
//        assembler = getHandler("assembler");
//        ingot_former = getHandler("ingot_former");
//        pressurizer = getHandler("pressurizer");
//        chemical_reactor = getHandler("chemical_reactor");
//        salt_mixer = getHandler("salt_mixer");
//        crystallizer = getHandler("crystallizer");
//        enricher = getHandler("enricher");
//        extractor = getHandler("extractor");
//        centrifuge = getHandler("centrifuge");
//        rock_crusher = getHandler("rock_crusher");
//        electric_furnace = getHandler("electric_furnace");
        collector = getHandler("collector");
        decay_generator = getHandler("decay_generator");
//        machine_diaphragm = getHandler("machine_diaphragm");
//        machine_sieve_assembly = getHandler("machine_sieve_assembly");
//        multiblock_electrolyzer = getHandler("multiblock_electrolyzer");
//        electrolyzer_cathode = getHandler("electrolyzer_cathode");
//        electrolyzer_anode = getHandler("electrolyzer_anode");
//        multiblock_distiller = getHandler("multiblock_distiller");
//        multiblock_infiltrator = getHandler("multiblock_infiltrator");
//        infiltrator_pressure_fluid = getHandler("infiltrator_pressure_fluid");
        fission_moderator = getHandler("fission_moderator");
        fission_reflector = getHandler("fission_reflector");
        fission_irradiator = getHandler("fission_irradiator");
        pebble_fission = getHandler("pebble_fission");
        solid_fission = getHandler("solid_fission");
        fission_heating = getHandler("fission_heating");
        salt_fission = getHandler("salt_fission");
        coolant_heater = getHandler("coolant_heater");
        fission_emergency_cooling = getHandler("fission_emergency_cooling");
//        heat_exchanger = getHandler("heat_exchanger");
//        condenser = getHandler("condenser");
//        condenser_dissipation_fluid = getHandler("condenser_dissipation_fluid");
        turbine = getHandler("turbine");
//        radiation_scrubber = getHandler("radiation_scrubber");
//        radiation_block_mutation = getHandler("radiation_block_mutation");
//        radiation_block_purification = getHandler("radiation_block_purification");
    }

    public static void init(RecipeManager recipeManager) {
        for (BasicRecipeHandler<?> handler : getHandlers()) {
            handler.init(recipeManager);
        }
    }

//    public static void postInit() {
//        for (BasicRecipeHandler handler : getHandlers()) {
//            handler.postInit();
//        }
//    }

    public static void clearRecipes() {
        for (BasicRecipeHandler<?> handler : getHandlers()) {
            handler.clearRecipes();
        }
    }

    public static class FissionModeratorRecipes extends BasicRecipeHandler<FissionModeratorRecipe> {
        public FissionModeratorRecipes() {
            super("fission_moderator", 1, 0, 0, 0);
        }
    }

    public static class FissionIrradiatorRecipes extends BasicRecipeHandler<FissionIrradiatorRecipe> {
        public FissionIrradiatorRecipes() {
            super("fission_irradiator", 1, 0, 1, 0);
        }
    }

    public static class FissionHeatingRecipes extends BasicRecipeHandler<FissionHeatingRecipe> {
        public FissionHeatingRecipes() {
            super("fission_heating", 0, 1, 0, 1);
        }
    }

    public static class FissionEmergencyCoolingRecipes extends BasicRecipeHandler<FissionEmergencyCoolingRecipe> {
        public FissionEmergencyCoolingRecipes() {
            super("fission_emergency_cooling", 0, 1, 0, 1);
        }
    }

    public static class FissionReflectorRecipes extends BasicRecipeHandler<FissionReflectorRecipe> {
        public FissionReflectorRecipes() {
            super("fission_reflector", 1, 0, 0, 0);
        }
    }

    public static class SolidFissionRecipes extends BasicRecipeHandler<SolidFissionRecipe> {
        public SolidFissionRecipes() {
            super("solid_fission", 1, 0, 1, 0);
        }
    }

    public static class SaltFissionRecipes extends BasicRecipeHandler<SaltFissionRecipe> {
        public SaltFissionRecipes() {
            super("salt_fission", 0, 1, 0, 1);
        }
    }

    public static class PebbleFissionRecipes extends BasicRecipeHandler<PebbleFissionRecipe> {
        public PebbleFissionRecipes() {
            super("pebble_fission", 1, 0, 1, 0);
        }
    }

    public static class TurbineRecipes extends BasicRecipeHandler<TurbineRecipe> {
        public TurbineRecipes() {
            super("turbine", 0, 1, 0, 1);
        }
    }

    public static class DecayGeneratorRecipes extends BasicRecipeHandler<DecayGeneratorRecipe> {
        public DecayGeneratorRecipes() {
            super("decay_generator", 1, 0, 1, 0);
        }
    }

    public static class CollectorRecipes extends BasicRecipeHandler<CollectorRecipe> {
        public CollectorRecipes() {
            super("collector", 1, 0, 1, 1);
        }
    }

//    public class ElectrolyzerCathodeRecipes extends BasicRecipeHandler<ElectrolyzerCathodeRecipe> { TODO
//        public ElectrolyzerCathodeRecipes() {
//            super("electrolyzer_cathode", 1, 0, 0, 0);
//        }
//    }
//
//    public class MachineDiaphragmRecipes extends BasicRecipeHandler<MachineDiaphragmRecipe> {
//        public MachineDiaphragmRecipes() {
//            super("machine_diaphragm", 1, 0, 0, 0);
//        }
//    }
//
//    public class MachineSieveAssemblyRecipes extends BasicRecipeHandler<MachineSieveAssemblyRecipe> {
//        public MachineSieveAssemblyRecipes() {
//            super("machine_sieve_assembly", 1, 0, 0, 0);
//        }
//    }
}