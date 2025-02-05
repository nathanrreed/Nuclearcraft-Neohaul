package com.nred.nuclearcraft.block.collector;

import com.nred.nuclearcraft.helpers.CustomFluidStackHandler;
import com.nred.nuclearcraft.recipe.CollectorRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.registries.DeferredHolder;

import static com.nred.nuclearcraft.block.collector.MACHINE_LEVEL.BASE;
import static com.nred.nuclearcraft.registration.BlockEntityRegistration.WATER_SOURCE_TYPES;
import static com.nred.nuclearcraft.registration.RecipeTypeRegistration.*;

public class WaterSourceEntity extends CollectorEntity {
    public CustomFluidStackHandler fluidStackHandler = new CustomFluidStackHandler(getMax(), false, true);

    public WaterSourceEntity(BlockPos pos, BlockState blockState, MACHINE_LEVEL level) {
        super(WATER_SOURCE_TYPES.get(level).get(), pos, blockState, level);
    }

    public WaterSourceEntity(BlockPos pos, BlockState blockState) {
        super(WATER_SOURCE_TYPES.get(BASE).get(), pos, blockState, BASE);
    }

    @Override
    public double getAmountPerTick(Level level) {
        DeferredHolder<RecipeType<?>, RecipeType<CollectorRecipe>> type = switch (this.machineLevel) {
            case BASE -> WATER_SOURCE_RECIPE_TYPE;
            case COMPACT -> WATER_SOURCE_COMPACT_RECIPE_TYPE;
            case DENSE -> WATER_SOURCE_DENSE_RECIPE_TYPE;
        };
        return level.getRecipeManager().getAllRecipesFor(type.get()).getFirst().value().rate();
    }

    @Override
    public int getMax() {
        return switch (this.machineLevel) {
            case BASE -> 1200;//TODO add to config
            case COMPACT -> 9600;
            case DENSE -> 76800;
        };
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        fluidStackHandler.writeToNBT(registries, tag);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        fluidStackHandler.readFromNBT(registries, tag);
    }

    @Override
    public void tick(Level level, BlockPos pos, BlockState state, BlockEntity blockEntity) {
        if (fluidStackHandler.getFluidAmount() < fluidStackHandler.getCapacity()) {
            fluidStackHandler.internalInsertFluid(new FluidStack(Fluids.WATER, (int) getAmountPerTick(level)), IFluidHandler.FluidAction.EXECUTE);
        }
    }
}