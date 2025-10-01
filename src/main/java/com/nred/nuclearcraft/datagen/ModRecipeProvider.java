package com.nred.nuclearcraft.datagen;

import com.nred.nuclearcraft.block.collector.MACHINE_LEVEL;
import com.nred.nuclearcraft.datagen.recipes.*;
import com.nred.nuclearcraft.recipe.base_types.ProcessorRecipeBuilder;
import com.nred.nuclearcraft.recipe.collector.CollectorRecipeBuilder;
import com.nred.nuclearcraft.recipe.processor.*;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.conditions.IConditionBuilder;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static com.nred.nuclearcraft.NuclearcraftNeohaul.MODID;
import static com.nred.nuclearcraft.helpers.Location.cLoc;
import static com.nred.nuclearcraft.info.Names.*;
import static com.nred.nuclearcraft.registration.BlockRegistration.*;
import static com.nred.nuclearcraft.registration.FluidRegistration.FISSION_FUEL_MAP;
import static com.nred.nuclearcraft.registration.FluidRegistration.GAS_MAP;
import static com.nred.nuclearcraft.registration.ItemRegistration.*;
import static net.minecraft.data.recipes.RecipeCategory.*;

public class ModRecipeProvider extends RecipeProvider implements IConditionBuilder {
    public ModRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    private <T extends AbstractCookingRecipe> void smelting(RecipeOutput recipeOutput, RecipeSerializer<T> pSmeltingSerializer, AbstractCookingRecipe.Factory<T> factory, List<Ingredient> ingredients, RecipeCategory pCategory, ItemLike pResult, float pExp, int pSmeltTime, String pGroup, String pRecipeName) {
        for (Ingredient ingredient : ingredients) {
            SimpleCookingRecipeBuilder.generic(
                            ingredient, pCategory, pResult, pExp, pSmeltTime, pSmeltingSerializer, factory
                    ).group(pGroup)
                    .unlockedBy(getHasName(ingredient.getItems()[0].getItem()), has(ingredient.getItems()[0].getItem()))
                    .save(
                            recipeOutput,
                            MODID + ":" + getItemName(pResult) + "_" + pRecipeName + "_" + getItemName(ingredient.getItems()[0].getItem())
                    );
        }
    }

    private void oreSmelting(RecipeOutput recipeOutput, List<Ingredient> ingredients, RecipeCategory pCategory, ItemLike pResult, float pExp, int pSmeltTime, String pGroup, boolean blast) {
        smelting(recipeOutput, RecipeSerializer.SMELTING_RECIPE, SmeltingRecipe::new, ingredients, pCategory, pResult, pExp, pSmeltTime, pGroup, "from_smelting");
        if (blast) {
            smelting(recipeOutput, RecipeSerializer.BLASTING_RECIPE, BlastingRecipe::new, ingredients, pCategory, pResult, pExp, pSmeltTime / 2, pGroup, "from_blasting");
        }
    }

    @Override
    public void buildRecipes(RecipeOutput recipeOutput) {
        for (String ore : ORES) {
            oreSmelting(recipeOutput, List.of(Ingredient.of(RAW_MAP.get(ore)), Ingredient.of(DUST_MAP.get(ore)), Ingredient.of(ORE_MAP.get(ore), ORE_MAP.get(ore + "_deepslate"))), MISC, INGOT_MAP.get(ore), 0.25f, 200, ore, true);
        }

        full9Block(recipeOutput, INGOTS, INGOT_MAP, INGOT_BLOCK_MAP);
        full9Block(recipeOutput, RAWS, RAW_MAP, RAW_BLOCK_MAP);
        full9Item(recipeOutput, NUGGETS, NUGGET_MAP, INGOT_MAP);
        full9Single(recipeOutput, AMERICIUM_MAP.get("243"), FERTILE_ISOTOPE_MAP.get("americium"));
        full9Single(recipeOutput, BERKELIUM_MAP.get("247"), FERTILE_ISOTOPE_MAP.get("berkelium"));
        full9Single(recipeOutput, CALIFORNIUM_MAP.get("252"), FERTILE_ISOTOPE_MAP.get("californium"));
        full9Single(recipeOutput, CURIUM_MAP.get("246"), FERTILE_ISOTOPE_MAP.get("curium"));
        full9Single(recipeOutput, NEPTUNIUM_MAP.get("237"), FERTILE_ISOTOPE_MAP.get("neptunium"));
        full9Single(recipeOutput, PLUTONIUM_MAP.get("242"), FERTILE_ISOTOPE_MAP.get("plutonium"));
        full9Single(recipeOutput, URANIUM_MAP.get("238"), FERTILE_ISOTOPE_MAP.get("uranium"));

        parts(recipeOutput);
        upgrades(recipeOutput);
        compounds(recipeOutput);
        collectors(recipeOutput);
        processors(recipeOutput);
        foods(recipeOutput);
        solar_panels(recipeOutput);
        fuels(recipeOutput);
        turbine(recipeOutput);

        new AlloyFurnaceRecipeProvider(recipeOutput);
        new CentrifugeProvider(recipeOutput);
        new ChemicalReactorProvider(recipeOutput);
        new CrystallizerProvider(recipeOutput);
        new DecayHastenerProvider(recipeOutput);
        new ElectrolyzerProvider(recipeOutput);
        new FluidEnricherProvider(recipeOutput);
        new FluidExtractorProvider(recipeOutput);
        new FluidInfuserProvider(recipeOutput);
        new FluidMixerProvider(recipeOutput);
        new FuelReprocessorProvider(recipeOutput);
        new IngotFormerProvider(recipeOutput);
        new ManufactoryRecipeProvider(recipeOutput);
        new MelterProvider(recipeOutput);
        new PressurizerProvider(recipeOutput);
        new RockCrusherProvider(recipeOutput);
        new SeparatorProvider(recipeOutput);
        new SupercoolerProvider(recipeOutput);
        new TurbineProvider(recipeOutput);
        new SolidFissionProvider(recipeOutput);
        new SaltFissionProvider(recipeOutput);

        ShapedRecipeBuilder.shaped(MISC, PORTABLE_ENDER_CHEST).pattern(" S ").pattern("WEW").pattern("TWT")
                .define('S', Items.STRING).define('W', ItemTags.WOOL).define('E', Items.ENDER_CHEST).define('T', ALLOY_MAP.get("tough"))
                .unlockedBy(getHasName(Items.ENDER_CHEST), has(Items.ENDER_CHEST)).save(recipeOutput);

        ShapelessRecipeBuilder.shapeless(MISC, MUSIC_DISC_MAP.get("music_disc_hyperspace"), 1)
                .requires(Ingredient.of(Tags.Items.MUSIC_DISCS), 1)
                .requires(COMPOUND_MAP.get("dimensional_blend"), 1)
                .unlockedBy(getHasName(COMPOUND_MAP.get("dimensional_blend")), has(COMPOUND_MAP.get("dimensional_blend"))).save(recipeOutput);

        ShapelessRecipeBuilder.shapeless(MISC, MUSIC_DISC_MAP.get("music_disc_money_for_nothing"), 1)
                .requires(Ingredient.of(Tags.Items.MUSIC_DISCS), 1)
                .requires(Ingredient.of(tag(Tags.Items.INGOTS, "silver")), 1)
                .unlockedBy(getHasName(INGOT_MAP.get("silver")), has(INGOT_MAP.get("silver"))).save(recipeOutput);

        ShapelessRecipeBuilder.shapeless(MISC, MUSIC_DISC_MAP.get("music_disc_end_of_the_world"), 1)
                .requires(Ingredient.of(Tags.Items.MUSIC_DISCS), 1)
                .requires(URANIUM_MAP.get("235"), 1)
                .unlockedBy(getHasName(URANIUM_MAP.get("235")), has(URANIUM_MAP.get("235"))).save(recipeOutput);

        ShapelessRecipeBuilder.shapeless(MISC, MUSIC_DISC_MAP.get("music_disc_wanderer"), 1)
                .requires(Ingredient.of(Tags.Items.MUSIC_DISCS), 1)
                .requires(ALLOY_MAP.get("tough"), 1)
                .unlockedBy(getHasName(ALLOY_MAP.get("tough")), has(ALLOY_MAP.get("tough"))).save(recipeOutput);

        ShapedRecipeBuilder.shaped(MISC, LITHIUM_ION_CELL).pattern("HHH").pattern("FLF").pattern("MMM")
                .define('H', ALLOY_MAP.get("hard_carbon")).define('F', ALLOY_MAP.get("ferroboron")).define('L', tag(Tags.Items.INGOTS, "lithium")).define('M', ALLOY_MAP.get("lithium_manganese_dioxide"))
                .unlockedBy(getHasName(Items.ENDER_CHEST), has(Items.ENDER_CHEST)).save(recipeOutput);
    }

