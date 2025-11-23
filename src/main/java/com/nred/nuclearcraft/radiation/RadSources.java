package com.nred.nuclearcraft.radiation;

import com.nred.nuclearcraft.recipe.RecipeHelper;
import com.nred.nuclearcraft.util.RegistryHelper;
import it.unimi.dsi.fastutil.ints.Int2DoubleMap;
import it.unimi.dsi.fastutil.ints.Int2DoubleOpenHashMap;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.objects.Object2DoubleMap;
import it.unimi.dsi.fastutil.objects.Object2DoubleOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import it.zerono.mods.zerocore.lib.item.ItemHelper;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.common.Tags;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.nred.nuclearcraft.config.NCConfig.*;
import static com.nred.nuclearcraft.helpers.Location.ncLoc;
import static com.nred.nuclearcraft.registration.BlockRegistration.*;
import static com.nred.nuclearcraft.registration.ItemRegistration.HEAVY_RADIATION_SHIELDING;
import static com.nred.nuclearcraft.registration.ItemRegistration.PART_MAP;

public class RadSources {
    public static final ObjectSet<TagKey<Item>> ORE_BLACKLIST = new ObjectOpenHashSet<>();
    public static final IntOpenHashSet STACK_BLACKLIST = new IntOpenHashSet();
    public static final ObjectSet<String> FLUID_BLACKLIST = new ObjectOpenHashSet<>();

    public static final Object2DoubleMap<TagKey<Item>> TAG_MAP = new Object2DoubleOpenHashMap<>();
    public static final Int2DoubleMap STACK_MAP = new Int2DoubleOpenHashMap();
    public static final Object2DoubleMap<String> FLUID_MAP = new Object2DoubleOpenHashMap<>();

    public static final Int2DoubleMap FOOD_RAD_MAP = new Int2DoubleOpenHashMap();
    public static final Int2DoubleMap FOOD_RESISTANCE_MAP = new Int2DoubleOpenHashMap();

    public static final List<Runnable> RUNNABLES = new ArrayList<>();

    public static void addToOreBlacklist(TagKey<Item> ore) {
        ORE_BLACKLIST.add(ore);
    }

    public static void addToStackBlacklist(ItemStack stack) {
        if (stack != null) {
            STACK_BLACKLIST.add(RecipeHelper.pack(stack));
        }
    }

    public static void addToFluidBlacklist(String fluidName) {
        FLUID_BLACKLIST.add(fluidName);
    }

    public static void addToTagMap(TagKey<Item> tag, double radiation) {
        if (ORE_BLACKLIST.contains(tag)) {
            return;
        }
        TAG_MAP.put(tag, radiation);
    }

    public static void addToStackMap(ItemStack stack, double radiation) {
        int packed = RecipeHelper.pack(stack);
        if (packed == 0 || STACK_BLACKLIST.contains(packed)) {
            return;
        }
        STACK_MAP.put(packed, radiation);
    }

    public static void addToFluidMap(String fluidName, double radiation) {
        if (FLUID_BLACKLIST.contains(fluidName)) {
            return;
        }
        FLUID_MAP.put(fluidName, radiation);

        Fluid fluid = BuiltInRegistries.FLUID.get(ResourceLocation.parse(fluidName));
        if (fluid != Fluids.EMPTY) {
            Block fluidBlock = BuiltInRegistries.BLOCK.get(ResourceLocation.parse(fluidName));

            if (fluidBlock != Blocks.AIR) {
                addToStackMap(new ItemStack(fluidBlock), radiation);
            }
        }
    }

    public static void addToFoodMaps(ItemStack stack, double radiation, double resistance) {
        int packed = RecipeHelper.pack(stack);
        if (packed == 0) {
            return;
        }
        FOOD_RAD_MAP.put(packed, radiation);
        FOOD_RESISTANCE_MAP.put(packed, resistance);
    }

    private static final Object2DoubleMap<TagKey<Item>> PREFIX_MULTIPLIER_MAP = new Object2DoubleOpenHashMap<>();

    private static final double FLUID_MULTIPLIER = 125D / 18D;

    public static void addMaterialPrefixMultiplier(TagKey<Item> prefix, double multiplier) {
        PREFIX_MULTIPLIER_MAP.put(prefix, multiplier);
    }

