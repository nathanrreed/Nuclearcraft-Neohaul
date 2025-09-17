package com.nred.nuclearcraft.screen.processor;

import com.nred.nuclearcraft.menu.processor.SaltMixerMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

import static com.nred.nuclearcraft.config.Config.PROCESSOR_CONFIG_MAP;

public class FluidMixerScreen extends ProcessorScreen<SaltMixerMenu> {
    public FluidMixerScreen(SaltMixerMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title, "fluid_mixer", PROCESSOR_CONFIG_MAP.get("fluid_mixer").base_power(), 84, 0);
    }
}