    private void fuels(RecipeOutput recipeOutput) {
        fuel(recipeOutput, "233", "238", URANIUM_MAP, FUEL_URANIUM_MAP, 1, "leu_");
        fuel(recipeOutput, "233", "238", URANIUM_MAP, FUEL_URANIUM_MAP, 3, "heu_");
        fuel(recipeOutput, "235", "238", URANIUM_MAP, FUEL_URANIUM_MAP, 1, "leu_");
        fuel(recipeOutput, "235", "238", URANIUM_MAP, FUEL_URANIUM_MAP, 3, "heu_");

        fuel(recipeOutput, "236", "237", NEPTUNIUM_MAP, FUEL_NEPTUNIUM_MAP, 1, "len_");
        fuel(recipeOutput, "236", "237", NEPTUNIUM_MAP, FUEL_NEPTUNIUM_MAP, 3, "hen_");

        fuel(recipeOutput, "239", "242", PLUTONIUM_MAP, FUEL_PLUTONIUM_MAP, 1, "lep_");
        fuel(recipeOutput, "239", "242", PLUTONIUM_MAP, FUEL_PLUTONIUM_MAP, 3, "hep_");
        fuel(recipeOutput, "241", "242", PLUTONIUM_MAP, FUEL_PLUTONIUM_MAP, 1, "lep_");
        fuel(recipeOutput, "241", "242", PLUTONIUM_MAP, FUEL_PLUTONIUM_MAP, 3, "hep_");

        fuel(recipeOutput, "242", "243", AMERICIUM_MAP, FUEL_AMERICIUM_MAP, 1, "lea_");
        fuel(recipeOutput, "242", "243", AMERICIUM_MAP, FUEL_AMERICIUM_MAP, 3, "hea_");

        fuel(recipeOutput, "243", "246", CURIUM_MAP, FUEL_CURIUM_MAP, 1, "lecm_");
        fuel(recipeOutput, "243", "246", CURIUM_MAP, FUEL_CURIUM_MAP, 3, "hecm_");
        fuel(recipeOutput, "245", "246", CURIUM_MAP, FUEL_CURIUM_MAP, 1, "lecm_");
        fuel(recipeOutput, "245", "246", CURIUM_MAP, FUEL_CURIUM_MAP, 3, "hecm_");
        fuel(recipeOutput, "247", "246", CURIUM_MAP, FUEL_CURIUM_MAP, 1, "lecm_");
        fuel(recipeOutput, "247", "246", CURIUM_MAP, FUEL_CURIUM_MAP, 3, "hecm_");

        fuel(recipeOutput, "248", "247", BERKELIUM_MAP, FUEL_BERKELIUM_MAP, 1, "leb_");
        fuel(recipeOutput, "248", "247", BERKELIUM_MAP, FUEL_BERKELIUM_MAP, 3, "heb_");

        fuel(recipeOutput, "249", "252", CALIFORNIUM_MAP, FUEL_CALIFORNIUM_MAP, 1, "lecf_");
        fuel(recipeOutput, "249", "252", CALIFORNIUM_MAP, FUEL_CALIFORNIUM_MAP, 3, "hecf_");
        fuel(recipeOutput, "251", "252", CALIFORNIUM_MAP, FUEL_CALIFORNIUM_MAP, 1, "lecf_");
        fuel(recipeOutput, "251", "252", CALIFORNIUM_MAP, FUEL_CALIFORNIUM_MAP, 3, "hecf_");

        fuel(recipeOutput, "239", "238", PLUTONIUM_MAP, URANIUM_MAP, FUEL_MIXED_MAP, 1, "mix_");
        fuel(recipeOutput, "241", "238", PLUTONIUM_MAP, URANIUM_MAP, FUEL_MIXED_MAP, 1, "mix_");
        for (String suffix : List.of("_za", "_c")) {
            new ProcessorRecipeBuilder(SeparatorRecipe.class, 1, 1).addItemInput(FUEL_MIXED_MAP.get("mix_239" + suffix), 9).addItemResult(URANIUM_MAP.get("238" + suffix), 8).addItemResult(PLUTONIUM_MAP.get("239" + suffix), 1).save(recipeOutput, "mix_239" + suffix);
            new ProcessorRecipeBuilder(SeparatorRecipe.class, 1, 1).addItemInput(FUEL_MIXED_MAP.get("mix_241" + suffix), 9).addItemResult(URANIUM_MAP.get("238" + suffix), 8).addItemResult(PLUTONIUM_MAP.get("241" + suffix), 1).save(recipeOutput, "mix_241" + suffix);
        }

        fuel(recipeOutput, "tbu", "tbu", new HashMap<>(), FUEL_THORIUM_MAP, 1, "");
    }

    private void fuel(RecipeOutput recipeOutput, String minor, String major, HashMap<String, DeferredItem<Item>> typeMap, HashMap<String, DeferredItem<Item>> fuelTypeMap, int amount, String prefix) {
        fuel(recipeOutput, minor, major, typeMap, typeMap, fuelTypeMap, amount, prefix);
    }

    private void fuel(RecipeOutput recipeOutput, String minor, String major, HashMap<String, DeferredItem<Item>> typeMap1, HashMap<String, DeferredItem<Item>> typeMap2, HashMap<String, DeferredItem<Item>> fuelTypeMap, int amount, String prefix) {
        if (!prefix.isEmpty()) {
            for (String suffix : List.of("", "_c", "_ox", "_ni", "_za")) {
                ShapelessRecipeBuilder.shapeless(MISC, fuelTypeMap.get(prefix + minor + suffix)).requires(typeMap1.get(minor), amount).requires(typeMap2.get(major), 9 - amount)
                        .unlockedBy(getHasName(typeMap1.get(minor)), has(typeMap1.get(minor))).save(recipeOutput, prefix + minor + suffix);
            }
        }

        oreSmelting(recipeOutput, List.of(Ingredient.of(fuelTypeMap.get(prefix + minor + "_ni"), fuelTypeMap.get(prefix + minor + "_ox"))), MISC, fuelTypeMap.get(prefix + minor), 0.25f, 200, minor, true);

        new ProcessorRecipeBuilder(SeparatorRecipe.class, 1, 1).addItemInput(fuelTypeMap.get(prefix + minor + "_za"), 1).addItemResult(fuelTypeMap.get(prefix + minor), 1).addItemResult(DUST_MAP.get("zirconium"), 1).save(recipeOutput, prefix + minor + "_from_za");
        new ProcessorRecipeBuilder(SeparatorRecipe.class, 1, 1).addItemInput(fuelTypeMap.get(prefix + minor + "_c"), 1).addItemResult(fuelTypeMap.get(prefix + minor), 1).addItemResult(DUST_MAP.get("graphite"), 1).save(recipeOutput, prefix + minor + "_from_c");

        if (!prefix.isEmpty()) {
            for (String suffix : List.of("", "_ox", "_ni")) {
                new ProcessorRecipeBuilder(SeparatorRecipe.class, 1, 1).addItemInput(fuelTypeMap.get(prefix + minor + suffix), 9).addItemResult(typeMap2.get(major + suffix), 9 - amount).addItemResult(typeMap1.get(minor + suffix), amount).save(recipeOutput, prefix + minor + suffix);
            }
        }

        new ProcessorRecipeBuilder(AlloyFurnaceRecipe.class, 1, 1).addItemInput(fuelTypeMap.get(prefix + minor), 1).addItemInput(Ingredient.of(INGOT_MAP.get("graphite"), DUST_MAP.get("graphite")), 1).addItemResult(fuelTypeMap.get(prefix + minor + "_c"), 1).save(recipeOutput, prefix + minor + "_from_c");
        new ProcessorRecipeBuilder(AlloyFurnaceRecipe.class, 1, 1).addItemInput(fuelTypeMap.get(prefix + minor), 1).addItemInput(Ingredient.of(INGOT_MAP.get("zirconium"), DUST_MAP.get("zirconium")), 1).addItemResult(fuelTypeMap.get(prefix + minor + "_za"), 1).save(recipeOutput, prefix + minor + "_from_za");

        new ProcessorRecipeBuilder(FluidInfuserRecipe.class, 1, 1).addItemInput(fuelTypeMap.get(prefix + minor), 1).addFluidInput(GAS_MAP.get("nitrogen"), 1000).addItemResult(fuelTypeMap.get(prefix + minor + "_ni"), 1).save(recipeOutput, prefix + minor + "_from_ni");
        new ProcessorRecipeBuilder(FluidInfuserRecipe.class, 1, 1).addItemInput(fuelTypeMap.get(prefix + minor), 1).addFluidInput(GAS_MAP.get("oxygen"), 1000).addItemResult(fuelTypeMap.get(prefix + minor + "_ox"), 1).save(recipeOutput, prefix + minor + "_from_ox");

        new ProcessorRecipeBuilder(IngotFormerRecipe.class, 1, 1).addFluidInput(FISSION_FUEL_MAP.get(prefix + minor), 144).addItemResult(fuelTypeMap.get(prefix + minor), 1).save(recipeOutput, prefix + minor);

        new ProcessorRecipeBuilder(MelterRecipe.class, 1, 1).addItemInput(fuelTypeMap.get(prefix + minor), 1).addFluidResult(FISSION_FUEL_MAP.get(prefix + minor), 144).save(recipeOutput, prefix + minor);

        new ProcessorRecipeBuilder(AssemblerRecipe.class, 1, 1).addItemInput(fuelTypeMap.get(prefix + minor + "_c"), 9).addItemInput(DUST_MAP.get("graphite"), 1).addItemInput(PART_MAP.get("pyrolytic_carbon"), 1).addItemInput(ALLOY_MAP.get("silicon_carbide"), 1).addItemResult(fuelTypeMap.get(prefix + minor + "_tr"), 9).save(recipeOutput, prefix + minor);
    }

