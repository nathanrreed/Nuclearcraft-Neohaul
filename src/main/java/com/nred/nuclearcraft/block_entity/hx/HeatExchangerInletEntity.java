package com.nred.nuclearcraft.block_entity.hx;

import com.google.common.collect.Lists;
import com.nred.nuclearcraft.block_entity.fluid.ITileFluid;
import com.nred.nuclearcraft.block_entity.internal.fluid.*;
import com.nred.nuclearcraft.block_entity.internal.processor.AbstractProcessorElement;
import com.nred.nuclearcraft.handler.BasicRecipeHandler;
import com.nred.nuclearcraft.multiblock.hx.HeatExchanger;
import com.nred.nuclearcraft.multiblock.hx.HeatExchangerLogic;
import com.nred.nuclearcraft.multiblock.hx.HeatExchangerTubeNetwork;
import com.nred.nuclearcraft.recipe.BasicRecipe;
import com.nred.nuclearcraft.recipe.NCRecipes;
import com.nred.nuclearcraft.util.InventoryStackList;
import it.zerono.mods.zerocore.lib.multiblock.cuboid.PartPosition;
import it.zerono.mods.zerocore.lib.multiblock.validation.IMultiblockValidator;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.nred.nuclearcraft.registration.BlockEntityRegistration.HX_ENTITY_TYPE;
import static com.nred.nuclearcraft.registration.BlockRegistration.FACING_ALL;

public class HeatExchangerInletEntity extends AbstractHeatExchangerEntity implements ITileFluid {
    public boolean isMasterInlet = false;

    public final @Nonnull List<Tank> masterTanks = Lists.newArrayList(new Tank(HeatExchanger.BASE_MAX_INPUT, NCRecipes.heat_exchanger.getValidFluids(level, 0)), new Tank(HeatExchanger.BASE_MAX_OUTPUT, null));

    private @Nonnull FluidConnection[] fluidConnections = ITileFluid.fluidConnectionAll(TankSorption.IN);

    private final @Nonnull FluidTileWrapper[] fluidSides;
    private final @Nonnull ChemicalTileWrapper[] chemicalSides;

    public @Nullable HeatExchangerTubeNetwork network;

    public int inputTemperature = 300;
    public int outputTemperature = 300;
    public boolean isHeating = false;

    public double speedMultiplierProposal = 0D;
    public double heatTransferRateProposal = 0D;

    public class InletProcessorElement extends AbstractProcessorElement {
        public double heatTransferRate, shellSpeedMultiplier;

        @Override
        public Level getWorld() {
            return level;
        }

        @Override
        public BasicRecipeHandler<?> getRecipeHandler() {
            HeatExchangerLogic logic = getLogic();
            return logic == null ? NCRecipes.heat_exchanger : logic.getInletRecipeHandler(HeatExchangerInletEntity.this);
        }

        @Override
        public void setRecipeStats(@Nullable BasicRecipe basicRecipe) {
            HeatExchangerLogic logic = getLogic();
            if (logic != null) {
                logic.setInletRecipeStats(HeatExchangerInletEntity.this, basicRecipe);
            }
        }

        @Override
        public @Nonnull NonNullList<ItemStack> getInventoryStacks() {
            return InventoryStackList.EMPTY_LIST;
        }

        @Override
        public @Nonnull List<Tank> getTanks() {
            Optional<HeatExchanger> hx = getMultiblockController();
            return network == null && hx.isPresent() ? hx.get().shellTanks : masterTanks;
        }

        @Override
        public boolean getConsumesInputs() {
            return true;
        }

        @Override
        public boolean getLosesProgress() {
            return false;
        }

        @Override
        public int getItemInputSize() {
            return 0;
        }

        @Override
        public int getFluidInputSize() {
            return 1;
        }

        @Override
        public int getItemOutputSize() {
            return 0;
        }

        @Override
        public int getFluidOutputSize() {
            return 1;
        }

        @Override
        public int getItemInputSlot(int index) {
            return index;
        }

        @Override
        public int getFluidInputTank(int index) {
            return index;
        }

        @Override
        public int getItemOutputSlot(int index) {
            return index;
        }

        @Override
        public int getFluidOutputTank(int index) {
            return index + 1;
        }

        @Override
        public double getSpeedMultiplier() {
            HeatExchangerLogic logic = getLogic();
            return logic == null ? 0D : logic.getInletSpeedMultiplier(HeatExchangerInletEntity.this);
        }

        @Override
        public boolean isHalted() {
            Optional<HeatExchanger> hx = getMultiblockController();
            return hx.isEmpty() || !hx.get().isExchangerOn;
        }

