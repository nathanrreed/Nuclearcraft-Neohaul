package com.nred.nuclearcraft.block_entity.fission.port;

import com.nred.nuclearcraft.block_entity.fission.FissionCoolerEntity;
import com.nred.nuclearcraft.handler.NCRecipes;
import com.nred.nuclearcraft.menu.multiblock.port.FissionCoolerPortMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import static com.nred.nuclearcraft.registration.BlockEntityRegistration.FISSION_ENTITY_TYPE;
import static com.nred.nuclearcraft.util.FluidStackHelper.INGOT_BLOCK_VOLUME;

public class FissionCoolerPortEntity extends FissionFluidPortEntity<FissionCoolerPortEntity, FissionCoolerEntity> {
    public FissionCoolerPortEntity(BlockPos pos, BlockState blockState) {
        super(FISSION_ENTITY_TYPE.get("cooler_port").get(), pos, blockState, "fission_cooler_port", FissionCoolerPortEntity.class, INGOT_BLOCK_VOLUME, null, NCRecipes.fission_emergency_cooling);
    }

    // MenuProvider

    @Override
    public @Nullable AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        return new FissionCoolerPortMenu(containerId, playerInventory, this);
    }

    @Override
    public boolean canOpenGui(Level world, BlockPos position, BlockState state) {
        return true;
    }
}