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
import static com.nred.nuclearcraft.registration.FluidRegistration.*;

public class ModFluidTagProvider extends FluidTagsProvider {

    public ModFluidTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> provider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, provider, MODID, existingFileHelper);
    }

    public static final TagKey<Fluid> CRYOTHEUM_TAG = FluidTags.create(ncLoc("cryotheum"));
    public static final TagKey<Fluid> STEAM_TAG = FluidTags.create(ResourceLocation.parse("c:steam"));
    public static final TagKey<Fluid> OXYGEN_TAG = FluidTags.create(ResourceLocation.parse("c:oxygen"));
    public static final TagKey<Fluid> NITROGEN_TAG = FluidTags.create(ResourceLocation.parse("c:nitrogen"));
    public static final TagKey<Fluid> TRITIUM_TAG = FluidTags.create(ResourceLocation.parse("c:tritium"));
    public static final TagKey<Fluid> HYDROGEN_TAG = FluidTags.create(ResourceLocation.parse("c:hydrogen"));
    public static final TagKey<Fluid> HELIUM_TAG = FluidTags.create(ResourceLocation.parse("c:helium"));

    // Used in INFILTRATOR_PRESSURE_DATA, but not created in mod
    public static final TagKey<Fluid> ARGON_TAG = FluidTags.create(ResourceLocation.parse("c:argon"));
    public static final TagKey<Fluid> NEON_TAG = FluidTags.create(ResourceLocation.parse("c:neon"));

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(CRYOTHEUM_TAG).add(CUSTOM_FLUID_MAP.get("cryotheum").still.get());
        tag(STEAM_TAG).add(STEAM_MAP.get("steam").still.get());
        tag(OXYGEN_TAG).add(GAS_MAP.get("oxygen").still.get());
        tag(NITROGEN_TAG).add(GAS_MAP.get("nitrogen").still.get());
        tag(TRITIUM_TAG).add(GAS_MAP.get("tritium").still.get());
        tag(HYDROGEN_TAG).add(GAS_MAP.get("hydrogen").still.get());
        tag(HELIUM_TAG).add(GAS_MAP.get("helium").still.get());
    }
}
