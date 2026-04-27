package com.nred.nuclearcraft.block_entity.internal.fluid;

import com.nred.nuclearcraft.block_entity.fluid.ITileFluid;
import com.nred.nuclearcraft.util.ChemicalHelper;
import mekanism.api.Action;
import mekanism.api.chemical.ChemicalStack;
import mekanism.api.chemical.IChemicalHandler;
import net.minecraft.core.Direction;

public class ChemicalTile extends ChemicalTileWrapper implements IChemicalHandler {
    public final ITileFluid tile;
    private final Direction side;

    public ChemicalTile(ITileFluid tile, Direction side) {
        super(tile, side);
        this.tile = tile;
        this.side = side;
    }

    @Override
    public int getChemicalTanks() {
        return tile.getTanks().size();
    }

    @Override
    public ChemicalStack getChemicalInTank(int tank) {
        return ChemicalHelper.getGasFromFluid(tile.getTanks().get(tank).getFluid(), tile.getTileWorld());
    }

    @Override
    public void setChemicalInTank(int tank, ChemicalStack stack) {
        tile.getTanks().get(tank).setFluid(ChemicalHelper.getFluidFromGas(stack, tile.getTileWorld()));
    }

    @Override
    public boolean isValid(int tank, ChemicalStack stack) {
        return tile.isFluidValidForTank(tank, ChemicalHelper.getFluidFromGas(stack, tile.getTileWorld()));
    }

    @Override
    public ChemicalStack insertChemical(int tank, ChemicalStack stack, Action action) {
        int amount = tile.fill(side, ChemicalHelper.getFluidFromGas(stack, tile.getTileWorld()), action.toFluidAction());
        tile.onWrapperReceiveGas(amount, action);
        return stack.copyWithAmount(stack.getAmount() - amount);
    }

    @Override
    public ChemicalStack extractChemical(int tank, long amount, Action action) {
        ChemicalStack stack = ChemicalHelper.getGasFromFluid(tile.drain(side, (int) amount, action.toFluidAction()), tile.getTileWorld());
        tile.onWrapperDrawGas(stack, action);
        return stack;
    }

    @Override
    public long getChemicalTankCapacity(int tank) {
        return tile.getTanks().get(tank).getCapacity();
    }
}