package com.nred.nuclearcraft.screen.processor;

import com.nred.nuclearcraft.block_entity.ITileGui;
import com.nred.nuclearcraft.block_entity.fluid.ITileFluid;
import com.nred.nuclearcraft.block_entity.internal.fluid.TankOutputSetting;
import com.nred.nuclearcraft.block_entity.internal.fluid.TankSorption;
import com.nred.nuclearcraft.block_entity.processor.IProcessor;
import com.nred.nuclearcraft.block_entity.processor.info.ProcessorMenuInfo;
import com.nred.nuclearcraft.gui.NCButton;
import com.nred.nuclearcraft.gui.NCEnumButton;
import com.nred.nuclearcraft.menu.InfoTileMenu;
import com.nred.nuclearcraft.menu.processor.ProcessorMenu;
import com.nred.nuclearcraft.payload.gui.ResetTankSorptionsPacket;
import com.nred.nuclearcraft.payload.gui.ToggleTankOutputSettingPacket;
import com.nred.nuclearcraft.payload.gui.ToggleTankSorptionPacket;
import com.nred.nuclearcraft.payload.processor.ProcessorUpdatePacket;
import com.nred.nuclearcraft.screen.NCGui;
import com.nred.nuclearcraft.util.NCUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import static com.nred.nuclearcraft.NuclearcraftNeohaul.MODID;
import static com.nred.nuclearcraft.helpers.Location.ncLoc;

public abstract class GuiFluidSorptions<TILE extends BlockEntity & ITileGui<TILE, PACKET, INFO> & ITileFluid & IProcessor<TILE, PACKET, INFO>, PACKET extends ProcessorUpdatePacket, INFO extends ProcessorMenuInfo<TILE, PACKET, INFO>> extends NCGui<ProcessorMenu<TILE, PACKET, INFO>> {
    protected final NCGui<?> parent;
    protected final TILE tile;
    protected final Direction[] dirs;
    protected final int slot;
    protected final TankSorption.Type sorptionType;
    protected static ResourceLocation gui_texture;
    protected int[] a, b;

    public GuiFluidSorptions(NCGui<? extends InfoTileMenu<TILE, PACKET, INFO>> parent, TILE tile, int slot, TankSorption.Type sorptionType) {
        super((ProcessorMenu<TILE, PACKET, INFO>) parent.getMenu(), parent.getMenu().inventory, Component.empty());

        this.parent = parent;
        this.tile = tile;
        dirs = Direction.values();
        this.slot = slot;
        this.sorptionType = sorptionType;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (isEscapeKeyDown(keyCode, scanCode)) {
            Minecraft.getInstance().setScreen(parent);
            return true;
        } else {
            return super.keyPressed(keyCode, scanCode, modifiers);
        }
    }

    @Override
    public void renderTooltip(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        drawTooltip(guiGraphics, Component.translatable(MODID + ".tooltip.bottom_config", Component.translatable(MODID + ".tooltip." + tile.getTankSorption(dirs[0], slot).getSerializedName() + "_config")).withStyle(tile.getTankSorption(dirs[0], slot).getTextColor()), mouseX, mouseY, a[0], b[0], 18, 18);
        drawTooltip(guiGraphics, Component.translatable(MODID + ".tooltip.top_config", Component.translatable(MODID + ".tooltip." + tile.getTankSorption(dirs[1], slot).getSerializedName() + "_config")).withStyle(tile.getTankSorption(dirs[1], slot).getTextColor()), mouseX, mouseY, a[1], b[1], 18, 18);
        drawTooltip(guiGraphics, Component.translatable(MODID + ".tooltip.left_config", Component.translatable(MODID + ".tooltip." + tile.getTankSorption(dirs[2], slot).getSerializedName() + "_config")).withStyle(tile.getTankSorption(dirs[2], slot).getTextColor()), mouseX, mouseY, a[2], b[2], 18, 18);
        drawTooltip(guiGraphics, Component.translatable(MODID + ".tooltip.right_config", Component.translatable(MODID + ".tooltip." + tile.getTankSorption(dirs[3], slot).getSerializedName() + "_config")).withStyle(tile.getTankSorption(dirs[3], slot).getTextColor()), mouseX, mouseY, a[3], b[3], 18, 18);
        drawTooltip(guiGraphics, Component.translatable(MODID + ".tooltip.front_config", Component.translatable(MODID + ".tooltip." + tile.getTankSorption(dirs[4], slot).getSerializedName() + "_config")).withStyle(tile.getTankSorption(dirs[4], slot).getTextColor()), mouseX, mouseY, a[4], b[4], 18, 18);
        drawTooltip(guiGraphics, Component.translatable(MODID + ".tooltip.back_config", Component.translatable(MODID + ".tooltip." + tile.getTankSorption(dirs[5], slot).getSerializedName() + "_config")).withStyle(tile.getTankSorption(dirs[5], slot).getTextColor()), mouseX, mouseY, a[5], b[5], 18, 18);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY) {
        super.drawGuiContainerForegroundLayer(guiGraphics, partialTicks, mouseX, mouseY);
        guiGraphics.setColor(1F, 1F, 1F, 0.75F);
        BlockState state = tile.getBlockState(tile.getTilePos());
        for (int i = 0; i < 6; ++i) {
            renderGuiBlock(guiGraphics, state, dirs[i], a[i] + 1, b[i] + 1, 16, 16);
        }
        guiGraphics.setColor(1F, 1F, 1F, 1F);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY) {
        guiGraphics.blitSprite(gui_texture, imageWidth, imageHeight, 0, 0, leftPos, topPos, imageWidth, imageHeight);
    }

