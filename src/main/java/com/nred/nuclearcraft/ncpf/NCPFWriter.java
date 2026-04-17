package com.nred.nuclearcraft.ncpf;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nred.nuclearcraft.config.NCConfig;
import com.nred.nuclearcraft.ncpf.element.NCPFElement;
import com.nred.nuclearcraft.ncpf.nuclearcraft.NCPFOverhaulMSRConfiguration;
import com.nred.nuclearcraft.ncpf.nuclearcraft.NCPFOverhaulSFRConfiguration;
import com.nred.nuclearcraft.ncpf.nuclearcraft.NCPFOverhaulTurbineConfiguration;
import com.nred.nuclearcraft.recipe.NCRecipes;
import com.nred.nuclearcraft.util.NCUtil;
import net.minecraft.world.level.block.Block;
import net.neoforged.fml.ModList;
import net.neoforged.fml.loading.FMLPaths;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.nred.nuclearcraft.NuclearcraftNeohaul.MODID;
import static com.nred.nuclearcraft.registration.BlockEntityRegistration.FISSION_ENTITY_TYPE;
import static com.nred.nuclearcraft.registration.BlockEntityRegistration.TURBINE_ENTITY_TYPE;

public class NCPFWriter {
    public static void exportNCPF() {
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();

            NCPFRoot ncpf = new NCPFRoot();

            ncpf.modules.put("nuclearcraft:generated", new Object() {
                final String nuclearcraft_version = ModList.get().getModFileById(MODID).versionString();
            });

            // Fission SFR
            {
                NCPFBuilder.configContext = "overhaul_sfr";
                NCPFOverhaulSFRConfiguration cfg = new NCPFOverhaulSFRConfiguration();

                Map<String, Object> settings = new HashMap<>();
                settings.put("min_size", NCConfig.fission_min_size);
                settings.put("max_size", NCConfig.fission_max_size);
                settings.put("neutron_reach", NCConfig.fission_neutron_reach);
                settings.put("sparsity_penalty_multiplier", NCConfig.fission_sparsity_penalty_params[0]);
                settings.put("sparsity_penalty_threshold", NCConfig.fission_sparsity_penalty_params[1]);
                settings.put("cooling_efficiency_leniency", NCConfig.fission_cooling_efficiency_leniency);
                cfg.modules.put("nuclearcraft:overhaul_sfr_configuration_settings", settings);

                // Blocks

                for (String fissionEntityType : List.of("solid_fuel_fission_controller", "monitor", "source_manager", "shield_manager", "vent", "computer_port", "power_port", "casing", "glass", "source", "cell", "cell_port", "shield", "conductor", "irradiator", "irradiator_port", "heat_sink")) {
                    for (Block block : FISSION_ENTITY_TYPE.get(fissionEntityType).get().getValidBlocks()) {
                        NCPFBuilder.translate(cfg.blocks, block);
                    }
                }
                NCPFBuilder.translateModerator(cfg.blocks);
                NCPFBuilder.translateReflector(cfg.blocks);

                // Coolant Recipes
                NCPFBuilder.translate(cfg.coolant_recipes, NCRecipes.fission_heating);

                Map<String, Object> globalElementsModule = new HashMap<>();
                List<NCPFElement> globalElements = new ArrayList<>();

                NCPFBuilder.translateOutputs(globalElements, NCRecipes.fission_heating);
                NCPFBuilder.translateOutputs(globalElements, NCRecipes.solid_fission);
                NCPFBuilder.translateOutputs(globalElements, NCRecipes.fission_irradiator);

                List<List<NCPFElement>> lists = new ArrayList<>();
                lists.add(cfg.blocks);
                lists.add(cfg.coolant_recipes);

                removeDuplicateNCPFElements(gson, globalElements, lists);

                lists.add(globalElements);

                for (NCPFElement block : cfg.blocks) {
                    if (block.modules == null) {
                        continue;
                    }

                    Map<String, Object> blockRecipes = NCPFHelper.get(block.modules, "ncpf:block_recipes");
                    if (blockRecipes == null) {
                        continue;
                    }

                    List<NCPFElement> recipes = NCPFHelper.get(blockRecipes, "recipes");
                    lists.add(recipes);
                }

                globalElementsModule.put("elements", globalElements);
                cfg.modules.put("plannerator:global_elements", globalElementsModule);
                ncpf.configuration.put("nuclearcraft:overhaul_sfr", cfg);
            }

            // Fission MSR
            {
                NCPFBuilder.configContext = "overhaul_msr";
                NCPFOverhaulMSRConfiguration cfg = new NCPFOverhaulMSRConfiguration();

                Map<String, Object> settings = new HashMap<>();
                settings.put("min_size", NCConfig.fission_min_size);
                settings.put("max_size", NCConfig.fission_max_size);
                settings.put("neutron_reach", NCConfig.fission_neutron_reach);
                settings.put("sparsity_penalty_multiplier", NCConfig.fission_sparsity_penalty_params[0]);
                settings.put("sparsity_penalty_threshold", NCConfig.fission_sparsity_penalty_params[1]);
                settings.put("cooling_efficiency_leniency", NCConfig.fission_cooling_efficiency_leniency);
                cfg.modules.put("nuclearcraft:overhaul_msr_configuration_settings", settings);

                // Blocks

                for (String fissionEntityType : List.of("molten_salt_fission_controller", "monitor", "source_manager", "shield_manager", "computer_port", "power_port", "casing", "glass", "source", "vessel", "vessel_port", "shield", "conductor", "irradiator", "irradiator_port", "coolant_heater", "coolant_heater_port")) {
                    for (Block block : FISSION_ENTITY_TYPE.get(fissionEntityType).get().getValidBlocks()) {
                        NCPFBuilder.translate(cfg.blocks, block);
                    }
                }
                NCPFBuilder.translateModerator(cfg.blocks);
                NCPFBuilder.translateReflector(cfg.blocks);

                Map<String, Object> globalElementsModule = new HashMap<>();
                List<NCPFElement> globalElements = new ArrayList<>();

                NCPFBuilder.translateOutputs(globalElements, NCRecipes.salt_fission);
                NCPFBuilder.translateOutputs(globalElements, NCRecipes.coolant_heater);
                NCPFBuilder.translateOutputs(globalElements, NCRecipes.fission_irradiator);

                List<List<NCPFElement>> lists = new ArrayList<>();
                lists.add(cfg.blocks);

                globalLoop:
                for (int i = 0; i < globalElements.size(); ++i) {
                    NCPFElement globalElem = globalElements.get(i);
                    for (int j = 0; j < globalElements.size(); ++j) {
                        if (i == j) {
                            continue;
                        }

                        NCPFElement elem = globalElements.get(j);
                        if (gson.toJson(elem).equals(gson.toJson(globalElem))) {
                            globalElements.remove(j--);
                        }
                    }

                    for (List<NCPFElement> list : lists) {
                        for (NCPFElement elem : list) {
                            if (gson.toJson(elem).equals(gson.toJson(globalElem))) {
                                globalElements.remove(i--);
                                continue globalLoop;
                            }
                        }
                    }
                }

                lists.add(globalElements);

                for (NCPFElement block : cfg.blocks) {
                    if (block.modules == null) {
                        continue;
                    }

                    Map<String, Object> blockRecipes = NCPFHelper.get(block.modules, "ncpf:block_recipes");
                    if (blockRecipes == null) {
                        continue;
                    }

                    List<NCPFElement> recipes = NCPFHelper.get(blockRecipes, "recipes");
                    lists.add(recipes);
                }

                globalElementsModule.put("elements", globalElements);
                cfg.modules.put("plannerator:global_elements", globalElementsModule);
                ncpf.configuration.put("nuclearcraft:overhaul_msr", cfg);
            }

            // Turbine
            {
                NCPFBuilder.configContext = "overhaul_turbine";
                NCPFOverhaulTurbineConfiguration cfg = new NCPFOverhaulTurbineConfiguration();

                Map<String, Object> settings = new HashMap<>();
                settings.put("min_width", NCConfig.turbine_min_size);
                settings.put("min_length", NCConfig.turbine_min_size);
                settings.put("max_size", NCConfig.turbine_max_size);
                settings.put("throughput_efficiency_leniency_multiplier", NCConfig.turbine_throughput_leniency_params[0]);
                settings.put("throughput_efficiency_leniency_threshold", NCConfig.turbine_throughput_leniency_params[1]);
                settings.put("throughput_factor", NCConfig.turbine_tension_throughput_factor);
                settings.put("fluid_per_blade", NCConfig.turbine_mb_per_blade);
                settings.put("power_bonus", NCConfig.turbine_power_bonus_multiplier);
                cfg.modules.put("nuclearcraft:overhaul_turbine_configuration_settings", settings);

                // Blocks
                for (var turbineEntityType : TURBINE_ENTITY_TYPE.values()) {
                    for (Block block : turbineEntityType.get().getValidBlocks()) {
                        NCPFBuilder.translate(cfg.blocks, block);
                    }
                }

                // Coolant Recipes
                NCPFBuilder.translate(cfg.recipes, NCRecipes.turbine);

                Map<String, Object> globalElementsModule = new HashMap<>();
                List<NCPFElement> globalElements = new ArrayList<>();

                NCPFBuilder.translateOutputs(globalElements, NCRecipes.turbine);

                List<List<NCPFElement>> lists = new ArrayList<>();
                lists.add(cfg.blocks);
                lists.add(cfg.recipes);

                globalLoop:
                for (int i = 0; i < globalElements.size(); ++i) {
                    NCPFElement globalElem = globalElements.get(i);
                    for (int j = 0; j < globalElements.size(); ++j) {
                        if (i == j) {
                            continue;
                        }

                        NCPFElement elem = globalElements.get(j);
                        if (gson.toJson(elem).equals(gson.toJson(globalElem))) {
                            globalElements.remove(j--);
                        }
                    }

                    for (List<NCPFElement> list : lists) {
                        for (NCPFElement elem : list) {
                            if (gson.toJson(elem).equals(gson.toJson(globalElem))) {
                                globalElements.remove(i--);
                                continue globalLoop;
                            }
                        }
                    }
                }

                lists.add(globalElements);

                for (NCPFElement block : cfg.blocks) {
                    if (block.modules == null) {
                        continue;
                    }

                    Map<String, Object> blockRecipes = NCPFHelper.get(block.modules, "ncpf:block_recipes");
                    if (blockRecipes == null) {
                        continue;
                    }

                    List<NCPFElement> recipes = NCPFHelper.get(blockRecipes, "recipes");
                    lists.add(recipes);
                }

                globalElementsModule.put("elements", globalElements);
                cfg.modules.put("plannerator:global_elements", globalElementsModule);

                ncpf.configuration.put("nuclearcraft:overhaul_turbine", cfg);
            }

            try (FileWriter writer = new FileWriter(new File(FMLPaths.CONFIGDIR.get().toFile(), "nuclearcraftneohaul.ncpf.json"))) {
                gson.toJson(ncpf, writer);
            }
        } catch (Exception e) {
            NCUtil.getLogger().error("Unable to create nuclearcraftneohaul.ncpf.json file!", e);
        }
    }

    private static void removeDuplicateNCPFElements(Gson gson, List<NCPFElement> globalElements, List<List<NCPFElement>> lists) {
        for (int i = 0; i < globalElements.size(); ++i) {
            NCPFElement globalElem = globalElements.get(i);
            Map<String, Object> globalModulesWas = globalElem.modules;
            globalElem.modules = null;

            for (int j = 0; j < globalElements.size(); ++j) {
                if (i == j) {
                    continue;
                }

                NCPFElement elem = globalElements.get(j);
                Map<String, Object> modulesWas = elem.modules;

                elem.modules = null;
                if (gson.toJson(elem).equals(gson.toJson(globalElem))) {
                    globalElements.remove(j--);
                }
                elem.modules = modulesWas;
            }

            listLoop:
            for (List<NCPFElement> list : lists) {
                for (NCPFElement elem : list) {
                    Map<String, Object> modulesWas = elem.modules;

                    elem.modules = null;
                    if (gson.toJson(elem).equals(gson.toJson(globalElem))) {
                        globalElements.remove(i--);
                        elem.modules = modulesWas;
                        break listLoop;
                    }
                    elem.modules = modulesWas;
                }
            }

            globalElem.modules = globalModulesWas;
        }
    }
}