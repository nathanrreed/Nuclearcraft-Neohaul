package com.nred.nuclearcraft.multiblock.fisson.molten_salt;

import com.nred.nuclearcraft.block_entity.fission.AbstractFissionEntity;
import com.nred.nuclearcraft.multiblock.PlacementRule;
import com.nred.nuclearcraft.multiblock.fisson.FissionPlacement;
import com.nred.nuclearcraft.multiblock.fisson.FissionReactor;
import it.zerono.mods.zerocore.lib.CodeHelper;
import it.zerono.mods.zerocore.lib.multiblock.variant.IMultiblockVariant;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.nred.nuclearcraft.config.NCConfig.fission_heater_cooling_rate;
import static com.nred.nuclearcraft.registration.FluidRegistration.COOLANT_MAP;

public class FissionCoolantHeaterType implements StringRepresentable, IMultiblockVariant {
    public static final FissionCoolantHeaterType STANDARD = new FissionCoolantHeaterType("standard", 0, () -> fission_heater_cooling_rate[0]);
    public static final FissionCoolantHeaterType IRON = new FissionCoolantHeaterType("iron", 1, () -> fission_heater_cooling_rate[1]);
    public static final FissionCoolantHeaterType REDSTONE = new FissionCoolantHeaterType("redstone", 2, () -> fission_heater_cooling_rate[2]);
    public static final FissionCoolantHeaterType QUARTZ = new FissionCoolantHeaterType("quartz", 3, () -> fission_heater_cooling_rate[3]);
    public static final FissionCoolantHeaterType OBSIDIAN = new FissionCoolantHeaterType("obsidian", 4, () -> fission_heater_cooling_rate[4]);
    public static final FissionCoolantHeaterType NETHER_BRICK = new FissionCoolantHeaterType("nether_brick", 5, () -> fission_heater_cooling_rate[5]);
    public static final FissionCoolantHeaterType GLOWSTONE = new FissionCoolantHeaterType("glowstone", 6, () -> fission_heater_cooling_rate[6]);
    public static final FissionCoolantHeaterType LAPIS = new FissionCoolantHeaterType("lapis", 7, () -> fission_heater_cooling_rate[7]);
    public static final FissionCoolantHeaterType GOLD = new FissionCoolantHeaterType("gold", 8, () -> fission_heater_cooling_rate[8]);
    public static final FissionCoolantHeaterType PRISMARINE = new FissionCoolantHeaterType("prismarine", 9, () -> fission_heater_cooling_rate[9]);
    public static final FissionCoolantHeaterType SLIME = new FissionCoolantHeaterType("slime", 10, () -> fission_heater_cooling_rate[10]);
    public static final FissionCoolantHeaterType END_STONE = new FissionCoolantHeaterType("end_stone", 11, () -> fission_heater_cooling_rate[11]);
    public static final FissionCoolantHeaterType PURPUR = new FissionCoolantHeaterType("purpur", 12, () -> fission_heater_cooling_rate[12]);
    public static final FissionCoolantHeaterType DIAMOND = new FissionCoolantHeaterType("diamond", 13, () -> fission_heater_cooling_rate[13]);
    public static final FissionCoolantHeaterType EMERALD = new FissionCoolantHeaterType("emerald", 14, () -> fission_heater_cooling_rate[14]);
    public static final FissionCoolantHeaterType COPPER = new FissionCoolantHeaterType("copper", 15, () -> fission_heater_cooling_rate[15]);
    public static final FissionCoolantHeaterType TIN = new FissionCoolantHeaterType("tin", 16, () -> fission_heater_cooling_rate[16]);
    public static final FissionCoolantHeaterType LEAD = new FissionCoolantHeaterType("lead", 17, () -> fission_heater_cooling_rate[17]);
    public static final FissionCoolantHeaterType BORON = new FissionCoolantHeaterType("boron", 18, () -> fission_heater_cooling_rate[18]);
    public static final FissionCoolantHeaterType LITHIUM = new FissionCoolantHeaterType("lithium", 19, () -> fission_heater_cooling_rate[19]);
    public static final FissionCoolantHeaterType MAGNESIUM = new FissionCoolantHeaterType("magnesium", 20, () -> fission_heater_cooling_rate[20]);
    public static final FissionCoolantHeaterType MANGANESE = new FissionCoolantHeaterType("manganese", 21, () -> fission_heater_cooling_rate[21]);
    public static final FissionCoolantHeaterType ALUMINUM = new FissionCoolantHeaterType("aluminum", 22, () -> fission_heater_cooling_rate[22]);
    public static final FissionCoolantHeaterType SILVER = new FissionCoolantHeaterType("silver", 23, () -> fission_heater_cooling_rate[23]);
    public static final FissionCoolantHeaterType FLUORITE = new FissionCoolantHeaterType("fluorite", 24, () -> fission_heater_cooling_rate[24]);
    public static final FissionCoolantHeaterType VILLIAUMITE = new FissionCoolantHeaterType("villiaumite", 25, () -> fission_heater_cooling_rate[25]);
    public static final FissionCoolantHeaterType CAROBBIITE = new FissionCoolantHeaterType("carobbiite", 26, () -> fission_heater_cooling_rate[26]);
    public static final FissionCoolantHeaterType ARSENIC = new FissionCoolantHeaterType("arsenic", 27, () -> fission_heater_cooling_rate[27]);
    public static final FissionCoolantHeaterType LIQUID_NITROGEN = new FissionCoolantHeaterType("liquid_nitrogen", 28, () -> fission_heater_cooling_rate[28]);
    public static final FissionCoolantHeaterType LIQUID_HELIUM = new FissionCoolantHeaterType("liquid_helium", 29, () -> fission_heater_cooling_rate[29]);
    public static final FissionCoolantHeaterType ENDERIUM = new FissionCoolantHeaterType("enderium", 30, () -> fission_heater_cooling_rate[30]);
    public static final FissionCoolantHeaterType CRYOTHEUM = new FissionCoolantHeaterType("cryotheum", 31, () -> fission_heater_cooling_rate[31]);
    private static final AtomicInteger _id = new AtomicInteger(31); // Used to increment KubeJS additions

