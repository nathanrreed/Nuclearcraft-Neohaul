package com.nred.nuclearcraft.registration;

import com.nred.nuclearcraft.block.fluid.CoriumFluidBlock;
import com.nred.nuclearcraft.block.fluid.LiquidFluidBlock;
import com.nred.nuclearcraft.block.fluid.SuperFluidBlock;
import com.nred.nuclearcraft.fluid.CoriumFluid;
import com.nred.nuclearcraft.fluid.PlasmaFluid;
import com.nred.nuclearcraft.info.NCFluid;
import com.nred.nuclearcraft.info.NCFluidMaker;
import com.nred.nuclearcraft.util.ColorHelper;
import net.minecraft.util.FastColor;
import org.apache.commons.lang3.tuple.Triple;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static com.nred.nuclearcraft.registration.Registers.*;

public class FluidRegistration {
    private static final NCFluidMaker MAKER = new NCFluidMaker(FLUID_TYPES, FLUIDS, BLOCKS, ITEMS);

    public static final Map<String, NCFluid> GAS_MAP = createGasses();
    public static final Map<String, NCFluid> MOLTEN_MAP = createMolten();
    public static final Map<String, NCFluid> HOT_GAS_MAP = createHotGas();
    public static final Map<String, NCFluid> SUGAR_MAP = createSugar();
    public static final Map<String, NCFluid> CHOCOLATE_MAP = createChocolate();
    public static final Map<String, NCFluid> FISSION_FLUID_MAP = createFission();
    public static final Map<String, NCFluid> STEAM_MAP = createSteam();
    public static final Map<String, NCFluid> SALT_SOLUTION_MAP = createSaltSolution();
    public static final Map<String, NCFluid> ACID_MAP = createAcid();
    public static final Map<String, NCFluid> FLAMMABLE_MAP = createFlammable();
    public static final Map<String, NCFluid> SOUL_MAP = createSoul();
    public static final Map<String, NCFluid> HOT_COOLANT_MAP = new HashMap<>();
    public static final Map<String, NCFluid> COOLANT_MAP = createCoolant();
    public static final Map<String, NCFluid> CUSTOM_FLUID_MAP = createCustomFluid();
    public static final Map<String, NCFluid> FISSION_FUEL_MAP = createFissionFuel();

    private static Map<String, NCFluid> createGasses() {
        Map<String, NCFluid> map = new java.util.HashMap<>();
        map.put("oxygen", MAKER.registerFluid("oxygen", 0xFF7E8CC8, NCFluid.GAS_TYPE));
        map.put("hydrogen", MAKER.registerFluid("hydrogen", 0xFFB37AC4, NCFluid.GAS_TYPE));
        map.put("deuterium", MAKER.registerFluid("deuterium", 0xFF9E6FEF, NCFluid.GAS_TYPE));
        map.put("tritium", MAKER.registerFluid("tritium", 0xFF5DBBD6, NCFluid.GAS_TYPE));
        map.put("helium_3", MAKER.registerFluid("helium_3", 0xFFCBBB67, NCFluid.GAS_TYPE));
        map.put("helium", MAKER.registerFluid("helium", 0xFFC57B81, NCFluid.GAS_TYPE));

        map.put("nitrogen", MAKER.registerFluid("nitrogen", 0xFF7CC37B, NCFluid.GAS_TYPE));
        map.put("fluorine", MAKER.registerFluid("fluorine", 0xFFD3C75D, NCFluid.GAS_TYPE));

        map.put("methane", MAKER.registerFluid("methane", 0xFFD9AFB3, NCFluid.GAS_TYPE));
        map.put("carbon_dioxide", MAKER.registerFluid("carbon_dioxide", 0xFF5C635A, NCFluid.GAS_TYPE));
        map.put("carbon_monoxide", MAKER.registerFluid("carbon_monoxide", 0xFF4C5649, NCFluid.GAS_TYPE));
        map.put("ethene", MAKER.registerFluid("ethene", 0xFFFFE4A3, NCFluid.GAS_TYPE));
        map.put("ethyne", MAKER.registerFluid("ethyne", 0xFFFFE442, NCFluid.GAS_TYPE));

        map.put("fluoromethane", MAKER.registerFluid("fluoromethane", 0xFF424C05, NCFluid.GAS_TYPE));
        map.put("ammonia", MAKER.registerFluid("ammonia", 0xFF7AC3A0, NCFluid.GAS_TYPE));
        map.put("oxygen_difluoride", MAKER.registerFluid("oxygen_difluoride", 0xFFEA1B01, NCFluid.GAS_TYPE));
        map.put("diborane", MAKER.registerFluid("diborane", 0xFFCC6E8C, NCFluid.GAS_TYPE));
        map.put("sulfur_dioxide", MAKER.registerFluid("sulfur_dioxide", 0xFFC3BC7A, NCFluid.GAS_TYPE));
        map.put("sulfur_trioxide", MAKER.registerFluid("sulfur_trioxide", 0xFFD3AE5D, NCFluid.GAS_TYPE));
        map.put("sulfur_hexafluoride", MAKER.registerFluid("sulfur_hexafluoride", 0xFFC6FC46, NCFluid.GAS_TYPE));
        map.put("tetrafluoroethene", MAKER.registerFluid("tetrafluoroethene", 0xFF7EA542, NCFluid.GAS_TYPE));
        map.put("hydrogen_sulfide", MAKER.registerFluid("hydrogen_sulfide", 0xFF785830, NCFluid.GAS_TYPE));
        map.put("depleted_hydrogen_sulfide", MAKER.registerFluid("depleted_hydrogen_sulfide", 0xFF59514E, NCFluid.GAS_TYPE));

        return map;
    }

