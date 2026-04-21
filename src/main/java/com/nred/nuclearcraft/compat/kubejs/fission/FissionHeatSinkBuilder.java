package com.nred.nuclearcraft.compat.kubejs.fission;

import com.nred.nuclearcraft.multiblock.fisson.FissionPartType;
import com.nred.nuclearcraft.multiblock.fisson.solid.FissionHeatSinkType;
import dev.latvian.mods.kubejs.block.BlockBuilder;
import dev.latvian.mods.kubejs.block.BlockItemBuilder;
import dev.latvian.mods.kubejs.client.LangKubeEvent;
import dev.latvian.mods.kubejs.registry.AdditionalObjectRegistry;
import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.rhino.util.ReturnsSelf;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.ArrayList;

import static com.nred.nuclearcraft.NuclearcraftNeohaul.MODID;
import static com.nred.nuclearcraft.compat.kubejs.BlockEntityTypeAddBlocksForKubeJS.BLOCK_ENTITY_TYPES_MAP;
import static com.nred.nuclearcraft.compat.kubejs.BlockEntityTypeAddBlocksForKubeJS.PLACEMENT_RULE_MAP;
import static com.nred.nuclearcraft.registration.BlockEntityRegistration.FISSION_ENTITY_TYPE;
import static com.nred.nuclearcraft.registration.BlockRegistration.HEAT_SINK_FUNCTION;

@ReturnsSelf
public class FissionHeatSinkBuilder extends BlockBuilder {
    private FissionHeatSinkType heatSinkType;
    private String rule;
    private String singular;
    private String plural;

    public FissionHeatSinkBuilder(ResourceLocation i) {
        super(i);

        defaultTags.add(BlockTags.MINEABLE_WITH_PICKAXE.location());
    }

    @Info("Sets the heat sink coolingRate, placement rule, singular and plural strings.")
    public FissionHeatSinkBuilder setHeatSinkData(Integer coolingRate, String rule, String singular, String plural) {
        heatSinkType = new FissionHeatSinkType(this.id.toString(), () -> coolingRate);
        this.rule = rule;
        this.singular = singular;
        this.plural = plural;
        return this;
    }

    @Override
    public void generateLang(LangKubeEvent lang) {
        if (singular == null || plural == null) throw new RuntimeException("Missing dynamo coil tooltip data! Must be set with setDynamoCoiData(conductivity, rule, singular, plural)");
        lang.add(MODID, "nc.sf." + id + "0", singular);
        lang.add(MODID, "nc.sf." + id + "1", plural);

        super.generateLang(lang);
    }

    @Override
    public Block createObject() {
        if (heatSinkType == null) throw new RuntimeException("Missing heat sink data! Must be set with setHeatSinkData(coolingRate, rule, singular, plural)");
        BLOCK_ENTITY_TYPES_MAP.computeIfAbsent(FISSION_ENTITY_TYPE.get("heat_sink").getId(), e -> new ArrayList<>()).add(this.id);
        PLACEMENT_RULE_MAP.put(id, rule);
        return FissionPartType.HeatSink.createBlock(heatSinkType);
    }

    @Override
    public void createAdditionalObjects(AdditionalObjectRegistry registry) {
        itemBuilder = new FissionHeatSinkItemBuilder(id, heatSinkType);
        ((FissionHeatSinkItemBuilder) itemBuilder).blockBuilder = this;
        registry.add(Registries.ITEM, itemBuilder);
    }

    private static class FissionHeatSinkItemBuilder extends BlockItemBuilder {
        private final FissionHeatSinkType heatSinkType;

        public FissionHeatSinkItemBuilder(ResourceLocation id, FissionHeatSinkType heatSinkType) {
            super(id);
            this.heatSinkType = heatSinkType;
        }

        @Override
        public Item createObject() {
            return HEAT_SINK_FUNCTION.apply(this.blockBuilder.get(), heatSinkType);
        }
    }
}