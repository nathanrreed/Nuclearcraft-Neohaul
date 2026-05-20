package com.nred.nuclearcraft.multiblock.internal;

import it.zerono.mods.zerocore.lib.multiblock.validation.ValidationError;
import net.minecraft.core.BlockPos;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public class MultiblockValidationError extends ValidationError {
    private @Nullable Collection<BlockPos> _positions;

    public MultiblockValidationError(@Nullable Collection<BlockPos> positions, String messageFormatStringResourceKey, @Nullable Object... messageParameters) {
        super(null, messageFormatStringResourceKey, messageParameters);
        this._positions = positions;
    }

    public @Nullable Collection<BlockPos> getPositions() {
        return this._positions;
    }
}