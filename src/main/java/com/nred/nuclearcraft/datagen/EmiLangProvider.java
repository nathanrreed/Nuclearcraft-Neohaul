package com.nred.nuclearcraft.datagen;

import net.minecraft.data.DataGenerator;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.LanguageProvider;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

import static com.nred.nuclearcraft.compat.emi.ModEmiPlugin.*;
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

        add("emi.category." + EMI_COLLECTOR_CATEGORY.getId().toLanguageKey(), "Collector");
        add("emi.category." + EMI_DECAY_GENERATOR_CATEGORY.getId().toLanguageKey(), "Decay Generator");

        add("emi.category." + EMI_TURBINE_CATEGORY.getId().toLanguageKey(), "Turbine");

        add("emi.category." + EMI_SOLID_FISSION_CATEGORY.getId().toLanguageKey(), "Solid Fuel Fission");
        add("emi.category." + EMI_SALT_FISSION_CATEGORY.getId().toLanguageKey(), "Molten Salt Fission");
        add("emi.category." + EMI_MODERATOR_CATEGORY.getId().toLanguageKey(), "Fission Moderator");
        add("emi.category." + EMI_REFLECTOR_CATEGORY.getId().toLanguageKey(), "Fission Reflector");
        add("emi.category." + EMI_IRRADIATOR_CATEGORY.getId().toLanguageKey(), "Fission Irradiator");
        add("emi.category." + EMI_VENT_CATEGORY.getId().toLanguageKey(), "Fission Vent Heating");
        add("emi.category." + EMI_EMERGENCY_COOLING_CATEGORY.getId().toLanguageKey(), "Fission Emergency Cooling");
        add("emi.category." + EMI_SALT_COOLING_CATEGORY.getId().toLanguageKey(), "Molten Salt Cooling");

        add("emi.category." + EMI_CONDENSER_CATEGORY.getId().toLanguageKey(), "Condenser");
        add("emi.category." + EMI_CONDENSER_DISSIPATION_CATEGORY.getId().toLanguageKey(), "Condenser Dissipation");
        add("emi.category." + EMI_HEAT_EXCHANGER_CATEGORY.getId().toLanguageKey(), "Heat Exchanger");

        add("emi.category." + EMI_MULTIBLOCK_DISTILLER_CATEGORY.getId().toLanguageKey(), "Multiblock Distiller");
        add("emi.category." + EMI_MULTIBLOCK_ELECTROLYZER_CATEGORY.getId().toLanguageKey(), "Multiblock Electrolyzer");
        add("emi.category." + EMI_MULTIBLOCK_INFILTRATOR_CATEGORY.getId().toLanguageKey(), "Multiblock Infiltrator");
        add("emi.category." + EMI_DIAPHRAGM_CATEGORY.getId().toLanguageKey(), "Diaphragm");
        add("emi.category." + EMI_SIEVE_ASSEMBLY_CATEGORY.getId().toLanguageKey(), "Sieve Assembly");
        add("emi.category." + EMI_ELECTROLYZER_CATHODE_CATEGORY.getId().toLanguageKey(), "Electrolyzer Cathode");
        add("emi.category." + EMI_ELECTROLYZER_ANODE_CATEGORY.getId().toLanguageKey(), "Electrolyzer Anode");
        add("emi.category." + EMI_INFILTRATOR_PRESSURE_CATEGORY.getId().toLanguageKey(), "Infiltrator Pressure Fluid");
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