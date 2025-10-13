package com.nred.nuclearcraft.multiblock.fisson;

import it.zerono.mods.zerocore.lib.CodeHelper;
import it.zerono.mods.zerocore.lib.multiblock.variant.IMultiblockVariant;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.function.Function;

public enum FissionReflectorType implements StringRepresentable, IMultiblockVariant {
    BERYLLIUM_CARBON("beryllium_carbon", 0.5D, 0.25D),
    LEAD_STEEL("lead_steel", 1D, 0.5D);

    private final String name;
    private final double efficiency;
    private final double reflectivity;
    private final String _translationKey;
    private final Function<Block.Properties, Block.Properties> _blockPropertiesFixer;

    FissionReflectorType(String name, double efficiency, double reflectivity) {
        this.name = name;
        this.efficiency = efficiency;
        this.reflectivity = reflectivity;

        this._translationKey = ""; // TODO ADD
        this._blockPropertiesFixer = null;
    }

    @Override
    public String toString() {
        return getSerializedName();
    }

    public double getEfficiency() {
        return efficiency;
    }

    public double getReflectivity() {
        return reflectivity;
    }

    @Override
    public String getSerializedName() {
        return name;
    }

    @Override
    public int getId() {
        return this.ordinal();
    }

    @Override
    public String getName() {
        return CodeHelper.neutralLowercase(this.name());
    }

    @Override
    public String getTranslationKey() {
        return this._translationKey;
    }

    @Override
    public BlockBehaviour.Properties getBlockProperties() {
        return this._blockPropertiesFixer.apply(this.getDefaultBlockProperties());
    }
}