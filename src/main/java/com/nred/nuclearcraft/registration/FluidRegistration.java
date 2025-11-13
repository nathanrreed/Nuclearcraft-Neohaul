package com.nred.nuclearcraft.registration;

import com.nred.nuclearcraft.block.fluid.CoriumFluidBlock;
import com.nred.nuclearcraft.block.fluid.LiquidFluidBlock;
import com.nred.nuclearcraft.block.fluid.SuperFluidBlock;
import com.nred.nuclearcraft.fluid.CoriumFluid;
import com.nred.nuclearcraft.fluid.PlasmaFluid;
import com.nred.nuclearcraft.info.Fluids;
import net.minecraft.util.FastColor;

import java.util.HashMap;
import java.util.Map;

public class FluidRegistration {
    public static final Map<String, Fluids> GAS_MAP = createGasses();
    public static final Map<String, Fluids> MOLTEN_MAP = createMolten();
    public static final Map<String, Fluids> HOT_GAS_MAP = createHotGas();
    public static final Map<String, Fluids> SUGAR_MAP = createSugar();
    public static final Map<String, Fluids> CHOCOLATE_MAP = createChocolate();
    public static final Map<String, Fluids> FISSION_FLUID_MAP = createFission();
    public static final Map<String, Fluids> STEAM_MAP = createSteam();
    public static final Map<String, Fluids> SALT_SOLUTION_MAP = createSaltSolution();
    public static final Map<String, Fluids> ACID_MAP = createAcid();
    public static final Map<String, Fluids> FLAMMABLE_MAP = createFlammable();
    public static final Map<String, Fluids> HOT_COOLANT_MAP = new HashMap<>();
    public static final Map<String, Fluids> COOLANT_MAP = createCoolant();
    public static final Map<String, Fluids> CUSTOM_FLUID_MAP = createCustomFluid();
    public static final Map<String, Fluids> FISSION_FUEL_MAP = createFissionFuel();

    private static Map<String, Fluids> createGasses() {
        Map<String, Fluids> map = new java.util.HashMap<>();
        map.put("oxygen", new Fluids("oxygen", 0xFF7E8CC8, Fluids.GAS_TYPE));
        map.put("hydrogen", new Fluids("hydrogen", 0xFFB37AC4, Fluids.GAS_TYPE));
        map.put("deuterium", new Fluids("deuterium", 0xFF9E6FEF, Fluids.GAS_TYPE));
        map.put("tritium", new Fluids("tritium", 0xFF5DBBD6, Fluids.GAS_TYPE));
        map.put("helium_3", new Fluids("helium_3", 0xFFCBBB67, Fluids.GAS_TYPE));
        map.put("helium", new Fluids("helium", 0xFFC57B81, Fluids.GAS_TYPE));
        map.put("nitrogen", new Fluids("nitrogen", 0xFF7CC37B, Fluids.GAS_TYPE));
        map.put("fluorine", new Fluids("fluorine", 0xFFD3C75D, Fluids.GAS_TYPE));
        map.put("carbon_dioxide", new Fluids("carbon_dioxide", 0xFF5C635A, Fluids.GAS_TYPE));
        map.put("carbon_monoxide", new Fluids("carbon_monoxide", 0xFF4C5649, Fluids.GAS_TYPE));
        map.put("ethylene", new Fluids("ethylene", 0xFFFFE4A3, Fluids.GAS_TYPE));
        map.put("acetylene", new Fluids("acetylene", 0xFFFFE442, Fluids.GAS_TYPE));
        map.put("fluoromethane", new Fluids("fluoromethane", 0xFF424C05, Fluids.GAS_TYPE));
        map.put("ammonia", new Fluids("ammonia", 0xFF7AC3A0, Fluids.GAS_TYPE));
        map.put("oxygen_difluoride", new Fluids("oxygen_difluoride", 0xFFEA1B01, Fluids.GAS_TYPE));
        map.put("diborane", new Fluids("diborane", 0xFFCC6E8C, Fluids.GAS_TYPE));
        map.put("sulfur_dioxide", new Fluids("sulfur_dioxide", 0xFFC3BC7A, Fluids.GAS_TYPE));
        map.put("sulfur_trioxide", new Fluids("sulfur_trioxide", 0xFFD3AE5D, Fluids.GAS_TYPE));
        map.put("tetrafluoroethene", new Fluids("tetrafluoroethene", 0xFF7EA542, Fluids.GAS_TYPE));
        map.put("hydrogen_sulfide", new Fluids("hydrogen_sulfide", 0xFF785830, Fluids.GAS_TYPE));
        map.put("depleted_hydrogen_sulfide", new Fluids("depleted_hydrogen_sulfide", 0xFF59514E, Fluids.GAS_TYPE));
        return map;
    }

