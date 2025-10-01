package com.nred.nuclearcraft.multiblock.fisson.solid;

import com.nred.nuclearcraft.block.fission.FissionHeatSinkEntity;
import com.nred.nuclearcraft.enumm.ITileEnum;
import it.zerono.mods.zerocore.lib.CodeHelper;
import it.zerono.mods.zerocore.lib.multiblock.variant.IMultiblockVariant;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.function.Function;
import java.util.function.Supplier;

import static com.nred.nuclearcraft.config.Config2.fission_sink_cooling_rate;

public enum FissionHeatSinkType implements StringRepresentable, ITileEnum<FissionHeatSinkEntity.Variant>, IMultiblockVariant {
    WATER("", () -> fission_sink_cooling_rate[0], FissionHeatSinkEntity.Water.class),
    IRON("iron", () -> fission_sink_cooling_rate[1], FissionHeatSinkEntity.Iron.class),
    REDSTONE("redstone", () -> fission_sink_cooling_rate[2], FissionHeatSinkEntity.Redstone.class),
    QUARTZ("quartz", () -> fission_sink_cooling_rate[3], FissionHeatSinkEntity.Quartz.class),
    OBSIDIAN("obsidian", () -> fission_sink_cooling_rate[4], FissionHeatSinkEntity.Obsidian.class),
    NETHER_BRICK("nether_brick", () -> fission_sink_cooling_rate[5], FissionHeatSinkEntity.NetherBrick.class),
    GLOWSTONE("glowstone", () -> fission_sink_cooling_rate[6], FissionHeatSinkEntity.Glowstone.class),
    LAPIS("lapis", () -> fission_sink_cooling_rate[7], FissionHeatSinkEntity.Lapis.class),
    GOLD("gold", () -> fission_sink_cooling_rate[8], FissionHeatSinkEntity.Gold.class),
    PRISMARINE("prismarine", () -> fission_sink_cooling_rate[9], FissionHeatSinkEntity.Prismarine.class),
    SLIME("slime", () -> fission_sink_cooling_rate[10], FissionHeatSinkEntity.Slime.class),
    END_STONE("end_stone", () -> fission_sink_cooling_rate[11], FissionHeatSinkEntity.EndStone.class),
    PURPUR("purpur", () -> fission_sink_cooling_rate[12], FissionHeatSinkEntity.Purpur.class),
    DIAMOND("diamond", () -> fission_sink_cooling_rate[13], FissionHeatSinkEntity.Diamond.class),
    EMERALD("emerald", () -> fission_sink_cooling_rate[14], FissionHeatSinkEntity.Emerald.class),
    COPPER("copper", () -> fission_sink_cooling_rate[15], FissionHeatSinkEntity.Copper.class),
    TIN("tin", () -> fission_sink_cooling_rate[16], FissionHeatSinkEntity.Tin.class),
    LEAD("lead", () -> fission_sink_cooling_rate[17], FissionHeatSinkEntity.Lead.class),
    BORON("boron", () -> fission_sink_cooling_rate[18], FissionHeatSinkEntity.Boron.class),
    LITHIUM("lithium", () -> fission_sink_cooling_rate[19], FissionHeatSinkEntity.Lithium.class),
    MAGNESIUM("magnesium", () -> fission_sink_cooling_rate[20], FissionHeatSinkEntity.Magnesium.class),
    MANGANESE("manganese", () -> fission_sink_cooling_rate[21], FissionHeatSinkEntity.Manganese.class),
    ALUMINUM("aluminum", () -> fission_sink_cooling_rate[22], FissionHeatSinkEntity.Aluminum.class),
    SILVER("silver", () -> fission_sink_cooling_rate[23], FissionHeatSinkEntity.Silver.class),
    FLUORITE("fluorite", () -> fission_sink_cooling_rate[24], FissionHeatSinkEntity.Fluorite.class),
    VILLIAUMITE("villiaumite", () -> fission_sink_cooling_rate[25], FissionHeatSinkEntity.Villiaumite.class),
    CAROBBIITE("carobbiite", () -> fission_sink_cooling_rate[26], FissionHeatSinkEntity.Carobbiite.class),
    ARSENIC("arsenic", () -> fission_sink_cooling_rate[27], FissionHeatSinkEntity.Arsenic.class),
    LIQUID_NITROGEN("liquid_nitrogen", () -> fission_sink_cooling_rate[28], FissionHeatSinkEntity.LiquidNitrogen.class),
    LIQUID_HELIUM("liquid_helium", () -> fission_sink_cooling_rate[29], FissionHeatSinkEntity.LiquidHelium.class),
    ENDERIUM("enderium", () -> fission_sink_cooling_rate[30], FissionHeatSinkEntity.Enderium.class),
    CRYOTHEUM("cryotheum", () -> fission_sink_cooling_rate[31], FissionHeatSinkEntity.Cryotheum.class);

    private final String name;
    private final Supplier<Integer> coolingRate;
    private final String _translationKey;
    private final Function<Block.Properties, Block.Properties> _blockPropertiesFixer;
    private final Class<? extends FissionHeatSinkEntity.Variant> tileClass;

    FissionHeatSinkType(String name, Supplier<Integer> coolingRate, Class<? extends FissionHeatSinkEntity.Variant> tileClass) {
        this.name = name;
        this.coolingRate = coolingRate;
        this.tileClass = tileClass;

        this._translationKey = ""; // TODO ADD
        this._blockPropertiesFixer = null;
    }

    @Override
    public String toString() {
        return getSerializedName();
    }

    public double getCoolingRate() {
        return coolingRate.get();
    }

    @Override
    public String getSerializedName() {
        return name;
    }

    @Override
    public Class<? extends FissionHeatSinkEntity.Variant> getTileClass() {
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