package com.nred.nuclearcraft.compat.kubejs.turbine;

import com.nred.nuclearcraft.multiblock.turbine.TurbineDynamoCoilType;
import com.nred.nuclearcraft.multiblock.turbine.TurbinePartType;
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
import static com.nred.nuclearcraft.registration.BlockEntityRegistration.TURBINE_ENTITY_TYPE;
import static com.nred.nuclearcraft.registration.BlockRegistration.COIL_FUNCTION;

@ReturnsSelf
public class DynamoCoilBuilder extends BlockBuilder {
    private TurbineDynamoCoilType dynamoCoilType;
    private String rule;
    private String singular;
    private String plural;

    public DynamoCoilBuilder(ResourceLocation i) {
        super(i);

        defaultTags.add(BlockTags.MINEABLE_WITH_PICKAXE.location());
    }

    @Info("Sets the dynamo coil conductivity, placement rule, singular and plural strings.")
    public DynamoCoilBuilder setDynamoCoilData(Double conductivity, String rule, String singular, String plural) {
        dynamoCoilType = new TurbineDynamoCoilType(this.id.toString(), () -> conductivity);
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
        if (dynamoCoilType == null) throw new RuntimeException("Missing dynamo coil data! Must be set with setDynamoCoiData(conductivity, rule, singular, plural)");
        BLOCK_ENTITY_TYPES_MAP.computeIfAbsent(TURBINE_ENTITY_TYPE.get("dynamo").getId(), e -> new ArrayList<>()).add(this.id);
        PLACEMENT_RULE_MAP.put(id, rule);
        return TurbinePartType.DynamoCoil.createBlock(dynamoCoilType);
    }

    @Override
    public void createAdditionalObjects(AdditionalObjectRegistry registry) {
        itemBuilder = new DynamoCoilItemBuilder(id, dynamoCoilType);
        ((DynamoCoilItemBuilder) itemBuilder).blockBuilder = this;
        registry.add(Registries.ITEM, itemBuilder);
    }

    private static class DynamoCoilItemBuilder extends BlockItemBuilder {
        private final TurbineDynamoCoilType dynamoCoilType;

        public DynamoCoilItemBuilder(ResourceLocation id, TurbineDynamoCoilType dynamoCoilType) {
            super(id);
            this.dynamoCoilType = dynamoCoilType;
        }

        @Override
        public Item createObject() {
            return COIL_FUNCTION.apply(this.blockBuilder.get(), dynamoCoilType);
        }
    }
}