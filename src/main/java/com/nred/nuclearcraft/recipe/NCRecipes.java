package com.nred.nuclearcraft.recipe;

import com.nred.nuclearcraft.block_entity.internal.fluid.Tank;
import com.nred.nuclearcraft.handler.BasicRecipeHandler;
import com.nred.nuclearcraft.handler.CoolantHeaterRecipes;
import com.nred.nuclearcraft.handler.SizedChanceFluidIngredient;
import com.nred.nuclearcraft.handler.SizedChanceItemIngredient;
import com.nred.nuclearcraft.recipe.exchanger.CondenserRecipe;
import com.nred.nuclearcraft.recipe.exchanger.HeatExchangerRecipe;
import com.nred.nuclearcraft.recipe.fission.*;
import com.nred.nuclearcraft.recipe.machine.*;
import com.nred.nuclearcraft.recipe.processor.ElectricFurnaceRecipe;
import com.nred.nuclearcraft.recipe.turbine.TurbineRecipe;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.fluids.crafting.FluidIngredient;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.*;

import static com.nred.nuclearcraft.registration.RecipeTypeRegistration.ELECTROLYZER_ELECTROLYTE_RECIPE_TYPE;

public class NCRecipes {
    private static boolean initialized = false;

    private static final Object2ObjectMap<String, BasicRecipeHandler<?>> RECIPE_HANDLER_MAP = new Object2ObjectOpenHashMap<>();

    public NCRecipes() {
        registerRecipes();
    }

    public static void putHandler(BasicRecipeHandler<?> handler) {
        RECIPE_HANDLER_MAP.put(handler.getName(), handler);
    }

    @SuppressWarnings("unchecked")
    public static <T extends BasicRecipeHandler<?>> T getHandler(String name) {
        return (T) RECIPE_HANDLER_MAP.get(name);
    }

    public static Collection<BasicRecipeHandler<?>> getHandlers() {
        return RECIPE_HANDLER_MAP.values();
    }

    public static List<? extends BasicRecipe> getRecipeList(String name) {
        return getHandler(name).getRecipeList();
    }

    public static List<Set<ResourceLocation>> getValidFluids(String name) {
        return getHandler(name).validFluids;
    }

