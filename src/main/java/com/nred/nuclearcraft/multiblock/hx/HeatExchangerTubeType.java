package com.nred.nuclearcraft.multiblock.hx;

import com.nred.nuclearcraft.block_entity.hx.HeatExchangerTubeEntity;
import com.nred.nuclearcraft.enumm.ITileEnum;
import it.zerono.mods.zerocore.lib.CodeHelper;
import it.zerono.mods.zerocore.lib.multiblock.variant.IMultiblockVariant;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.function.Function;
import java.util.function.Supplier;

import static com.nred.nuclearcraft.config.NCConfig.heat_exchanger_heat_retention_mult;
import static com.nred.nuclearcraft.config.NCConfig.heat_exchanger_heat_transfer_coefficient;


public enum HeatExchangerTubeType implements StringRepresentable, ITileEnum<HeatExchangerTubeEntity.Variant>, IMultiblockVariant {
    COPPER("copper", () -> heat_exchanger_heat_transfer_coefficient[0], () -> heat_exchanger_heat_retention_mult[0], HeatExchangerTubeEntity.Copper.class),
    HARD_CARBON("hard_carbon", () -> heat_exchanger_heat_transfer_coefficient[1], () -> heat_exchanger_heat_retention_mult[1], HeatExchangerTubeEntity.HardCarbon.class),
    THERMOCONDUCTING("thermoconducting", () -> heat_exchanger_heat_transfer_coefficient[2], () -> heat_exchanger_heat_retention_mult[2], HeatExchangerTubeEntity.Thermoconducting.class);

    private final String name;
    private final Supplier<Double> heatTransferCoefficient;
    private final Supplier<Double> heatRetentionMult;
    private final String _translationKey;
    private final Function<Block.Properties, Block.Properties> _blockPropertiesFixer;
    private final Class<? extends HeatExchangerTubeEntity.Variant> tileClass;

    HeatExchangerTubeType(String name, Supplier<Double> heatTransferCoefficient, Supplier<Double> heatRetentionMult, Class<? extends HeatExchangerTubeEntity.Variant> tileClass) {
        this.name = name;
        this.heatTransferCoefficient = heatTransferCoefficient;
        this.heatRetentionMult = heatRetentionMult;
        this.tileClass = tileClass;

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
    public Class<? extends HeatExchangerTubeEntity.Variant> getTileClass() {
        return tileClass;
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