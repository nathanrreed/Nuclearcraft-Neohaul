package com.nred.nuclearcraft.block.collector;

import com.nred.nuclearcraft.helpers.CustomItemStackHandler;
import com.nred.nuclearcraft.recipe.CollectorRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.registries.DeferredHolder;

import static com.nred.nuclearcraft.block.collector.MACHINE_LEVEL.BASE;
import static com.nred.nuclearcraft.registration.BlockEntityRegistration.COBBLE_GENERATOR_TYPES;
import static com.nred.nuclearcraft.registration.RecipeTypeRegistration.*;

public class CobbleGeneratorEntity extends CollectorEntity {
    public CustomItemStackHandler itemStackHandler = new CustomItemStackHandler(1, false, true) {
        @Override
        protected int getStackLimit(int slot, ItemStack stack) {
            return getMax();
        }
    };

    private double partial = 0;

    public CobbleGeneratorEntity(BlockPos pos, BlockState blockState, MACHINE_LEVEL level) {
        super(COBBLE_GENERATOR_TYPES.get(level).get(), pos, blockState, level);
    }

    public CobbleGeneratorEntity(BlockPos pos, BlockState blockState) {
        super(COBBLE_GENERATOR_TYPES.get(BASE).get(), pos, blockState, BASE);
    }

    @Override
    public double getAmountPerTick(Level level) {
        DeferredHolder<RecipeType<?>, RecipeType<CollectorRecipe>> type = switch (this.machineLevel) {
            case BASE -> COBBLE_GENERATOR_RECIPE_TYPE;
            case COMPACT -> COBBLE_GENERATOR_COMPACT_RECIPE_TYPE;
            case DENSE -> COBBLE_GENERATOR_DENSE_RECIPE_TYPE;
        };
        return level.getRecipeManager().getAllRecipesFor(type.get()).getFirst().value().rate();
    }

    @Override
    public int getMax() {
        return switch (this.machineLevel) {
            case BASE -> 16;//TODO add to config
            case COMPACT -> 120;
            case DENSE -> 960;
        };
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put("inventory", itemStackHandler.serializeNBT(registries));
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        itemStackHandler.deserializeNBT(registries, tag.getCompound("inventory"));
    }

    @Override
    public void tick(Level level, BlockPos pos, BlockState state, BlockEntity blockEntity) {
        if (itemStackHandler.getStackInSlot(0).getCount() < getMax()) {
            partial += getAmountPerTick(level);
            if (partial >= 1) {
                itemStackHandler.internalInsertItem(0, new ItemStack(Items.COBBLESTONE, (int) Math.floor(partial)), false);
                partial -= ((int) Math.floor(partial));
            }
        }
    }
}