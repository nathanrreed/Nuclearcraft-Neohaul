package com.nred.nuclearcraft.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.nred.nuclearcraft.capability.radiation.entity.IEntityRads;
import com.nred.nuclearcraft.radiation.RadiationHelper;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.server.level.ServerPlayer;

public class CommandSetPlayerRadiation {
    public static void register(CommandDispatcher<CommandSourceStack> command) {
        command.register(Commands.literal("nc_set_player_radiation").requires(ctx -> ctx.hasPermission(Commands.LEVEL_GAMEMASTERS))
                .then(Commands.argument("radiation_level", DoubleArgumentType.doubleArg(0)).executes(ctx -> execute(ctx, false))
                        .then(Commands.argument("player", EntityArgument.player()).executes(ctx -> execute(ctx, true))))
        );
    }

    private static int execute(CommandContext<CommandSourceStack> ctx, boolean hasPlayer) throws CommandSyntaxException {
        double newRadiation = DoubleArgumentType.getDouble(ctx, "radiation_level");

        ServerPlayer player = hasPlayer ? EntityArgument.getPlayer(ctx, "player") : ctx.getSource().getPlayer();
        if (player != null) {
            IEntityRads playerRads = RadiationHelper.getEntityRadiation(player);
            if (playerRads != null) {
                playerRads.setTotalRads(newRadiation, false);
            }

            return 1;
        }
        return 0;
    }
}
