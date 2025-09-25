package com.nred.nuclearcraft.gui;

import com.nred.nuclearcraft.NuclearcraftNeohaul;
import com.nred.nuclearcraft.payload.ButtonPressPayload;
import com.nred.nuclearcraft.screen.processor.ProcessorScreen;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.Arrays;
import java.util.List;

import static com.nred.nuclearcraft.enumm.ButtonEnum.*;
import static com.nred.nuclearcraft.helpers.Location.ncLoc;

public class CycleButton extends AbstractButton {
    public final List<Element> options;
    private final int index;
    private final ResourceLocation innerTexture;
    @org.jetbrains.annotations.NotNull
    private final String type;
    @org.jetbrains.annotations.NotNull
    public final String prefix;
    private int i = 0;

    public CycleButton(int x, int y, String type, String prefix, int index, ResourceLocation innerTexture) {
        super(x, y, 18, 18, Component.empty());
        this.type = type;
        this.prefix = prefix;
        options = switch (type) {
            case "fluid_input" ->
                    List.of(new Element(NuclearcraftNeohaul.MODID + ".tooltip.side_config.output", ncLoc("button/red"), ChatFormatting.RED), new Element(NuclearcraftNeohaul.MODID + ".tooltip.side_config.auto_output", ncLoc("button/magenta"), ChatFormatting.LIGHT_PURPLE), new Element(NuclearcraftNeohaul.MODID + ".tooltip.side_config.disabled", ncLoc("button/gray"), ChatFormatting.GRAY), new Element(NuclearcraftNeohaul.MODID + ".tooltip.side_config.input", ncLoc("button/cyan"), ChatFormatting.AQUA));
            case "fluid_output" ->
                    List.of(new Element(NuclearcraftNeohaul.MODID + ".tooltip.side_config.output", ncLoc("button/red"), ChatFormatting.RED), new Element(NuclearcraftNeohaul.MODID + ".tooltip.side_config.auto_output", ncLoc("button/magenta"), ChatFormatting.LIGHT_PURPLE), new Element(NuclearcraftNeohaul.MODID + ".tooltip.side_config.disabled", ncLoc("button/gray"), ChatFormatting.GRAY));
            case "item_input" ->
                    List.of(new Element(NuclearcraftNeohaul.MODID + ".tooltip.side_config.output", ncLoc("button/orange"), ChatFormatting.GOLD), new Element(NuclearcraftNeohaul.MODID + ".tooltip.side_config.auto_output", ncLoc("button/yellow"), ChatFormatting.YELLOW), new Element(NuclearcraftNeohaul.MODID + ".tooltip.side_config.disabled", ncLoc("button/gray"), ChatFormatting.GRAY), new Element(NuclearcraftNeohaul.MODID + ".tooltip.side_config.input", ncLoc("button/light_blue"), ChatFormatting.BLUE));
            case "item_output" ->
                    List.of(new Element(NuclearcraftNeohaul.MODID + ".tooltip.side_config.output", ncLoc("button/orange"), ChatFormatting.GOLD), new Element(NuclearcraftNeohaul.MODID + ".tooltip.side_config.auto_output", ncLoc("button/yellow"), ChatFormatting.YELLOW), new Element(NuclearcraftNeohaul.MODID + ".tooltip.side_config.disabled", ncLoc("button/gray"), ChatFormatting.GRAY));
            case String s -> {
                if (s.equals("fluid_slot_setting") || s.equals("item_slot_setting"))
                    yield List.of(new Element(NuclearcraftNeohaul.MODID + ".tooltip.side_config.slot_setting.default", ncLoc("button/default_off"), ChatFormatting.WHITE), new Element(NuclearcraftNeohaul.MODID + ".tooltip.side_config.slot_setting.void_excess", ncLoc("button/void_excess_off"), ChatFormatting.LIGHT_PURPLE), new Element(NuclearcraftNeohaul.MODID + ".tooltip.side_config.slot_setting.void_all", ncLoc("button/void_all_off"), ChatFormatting.DARK_PURPLE));
                throw new IllegalStateException("Unexpected value: " + type);
            }
        };
        this.index = index;
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

        if (Minecraft.getInstance().screen instanceof ProcessorScreen<?> screen) {
            if (prefix.equals("tooltip.side_config.slot_setting.slot")) {
                PacketDistributor.sendToServer(new ButtonPressPayload(ITEM_OUTPUT_SLOT_SETTING, index, screen.getMenu().info.pos(), button == 0));
                screen.getMenu().info.itemOutputSettings().set(index, i);
            } else if (prefix.equals("tooltip.side_config.slot_setting.tank")) {
                PacketDistributor.sendToServer(new ButtonPressPayload(FLUID_OUTPUT_SLOT_SETTING, index, screen.getMenu().info.pos(), button == 0));
                screen.getMenu().info.fluidOutputSettings().set(index, i);
            } else {
                Direction dir = Direction.byName(Arrays.stream(prefix.split("\\.")).toList().getLast());
                if (type.startsWith("item")) {
                    PacketDistributor.sendToServer(new ButtonPressPayload(ITEM_SIDE_CONFIG_SETTING, index, screen.getMenu().info.pos(), dir.ordinal(), button == 0));
                    if (type.endsWith("input")) {
                        screen.getMenu().info.itemSideConfig().get(dir).set(index, screen.getMenu().info.itemSideConfig().get(dir).get(index).nextInput(button == 0));
                    } else {
                        screen.getMenu().info.itemSideConfig().get(dir).set(index, screen.getMenu().info.itemSideConfig().get(dir).get(index).nextOutput(button == 0));
                    }
                } else {
                    PacketDistributor.sendToServer(new ButtonPressPayload(FLUID_SIDE_CONFIG_SETTING, index, screen.getMenu().info.pos(), dir.ordinal(), button == 0));
                    if (type.endsWith("input")) {
                        screen.getMenu().info.fluidSideConfig().get(dir).set(index, screen.getMenu().info.fluidSideConfig().get(dir).get(index).nextInput(button == 0));
                    } else {
                        screen.getMenu().info.fluidSideConfig().get(dir).set(index, screen.getMenu().info.fluidSideConfig().get(dir).get(index).nextOutput(button == 0));
                    }
                }
            }
        }
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

    public void setIndex(int i) {
        this.i = i;
        this.setTooltip();
    }

    public int getIndex() {
        return i;
    }

    public void setTooltip() {
        setTooltip(Tooltip.create(Component.translatable(prefix, Component.translatable(options.get(getIndex()).postfix()).withStyle(options.get(getIndex()).color()))));
    }
}