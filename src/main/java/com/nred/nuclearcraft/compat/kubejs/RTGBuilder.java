package com.nred.nuclearcraft.compat.kubejs;

import com.nred.nuclearcraft.multiblock.rtg.RTGPartType;
import com.nred.nuclearcraft.multiblock.rtg.RTGType;
import dev.latvian.mods.kubejs.block.BlockBuilder;
import dev.latvian.mods.kubejs.block.BlockItemBuilder;
import dev.latvian.mods.kubejs.client.ModelGenerator;
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
import static com.nred.nuclearcraft.registration.BlockEntityRegistration.RTG_ENTITY_TYPE;
import static com.nred.nuclearcraft.registration.BlockRegistration.RTG_FUNCTION;

@ReturnsSelf
public class RTGBuilder extends BlockBuilder {
    private static final ResourceLocation MODEL = ncLoc("block/top_sides");

    private RTGType rtgType;

    public RTGBuilder(ResourceLocation i) {
        super(i);

        defaultTags.add(BlockTags.MINEABLE_WITH_PICKAXE.location());
    }

    @Info("Sets the RTG power and radiation.")
    public RTGBuilder setRTGData(Integer power, double radiation) {
        rtgType = new RTGType(this.id.toString(), () -> power, radiation);
        return this;
    }

    @Override
    protected void generateBlockModels(KubeAssetGenerator gen) {
        gen.blockModel(id, m -> {
            m.parent(MODEL);
            m.texture("sides", textures.getOrDefault("sides", id.withPath(ID.BLOCK).withSuffix("_side").toString()));
            m.texture("top", textures.getOrDefault("top", id.withPath(ID.BLOCK).withSuffix("_top").toString()));
        });
    }

    @Override
    protected void generateItemModel(ModelGenerator m) {
        m.parent(MODEL);
        m.texture("sides", textures.getOrDefault("sides", id.withPath(ID.BLOCK).withSuffix("_side").toString()));
        m.texture("top", textures.getOrDefault("top", id.withPath(ID.BLOCK).withSuffix("_top").toString()));
    }

    @Override
    public Block createObject() {
        if (rtgType == null) throw new RuntimeException("Missing RTG data! Must be set with setRTGData(power, radiation)");
        BLOCK_ENTITY_TYPES_MAP.computeIfAbsent(RTG_ENTITY_TYPE.getId(), e -> new ArrayList<>()).add(this.id);
        return RTGPartType.RTG.createBlock(rtgType);
    }

    @Override
    public void createAdditionalObjects(AdditionalObjectRegistry registry) {
        itemBuilder = new RTGItemBuilder(id, rtgType);
        ((RTGItemBuilder) itemBuilder).blockBuilder = this;
        registry.add(Registries.ITEM, itemBuilder);
    }

    private static class RTGItemBuilder extends BlockItemBuilder {
        private final RTGType rtgType;

        public RTGItemBuilder(ResourceLocation id, RTGType rtgType) {
            super(id);
            this.rtgType = rtgType;
        }

        @Override
        public Item createObject() {
            return RTG_FUNCTION.apply(this.blockBuilder.get(), rtgType);
        }
    }
}