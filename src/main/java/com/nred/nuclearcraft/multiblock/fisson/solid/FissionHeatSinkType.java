package com.nred.nuclearcraft.multiblock.fisson.solid;

import com.nred.nuclearcraft.block_entity.fission.SolidFissionHeatSinkEntity;
import com.nred.nuclearcraft.enumm.ITileEnum;
import it.zerono.mods.zerocore.lib.CodeHelper;
import it.zerono.mods.zerocore.lib.multiblock.variant.IMultiblockVariant;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.function.Function;
import java.util.function.Supplier;

import static com.nred.nuclearcraft.config.NCConfig.fission_sink_cooling_rate;

public enum FissionHeatSinkType implements StringRepresentable, ITileEnum<SolidFissionHeatSinkEntity.Variant>, IMultiblockVariant {
    WATER("", () -> fission_sink_cooling_rate[0], SolidFissionHeatSinkEntity.Water.class),
    IRON("iron", () -> fission_sink_cooling_rate[1], SolidFissionHeatSinkEntity.Iron.class),
    REDSTONE("redstone", () -> fission_sink_cooling_rate[2], SolidFissionHeatSinkEntity.Redstone.class),
    QUARTZ("quartz", () -> fission_sink_cooling_rate[3], SolidFissionHeatSinkEntity.Quartz.class),
    OBSIDIAN("obsidian", () -> fission_sink_cooling_rate[4], SolidFissionHeatSinkEntity.Obsidian.class),
    NETHER_BRICK("nether_brick", () -> fission_sink_cooling_rate[5], SolidFissionHeatSinkEntity.NetherBrick.class),
    GLOWSTONE("glowstone", () -> fission_sink_cooling_rate[6], SolidFissionHeatSinkEntity.Glowstone.class),
    LAPIS("lapis", () -> fission_sink_cooling_rate[7], SolidFissionHeatSinkEntity.Lapis.class),
    GOLD("gold", () -> fission_sink_cooling_rate[8], SolidFissionHeatSinkEntity.Gold.class),
    PRISMARINE("prismarine", () -> fission_sink_cooling_rate[9], SolidFissionHeatSinkEntity.Prismarine.class),
    SLIME("slime", () -> fission_sink_cooling_rate[10], SolidFissionHeatSinkEntity.Slime.class),
    END_STONE("end_stone", () -> fission_sink_cooling_rate[11], SolidFissionHeatSinkEntity.EndStone.class),
    PURPUR("purpur", () -> fission_sink_cooling_rate[12], SolidFissionHeatSinkEntity.Purpur.class),
    DIAMOND("diamond", () -> fission_sink_cooling_rate[13], SolidFissionHeatSinkEntity.Diamond.class),
    EMERALD("emerald", () -> fission_sink_cooling_rate[14], SolidFissionHeatSinkEntity.Emerald.class),
    COPPER("copper", () -> fission_sink_cooling_rate[15], SolidFissionHeatSinkEntity.Copper.class),
    TIN("tin", () -> fission_sink_cooling_rate[16], SolidFissionHeatSinkEntity.Tin.class),
    LEAD("lead", () -> fission_sink_cooling_rate[17], SolidFissionHeatSinkEntity.Lead.class),
    BORON("boron", () -> fission_sink_cooling_rate[18], SolidFissionHeatSinkEntity.Boron.class),
    LITHIUM("lithium", () -> fission_sink_cooling_rate[19], SolidFissionHeatSinkEntity.Lithium.class),
    MAGNESIUM("magnesium", () -> fission_sink_cooling_rate[20], SolidFissionHeatSinkEntity.Magnesium.class),
    MANGANESE("manganese", () -> fission_sink_cooling_rate[21], SolidFissionHeatSinkEntity.Manganese.class),
    ALUMINUM("aluminum", () -> fission_sink_cooling_rate[22], SolidFissionHeatSinkEntity.Aluminum.class),
    SILVER("silver", () -> fission_sink_cooling_rate[23], SolidFissionHeatSinkEntity.Silver.class),
    FLUORITE("fluorite", () -> fission_sink_cooling_rate[24], SolidFissionHeatSinkEntity.Fluorite.class),
    VILLIAUMITE("villiaumite", () -> fission_sink_cooling_rate[25], SolidFissionHeatSinkEntity.Villiaumite.class),
    CAROBBIITE("carobbiite", () -> fission_sink_cooling_rate[26], SolidFissionHeatSinkEntity.Carobbiite.class),
    ARSENIC("arsenic", () -> fission_sink_cooling_rate[27], SolidFissionHeatSinkEntity.Arsenic.class),
    LIQUID_NITROGEN("liquid_nitrogen", () -> fission_sink_cooling_rate[28], SolidFissionHeatSinkEntity.LiquidNitrogen.class),
    LIQUID_HELIUM("liquid_helium", () -> fission_sink_cooling_rate[29], SolidFissionHeatSinkEntity.LiquidHelium.class),
    ENDERIUM("enderium", () -> fission_sink_cooling_rate[30], SolidFissionHeatSinkEntity.Enderium.class),
    CRYOTHEUM("cryotheum", () -> fission_sink_cooling_rate[31], SolidFissionHeatSinkEntity.Cryotheum.class);

    private final String name;
    private final Supplier<Integer> coolingRate;
    private final String _translationKey;
    private final Function<Block.Properties, Block.Properties> _blockPropertiesFixer;
    private final Class<? extends SolidFissionHeatSinkEntity.Variant> tileClass;

    FissionHeatSinkType(String name, Supplier<Integer> coolingRate, Class<? extends SolidFissionHeatSinkEntity.Variant> tileClass) {
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

    public int getCoolingRate() {
        return coolingRate.get();
    }

    @Override
    public String getSerializedName() {
        return name;
    }

    @Override
    public Class<? extends SolidFissionHeatSinkEntity.Variant> getTileClass() {
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