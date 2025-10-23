package com.nred.nuclearcraft.registration;

import com.nred.nuclearcraft.NCInfo;
import com.nred.nuclearcraft.block.NCItemBlock;
import com.nred.nuclearcraft.block.SolidifiedCorium;
import com.nred.nuclearcraft.block.SupercoldIceBlock;
import com.nred.nuclearcraft.block.batteries.BatteryBlock;
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
import com.nred.nuclearcraft.block.processor.nuclear_furnace.NuclearFurnace;
import com.nred.nuclearcraft.block.processor.pressurizer.Pressurizer;
import com.nred.nuclearcraft.block.processor.rock_crusher.RockCrusher;
import com.nred.nuclearcraft.block.processor.separator.Separator;
import com.nred.nuclearcraft.block.processor.supercooler.Supercooler;
import com.nred.nuclearcraft.block.tile.BlockSimpleTile;
import com.nred.nuclearcraft.block.tile.dummy.BlockMachineInterface;
import com.nred.nuclearcraft.multiblock.fisson.FissionNeutronShieldType;
import com.nred.nuclearcraft.multiblock.fisson.FissionPartType;
import com.nred.nuclearcraft.multiblock.fisson.FissionSourceType;
import com.nred.nuclearcraft.multiblock.fisson.molten_salt.FissionCoolantHeaterPortType;
import com.nred.nuclearcraft.multiblock.fisson.molten_salt.FissionCoolantHeaterType;
import com.nred.nuclearcraft.multiblock.fisson.solid.FissionHeatSinkType;
import com.nred.nuclearcraft.multiblock.turbine.TurbinePartType;
import com.nred.nuclearcraft.util.InfoHelper;
import com.nred.nuclearcraft.util.NCMath;
import com.nred.nuclearcraft.util.PrimitiveFunction.ObjIntFunction;
import com.nred.nuclearcraft.util.UnitHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.registries.DeferredBlock;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.nred.nuclearcraft.NuclearcraftNeohaul.MODID;
import static com.nred.nuclearcraft.config.Config2.*;
import static com.nred.nuclearcraft.info.Names.*;
import static com.nred.nuclearcraft.multiblock.turbine.TurbineDynamoCoilType.*;
import static com.nred.nuclearcraft.multiblock.turbine.TurbineRotorBladeType.*;
import static com.nred.nuclearcraft.multiblock.turbine.TurbineRotorStatorType.STANDARD;
import static com.nred.nuclearcraft.registration.Registers.BLOCKS;
import static com.nred.nuclearcraft.registration.Registers.ITEMS;

public class BlockRegistration {
    public static final BooleanProperty FRAME = BooleanProperty.create(MODID + "_frame");
    public static final BooleanProperty ACTIVE = BooleanProperty.create(MODID + "_active");
    public static final DirectionProperty FACING_HORIZONTAL = BlockStateProperties.HORIZONTAL_FACING;
    public static final DirectionProperty FACING_ALL = BlockStateProperties.FACING;
    public static final EnumProperty<Direction.Axis> AXIS_ALL = EnumProperty.create("axis", Direction.Axis.class);

    public static final HashMap<String, DeferredBlock<Block>> ORE_MAP = createOres();
    public static final HashMap<String, DeferredBlock<Block>> INGOT_BLOCK_MAP = createBlocks(INGOTS, "block", Blocks.IRON_BLOCK);
    public static final HashMap<String, DeferredBlock<Block>> MATERIAL_BLOCK_MAP = createBlocks(MATERIAL_BLOCKS, "block", Blocks.IRON_BLOCK);
    public static final HashMap<String, DeferredBlock<Block>> RAW_BLOCK_MAP = createBlocks(RAWS, "raw", "block", Blocks.RAW_IRON_BLOCK);
    public static final HashMap<String, DeferredBlock<Block>> FERTILE_ISOTOPE_MAP = createBlocks(FERTILE_ISOTOPES, "fertile_isotope", "block", Blocks.IRON_BLOCK);
    public static final HashMap<String, DeferredBlock<Block>> COLLECTOR_MAP = createCollectors();
    public static final HashMap<String, DeferredBlock<Block>> SOLAR_MAP = createSolarPanels();
    public static final HashMap<String, DeferredBlock<Block>> TURBINE_MAP = createTurbineParts();
    public static final HashMap<String, DeferredBlock<Block>> FISSION_REACTOR_MAP = createFissionParts();
    public static final HashMap<String, DeferredBlock<Block>> BATTERY_MAP = createBatteries();