    static {
        for (TagKey<Item> tag : List.of(Tags.Items.INGOTS, Tags.Items.DUSTS, Tags.Items.GEMS)) {
            addMaterialPrefixMultiplier(tag, 1.0);
        }
        for (TagKey<Item> tag : List.of(Tags.Items.NUGGETS)) {
            addMaterialPrefixMultiplier(tag, 1D / 9.0);
        }
        for (TagKey<Item> tag : List.of(Tags.Items.ORES)) {
            addMaterialPrefixMultiplier(tag, 1D / 4.0);
        }
        for (TagKey<Item> tag : List.of(Tags.Items.RODS)) {
            addMaterialPrefixMultiplier(tag, 1D / 2.0);
        }
        for (TagKey<Item> tag : List.of(Tags.Items.STORAGE_BLOCKS, Tags.Items.RAW_MATERIALS)) {
            addMaterialPrefixMultiplier(tag, 9.0);
        }
    }

    public static TagKey<Item> tag(TagKey<Item> base, String suffix) {
        return ItemTags.create(base.location().withSuffix("/" + suffix));
    }

    public static void init() {
        for (String ore : radiation_ores_blacklist) {
            addToOreBlacklist(tag(Tags.Items.ORES, ore));
        }
        for (String item : radiation_items_blacklist) {
            addToStackBlacklist(RegistryHelper.itemStackFromRegistry(item));
        }
        for (String block : radiation_blocks_blacklist) {
            addToStackBlacklist(RegistryHelper.blockStackFromRegistry(block));
        }
        for (String fluid : radiation_fluids_blacklist) {
            addToFluidBlacklist(fluid);
        }

        putMaterial(BISMUTH, "bismuth");
        putMaterial(RADIUM, "radium");
        putMaterial(POLONIUM, "polonium");
        putMaterial(TBP, "tbp");
        putMaterial(PROTACTINIUM_233, "protactinium_233");
        putMaterial(STRONTIUM_90, "strontium_90");
        putMaterial(RUTHENIUM_106, "ruthenium_106");
        putMaterial(CAESIUM_137, "cesium_137");
        putMaterial(CAESIUM_137, "caesium_137");
        putMaterial(PROMETHIUM_147, "promethium_147");
        putMaterial(EUROPIUM_155, "europium_155");

        putMaterial(THORIUM, "thorium");
        putMaterial(URANIUM, "uranium", "yellorium");
        putMaterial(PLUTONIUM, "plutonium", "blutonium");
        putMaterial(URANIUM_238, "cyanite");

        putIsotope(URANIUM_233, "uranium_233");
        putIsotope(URANIUM_235, "uranium_235");
        putIsotope(URANIUM_238, "uranium_238");

        putIsotope(NEPTUNIUM_236, "neptunium_236");
        putIsotope(NEPTUNIUM_237, "neptunium_237");

        putIsotope(PLUTONIUM_238, "plutonium_238");
        putIsotope(PLUTONIUM_239, "plutonium_239");
        putIsotope(PLUTONIUM_241, "plutonium_241");
        putIsotope(PLUTONIUM_242, "plutonium_242");

        putIsotope(AMERICIUM_241, "americium_241");
        putIsotope(AMERICIUM_242, "americium_242");
        putIsotope(AMERICIUM_243, "americium_243");

        putIsotope(CURIUM_243, "curium_243");
        putIsotope(CURIUM_245, "curium_245");
        putIsotope(CURIUM_246, "curium_246");
        putIsotope(CURIUM_247, "curium_247");

        putIsotope(BERKELIUM_247, "berkelium_247");
        putIsotope(BERKELIUM_248, "berkelium_248");

        putIsotope(CALIFORNIUM_249, "californium_249");
        putIsotope(CALIFORNIUM_250, "californium_250");
        putIsotope(CALIFORNIUM_251, "californium_251");
        putIsotope(CALIFORNIUM_252, "californium_252");

        putFuel(TBU, DEPLETED_TBU, "tbu");

        putFuel(LEU_233, DEPLETED_LEU_233, "leu_233");
        putFuel(HEU_233, DEPLETED_HEU_233, "heu_233");
        putFuel(LEU_235, DEPLETED_LEU_235, "leu_235");
        putFuel(HEU_235, DEPLETED_HEU_235, "heu_235");

        putFuel(LEN_236, DEPLETED_LEN_236, "len_236");
        putFuel(HEN_236, DEPLETED_HEN_236, "hen_236");

        putFuel(LEP_239, DEPLETED_LEP_239, "lep_239");
        putFuel(HEP_239, DEPLETED_HEP_239, "hep_239");
        putFuel(LEP_241, DEPLETED_LEP_241, "lep_241");
        putFuel(HEP_241, DEPLETED_HEP_241, "hep_241");

        putFuel(MIX_239, DEPLETED_MIX_239, "mix_239");
        putFuel(MIX_241, DEPLETED_MIX_241, "mix_241");

        putFuel(LEA_242, DEPLETED_LEA_242, "lea_242");
        putFuel(HEA_242, DEPLETED_HEA_242, "hea_242");

        putFuel(LECm_243, DEPLETED_LECm_243, "lecm_243");
        putFuel(HECm_243, DEPLETED_HECm_243, "hecm_243");
        putFuel(LECm_245, DEPLETED_LECm_245, "lecm_245");
        putFuel(HECm_245, DEPLETED_HECm_245, "hecm_245");
        putFuel(LECm_247, DEPLETED_LECm_247, "lecm_247");
        putFuel(HECm_247, DEPLETED_HECm_247, "hecm_247");

        putFuel(LEB_248, DEPLETED_LEB_248, "leb_248");
        putFuel(HEB_248, DEPLETED_HEB_248, "heb_248");

        putFuel(LECf_249, DEPLETED_LECf_249, "lecf_249");
        putFuel(HECf_249, DEPLETED_HECf_249, "hecf_249");
        putFuel(LECf_251, DEPLETED_LECf_251, "lecf_251");
        putFuel(HECf_251, DEPLETED_HECf_251, "hecf_251");

        put(URANIUM_238 * 4D, PART_MAP.get("du_plating").toStack());
        put(URANIUM_238 * 16D, SOLAR_MAP.get("solar_panel_du").asItem(), BATTERY_MAP.get("du_voltaic_pile").asItem(), BATTERY_MAP.get("du_lithium_ion_battery").asItem());
        put(URANIUM_238 * 12D, HEAVY_RADIATION_SHIELDING.toStack());

        put(URANIUM_238 / 4D, RTG_MAP.get("rtg_uranium").asItem());
        put(PLUTONIUM_238 / 4D, RTG_MAP.get("rtg_plutonium").asItem());
        put(AMERICIUM_241 / 4D, RTG_MAP.get("rtg_americium").asItem());
        put(CALIFORNIUM_250 / 4D, RTG_MAP.get("rtg_californium").asItem());

        put(RADIUM / 4D, FISSION_REACTOR_MAP.get("radium_beryllium_source").asItem());
        put(POLONIUM / 4D, FISSION_REACTOR_MAP.get("polonium_beryllium_source").asItem());
        put(CALIFORNIUM_252 / 4D, FISSION_REACTOR_MAP.get("californium_source").asItem());

        put(TRITIUM / 256D, TRITIUM_LAMP.asItem());

        put(CORIUM * FLUID_MULTIPLIER, SOLIDIFIED_CORIUM.asItem());

        putTag(CAESIUM_137 / 4D, tag(Tags.Items.DUSTS, "irradiated_borax"));

        putFluid(FUSION, "plasma");
        putFluid(TRITIUM, "tritium");

        putFluid(CORIUM, "corium");

        putFluid(THORIUM, "thorium");
        putFluid(URANIUM, "uranium", "yellorium");
        putFluid(PLUTONIUM, "plutonium", "blutonium");
        putFluid(URANIUM_238, "cyanite");

        putFluid(BISMUTH, "bismuth");
        putFluid(RADIUM, "radium");
        putFluid(POLONIUM, "polonium");
        putFluid(PROTACTINIUM_233, "protactinium_233");
        putFluid(TBP, "tbp");
        putFluid(STRONTIUM_90, "strontium_90");
        putFluid(RUTHENIUM_106, "ruthenium_106");
        putFluid(CAESIUM_137, "cesium_137");
        putFluid(CAESIUM_137, "caesium_137");
        putFluid(PROMETHIUM_147, "promethium_147");
        putFluid(EUROPIUM_155, "europium_155");

        putFluid(CAESIUM_137 / 4D, "irradiated_borax_solution");

        // Custom and Stack Entries

        for (String oreInfo : radiation_ores) {
            int scorePos = oreInfo.lastIndexOf('_');
            if (scorePos == -1) {
                continue;
            }
            addToTagMap(tag(Tags.Items.ORES, oreInfo.substring(0, scorePos)), Double.parseDouble(oreInfo.substring(scorePos + 1)));
        }
        for (String itemInfo : radiation_items) {
            int scorePos = itemInfo.lastIndexOf('_');
            if (scorePos == -1) {
                continue;
            }
            ItemStack stack = RegistryHelper.itemStackFromRegistry(itemInfo.substring(0, scorePos));
            if (stack != null) {
                addToStackMap(stack, Double.parseDouble(itemInfo.substring(scorePos + 1)));
            }
        }
        for (String blockInfo : radiation_blocks) {
            int scorePos = blockInfo.lastIndexOf('_');
            if (scorePos == -1) {
                continue;
            }
            ItemStack stack = RegistryHelper.blockStackFromRegistry(blockInfo.substring(0, scorePos));
            if (stack != null) {
                addToStackMap(stack, Double.parseDouble(blockInfo.substring(scorePos + 1)));
            }
        }
        for (String fluidInfo : radiation_fluids) {
            int scorePos = fluidInfo.lastIndexOf('_');
            if (scorePos == -1) {
                continue;
            }
            addToFluidMap(fluidInfo.substring(0, scorePos), Double.parseDouble(fluidInfo.substring(scorePos + 1)));
        }

        // Food Entries

        for (String itemInfo : radiation_foods) {
            int scorePos = itemInfo.lastIndexOf('_');
            if (scorePos == -1) {
                continue;
            }
            double resistance = Double.parseDouble(itemInfo.substring(scorePos + 1));
            itemInfo = itemInfo.substring(0, scorePos);
            scorePos = itemInfo.lastIndexOf('_');
            if (scorePos == -1) {
                continue;
            }
            ItemStack stack = RegistryHelper.itemStackFromRegistry(itemInfo.substring(0, scorePos));
            double rads = Double.parseDouble(itemInfo.substring(scorePos + 1));
            if (stack != null && (rads != 0D || resistance != 0D) && stack.has(DataComponents.FOOD)) {
                addToFoodMaps(stack, rads, resistance);
            }
        }

        for (Runnable runnable : RUNNABLES) {
            runnable.run();
        }
    }

