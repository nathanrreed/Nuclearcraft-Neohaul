package com.nred.nuclearcraft.multiblock.turbine;

import com.nred.nuclearcraft.block.turbine.TurbineDynamoCoilEntity;
import com.nred.nuclearcraft.enumm.ITileEnum;
import it.zerono.mods.zerocore.lib.CodeHelper;
import it.zerono.mods.zerocore.lib.multiblock.variant.IMultiblockVariant;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.function.Function;
import java.util.function.Supplier;

import static com.nred.nuclearcraft.config.Config.turbine_coil_conductivity;

public enum TurbineDynamoCoilType implements StringRepresentable, ITileEnum<TurbineDynamoCoilEntity.Variant>, IMultiblockVariant {
    MAGNESIUM("magnesium", () -> turbine_coil_conductivity.get(0), TurbineDynamoCoilEntity.Magnesium.class),
    BERYLLIUM("beryllium", () -> turbine_coil_conductivity.get(1), TurbineDynamoCoilEntity.Beryllium.class),
    ALUMINUM("aluminum", () -> turbine_coil_conductivity.get(2), TurbineDynamoCoilEntity.Aluminum.class),
    GOLD("gold", () -> turbine_coil_conductivity.get(3), TurbineDynamoCoilEntity.Gold.class),
    COPPER("copper", () -> turbine_coil_conductivity.get(4), TurbineDynamoCoilEntity.Copper.class),
    SILVER("silver", () -> turbine_coil_conductivity.get(5), TurbineDynamoCoilEntity.Silver.class);

    private final String name;
    private final Supplier<Double> conductivity;
    private final String _translationKey;
    private final Function<Block.Properties, Block.Properties> _blockPropertiesFixer;
    private final Class<? extends TurbineDynamoCoilEntity.Variant> tileClass;

    TurbineDynamoCoilType(String name, Supplier<Double> conductivity, Class<? extends TurbineDynamoCoilEntity.Variant> tileClass) {
        this.name = name;
        this.conductivity = conductivity;
        this.tileClass = tileClass;

        this._translationKey = ""; // TODO ADD
        this._blockPropertiesFixer = null;
    }

    @Override
    public String toString() {
        return getSerializedName();
    }

    public double getConductivity() {
        return conductivity.get();
    }

    @Override
    public String getSerializedName() {
        return name;
    }

    @Override
    public Class<? extends TurbineDynamoCoilEntity.Variant> getTileClass() {
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