        @Override
        public void produceProducts() {
            int consumedAmount = consumedTanks.get(0).getFluidAmount();

            if (isMasterShellInlet()) {
                getMultiblockController().get().shellInputRate += consumedAmount;
            } else {
                getMultiblockController().get().tubeInputRate += consumedAmount;
            }

            super.produceProducts();
        }

        @Override
        public void onResumeProcessingState() {
            getMultiblockController().ifPresent(hx -> hx.packetFlag |= 1);
        }

        @Override
        public void onChangeProcessingState() {
            getMultiblockController().ifPresent(hx -> hx.packetFlag |= isMasterShellInlet() ? 2 : 1);
        }

        @Override
        public void process(Level level) {
            HeatExchangerLogic logic = getLogic();
            if (logic != null) {
                logic.inletProcess(HeatExchangerInletEntity.this);
            }
        }

        @Override
        public void refreshActivityOnProduction() {
            super.refreshActivityOnProduction();
            if (!canProcessInputs) {
                getMultiblockController().ifPresent(heatExchanger -> heatExchanger.refreshFlag = true);
            }
        }
    }

    public final InletProcessorElement processor = new InletProcessorElement();

    public HeatExchangerInletEntity(final BlockPos position, final BlockState blockState) {
        super(HX_ENTITY_TYPE.get("inlet").get(), position, blockState);
        fluidSides = ITileFluid.getDefaultFluidSides(this);
        chemicalSides = ITileFluid.getDefaultChemicalSides(this);
    }

    @Override
    public boolean isGoodForPosition(PartPosition position, IMultiblockValidator validatorCallback) {
        return position.isFace();
    }

    @Override
    public void onPreMachineAssembled(HeatExchanger controller) {
        super.onPreMachineAssembled(controller);
        if (!level.isClientSide()) {
            Optional<Direction> facing = getPartPosition().getDirection();
            facing.ifPresent(direction -> level.setBlock(worldPosition, level.getBlockState(worldPosition).setValue(FACING_ALL, direction), 2));
        }
    }

    @Override
    public void onPreMachineBroken() {
        isMasterInlet = false;
        network = null;
        super.onPreMachineBroken();
    }

    public boolean isMasterShellInlet() {
        Optional<HeatExchanger> hx = getMultiblockController();
        return hx.isPresent() && this == hx.get().masterShellInlet;
    }

    // Fluids

    @Override
    public @Nonnull List<Tank> getTanks() {
        HeatExchangerLogic logic = getLogic();
        return logic == null ? Collections.emptyList() : logic.getInletTanks(network);
    }

    @Override
    public void clearAllTanks() {
        ITileFluid.super.clearAllTanks();
        for (Tank tank : masterTanks) {
            tank.setFluidStored(null);
        }
        for (Tank tank : processor.consumedTanks) {
            tank.setFluidStored(null);
        }
        processor.refreshAll(level);
    }

    @Override
    public @Nonnull FluidConnection[] getFluidConnections() {
        return fluidConnections;
    }

    @Override
    public void setFluidConnections(@Nonnull FluidConnection[] connections) {
        fluidConnections = connections;
    }

    @Override
    public @Nonnull FluidTileWrapper[] getFluidSides() {
        return fluidSides;
    }

    @Override
    public @Nonnull ChemicalTileWrapper[] getChemicalSides() {
        return chemicalSides;
    }

    @Override
    public boolean getInputTanksSeparated() {
        return false;
    }

    @Override
    public void setInputTanksSeparated(boolean separated) {
    }

    @Override
    public boolean getVoidUnusableFluidInput(int tankNumber) {
        return false;
    }

    @Override
    public void setVoidUnusableFluidInput(int tankNumber, boolean voidUnusableFluidInput) {
    }

    @Override
    public TankOutputSetting getTankOutputSetting(int tankNumber) {
        return TankOutputSetting.DEFAULT;
    }

    @Override
    public void setTankOutputSetting(int tankNumber, TankOutputSetting setting) {
    }

    // NBT

    @Override
    public CompoundTag writeAll(CompoundTag nbt, HolderLookup.Provider registries) {
        super.writeAll(nbt, registries);
        for (int i = 0; i < masterTanks.size(); ++i) {
            masterTanks.get(i).writeToNBT(nbt, registries, "masterTanks" + i);
        }
        writeFluidConnections(nbt, registries);
        processor.writeToNBT(nbt, registries, "processor");
        return nbt;
    }

    @Override
    public void readAll(CompoundTag nbt, HolderLookup.Provider registries) {
        super.readAll(nbt, registries);
        for (int i = 0; i < masterTanks.size(); ++i) {
            masterTanks.get(i).readFromNBT(nbt, registries, "masterTanks" + i);
        }
        readFluidConnections(nbt, registries);
        processor.readFromNBT(nbt, registries, "processor");
    }
}