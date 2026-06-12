package com.nred.nuclearcraft.compat.crafttweaker.ingredient;

import com.blamejared.crafttweaker.api.annotation.ZenRegister;
import com.blamejared.crafttweaker.api.ingredient.IIngredientWithAmount;
import com.blamejared.crafttweaker_annotations.annotations.Document;
import com.nred.nuclearcraft.recipe.SizedChanceItemIngredient;
import org.openzen.zencode.java.ZenCodeType;

@ZenRegister
@ZenCodeType.Name("mods.nuclearcraft.ChanceItemIngredient")
@Document("mods/nuclearcraft/ChanceItemIngredient")
public final class CTChanceItemIngredient {
    private final IIngredientWithAmount internalIngredient;
    private final int chancePercent;
    private final int minStackSize;

    private CTChanceItemIngredient(IIngredientWithAmount internalIngredient, int chancePercent, int minStackSize) {
        this.internalIngredient = internalIngredient;
        this.chancePercent = chancePercent;
        this.minStackSize = minStackSize;
    }

    @ZenCodeType.Method
    public static CTChanceItemIngredient create(IIngredientWithAmount ingredient, int chancePercent, @ZenCodeType.OptionalInt(0) int minStackSize) {
        return new CTChanceItemIngredient(ingredient, chancePercent, minStackSize);
    }

    @ZenCodeType.Method
    public IIngredientWithAmount getInternalIngredient() {
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
    public SizedChanceItemIngredient asSizedChanceItemIngredient() {
        return new SizedChanceItemIngredient(internalIngredient.ingredient().asVanillaIngredient(), internalIngredient.amount(), chancePercent, minStackSize);
    }
}