    public static void postInit() {
        TAG_MAP.forEach((key, value) -> Arrays.stream(Ingredient.of(key).getItems()).toList().forEach(s -> addToStackMap(s, value)));
    }

    public static void refreshRadSources(boolean postInit) {
        STACK_BLACKLIST.clear();
        STACK_MAP.clear();
        FOOD_RAD_MAP.clear();
        FOOD_RESISTANCE_MAP.clear();

        init();
        if (postInit) {
            postInit();
        }
    }

    public static void putMaterial(double radiation, String... ores) {
        for (String name : ores) {
            for (Object2DoubleMap.Entry<TagKey<Item>> entry : PREFIX_MULTIPLIER_MAP.object2DoubleEntrySet()) {
                addToTagMap(tag(entry.getKey(), name), radiation * entry.getDoubleValue());
            }
        }
    }

    public static void putIsotope(double radiation, String name) {
        for (String suffix : new String[]{"", "_c", "_ox", "_ni", "_za"}) {
            addToStackMap(new ItemStack(ItemHelper.getItemFrom(ncLoc(name + suffix))), radiation);
        }
        if (name != null) {
            for (String suffix : new String[]{"", "_za", "_fluoride", "_fluoride_flibe"}) {
                addToFluidMap(name + suffix, radiation * FLUID_MULTIPLIER);
            }
        }
    }

