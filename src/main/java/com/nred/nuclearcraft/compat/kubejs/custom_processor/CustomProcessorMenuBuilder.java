package com.nred.nuclearcraft.compat.kubejs.custom_processor;

import com.nred.nuclearcraft.menu.processor.ProcessorMenu;
import com.nred.nuclearcraft.menu.processor.ProcessorMenuImpl.BasicEnergyProcessorMenuDyn;
import com.nred.nuclearcraft.menu.processor.ProcessorMenuImpl.BasicUpgradableEnergyProcessorMenuDyn;
import dev.latvian.mods.kubejs.registry.BuilderBase;
import dev.latvian.mods.rhino.util.ReturnsSelf;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredHolder;

import static com.nred.nuclearcraft.handler.BlockEntityInfoHandler.getProcessorMenuUpgradable;
import static com.nred.nuclearcraft.registration.MenuRegistration.PROCESSOR_MENU_TYPES;

@ReturnsSelf
public class CustomProcessorMenuBuilder extends BuilderBase<MenuType<ProcessorMenu<?, ?, ?, ?>>> {
    public CustomProcessorMenuBuilder(ResourceLocation i) {
        super(i);
    }

    @Override
    public MenuType<ProcessorMenu<?, ?, ?, ?>> createObject() {
        String name = id.toString();

        PROCESSOR_MENU_TYPES.put(name, DeferredHolder.create(Registries.MENU, this.id));

        if (getProcessorMenuUpgradable(name)) {
            return IMenuTypeExtension.create((containerId, inventory, extraData) -> new BasicUpgradableEnergyProcessorMenuDyn(PROCESSOR_MENU_TYPES.get(name).get(), containerId, inventory, extraData));
        } else {
            return IMenuTypeExtension.create((containerId, inventory, extraData) -> new BasicEnergyProcessorMenuDyn(PROCESSOR_MENU_TYPES.get(name).get(), containerId, inventory, extraData));
        }
    }
}