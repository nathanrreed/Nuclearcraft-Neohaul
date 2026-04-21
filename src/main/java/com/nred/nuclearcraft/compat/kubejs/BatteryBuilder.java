package com.nred.nuclearcraft.compat.kubejs;

import com.nred.nuclearcraft.multiblock.battery.BatteryPartType;
import com.nred.nuclearcraft.multiblock.battery.BatteryType;
import dev.latvian.mods.kubejs.block.BlockBuilder;
import dev.latvian.mods.kubejs.block.BlockItemBuilder;
import dev.latvian.mods.kubejs.client.ModelGenerator;
import dev.latvian.mods.kubejs.client.MultipartBlockStateGenerator;
import dev.latvian.mods.kubejs.generator.KubeAssetGenerator;
import dev.latvian.mods.kubejs.generator.KubeDataGenerator;
import dev.latvian.mods.kubejs.registry.AdditionalObjectRegistry;
import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.kubejs.util.ID;
import dev.latvian.mods.rhino.util.ReturnsSelf;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.CopyComponentsFunction;
import net.minecraft.world.level.storage.loot.predicates.ExplosionCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import static com.nred.nuclearcraft.compat.kubejs.BlockEntityTypeAddBlocksForKubeJS.BLOCK_ENTITY_TYPES_MAP;
import static com.nred.nuclearcraft.helpers.Location.ncLoc;
import static com.nred.nuclearcraft.registration.BlockEntityRegistration.BATTERY_ENTITY_TYPE;
import static com.nred.nuclearcraft.registration.BlockRegistration.BATTERY_FUNCTION;

@ReturnsSelf
public class BatteryBuilder extends BlockBuilder {
    private static final ResourceLocation MODEL = ncLoc("block/top_sides");
    private static final ResourceLocation MODEL_FACE = ncLoc("block/face");

    private BatteryType batteryType;

    public BatteryBuilder(ResourceLocation i) {
        super(i);

        defaultTags.add(BlockTags.MINEABLE_WITH_PICKAXE.location());
    }

    @Info("Sets the battery capacity and max_transfer.")
    public BatteryBuilder setBatteryData(Integer capacity, Integer max_transfer) {
        batteryType = new BatteryType(this.id.toString(), () -> capacity, () -> max_transfer);
        return this;
    }

    @Override
    protected boolean useMultipartBlockState() {
        return true;
    }

    @Override
    protected void generateMultipartBlockState(MultipartBlockStateGenerator bs) {
        ResourceLocation modelSideIn = newID("block/", "_side_in");
        ResourceLocation modelSideOut = newID("block/", "_side_out");
        ResourceLocation modelSideNon = newID("block/", "_side_non");
        ResourceLocation modelTopIn = newID("block/", "_top_in");
        ResourceLocation modelTopOut = newID("block/", "_top_out");
        ResourceLocation modelTopNon = newID("block/", "_top_non");

        bs.part("down=in", p -> p.model(modelTopIn).x(90));
        bs.part("east=in", p -> p.model(modelSideIn).y(90));
        bs.part("north=in", p -> p.model(modelSideIn));
        bs.part("south=in", p -> p.model(modelSideIn).y(180));
        bs.part("up=in", p -> p.model(modelTopIn).x(270));
        bs.part("west=in", p -> p.model(modelSideIn).y(270));

        bs.part("down=out", p -> p.model(modelTopOut).x(90));
        bs.part("east=out", p -> p.model(modelSideOut).y(90));
        bs.part("north=out", p -> p.model(modelSideOut));
        bs.part("south=out", p -> p.model(modelSideOut).y(180));
        bs.part("up=out", p -> p.model(modelTopOut).x(270));
        bs.part("west=out", p -> p.model(modelSideOut).y(270));

        bs.part("down=non", p -> p.model(modelTopNon).x(90));
        bs.part("east=non", p -> p.model(modelSideNon).y(90));
        bs.part("north=non", p -> p.model(modelSideNon));
        bs.part("south=non", p -> p.model(modelSideNon).y(180));
        bs.part("up=non", p -> p.model(modelTopNon).x(270));
        bs.part("west=non", p -> p.model(modelSideNon).y(270));
    }

    @Override
    protected void generateBlockModels(KubeAssetGenerator gen) {
        for (String st : List.of("side_in", "side_out", "side_non", "top_in", "top_out", "top_non")) {
            gen.blockModel(id.withSuffix("_" + st), m -> {
                m.parent(MODEL_FACE);
                m.texture("face", textures.getOrDefault(st, id.withPath(ID.BLOCK).withSuffix("_" + st).toString()));
            });
        }
    }

    @Override
    protected void generateItemModel(ModelGenerator m) {
        m.parent(MODEL);
        m.texture("sides", textures.getOrDefault("sides", id.withPath(ID.BLOCK).withSuffix("_side_in").toString()));
        m.texture("top", textures.getOrDefault("top", id.withPath(ID.BLOCK).withSuffix("_top_in").toString()));
    }

    @Override
    public @Nullable LootTable generateLootTable(KubeDataGenerator generator) {
        LootPool.Builder pool = new LootPool.Builder();
        pool.setRolls(ConstantValue.exactly(1.0f));
        pool.when(ExplosionCondition.survivesExplosion());
        pool.add(LootItem.lootTableItem(get().asItem()).apply(CopyComponentsFunction.copyComponents(CopyComponentsFunction.Source.BLOCK_ENTITY).include(DataComponents.CUSTOM_DATA).include(DataComponents.CUSTOM_NAME)));
        return new LootTable.Builder().withPool(pool).build();
    }

    @Override
    public Block createObject() {
        if (batteryType == null) throw new RuntimeException("Missing battery data! Must be set with setBatteryData(capacity, max_transfer)");
        BLOCK_ENTITY_TYPES_MAP.computeIfAbsent(BATTERY_ENTITY_TYPE.getId(), e -> new ArrayList<>()).add(this.id);
        return BatteryPartType.Battery.createBlock(batteryType);
    }

    @Override
    public void createAdditionalObjects(AdditionalObjectRegistry registry) {
        itemBuilder = new BatteryItemBuilder(id, batteryType);
        ((BatteryItemBuilder) itemBuilder).blockBuilder = this;
        registry.add(Registries.ITEM, itemBuilder);
    }

    private static class BatteryItemBuilder extends BlockItemBuilder {
        private final BatteryType batteryType;

        public BatteryItemBuilder(ResourceLocation id, BatteryType batteryType) {
            super(id);
            this.batteryType = batteryType;
        }

        @Override
        public Item createObject() {
            return BATTERY_FUNCTION.apply(this.blockBuilder.get());
        }
    }
}