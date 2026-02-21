package com.nred.nuclearcraft.block_entity.internal.fluid;


import mekanism.api.Action;
import mekanism.api.chemical.ChemicalStack;
import mekanism.api.chemical.IChemicalHandler;

public class ChemicalTankVoid extends TankVoid implements IChemicalHandler {
    public ChemicalTankVoid() {
    }

    @Override
    public int getChemicalTanks() {
        return 1;
    }

    @Override
    public ChemicalStack getChemicalInTank(int tank) {
        return ChemicalStack.EMPTY;
    }

    @Override
    public void setChemicalInTank(int tank, ChemicalStack stack) {
    }

    @Override
    public long getChemicalTankCapacity(int tank) {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isValid(int tank, ChemicalStack stack) {
        return true;
    }

    @Override
    public ChemicalStack insertChemical(int tank, ChemicalStack stack, Action action) {
        return ChemicalStack.EMPTY;
    }

    @Override
    public ChemicalStack extractChemical(int tank, long amount, Action action) {
        return ChemicalStack.EMPTY;
    }
}