package com.nred.nuclearcraft.multiblock.fisson.pebble;

import com.nred.nuclearcraft.block_entity.fission.AbstractFissionEntity;
import com.nred.nuclearcraft.multiblock.PlacementRule;
import com.nred.nuclearcraft.multiblock.fisson.FissionPlacement;
import com.nred.nuclearcraft.multiblock.fisson.FissionReactor;
import it.zerono.mods.zerocore.lib.CodeHelper;
import it.zerono.mods.zerocore.lib.multiblock.variant.IMultiblockVariant;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.fluids.crafting.FluidIngredient;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.nred.nuclearcraft.config.NCConfig.fission_cooler_cooling_rate;
import static com.nred.nuclearcraft.registration.FluidRegistration.GAS_MAP;

public class FissionCoolerType implements StringRepresentable, IMultiblockVariant {
    public static final HashMap<String, FissionCoolerType> DEFAULT_COOLER_MAP = new HashMap<>(Stream.of(
            new FissionCoolerType("oxygen", 0, () -> fission_cooler_cooling_rate[0]),
            new FissionCoolerType("hydrogen", 1, () -> fission_cooler_cooling_rate[1]),
            new FissionCoolerType("helium", 2, () -> fission_cooler_cooling_rate[2]),
            new FissionCoolerType("nitrogen", 3, () -> fission_cooler_cooling_rate[3]),
            new FissionCoolerType("fluorine", 4, () -> fission_cooler_cooling_rate[4]),
            new FissionCoolerType("methane", 5, () -> fission_cooler_cooling_rate[5]),
            new FissionCoolerType("carbon_dioxide", 6, () -> fission_cooler_cooling_rate[6]),
            new FissionCoolerType("carbon_monoxide", 7, () -> fission_cooler_cooling_rate[7]),
            new FissionCoolerType("ethene", 8, () -> fission_cooler_cooling_rate[8]),
            new FissionCoolerType("ethyne", 9, () -> fission_cooler_cooling_rate[9]),
            new FissionCoolerType("fluoromethane", 10, () -> fission_cooler_cooling_rate[10]),
            new FissionCoolerType("ammonia", 11, () -> fission_cooler_cooling_rate[11]),
            new FissionCoolerType("diborane", 12, () -> fission_cooler_cooling_rate[12]),
            new FissionCoolerType("sulfur_dioxide", 13, () -> fission_cooler_cooling_rate[13]),
            new FissionCoolerType("sulfur_trioxide", 14, () -> fission_cooler_cooling_rate[14]),
            new FissionCoolerType("sulfur_hexafluoride", 15, () -> fission_cooler_cooling_rate[15])

    ).collect(Collectors.toMap(FissionCoolerType::getName, e -> e)));
    public static final ConcurrentHashMap<String, FissionCoolerType> ADDED_COOLER_MAP = new ConcurrentHashMap<>();
    private static final AtomicInteger _id = new AtomicInteger(15); // Used to increment KubeJS additions

    private final String name;
    private final int id;
    private ResourceLocation coolantId;
    private final Supplier<Integer> coolingRate; // TODO remove?
    private final String _translationKey;
    private final Function<Block.Properties, Block.Properties> _blockPropertiesFixer;

    private FissionCoolerType(String name, int id, Supplier<Integer> coolingRate) {
        this.name = name;
        this.id = id;
        this.coolingRate = coolingRate;

        this._translationKey = ""; // TODO ADD
        this._blockPropertiesFixer = null;
    }

    public FissionCoolerType(String name, Supplier<Integer> coolingRate, ResourceLocation fluid_id) { // Used by KubeJS
        this(name, _id.incrementAndGet(), coolingRate);
        this.coolantId = fluid_id;
        ADDED_COOLER_MAP.put(name, this);
    }

    public static List<FissionCoolerType> default_values() {
        return DEFAULT_COOLER_MAP.values().stream().toList();
    }

    public static FissionCoolerType getType(String name) {
        if (name.contains(":")) {
            return ADDED_COOLER_MAP.get(name);
        } else {
            return DEFAULT_COOLER_MAP.get(name.replace("_cooler", ""));
        }
    }

    public PlacementRule<FissionReactor, AbstractFissionEntity> getRule() {
        return FissionPlacement.RULE_MAP.get(this.name + (this.name.contains(":") ? "" : "_cooler"));
    }

    public String getTooltipRule() {
        return FissionPlacement.TOOLTIP_MAP.get(this.name + (this.name.contains(":") ? "" : "_cooler"));
    }

    public Set<ResourceLocation> validFluids() {
        if (coolantId == null) {
            if (!name.contains(":")) {
                return Stream.concat(Stream.of(BuiltInRegistries.FLUID.getKey(GAS_MAP.get(getName()).still.value())), Arrays.stream(FluidIngredient.tag(FluidTags.create(ResourceLocation.fromNamespaceAndPath("c", getName()))).getStacks()).map(e -> BuiltInRegistries.FLUID.getKey(e.getFluid()))).collect(Collectors.toSet());
            }
        }
        return Collections.singleton(coolantId);
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