    private static Map<String, NCFluid> createMolten() {
        Map<String, NCFluid> map = new java.util.HashMap<>();
        map.put("boron_10", MAKER.registerFluid("boron_10", 0xFF7D7D7D, NCFluid.MOLTEN_TYPE));
        map.put("boron_11", MAKER.registerFluid("boron_11", 0xFF7D7D7D, NCFluid.MOLTEN_TYPE));
        map.put("lithium_6", MAKER.registerFluid("lithium_6", 0xFFEFEFEF, NCFluid.MOLTEN_TYPE));
        map.put("lithium_7", MAKER.registerFluid("lithium_7", 0xFFEFEFEF, NCFluid.MOLTEN_TYPE));

        map.put("steel", MAKER.registerFluid("steel", 0xFF7B7B7B, NCFluid.MOLTEN_TYPE));
        map.put("ferroboron", MAKER.registerFluid("ferroboron", 0xFF4A4A4A, NCFluid.MOLTEN_TYPE));
        map.put("tough", MAKER.registerFluid("tough", 0xFF150F21, NCFluid.MOLTEN_TYPE));
        map.put("hard_carbon", MAKER.registerFluid("hard_carbon", 0xFF202020, NCFluid.MOLTEN_TYPE));
        map.put("hastelloy", MAKER.registerFluid("hastelloy", 0xFF8F9C9D, NCFluid.MOLTEN_TYPE));
        map.put("nichrome", MAKER.registerFluid("nichrome", 0xFF5F6958, NCFluid.MOLTEN_TYPE));
        map.put("coal", MAKER.registerFluid("coal", 0xFF7D7D7D, NCFluid.MOLTEN_TYPE));

        map.put("beryllium", MAKER.registerFluid("beryllium", 0xFFD4DBC2, NCFluid.MOLTEN_TYPE));
        map.put("zirconium", MAKER.registerFluid("zirconium", 0xFFE0E0B8, NCFluid.MOLTEN_TYPE));
        map.put("manganese_dioxide", MAKER.registerFluid("manganese_dioxide", 0xFF28211E, NCFluid.MOLTEN_TYPE));
        map.put("sulfur", MAKER.registerFluid("sulfur", 0xFFDEDE7A, NCFluid.MOLTEN_TYPE));
        map.put("barium", MAKER.registerFluid("barium", 0xFF4B4B4B, NCFluid.MOLTEN_TYPE));
        map.put("barium_oxide", MAKER.registerFluid("barium_oxide", 0xFFC7D4D6, NCFluid.MOLTEN_TYPE));
        map.put("nickel", MAKER.registerFluid("nickel", 0xFFA3A998, NCFluid.MOLTEN_TYPE));
        map.put("nickel_oxide", MAKER.registerFluid("nickel_oxide", 0xFF435E49, NCFluid.MOLTEN_TYPE));
        map.put("palladium", MAKER.registerFluid("palladium", 0xFF767676, NCFluid.MOLTEN_TYPE));
        map.put("chromium", MAKER.registerFluid("chromium", 0xFFE7E7E7, NCFluid.MOLTEN_TYPE));
        map.put("holmium", MAKER.registerFluid("holmium", 0xFFDCB49C, NCFluid.MOLTEN_TYPE));
        map.put("dysprosium", MAKER.registerFluid("dysprosium", 0xFFC381E4, NCFluid.MOLTEN_TYPE));
        map.put("gadolinium", MAKER.registerFluid("gadolinium", 0xFF99C3F2, NCFluid.MOLTEN_TYPE));

        map.put("lead_platinum", MAKER.registerFluid("lead_platinum", 0xFF415B60, NCFluid.MOLTEN_TYPE));
        map.put("enderium", MAKER.registerFluid("enderium", 0xFF0B5B5C, NCFluid.MOLTEN_TYPE));

        map.put("lif", MAKER.registerFluid("lif", 0xFFCDCDCB, NCFluid.MOLTEN_TYPE));
        map.put("bef2", MAKER.registerFluid("bef2", 0xFFBEC6AA, NCFluid.MOLTEN_TYPE));
        map.put("flibe", MAKER.registerFluid("flibe", 0xFFC1C8B0, NCFluid.MOLTEN_TYPE));
        map.put("naoh", MAKER.registerFluid("naoh", 0xFFC2B7BB, NCFluid.MOLTEN_TYPE));
        map.put("koh", MAKER.registerFluid("koh", 0xFFB8C6B0, NCFluid.MOLTEN_TYPE));
        map.put("barium_sulfide", MAKER.registerFluid("barium_sulfide", 0xFFBDA776, NCFluid.MOLTEN_TYPE));
        map.put("bacro_nio", MAKER.registerFluid("bacro_nio", 0xFF414641, NCFluid.MOLTEN_TYPE));
        map.put("bacro", MAKER.registerFluid("bacro", 0xFF5E615E, NCFluid.MOLTEN_TYPE));
        map.put("baalo", MAKER.registerFluid("baalo", 0xFF857958, NCFluid.MOLTEN_TYPE));
        map.put("aluminum_sulfide", MAKER.registerFluid("aluminum_sulfide", 0xFFBEFFA2, NCFluid.MOLTEN_TYPE));
        map.put("nickel_sulfide", MAKER.registerFluid("nickel_sulfide", 0xFFCBD3AD, NCFluid.MOLTEN_TYPE));

        map.put("dfdps", MAKER.registerFluid("dfdps", 0xFFB4B3A7, NCFluid.MOLTEN_TYPE));
        map.put("polyphenylene_sulfide", MAKER.registerFluid("polyphenylene_sulfide", 0xFF3F3D3E, NCFluid.MOLTEN_TYPE));
        map.put("polydimethylsilylene", MAKER.registerFluid("polydimethylsilylene", 0xFF774F60, NCFluid.MOLTEN_TYPE));
        map.put("polymethylsilylene_methylene", MAKER.registerFluid("polymethylsilylene_methylene", 0xFF5A5246, NCFluid.MOLTEN_TYPE));
        map.put("polyethersulfone", MAKER.registerFluid("polyethersulfone", 0xFFC9B8A6, NCFluid.MOLTEN_TYPE));
        map.put("polytetrafluoroethene", MAKER.registerFluid("polytetrafluoroethene", 0xFF7F9F4D, NCFluid.MOLTEN_TYPE));

        map.put("sodium", MAKER.registerFluid("sodium", 0xFFC1898C, NCFluid.MOLTEN_TYPE));
        map.put("potassium", MAKER.registerFluid("potassium", 0xFFB8C503, NCFluid.MOLTEN_TYPE));

        map.put("sodium_sulfide", MAKER.registerFluid("sodium_sulfide", 0xFF9A8B0B, NCFluid.MOLTEN_TYPE));
        map.put("potassium_sulfide", MAKER.registerFluid("potassium_sulfide", 0xFF917C34, NCFluid.MOLTEN_TYPE));

        map.put("silicon", MAKER.registerFluid("silicon", 0xFF676767, NCFluid.MOLTEN_TYPE));
        map.put("bas", MAKER.registerFluid("bas", 0xFF9B9B89, NCFluid.MOLTEN_TYPE));

        map.put("alugentum", MAKER.registerFluid("alugentum", 0xFFB5C9CB, NCFluid.MOLTEN_TYPE));
        map.put("alumina", MAKER.registerFluid("alumina", 0xFF919880, NCFluid.MOLTEN_TYPE));

        map.put("iron", MAKER.registerFluid("iron", 0xFF8D1515, NCFluid.MOLTEN_TYPE));
        map.put("redstone", MAKER.registerFluid("redstone", 0xFFAB1C09, NCFluid.MOLTEN_TYPE));
        map.put("quartz", MAKER.registerFluid("quartz", 0xFFECE9E2, NCFluid.MOLTEN_TYPE));
        map.put("obsidian", MAKER.registerFluid("obsidian", 0xFF1C1828, NCFluid.MOLTEN_TYPE));
        map.put("nether_brick", MAKER.registerFluid("nether_brick", 0xFF271317, NCFluid.MOLTEN_TYPE));
        map.put("glowstone", MAKER.registerFluid("glowstone", 0xFFA38037, NCFluid.GLOWSTONE_TYPE));
        map.put("lapis", MAKER.registerFluid("lapis", 0xFF27438A, NCFluid.MOLTEN_TYPE));
        map.put("gold", MAKER.registerFluid("gold", 0xFFE6DA3C, NCFluid.MOLTEN_TYPE));
        map.put("prismarine", MAKER.registerFluid("prismarine", 0xFF70A695, NCFluid.MOLTEN_TYPE));
        map.put("slime", MAKER.registerFluid("slime", 0xFF79C865, NCFluid.MOLTEN_TYPE));
        map.put("end_stone", MAKER.registerFluid("end_stone", 0xFFE7E9B3, NCFluid.MOLTEN_TYPE));
        map.put("purpur", MAKER.registerFluid("purpur", 0xFFA979A9, NCFluid.MOLTEN_TYPE));
        map.put("diamond", MAKER.registerFluid("diamond", 0xFF6FDFDA, NCFluid.MOLTEN_TYPE));
        map.put("emerald", MAKER.registerFluid("emerald", 0xFF51D975, NCFluid.MOLTEN_TYPE));
        map.put("copper", MAKER.registerFluid("copper", 0xFFAD6544, NCFluid.MOLTEN_TYPE));
        map.put("tin", MAKER.registerFluid("tin", 0xFFD9DDF0, NCFluid.MOLTEN_TYPE));
        map.put("lead", MAKER.registerFluid("lead", 0xFF3F4C4C, NCFluid.MOLTEN_TYPE));
        map.put("boron", MAKER.registerFluid("boron", 0xFF7D7D7D, NCFluid.MOLTEN_TYPE));
        map.put("lithium", MAKER.registerFluid("lithium", 0xFFEFEFEF, NCFluid.MOLTEN_TYPE));
        map.put("magnesium", MAKER.registerFluid("magnesium", 0xFFEED5E1, NCFluid.MOLTEN_TYPE));
        map.put("manganese", MAKER.registerFluid("manganese", 0xFF99A1CA, NCFluid.MOLTEN_TYPE));
        map.put("aluminum", MAKER.registerFluid("aluminum", 0xFFB5ECD5, NCFluid.MOLTEN_TYPE));
        map.put("silver", MAKER.registerFluid("silver", 0xFFE2DAF6, NCFluid.MOLTEN_TYPE));
        map.put("fluorite", MAKER.registerFluid("fluorite", 0xFF8AB492, NCFluid.MOLTEN_TYPE));
        map.put("villiaumite", MAKER.registerFluid("villiaumite", 0xFFB06C56, NCFluid.MOLTEN_TYPE));
        map.put("carobbiite", MAKER.registerFluid("carobbiite", 0xFF95A251, NCFluid.MOLTEN_TYPE));

        map.put("thorium", MAKER.registerFluid("thorium", 0xFF242424, NCFluid.FISSION_TYPE));
        map.put("uranium", MAKER.registerFluid("uranium", 0xFF375437, NCFluid.FISSION_TYPE));
        return map;
    }

