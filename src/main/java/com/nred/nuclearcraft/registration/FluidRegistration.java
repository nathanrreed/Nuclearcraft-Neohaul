package com.nred.nuclearcraft.registration;

import com.nred.nuclearcraft.info.Fluids;
import net.minecraft.util.FastColor;

import java.util.Map;

public class FluidRegistration {
    public static final Map<String, Fluids> GASSES = createGasses();
    public static final Map<String, Fluids> MOLTEN = createMolten();
    public static final Map<String, Fluids> HOT_GAS = createHotGas();
    public static final Map<String, Fluids> SUGAR = createSugar();
    public static final Map<String, Fluids> CHOCOLATE = createChocolate();
    public static final Map<String, Fluids> FISSION = createFission();
    public static final Map<String, Fluids> STEAM = createSteam();
    public static final Map<String, Fluids> SALT_SOLUTION = createSaltSolution();
    public static final Map<String, Fluids> ACID = createAcid();
    public static final Map<String, Fluids> FLAMMABLE = createFlammable();
    public static final Map<String, Fluids> HOT_COOLANT = createHotCoolant();
    public static final Map<String, Fluids> COOLANT = createCoolant();
    public static final Map<String, Fluids> CUSTOM_FLUID = createCustomFluid();

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
        map.put("ethene", new Fluids("ethene", 0xFFFFE4A3, Fluids.GAS_TYPE));
        map.put("fluoromethane", new Fluids("fluoromethane", 0xFF424C05, Fluids.GAS_TYPE));
        map.put("ammonia", new Fluids("ammonia", 0xFF7AC3A0, Fluids.GAS_TYPE));
        map.put("oxygen_difluoride", new Fluids("oxygen_difluoride", 0xFFEA1B01, Fluids.GAS_TYPE));
        map.put("diborane", new Fluids("diborane", 0xFFCC6E8C, Fluids.GAS_TYPE));
        map.put("sulfur_dioxide", new Fluids("sulfur_dioxide", 0xFFC3BC7A, Fluids.GAS_TYPE));
        map.put("sulfur_trioxide", new Fluids("sulfur_trioxide", 0xFFD3AE5D, Fluids.GAS_TYPE));
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

//        if (registerCoFHAlt()) { //TODO add
//            map.put("coal", new Fluids("coal", 0xFF7D7D7D, Fluids.MOLTEN_TYPE));
//        }
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
        map.put("bas", new Fluids("bas", 0xFF9B9B89, Fluids.MOLTEN_TYPE));
        map.put("alugentum", new Fluids("alugentum", 0xFFB5C9CB, Fluids.MOLTEN_TYPE));
        map.put("alumina", new Fluids("alumina", 0xFF919880, Fluids.MOLTEN_TYPE));
        //TODO add molten ores
        return map;
    }

    private static Map<String, Fluids> createCustomFluid() {
        Map<String, Fluids> map = new java.util.HashMap<>();
        map.put("liquid_helium", new Fluids("liquid_helium", true, -1, 150, 4, 1, 0));
        map.put("heavy_water", new Fluids("heavy_water", false, -1));
        map.put("liquid_nitrogen", new Fluids("liquid_nitrogen", "liquid", false, false, 0xFF31C23A, 810, 70, 170, 0));
        //if (registerCoFHAlt()) { TODO
        //    map.put("ender", new Fluids("ender", "liquid", true, false, 0xFF14584D, 4000, 300, 2500, 3));
        //    map.put("cryotheum", new Fluids("cryotheum", "liquid", false, 0xFF0099C1));
        //}
        map.put("plasma", new Fluids("plasma", true, false, -1, 50, 1000000, 100, 0));
        map.put("radaway", new Fluids("radaway", false, -1));
        map.put("radaway_slow", new Fluids("radaway_slow", false, -1));
        map.put("redstone_ethanol", new Fluids("redstone_ethanol", false, -1));
        map.put("corium", new Fluids("corium", "liquid", 0xFF7C7C6F, false, false));
        map.put("ice", new Fluids("ice", "liquid", false, false, 0xFFAFF1FF, 1000, 250, 2000, 0));
        map.put("slurry_ice", new Fluids("slurry_ice", "liquid", false, false, 0xFF7EAEB7, 950, 270, 4000, 0));
        map.put("emergency_coolant", new Fluids("emergency_coolant", "liquid", false, false, 0xFF6DD0E7, 2000, 100, 2000, 3));
        map.put("emergency_coolant_heated", new Fluids("emergency_coolant_heated", "liquid", false, false, 0xFFCDBEE7, 2000, 300, 1500, 9));
        map.put("preheated_water", new Fluids("preheated_water", "liquid", false, false, 0xFF2F43F4, 1000, 400, 250, 0));
        map.put("condensate_water", new Fluids("condensate_water", "liquid", false, false, 0xFF2F43F4, 1000, 300, 850, 0));
        return map;
    }

    private static Map<String, Fluids> createFlammable() {
        Map<String, Fluids> map = new java.util.HashMap<>();
        map.put("ethanol", new Fluids("ethanol", 0xFF655140, Fluids.FLAMMABLE_TYPE));
        map.put("methanol", new Fluids("methanol", 0xFF71524C, Fluids.FLAMMABLE_TYPE));
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
        map.put("boron_nitride_solution", new Fluids("boron_nitride_solution", FastColor.ARGB32.lerp(0.5f, 0x2F43F4, 0xFF6F8E5C), Fluids.SALT_SOLUTION_TYPE));
        map.put("fluorite_water", new Fluids("fluorite_water", FastColor.ARGB32.lerp(0.5f, 0x2F43F4, 0xFF8AB492), Fluids.SALT_SOLUTION_TYPE));
        map.put("calcium_sulfate_solution", new Fluids("calcium_sulfate_solution", FastColor.ARGB32.lerp(0.5f, 0x2F43F4, 0xFFB8B0A6), Fluids.SALT_SOLUTION_TYPE));
        map.put("sodium_fluoride_solution", new Fluids("sodium_fluoride_solution", FastColor.ARGB32.lerp(0.5f, 0x2F43F4, 0xFFC2B1A1), Fluids.SALT_SOLUTION_TYPE));
        map.put("potassium_fluoride_solution", new Fluids("potassium_fluoride_solution", FastColor.ARGB32.lerp(0.5f, 0x2F43F4, 0xFFC1C99D), Fluids.SALT_SOLUTION_TYPE));
        map.put("sodium_hydroxide_solution", new Fluids("sodium_hydroxide_solution", FastColor.ARGB32.lerp(0.5f, 0x2F43F4, 0xFFC2B7BB), Fluids.SALT_SOLUTION_TYPE));
        map.put("potassium_hydroxide_solution", new Fluids("potassium_hydroxide_solution", FastColor.ARGB32.lerp(0.5f, 0x2F43F4, 0xFFB8C6B0), Fluids.SALT_SOLUTION_TYPE));
        map.put("borax_solution", new Fluids("borax_solution", FastColor.ARGB32.lerp(0.5f, 0x2F43F4, 0xFFEEEEEE), Fluids.SALT_SOLUTION_TYPE));
        map.put("irradiated_borax_solution", new Fluids("irradiated_borax_solution", FastColor.ARGB32.lerp(0.5f, 0x2F43F4, 0xFFFFD0A3), Fluids.SALT_SOLUTION_TYPE));
        return map;
    }

    private static Map<String, Fluids> createChocolate() {
        Map<String, Fluids> map = new java.util.HashMap<>();
        map.put("chocolate_liquor", new Fluids("chocolate_liquor", 0xFF41241C, Fluids.CHOCOLATE_TYPE));
        map.put("cocoa_butter", new Fluids("cocoa_butter", 0xFFF6EEBF, Fluids.CHOCOLATE_TYPE));
        map.put("unsweetened_chocolate", new Fluids("unsweetened_chocolate", 0xFF2C0A08, Fluids.CHOCOLATE_TYPE));
        map.put("dark_chocolate", new Fluids("dark_chocolate", 0xFF2C0B06, Fluids.CHOCOLATE_TYPE));
        map.put("milk_chocolate", new Fluids("milk_chocolate", 0xFF884121, Fluids.CHOCOLATE_TYPE));
        map.put("marshmallow", new Fluids("marshmallow", 0xFFE1E1E3, Fluids.CHOCOLATE_TYPE));
        return map;
    }

    private static Map<String, Fluids> createSugar() {
        Map<String, Fluids> map = new java.util.HashMap<>();
        map.put("sugar", new Fluids("sugar", 0xFFFFD59A, Fluids.SUGAR_TYPE));
        map.put("gelatin", new Fluids("gelatin", 0xFFDDD09C, Fluids.CHOCOLATE_TYPE));
        map.put("hydrated_gelatin", new Fluids("hydrated_gelatin", FastColor.ARGB32.lerp(0.8f, 0x2F43F4, 0xFFDDD09C), Fluids.CHOCOLATE_TYPE));
        return map;
    }

    private static Map<String, Fluids> createSteam() {
        Map<String, Fluids> map = new java.util.HashMap<>();
//        if (registerCoFHAlt()) {
//            map.put("steam", new Fluids("steam", 0xFF929292, Fluids.STEAM_TYPE, 800));
//        }
        map.put("high_pressure_steam", new Fluids("high_pressure_steam", 0xFFBDBDBD, Fluids.STEAM_TYPE, 1200));
        map.put("exhaust_steam", new Fluids("exhaust_steam", 0xFFBDBDBD, Fluids.STEAM_TYPE, 500));
        map.put("low_pressure_steam", new Fluids("low_pressure_steam", 0xFFBDBDBD, Fluids.STEAM_TYPE, 800));
        map.put("low_quality_steam", new Fluids("low_quality_steam", 0xFFBDBDBD, Fluids.STEAM_TYPE, 350));
        return map;
    }

    private static Map<String, Fluids> createCoolant() {
        Map<String, Fluids> map = new java.util.HashMap<>();
        map.put("nak", new Fluids("nak", 0xFFFFE5BC, Fluids.COOLANT_TYPE));
        // TODO add all NaK
        return map;
    }

    private static Map<String, Fluids> createHotCoolant() {
        Map<String, Fluids> map = new java.util.HashMap<>();
        map.put("nak_hot", new Fluids("nak_hot", FastColor.ARGB32.lerp(0.2f, 0xFFFFD5AC, 0xFFFFE5BC), Fluids.HOT_COOLANT_TYPE));
        // TODO add all NaK
        return map;
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

    public static void init() {
    }
}
