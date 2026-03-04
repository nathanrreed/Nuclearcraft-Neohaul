package com.nred.nuclearcraft.command;

import net.neoforged.neoforge.event.RegisterCommandsEvent;

public class CommandHandler {
	public static void registerCommands(RegisterCommandsEvent event) {
		CommandSetChunkRadiation.register(event.getDispatcher());
		CommandSetChunkRadiusRadiation.register(event.getDispatcher());
		CommandSetWorldRadiation.register(event.getDispatcher());
		CommandSetPlayerRadiation.register(event.getDispatcher());
//		CommandReconstructScriptAddons.register(event.getDispatcher()); TODO
	}
}