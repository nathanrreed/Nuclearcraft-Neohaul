package com.nred.nuclearcraft.multiblock.rtg;

import com.nred.nuclearcraft.block.rtg.RTGBlock;
import it.zerono.mods.zerocore.lib.block.multiblock.MultiblockPartBlock;
import it.zerono.mods.zerocore.lib.block.multiblock.MultiblockPartTypeProperties;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;
import java.util.function.Supplier;

import static com.nred.nuclearcraft.registration.BlockEntityRegistration.RTG_ENTITY_TYPE;

public enum RTGPartType implements IRTGPartType {
    RTG(() -> RTG_ENTITY_TYPE::get, RTGBlock::new, "");

    private final MultiblockPartTypeProperties<RTGMultiblock, IRTGPartType> _properties;

    RTGPartType(final Supplier<@NotNull Supplier<@NotNull BlockEntityType<?>>> tileTypeSupplier,
                final Function<MultiblockPartBlock.@NotNull MultiblockPartProperties<IRTGPartType>, @NotNull MultiblockPartBlock<RTGMultiblock, IRTGPartType>> blockFactory,
                final String translationKey) {
        this._properties = new MultiblockPartTypeProperties<>(tileTypeSupplier, blockFactory, translationKey, bp -> bp, ppf -> ppf);
    }

    @Override
    public MultiblockPartTypeProperties<RTGMultiblock, IRTGPartType> getPartTypeProperties() {
        return this._properties;
    }

    @Override
    public String getSerializedName() {
        return this.name();
    }
}