    public static void putFuel(double fuelRadiation, double depletedRadiation, String name) {
        for (String suffix : new String[]{"", "_tr", "_c", "_ox", "_ni", "_za"}) {
            addToStackMap(new ItemStack(ItemHelper.getItemFrom(ncLoc(name + suffix))), fuelRadiation);
            addToStackMap(new ItemStack(ItemHelper.getItemFrom(ncLoc("depleted_" + name + suffix))), depletedRadiation);
        }
        if (name != null) {
            for (String suffix : new String[]{"", "_za", "_fluoride", "_fluoride_flibe"}) {
                addToFluidMap(name + suffix, fuelRadiation * FLUID_MULTIPLIER);
                addToFluidMap("depleted_" + name + suffix, depletedRadiation * FLUID_MULTIPLIER);
            }
        }
    }

    public static void putTag(double radiation, TagKey<Item>... tags) {
        for (TagKey<Item> tag : tags) {
            addToTagMap(tag, radiation);
        }
    }

    public static void put(double radiation, ItemStack... stacks) {
        for (ItemStack stack : stacks) {
            addToStackMap(stack, radiation);
        }
    }

    public static void put(double radiation, Item... items) {
        for (Item item : items) {
            addToStackMap(new ItemStack(item), radiation);
        }
    }

