package com.nred.nuclearcraft.block_entity.hx;

import com.google.common.collect.Lists;
import com.nred.nuclearcraft.block_entity.fluid.ITileFluid;
import com.nred.nuclearcraft.block_entity.internal.fluid.*;
import com.nred.nuclearcraft.block_entity.internal.processor.AbstractProcessorElement;
import com.nred.nuclearcraft.config.NCConfig;
import com.nred.nuclearcraft.handler.BasicRecipeHandler;
import com.nred.nuclearcraft.handler.NCRecipes;
import com.nred.nuclearcraft.multiblock.hx.HeatExchanger;
import com.nred.nuclearcraft.multiblock.hx.HeatExchangerLogic;
import com.nred.nuclearcraft.multiblock.hx.HeatExchangerTubeNetwork;
import com.nred.nuclearcraft.recipe.BasicRecipe;
import com.nred.nuclearcraft.recipe.RecipeInfo;
import com.nred.nuclearcraft.recipe.exchanger.CondenserRecipe;
import com.nred.nuclearcraft.recipe.exchanger.HeatExchangerRecipe;
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

    public @Nonnull NonNullList<ItemStack> inventoryStacks = NonNullList.withSize(0, ItemStack.EMPTY);

    public final @Nonnull List<Tank> masterTanks = Lists.newArrayList(new Tank(HeatExchanger.BASE_MAX_INPUT, NCRecipes.heat_exchanger.validFluids.get(0)), new Tank(HeatExchanger.BASE_MAX_OUTPUT, null));

    private @Nonnull FluidConnection[] fluidConnections = ITileFluid.fluidConnectionAll(TankSorption.IN);

    private final @Nonnull FluidTileWrapper[] fluidSides;
    private final @Nonnull ChemicalTileWrapper[] chemicalSides;

    public @Nullable HeatExchangerTubeNetwork network;

    int inputTemperature = 300;
    int outputTemperature = 300;
    boolean isHeating = false;

    public HeatExchangerInletEntity(final BlockPos position, final BlockState blockState) {
        super(HX_ENTITY_TYPE.get("inlet").get(), position, blockState);
        fluidSides = ITileFluid.getDefaultFluidSides(this);
        chemicalSides = ITileFluid.getDefaultChemicalSides(this);
    }

    public final AbstractProcessorElement processor = new AbstractProcessorElement() {
        double heatTransferRate, shellSpeedMultiplier;

        @Override
        public Level getWorld() {
            return level;
        }

        @Override
        public BasicRecipeHandler<?> getRecipeHandler() {
            HeatExchangerLogic logic = getLogic();
            return logic != null && logic.isCondenser() ? NCRecipes.condenser : NCRecipes.heat_exchanger;
        }

        @Override
        public void setRecipeStats(@Nullable BasicRecipe basicRecipe) {
            if (basicRecipe == null) {
                baseProcessTime = 1D;
                inputTemperature = 300;
                outputTemperature = 300;
                isHeating = false;
            } else {
                if (basicRecipe instanceof CondenserRecipe recipe) {
                    baseProcessTime = recipe.getCondenserCoolingRequired();
                    inputTemperature = recipe.getCondenserInputTemperature();
                    outputTemperature = recipe.getCondenserOutputTemperature();
                    isHeating = false;
                } else if (basicRecipe instanceof HeatExchangerRecipe recipe) {
                    baseProcessTime = recipe.getHeatExchangerHeatDifference();
                    inputTemperature = recipe.getHeatExchangerInputTemperature();
                    outputTemperature = recipe.getHeatExchangerOutputTemperature();
                    isHeating = recipe.getHeatExchangerIsHeating();
                }
            }
        }

        @Override
        public @Nonnull NonNullList<ItemStack> getInventoryStacks() {
            return inventoryStacks;
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
            HeatExchanger hx = getMultiblockController().orElse(null);
            if (hx == null) {
                return 0D;
            }

            if (isMasterShellInlet()) {
                return hx.shellSpeedMultiplier;
            }

            if (hx.getLogic().isCondenser()) {
                if (isHeating || hx.shellRecipe == null) {
                    return 0D;
                }

                int shellTemperature = hx.shellTanks.getFirst().getFluid().getFluidType().getTemperature();
                if (outputTemperature < shellTemperature) {
                    return 0D;
                }

                double absMeanTempDiff = getAbsMeanTempDiff(inputTemperature - shellTemperature, outputTemperature - shellTemperature);
                hx.totalTempDiff += absMeanTempDiff * network.usefulTubeCount;

                hx.activeContactCount += network.usefulTubeCount;

                ++hx.activeNetworkCount;
                hx.activeTubeCount += network.usefulTubeCount;

                double tubeFlowDirectionMultiplier = ((CondenserRecipe) recipeInfo.recipe).getCondenserFlowDirectionMultiplier(network.tubeFlow);

                double heatTransferMultiplier = absMeanTempDiff * tubeFlowDirectionMultiplier * hx.shellTanks.getFirst().getFluidAmountFraction();
                return heatTransferRate = heatTransferMultiplier * network.baseCoolingMultiplier;
            } else {
                RecipeInfo<BasicRecipe> shellRecipeInfo = hx.masterShellInlet.processor.recipeInfo;
                if (shellRecipeInfo == null) {
                    return 0D;
                }

                HeatExchangerRecipe shellRecipe = (HeatExchangerRecipe) shellRecipeInfo.recipe;
                boolean shellIsHeating = shellRecipe.getHeatExchangerIsHeating();
                if (isHeating == shellIsHeating) {
                    return 0D;
                }

                int shellInputTemperature = shellRecipe.getHeatExchangerInputTemperature();
                int shellOutputTemperature = shellRecipe.getHeatExchangerOutputTemperature();

                boolean contraflow = network.isContraflow();
                int inputEndShellTemperature = contraflow ? shellOutputTemperature : shellInputTemperature;
                int outputEndShellTemperature = contraflow ? shellInputTemperature : shellOutputTemperature;

                int inletTemperatureDiff = inputTemperature - inputEndShellTemperature;
                int outletTemperatureDiff = outputTemperature - outputEndShellTemperature;
                int sumTempDiff = inletTemperatureDiff + outletTemperatureDiff;
                if (sumTempDiff == 0) {
                    return 0D;
                }

                boolean heating = sumTempDiff < 0;
                if (isHeating != heating) {
                    return 0D;
                }

                if ((inletTemperatureDiff > 0 && outletTemperatureDiff < 0) || (inletTemperatureDiff < 0 && outletTemperatureDiff > 0)) {
                    return 0D;
                }

                double absMeanTempDiff = getAbsMeanTempDiff(inletTemperatureDiff, outletTemperatureDiff);
                hx.totalTempDiff += absMeanTempDiff * network.usefulTubeCount;

                hx.activeContactCount += network.usefulTubeCount;

                ++hx.activeNetworkCount;
                hx.activeTubeCount += network.usefulTubeCount;

                double tubeFlowDirectionMultiplier = ((HeatExchangerRecipe) recipeInfo.recipe).getHeatExchangerFlowDirectionMultiplier(network.tubeFlow);
                double shellFlowDirectionMultiplier = shellRecipe.getHeatExchangerFlowDirectionMultiplier(network.shellFlow);
                double heatTransferMultiplier = absMeanTempDiff * tubeFlowDirectionMultiplier * shellFlowDirectionMultiplier;

                heatTransferRate = heatTransferMultiplier * network.baseCoolingMultiplier;
                shellSpeedMultiplier = heatTransferMultiplier * (heating ? network.baseCoolingMultiplier : network.baseHeatingMultiplier);
                return heatTransferMultiplier * (heating ? network.baseHeatingMultiplier : network.baseCoolingMultiplier);
            }
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
            heatTransferRate = shellSpeedMultiplier = 0D;

            double speedMultiplier = getSpeedMultiplier();
            double maxProcessCount = speedMultiplier / baseProcessTime;

            time += speedMultiplier;

            int processCount = 0;
            while (time >= baseProcessTime) {
                finishProcess(level);
                ++processCount;
            }

            HeatExchanger hx = getMultiblockController().orElse(null);
            if (hx != null) {
                hx.heatTransferRate += heatTransferRate * (processCount == 0 ? 1D : processCount / maxProcessCount);
                hx.shellSpeedMultiplier += shellSpeedMultiplier * processCount / maxProcessCount;
            }
        }

        @Override
        public void refreshActivityOnProduction() {
            super.refreshActivityOnProduction();
            if (!canProcessInputs) {
                getMultiblockController().ifPresent(heatExchanger -> heatExchanger.refreshFlag = true);
            }
        }
    };

    public static double getAbsMeanTempDiff(int inTemperatureDiff, int outTemperatureDiff) {
        if (NCConfig.heat_exchanger_lmtd && inTemperatureDiff != outTemperatureDiff) {
            int absInTemperatureDiff = Math.abs(inTemperatureDiff), absOutTemperatureDiff = Math.abs(outTemperatureDiff);
            return (absInTemperatureDiff - absOutTemperatureDiff) / Math.log((double) absInTemperatureDiff / (double) absOutTemperatureDiff);
        } else {
            return Math.abs(0.5D * (inTemperatureDiff + outTemperatureDiff));
        }
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