    private static final BlockBehaviour.Properties BASE_PROPERTIES = BlockBehaviour.Properties.of().requiresCorrectToolForDrops().strength(5.0F, 1200.0F).isValidSpawn(Blocks::never).isRedstoneConductor((a, b, c) -> false);
    public static final HashMap<String, DeferredBlock<Block>> PROCESSOR_MAP = createProcessors();

    public static final DeferredBlock<Block> TRITIUM_LAMP = registerBlockItem("tritium_lamp", () -> new Block(BlockBehaviour.Properties.of().lightLevel(blockState -> 15)));
    public static final DeferredBlock<Block> HEAVY_WATER_MODERATOR = registerBlockItem("heavy_water_moderator", () -> new Block(BlockBehaviour.Properties.of()));

    public static final DeferredBlock<Block> SUPERCOLD_ICE = registerBlockItem("supercold_ice", SupercoldIceBlock::new);
    public static final DeferredBlock<Block> SOLIDIFIED_CORIUM = registerBlockItem("solidified_corium", SolidifiedCorium::new);
    public static final DeferredBlock<Block> UNIVERSAL_BIN = registerBlockItemWithTooltip("universal_bin", () -> new BlockSimpleTile<>("bin"), false);
    public static final DeferredBlock<Block> MACHINE_INTERFACE = registerBlockItemWithTooltip("machine_interface", () -> new BlockMachineInterface("machine_interface"), false);
    public static final DeferredBlock<Block> NUCLEAR_FURNACE = registerBlockItemWithTooltip("nuclear_furnace", () -> new NuclearFurnace(BlockBehaviour.Properties.ofFullCopy(Blocks.FURNACE)), false);

    public static final DeferredBlock<Block> DECAY_GENERATOR = registerBlockItem("decay_generator", () -> new BlockSimpleTile<>("decay_generator"));


    public static <T extends Block> DeferredBlock<Block> registerBlockItemWithTooltip(String name, Supplier<T> block, boolean hasFixed, Component... tooltip) {
        DeferredBlock<Block> toReturn = BLOCKS.register(name, block);
        ITEMS.register(name, () -> new NCItemBlock(toReturn.get(), ChatFormatting.RED, InfoHelper.EMPTY_ARRAY, hasFixed, ChatFormatting.AQUA, tooltip));
        return toReturn;
    }

    private static DeferredBlock<Block> registerBlockItem(String name, Supplier<Block> block) {
        DeferredBlock<Block> toReturn = BLOCKS.register(name, block);
        ITEMS.registerSimpleBlockItem(name, toReturn);
        return toReturn;
    }

    private static DeferredBlock<Block> registerBlockItemWithTooltip(String name, Supplier<Block> block, Function<Block, ? extends BlockItem> itemBlockFunction) {
        DeferredBlock<Block> toReturn = BLOCKS.register(name, block);
        ITEMS.register(name, () -> itemBlockFunction.apply(toReturn.get()));
        return toReturn;
    }