    private static Map<String, Fluids> createMolten() {
        Map<String, Fluids> map = new java.util.HashMap<>();
        map.put("boron_10", new Fluids("boron_10", 0xFF7D7D7D, Fluids.MOLTEN_TYPE));
        map.put("boron_11", new Fluids("boron_11", 0xFF7D7D7D, Fluids.MOLTEN_TYPE));
        map.put("lithium_6", new Fluids("lithium_6", 0xFFEFEFEF, Fluids.MOLTEN_TYPE));
        map.put("lithium_7", new Fluids("lithium_7", 0xFFEFEFEF, Fluids.MOLTEN_TYPE));
        map.put("steel", new Fluids("steel", 0xFF7B7B7B, Fluids.MOLTEN_TYPE));
        map.put("ferroboron", new Fluids("ferroboron", 0xFF4A4A4A, Fluids.MOLTEN_TYPE));
        map.put("tough", new Fluids("tough", 0xFF150F21, Fluids.MOLTEN_TYPE));
        map.put("hard_carbon", new Fluids("hard_carbon", 0xFF202020, Fluids.MOLTEN_TYPE));
        map.put("coal", new Fluids("coal", 0xFF7D7D7D, Fluids.MOLTEN_TYPE));
        map.put("beryllium", new Fluids("beryllium", 0xFFD4DBC2, Fluids.MOLTEN_TYPE));
        map.put("zirconium", new Fluids("zirconium", 0xFFE0E0B8, Fluids.MOLTEN_TYPE));
        map.put("manganese_dioxide", new Fluids("manganese_dioxide", 0xFF28211E, Fluids.MOLTEN_TYPE));
        map.put("sulfur", new Fluids("sulfur", 0xFFDEDE7A, Fluids.MOLTEN_TYPE));
        map.put("lead_platinum", new Fluids("lead_platinum", 0xFF415B60, Fluids.MOLTEN_TYPE));
        map.put("enderium", new Fluids("enderium", 0xFF0B5B5C, Fluids.MOLTEN_TYPE));
        map.put("lif", new Fluids("lif", 0xFFCDCDCB, Fluids.MOLTEN_TYPE));
        map.put("bef2", new Fluids("bef2", 0xFFBEC6AA, Fluids.MOLTEN_TYPE));
        map.put("flibe", new Fluids("flibe", 0xFFC1C8B0, Fluids.MOLTEN_TYPE));
        map.put("naoh", new Fluids("naoh", 0xFFC2B7BB, Fluids.MOLTEN_TYPE));
        map.put("koh", new Fluids("koh", 0xFFB8C6B0, Fluids.MOLTEN_TYPE));
        map.put("sodium", new Fluids("sodium", 0xFFC1898C, Fluids.MOLTEN_TYPE));
        map.put("potassium", new Fluids("potassium", 0xFFB8C503, Fluids.MOLTEN_TYPE));
        map.put("sodium_sulfide", new Fluids("sodium_sulfide", 0xFF9A8B0B, Fluids.MOLTEN_TYPE));
        map.put("potassium_sulfide", new Fluids("potassium_sulfide", 0xFF917C34, Fluids.MOLTEN_TYPE));
        map.put("silicon", new Fluids("silicon", 0xFF676767, Fluids.MOLTEN_TYPE));
        map.put("bas", new Fluids("bas", 0xFF9B9B89, Fluids.MOLTEN_TYPE));
        map.put("alugentum", new Fluids("alugentum", 0xFFB5C9CB, Fluids.MOLTEN_TYPE));
        map.put("alumina", new Fluids("alumina", 0xFF919880, Fluids.MOLTEN_TYPE));
        map.put("iron", new Fluids("iron", 0xFF8D1515, Fluids.MOLTEN_TYPE));
        map.put("redstone", new Fluids("redstone", 0xFFAB1C09, Fluids.MOLTEN_TYPE));
        map.put("quartz", new Fluids("quartz", 0xFFECE9E2, Fluids.MOLTEN_TYPE));
        map.put("obsidian", new Fluids("obsidian", 0xFF1C1828, Fluids.MOLTEN_TYPE));
        map.put("nether_brick", new Fluids("nether_brick", 0xFF271317, Fluids.MOLTEN_TYPE));
        map.put("glowstone", new Fluids("glowstone", 0xFFA38037, Fluids.GLOWSTONE_TYPE));
        map.put("lapis", new Fluids("lapis", 0xFF27438A, Fluids.MOLTEN_TYPE));
        map.put("gold", new Fluids("gold", 0xFFE6DA3C, Fluids.MOLTEN_TYPE));
        map.put("prismarine", new Fluids("prismarine", 0xFF70A695, Fluids.MOLTEN_TYPE));
        map.put("slime", new Fluids("slime", 0xFF79C865, Fluids.MOLTEN_TYPE));
        map.put("end_stone", new Fluids("end_stone", 0xFFE7E9B3, Fluids.MOLTEN_TYPE));
        map.put("purpur", new Fluids("purpur", 0xFFA979A9, Fluids.MOLTEN_TYPE));
        map.put("diamond", new Fluids("diamond", 0xFF6FDFDA, Fluids.MOLTEN_TYPE));
        map.put("emerald", new Fluids("emerald", 0xFF51D975, Fluids.MOLTEN_TYPE));
        map.put("copper", new Fluids("copper", 0xFFAD6544, Fluids.MOLTEN_TYPE));
        map.put("tin", new Fluids("tin", 0xFFD9DDF0, Fluids.MOLTEN_TYPE));
        map.put("lead", new Fluids("lead", 0xFF3F4C4C, Fluids.MOLTEN_TYPE));
        map.put("boron", new Fluids("boron", 0xFF7D7D7D, Fluids.MOLTEN_TYPE));
        map.put("lithium", new Fluids("lithium", 0xFFEFEFEF, Fluids.MOLTEN_TYPE));
        map.put("magnesium", new Fluids("magnesium", 0xFFEED5E1, Fluids.MOLTEN_TYPE));
        map.put("manganese", new Fluids("manganese", 0xFF99A1CA, Fluids.MOLTEN_TYPE));
        map.put("aluminum", new Fluids("aluminum", 0xFFB5ECD5, Fluids.MOLTEN_TYPE));
        map.put("silver", new Fluids("silver", 0xFFE2DAF6, Fluids.MOLTEN_TYPE));
        map.put("fluorite", new Fluids("fluorite", 0xFF8AB492, Fluids.MOLTEN_TYPE));
        map.put("villiaumite", new Fluids("villiaumite", 0xFFB06C56, Fluids.MOLTEN_TYPE));
        map.put("carobbiite", new Fluids("carobbiite", 0xFF95A251, Fluids.MOLTEN_TYPE));
        map.put("dfdps", new Fluids("dfdps", 0xFFB4B3A7, Fluids.MOLTEN_TYPE));
        map.put("polyphenylene_sulfide", new Fluids("polyphenylene_sulfide", 0xFF3F3D3E, Fluids.MOLTEN_TYPE));
        map.put("polydimethylsilylene", new Fluids("polydimethylsilylene", 0xFF774F60, Fluids.MOLTEN_TYPE));
        map.put("polymethylsilylene_methylene", new Fluids("polymethylsilylene_methylene", 0xFF5A5246, Fluids.MOLTEN_TYPE));
        map.put("polyethersulfone", new Fluids("polyethersulfone", 0xFFC9B8A6, Fluids.MOLTEN_TYPE));
        map.put("polytetrafluoroethene", new Fluids("polytetrafluoroethene", 0xFF7F9F4D, Fluids.MOLTEN_TYPE));
        map.put("thorium", new Fluids("thorium", 0xFF242424, Fluids.MOLTEN_TYPE));
        map.put("uranium", new Fluids("uranium", 0xFF375437, Fluids.MOLTEN_TYPE));
        return map;
    }