    private void full9Block(RecipeOutput recipeOutput, List<String> list, HashMap<String, DeferredItem<Item>> itemMap, HashMap<String, DeferredBlock<Block>> resultMap) {
        for (String name : list) {
            ShapelessRecipeBuilder.shapeless(BUILDING_BLOCKS, resultMap.get(name), 1).requires(itemMap.get(name), 9).unlockedBy(getHasName(itemMap.get(name)), has(itemMap.get(name))).save(recipeOutput, getId(resultMap.get(name).asItem(), itemMap.get(name).asItem()));
            ShapelessRecipeBuilder.shapeless(BUILDING_BLOCKS, itemMap.get(name), 9).requires(resultMap.get(name), 1).unlockedBy(getHasName(resultMap.get(name)), has(resultMap.get(name))).save(recipeOutput, getId(itemMap.get(name).asItem(), resultMap.get(name).asItem()));
        }
    }

    private void full9Item(RecipeOutput recipeOutput, List<String> list, HashMap<String, DeferredItem<Item>> itemMap, HashMap<String, DeferredItem<Item>> resultMap) {
        for (String name : list) {
            ShapelessRecipeBuilder.shapeless(MISC, resultMap.get(name), 1).requires(itemMap.get(name), 9).unlockedBy(getHasName(itemMap.get(name)), has(itemMap.get(name))).save(recipeOutput, getId(resultMap.get(name).asItem(), itemMap.get(name).asItem()));
            ShapelessRecipeBuilder.shapeless(MISC, itemMap.get(name), 9).requires(resultMap.get(name), 1).unlockedBy(getHasName(resultMap.get(name)), has(resultMap.get(name))).save(recipeOutput, getId(itemMap.get(name).asItem(), resultMap.get(name).asItem()));
        }
    }

    private void full9Single(RecipeOutput recipeOutput, ItemLike item, ItemLike result) {
        ShapelessRecipeBuilder.shapeless(MISC, result, 1).requires(item, 9).unlockedBy(getHasName(item), has(item)).save(recipeOutput, getId(result.asItem(), item.asItem()));
        ShapelessRecipeBuilder.shapeless(MISC, item, 9).requires(result, 1).unlockedBy(getHasName(result), has(result)).save(recipeOutput, getId(item.asItem(), result.asItem()));
    }

    private String getId(Item result, Item input) {
        return MODID + ":" + ResourceLocation.parse(result.toString()).getPath() + "_from_" + ResourceLocation.parse(input.toString()).getPath();
    }

    private void parts(RecipeOutput recipeOutput) {
        ItemLike result = PART_MAP.get("basic_plating");
        TagKey<Item> tag = tag(Tags.Items.INGOTS, "lead");

        ShapedRecipeBuilder.shaped(MISC, result, 2).pattern("LG").pattern("GL")
                .define('L', tag(Tags.Items.INGOTS, "lead")).define('G', tag(Tags.Items.DUSTS, "graphite"))
                .unlockedBy(getHasName(result), has(tag)).save(recipeOutput, MODID + ":basic_plating_1");
        ShapedRecipeBuilder.shaped(MISC, result, 2).pattern("GL").pattern("LG")
                .define('L', tag).define('G', tag(Tags.Items.DUSTS, "graphite"))
                .unlockedBy(getHasName(Ingredient.of(tag).getItems()[0].getItem()), has(tag)).save(recipeOutput, MODID + ":basic_plating_2");

        ShapedRecipeBuilder.shaped(MISC, PART_MAP.get("advanced_plating")).pattern(" R ").pattern("TPT").pattern(" R ")
                .define('R', Items.REDSTONE).define('P', PART_MAP.get("basic_plating")).define('T', ALLOY_MAP.get("tough"))
                .unlockedBy(getHasName(PART_MAP.get("basic_plating")), has(PART_MAP.get("basic_plating"))).save(recipeOutput);

        ShapedRecipeBuilder.shaped(MISC, PART_MAP.get("du_plating")).pattern("SUS").pattern("UPU").pattern("SUS")
                .define('S', GEM_DUST_MAP.get("sulfur")).define('P', PART_MAP.get("advanced_plating")).define('U', URANIUM_MAP.get("238"))
                .unlockedBy(getHasName(PART_MAP.get("advanced_plating")), has(PART_MAP.get("advanced_plating"))).save(recipeOutput);

        ShapedRecipeBuilder.shaped(MISC, PART_MAP.get("elite_plating")).pattern("CBC").pattern("BPB").pattern("CBC")
                .define('C', COMPOUND_MAP.get("crystal_binder")).define('P', PART_MAP.get("du_plating")).define('B', INGOT_MAP.get("boron"))
                .unlockedBy(getHasName(PART_MAP.get("du_plating")), has(PART_MAP.get("du_plating"))).save(recipeOutput);

        ShapedRecipeBuilder.shaped(MISC, PART_MAP.get("copper_solenoid"), 2).pattern("CC").pattern("II").pattern("CC")
                .define('C', tag(Tags.Items.INGOTS, "copper")).define('I', tag(Tags.Items.INGOTS, "iron"))
                .unlockedBy(getHasName(Items.COPPER_INGOT), has(Items.COPPER_INGOT)).save(recipeOutput);

        ShapedRecipeBuilder.shaped(MISC, PART_MAP.get("magnesium_diboride_solenoid"), 2).pattern("MM").pattern("MM").pattern("MM")
                .define('M', ALLOY_MAP.get("magnesium_diboride"))
                .unlockedBy(getHasName(ALLOY_MAP.get("magnesium_diboride")), has(ALLOY_MAP.get("magnesium_diboride"))).save(recipeOutput);

        ShapedRecipeBuilder.shaped(MISC, PART_MAP.get("servomechanism"), 1).pattern("F F").pattern("RSR").pattern("SCS")
                .define('S', tag(Tags.Items.INGOTS, "steel")).define('C', tag(Tags.Items.INGOTS, "copper")).define('R', Items.REDSTONE).define('F', ALLOY_MAP.get("ferroboron"))
                .unlockedBy(getHasName(ALLOY_MAP.get("ferroboron")), has(ALLOY_MAP.get("ferroboron"))).save(recipeOutput);

        ShapedRecipeBuilder.shaped(MISC, PART_MAP.get("electric_motor"), 1).pattern("SSG").pattern("CCI").pattern("SSG")
                .define('S', tag(Tags.Items.INGOTS, "steel")).define('I', tag(Tags.Items.INGOTS, "iron")).define('G', Items.GOLD_NUGGET).define('C', PART_MAP.get("copper_solenoid"))
                .unlockedBy(getHasName(PART_MAP.get("copper_solenoid")), has(PART_MAP.get("copper_solenoid"))).save(recipeOutput);

        ShapedRecipeBuilder.shaped(MISC, PART_MAP.get("linear_actuator"), 1).pattern("  S").pattern("FP ").pattern("CF ")
                .define('S', tag(Tags.Items.INGOTS, "steel")).define('P', Items.PISTON).define('C', tag(Tags.Items.INGOTS, "copper")).define('F', ALLOY_MAP.get("ferroboron"))
                .unlockedBy(getHasName(ALLOY_MAP.get("ferroboron")), has(ALLOY_MAP.get("ferroboron"))).save(recipeOutput);

        ShapedRecipeBuilder.shaped(MISC, PART_BLOCK_MAP.get("machine_chassis"), 1).pattern("LSL").pattern("STS").pattern("LSL")
                .define('S', tag(Tags.Items.INGOTS, "steel")).define('L', tag(Tags.Items.INGOTS, "lead")).define('T', ALLOY_MAP.get("tough"))
                .unlockedBy(getHasName(ALLOY_MAP.get("tough")), has(ALLOY_MAP.get("tough"))).save(recipeOutput);

        ShapedRecipeBuilder.shaped(MISC, PART_BLOCK_MAP.get("empty_frame"), 1).pattern("PTP").pattern("I I").pattern("PTP")
                .define('T', tag(Tags.Items.INGOTS, "tin")).define('I', tag(Tags.Items.INGOTS, "iron")).define('P', PART_MAP.get("basic_plating"))
                .unlockedBy(getHasName(PART_MAP.get("basic_plating")), has(PART_MAP.get("basic_plating"))).save(recipeOutput);

        ShapedRecipeBuilder.shaped(MISC, PART_BLOCK_MAP.get("steel_chassis"), 1).pattern("STS").pattern("TCT").pattern("STS")
                .define('S', tag(Tags.Items.INGOTS, "steel")).define('C', tag(Tags.Items.INGOTS, "copper")).define('T', ALLOY_MAP.get("tough"))
                .unlockedBy(getHasName(ALLOY_MAP.get("tough")), has(ALLOY_MAP.get("tough"))).save(recipeOutput);

        ShapedRecipeBuilder.shaped(MISC, PART_BLOCK_MAP.get("empty_heat_sink"), 8).pattern("PSP").pattern("T T").pattern("PSP")
                .define('S', tag(Tags.Items.INGOTS, "steel")).define('P', PART_MAP.get("advanced_plating")).define('T', ALLOY_MAP.get("tough"))
                .unlockedBy(getHasName(PART_MAP.get("advanced_plating")), has(PART_MAP.get("advanced_plating"))).save(recipeOutput);
    }

