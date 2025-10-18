package com.nred.nuclearcraft.multiblock.fisson.molten_salt;

import com.nred.nuclearcraft.block_entity.fission.SaltFissionHeaterEntity;
import com.nred.nuclearcraft.enumm.ITileEnum;
import it.zerono.mods.zerocore.lib.CodeHelper;
import it.zerono.mods.zerocore.lib.multiblock.variant.IMultiblockVariant;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.function.Function;
import java.util.function.Supplier;

import static com.nred.nuclearcraft.config.Config2.fission_heater_cooling_rate;

public enum FissionCoolantHeaterType implements StringRepresentable, ITileEnum<SaltFissionHeaterEntity.Variant>, IMultiblockVariant, ICoolantType {
    STANDARD("standard", () -> fission_heater_cooling_rate[0], SaltFissionHeaterEntity.Standard.class),
    IRON("iron", () -> fission_heater_cooling_rate[1], SaltFissionHeaterEntity.Iron.class),
    REDSTONE("redstone", () -> fission_heater_cooling_rate[2], SaltFissionHeaterEntity.Redstone.class),
    QUARTZ("quartz", () -> fission_heater_cooling_rate[3], SaltFissionHeaterEntity.Quartz.class),
    OBSIDIAN("obsidian", () -> fission_heater_cooling_rate[4], SaltFissionHeaterEntity.Obsidian.class),
    NETHER_BRICK("nether_brick", () -> fission_heater_cooling_rate[5], SaltFissionHeaterEntity.NetherBrick.class),
    GLOWSTONE("glowstone", () -> fission_heater_cooling_rate[6], SaltFissionHeaterEntity.Glowstone.class),
    LAPIS("lapis", () -> fission_heater_cooling_rate[7], SaltFissionHeaterEntity.Lapis.class),
    GOLD("gold", () -> fission_heater_cooling_rate[8], SaltFissionHeaterEntity.Gold.class),
    PRISMARINE("prismarine", () -> fission_heater_cooling_rate[9], SaltFissionHeaterEntity.Prismarine.class),
    SLIME("slime", () -> fission_heater_cooling_rate[10], SaltFissionHeaterEntity.Slime.class),
    END_STONE("end_stone", () -> fission_heater_cooling_rate[11], SaltFissionHeaterEntity.EndStone.class),
    PURPUR("purpur", () -> fission_heater_cooling_rate[12], SaltFissionHeaterEntity.Purpur.class),
    DIAMOND("diamond", () -> fission_heater_cooling_rate[13], SaltFissionHeaterEntity.Diamond.class),
    EMERALD("emerald", () -> fission_heater_cooling_rate[14], SaltFissionHeaterEntity.Emerald.class),
    COPPER("copper", () -> fission_heater_cooling_rate[15], SaltFissionHeaterEntity.Copper.class),
    TIN("tin", () -> fission_heater_cooling_rate[16], SaltFissionHeaterEntity.Tin.class),
    LEAD("lead", () -> fission_heater_cooling_rate[17], SaltFissionHeaterEntity.Lead.class),
    BORON("boron", () -> fission_heater_cooling_rate[18], SaltFissionHeaterEntity.Boron.class),
    LITHIUM("lithium", () -> fission_heater_cooling_rate[19], SaltFissionHeaterEntity.Lithium.class),
    MAGNESIUM("magnesium", () -> fission_heater_cooling_rate[20], SaltFissionHeaterEntity.Magnesium.class),
    MANGANESE("manganese", () -> fission_heater_cooling_rate[21], SaltFissionHeaterEntity.Manganese.class),
    ALUMINUM("aluminum", () -> fission_heater_cooling_rate[22], SaltFissionHeaterEntity.Aluminum.class),
    SILVER("silver", () -> fission_heater_cooling_rate[23], SaltFissionHeaterEntity.Silver.class),
    FLUORITE("fluorite", () -> fission_heater_cooling_rate[24], SaltFissionHeaterEntity.Fluorite.class),
    VILLIAUMITE("villiaumite", () -> fission_heater_cooling_rate[25], SaltFissionHeaterEntity.Villiaumite.class),
    CAROBBIITE("carobbiite", () -> fission_heater_cooling_rate[26], SaltFissionHeaterEntity.Carobbiite.class),
    ARSENIC("arsenic", () -> fission_heater_cooling_rate[27], SaltFissionHeaterEntity.Arsenic.class),
    LIQUID_NITROGEN("liquid_nitrogen", () -> fission_heater_cooling_rate[28], SaltFissionHeaterEntity.LiquidNitrogen.class),
    LIQUID_HELIUM("liquid_helium", () -> fission_heater_cooling_rate[29], SaltFissionHeaterEntity.LiquidHelium.class),
    ENDERIUM("enderium", () -> fission_heater_cooling_rate[30], SaltFissionHeaterEntity.Enderium.class),
    CRYOTHEUM("cryotheum", () -> fission_heater_cooling_rate[31], SaltFissionHeaterEntity.Cryotheum.class);

    private final String name;
    private final Supplier<Integer> coolingRate; // TODO remove?
    private final String _translationKey;
    private final Function<Block.Properties, Block.Properties> _blockPropertiesFixer;
    private final Class<? extends SaltFissionHeaterEntity.Variant> tileClass;

    FissionCoolantHeaterType(String name, Supplier<Integer> coolingRate, Class<? extends SaltFissionHeaterEntity.Variant> tileClass) {
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
    public Class<? extends SaltFissionHeaterEntity.Variant> getTileClass() {
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