    private static Map<String, Fluids> createCustomFluid() {
        Map<String, Fluids> map = new java.util.HashMap<>();
        map.put("liquid_helium", new Fluids("liquid_helium", true, -1, 150, 4, 1, 0, SuperFluidBlock::new));
        map.put("le_water", new Fluids("le_water", false, -1, LiquidFluidBlock::new));
        map.put("he_water", new Fluids("he_water", false, -1, LiquidFluidBlock::new));
        map.put("heavy_water", new Fluids("heavy_water", false, -1, LiquidFluidBlock::new));
        map.put("hydrogen_peroxide", new Fluids("hydrogen_peroxide", false, -1, LiquidFluidBlock::new));
        map.put("liquid_nitrogen", new Fluids("liquid_nitrogen", "liquid", false, false, 0xFF31C23A, 810, 70, 170, 0, LiquidFluidBlock::new));
        map.put("ender", new Fluids("ender", "liquid_still", true, false, 0xFF14584D, 4000, 300, 2500, 3, LiquidFluidBlock::new));
        map.put("cryotheum", new Fluids("cryotheum", 0xFF0099C1, Fluids.CRYOTHEUM_TYPE));
        map.put("plasma", new Fluids("plasma", -1, Fluids.PLASMA_TYPE, PlasmaFluid.Source::new, PlasmaFluid.Flowing::new));
        map.put("radaway", new Fluids("radaway", false, -1, LiquidFluidBlock::new));
        map.put("radaway_slow", new Fluids("radaway_slow", false, -1, LiquidFluidBlock::new));
        map.put("corium", new Fluids("corium", 0xFF7C7C6F, new Fluids.TypeInfo(Fluids.MOLTEN_TYPE,CoriumFluidBlock::new), CoriumFluid.Source::new, CoriumFluid.Flowing::new));
        map.put("ice", new Fluids("ice", "liquid", false, false, 0xFFAFF1FF, 1000, 250, 2000, 0, LiquidFluidBlock::new));
        map.put("slurry_ice", new Fluids("slurry_ice", "liquid", false, false, 0xFF7EAEB7, 950, 270, 4000, 0, LiquidFluidBlock::new));
        map.put("emergency_coolant", new Fluids("emergency_coolant", "liquid", false, false, 0xFF6DD0E7, 2000, 100, 2000, 0, LiquidFluidBlock::new));
        map.put("emergency_coolant_heated", new Fluids("emergency_coolant_heated", "liquid", false, false, 0xFFCDBEE7, 2000, 400, 1500, 7, LiquidFluidBlock::new));
        map.put("preheated_water", new Fluids("preheated_water", "liquid", false, false, 0xFF2F43F4, 1000, 400, 250, 0, LiquidFluidBlock::new));
        map.put("condensate_water", new Fluids("condensate_water", "liquid", false, false, 0xFF2F43F4, 1000, 350, 850, 0, LiquidFluidBlock::new));
        return map;
    }

