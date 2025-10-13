package com.nred.nuclearcraft.recipe;

import com.nred.nuclearcraft.util.NCMath;
import com.nred.nuclearcraft.util.UnitHelper;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;

public class RecipeUnitInfo {
    public static final RecipeUnitInfo DEFAULT = new RecipeUnitInfo("R/t", 0, 1D);

    public final String unit;
    public final int startingPrefix;
    public final double rateMultiplier;

    public RecipeUnitInfo(String unit, int startingPrefix, double rateMultiplier) {
        this.unit = unit;
        this.startingPrefix = startingPrefix;
        this.rateMultiplier = rateMultiplier;
    }

    public RecipeUnitInfo withRateMultiplier(double rateMultiplier) {
        return new RecipeUnitInfo(unit, startingPrefix, rateMultiplier);
    }

    public void writeToNBT(CompoundTag nbt, HolderLookup.Provider registries, String name) {
        CompoundTag tag = new CompoundTag();
        tag.putString("unit", unit);
        tag.putInt("startingPrefix", startingPrefix);
        tag.putDouble("rateMultiplier", rateMultiplier);
        nbt.put(name, tag);
    }

    public static RecipeUnitInfo readFromNBT(CompoundTag nbt, HolderLookup.Provider registries, String name) {
        if (nbt.contains(name, 10)) {
            CompoundTag tag = nbt.getCompound(name);
            return new RecipeUnitInfo(tag.getString("unit"), tag.getInt("startingPrefix"), tag.getDouble("rateMultiplier"));
        }
        return DEFAULT;
    }

    public String getString(Double processTime, int maxLength) {
        double rate = processTime == null ? 0D : rateMultiplier / processTime;
        if (unit.equals("B") || unit.equals("B/t")) {
            return UnitHelper.prefix(rate, maxLength, unit, startingPrefix);
        } else {
            return NCMath.sigFigs(rate, maxLength) + " " + unit;
        }
    }
}