    private void upgrades(RecipeOutput recipeOutput) {
        ShapedRecipeBuilder.shaped(MISC, UPGRADE_MAP.get("speed"), 1).pattern("LRL").pattern("RWR").pattern("LRL")
                .define('L', Items.LAPIS_LAZULI).define('R', Items.REDSTONE).define('W', Items.HEAVY_WEIGHTED_PRESSURE_PLATE)
                .unlockedBy(getHasName(Items.LAPIS_LAZULI), has(Items.LAPIS_LAZULI)).save(recipeOutput);

        ShapedRecipeBuilder.shaped(MISC, UPGRADE_MAP.get("energy"), 1).pattern("OQO").pattern("QWQ").pattern("OQO")
                .define('Q', tag(Tags.Items.DUSTS, "quartz")).define('O', tag(Tags.Items.DUSTS, "obsidian")).define('W', Items.LIGHT_WEIGHTED_PRESSURE_PLATE)
                .unlockedBy(getHasName(Items.LAPIS_LAZULI), has(Items.LAPIS_LAZULI)).save(recipeOutput);
    }

    private void compounds(RecipeOutput recipeOutput) {
        ShapelessRecipeBuilder.shapeless(MISC, COMPOUND_MAP.get("crystal_binder"), 2)
                .requires(ingredient(Tags.Items.DUSTS, "rhodochrosite"), 1)
                .requires(ingredient(Tags.Items.DUSTS, "obsidian"), 1)
                .requires(ingredient(Tags.Items.DUSTS, "magnesium"), 1)
                .requires(ingredient(Tags.Items.DUSTS, "calcium_sulfate"), 1)
                .unlockedBy(getHasName(GEM_DUST_MAP.get("rhodochrosite")), has(GEM_DUST_MAP.get("rhodochrosite"))).save(recipeOutput);

        ShapelessRecipeBuilder.shapeless(MISC, COMPOUND_MAP.get("energetic_blend"), 2)
                .requires(Items.GLOWSTONE_DUST, 1)
                .requires(Items.REDSTONE, 1)
                .unlockedBy(getHasName(Items.GLOWSTONE), has(Items.GLOWSTONE)).save(recipeOutput);

        ShapelessRecipeBuilder.shapeless(MISC, COMPOUND_MAP.get("dimensional_blend"), 2)
                .requires(ingredient(Tags.Items.DUSTS, "end_stone"), 1)
                .requires(ingredient(Tags.Items.DUSTS, "obsidian"), 4)
                .unlockedBy(getHasName(Items.GLOWSTONE), has(Items.GLOWSTONE)).save(recipeOutput);

        ShapelessRecipeBuilder.shapeless(MISC, COMPOUND_MAP.get("c_mn_blend"), 2)
                .requires(ingredient(Tags.Items.DUSTS, "graphite"), 1)
                .requires(ingredient(Tags.Items.DUSTS, "manganese"), 1)
                .unlockedBy(getHasName(Items.GLOWSTONE), has(Items.GLOWSTONE)).save(recipeOutput);
    }

