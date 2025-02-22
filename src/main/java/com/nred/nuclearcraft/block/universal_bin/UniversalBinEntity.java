package com.nred.nuclearcraft.block.universal_bin;

import com.nred.nuclearcraft.helpers.CustomEnergyHandler;
import com.nred.nuclearcraft.helpers.CustomFluidStackHandler;
import com.nred.nuclearcraft.helpers.CustomItemStackHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;

import static com.nred.nuclearcraft.registration.BlockEntityRegistration.UNIVERSAL_BIN_ENTITY_TYPE;

public class UniversalBinEntity extends BlockEntity {
    public UniversalBinEntity(BlockPos pos, BlockState blockState) {
        super(UNIVERSAL_BIN_ENTITY_TYPE.get(), pos, blockState);
    }

    public CustomFluidStackHandler fluidStackHandler = new CustomFluidStackHandler(Integer.MAX_VALUE, 1, true, false) {
        @Override
        public boolean isFluidValid(int tank, FluidStack stack) {
            return true;
        }

        @Override
        public boolean canOutput(int tank) {
            return false;
        }

        @Override
        public int fill(FluidStack resource, FluidAction action) {
            return resource.getAmount();
        }
    };
    public CustomItemStackHandler itemStackHandler = new CustomItemStackHandler(1, true, false) {
        @Override
        public @NotNull ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
            return ItemStack.EMPTY;
        }
    };
    public CustomEnergyHandler energyHandler = new CustomEnergyHandler(Integer.MAX_VALUE, true, false) {
        @Override
        public int receiveEnergy(int toReceive, boolean simulate) {
            return toReceive;
        }
    };
}