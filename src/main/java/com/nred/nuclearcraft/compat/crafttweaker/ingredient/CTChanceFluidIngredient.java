package com.nred.nuclearcraft.compat.crafttweaker.ingredient;

import com.blamejared.crafttweaker.api.annotation.ZenRegister;
import com.blamejared.crafttweaker.api.fluid.IFluidStack;
import com.blamejared.crafttweaker.natives.ingredient.ExpandCTFluidIngredientNeoForge;
import com.blamejared.crafttweaker_annotations.annotations.Document;
import com.nred.nuclearcraft.recipe.SizedChanceFluidIngredient;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;
import org.openzen.zencode.java.ZenCodeType;

@ZenRegister
@ZenCodeType.Name("mods.nuclearcraft.ChanceFluidIngredient")
@Document("mods/nuclearcraft/ChanceFluidIngredient")
public final class CTChanceFluidIngredient {
    private final IFluidStack internalIngredient;
    private final int chancePercent;
    private final int minStackSize;
    private final int increment;

    private CTChanceFluidIngredient(IFluidStack internalIngredient, int chancePercent, int minStackSize, int increment) {
        this.internalIngredient = internalIngredient;
        this.chancePercent = chancePercent;
        this.minStackSize = minStackSize;
        this.increment = increment;
    }

    @ZenCodeType.Method
    public static CTChanceFluidIngredient create(IFluidStack ingredient, int chancePercent, @ZenCodeType.OptionalInt(0) int minStackSize, @ZenCodeType.OptionalInt(1) int increment) {
        return new CTChanceFluidIngredient(ingredient, chancePercent, minStackSize, increment);
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

    @ZenCodeType.Method
    public int getIncrement() {
        return increment;
    }

    @ZenCodeType.Method
    public SizedChanceFluidIngredient asSizedChanceFluidIngredient() {
        SizedFluidIngredient ingredient = ExpandCTFluidIngredientNeoForge.asSizedFluidIngredient(internalIngredient.asFluidIngredient());
        return new SizedChanceFluidIngredient(ingredient.ingredient(), ingredient.amount(), chancePercent, minStackSize, increment
        );
    }
}