    private void collectors(RecipeOutput recipeOutput) {
        ShapedRecipeBuilder.shaped(MISC, COLLECTOR_MAP.get("cobblestone_generator"), 1).pattern("PTP").pattern("W L").pattern("PTP")
                .define('P', PART_MAP.get("basic_plating")).define('T', tag(Tags.Items.INGOTS, "tin")).define('W', Items.WATER_BUCKET).define('L', Items.LAVA_BUCKET)
                .unlockedBy(getHasName(PART_MAP.get("basic_plating")), has(PART_MAP.get("basic_plating"))).save(recipeOutput, MODID + ":cobblestone_generator_1");
        ShapedRecipeBuilder.shaped(MISC, COLLECTOR_MAP.get("cobblestone_generator"), 1).pattern("PTP").pattern("L W").pattern("PTP")
                .define('P', PART_MAP.get("basic_plating")).define('T', tag(Tags.Items.INGOTS, "tin")).define('W', Items.WATER_BUCKET).define('L', Items.LAVA_BUCKET)
                .unlockedBy(getHasName(PART_MAP.get("basic_plating")), has(PART_MAP.get("basic_plating"))).save(recipeOutput, MODID + ":cobblestone_generator_2");

        ShapedRecipeBuilder.shaped(MISC, COLLECTOR_MAP.get("cobblestone_generator_compact"), 1).pattern("CCC").pattern("CBC").pattern("CCC")
                .define('C', COLLECTOR_MAP.get("cobblestone_generator")).define('B', tag(Tags.Items.INGOTS, "bronze"))
                .unlockedBy(getHasName(COLLECTOR_MAP.get("cobblestone_generator")), has(COLLECTOR_MAP.get("cobblestone_generator"))).save(recipeOutput);

        ShapedRecipeBuilder.shaped(MISC, COLLECTOR_MAP.get("cobblestone_generator_dense"), 1).pattern("CCC").pattern("CGC").pattern("CCC")
                .define('C', COLLECTOR_MAP.get("cobblestone_generator_compact")).define('G', tag(Tags.Items.INGOTS, "gold"))
                .unlockedBy(getHasName(COLLECTOR_MAP.get("cobblestone_generator_compact")), has(COLLECTOR_MAP.get("cobblestone_generator_compact"))).save(recipeOutput);

        ShapedRecipeBuilder.shaped(MISC, COLLECTOR_MAP.get("water_source"), 1).pattern("PTP").pattern("W W").pattern("PTP")
                .define('P', PART_MAP.get("basic_plating")).define('T', tag(Tags.Items.INGOTS, "tin")).define('W', Items.WATER_BUCKET)
                .unlockedBy(getHasName(PART_MAP.get("basic_plating")), has(PART_MAP.get("basic_plating"))).save(recipeOutput);

        ShapedRecipeBuilder.shaped(MISC, COLLECTOR_MAP.get("water_source_compact"), 1).pattern("CCC").pattern("CBC").pattern("CCC")
                .define('C', COLLECTOR_MAP.get("water_source")).define('B', tag(Tags.Items.INGOTS, "bronze"))
                .unlockedBy(getHasName(COLLECTOR_MAP.get("water_source")), has(COLLECTOR_MAP.get("water_source"))).save(recipeOutput);

        ShapedRecipeBuilder.shaped(MISC, COLLECTOR_MAP.get("water_source_dense"), 1).pattern("CCC").pattern("CGC").pattern("CCC")
                .define('C', COLLECTOR_MAP.get("water_source_compact")).define('G', tag(Tags.Items.INGOTS, "gold"))
                .unlockedBy(getHasName(COLLECTOR_MAP.get("water_source_compact")), has(COLLECTOR_MAP.get("water_source_compact"))).save(recipeOutput);

        ShapedRecipeBuilder.shaped(MISC, COLLECTOR_MAP.get("nitrogen_collector"), 1).pattern("PBP").pattern("E E").pattern("PBP")
                .define('P', PART_MAP.get("advanced_plating")).define('B', tag(Tags.Items.INGOTS, "beryllium")).define('E', Items.BUCKET)
                .unlockedBy(getHasName(PART_MAP.get("advanced_plating")), has(PART_MAP.get("advanced_plating"))).save(recipeOutput);

        ShapedRecipeBuilder.shaped(MISC, COLLECTOR_MAP.get("nitrogen_collector_compact"), 1).pattern("CCC").pattern("CBC").pattern("CCC")
                .define('C', COLLECTOR_MAP.get("nitrogen_collector")).define('B', tag(Tags.Items.INGOTS, "bronze"))
                .unlockedBy(getHasName(COLLECTOR_MAP.get("nitrogen_collector")), has(COLLECTOR_MAP.get("nitrogen_collector"))).save(recipeOutput);

        ShapedRecipeBuilder.shaped(MISC, COLLECTOR_MAP.get("nitrogen_collector_dense"), 1).pattern("CCC").pattern("CGC").pattern("CCC")
                .define('C', COLLECTOR_MAP.get("nitrogen_collector_compact")).define('G', tag(Tags.Items.INGOTS, "gold"))
                .unlockedBy(getHasName(COLLECTOR_MAP.get("nitrogen_collector_compact")), has(COLLECTOR_MAP.get("nitrogen_collector_compact"))).save(recipeOutput);

        new CollectorRecipeBuilder(new ItemStack(Items.COBBLESTONE), MACHINE_LEVEL.BASE, 0.125).save(recipeOutput, MODID + ":cobblestone_generator_rate");
        new CollectorRecipeBuilder(new ItemStack(Items.COBBLESTONE), MACHINE_LEVEL.COMPACT, 1.0).save(recipeOutput, MODID + ":cobblestone_generator_compact_rate");
        new CollectorRecipeBuilder(new ItemStack(Items.COBBLESTONE), MACHINE_LEVEL.DENSE, 8.0).save(recipeOutput, MODID + ":cobblestone_generator_dense_rate");

        new CollectorRecipeBuilder(new FluidStack(Fluids.WATER, 10), MACHINE_LEVEL.BASE).save(recipeOutput, MODID + ":water_source_rate");
        new CollectorRecipeBuilder(new FluidStack(Fluids.WATER, 80), MACHINE_LEVEL.COMPACT).save(recipeOutput, MODID + ":water_source_compact_rate");
        new CollectorRecipeBuilder(new FluidStack(Fluids.WATER, 640), MACHINE_LEVEL.DENSE).save(recipeOutput, MODID + ":water_source_dense_rate");

        new CollectorRecipeBuilder(new FluidStack(GAS_MAP.get("nitrogen").still, 5), MACHINE_LEVEL.BASE).save(recipeOutput, MODID + ":nitrogen_collector_rate");
        new CollectorRecipeBuilder(new FluidStack(GAS_MAP.get("nitrogen").still, 40), MACHINE_LEVEL.COMPACT).save(recipeOutput, MODID + ":nitrogen_collector_compact_rate");
        new CollectorRecipeBuilder(new FluidStack(GAS_MAP.get("nitrogen").still, 320), MACHINE_LEVEL.DENSE).save(recipeOutput, MODID + ":nitrogen_collector_dense_rate");
    }

    private void solar_panels(RecipeOutput recipeOutput) {
        ShapedRecipeBuilder.shaped(MISC, SOLAR_MAP.get("solar_panel_basic"), 1).pattern("GQG").pattern("WLW").pattern("CWC")
                .define('G', tag(Tags.Items.DUSTS, "graphite")).define('Q', tag(Tags.Items.DUSTS, "quartz")).define('W', Items.HEAVY_WEIGHTED_PRESSURE_PLATE).define('L', Items.LAPIS_LAZULI).define('C', PART_MAP.get("copper_solenoid"))
                .unlockedBy(getHasName(Items.LAPIS_LAZULI), has(Items.LAPIS_LAZULI)).save(recipeOutput);

        ShapedRecipeBuilder.shaped(MISC, SOLAR_MAP.get("solar_panel_advanced"), 1).pattern("PGP").pattern("SSS").pattern("PCP")
                .define('P', PART_MAP.get("advanced_plating")).define('G', tag(Tags.Items.DUSTS, "graphite")).define('S', SOLAR_MAP.get("solar_panel_basic")).define('C', PART_MAP.get("copper_solenoid"))
                .unlockedBy(getHasName(SOLAR_MAP.get("solar_panel_basic")), has(SOLAR_MAP.get("solar_panel_basic"))).save(recipeOutput);

        ShapedRecipeBuilder.shaped(MISC, SOLAR_MAP.get("solar_panel_du"), 1).pattern("PGP").pattern("SSS").pattern("PMP")
                .define('P', PART_MAP.get("du_plating")).define('G', tag(Tags.Items.DUSTS, "graphite")).define('S', SOLAR_MAP.get("solar_panel_advanced")).define('M', PART_MAP.get("magnesium_diboride_solenoid"))
                .unlockedBy(getHasName(SOLAR_MAP.get("solar_panel_advanced")), has(SOLAR_MAP.get("solar_panel_advanced"))).save(recipeOutput);

        ShapedRecipeBuilder.shaped(MISC, SOLAR_MAP.get("solar_panel_elite"), 1).pattern("PBP").pattern("SSS").pattern("PMP")
                .define('P', PART_MAP.get("elite_plating")).define('B', GEM_MAP.get("boron_nitride")).define('S', SOLAR_MAP.get("solar_panel_du")).define('M', PART_MAP.get("magnesium_diboride_solenoid"))
                .unlockedBy(getHasName(SOLAR_MAP.get("solar_panel_du")), has(SOLAR_MAP.get("solar_panel_du"))).save(recipeOutput);
    }

