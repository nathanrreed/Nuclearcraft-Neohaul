package com.nred.nuclearcraft.registration;

import com.nred.nuclearcraft.NuclearcraftNeohaul;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.neoforge.registries.DeferredHolder;

import static com.nred.nuclearcraft.helpers.Concat.blockStackValues;
import static com.nred.nuclearcraft.helpers.Concat.itemStackValues;
import static com.nred.nuclearcraft.registration.BlockRegistration.*;
import static com.nred.nuclearcraft.registration.ItemRegistration.*;
import static com.nred.nuclearcraft.registration.Registers.CREATIVE_MODE_TABS;

public class CreativeTabsRegistration {
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> MATERIALS_TAB = CREATIVE_MODE_TABS.register("materials_tab", () -> CreativeModeTab.builder()
            .title(Component.translatable(NuclearcraftNeohaul.MODID + ".creative_tab.title.materials"))
            .icon(() -> ALLOY_MAP.get("extreme").get().getDefaultInstance())
            .displayItems((parameters, output) -> {
                output.acceptAll(blockStackValues(ORE_MAP, INGOT_BLOCK_MAP, RAW_BLOCK_MAP, FERTILE_ISOTOPE_MAP));
                output.acceptAll(itemStackValues(INGOT_MAP, NUGGET_MAP, RAW_MAP, DUST_MAP, GEM_MAP, GEM_DUST_MAP, ALLOY_MAP, COMPOUND_MAP, PART_MAP, PART_BLOCK_MAP, AMERICIUM_MAP, BERKELIUM_MAP, BORON_MAP, CALIFORNIUM_MAP, CURIUM_MAP, DEPLETED_FUEL_AMERICIUM_MAP, DEPLETED_FUEL_BERKELIUM_MAP, DEPLETED_FUEL_CALIFORNIUM_MAP, DEPLETED_FUEL_CURIUM_MAP, DEPLETED_FUEL_IC2_MAP, DEPLETED_FUEL_MIXED_MAP, DEPLETED_FUEL_NEPTUNIUM_MAP, DEPLETED_FUEL_PLUTONIUM_MAP, DEPLETED_FUEL_THORIUM_MAP, DEPLETED_FUEL_URANIUM_MAP, FUEL_AMERICIUM_MAP, FUEL_BERKELIUM_MAP, FUEL_CALIFORNIUM_MAP, FUEL_CURIUM_MAP, FUEL_MIXED_MAP, FUEL_NEPTUNIUM_MAP, FUEL_PLUTONIUM_MAP, FUEL_THORIUM_MAP, FUEL_URANIUM_MAP, LITHIUM_MAP, NEPTUNIUM_MAP, PLUTONIUM_MAP)); //, THORIUM_MAP
                output.accept(SUPERCOLD_ICE);
            }).build());

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> MACHINES_TAB = CREATIVE_MODE_TABS.register("machines_tab", () -> CreativeModeTab.builder()
            .title(Component.translatable(NuclearcraftNeohaul.MODID + ".creative_tab.title.machines"))
            .withTabsBefore(MATERIALS_TAB.getId())
            .icon(() -> PROCESSOR_MAP.get("manufactory").asItem().getDefaultInstance())
            .displayItems((parameters, output) -> {
                output.accept(NUCLEAR_FURNACE);
                output.acceptAll(blockStackValues(PROCESSOR_MAP));
                output.accept(MACHINE_INTERFACE);
                output.acceptAll(blockStackValues(SOLAR_MAP));
                output.accept(DECAY_GENERATOR);
                output.accept(UNIVERSAL_BIN);
                output.acceptAll(blockStackValues(COLLECTOR_MAP));
                output.acceptAll(itemStackValues(UPGRADE_MAP));
                output.accept(LITHIUM_ION_CELL);
                output.accept(MULTITOOL);
            }).build());

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> MULTIBLOCKS_TAB = CREATIVE_MODE_TABS.register("multiblocks_tab", () -> CreativeModeTab.builder()
            .title(Component.translatable(NuclearcraftNeohaul.MODID + ".creative_tab.title.multiblocks"))
            .withTabsBefore(MACHINES_TAB.getId())
            .icon(() -> FISSION_REACTOR_MAP.get("solid_fuel_fission_controller").asItem().getDefaultInstance())
            .displayItems((parameters, output) -> {
                output.acceptAll(blockStackValues(RTG_MAP, BATTERY_MAP, FISSION_REACTOR_MAP, TURBINE_MAP));
            }).build());

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> RADIATION_TAB = CREATIVE_MODE_TABS.register("radiation_tab", () -> CreativeModeTab.builder()
            .title(Component.translatable(NuclearcraftNeohaul.MODID + ".creative_tab.title.radiation"))
            .withTabsBefore(MULTIBLOCKS_TAB.getId())
            .icon(() -> GLOWING_MUSHROOM.asItem().getDefaultInstance()) // TODO change to geiger counter
            .displayItems((parameters, output) -> {
                output.accept(GLOWING_MUSHROOM);
            }).build());

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> MISC_TAB = CREATIVE_MODE_TABS.register("misc_tab", () -> CreativeModeTab.builder()
            .title(Component.translatable(NuclearcraftNeohaul.MODID + ".creative_tab.title.miscellaneous"))
            .withTabsBefore(RADIATION_TAB.getId())
            .icon(() -> FOOD_MAP.get("smore").asItem().getDefaultInstance())
            .displayItems((parameters, output) -> {
                output.accept(TRITIUM_LAMP);
                output.accept(SOLIDIFIED_CORIUM);
                output.accept(PORTABLE_ENDER_CHEST);
                output.accept(HEAVY_WATER_MODERATOR);
                output.acceptAll(itemStackValues(MUSIC_DISC_MAP, FOOD_MAP));
                output.accept(FOURSMORE);
            }).build());

    public static void init() {
    }
}