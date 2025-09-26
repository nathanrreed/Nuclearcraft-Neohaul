package com.nred.nuclearcraft.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.FluidTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

import static com.nred.nuclearcraft.NuclearcraftNeohaul.MODID;
import static com.nred.nuclearcraft.helpers.Location.ncLoc;
import static com.nred.nuclearcraft.registration.FluidRegistration.CUSTOM_FLUID_MAP;
import static com.nred.nuclearcraft.registration.FluidRegistration.STEAM_MAP;

public class ModFluidTagProvider extends FluidTagsProvider {

    public ModFluidTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> provider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, provider, MODID, existingFileHelper);
    }

    public static final TagKey<Fluid> CRYOTHEUM_KEY = FluidTags.create(ncLoc("cryotheum"));
    public static final TagKey<Fluid> STEAM_KEY = FluidTags.create(ResourceLocation.parse("c:steam"));

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(CRYOTHEUM_KEY).add(CUSTOM_FLUID_MAP.get("cryotheum").still.get());
        tag(STEAM_KEY).add(STEAM_MAP.get("steam").still.get());
    }
}