    private void processors(RecipeOutput recipeOutput) {
        // Alloy Furnace
        ShapedRecipeBuilder.shaped(MISC, PROCESSOR_MAP.get("alloy_furnace"), 1).pattern("PRP").pattern("BFB").pattern("PCP")
                .define('P', PART_MAP.get("basic_plating")).define('R', Items.REDSTONE).define('B', Items.BRICK).define('F', Items.FURNACE).define('C', PART_MAP.get("copper_solenoid"))
                .unlockedBy(getHasName(PART_MAP.get("basic_plating")), has(PART_MAP.get("basic_plating"))).save(recipeOutput);

        // Assembler
        ShapedRecipeBuilder.shaped(MISC, PROCESSOR_MAP.get("assembler"), 1).pattern("PHP").pattern("LML").pattern("PEP")
                .define('P', PART_MAP.get("basic_plating")).define('H', ALLOY_MAP.get("hard_carbon")).define('E', PART_MAP.get("electric_motor")).define('L', PART_MAP.get("linear_actuator")).define('M', PART_BLOCK_MAP.get("machine_chassis"))
                .unlockedBy(getHasName(PART_BLOCK_MAP.get("machine_chassis")), has(PART_BLOCK_MAP.get("machine_chassis"))).save(recipeOutput);

        // Centrifuge
        ShapedRecipeBuilder.shaped(MISC, PROCESSOR_MAP.get("centrifuge"), 1).pattern("PFP").pattern("EME").pattern("PSP")
                .define('P', PART_MAP.get("advanced_plating")).define('F', ALLOY_MAP.get("ferroboron")).define('E', PART_MAP.get("electric_motor")).define('S', PART_MAP.get("servomechanism")).define('M', PART_BLOCK_MAP.get("machine_chassis"))
                .unlockedBy(getHasName(PART_BLOCK_MAP.get("machine_chassis")), has(PART_BLOCK_MAP.get("machine_chassis"))).save(recipeOutput);

        // Chemical Reactor
        ShapedRecipeBuilder.shaped(MISC, PROCESSOR_MAP.get("chemical_reactor"), 1).pattern("PEP").pattern("GMG").pattern("PSP")
                .define('P', PART_MAP.get("advanced_plating")).define('G', Items.GLOWSTONE_DUST).define('E', PART_MAP.get("electric_motor")).define('S', PART_MAP.get("servomechanism")).define('M', PART_BLOCK_MAP.get("machine_chassis"))
                .unlockedBy(getHasName(PART_BLOCK_MAP.get("machine_chassis")), has(PART_BLOCK_MAP.get("machine_chassis"))).save(recipeOutput);

        // Crystallizer
        ShapedRecipeBuilder.shaped(MISC, PROCESSOR_MAP.get("crystallizer"), 1).pattern("PSP").pattern("SMS").pattern("PCP")
                .define('P', PART_MAP.get("advanced_plating")).define('S', PART_MAP.get("copper_solenoid")).define('C', Items.CAULDRON).define('M', PART_BLOCK_MAP.get("machine_chassis"))
                .unlockedBy(getHasName(PART_BLOCK_MAP.get("machine_chassis")), has(PART_BLOCK_MAP.get("machine_chassis"))).save(recipeOutput);

        // Decay Hastener
        ShapedRecipeBuilder.shaped(MISC, PROCESSOR_MAP.get("decay_hastener"), 1).pattern("PGP").pattern("EME").pattern("PCP")
                .define('P', PART_MAP.get("advanced_plating")).define('G', Items.GLOWSTONE_DUST).define('E', Items.ENDER_PEARL).define('C', PART_MAP.get("copper_solenoid")).define('M', PART_BLOCK_MAP.get("machine_chassis"))
                .unlockedBy(getHasName(PART_BLOCK_MAP.get("machine_chassis")), has(PART_BLOCK_MAP.get("machine_chassis"))).save(recipeOutput);

        // Electric Furnace
        ShapedRecipeBuilder.shaped(MISC, PROCESSOR_MAP.get("electric_furnace"), 1).pattern("LIL").pattern("BFB").pattern("LCL")
                .define('L', INGOT_MAP.get("lead")).define('I', Items.IRON_INGOT).define('B', Items.BRICK).define('F', Items.FURNACE).define('C', PART_MAP.get("copper_solenoid"))
                .unlockedBy(getHasName(INGOT_MAP.get("lead")), has(INGOT_MAP.get("lead"))).save(recipeOutput);

        // Electrolyzer
        ShapedRecipeBuilder.shaped(MISC, PROCESSOR_MAP.get("electrolyzer"), 1).pattern("PGP").pattern("CMC").pattern("PEP")
                .define('P', PART_MAP.get("advanced_plating")).define('G', INGOT_MAP.get("graphite")).define('C', PART_MAP.get("copper_solenoid")).define('E', PART_MAP.get("electric_motor")).define('M', PART_BLOCK_MAP.get("machine_chassis"))
                .unlockedBy(getHasName(PART_BLOCK_MAP.get("machine_chassis")), has(PART_BLOCK_MAP.get("machine_chassis"))).save(recipeOutput);

        // Enricher
        ShapedRecipeBuilder.shaped(MISC, PROCESSOR_MAP.get("fluid_enricher"), 1).pattern("PHP").pattern("LML").pattern("PEP")
                .define('P', PART_MAP.get("advanced_plating")).define('H', Items.HOPPER).define('E', PART_MAP.get("electric_motor")).define('L', Items.LAPIS_LAZULI).define('M', PART_BLOCK_MAP.get("machine_chassis"))
                .unlockedBy(getHasName(PART_BLOCK_MAP.get("machine_chassis")), has(PART_BLOCK_MAP.get("machine_chassis"))).save(recipeOutput);

        // Extractor
        ShapedRecipeBuilder.shaped(MISC, PROCESSOR_MAP.get("fluid_extractor"), 1).pattern("PAP").pattern("BMB").pattern("PSP")
                .define('P', PART_MAP.get("advanced_plating")).define('A', tag(Tags.Items.INGOTS, "magnesium")).define('B', Items.BUCKET).define('S', PART_MAP.get("servomechanism")).define('M', PART_BLOCK_MAP.get("machine_chassis"))
                .unlockedBy(getHasName(PART_BLOCK_MAP.get("machine_chassis")), has(PART_BLOCK_MAP.get("machine_chassis"))).save(recipeOutput);

        // Fuel Reprocessor
        ShapedRecipeBuilder.shaped(MISC, PROCESSOR_MAP.get("fuel_reprocessor"), 1).pattern("PBP").pattern("TMT").pattern("PLP")
                .define('P', PART_MAP.get("basic_plating")).define('T', ALLOY_MAP.get("tough")).define('B', INGOT_MAP.get("boron")).define('L', PART_MAP.get("linear_actuator")).define('M', PART_BLOCK_MAP.get("machine_chassis"))
                .unlockedBy(getHasName(PART_BLOCK_MAP.get("machine_chassis")), has(PART_BLOCK_MAP.get("machine_chassis"))).save(recipeOutput);

        // Infuser
        ShapedRecipeBuilder.shaped(MISC, PROCESSOR_MAP.get("fluid_infuser"), 1).pattern("PBP").pattern("GMG").pattern("PSP")
                .define('P', PART_MAP.get("advanced_plating")).define('B', Items.BUCKET).define('G', Items.GOLD_INGOT).define('S', PART_MAP.get("servomechanism")).define('M', PART_BLOCK_MAP.get("machine_chassis"))
                .unlockedBy(getHasName(PART_BLOCK_MAP.get("machine_chassis")), has(PART_BLOCK_MAP.get("machine_chassis"))).save(recipeOutput);

        // Ingot Former
        ShapedRecipeBuilder.shaped(MISC, PROCESSOR_MAP.get("ingot_former"), 1).pattern("PHP").pattern("FMF").pattern("PTP")
                .define('P', PART_MAP.get("basic_plating")).define('T', ALLOY_MAP.get("tough")).define('F', ALLOY_MAP.get("ferroboron")).define('H', Items.HOPPER).define('M', PART_BLOCK_MAP.get("machine_chassis"))
                .unlockedBy(getHasName(PART_BLOCK_MAP.get("machine_chassis")), has(PART_BLOCK_MAP.get("machine_chassis"))).save(recipeOutput);

        // Manufactory
        ShapedRecipeBuilder.shaped(MISC, PROCESSOR_MAP.get("manufactory"), 1).pattern("LRL").pattern("FPF").pattern("LCL")
                .define('L', INGOT_MAP.get("lead")).define('R', Items.REDSTONE).define('F', Items.FLINT).define('P', Items.PISTON).define('C', PART_MAP.get("copper_solenoid"))
                .unlockedBy(getHasName(INGOT_MAP.get("lead")), has(INGOT_MAP.get("lead"))).save(recipeOutput);

        // Melter
        ShapedRecipeBuilder.shaped(MISC, PROCESSOR_MAP.get("melter"), 1).pattern("PNP").pattern("NMN").pattern("PSP")
                .define('P', PART_MAP.get("advanced_plating")).define('N', Items.NETHER_BRICK).define('S', PART_MAP.get("servomechanism")).define('M', PART_BLOCK_MAP.get("machine_chassis"))
                .unlockedBy(getHasName(PART_BLOCK_MAP.get("machine_chassis")), has(PART_BLOCK_MAP.get("machine_chassis"))).save(recipeOutput);

        // Mixer
        ShapedRecipeBuilder.shaped(MISC, PROCESSOR_MAP.get("fluid_mixer"), 1).pattern("PSP").pattern("BMB").pattern("PEP")
                .define('P', PART_MAP.get("basic_plating")).define('S', tag(Tags.Items.INGOTS, "steel")).define('E', PART_MAP.get("electric_motor")).define('B', Items.BUCKET).define('M', PART_BLOCK_MAP.get("machine_chassis"))
                .unlockedBy(getHasName(PART_BLOCK_MAP.get("machine_chassis")), has(PART_BLOCK_MAP.get("machine_chassis"))).save(recipeOutput);

        // Pressurizer
        ShapedRecipeBuilder.shaped(MISC, PROCESSOR_MAP.get("pressurizer"), 1).pattern("PTP").pattern("LML").pattern("PTP")
                .define('P', PART_MAP.get("advanced_plating")).define('T', ALLOY_MAP.get("tough")).define('L', PART_MAP.get("linear_actuator")).define('M', PART_BLOCK_MAP.get("machine_chassis"))
                .unlockedBy(getHasName(PART_BLOCK_MAP.get("machine_chassis")), has(PART_BLOCK_MAP.get("machine_chassis"))).save(recipeOutput);

        // Rock Crusher
        ShapedRecipeBuilder.shaped(MISC, PROCESSOR_MAP.get("rock_crusher"), 1).pattern("PEP").pattern("LML").pattern("PTP")
                .define('P', PART_MAP.get("advanced_plating")).define('T', ALLOY_MAP.get("tough")).define('E', PART_MAP.get("electric_motor")).define('L', PART_MAP.get("linear_actuator")).define('M', PART_BLOCK_MAP.get("machine_chassis"))
                .unlockedBy(getHasName(PART_BLOCK_MAP.get("machine_chassis")), has(PART_BLOCK_MAP.get("machine_chassis"))).save(recipeOutput);

        // Separator
        ShapedRecipeBuilder.shaped(MISC, PROCESSOR_MAP.get("separator"), 1).pattern("PEP").pattern("RMR").pattern("PEP")
                .define('P', PART_MAP.get("basic_plating")).define('R', Items.REDSTONE).define('E', PART_MAP.get("electric_motor")).define('M', PART_BLOCK_MAP.get("machine_chassis"))
                .unlockedBy(getHasName(PART_BLOCK_MAP.get("machine_chassis")), has(PART_BLOCK_MAP.get("machine_chassis"))).save(recipeOutput);

        // Supercooler
        ShapedRecipeBuilder.shaped(MISC, PROCESSOR_MAP.get("supercooler"), 1).pattern("PDP").pattern("HMH").pattern("PSP")
                .define('P', PART_MAP.get("advanced_plating")).define('D', ALLOY_MAP.get("magnesium_diboride")).define('H', ALLOY_MAP.get("hard_carbon")).define('S', PART_MAP.get("servomechanism")).define('M', PART_BLOCK_MAP.get("machine_chassis"))
                .unlockedBy(getHasName(PART_BLOCK_MAP.get("machine_chassis")), has(PART_BLOCK_MAP.get("machine_chassis"))).save(recipeOutput);

        // Universal Bin
        ShapedRecipeBuilder.shaped(MISC, UNIVERSAL_BIN, 1).pattern("PSP").pattern("S S").pattern("PSP")
                .define('P', PART_MAP.get("basic_plating")).define('S', ALLOY_MAP.get("silicon_carbide"))
                .unlockedBy(getHasName(PART_MAP.get("basic_plating")), has(PART_MAP.get("basic_plating"))).save(recipeOutput);
    }

