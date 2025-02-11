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
                output.acceptAll(itemStackValues(INGOT_MAP, NUGGET_MAP, RAW_MAP, DUST_MAP, GEM_MAP, GEM_DUST_MAP, ALLOY_MAP, COMPOUND_MAP, PART_MAP, PART_BLOCK_MAP, URANIUM_MAP));
            }).build());

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> MACHINES_TAB = CREATIVE_MODE_TABS.register("machines_tab", () -> CreativeModeTab.builder()
            .title(Component.translatable("creative_tab.title.machines"))
            .withTabsBefore(MATERIALS_TAB.getId())
            .icon(() -> PROCESSOR_MAP.get("manufactory").asItem().getDefaultInstance())
            .displayItems((parameters, output) -> {
                output.acceptAll(blockStackValues(PROCESSOR_MAP, COLLECTOR_MAP));
                output.acceptAll(itemStackValues(UPGRADE_MAP));
            }).build());

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> MISC_TAB = CREATIVE_MODE_TABS.register("misc_tab", () -> CreativeModeTab.builder()
            .title(Component.translatable("creative_tab.title.miscellaneous"))
            .withTabsBefore(MACHINES_TAB.getId())
            .icon(() -> FOOD_MAP.get("smore").asItem().getDefaultInstance())
            .displayItems((parameters, output) -> {
                output.acceptAll(itemStackValues(MUSIC_DISC_MAP, FOOD_MAP));
                output.accept(FOURSMORE);
                output.accept(PORTABLE_ENDER_CHEST);
            }).build());

    public static void init() {
    }
}