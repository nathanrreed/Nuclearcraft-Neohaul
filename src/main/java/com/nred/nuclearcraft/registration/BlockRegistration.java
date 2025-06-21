package com.nred.nuclearcraft.registration;

import com.nred.nuclearcraft.block.batteries.BatteryBlock;
import com.nred.nuclearcraft.block.SolidifiedCorium;
import com.nred.nuclearcraft.block.SupercoldIceBlock;
import com.nred.nuclearcraft.block.collector.MACHINE_LEVEL;
import com.nred.nuclearcraft.block.collector.cobblestone_generator.CobbleGenerator;
import com.nred.nuclearcraft.block.collector.nitrogen_collector.NitrogenCollector;
import com.nred.nuclearcraft.block.collector.water_source.WaterSource;
import com.nred.nuclearcraft.block.machine_interface.MachineInterface;
import com.nred.nuclearcraft.block.processor.alloy_furnace.AlloyFurnace;
import com.nred.nuclearcraft.block.processor.assembler.Assembler;
import com.nred.nuclearcraft.block.processor.centrifuge.Centrifuge;
import com.nred.nuclearcraft.block.processor.chemical_reactor.ChemicalReactor;
import com.nred.nuclearcraft.block.processor.crystallizer.Crystallizer;
import com.nred.nuclearcraft.block.processor.decay_hastener.DecayHastener;
import com.nred.nuclearcraft.block.processor.electric_furnace.ElectricFurnace;
import com.nred.nuclearcraft.block.processor.electrolyzer.Electrolyzer;
import com.nred.nuclearcraft.block.processor.fluid_enricher.Enricher;
import com.nred.nuclearcraft.block.processor.fluid_extractor.Extractor;
import com.nred.nuclearcraft.block.processor.fluid_infuser.Infuser;
import com.nred.nuclearcraft.block.processor.fluid_mixer.SaltMixer;
import com.nred.nuclearcraft.block.processor.fuel_reprocessor.FuelReprocessor;
import com.nred.nuclearcraft.block.processor.ingot_former.IngotFormer;
import com.nred.nuclearcraft.block.processor.manufactory.Manufactory;
import com.nred.nuclearcraft.block.processor.melter.Melter;
import com.nred.nuclearcraft.block.processor.pressurizer.Pressurizer;
import com.nred.nuclearcraft.block.processor.rock_crusher.RockCrusher;
import com.nred.nuclearcraft.block.processor.separator.Separator;
import com.nred.nuclearcraft.block.processor.supercooler.Supercooler;
import com.nred.nuclearcraft.block.solar.SolarPanel;
import com.nred.nuclearcraft.block.turbine.BlockTurbineCasing;
import com.nred.nuclearcraft.block.turbine.BlockTurbineGlass;
import com.nred.nuclearcraft.block.turbine.BlockTurbineRotorBlade;
import com.nred.nuclearcraft.block.turbine.BlockTurbineRotorStator;
import com.nred.nuclearcraft.block.universal_bin.UniversalBin;
import net.minecraft.core.BlockPos;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.registries.DeferredBlock;

import java.util.HashMap;
import java.util.List;
import java.util.function.Supplier;

import static com.nred.nuclearcraft.info.Names.*;
import static com.nred.nuclearcraft.registration.Registers.BLOCKS;
import static com.nred.nuclearcraft.registration.Registers.ITEMS;

public class BlockRegistration {
    public static final HashMap<String, DeferredBlock<Block>> ORE_MAP = createOres();
    public static final HashMap<String, DeferredBlock<Block>> INGOT_BLOCK_MAP = createBlocks(INGOTS, "block", Blocks.IRON_BLOCK);
    public static final HashMap<String, DeferredBlock<Block>> MATERIAL_BLOCK_MAP = createBlocks(MATERIAL_BLOCKS, "block", Blocks.IRON_BLOCK);
    public static final HashMap<String, DeferredBlock<Block>> RAW_BLOCK_MAP = createBlocks(RAWS, "raw", "block", Blocks.RAW_IRON_BLOCK);
    public static final HashMap<String, DeferredBlock<Block>> FERTILE_ISOTOPE_MAP = createBlocks(FERTILE_ISOTOPES, "fertile_isotope", "block", Blocks.IRON_BLOCK);
    public static final HashMap<String, DeferredBlock<Block>> COLLECTOR_MAP = createCollectors();
    public static final HashMap<String, DeferredBlock<Block>> SOLAR_MAP = createSolarPanels();
    public static final HashMap<String, DeferredBlock<Block>> TURBINE_MAP = createTurbineParts();
    public static final HashMap<String, DeferredBlock<Block>> BATTERY_MAP = createBatteries();