    public static void put(double radiation, Block... blocks) {
        for (Block block : blocks) {
            addToStackMap(new ItemStack(block), radiation);
        }
    }

    public static void putFluid(double radiation, String... fluids) {
        for (String fluid : fluids) {
            addToFluidMap(fluid, radiation * FLUID_MULTIPLIER);
        }
    }

    // Materials

    public static final double FUSION = 0.000000315D;
    public static final double NEUTRON = 0.00505D;
    public static final double TRITIUM = 0.0115D;

    public static final double CORIUM = 0.0000165D;

    public static final double THORIUM = 0.0000000000715D;
    public static final double URANIUM = 0.000000000385D;
    public static final double PLUTONIUM = 0.000042D;

    public static final double BISMUTH = 4.975E-20D;
    public static final double RADIUM = 0.000625;
    public static final double POLONIUM = 2.64D / 9D;
    public static final double PROTACTINIUM_233 = 13.54 / 9D;
    public static final double TBP = getFuelRadiation(THORIUM, 8.5D, PROTACTINIUM_233, 0.5D);
    public static final double STRONTIUM_90 = 0.0345D;
    public static final double RUTHENIUM_106 = 0.98D;
    public static final double CAESIUM_137 = 0.033D;
    public static final double PROMETHIUM_147 = 0.38D;
    public static final double EUROPIUM_155 = 0.21D;

    // Isotopes

    public static final double URANIUM_233 = 0.0000063D;
    public static final double URANIUM_235 = 0.00000000145D;
    public static final double URANIUM_238 = 0.000000000225D;

    public static final double NEPTUNIUM_236 = 0.0000065D;
    public static final double NEPTUNIUM_237 = 0.00000047D;

    public static final double PLUTONIUM_238 = 0.0115D;
    public static final double PLUTONIUM_239 = 0.0000415D;
    public static final double PLUTONIUM_241 = 0.0715D;
    public static final double PLUTONIUM_242 = 0.0000027D;

    public static final double AMERICIUM_241 = 0.00235D;
    public static final double AMERICIUM_242 = 0.0071D;
    public static final double AMERICIUM_243 = 0.00014D;

    public static final double CURIUM_243 = 0.0345D;
    public static final double CURIUM_245 = 0.00012D;
    public static final double CURIUM_246 = 0.000215D;
    public static final double CURIUM_247 = 0.0000000645D;

    public static final double BERKELIUM_247 = 0.000725D;
    public static final double BERKELIUM_248 = 0.00325D;

    public static final double CALIFORNIUM_249 = 0.00285D;
    public static final double CALIFORNIUM_250 = 0.0765D;
    public static final double CALIFORNIUM_251 = 0.00115D;
    public static final double CALIFORNIUM_252 = 0.38D;

    // Fuels

    public static double getFuelRadiation(double rad1, double amount1, double rad2, double amount2) {
        return (rad1 * amount1 + rad2 * amount2) / 9D;
    }

    @Deprecated
    public static double getDepletedFuelRadiation(double rad1, int amount1, double rad2, int amount2, double rad3, int amount3, double rad4, int amount4) {
        return (rad1 * amount1 + rad2 * amount2 + rad3 * amount3 + rad4 * amount4) / 9D;
    }

    public static double getDepletedFuelRadiation(double rad1, int amount1, double rad2, int amount2, double rad3, int amount3, double rad4, int amount4, double waste1, double waste2, double m, double r) {
        return (rad1 * amount1 + rad2 * amount2 + rad3 * amount3 + rad4 * amount4 + waste1 * m * r + waste2 * m * (1D - r)) / 9D;
    }

    public static final double TBU = getFuelRadiation(THORIUM, 8.5D, URANIUM_233, 0.5D);

    public static final double LEU_233 = getFuelRadiation(URANIUM_238, 8, URANIUM_233, 1);
    public static final double HEU_233 = getFuelRadiation(URANIUM_238, 6, URANIUM_233, 3);
    public static final double LEU_235 = getFuelRadiation(URANIUM_238, 8, URANIUM_235, 1);
    public static final double HEU_235 = getFuelRadiation(URANIUM_238, 6, URANIUM_235, 3);

