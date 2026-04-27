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

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.nred.nuclearcraft.config.NCConfig.fission_heater_cooling_rate;
import static com.nred.nuclearcraft.registration.FluidRegistration.COOLANT_MAP;

public class FissionCoolantHeaterType implements StringRepresentable, IMultiblockVariant {
    public static final HashMap<String, FissionCoolantHeaterType> DEFAULT_HEATER_MAP = new HashMap<>(Stream.of(
            new FissionCoolantHeaterType("standard", 0, () -> fission_heater_cooling_rate[0]),
            new FissionCoolantHeaterType("iron", 1, () -> fission_heater_cooling_rate[1]),
            new FissionCoolantHeaterType("redstone", 2, () -> fission_heater_cooling_rate[2]),
            new FissionCoolantHeaterType("quartz", 3, () -> fission_heater_cooling_rate[3]),
            new FissionCoolantHeaterType("obsidian", 4, () -> fission_heater_cooling_rate[4]),
            new FissionCoolantHeaterType("nether_brick", 5, () -> fission_heater_cooling_rate[5]),
            new FissionCoolantHeaterType("glowstone", 6, () -> fission_heater_cooling_rate[6]),
            new FissionCoolantHeaterType("lapis", 7, () -> fission_heater_cooling_rate[7]),
            new FissionCoolantHeaterType("gold", 8, () -> fission_heater_cooling_rate[8]),
            new FissionCoolantHeaterType("prismarine", 9, () -> fission_heater_cooling_rate[9]),
            new FissionCoolantHeaterType("slime", 10, () -> fission_heater_cooling_rate[10]),
            new FissionCoolantHeaterType("end_stone", 11, () -> fission_heater_cooling_rate[11]),
            new FissionCoolantHeaterType("purpur", 12, () -> fission_heater_cooling_rate[12]),
            new FissionCoolantHeaterType("diamond", 13, () -> fission_heater_cooling_rate[13]),
            new FissionCoolantHeaterType("emerald", 14, () -> fission_heater_cooling_rate[14]),
            new FissionCoolantHeaterType("copper", 15, () -> fission_heater_cooling_rate[15]),
            new FissionCoolantHeaterType("tin", 16, () -> fission_heater_cooling_rate[16]),
            new FissionCoolantHeaterType("lead", 17, () -> fission_heater_cooling_rate[17]),
            new FissionCoolantHeaterType("boron", 18, () -> fission_heater_cooling_rate[18]),
            new FissionCoolantHeaterType("lithium", 19, () -> fission_heater_cooling_rate[19]),
            new FissionCoolantHeaterType("magnesium", 20, () -> fission_heater_cooling_rate[20]),
            new FissionCoolantHeaterType("manganese", 21, () -> fission_heater_cooling_rate[21]),
            new FissionCoolantHeaterType("aluminum", 22, () -> fission_heater_cooling_rate[22]),
            new FissionCoolantHeaterType("silver", 23, () -> fission_heater_cooling_rate[23]),
            new FissionCoolantHeaterType("fluorite", 24, () -> fission_heater_cooling_rate[24]),
            new FissionCoolantHeaterType("villiaumite", 25, () -> fission_heater_cooling_rate[25]),
            new FissionCoolantHeaterType("carobbiite", 26, () -> fission_heater_cooling_rate[26]),
            new FissionCoolantHeaterType("arsenic", 27, () -> fission_heater_cooling_rate[27]),
            new FissionCoolantHeaterType("liquid_nitrogen", 28, () -> fission_heater_cooling_rate[28]),
            new FissionCoolantHeaterType("liquid_helium", 29, () -> fission_heater_cooling_rate[29]),
            new FissionCoolantHeaterType("enderium", 30, () -> fission_heater_cooling_rate[30]),
            new FissionCoolantHeaterType("cryotheum", 31, () -> fission_heater_cooling_rate[31])
    ).collect(Collectors.toMap(FissionCoolantHeaterType::getName, e -> e)));
    public static final ConcurrentHashMap<String, FissionCoolantHeaterType> ADDED_HEATER_MAP = new ConcurrentHashMap<>();
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
        ADDED_HEATER_MAP.put(name, this);
    }

    public static List<FissionCoolantHeaterType> default_values() {
        return DEFAULT_HEATER_MAP.values().stream().toList();
    }

    public static FissionCoolantHeaterType getType(String name) {
        if (name.contains(":")) {
            return ADDED_HEATER_MAP.get(name);
        } else {
            return DEFAULT_HEATER_MAP.get(name.replace("_heater", ""));
        }
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