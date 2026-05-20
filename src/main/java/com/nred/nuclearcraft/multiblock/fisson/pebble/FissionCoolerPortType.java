package com.nred.nuclearcraft.multiblock.fisson.pebble;

import it.zerono.mods.zerocore.lib.CodeHelper;
import it.zerono.mods.zerocore.lib.multiblock.variant.IMultiblockVariant;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.fluids.crafting.FluidIngredient;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.nred.nuclearcraft.registration.FluidRegistration.GAS_MAP;

public class FissionCoolerPortType implements StringRepresentable, IMultiblockVariant {
    public static final FissionCoolerPortType OXYGEN = new FissionCoolerPortType("oxygen", 0);
    public static final FissionCoolerPortType HYDROGEN = new FissionCoolerPortType("hydrogen", 1);
    public static final FissionCoolerPortType HELIUM = new FissionCoolerPortType("helium", 2);
    public static final FissionCoolerPortType NITROGEN = new FissionCoolerPortType("nitrogen", 3);
    public static final FissionCoolerPortType FLUORINE = new FissionCoolerPortType("fluorine", 4);
    public static final FissionCoolerPortType METHANE = new FissionCoolerPortType("methane", 5);
    public static final FissionCoolerPortType CARBON_DIOXIDE = new FissionCoolerPortType("carbon_dioxide", 6);
    public static final FissionCoolerPortType CARBON_MONOXIDE = new FissionCoolerPortType("carbon_monoxide", 7);
    public static final FissionCoolerPortType ETHENE = new FissionCoolerPortType("ethene", 8);
    public static final FissionCoolerPortType ETHYNE = new FissionCoolerPortType("ethyne", 9);
    public static final FissionCoolerPortType FLUOROMETHANE = new FissionCoolerPortType("fluoromethane", 10);
    public static final FissionCoolerPortType AMMONIA = new FissionCoolerPortType("ammonia", 11);
    public static final FissionCoolerPortType DIBORANE = new FissionCoolerPortType("diborane", 12);
    public static final FissionCoolerPortType SULFUR_DIOXIDE = new FissionCoolerPortType("sulfur_dioxide", 13);
    public static final FissionCoolerPortType SULFUR_TRIOXIDE = new FissionCoolerPortType("sulfur_trioxide", 14);
    public static final FissionCoolerPortType SULFUR_HEXAFLUORIDE = new FissionCoolerPortType("sulfur_hexafluoride", 15);
    private static final AtomicInteger _id = new AtomicInteger(15); // Used to increment KubeJS additions

    private final String name;
    private final int id;
    private ResourceLocation coolantId;
    private final String _translationKey;
    private final Function<Block.Properties, Block.Properties> _blockPropertiesFixer;

    private FissionCoolerPortType(String name, int id) {
        this.name = name;
        this.id = id;

        this._translationKey = ""; // TODO ADD
        this._blockPropertiesFixer = null;
    }

    public FissionCoolerPortType(String name, ResourceLocation fluid_id) { // Used by KubeJS
        this(name, _id.incrementAndGet());
        this.coolantId = fluid_id;
    }

    public static List<FissionCoolerPortType> default_values() {
        return List.of(OXYGEN, HYDROGEN, HELIUM, NITROGEN, FLUORINE, METHANE, CARBON_DIOXIDE, CARBON_MONOXIDE, ETHENE, ETHYNE, FLUOROMETHANE, AMMONIA, DIBORANE, SULFUR_DIOXIDE, SULFUR_TRIOXIDE, SULFUR_HEXAFLUORIDE);
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