package com.nred.nuclearcraft.multiblock.fisson;

import com.nred.nuclearcraft.block.GenericTooltipDeviceBlock;
import com.nred.nuclearcraft.block.fission.FissionCasingBlock;
import com.nred.nuclearcraft.block.fission.FissionControllerBlock;
import com.nred.nuclearcraft.block.fission.FissionVentBlock;
import com.nred.nuclearcraft.block.fission.IFissionPartType;
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
    HeatSink(() -> FISSION_ENTITY_TYPE.get("heat_sink")::get, GenericTooltipDeviceBlock::new, "custom"),
    Heater(() -> FISSION_ENTITY_TYPE.get("coolant_heater")::get, GenericTooltipDeviceBlock::new, "custom");

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
