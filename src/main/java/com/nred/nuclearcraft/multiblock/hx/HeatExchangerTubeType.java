package com.nred.nuclearcraft.multiblock.hx;

import it.zerono.mods.zerocore.lib.CodeHelper;
import it.zerono.mods.zerocore.lib.multiblock.variant.IMultiblockVariant;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.function.Function;
import java.util.function.Supplier;

import static com.nred.nuclearcraft.config.NCConfig.heat_exchanger_heat_retention_mult;
import static com.nred.nuclearcraft.config.NCConfig.heat_exchanger_heat_transfer_coefficient;


public enum HeatExchangerTubeType implements StringRepresentable, IMultiblockVariant {
    COPPER("copper", () -> heat_exchanger_heat_transfer_coefficient[0], () -> heat_exchanger_heat_retention_mult[0]),
    HARD_CARBON("hard_carbon", () -> heat_exchanger_heat_transfer_coefficient[1], () -> heat_exchanger_heat_retention_mult[1]),
    THERMOCONDUCTING("thermoconducting", () -> heat_exchanger_heat_transfer_coefficient[2], () -> heat_exchanger_heat_retention_mult[2]);

    private final String name;
    private final Supplier<Double> heatTransferCoefficient;
    private final Supplier<Double> heatRetentionMult;
    private final String _translationKey;
    private final Function<Block.Properties, Block.Properties> _blockPropertiesFixer;

    HeatExchangerTubeType(String name, Supplier<Double> heatTransferCoefficient, Supplier<Double> heatRetentionMult) {
        this.name = name;
        this.heatTransferCoefficient = heatTransferCoefficient;
        this.heatRetentionMult = heatRetentionMult;

        this._translationKey = ""; // TODO ADD
        this._blockPropertiesFixer = null;
    }

    @Override
    public String getSerializedName() {
        return name;
    }

    @Override
    public String toString() {
        return getName();
    }

    public double getHeatTransferCoefficient() {
        return heatTransferCoefficient.get();
    }

    public double getHeatRetentionMultiplier() {
        return heatRetentionMult.get();
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