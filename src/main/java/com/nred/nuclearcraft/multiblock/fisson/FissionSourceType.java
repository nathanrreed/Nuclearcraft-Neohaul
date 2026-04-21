package com.nred.nuclearcraft.multiblock.fisson;

import it.zerono.mods.zerocore.lib.CodeHelper;
import it.zerono.mods.zerocore.lib.multiblock.variant.IMultiblockVariant;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.nred.nuclearcraft.config.NCConfig.fission_source_efficiency;

public class FissionSourceType implements StringRepresentable, IMultiblockVariant {
    public static final FissionSourceType RADIUM_BERYLLIUM = new FissionSourceType("radium_beryllium", 0, () -> fission_source_efficiency[0]);
    public static final FissionSourceType POLONIUM_BERYLLIUM = new FissionSourceType("polonium_beryllium", 1, () -> fission_source_efficiency[1]);
    public static final FissionSourceType CALIFORNIUM = new FissionSourceType("californium", 2, () -> fission_source_efficiency[2]);
    private static final AtomicInteger _id = new AtomicInteger(2); // Used to increment KubeJS additions

    private final String name;
    private final int id;
    private final Supplier<Double> efficiency;
    private final String _translationKey;
    private final Function<Block.Properties, Block.Properties> _blockPropertiesFixer;

    private FissionSourceType(String name, int id, Supplier<Double> efficiency) {
        this.name = name;
        this.id = id;
        this.efficiency = efficiency;

        this._translationKey = ""; // TODO ADD
        this._blockPropertiesFixer = null;
    }

    public FissionSourceType(String name, Supplier<Double> efficiency) { // Used by KubeJS
        this(name, _id.incrementAndGet(), efficiency);
    }

    @Override
    public String toString() {
        return getSerializedName();
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