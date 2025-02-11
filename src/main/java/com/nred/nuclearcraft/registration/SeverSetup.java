package com.nred.nuclearcraft.registration;

import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.LootTableLoadEvent;

import static com.nred.nuclearcraft.NuclearcraftNeohaul.MODID;
import static com.nred.nuclearcraft.registration.ItemRegistration.*;

@EventBusSubscriber(modid = MODID, bus = EventBusSubscriber.Bus.GAME)
public class SeverSetup {
    @SubscribeEvent
    public static void onLootTableLoad(LootTableLoadEvent event) {
        if (BuiltInLootTables.SIMPLE_DUNGEON.location().equals(event.getName())) {
            event.getTable().addPool(LootPool.lootPool() // TODO check out this pool
                    .setRolls(UniformGenerator.between(1.0F, 3.0F))
                    .add(LootItem.lootTableItem(FOOD_MAP.get("dominos")).setWeight(11))
                    .add(LootItem.lootTableItem(FOOD_MAP.get("milk_chocolate")).setWeight(10))
                    .add(LootItem.lootTableItem(FOOD_MAP.get("marshmallow")).setWeight(6))
                    .add(LootItem.lootTableItem(FOOD_MAP.get("smore")).setWeight(12))
                    .add(LootItem.lootTableItem(FOURSMORE).setWeight(1))
                    .add(LootItem.lootTableItem(MUSIC_DISC_MAP.get("music_disc_end_of_the_world")).setWeight(15))
                    .add(LootItem.lootTableItem(MUSIC_DISC_MAP.get("music_disc_money_for_nothing")).setWeight(15))
                    .add(LootItem.lootTableItem(MUSIC_DISC_MAP.get("music_disc_wanderer")).setWeight(15))
                    .add(LootItem.lootTableItem(MUSIC_DISC_MAP.get("music_disc_hyperspace")).setWeight(15))
                    .build());
        }
    }
}