package com.nred.nuclearcraft.block.item.energy;

import com.nred.nuclearcraft.block.battery.BatteryBlock;
import com.nred.nuclearcraft.block_entity.internal.energy.EnergyConnection;
import com.nred.nuclearcraft.multiblock.battery.BatteryType;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.Block;

import java.util.function.Supplier;

public class BatteryItemBlock extends EnergyItemBlock {
    public BatteryItemBlock(Block block, Supplier<Integer> capacity, Supplier<Integer> maxTransfer, Component... tooltip) {
        super(block, capacity, maxTransfer, EnergyConnection.BOTH, tooltip);
    }

    public BatteryItemBlock(BatteryBlock block, Component... tooltip) {
        this(block, ((BatteryType) block.getMultiblockVariant().get()).getCapacity(), ((BatteryType) block.getMultiblockVariant().get()).getMaxTransfer(), tooltip);
    }
}