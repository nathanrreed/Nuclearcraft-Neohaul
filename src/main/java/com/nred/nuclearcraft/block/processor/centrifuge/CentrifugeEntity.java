package com.nred.nuclearcraft.block.processor.centrifuge;

import com.ibm.icu.impl.Pair;
import com.nred.nuclearcraft.block.processor.ProcessorEntity;
import com.nred.nuclearcraft.helpers.HandlerInfo;
import com.nred.nuclearcraft.menu.CentrifugeMenu;
import com.nred.nuclearcraft.menu.ProcessorInfo;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

import static com.nred.nuclearcraft.helpers.RecipeHelpers.probabilityUnpacker;

public class CentrifugeEntity extends ProcessorEntity {
    public CentrifugeEntity(BlockPos pos, BlockState blockState) {
        super(pos, blockState, "centrifuge", new HandlerInfo(0, 7, 2, 6));
    }

    @Override
    public void recipeOutputs() {
        if (recipe.value().fluidResults.size() < 6) {
            super.recipeOutputs();
            return;
        }

        for (int i = 0; i < recipe.value().fluidResults.size(); i++) {
            FluidStack temp = recipe.value().fluidResults.get(i).getFluids()[0].copy();
            if (i == 2 || i == 5) {
                Pair<Short, Short> probability = probabilityUnpacker(temp.getAmount());

                if (RandomSource.create().nextInt(0, 100) <= probability.first) {
                    fluidHandler.internalInsertFluid(temp.copyWithAmount(probability.second), IFluidHandler.FluidAction.EXECUTE);
                }
            } else {
                fluidHandler.internalInsertFluid(temp, IFluidHandler.FluidAction.EXECUTE);
            }
        }
    }

    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        return new CentrifugeMenu(containerId, playerInventory, ContainerLevelAccess.create(level, worldPosition), new ProcessorInfo(worldPosition, redstoneMode, "centrifuge"), this.progressSlot);
    }
}