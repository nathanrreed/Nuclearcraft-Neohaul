package com.nred.nuclearcraft.block_entity.fission.port;

import com.nred.nuclearcraft.block_entity.fission.FissionIrradiatorEntity;
import com.nred.nuclearcraft.recipe.NCRecipes;
import com.nred.nuclearcraft.menu.multiblock.port.FissionIrradiatorPortMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import static com.nred.nuclearcraft.registration.BlockEntityRegistration.FISSION_ENTITY_TYPE;

public class FissionIrradiatorPortEntity extends FissionItemPortEntity<FissionIrradiatorPortEntity, FissionIrradiatorEntity> {
    public FissionIrradiatorPortEntity(BlockPos pos, BlockState blockState) {
        super(FISSION_ENTITY_TYPE.get("irradiator_port").get(), pos, blockState, "fission_irradiator_port", FissionIrradiatorPortEntity.class, NCRecipes.fission_irradiator);
    }

    // MenuProvider

    @Override
    public @Nullable AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        return new FissionIrradiatorPortMenu(containerId, playerInventory, this);
    }

    @Override
    public boolean canOpenGui(Level world, BlockPos position, BlockState state) {
        return true;
    }
}