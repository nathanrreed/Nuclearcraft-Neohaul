package com.nred.nuclearcraft.registration;

import com.nred.nuclearcraft.NCInfo;
import com.nred.nuclearcraft.block.SolidifiedCorium;
import com.nred.nuclearcraft.block.SupercoldIceBlock;
import com.nred.nuclearcraft.block.battery.BlockBattery;
import com.nred.nuclearcraft.block.item.NCItemBlock;
import com.nred.nuclearcraft.block.item.energy.ItemBlockBattery;
import com.nred.nuclearcraft.block.processor.BlockProcessor;
import com.nred.nuclearcraft.block.processor.NuclearFurnaceBlock;
import com.nred.nuclearcraft.block.tile.BlockSimpleTile;
import com.nred.nuclearcraft.block.tile.dummy.BlockMachineInterface;
import com.nred.nuclearcraft.multiblock.battery.BatteryPartType;
import com.nred.nuclearcraft.multiblock.battery.BatteryType;
import com.nred.nuclearcraft.multiblock.fisson.FissionPartType;
import com.nred.nuclearcraft.multiblock.fisson.FissionSourceType;
import com.nred.nuclearcraft.multiblock.fisson.molten_salt.FissionCoolantHeaterPortType;
import com.nred.nuclearcraft.multiblock.fisson.molten_salt.FissionCoolantHeaterType;
import com.nred.nuclearcraft.multiblock.fisson.solid.FissionHeatSinkType;
import com.nred.nuclearcraft.multiblock.hx.HeatExchangerPartType;
import com.nred.nuclearcraft.multiblock.hx.HeatExchangerTubeType;
import com.nred.nuclearcraft.multiblock.machine.MachinePartType;
import com.nred.nuclearcraft.multiblock.rtg.RTGPartType;
import com.nred.nuclearcraft.multiblock.rtg.RTGType;
import com.nred.nuclearcraft.multiblock.turbine.TurbineDynamoCoilType;
import com.nred.nuclearcraft.multiblock.turbine.TurbinePartType;
import com.nred.nuclearcraft.multiblock.turbine.TurbineRotorBladeType;
import com.nred.nuclearcraft.property.MachinePortSorption;
import com.nred.nuclearcraft.util.InfoHelper;
import com.nred.nuclearcraft.util.NCMath;
import com.nred.nuclearcraft.util.PrimitiveFunction.ObjEnumFunction;
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
import static com.nred.nuclearcraft.config.NCConfig.*;
import static com.nred.nuclearcraft.info.Names.*;
import static com.nred.nuclearcraft.multiblock.fisson.FissionNeutronShieldType.BORON_SILVER;
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
    public static final BooleanProperty INVISIBLE = BooleanProperty.create("invisible");
    public static final EnumProperty<Direction.Axis> AXIS_ALL = BlockStateProperties.AXIS;
    public static final EnumProperty<MachinePortSorption> MACHINE_PORT_SORPTION = EnumProperty.create("machine_port_sorption", MachinePortSorption.class);

    public static final HashMap<String, DeferredBlock<Block>> ORE_MAP = createOres();
    public static final HashMap<String, DeferredBlock<Block>> INGOT_BLOCK_MAP = createBlocks(INGOTS, "block", Blocks.IRON_BLOCK);
    public static final HashMap<String, DeferredBlock<Block>> MATERIAL_BLOCK_MAP = createBlocks(MATERIAL_BLOCKS, "block", Blocks.IRON_BLOCK);
    public static final HashMap<String, DeferredBlock<Block>> RAW_BLOCK_MAP = createBlocks(RAWS, "raw", "block", Blocks.RAW_IRON_BLOCK);
    public static final HashMap<String, DeferredBlock<Block>> FERTILE_ISOTOPE_MAP = createBlocks(FERTILE_ISOTOPES, "fertile_isotope", "block", Blocks.IRON_BLOCK);
    public static final HashMap<String, DeferredBlock<Block>> COLLECTOR_MAP = createCollectors();
    public static final HashMap<String, DeferredBlock<Block>> SOLAR_MAP = createSolarPanels();
    public static final HashMap<String, DeferredBlock<Block>> TURBINE_MAP = createTurbineParts();
    public static final HashMap<String, DeferredBlock<Block>> HX_MAP = createHeatExchangerParts();
    public static final HashMap<String, DeferredBlock<Block>> FISSION_REACTOR_MAP = createFissionParts();
    public static final HashMap<String, DeferredBlock<Block>> BATTERY_MAP = createBatteries();
    public static final HashMap<String, DeferredBlock<Block>> RTG_MAP = createRTGs();
    public static final HashMap<String, DeferredBlock<Block>> MACHINE_MAP = createMachines();
    public static final HashMap<String, DeferredBlock<Block>> DISTILLER_MAP = createHeatDistillers();
    public static final HashMap<String, DeferredBlock<Block>> ELECTROLYZER_MAP = createElectrolyzers();
    public static final HashMap<String, DeferredBlock<Block>> INFILTRATOR_MAP = createInfiltrators();

    public static final HashMap<String, DeferredBlock<Block>> PROCESSOR_MAP = createProcessors();

    public static final DeferredBlock<Block> TRITIUM_LAMP = registerBlockItem("tritium_lamp", () -> new Block(BlockBehaviour.Properties.of().lightLevel(blockState -> 15)));
    public static final DeferredBlock<Block> SUPERCOLD_ICE = registerBlockItem("supercold_ice", SupercoldIceBlock::new);

    public static final DeferredBlock<Block> HEAVY_WATER_MODERATOR = registerBlockItem("heavy_water_moderator", () -> new Block(BlockBehaviour.Properties.of()));
    public static final DeferredBlock<Block> SOLIDIFIED_CORIUM = registerBlockItem("solidified_corium", SolidifiedCorium::new);
    public static final DeferredBlock<Block> UNIVERSAL_BIN = registerBlockItemWithTooltip("universal_bin", () -> new BlockSimpleTile<>("bin", false), false);
    public static final DeferredBlock<Block> MACHINE_INTERFACE = registerBlockItemWithTooltip("machine_interface", () -> new BlockMachineInterface("machine_interface"), false);
    public static final DeferredBlock<Block> NUCLEAR_FURNACE = registerBlockItemWithTooltip("nuclear_furnace", () -> new NuclearFurnaceBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.FURNACE)), false);

    public static final DeferredBlock<Block> DECAY_GENERATOR = registerBlockItem("decay_generator", () -> new BlockSimpleTile<>("decay_generator"));


    public static <T extends Block> DeferredBlock<Block> registerBlockItemWithTooltip(String name, Supplier<T> block, boolean hasFixed, Component... tooltip) {
        DeferredBlock<Block> toReturn = BLOCKS.register(name, block);
        ITEMS.register(name, () -> new NCItemBlock(toReturn.get(), ChatFormatting.RED, InfoHelper.EMPTY_ARRAY, hasFixed, ChatFormatting.AQUA, tooltip));
        return toReturn;
    }

    private static DeferredBlock<Block> registerBlockItemWithTooltip(String name, Supplier<Block> block, Function<Block, ? extends BlockItem> itemBlockFunction) {
        DeferredBlock<Block> toReturn = BLOCKS.register(name, block);
        ITEMS.register(name, () -> itemBlockFunction.apply(toReturn.get()));
        return toReturn;
    }

    private static DeferredBlock<Block> registerBlockItem(String name, Supplier<Block> block) {
        DeferredBlock<Block> toReturn = BLOCKS.register(name, block);
        ITEMS.registerSimpleBlockItem(name, toReturn);
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
        map.put("alloy_furnace", registerBlockItemWithTooltip("alloy_furnace", () -> new BlockProcessor<>("alloy_furnace"), false));
        map.put("assembler", registerBlockItemWithTooltip("assembler", () -> new BlockProcessor<>("assembler"), false));
        map.put("centrifuge", registerBlockItemWithTooltip("centrifuge", () -> new BlockProcessor<>("centrifuge"), false));
        map.put("chemical_reactor", registerBlockItemWithTooltip("chemical_reactor", () -> new BlockProcessor<>("chemical_reactor"), false));
        map.put("crystallizer", registerBlockItemWithTooltip("crystallizer", () -> new BlockProcessor<>("crystallizer"), false));
        map.put("decay_hastener", registerBlockItemWithTooltip("decay_hastener", () -> new BlockProcessor<>("decay_hastener"), false));
        map.put("electric_furnace", registerBlockItemWithTooltip("electric_furnace", () -> new BlockProcessor<>("electric_furnace"), false));
        map.put("electrolyzer", registerBlockItemWithTooltip("electrolyzer", () -> new BlockProcessor<>("electrolyzer"), false));
        map.put("fluid_enricher", registerBlockItemWithTooltip("fluid_enricher", () -> new BlockProcessor<>("fluid_enricher"), false));
        map.put("fluid_extractor", registerBlockItemWithTooltip("fluid_extractor", () -> new BlockProcessor<>("fluid_extractor"), false));
        map.put("fluid_infuser", registerBlockItemWithTooltip("fluid_infuser", () -> new BlockProcessor<>("fluid_infuser"), false));
        map.put("fluid_mixer", registerBlockItemWithTooltip("fluid_mixer", () -> new BlockProcessor<>("fluid_mixer"), false));
        map.put("fuel_reprocessor", registerBlockItemWithTooltip("fuel_reprocessor", () -> new BlockProcessor<>("fuel_reprocessor"), false));
        map.put("ingot_former", registerBlockItemWithTooltip("ingot_former", () -> new BlockProcessor<>("ingot_former"), false));
        map.put("manufactory", registerBlockItemWithTooltip("manufactory", () -> new BlockProcessor<>("manufactory"), false));
        map.put("melter", registerBlockItemWithTooltip("melter", () -> new BlockProcessor<>("melter"), false));
        map.put("pressurizer", registerBlockItemWithTooltip("pressurizer", () -> new BlockProcessor<>("pressurizer"), false));
        map.put("rock_crusher", registerBlockItemWithTooltip("rock_crusher", () -> new BlockProcessor<>("rock_crusher"), false));
        map.put("separator", registerBlockItemWithTooltip("separator", () -> new BlockProcessor<>("separator"), false));
        map.put("supercooler", registerBlockItemWithTooltip("supercooler", () -> new BlockProcessor<>("supercooler"), false));
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
        map.put("turbine_controller", registerBlockItem("turbine_controller", TurbinePartType.Controller::createBlock));
        map.put("turbine_casing", registerBlockItem("turbine_casing", TurbinePartType.Casing::createBlock));
        map.put("turbine_glass", registerBlockItem("turbine_glass", TurbinePartType.Glass::createBlock));
        map.put("turbine_rotor_shaft", registerBlockItem("turbine_rotor_shaft", TurbinePartType.RotorShaft::createBlock));
        ObjEnumFunction<Block, BlockItem> bladeFunction = (x, y) -> new NCItemBlock(x, new ChatFormatting[]{ChatFormatting.LIGHT_PURPLE, ChatFormatting.GRAY}, NCInfo.rotorBladeFixedInfo((TurbineRotorBladeType) y), true, ChatFormatting.AQUA, NCInfo.rotorBladeInfo());
        map.put("steel_turbine_rotor_blade", registerBlockItemWithTooltip("steel_turbine_rotor_blade", () -> TurbinePartType.RotorBlade.createBlock(STEEL), x -> bladeFunction.apply(x, STEEL)));
        map.put("extreme_alloy_turbine_rotor_blade", registerBlockItemWithTooltip("extreme_alloy_turbine_rotor_blade", () -> TurbinePartType.RotorBlade.createBlock(EXTREME), x -> bladeFunction.apply(x, EXTREME)));
        map.put("sic_turbine_rotor_blade", registerBlockItemWithTooltip("sic_turbine_rotor_blade", () -> TurbinePartType.RotorBlade.createBlock(SIC_SIC_CMC), x -> bladeFunction.apply(x, SIC_SIC_CMC)));
        map.put("standard_turbine_rotor_stator", registerBlockItemWithTooltip("standard_turbine_rotor_stator", () -> TurbinePartType.RotorStator.createBlock(STANDARD), x -> new NCItemBlock(x, ChatFormatting.GRAY, NCInfo.rotorStatorFixedInfo(STANDARD), true, ChatFormatting.AQUA, NCInfo.rotorStatorInfo())));
        map.put("turbine_rotor_bearing", registerBlockItem("turbine_rotor_bearing", TurbinePartType.RotorBearing::createBlock));
        ObjEnumFunction<Block, BlockItem> coilFunction = (x, y) -> new NCItemBlock(x, new ChatFormatting[]{ChatFormatting.LIGHT_PURPLE, ChatFormatting.GRAY}, NCInfo.dynamoCoilFixedInfo((TurbineDynamoCoilType) y), true, ChatFormatting.AQUA, false);
        map.put("magnesium_turbine_dynamo_coil", registerBlockItemWithTooltip("magnesium_turbine_dynamo_coil", () -> TurbinePartType.Dynamo.createBlock(MAGNESIUM), x -> coilFunction.apply(x, MAGNESIUM)));
        map.put("beryllium_turbine_dynamo_coil", registerBlockItemWithTooltip("beryllium_turbine_dynamo_coil", () -> TurbinePartType.Dynamo.createBlock(BERYLLIUM), x -> coilFunction.apply(x, BERYLLIUM)));
        map.put("aluminum_turbine_dynamo_coil", registerBlockItemWithTooltip("aluminum_turbine_dynamo_coil", () -> TurbinePartType.Dynamo.createBlock(ALUMINUM), x -> coilFunction.apply(x, ALUMINUM)));
        map.put("gold_turbine_dynamo_coil", registerBlockItemWithTooltip("gold_turbine_dynamo_coil", () -> TurbinePartType.Dynamo.createBlock(GOLD), x -> coilFunction.apply(x, GOLD)));
        map.put("copper_turbine_dynamo_coil", registerBlockItemWithTooltip("copper_turbine_dynamo_coil", () -> TurbinePartType.Dynamo.createBlock(COPPER), x -> coilFunction.apply(x, COPPER)));
        map.put("silver_turbine_dynamo_coil", registerBlockItemWithTooltip("silver_turbine_dynamo_coil", () -> TurbinePartType.Dynamo.createBlock(SILVER), x -> coilFunction.apply(x, SILVER)));
        map.put("turbine_coil_connector", registerBlockItem("turbine_coil_connector", TurbinePartType.DynamoConnector::createBlock));
        map.put("turbine_inlet", registerBlockItem("turbine_inlet", TurbinePartType.Inlet::createBlock));
        map.put("turbine_outlet", registerBlockItem("turbine_outlet", TurbinePartType.Outlet::createBlock));
        map.put("turbine_redstone_port", registerBlockItem("turbine_redstone_port", TurbinePartType.RedstonePort::createBlock));
        map.put("turbine_computer_port", registerBlockItemWithTooltip("turbine_computer_port", TurbinePartType.ComputerPort::createBlock, false));
        return map;
    }

    private static HashMap<String, DeferredBlock<Block>> createHeatExchangerParts() {
        HashMap<String, DeferredBlock<Block>> map = new LinkedHashMap<>();
        map.put("heat_exchanger_controller", registerBlockItem("heat_exchanger_controller", HeatExchangerPartType.HeatExchangerController::createBlock));
        map.put("condenser_controller", registerBlockItem("condenser_controller", HeatExchangerPartType.CondenserController::createBlock));
        map.put("heat_exchanger_casing", registerBlockItem("heat_exchanger_casing", HeatExchangerPartType.Casing::createBlock));
        map.put("heat_exchanger_glass", registerBlockItem("heat_exchanger_glass", HeatExchangerPartType.Glass::createBlock));
        map.put("heat_exchanger_inlet", registerBlockItem("heat_exchanger_inlet", HeatExchangerPartType.Inlet::createBlock));
        map.put("heat_exchanger_outlet", registerBlockItem("heat_exchanger_outlet", HeatExchangerPartType.Outlet::createBlock));
        ObjEnumFunction<Block, NCItemBlock> hxTubeItemBlockFunction = (x, y) -> new NCItemBlock(x, new ChatFormatting[]{ChatFormatting.YELLOW, ChatFormatting.BLUE}, NCInfo.hxTubeFixedInfo((HeatExchangerTubeType) y), true, ChatFormatting.AQUA, NCInfo.hxTubeInfo());
        map.put("copper_heat_exchanger_tube", registerBlockItemWithTooltip("copper_heat_exchanger_tube", () -> HeatExchangerPartType.Tube.createBlock(HeatExchangerTubeType.COPPER), x -> hxTubeItemBlockFunction.apply(x, HeatExchangerTubeType.COPPER)));
        map.put("hard_carbon_heat_exchanger_tube", registerBlockItemWithTooltip("hard_carbon_heat_exchanger_tube", () -> HeatExchangerPartType.Tube.createBlock(HeatExchangerTubeType.HARD_CARBON), x -> hxTubeItemBlockFunction.apply(x, HeatExchangerTubeType.HARD_CARBON)));
        map.put("thermoconducting_alloy_heat_exchanger_tube", registerBlockItemWithTooltip("thermoconducting_alloy_heat_exchanger_tube", () -> HeatExchangerPartType.Tube.createBlock(HeatExchangerTubeType.THERMOCONDUCTING), x -> hxTubeItemBlockFunction.apply(x, HeatExchangerTubeType.THERMOCONDUCTING)));
        map.put("heat_exchanger_shell_baffle", registerBlockItem("heat_exchanger_shell_baffle", HeatExchangerPartType.Baffle::createBlock));
        map.put("heat_exchanger_redstone_port", registerBlockItem("heat_exchanger_redstone_port", HeatExchangerPartType.RedstonePort::createBlock));
        map.put("heat_exchanger_computer_port", registerBlockItemWithTooltip("heat_exchanger_computer_port", HeatExchangerPartType.ComputerPort::createBlock, false));
        return map;
    }

    private static HashMap<String, DeferredBlock<Block>> createFissionParts() {
        HashMap<String, DeferredBlock<Block>> map = new LinkedHashMap<>();
        map.put("fission_casing", registerBlockItem("fission_reactor_casing", FissionPartType.Casing::createBlock));
        map.put("fission_glass", registerBlockItem("fission_glass", FissionPartType.Glass::createBlock));
        map.put("fission_conductor", registerBlockItem("fission_conductor", FissionPartType.Conductor::createBlock));
        map.put("fission_monitor", registerBlockItem("fission_monitor", FissionPartType.Monitor::createBlock));
        map.put("beryllium_carbon_reflector", registerBlockItem("beryllium_carbon_reflector", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK))));
        map.put("lead_steel_reflector", registerBlockItem("lead_steel_reflector", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK))));
        map.put("fission_vent", registerBlockItem("fission_vent", FissionPartType.Vent::createBlock));
        map.put("fission_irradiator", registerBlockItem("fission_irradiator", FissionPartType.Irradiator::createBlock));
        map.put("fission_cooler", registerBlockItem("fission_cooler", FissionPartType.Cooler::createBlock));
        ObjEnumFunction<Block, BlockItem> sourceFunction = (x, y) -> new NCItemBlock(x, ChatFormatting.LIGHT_PURPLE, NCInfo.neutronSourceFixedInfo((FissionSourceType) y), true, ChatFormatting.AQUA, false, NCInfo.neutronSourceInfo());
        map.put("radium_beryllium_source", registerBlockItemWithTooltip("radium_beryllium_source", () -> FissionPartType.Source.createBlock(FissionSourceType.RADIUM_BERYLLIUM), x -> sourceFunction.apply(x, FissionSourceType.RADIUM_BERYLLIUM)));
        map.put("polonium_beryllium_source", registerBlockItemWithTooltip("polonium_beryllium_source", () -> FissionPartType.Source.createBlock(FissionSourceType.POLONIUM_BERYLLIUM), x -> sourceFunction.apply(x, FissionSourceType.POLONIUM_BERYLLIUM)));
        map.put("californium_source", registerBlockItemWithTooltip("californium_source", () -> FissionPartType.Source.createBlock(FissionSourceType.CALIFORNIUM), x -> sourceFunction.apply(x, FissionSourceType.CALIFORNIUM)));
        map.put("boron_silver_shield", registerBlockItemWithTooltip("boron_silver_shield", () -> FissionPartType.Shield.createBlock(BORON_SILVER), x -> new NCItemBlock(x, new ChatFormatting[]{ChatFormatting.YELLOW, ChatFormatting.LIGHT_PURPLE}, NCInfo.neutronShieldFixedInfo(BORON_SILVER), true, ChatFormatting.AQUA, false, NCInfo.neutronShieldInfo())));
        map.put("fission_computer_port", registerBlockItemWithTooltip("fission_computer_port", FissionPartType.ComputerPort::createBlock, false));
        map.put("fission_irradiator_port", registerBlockItem("fission_irradiator_port", FissionPartType.IrradiatorPort::createBlock));
        map.put("fission_cooler_port", registerBlockItem("fission_cooler_port", FissionPartType.CoolerPort::createBlock));
        map.put("fission_shield_manager", registerBlockItem("fission_shield_manager", FissionPartType.ShieldManager::createBlock));
        map.put("fission_source_manager", registerBlockItem("fission_source_manager", FissionPartType.SourceManager::createBlock));
        map.put("fission_power_port", registerBlockItem("fission_power_port", FissionPartType.PowerPort::createBlock));
        // Solid
        map.put("solid_fuel_fission_controller", registerBlockItem("solid_fuel_fission_controller", FissionPartType.SolidFuelController::createBlock));
        map.put("fission_fuel_cell", registerBlockItem("fission_fuel_cell", FissionPartType.Cell::createBlock));
        map.put("fission_fuel_cell_port", registerBlockItem("fission_fuel_cell_port", FissionPartType.CellPort::createBlock));
        map.put("water_fission_heat_sink", registerBlockItemWithTooltip("water_fission_heat_sink", () -> FissionPartType.HeatSink.createBlock(FissionHeatSinkType.WATER), x -> new NCItemBlock(x, ChatFormatting.BLUE, NCInfo.sinkCoolingRateFixedInfo(FissionHeatSinkType.WATER), true, ChatFormatting.AQUA, false)));
        for (String name : COOLANTS) {
            map.put(name + "_fission_heat_sink", registerBlockItemWithTooltip(name + "_fission_heat_sink", () -> FissionPartType.HeatSink.createBlock(FissionHeatSinkType.valueOf(name.toUpperCase())), x -> new NCItemBlock(x, ChatFormatting.BLUE, NCInfo.sinkCoolingRateFixedInfo(FissionHeatSinkType.valueOf(name.toUpperCase())), true, ChatFormatting.AQUA, false)));
        }
        // Salt
        map.put("molten_salt_fission_controller", registerBlockItem("molten_salt_fission_controller", FissionPartType.MoltenSaltController::createBlock));
        map.put("fission_fuel_vessel", registerBlockItem("fission_fuel_vessel", FissionPartType.Vessel::createBlock));
        map.put("fission_fuel_vessel_port", registerBlockItem("fission_fuel_vessel_port", FissionPartType.VesselPort::createBlock));
        map.put("standard_fission_coolant_heater", registerBlockItemWithTooltip("standard_fission_coolant_heater", () -> FissionPartType.Heater.createBlock(FissionCoolantHeaterType.STANDARD), x -> new NCItemBlock(x, ChatFormatting.BLUE, NCInfo.heaterCoolingRateFixedInfo(FissionCoolantHeaterType.STANDARD), true, ChatFormatting.AQUA, false)));
        for (String name : COOLANTS) {
            map.put(name + "_fission_coolant_heater", registerBlockItemWithTooltip(name + "_fission_coolant_heater", () -> FissionPartType.Heater.createBlock(FissionCoolantHeaterType.valueOf(name.toUpperCase())), x -> new NCItemBlock(x, ChatFormatting.BLUE, NCInfo.heaterCoolingRateFixedInfo(FissionCoolantHeaterType.valueOf(name.toUpperCase())), true, ChatFormatting.AQUA, false)));
        }
        map.put("standard_fission_coolant_heater_port", registerBlockItem("standard_fission_coolant_heater_port", () -> FissionPartType.HeaterPort.createBlock(FissionCoolantHeaterPortType.STANDARD)));
        for (String name : COOLANTS) {
            map.put(name + "_fission_coolant_heater_port", registerBlockItem(name + "_fission_coolant_heater_port", () -> FissionPartType.HeaterPort.createBlock(FissionCoolantHeaterPortType.valueOf(name.toUpperCase()))));
        }
        return map;
    }

    private static HashMap<String, DeferredBlock<Block>> createBatteries() {
        Function<Block, ItemBlockBattery> itemBlockBatteryFunction = x -> new ItemBlockBattery((BlockBattery) x, NCInfo.batteryInfo());
        HashMap<String, DeferredBlock<Block>> map = new LinkedHashMap<>();
        map.put("basic_voltaic_pile", registerBlockItemWithTooltip("basic_voltaic_pile", () -> BatteryPartType.BATTERY.createBlock(BatteryType.VOLTAIC_PILE_BASIC), itemBlockBatteryFunction));
        map.put("advanced_voltaic_pile", registerBlockItemWithTooltip("advanced_voltaic_pile", () -> BatteryPartType.BATTERY.createBlock(BatteryType.VOLTAIC_PILE_ADVANCED), itemBlockBatteryFunction));
        map.put("du_voltaic_pile", registerBlockItemWithTooltip("du_voltaic_pile", () -> BatteryPartType.BATTERY.createBlock(BatteryType.VOLTAIC_PILE_DU), itemBlockBatteryFunction));
        map.put("elite_voltaic_pile", registerBlockItemWithTooltip("elite_voltaic_pile", () -> BatteryPartType.BATTERY.createBlock(BatteryType.VOLTAIC_PILE_ELITE), itemBlockBatteryFunction));

        map.put("basic_lithium_ion_battery", registerBlockItemWithTooltip("basic_lithium_ion_battery", () -> BatteryPartType.BATTERY.createBlock(BatteryType.LITHIUM_ION_BATTERY_BASIC), itemBlockBatteryFunction));
        map.put("advanced_lithium_ion_battery", registerBlockItemWithTooltip("advanced_lithium_ion_battery", () -> BatteryPartType.BATTERY.createBlock(BatteryType.LITHIUM_ION_BATTERY_ADVANCED), itemBlockBatteryFunction));
        map.put("du_lithium_ion_battery", registerBlockItemWithTooltip("du_lithium_ion_battery", () -> BatteryPartType.BATTERY.createBlock(BatteryType.LITHIUM_ION_BATTERY_DU), itemBlockBatteryFunction));
        map.put("elite_lithium_ion_battery", registerBlockItemWithTooltip("elite_lithium_ion_battery", () -> BatteryPartType.BATTERY.createBlock(BatteryType.LITHIUM_ION_BATTERY_ELITE), itemBlockBatteryFunction));
        return map;
    }

    private static HashMap<String, DeferredBlock<Block>> createRTGs() {
        HashMap<String, DeferredBlock<Block>> map = new LinkedHashMap<>();
        map.put("rtg_uranium", registerBlockItemWithTooltip("rtg_uranium", () -> RTGPartType.RTG.createBlock(RTGType.URANIUM), false, NCInfo.rtgInfo(() -> rtg_power[0])));
        map.put("rtg_plutonium", registerBlockItemWithTooltip("rtg_plutonium", () -> RTGPartType.RTG.createBlock((RTGType.PLUTONIUM)), false, NCInfo.rtgInfo(() -> rtg_power[1])));
        map.put("rtg_americium", registerBlockItemWithTooltip("rtg_americium", () -> RTGPartType.RTG.createBlock((RTGType.AMERICIUM)), false, NCInfo.rtgInfo(() -> rtg_power[2])));
        map.put("rtg_californium", registerBlockItemWithTooltip("rtg_californium", () -> RTGPartType.RTG.createBlock((RTGType.CALIFORNIUM)), false, NCInfo.rtgInfo(() -> rtg_power[3])));
        return map;
    }

    private static HashMap<String, DeferredBlock<Block>> createMachines() {
        HashMap<String, DeferredBlock<Block>> map = new LinkedHashMap<>();
        map.put("large_machine_frame", registerBlockItem("machine_casing", MachinePartType.Frame::createBlock));
        map.put("large_machine_glass", registerBlockItem("machine_glass", MachinePartType.Glass::createBlock));
        map.put("large_machine_power_port", registerBlockItem("machine_power_port", MachinePartType.PowerPort::createBlock));
        map.put("large_machine_process_port", registerBlockItem("machine_process_port", MachinePartType.ProcessPort::createBlock));
        map.put("large_machine_reservoir_port", registerBlockItem("machine_reservoir_port", MachinePartType.ReservoirPort::createBlock));
        map.put("large_machine_redstone_port", registerBlockItem("machine_redstone_port", MachinePartType.RedstonePort::createBlock));
        map.put("large_machine_computer_port", registerBlockItem("machine_computer_port", MachinePartType.ComputerPort::createBlock));
        map.put("sintered_steel_diaphragm", registerBlockItem("sintered_steel_diaphragm", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.COBBLESTONE))));
        map.put("polyethersulfone_diaphragm", registerBlockItem("polyethersulfone_diaphragm", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.COBBLESTONE))));
        map.put("zirfon_diaphragm", registerBlockItem("zirfon_diaphragm", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.COBBLESTONE))));
        map.put("steel_sieve_assembly", registerBlockItem("steel_sieve_assembly", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK))));
        map.put("polytetrafluoroethene_sieve_assembly", registerBlockItem("polytetrafluoroethene_sieve_assembly", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK))));
        map.put("hastelloy_sieve_assembly", registerBlockItem("hastelloy_sieve_assembly", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK))));
        return map;
    }

    private static HashMap<String, DeferredBlock<Block>> createElectrolyzers() {
        HashMap<String, DeferredBlock<Block>> map = new LinkedHashMap<>();
        return map;
    }

    private static HashMap<String, DeferredBlock<Block>> createHeatDistillers() {
        HashMap<String, DeferredBlock<Block>> map = new LinkedHashMap<>();
        return map;
    }

    private static HashMap<String, DeferredBlock<Block>> createInfiltrators() {
        HashMap<String, DeferredBlock<Block>> map = new LinkedHashMap<>();
        return map;
    }

    public static void init() {
    }
}