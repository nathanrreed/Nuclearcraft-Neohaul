package com.nred.nuclearcraft.multiblock.turbine;

import com.nred.nuclearcraft.block.GenericDirectionalTooltipDeviceBlock;
import com.nred.nuclearcraft.block.GenericTooltipDeviceBlock;
import com.nred.nuclearcraft.block.turbine.*;
import it.zerono.mods.zerocore.base.multiblock.part.GlassBlock;
import it.zerono.mods.zerocore.lib.block.multiblock.MultiblockPartBlock;
import it.zerono.mods.zerocore.lib.block.multiblock.MultiblockPartTypeProperties;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;
import java.util.function.Supplier;

import static com.nred.nuclearcraft.registration.BlockEntityRegistration.TURBINE_ENTITY_TYPE;

public enum TurbinePartType implements ITurbinePartType {
    Controller(() -> TURBINE_ENTITY_TYPE.get("controller")::get, TurbineControllerBlock::new),
    Casing(() -> TURBINE_ENTITY_TYPE.get("casing")::get, TurbineCasingBlock::new),
    Glass(() -> TURBINE_ENTITY_TYPE.get("glass")::get, GlassBlock::new, GlassBlock::addGlassProperties),
    RotorBearing(() -> TURBINE_ENTITY_TYPE.get("rotor_bearing")::get, GenericTooltipDeviceBlock::new),
    RotorShaft(() -> TURBINE_ENTITY_TYPE.get("rotor_shaft")::get, TurbineRotorShaftBlock::new),
    RotorBlade(() -> TURBINE_ENTITY_TYPE.get("rotor_blade")::get, TurbineRotorBladeBlock::new),
    RotorStator(() -> TURBINE_ENTITY_TYPE.get("rotor_stator")::get, TurbineRotorStatorBlock::new),
    Outlet(() -> TURBINE_ENTITY_TYPE.get("outlet")::get, GenericDirectionalTooltipDeviceBlock::new),
    Inlet(() -> TURBINE_ENTITY_TYPE.get("inlet")::get, GenericDirectionalTooltipDeviceBlock::new),
    Dynamo(() -> TURBINE_ENTITY_TYPE.get("dynamo")::get, GenericTooltipDeviceBlock::new),
    DynamoConnector(() -> TURBINE_ENTITY_TYPE.get("coil_connector")::get, GenericTooltipDeviceBlock::new),
    ComputerPort(() -> TURBINE_ENTITY_TYPE.get("computer_port")::get, GenericTooltipDeviceBlock::new),
    RedstonePort(() -> TURBINE_ENTITY_TYPE.get("redstone_port")::get, TurbineRedstonePortBlock::new),
    ;

    private final MultiblockPartTypeProperties<Turbine, ITurbinePartType> _properties;

    TurbinePartType(final Supplier<@NotNull Supplier<@NotNull BlockEntityType<?>>> tileTypeSupplier,
                    final Function<MultiblockPartBlock.@NotNull MultiblockPartProperties<ITurbinePartType>, @NotNull MultiblockPartBlock<Turbine, ITurbinePartType>> blockFactory,
                    final Function<Block.@NotNull Properties, Block.@NotNull Properties> blockPropertiesFixer) {
        this._properties = new MultiblockPartTypeProperties<>(tileTypeSupplier, blockFactory, "", blockPropertiesFixer, ppf -> ppf);
    }

    TurbinePartType(final Supplier<@NotNull Supplier<@NotNull BlockEntityType<?>>> tileTypeSupplier,
                    final Function<MultiblockPartBlock.@NotNull MultiblockPartProperties<ITurbinePartType>, @NotNull MultiblockPartBlock<Turbine, ITurbinePartType>> blockFactory) {
        this._properties = new MultiblockPartTypeProperties<>(tileTypeSupplier, blockFactory, "", bp -> bp, ppf -> ppf);
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
