package com.nred.nuclearcraft.multiblock.fisson;

import com.nred.nuclearcraft.block.fission.FissionShieldEntity;
import com.nred.nuclearcraft.enumm.ITileEnum;
import it.zerono.mods.zerocore.lib.CodeHelper;
import it.zerono.mods.zerocore.lib.multiblock.variant.IMultiblockVariant;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.function.Function;
import java.util.function.Supplier;

import static com.nred.nuclearcraft.config.Config2.fission_shield_efficiency;
import static com.nred.nuclearcraft.config.Config2.fission_shield_heat_per_flux;

public enum FissionNeutronShieldType implements StringRepresentable, ITileEnum<FissionShieldEntity.Variant>, IMultiblockVariant {
    BORON_SILVER("boron_silver", () -> fission_shield_heat_per_flux[0], () -> fission_shield_efficiency[0], FissionShieldEntity.BoronSilver.class);

    private final String name;
    private final Supplier<Double> heatPerFlux;
    private final Supplier<Double> efficiency;
    private final String _translationKey;
    private final Function<Block.Properties, Block.Properties> _blockPropertiesFixer;
    private final Class<? extends FissionShieldEntity.Variant> tileClass;

    FissionNeutronShieldType(String name, Supplier<Double> heatPerFlux, Supplier<Double> efficiency, Class<? extends FissionShieldEntity.Variant> tileClass) {
        this.name = name;
        this.heatPerFlux = heatPerFlux;
        this.efficiency = efficiency;
        this.tileClass = tileClass;

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
    public Class<? extends FissionShieldEntity.Variant> getTileClass() {
        return this.tileClass;
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