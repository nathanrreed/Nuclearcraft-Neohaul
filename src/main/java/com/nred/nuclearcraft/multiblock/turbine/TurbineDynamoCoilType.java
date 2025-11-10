package com.nred.nuclearcraft.multiblock.turbine;

import it.zerono.mods.zerocore.lib.CodeHelper;
import it.zerono.mods.zerocore.lib.multiblock.variant.IMultiblockVariant;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.function.Function;
import java.util.function.Supplier;

import static com.nred.nuclearcraft.config.NCConfig.turbine_coil_conductivity;

public enum TurbineDynamoCoilType implements StringRepresentable, IMultiblockVariant {
    MAGNESIUM("magnesium", () -> turbine_coil_conductivity[0]),
    BERYLLIUM("beryllium", () -> turbine_coil_conductivity[1]),
    ALUMINUM("aluminum", () -> turbine_coil_conductivity[2]),
    GOLD("gold", () -> turbine_coil_conductivity[3]),
    COPPER("copper", () -> turbine_coil_conductivity[4]),
    SILVER("silver", () -> turbine_coil_conductivity[5]);

    private final String name;
    private final Supplier<Double> conductivity;
    private final String _translationKey;
    private final Function<Block.Properties, Block.Properties> _blockPropertiesFixer;

    TurbineDynamoCoilType(String name, Supplier<Double> conductivity) {
        this.name = name;
        this.conductivity = conductivity;

        this._translationKey = ""; // TODO ADD
        this._blockPropertiesFixer = null;
    }

    @Override
    public String toString() {
        return getSerializedName();
    }

    public double getConductivity() {
        return conductivity.get();
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