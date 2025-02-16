package com.nred.nuclearcraft.registration;

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
            .title(Component.translatable("creative_tab.title.materials"))
            .icon(() -> ALLOY_MAP.get("extreme").get().getDefaultInstance())
            .displayItems((parameters, output) -> {
                output.acceptAll(blockStackValues(ORE_MAP, INGOT_BLOCK_MAP));
                output.acceptAll(itemStackValues(INGOT_MAP, NUGGET_MAP, RAW_MAP, DUST_MAP, GEM_MAP, GEM_DUST_MAP, ALLOY_MAP, COMPOUND_MAP, PART_MAP, PART_BLOCK_MAP, AMERICIUM_MAP, BERKELIUM_MAP, BORON_MAP, CALIFORNIUM_MAP, CURIUM_MAP, DEPLETED_FUEL_AMERICIUM_MAP, DEPLETED_FUEL_BERKELIUM_MAP, DEPLETED_FUEL_CALIFORNIUM_MAP, DEPLETED_FUEL_CURIUM_MAP, DEPLETED_FUEL_IC2_MAP, DEPLETED_FUEL_MIXED_MAP, DEPLETED_FUEL_NEPTUNIUM_MAP, DEPLETED_FUEL_PLUTONIUM_MAP, DEPLETED_FUEL_THORIUM_MAP, DEPLETED_FUEL_URANIUM_MAP, FUEL_AMERICIUM_MAP, FUEL_BERKELIUM_MAP, FUEL_CALIFORNIUM_MAP, FUEL_CURIUM_MAP, FUEL_MIXED_MAP, FUEL_NEPTUNIUM_MAP, FUEL_PLUTONIUM_MAP, FUEL_THORIUM_MAP, FUEL_URANIUM_MAP, LITHIUM_MAP, NEPTUNIUM_MAP, PLUTONIUM_MAP, THORIUM_MAP));
                output.accept(SUPERCOLD_ICE);
            }).build());

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> MACHINES_TAB = CREATIVE_MODE_TABS.register("machines_tab", () -> CreativeModeTab.builder()
            .title(Component.translatable("creative_tab.title.machines"))
            .withTabsBefore(MATERIALS_TAB.getId())
            .icon(() -> PROCESSOR_MAP.get("manufactory").asItem().getDefaultInstance())
            .displayItems((parameters, output) -> {
                output.acceptAll(blockStackValues(PROCESSOR_MAP, SOLAR_MAP, COLLECTOR_MAP));
                output.acceptAll(itemStackValues(UPGRADE_MAP));
                output.accept(LITHIUM_ION_CELL);
            }).build());

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> MISC_TAB = CREATIVE_MODE_TABS.register("misc_tab", () -> CreativeModeTab.builder()
            .title(Component.translatable("creative_tab.title.miscellaneous"))
            .withTabsBefore(MACHINES_TAB.getId())
            .icon(() -> FOOD_MAP.get("smore").asItem().getDefaultInstance())
            .displayItems((parameters, output) -> {
                output.acceptAll(itemStackValues(MUSIC_DISC_MAP, FOOD_MAP));
                output.accept(FOURSMORE);
                output.accept(PORTABLE_ENDER_CHEST);
                output.accept(SOLIDIFIED_CORIUM);
            }).build());

    public static void init() {
    }
}