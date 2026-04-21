package com.nred.nuclearcraft.compat.kubejs.fission;

import com.nred.nuclearcraft.multiblock.fisson.FissionNeutronShieldType;
import com.nred.nuclearcraft.multiblock.fisson.FissionPartType;
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
import static com.nred.nuclearcraft.registration.BlockRegistration.SHIELD_FUNCTION;

@ReturnsSelf
public class FissionShieldBuilder extends BlockBuilder {
    private static final ResourceLocation MODEL = ncLoc("block/cube_all_overlayed");

    private FissionNeutronShieldType shieldType;

    public FissionShieldBuilder(ResourceLocation i) {
        super(i);

        defaultTags.add(BlockTags.MINEABLE_WITH_PICKAXE.location());
    }

    @Info("Sets the shield heatPerFlux and efficiency.")
    public FissionShieldBuilder setShieldData(Double heatPerFlux, Double efficiency) {
        shieldType = new FissionNeutronShieldType(this.id.toString(), () -> heatPerFlux, () -> efficiency);
        return this;
    }

    @Override
    protected void generateBlockState(VariantBlockStateGenerator bs) {
        ResourceLocation modelActive = newID("block/", "_on");
        ResourceLocation modelInActive = newID("block/", "_off");

        bs.variant("nuclearcraftneohaul_active=false", v -> v.model(modelInActive));
        bs.variant("nuclearcraftneohaul_active=true", v -> v.model(modelActive));
    }

    @Override
    protected void generateBlockModels(KubeAssetGenerator gen) {
        gen.blockModel(id.withSuffix("_on"), m -> {
            m.parent(MODEL);
            m.texture("all", baseTexture);
            m.texture("overlay", "nuclearcraftneohaul:block/fission/shield/on");
        });
        gen.blockModel(id.withSuffix("_off"), m -> {
            m.parent(MODEL);
            m.texture("all", baseTexture);
            m.texture("overlay", "nuclearcraftneohaul:block/fission/shield/off");
        });
    }

    @Override
    protected void generateItemModel(ModelGenerator m) {
        m.parent(MODEL);
        m.texture("all", baseTexture);
        m.texture("overlay", "nuclearcraftneohaul:block/fission/shield/off");
    }

    @Override
    public Block createObject() {
        if (shieldType == null) throw new RuntimeException("Missing shield data! Must be set with setShieldData(heatPerFlux, efficiency)");
        BLOCK_ENTITY_TYPES_MAP.computeIfAbsent(FISSION_ENTITY_TYPE.get("shield").getId(), e -> new ArrayList<>()).add(this.id);
        return FissionPartType.Shield.createBlock(shieldType);
    }

    @Override
    public void createAdditionalObjects(AdditionalObjectRegistry registry) {
        itemBuilder = new FissionShieldItemBuilder(id, shieldType);
        ((FissionShieldItemBuilder) itemBuilder).blockBuilder = this;
        registry.add(Registries.ITEM, itemBuilder);
    }

    private static class FissionShieldItemBuilder extends BlockItemBuilder {
        private final FissionNeutronShieldType shieldType;

        public FissionShieldItemBuilder(ResourceLocation id, FissionNeutronShieldType shieldType) {
            super(id);
            this.shieldType = shieldType;
        }

        @Override
        public Item createObject() {
            return SHIELD_FUNCTION.apply(this.blockBuilder.get(), shieldType);
        }
    }
}