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
import static com.nred.nuclearcraft.info.Names.GAS_COOLANTS;
import static com.nred.nuclearcraft.registration.FluidRegistration.*;

public class ModFluidTagProvider extends FluidTagsProvider {

    public ModFluidTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> provider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, provider, MODID, existingFileHelper);
    }

    public static final TagKey<Fluid> CRYOTHEUM_TAG = FluidTags.create(ResourceLocation.parse("c:cryotheum"));
    public static final TagKey<Fluid> ENDERIUM_TAG = FluidTags.create(ResourceLocation.parse("c:enderium"));
    public static final TagKey<Fluid> STEAM_TAG = FluidTags.create(ResourceLocation.parse("c:steam"));
    public static final TagKey<Fluid> OXYGEN_TAG = FluidTags.create(ResourceLocation.parse("c:oxygen"));
    public static final TagKey<Fluid> NITROGEN_TAG = FluidTags.create(ResourceLocation.parse("c:nitrogen"));
    public static final TagKey<Fluid> TRITIUM_TAG = FluidTags.create(ResourceLocation.parse("c:tritium"));
    public static final TagKey<Fluid> HYDROGEN_TAG = FluidTags.create(ResourceLocation.parse("c:hydrogen"));
    public static final TagKey<Fluid> HELIUM_TAG = FluidTags.create(ResourceLocation.parse("c:helium"));
    public static final TagKey<Fluid> SODIUM_TAG = FluidTags.create(ResourceLocation.parse("c:sodium"));

    // Used in INFILTRATOR_PRESSURE_DATA, but not created in mod
    public static final TagKey<Fluid> ARGON_TAG = FluidTags.create(ResourceLocation.parse("c:argon"));
    public static final TagKey<Fluid> NEON_TAG = FluidTags.create(ResourceLocation.parse("c:neon"));

    public static final TagKey<Fluid> DEUTERIUM_TAG = FluidTags.create(ResourceLocation.parse("c:deuterium"));
    public static final TagKey<Fluid> HELIUM_3_TAG = FluidTags.create(ResourceLocation.parse("c:helium_3"));

    public static final TagKey<Fluid> HYDROFLUORIC_ACID_TAG = FluidTags.create(ResourceLocation.parse("c:hydrofluoric_acid"));
    public static final TagKey<Fluid> BORIC_ACID_TAG = FluidTags.create(ResourceLocation.parse("c:boric_acid"));
    public static final TagKey<Fluid> SULFURIC_ACID_TAG = FluidTags.create(ResourceLocation.parse("c:sulfuric_acid"));
    public static final TagKey<Fluid> ORTHOSILICIC_ACID_TAG = FluidTags.create(ResourceLocation.parse("c:orthosilicic_acid"));

    public static final TagKey<Fluid> LIQUID_HELIUM_TAG = FluidTags.create(ResourceLocation.parse("c:liquid_helium"));
    public static final TagKey<Fluid> LIQUID_NITROGEN_TAG = FluidTags.create(ResourceLocation.parse("c:liquid_nitrogen"));

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(CRYOTHEUM_TAG).add(CUSTOM_FLUID_MAP.get("cryotheum").still.get());
        tag(STEAM_TAG).add(STEAM_MAP.get("steam").still.get());
        tag(TRITIUM_TAG).add(GAS_MAP.get("tritium").still.get());

        for (String gas : GAS_COOLANTS) {
            tag(FluidTags.create(ResourceLocation.fromNamespaceAndPath("c", gas))).add(GAS_MAP.get(gas).still.get());
        }

        for (String molten : MOLTEN_MAP.keySet()) { // TODO use these tags
            tag(FluidTags.create(ResourceLocation.fromNamespaceAndPath("c", molten))).add(MOLTEN_MAP.get(molten).still.get());
        }

        tag(HYDROFLUORIC_ACID_TAG).add(ACID_MAP.get("hydrofluoric_acid").still.get());
        tag(BORIC_ACID_TAG).add(ACID_MAP.get("boric_acid").still.get());
        tag(SULFURIC_ACID_TAG).add(ACID_MAP.get("sulfuric_acid").still.get());
        tag(ORTHOSILICIC_ACID_TAG).add(ACID_MAP.get("orthosilicic_acid").still.get());

        tag(DEUTERIUM_TAG).add(GAS_MAP.get("deuterium").still.get());
        tag(HELIUM_3_TAG).add(GAS_MAP.get("helium_3").still.get());

        tag(LIQUID_HELIUM_TAG).add(CUSTOM_FLUID_MAP.get("liquid_helium").still.get());
        tag(LIQUID_NITROGEN_TAG).add(CUSTOM_FLUID_MAP.get("liquid_nitrogen").still.get());
    }

    public static TagKey<Fluid> fluidTag(String name){
        return FluidTags.create(ResourceLocation.fromNamespaceAndPath("c", name));
    }
}
