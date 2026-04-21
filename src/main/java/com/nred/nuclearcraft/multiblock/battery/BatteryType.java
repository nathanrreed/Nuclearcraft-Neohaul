package com.nred.nuclearcraft.multiblock.battery;

import it.zerono.mods.zerocore.lib.CodeHelper;
import it.zerono.mods.zerocore.lib.multiblock.variant.IMultiblockVariant;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.nred.nuclearcraft.config.NCConfig.battery_block_capacity;
import static com.nred.nuclearcraft.config.NCConfig.battery_block_max_transfer;

public class BatteryType implements IBatteryType, IMultiblockVariant {
    public static final BatteryType VOLTAIC_PILE_BASIC = new BatteryType("voltaic_pile_basic", 0, () -> battery_block_capacity[0], () -> battery_block_max_transfer[0]);
    public static final BatteryType VOLTAIC_PILE_ADVANCED = new BatteryType("voltaic_pile_advanced", 1, () -> battery_block_capacity[1], () -> battery_block_max_transfer[1]);
    public static final BatteryType VOLTAIC_PILE_DU = new BatteryType("voltaic_pile_du", 2, () -> battery_block_capacity[2], () -> battery_block_max_transfer[2]);
    public static final BatteryType VOLTAIC_PILE_ELITE = new BatteryType("voltaic_pile_elite", 3, () -> battery_block_capacity[3], () -> battery_block_max_transfer[3]);

    public static final BatteryType LITHIUM_ION_BATTERY_BASIC = new BatteryType("lithium_ion_battery_basic", 4, () -> battery_block_capacity[4], () -> battery_block_max_transfer[4]);
    public static final BatteryType LITHIUM_ION_BATTERY_ADVANCED = new BatteryType("lithium_ion_battery_advanced", 5, () -> battery_block_capacity[5], () -> battery_block_max_transfer[5]);
    public static final BatteryType LITHIUM_ION_BATTERY_DU = new BatteryType("lithium_ion_battery_du", 6, () -> battery_block_capacity[6], () -> battery_block_max_transfer[6]);
    public static final BatteryType LITHIUM_ION_BATTERY_ELITE = new BatteryType("lithium_ion_battery_elite", 7, () -> battery_block_capacity[7], () -> battery_block_max_transfer[7]);
    private static final AtomicInteger _id = new AtomicInteger(7); // Used to increment KubeJS additions

    private final String name;
    private final int id;
    private final Supplier<Integer> capacity;
    private final Supplier<Integer> maxTransfer;
    private final String _translationKey;
    private final Function<Block.Properties, Block.Properties> _blockPropertiesFixer;

    private BatteryType(String name, int id, Supplier<Integer> capacity, Supplier<Integer> max_transfer) {
        this.name = name;
        this.id = id;
        this.capacity = capacity;
        maxTransfer = max_transfer;

        this._translationKey = ""; // TODO ADD
        this._blockPropertiesFixer = null;
    }

    public BatteryType(String name, Supplier<Integer> capacity, Supplier<Integer> max_transfer) { // Used by KubeJS
        this(name, _id.incrementAndGet(), capacity, max_transfer);
    }

    @Override
    public Supplier<Integer> getCapacity() {
        return capacity;
    }

    @Override
    public Supplier<Integer> getMaxTransfer() {
        return maxTransfer; // TODO only used in item
    }

    @Override
    public int getId() {
        return id;
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