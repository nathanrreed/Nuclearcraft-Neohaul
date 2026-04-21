package com.nred.nuclearcraft.compat.kubejs.fission;

import com.nred.nuclearcraft.multiblock.fisson.FissionPartType;
import com.nred.nuclearcraft.multiblock.fisson.FissionSourceType;
import dev.latvian.mods.kubejs.block.BlockBuilder;
import dev.latvian.mods.kubejs.block.BlockItemBuilder;
import dev.latvian.mods.kubejs.client.ModelGenerator;
import dev.latvian.mods.kubejs.client.VariantBlockStateGenerator;
import dev.latvian.mods.kubejs.generator.KubeAssetGenerator;
import dev.latvian.mods.kubejs.registry.AdditionalObjectRegistry;
import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.rhino.util.ReturnsSelf;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.ArrayList;

import static com.nred.nuclearcraft.compat.kubejs.BlockEntityTypeAddBlocksForKubeJS.BLOCK_ENTITY_TYPES_MAP;
import static com.nred.nuclearcraft.helpers.Location.ncLoc;
import static com.nred.nuclearcraft.registration.BlockEntityRegistration.FISSION_ENTITY_TYPE;
import static com.nred.nuclearcraft.registration.BlockRegistration.SOURCE_FUNCTION;

@ReturnsSelf
public class FissionSourceBuilder extends BlockBuilder {
    private static final ResourceLocation MODEL = ncLoc("block/machine");

    private FissionSourceType sourceType;

    public FissionSourceBuilder(ResourceLocation i) {
        super(i);

        defaultTags.add(BlockTags.MINEABLE_WITH_PICKAXE.location());
    }

    @Info("Sets the source efficiency.")
    public FissionSourceBuilder setSourceData(Double efficiency) {
        sourceType = new FissionSourceType(this.id.toString(), () -> efficiency);
        return this;
    }

    @Override
    protected void generateBlockState(VariantBlockStateGenerator bs) {
        ResourceLocation modelActive = newID("block/", "_on");
        ResourceLocation modelInactive = newID("block/", "_off");

        bs.variant("facing=east,nuclearcraftneohaul_active=false", v -> v.model(modelInactive).y(90));
        bs.variant("facing=east,nuclearcraftneohaul_active=true", v -> v.model(modelActive).y(90));
        bs.variant("facing=north,nuclearcraftneohaul_active=false", v -> v.model(modelInactive));
        bs.variant("facing=north,nuclearcraftneohaul_active=true", v -> v.model(modelActive));
        bs.variant("facing=south,nuclearcraftneohaul_active=false", v -> v.model(modelInactive).y(180));
        bs.variant("facing=south,nuclearcraftneohaul_active=true", v -> v.model(modelActive).y(180));
        bs.variant("facing=west,nuclearcraftneohaul_active=false", v -> v.model(modelInactive).y(270));
        bs.variant("facing=west,nuclearcraftneohaul_active=true", v -> v.model(modelActive).y(270));
    }

    @Override
    protected void generateBlockModels(KubeAssetGenerator gen) {
        gen.blockModel(id.withSuffix("_on"), m -> {
            m.parent(MODEL);
            m.texture("back", "nuclearcraftneohaul:block/fission/source/source_back_on");
            m.texture("bottom", "nuclearcraftneohaul:block/fission/source/source_side");
            m.texture("front", baseTexture);
            m.texture("side", "nuclearcraftneohaul:block/fission/source/source_side");
            m.texture("top", "nuclearcraftneohaul:block/fission/source/source_side");
        });
        gen.blockModel(id.withSuffix("_off"), m -> {
            m.parent(MODEL);
            m.texture("back", "nuclearcraftneohaul:block/fission/source/source_back_off");
            m.texture("bottom", "nuclearcraftneohaul:block/fission/source/source_side");
            m.texture("front", baseTexture);
            m.texture("side", "nuclearcraftneohaul:block/fission/source/source_side");
            m.texture("top", "nuclearcraftneohaul:block/fission/source/source_side");
        });
    }

    @Override
    protected void generateItemModel(ModelGenerator m) {
        m.parent(MODEL);
        m.texture("back", "nuclearcraftneohaul:block/fission/source/source_back_off");
        m.texture("bottom", "nuclearcraftneohaul:block/fission/source/source_side");
        m.texture("front", baseTexture);
        m.texture("side", "nuclearcraftneohaul:block/fission/source/source_side");
        m.texture("top", "nuclearcraftneohaul:block/fission/source/source_side");
    }

    @Override
    public Block createObject() {
        if (sourceType == null) throw new RuntimeException("Missing source data! Must be set with setSourceData(efficiency)");
        BLOCK_ENTITY_TYPES_MAP.computeIfAbsent(FISSION_ENTITY_TYPE.get("source").getId(), e -> new ArrayList<>()).add(this.id);
        return FissionPartType.Source.createBlock(sourceType);
    }

    @Override
    public void createAdditionalObjects(AdditionalObjectRegistry registry) {
        itemBuilder = new FissionSourceItemBuilder(id, sourceType);
        ((FissionSourceItemBuilder) itemBuilder).blockBuilder = this;
        registry.add(Registries.ITEM, itemBuilder);
    }

    private static class FissionSourceItemBuilder extends BlockItemBuilder {
        private final FissionSourceType sourceType;

        public FissionSourceItemBuilder(ResourceLocation id, FissionSourceType sourceType) {
            super(id);
            this.sourceType = sourceType;
        }

        @Override
        public Item createObject() {
            return SOURCE_FUNCTION.apply(this.blockBuilder.get(), sourceType);
        }
    }
}