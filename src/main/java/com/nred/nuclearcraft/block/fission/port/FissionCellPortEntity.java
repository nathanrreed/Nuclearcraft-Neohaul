package com.nred.nuclearcraft.block.fission.port;

import com.nred.nuclearcraft.block.fission.SolidFissionCellEntity;
import com.nred.nuclearcraft.handler.NCRecipes;
import com.nred.nuclearcraft.menu.multiblock.port.FissionCellPortMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import static com.nred.nuclearcraft.registration.BlockEntityRegistration.FISSION_ENTITY_TYPE;

public class FissionCellPortEntity extends FissionItemPortEntity<FissionCellPortEntity, SolidFissionCellEntity> {
    public FissionCellPortEntity(final BlockPos position, final BlockState blockState) {
        super(FISSION_ENTITY_TYPE.get("cell_port").get(), position, blockState, "fission_cell_port", FissionCellPortEntity.class, NCRecipes.solid_fission);
    }

    // MenuProvider

    @Override
    public @Nullable AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        return new FissionCellPortMenu(containerId, playerInventory, this);
    }

    @Override
    public boolean canOpenGui(Level world, BlockPos position, BlockState state) {
        return true;
    }
}