package com.nred.nuclearcraft.multiblock.fisson;

import it.zerono.mods.zerocore.lib.CodeHelper;
import it.zerono.mods.zerocore.lib.multiblock.variant.IMultiblockVariant;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.nred.nuclearcraft.config.NCConfig.fission_shield_efficiency;
import static com.nred.nuclearcraft.config.NCConfig.fission_shield_heat_per_flux;

public class FissionNeutronShieldType implements StringRepresentable, IMultiblockVariant {
    public static final FissionNeutronShieldType BORON_SILVER = new FissionNeutronShieldType("boron_silver", 0, () -> fission_shield_heat_per_flux[0], () -> fission_shield_efficiency[0]);
    private static final AtomicInteger _id = new AtomicInteger(0); // Used to increment KubeJS additions

    private final String name;
    private final int id;
    private final Supplier<Double> heatPerFlux;
    private final Supplier<Double> efficiency;
    private final String _translationKey;
    private final Function<Block.Properties, Block.Properties> _blockPropertiesFixer;

    private FissionNeutronShieldType(String name, int id, Supplier<Double> heatPerFlux, Supplier<Double> efficiency) {
        this.name = name;
        this.id = id;
        this.heatPerFlux = heatPerFlux;
        this.efficiency = efficiency;

        this._translationKey = ""; // TODO ADD
        this._blockPropertiesFixer = null;
    }

    public FissionNeutronShieldType(String name, Supplier<Double> heatPerFlux, Supplier<Double> efficiency) { // Used by KubeJS
        this(name, _id.incrementAndGet(), heatPerFlux, efficiency);
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
        return this.id;
    }

    @Override
    public String getName() {
        return CodeHelper.neutralLowercase(this.name);
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