    public static final double LEN_236 = getFuelRadiation(NEPTUNIUM_237, 8, NEPTUNIUM_236, 1);
    public static final double HEN_236 = getFuelRadiation(NEPTUNIUM_237, 6, NEPTUNIUM_236, 3);

    public static final double LEP_239 = getFuelRadiation(PLUTONIUM_242, 8, PLUTONIUM_239, 1);
    public static final double HEP_239 = getFuelRadiation(PLUTONIUM_242, 6, PLUTONIUM_239, 3);
    public static final double LEP_241 = getFuelRadiation(PLUTONIUM_242, 8, PLUTONIUM_241, 1);
    public static final double HEP_241 = getFuelRadiation(PLUTONIUM_242, 6, PLUTONIUM_241, 3);

    public static final double MIX_239 = getFuelRadiation(URANIUM_238, 8, PLUTONIUM_239, 1);
    public static final double MIX_241 = getFuelRadiation(URANIUM_238, 8, PLUTONIUM_241, 1);

    public static final double LEA_242 = getFuelRadiation(AMERICIUM_243, 8, AMERICIUM_242, 1);
    public static final double HEA_242 = getFuelRadiation(AMERICIUM_243, 6, AMERICIUM_242, 3);

    public static final double LECm_243 = getFuelRadiation(CURIUM_246, 8, CURIUM_243, 1);
    public static final double HECm_243 = getFuelRadiation(CURIUM_246, 6, CURIUM_243, 3);
    public static final double LECm_245 = getFuelRadiation(CURIUM_246, 8, CURIUM_245, 1);
    public static final double HECm_245 = getFuelRadiation(CURIUM_246, 6, CURIUM_245, 3);
    public static final double LECm_247 = getFuelRadiation(CURIUM_246, 8, CURIUM_247, 1);
    public static final double HECm_247 = getFuelRadiation(CURIUM_246, 6, CURIUM_247, 3);

    public static final double LEB_248 = getFuelRadiation(BERKELIUM_247, 8, BERKELIUM_248, 1);
    public static final double HEB_248 = getFuelRadiation(BERKELIUM_247, 6, BERKELIUM_248, 3);

    public static final double LECf_249 = getFuelRadiation(CALIFORNIUM_252, 8, CALIFORNIUM_249, 1);
    public static final double HECf_249 = getFuelRadiation(CALIFORNIUM_252, 6, CALIFORNIUM_249, 3);
    public static final double LECf_251 = getFuelRadiation(CALIFORNIUM_252, 8, CALIFORNIUM_251, 1);
    public static final double HECf_251 = getFuelRadiation(CALIFORNIUM_252, 6, CALIFORNIUM_251, 3);

    public static final double DEPLETED_TBU = getDepletedFuelRadiation(URANIUM_233, 1, URANIUM_238, 5, NEPTUNIUM_236, 1, NEPTUNIUM_237, 1, STRONTIUM_90, CAESIUM_137, 0.5D, 0.5D);

    public static final double DEPLETED_LEU_233 = getDepletedFuelRadiation(URANIUM_238, 5, PLUTONIUM_241, 1, PLUTONIUM_242, 1, AMERICIUM_243, 1, STRONTIUM_90, CAESIUM_137, 0.5D, 0.5D);
    public static final double DEPLETED_HEU_233 = getDepletedFuelRadiation(URANIUM_235, 1, URANIUM_238, 2, PLUTONIUM_242, 3, AMERICIUM_243, 1, STRONTIUM_90, CAESIUM_137, 1.5D, 0.5D);
    public static final double DEPLETED_LEU_235 = getDepletedFuelRadiation(URANIUM_238, 4, PLUTONIUM_239, 1, PLUTONIUM_242, 2, AMERICIUM_243, 1, 0D, CAESIUM_137, 0.5D, 0.5D);
    public static final double DEPLETED_HEU_235 = getDepletedFuelRadiation(URANIUM_238, 3, NEPTUNIUM_236, 1, PLUTONIUM_242, 2, AMERICIUM_243, 1, 0D, CAESIUM_137, 1.5D, 0.5D);

    public static final double DEPLETED_LEN_236 = getDepletedFuelRadiation(URANIUM_238, 4, NEPTUNIUM_237, 1, PLUTONIUM_241, 1, PLUTONIUM_242, 2, 0D, CAESIUM_137, 0.5D, 0.5D);
    public static final double DEPLETED_HEN_236 = getDepletedFuelRadiation(URANIUM_238, 4, PLUTONIUM_238, 1, PLUTONIUM_241, 1, PLUTONIUM_242, 1, 0D, CAESIUM_137, 1.5D, 0.5D);

