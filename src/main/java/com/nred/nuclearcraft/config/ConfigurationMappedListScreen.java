package com.nred.nuclearcraft.config;

import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.List;

public class ConfigurationMappedListScreen<T> extends ConfigurationScreen.ConfigurationListScreen<T> {
    private final String order;

    public ConfigurationMappedListScreen(Context context, String key, Component title, String order, ModConfigSpec.ListValueSpec spec, ModConfigSpec.ConfigValue<List<T>> valueList) {
        super(context, key, title, spec, valueList);
        this.order = order;
    }

    @Override
    protected AbstractWidget createListLabel(int idx) {
        return new StringWidget(0, 0, Button.DEFAULT_WIDTH, Button.DEFAULT_HEIGHT, Component.literal(Component.translatable(this.order).getString().split(",")[idx].strip()), font).alignRight();
    }

    @Override
    public ConfigurationScreen.ConfigurationSectionScreen rebuild() {
        return super.rebuild();
    }
}