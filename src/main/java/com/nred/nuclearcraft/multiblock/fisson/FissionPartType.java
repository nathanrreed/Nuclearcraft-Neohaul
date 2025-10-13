package com.nred.nuclearcraft.multiblock.fisson;

import com.nred.nuclearcraft.block.GenericActiveDirectionalTooltipDeviceBlock;
import com.nred.nuclearcraft.block.GenericDirectionalTooltipDeviceBlock;
import com.nred.nuclearcraft.block.GenericTooltipDeviceBlock;
import com.nred.nuclearcraft.block.fission.*;
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
    SolidFuelController(() -> FISSION_ENTITY_TYPE.get("solid_fuel_fission_controller")::get, FissionControllerBlock::new, ""),
    MoltenSaltController(() -> FISSION_ENTITY_TYPE.get("molten_salt_fission_controller")::get, FissionControllerBlock::new, ""),
    Casing(() -> FISSION_ENTITY_TYPE.get("casing")::get, FissionCasingBlock::new, ""),
    Glass(() -> FISSION_ENTITY_TYPE.get("glass")::get, GlassBlock::new, "", GlassBlock::addGlassProperties),
    Vent(() -> FISSION_ENTITY_TYPE.get("vent")::get, FissionVentBlock::new, ""),
    Reflector(() -> FISSION_ENTITY_TYPE.get("reflector")::get, GenericTooltipDeviceBlock::new, ""),
    Conductor(() -> FISSION_ENTITY_TYPE.get("conductor")::get, GenericTooltipDeviceBlock::new, ""),

    Shield(() -> FISSION_ENTITY_TYPE.get("shield")::get, FissionShieldBlock::new, ""),
    Source(() -> FISSION_ENTITY_TYPE.get("source")::get, FissionSourceBlock::new, ""),

    Cell(() -> FISSION_ENTITY_TYPE.get("cell")::get, GenericTooltipDeviceBlock::new, ""),
    CellPort(() -> FISSION_ENTITY_TYPE.get("cell_port")::get, GenericActiveDirectionalTooltipDeviceBlock::new, ""),

    Cooler(() -> FISSION_ENTITY_TYPE.get("cooler")::get, GenericTooltipDeviceBlock::new, ""),
    CoolerPort(() -> FISSION_ENTITY_TYPE.get("cooler")::get, GenericActiveDirectionalTooltipDeviceBlock::new, ""),

    Irradiator(() -> FISSION_ENTITY_TYPE.get("irradiator")::get, GenericTooltipDeviceBlock::new, ""),
    IrradiatorPort(() -> FISSION_ENTITY_TYPE.get("irradiator_port")::get, GenericActiveDirectionalTooltipDeviceBlock::new, ""),

    Vessel(() -> FISSION_ENTITY_TYPE.get("vessel")::get, GenericTooltipDeviceBlock::new, ""),
    VesselPort(() -> FISSION_ENTITY_TYPE.get("vessel_port")::get, GenericActiveDirectionalTooltipDeviceBlock::new, ""),

    HeatSink(() -> FISSION_ENTITY_TYPE.get("heat_sink")::get, GenericTooltipDeviceBlock::new, "custom"),
    Heater(() -> FISSION_ENTITY_TYPE.get("coolant_heater")::get, GenericTooltipDeviceBlock::new, "custom"),
    HeaterPort(() -> FISSION_ENTITY_TYPE.get("coolant_heater_port")::get, GenericActiveDirectionalTooltipDeviceBlock::new, ""),

    ComputerPort(() -> FISSION_ENTITY_TYPE.get("computer_port")::get, GenericTooltipDeviceBlock::new, ""),

    // TODO
//    Monitor(() -> FISSION_ENTITY_TYPE.get("monitor")::get, GenericTooltipDeviceBlock::new, ""),
//    PowerPort(() -> FISSION_ENTITY_TYPE.get("power_port")::get, GenericTooltipDeviceBlock::new, ""),
//    Manager(() -> FISSION_ENTITY_TYPE.get("manager")::get, GenericTooltipDeviceBlock::new, ""),
//    ShieldManager(() -> FISSION_ENTITY_TYPE.get("shield_manager")::get, GenericTooltipDeviceBlock::new, ""),
//    SourceManager(() -> FISSION_ENTITY_TYPE.get("source_manager")::get, GenericTooltipDeviceBlock::new, ""),

    ;

    private final MultiblockPartTypeProperties<FissionReactor, IFissionPartType> _properties;

    FissionPartType(final Supplier<@NotNull Supplier<@NotNull BlockEntityType<?>>> tileTypeSupplier,
                    final Function<MultiblockPartBlock.@NotNull MultiblockPartProperties<IFissionPartType>, @NotNull MultiblockPartBlock<FissionReactor, IFissionPartType>> blockFactory,
                    final String translationKey,
                    final Function<Block.@NotNull Properties, Block.@NotNull Properties> blockPropertiesFixer) {
        this._properties = new MultiblockPartTypeProperties<>(tileTypeSupplier, blockFactory, translationKey, blockPropertiesFixer, ppf -> ppf);
    }

    FissionPartType(final Supplier<@NotNull Supplier<@NotNull BlockEntityType<?>>> tileTypeSupplier,
                    final Function<MultiblockPartBlock.@NotNull MultiblockPartProperties<IFissionPartType>, @NotNull MultiblockPartBlock<FissionReactor, IFissionPartType>> blockFactory,
                    final String translationKey) {
        this._properties = new MultiblockPartTypeProperties<>(tileTypeSupplier, blockFactory, translationKey, bp -> bp, ppf -> ppf);
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
