package com.nred.nuclearcraft.compat.kubejs.fission;

import com.nred.nuclearcraft.multiblock.fisson.FissionPartType;
import com.nred.nuclearcraft.multiblock.fisson.molten_salt.FissionCoolantHeaterPortType;
import dev.latvian.mods.kubejs.block.BlockBuilder;
import dev.latvian.mods.kubejs.client.ModelGenerator;
import dev.latvian.mods.kubejs.client.VariantBlockStateGenerator;
import dev.latvian.mods.kubejs.generator.KubeAssetGenerator;
import dev.latvian.mods.rhino.util.ReturnsSelf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;

import java.util.ArrayList;

import static com.nred.nuclearcraft.compat.kubejs.BlockEntityTypeAddBlocksForKubeJS.BLOCK_ENTITY_TYPES_MAP;
import static com.nred.nuclearcraft.helpers.Location.ncLoc;
import static com.nred.nuclearcraft.registration.BlockEntityRegistration.FISSION_ENTITY_TYPE;

@ReturnsSelf
public class FissionCoolantHeaterPortBuilder extends BlockBuilder {
    private static final ResourceLocation MODEL = ncLoc("block/machine_overlayed");

    public FissionCoolantHeaterPortType heaterPortType;

    public FissionCoolantHeaterPortBuilder(ResourceLocation i) {
        super(i);

        defaultTags.add(BlockTags.MINEABLE_WITH_PICKAXE.location());
    }

    @Override
    protected void generateBlockState(VariantBlockStateGenerator bs) {
        ResourceLocation modelActive = newID("block/", "_on");
        ResourceLocation modelInactive = newID("block/", "_off");

        bs.variant("axis=y,nuclearcraftneohaul_active=false", v -> v.model(modelInactive).y(90));
        bs.variant("axis=y,nuclearcraftneohaul_active=true", v -> v.model(modelActive).y(90));
        bs.variant("axis=x,nuclearcraftneohaul_active=false", v -> v.model(modelInactive).x(90));
        bs.variant("axis=x,nuclearcraftneohaul_active=true", v -> v.model(modelActive).x(90));
        bs.variant("axis=z,nuclearcraftneohaul_active=false", v -> v.model(modelInactive));
        bs.variant("axis=z,nuclearcraftneohaul_active=true", v -> v.model(modelActive));
    }

    @Override
    protected void generateBlockModels(KubeAssetGenerator gen) {
        gen.blockModel(id.withSuffix("_on"), m -> {
            m.parent(MODEL);
            m.texture("back", baseTexture);
            m.texture("back_overlay", "nuclearcraftneohaul:block/fission/port/heater/out");
            m.texture("bottom", "nuclearcraftneohaul:block/fission/port/top");
            m.texture("bottom_overlay", "nuclearcraftneohaul:block/block_invisible");
            m.texture("front", baseTexture);
            m.texture("front_overlay", "nuclearcraftneohaul:block/fission/port/heater/out");
            m.texture("side", "nuclearcraftneohaul:block/fission/port/side");
            m.texture("side_overlay", "nuclearcraftneohaul:block/block_invisible");
            m.texture("top", "nuclearcraftneohaul:block/fission/port/top");
            m.texture("top_overlay", "nuclearcraftneohaul:block/block_invisible");
        });
        gen.blockModel(id.withSuffix("_off"), m -> {
            m.parent(MODEL);
            m.texture("back", baseTexture);
            m.texture("back_overlay", "nuclearcraftneohaul:block/fission/port/heater/in");
            m.texture("bottom", "nuclearcraftneohaul:block/fission/port/top");
            m.texture("bottom_overlay", "nuclearcraftneohaul:block/block_invisible");
            m.texture("front", baseTexture);
            m.texture("front_overlay", "nuclearcraftneohaul:block/fission/port/heater/in");
            m.texture("side", "nuclearcraftneohaul:block/fission/port/side");
            m.texture("side_overlay", "nuclearcraftneohaul:block/block_invisible");
            m.texture("top", "nuclearcraftneohaul:block/fission/port/top");
            m.texture("top_overlay", "nuclearcraftneohaul:block/block_invisible");
        });
    }

    @Override
    protected void generateItemModel(ModelGenerator m) {
        m.parent(MODEL);
        m.texture("back", baseTexture);
        m.texture("back_overlay", "nuclearcraftneohaul:block/fission/port/heater/in");
        m.texture("bottom", "nuclearcraftneohaul:block/fission/port/top");
        m.texture("bottom_overlay", "nuclearcraftneohaul:block/block_invisible");
        m.texture("front", baseTexture);
        m.texture("front_overlay", "nuclearcraftneohaul:block/fission/port/heater/in");
        m.texture("side", "nuclearcraftneohaul:block/fission/port/side");
        m.texture("side_overlay", "nuclearcraftneohaul:block/block_invisible");
        m.texture("top", "nuclearcraftneohaul:block/fission/port/top");
        m.texture("top_overlay", "nuclearcraftneohaul:block/block_invisible");
    }

    @Override
    public Block createObject() {
        if (heaterPortType == null) throw new RuntimeException("Missing coolant heater port data! Must be set with setCoolantHeaterPortData(fluid_id)");
        BLOCK_ENTITY_TYPES_MAP.computeIfAbsent(FISSION_ENTITY_TYPE.get("coolant_heater_port").getId(), e -> new ArrayList<>()).add(this.id);
        return FissionPartType.HeaterPort.createBlock(heaterPortType);
    }
}