package com.nred.nuclearcraft.block_entity.fission.port;

import com.nred.nuclearcraft.block_entity.fission.SaltFissionVesselEntity;
import com.nred.nuclearcraft.recipe.NCRecipes;
import com.nred.nuclearcraft.menu.multiblock.port.FissionVesselPortMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuConstructor;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

import static com.nred.nuclearcraft.registration.BlockEntityRegistration.FISSION_ENTITY_TYPE;
import static com.nred.nuclearcraft.util.FluidStackHelper.INGOT_BLOCK_VOLUME;

public class FissionVesselPortEntity extends FissionFluidPortEntity<FissionVesselPortEntity, SaltFissionVesselEntity> implements MenuConstructor {
    public FissionVesselPortEntity(BlockPos pos, BlockState blockState) {
        super(FISSION_ENTITY_TYPE.get("vessel_port").get(), pos, blockState, "fission_vessel_port", FissionVesselPortEntity.class, INGOT_BLOCK_VOLUME, (Set<ResourceLocation>) NCRecipes.salt_fission.validFluids.get(0), NCRecipes.salt_fission);
    }

    // MenuProvider

    @Override
    public @Nullable AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        return new FissionVesselPortMenu(containerId, playerInventory, this);
    }

    @Override
    public boolean canOpenGui(Level world, BlockPos position, BlockState state) {
        return true;
    }
}