    private static Map<String, Fluids> createFlammable() {
        Map<String, Fluids> map = new java.util.HashMap<>();
        map.put("ethanol", new Fluids("ethanol", 0xFF655140, Fluids.FLAMMABLE_TYPE));
        map.put("methanol", new Fluids("methanol", 0xFF71524C, Fluids.FLAMMABLE_TYPE));
        map.put("benzene", new Fluids("benzene", 0xFF999999, Fluids.FLAMMABLE_TYPE));
        map.put("phenol", new Fluids("phenol", 0xFFF2F2F2, Fluids.FLAMMABLE_TYPE));
        map.put("fluorobenzene", new Fluids("fluorobenzene", 0xFFBAB58B, Fluids.FLAMMABLE_TYPE));
        map.put("difluorobenzene", new Fluids("difluorobenzene", 0xFF8CB57B, Fluids.FLAMMABLE_TYPE));
        map.put("dimethyldifluorosilane", new Fluids("dimethyldifluorosilane", 0xFFAEAF80, Fluids.FLAMMABLE_TYPE));
        map.put("redstone_ethanol", new Fluids("redstone_ethanol", false, -1, LiquidFluidBlock::new));
        return map;
    }

    private static Map<String, Fluids> createAcid() {
        Map<String, Fluids> map = new java.util.HashMap<>();
        map.put("hydrofluoric_acid", new Fluids("hydrofluoric_acid", 0xFF004C05, Fluids.ACID_TYPE));
        map.put("boric_acid", new Fluids("boric_acid", 0xFF696939, Fluids.ACID_TYPE));
        map.put("sulfuric_acid", new Fluids("sulfuric_acid", 0xFF454500, Fluids.ACID_TYPE));
        return map;
    }

