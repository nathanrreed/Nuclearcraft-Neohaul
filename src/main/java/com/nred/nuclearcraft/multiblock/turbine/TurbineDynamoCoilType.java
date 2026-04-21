package com.nred.nuclearcraft.multiblock.turbine;

import com.nred.nuclearcraft.block_entity.turbine.AbstractTurbineEntity;
import com.nred.nuclearcraft.multiblock.PlacementRule;
import it.zerono.mods.zerocore.lib.CodeHelper;
import it.zerono.mods.zerocore.lib.multiblock.variant.IMultiblockVariant;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.nred.nuclearcraft.config.NCConfig.turbine_coil_conductivity;

public class TurbineDynamoCoilType implements StringRepresentable, IMultiblockVariant {
    public static final TurbineDynamoCoilType MAGNESIUM = new TurbineDynamoCoilType("magnesium", 0, () -> turbine_coil_conductivity[0]);
    public static final TurbineDynamoCoilType BERYLLIUM = new TurbineDynamoCoilType("beryllium", 1, () -> turbine_coil_conductivity[1]);
    public static final TurbineDynamoCoilType ALUMINUM = new TurbineDynamoCoilType("aluminum", 2, () -> turbine_coil_conductivity[2]);
    public static final TurbineDynamoCoilType GOLD = new TurbineDynamoCoilType("gold", 3, () -> turbine_coil_conductivity[3]);
    public static final TurbineDynamoCoilType COPPER = new TurbineDynamoCoilType("copper", 4, () -> turbine_coil_conductivity[4]);
    public static final TurbineDynamoCoilType SILVER = new TurbineDynamoCoilType("silver", 5, () -> turbine_coil_conductivity[5]);
    private static final AtomicInteger _id = new AtomicInteger(5); // Used to increment KubeJS additions

    private final String name;
    private final int id;
    private final Supplier<Double> conductivity;
    private final String _translationKey;
    private final Function<Block.Properties, Block.Properties> _blockPropertiesFixer;

    private TurbineDynamoCoilType(String name, int id, Supplier<Double> conductivity) {
        this.name = name;
        this.id = id;
        this.conductivity = conductivity;

        this._translationKey = ""; // TODO ADD
        this._blockPropertiesFixer = null;
    }

    public TurbineDynamoCoilType(String name, Supplier<Double> conductivity) { // Used by KubeJS
        this(name, _id.incrementAndGet(), conductivity);
    }

    public PlacementRule<Turbine, AbstractTurbineEntity> getRule() {
        return TurbinePlacement.RULE_MAP.get(this.name + (this.name.contains(":") ? "" : "_coil"));
    }

    public String getTooltipRule() {
        return TurbinePlacement.TOOLTIP_MAP.get(this.name + (this.name.contains(":") ? "" : "_coil"));
    }

    @Override
    public String toString() {
        return getSerializedName();
    }

    public double getConductivity() {
        return conductivity.get();
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