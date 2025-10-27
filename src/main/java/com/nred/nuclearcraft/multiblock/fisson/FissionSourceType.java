package com.nred.nuclearcraft.multiblock.fisson;

import com.nred.nuclearcraft.block_entity.fission.FissionSourceEntity;
import com.nred.nuclearcraft.enumm.ITileEnum;
import it.zerono.mods.zerocore.lib.CodeHelper;
import it.zerono.mods.zerocore.lib.multiblock.variant.IMultiblockVariant;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.function.Function;
import java.util.function.Supplier;

import static com.nred.nuclearcraft.config.NCConfig.fission_source_efficiency;

public enum FissionSourceType implements StringRepresentable, ITileEnum<FissionSourceEntity.Variant>, IMultiblockVariant {
    RADIUM_BERYLLIUM("radium_beryllium", () -> fission_source_efficiency[0], FissionSourceEntity.RadiumBeryllium.class),
    POLONIUM_BERYLLIUM("polonium_beryllium", () -> fission_source_efficiency[1], FissionSourceEntity.PoloniumBeryllium.class),
    CALIFORNIUM("californium", () -> fission_source_efficiency[2], FissionSourceEntity.Californium.class);

    private final String name;
    private final Supplier<Double> efficiency;
    private final String _translationKey;
    private final Function<Block.Properties, Block.Properties> _blockPropertiesFixer;
    private final Class<? extends FissionSourceEntity.Variant> tileClass;

    FissionSourceType(String name, Supplier<Double> efficiency, Class<? extends FissionSourceEntity.Variant> tileClass) {
        this.name = name;
        this.efficiency = efficiency;
        this.tileClass = tileClass;

        this._translationKey = ""; // TODO ADD
        this._blockPropertiesFixer = null;
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
    public Class<? extends FissionSourceEntity.Variant> getTileClass() {
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