    private static Map<String, Fluids> createSaltSolution() {
        Map<String, Fluids> map = new java.util.HashMap<>();
        map.put("boron_nitride_solution", new Fluids("boron_nitride_solution", FastColor.ARGB32.lerp(0.5f, 0xFF2F43F4, 0xFF6F8E5C), Fluids.SALT_SOLUTION_TYPE));
        map.put("fluorite_water", new Fluids("fluorite_water", FastColor.ARGB32.lerp(0.5f, 0xFF2F43F4, 0xFF8AB492), Fluids.SALT_SOLUTION_TYPE));
        map.put("calcium_sulfate_solution", new Fluids("calcium_sulfate_solution", FastColor.ARGB32.lerp(0.5f, 0xFF2F43F4, 0xFFB8B0A6), Fluids.SALT_SOLUTION_TYPE));
        map.put("sodium_fluoride_solution", new Fluids("sodium_fluoride_solution", FastColor.ARGB32.lerp(0.5f, 0xFF2F43F4, 0xFFC2B1A1), Fluids.SALT_SOLUTION_TYPE));
        map.put("potassium_fluoride_solution", new Fluids("potassium_fluoride_solution", FastColor.ARGB32.lerp(0.5f, 0xFF2F43F4, 0xFFC1C99D), Fluids.SALT_SOLUTION_TYPE));
        map.put("sodium_hydroxide_solution", new Fluids("sodium_hydroxide_solution", FastColor.ARGB32.lerp(0.5f, 0xFF2F43F4, 0xFFC2B7BB), Fluids.SALT_SOLUTION_TYPE));
        map.put("potassium_hydroxide_solution", new Fluids("potassium_hydroxide_solution", FastColor.ARGB32.lerp(0.5f, 0xFF2F43F4, 0xFFB8C6B0), Fluids.SALT_SOLUTION_TYPE));
        map.put("borax_solution", new Fluids("borax_solution", FastColor.ARGB32.lerp(0.5f, 0xFF2F43F4, 0xFFEEEEEE), Fluids.SALT_SOLUTION_TYPE));
        map.put("irradiated_borax_solution", new Fluids("irradiated_borax_solution", FastColor.ARGB32.lerp(0.5f, 0xFF2F43F4, 0xFFFFD0A3), Fluids.SALT_SOLUTION_TYPE));
        map.put("ammonium_sulfate_solution", new Fluids("ammonium_sulfate_solution", FastColor.ARGB32.lerp(0.5f, 0xFF2F43F4, 0xFF6CA377), Fluids.SALT_SOLUTION_TYPE));
        map.put("ammonium_bisulfate_solution", new Fluids("ammonium_bisulfate_solution", FastColor.ARGB32.lerp(0.5f, 0xFF2F43F4, 0xFF5F8450), Fluids.SALT_SOLUTION_TYPE));
        map.put("ammonium_persulfate_solution", new Fluids("ammonium_persulfate_solution", FastColor.ARGB32.lerp(0.5f, 0xFF2F43F4, 0xFF74A364), Fluids.SALT_SOLUTION_TYPE));
        map.put("hydroquinone_solution", new Fluids("hydroquinone_solution", FastColor.ARGB32.lerp(0.5f, 0xFF2F43F4, 0xFFB7B7B7), Fluids.SALT_SOLUTION_TYPE));
        map.put("sodium_hydroquinone_solution", new Fluids("sodium_hydroquinone_solution", FastColor.ARGB32.lerp(0.5f, 0xFF2F43F4, 0xFFC9B9BA), Fluids.SALT_SOLUTION_TYPE));
        map.put("potassium_hydroquinone_solution", new Fluids("potassium_hydroquinone_solution", FastColor.ARGB32.lerp(0.5f, 0xFF2F43F4, 0xFFC6CA94), Fluids.SALT_SOLUTION_TYPE));
        return map;
    }

    private static Map<String, Fluids> createChocolate() {
        Map<String, Fluids> map = new java.util.HashMap<>();
        map.put("chocolate_liquor", new Fluids("chocolate_liquor", 0xFF41241C, Fluids.CHOCOLATE_TYPE));
        map.put("cocoa_butter", new Fluids("cocoa_butter", 0xFFF6EEBF, Fluids.CHOCOLATE_TYPE));
        map.put("unsweetened_chocolate", new Fluids("unsweetened_chocolate", 0xFF2C0A08, Fluids.CHOCOLATE_TYPE));
        map.put("dark_chocolate", new Fluids("dark_chocolate", 0xFF2C0B06, Fluids.CHOCOLATE_TYPE));
        map.put("milk_chocolate", new Fluids("milk_chocolate", 0xFF884121, Fluids.CHOCOLATE_TYPE));
        return map;
    }

