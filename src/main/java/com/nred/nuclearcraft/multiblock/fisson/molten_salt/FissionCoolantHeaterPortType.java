package com.nred.nuclearcraft.multiblock.fisson.molten_salt;

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

import static com.nred.nuclearcraft.registration.FluidRegistration.COOLANT_MAP;

public class FissionCoolantHeaterPortType implements StringRepresentable, IMultiblockVariant {
    public static final FissionCoolantHeaterPortType STANDARD = new FissionCoolantHeaterPortType("standard", 0);
    public static final FissionCoolantHeaterPortType IRON = new FissionCoolantHeaterPortType("iron", 1);
    public static final FissionCoolantHeaterPortType REDSTONE = new FissionCoolantHeaterPortType("redstone", 2);
    public static final FissionCoolantHeaterPortType QUARTZ = new FissionCoolantHeaterPortType("quartz", 3);
    public static final FissionCoolantHeaterPortType OBSIDIAN = new FissionCoolantHeaterPortType("obsidian", 4);
    public static final FissionCoolantHeaterPortType NETHER_BRICK = new FissionCoolantHeaterPortType("nether_brick", 5);
    public static final FissionCoolantHeaterPortType GLOWSTONE = new FissionCoolantHeaterPortType("glowstone", 6);
    public static final FissionCoolantHeaterPortType LAPIS = new FissionCoolantHeaterPortType("lapis", 7);
    public static final FissionCoolantHeaterPortType GOLD = new FissionCoolantHeaterPortType("gold", 8);
    public static final FissionCoolantHeaterPortType PRISMARINE = new FissionCoolantHeaterPortType("prismarine", 9);
    public static final FissionCoolantHeaterPortType SLIME = new FissionCoolantHeaterPortType("slime", 10);
    public static final FissionCoolantHeaterPortType END_STONE = new FissionCoolantHeaterPortType("end_stone", 11);
    public static final FissionCoolantHeaterPortType PURPUR = new FissionCoolantHeaterPortType("purpur", 12);
    public static final FissionCoolantHeaterPortType DIAMOND = new FissionCoolantHeaterPortType("diamond", 13);
    public static final FissionCoolantHeaterPortType EMERALD = new FissionCoolantHeaterPortType("emerald", 14);
    public static final FissionCoolantHeaterPortType COPPER = new FissionCoolantHeaterPortType("copper", 15);
    public static final FissionCoolantHeaterPortType TIN = new FissionCoolantHeaterPortType("tin", 16);
    public static final FissionCoolantHeaterPortType LEAD = new FissionCoolantHeaterPortType("lead", 17);
    public static final FissionCoolantHeaterPortType BORON = new FissionCoolantHeaterPortType("boron", 18);
    public static final FissionCoolantHeaterPortType LITHIUM = new FissionCoolantHeaterPortType("lithium", 19);
    public static final FissionCoolantHeaterPortType MAGNESIUM = new FissionCoolantHeaterPortType("magnesium", 20);
    public static final FissionCoolantHeaterPortType MANGANESE = new FissionCoolantHeaterPortType("manganese", 21);
    public static final FissionCoolantHeaterPortType ALUMINUM = new FissionCoolantHeaterPortType("aluminum", 22);
    public static final FissionCoolantHeaterPortType SILVER = new FissionCoolantHeaterPortType("silver", 23);
    public static final FissionCoolantHeaterPortType FLUORITE = new FissionCoolantHeaterPortType("fluorite", 24);
    public static final FissionCoolantHeaterPortType VILLIAUMITE = new FissionCoolantHeaterPortType("villiaumite", 25);
    public static final FissionCoolantHeaterPortType CAROBBIITE = new FissionCoolantHeaterPortType("carobbiite", 26);
    public static final FissionCoolantHeaterPortType ARSENIC = new FissionCoolantHeaterPortType("arsenic", 27);
    public static final FissionCoolantHeaterPortType LIQUID_NITROGEN = new FissionCoolantHeaterPortType("liquid_nitrogen", 28);
    public static final FissionCoolantHeaterPortType LIQUID_HELIUM = new FissionCoolantHeaterPortType("liquid_helium", 29);
    public static final FissionCoolantHeaterPortType ENDERIUM = new FissionCoolantHeaterPortType("enderium", 30);
    public static final FissionCoolantHeaterPortType CRYOTHEUM = new FissionCoolantHeaterPortType("cryotheum", 31);
    private static final AtomicInteger _id = new AtomicInteger(31); // Used to increment KubeJS additions

    private final String name;
    private final int id;
    private ResourceLocation coolantId;
    private final String _translationKey;
    private final Function<Block.Properties, Block.Properties> _blockPropertiesFixer;

    private FissionCoolantHeaterPortType(String name, int id) {
        this.name = name;
        this.id = id;

        this._translationKey = ""; // TODO ADD
        this._blockPropertiesFixer = null;
    }

    public FissionCoolantHeaterPortType(String name, ResourceLocation fluid_id) { // Used by KubeJS
        this(name, _id.incrementAndGet());
        this.coolantId = fluid_id;
    }

    public static List<FissionCoolantHeaterPortType> default_values() {
        return List.of(STANDARD, IRON, REDSTONE, QUARTZ, OBSIDIAN, NETHER_BRICK, GLOWSTONE, LAPIS, GOLD, PRISMARINE, SLIME, END_STONE, PURPUR, DIAMOND, EMERALD, COPPER, TIN, LEAD, BORON, LITHIUM, MAGNESIUM, MANGANESE, ALUMINUM, SILVER, FLUORITE, VILLIAUMITE, CAROBBIITE, ARSENIC, LIQUID_NITROGEN, LIQUID_HELIUM, ENDERIUM, CRYOTHEUM);
    }

    public ResourceLocation getCoolantId() {
        if (coolantId == null) {
            if (this.id == 0) {
                coolantId = BuiltInRegistries.FLUID.getKey(COOLANT_MAP.get("nak").still.value());
            } else if (!name.contains(":")) {
                coolantId = BuiltInRegistries.FLUID.getKey(COOLANT_MAP.get(getName() + "_nak").still.value());
            }
        }
        return coolantId;
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