    private void turbine(RecipeOutput recipeOutput) {
        ShapedRecipeBuilder.shaped(MISC, TURBINE_MAP.get("turbine_controller"), 1).pattern("HTH").pattern("TST").pattern("HTH")
                .define('T', ALLOY_MAP.get("tough")).define('H', ALLOY_MAP.get("hsla_steel")).define('S', PART_BLOCK_MAP.get("steel_chassis"))
                .unlockedBy(getHasName(PART_BLOCK_MAP.get("steel_chassis")), has(PART_BLOCK_MAP.get("steel_chassis"))).save(recipeOutput);
        ShapedRecipeBuilder.shaped(MISC, TURBINE_MAP.get("turbine_casing"), 8).pattern(" H ").pattern("HSH").pattern(" H ")
                .define('H', ALLOY_MAP.get("hsla_steel")).define('S', PART_BLOCK_MAP.get("steel_chassis"))
                .unlockedBy(getHasName(PART_BLOCK_MAP.get("steel_chassis")), has(PART_BLOCK_MAP.get("steel_chassis"))).save(recipeOutput);
        ShapelessRecipeBuilder.shapeless(MISC, TURBINE_MAP.get("turbine_casing"), 1).requires(TURBINE_MAP.get("turbine_glass"))
                .unlockedBy(getHasName(TURBINE_MAP.get("turbine_glass")), has(TURBINE_MAP.get("turbine_glass"))).save(recipeOutput, MODID + ":turbine_casing_from_turbine_glass");
        ShapelessRecipeBuilder.shapeless(MISC, TURBINE_MAP.get("turbine_glass"), 1).requires(TURBINE_MAP.get("turbine_casing"))
                .unlockedBy(getHasName(TURBINE_MAP.get("turbine_casing")), has(TURBINE_MAP.get("turbine_casing"))).requires(Tags.Items.GLASS_BLOCKS).save(recipeOutput);

        ShapedRecipeBuilder.shaped(MISC, TURBINE_MAP.get("turbine_rotor_bearing"), 4).pattern("HGH").pattern("GSG").pattern("HGH")
                .define('H', ALLOY_MAP.get("hsla_steel")).define('S', PART_BLOCK_MAP.get("steel_chassis")).define('G', Items.GOLD_NUGGET)
                .unlockedBy(getHasName(PART_BLOCK_MAP.get("steel_chassis")), has(PART_BLOCK_MAP.get("steel_chassis"))).save(recipeOutput);
        ShapedRecipeBuilder.shaped(MISC, TURBINE_MAP.get("turbine_rotor_shaft"), 4).pattern("HHH").pattern("TTT").pattern("HHH")
                .define('T', ALLOY_MAP.get("tough")).define('H', ALLOY_MAP.get("hsla_steel"))
                .unlockedBy(getHasName(ALLOY_MAP.get("hsla_steel")), has(ALLOY_MAP.get("hsla_steel"))).save(recipeOutput);
        ShapedRecipeBuilder.shaped(MISC, TURBINE_MAP.get("turbine_inlet"), 4).pattern("HTH").pattern("MSM").pattern("HTH")
                .define('T', ALLOY_MAP.get("tough")).define('H', ALLOY_MAP.get("hsla_steel")).define('S', PART_BLOCK_MAP.get("steel_chassis")).define('M', PART_MAP.get("servomechanism"))
                .unlockedBy(getHasName(PART_BLOCK_MAP.get("steel_chassis")), has(PART_BLOCK_MAP.get("steel_chassis"))).save(recipeOutput);
        ShapedRecipeBuilder.shaped(MISC, TURBINE_MAP.get("turbine_outlet"), 4).pattern("HHH").pattern("MSM").pattern("HHH")
                .define('H', ALLOY_MAP.get("hsla_steel")).define('S', PART_BLOCK_MAP.get("steel_chassis")).define('M', PART_MAP.get("servomechanism"))
                .unlockedBy(getHasName(PART_BLOCK_MAP.get("steel_chassis")), has(PART_BLOCK_MAP.get("steel_chassis"))).save(recipeOutput);
        ShapedRecipeBuilder.shaped(MISC, TURBINE_MAP.get("turbine_coil_connector"), 4).pattern("HHH").pattern("HTH").pattern("HHH")
                .define('T', ALLOY_MAP.get("tough")).define('H', ALLOY_MAP.get("hsla_steel"))
                .unlockedBy(getHasName(ALLOY_MAP.get("hsla_steel")), has(ALLOY_MAP.get("hsla_steel"))).save(recipeOutput);
        ShapedRecipeBuilder.shaped(MISC, TURBINE_MAP.get("turbine_redstone_port"), 1).pattern("HRH").pattern("TST").pattern("HRH")
                .define('T', Items.REDSTONE_TORCH).define('R', Items.REDSTONE).define('H', ALLOY_MAP.get("hsla_steel")).define('S', PART_BLOCK_MAP.get("steel_chassis"))
                .unlockedBy(getHasName(PART_BLOCK_MAP.get("steel_chassis")), has(PART_BLOCK_MAP.get("steel_chassis"))).save(recipeOutput);
        ShapedRecipeBuilder.shaped(MISC, TURBINE_MAP.get("turbine_computer_port"), 4).pattern("HRH").pattern("CSC").pattern("HWH")
                .define('H', ALLOY_MAP.get("hsla_steel")).define('S', PART_BLOCK_MAP.get("steel_chassis")).define('R', Items.REDSTONE).define('C', Ingredient.of(BuiltInRegistries.ITEM.get(ResourceLocation.parse("computercraft:cable")))).define('W', Ingredient.of(BuiltInRegistries.ITEM.get(ResourceLocation.parse("computercraft:wired_modem"))))
                .unlockedBy(getHasName(PART_BLOCK_MAP.get("steel_chassis")), has(PART_BLOCK_MAP.get("steel_chassis"))).save(recipeOutput);

        // Dynamos
        ShapedRecipeBuilder.shaped(MISC, TURBINE_MAP.get("magnesium_turbine_dynamo_coil"), 2).pattern("MMM").pattern("HTH").pattern("MMM")
                .define('T', ALLOY_MAP.get("tough")).define('H', ALLOY_MAP.get("hsla_steel")).define('M', tag(Tags.Items.INGOTS, "magnesium"))
                .unlockedBy(getHasName(INGOT_MAP.get("magnesium")), has(INGOT_MAP.get("magnesium"))).save(recipeOutput);
        ShapedRecipeBuilder.shaped(MISC, TURBINE_MAP.get("beryllium_turbine_dynamo_coil"), 2).pattern("BBB").pattern("HTH").pattern("BBB")
                .define('T', ALLOY_MAP.get("tough")).define('H', ALLOY_MAP.get("hsla_steel")).define('B', tag(Tags.Items.INGOTS, "beryllium"))
                .unlockedBy(getHasName(INGOT_MAP.get("beryllium")), has(INGOT_MAP.get("beryllium"))).save(recipeOutput);
        ShapedRecipeBuilder.shaped(MISC, TURBINE_MAP.get("aluminum_turbine_dynamo_coil"), 2).pattern("AAA").pattern("HTH").pattern("AAA")
                .define('T', ALLOY_MAP.get("tough")).define('H', ALLOY_MAP.get("hsla_steel")).define('A', tag(Tags.Items.INGOTS, "aluminum"))
                .unlockedBy(getHasName(INGOT_MAP.get("aluminum")), has(INGOT_MAP.get("aluminum"))).save(recipeOutput);
        ShapedRecipeBuilder.shaped(MISC, TURBINE_MAP.get("gold_turbine_dynamo_coil"), 2).pattern("GGG").pattern("HTH").pattern("GGG")
                .define('T', ALLOY_MAP.get("tough")).define('H', ALLOY_MAP.get("hsla_steel")).define('G', tag(Tags.Items.INGOTS, "gold"))
                .unlockedBy(getHasName(Items.GOLD_INGOT), has(Items.GOLD_INGOT)).save(recipeOutput);
        ShapedRecipeBuilder.shaped(MISC, TURBINE_MAP.get("copper_turbine_dynamo_coil"), 2).pattern("CCC").pattern("HTH").pattern("CCC")
                .define('T', ALLOY_MAP.get("tough")).define('H', ALLOY_MAP.get("hsla_steel")).define('C', tag(Tags.Items.INGOTS, "copper"))
                .unlockedBy(getHasName(Items.COPPER_INGOT), has(Items.COPPER_INGOT)).save(recipeOutput);
        ShapedRecipeBuilder.shaped(MISC, TURBINE_MAP.get("silver_turbine_dynamo_coil"), 2).pattern("SSS").pattern("HTH").pattern("SSS")
                .define('T', ALLOY_MAP.get("tough")).define('H', ALLOY_MAP.get("hsla_steel")).define('S', tag(Tags.Items.INGOTS, "silver"))
                .unlockedBy(getHasName(INGOT_MAP.get("silver")), has(INGOT_MAP.get("silver"))).save(recipeOutput);

        // Rotor Blades
        ShapedRecipeBuilder.shaped(MISC, TURBINE_MAP.get("steel_turbine_rotor_blade"), 4).pattern("SHS").pattern("SHS").pattern("SHS")
                .define('S', tag(Tags.Items.INGOTS, "steel")).define('H', ALLOY_MAP.get("hsla_steel"))
                .unlockedBy(getHasName(ALLOY_MAP.get("hsla_steel")), has(ALLOY_MAP.get("hsla_steel"))).save(recipeOutput);
        ShapedRecipeBuilder.shaped(MISC, TURBINE_MAP.get("extreme_alloy_turbine_rotor_blade"), 4).pattern("EHE").pattern("EHE").pattern("EHE")
                .define('E', ALLOY_MAP.get("extreme")).define('H', ALLOY_MAP.get("hsla_steel"))
                .unlockedBy(getHasName(ALLOY_MAP.get("hsla_steel")), has(ALLOY_MAP.get("hsla_steel"))).save(recipeOutput);
        ShapedRecipeBuilder.shaped(MISC, TURBINE_MAP.get("sic_turbine_rotor_blade"), 4).pattern("SHS").pattern("SHS").pattern("SHS")
                .define('S', ALLOY_MAP.get("sic_sic_cmc")).define('H', ALLOY_MAP.get("hsla_steel"))
                .unlockedBy(getHasName(ALLOY_MAP.get("hsla_steel")), has(ALLOY_MAP.get("hsla_steel"))).save(recipeOutput);
        ShapedRecipeBuilder.shaped(MISC, TURBINE_MAP.get("standard_turbine_rotor_stator"), 4).pattern("HH").pattern("HH").pattern("HH")
                .define('H', ALLOY_MAP.get("hsla_steel"))
                .unlockedBy(getHasName(ALLOY_MAP.get("hsla_steel")), has(ALLOY_MAP.get("hsla_steel"))).save(recipeOutput);
    }


