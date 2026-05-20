package com.nred.nuclearcraft.block_entity.fission.port;

import com.nred.nuclearcraft.block_entity.fission.PebbleFissionChamberEntity;
import com.nred.nuclearcraft.menu.multiblock.port.FissionChamberPortMenu;
import com.nred.nuclearcraft.recipe.NCRecipes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import static com.nred.nuclearcraft.registration.BlockEntityRegistration.FISSION_ENTITY_TYPE;

public class FissionChamberPortEntity extends FissionItemPortEntity<FissionChamberPortEntity, PebbleFissionChamberEntity> {
    public FissionChamberPortEntity(final BlockPos position, final BlockState blockState) {
        super(FISSION_ENTITY_TYPE.get("chamber_port").get(), position, blockState, "fission_chamber_port", FissionChamberPortEntity.class, NCRecipes.pebble_fission);
    }

    // MenuProvider

    @Override
    public @Nullable AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        return new FissionChamberPortMenu(containerId, playerInventory, this);
    }

    @Override
    public boolean canOpenGui(Level world, BlockPos position, BlockState state) {
        return true;
    }
}