package com.nred.nuclearcraft.multiblock.turbine;

import com.nred.nuclearcraft.block.GenericTooltipDeviceBlock;
import com.nred.nuclearcraft.block.HorizontalGenericDeviceBlock;
import com.nred.nuclearcraft.block.turbine.*;
import it.zerono.mods.zerocore.base.multiblock.part.GlassBlock;
import it.zerono.mods.zerocore.lib.block.multiblock.MultiblockPartBlock;
import it.zerono.mods.zerocore.lib.block.multiblock.MultiblockPartTypeProperties;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;
import java.util.function.Supplier;

import static com.nred.nuclearcraft.registration.BlockEntityRegistration.*;

public enum TurbinePartType implements ITurbinePartType {
    Casing(() -> TURBINE_CASING::get, MultiblockPartBlock::new, ""),
    Glass(() -> TURBINE_GLASS::get, GlassBlock::new, "", GlassBlock::addGlassProperties),
    Controller(() -> TURBINE_CONTROLLER::get, TurbineControllerBlock::new, "tooltip.turbine_controller"),
    RotorBearing(() -> TURBINE_ROTOR_BEARING::get, GenericTooltipDeviceBlock::new, "tooltip.turbine_rotor_bearing"),
    RotorShaft(() -> TURBINE_ROTOR_SHAFT::get, TurbineRotorShaftBlock::new, "tooltip.turbine_rotor_shaft"),
    Outlet(() -> TURBINE_OUTLET::get, HorizontalGenericDeviceBlock::new, ""),
    Inlet(() -> TURBINE_INLET::get, HorizontalGenericDeviceBlock::new, ""),
    RotorBlade(() -> TURBINE_ROTOR_BLADE::get, TurbineRotorBladeBlock::new, "tooltip.turbine_rotor_blade"),
    RotorStator(() -> TURBINE_ROTOR_STATOR::get, TurbineRotorStatorBlock::new, "tooltip.turbine_rotor_stator"),
    Dynamo(() -> TURBINE_DYNAMO::get, GenericTooltipDeviceBlock::new, "custom"),
    DynamoConnector(() -> TURBINE_DYNAMO::get, GenericTooltipDeviceBlock::new, "custom"),
     ComputerPort(() -> TURBINE_COMPUTER_PORT::get, GenericTooltipDeviceBlock::new, "tooltip.turbine_computer_port"),
     RedstonePort(() -> TURBINE_REDSTONE_PORT::get, TurbineRedstonePortBlock::new, ""),
    ;

    private final MultiblockPartTypeProperties<Turbine, ITurbinePartType> _properties;

    TurbinePartType(final Supplier<@NotNull Supplier<@NotNull BlockEntityType<?>>> tileTypeSupplier,
                    final Function<MultiblockPartBlock.@NotNull MultiblockPartProperties<ITurbinePartType>, @NotNull MultiblockPartBlock<Turbine, ITurbinePartType>> blockFactory,
                    final String translationKey,
                    final Function<Block.@NotNull Properties, Block.@NotNull Properties> blockPropertiesFixer) {
        this._properties = new MultiblockPartTypeProperties<>(tileTypeSupplier, blockFactory, translationKey, blockPropertiesFixer, ppf -> ppf);
    }

    TurbinePartType(final Supplier<@NotNull Supplier<@NotNull BlockEntityType<?>>> tileTypeSupplier,
                    final Function<MultiblockPartBlock.@NotNull MultiblockPartProperties<ITurbinePartType>, @NotNull MultiblockPartBlock<Turbine, ITurbinePartType>> blockFactory,
                    final String translationKey) {
        this._properties = new MultiblockPartTypeProperties<>(tileTypeSupplier, blockFactory, translationKey, bp -> bp, ppf -> ppf);
    }

    @Override
    public MultiblockPartTypeProperties<Turbine, ITurbinePartType> getPartTypeProperties() {
        return this._properties;
    }

    @Override
    public String getSerializedName() {
        return this.name();
    }
}
