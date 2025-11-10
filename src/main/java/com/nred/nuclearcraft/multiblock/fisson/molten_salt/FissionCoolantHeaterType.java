package com.nred.nuclearcraft.multiblock.fisson.molten_salt;

import it.zerono.mods.zerocore.lib.CodeHelper;
import it.zerono.mods.zerocore.lib.multiblock.variant.IMultiblockVariant;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.function.Function;
import java.util.function.Supplier;

import static com.nred.nuclearcraft.config.NCConfig.fission_heater_cooling_rate;

public enum FissionCoolantHeaterType implements StringRepresentable, IMultiblockVariant, ICoolantType {
    STANDARD("standard", () -> fission_heater_cooling_rate[0]),
    IRON("iron", () -> fission_heater_cooling_rate[1]),
    REDSTONE("redstone", () -> fission_heater_cooling_rate[2]),
    QUARTZ("quartz", () -> fission_heater_cooling_rate[3]),
    OBSIDIAN("obsidian", () -> fission_heater_cooling_rate[4]),
    NETHER_BRICK("nether_brick", () -> fission_heater_cooling_rate[5]),
    GLOWSTONE("glowstone", () -> fission_heater_cooling_rate[6]),
    LAPIS("lapis", () -> fission_heater_cooling_rate[7]),
    GOLD("gold", () -> fission_heater_cooling_rate[8]),
    PRISMARINE("prismarine", () -> fission_heater_cooling_rate[9]),
    SLIME("slime", () -> fission_heater_cooling_rate[10]),
    END_STONE("end_stone", () -> fission_heater_cooling_rate[11]),
    PURPUR("purpur", () -> fission_heater_cooling_rate[12]),
    DIAMOND("diamond", () -> fission_heater_cooling_rate[13]),
    EMERALD("emerald", () -> fission_heater_cooling_rate[14]),
    COPPER("copper", () -> fission_heater_cooling_rate[15]),
    TIN("tin", () -> fission_heater_cooling_rate[16]),
    LEAD("lead", () -> fission_heater_cooling_rate[17]),
    BORON("boron", () -> fission_heater_cooling_rate[18]),
    LITHIUM("lithium", () -> fission_heater_cooling_rate[19]),
    MAGNESIUM("magnesium", () -> fission_heater_cooling_rate[20]),
    MANGANESE("manganese", () -> fission_heater_cooling_rate[21]),
    ALUMINUM("aluminum", () -> fission_heater_cooling_rate[22]),
    SILVER("silver", () -> fission_heater_cooling_rate[23]),
    FLUORITE("fluorite", () -> fission_heater_cooling_rate[24]),
    VILLIAUMITE("villiaumite", () -> fission_heater_cooling_rate[25]),
    CAROBBIITE("carobbiite", () -> fission_heater_cooling_rate[26]),
    ARSENIC("arsenic", () -> fission_heater_cooling_rate[27]),
    LIQUID_NITROGEN("liquid_nitrogen", () -> fission_heater_cooling_rate[28]),
    LIQUID_HELIUM("liquid_helium", () -> fission_heater_cooling_rate[29]),
    ENDERIUM("enderium", () -> fission_heater_cooling_rate[30]),
    CRYOTHEUM("cryotheum", () -> fission_heater_cooling_rate[31]);

    private final String name;
    private final Supplier<Integer> coolingRate; // TODO remove?
    private final String _translationKey;
    private final Function<Block.Properties, Block.Properties> _blockPropertiesFixer;

    FissionCoolantHeaterType(String name, Supplier<Integer> coolingRate) {
        this.name = name;
        this.coolingRate = coolingRate;

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