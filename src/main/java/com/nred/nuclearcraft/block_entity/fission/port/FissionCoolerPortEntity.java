package com.nred.nuclearcraft.block_entity.fission.port;

import com.nred.nuclearcraft.block_entity.fission.PebbleFissionCoolerEntity;
import com.nred.nuclearcraft.menu.multiblock.port.FissionCoolerPortMenu;
import com.nred.nuclearcraft.multiblock.fisson.pebble.FissionCoolerPortType;
import com.nred.nuclearcraft.recipe.NCRecipes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import static com.nred.nuclearcraft.registration.BlockEntityRegistration.FISSION_ENTITY_TYPE;
import static com.nred.nuclearcraft.util.FluidStackHelper.INGOT_BLOCK_VOLUME;

public class FissionCoolerPortEntity extends FissionFluidPortEntity<FissionCoolerPortEntity, PebbleFissionCoolerEntity> {
    private final FissionCoolerPortType coolerType;

    public FissionCoolerPortEntity(BlockPos pos, BlockState blockState, FissionCoolerPortType coolerType) {
        super(FISSION_ENTITY_TYPE.get("cooler_port").get(), pos, blockState, "fission_cooler_port", FissionCoolerPortEntity.class, INGOT_BLOCK_VOLUME, e -> coolerType.validFluids(), NCRecipes.gas_cooler);
        this.coolerType = coolerType;
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

    @Override
    public Object getFilterKey() {
        return coolerType.getName();
    }
}