    private final String name;
    private final int id;
    private ResourceLocation coolantId;
    private final Supplier<Integer> coolingRate; // TODO remove?
    private final String _translationKey;
    private final Function<Block.Properties, Block.Properties> _blockPropertiesFixer;

    private FissionCoolantHeaterType(String name, int id, Supplier<Integer> coolingRate) {
        this.name = name;
        this.id = id;
        this.coolingRate = coolingRate;

        this._translationKey = ""; // TODO ADD
        this._blockPropertiesFixer = null;
    }

    public FissionCoolantHeaterType(String name, Supplier<Integer> coolingRate, ResourceLocation fluid_id) { // Used by KubeJS
        this(name, _id.incrementAndGet(), coolingRate);
        this.coolantId = fluid_id;
    }

    public static List<FissionCoolantHeaterType> default_values() {
        return List.of(STANDARD, IRON, REDSTONE, QUARTZ, OBSIDIAN, NETHER_BRICK, GLOWSTONE, LAPIS, GOLD, PRISMARINE, SLIME, END_STONE, PURPUR, DIAMOND, EMERALD, COPPER, TIN, LEAD, BORON, LITHIUM, MAGNESIUM, MANGANESE, ALUMINUM, SILVER, FLUORITE, VILLIAUMITE, CAROBBIITE, ARSENIC, LIQUID_NITROGEN, LIQUID_HELIUM, ENDERIUM, CRYOTHEUM);
    }

    public PlacementRule<FissionReactor, AbstractFissionEntity> getRule() {
        return FissionPlacement.RULE_MAP.get(this.name + (this.name.contains(":") ? "" : "_heater"));
    }

    public String getTooltipRule() {
        return FissionPlacement.TOOLTIP_MAP.get(this.name + (this.name.contains(":") ? "" : "_heater"));
    }

    public ResourceLocation getCoolantId() {
        return coolantId;
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
        if (coolantId == null) {
            if (this.id == 0) {
                coolantId = BuiltInRegistries.FLUID.getKey(COOLANT_MAP.get("nak").still.value());
            } else if (!name.contains(":")) {
                coolantId = BuiltInRegistries.FLUID.getKey(COOLANT_MAP.get(getName() + "_nak").still.value());
            }
        }
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