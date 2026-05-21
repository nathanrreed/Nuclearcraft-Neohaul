package com.nred.nuclearcraft.multiblock.distributor;

import com.nred.nuclearcraft.block.GenericTooltipDeviceBlock;
import it.zerono.mods.zerocore.lib.block.multiblock.MultiblockPartBlock;
import it.zerono.mods.zerocore.lib.block.multiblock.MultiblockPartTypeProperties;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;
import java.util.function.Supplier;

import static com.nred.nuclearcraft.registration.BlockEntityRegistration.*;

public enum DistributorPartType implements IDistributorPartType {
    Inlet(() -> DISTRIBUTOR_INLET_ENTITY_TYPE::get, GenericTooltipDeviceBlock::new, ""),
    Outlet(() -> DISTRIBUTOR_OUTLET_ENTITY_TYPE::get, GenericTooltipDeviceBlock::new, ""),
    Buffer(() -> DISTRIBUTOR_BUFFER_ENTITY_TYPE::get, GenericTooltipDeviceBlock::new, "");

    private final MultiblockPartTypeProperties<Distributor, IDistributorPartType> _properties;

    DistributorPartType(final Supplier<@NotNull Supplier<@NotNull BlockEntityType<?>>> tileTypeSupplier,
                        final Function<MultiblockPartBlock.@NotNull MultiblockPartProperties<IDistributorPartType>, @NotNull MultiblockPartBlock<Distributor, IDistributorPartType>> blockFactory,
                        final String translationKey) {
        this._properties = new MultiblockPartTypeProperties<>(tileTypeSupplier, blockFactory, translationKey, bp -> bp, ppf -> ppf);
    }

    @Override
    public MultiblockPartTypeProperties<Distributor, IDistributorPartType> getPartTypeProperties() {
        return this._properties;
    }

    @Override
    public String getSerializedName() {
        return this.name();
    }
}
