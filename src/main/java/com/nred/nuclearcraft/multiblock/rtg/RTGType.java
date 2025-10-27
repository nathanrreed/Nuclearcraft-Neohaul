package com.nred.nuclearcraft.multiblock.rtg;

import com.nred.nuclearcraft.radiation.RadSources;
import it.zerono.mods.zerocore.lib.CodeHelper;
import it.zerono.mods.zerocore.lib.multiblock.variant.IMultiblockVariant;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.function.Function;

import static com.nred.nuclearcraft.config.NCConfig.rtg_power;

public enum RTGType implements StringRepresentable, IMultiblockVariant {
    URANIUM(0, "uranium", RadSources.URANIUM_238),
    PLUTONIUM(1, "plutonium", RadSources.PLUTONIUM_238),
    AMERICIUM(2, "americium", RadSources.AMERICIUM_241),
    CALIFORNIUM(3, "californium", RadSources.CALIFORNIUM_250);

    private final int id;
    private final String name;
    private final double radiation;
    private final String _translationKey;
    private final Function<Block.Properties, Block.Properties> _blockPropertiesFixer;

    RTGType(int id, String name, double radiation) {
        this.id = id;
        this.name = name;
        this.radiation = radiation;

        this._translationKey = ""; // TODO ADD
        this._blockPropertiesFixer = null;
    }

    @Override
    public String getSerializedName() {
        return name;
    }

    @Override
    public String toString() {
        return getSerializedName();
    }

    public int getPower() {
        return rtg_power[id];
    }

    public double getRadiation() {
        return radiation / 8D;
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