    private static Map<String, Fluids> createSugar() {
        Map<String, Fluids> map = new java.util.HashMap<>();
        map.put("sugar", new Fluids("sugar", 0xFFFFD59A, Fluids.SUGAR_TYPE));
        map.put("gelatin", new Fluids("gelatin", 0xFFDDD09C, Fluids.CHOCOLATE_TYPE));
        map.put("hydrated_gelatin", new Fluids("hydrated_gelatin", FastColor.ARGB32.lerp(0.8f, 0xFF2F43F4, 0xFFDDD09C), Fluids.CHOCOLATE_TYPE));
        map.put("marshmallow", new Fluids("marshmallow", 0xFFE1E1E3, Fluids.CHOCOLATE_TYPE));
        return map;
    }

    private static Map<String, Fluids> createSteam() {
        Map<String, Fluids> map = new java.util.HashMap<>();
        map.put("steam", new Fluids("steam", 0xFF929292, Fluids.STEAM_TYPE, 800));
        map.put("high_pressure_steam", new Fluids("high_pressure_steam", 0xFFBDBDBD, Fluids.STEAM_TYPE, 1200));
        map.put("exhaust_steam", new Fluids("exhaust_steam", 0xFFBDBDBD, Fluids.STEAM_TYPE, 500));
        map.put("low_pressure_steam", new Fluids("low_pressure_steam", 0xFFBDBDBD, Fluids.STEAM_TYPE, 800));
        map.put("low_quality_steam", new Fluids("low_quality_steam", 0xFFBDBDBD, Fluids.STEAM_TYPE, 350));
        return map;
    }

    private static Map<String, Fluids> createCoolant() {
        Map<String, Fluids> map = new java.util.HashMap<>();
        map.put("nak", new Fluids("nak", 0xFFFFE5BC, Fluids.COOLANT_TYPE));
        HOT_COOLANT_MAP.put("nak_hot", new Fluids("nak_hot", FastColor.ARGB32.lerp(0.2f, 0xFFFFD5AC, 0xFFFFE5BC), Fluids.HOT_COOLANT_TYPE));

        addNAKPairs("iron", 0xFF8D1515, map);
        addNAKPairs("redstone", 0xFFAB1C09, map);
        addNAKPairs("quartz", 0xFFECE9E2, map);
        addNAKPairs("obsidian", 0xFF1C1828, map);
        addNAKPairs("nether_brick", 0xFF271317, map);
        addNAKPairs("glowstone", 0xFFA38037, map);
        addNAKPairs("lapis", 0xFF27438A, map);
        addNAKPairs("gold", 0xFFE6DA3C, map);
        addNAKPairs("prismarine", 0xFF70A695, map);
        addNAKPairs("slime", 0xFF79C865, map);
        addNAKPairs("end_stone", 0xFFE7E9B3, map);
        addNAKPairs("purpur", 0xFFA979A9, map);
        addNAKPairs("diamond", 0xFF6FDFDA, map);
        addNAKPairs("emerald", 0xFF51D975, map);
        addNAKPairs("copper", 0xFFAD6544, map);
        addNAKPairs("tin", 0xFFD9DDF0, map);
        addNAKPairs("lead", 0xFF3F4C4C, map);
        addNAKPairs("boron", 0xFF7D7D7D, map);
        addNAKPairs("lithium", 0xFFEFEFEF, map);
        addNAKPairs("magnesium", 0xFFEED5E1, map);
        addNAKPairs("manganese", 0xFF99A1CA, map);
        addNAKPairs("aluminum", 0xFFB5ECD5, map);
        addNAKPairs("silver", 0xFFE2DAF6, map);
        addNAKPairs("fluorite", 0xFF8AB492, map);
        addNAKPairs("villiaumite", 0xFFB06C56, map);
        addNAKPairs("carobbiite", 0xFF95A251, map);
        addNAKPairs("arsenic", 0xFF818475, map);
        addNAKPairs("liquid_nitrogen", 0xFF31C23A, map);
        addNAKPairs("liquid_helium", 0xFFF3433D, map);
        addNAKPairs("enderium", 0xFF0B5B5C, map);
        addNAKPairs("cryotheum", 0xFF0099C1, map);

        return map;
    }

    private static void addNAKPairs(String name, int colour, Map<String, Fluids> map) {
        map.put(name + "_nak", new Fluids(name + "_nak", FastColor.ARGB32.lerp(0.375f, colour, 0xFFE5BC), Fluids.COOLANT_TYPE));
        HOT_COOLANT_MAP.put(name + "_nak_hot", new Fluids(name + "_nak_hot", FastColor.ARGB32.lerp(0.2f, colour, 0xFFE5BC), Fluids.HOT_COOLANT_TYPE));
    }