    private static Map<String, NCFluid> createCustomFluid() {
        Map<String, NCFluid> map = new java.util.HashMap<>();
        map.put("liquid_helium", MAKER.registerFluid("liquid_helium", true, -1, 150, 4, 1, 0, SuperFluidBlock::new));
        map.put("le_water", MAKER.registerFluid("le_water", false, -1, LiquidFluidBlock::new));
        map.put("he_water", MAKER.registerFluid("he_water", false, -1, LiquidFluidBlock::new));
        map.put("heavy_water", MAKER.registerFluid("heavy_water", false, -1, LiquidFluidBlock::new));
        map.put("hydrogen_peroxide", MAKER.registerFluid("hydrogen_peroxide", false, -1, LiquidFluidBlock::new));
        map.put("liquid_nitrogen", MAKER.registerFluid("liquid_nitrogen", "liquid", false, false, 0xFF31C23A, 810, 70, 170, 0, LiquidFluidBlock::new));
        map.put("ender", MAKER.registerFluid("ender", "liquid_opaque", false, false, 0xFF14584D, 4000, 300, 2500, 3, LiquidFluidBlock::new));
        map.put("cryotheum", MAKER.registerFluid("cryotheum", 0xFF0099C1, NCFluid.CRYOTHEUM_TYPE));
        map.put("plasma", new NCFluid(MAKER, "plasma", -1, NCFluid.PLASMA_TYPE, PlasmaFluid.Source::new, PlasmaFluid.Flowing::new));
        map.put("radaway", MAKER.registerFluid("radaway", false, -1, LiquidFluidBlock::new));
        map.put("radaway_slow", MAKER.registerFluid("radaway_slow", false, -1, LiquidFluidBlock::new));
        map.put("corium", new NCFluid(MAKER, "corium", 0xFF7C7C6F, new NCFluid.TypeInfo(NCFluid.MOLTEN_TYPE, CoriumFluidBlock::new), CoriumFluid.Source::new, CoriumFluid.Flowing::new));
        map.put("ice", MAKER.registerFluid("ice", "liquid", false, false, 0xFFAFF1FF, 1000, 250, 2000, 0, LiquidFluidBlock::new));
        map.put("slurry_ice", MAKER.registerFluid("slurry_ice", "liquid", false, false, 0xFF7EAEB7, 950, 270, 4000, 0, LiquidFluidBlock::new));
        map.put("emergency_coolant", MAKER.registerFluid("emergency_coolant", "liquid_opaque", false, false, 0xFF6DD0E7, 2000, 100, 2000, 0, LiquidFluidBlock::new));
        map.put("emergency_coolant_heated", MAKER.registerFluid("emergency_coolant_heated", "liquid_opaque", false, false, 0xFFCDBEE7, 2000, 400, 1500, 7, LiquidFluidBlock::new));
        map.put("preheated_water", MAKER.registerFluid("preheated_water", "liquid", false, false, 0xFF1D35F2, 1000, 400, 250, 0, LiquidFluidBlock::new));
        map.put("condensate_water", MAKER.registerFluid("condensate_water", "liquid", false, false, 0xFF2F43F4, 1000, 350, 850, 0, LiquidFluidBlock::new));
        return map;
    }