    @Override
    public void init() {
        super.init();
        for (int i = 0; i < 6; ++i) {
            addRenderableWidget(new NCEnumButton.TankSorption(i, leftPos + a[i], topPos + b[i], tile.getTankSorption(dirs[i], slot), sorptionType, this::sorptionPressed));
        }
    }

    protected void sorptionPressed(NCButton button, int pressed) {
        if (tile.getTileWorld().isClientSide() && button.id < 6) {
            if (button.id == 4 && NCUtil.isModifierKeyDown()) {
                for (int j = 0; j < 6; ++j) {
                    TankSorption sorption = pressed == 0 ? TankSorption.NON : tile.getFluidConnection(dirs[j]).getDefaultTankSorption(slot);

                    tile.setTankSorption(dirs[j], slot, sorption);
                    ((NCEnumButton.TankSorption) children().get(j)).set(sorption);
                }
                new ResetTankSorptionsPacket(tile, slot, pressed == 1).sendToServer();
                return;
            }
            tile.toggleTankSorption(dirs[button.id], slot, sorptionType, pressed == 1);
            new ToggleTankSorptionPacket(tile, dirs[button.id], slot, tile.getTankSorption(dirs[button.id], slot)).sendToServer();
        }
    }

    public static class Input<TILE extends BlockEntity & ITileGui<TILE, PACKET, INFO> & ITileFluid & IProcessor<TILE, PACKET, INFO>, PACKET extends ProcessorUpdatePacket, INFO extends ProcessorMenuInfo<TILE, PACKET, INFO>> extends GuiFluidSorptions<TILE, PACKET, INFO> {
        public Input(NCGui<? extends InfoTileMenu<TILE, PACKET, INFO>> parent, TILE tile, int slot) {
            super(parent, tile, slot, TankSorption.Type.INPUT);
            gui_texture = ncLoc("side_config/fluid_input");
            a = new int[]{25, 25, 7, 43, 25, 43};
            b = new int[]{43, 7, 25, 25, 25, 43};
            imageWidth = 68;
            imageHeight = 68;
        }
    }

    public static class Output<TILE extends BlockEntity & ITileGui<TILE, PACKET, INFO> & ITileFluid & IProcessor<TILE, PACKET, INFO>, PACKET extends ProcessorUpdatePacket, INFO extends ProcessorMenuInfo<TILE, PACKET, INFO>> extends GuiFluidSorptions<TILE, PACKET, INFO> {
        public TankOutputSetting outputSetting;

        public Output(NCGui<? extends InfoTileMenu<TILE, PACKET, INFO>> parent, TILE tile, int slot) {
            super(parent, tile, slot, TankSorption.Type.OUTPUT);
            gui_texture = ncLoc("side_config/fluid_output");
            a = new int[]{47, 47, 29, 65, 47, 65};
            b = new int[]{43, 7, 25, 25, 25, 43};
            imageWidth = 90;
            imageHeight = 68;
            outputSetting = tile.getTankOutputSetting(slot);
        }

        @Override
        public void renderTooltip(GuiGraphics guiGraphics, int mouseX, int mouseY) {
            super.renderTooltip(guiGraphics, mouseX, mouseY);
            drawTooltip(guiGraphics, Component.translatable(MODID + ".tooltip.tank_setting_config", Component.translatable(MODID + ".tooltip." + outputSetting.getSerializedName() + "_setting_config").withStyle(outputSetting.getTextColor())), mouseX, mouseY, 7, 25, 18, 18);
        }

        @Override
        public void init() {
            super.init();
            addRenderableWidget(new NCEnumButton.TankOutputSetting(6, leftPos + 7, topPos + 25, outputSetting, this::sorptionPressed));
        }

        @Override
        protected void sorptionPressed(NCButton button, int pressed) {
            super.sorptionPressed(button, pressed);
            if (tile.getTileWorld().isClientSide()) {
                if (button.id == 6) {
                    outputSetting = outputSetting.next(pressed == 1);
                }
            }
        }

        @Override
        public void onClose() {
            super.onClose();
            tile.setTankOutputSetting(slot, outputSetting);
            new ToggleTankOutputSettingPacket(tile, slot, outputSetting).sendToServer();
        }
    }
}