package com.nred.nuclearcraft.multiblock.turbine;

import it.zerono.mods.zerocore.lib.CodeHelper;
import it.zerono.mods.zerocore.lib.multiblock.variant.IMultiblockVariant;
import net.minecraft.world.level.block.Block;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.nred.nuclearcraft.config.NCConfig.turbine_blade_efficiency;
import static com.nred.nuclearcraft.config.NCConfig.turbine_blade_expansion;

public class TurbineRotorBladeType implements TurbineRotorBladeUtil.IRotorBladeType, IMultiblockVariant {
    public static final TurbineRotorBladeType STEEL = new TurbineRotorBladeType("steel", 0, () -> turbine_blade_efficiency[0], () -> turbine_blade_expansion[0]);
    public static final TurbineRotorBladeType EXTREME = new TurbineRotorBladeType("extreme", 1, () -> turbine_blade_efficiency[1], () -> turbine_blade_expansion[1]);
    public static final TurbineRotorBladeType SIC_SIC_CMC = new TurbineRotorBladeType("sic_sic_cmc", 2, () -> turbine_blade_efficiency[2], () -> turbine_blade_expansion[2]);
    private static final AtomicInteger _id = new AtomicInteger(2); // Used to increment KubeJS additions

    private final String name;
    private final Supplier<Double> efficiency;
    private final Supplier<Double> expansion;
    private final String _translationKey;
    private final Function<Block.Properties, Block.Properties> _blockPropertiesFixer;
    private final int id;

    public TurbineRotorBladeType(String name, int id, Supplier<Double> efficiency, Supplier<Double> expansion) {
        this.name = name;
        this.efficiency = efficiency;
        this.expansion = expansion;

        this._translationKey = ""; // TODO ADD
        this._blockPropertiesFixer = null;

        this.id = id;
    }

    public TurbineRotorBladeType(String name, Supplier<Double> efficiency, Supplier<Double> expansion) { // Used by KubeJS
        this(name, _id.incrementAndGet(), efficiency, expansion);
    }

    @Override
    public String toString() {
        return getSerializedName();
    }

    @Override
    public double getEfficiency() {
        return efficiency.get();
    }

    @Override
    public double getExpansionCoefficient() {
        return expansion.get();
    }

    @Override
    public String getSerializedName() {
        return this.name;
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
    public Block.Properties getBlockProperties() {
        return this._blockPropertiesFixer.apply(this.getDefaultBlockProperties());
    }
}