    private static Map<String, NCFluid> createFlammable() {
        Map<String, NCFluid> map = new java.util.HashMap<>();
        map.put("ethanol", MAKER.registerFluid("ethanol", 0xFF655140, NCFluid.FLAMMABLE_TYPE));
        map.put("methanol", MAKER.registerFluid("methanol", 0xFF71524C, NCFluid.FLAMMABLE_TYPE));
        map.put("benzene", MAKER.registerFluid("benzene", 0xFF999999, NCFluid.FLAMMABLE_TYPE));
        map.put("phenol", MAKER.registerFluid("phenol", 0xFFF2F2F2, NCFluid.FLAMMABLE_TYPE));
        map.put("fluorobenzene", MAKER.registerFluid("fluorobenzene", 0xFFBAB58B, NCFluid.FLAMMABLE_TYPE));
        map.put("difluorobenzene", MAKER.registerFluid("difluorobenzene", 0xFF8CB57B, NCFluid.FLAMMABLE_TYPE));
        map.put("dimethyldifluorosilane", MAKER.registerFluid("dimethyldifluorosilane", 0xFFAEAF80, NCFluid.FLAMMABLE_TYPE));
        map.put("redstone_ethanol", MAKER.registerFluid("redstone_ethanol", false, -1, LiquidFluidBlock::new));
        return map;
    }

