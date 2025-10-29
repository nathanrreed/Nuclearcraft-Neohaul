package com.nred.nuclearcraft.multiblock.hx;

import com.nred.nuclearcraft.block.GenericDirectionalTooltipDeviceBlock;
import com.nred.nuclearcraft.block.hx.*;
import it.zerono.mods.zerocore.base.multiblock.part.GlassBlock;
import it.zerono.mods.zerocore.lib.block.multiblock.MultiblockPartBlock;
import it.zerono.mods.zerocore.lib.block.multiblock.MultiblockPartTypeProperties;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;
import java.util.function.Supplier;

import static com.nred.nuclearcraft.registration.BlockEntityRegistration.HX_ENTITY_TYPE;

public enum HeatExchangerPartType implements IHeatExchangerPartType {
    HeatExchangerController(() -> HX_ENTITY_TYPE.get("heat_exchanger_controller")::get, HeatExchangerControllerBlock::new),
    CondenserController(() -> HX_ENTITY_TYPE.get("condenser_controller")::get, CondenserControllerBlock::new),
    Casing(() -> HX_ENTITY_TYPE.get("casing")::get, HeatExchangerCasingBlock::new),
    Glass(() -> HX_ENTITY_TYPE.get("glass")::get, GlassBlock::new, GlassBlock::addGlassProperties),
    Tube(() -> HX_ENTITY_TYPE.get("tube")::get, HeatExchangerTubeBlock::new),
    Baffle(() -> HX_ENTITY_TYPE.get("baffle")::get, GenericDirectionalTooltipDeviceBlock::new),
    Inlet(() -> HX_ENTITY_TYPE.get("inlet")::get, GenericDirectionalTooltipDeviceBlock::new),
    Outlet(() -> HX_ENTITY_TYPE.get("outlet")::get, GenericDirectionalTooltipDeviceBlock::new),
    RedstonePort(() -> HX_ENTITY_TYPE.get("redstone_port")::get, HeatExchangerRedstonePortBlock::new),
    ComputerPort(() -> HX_ENTITY_TYPE.get("computer_port")::get, GenericDirectionalTooltipDeviceBlock::new),
    ;

    private final MultiblockPartTypeProperties<HeatExchanger, IHeatExchangerPartType> _properties;

    HeatExchangerPartType(final Supplier<@NotNull Supplier<@NotNull BlockEntityType<?>>> tileTypeSupplier,
                          final Function<MultiblockPartBlock.@NotNull MultiblockPartProperties<IHeatExchangerPartType>, @NotNull MultiblockPartBlock<HeatExchanger, IHeatExchangerPartType>> blockFactory,
                          final Function<Block.@NotNull Properties, Block.@NotNull Properties> blockPropertiesFixer) {
        this._properties = new MultiblockPartTypeProperties<>(tileTypeSupplier, blockFactory, "", blockPropertiesFixer, ppf -> ppf);
    }

    HeatExchangerPartType(final Supplier<@NotNull Supplier<@NotNull BlockEntityType<?>>> tileTypeSupplier,
                          final Function<MultiblockPartBlock.@NotNull MultiblockPartProperties<IHeatExchangerPartType>, @NotNull MultiblockPartBlock<HeatExchanger, IHeatExchangerPartType>> blockFactory) {
        this._properties = new MultiblockPartTypeProperties<>(tileTypeSupplier, blockFactory, "", bp -> bp, ppf -> ppf);
    }

    @Override
    public MultiblockPartTypeProperties<HeatExchanger, IHeatExchangerPartType> getPartTypeProperties() {
        return this._properties;
    }

    @Override
    public String getSerializedName() {
        return this.name();
    }
}
