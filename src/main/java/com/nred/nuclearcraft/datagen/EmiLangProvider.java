package com.nred.nuclearcraft.datagen;

import net.minecraft.data.DataGenerator;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.LanguageProvider;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

import static com.nred.nuclearcraft.compat.emi.ModEmiPlugin.EMI_PROCESSOR_CATEGORIES;
import static com.nred.nuclearcraft.info.Names.*;

public class EmiLangProvider extends LanguageProvider {
    public EmiLangProvider(DataGenerator gen, String locale) {
        super(gen.getPackOutput(), "emi", locale);
    }

    @Override
    protected void addTranslations() {
        for (String ore : ORES) {
            add(ItemTags.create(Tags.Blocks.ORES.location().withSuffix("/" + ore)), StringUtils.capitalize(ore + " Ores"));
        }

        // Items
        simpleName(INGOTS, Tags.Items.INGOTS, " Ingots");
        simpleName(GEMS, Tags.Items.GEMS, "s");
        simpleName(DUSTS, Tags.Items.DUSTS, " Dusts");
        simpleName(GEM_DUSTS, Tags.Items.DUSTS, " Dusts");
        simpleName(COMPOUNDS, Tags.Items.DUSTS, " Dusts");
        simpleName(RAWS, Tags.Items.RAW_MATERIALS, "", "Raw ");
        simpleName(NUGGETS, Tags.Items.NUGGETS, " Nuggets");

        // Blocks
        simpleName(INGOTS, Tags.Blocks.STORAGE_BLOCKS, " Block");
        simpleName(INGOTS, Tags.Blocks.STORAGE_BLOCKS, "Block of Raw ", "", "raw_");

        add("emi.category." + EMI_PROCESSOR_CATEGORIES.get("alloy_furnace").getId().toLanguageKey(), "Alloy Furnace");
        add("emi.category." + EMI_PROCESSOR_CATEGORIES.get("assembler").getId().toLanguageKey(), "Assembler");
        add("emi.category." + EMI_PROCESSOR_CATEGORIES.get("centrifuge").getId().toLanguageKey(), "Centrifuge");
        add("emi.category." + EMI_PROCESSOR_CATEGORIES.get("chemical_reactor").getId().toLanguageKey(), "Chemical Reactor");
        add("emi.category." + EMI_PROCESSOR_CATEGORIES.get("crystallizer").getId().toLanguageKey(), "Crystallizer");
        add("emi.category." + EMI_PROCESSOR_CATEGORIES.get("decay_hastener").getId().toLanguageKey(), "Decay Hastener");
        add("emi.category." + EMI_PROCESSOR_CATEGORIES.get("electric_furnace").getId().toLanguageKey(), "Electric Furnace");
        add("emi.category." + EMI_PROCESSOR_CATEGORIES.get("electrolyzer").getId().toLanguageKey(), "Electrolyzer");
        add("emi.category." + EMI_PROCESSOR_CATEGORIES.get("fluid_enricher").getId().toLanguageKey(), "Fluid Enricher");
        add("emi.category." + EMI_PROCESSOR_CATEGORIES.get("fluid_extractor").getId().toLanguageKey(), "Fluid Extractor");
        add("emi.category." + EMI_PROCESSOR_CATEGORIES.get("fuel_reprocessor").getId().toLanguageKey(), "Fuel Reprocessor");
        add("emi.category." + EMI_PROCESSOR_CATEGORIES.get("fluid_infuser").getId().toLanguageKey(), "Fluid Infuser");
        add("emi.category." + EMI_PROCESSOR_CATEGORIES.get("ingot_former").getId().toLanguageKey(), "Ingot Former");
        add("emi.category." + EMI_PROCESSOR_CATEGORIES.get("manufactory").getId().toLanguageKey(), "Manufactory");
        add("emi.category." + EMI_PROCESSOR_CATEGORIES.get("melter").getId().toLanguageKey(), "Melter");
        add("emi.category." + EMI_PROCESSOR_CATEGORIES.get("pressurizer").getId().toLanguageKey(), "Pressurizer");
        add("emi.category." + EMI_PROCESSOR_CATEGORIES.get("rock_crusher").getId().toLanguageKey(), "Rock Crusher");
        add("emi.category." + EMI_PROCESSOR_CATEGORIES.get("fluid_mixer").getId().toLanguageKey(), "Fluid Mixer");
        add("emi.category." + EMI_PROCESSOR_CATEGORIES.get("separator").getId().toLanguageKey(), "Separator");
        add("emi.category." + EMI_PROCESSOR_CATEGORIES.get("supercooler").getId().toLanguageKey(), "Supercooler");
    }

    private void simpleName(List<String> list, TagKey<?> key, String append) {
        simpleName(list, key, "", append);
    }

    private void simpleName(List<String> list, TagKey<?> key, String prepend, String append) {
        simpleName(list, key, prepend, append, "");
    }

    private void simpleName(List<String> list, TagKey<?> key, String prepend, String append, String prefix) {
        for (String name : list) {
            add(ItemTags.create(key.location().withSuffix("/" + prefix + name + "s")), prepend + StringUtils.capitalize(name) + append);
        }
    }
}