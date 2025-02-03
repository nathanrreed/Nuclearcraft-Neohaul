package com.nred.nuclearcraft.datagen;

import net.minecraft.data.DataGenerator;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.LanguageProvider;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

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
        simpleName(RAWS, Tags.Items.RAW_MATERIALS, "", "Raw ");
        simpleName(NUGGETS, Tags.Items.NUGGETS, " Nuggets");
        simpleName(COMPOUNDS, Tags.Items.DUSTS, " Nuggets");
//        simpleName(ALLOYS, Tags.Items.NUGGETS, "s"); //TODO

        // Blocks
        simpleName(INGOTS, Tags.Blocks.STORAGE_BLOCKS, " Block");
        simpleName(INGOTS, Tags.Blocks.STORAGE_BLOCKS, "Block of Raw ", "", "raw_");
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