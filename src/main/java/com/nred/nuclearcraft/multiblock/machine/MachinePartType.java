package com.nred.nuclearcraft.multiblock.machine;

import com.nred.nuclearcraft.block.GenericActiveDirectionalTooltipDeviceBlock;
import com.nred.nuclearcraft.block.GenericActiveTooltipDeviceBlock;
import com.nred.nuclearcraft.block.GenericTooltipDeviceBlock;
import com.nred.nuclearcraft.block.machine.*;
import it.zerono.mods.zerocore.base.multiblock.part.GlassBlock;
import it.zerono.mods.zerocore.lib.block.multiblock.MultiblockPartBlock;
import it.zerono.mods.zerocore.lib.block.multiblock.MultiblockPartTypeProperties;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;
import java.util.function.Supplier;

import static com.nred.nuclearcraft.registration.BlockEntityRegistration.MACHINE_ENTITY_TYPE;

public enum MachinePartType implements IMachinePartType {
    Frame(() -> MACHINE_ENTITY_TYPE.get("casing")::get, MachineFrameBlock::new),
    Glass(() -> MACHINE_ENTITY_TYPE.get("glass")::get, GlassBlock::new, GlassBlock::addGlassProperties),
    PowerPort(() -> MACHINE_ENTITY_TYPE.get("power_port")::get, GenericActiveDirectionalTooltipDeviceBlock::new),
    ProcessPort(() -> MACHINE_ENTITY_TYPE.get("process_port")::get, MachineProcessPortBlock::new),
    ReservoirPort(() -> MACHINE_ENTITY_TYPE.get("reservoir_port")::get, GenericActiveDirectionalTooltipDeviceBlock::new),
    RedstonePort(() -> MACHINE_ENTITY_TYPE.get("redstone_port")::get, MachineRedstonePortBlock::new),
    ComputerPort(() -> MACHINE_ENTITY_TYPE.get("computer_port")::get, GenericTooltipDeviceBlock::new),

    // Electrolyzer
    ElectrolyzerController(() -> MACHINE_ENTITY_TYPE.get("electrolyzer_controller")::get, ElectrolyzerControllerBlock::new),
    CathodeTerminal(() -> MACHINE_ENTITY_TYPE.get("electrolyzer_cathode_terminal")::get, ElectrolyzerCathodeTerminalBlock::new),
    AnodeTerminal(() -> MACHINE_ENTITY_TYPE.get("electrolyzer_anode_terminal")::get, ElectrolyzerAnodeTerminalBlock::new),

    // Distiller
    DistillerController(() -> MACHINE_ENTITY_TYPE.get("distiller_controller")::get, DistillerControllerBlock::new),
    DistillerSieveTray(() -> MACHINE_ENTITY_TYPE.get("distiller_sieve_tray")::get, DistillerSieveTrayBlock::new),
    DistillerRefluxUnit(() -> MACHINE_ENTITY_TYPE.get("distiller_reflux_unit")::get, GenericTooltipDeviceBlock::new),
    DistillerReboilingUnit(() -> MACHINE_ENTITY_TYPE.get("distiller_reboiling_unit")::get, GenericActiveTooltipDeviceBlock::new),
    DistillerLiquidDistributor(() -> MACHINE_ENTITY_TYPE.get("distiller_liquid_distributor")::get, GenericTooltipDeviceBlock::new),

    // Infiltrator
    InfiltratorController(() -> MACHINE_ENTITY_TYPE.get("infiltrator_controller")::get, InfiltratorControllerBlock::new),
    InfiltratorPressureChamber(() -> MACHINE_ENTITY_TYPE.get("infiltrator_pressure_chamber")::get, GenericTooltipDeviceBlock::new),
    InfiltratorHeatingUnit(() -> MACHINE_ENTITY_TYPE.get("infiltrator_heating_unit")::get, GenericActiveTooltipDeviceBlock::new),
    ;

    private final MultiblockPartTypeProperties<Machine, IMachinePartType> _properties;

    MachinePartType(final Supplier<@NotNull Supplier<@NotNull BlockEntityType<?>>> tileTypeSupplier,
                    final Function<MultiblockPartBlock.@NotNull MultiblockPartProperties<IMachinePartType>, @NotNull MultiblockPartBlock<Machine, IMachinePartType>> blockFactory,
                    final Function<Block.@NotNull Properties, Block.@NotNull Properties> blockPropertiesFixer) {
        this._properties = new MultiblockPartTypeProperties<>(tileTypeSupplier, blockFactory, "", blockPropertiesFixer, ppf -> ppf);
    }

    MachinePartType(final Supplier<@NotNull Supplier<@NotNull BlockEntityType<?>>> tileTypeSupplier,
                    final Function<MultiblockPartBlock.@NotNull MultiblockPartProperties<IMachinePartType>, @NotNull MultiblockPartBlock<Machine, IMachinePartType>> blockFactory) {
        this._properties = new MultiblockPartTypeProperties<>(tileTypeSupplier, blockFactory, "", bp -> bp, ppf -> ppf);
    }

    @Override
    public MultiblockPartTypeProperties<Machine, IMachinePartType> getPartTypeProperties() {
        return this._properties;
    }

    @Override
    public String getSerializedName() {
        return this.name();
    }
}