    private static Map<String, NCFluid> createAcid() {
        Map<String, NCFluid> map = new java.util.HashMap<>();
        map.put("hydrofluoric_acid", MAKER.registerFluid("hydrofluoric_acid", 0xFF004C05, NCFluid.ACID_TYPE));
        map.put("boric_acid", MAKER.registerFluid("boric_acid", 0xFF696939, NCFluid.ACID_TYPE));
        map.put("sulfuric_acid", MAKER.registerFluid("sulfuric_acid", 0xFF454500, NCFluid.ACID_TYPE));
        map.put("orthosilicic_acid", MAKER.registerFluid("orthosilicic_acid", 0xFFB8B8B8, NCFluid.ACID_TYPE));
        return map;
    }

    private static Map<String, NCFluid> createSaltSolution() {
        Map<String, NCFluid> map = new java.util.HashMap<>();
        map.put("boron_nitride_solution", MAKER.registerFluid("boron_nitride_solution", FastColor.ARGB32.lerp(0.5f, 0xFF2F43F4, 0xFF6F8E5C), NCFluid.SALT_SOLUTION_TYPE));
        map.put("fluorite_water", MAKER.registerFluid("fluorite_water", FastColor.ARGB32.lerp(0.5f, 0xFF2F43F4, 0xFF8AB492), NCFluid.SALT_SOLUTION_TYPE));
        map.put("calcium_sulfate_solution", MAKER.registerFluid("calcium_sulfate_solution", FastColor.ARGB32.lerp(0.5f, 0xFF2F43F4, 0xFFB8B0A6), NCFluid.SALT_SOLUTION_TYPE));
        map.put("sodium_fluoride_solution", MAKER.registerFluid("sodium_fluoride_solution", FastColor.ARGB32.lerp(0.5f, 0xFF2F43F4, 0xFFC2B1A1), NCFluid.SALT_SOLUTION_TYPE));
        map.put("potassium_fluoride_solution", MAKER.registerFluid("potassium_fluoride_solution", FastColor.ARGB32.lerp(0.5f, 0xFF2F43F4, 0xFFC1C99D), NCFluid.SALT_SOLUTION_TYPE));
        map.put("sodium_hydroxide_solution", MAKER.registerFluid("sodium_hydroxide_solution", FastColor.ARGB32.lerp(0.5f, 0xFF2F43F4, 0xFFC2B7BB), NCFluid.SALT_SOLUTION_TYPE));
        map.put("potassium_hydroxide_solution", MAKER.registerFluid("potassium_hydroxide_solution", FastColor.ARGB32.lerp(0.5f, 0xFF2F43F4, 0xFFB8C6B0), NCFluid.SALT_SOLUTION_TYPE));
        map.put("borax_solution", MAKER.registerFluid("borax_solution", FastColor.ARGB32.lerp(0.5f, 0xFF2F43F4, 0xFFEEEEEE), NCFluid.SALT_SOLUTION_TYPE));
        map.put("irradiated_borax_solution", MAKER.registerFluid("irradiated_borax_solution", FastColor.ARGB32.lerp(0.5f, 0xFF2F43F4, 0xFFFFD0A3), NCFluid.SALT_SOLUTION_TYPE));
        map.put("ammonium_sulfate_solution", MAKER.registerFluid("ammonium_sulfate_solution", FastColor.ARGB32.lerp(0.5f, 0xFF2F43F4, 0xFF6CA377), NCFluid.SALT_SOLUTION_TYPE));
        map.put("ammonium_bisulfate_solution", MAKER.registerFluid("ammonium_bisulfate_solution", FastColor.ARGB32.lerp(0.5f, 0xFF2F43F4, 0xFF5F8450), NCFluid.SALT_SOLUTION_TYPE));
        map.put("ammonium_persulfate_solution", MAKER.registerFluid("ammonium_persulfate_solution", FastColor.ARGB32.lerp(0.5f, 0xFF2F43F4, 0xFF74A364), NCFluid.SALT_SOLUTION_TYPE));
        map.put("hydroquinone_solution", MAKER.registerFluid("hydroquinone_solution", FastColor.ARGB32.lerp(0.5f, 0xFF2F43F4, 0xFFB7B7B7), NCFluid.SALT_SOLUTION_TYPE));
        map.put("sodium_hydroquinone_solution", MAKER.registerFluid("sodium_hydroquinone_solution", FastColor.ARGB32.lerp(0.5f, 0xFF2F43F4, 0xFFC9B9BA), NCFluid.SALT_SOLUTION_TYPE));
        map.put("potassium_hydroquinone_solution", MAKER.registerFluid("potassium_hydroquinone_solution", FastColor.ARGB32.lerp(0.5f, 0xFF2F43F4, 0xFFC6CA94), NCFluid.SALT_SOLUTION_TYPE));
        map.put("dysprholminite_water", MAKER.registerFluid("dysprholminite_water", FastColor.ARGB32.lerp(0.5f, 0xFF2F43F4, 0xFF454215), NCFluid.SALT_SOLUTION_TYPE));
        map.put("hodybeso_solution", MAKER.registerFluid("hodybeso_solution", FastColor.ARGB32.lerp(0.5f, 0xFF2F43F4, 0xFFCAB94E), NCFluid.SALT_SOLUTION_TYPE));
        return map;
    }

