package com.nred.nuclearcraft.compat.kubejs.fission;

import com.nred.nuclearcraft.multiblock.fisson.FissionPartType;
import com.nred.nuclearcraft.multiblock.fisson.pebble.FissionCoolerPortType;
import com.nred.nuclearcraft.multiblock.fisson.pebble.FissionCoolerType;
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
import static com.nred.nuclearcraft.registration.BlockRegistration.COOLER_FUNCTION;

@ReturnsSelf
public class FissionCoolerAndPortBuilder extends BlockBuilder {
    private FissionCoolerType coolerType;
    private FissionCoolerPortBuilder coolerPort;

    private String rule;
    private String singular;
    private String plural;

    public FissionCoolerAndPortBuilder(ResourceLocation i) {
        super(i);

        defaultTags.add(BlockTags.MINEABLE_WITH_PICKAXE.location());
    }

    @Info("Sets the gas cooler coolingRate, fluid id, placement rule, singular and plural strings.")
    public FissionCoolerAndPortBuilder setCoolerData(Integer coolingRate, ResourceLocation fluid_id, String rule, String singular, String plural) {
        coolerType = new FissionCoolerType(this.id.toString(), () -> coolingRate, fluid_id);
        coolerPort = new FissionCoolerPortBuilder(this.id.withSuffix("_port"));
        coolerPort.coolerPortType = new FissionCoolerPortType(this.id.toString(), fluid_id);

        this.rule = rule;
        this.singular = singular;
        this.plural = plural;
        return this;
    }

    @Override
    public void generateLang(LangKubeEvent lang) {
        if (singular == null || plural == null) throw new RuntimeException("Missing cooler tooltip data! Must be set with setCoolerData(coolingRate, fluid_id, rule, singular, plural)");
        lang.add(MODID, "nc.sf." + id + "0", singular);
        lang.add(MODID, "nc.sf." + id + "1", plural);

        super.generateLang(lang);
    }

    @Override
    public Block createObject() {
        if (coolerType == null) throw new RuntimeException("Missing gas cooler data! Must be set with setCoolerData(coolingRate, fluid_id, rule, singular, plural)");
        BLOCK_ENTITY_TYPES_MAP.computeIfAbsent(FISSION_ENTITY_TYPE.get("cooler").getId(), e -> new ArrayList<>()).add(this.id);
        BLOCK_ENTITY_TYPES_MAP.computeIfAbsent(FISSION_ENTITY_TYPE.get("cooler_port").getId(), e -> new ArrayList<>()).add(coolerPort.id);

        PLACEMENT_RULE_MAP.put(id, rule);
        return FissionPartType.Cooler.createBlock(coolerType);
    }

    @Override
    public void createAdditionalObjects(AdditionalObjectRegistry registry) {
        itemBuilder = new FissionCoolerItemBuilder(id, coolerType);
        ((FissionCoolerItemBuilder) itemBuilder).blockBuilder = this;

        registry.add(Registries.BLOCK, coolerPort);
        registry.add(Registries.ITEM, coolerPort.itemBuilder);
        registry.add(Registries.ITEM, itemBuilder);
    }

    private static class FissionCoolerItemBuilder extends BlockItemBuilder {
        private final FissionCoolerType coolerType;

        public FissionCoolerItemBuilder(ResourceLocation id, FissionCoolerType coolerType) {
            super(id);
            this.coolerType = coolerType;
        }

        @Override
        public Item createObject() {
            return COOLER_FUNCTION.apply(this.blockBuilder.get(), coolerType);
        }
    }
}