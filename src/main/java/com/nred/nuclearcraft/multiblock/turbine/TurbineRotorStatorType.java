package com.nred.nuclearcraft.multiblock.turbine;

import com.nred.nuclearcraft.block.turbine.TurbineRotorStatorEntity;
import com.nred.nuclearcraft.enumm.ITileEnum;
import it.zerono.mods.zerocore.lib.CodeHelper;
import it.zerono.mods.zerocore.lib.multiblock.variant.IMultiblockVariant;
import net.minecraft.world.level.block.Block;

import java.util.function.Function;
import java.util.function.Supplier;

import static com.nred.nuclearcraft.config.Config2.turbine_stator_expansion;

public enum TurbineRotorStatorType implements TurbineRotorBladeUtil.IRotorStatorType, ITileEnum<TurbineRotorStatorEntity.Variant>, IMultiblockVariant {
    STANDARD("standard", () -> turbine_stator_expansion, TurbineRotorStatorEntity.Standard.class);

    private final String name;
    private final Supplier<Double> expansion;
    private final Class<? extends TurbineRotorStatorEntity.Variant> tileClass;
    private final String _translationKey;
    private final Function<Block.Properties, Block.Properties> _blockPropertiesFixer;

    TurbineRotorStatorType(String name, Supplier<Double> expansion, Class<? extends TurbineRotorStatorEntity.Variant> tileClass) {
        this.name = name;
        this.expansion = expansion;
        this.tileClass = tileClass;

        this._translationKey = ""; // TODO ADD
        this._blockPropertiesFixer = null;
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
    public Class<? extends TurbineRotorStatorEntity.Variant> getTileClass() {
        return tileClass;
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
    public Block.Properties getBlockProperties() {
        return this._blockPropertiesFixer.apply(this.getDefaultBlockProperties());
    }
}