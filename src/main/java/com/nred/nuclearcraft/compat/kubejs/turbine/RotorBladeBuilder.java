package com.nred.nuclearcraft.compat.kubejs.turbine;

import com.nred.nuclearcraft.multiblock.turbine.TurbinePartType;
import com.nred.nuclearcraft.multiblock.turbine.TurbineRotorBladeType;
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
import static com.nred.nuclearcraft.registration.BlockRegistration.BLADE_FUNCTION;

@ReturnsSelf
public class RotorBladeBuilder extends BlockBuilder {
    private static final ResourceLocation MODEL = ncLoc("block/turbine_rotor_blade");
    private static final ResourceLocation INVISIBLE = ncLoc("block/block_invisible");

    private TurbineRotorBladeType bladeType;

    public RotorBladeBuilder(ResourceLocation i) {
        super(i);

        defaultTags.add(BlockTags.MINEABLE_WITH_PICKAXE.location());
    }

    @Info("Sets the blade efficiency and expansion_coefficient.")
    public RotorBladeBuilder setBladeData(Double efficiency, Double expansion_coefficient) {
        bladeType = new TurbineRotorBladeType(this.id.toString(), () -> efficiency, () -> expansion_coefficient);
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
        if (bladeType == null) throw new RuntimeException("Missing blade data! Must be set with setBladeData(efficiency, expansion_coefficient)");
        BLOCK_ENTITY_TYPES_MAP.computeIfAbsent(TURBINE_ENTITY_TYPE.get("rotor_blade").getId(), e -> new ArrayList<>()).add(this.id);
        return TurbinePartType.RotorBlade.createBlock(bladeType);
    }

    @Override
    public void createAdditionalObjects(AdditionalObjectRegistry registry) {
        itemBuilder = new RotorBladeItemBuilder(id, bladeType);
        ((RotorBladeItemBuilder) itemBuilder).blockBuilder = this;
        registry.add(Registries.ITEM, itemBuilder);
    }

    private static class RotorBladeItemBuilder extends BlockItemBuilder {
        private final TurbineRotorBladeType bladeType;

        public RotorBladeItemBuilder(ResourceLocation id, TurbineRotorBladeType bladeType) {
            super(id);
            this.bladeType = bladeType;
        }

        @Override
        public Item createObject() {
            return BLADE_FUNCTION.apply(this.blockBuilder.get(), bladeType);
        }
    }
}