    public static final double DEPLETED_LEP_239 = getDepletedFuelRadiation(PLUTONIUM_242, 5, AMERICIUM_242, 1, AMERICIUM_243, 1, CURIUM_246, 1, STRONTIUM_90, PROMETHIUM_147, 0.5D, 0.5D);
    public static final double DEPLETED_HEP_239 = getDepletedFuelRadiation(PLUTONIUM_241, 1, AMERICIUM_242, 1, AMERICIUM_243, 4, CURIUM_243, 1, STRONTIUM_90, PROMETHIUM_147, 1.5D, 0.5D);
    public static final double DEPLETED_LEP_241 = getDepletedFuelRadiation(PLUTONIUM_242, 5, AMERICIUM_243, 1, CURIUM_246, 1, BERKELIUM_247, 1, STRONTIUM_90, PROMETHIUM_147, 0.5D, 0.5D);
    public static final double DEPLETED_HEP_241 = getDepletedFuelRadiation(PLUTONIUM_241, 1, AMERICIUM_242, 1, AMERICIUM_243, 3, CURIUM_246, 2, STRONTIUM_90, PROMETHIUM_147, 1.5D, 0.5D);

    public static final double DEPLETED_MIX_239 = getDepletedFuelRadiation(URANIUM_238, 4, PLUTONIUM_241, 1, PLUTONIUM_242, 2, AMERICIUM_243, 1, STRONTIUM_90, PROMETHIUM_147, 0.5D, 0.5D);
    public static final double DEPLETED_MIX_241 = getDepletedFuelRadiation(URANIUM_238, 3, PLUTONIUM_241, 1, PLUTONIUM_242, 3, AMERICIUM_243, 1, STRONTIUM_90, PROMETHIUM_147, 0.5D, 0.5D);

    public static final double DEPLETED_LEA_242 = getDepletedFuelRadiation(AMERICIUM_243, 3, CURIUM_245, 1, CURIUM_246, 3, BERKELIUM_248, 1, 0D, PROMETHIUM_147, 0.5D, 0.5D);
    public static final double DEPLETED_HEA_242 = getDepletedFuelRadiation(AMERICIUM_243, 3, CURIUM_243, 1, CURIUM_246, 2, BERKELIUM_247, 1, 0D, PROMETHIUM_147, 1.5D, 0.5D);

    public static final double DEPLETED_LECm_243 = getDepletedFuelRadiation(CURIUM_246, 4, CURIUM_247, 1, BERKELIUM_247, 2, BERKELIUM_248, 1, 0D, PROMETHIUM_147, 0.5D, 0.5D);
    public static final double DEPLETED_HECm_243 = getDepletedFuelRadiation(CURIUM_245, 1, CURIUM_246, 3, BERKELIUM_247, 2, BERKELIUM_248, 1, 0D, PROMETHIUM_147, 1.5D, 0.5D);
    public static final double DEPLETED_LECm_245 = getDepletedFuelRadiation(CURIUM_246, 4, CURIUM_247, 1, BERKELIUM_247, 2, CALIFORNIUM_249, 1, 0D, EUROPIUM_155, 0.5D, 0.6D);
    public static final double DEPLETED_HECm_245 = getDepletedFuelRadiation(CURIUM_246, 3, CURIUM_247, 1, BERKELIUM_247, 2, CALIFORNIUM_249, 1, 0D, EUROPIUM_155, 1.5D, 0.6D);
    public static final double DEPLETED_LECm_247 = getDepletedFuelRadiation(CURIUM_246, 5, BERKELIUM_247, 1, BERKELIUM_248, 1, CALIFORNIUM_249, 1, 0D, EUROPIUM_155, 0.5D, 0.6D);
    public static final double DEPLETED_HECm_247 = getDepletedFuelRadiation(BERKELIUM_247, 4, BERKELIUM_248, 1, CALIFORNIUM_249, 1, CALIFORNIUM_251, 1, 0D, EUROPIUM_155, 1.5D, 0.6D);

    public static final double DEPLETED_LEB_248 = getDepletedFuelRadiation(BERKELIUM_247, 5, BERKELIUM_248, 1, CALIFORNIUM_249, 1, CALIFORNIUM_251, 1, RUTHENIUM_106, PROMETHIUM_147, 0.5D, 0.6D);
    public static final double DEPLETED_HEB_248 = getDepletedFuelRadiation(BERKELIUM_248, 1, CALIFORNIUM_249, 1, CALIFORNIUM_251, 2, CALIFORNIUM_252, 3, RUTHENIUM_106, PROMETHIUM_147, 1.5D, 0.6D);

