package com.nred.nuclearcraft.config;

import com.electronwill.nightconfig.core.UnmodifiableConfig;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.common.ModConfigSpec;
import org.jspecify.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.nred.nuclearcraft.NuclearcraftNeohaul.MODID;
import static net.neoforged.neoforge.client.gui.ConfigurationScreen.CRUMB_SEPARATOR;

public class ModConfigScreen extends ConfigurationScreen.ConfigurationSectionScreen {
    public ModConfigScreen(Screen parent, ModConfig.Type type, ModConfig modConfig, Component title) {
        super(parent, type, modConfig, title);
    }

    public ModConfigScreen(Context parentContext, Screen parent, Map<String, Object> valueSpecs, String key, Set<? extends UnmodifiableConfig.Entry> entrySet, Component title) {
        super(parentContext, parent, valueSpecs, key, entrySet, title);
    }

    private final Map<String, String> INDEX_MAP = indexMap();

    private Map<String, String> indexMap() {
        HashMap<String, String> map = new HashMap<>();
        map.put("processor_time", MODID + ".configuration.processor_order");
        map.put("processor_power", MODID + ".configuration.processor_order");
        map.put("speed_upgrade_power_laws_fp", MODID + ".configuration.speed_upgrade_fp_order");
        map.put("speed_upgrade_multipliers_fp", MODID + ".configuration.speed_upgrade_fp_order");
        map.put("energy_upgrade_power_laws_fp", MODID + ".configuration.energy_upgrade_fp_order");
        map.put("energy_upgrade_multipliers_fp", MODID + ".configuration.energy_upgrade_fp_order");
        map.put("upgrade_stack_sizes", MODID + ".configuration.upgrade_order");
        map.put("processor_passive_rate", MODID + ".configuration.processor_passive_rate_order");
        map.put("rtg_power", MODID + ".configuration.rtg_order");
        map.put("solar_power", MODID + ".configuration.solar_order");
        map.put("battery_block_capacity", MODID + ".configuration.battery_block_order");
        map.put("battery_block_max_transfer", MODID + ".configuration.battery_block_order");
        map.put("battery_item_capacity", MODID + ".configuration.battey_item_order");
        map.put("battery_item_max_transfer", MODID + ".configuration.battey_item_order");
        map.put("fission_source_efficiency", MODID + ".configuration.fission_source_order");
        map.put("fission_sink_cooling_rate", MODID + ".configuration.heat_sink_order");
        map.put("fission_sink_rule", MODID + ".configuration.heat_sink_order");
        map.put("fission_heater_cooling_rate", MODID + ".configuration.heat_sink_order");
        map.put("fission_heater_rule", MODID + ".configuration.heater_order");
        map.put("fission_shield_heat_per_flux", MODID + ".configuration.fission_shield_order");
        map.put("fission_shield_efficiency", MODID + ".configuration.fission_shield_order");
        map.put("turbine_blade_efficiency", MODID + ".configuration.turbine_blade_order");
        map.put("turbine_blade_expansion", MODID + ".configuration.turbine_blade_order");
        map.put("turbine_coil_conductivity", MODID + ".configuration.turbine_coil_order");
        map.put("turbine_coil_rule", MODID + ".configuration.turbine_coil_order");
        map.put("turbine_connector_rule", MODID + ".configuration.turbine_connector_order");
        map.put("radiation_sound_volumes", MODID + ".configuration.radiation_sound_order");
        map.put("radiation_shielding_level", MODID + ".configuration.radiation_shielding_order");

        map.put("fission_sparsity_penalty_params", MODID + ".configuration.fission_sparsity_penalty_params_order");

        map.put("fission_decay_build_up_times", MODID + ".configuration.fission_decay_order");
        map.put("fission_decay_lifetimes", MODID + ".configuration.fission_decay_order");
        map.put("fission_decay_equilibrium_factors", MODID + ".configuration.fission_decay_order");
        map.put("fission_decay_daughter_multipliers", MODID + ".configuration.fission_decay_daughter_multipliers_order");
        map.put("fission_decay_term_multipliers", MODID + ".configuration.fission_decay_term_multipliers_order");

        map.put("heat_exchanger_heat_transfer_coefficient", MODID + ".configuration.hx_tube_order");
        map.put("heat_exchanger_heat_retention_mult", MODID + ".configuration.hx_tube_order");
        return map;
    }

    private static final String SECTION = "neoforge.configuration.uitext.section";
    private static final String SECTION_TEXT = "neoforge.configuration.uitext.sectiontext";
    private static final String CRUMB = "neoforge.configuration.uitext.breadcrumb.order";
    protected static final ConfigurationScreen.TranslationChecker translationChecker = new ConfigurationScreen.TranslationChecker();

    @Override
    protected @Nullable <T> Element createList(String key, ModConfigSpec.ListValueSpec spec, ModConfigSpec.ConfigValue<List<T>> list) {
        if (INDEX_MAP.containsKey(key)) {
            return new Element(Component.translatable(SECTION, getTranslationComponent(key)), getTooltipComponent(key, null),
                    Button.builder(Component.translatable(SECTION, Component.translatable(translationChecker.check(getTranslationKey(key) + ".button", SECTION_TEXT))),
                                    button -> minecraft.setScreen(((ConfigurationMappedListScreen<?>) sectionCache.computeIfAbsent(key,
                                            k -> new ConfigurationMappedListScreen<>(Context.list(context, this), key, Component.translatable(CRUMB, this.getTitle(), CRUMB_SEPARATOR, getTranslationComponent(key)), INDEX_MAP.get(key), spec, list))).rebuild()))
                            .tooltip(Tooltip.create(getTooltipComponent(key, null))).build(),
                    false);
        }
        return super.createList(key, spec, list);
    }

    @Override
    protected @Nullable Element createSection(String key, UnmodifiableConfig subconfig, UnmodifiableConfig subsection) {
        Element element = super.createSection(key, subconfig, subsection);
        assert element != null;
        if (element.widget() instanceof Button btn) {
            return new Element(element.name(), element.tooltip(),
                    Button.builder(btn.getMessage(),
                                    button -> minecraft.setScreen(sectionCache.computeIfAbsent(key,
                                            k -> new ModConfigScreen(context, this, subconfig.valueMap(), key, subsection.entrySet(), Component.translatable(getTranslationKey(key))).rebuild())))
                            .tooltip(Tooltip.create(getTooltipComponent(key, null)))
                            .width(btn.getWidth())
                            .build(), element.undoable());
        }
        return element;
    }
}