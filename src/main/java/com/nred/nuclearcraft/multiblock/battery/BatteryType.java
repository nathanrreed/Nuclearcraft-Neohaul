package com.nred.nuclearcraft.multiblock.battery;

import it.zerono.mods.zerocore.lib.CodeHelper;
import it.zerono.mods.zerocore.lib.multiblock.variant.IMultiblockVariant;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.fml.common.asm.enumextension.ExtensionInfo;
import net.neoforged.fml.common.asm.enumextension.IExtensibleEnum;

import java.util.function.Function;

public enum BatteryType implements IBatteryType, IMultiblockVariant, IExtensibleEnum {
    VOLTAIC_PILE_BASIC(0, 1600000, 16000),
    VOLTAIC_PILE_ADVANCED(1, 6400000, 64000),
    VOLTAIC_PILE_DU(2, 25600000, 256000),
    VOLTAIC_PILE_ELITE(3, 102400000, 1024000),

    LITHIUM_ION_BATTERY_BASIC(4, 32000000, 320000),
    LITHIUM_ION_BATTERY_ADVANCED(5, 128000000, 1280000),
    LITHIUM_ION_BATTERY_DU(6, 512000000, 5120000),
    LITHIUM_ION_BATTERY_ELITE(7, 2048000000, 20480000);

    private final int id;
    private final int capacity;
    private final int max_transfer;
    private final String _translationKey;
    private final Function<Block.Properties, Block.Properties> _blockPropertiesFixer;

    BatteryType(int id, int capacity, int max_transfer) {
        this.id = id;
        this.capacity = capacity;
        this.max_transfer = max_transfer;

        this._translationKey = ""; // TODO ADD
        this._blockPropertiesFixer = null;
    }

    @Override
    public long getCapacity() {
        return capacity;
    }

    @Override
    public int getMaxTransfer() {
        return max_transfer;
    }

    @Override
    public int getId() {
        return id;
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

    public static ExtensionInfo getExtensionInfo() {
        return ExtensionInfo.nonExtended(BatteryType.class);
    }
}