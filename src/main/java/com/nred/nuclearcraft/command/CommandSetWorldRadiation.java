package com.nred.nuclearcraft.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.nred.nuclearcraft.capability.radiation.source.IRadiationSource;
import com.nred.nuclearcraft.radiation.RadiationHelper;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ChunkHolder;
import net.minecraft.world.level.chunk.LevelChunk;

public class CommandSetWorldRadiation {
    public static void register(CommandDispatcher<CommandSourceStack> command) {
        command.register(Commands.literal("nc_set_world_radiation").requires(ctx -> ctx.hasPermission(Commands.LEVEL_GAMEMASTERS))
                .then(Commands.argument("radiation_level", DoubleArgumentType.doubleArg(0)).executes(ctx -> execute(ctx))));
    }

    private static int execute(CommandContext<CommandSourceStack> ctx) {
        double newRadiation = DoubleArgumentType.getDouble(ctx, "radiation_level");

        for (ChunkHolder chunk : ctx.getSource().getLevel().getChunkSource().chunkMap.getChunks()) {
            LevelChunk loadedChunk = chunk.getTickingChunk();
            if (loadedChunk != null && loadedChunk.loaded) {
                IRadiationSource chunkSource = RadiationHelper.getRadiationSource(loadedChunk);
                if (chunkSource != null) {
                    chunkSource.setRadiationLevel(newRadiation);
                }
            }
        }
        return 1;
    }
}