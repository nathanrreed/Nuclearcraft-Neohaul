package com.nred.nuclearcraft.compat.sable;

import com.nred.nuclearcraft.capability.radiation.source.IRadiationSource;
import com.nred.nuclearcraft.radiation.RadiationHelper;
import dev.ryanhcode.sable.companion.SableCompanion;
import dev.ryanhcode.sable.companion.SubLevelAccess;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.ChunkAccess;
import org.joml.Vector3dc;

public class SableUtil {
    public static IRadiationSource getSableChunkSource(Level level, ChunkAccess chunk) {
        SubLevelAccess subLevelAccess = SableCompanion.INSTANCE.getContaining(level, chunk.getPos());
        if (subLevelAccess != null) {
            Vector3dc pos = subLevelAccess.logicalPose().position();
            return RadiationHelper.getRadiationSource(level.getChunkAt(new BlockPos(Mth.floor(pos.x()), Mth.floor(pos.y()), Mth.floor(pos.z()))));
        }
        return null;
    }
}