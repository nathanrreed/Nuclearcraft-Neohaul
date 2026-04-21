package com.nred.nuclearcraft.multiblock.fisson.solid;

import com.nred.nuclearcraft.block_entity.fission.AbstractFissionEntity;
import com.nred.nuclearcraft.multiblock.PlacementRule;
import com.nred.nuclearcraft.multiblock.fisson.FissionPlacement;
import com.nred.nuclearcraft.multiblock.fisson.FissionReactor;
import it.zerono.mods.zerocore.lib.CodeHelper;
import it.zerono.mods.zerocore.lib.multiblock.variant.IMultiblockVariant;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.nred.nuclearcraft.config.NCConfig.fission_sink_cooling_rate;

public class FissionHeatSinkType implements StringRepresentable, IMultiblockVariant {
    public static final FissionHeatSinkType WATER = new FissionHeatSinkType("water", 0, () -> fission_sink_cooling_rate[0]);
    public static final FissionHeatSinkType IRON = new FissionHeatSinkType("iron", 1, () -> fission_sink_cooling_rate[1]);
    public static final FissionHeatSinkType REDSTONE = new FissionHeatSinkType("redstone", 2, () -> fission_sink_cooling_rate[2]);
    public static final FissionHeatSinkType QUARTZ = new FissionHeatSinkType("quartz", 3, () -> fission_sink_cooling_rate[3]);
    public static final FissionHeatSinkType OBSIDIAN = new FissionHeatSinkType("obsidian", 4, () -> fission_sink_cooling_rate[4]);
    public static final FissionHeatSinkType NETHER_BRICK = new FissionHeatSinkType("nether_brick", 5, () -> fission_sink_cooling_rate[5]);
    public static final FissionHeatSinkType GLOWSTONE = new FissionHeatSinkType("glowstone", 6, () -> fission_sink_cooling_rate[6]);
    public static final FissionHeatSinkType LAPIS = new FissionHeatSinkType("lapis", 7, () -> fission_sink_cooling_rate[7]);
    public static final FissionHeatSinkType GOLD = new FissionHeatSinkType("gold", 8, () -> fission_sink_cooling_rate[8]);
    public static final FissionHeatSinkType PRISMARINE = new FissionHeatSinkType("prismarine", 9, () -> fission_sink_cooling_rate[9]);
    public static final FissionHeatSinkType SLIME = new FissionHeatSinkType("slime", 10, () -> fission_sink_cooling_rate[10]);
    public static final FissionHeatSinkType END_STONE = new FissionHeatSinkType("end_stone", 11, () -> fission_sink_cooling_rate[11]);
    public static final FissionHeatSinkType PURPUR = new FissionHeatSinkType("purpur", 12, () -> fission_sink_cooling_rate[12]);
    public static final FissionHeatSinkType DIAMOND = new FissionHeatSinkType("diamond", 13, () -> fission_sink_cooling_rate[13]);
    public static final FissionHeatSinkType EMERALD = new FissionHeatSinkType("emerald", 14, () -> fission_sink_cooling_rate[14]);
    public static final FissionHeatSinkType COPPER = new FissionHeatSinkType("copper", 15, () -> fission_sink_cooling_rate[15]);
    public static final FissionHeatSinkType TIN = new FissionHeatSinkType("tin", 16, () -> fission_sink_cooling_rate[16]);
    public static final FissionHeatSinkType LEAD = new FissionHeatSinkType("lead", 17, () -> fission_sink_cooling_rate[17]);
    public static final FissionHeatSinkType BORON = new FissionHeatSinkType("boron", 18, () -> fission_sink_cooling_rate[18]);
    public static final FissionHeatSinkType LITHIUM = new FissionHeatSinkType("lithium", 19, () -> fission_sink_cooling_rate[19]);
    public static final FissionHeatSinkType MAGNESIUM = new FissionHeatSinkType("magnesium", 20, () -> fission_sink_cooling_rate[20]);
    public static final FissionHeatSinkType MANGANESE = new FissionHeatSinkType("manganese", 21, () -> fission_sink_cooling_rate[21]);
    public static final FissionHeatSinkType ALUMINUM = new FissionHeatSinkType("aluminum", 22, () -> fission_sink_cooling_rate[22]);
    public static final FissionHeatSinkType SILVER = new FissionHeatSinkType("silver", 23, () -> fission_sink_cooling_rate[23]);
    public static final FissionHeatSinkType FLUORITE = new FissionHeatSinkType("fluorite", 24, () -> fission_sink_cooling_rate[24]);
    public static final FissionHeatSinkType VILLIAUMITE = new FissionHeatSinkType("villiaumite", 25, () -> fission_sink_cooling_rate[25]);
    public static final FissionHeatSinkType CAROBBIITE = new FissionHeatSinkType("carobbiite", 26, () -> fission_sink_cooling_rate[26]);
    public static final FissionHeatSinkType ARSENIC = new FissionHeatSinkType("arsenic", 27, () -> fission_sink_cooling_rate[27]);
    public static final FissionHeatSinkType LIQUID_NITROGEN = new FissionHeatSinkType("liquid_nitrogen", 28, () -> fission_sink_cooling_rate[28]);
    public static final FissionHeatSinkType LIQUID_HELIUM = new FissionHeatSinkType("liquid_helium", 29, () -> fission_sink_cooling_rate[29]);
    public static final FissionHeatSinkType ENDERIUM = new FissionHeatSinkType("enderium", 30, () -> fission_sink_cooling_rate[30]);
    public static final FissionHeatSinkType CRYOTHEUM = new FissionHeatSinkType("cryotheum", 31, () -> fission_sink_cooling_rate[31]);
    private static final AtomicInteger _id = new AtomicInteger(31); // Used to increment KubeJS additions

    private final String name;
    private final int id;
    private final Supplier<Integer> coolingRate;
    private final String _translationKey;
    private final Function<Block.Properties, Block.Properties> _blockPropertiesFixer;

    private FissionHeatSinkType(String name, int id, Supplier<Integer> coolingRate) {
        this.name = name;
        this.id = id;
        this.coolingRate = coolingRate;

        this._translationKey = ""; // TODO ADD
        this._blockPropertiesFixer = null;
    }

    public FissionHeatSinkType(String name, Supplier<Integer> coolingRate) { // Used by KubeJS
        this(name, _id.incrementAndGet(), coolingRate);
    }

    public PlacementRule<FissionReactor, AbstractFissionEntity> getRule() {
        return FissionPlacement.RULE_MAP.get(this.name + (this.name.contains(":") ? "" : "_sink"));
    }

    public String getTooltipRule() {
        return FissionPlacement.TOOLTIP_MAP.get(this.name + (this.name.contains(":") ? "" : "_sink"));
    }

    public static List<FissionHeatSinkType> default_values() {
        return List.of(WATER, IRON, REDSTONE, QUARTZ, OBSIDIAN, NETHER_BRICK, GLOWSTONE, LAPIS, GOLD, PRISMARINE, SLIME, END_STONE, PURPUR, DIAMOND, EMERALD, COPPER, TIN, LEAD, BORON, LITHIUM, MAGNESIUM, MANGANESE, ALUMINUM, SILVER, FLUORITE, VILLIAUMITE, CAROBBIITE, ARSENIC, LIQUID_NITROGEN, LIQUID_HELIUM, ENDERIUM, CRYOTHEUM);
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
    public int getId() {
        return this.id;
    }

    @Override
    public String getName() {
        return CodeHelper.neutralLowercase(this.name);
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