    public void registerRecipes() {
        if (initialized) {
            return;
        }
        initialized = true;

//        RadSources.init(); TODO

        putHandler(new ManufactoryRecipes());
        putHandler(new SeparatorRecipes());
        putHandler(new DecayHastenerRecipes());
        putHandler(new FuelReprocessorRecipes());
        putHandler(new AlloyFurnaceRecipes());
        putHandler(new InfuserRecipes());
        putHandler(new MelterRecipes());
        putHandler(new SupercoolerRecipes());
        putHandler(new ElectrolyzerRecipes());
        putHandler(new AssemblerRecipes());
        putHandler(new IngotFormerRecipes());
        putHandler(new PressurizerRecipes());
        putHandler(new ChemicalReactorRecipes());
        putHandler(new SaltMixerRecipes());
        putHandler(new CrystallizerRecipes());
        putHandler(new EnricherRecipes());
        putHandler(new ExtractorRecipes());
        putHandler(new CentrifugeRecipes());
        putHandler(new RockCrusherRecipes());
        putHandler(new ElectricFurnaceRecipes());
        putHandler(new CollectorRecipes());
        putHandler(new DecayGeneratorRecipes());
        putHandler(new MachineDiaphragmRecipes());
        putHandler(new MachineSieveAssemblyRecipes());
        putHandler(new MultiblockElectrolyzerRecipes());
        putHandler(new ElectrolyzerElectrolyteRecipes());
        putHandler(new ElectrolyzerCathodeRecipes());
        putHandler(new ElectrolyzerAnodeRecipes());
        putHandler(new MultiblockDistillerRecipes());
        putHandler(new MultiblockInfiltratorRecipes());
        putHandler(new InfiltratorPressureFluidRecipes());
        putHandler(new FissionModeratorRecipes());
        putHandler(new FissionReflectorRecipes());
        putHandler(new FissionIrradiatorRecipes());
        putHandler(new PebbleFissionRecipes());
        putHandler(new SolidFissionRecipes());
        putHandler(new FissionHeatingRecipes());
        putHandler(new SaltFissionRecipes());
        putHandler(new CoolantHeaterRecipes());
        putHandler(new FissionEmergencyCoolingRecipes());
        putHandler(new HeatExchangerRecipes());
        putHandler(new CondenserRecipes());
        putHandler(new TurbineRecipes());
        putHandler(new RadiationScrubberRecipes());
        putHandler(new RadiationBlockMutation());
        putHandler(new RadiationBlockPurification());

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

    public static ManufactoryRecipes manufactory;
    public static SeparatorRecipes separator;
    public static DecayHastenerRecipes decay_hastener;
    public static FuelReprocessorRecipes fuel_reprocessor;
    public static AlloyFurnaceRecipes alloy_furnace;
    public static InfuserRecipes infuser;
    public static MelterRecipes melter;
    public static SupercoolerRecipes supercooler;
    public static ElectrolyzerRecipes electrolyzer;
    public static AssemblerRecipes assembler;
    public static IngotFormerRecipes ingot_former;
    public static PressurizerRecipes pressurizer;
    public static ChemicalReactorRecipes chemical_reactor;
    public static SaltMixerRecipes salt_mixer;
    public static CrystallizerRecipes crystallizer;
    public static EnricherRecipes enricher;
    public static ExtractorRecipes extractor;
    public static CentrifugeRecipes centrifuge;
    public static RockCrusherRecipes rock_crusher;
    public static ElectricFurnaceRecipes electric_furnace;
    public static CollectorRecipes collector;
    public static DecayGeneratorRecipes decay_generator;
    public static MachineDiaphragmRecipes machine_diaphragm;
    public static MachineSieveAssemblyRecipes machine_sieve_assembly;
    public static MultiblockElectrolyzerRecipes multiblock_electrolyzer;
    public static ElectrolyzerElectrolyteRecipes electrolyzer_electrolyte;
    public static ElectrolyzerCathodeRecipes electrolyzer_cathode;
    public static ElectrolyzerAnodeRecipes electrolyzer_anode;
    public static MultiblockDistillerRecipes multiblock_distiller;
    public static MultiblockInfiltratorRecipes multiblock_infiltrator;
    public static InfiltratorPressureFluidRecipes infiltrator_pressure_fluid;
    public static FissionModeratorRecipes fission_moderator;
    public static FissionReflectorRecipes fission_reflector;
    public static FissionIrradiatorRecipes fission_irradiator;
    public static PebbleFissionRecipes pebble_fission;
    public static SolidFissionRecipes solid_fission;
    public static FissionHeatingRecipes fission_heating;
    public static SaltFissionRecipes salt_fission;
    public static CoolantHeaterRecipes coolant_heater;
    public static FissionEmergencyCoolingRecipes fission_emergency_cooling;
    public static HeatExchangerRecipes heat_exchanger;
    public static CondenserRecipes condenser;
    public static TurbineRecipes turbine;
    public static RadiationScrubberRecipes radiation_scrubber;
    public static RadiationBlockMutation radiation_block_mutation;
    public static RadiationBlockPurification radiation_block_purification;

    public void registerShortcuts() {
        manufactory = getHandler("manufactory");
        separator = getHandler("separator");
        decay_hastener = getHandler("decay_hastener");
        fuel_reprocessor = getHandler("fuel_reprocessor");
        alloy_furnace = getHandler("alloy_furnace");
        infuser = getHandler("infuser");
        melter = getHandler("melter");
        supercooler = getHandler("supercooler");
        electrolyzer = getHandler("electrolyzer");
        assembler = getHandler("assembler");
        ingot_former = getHandler("ingot_former");
        pressurizer = getHandler("pressurizer");
        chemical_reactor = getHandler("chemical_reactor");
        salt_mixer = getHandler("salt_mixer");
        crystallizer = getHandler("crystallizer");
        enricher = getHandler("enricher");
        extractor = getHandler("extractor");
        centrifuge = getHandler("centrifuge");
        rock_crusher = getHandler("rock_crusher");
        electric_furnace = getHandler("electric_furnace");
        collector = getHandler("collector");
        decay_generator = getHandler("decay_generator");
        machine_diaphragm = getHandler("machine_diaphragm");
        machine_sieve_assembly = getHandler("machine_sieve_assembly");
        multiblock_electrolyzer = getHandler("multiblock_electrolyzer");
        electrolyzer_electrolyte = getHandler("electrolyzer_electrolyte");
        electrolyzer_cathode = getHandler("electrolyzer_cathode");
        electrolyzer_anode = getHandler("electrolyzer_anode");
        multiblock_distiller = getHandler("multiblock_distiller");
        multiblock_infiltrator = getHandler("multiblock_infiltrator");
        infiltrator_pressure_fluid = getHandler("infiltrator_pressure_fluid");
        fission_moderator = getHandler("fission_moderator");
        fission_reflector = getHandler("fission_reflector");
        fission_irradiator = getHandler("fission_irradiator");
        pebble_fission = getHandler("pebble_fission");
        solid_fission = getHandler("solid_fission");
        fission_heating = getHandler("fission_heating");
        salt_fission = getHandler("salt_fission");
        coolant_heater = getHandler("coolant_heater");
        fission_emergency_cooling = getHandler("fission_emergency_cooling");
        heat_exchanger = getHandler("heat_exchanger");
        condenser = getHandler("condenser");
        turbine = getHandler("turbine");
        radiation_scrubber = getHandler("radiation_scrubber");
        radiation_block_mutation = getHandler("radiation_block_mutation");
        radiation_block_purification = getHandler("radiation_block_purification");
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

    public static class ElectrolyzerCathodeRecipes extends BasicRecipeHandler<ElectrolyzerCathodeRecipe> {
        public ElectrolyzerCathodeRecipes() {
            super("electrolyzer_cathode", 1, 0, 0, 0);
        }
    }

    public static class ElectrolyzerAnodeRecipes extends BasicRecipeHandler<ElectrolyzerAnodeRecipe> {
        public ElectrolyzerAnodeRecipes() {
            super("electrolyzer_anode", 1, 0, 0, 0);
        }
    }

    public static class MultiblockDistillerRecipes extends BasicRecipeHandler<MultiblockDistillerRecipe> {
        public MultiblockDistillerRecipes() {
            super("multiblock_distiller", 0, 2, 0, 8);
        }
    }

    public static class MachineDiaphragmRecipes extends BasicRecipeHandler<MachineDiaphragmRecipe> {
        public MachineDiaphragmRecipes() {
            super("machine_diaphragm", 1, 0, 0, 0);
        }
    }

    public static class MachineSieveAssemblyRecipes extends BasicRecipeHandler<MachineSieveAssemblyRecipe> {
        public MachineSieveAssemblyRecipes() {
            super("machine_sieve_assembly", 1, 0, 0, 0);
        }
    }

    public static class InfiltratorPressureFluidRecipes extends BasicRecipeHandler<InfiltratorPressureFluidRecipe> {
        public InfiltratorPressureFluidRecipes() {
            super("infiltrator_pressure_fluid", 0, 1, 0, 0);
        }
    }

    public static class MultiblockInfiltratorRecipes extends BasicRecipeHandler<MultiblockInfiltratorRecipe> {
        public MultiblockInfiltratorRecipes() {
            super("multiblock_infiltrator", 2, 2, 1, 0);
        }
    }

    public static class MultiblockElectrolyzerRecipes extends BasicRecipeHandler<MultiblockElectrolyzerRecipe> {
        public MultiblockElectrolyzerRecipes() {
            super("multiblock_electrolyzer", 2, 2, 4, 4);
        }
    }

    public static class ElectrolyzerElectrolyteRecipes extends BasicRecipeHandler<ElectrolyzerElectrolyteRecipe> {
        public ElectrolyzerElectrolyteRecipes() {
            super("electrolyzer_electrolyte", 0, 1, 0, 0);
        }

        public @Nullable RecipeInfo<ElectrolyzerElectrolyteRecipe> getRecipeInfoFromInputs(Level level, String electrolyte_group, List<Tank> fluidInputs) { // TODO readd caching
            List<SizedChanceFluidIngredient> fluidIngredients = fluidInputs.stream().filter(tank -> !tank.isEmpty()).map(tank -> tank.isEmpty() ? new SizedChanceFluidIngredient(FluidIngredient.empty(), 1) : SizedChanceFluidIngredient.of(tank.getFluid())).toList();

            List<RecipeHolder<ElectrolyzerElectrolyteRecipe>> recipes = level.getRecipeManager().getRecipesFor(ELECTROLYZER_ELECTROLYTE_RECIPE_TYPE.get(), new BasicRecipeInput(List.of(), fluidIngredients), level);
            recipes = recipes.stream().filter(r -> r.value().getGroup().equals(electrolyte_group)).toList();
            assert recipes.size() <= 1; // Make sure there is no overlapping recipes
            ElectrolyzerElectrolyteRecipe recipe = recipes.stream().findFirst().map(RecipeHolder::value).orElse(null);
            if (recipe == null)
                return null;
            return new RecipeInfo<>(recipe, RecipeHelper.matchIngredients(IngredientSorption.INPUT, List.of(), fluidIngredients, List.of(), fluidInputs));
        }
    }

    public static class AlloyFurnaceRecipes extends BasicProcessorRecipeHandler {
        public AlloyFurnaceRecipes() {
            super("alloy_furnace", 2, 0, 1, 0);
        }
    }

    public static class AssemblerRecipes extends BasicProcessorRecipeHandler {
        public AssemblerRecipes() {
            super("assembler", 4, 0, 1, 0);
        }
    }

    public static class CentrifugeRecipes extends BasicProcessorRecipeHandler {
        public CentrifugeRecipes() {
            super("centrifuge", 0, 1, 0, 6);
        }
    }

    public static class ChemicalReactorRecipes extends BasicProcessorRecipeHandler {
        public ChemicalReactorRecipes() {
            super("chemical_reactor", 0, 2, 0, 2);
        }
    }

    public static class CrystallizerRecipes extends BasicProcessorRecipeHandler {
        public CrystallizerRecipes() {
            super("crystallizer", 0, 1, 1, 0);
        }
    }

    public static class DecayHastenerRecipes extends BasicProcessorRecipeHandler {
        public DecayHastenerRecipes() {
            super("decay_hastener", 1, 0, 1, 0);
        }
    }

    public static class ElectricFurnaceRecipes extends BasicProcessorRecipeHandler {
        public ElectricFurnaceRecipes() {
            super("electric_furnace", 1, 0, 1, 0);
        }

        public static ProcessorRecipe getVanillaFurnaceRecipe(SmeltingRecipe recipe) {
            return new ElectricFurnaceRecipe(recipe.getIngredients().stream().map(i -> new SizedChanceItemIngredient(i, 1)).toList(), Collections.singletonList(SizedChanceItemIngredient.of(recipe.getResultItem(null).getItem(), recipe.getResultItem(null).getCount())), Collections.emptyList(), Collections.emptyList(), 1.0, 1.0);
        }

        @Override
        public @NotNull List<ProcessorRecipe> getRecipes(@NotNull RecipeManager recipeManager) {
            if (recipeList.isEmpty()) {
                recipeList = recipeManager.getAllRecipesFor(RecipeType.SMELTING).stream().map(RecipeHolder::value).map(ElectricFurnaceRecipes::getVanillaFurnaceRecipe).toList();
            }
            return recipeList;
        }

        @Override
        public @Nullable ProcessorRecipe getRecipeFromIngredients(Level level, List<SizedChanceItemIngredient> itemIngredients, List<SizedChanceFluidIngredient> fluidIngredients) {
            Optional<ItemStack> item = itemIngredients.stream().flatMap(a -> Arrays.stream(a.getItems())).findFirst();
            if (item.isEmpty()) {
                return null;
            }
            SmeltingRecipe recipe = level.getRecipeManager().getRecipesFor(RecipeType.SMELTING, new SingleRecipeInput(item.get()), level).stream().findFirst().map(RecipeHolder::value).orElse(null);
            return recipe == null ? null : getVanillaFurnaceRecipe(recipe);
        }
    }

    public static class ElectrolyzerRecipes extends BasicProcessorRecipeHandler {
        public ElectrolyzerRecipes() {
            super("electrolyzer", 0, 1, 0, 4);
        }
    }

    public static class EnricherRecipes extends BasicProcessorRecipeHandler {
        public EnricherRecipes() {
            super("enricher", 1, 1, 0, 1);
        }
    }

    public static class ExtractorRecipes extends BasicProcessorRecipeHandler {
        public ExtractorRecipes() {
            super("extractor", 1, 0, 1, 1);
        }
    }

    public static class FuelReprocessorRecipes extends BasicProcessorRecipeHandler {
        public FuelReprocessorRecipes() {
            super("fuel_reprocessor", 1, 0, 8, 0);
        }
    }

    public static class InfuserRecipes extends BasicProcessorRecipeHandler {
        public InfuserRecipes() {
            super("infuser", 1, 1, 1, 0);
        }
    }

    public static class IngotFormerRecipes extends BasicProcessorRecipeHandler {
        public IngotFormerRecipes() {
            super("ingot_former", 0, 1, 1, 0);
        }
    }

    public static class ManufactoryRecipes extends BasicProcessorRecipeHandler {
        public ManufactoryRecipes() {
            super("manufactory", 1, 0, 1, 0);
        }
    }

    public static class MelterRecipes extends BasicProcessorRecipeHandler {
        public MelterRecipes() {
            super("melter", 1, 0, 0, 1);
        }
    }

    public static class PressurizerRecipes extends BasicProcessorRecipeHandler {
        public PressurizerRecipes() {
            super("pressurizer", 1, 0, 1, 0);
        }
    }

    public static class RockCrusherRecipes extends BasicProcessorRecipeHandler {
        public RockCrusherRecipes() {
            super("rock_crusher", 1, 0, 3, 0);
        }
    }

    public static class SaltMixerRecipes extends BasicProcessorRecipeHandler {
        public SaltMixerRecipes() {
            super("salt_mixer", 0, 2, 0, 1);
        }
    }

    public static class SeparatorRecipes extends BasicProcessorRecipeHandler {
        public SeparatorRecipes() {
            super("separator", 1, 0, 2, 0);
        }
    }

    public static class SupercoolerRecipes extends BasicProcessorRecipeHandler {
        public SupercoolerRecipes() {
            super("supercooler", 0, 1, 0, 1);
        }
    }

    public abstract static class BasicProcessorRecipeHandler extends BasicRecipeHandler<ProcessorRecipe> {
        public BasicProcessorRecipeHandler(@Nonnull String name, int itemInputSize, int fluidInputSize, int itemOutputSize, int fluidOutputSize) {
            super(name, itemInputSize, fluidInputSize, itemOutputSize, fluidOutputSize);
        }
    }

    public static class CondenserRecipes extends BasicRecipeHandler<CondenserRecipe> {
        public CondenserRecipes() {
            super("condenser", 0, 1, 0, 1);
        }
    }

    public static class HeatExchangerRecipes extends BasicRecipeHandler<HeatExchangerRecipe> {
        public HeatExchangerRecipes() {
            super("heat_exchanger", 0, 1, 0, 1);
        }
    }

    public static class RadiationScrubberRecipes extends BasicRecipeHandler<RadiationScrubberRecipe> {
        public RadiationScrubberRecipes() {
            super("radiation_scrubber", 1, 1, 1, 1);
        }
    }

    public static class RadiationBlockMutation extends BasicRecipeHandler<RadiationBlockMutationRecipe> {
        public RadiationBlockMutation() {
            super("radiation_block_mutation", 1, 0, 1, 0);
        }
    }

    public static class RadiationBlockPurification extends BasicRecipeHandler<RadiationBlockPurificationRecipe> {
        public RadiationBlockPurification() {
            super("radiation_block_purification", 1, 0, 1, 0);
        }
    }
}