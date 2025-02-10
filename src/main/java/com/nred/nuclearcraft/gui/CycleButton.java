package com.nred.nuclearcraft.gui;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;

import java.util.List;

import static com.nred.nuclearcraft.helpers.Location.ncLoc;

public class CycleButton extends AbstractButton {
    private final List<Element> options;
    private final ResourceLocation innerTexture;
    private final String prefix;
    private int i = 0;

    public CycleButton(int x, int y, String type, String prefix, ResourceLocation innerTexture) {
        super(x, y, 18, 18, Component.empty());
        this.prefix = prefix;
        options = switch (type) {
            case "fluid_input" ->
                    List.of(new Element("tooltip.side_config.input", ncLoc("button/cyan"), ChatFormatting.AQUA), new Element("tooltip.side_config.output", ncLoc("button/red"), ChatFormatting.RED), new Element("tooltip.side_config.auto_output", ncLoc("button/magenta"), ChatFormatting.LIGHT_PURPLE), new Element("tooltip.side_config.disabled", ncLoc("button/gray"), ChatFormatting.GRAY));
            case "fluid_output" ->
                    List.of(new Element("tooltip.side_config.output", ncLoc("button/red"), ChatFormatting.RED), new Element("tooltip.side_config.auto_output", ncLoc("button/magenta"), ChatFormatting.LIGHT_PURPLE), new Element("tooltip.side_config.disabled", ncLoc("button/gray"), ChatFormatting.GRAY));
            case "item_input" ->
                    List.of(new Element("tooltip.side_config.input", ncLoc("button/light_blue"), ChatFormatting.BLUE), new Element("tooltip.side_config.output", ncLoc("button/orange"), ChatFormatting.GOLD), new Element("tooltip.side_config.auto_output", ncLoc("button/yellow"), ChatFormatting.YELLOW), new Element("tooltip.side_config.disabled", ncLoc("button/gray"), ChatFormatting.GRAY));
            case "item_output" ->
                    List.of(new Element("tooltip.side_config.output", ncLoc("button/orange"), ChatFormatting.GOLD), new Element("tooltip.side_config.auto_output", ncLoc("button/yellow"), ChatFormatting.YELLOW), new Element("tooltip.side_config.disabled", ncLoc("button/gray"), ChatFormatting.GRAY));
            case "slot_setting" ->
                    List.of(new Element("tooltip.side_config.slot_setting.default", ncLoc("button/default_off"), ChatFormatting.WHITE), new Element("tooltip.side_config.slot_setting.void_excess", ncLoc("button/void_excess_off"), ChatFormatting.LIGHT_PURPLE), new Element("tooltip.side_config.slot_setting.void_all", ncLoc("button/void_all_off"), ChatFormatting.DARK_PURPLE));

            default -> throw new IllegalStateException("Unexpected value: " + type);
        };
        this.innerTexture = innerTexture;

        setTooltip(Tooltip.create(Component.translatable(prefix, Component.translatable(options.getFirst().postfix()).withStyle(options.getFirst().color()))));
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
        //TODO check
    }

    @Override
    public void onPress() {
    }

    @Override
    public void onClick(double mouseX, double mouseY, int button) {
        i = Math.floorMod((button == 0 ? i + 1 : i - 1), options.size());
        setTooltip(Tooltip.create(Component.translatable(prefix, Component.translatable(options.get(i).postfix()).withStyle(options.get(i).color()))));
        //TODO send to server
    }

    @Override
    protected boolean isValidClickButton(int button) {
        return button == 0 || button == 1;
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        guiGraphics.blitSprite(options.get(i).outerTexture(), getX(), getY(), width, height);
        if (innerTexture != null) {
            guiGraphics.blit(getX() + 1, getY() + 1, 1, width - 2, height - 2, Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(innerTexture), 1f, 1f, 1f, 0.8f);
        }
    }
}

record Element(String postfix, ResourceLocation outerTexture, ChatFormatting color) {
}