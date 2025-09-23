package com.nred.nuclearcraft.multiblock.turbine;

import com.nred.nuclearcraft.block.HorizontalGenericDeviceBlock;
import com.nred.nuclearcraft.block.turbine.ITurbinePartType;
import com.nred.nuclearcraft.block.turbine.TurbineRotorBladeBlock;
import com.nred.nuclearcraft.block.turbine.TurbineRotorShaftBlock;
import com.nred.nuclearcraft.block.turbine.TurbineRotorStatorBlock;
import it.zerono.mods.zerocore.base.multiblock.part.GenericDeviceBlock;
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
    Casing(() -> TURBINE_CASING::get, MultiblockPartBlock::new, ""), //TODO ADD TRANSLATION
    Glass(() -> TURBINE_GLASS::get, GlassBlock::new, "", GlassBlock::addGlassProperties),
    Controller(() -> TURBINE_CONTROLLER::get, HorizontalGenericDeviceBlock::new, ""),
    RotorBearing(() -> TURBINE_ROTOR_BEARING::get, GenericDeviceBlock::new, ""),
    RotorShaft(() -> TURBINE_ROTOR_SHAFT::get, TurbineRotorShaftBlock::new, ""),
    Outlet(() -> TURBINE_OUTLET::get, HorizontalGenericDeviceBlock::new, ""),
    Inlet(() -> TURBINE_INLET::get, HorizontalGenericDeviceBlock::new, ""),
    RotorBlade(() -> TURBINE_ROTOR_BLADE::get, TurbineRotorBladeBlock::new, ""),
    RotorStator(() -> TURBINE_ROTOR_STATOR::get, TurbineRotorStatorBlock::new, ""),
    Dynamo(() -> TURBINE_DYNAMO::get, MultiblockPartBlock::new, ""),
    DynamoConnector(() -> TURBINE_DYNAMO::get, MultiblockPartBlock::new, ""),
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
