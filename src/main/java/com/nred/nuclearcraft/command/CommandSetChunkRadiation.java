package com.nred.nuclearcraft.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.nred.nuclearcraft.capability.radiation.source.IRadiationSource;
import com.nred.nuclearcraft.radiation.RadiationHelper;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.coordinates.ColumnPosArgument;
import net.minecraft.server.level.ColumnPos;
import net.minecraft.world.level.chunk.LevelChunk;

public class CommandSetChunkRadiation {
    public static void register(CommandDispatcher<CommandSourceStack> command) {
        command.register(Commands.literal("nc_set_chunk_radiation").requires(ctx -> ctx.hasPermission(Commands.LEVEL_GAMEMASTERS))
                .then(Commands.argument("radiation_level", DoubleArgumentType.doubleArg(0)).executes(ctx -> execute(ctx, true))
                        .then(Commands.argument("chunk", ColumnPosArgument.columnPos()).executes(ctx -> execute(ctx, false))))
        );
    }

    private static int execute(CommandContext<CommandSourceStack> ctx, boolean currentPos) {
        double newRadiation = DoubleArgumentType.getDouble(ctx, "radiation_level");

        if (currentPos) {
            LevelChunk chunk = ctx.getSource().getLevel().getChunk(ctx.getSource().getPlayer().getBlockX(), ctx.getSource().getPlayer().getBlockZ());
            if (chunk != null && chunk.loaded) {
                IRadiationSource chunkSource = RadiationHelper.getRadiationSource(chunk);
                if (chunkSource != null) {
                    chunkSource.setRadiationLevel(newRadiation);
                }
            }
        } else {
            ColumnPos pos = ColumnPosArgument.getColumnPos(ctx, "chunk");

            LevelChunk chunk = ctx.getSource().getLevel().getChunk(pos.toChunkPos().x, pos.toChunkPos().z);
            if (chunk != null && chunk.loaded) {
                IRadiationSource chunkSource = RadiationHelper.getRadiationSource(chunk);
                if (chunkSource != null) {
                    chunkSource.setRadiationLevel(newRadiation);
                }
            }
        }

        return 1;
    }
}