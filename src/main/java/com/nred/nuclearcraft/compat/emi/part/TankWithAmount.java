package com.nred.nuclearcraft.compat.emi.part;

import dev.emi.emi.api.render.EmiTooltipComponents;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.widget.TankWidget;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;

import java.util.List;

public class TankWithAmount extends TankWidget {
    public TankWithAmount(EmiIngredient stack, int x, int y, int w, int h) {
        super(stack, x, y, w, h, stack.getAmount());
    }

    public TankWithAmount(EmiIngredient stack, int x, int y) {
        super(stack, x, y, 18, 18, stack.getAmount());
    }

    @Override
    protected void addSlotTooltip(List<ClientTooltipComponent> list) {
        if (stack.getAmount() == 1) {
            list.add(EmiTooltipComponents.getAmount(stack));
        }
        super.addSlotTooltip(list);
    }
}