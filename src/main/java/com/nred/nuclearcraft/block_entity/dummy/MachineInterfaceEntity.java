package com.nred.nuclearcraft.block_entity.dummy;

import com.nred.nuclearcraft.block_entity.energy.ITileEnergy;
import com.nred.nuclearcraft.block_entity.fluid.ITileFluid;
import com.nred.nuclearcraft.block_entity.internal.energy.EnergyConnection;
import com.nred.nuclearcraft.block_entity.internal.fluid.TankSorption;
import com.nred.nuclearcraft.block_entity.internal.inventory.ItemSorption;
import com.nred.nuclearcraft.block_entity.inventory.ITileInventory;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import static com.nred.nuclearcraft.config.Config2.machine_update_rate;
import static com.nred.nuclearcraft.registration.BlockEntityRegistration.MACHINE_INTERFACE_ENTITY_TYPE;

public class MachineInterfaceEntity extends TileDummy<IInterfaceable> {
    public MachineInterfaceEntity(BlockPos pos, BlockState blockState) {
        super(MACHINE_INTERFACE_ENTITY_TYPE.get(), pos, blockState, IInterfaceable.class, "machine_interface", ITileInventory.inventoryConnectionAll(ItemSorption.BOTH), ITileEnergy.energyConnectionAll(EnergyConnection.BOTH), machine_update_rate, null, ITileFluid.fluidConnectionAll(TankSorption.BOTH));
    }

    @Override
    public void update() {
        super.update();
        if (!level.isClientSide) {
            pushEnergy();
        }
    }

    // Find Master

    @Override
    public void findMaster() {
        for (Direction side : Direction.values()) {
            BlockEntity tile = level.getBlockEntity(worldPosition.relative(side));
            if (tile != null) {
                if (isMaster(worldPosition.relative(side))) {
                    masterPosition = worldPosition.relative(side);
                    return;
                }
            }
        }
        masterPosition = null;
    }
}