package com.nred.nuclearcraft.datagen;

import com.nred.nuclearcraft.advancements.AssembleTrigger;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementType;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.PlayerTrigger;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.advancements.AdvancementSubProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.common.data.AdvancementProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static com.nred.nuclearcraft.NuclearcraftNeohaul.MODID;
import static com.nred.nuclearcraft.helpers.Location.ncLoc;
import static com.nred.nuclearcraft.registration.BlockRegistration.*;
import static com.nred.nuclearcraft.registration.ItemRegistration.*;
import static com.nred.nuclearcraft.registration.TriggerTypeRegistration.SALT_FISSION_ASSEMBLE_TRIGGER;
import static com.nred.nuclearcraft.registration.TriggerTypeRegistration.SOLID_FISSION_ASSEMBLE_TRIGGER;
import static net.minecraft.advancements.Advancement.Builder.advancement;
import static net.minecraft.advancements.Advancement.Builder.recipeAdvancement;

public class ModAdvancementProvider extends AdvancementProvider {
    public ModAdvancementProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper existingFileHelper) {
        // Add an instance of our generator to the list parameter. This can be done as many times as you want.
        // Having multiple generators is purely for organization, all functionality can be achieved with a single generator.
        super(output, lookupProvider, existingFileHelper, List.of(new ModAdvancementGenerator()));
    }

    private static final class ModAdvancementGenerator implements AdvancementProvider.AdvancementGenerator {
        @Override
        public void generate(HolderLookup.Provider registries, Consumer<AdvancementHolder> saver, ExistingFileHelper existingFileHelper) {
            // All methods follow the builder pattern, meaning that chaining is possible and encouraged.
            // For better readability of the explanations, chaining will not be done here.

            // Create an advancement builder using the static #advancement() method.
            // Using #advancement() automatically enables telemetry events. If you do not want this,
            // #recipeAdvancement() can be used instead, there are no other functional differences.
            Advancement.Builder builder = advancement();

            // Sets the parent of the advancement. You can use another advancement you have already generated,
            // or create a placeholder advancement using the static AdvancementSubProvider#createPlaceholder method.
//            builder.parent(AdvancementSubProvider.createPlaceholder(MODID + ":root")); //AdvancementSubProvider.createPlaceholder("minecraft:story/root")

            // Sets the display properties of the advancement. This can either be a DisplayInfo object,
            // or pass in the values directly. If values are passed in directly, a DisplayInfo object will be created for you.
            builder.display(
                    // The advancement icon. Can be an ItemStack or an ItemLike.
                    PELLET_URANIUM_MAP.get("leu_233"),
                    // The advancement title and description. Don't forget to add translations for these!
                    Component.translatable("advancement." + MODID + ".root.title"),
                    Component.translatable("advancement." + MODID + ".root.description"),
                    // The background texture. Use null if you don't want a background texture (for non-root advancements).
                    ncLoc("textures/block/wasteland_earth.png"),
                    // The frame type. Valid values are AdvancementType.TASK, CHALLENGE, or GOAL.
                    AdvancementType.GOAL,
                    // Whether to show the advancement toast or not.
                    false,
                    // Whether to announce the advancement into chat or not.
                    false,
                    // Whether the advancement should be hidden or not.
                    false
            );

            // An advancement reward builder. Can be created with any of the four reward types, and further rewards
            // can be added using the methods prefixed with add. This can also be built beforehand,
            // and the resulting AdvancementRewards can then be reused across multiple advancement builders.
//            builder.rewards(
//                    // Alternatively, use addExperience() to add to an existing builder.
//                    AdvancementRewards.Builder.experience(100)
//                            // Alternatively, use loot() to create a new builder.
//                            .addLootTable(ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.fromNamespaceAndPath("minecraft", "chests/igloo")))
//                            // Alternatively, use recipe() to create a new builder.
//                            .addRecipe(ResourceLocation.fromNamespaceAndPath("minecraft", "iron_ingot"))
//                            // Alternatively, use function() to create a new builder.
//                            .runs(ncLoc("example_function"))
//            );

            // Adds a criterion with the given name to the advancement. Use the corresponding trigger instance's static method.
            builder.addCriterion("unlocked", PlayerTrigger.TriggerInstance.tick());

            // Adds a requirements handler. Minecraft natively provides allOf() and anyOf(), more complex requirements
            // must be implemented manually. Only has an effect with two or more criteria.
//            builder.requirements(AdvancementRequirements.allOf(List.of("pickup_dirt")));

            // Save the advancement to disk, using the given resource location. This returns an AdvancementHolder,
            // which may be stored in a variable and used as a parent by other advancement builders.
            builder.save(saver, ncLoc("root"), existingFileHelper);

            craft(PROCESSOR_MAP.get("manufactory"), ncLoc("root"), saver, existingFileHelper);
            craft(PROCESSOR_MAP.get("alloy_furnace"), ncLoc("manufactory"), saver, existingFileHelper);
            craft(PROCESSOR_MAP.get("assembler"), ncLoc("alloy_furnace"), saver, existingFileHelper);
            craft(PROCESSOR_MAP.get("centrifuge"), ncLoc("alloy_furnace"), saver, existingFileHelper);
            craft(PROCESSOR_MAP.get("chemical_reactor"), ncLoc("alloy_furnace"), saver, existingFileHelper);
            craft(PROCESSOR_MAP.get("crystallizer"), ncLoc("chemical_reactor"), saver, existingFileHelper);
            craft(PROCESSOR_MAP.get("electric_furnace"), ncLoc("manufactory"), saver, existingFileHelper);
            craft(PROCESSOR_MAP.get("electrolyzer"), ncLoc("alloy_furnace"), saver, existingFileHelper);
            craft(PROCESSOR_MAP.get("fluid_enricher"), ncLoc("chemical_reactor"), saver, existingFileHelper);
            craft(PROCESSOR_MAP.get("fluid_extractor"), ncLoc("alloy_furnace"), saver, existingFileHelper);
            craft(PROCESSOR_MAP.get("fuel_reprocessor"), ncLoc("alloy_furnace"), saver, existingFileHelper);
            craft(PROCESSOR_MAP.get("fluid_infuser"), ncLoc("alloy_furnace"), saver, existingFileHelper);
            craft(PROCESSOR_MAP.get("melter"), ncLoc("alloy_furnace"), saver, existingFileHelper);
            craft(PROCESSOR_MAP.get("ingot_former"), ncLoc("melter"), saver, existingFileHelper);
            craft(NUCLEAR_FURNACE, ncLoc("alloy_furnace"), saver, existingFileHelper);
            craft(PROCESSOR_MAP.get("pressurizer"), ncLoc("crystallizer"), saver, existingFileHelper);
            craft(PROCESSOR_MAP.get("rock_crusher"), ncLoc("alloy_furnace"), saver, existingFileHelper);
            craft(PROCESSOR_MAP.get("fluid_mixer"), ncLoc("alloy_furnace"), saver, existingFileHelper);
            craft(PROCESSOR_MAP.get("separator"), ncLoc("alloy_furnace"), saver, existingFileHelper);
            craft(PROCESSOR_MAP.get("supercooler"), ncLoc("alloy_furnace"), saver, existingFileHelper);
            craft(PROCESSOR_MAP.get("decay_hastener"), ncLoc("separator"), saver, existingFileHelper);

            craft(UPGRADE_MAP.get("energy"), ncLoc("manufactory"), saver, existingFileHelper);
            craft(UPGRADE_MAP.get("speed"), ncLoc("manufactory"), saver, existingFileHelper);
            craft(MACHINE_INTERFACE, ncLoc("manufactory"), saver, existingFileHelper);

            craft(DECAY_GENERATOR, ncLoc("root"), saver, existingFileHelper);
            craft(UNIVERSAL_BIN, ncLoc("root"), saver, existingFileHelper);
            craft(BATTERY_MAP.get("basic_voltaic_pile"), ncLoc("root"), saver, existingFileHelper);
            craft(BATTERY_MAP.get("basic_lithium_ion_battery"), ncLoc("basic_voltaic_pile"), saver, existingFileHelper);
            craft(SOLAR_MAP.get("solar_panel_basic"), ncLoc("decay_generator"), saver, existingFileHelper);

            craft(FISSION_REACTOR_MAP.get("solid_fuel_fission_controller"), ncLoc("separator"), saver, existingFileHelper);
            assembled("solid_fission_reactor_assembled", FISSION_REACTOR_MAP.get("solid_fuel_fission_controller"), ncLoc("solid_fuel_fission_controller"), SOLID_FISSION_ASSEMBLE_TRIGGER, saver, existingFileHelper);
            craft(FISSION_REACTOR_MAP.get("molten_salt_fission_controller"), ncLoc("solid_fission_reactor_assembled"), saver, existingFileHelper);
            assembled("salt_fission_reactor_assembled", FISSION_REACTOR_MAP.get("molten_salt_fission_controller"), ncLoc("molten_salt_fission_controller"), SALT_FISSION_ASSEMBLE_TRIGGER, saver, existingFileHelper);

            has(NEPTUNIUM_MAP, NEPTUNIUM_MAP.get("236"), "neptunium", ncLoc("solid_fission_reactor_assembled"), saver, existingFileHelper);
            has(PLUTONIUM_MAP, PLUTONIUM_MAP.get("239"), "plutonium", ncLoc("solid_fission_reactor_assembled"), saver, existingFileHelper);
            has(AMERICIUM_MAP, AMERICIUM_MAP.get("243"), "americium", ncLoc("plutonium"), saver, existingFileHelper);
            has(CURIUM_MAP, CURIUM_MAP.get("243"), "curium", ncLoc("plutonium"), saver, existingFileHelper);
            has(BERKELIUM_MAP, BERKELIUM_MAP.get("248"), "berkelium", ncLoc("curium"), saver, existingFileHelper);
            has(CALIFORNIUM_MAP, CALIFORNIUM_MAP.get("251"), "californium", ncLoc("curium"), saver, existingFileHelper);
            // TODO RTGs
        }

        private void assembled(String name, ItemLike icon, ResourceLocation parent, Supplier<AssembleTrigger> trigger, Consumer<AdvancementHolder> saver, ExistingFileHelper existingFileHelper) {
            Advancement.Builder builder = recipeAdvancement().parent(AdvancementSubProvider.createPlaceholder(parent.toString()));
            builder.display(
                    icon,
                    Component.translatable("advancement." + MODID + "." + name + ".title"),
                    Component.translatable("advancement." + MODID + "." + name + ".description"),
                    null,
                    AdvancementType.TASK,
                    false,
                    true,
                    false
            );
            builder.addCriterion("assemble_" + name, trigger.get().createCriterion(new AssembleTrigger.TriggerInstance(Optional.empty())));
            builder.requirements(AdvancementRequirements.allOf(List.of("assemble_" + name)));
            builder.save(saver, ncLoc(name), existingFileHelper);
        }

        private void craft(DeferredHolder<?, ?> item, ResourceLocation parent, Consumer<AdvancementHolder> saver, ExistingFileHelper existingFileHelper) {
            String name = item.getId().getPath();
            Advancement.Builder builder = recipeAdvancement().parent(AdvancementSubProvider.createPlaceholder(parent.toString()));
            builder.display(
                    (ItemLike) item,
                    Component.translatable("advancement." + MODID + "." + name + ".title"),
                    Component.translatable("advancement." + MODID + "." + name + ".description"),
                    null,
                    AdvancementType.TASK,
                    false,
                    false,
                    false
            );
            builder.addCriterion("pickup_" + name, InventoryChangeTrigger.TriggerInstance.hasItems((ItemLike) item));
            builder.requirements(AdvancementRequirements.allOf(List.of("pickup_" + name)));
            builder.save(saver, ncLoc(name), existingFileHelper);
        }

        private void has(HashMap<String, DeferredItem<Item>> items, ItemLike icon, String name, ResourceLocation parent, Consumer<AdvancementHolder> saver, ExistingFileHelper existingFileHelper) {
            Advancement.Builder builder = recipeAdvancement().parent(AdvancementSubProvider.createPlaceholder(parent.toString()));
            builder.display(
                    icon,
                    Component.translatable("advancement." + MODID + "." + name + ".title"),
                    Component.translatable("advancement." + MODID + "." + name + ".description"),
                    null,
                    AdvancementType.TASK,
                    false,
                    false,
                    false
            );
            builder.addCriterion("pickup_" + name, InventoryChangeTrigger.TriggerInstance.hasItems(items.values().stream().map(DeferredItem::asItem).toArray(Item[]::new)));
            builder.requirements(AdvancementRequirements.allOf(List.of("pickup_" + name)));
            builder.save(saver, ncLoc(name), existingFileHelper);
        }
    }
}