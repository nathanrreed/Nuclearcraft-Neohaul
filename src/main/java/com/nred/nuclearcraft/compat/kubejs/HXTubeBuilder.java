package com.nred.nuclearcraft.compat.kubejs;

import com.nred.nuclearcraft.multiblock.hx.HeatExchangerPartType;
import com.nred.nuclearcraft.multiblock.hx.HeatExchangerTubeType;
import dev.latvian.mods.kubejs.block.BlockBuilder;
import dev.latvian.mods.kubejs.block.BlockItemBuilder;
import dev.latvian.mods.kubejs.client.ModelGenerator;
import dev.latvian.mods.kubejs.client.MultipartBlockStateGenerator;
import dev.latvian.mods.kubejs.generator.KubeAssetGenerator;
import dev.latvian.mods.kubejs.registry.AdditionalObjectRegistry;
import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.kubejs.util.ID;
import dev.latvian.mods.rhino.util.ReturnsSelf;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.ArrayList;

import static com.nred.nuclearcraft.compat.kubejs.BlockEntityTypeAddBlocksForKubeJS.BLOCK_ENTITY_TYPES_MAP;
import static com.nred.nuclearcraft.helpers.Location.ncLoc;
import static com.nred.nuclearcraft.registration.BlockEntityRegistration.HX_ENTITY_TYPE;
import static com.nred.nuclearcraft.registration.BlockRegistration.HX_TUBE_FUNCTION;

@ReturnsSelf
public class HXTubeBuilder extends BlockBuilder {
    private static final ResourceLocation MODEL_CENTER = ncLoc("block/heat_exchanger_tube_center");
    private static final ResourceLocation MODEL_OPEN = ncLoc("block/heat_exchanger_tube_open");
    private static final ResourceLocation MODEL_OPEN_BAFFLE = ncLoc("block/heat_exchanger_tube_open_baffle");
    private static final ResourceLocation MODEL_CLOSED_BAFFLE = ncLoc("block/heat_exchanger_tube_closed_baffle");

    private HeatExchangerTubeType hxTubeType;

    public HXTubeBuilder(ResourceLocation i) {
        super(i);

        defaultTags.add(BlockTags.MINEABLE_WITH_PICKAXE.location());
    }

    @Info("Sets the HX tube heatTransferCoefficient and heatRetentionMult.")
    public HXTubeBuilder setHXTubeData(Double heatTransferCoefficient, Double heatRetentionMult) {
        hxTubeType = new HeatExchangerTubeType(this.id.toString(), () -> heatTransferCoefficient, () -> heatRetentionMult);
        return this;
    }

    @Override
    protected boolean useMultipartBlockState() {
        return true;
    }

    @Override
    protected void generateMultipartBlockState(MultipartBlockStateGenerator bs) {
        ResourceLocation modelCenter = newID("block/", "_center");
        ResourceLocation modelOpen = newID("block/", "_open");
        ResourceLocation modelOpenBaffle = newID("block/", "_open_baffle");

        bs.part("", modelCenter);
        bs.part("down=open", p -> p.model(modelOpen).x(90).uvlock());
        bs.part("east=open", p -> p.model(modelOpen).y(90).uvlock());
        bs.part("north=open", p -> p.model(modelOpen).uvlock());
        bs.part("south=open", p -> p.model(modelOpen).y(180).uvlock());
        bs.part("up=open", p -> p.model(modelOpen).x(270).uvlock());
        bs.part("west=open", p -> p.model(modelOpen).y(270).uvlock());

        bs.part("down=closed_baffle", p -> p.model(MODEL_CLOSED_BAFFLE).x(90).uvlock());
        bs.part("east=closed_baffle", p -> p.model(MODEL_CLOSED_BAFFLE).y(90).uvlock());
        bs.part("north=closed_baffle", p -> p.model(MODEL_CLOSED_BAFFLE).uvlock());
        bs.part("south=closed_baffle", p -> p.model(MODEL_CLOSED_BAFFLE).y(180).uvlock());
        bs.part("up=closed_baffle", p -> p.model(MODEL_CLOSED_BAFFLE).x(270).uvlock());
        bs.part("west=closed_baffle", p -> p.model(MODEL_CLOSED_BAFFLE).y(270).uvlock());

        bs.part("down=open_baffle", p -> p.model(modelOpenBaffle).x(90).uvlock());
        bs.part("east=open_baffle", p -> p.model(modelOpenBaffle).y(90).uvlock());
        bs.part("north=open_baffle", p -> p.model(modelOpenBaffle).uvlock());
        bs.part("south=open_baffle", p -> p.model(modelOpenBaffle).y(180).uvlock());
        bs.part("up=open_baffle", p -> p.model(modelOpenBaffle).x(270).uvlock());
        bs.part("west=open_baffle", p -> p.model(modelOpenBaffle).y(270).uvlock());
    }

    @Override
    protected void generateBlockModels(KubeAssetGenerator gen) {
        gen.blockModel(id.withSuffix("_center"), m -> {
            m.parent(MODEL_CENTER);
            m.texture("all", id.withPath(ID.BLOCK).withSuffix("_center").toString());
        });
        gen.blockModel(id.withSuffix("_open"), m -> {
            m.parent(MODEL_OPEN);
            m.texture("all", id.withPath(ID.BLOCK).withSuffix("_open").toString());
        });
        gen.blockModel(id.withSuffix("_open_baffle"), m -> {
            m.parent(MODEL_OPEN_BAFFLE);
            m.texture("all", id.withPath(ID.BLOCK).withSuffix("_open_baffle").toString());
        });
    }

    @Override
    protected void generateItemModel(ModelGenerator m) {
        m.parent(MODEL_CENTER);
        m.texture("all", id.withPath(ID.BLOCK).withSuffix("_center").toString());
    }

    @Override
    public Block createObject() {
        if (hxTubeType == null) throw new RuntimeException("Missing HX tube data! Must be set with setHXTubeData( heatTransferCoefficient, heatRetentionMult)");
        BLOCK_ENTITY_TYPES_MAP.computeIfAbsent(HX_ENTITY_TYPE.get("tube").getId(), e -> new ArrayList<>()).add(this.id);
        return HeatExchangerPartType.Tube.createBlock(hxTubeType);
    }

    @Override
    public void createAdditionalObjects(AdditionalObjectRegistry registry) {
        itemBuilder = new HXTubeItemBuilder(id, hxTubeType);
        ((HXTubeItemBuilder) itemBuilder).blockBuilder = this;
        registry.add(Registries.ITEM, itemBuilder);
    }

    private static class HXTubeItemBuilder extends BlockItemBuilder {
        private final HeatExchangerTubeType hxTubeType;

        public HXTubeItemBuilder(ResourceLocation id, HeatExchangerTubeType hxTubeType) {
            super(id);
            this.hxTubeType = hxTubeType;
        }

        @Override
        public Item createObject() {
            return HX_TUBE_FUNCTION.apply(this.blockBuilder.get(), hxTubeType);
        }
    }
}