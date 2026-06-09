package com.nred.nuclearcraft.compat.crafttweaker.ingredient;

import com.blamejared.crafttweaker.api.annotation.ZenRegister;
import com.blamejared.crafttweaker.api.fluid.IFluidStack;
import com.blamejared.crafttweaker_annotations.annotations.Document;
import org.openzen.zencode.java.ZenCodeType;

@ZenRegister
@ZenCodeType.Name("mods.nuclearcraft.ChanceFluidIngredient")
@Document("mods/nuclearcraft/ChanceFluidIngredient")
public final class CTChanceFluidIngredient {
    private final IFluidStack internalIngredient;
    private final int chancePercent;
    private final int minStackSize;

    private CTChanceFluidIngredient(IFluidStack internalIngredient, int chancePercent, int minStackSize) {
        this.internalIngredient = internalIngredient;
        this.chancePercent = chancePercent;
        this.minStackSize = minStackSize;
    }

    @ZenCodeType.Method
    public static CTChanceFluidIngredient create(IFluidStack ingredient, int chancePercent, @ZenCodeType.OptionalInt(0) int minStackSize) {
        return new CTChanceFluidIngredient(ingredient, chancePercent, minStackSize);
    }

    @ZenCodeType.Method
    public IFluidStack getInternalIngredient() {
        return internalIngredient;
    }

    @ZenCodeType.Method
    public int getChancePercent() {
        return chancePercent;
    }

    @ZenCodeType.Method
    public int getMinStackSize() {
        return minStackSize;
    }
}
