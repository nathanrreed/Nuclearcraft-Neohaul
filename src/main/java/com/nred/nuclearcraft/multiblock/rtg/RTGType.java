package com.nred.nuclearcraft.multiblock.rtg;

import com.nred.nuclearcraft.radiation.RadSources;
import it.zerono.mods.zerocore.lib.CodeHelper;
import it.zerono.mods.zerocore.lib.multiblock.variant.IMultiblockVariant;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.nred.nuclearcraft.config.NCConfig.rtg_power;

public class RTGType implements StringRepresentable, IMultiblockVariant {
    public static final RTGType URANIUM = new RTGType(0, "uranium", () -> rtg_power[0], RadSources.URANIUM_238);
    public static final RTGType PLUTONIUM = new RTGType(1, "plutonium", () -> rtg_power[1], RadSources.PLUTONIUM_238);
    public static final RTGType AMERICIUM = new RTGType(2, "americium", () -> rtg_power[2], RadSources.AMERICIUM_241);
    public static final RTGType CALIFORNIUM = new RTGType(3, "californium", () -> rtg_power[3], RadSources.CALIFORNIUM_250);
    private static final AtomicInteger _id = new AtomicInteger(3); // Used to increment KubeJS additions

    private final int id;
    private final String name;
    private final Supplier<Integer> power;
    private final double radiation;
    private final String _translationKey;
    private final Function<Block.Properties, Block.Properties> _blockPropertiesFixer;

    private RTGType(int id, String name, Supplier<Integer> power, double radiation) {
        this.id = id;
        this.name = name;
        this.power = power;
        this.radiation = radiation;

        this._translationKey = ""; // TODO ADD
        this._blockPropertiesFixer = null;
    }

    public RTGType(String name, Supplier<Integer> power, double radiation) {
        this(_id.incrementAndGet(), name, power, radiation);
    }

    @Override
    public String getSerializedName() {
        return name;
    }

    @Override
    public String toString() {
        return getSerializedName();
    }

    public int getPower() {
        return power.get();
    }

    public double getRadiation() {
        return radiation / 8.0;
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