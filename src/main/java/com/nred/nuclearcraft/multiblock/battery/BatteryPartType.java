package com.nred.nuclearcraft.multiblock.battery;

import com.nred.nuclearcraft.block.battery.BlockBattery;
import it.zerono.mods.zerocore.lib.block.multiblock.MultiblockPartBlock;
import it.zerono.mods.zerocore.lib.block.multiblock.MultiblockPartTypeProperties;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;
import java.util.function.Supplier;

import static com.nred.nuclearcraft.registration.BlockEntityRegistration.BATTERY_ENTITY_TYPE;

public enum BatteryPartType implements IBatteryPartType {
    BATTERY(() -> BATTERY_ENTITY_TYPE::get, BlockBattery::new, "");

    private final MultiblockPartTypeProperties<BatteryMultiblock, IBatteryPartType> _properties;

    BatteryPartType(final Supplier<@NotNull Supplier<@NotNull BlockEntityType<?>>> tileTypeSupplier,
                    final Function<MultiblockPartBlock.@NotNull MultiblockPartProperties<IBatteryPartType>, @NotNull MultiblockPartBlock<BatteryMultiblock, IBatteryPartType>> blockFactory,
                    final String translationKey) {
        this._properties = new MultiblockPartTypeProperties<>(tileTypeSupplier, blockFactory, translationKey, bp -> bp, ppf -> ppf);
    }

    @Override
    public MultiblockPartTypeProperties<BatteryMultiblock, IBatteryPartType> getPartTypeProperties() {
        return this._properties;
    }

    @Override
    public String getSerializedName() {
        return this.name();
    }
}
