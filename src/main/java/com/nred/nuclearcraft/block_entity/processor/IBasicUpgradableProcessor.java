package com.nred.nuclearcraft.block_entity.processor;

import com.nred.nuclearcraft.block_entity.ITileInstallable;
import com.nred.nuclearcraft.block_entity.processor.info.ProcessorMenuInfoImpl;
import com.nred.nuclearcraft.payload.processor.ProcessorUpdatePacket;
import com.nred.nuclearcraft.util.PrimitiveFunction;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandler;

import static com.nred.nuclearcraft.registration.ItemRegistration.UPGRADE_MAP;

public interface IBasicUpgradableProcessor<TILE extends BlockEntity & IBasicUpgradableProcessor<TILE, PACKET>, PACKET extends ProcessorUpdatePacket> extends IProcessor<TILE, PACKET, ProcessorMenuInfoImpl.BasicUpgradableProcessorMenuInfo<TILE, PACKET>>, ITileInstallable {
    default boolean tryInstall(Player player, InteractionHand hand, Direction facing) {
        ItemStack held = player.getItemInHand(hand);

        PrimitiveFunction.ToBooleanBiFunction<Integer, ItemStack> tryInstallUpgrade = (x, y) -> {
            if (ItemStack.isSameItem(held, y)) {
                IItemHandler inv = player.level().getCapability(Capabilities.ItemHandler.BLOCK, getTilePos(), facing);
                if (inv != null && inv.isItemValid(x, held)) {
                    if (player.isCrouching()) {
                        player.setItemInHand(InteractionHand.MAIN_HAND, inv.insertItem(x, held, false));
                        return true;
                    } else {
                        if (inv.insertItem(x, y, false).isEmpty()) {
                            player.getItemInHand(hand).shrink(1);
                            return true;
                        }
                    }
                }
            }
            return false;
        };

        ProcessorMenuInfoImpl.BasicUpgradableProcessorMenuInfo<TILE, PACKET> info = getContainerInfo();
        return tryInstallUpgrade.applyAsBoolean(info.speedUpgradeSlot, UPGRADE_MAP.get("speed").toStack()) || tryInstallUpgrade.applyAsBoolean(info.energyUpgradeSlot, UPGRADE_MAP.get("energy").toStack());
    }
}