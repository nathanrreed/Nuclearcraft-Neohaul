package com.nred.nuclearcraft.menu.processor;

import com.nred.nuclearcraft.block_entity.processor.IProcessor;
import com.nred.nuclearcraft.block_entity.processor.info.UpgradableProcessorMenuInfo;
import com.nred.nuclearcraft.menu.slot.ProcessorSpecificInputSlot;
import com.nred.nuclearcraft.payload.processor.ProcessorUpdatePacket;
import com.nred.nuclearcraft.util.Lazy;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;

import static com.nred.nuclearcraft.registration.ItemRegistration.UPGRADE_MAP;

public abstract class UpgradableProcessorMenu<TILE extends BlockEntity & IProcessor<TILE, PACKET, INFO>, PACKET extends ProcessorUpdatePacket, INFO extends UpgradableProcessorMenuInfo<TILE, PACKET, INFO>> extends ProcessorMenu<TILE, PACKET, INFO> {
    public UpgradableProcessorMenu(MenuType<?> menuType, int containerId, Inventory inventory, TILE tile) {
        super(menuType, containerId, inventory, tile);
    }

    @Override
    protected void addMachineSlots(Player player) {
        super.addMachineSlots(player);

        int[] speedUpgradeStackXY = info.speedUpgradeStackXY, energyUpgradeStackXY = info.energyUpgradeStackXY;
        addSlot(new ProcessorSpecificInputSlot(tile, info.speedUpgradeSlot, speedUpgradeStackXY[0], speedUpgradeStackXY[1], speed_upgrade.get()));
        addSlot(new ProcessorSpecificInputSlot(tile, info.energyUpgradeSlot, energyUpgradeStackXY[0], energyUpgradeStackXY[1], energy_upgrade.get()));
    }

    @Override
    public ItemStack transferPlayerStack(Player player, int index, int invStart, int invEnd, ItemStack stack) {
        if (stack.getItem() == UPGRADE_MAP.get("speed").asItem() || stack.getItem() == UPGRADE_MAP.get("energy").asItem()) {
            if (tile.canPlaceItem(info.speedUpgradeSlot, stack)) {
                if (!moveItemStackTo(stack, info.speedUpgradeSlot, info.speedUpgradeSlot + 1, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (tile.canPlaceItem(info.energyUpgradeSlot, stack)) {
                if (!moveItemStackTo(stack, info.energyUpgradeSlot, info.energyUpgradeSlot + 1, false)) {
                    return ItemStack.EMPTY;
                }
            }
        }
        return super.transferPlayerStack(player, index, invStart, invEnd, stack);
    }

    public static Lazy<ItemStack> speed_upgrade = new Lazy<>(() -> UPGRADE_MAP.get("speed").toStack());
    public static Lazy<ItemStack> energy_upgrade = new Lazy<>(() -> UPGRADE_MAP.get("energy").toStack());
}