    // TODO make real mushroom
    public static final DeferredBlock<Block> GLOWING_MUSHROOM = registerBlockItem("glowing_mushroom", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_YELLOW).noCollission().randomTicks().instabreak().sound(SoundType.GRASS)
            .lightLevel(blockState -> 10).hasPostProcess((state, level, pos) -> true).pushReaction(PushReaction.DESTROY)) {
        @Override
        protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
            return Block.box(5.0, 0.0, 5.0, 11.0, 6.0, 11.0);
        }
    });

    private static HashMap<String, DeferredBlock<Block>> createOres() {
        HashMap<String, DeferredBlock<Block>> map = new LinkedHashMap<>();
        for (String ore : ORES) {
            map.put(ore, registerBlockItem(ore + "_ore", () -> new DropExperienceBlock(ConstantInt.of(0), BlockBehaviour.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(3.0F, 3.0F))));
            map.put(ore + "_deepslate", registerBlockItem(ore + "_deepslate_ore", () -> new DropExperienceBlock(ConstantInt.of(0), BlockBehaviour.Properties.of().mapColor(MapColor.DEEPSLATE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(4.5F, 3.0F).sound(SoundType.DEEPSLATE))));
        }

        return map;
    }

    private static HashMap<String, DeferredBlock<Block>> createBlocks(List<String> names, String append, Block copy) {
        return createBlocks(names, "", append, copy);
    }

    private static HashMap<String, DeferredBlock<Block>> createBlocks(List<String> names, String prepend, String append, Block copy) {
        HashMap<String, DeferredBlock<Block>> map = new LinkedHashMap<>();
        for (String name : names) {
            String reg_name = (!prepend.isEmpty() ? (prepend + "_") : "") + name + "_" + append;
            map.put(name, BLOCKS.register(reg_name, () -> new Block(BlockBehaviour.Properties.ofFullCopy(copy))));
            ITEMS.registerSimpleBlockItem(reg_name, map.get(name));
        }
        return map;
    }

    private static HashMap<String, DeferredBlock<Block>> createCollectors() {
        HashMap<String, DeferredBlock<Block>> map = new LinkedHashMap<>();
        ObjIntFunction<Block, BlockItem> cobbleGenFn = (x, y) -> {
            Supplier<String> rateString = () -> NCMath.sigFigs(processor_passive_rate[0] * y, 5) + " C/t";
            return new NCItemBlock(x, cobble_gen_power > 0 ? Component.translatable(MODID + ".tooltip.cobblestone_generator_req_power", rateString, UnitHelper.prefix(cobble_gen_power * y, 5, "RF/t")) : Component.translatable(MODID + ".tooltip.cobblestone_generator_no_req_power", rateString));
        };
        map.put("cobblestone_generator", registerBlockItemWithTooltip("cobblestone_generator", () -> new BlockSimpleTile<>("cobblestone_generator"), x -> cobbleGenFn.apply(x, 1)));
        map.put("cobblestone_generator_compact", registerBlockItemWithTooltip("cobblestone_generator_compact", () -> new BlockSimpleTile<>("cobblestone_generator_compact"), x -> cobbleGenFn.apply(x, 8)));
        map.put("cobblestone_generator_dense", registerBlockItemWithTooltip("cobblestone_generator_dense", () -> new BlockSimpleTile<>("cobblestone_generator_dense"), x -> cobbleGenFn.apply(x, 64)));

        ObjIntFunction<Block, BlockItem> waterSourceItemBlockFunction = (x, y) -> new NCItemBlock(x, Component.translatable(MODID + ".tooltip.water_source", (Supplier<String>) () -> UnitHelper.prefix(processor_passive_rate[1] * y, 5, "B/t", -1)));
        map.put("water_source", registerBlockItemWithTooltip("water_source", () -> new BlockSimpleTile<>("water_source"), x -> waterSourceItemBlockFunction.apply(x, 1)));
        map.put("water_source_compact", registerBlockItemWithTooltip("water_source_compact", () -> new BlockSimpleTile<>("water_source_compact"), x -> waterSourceItemBlockFunction.apply(x, 8)));
        map.put("water_source_dense", registerBlockItemWithTooltip("water_source_dense", () -> new BlockSimpleTile<>("water_source_dense"), x -> waterSourceItemBlockFunction.apply(x, 64)));
        ObjIntFunction<Block, BlockItem> nitrogenCollectorItemBlockFunction = (x, y) -> new NCItemBlock(x, Component.translatable(MODID + ".tooltip.nitrogen_collector", (Supplier<String>) () -> UnitHelper.prefix(processor_passive_rate[2] * y, 5, "B/t", -1)));
        map.put("nitrogen_collector", registerBlockItemWithTooltip("nitrogen_collector", () -> new BlockSimpleTile<>("nitrogen_collector"), x -> nitrogenCollectorItemBlockFunction.apply(x, 1)));
        map.put("nitrogen_collector_compact", registerBlockItemWithTooltip("nitrogen_collector_compact", () -> new BlockSimpleTile<>("nitrogen_collector_compact"), x -> nitrogenCollectorItemBlockFunction.apply(x, 8)));
        map.put("nitrogen_collector_dense", registerBlockItemWithTooltip("nitrogen_collector_dense", () -> new BlockSimpleTile<>("nitrogen_collector_dense"), x -> nitrogenCollectorItemBlockFunction.apply(x, 64)));

        return map;
    }

    private static HashMap<String, DeferredBlock<Block>> createProcessors() {
        HashMap<String, DeferredBlock<Block>> map = new LinkedHashMap<>();
        map.put("alloy_furnace", registerBlockItemWithTooltip("alloy_furnace", () -> new AlloyFurnace(BASE_PROPERTIES), false));
        map.put("assembler", registerBlockItemWithTooltip("assembler", () -> new Assembler(BASE_PROPERTIES), false));
        map.put("centrifuge", registerBlockItemWithTooltip("centrifuge", () -> new Centrifuge(BASE_PROPERTIES), false));
        map.put("chemical_reactor", registerBlockItemWithTooltip("chemical_reactor", () -> new ChemicalReactor(BASE_PROPERTIES), false));
        map.put("crystallizer", registerBlockItemWithTooltip("crystallizer", () -> new Crystallizer(BASE_PROPERTIES), false));
        map.put("decay_hastener", registerBlockItemWithTooltip("decay_hastener", () -> new DecayHastener(BASE_PROPERTIES), false));
        map.put("electric_furnace", registerBlockItemWithTooltip("electric_furnace", () -> new ElectricFurnace(BASE_PROPERTIES), false));
        map.put("electrolyzer", registerBlockItemWithTooltip("electrolyzer", () -> new Electrolyzer(BASE_PROPERTIES), false));
        map.put("fluid_enricher", registerBlockItemWithTooltip("fluid_enricher", () -> new Enricher(BASE_PROPERTIES), false));
        map.put("fluid_extractor", registerBlockItemWithTooltip("fluid_extractor", () -> new Extractor(BASE_PROPERTIES), false));
        map.put("fuel_reprocessor", registerBlockItemWithTooltip("fuel_reprocessor", () -> new FuelReprocessor(BASE_PROPERTIES), false));
        map.put("fluid_infuser", registerBlockItemWithTooltip("fluid_infuser", () -> new Infuser(BASE_PROPERTIES), false));
        map.put("ingot_former", registerBlockItemWithTooltip("ingot_former", () -> new IngotFormer(BASE_PROPERTIES), false));
        map.put("manufactory", registerBlockItemWithTooltip("manufactory", () -> new Manufactory(BASE_PROPERTIES), false));
        map.put("melter", registerBlockItemWithTooltip("melter", () -> new Melter(BASE_PROPERTIES), false));
        map.put("pressurizer", registerBlockItemWithTooltip("pressurizer", () -> new Pressurizer(BASE_PROPERTIES), false));
        map.put("rock_crusher", registerBlockItemWithTooltip("rock_crusher", () -> new RockCrusher(BASE_PROPERTIES), false));
        map.put("fluid_mixer", registerBlockItemWithTooltip("fluid_mixer", () -> new SaltMixer(BASE_PROPERTIES), false));
        map.put("separator", registerBlockItemWithTooltip("separator", () -> new Separator(BASE_PROPERTIES), false));
        map.put("supercooler", registerBlockItemWithTooltip("supercooler", () -> new Supercooler(BASE_PROPERTIES), false));
        return map;
    }

    private static HashMap<String, DeferredBlock<Block>> createSolarPanels() {
        HashMap<String, DeferredBlock<Block>> map = new LinkedHashMap<>();
        map.put("solar_panel_basic", registerBlockItemWithTooltip("solar_panel_basic", () -> new BlockSimpleTile<>("solar_panel_basic"), false, NCInfo.solarPanelInfo(() -> solar_power[0])));
        map.put("solar_panel_advanced", registerBlockItemWithTooltip("solar_panel_advanced", () -> new BlockSimpleTile<>("solar_panel_advanced"), false, NCInfo.solarPanelInfo(() -> solar_power[1])));
        map.put("solar_panel_du", registerBlockItemWithTooltip("solar_panel_du", () -> new BlockSimpleTile<>("solar_panel_du"), false, NCInfo.solarPanelInfo(() -> solar_power[2])));
        map.put("solar_panel_elite", registerBlockItemWithTooltip("solar_panel_elite", () -> new BlockSimpleTile<>("solar_panel_elite"), false, NCInfo.solarPanelInfo(() -> solar_power[3])));
        return map;
    }

    private static HashMap<String, DeferredBlock<Block>> createTurbineParts() {
        HashMap<String, DeferredBlock<Block>> map = new LinkedHashMap<>();
        map.put("turbine_casing", registerBlockItem("turbine_casing", TurbinePartType.Casing::createBlock));
        map.put("turbine_glass", registerBlockItem("turbine_glass", TurbinePartType.Glass::createBlock));
        map.put("turbine_rotor_bearing", registerBlockItem("turbine_rotor_bearing", TurbinePartType.RotorBearing::createBlock));
        map.put("turbine_rotor_shaft", registerBlockItem("turbine_rotor_shaft", TurbinePartType.RotorShaft::createBlock));
        map.put("turbine_inlet", registerBlockItem("turbine_inlet", TurbinePartType.Inlet::createBlock));
        map.put("turbine_outlet", registerBlockItem("turbine_outlet", TurbinePartType.Outlet::createBlock));
        map.put("turbine_controller", registerBlockItem("turbine_controller", TurbinePartType.Controller::createBlock));
        map.put("turbine_coil_connector", registerBlockItem("turbine_coil_connector", TurbinePartType.DynamoConnector::createBlock));
        map.put("turbine_computer_port", registerBlockItem("turbine_computer_port", TurbinePartType.ComputerPort::createBlock));
        map.put("turbine_redstone_port", registerBlockItem("turbine_redstone_port", TurbinePartType.RedstonePort::createBlock));

        map.put("magnesium_turbine_dynamo_coil", registerBlockItem("magnesium_turbine_dynamo_coil", () -> TurbinePartType.Dynamo.createBlock(MAGNESIUM)));
        map.put("beryllium_turbine_dynamo_coil", registerBlockItem("beryllium_turbine_dynamo_coil", () -> TurbinePartType.Dynamo.createBlock(BERYLLIUM)));
        map.put("aluminum_turbine_dynamo_coil", registerBlockItem("aluminum_turbine_dynamo_coil", () -> TurbinePartType.Dynamo.createBlock(ALUMINUM)));
        map.put("gold_turbine_dynamo_coil", registerBlockItem("gold_turbine_dynamo_coil", () -> TurbinePartType.Dynamo.createBlock(GOLD)));
        map.put("copper_turbine_dynamo_coil", registerBlockItem("copper_turbine_dynamo_coil", () -> TurbinePartType.Dynamo.createBlock(COPPER)));
        map.put("silver_turbine_dynamo_coil", registerBlockItem("silver_turbine_dynamo_coil", () -> TurbinePartType.Dynamo.createBlock(SILVER)));

        map.put("steel_turbine_rotor_blade", registerBlockItem("steel_turbine_rotor_blade", () -> TurbinePartType.RotorBlade.createBlock(STEEL)));
        map.put("extreme_alloy_turbine_rotor_blade", registerBlockItem("extreme_alloy_turbine_rotor_blade", () -> TurbinePartType.RotorBlade.createBlock(EXTREME)));
        map.put("sic_turbine_rotor_blade", registerBlockItem("sic_turbine_rotor_blade", () -> TurbinePartType.RotorBlade.createBlock(SIC_SIC_CMC)));
        map.put("standard_turbine_rotor_stator", registerBlockItem("standard_turbine_rotor_stator", () -> TurbinePartType.RotorStator.createBlock(STANDARD)));
        return map;
    }

    private static HashMap<String, DeferredBlock<Block>> createFissionParts() {
        HashMap<String, DeferredBlock<Block>> map = new LinkedHashMap<>();
        map.put("solid_fuel_fission_controller", registerBlockItem("solid_fuel_fission_controller", FissionPartType.SolidFuelController::createBlock));
        map.put("molten_salt_fission_controller", registerBlockItem("molten_salt_fission_controller", FissionPartType.MoltenSaltController::createBlock));
        map.put("fission_casing", registerBlockItem("fission_reactor_casing", FissionPartType.Casing::createBlock));
        map.put("fission_glass", registerBlockItem("fission_glass", FissionPartType.Glass::createBlock));
        map.put("fission_vent", registerBlockItem("fission_vent", FissionPartType.Vent::createBlock));
        map.put("fission_conductor", registerBlockItem("fission_conductor", FissionPartType.Conductor::createBlock));

        map.put("boron_silver_shield", registerBlockItem("boron_silver_shield", () -> FissionPartType.Shield.createBlock(FissionNeutronShieldType.BORON_SILVER)));

        map.put("radium_beryllium_source", registerBlockItem("radium_beryllium_source", () -> FissionPartType.Source.createBlock(FissionSourceType.RADIUM_BERYLLIUM)));
        map.put("polonium_beryllium_source", registerBlockItem("polonium_beryllium_source", () -> FissionPartType.Source.createBlock(FissionSourceType.POLONIUM_BERYLLIUM)));
        map.put("californium_source", registerBlockItem("californium_source", () -> FissionPartType.Source.createBlock(FissionSourceType.CALIFORNIUM)));

        map.put("beryllium_carbon_reflector", registerBlockItem("beryllium_carbon_reflector", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK))));
        map.put("lead_steel_reflector", registerBlockItem("lead_steel_reflector", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK))));

        map.put("fission_fuel_cell", registerBlockItem("fission_fuel_cell", FissionPartType.Cell::createBlock));
        map.put("fission_fuel_cell_port", registerBlockItem("fission_fuel_cell_port", FissionPartType.CellPort::createBlock));

        map.put("fission_cooler", registerBlockItem("fission_cooler", FissionPartType.Cooler::createBlock));
        map.put("fission_cooler_port", registerBlockItem("fission_cooler_port", FissionPartType.CoolerPort::createBlock));

        map.put("fission_irradiator", registerBlockItem("fission_irradiator", FissionPartType.Irradiator::createBlock));
        map.put("fission_irradiator_port", registerBlockItem("fission_irradiator_port", FissionPartType.IrradiatorPort::createBlock));

        map.put("fission_fuel_vessel", registerBlockItem("fission_fuel_vessel", FissionPartType.Vessel::createBlock));
        map.put("fission_fuel_vessel_port", registerBlockItem("fission_fuel_vessel_port", FissionPartType.VesselPort::createBlock));

        map.put("water_fission_heat_sink", registerBlockItem("water_fission_heat_sink", () -> FissionPartType.HeatSink.createBlock(FissionHeatSinkType.WATER)));
        map.put("standard_fission_coolant_heater", registerBlockItem("standard_fission_coolant_heater", () -> FissionPartType.Heater.createBlock(FissionCoolantHeaterType.STANDARD)));
        map.put("standard_fission_coolant_heater_port", registerBlockItem("standard_fission_coolant_heater_port", () -> FissionPartType.HeaterPort.createBlock(FissionCoolantHeaterPortType.STANDARD)));
        for (String name : FISSION_HEAT_PARTS) {
            map.put(name + "_fission_heat_sink", registerBlockItem(name + "_fission_heat_sink", () -> FissionPartType.HeatSink.createBlock(FissionHeatSinkType.valueOf(name.toUpperCase()))));
            map.put(name + "_fission_coolant_heater", registerBlockItem(name + "_fission_coolant_heater", () -> FissionPartType.Heater.createBlock(FissionCoolantHeaterType.valueOf(name.toUpperCase()))));
            map.put(name + "_fission_coolant_heater_port", registerBlockItem(name + "_fission_coolant_heater_port", () -> FissionPartType.HeaterPort.createBlock(FissionCoolantHeaterPortType.valueOf(name.toUpperCase()))));
        }

        map.put("fission_monitor", registerBlockItem("fission_monitor", FissionPartType.Monitor::createBlock));
        map.put("fission_shield_manager", registerBlockItem("fission_shield_manager", FissionPartType.ShieldManager::createBlock));
        map.put("fission_source_manager", registerBlockItem("fission_source_manager", FissionPartType.SourceManager::createBlock));

        map.put("fission_computer_port", registerBlockItem("fission_computer_port", FissionPartType.ComputerPort::createBlock));

        return map;
    }

    private static HashMap<String, DeferredBlock<Block>> createBatteries() {
        HashMap<String, DeferredBlock<Block>> map = new LinkedHashMap<>();
        map.put("basic_voltaic_pile", registerBlockItem("basic_voltaic_pile", () -> new BatteryBlock(0)));
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