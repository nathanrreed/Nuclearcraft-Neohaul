package com.nred.nuclearcraft.block.collector.nitrogen_collector;

import com.nred.nuclearcraft.block.collector.CollectorEntity;
import com.nred.nuclearcraft.block.collector.MACHINE_LEVEL;
import com.nred.nuclearcraft.config.CollectorConfig;
import com.nred.nuclearcraft.helpers.CustomFluidStackHandler;
import com.nred.nuclearcraft.recipe.collector.CollectorRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.BlockCapabilityCache;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

import static com.nred.nuclearcraft.block.collector.MACHINE_LEVEL.BASE;
import static com.nred.nuclearcraft.helpers.SimpleHelper.shuffledDirections;
import static com.nred.nuclearcraft.registration.BlockEntityRegistration.NITROGEN_COLLECTOR_TYPES;
import static com.nred.nuclearcraft.registration.FluidRegistration.GAS_MAP;
import static com.nred.nuclearcraft.registration.RecipeTypeRegistration.*;

public class NitrogenCollectorEntity extends CollectorEntity {
    public CustomFluidStackHandler fluidStackHandler = new CustomFluidStackHandler(getMax(), 1, false, true) {
        @Override
        public boolean isFluidValid(int tank, FluidStack stack) {
            return true;
        }
    };

    public NitrogenCollectorEntity(BlockPos pos, BlockState blockState, MACHINE_LEVEL level) {
        super(NITROGEN_COLLECTOR_TYPES.get(level).get(), pos, blockState, level);
    }

    public NitrogenCollectorEntity(BlockPos pos, BlockState blockState) {
        this(pos, blockState, BASE);
    }

    private final Map<Direction, BlockCapabilityCache<IFluidHandler, Direction>> capCache = new HashMap<>();

    @Override
    public double getAmountPerTick(Level level) {
        DeferredHolder<RecipeType<?>, RecipeType<CollectorRecipe>> type = switch (this.machineLevel) {
            case BASE -> NITROGEN_COLLECTOR_RECIPE_TYPE;
            case COMPACT -> NITROGEN_COLLECTOR_COMPACT_RECIPE_TYPE;
            case DENSE -> NITROGEN_COLLECTOR_DENSE_RECIPE_TYPE;
        };
        return level.getRecipeManager().getAllRecipesFor(type.get()).getFirst().value().rate();
    }

    @Override
    public int getMax() {
        return CollectorConfig.nitrogenCapacity.get(this.machineLevel);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put("fluids", fluidStackHandler.serializeNBT(registries));
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        fluidStackHandler.deserializeNBT(registries, tag.getCompound("fluids"));
    }

    @Override
    public void tick(Level level, BlockPos pos, BlockState state, BlockEntity blockEntity) {
        if (fluidStackHandler.getFluidAmount(0) < fluidStackHandler.getTankCapacity(0)) {
            fluidStackHandler.internalInsertFluid(new FluidStack(GAS_MAP.get("nitrogen").still, (int) getAmountPerTick(level)), IFluidHandler.FluidAction.EXECUTE);
        }

        if (level != null && !level.isClientSide && fluidStackHandler.getFluidAmount(0) > 0) {
            for (Direction dir : shuffledDirections()) {
                if (this.capCache.get(dir) == null) {
                    this.capCache.put(dir, BlockCapabilityCache.create(Capabilities.FluidHandler.BLOCK, ((ServerLevel) level), pos.relative(dir), dir.getOpposite(), () -> !this.isRemoved(), this::onCapInvalidate));
                } else if (capCache.get(dir).getCapability() != null) {
                    @Nullable IFluidHandler cap = capCache.get(dir).getCapability();
                    FluidStack internal = fluidStackHandler.internalExtractFluid(fluidStackHandler.getTankCapacity(0), IFluidHandler.FluidAction.EXECUTE);
                    fluidStackHandler.internalInsertFluid(internal.copyWithAmount(internal.getAmount() - cap.fill(internal.copy(), IFluidHandler.FluidAction.EXECUTE)), IFluidHandler.FluidAction.EXECUTE);
                }
            }
        }
    }

    private void onCapInvalidate() {

    }
}