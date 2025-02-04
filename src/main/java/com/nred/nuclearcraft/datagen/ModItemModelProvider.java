package com.nred.nuclearcraft.datagen;

import com.nred.nuclearcraft.info.Fluids;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.client.model.generators.loaders.DynamicFluidContainerModelBuilder;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredItem;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.nred.nuclearcraft.NuclearcraftNeohaul.MODID;
import static com.nred.nuclearcraft.helpers.Concat.fluidEntries;
import static com.nred.nuclearcraft.helpers.Location.neoLoc;
import static com.nred.nuclearcraft.info.Names.*;
import static com.nred.nuclearcraft.registration.FluidRegistration.*;
import static com.nred.nuclearcraft.registration.ItemRegistration.*;

public class ModItemModelProvider extends ItemModelProvider {
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

        buckets();
    }

    private void buckets() {
        for (Map.Entry<String, Fluids> entry : fluidEntries(GASSES, MOLTEN, CUSTOM_FLUID, HOT_GAS, SUGAR, CHOCOLATE, FISSION, STEAM, SALT_SOLUTION, ACID, FLAMMABLE, HOT_COOLANT, COOLANT)) {
            dynamicBucket(entry);
        }
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

    private void dynamicBucket(Map.Entry<String, Fluids> entry) {
        Fluids fluid = entry.getValue();
        withExistingParent(entry.getKey() + "_bucket", fluid.gaseous ? neoLoc("item/bucket") : neoLoc("item/bucket_drip"))
                .customLoader(DynamicFluidContainerModelBuilder::begin)
                .fluid(fluid.still.get()).flipGas(fluid.gaseous).applyTint(true);
    }
}