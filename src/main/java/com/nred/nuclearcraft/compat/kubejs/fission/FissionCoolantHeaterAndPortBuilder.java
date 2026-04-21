package com.nred.nuclearcraft.compat.kubejs.fission;

import com.nred.nuclearcraft.multiblock.fisson.FissionPartType;
import com.nred.nuclearcraft.multiblock.fisson.molten_salt.FissionCoolantHeaterPortType;
import com.nred.nuclearcraft.multiblock.fisson.molten_salt.FissionCoolantHeaterType;
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
import static com.nred.nuclearcraft.registration.BlockRegistration.COOLANT_HEATER_FUNCTION;

@ReturnsSelf
public class FissionCoolantHeaterAndPortBuilder extends BlockBuilder {
    private FissionCoolantHeaterType heaterType;
    private FissionCoolantHeaterPortBuilder heaterPort;

    private String rule;
    private String singular;
    private String plural;

    public FissionCoolantHeaterAndPortBuilder(ResourceLocation i) {
        super(i);

        defaultTags.add(BlockTags.MINEABLE_WITH_PICKAXE.location());
    }

    @Info("Sets the coolant heater coolingRate, fluid id, placement rule, singular and plural strings.")
    public FissionCoolantHeaterAndPortBuilder setCoolantHeaterData(Integer coolingRate, ResourceLocation fluid_id, String rule, String singular, String plural) {
        heaterType = new FissionCoolantHeaterType(this.id.toString(), () -> coolingRate, fluid_id);
        heaterPort = new FissionCoolantHeaterPortBuilder(this.id.withSuffix("_port"));
        heaterPort.heaterPortType = new FissionCoolantHeaterPortType(this.id.toString(), fluid_id);

        this.rule = rule;
        this.singular = singular;
        this.plural = plural;
        return this;
    }

    @Override
    public void generateLang(LangKubeEvent lang) {
        if (singular == null || plural == null) throw new RuntimeException("Missing heater tooltip data! Must be set with setCoolantHeaterData(coolingRate, fluid_id, rule, singular, plural)");
        lang.add(MODID, "nc.sf." + id + "0", singular);
        lang.add(MODID, "nc.sf." + id + "1", plural);

        super.generateLang(lang);
    }

    @Override
    public Block createObject() {
        if (heaterType == null) throw new RuntimeException("Missing coolant heater data! Must be set with setCoolantHeaterData(coolingRate, fluid_id, rule, singular, plural)");
        BLOCK_ENTITY_TYPES_MAP.computeIfAbsent(FISSION_ENTITY_TYPE.get("coolant_heater").getId(), e -> new ArrayList<>()).add(this.id);
        BLOCK_ENTITY_TYPES_MAP.computeIfAbsent(FISSION_ENTITY_TYPE.get("coolant_heater_port").getId(), e -> new ArrayList<>()).add(heaterPort.id);

        PLACEMENT_RULE_MAP.put(id, rule);
        return FissionPartType.Heater.createBlock(heaterType);
    }

    @Override
    public void createAdditionalObjects(AdditionalObjectRegistry registry) {
        itemBuilder = new FissionCoolantHeaterItemBuilder(id, heaterType);
        ((FissionCoolantHeaterItemBuilder) itemBuilder).blockBuilder = this;

        registry.add(Registries.BLOCK, heaterPort);
        registry.add(Registries.ITEM, heaterPort.itemBuilder);
        registry.add(Registries.ITEM, itemBuilder);
    }

    private static class FissionCoolantHeaterItemBuilder extends BlockItemBuilder {
        private final FissionCoolantHeaterType heaterType;

        public FissionCoolantHeaterItemBuilder(ResourceLocation id, FissionCoolantHeaterType heaterType) {
            super(id);
            this.heaterType = heaterType;
        }

        @Override
        public Item createObject() {
            return COOLANT_HEATER_FUNCTION.apply(this.blockBuilder.get(), heaterType);
        }
    }
}