    private void foods(RecipeOutput recipeOutput) {
        ShapedRecipeBuilder.shaped(FOOD, FOOD_MAP.get("dominos"), 4).pattern("BBB").pattern("PSC").pattern("MUU")
                .define('B', Items.BREAD).define('P', Items.COOKED_PORKCHOP).define('S', Items.COOKED_BEEF).define('C', Items.COOKED_CHICKEN).define('M', Items.COOKED_MUTTON).define('U', Items.BROWN_MUSHROOM)
                .unlockedBy(getHasName(Items.BREAD), has(Items.BREAD)).save(recipeOutput);

        SimpleCookingRecipeBuilder.smelting(Ingredient.of(Items.COCOA_BEANS), RecipeCategory.FOOD, FOOD_MAP.get("roasted_cocoa_beans"), 0.35F, 200).unlockedBy(getHasName(Items.COCOA_BEANS), has(Items.COCOA_BEANS)).save(recipeOutput, MODID + ":roasted_cocoa_beans_smelting");
        SimpleCookingRecipeBuilder.smoking(Ingredient.of(Items.COCOA_BEANS), RecipeCategory.FOOD, FOOD_MAP.get("roasted_cocoa_beans"), 0.35F, 100).unlockedBy(getHasName(Items.COCOA_BEANS), has(Items.COCOA_BEANS)).save(recipeOutput, MODID + ":roasted_cocoa_beans_smoking");
        SimpleCookingRecipeBuilder.campfireCooking(Ingredient.of(Items.COCOA_BEANS), RecipeCategory.FOOD, FOOD_MAP.get("roasted_cocoa_beans"), 0.35F, 300).unlockedBy(getHasName(Items.COCOA_BEANS), has(Items.COCOA_BEANS)).save(recipeOutput, MODID + ":roasted_cocoa_beans_campfire");

        ShapedRecipeBuilder.shaped(FOOD, FOOD_MAP.get("smore"), 1).pattern("GC").pattern("MG")
                .define('G', FOOD_MAP.get("graham_cracker")).define('C', FOOD_MAP.get("milk_chocolate")).define('M', FOOD_MAP.get("marshmallow"))
                .unlockedBy(getHasName(FOOD_MAP.get("marshmallow")), has(FOOD_MAP.get("marshmallow"))).save(recipeOutput);

        ShapedRecipeBuilder.shaped(FOOD, FOOD_MAP.get("moresmore"), 1).pattern("SC").pattern("MS")
                .define('S', FOOD_MAP.get("smore")).define('C', FOOD_MAP.get("milk_chocolate")).define('M', FOOD_MAP.get("marshmallow"))
                .unlockedBy(getHasName(FOOD_MAP.get("smore")), has(FOOD_MAP.get("smore"))).save(recipeOutput);

        ShapedRecipeBuilder.shaped(FOOD, FOURSMORE, 1).pattern("SC").pattern("MS")
                .define('S', FOOD_MAP.get("moresmore")).define('C', FOOD_MAP.get("milk_chocolate")).define('M', FOOD_MAP.get("marshmallow"))
                .unlockedBy(getHasName(FOOD_MAP.get("moresmore")), has(FOOD_MAP.get("moresmore"))).save(recipeOutput);
    }

    public static TagKey<Item> tag(TagKey<Item> tag, String name) {
        return ItemTags.create(tag.location().withSuffix("/" + name));
    }

    public static TagKey<Fluid> fluidTag(String name) {
        return FluidTags.create(cLoc(name));
    }

    private Ingredient ingredient(TagKey<Item> tag, String name) {
        return Ingredient.of(ItemTags.create(tag.location().withSuffix("/" + name)));
    }
}