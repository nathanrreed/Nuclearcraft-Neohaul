package com.nred.nuclearcraft.multiblock.fisson.molten_salt;

import com.nred.nuclearcraft.block_entity.fission.port.FissionHeaterPortEntity;
import com.nred.nuclearcraft.enumm.ITileEnum;
import it.zerono.mods.zerocore.lib.CodeHelper;
import it.zerono.mods.zerocore.lib.multiblock.variant.IMultiblockVariant;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.function.Function;

public enum FissionCoolantHeaterPortType implements StringRepresentable, ITileEnum<FissionHeaterPortEntity.Variant>, IMultiblockVariant, ICoolantType {
    STANDARD("standard", FissionHeaterPortEntity.Standard.class),
    IRON("iron", FissionHeaterPortEntity.Iron.class),
    REDSTONE("redstone", FissionHeaterPortEntity.Redstone.class),
    QUARTZ("quartz", FissionHeaterPortEntity.Quartz.class),
    OBSIDIAN("obsidian", FissionHeaterPortEntity.Obsidian.class),
    NETHER_BRICK("nether_brick", FissionHeaterPortEntity.NetherBrick.class),
    GLOWSTONE("glowstone", FissionHeaterPortEntity.Glowstone.class),
    LAPIS("lapis", FissionHeaterPortEntity.Lapis.class),
    GOLD("gold", FissionHeaterPortEntity.Gold.class),
    PRISMARINE("prismarine", FissionHeaterPortEntity.Prismarine.class),
    SLIME("slime", FissionHeaterPortEntity.Slime.class),
    END_STONE("end_stone", FissionHeaterPortEntity.EndStone.class),
    PURPUR("purpur", FissionHeaterPortEntity.Purpur.class),
    DIAMOND("diamond", FissionHeaterPortEntity.Diamond.class),
    EMERALD("emerald", FissionHeaterPortEntity.Emerald.class),
    COPPER("copper", FissionHeaterPortEntity.Copper.class),
    TIN("tin", FissionHeaterPortEntity.Tin.class),
    LEAD("lead", FissionHeaterPortEntity.Lead.class),
    BORON("boron", FissionHeaterPortEntity.Boron.class),
    LITHIUM("lithium", FissionHeaterPortEntity.Lithium.class),
    MAGNESIUM("magnesium", FissionHeaterPortEntity.Magnesium.class),
    MANGANESE("manganese", FissionHeaterPortEntity.Manganese.class),
    ALUMINUM("aluminum", FissionHeaterPortEntity.Aluminum.class),
    SILVER("silver", FissionHeaterPortEntity.Silver.class),
    FLUORITE("fluorite", FissionHeaterPortEntity.Fluorite.class),
    VILLIAUMITE("villiaumite", FissionHeaterPortEntity.Villiaumite.class),
    CAROBBIITE("carobbiite", FissionHeaterPortEntity.Carobbiite.class),
    ARSENIC("arsenic", FissionHeaterPortEntity.Arsenic.class),
    LIQUID_NITROGEN("liquid_nitrogen", FissionHeaterPortEntity.LiquidNitrogen.class),
    LIQUID_HELIUM("liquid_helium", FissionHeaterPortEntity.LiquidHelium.class),
    ENDERIUM("enderium", FissionHeaterPortEntity.Enderium.class),
    CRYOTHEUM("cryotheum", FissionHeaterPortEntity.Cryotheum.class);

    private final String name;
    private final String _translationKey;
    private final Function<Block.Properties, Block.Properties> _blockPropertiesFixer;
    private final Class<? extends FissionHeaterPortEntity.Variant> tileClass;

    FissionCoolantHeaterPortType(String name, Class<? extends FissionHeaterPortEntity.Variant> tileClass) {
        this.name = name;
        this.tileClass = tileClass;

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
    public Class<? extends FissionHeaterPortEntity.Variant> getTileClass() {
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