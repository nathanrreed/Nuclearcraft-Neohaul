package com.nred.nuclearcraft.multiblock.fisson;

import com.nred.nuclearcraft.block.GenericActiveAxisTooltipDeviceBlock;
import com.nred.nuclearcraft.block.GenericActiveDirectionalTooltipDeviceBlock;
import com.nred.nuclearcraft.block.GenericTooltipDeviceBlock;
import com.nred.nuclearcraft.block.fission.*;
import com.nred.nuclearcraft.block.fission.manager.FissionShieldManagerBlock;
import com.nred.nuclearcraft.block.fission.manager.FissionSourceManagerBlock;
import com.nred.nuclearcraft.block.fission.port.FissionIrradiatorPortBlock;
import com.nred.nuclearcraft.block.fission.port.FissionCellPortBlock;
import com.nred.nuclearcraft.block.fission.port.FissionCoolerPortBlock;
import com.nred.nuclearcraft.block.fission.port.FissionVesselPortBlock;
import it.zerono.mods.zerocore.base.multiblock.part.GlassBlock;
import it.zerono.mods.zerocore.lib.block.multiblock.MultiblockPartBlock;
import it.zerono.mods.zerocore.lib.block.multiblock.MultiblockPartTypeProperties;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;
import java.util.function.Supplier;

import static com.nred.nuclearcraft.registration.BlockEntityRegistration.FISSION_ENTITY_TYPE;

public enum FissionPartType implements IFissionPartType {
    SolidFuelController(() -> FISSION_ENTITY_TYPE.get("solid_fuel_fission_controller")::get, FissionControllerBlock::new),
    MoltenSaltController(() -> FISSION_ENTITY_TYPE.get("molten_salt_fission_controller")::get, FissionControllerBlock::new),
    Casing(() -> FISSION_ENTITY_TYPE.get("casing")::get, FissionCasingBlock::new),
    Glass(() -> FISSION_ENTITY_TYPE.get("glass")::get, GlassBlock::new, GlassBlock::addGlassProperties),
    Vent(() -> FISSION_ENTITY_TYPE.get("vent")::get, FissionVentBlock::new),
    Conductor(() -> FISSION_ENTITY_TYPE.get("conductor")::get, GenericTooltipDeviceBlock::new),

    Shield(() -> FISSION_ENTITY_TYPE.get("shield")::get, FissionShieldBlock::new),
    Source(() -> FISSION_ENTITY_TYPE.get("source")::get, FissionSourceBlock::new),

    Cell(() -> FISSION_ENTITY_TYPE.get("cell")::get, GenericTooltipDeviceBlock::new),
    CellPort(() -> FISSION_ENTITY_TYPE.get("cell_port")::get, FissionCellPortBlock::new),

    Cooler(() -> FISSION_ENTITY_TYPE.get("cooler")::get, GenericTooltipDeviceBlock::new),
    CoolerPort(() -> FISSION_ENTITY_TYPE.get("cooler")::get, FissionCoolerPortBlock::new),

    Irradiator(() -> FISSION_ENTITY_TYPE.get("irradiator")::get, GenericTooltipDeviceBlock::new),
    IrradiatorPort(() -> FISSION_ENTITY_TYPE.get("irradiator_port")::get, FissionIrradiatorPortBlock::new),

    Vessel(() -> FISSION_ENTITY_TYPE.get("vessel")::get, GenericTooltipDeviceBlock::new),
    VesselPort(() -> FISSION_ENTITY_TYPE.get("vessel_port")::get, FissionVesselPortBlock::new),

    HeatSink(() -> FISSION_ENTITY_TYPE.get("heat_sink")::get, GenericTooltipDeviceBlock::new),
    Heater(() -> FISSION_ENTITY_TYPE.get("coolant_heater")::get, GenericTooltipDeviceBlock::new),
    HeaterPort(() -> FISSION_ENTITY_TYPE.get("coolant_heater_port")::get, GenericActiveAxisTooltipDeviceBlock::new),

    ComputerPort(() -> FISSION_ENTITY_TYPE.get("computer_port")::get, GenericTooltipDeviceBlock::new),

    Monitor(() -> FISSION_ENTITY_TYPE.get("monitor")::get, FissionMonitorBlock::new),
    ShieldManager(() -> FISSION_ENTITY_TYPE.get("shield_manager")::get, FissionShieldManagerBlock::new),
    SourceManager(() -> FISSION_ENTITY_TYPE.get("source_manager")::get, FissionSourceManagerBlock::new),
    PowerPort(() -> FISSION_ENTITY_TYPE.get("power_port")::get, GenericActiveDirectionalTooltipDeviceBlock::new),
    ;

    private final MultiblockPartTypeProperties<FissionReactor, IFissionPartType> _properties;

    FissionPartType(final Supplier<@NotNull Supplier<@NotNull BlockEntityType<?>>> tileTypeSupplier,
                    final Function<MultiblockPartBlock.@NotNull MultiblockPartProperties<IFissionPartType>, @NotNull MultiblockPartBlock<FissionReactor, IFissionPartType>> blockFactory,
                    final Function<Block.@NotNull Properties, Block.@NotNull Properties> blockPropertiesFixer) {
        this._properties = new MultiblockPartTypeProperties<>(tileTypeSupplier, blockFactory, "", blockPropertiesFixer, ppf -> ppf);
    }

    FissionPartType(final Supplier<@NotNull Supplier<@NotNull BlockEntityType<?>>> tileTypeSupplier,
                    final Function<MultiblockPartBlock.@NotNull MultiblockPartProperties<IFissionPartType>, @NotNull MultiblockPartBlock<FissionReactor, IFissionPartType>> blockFactory) {
        this._properties = new MultiblockPartTypeProperties<>(tileTypeSupplier, blockFactory, "", bp -> bp, ppf -> ppf);
    }

    @Override
    public MultiblockPartTypeProperties<FissionReactor, IFissionPartType> getPartTypeProperties() {
        return this._properties;
    }

    @Override
    public String getSerializedName() {
        return this.name();
    }
}
