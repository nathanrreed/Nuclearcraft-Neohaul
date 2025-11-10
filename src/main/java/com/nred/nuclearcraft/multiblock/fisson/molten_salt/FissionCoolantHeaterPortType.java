package com.nred.nuclearcraft.multiblock.fisson.molten_salt;

import it.zerono.mods.zerocore.lib.CodeHelper;
import it.zerono.mods.zerocore.lib.multiblock.variant.IMultiblockVariant;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.function.Function;

public enum FissionCoolantHeaterPortType implements StringRepresentable, IMultiblockVariant, ICoolantType {
    STANDARD("standard"),
    IRON("iron"),
    REDSTONE("redstone"),
    QUARTZ("quartz"),
    OBSIDIAN("obsidian"),
    NETHER_BRICK("nether_brick"),
    GLOWSTONE("glowstone"),
    LAPIS("lapis"),
    GOLD("gold"),
    PRISMARINE("prismarine"),
    SLIME("slime"),
    END_STONE("end_stone"),
    PURPUR("purpur"),
    DIAMOND("diamond"),
    EMERALD("emerald"),
    COPPER("copper"),
    TIN("tin"),
    LEAD("lead"),
    BORON("boron"),
    LITHIUM("lithium"),
    MAGNESIUM("magnesium"),
    MANGANESE("manganese"),
    ALUMINUM("aluminum"),
    SILVER("silver"),
    FLUORITE("fluorite"),
    VILLIAUMITE("villiaumite"),
    CAROBBIITE("carobbiite"),
    ARSENIC("arsenic"),
    LIQUID_NITROGEN("liquid_nitrogen"),
    LIQUID_HELIUM("liquid_helium"),
    ENDERIUM("enderium"),
    CRYOTHEUM("cryotheum");

    private final String name;
    private final String _translationKey;
    private final Function<Block.Properties, Block.Properties> _blockPropertiesFixer;

    FissionCoolantHeaterPortType(String name) {
        this.name = name;

        this._translationKey = ""; // TODO ADD
        this._blockPropertiesFixer = null;
    }

    @Override
    public String toString() {
        return getSerializedName();
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
    public BlockBehaviour.Properties getBlockProperties() {
        return this._blockPropertiesFixer.apply(this.getDefaultBlockProperties());
    }
}