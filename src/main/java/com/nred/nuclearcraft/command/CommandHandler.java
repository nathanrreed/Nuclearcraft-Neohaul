package com.nred.nuclearcraft.command;

import net.neoforged.neoforge.event.RegisterCommandsEvent;

public class CommandHandler {
    public static void registerCommands(RegisterCommandsEvent event) {
        CommandSetChunkRadiation.register(event.getDispatcher());
        CommandSetChunkRadiusRadiation.register(event.getDispatcher());
        CommandSetWorldRadiation.register(event.getDispatcher());
        CommandSetPlayerRadiation.register(event.getDispatcher());


//        event.getDispatcher().register(Commands.literal("nc_test").executes(ctx -> {  // For Testing places all blocks
//                    BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos().set(0, 90, 0);
//                    for (ResourceLocation resourceLocation : BuiltInRegistries.BLOCK.keySet()) {
//                        if (resourceLocation.getNamespace().equals(NuclearcraftNeohaul.MODID) && !resourceLocation.getPath().contains("fission_cooler")) {
//                            ctx.getSource().getLevel().setBlockAndUpdate(pos, BuiltInRegistries.BLOCK.get(resourceLocation).defaultBlockState());
//
//                            if (pos.getX() == 64) {
//                                pos.move(-64, 0, 1);
//                            } else {
//                                pos.move(1, 0, 0);
//                            }
//                        }
//                    }
//                    return 0;
//                }
//        ));

//		CommandReconstructScriptAddons.register(event.getDispatcher()); TODO
    }
}