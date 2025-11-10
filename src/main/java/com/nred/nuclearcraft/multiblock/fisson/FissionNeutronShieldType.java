package com.nred.nuclearcraft.multiblock.fisson;

import it.zerono.mods.zerocore.lib.CodeHelper;
import it.zerono.mods.zerocore.lib.multiblock.variant.IMultiblockVariant;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.function.Function;
import java.util.function.Supplier;

import static com.nred.nuclearcraft.config.NCConfig.fission_shield_efficiency;
import static com.nred.nuclearcraft.config.NCConfig.fission_shield_heat_per_flux;

public enum FissionNeutronShieldType implements StringRepresentable, IMultiblockVariant {
    BORON_SILVER("boron_silver", () -> fission_shield_heat_per_flux[0], () -> fission_shield_efficiency[0]);

    private final String name;
    private final Supplier<Double> heatPerFlux;
    private final Supplier<Double> efficiency;
    private final String _translationKey;
    private final Function<Block.Properties, Block.Properties> _blockPropertiesFixer;

    FissionNeutronShieldType(String name, Supplier<Double> heatPerFlux, Supplier<Double> efficiency) {
        this.name = name;
        this.heatPerFlux = heatPerFlux;
        this.efficiency = efficiency;

        this._translationKey = ""; // TODO ADD
        this._blockPropertiesFixer = null;
    }

    @Override
    public String toString() {
        return getSerializedName();
    }

    public double getHeatPerFlux() {
        return heatPerFlux.get();
    }

    public double getEfficiency() {
        return efficiency.get();
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