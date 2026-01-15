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

    @Override
    public void registerModels() {
        simpleItems(INGOTS, INGOT_MAP, "ingot");
        simpleItems(GEMS, GEM_MAP, "gem");
        simpleItems(DUSTS, DUST_MAP, "dust");
        simpleItems(FISSION_DUSTS, FISSION_DUST_MAP, "fission_dust");
        simpleItems(GEM_DUSTS, GEM_DUST_MAP, "gem_dust");
        simpleItems(RAWS, RAW_MAP, "raw");
        simpleItems(NUGGETS, NUGGET_MAP, "nugget");
        simpleItems(ALLOYS, ALLOY_MAP, "alloy");
        simpleItems(PARTS, PART_MAP, "part");
        simpleItems(COMPOUNDS, COMPOUND_MAP, "compound");
        simpleItems(UPGRADES, UPGRADE_MAP, "upgrade");
        simpleItems(MUSIC_DISC_MAP, "music_disc");
        simpleItems(FOOD_MAP, "food");

        simpleItems(AMERICIUM_MAP, "americium");
        simpleItems(BERKELIUM_MAP, "berkelium");
        simpleItems(BORON_MAP, "boron");
        simpleItems(CALIFORNIUM_MAP, "californium");
        simpleItems(CURIUM_MAP, "curium");
        simpleItems(DEPLETED_FUEL_AMERICIUM_MAP, "depleted_fuel_americium");
        simpleItems(DEPLETED_FUEL_BERKELIUM_MAP, "depleted_fuel_berkelium");
        simpleItems(DEPLETED_FUEL_CALIFORNIUM_MAP, "depleted_fuel_californium");
        simpleItems(DEPLETED_FUEL_CURIUM_MAP, "depleted_fuel_curium");
        simpleItems(DEPLETED_FUEL_MIXED_MAP, "depleted_fuel_mixed");
        simpleItems(DEPLETED_FUEL_NEPTUNIUM_MAP, "depleted_fuel_neptunium");
        simpleItems(DEPLETED_FUEL_PLUTONIUM_MAP, "depleted_fuel_plutonium");
        simpleItems(DEPLETED_FUEL_THORIUM_MAP, "depleted_fuel_thorium");
        simpleItems(DEPLETED_FUEL_URANIUM_MAP, "depleted_fuel_uranium");
        simpleItems(FUEL_AMERICIUM_MAP, "fuel_americium");
        simpleItems(FUEL_BERKELIUM_MAP, "fuel_berkelium");
        simpleItems(FUEL_CALIFORNIUM_MAP, "fuel_californium");
        simpleItems(FUEL_CURIUM_MAP, "fuel_curium");
        simpleItems(FUEL_MIXED_MAP, "fuel_mixed");
        simpleItems(FUEL_NEPTUNIUM_MAP, "fuel_neptunium");
        simpleItems(FUEL_PLUTONIUM_MAP, "fuel_plutonium");
        simpleItems(FUEL_THORIUM_MAP, "fuel_thorium");
        simpleItems(FUEL_URANIUM_MAP, "fuel_uranium");
        simpleItems(PELLET_AMERICIUM_MAP, "fuel_americium");
        simpleItems(PELLET_BERKELIUM_MAP, "fuel_berkelium");
        simpleItems(PELLET_CALIFORNIUM_MAP, "fuel_californium");
        simpleItems(PELLET_CURIUM_MAP, "fuel_curium");
        simpleItems(PELLET_MIXED_MAP, "fuel_mixed");
        simpleItems(PELLET_NEPTUNIUM_MAP, "fuel_neptunium");
        simpleItems(PELLET_PLUTONIUM_MAP, "fuel_plutonium");
        simpleItems(PELLET_THORIUM_MAP, "fuel_thorium");
        simpleItems(PELLET_URANIUM_MAP, "fuel_uranium");
        simpleItems(LITHIUM_MAP, "lithium");
        simpleItems(NEPTUNIUM_MAP, "neptunium");
        simpleItems(PLUTONIUM_MAP, "plutonium");
        simpleItems(URANIUM_MAP, "uranium");

        simpleBlocks(List.of("empty_frame", "empty_heat_sink"), PART_BLOCK_MAP, "part");

        basicItem(PORTABLE_ENDER_CHEST.get());
        basicItem(LITHIUM_ION_CELL.get());
        basicItem(MULTITOOL.get());
        basicItem(GEIGER_COUNTER.get());
        basicItem(RADIATION_BADGE.get());

        basicItem(RADAWAY.get());
        basicItem(RADAWAY_SLOW.get());
        basicItem(RAD_X.get());

        simpleItem(LIGHT_RADIATION_SHIELDING, "rad_shielding/light");
        simpleItem(MEDIUM_RADIATION_SHIELDING, "rad_shielding/medium");
        simpleItem(HEAVY_RADIATION_SHIELDING, "rad_shielding/heavy");

        simpleItem(HAZMAT_HELMET, "armour/hazmat_helmet");
        simpleItem(HAZMAT_CHESTPLATE, "armour/hazmat_chestplate");
        simpleItem(HAZMAT_LEGGINGS, "armour/hazmat_leggings");
        simpleItem(HAZMAT_BOOTS, "armour/hazmat_boots");

        simpleItem(BORON_HELMET, "armour/boron_helmet");
        simpleItem(BORON_CHESTPLATE, "armour/boron_chestplate");
        simpleItem(BORON_LEGGINGS, "armour/boron_leggings");
        simpleItem(BORON_BOOTS, "armour/boron_boots");

        simpleItem(BORON_NITRIDE_HELMET, "armour/boron_nitride_helmet");
        simpleItem(BORON_NITRIDE_CHESTPLATE, "armour/boron_nitride_chestplate");
        simpleItem(BORON_NITRIDE_LEGGINGS, "armour/boron_nitride_leggings");
        simpleItem(BORON_NITRIDE_BOOTS, "armour/boron_nitride_boots");

        simpleItem(HARD_CARBON_HELMET, "armour/hard_carbon_helmet");
        simpleItem(HARD_CARBON_CHESTPLATE, "armour/hard_carbon_chestplate");
        simpleItem(HARD_CARBON_LEGGINGS, "armour/hard_carbon_leggings");
        simpleItem(HARD_CARBON_BOOTS, "armour/hard_carbon_boots");

        simpleItem(TOUGH_HELMET, "armour/tough_helmet");
        simpleItem(TOUGH_CHESTPLATE, "armour/tough_chestplate");
        simpleItem(TOUGH_LEGGINGS, "armour/tough_leggings");
        simpleItem(TOUGH_BOOTS, "armour/tough_boots");

        handheldItem(BORON_SWORD, "tool/boron_sword");
        handheldItem(BORON_PICKAXE, "tool/boron_pickaxe");
        handheldItem(BORON_SHOVEL, "tool/boron_shovel");
        handheldItem(BORON_AXE, "tool/boron_axe");
        handheldItem(BORON_HOE, "tool/boron_hoe");
        handheldItem(BORON_SPAXELHOE, "tool/boron_spaxelhoe");

        handheldItem(BORON_NITRIDE_SWORD, "tool/boron_nitride_sword");
        handheldItem(BORON_NITRIDE_PICKAXE, "tool/boron_nitride_pickaxe");
        handheldItem(BORON_NITRIDE_SHOVEL, "tool/boron_nitride_shovel");
        handheldItem(BORON_NITRIDE_AXE, "tool/boron_nitride_axe");
        handheldItem(BORON_NITRIDE_HOE, "tool/boron_nitride_hoe");
        handheldItem(BORON_NITRIDE_SPAXELHOE, "tool/boron_nitride_spaxelhoe");

        handheldItem(HARD_CARBON_SWORD, "tool/hard_carbon_sword");
        handheldItem(HARD_CARBON_PICKAXE, "tool/hard_carbon_pickaxe");
        handheldItem(HARD_CARBON_SHOVEL, "tool/hard_carbon_shovel");
        handheldItem(HARD_CARBON_AXE, "tool/hard_carbon_axe");
        handheldItem(HARD_CARBON_HOE, "tool/hard_carbon_hoe");
        handheldItem(HARD_CARBON_SPAXELHOE, "tool/hard_carbon_spaxelhoe");

        handheldItem(TOUGH_SWORD, "tool/tough_sword");
        handheldItem(TOUGH_PICKAXE, "tool/tough_pickaxe");
        handheldItem(TOUGH_SHOVEL, "tool/tough_shovel");
        handheldItem(TOUGH_AXE, "tool/tough_axe");
        handheldItem(TOUGH_HOE, "tool/tough_hoe");
        handheldItem(TOUGH_SPAXELHOE, "tool/tough_spaxelhoe");

        withExistingParent(FERAL_GHOUL_SPAWN_EGG.getId().getPath(), mcLoc("item/template_spawn_egg"));

        buckets();
    }

    private void buckets() {
        for (Map.Entry<String, Fluids> entry : fluidEntries(GAS_MAP, MOLTEN_MAP, CUSTOM_FLUID_MAP, HOT_GAS_MAP, SUGAR_MAP, CHOCOLATE_MAP, FISSION_FLUID_MAP, STEAM_MAP, SALT_SOLUTION_MAP, ACID_MAP, FLAMMABLE_MAP, HOT_COOLANT_MAP, COOLANT_MAP, FISSION_FUEL_MAP)) {
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

    private void simpleItem(DeferredItem<Item> deferredItem, String name) {
        buildItem(deferredItem, name, "item/generated");
    }

    private void handheldItem(DeferredItem<Item> deferredItem, String name) {
        buildItem(deferredItem, name, "item/handheld");
    }

    private void buildItem(DeferredItem<Item> deferredItem, String name, String parent) {
        ResourceLocation item = BuiltInRegistries.ITEM.getKey(deferredItem.asItem());
        getBuilder(item.toString())
                .parent(new ModelFile.UncheckedModelFile(parent))
                .texture("layer0", ResourceLocation.fromNamespaceAndPath(item.getNamespace(), "item/" + name));
    }

    private void simpleItems(HashMap<String, DeferredItem<Item>> map, String folder) {
        simpleItems(map.keySet().stream().toList(), map, folder);
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