    private static Map<String, NCFluid> createChocolate() {
        Map<String, NCFluid> map = new java.util.HashMap<>();
        map.put("chocolate_liquor", MAKER.registerFluid("chocolate_liquor", 0xFF41241C, NCFluid.CHOCOLATE_TYPE));
        map.put("cocoa_butter", MAKER.registerFluid("cocoa_butter", 0xFFF6EEBF, NCFluid.CHOCOLATE_TYPE));
        map.put("unsweetened_chocolate", MAKER.registerFluid("unsweetened_chocolate", 0xFF2C0A08, NCFluid.CHOCOLATE_TYPE));
        map.put("dark_chocolate", MAKER.registerFluid("dark_chocolate", 0xFF2C0B06, NCFluid.CHOCOLATE_TYPE));
        map.put("milk_chocolate", MAKER.registerFluid("milk_chocolate", 0xFF884121, NCFluid.CHOCOLATE_TYPE));
        return map;
    }

    private static Map<String, NCFluid> createSugar() {
        Map<String, NCFluid> map = new java.util.HashMap<>();
        map.put("sugar", MAKER.registerFluid("sugar", 0xFFFFD59A, NCFluid.SUGAR_TYPE));
        map.put("gelatin", MAKER.registerFluid("gelatin", 0xFFDDD09C, NCFluid.CHOCOLATE_TYPE));
        map.put("hydrated_gelatin", MAKER.registerFluid("hydrated_gelatin", FastColor.ARGB32.lerp(0.8f, 0xFF2F43F4, 0xFFDDD09C), NCFluid.CHOCOLATE_TYPE));
        map.put("marshmallow", MAKER.registerFluid("marshmallow", 0xFFE1E1E3, NCFluid.CHOCOLATE_TYPE));
        return map;
    }

    private static Map<String, NCFluid> createSteam() {
        Map<String, NCFluid> map = new java.util.HashMap<>();
        map.put("steam", MAKER.registerFluid("steam", 0xFF929292, NCFluid.STEAM_TYPE, 800));
        map.put("high_pressure_steam", MAKER.registerFluid("high_pressure_steam", 0xFFBDBDBD, NCFluid.STEAM_TYPE, 1200));
        map.put("exhaust_steam", MAKER.registerFluid("exhaust_steam", 0xFFBDBDBD, NCFluid.STEAM_TYPE, 500));
        map.put("low_pressure_steam", MAKER.registerFluid("low_pressure_steam", 0xFFBDBDBD, NCFluid.STEAM_TYPE, 800));
        map.put("low_quality_steam", MAKER.registerFluid("low_quality_steam", 0xFFBDBDBD, NCFluid.STEAM_TYPE, 350));
        return map;
    }

