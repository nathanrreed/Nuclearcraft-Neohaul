package com.nred.nuclearcraft.datagen;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredItem;

import java.util.HashMap;
import java.util.List;

import static com.nred.nuclearcraft.NuclearcraftNeohaul.MODID;
import static com.nred.nuclearcraft.info.Names.*;
import static com.nred.nuclearcraft.registration.ItemRegistration.*;

class ModItemModelProvider extends ItemModelProvider {
    ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, MODID, existingFileHelper);
    }

//    public ModelBuilder<ItemModelBuilder> bucketItem(BucketItem item) {
//        withExistingParent(name(entry.getBucket()), entry.flowing().get().getFluidType().getDensity() < 0 ? neoLoc("item/bucket") : neoLoc("item/bucket_drip"))
//                .customLoader(DynamicFluidContainerModelBuilder::begin)
//                .fluid(entry.getStill())
//                .flipGas(entry.flowing().get().getFluidType().getDensity() < 0)
//                .applyTint(true);
//    }

//    public ItemModelBuilder metaItem(Item item) {
//        return metaItem(Objects.requireNonNull(BuiltInRegistries.ITEM.getKey(item)));
//    }

//    public ItemModelBuilder metaItem(ResourceLocation item) {
//        return getBuilder(this.folder + "/" + item.getPath()) // Force to put in items
//                .parent(new ModelFile.UncheckedModelFile("item/generated"))
//                .texture("layer0", ResourceLocation.fromNamespaceAndPath(item.getNamespace(), "item/" + item.getPath()));
//    }

    @Override
    public void registerModels() {
        simpleItems(INGOTS, INGOT_MAP, "ingot");
        simpleItems(GEMS, GEM_MAP, "gem");
        simpleItems(DUSTS, DUST_MAP, "dust");
        simpleItems(GEM_DUSTS, GEM_DUST_MAP, "gem_dust");
        simpleItems(RAWS, RAW_MAP, "raw");
        simpleItems(NUGGETS, NUGGET_MAP, "nugget");
        simpleItems(ALLOYS, ALLOY_MAP, "alloy");
        simpleItems(PARTS, PART_MAP, "part");
        simpleItems(COMPOUNDS, COMPOUND_MAP, "compound");
        simpleItems(UPGRADES, UPGRADE_MAP, "upgrade");
        simpleItems(URANIUMS, URANIUM_MAP, "uranium");

        simpleBlocks(List.of("empty_frame", "empty_heat_sink"), PART_BLOCK_MAP, "part");
    }

    private void simpleItems(List<String> list, HashMap<String, DeferredItem<Item>> map, String folder) {
        for (String name : list) {
            ResourceLocation item = BuiltInRegistries.ITEM.getKey(map.get(name).asItem());
            getBuilder(item.toString())
                    .parent(new ModelFile.UncheckedModelFile("item/generated"))
                    .texture("layer0", ResourceLocation.fromNamespaceAndPath(item.getNamespace(), "item/" + folder + "/" + name));
        }
    }

    private void simpleBlocks(List<String> list, HashMap<String, DeferredItem<Item>> map, String folder) {
        for (String name : list) {
            ResourceLocation item = BuiltInRegistries.ITEM.getKey(map.get(name).asItem());
            getBuilder(item.toString())
                    .parent(new ModelFile.UncheckedModelFile(BLOCK_FOLDER + "/cube_all"))
                    .texture("all", ResourceLocation.fromNamespaceAndPath(item.getNamespace(), "item/" + folder + "/" + name));
        }
    }

    private void modelBlock(List<String> list, HashMap<String, DeferredItem<Item>> map) {
        for (String name : list) {
            ResourceLocation item = BuiltInRegistries.ITEM.getKey(map.get(name).asItem());
            getBuilder(item.toString());
        }
    }
}