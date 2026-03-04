package com.nred.nuclearcraft.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.nred.nuclearcraft.capability.radiation.source.IRadiationSource;
import com.nred.nuclearcraft.radiation.RadiationHelper;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.world.level.chunk.LevelChunk;

public class CommandSetChunkRadiusRadiation {
    public static void register(CommandDispatcher<CommandSourceStack> command) {
        command.register(Commands.literal("nc_set_chunk_radius_radiation").requires(ctx -> ctx.hasPermission(Commands.LEVEL_GAMEMASTERS))
                .then(Commands.argument("radiation_level", DoubleArgumentType.doubleArg(0))
                        .then(Commands.argument("chunk_radius", IntegerArgumentType.integer()).executes(CommandSetChunkRadiusRadiation::execute)))
        );
    }

    private static int execute(CommandContext<CommandSourceStack> ctx) {
        double newRadiation = DoubleArgumentType.getDouble(ctx, "radiation_level");
        int radius = IntegerArgumentType.getInteger(ctx, "chunk_radius");

        int senderX = ctx.getSource().getPlayer().getBlockX() >> 4, senderZ = ctx.getSource().getPlayer().getBlockZ() >> 4;
        for (int x = -radius; x <= radius; ++x) {
            for (int z = -radius; z <= radius; ++z) {
                LevelChunk chunk = ctx.getSource().getLevel().getChunk(senderX + x, senderZ + z);
                if (chunk != null && chunk.loaded) {
                    IRadiationSource chunkSource = RadiationHelper.getRadiationSource(chunk);
                    if (chunkSource != null) {
                        chunkSource.setRadiationLevel(newRadiation);
                    }
                }
            }
        }
        return 0;
    }
}