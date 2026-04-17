package com.nred.nuclearcraft.compat.kubejs;

import dev.latvian.mods.kubejs.plugin.KubeJSPlugin;
import dev.latvian.mods.kubejs.registry.BuilderTypeRegistry;
import net.minecraft.core.registries.Registries;

import static com.nred.nuclearcraft.helpers.Location.ncLoc;

public class ModKubeJSPlugin implements KubeJSPlugin {

    @Override
    public void registerBuilderTypes(BuilderTypeRegistry registry) {
        registry.of(Registries.BLOCK, reg -> {
            reg.add(ncLoc("rotor_blade"), RotorBladeBuilder.class, RotorBladeBuilder::new);
        });
    }
}