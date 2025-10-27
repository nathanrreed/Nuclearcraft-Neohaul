package com.nred.nuclearcraft.multiblock.battery;

import it.zerono.mods.zerocore.lib.CodeHelper;
import it.zerono.mods.zerocore.lib.multiblock.variant.IMultiblockVariant;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.fml.common.asm.enumextension.ExtensionInfo;
import net.neoforged.fml.common.asm.enumextension.IExtensibleEnum;

import java.util.function.Function;
import java.util.function.Supplier;

import static com.nred.nuclearcraft.config.NCConfig.battery_block_capacity;
import static com.nred.nuclearcraft.config.NCConfig.battery_block_max_transfer;

public enum BatteryType implements IBatteryType, IMultiblockVariant, IExtensibleEnum {
    VOLTAIC_PILE_BASIC(0),
    VOLTAIC_PILE_ADVANCED(1),
    VOLTAIC_PILE_DU(2),
    VOLTAIC_PILE_ELITE(3),

    LITHIUM_ION_BATTERY_BASIC(4),
    LITHIUM_ION_BATTERY_ADVANCED(5),
    LITHIUM_ION_BATTERY_DU(6),
    LITHIUM_ION_BATTERY_ELITE(7);

    private final int id;
    private final String _translationKey;
    private final Function<Block.Properties, Block.Properties> _blockPropertiesFixer;

    BatteryType(int id) {
        this.id = id;

        this._translationKey = ""; // TODO ADD
        this._blockPropertiesFixer = null;
    }

    @Override
    public Supplier<Integer> getCapacity() {
        return () -> battery_block_capacity[id];
    }

    @Override
    public Supplier<Integer> getMaxTransfer() {
        return () -> battery_block_max_transfer[id];
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