package com.nred.nuclearcraft.multiblock.internal;

import com.nred.nuclearcraft.NuclearcraftNeohaul;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;

public class MultiblockValidationError {

    public static final MultiblockValidationError VALIDATION_ERROR_TOO_FEW_PARTS = new MultiblockValidationError("zerocore.api.nc.multiblock.validation.too_few_parts", null);

    public MultiblockValidationError(String messageFormatStringResourceKey, BlockPos pos, Object... messageParameters) {
        _resourceKey = messageFormatStringResourceKey;
        _parameters = messageParameters;
        this.pos = pos;
    }

    /**
     * @return the position of the last validation error encountered when trying to assemble the multiblock, or null if there is no position.
     */
    public BlockPos getErrorPos() {
        return pos;
    }

    public Component getChatMessage() {
        return Component.translatable(_resourceKey, _parameters);
    }

    public MultiblockValidationError updatedError(Level level) {
        if (pos == null) {
            return this;
        }
        if (_resourceKey.equals(NuclearcraftNeohaul.MODID + ".multiblock_validation.invalid_block")) {
            return new MultiblockValidationError(_resourceKey, pos, pos.getX(), pos.getY(), pos.getZ(), level.getBlockState(pos).getBlock().getName());
        }
        return this;
    }

    protected final String _resourceKey;
    protected final Object[] _parameters;
    protected final BlockPos pos;
}
