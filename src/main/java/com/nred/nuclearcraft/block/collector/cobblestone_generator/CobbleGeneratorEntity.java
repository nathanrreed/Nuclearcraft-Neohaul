package com.nred.nuclearcraft.block.collector.cobblestone_generator;

import com.nred.nuclearcraft.block.collector.CollectorEntity;
import com.nred.nuclearcraft.block.collector.MACHINE_LEVEL;
import com.nred.nuclearcraft.config.CollectorConfig;
import com.nred.nuclearcraft.helpers.CustomItemStackHandler;
import com.nred.nuclearcraft.recipe.collector.CollectorRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.BlockCapabilityCache;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

import static com.nred.nuclearcraft.block.collector.MACHINE_LEVEL.BASE;
import static com.nred.nuclearcraft.helpers.SimpleHelper.shuffledDirections;
import static com.nred.nuclearcraft.registration.BlockEntityRegistration.COBBLE_GENERATOR_TYPES;
import static com.nred.nuclearcraft.registration.RecipeTypeRegistration.*;

public class CobbleGeneratorEntity extends CollectorEntity {
    public CustomItemStackHandler itemStackHandler = new CustomItemStackHandler(1, false, true) {
        @Override
        protected int getStackLimit(int slot, ItemStack stack) {
            return getMax();
        }
    };

    private final Map<Direction, BlockCapabilityCache<IItemHandler, Direction>> capCache = new HashMap<>();
    private double partial = 0;

    public CobbleGeneratorEntity(BlockPos pos, BlockState blockState, MACHINE_LEVEL machineLevel) {
        super(COBBLE_GENERATOR_TYPES.get(machineLevel).get(), pos, blockState, machineLevel);
    }

    public CobbleGeneratorEntity(BlockPos pos, BlockState blockState) {
        this(pos, blockState, BASE);
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
        return CollectorConfig.cobblestoneCapacity.get(this.machineLevel);
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

        if (level != null && !level.isClientSide && itemStackHandler.getStackInSlot(0).getCount() > 0) {
            for (Direction dir : shuffledDirections()) {
                if (this.capCache.get(dir) == null) {
                    this.capCache.put(dir, BlockCapabilityCache.create(Capabilities.ItemHandler.BLOCK, ((ServerLevel) level), pos.relative(dir), dir.getOpposite(), () -> !this.isRemoved(), () -> onCapInvalidate()));
                } else if (capCache.get(dir).getCapability() != null) {
                    @Nullable IItemHandler cap = capCache.get(dir).getCapability();
                    for (int slot = 0; slot < cap.getSlots(); slot++) {
                        itemStackHandler.setStackInSlot(0, cap.insertItem(slot, itemStackHandler.getStackInSlot(0), false));
                    }
                }
            }
        }
    }

    private void onCapInvalidate() {

    }
}