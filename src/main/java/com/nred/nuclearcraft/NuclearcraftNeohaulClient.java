package com.nred.nuclearcraft;

import com.nred.nuclearcraft.compat.guideme.ModGuideMEPlugin;
import com.nred.nuclearcraft.util.ModCheck;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;

@Mod(value = NuclearcraftNeohaul.MODID, dist = Dist.CLIENT)
public class NuclearcraftNeohaulClient {
    public NuclearcraftNeohaulClient(IEventBus modEventBus, ModContainer modContainer) {
        if (ModCheck.guidemeLoaded()) {
            ModGuideMEPlugin.createGuidebook();
        }
    }
}