    public static final double DEPLETED_LECf_249 = getDepletedFuelRadiation(CALIFORNIUM_252, 2, CALIFORNIUM_252, 2, CALIFORNIUM_252, 2, CALIFORNIUM_252, 2, RUTHENIUM_106, PROMETHIUM_147, 0.5D, 0.6D);
    public static final double DEPLETED_HECf_249 = getDepletedFuelRadiation(CALIFORNIUM_250, 1, CALIFORNIUM_252, 2, CALIFORNIUM_252, 2, CALIFORNIUM_252, 2, RUTHENIUM_106, PROMETHIUM_147, 1.5D, 0.6D);
    public static final double DEPLETED_LECf_251 = getDepletedFuelRadiation(CALIFORNIUM_252, 2, CALIFORNIUM_252, 2, CALIFORNIUM_252, 2, CALIFORNIUM_252, 2, RUTHENIUM_106, EUROPIUM_155, 0.5D, 0.6D);
    public static final double DEPLETED_HECf_251 = getDepletedFuelRadiation(CALIFORNIUM_252, 2, CALIFORNIUM_252, 2, CALIFORNIUM_252, 2, CALIFORNIUM_252, 1, RUTHENIUM_106, EUROPIUM_155, 1.5D, 0.6D);

    public static final double TBU_FISSION = (TBU + DEPLETED_TBU + CAESIUM_137) / 64D;

    public static final double LEU_233_FISSION = (LEU_233 + DEPLETED_LEU_233) / 64D;
    public static final double HEU_233_FISSION = (HEU_233 + DEPLETED_HEU_233) / 64D;
    public static final double LEU_235_FISSION = (LEU_235 + DEPLETED_LEU_235) / 64D;
    public static final double HEU_235_FISSION = (HEU_235 + DEPLETED_HEU_235) / 64D;

    public static final double LEN_236_FISSION = (LEN_236 + DEPLETED_LEN_236) / 64D;
    public static final double HEN_236_FISSION = (HEN_236 + DEPLETED_HEN_236) / 64D;

    public static final double LEP_239_FISSION = (LEP_239 + DEPLETED_LEP_239) / 64D;
    public static final double HEP_239_FISSION = (HEP_239 + DEPLETED_HEP_239) / 64D;
    public static final double LEP_241_FISSION = (LEP_241 + DEPLETED_LEP_241) / 64D;
    public static final double HEP_241_FISSION = (HEP_241 + DEPLETED_HEP_241) / 64D;

    public static final double MIX_239_FISSION = (MIX_239 + DEPLETED_MIX_239) / 64D;
    public static final double MIX_241_FISSION = (MIX_241 + DEPLETED_MIX_241) / 64D;

    public static final double LEA_242_FISSION = (LEA_242 + DEPLETED_LEA_242) / 64D;
    public static final double HEA_242_FISSION = (HEA_242 + DEPLETED_HEA_242) / 64D;

    public static final double LECm_243_FISSION = (LECm_243 + DEPLETED_LECm_243) / 64D;
    public static final double HECm_243_FISSION = (HECm_243 + DEPLETED_HECm_243) / 64D;
    public static final double LECm_245_FISSION = (LECm_245 + DEPLETED_LECm_245) / 64D;
    public static final double HECm_245_FISSION = (HECm_245 + DEPLETED_HECm_245) / 64D;
    public static final double LECm_247_FISSION = (LECm_247 + DEPLETED_LECm_247) / 64D;
    public static final double HECm_247_FISSION = (HECm_247 + DEPLETED_HECm_247) / 64D;

    public static final double LEB_248_FISSION = (LEB_248 + DEPLETED_LEB_248) / 64D;
    public static final double HEB_248_FISSION = (HEB_248 + DEPLETED_HEB_248) / 64D;

    public static final double LECf_249_FISSION = (LECf_249 + DEPLETED_LECf_249) / 64D;
    public static final double HECf_249_FISSION = (HECf_249 + DEPLETED_HECf_249) / 64D;
    public static final double LECf_251_FISSION = (LECf_251 + DEPLETED_LECf_251) / 64D;
    public static final double HECf_251_FISSION = (HECf_251 + DEPLETED_HECf_251) / 64D;
}