    private static Map<String, Fluids> createHotGas() {
        Map<String, Fluids> map = new java.util.HashMap<>();
        map.put("arsenic", new Fluids("arsenic", 0xFF818475, Fluids.HOT_GAS_TYPE));
        map.put("sic_vapor", new Fluids("sic_vapor", 0xFF78746A, Fluids.HOT_GAS_TYPE));

        return map;
    }

    private static Map<String, Fluids> createFission() {
        Map<String, Fluids> map = new java.util.HashMap<>();
        map.put("strontium_90", new Fluids("strontium_90", 0xFFB8BE88, Fluids.FISSION_TYPE));
        map.put("molybdenum", new Fluids("molybdenum", 0xFFBCC5E4, Fluids.FISSION_TYPE));
        map.put("ruthenium_106", new Fluids("ruthenium_106", 0xFFA3A3A3, Fluids.FISSION_TYPE));
        map.put("caesium_137", new Fluids("caesium_137", 0xFFADADAD, Fluids.FISSION_TYPE));
        map.put("promethium_147", new Fluids("promethium_147", 0xFF96C199, Fluids.FISSION_TYPE));
        map.put("europium_155", new Fluids("europium_155", 0xFF74664A, Fluids.FISSION_TYPE));
        return map;
    }

    private static Map<String, Fluids> createFissionFuel() {
        Map<String, Fluids> map = new java.util.HashMap<>();

        // Isotopes
        map.put("uranium_233", new Fluids("uranium_233", 0xFF212E20, Fluids.MOLTEN_TYPE));
        map.put("uranium_235", new Fluids("uranium_235", 0xFF102D10, Fluids.MOLTEN_TYPE));
        map.put("uranium_238", new Fluids("uranium_238", 0xFF333E32, Fluids.MOLTEN_TYPE));
        map.put("neptunium_236", new Fluids("neptunium_236", 0xFF293E40, Fluids.MOLTEN_TYPE));
        map.put("neptunium_237", new Fluids("neptunium_237", 0xFF2E3A42, Fluids.MOLTEN_TYPE));
        map.put("plutonium_238", new Fluids("plutonium_238", 0xFF999999, Fluids.MOLTEN_TYPE));
        map.put("plutonium_239", new Fluids("plutonium_239", 0xFF9E9E9E, Fluids.MOLTEN_TYPE));
        map.put("plutonium_241", new Fluids("plutonium_241", 0xFF909090, Fluids.MOLTEN_TYPE));
        map.put("plutonium_242", new Fluids("plutonium_242", 0xFFABABAB, Fluids.MOLTEN_TYPE));
        map.put("americium_241", new Fluids("americium_241", 0xFF393725, Fluids.MOLTEN_TYPE));
        map.put("americium_242", new Fluids("americium_242", 0xFF332D1A, Fluids.MOLTEN_TYPE));
        map.put("americium_243", new Fluids("americium_243", 0xFF2C280C, Fluids.MOLTEN_TYPE));
        map.put("curium_243", new Fluids("curium_243", 0xFF311137, Fluids.MOLTEN_TYPE));
        map.put("curium_245", new Fluids("curium_245", 0xFF2D102D, Fluids.MOLTEN_TYPE));
        map.put("curium_246", new Fluids("curium_246", 0xFF3F2442, Fluids.MOLTEN_TYPE));
        map.put("curium_247", new Fluids("curium_247", 0xFF321635, Fluids.MOLTEN_TYPE));
        map.put("berkelium_247", new Fluids("berkelium_247", 0xFF5A2301, Fluids.MOLTEN_TYPE));
        map.put("berkelium_248", new Fluids("berkelium_248", 0xFF602502, Fluids.MOLTEN_TYPE));
        map.put("californium_249", new Fluids("californium_249", 0xFF460D12, Fluids.MOLTEN_TYPE));
        map.put("californium_250", new Fluids("californium_250", 0xFF3E0C14, Fluids.MOLTEN_TYPE));
        map.put("californium_251", new Fluids("californium_251", 0xFF3B0B16, Fluids.MOLTEN_TYPE));
        map.put("californium_252", new Fluids("californium_252", 0xFF430B0E, Fluids.MOLTEN_TYPE));

        // Fuels
        addFuelFluids("tbu", 0xFF272727, map);

        addFuelFluids("leu_233", 0xFF1D321B, map);
        addFuelFluids("heu_233", 0xFF123B0D, map);
        addFuelFluids("leu_235", 0xFF1D321B, map);
        addFuelFluids("heu_235", 0xFF123B0D, map);

        addFuelFluids("len_236", 0xFF1B3132, map);
        addFuelFluids("hen_236", 0xFF0D393B, map);

        addFuelFluids("lep_239", 0xFF949494, map);
        addFuelFluids("hep_239", 0xFF8A8A8A, map);
        addFuelFluids("lep_241", 0xFF969696, map);
        addFuelFluids("hep_241", 0xFF8C8C8C, map);

        addFuelFluids("mix_239", 0xFF485547, map);
        addFuelFluids("mix_241", 0xFF404E3E, map);

        addFuelFluids("lea_242", 0xFF322C1B, map);
        addFuelFluids("hea_242", 0xFF3B2F0D, map);

        addFuelFluids("lecm_243", 0xFF301B32, map);
        addFuelFluids("hecm_243", 0xFF360D3B, map);
        addFuelFluids("lecm_245", 0xFF2D1A2D, map);
        addFuelFluids("hecm_245", 0xFF340B2E, map);
        addFuelFluids("lecm_247", 0xFF331F35, map);
        addFuelFluids("hecm_247", 0xFF39103E, map);

        addFuelFluids("leb_248", 0xFF4A1B04, map);
        addFuelFluids("heb_248", 0xFF571B00, map);

        addFuelFluids("lecf_249", 0xFF3D0310, map);
        addFuelFluids("hecf_249", 0xFF4A0212, map);
        addFuelFluids("lecf_251", 0xFF3A0313, map);
        addFuelFluids("hecf_251", 0xFF460215, map);

        addFuelFluids("depleted_tbu", 0xFF1D3826, map);

        addFuelFluids("depleted_leu_233", 0xFF5C5C53, map);
        addFuelFluids("depleted_heu_233", 0xFF34484B, map);
        addFuelFluids("depleted_leu_235", 0xFF3A493D, map);
        addFuelFluids("depleted_heu_235", 0xFF4D5352, map);

        addFuelFluids("depleted_len_236", 0xFF4E4E46, map);
        addFuelFluids("depleted_hen_236", 0xFF646663, map);

        addFuelFluids("depleted_lep_239", 0xFF5C545D, map);
        addFuelFluids("depleted_hep_239", 0xFF392D2F, map);
        addFuelFluids("depleted_lep_241", 0xFF473441, map);
        addFuelFluids("depleted_hep_241", 0xFF40213A, map);

        addFuelFluids("depleted_mix_239", 0xFF2F342C, map);
        addFuelFluids("depleted_mix_241", 0xFF2F342F, map);

        addFuelFluids("depleted_lea_242", 0xFF311830, map);
        addFuelFluids("depleted_hea_242", 0xFF391C28, map);

        addFuelFluids("depleted_lecm_243", 0xFF441F1F, map);
        addFuelFluids("depleted_hecm_243", 0xFF401920, map);
        addFuelFluids("depleted_lecm_245", 0xFF49221A, map);
        addFuelFluids("depleted_hecm_245", 0xFF462119, map);
        addFuelFluids("depleted_lecm_247", 0xFF431512, map);
        addFuelFluids("depleted_hecm_247", 0xFF441415, map);

        addFuelFluids("depleted_leb_248", 0xFF471B22, map);
        addFuelFluids("depleted_heb_248", 0xFF481F26, map);

        addFuelFluids("depleted_lecf_249", 0xFF4B1B23, map);
        addFuelFluids("depleted_hecf_249", 0xFF491A22, map);
        addFuelFluids("depleted_lecf_251", 0xFF4A1B23, map);
        addFuelFluids("depleted_hecf_251", 0xFF4B1B23, map);
        return map;
    }

    public static void addFuelFluids(String name, int color, Map<String, Fluids> map) {
        map.put(name, new Fluids(name, color, Fluids.MOLTEN_TYPE));
        map.put(name + "_fluoride", new Fluids(name + "_fluoride", FastColor.ARGB32.lerp(0.125f, color, 0xD3C85D), Fluids.MOLTEN_TYPE));
        map.put(name + "_fluoride_flibe", new Fluids(name + "_fluoride_flibe", FastColor.ARGB32.lerp(0.4f, color, 0xC1C8B0), Fluids.MOLTEN_TYPE));
    }

    public static void init() {
    }
}