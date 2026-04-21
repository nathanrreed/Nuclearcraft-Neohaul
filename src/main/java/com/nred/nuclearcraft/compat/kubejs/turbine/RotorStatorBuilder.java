package com.nred.nuclearcraft.compat.kubejs.turbine;

import com.nred.nuclearcraft.multiblock.turbine.TurbinePartType;
import com.nred.nuclearcraft.multiblock.turbine.TurbineRotorStatorType;
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
import static com.nred.nuclearcraft.registration.BlockEntityRegistration.TURBINE_ENTITY_TYPE;
import static com.nred.nuclearcraft.registration.BlockRegistration.STATOR_FUNCTION;

@ReturnsSelf
public class RotorStatorBuilder extends BlockBuilder {
    private static final ResourceLocation MODEL = ncLoc("block/turbine_rotor_stator");
    private static final ResourceLocation INVISIBLE = ncLoc("block/block_invisible");

    private TurbineRotorStatorType statorType;

    public RotorStatorBuilder(ResourceLocation i) {
        super(i);

        defaultTags.add(BlockTags.MINEABLE_WITH_PICKAXE.location());
    }

    @Info("Sets the stator expansion_coefficient.")
    public RotorStatorBuilder setStatorData(Double expansion_coefficient) {
        statorType = new TurbineRotorStatorType(this.id.toString(), () -> expansion_coefficient);
        return this;
    }

    @Override
    protected void generateBlockState(VariantBlockStateGenerator bs) {
        ResourceLocation model = newID("block/", "");

        bs.variant("dir=invisible", v -> v.model(INVISIBLE));
        bs.variant("dir=x", v -> v.model(model).x(90).y(90));
        bs.variant("dir=y", v -> v.model(model));
        bs.variant("dir=z", v -> v.model(model).x(90));
    }

    @Override
    protected void generateBlockModels(KubeAssetGenerator gen) {
        gen.blockModel(id, m -> {
            m.parent(MODEL);
            m.texture("texture", baseTexture);
        });
    }

    @Override
    protected void generateItemModel(ModelGenerator m) {
        m.parent(MODEL);
        m.texture("texture", baseTexture);
    }

    @Override
    public Block createObject() {
        if (statorType == null) throw new RuntimeException("Missing stator data! Must be set with setStatorData(expansion_coefficient)");
        BLOCK_ENTITY_TYPES_MAP.computeIfAbsent(TURBINE_ENTITY_TYPE.get("rotor_stator").getId(), e -> new ArrayList<>()).add(this.id);
        return TurbinePartType.RotorStator.createBlock(statorType);
    }

    @Override
    public void createAdditionalObjects(AdditionalObjectRegistry registry) {
        itemBuilder = new RotorStatorItemBuilder(id, statorType);
        ((RotorStatorItemBuilder) itemBuilder).blockBuilder = this;
        registry.add(Registries.ITEM, itemBuilder);
    }

    private static class RotorStatorItemBuilder extends BlockItemBuilder {
        private final TurbineRotorStatorType statorType;

        public RotorStatorItemBuilder(ResourceLocation id, TurbineRotorStatorType statorType) {
            super(id);
            this.statorType = statorType;
        }

        @Override
        public Item createObject() {
            return STATOR_FUNCTION.apply(this.blockBuilder.get(), statorType);
        }
    }
}