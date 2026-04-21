package com.nred.nuclearcraft.multiblock.turbine;

import it.zerono.mods.zerocore.lib.CodeHelper;
import it.zerono.mods.zerocore.lib.multiblock.variant.IMultiblockVariant;
import net.minecraft.world.level.block.Block;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.nred.nuclearcraft.config.NCConfig.turbine_stator_expansion;

public class TurbineRotorStatorType implements TurbineRotorBladeUtil.IRotorStatorType, IMultiblockVariant {
    public static final TurbineRotorStatorType STANDARD = new TurbineRotorStatorType("standard", 0, () -> turbine_stator_expansion);
    private static final AtomicInteger _id = new AtomicInteger(0); // Used to increment KubeJS additions

    private final String name;
    private final Supplier<Double> expansion;
    private final String _translationKey;
    private final Function<Block.Properties, Block.Properties> _blockPropertiesFixer;
    private final int id;

    private TurbineRotorStatorType(String name, int id, Supplier<Double> expansion) {
        this.name = name;
        this.id = id;
        this.expansion = expansion;

        this._translationKey = ""; // TODO ADD
        this._blockPropertiesFixer = null;
    }

    public TurbineRotorStatorType(String name, Supplier<Double> expansion) { // Used by KubeJS
        this(name, _id.incrementAndGet(), expansion);
    }

    @Override
    public String toString() {
        return getSerializedName();
    }

    @Override
    public double getExpansionCoefficient() {
        return expansion.get();
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
    public Block.Properties getBlockProperties() {
        return this._blockPropertiesFixer.apply(this.getDefaultBlockProperties());
    }
}