    private static Map<String, NCFluid> createCoolant() {
        Map<String, NCFluid> map = new java.util.HashMap<>();
        map.put("nak", MAKER.registerFluid("nak", 0xFFFFE5BC, NCFluid.COOLANT_TYPE));
        HOT_COOLANT_MAP.put("nak_hot", MAKER.registerFluid("nak_hot", FastColor.ARGB32.lerp(0.2f, 0xFFFFD5AC, 0xFFFFE5BC), NCFluid.HOT_COOLANT_TYPE));

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

    private static void addNAKPairs(String name, int colour, Map<String, NCFluid> map) {
        map.put(name + "_nak", MAKER.registerFluid(name + "_nak", FastColor.ARGB32.lerp(0.375f, colour, 0xFFE5BC), NCFluid.COOLANT_TYPE));
        HOT_COOLANT_MAP.put(name + "_nak_hot", MAKER.registerFluid(name + "_nak_hot", FastColor.ARGB32.lerp(0.2f, colour, 0xFFE5BC), NCFluid.HOT_COOLANT_TYPE));
    }

    private static Map<String, NCFluid> createHotGas() {
        Map<String, NCFluid> map = new java.util.HashMap<>();
        map.put("arsenic", MAKER.registerFluid("arsenic", 0xFF818475, NCFluid.HOT_GAS_TYPE));
        map.put("sic_vapor", MAKER.registerFluid("sic_vapor", 0xFF78746A, NCFluid.HOT_GAS_TYPE));
        map.put("fso_vapor", MAKER.registerFluid("fso_vapor", 0xFF8C862E, NCFluid.HOT_GAS_TYPE));
        map.put("hodybef_vapor", MAKER.registerFluid("hodybef_vapor", 0xFFA18A72, NCFluid.HOT_GAS_TYPE));

        for (Triple<String, Float, Integer> triple : Arrays.asList(Triple.of("hot", 4F, 1000), Triple.of("exhaust", 2F, 800))) {
            String suffix = triple.getLeft();
            Float saturation = triple.getMiddle();
            Integer temp = triple.getRight();

            map.put("oxygen_" + suffix, MAKER.registerFluid("oxygen_" + suffix, ColorHelper.saturate(0xFF7E8CC8, saturation), NCFluid.HOT_GAS_TYPE, temp));
            map.put("hydrogen_" + suffix, MAKER.registerFluid("hydrogen_" + suffix, ColorHelper.saturate(0xFFB37AC4, saturation), NCFluid.HOT_GAS_TYPE, temp));
            map.put("helium_" + suffix, MAKER.registerFluid("helium_" + suffix, ColorHelper.saturate(0xFFC57B81, saturation), NCFluid.HOT_GAS_TYPE, temp));
            map.put("nitrogen_" + suffix, MAKER.registerFluid("nitrogen_" + suffix, ColorHelper.saturate(0xFF7CC37B, saturation), NCFluid.HOT_GAS_TYPE, temp));
            map.put("fluorine_" + suffix, MAKER.registerFluid("fluorine_" + suffix, ColorHelper.saturate(0xFFD3C75D, saturation), NCFluid.HOT_GAS_TYPE, temp));
            map.put("methane_" + suffix, MAKER.registerFluid("methane_" + suffix, ColorHelper.saturate(0xFFD9AFB3, saturation), NCFluid.HOT_GAS_TYPE, temp));
            map.put("carbon_dioxide_" + suffix, MAKER.registerFluid("carbon_dioxide_" + suffix, ColorHelper.saturate(0xFF5C635A, saturation), NCFluid.HOT_GAS_TYPE, temp));
            map.put("carbon_monoxide_" + suffix, MAKER.registerFluid("carbon_monoxide_" + suffix, ColorHelper.saturate(0xFF4C5649, saturation), NCFluid.HOT_GAS_TYPE, temp));
            map.put("ethene_" + suffix, MAKER.registerFluid("ethene_" + suffix, ColorHelper.saturate(0xFFFFE4A3, saturation), NCFluid.HOT_GAS_TYPE, temp));
            map.put("ethyne_" + suffix, MAKER.registerFluid("ethyne_" + suffix, ColorHelper.saturate(0xFFFFE442, saturation), NCFluid.HOT_GAS_TYPE, temp));
            map.put("fluoromethane_" + suffix, MAKER.registerFluid("fluoromethane_" + suffix, ColorHelper.saturate(0xFF424C05, saturation), NCFluid.HOT_GAS_TYPE, temp));
            map.put("ammonia_" + suffix, MAKER.registerFluid("ammonia_" + suffix, ColorHelper.saturate(0xFF7AC3A0, saturation), NCFluid.HOT_GAS_TYPE, temp));
            map.put("diborane_" + suffix, MAKER.registerFluid("diborane_" + suffix, ColorHelper.saturate(0xFFCC6E8C, saturation), NCFluid.HOT_GAS_TYPE, temp));
            map.put("sulfur_dioxide_" + suffix, MAKER.registerFluid("sulfur_dioxide_" + suffix, ColorHelper.saturate(0xFFC3BC7A, saturation), NCFluid.HOT_GAS_TYPE, temp));
            map.put("sulfur_trioxide_" + suffix, MAKER.registerFluid("sulfur_trioxide_" + suffix, ColorHelper.saturate(0xFFD3AE5D, saturation), NCFluid.HOT_GAS_TYPE, temp));
            map.put("sulfur_hexafluoride_" + suffix, MAKER.registerFluid("sulfur_hexafluoride_" + suffix, ColorHelper.saturate(0xFFC6FC46, saturation), NCFluid.HOT_GAS_TYPE, temp));
        }

        return map;
    }

    private static Map<String, NCFluid> createFission() {
        Map<String, NCFluid> map = new java.util.HashMap<>();
        map.put("strontium_90", MAKER.registerFluid("strontium_90", 0xFFB8BE88, NCFluid.FISSION_TYPE));
        map.put("molybdenum", MAKER.registerFluid("molybdenum", 0xFFBCC5E4, NCFluid.FISSION_TYPE));
        map.put("ruthenium_106", MAKER.registerFluid("ruthenium_106", 0xFFA3A3A3, NCFluid.FISSION_TYPE));
        map.put("caesium_137", MAKER.registerFluid("caesium_137", 0xFFADADAD, NCFluid.FISSION_TYPE));
        map.put("promethium_147", MAKER.registerFluid("promethium_147", 0xFF96C199, NCFluid.FISSION_TYPE));
        map.put("europium_155", MAKER.registerFluid("europium_155", 0xFF74664A, NCFluid.FISSION_TYPE));
        return map;
    }

    private static Map<String, NCFluid> createSoul() {
        Map<String, NCFluid> map = new java.util.HashMap<>();
        map.put("soul", MAKER.registerFluid("soul", 0xFF7B6F68, NCFluid.SOUL_TYPE));
        map.put("mysterious_soul", MAKER.registerFluid("mysterious_soul", 0xFF985CA4, NCFluid.MYSTERIOUS_SOUL_TYPE));
        return map;
    }

    private static Map<String, NCFluid> createFissionFuel() {
        Map<String, NCFluid> map = new java.util.HashMap<>();

        // Isotopes
        map.put("uranium_233", MAKER.registerFluid("uranium_233", 0xFF212E20, NCFluid.MOLTEN_TYPE));
        map.put("uranium_235", MAKER.registerFluid("uranium_235", 0xFF102D10, NCFluid.MOLTEN_TYPE));
        map.put("uranium_238", MAKER.registerFluid("uranium_238", 0xFF333E32, NCFluid.MOLTEN_TYPE));
        map.put("neptunium_236", MAKER.registerFluid("neptunium_236", 0xFF293E40, NCFluid.MOLTEN_TYPE));
        map.put("neptunium_237", MAKER.registerFluid("neptunium_237", 0xFF2E3A42, NCFluid.MOLTEN_TYPE));
        map.put("plutonium_238", MAKER.registerFluid("plutonium_238", 0xFF999999, NCFluid.MOLTEN_TYPE));
        map.put("plutonium_239", MAKER.registerFluid("plutonium_239", 0xFF9E9E9E, NCFluid.MOLTEN_TYPE));
        map.put("plutonium_241", MAKER.registerFluid("plutonium_241", 0xFF909090, NCFluid.MOLTEN_TYPE));
        map.put("plutonium_242", MAKER.registerFluid("plutonium_242", 0xFFABABAB, NCFluid.MOLTEN_TYPE));
        map.put("americium_241", MAKER.registerFluid("americium_241", 0xFF393725, NCFluid.MOLTEN_TYPE));
        map.put("americium_242", MAKER.registerFluid("americium_242", 0xFF332D1A, NCFluid.MOLTEN_TYPE));
        map.put("americium_243", MAKER.registerFluid("americium_243", 0xFF2C280C, NCFluid.MOLTEN_TYPE));
        map.put("curium_243", MAKER.registerFluid("curium_243", 0xFF311137, NCFluid.MOLTEN_TYPE));
        map.put("curium_245", MAKER.registerFluid("curium_245", 0xFF2D102D, NCFluid.MOLTEN_TYPE));
        map.put("curium_246", MAKER.registerFluid("curium_246", 0xFF3F2442, NCFluid.MOLTEN_TYPE));
        map.put("curium_247", MAKER.registerFluid("curium_247", 0xFF321635, NCFluid.MOLTEN_TYPE));
        map.put("berkelium_247", MAKER.registerFluid("berkelium_247", 0xFF5A2301, NCFluid.MOLTEN_TYPE));
        map.put("berkelium_248", MAKER.registerFluid("berkelium_248", 0xFF602502, NCFluid.MOLTEN_TYPE));
        map.put("californium_249", MAKER.registerFluid("californium_249", 0xFF460D12, NCFluid.MOLTEN_TYPE));
        map.put("californium_250", MAKER.registerFluid("californium_250", 0xFF3E0C14, NCFluid.MOLTEN_TYPE));
        map.put("californium_251", MAKER.registerFluid("californium_251", 0xFF3B0B16, NCFluid.MOLTEN_TYPE));
        map.put("californium_252", MAKER.registerFluid("californium_252", 0xFF430B0E, NCFluid.MOLTEN_TYPE));

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

    public static void addFuelFluids(String name, int color, Map<String, NCFluid> map) {
        map.put(name, MAKER.registerFluid(name, color, NCFluid.MOLTEN_TYPE));
        map.put(name + "_fluoride", MAKER.registerFluid(name + "_fluoride", FastColor.ARGB32.lerp(0.125f, color, 0xFFD3C85D), NCFluid.MOLTEN_TYPE));
        map.put(name + "_fluoride_flibe", MAKER.registerFluid(name + "_fluoride_flibe", FastColor.ARGB32.lerp(0.4f, color, 0xFFC1C8B0), NCFluid.MOLTEN_TYPE));
    }

    public static void init() {
    }
}