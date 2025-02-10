package com.nred.nuclearcraft.gui;

import com.nred.nuclearcraft.payload.ButtonPressPayload;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.PacketDistributor;

import static com.nred.nuclearcraft.enumm.ButtonEnum.REDSTONE_MODE;
import static com.nred.nuclearcraft.helpers.Location.ncLoc;
import static net.minecraft.network.chat.CommonComponents.optionStatus;

public class RedstoneModeToggleButton extends Button {
    private final BlockPos pos;
    private static final ResourceLocation on = ncLoc("button/redstone_control_on");
    private static final ResourceLocation off = ncLoc("button/redstone_control_off");
    private boolean state;
    private boolean justClicked = false;

    public RedstoneModeToggleButton(int x, int y, BlockPos pos, boolean initial_state) {
        super(x, y, 18, 18, Component.translatable("tooltip.redstone_control"), null, DEFAULT_NARRATION);

        setTooltip(Tooltip.create(Component.translatable("tooltip.redstone_control"), optionStatus(Component.translatable("tooltip.redstone_control"), state)));
        this.state = initial_state;
        this.pos = pos;
    }

    @Override
    public void onPress() {
        PacketDistributor.sendToServer(new ButtonPressPayload(REDSTONE_MODE, pos));
        state ^= true;
        justClicked = true;
    }

    @Override
    public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        if (!isHovered()) {
            justClicked = false;
        }
        guiGraphics.blitSprite((justClicked ? state : (state ^ isHovered)) ? on : off, this.getX(), this.getY(), this.width, this.height);
    }
}