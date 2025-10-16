package com.nred.nuclearcraft.multiblock.turbine;

import com.nred.nuclearcraft.block_entity.turbine.TurbineRotorBladeEntity;
import com.nred.nuclearcraft.enumm.ITileEnum;
import it.zerono.mods.zerocore.lib.CodeHelper;
import it.zerono.mods.zerocore.lib.multiblock.variant.IMultiblockVariant;
import net.minecraft.world.level.block.Block;

import java.util.function.Function;
import java.util.function.Supplier;

import static com.nred.nuclearcraft.config.Config2.turbine_blade_efficiency;
import static com.nred.nuclearcraft.config.Config2.turbine_blade_expansion;

public enum TurbineRotorBladeType implements TurbineRotorBladeUtil.IRotorBladeType, ITileEnum<TurbineRotorBladeEntity.Variant>, IMultiblockVariant {
    STEEL("steel", () -> turbine_blade_efficiency[0], () -> turbine_blade_expansion[0], TurbineRotorBladeEntity.Steel.class),
    EXTREME("extreme", () -> turbine_blade_efficiency[1], () -> turbine_blade_expansion[1], TurbineRotorBladeEntity.Extreme.class),
    SIC_SIC_CMC("sic_sic_cmc", () -> turbine_blade_efficiency[2], () -> turbine_blade_expansion[2], TurbineRotorBladeEntity.SicSicCMC.class);

    private final String name;
    private final Supplier<Double> efficiency;
    private final Supplier<Double> expansion;
    private final Class<? extends TurbineRotorBladeEntity.Variant> tileClass;
    private final String _translationKey;
    private final Function<Block.Properties, Block.Properties> _blockPropertiesFixer;

    TurbineRotorBladeType(String name, Supplier<Double> efficiency, Supplier<Double> expansion, Class<? extends TurbineRotorBladeEntity.Variant> tileClass) {
        this.name = name;
        this.efficiency = efficiency;
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
    public double getEfficiency() {
        return efficiency.get();
    }

    @Override
    public double getExpansionCoefficient() {
        return expansion.get();
    }

    @Override
    public Class<? extends TurbineRotorBladeEntity.Variant> getTileClass() {
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
