package com.nred.nuclearcraft.multiblock.machine;

import com.nred.nuclearcraft.block.GenericActiveDirectionalTooltipDeviceBlock;
import com.nred.nuclearcraft.block.GenericTooltipDeviceBlock;
import com.nred.nuclearcraft.block.machine.MachineFrameBlock;
import com.nred.nuclearcraft.block.machine.MachineProcessPortBlock;
import com.nred.nuclearcraft.block.machine.MachineRedstonePortBlock;
import com.nred.nuclearcraft.multiblock.hx.HeatExchanger;
import com.nred.nuclearcraft.multiblock.hx.IHeatExchangerPartType;
import it.zerono.mods.zerocore.base.multiblock.part.GlassBlock;
import it.zerono.mods.zerocore.lib.block.multiblock.MultiblockPartBlock;
import it.zerono.mods.zerocore.lib.block.multiblock.MultiblockPartTypeProperties;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;
import java.util.function.Supplier;

import static com.nred.nuclearcraft.registration.BlockEntityRegistration.MACHINE_ENTITY_TYPE;

public enum MachinePartType implements IHeatExchangerPartType {
    Frame(() -> MACHINE_ENTITY_TYPE.get("casing")::get, MachineFrameBlock::new),
    Glass(() -> MACHINE_ENTITY_TYPE.get("glass")::get, GlassBlock::new, GlassBlock::addGlassProperties),
    PowerPort(() -> MACHINE_ENTITY_TYPE.get("power_port")::get, GenericActiveDirectionalTooltipDeviceBlock::new),
    ProcessPort(() -> MACHINE_ENTITY_TYPE.get("process_port")::get, MachineProcessPortBlock::new),
    ReservoirPort(() -> MACHINE_ENTITY_TYPE.get("reservoir_port")::get, GenericActiveDirectionalTooltipDeviceBlock::new),
    RedstonePort(() -> MACHINE_ENTITY_TYPE.get("redstone_port")::get, MachineRedstonePortBlock::new),
    ComputerPort(() -> MACHINE_ENTITY_TYPE.get("computer_port")::get, GenericTooltipDeviceBlock::new),
    ;

    private final MultiblockPartTypeProperties<HeatExchanger, IHeatExchangerPartType> _properties;

    MachinePartType(final Supplier<@NotNull Supplier<@NotNull BlockEntityType<?>>> tileTypeSupplier,
                    final Function<MultiblockPartBlock.@NotNull MultiblockPartProperties<IHeatExchangerPartType>, @NotNull MultiblockPartBlock<HeatExchanger, IHeatExchangerPartType>> blockFactory,
                    final Function<Block.@NotNull Properties, Block.@NotNull Properties> blockPropertiesFixer) {
        this._properties = new MultiblockPartTypeProperties<>(tileTypeSupplier, blockFactory, "", blockPropertiesFixer, ppf -> ppf);
    }

    MachinePartType(final Supplier<@NotNull Supplier<@NotNull BlockEntityType<?>>> tileTypeSupplier,
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