    private static final BlockBehaviour.Properties BASE_PROPERTIES = BlockBehaviour.Properties.of().requiresCorrectToolForDrops().strength(5.0F, 1200.0F).isValidSpawn(Blocks::never).isRedstoneConductor((a, b, c) -> false);
    public static final HashMap<String, DeferredBlock<Block>> PROCESSOR_MAP = createProcessors();

    public static final DeferredBlock<Block> TRITIUM_LAMP = registerBlockItem("tritium_lamp", () -> new Block(BlockBehaviour.Properties.of().lightLevel(blockState -> 15)));
    public static final DeferredBlock<Block> SUPERCOLD_ICE = registerBlockItem("supercold_ice", SupercoldIceBlock::new);
    public static final DeferredBlock<Block> SOLIDIFIED_CORIUM = registerBlockItem("solidified_corium", SolidifiedCorium::new);
    public static final DeferredBlock<Block> UNIVERSAL_BIN = registerBlockItem("universal_bin", UniversalBin::new);
    public static final DeferredBlock<Block> MACHINE_INTERFACE = registerBlockItem("machine_interface", MachineInterface::new);

    // TODO make real mushroom
    public static final DeferredBlock<Block> GLOWING_MUSHROOM = registerBlockItem("glowing_mushroom", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_YELLOW).noCollission().randomTicks().instabreak().sound(SoundType.GRASS)
            .lightLevel(blockState -> 10).hasPostProcess((state, level, pos) -> true).pushReaction(PushReaction.DESTROY)) {
        @Override
        protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
            return Block.box(5.0, 0.0, 5.0, 11.0, 6.0, 11.0);
        }
    });

    private static HashMap<String, DeferredBlock<Block>> createOres() {
        HashMap<String, DeferredBlock<Block>> map = new HashMap<>();
        for (String ore : ORES) {
            map.put(ore, registerBlockItem(ore + "_ore", () -> new DropExperienceBlock(ConstantInt.of(0), BlockBehaviour.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(3.0F, 3.0F))));
            map.put(ore + "_deepslate", registerBlockItem(ore + "_deepslate_ore", () -> new DropExperienceBlock(ConstantInt.of(0), BlockBehaviour.Properties.of().mapColor(MapColor.DEEPSLATE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(4.5F, 3.0F).sound(SoundType.DEEPSLATE))));
        }

        return map;
    }

    private static DeferredBlock<Block> registerBlockItem(String name, Supplier<Block> block) {
        DeferredBlock<Block> toReturn = BLOCKS.register(name, block);
        ITEMS.registerSimpleBlockItem(name, toReturn);
        return toReturn;
    }

    private static HashMap<String, DeferredBlock<Block>> createBlocks(List<String> names, String append, Block copy) {
        return createBlocks(names, "", append, copy);
    }

    private static HashMap<String, DeferredBlock<Block>> createBlocks(List<String> names, String prepend, String append, Block copy) {
        HashMap<String, DeferredBlock<Block>> map = new HashMap<>();
        for (String name : names) {
            String reg_name = (!prepend.isEmpty() ? (prepend + "_") : "") + name + "_" + append;
            map.put(name, BLOCKS.register(reg_name, () -> new Block(BlockBehaviour.Properties.ofFullCopy(copy))));
            ITEMS.registerSimpleBlockItem(reg_name, map.get(name));
        }
        return map;
    }

    private static HashMap<String, DeferredBlock<Block>> createCollectors() {
        HashMap<String, DeferredBlock<Block>> map = new HashMap<>();
        for (MACHINE_LEVEL level : MACHINE_LEVEL.values()) {
            String type = level.toString().isEmpty() ? "" : "_" + level.toString().toLowerCase();
            String name = "cobblestone_generator" + type;
            map.put(name, BLOCKS.register(name, () -> new CobbleGenerator(BlockBehaviour.Properties.ofFullCopy(Blocks.COBBLESTONE), level)));
            ITEMS.registerSimpleBlockItem(name, map.get(name));

            name = "water_source" + type;
            map.put(name, BLOCKS.register(name, () -> new WaterSource(BlockBehaviour.Properties.ofFullCopy(Blocks.COBBLESTONE), level)));
            ITEMS.registerSimpleBlockItem(name, map.get(name));

            name = "nitrogen_collector" + type;
            map.put(name, BLOCKS.register(name, () -> new NitrogenCollector(BlockBehaviour.Properties.ofFullCopy(Blocks.COBBLESTONE), level)));
            ITEMS.registerSimpleBlockItem(name, map.get(name));
        }
        return map;
    }

    private static HashMap<String, DeferredBlock<Block>> createProcessors() {
        HashMap<String, DeferredBlock<Block>> map = new HashMap<>();
        map.put("alloy_furnace", registerBlockItem("alloy_furnace", () -> new AlloyFurnace(BASE_PROPERTIES)));
        map.put("assembler", registerBlockItem("assembler", () -> new Assembler(BASE_PROPERTIES)));
        map.put("centrifuge", registerBlockItem("centrifuge", () -> new Centrifuge(BASE_PROPERTIES)));
        map.put("chemical_reactor", registerBlockItem("chemical_reactor", () -> new ChemicalReactor(BASE_PROPERTIES)));
        map.put("crystallizer", registerBlockItem("crystallizer", () -> new Crystallizer(BASE_PROPERTIES)));
        map.put("decay_hastener", registerBlockItem("decay_hastener", () -> new DecayHastener(BASE_PROPERTIES)));
        map.put("electric_furnace", registerBlockItem("electric_furnace", () -> new ElectricFurnace(BASE_PROPERTIES)));
        map.put("electrolyzer", registerBlockItem("electrolyzer", () -> new Electrolyzer(BASE_PROPERTIES)));
        map.put("fluid_enricher", registerBlockItem("fluid_enricher", () -> new Enricher(BASE_PROPERTIES)));
        map.put("fluid_extractor", registerBlockItem("fluid_extractor", () -> new Extractor(BASE_PROPERTIES)));
        map.put("fuel_reprocessor", registerBlockItem("fuel_reprocessor", () -> new FuelReprocessor(BASE_PROPERTIES)));
        map.put("fluid_infuser", registerBlockItem("fluid_infuser", () -> new Infuser(BASE_PROPERTIES)));
        map.put("ingot_former", registerBlockItem("ingot_former", () -> new IngotFormer(BASE_PROPERTIES)));
        map.put("manufactory", registerBlockItem("manufactory", () -> new Manufactory(BASE_PROPERTIES)));
        map.put("melter", registerBlockItem("melter", () -> new Melter(BASE_PROPERTIES)));
        map.put("pressurizer", registerBlockItem("pressurizer", () -> new Pressurizer(BASE_PROPERTIES)));
        map.put("rock_crusher", registerBlockItem("rock_crusher", () -> new RockCrusher(BASE_PROPERTIES)));
        map.put("fluid_mixer", registerBlockItem("fluid_mixer", () -> new SaltMixer(BASE_PROPERTIES)));
        map.put("separator", registerBlockItem("separator", () -> new Separator(BASE_PROPERTIES)));
        map.put("supercooler", registerBlockItem("supercooler", () -> new Supercooler(BASE_PROPERTIES)));
        return map;
    }

    private static HashMap<String, DeferredBlock<Block>> createSolarPanels() {
        HashMap<String, DeferredBlock<Block>> map = new HashMap<>();
        map.put("solar_panel_basic", registerBlockItem("solar_panel_basic", () -> new SolarPanel(0)));
        map.put("solar_panel_advanced", registerBlockItem("solar_panel_advanced", () -> new SolarPanel(1)));
        map.put("solar_panel_du", registerBlockItem("solar_panel_du", () -> new SolarPanel(2)));
        map.put("solar_panel_elite", registerBlockItem("solar_panel_elite", () -> new SolarPanel(3)));
        return map;
    }

    private static HashMap<String, DeferredBlock<Block>> createTurbineParts() {
        HashMap<String, DeferredBlock<Block>> map = new HashMap<>(); //TODO MAKE CUSTOM BLOCKS
        map.put("turbine_casing", registerBlockItem("turbine_casing", BlockTurbineCasing::new));
        map.put("turbine_glass", registerBlockItem("turbine_glass", BlockTurbineGlass::new));
        map.put("turbine_rotor_bearing", registerBlockItem("turbine_rotor_bearing", () -> new Block(BlockBehaviour.Properties.of())));
        map.put("turbine_rotor_shaft", registerBlockItem("turbine_rotor_shaft", () -> new Block(BlockBehaviour.Properties.of())));
        map.put("turbine_inlet", registerBlockItem("turbine_inlet", () -> new Block(BlockBehaviour.Properties.of())));
        map.put("turbine_outlet", registerBlockItem("turbine_outlet", () -> new Block(BlockBehaviour.Properties.of())));
        map.put("turbine_controller", registerBlockItem("turbine_controller", () -> new Block(BlockBehaviour.Properties.of())));
        map.put("turbine_coil_connector", registerBlockItem("turbine_coil_connector", () -> new Block(BlockBehaviour.Properties.of())));

        map.put("magnesium_turbine_dynamo_coil", registerBlockItem("magnesium_turbine_dynamo_coil", () -> new Block(BlockBehaviour.Properties.of())));
        map.put("beryllium_turbine_dynamo_coil", registerBlockItem("beryllium_turbine_dynamo_coil", () -> new Block(BlockBehaviour.Properties.of())));
        map.put("aluminum_turbine_dynamo_coil", registerBlockItem("aluminum_turbine_dynamo_coil", () -> new Block(BlockBehaviour.Properties.of())));
        map.put("gold_turbine_dynamo_coil", registerBlockItem("gold_turbine_dynamo_coil", () -> new Block(BlockBehaviour.Properties.of())));
        map.put("copper_turbine_dynamo_coil", registerBlockItem("copper_turbine_dynamo_coil", () -> new Block(BlockBehaviour.Properties.of())));
        map.put("silver_turbine_dynamo_coil", registerBlockItem("silver_turbine_dynamo_coil", () -> new Block(BlockBehaviour.Properties.of())));

        map.put("steel_turbine_rotor_blade", registerBlockItem("steel_turbine_rotor_blade", () -> new BlockTurbineRotorBlade(BlockBehaviour.Properties.of())));
        map.put("extreme_alloy_turbine_rotor_blade", registerBlockItem("extreme_alloy_turbine_rotor_blade", () -> new BlockTurbineRotorBlade(BlockBehaviour.Properties.of())));
        map.put("sic_turbine_rotor_blade", registerBlockItem("sic_turbine_rotor_blade", () -> new BlockTurbineRotorBlade(BlockBehaviour.Properties.of())));
        map.put("standard_turbine_rotor_stator", registerBlockItem("standard_turbine_rotor_stator", () -> new BlockTurbineRotorStator(BlockBehaviour.Properties.of())));
        return map;
    }

    private static HashMap<String, DeferredBlock<Block>> createBatteries() {
        HashMap<String, DeferredBlock<Block>> map = new HashMap<>();
        map.put("basic_voltaic_pile", registerBlockItem("basic_voltaic_pile", () -> new BatteryBlock( 0)));
        map.put("advanced_voltaic_pile", registerBlockItem("advanced_voltaic_pile", () -> new BatteryBlock(1)));
        map.put("du_voltaic_pile", registerBlockItem("du_voltaic_pile", () -> new BatteryBlock(2)));
        map.put("elite_voltaic_pile", registerBlockItem("elite_voltaic_pile", () -> new BatteryBlock(3)));

        map.put("basic_lithium_ion_battery", registerBlockItem("basic_lithium_ion_battery", () -> new BatteryBlock(10)));
        map.put("advanced_lithium_ion_battery", registerBlockItem("advanced_lithium_ion_battery", () -> new BatteryBlock(11)));
        map.put("du_lithium_ion_battery", registerBlockItem("du_lithium_ion_battery", () -> new BatteryBlock(12)));
        map.put("elite_lithium_ion_battery", registerBlockItem("elite_lithium_ion_battery", () -> new BatteryBlock(13)));
        return map;
    }

    public static void init() {
    }
}