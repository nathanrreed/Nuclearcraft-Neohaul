package com.nred.nuclearcraft.multiblock.fisson.molten_salt;

import com.nred.nuclearcraft.block.fission.FissionHeaterEntity;
import com.nred.nuclearcraft.enumm.ITileEnum;
import it.zerono.mods.zerocore.lib.CodeHelper;
import it.zerono.mods.zerocore.lib.multiblock.variant.IMultiblockVariant;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.function.Function;
import java.util.function.Supplier;

import static com.nred.nuclearcraft.config.Config2.fission_heater_cooling_rate;

public enum FissionCoolantHeaterType implements StringRepresentable, ITileEnum<FissionHeaterEntity.Variant>, IMultiblockVariant {
    STANDARD("standard", () -> fission_heater_cooling_rate[0], FissionHeaterEntity.Standard.class),
    IRON("iron", () -> fission_heater_cooling_rate[1], FissionHeaterEntity.Iron.class),
    REDSTONE("redstone", () -> fission_heater_cooling_rate[2], FissionHeaterEntity.Redstone.class),
    QUARTZ("quartz", () -> fission_heater_cooling_rate[3], FissionHeaterEntity.Quartz.class),
    OBSIDIAN("obsidian", () -> fission_heater_cooling_rate[4], FissionHeaterEntity.Obsidian.class),
    NETHER_BRICK("nether_brick", () -> fission_heater_cooling_rate[5], FissionHeaterEntity.NetherBrick.class),
    GLOWSTONE("glowstone", () -> fission_heater_cooling_rate[6], FissionHeaterEntity.Glowstone.class),
    LAPIS("lapis", () -> fission_heater_cooling_rate[7], FissionHeaterEntity.Lapis.class),
    GOLD("gold", () -> fission_heater_cooling_rate[8], FissionHeaterEntity.Gold.class),
    PRISMARINE("prismarine", () -> fission_heater_cooling_rate[9], FissionHeaterEntity.Prismarine.class),
    SLIME("slime", () -> fission_heater_cooling_rate[10], FissionHeaterEntity.Slime.class),
    END_STONE("end_stone", () -> fission_heater_cooling_rate[11], FissionHeaterEntity.EndStone.class),
    PURPUR("purpur", () -> fission_heater_cooling_rate[12], FissionHeaterEntity.Purpur.class),
    DIAMOND("diamond", () -> fission_heater_cooling_rate[13], FissionHeaterEntity.Diamond.class),
    EMERALD("emerald", () -> fission_heater_cooling_rate[14], FissionHeaterEntity.Emerald.class),
    COPPER("copper", () -> fission_heater_cooling_rate[15], FissionHeaterEntity.Copper.class),
    TIN("tin", () -> fission_heater_cooling_rate[16], FissionHeaterEntity.Tin.class),
    LEAD("lead", () -> fission_heater_cooling_rate[17], FissionHeaterEntity.Lead.class),
    BORON("boron", () -> fission_heater_cooling_rate[18], FissionHeaterEntity.Boron.class),
    LITHIUM("lithium", () -> fission_heater_cooling_rate[19], FissionHeaterEntity.Lithium.class),
    MAGNESIUM("magnesium", () -> fission_heater_cooling_rate[20], FissionHeaterEntity.Magnesium.class),
    MANGANESE("manganese", () -> fission_heater_cooling_rate[21], FissionHeaterEntity.Manganese.class),
    ALUMINUM("aluminum", () -> fission_heater_cooling_rate[22], FissionHeaterEntity.Aluminum.class),
    SILVER("silver", () -> fission_heater_cooling_rate[23], FissionHeaterEntity.Silver.class),
    FLUORITE("fluorite", () -> fission_heater_cooling_rate[24], FissionHeaterEntity.Fluorite.class),
    VILLIAUMITE("villiaumite", () -> fission_heater_cooling_rate[25], FissionHeaterEntity.Villiaumite.class),
    CAROBBIITE("carobbiite", () -> fission_heater_cooling_rate[26], FissionHeaterEntity.Carobbiite.class),
    ARSENIC("arsenic", () -> fission_heater_cooling_rate[27], FissionHeaterEntity.Arsenic.class),
    LIQUID_NITROGEN("liquid_nitrogen", () -> fission_heater_cooling_rate[28], FissionHeaterEntity.LiquidNitrogen.class),
    LIQUID_HELIUM("liquid_helium", () -> fission_heater_cooling_rate[29], FissionHeaterEntity.LiquidHelium.class),
    ENDERIUM("enderium", () -> fission_heater_cooling_rate[30], FissionHeaterEntity.Enderium.class),
    CRYOTHEUM("cryotheum", () -> fission_heater_cooling_rate[31], FissionHeaterEntity.Cryotheum.class);

    private final String name;
    private final Supplier<Integer> coolingRate;
    private final String _translationKey;
    private final Function<Block.Properties, Block.Properties> _blockPropertiesFixer;
    private final Class<? extends FissionHeaterEntity.Variant> tileClass;

    FissionCoolantHeaterType(String name, Supplier<Integer> coolingRate, Class<? extends FissionHeaterEntity.Variant> tileClass) {
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
    public Class<? extends FissionHeaterEntity.Variant> getTileClass() {
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