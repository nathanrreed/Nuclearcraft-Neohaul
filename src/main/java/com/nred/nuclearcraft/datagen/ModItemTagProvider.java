package com.nred.nuclearcraft.datagen;

import com.nred.nuclearcraft.info.Fluids;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredItem;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static com.nred.nuclearcraft.NuclearcraftNeohaul.MODID;
import static com.nred.nuclearcraft.helpers.Concat.fluidValues;
import static com.nred.nuclearcraft.info.Names.*;
import static com.nred.nuclearcraft.registration.BlockRegistration.GLOWING_MUSHROOM;
import static com.nred.nuclearcraft.registration.BlockRegistration.ORE_MAP;
import static com.nred.nuclearcraft.registration.FluidRegistration.*;
import static com.nred.nuclearcraft.registration.FluidRegistration.FISSION_FUEL_MAP;
import static com.nred.nuclearcraft.registration.ItemRegistration.*;

class ModItemTagProvider extends ItemTagsProvider {
    public ModItemTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, CompletableFuture<TagLookup<Block>> blockTags, ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, blockTags, MODID, existingFileHelper);
    }

    @Override
    public void addTags(HolderLookup.Provider provider) {
        for (String name : ORES) {
            tag(Tags.Items.ORES).add(ORE_MAP.get(name).asItem(), ORE_MAP.get(name + "_deepslate").asItem());
            tag(ItemTags.create(Tags.Items.ORES.location().withSuffix("/" + name))).add(ORE_MAP.get(name).asItem(), ORE_MAP.get(name + "_deepslate").asItem());
            tag(Tags.Items.ORES_IN_GROUND_DEEPSLATE).add(ORE_MAP.get(name + "_deepslate").asItem());
            tag(Tags.Items.ORES_IN_GROUND_STONE).add(ORE_MAP.get(name).asItem());
        }
        simpleTag(INGOTS, INGOT_MAP, Tags.Items.INGOTS);
        simpleTag(ALLOYS, ALLOY_MAP, Tags.Items.INGOTS);
        simpleTag(GEMS, GEM_MAP, Tags.Items.GEMS);
        simpleTag(DUSTS, DUST_MAP, Tags.Items.DUSTS);
        simpleTag(GEM_DUSTS, GEM_DUST_MAP, Tags.Items.DUSTS);
        simpleTag(COMPOUNDS, COMPOUND_MAP, Tags.Items.DUSTS);
        simpleTag(RAWS, RAW_MAP, Tags.Items.RAW_MATERIALS);
        simpleTag(NUGGETS, NUGGET_MAP, Tags.Items.NUGGETS);
        simpleTag(MUSIC_DISC_MAP, Tags.Items.MUSIC_DISCS);
        simpleTag(FOOD_MAP, Tags.Items.FOODS);

        tag(Tags.Items.MUSHROOMS).add(GLOWING_MUSHROOM.asItem());

        buckets();
    }

    private void buckets() {
        for (Fluids fluid : fluidValues(GAS_MAP, MOLTEN_MAP, CUSTOM_FLUID_MAP, HOT_GAS_MAP, SUGAR_MAP, CHOCOLATE_MAP, FISSION_FLUID_MAP, STEAM_MAP, SALT_SOLUTION_MAP, ACID_MAP, FLAMMABLE_MAP, HOT_COOLANT_MAP, COOLANT_MAP, FISSION_FUEL_MAP)) {
            tag(Tags.Items.BUCKETS).add(fluid.bucket.get());
        }
    }

    private void simpleTag(List<String> list, HashMap<String, DeferredItem<Item>> map, TagKey<Item> tag) {
        for (String name : list) {
            tag(tag).add(map.get(name).asItem());
            tag(ItemTags.create(tag.location().withSuffix("/" + name))).add(map.get(name).asItem());
        }
    }

    private void simpleTag(HashMap<String, DeferredItem<Item>> map, TagKey<Item> tag) {
        simpleTag(map.keySet().stream().toList(), map, tag);
    }
}