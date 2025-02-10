package com.nred.nuclearcraft.gui;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.Map;

import static com.nred.nuclearcraft.helpers.Location.ncLoc;

public class ChangeSideConfigButton extends Button {
    private final ResourceLocation texture;
    public final String type;
    private static final Map<String, ChatFormatting> color = Map.of(
            "energy_upgrade", ChatFormatting.YELLOW,
            "speed_upgrade", ChatFormatting.DARK_BLUE,
            "fluid_input", ChatFormatting.DARK_AQUA,
            "fluid_output", ChatFormatting.RED,
            "item_input", ChatFormatting.BLUE,
            "item_output", ChatFormatting.GOLD
    );

    public ChangeSideConfigButton(int x, int y, String type, Button.OnPress onPress, int size) {
        super(x, y, size, size, Component.translatable("tooltip.side_config"), onPress, DEFAULT_NARRATION);
        texture = ncLoc("button/" + type);
        this.type = type;
        this.visible = false;
        setTooltip(Tooltip.create(Component.translatable("tooltip.side_config." + type).withStyle(color.get(type))));
    }

    public ChangeSideConfigButton(int x, int y, String type, Button.OnPress onPress) {
        this(x, y, type, onPress, 18);
    }

    @Override
    public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        guiGraphics.blitSprite(texture, this.getX(), this.getY(), this.width, this.height);
    }
}