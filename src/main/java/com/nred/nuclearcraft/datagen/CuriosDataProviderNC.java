package com.nred.nuclearcraft.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import top.theillusivec4.curios.api.CuriosDataProvider;

import java.util.concurrent.CompletableFuture;

import static com.nred.nuclearcraft.NuclearcraftNeohaul.MODID;

public class CuriosDataProviderNC extends CuriosDataProvider {
    public CuriosDataProviderNC(PackOutput output, ExistingFileHelper fileHelper, CompletableFuture<HolderLookup.Provider> registries) {
        super(MODID, output, fileHelper, registries);
    }

    @Override
    public void generate(HolderLookup.Provider registries, ExistingFileHelper fileHelper) {
        this.createEntities("player").addPlayer().addSlots("curio");
    }
}