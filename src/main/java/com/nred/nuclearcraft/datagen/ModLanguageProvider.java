package com.nred.nuclearcraft.datagen;

import com.nred.nuclearcraft.info.Fluids;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.LanguageProvider;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import static com.nred.nuclearcraft.NuclearcraftNeohaul.MODID;
import static com.nred.nuclearcraft.info.Names.*;
import static com.nred.nuclearcraft.registration.BlockRegistration.*;
import static com.nred.nuclearcraft.registration.FluidRegistration.*;
import static com.nred.nuclearcraft.registration.ItemRegistration.*;

public class ModLanguageProvider extends LanguageProvider {

    public ModLanguageProvider(DataGenerator gen, String locale) {
        super(gen.getPackOutput(), MODID, locale);
    }

    @Override
    protected void addTranslations() {
        advancements();
        ores();
        items();
        blocks();
        buckets();
        tooltips();
        messages();
        menus();
        creativeTabs();
        sounds();
        damage_types();
        guide_book();
        string_formatting();
        info();
        config();
        turbine();
        fission_reactor();
        heat_exchanger();

        add(MODID + ".multiblock.validation.reactor.invalid_frame_block", "Block at %1$d, %2$d, %3$d is not valid for use in the multiblock's frame");
        add(MODID + ".multiblock.validation.invalid_part_for_interior", "Block at %1$d, %2$d, %3$d is not valid for use in the multiblock's interior");

        add(SUPERCOLD_ICE.asItem(), capitalize(SUPERCOLD_ICE.getId().getPath()));
        add(TRITIUM_LAMP.asItem(), capitalize(TRITIUM_LAMP.getId().getPath()));
        add(HEAVY_WATER_MODERATOR.asItem(), capitalize(HEAVY_WATER_MODERATOR.getId().getPath()));
        add(UNIVERSAL_BIN.asItem(), capitalize(UNIVERSAL_BIN.getId().getPath()));
        add(DECAY_GENERATOR.asItem(), capitalize(DECAY_GENERATOR.getId().getPath()));
        add(MACHINE_INTERFACE.asItem(), capitalize(MACHINE_INTERFACE.getId().getPath()));
        add(SOLIDIFIED_CORIUM.asItem(), capitalize(SOLIDIFIED_CORIUM.getId().getPath()));
        add(LITHIUM_ION_CELL.asItem(), capitalize(LITHIUM_ION_CELL.getId().getPath()));
        add("jei.probability", "Production chance: %s%%");
    }

    private void advancements() {
        add("advancement." + MODID + ".root.title", "NuclearCraft");
        add("advancement." + MODID + ".root.description", "Nuclear Physics, Minecraft Style");

        add("advancement." + MODID + ".manufactory.title", "Industrializing");
        add("advancement." + MODID + ".manufactory.description", "Craft a Manufactory");

        add("advancement." + MODID + ".separator.title", "Fractionalization");
        add("advancement." + MODID + ".separator.description", "Craft a Separator");

        add("advancement." + MODID + ".basic_voltaic_pile.title", "Electric Avenue");
        add("advancement." + MODID + ".basic_voltaic_pile.description", "Craft a Basic Voltaic Pile");

        add("advancement." + MODID + ".electrolyzer.title", "Gift of Fizzacles");
        add("advancement." + MODID + ".electrolyzer.description", "Craft an Electrolyzer");

        add("advancement." + MODID + ".decay_generator.title", "Alpha Power");
        add("advancement." + MODID + ".decay_generator.description", "Craft a Decay Generator");

        add("advancement." + MODID + ".bin.title", "Mr Dilkington");
        add("advancement." + MODID + ".bin.description", "Craft a Universal Bin");

        add("advancement." + MODID + ".portable_ender_chest.title", "Mary Poppins");
        add("advancement." + MODID + ".portable_ender_chest.description", "Craft a Portable Ender Chest");

        add("advancement." + MODID + ".speed_upgrade.title", "Overclocking");
        add("advancement." + MODID + ".speed_upgrade.description", "Craft a Speed Upgrade");

        add("advancement." + MODID + ".energy_upgrade.title", "Polynomial Quenching");
        add("advancement." + MODID + ".energy_upgrade.description", "Craft an Energy Upgrade");

        add("advancement." + MODID + ".machine_interface.title", "Cubic Extension");
        add("advancement." + MODID + ".machine_interface.description", "Craft a Machine Interface");

        add("advancement." + MODID + ".alloy_furnace.title", "Smelting up a Storm");
        add("advancement." + MODID + ".alloy_furnace.description", "Craft an Alloy Furnace");

        add("advancement." + MODID + ".fuel_reprocessor.title", "Picking up the Pieces");
        add("advancement." + MODID + ".fuel_reprocessor.description", "Craft a Fuel Reprocessor");

        add("advancement." + MODID + ".decay_hastener.title", "Extreme Radiation");
        add("advancement." + MODID + ".decay_hastener.description", "Craft a Decay Hastener");

        add("advancement." + MODID + ".basic_lithium_ion_battery.title", "Volta's Footsteps");
        add("advancement." + MODID + ".basic_lithium_ion_battery.description", "Craft a Basic Lithium Ion Battery");

        add("advancement." + MODID + ".assembler.title", "Production Line");
        add("advancement." + MODID + ".assembler.description", "Craft an Assembler");

        add("advancement." + MODID + ".solar_panel_basic.title", "Eco-Friendly");
        add("advancement." + MODID + ".solar_panel_basic.description", "Craft a Basic Solar Panel");

        add("advancement." + MODID + ".rtg_uranium.title", "Warm Green Glow");
        add("advancement." + MODID + ".rtg_uranium.description", "Craft a Uranium RTG");

        add("advancement." + MODID + ".infuser.title", "Soaking it Up");
        add("advancement." + MODID + ".infuser.description", "Craft a Fluid Infuser");

        add("advancement." + MODID + ".melter.title", "Hot and Spicy");
        add("advancement." + MODID + ".melter.description", "Craft a Melter");

        add("advancement." + MODID + ".chemical_reactor.title", "The Full Poliakoff");
        add("advancement." + MODID + ".chemical_reactor.description", "Craft a Chemical Reactor");

        add("advancement." + MODID + ".supercooler.title", "Hoth Machine");
        add("advancement." + MODID + ".supercooler.description", "Craft a Supercooler");

        add("advancement." + MODID + ".extractor.title", "Squeezing the Sponge");
        add("advancement." + MODID + ".extractor.description", "Craft a Fluid Extractor");

        add("advancement." + MODID + ".enricher.title", "Chemical Brewery");
        add("advancement." + MODID + ".enricher.description", "Craft a Fluid Enricher");

        add("advancement." + MODID + ".nuclear_furnace.title", "Kitchen Nightmare");
        add("advancement." + MODID + ".nuclear_furnace.description", "Craft a Nuclear Furnace");

        add("advancement." + MODID + ".neptunium.title", "Sweet Neptune");
        add("advancement." + MODID + ".neptunium.description", "Obtain some Neptunium");

        add("advancement." + MODID + ".rtg_plutonium.title", "Demon Core");
        add("advancement." + MODID + ".rtg_plutonium.description", "Craft a Plutonium RTG");

        add("advancement." + MODID + ".ingot_former.title", "Ceramic Mold");
        add("advancement." + MODID + ".ingot_former.description", "Craft an Ingot Former");

        add("advancement." + MODID + ".salt_mixer.title", "Blend and Fold");
        add("advancement." + MODID + ".salt_mixer.description", "Craft a Fluid Mixer");

        add("advancement." + MODID + ".crystallizer.title", "Breaking Bad");
        add("advancement." + MODID + ".crystallizer.description", "Craft a Crystallizer");

        add("advancement." + MODID + ".centrifuge.title", "James Bond");
        add("advancement." + MODID + ".centrifuge.description", "Craft a Centrifuge");

        add("advancement." + MODID + ".rock_crusher.title", "Rolling Stones");
        add("advancement." + MODID + ".rock_crusher.description", "Craft a Rock Crusher");

        add("advancement." + MODID + ".electric_furnace.title", "Proof by Induction");
        add("advancement." + MODID + ".electric_furnace.description", "Craft an Electric Furnace");

        add("advancement." + MODID + ".plutonium.title", "Criticality");
        add("advancement." + MODID + ".plutonium.description", "Obtain some Plutonium");

        add("advancement." + MODID + ".rtg_americium.title", "Rad-Spangled Power");
        add("advancement." + MODID + ".rtg_americium.description", "Craft an Americium RTG");

        add("advancement." + MODID + ".pressurizer.title", "The Terror of Knowing");
        add("advancement." + MODID + ".pressurizer.description", "Craft a Pressurizer");

        add("advancement." + MODID + ".americium.title", "In Physics We Trust");
        add("advancement." + MODID + ".americium.description", "Obtain some Americium");

        add("advancement." + MODID + ".rtg_californium.title", "End of the Line");
        add("advancement." + MODID + ".rtg_californium.description", "Craft a Californium RTG");

        add("advancement." + MODID + ".curium.title", "Curious Marie");
        add("advancement." + MODID + ".curium.description", "Obtain some Curium");

        add("advancement." + MODID + ".berkelium.title", "Made in Alameda");
        add("advancement." + MODID + ".berkelium.description", "Obtain some Berkelium");

        add("advancement." + MODID + ".californium.title", "California Dreamin'");
        add("advancement." + MODID + ".californium.description", "Obtain some Californium");

        add("advancement." + MODID + ".solid_fuel_fission_controller.title", "Splitting the Atom [1/2]");
        add("advancement." + MODID + ".solid_fuel_fission_controller.description", "Craft a Solid Fuel Fission Controller");

        add("advancement." + MODID + ".molten_salt_fission_controller.title", "Next Gen Nuclear [1/2]");
        add("advancement." + MODID + ".molten_salt_fission_controller.description", "Craft a Molten Salt Fission Controller");

        add("advancement." + MODID + ".heat_exchanger_controller.title", "Thermal Contact [1/2]");
        add("advancement." + MODID + ".heat_exchanger_controller.description", "Craft a Heat Exchanger Controller");

        add("advancement." + MODID + ".condenser_controller.title", "Latent Ludwig [1/2]");
        add("advancement." + MODID + ".condenser_controller.description", "Craft a Condenser Controller");

        add("advancement." + MODID + ".turbine_controller.title", "Respecting Rankine [1/2]");
        add("advancement." + MODID + ".turbine_controller.description", "Craft a Turbine Controller");

        add("advancement." + MODID + ".electrolyzer_assembled.title", "Fizzing Electrodes");
        add("advancement." + MODID + ".electrolyzer_assembled.description", "Build a Multiblock Electrolyzer");

        add("advancement." + MODID + ".distiller_assembled.title", "Gertcha Cowson");
        add("advancement." + MODID + ".distiller_assembled.description", "Build a Multiblock Distiller");

        add("advancement." + MODID + ".infiltrator_assembled.title", "Hola Pablo");
        add("advancement." + MODID + ".infiltrator_assembled.description", "Build a Multiblock Infiltrator");

        add("advancement." + MODID + ".solid_fission_reactor_assembled.title", "Splitting the Atom [2/2]");
        add("advancement." + MODID + ".solid_fission_reactor_assembled.description", "Build a Solid Fuel Fission Reactor");

        add("advancement." + MODID + ".salt_fission_reactor_assembled.title", "Next Gen Nuclear [2/2]");
        add("advancement." + MODID + ".salt_fission_reactor_assembled.description", "Build a Molten Salt Fission Reactor");

        add("advancement." + MODID + ".heat_exchanger_assembled.title", "Thermal Contact [2/2]");
        add("advancement." + MODID + ".heat_exchanger_assembled.description", "Build a Heat Exchanger");

        add("advancement." + MODID + ".condenser_assembled.title", "Latent Ludwig [2/2]");
        add("advancement." + MODID + ".condenser_assembled.description", "Build a Condenser");

        add("advancement." + MODID + ".turbine_assembled.title", "Respecting Rankine [2/2]");
        add("advancement." + MODID + ".turbine_assembled.description", "Build a Turbine");

        add("advancement." + MODID + ".geiger_counter.title", "Pripyat Preparations");
        add("advancement." + MODID + ".geiger_counter.description", "Craft a Geiger Counter");

        add("advancement." + MODID + ".radaway.title", "Ridding the Rads");
        add("advancement." + MODID + ".radaway.description", "Make some RadAway");

        add("advancement." + MODID + ".rad_x.title", "Imitative Immunity");
        add("advancement." + MODID + ".rad_x.description", "Make some Rad-X");

        add("advancement." + MODID + ".rad_shielding.title", "Brave Sir Robin");
        add("advancement." + MODID + ".rad_shielding.description", "Craft some Heavy Radiation Shielding");

        add("advancement." + MODID + ".radiation_scrubber.title", "Pollution Control");
        add("advancement." + MODID + ".radiation_scrubber.description", "Craft a Radiation Scrubber");

        add("advancement." + MODID + ".marshmallow.title", "The Lansdell Manoeuvre");
        add("advancement." + MODID + ".marshmallow.description", "Make a Marshmallow");

        add("advancement." + MODID + ".smore.title", "Gather Round the Campfire");
        add("advancement." + MODID + ".smore.description", "Make a S'more S'mingot");

        add("advancement." + MODID + ".moresmore.title", "S'more of the Same");
        add("advancement." + MODID + ".moresmore.description", "Make a MoreS'more DoubleS'mingot");

        add("advancement." + MODID + ".foursmore.title", "Even S'more Where That Came From");
        add("advancement." + MODID + ".foursmore.description", "Make a FourS'more QuadS'mingot");
    }

    private void ores() {
        for (String ore : ORES) {
            add(ORE_MAP.get(ore).get(), StringUtils.capitalize(ore) + " Ore");
            add(ORE_MAP.get(ore + "_deepslate").get(), StringUtils.capitalize(ore) + " Deepslate Ore");
        }
    }

    private void info() {
        add(MODID + ".info.reflector", "Fission Reactor Reflector");
        add(MODID + ".info.reflector.reflectivity", "Reflectivity Factor: %s");
        add(MODID + ".info.reflector.efficiency", "Efficiency Multiplier: %s");

        add(MODID + ".info.fission_fuel", "Fission Reactor Fuel");
        add(MODID + ".info.fission_fuel.base_time", "Base Depletion Time: %s");
        add(MODID + ".info.fission_fuel.base_heat", "Base Heat Gen: %s");
        add(MODID + ".info.fission_fuel.base_efficiency", "Efficiency Multiplier: %s");
        add(MODID + ".info.fission_fuel.criticality", "Criticality Factor: %s");
        add(MODID + ".info.fission_fuel.decay_factor", "Decay Factor: %s");
        add(MODID + ".info.fission_fuel.self_priming", "Self-priming!");
    }

    private void guide_book() {
        add(MODID + ".guide_book.name", "NuclearCraft Guide");
        add(MODID + ".guide_book.edition", "Neohaul Edition");
        add(MODID + ".guide_book.desc", "NuclearCraft (NC) is a tech mod focused on nuclear power generation. Most of the mod's features and mechanics are inspired by real equivalents, though there are many simplifications, abstractions, and deviations from reality for the sake of interesting gameplay.");
    }

    private void turbine() {
        add(MODID + ".tooltip.turbine_controller", "The heart of the multiblock - one is needed in the wall for it to form.");
        add(MODID + ".tooltip.turbine_rotor_shaft", "Connects the rotor blades to the dynamo to convert the generated kinetic energy into electrical energy. Must be placed axially as a cuboid along the center of the multiblock interior.");
        add(MODID + ".tooltip.turbine_rotor_blade_efficiency", "Efficiency Multiplier: %s");
        add(MODID + ".tooltip.turbine_rotor_blade_expansion", "Expansion Coefficient: %s");
        add("block." + MODID + ".turbine_rotor_blade.desc", "Used to convert the energy of the oncoming fluid flow into rotational energy in the rotor shaft. The expansion coefficient is larger than unity, so the volume of the fluid flow will increase each time it passes through a set. Must be placed in complete sets of four coplanar groups extending from the multiblock shaft to the wall. Each blade block can process up to %s of oncoming fluid.");
        add(MODID + ".tooltip.turbine_rotor_stator_expansion", "Expansion Coefficient: %s");
        add("block." + MODID + ".turbine_rotor_stator.desc", "Used to increase the density of the oncoming fluid flow, which can increase the efficiency of the multiblock by counteracting overexpansion generated by sets of rotor blades. Must be placed in complete sets of four coplanar groups extending from the multiblock shaft to the wall.");
        add(MODID + ".tooltip.turbine_rotor_bearing", "Connects the rotor shaft to the multiblock wall and dynamo. Must cover the full area of each end of the shaft. Dynamo coils must be connected to the bearings either directly or via other dynamo coils and dynamo coil connectors. Connecting at least as many coils as bearing blocks at each end will avoid efficiency penalties.");
        add(MODID + ".tooltip.turbine_dynamo_coil.conductivity", "Conductivity Multiplier: %s");
        add("block." + MODID + ".turbine_computer_port.desc", "Used to access the turbine via CC: Tweaked.");

        add(MODID + ".recipe_viewer.turbine_energy_density", "Base Energy Density: %s");
        add(MODID + ".recipe_viewer.turbine_expansion", "Fluid Expansion: %s");
        add(MODID + ".recipe_viewer.turbine_spin_up_multiplier", "Spin-up Multiplier: %s");

        add(MODID + ".recipe_viewer.condenser_fluid_temp_in", "Input Temperature: %s");
        add(MODID + ".recipe_viewer.condenser_fluid_temp_out", "Output Temperature: %s");
        add(MODID + ".recipe_viewer.condenser_cooling_required", "Cooling Required: %s");
        add(MODID + ".recipe_viewer.condenser_horizontal_bonus", "Horizontal Flow Bonus: %s");
        add(MODID + ".recipe_viewer.condenser_upward_bonus", "Upward Flow Bonus: %s");
        add(MODID + ".recipe_viewer.condenser_downward_bonus", "Downward Flow Bonus: %s");


        add(MODID + ".recipe_viewer.exchanger_fluid_temp_in", "Input Temperature: %s");
        add(MODID + ".recipe_viewer.exchanger_fluid_temp_out", "Output Temperature: %s");
        add(MODID + ".recipe_viewer.exchanger_heating_required", "Heating Required: %s");
        add(MODID + ".recipe_viewer.exchanger_cooling_required", "Cooling Required: %s");
        add(MODID + ".recipe_viewer.exchanger_horizontal_bonus", "Horizontal Flow Bonus: %s");
        add(MODID + ".recipe_viewer.exchanger_upward_bonus", "Upward Flow Bonus: %s");
        add(MODID + ".recipe_viewer.exchanger_downward_bonus", "Downward Flow Bonus: %s");

        add(MODID + ".recipe_viewer.condenser_dissipation_fluid_temp", "Temperature: %s");

        add(MODID + ".multiblock_validation.turbine.need_bearings", "There must be two sets of rotor bearings in opposing walls of the multiblock for it to form");
        add(MODID + ".multiblock_validation.turbine.bearings_side_square", "The walls housing the rotor bearings must be square for the multiblock to form");
        add(MODID + ".multiblock_validation.turbine.valve_wrong_wall", "Inlets and outlets must be installed opposite each other on the walls housing the rotor bearings for the multiblock to form");
        add(MODID + ".multiblock_validation.turbine.bearings_center_and_square", "The sets of rotor bearings must be square and centered in the middle of each opposing wall for the multiblock to form");
        add(MODID + ".multiblock_validation.turbine.shaft_center", "The rotor shaft must be fully connected to each set of rotor bearings for the multiblock to form");
        add(MODID + ".multiblock_validation.turbine.space_between_blades", "The space between the sets of rotor blades and stators must be empty for the multiblock to form");
        add(MODID + ".multiblock_validation.turbine.different_type_blades", "The rotor blades or stators about each section of the rotor shaft must be of the same type for the multiblock to form");
        add(MODID + ".multiblock_validation.turbine.missing_blades", "Each section of the rotor shaft must have rotor blades or stators of the same type extend fully to the wall for the multiblock to form");
    }

    private void fission_reactor() {
        add(MODID + ".tooltip.solid_fission_sink.cooling_rate", "Cooling Rate: %s H/t");
        add(MODID + ".tooltip.salt_fission_heater.cooling_rate", "Cooling Rate: %s H/t");

        add(MODID + ".tooltip.solid_fission_controller", "Solid Fuel Reactor");
        add(MODID + ".tooltip.solid_fission_controller.output_rate", "Production Rate: %s");

        add(MODID + ".tooltip.salt_fission_controller", "Molten Salt Reactor");
        add(MODID + ".tooltip.salt_fission_controller.heating_speed_multiplier", "Mean Heating Speed: %s");

        add(MODID + ".tooltip.fission_controller.clusters", "Number of Clusters: %s");
        add(MODID + ".tooltip.fission_controller.sparsity", "Sparsity Efficiency: %s");
        add(MODID + ".tooltip.fission_controller.useful_parts", "Useful Components: %s");
        add(MODID + ".tooltip.fission_controller.temperature", "Casing Temp: %s");
        add(MODID + ".tooltip.fission_controller.net_heating", "Net Casing Heating: %s");
        add(MODID + ".tooltip.fission_controller.effective_heat", "Effective Casing Heat: %s");
        add(MODID + ".tooltip.fission_controller.reserved_heat", "Reserved Casing Heat: %s");
        add(MODID + ".tooltip.fission_controller.net_cluster_heating", "Net Cluster Heating: %s");
        add(MODID + ".tooltip.fission_controller.total_cluster_cooling", "Total Cluster Cooling: %s");
        add(MODID + ".tooltip.fission_controller.efficiency", "Mean Efficiency: %s");
        add(MODID + ".tooltip.fission_controller.heat_mult", "Mean Heat Multiplier: %s");
        add(MODID + ".tooltip.fission_controller.heat_stored", "Casing Heat Level: %s");
        add("block." + MODID + ".fission_computer_port.desc", "Used to access the reactor via CC: Tweaked.");

        add(MODID + ".tooltip.fission_source.efficiency", "Efficiency Multiplier: %s");
        add(MODID + ".tooltip.fission_shield.efficiency", "Efficiency Multiplier: %s");
        add(MODID + ".tooltip.fission_shield.heat_per_flux", "Heat Gen Per Flux: %s");

        add(MODID + ".tooltip.fission_component.heat_stored", "Cluster Heat Level: %s");

        add(MODID + ".fission_reactor_source.no_target", "Has no target!");
        add(MODID + ".fission_reactor_source.target", "Targeting %4$s at [%1$d, %2$d, %3$d]");

        add(MODID + ".multiblock_validation.fission_reactor.prohibit_cells", "This is not a solid fuel reactor - there must be no cells for it to form");
        add(MODID + ".multiblock_validation.fission_reactor.prohibit_sinks", "This is not a solid fuel reactor - there must be no sinks for it to form");
        add(MODID + ".multiblock_validation.fission_reactor.prohibit_vessels", "This is not a molten salt reactor - there must be no vessels for it to form");
        add(MODID + ".multiblock_validation.fission_reactor.prohibit_heaters", "This is not a molten salt reactor - there must be no heaters for it to form");

        add(MODID + ".recipe_viewer.irradiator_flux_required", "Total Flux Required: %s");
        add(MODID + ".recipe_viewer.irradiator_heat_per_flux", "Heat Gen Per Flux: %s");
        add(MODID + ".recipe_viewer.irradiator_process_efficiency", "Efficiency Bonus: %s");
        add(MODID + ".recipe_viewer.irradiator_valid_flux_minimum", "Minimum Valid Flux: %s");
        add(MODID + ".recipe_viewer.irradiator_valid_flux_maximum", "Maximum Valid Flux: %s");
        add(MODID + ".recipe_viewer.irradiator_valid_flux_range", "Valid Flux Range: %s");
        add(MODID + ".recipe_viewer.radiation_per_flux", "Radiation Per Flux: %s");
        add(MODID + ".recipe_viewer.coolant_heater_rate", "Cooling Rate: %s");
        add(MODID + ".recipe_viewer.fission_emergency_cooling_heating_required", "Heating Required: %s");
        add(MODID + ".recipe_viewer.fission_heating_required", "Heating Required: %s");
    }

    private void heat_exchanger() {
        add(MODID + ".multiblock_validation.heat_exchanger.invalid_shell", "Shell must have at least one inlet and one outlet for the heat exchanger to form");
        add(MODID + ".multiblock_validation.heat_exchanger.blocked_inlet", "Shell inlet must not be blocked by baffle for the heat exchanger to form");
        add(MODID + ".multiblock_validation.heat_exchanger.blocked_outlet", "Shell outlet must not be blocked by baffle for the heat exchanger to form");
        add(MODID + ".multiblock_validation.heat_exchanger.dangling_tube", "Tubes must not have dangling connections for the heat exchanger to form");
        add(MODID + ".multiblock_validation.heat_exchanger.invalid_network", "Tube networks must have at least one inlet and one outlet for the heat exchanger to form");
        add(MODID + ".multiblock_validation.heat_exchanger.blocked_shell", "Each shell inlet must have an available shell outlet for the heat exchanger to form");

        add(MODID + ".multiblock_validation.condenser.dangling_tube", "Tubes must not have dangling connections for the condenser to form");
        add(MODID + ".multiblock_validation.condenser.invalid_network", "Tube networks must have at least one inlet and one outlet for the condenser to form");
        add(MODID + ".multiblock_validation.condenser.blocked_shell", "Shell interior must be completely connected for the condenser to form");

        add(MODID + ".tooltip.heat_exchanger_tube.heat_transfer_coefficient", "Heat Transfer Coefficient: %s");
        add(MODID + ".tooltip.heat_exchanger_tube.heat_retention_mult", "Heat Retention Multiplier: %s");
        add("block." + MODID + ".heat_exchanger_tube.desc", "Transfers heat between fluids.");
        add("block." + MODID + ".heat_exchanger_computer_port.desc", "Used to access the exchanger via CC: Tweaked.");
    }

    private void string_formatting() {
        add("nc.sf.plural_rule", "1,1:0");

        add("nc.sf.zero", "zero");
        add("nc.sf.one", "one");
        add("nc.sf.two", "two");
        add("nc.sf.three", "three");
        add("nc.sf.four", "four");
        add("nc.sf.five", "five");
        add("nc.sf.six", "six");

        add("nc.sf.no", "no");

        add("nc.sf.and", "and");
        add("nc.sf.or", "or");

        add("nc.sf.placement_rule.and_or.adjacent0", "%1$s %2$s");
        add("nc.sf.placement_rule.and_or.adjacent1", "%1$s, %2$s");
        add("nc.sf.placement_rule.and_or.adjacent2", "%1$s %2$s %3$s");
        add("nc.sf.placement_rule.and_or.adjacent3", "%1$s, %2$s %3$s");
        add("nc.sf.placement_rule.and_or.adjacent4", "%1$s.");

        add("nc.sf.placement_rule.adjacent.must_be_adjacent_to", "Must be adjacent to");
        add("nc.sf.placement_rule.adjacent.at_least", "at least %1$s");
        add("nc.sf.placement_rule.adjacent.at_least_along_axis", "at least %1$s along a common axis");
        add("nc.sf.placement_rule.adjacent.at_least_along_axes", "at least %1$s along common axes");
        add("nc.sf.placement_rule.adjacent.at_least_at_vertex", "at least %1$s at a common vertex");
        add("nc.sf.placement_rule.adjacent.at_least_along_edge", "at least %1$s at a common edge");
        add("nc.sf.placement_rule.adjacent.exactly", "exactly %1$s");
        add("nc.sf.placement_rule.adjacent.exactly_along_axis", "exactly %1$s which must share a common axis");
        add("nc.sf.placement_rule.adjacent.exactly_along_axes", "exactly %1$s which must share common axes");
        add("nc.sf.placement_rule.adjacent.exactly_at_vertex", "exactly %1$s which must share a common vertex");
        add("nc.sf.placement_rule.adjacent.exactly_along_edge", "exactly %1$s which must share a common edge");
        add("nc.sf.placement_rule.adjacent.at_most", "at most %1$s");
        add("nc.sf.placement_rule.adjacent.at_most_along_axis", "at most %1$s along a common axis");
        add("nc.sf.placement_rule.adjacent.at_most_along_axes", "at most %1$s along common axes");
        add("nc.sf.placement_rule.adjacent.no", "absolutely %1$s");

        add("nc.sf.placement_rule.adjacent.ambiguity0prev", "!common axis&&!common axes&&!common vertex&&!common edge");
        add("nc.sf.placement_rule.adjacent.ambiguity0last", "common axis||common axes||common vertex||common edge");
        add("nc.sf.placement_rule.adjacent.ambiguity1prev", "share a common||share common");

        add("nc.sf.reactor_casing0", "%s reactor casing");
        add("nc.sf.reactor_casing1", "%s reactor casing");
        add("nc.sf.conductor0", "%s conductor");
        add("nc.sf.conductor1", "%s conductors");

        add("nc.sf.cell0", "%s functional fuel cell");
        add("nc.sf.cell1", "%s functional fuel cells");
        add("nc.sf.vessel0", "%s functional fuel vessel");
        add("nc.sf.vessel1", "%s functional fuel vessels");

        add("nc.sf.moderator0", "%s active moderator");
        add("nc.sf.moderator1", "%s active moderators");
        add("nc.sf.reflector0", "%s active reflector");
        add("nc.sf.reflector1", "%s active reflectors");

        add("nc.sf.irradiator0", "%s functional irradiator");
        add("nc.sf.irradiator1", "%s functional irradiators");
        add("nc.sf.shield0", "%s valid open shield");
        add("nc.sf.shield1", "%s valid open shields");

        add("nc.sf.any_sink0", "%s sink of any type");
        add("nc.sf.any_sink1", "%s sinks of any type");
        add("nc.sf.water_sink0", "%s valid water sink");
        add("nc.sf.water_sink1", "%s valid water sinks");
        add("nc.sf.iron_sink0", "%s valid iron sink");
        add("nc.sf.iron_sink1", "%s valid iron sinks");
        add("nc.sf.redstone_sink0", "%s valid redstone sink");
        add("nc.sf.redstone_sink1", "%s valid redstone sinks");
        add("nc.sf.quartz_sink0", "%s valid quartz sink");
        add("nc.sf.quartz_sink1", "%s valid quartz sinks");
        add("nc.sf.obsidian_sink0", "%s valid obsidian sink");
        add("nc.sf.obsidian_sink1", "%s valid obsidian sinks");
        add("nc.sf.nether_brick_sink0", "%s valid nether brick sink");
        add("nc.sf.nether_brick_sink1", "%s valid nether brick sinks");
        add("nc.sf.glowstone_sink0", "%s valid glowstone sink");
        add("nc.sf.glowstone_sink1", "%s valid glowstone sinks");
        add("nc.sf.lapis_sink0", "%s valid lapis sink");
        add("nc.sf.lapis_sink1", "%s valid lapis sinks");
        add("nc.sf.gold_sink0", "%s valid gold sink");
        add("nc.sf.gold_sink1", "%s valid gold sinks");
        add("nc.sf.prismarine_sink0", "%s valid prismarine sink");
        add("nc.sf.prismarine_sink1", "%s valid prismarine sinks");
        add("nc.sf.slime_sink0", "%s valid slime sink");
        add("nc.sf.slime_sink1", "%s valid slime sinks");
        add("nc.sf.end_stone_sink0", "%s valid end stone sink");
        add("nc.sf.end_stone_sink1", "%s valid end stone sinks");
        add("nc.sf.purpur_sink0", "%s valid purpur sink");
        add("nc.sf.purpur_sink1", "%s valid purpur sinks");
        add("nc.sf.diamond_sink0", "%s valid diamond sink");
        add("nc.sf.diamond_sink1", "%s valid diamond sinks");
        add("nc.sf.emerald_sink0", "%s valid emerald sink");
        add("nc.sf.emerald_sink1", "%s valid emerald sinks");
        add("nc.sf.copper_sink0", "%s valid copper sink");
        add("nc.sf.copper_sink1", "%s valid copper sinks");
        add("nc.sf.tin_sink0", "%s valid tin sink");
        add("nc.sf.tin_sink1", "%s valid tin sinks");
        add("nc.sf.lead_sink0", "%s valid lead sink");
        add("nc.sf.lead_sink1", "%s valid lead sinks");
        add("nc.sf.boron_sink0", "%s valid boron sink");
        add("nc.sf.boron_sink1", "%s valid boron sinks");
        add("nc.sf.lithium_sink0", "%s valid lithium sink");
        add("nc.sf.lithium_sink1", "%s valid lithium sinks");
        add("nc.sf.magnesium_sink0", "%s valid magnesium sink");
        add("nc.sf.magnesium_sink1", "%s valid magnesium sinks");
        add("nc.sf.manganese_sink0", "%s valid manganese sink");
        add("nc.sf.manganese_sink1", "%s valid manganese sinks");
        add("nc.sf.aluminum_sink0", "%s valid aluminum sink");
        add("nc.sf.aluminum_sink1", "%s valid aluminum sinks");
        add("nc.sf.silver_sink0", "%s valid silver sink");
        add("nc.sf.silver_sink1", "%s valid silver sinks");
        add("nc.sf.fluorite_sink0", "%s valid fluorite sink");
        add("nc.sf.fluorite_sink1", "%s valid fluorite sinks");
        add("nc.sf.villiaumite_sink0", "%s valid villiaumite sink");
        add("nc.sf.villiaumite_sink1", "%s valid villiaumite sinks");
        add("nc.sf.carobbiite_sink0", "%s valid carobbiite sink");
        add("nc.sf.carobbiite_sink1", "%s valid carobbiite sinks");
        add("nc.sf.arsenic_sink0", "%s valid arsenic sink");
        add("nc.sf.arsenic_sink1", "%s valid arsenic sinks");
        add("nc.sf.liquid_nitrogen_sink0", "%s valid liquid nitrogen sink");
        add("nc.sf.liquid_nitrogen_sink1", "%s valid liquid nitrogen sinks");
        add("nc.sf.liquid_helium_sink0", "%s valid liquid helium sink");
        add("nc.sf.liquid_helium_sink1", "%s valid liquid helium sinks");
        add("nc.sf.enderium_sink0", "%s valid enderium sink");
        add("nc.sf.enderium_sink1", "%s valid enderium sinks");
        add("nc.sf.cryotheum_sink0", "%s valid cryotheum sink");
        add("nc.sf.cryotheum_sink1", "%s valid cryotheum sinks");

        add("nc.sf.any_heater0", "%s heater of any type");
        add("nc.sf.any_heater1", "%s heaters of any type");
        add("nc.sf.standard_heater0", "%s functional standard heater");
        add("nc.sf.standard_heater1", "%s functional standard heaters");
        add("nc.sf.iron_heater0", "%s functional iron heater");
        add("nc.sf.iron_heater1", "%s functional iron heaters");
        add("nc.sf.redstone_heater0", "%s functional redstone heater");
        add("nc.sf.redstone_heater1", "%s functional redstone heaters");
        add("nc.sf.quartz_heater0", "%s functional quartz heater");
        add("nc.sf.quartz_heater1", "%s functional quartz heaters");
        add("nc.sf.obsidian_heater0", "%s functional obsidian heater");
        add("nc.sf.obsidian_heater1", "%s functional obsidian heaters");
        add("nc.sf.nether_brick_heater0", "%s functional nether brick heater");
        add("nc.sf.nether_brick_heater1", "%s functional nether brick heaters");
        add("nc.sf.glowstone_heater0", "%s functional glowstone heater");
        add("nc.sf.glowstone_heater1", "%s functional glowstone heaters");
        add("nc.sf.lapis_heater0", "%s functional lapis heater");
        add("nc.sf.lapis_heater1", "%s functional lapis heaters");
        add("nc.sf.gold_heater0", "%s functional gold heater");
        add("nc.sf.gold_heater1", "%s functional gold heaters");
        add("nc.sf.prismarine_heater0", "%s functional prismarine heater");
        add("nc.sf.prismarine_heater1", "%s functional prismarine heaters");
        add("nc.sf.slime_heater0", "%s functional slime heater");
        add("nc.sf.slime_heater1", "%s functional slime heaters");
        add("nc.sf.end_stone_heater0", "%s functional end stone heater");
        add("nc.sf.end_stone_heater1", "%s functional end stone heaters");
        add("nc.sf.purpur_heater0", "%s functional purpur heater");
        add("nc.sf.purpur_heater1", "%s functional purpur heaters");
        add("nc.sf.diamond_heater0", "%s functional diamond heater");
        add("nc.sf.diamond_heater1", "%s functional diamond heaters");
        add("nc.sf.emerald_heater0", "%s functional emerald heater");
        add("nc.sf.emerald_heater1", "%s functional emerald heaters");
        add("nc.sf.copper_heater0", "%s functional copper heater");
        add("nc.sf.copper_heater1", "%s functional copper heaters");
        add("nc.sf.tin_heater0", "%s functional tin heater");
        add("nc.sf.tin_heater1", "%s functional tin heaters");
        add("nc.sf.lead_heater0", "%s functional lead heater");
        add("nc.sf.lead_heater1", "%s functional lead heaters");
        add("nc.sf.boron_heater0", "%s functional boron heater");
        add("nc.sf.boron_heater1", "%s functional boron heaters");
        add("nc.sf.lithium_heater0", "%s functional lithium heater");
        add("nc.sf.lithium_heater1", "%s functional lithium heaters");
        add("nc.sf.magnesium_heater0", "%s functional magnesium heater");
        add("nc.sf.magnesium_heater1", "%s functional magnesium heaters");
        add("nc.sf.manganese_heater0", "%s functional manganese heater");
        add("nc.sf.manganese_heater1", "%s functional manganese heaters");
        add("nc.sf.aluminum_heater0", "%s functional aluminum heater");
        add("nc.sf.aluminum_heater1", "%s functional aluminum heaters");
        add("nc.sf.silver_heater0", "%s functional silver heater");
        add("nc.sf.silver_heater1", "%s functional silver heaters");
        add("nc.sf.fluorite_heater0", "%s functional fluorite heater");
        add("nc.sf.fluorite_heater1", "%s functional fluorite heaters");
        add("nc.sf.villiaumite_heater0", "%s functional villiaumite heater");
        add("nc.sf.villiaumite_heater1", "%s functional villiaumite heaters");
        add("nc.sf.carobbiite_heater0", "%s functional carobbiite heater");
        add("nc.sf.carobbiite_heater1", "%s functional carobbiite heaters");
        add("nc.sf.arsenic_heater0", "%s functional arsenic heater");
        add("nc.sf.arsenic_heater1", "%s functional arsenic heaters");
        add("nc.sf.liquid_nitrogen_heater0", "%s functional liquid nitrogen heater");
        add("nc.sf.liquid_nitrogen_heater1", "%s functional liquid nitrogen heaters");
        add("nc.sf.liquid_helium_heater0", "%s functional liquid helium heater");
        add("nc.sf.liquid_helium_heater1", "%s functional liquid helium heaters");
        add("nc.sf.enderium_heater0", "%s functional enderium heater");
        add("nc.sf.enderium_heater1", "%s functional enderium heaters");
        add("nc.sf.cryotheum_heater0", "%s functional cryotheum heater");
        add("nc.sf.cryotheum_heater1", "%s functional cryotheum heaters");

        add("nc.sf.turbine_casing0", "%s multiblock casing");
        add("nc.sf.turbine_casing1", "%s multiblock casing");
        add("nc.sf.bearing0", "%s rotor bearing");
        add("nc.sf.connector0", "%s valid coil connector");
        add("nc.sf.connector1", "%s valid coil connectors");

        add("nc.sf.any_coil0", "%s of any valid dynamo coil");
        add("nc.sf.any_coil1", "any %s valid dynamo coils");
        add("nc.sf.magnesium_coil0", "%s valid magnesium dynamo coil");
        add("nc.sf.magnesium_coil1", "%s valid magnesium dynamo coils");
        add("nc.sf.beryllium_coil0", "%s valid beryllium dynamo coil");
        add("nc.sf.beryllium_coil1", "%s valid beryllium dynamo coils");
        add("nc.sf.aluminum_coil0", "%s valid aluminum dynamo coil");
        add("nc.sf.aluminum_coil1", "%s valid aluminum dynamo coils");
        add("nc.sf.gold_coil0", "%s valid gold dynamo coil");
        add("nc.sf.gold_coil1", "%s valid gold dynamo coils");
        add("nc.sf.copper_coil0", "%s valid copper dynamo coil");
        add("nc.sf.copper_coil1", "%s valid copper dynamo coils");
        add("nc.sf.silver_coil0", "%s valid silver dynamo coil");
        add("nc.sf.silver_coil1", "%s valid silver dynamo coils");

        add("nc.sf.power_adverb0", "be constant");
        add("nc.sf.power_adverb1", "%s linearly");
        add("nc.sf.power_adverb2", "%s quadratically");
        add("nc.sf.power_adverb3", "%s cubically");
        add("nc.sf.power_adverb4", "%s quartically");
        add("nc.sf.power_adverb5", "%s quintically");
        add("nc.sf.power_adverb6", "%s sextically");
        add("nc.sf.power_adverb7", "%s septically");
        add("nc.sf.power_adverb8", "%s octically");
        add("nc.sf.power_adverb9", "%s nonically");
        add("nc.sf.power_adverb10", "%s decically");
        add("nc.sf.power_adverb11", "%s undecically");
        add("nc.sf.power_adverb12", "%s duodecically");
        add("nc.sf.power_adverb13", "%s tredecically");
        add("nc.sf.power_adverb14", "%s quattuordecically");
        add("nc.sf.power_adverb15", "%s quindecically");

        add("nc.sf.increase", "increase");
        add("nc.sf.decrease", "decrease");
        add("nc.sf.increase_approximately", "increase approximately");
        add("nc.sf.decrease_approximately", "decrease approximately");
        add("nc.sf.power_adverb_preposition", "%1$s %2$s");
        add("nc.sf.with", "with");

        add("nc.sf.ordinal1", "first");
        add("nc.sf.ordinal2", "second");
        add("nc.sf.ordinal3", "third");
        add("nc.sf.ordinal4", "fourth");
        add("nc.sf.ordinal5", "fifth");
        add("nc.sf.ordinal6", "sixth");
        add("nc.sf.ordinal7", "seventh");
        add("nc.sf.ordinal8", "eighth");
        add("nc.sf.ordinal9", "ninth");
        add("nc.sf.ordinal10", "tenth");
        add("nc.sf.ordinal11", "eleventh");
        add("nc.sf.ordinal12", "twelfth");
        add("nc.sf.ordinal13", "thirteenth");
        add("nc.sf.ordinal14", "fourteenth");
        add("nc.sf.ordinal15", "fifteenth");
        add("nc.sf.ordinal16", "sixteenth");
    }

    private void items() {
        simpleItems(INGOTS, INGOT_MAP, " Ingot");
        simpleItems(GEMS, GEM_MAP, "");
        simpleItems(DUSTS, DUST_MAP, " Dust");
        replaceItems(FISSION_DUSTS, FISSION_DUST_MAP, "", " Dust", Map.of("tbp", "Protactinium-Enriched Thorium Dust"));
        replaceItems(GEM_DUSTS, GEM_DUST_MAP, "Crushed ", "", Map.of("boron_nitride", "Hexagonal Boron Nitride", "sulfur", "Sulfur"));
        simpleItems(RAWS, RAW_MAP, "Raw ", "");
        simpleItems(NUGGETS, NUGGET_MAP, " Nugget");
        replaceItems(ALLOYS, ALLOY_MAP, "", " Alloy Ingot", Map.of("hsla_steel", "HSLA Steel"));
        replaceItems(PARTS, PART_MAP, "", "", Map.of("du_plating", "DU Plating"));
        simpleItems(PART_BLOCKS, PART_BLOCK_MAP, "");
        replaceItems(COMPOUNDS, COMPOUND_MAP, "", "", Map.of("c_mn_blend", "Carbon-Manganese Blend"));
        simpleItems(UPGRADES, UPGRADE_MAP, " Upgrade");

        add(GLOWING_MUSHROOM.get(), "Glowing Mushroom");

        fuelTypeItems(AMERICIUM_MAP, "Americium-");
        fuelTypeItems(BERKELIUM_MAP, "Berkelium-");
        fuelTypeItems(BORON_MAP, "Boron-");
        fuelTypeItems(CALIFORNIUM_MAP, "Californium-");
        fuelTypeItems(CURIUM_MAP, "Curium-");
        fuelTypeItems(LITHIUM_MAP, "Lithium-");
        fuelTypeItems(NEPTUNIUM_MAP, "Neptunium-");
        fuelTypeItems(PLUTONIUM_MAP, "Plutonium-");
        fuelTypeItems(URANIUM_MAP, "Uranium-");

        fuelPelletItems(PELLET_AMERICIUM_MAP);
        fuelPelletItems(PELLET_BERKELIUM_MAP);
        fuelPelletItems(PELLET_CALIFORNIUM_MAP);
        fuelPelletItems(PELLET_CURIUM_MAP);
        fuelPelletItems(PELLET_MIXED_MAP);
        fuelPelletItems(PELLET_NEPTUNIUM_MAP);
        fuelPelletItems(PELLET_PLUTONIUM_MAP);
        fuelPelletItems(PELLET_THORIUM_MAP);
        fuelPelletItems(PELLET_URANIUM_MAP);

        fuelPelletTypeItems(FUEL_AMERICIUM_MAP, "");
        fuelPelletTypeItems(FUEL_BERKELIUM_MAP, "");
        fuelPelletTypeItems(FUEL_CALIFORNIUM_MAP, "");
        fuelPelletTypeItems(FUEL_CURIUM_MAP, "");
        fuelPelletTypeItems(FUEL_NEPTUNIUM_MAP, "");
        fuelPelletTypeItems(FUEL_PLUTONIUM_MAP, "");
        fuelPelletTypeItems(FUEL_THORIUM_MAP, "");
        fuelPelletTypeItems(FUEL_URANIUM_MAP, "");

        add(FUEL_MIXED_MAP.get("mix_239_ni").get(), "MNI-239 Fuel Pellet");
        add(FUEL_MIXED_MAP.get("mix_239_ox").get(), "MOX-239 Fuel Pellet");
        add(FUEL_MIXED_MAP.get("mix_239_tr").get(), "MTRISO-239 Fuel Pellet");
        add(FUEL_MIXED_MAP.get("mix_239_za").get(), "MZA-239 Fuel Pellet");
        add(FUEL_MIXED_MAP.get("mix_241_ni").get(), "MNI-241 Fuel Pellet");
        add(FUEL_MIXED_MAP.get("mix_241_ox").get(), "MOX-241 Fuel Pellet");
        add(FUEL_MIXED_MAP.get("mix_241_tr").get(), "MTRISO-241 Fuel Pellet");
        add(FUEL_MIXED_MAP.get("mix_241_za").get(), "MZA-241 Fuel Pellet");

        fuelPelletTypeItems(DEPLETED_FUEL_AMERICIUM_MAP, "Depleted ");
        fuelPelletTypeItems(DEPLETED_FUEL_BERKELIUM_MAP, "Depleted ");
        fuelPelletTypeItems(DEPLETED_FUEL_CALIFORNIUM_MAP, "Depleted ");
        fuelPelletTypeItems(DEPLETED_FUEL_CURIUM_MAP, "Depleted ");
        fuelPelletTypeItems(DEPLETED_FUEL_NEPTUNIUM_MAP, "Depleted ");
        fuelPelletTypeItems(DEPLETED_FUEL_PLUTONIUM_MAP, "Depleted ");
        fuelPelletTypeItems(DEPLETED_FUEL_THORIUM_MAP, "Depleted ");
        fuelPelletTypeItems(DEPLETED_FUEL_URANIUM_MAP, "Depleted ");

        add(DEPLETED_FUEL_MIXED_MAP.get("mix_239_ni").get(), "Depleted MNI-239 Fuel Pellet");
        add(DEPLETED_FUEL_MIXED_MAP.get("mix_239_ox").get(), "Depleted MOX-239 Fuel Pellet");
        add(DEPLETED_FUEL_MIXED_MAP.get("mix_239_tr").get(), "Depleted MTRISO-239 Fuel Pebble");
        add(DEPLETED_FUEL_MIXED_MAP.get("mix_239_za").get(), "Depleted MZA-239 Fuel Pellet");
        add(DEPLETED_FUEL_MIXED_MAP.get("mix_241_ni").get(), "Depleted MNI-241 Fuel Pellet");
        add(DEPLETED_FUEL_MIXED_MAP.get("mix_241_ox").get(), "Depleted MOX-241 Fuel Pellet");
        add(DEPLETED_FUEL_MIXED_MAP.get("mix_241_tr").get(), "Depleted MTRISO-241 Fuel Pebble");
        add(DEPLETED_FUEL_MIXED_MAP.get("mix_241_za").get(), "Depleted MZA-241 Fuel Pellet");

        simpleItems(FOOD_MAP, Map.of("dominos", "Domino's Special", "smore", "S'more S'mingot", "moresmore", "MoreS'more DoubleS'mingot"));
        add(PORTABLE_ENDER_CHEST.get(), "Portable Ender Chest");
        add(FOURSMORE.get(), "FourS'more QuadS'mingot");
        add(MULTITOOL.get(), "Multitool");
    }

    private void blocks() {
        simpleBlocks(INGOTS, INGOT_BLOCK_MAP, " Block");
        simpleBlocks(MATERIAL_BLOCKS, MATERIAL_BLOCK_MAP, " Block");
        simpleBlocks(RAWS, RAW_BLOCK_MAP, "Block of Raw ", "");

        add(FERTILE_ISOTOPE_MAP.get("americium").get(), "Americium-243 Block");
        add(FERTILE_ISOTOPE_MAP.get("berkelium").get(), "Berkelium-247 Block");
        add(FERTILE_ISOTOPE_MAP.get("californium").get(), "Californium-252 Block");
        add(FERTILE_ISOTOPE_MAP.get("curium").get(), "Curium-246 Block");
        add(FERTILE_ISOTOPE_MAP.get("neptunium").get(), "Neptunium-237 Block");
        add(FERTILE_ISOTOPE_MAP.get("plutonium").get(), "Plutonium-242 Block");
        add(FERTILE_ISOTOPE_MAP.get("uranium").get(), "Uranium-238 Block");

        for (String collector : COLLECTOR_MAP.keySet()) {
            add(COLLECTOR_MAP.get(collector).asItem(), capitalize(collector));
        }

        simpleBlocks(PROCESSOR_MAP);
        simpleBlocks(RTG_MAP, Map.of("rtg_uranium", "Uranium RTG", "rtg_plutonium", "Plutonium RTG", "rtg_americium", "Americium RTG", "rtg_californium", "Californium RTG"));
        add(NUCLEAR_FURNACE.asItem(), capitalize(NUCLEAR_FURNACE.getId().getPath()));

        simpleBlocks(SOLAR_MAP, Map.of("solar_panel_basic", "Basic Solar Panel", "solar_panel_advanced", "Advanced Solar Panel", "solar_panel_du", "DU Solar Panel", "solar_panel_elite", "Elite Solar Panel"));
        simpleBlocks(TURBINE_MAP, Map.of("sic_turbine_rotor_blade", "SiC-SiC CMC Turbine Rotor Blade", "turbine_inlet", "Turbine Fluid Inlet", "turbine_outlet", "Turbine Fluid Outlet"));
        simpleBlocks(FISSION_REACTOR_MAP, Map.of("boron_silver_shield", "Boron-Silver Fission Neutron Shield", "radium_beryllium_source", "Ra-Be Fission Neutron Source", "polonium_beryllium_source", "Po-Be Fission Neutron Source", "californium_source", "Cf-252 Fission Neutron Source", "beryllium_carbon_reflector", "Beryllium-Carbon Neutron Reflector", "lead_steel_reflector", "Lead-Steel Neutron Reflector"));
        simpleBlocks(BATTERY_MAP, Map.of("du_voltaic_pile", "DU Voltaic Pile", "du_lithium_ion_battery", "DU Lithium Ion Battery"));
        simpleBlocks(HX_MAP);
    }

    private void buckets() {
        buckets(GAS_MAP, Map.of("helium_3", "Helium-3"));
        buckets(MOLTEN_MAP, Map.of("boron_10", "Boron-10",
                "boron_11", "Boron-11",
                "lithium_6", "Lithium-6",
                "lithium_7", "Lithium-7",
                "lif", "Lithium Fluoride",
                "bef2", "Beryllium Fluoride",
                "flibe", "FLiBe Salt Mixture",
                "naoh", "Sodium Hydroxide",
                "koh", "Potassium Hydroxide",
                "bas", "Boron Arsenide"), "Molten ");
        buckets(HOT_GAS_MAP, Map.of());
        buckets(SUGAR_MAP, Map.of());
        buckets(CHOCOLATE_MAP, Map.of());
        buckets(FISSION_FLUID_MAP, Map.of("strontium_90", "Strontium-90",
                "ruthenium_106", "Ruthenium-106",
                "caesium_137", "Caesium-137",
                "promethium_147", "Promethium-147",
                "europium_155", "Europium-155"));
        buckets(STEAM_MAP, Map.of());
        buckets(SALT_SOLUTION_MAP, Map.of());
        buckets(ACID_MAP, Map.of());
        buckets(FLAMMABLE_MAP, Map.of());
        nakBuckets(HOT_COOLANT_MAP);
        nakBuckets(COOLANT_MAP);
        buckets(CUSTOM_FLUID_MAP, Map.of("radaway", "RadAway Fluid", "radaway_slow", "Slow-Acting RadAway Fluid"));
        fissionBuckets(FISSION_FUEL_MAP);
    }

    private void buckets(Map<String, Fluids> type, Map<String, String> replacers) {
        buckets(type, replacers, "");
    }

    private void buckets(Map<String, Fluids> type, Map<String, String> replacers, String prefix) {
        for (String fluid : type.keySet()) {

            String name = replacers.getOrDefault(fluid, capitalize(fluid));
            add(type.get(fluid).bucket.asItem(), prefix + name + " Bucket");
            add(type.get(fluid).type.get().getDescriptionId(), prefix + name);
            add(type.get(fluid).block.get(), prefix + name);
        }
    }

    private void fissionBuckets(Map<String, Fluids> type) {
        for (String fluid : type.keySet()) {
            StringBuilder prefix = new StringBuilder("Molten ");
            String name = fluid;
            String suffix = "";

            if (fluid.contains("flibe")) {
                prefix.append("FLiBe Salt Solution of ");
                suffix = " Fuel";
                name = name.replace("flibe", "");
            }

            if (fluid.contains("depleted")) {
                prefix.append("Depleted ");
                name = name.replace("depleted_", "");
            }

            if (Pattern.compile("hec([mf])").matcher(fluid).find()) {
                name = capitalize(name.replace("hec", "HEC").replaceFirst("_", "-"));
            } else if (Pattern.compile("lec([mf])").matcher(fluid).find()) {
                name = capitalize(name.replace("lec", "LEC").replaceFirst("_", "-"));
            } else if (Pattern.compile("^.{3}((_\\d{3})|$)").matcher(name).find()) {
                name = capitalize(name.substring(0, 3).toUpperCase() + (name.length() > 3 ? (Character.isDigit(name.charAt(4)) ? "-" : "_") + name.substring(4) : ""));
            } else {
                name = capitalize(name);
            }

            add(type.get(fluid).bucket.asItem(), prefix + name + suffix + " Bucket");
            add(type.get(fluid).type.get().getDescriptionId(), prefix + name + suffix);
            add(type.get(fluid).block.get(), prefix + name + suffix);
        }
    }

    private void nakBuckets(Map<String, Fluids> type) {
        for (String fluid : type.keySet()) {
            String name = switch (fluid) {
                case "nak" -> "Eutectic NaK Alloy";
                case "nak_hot" -> "Hot Eutectic NaK Alloy";
                case String temp -> {
                    String prefix = "";
                    if (temp.contains("nak_hot")) {
                        prefix = "Hot ";
                    }
                    yield prefix + "Eutectic NaK-" + capitalize(temp.substring(0, temp.indexOf("_nak"))) + " Mixture";
                }
            };
            add(type.get(fluid).bucket.asItem(), name + " Bucket");
            add(type.get(fluid).type.get().getDescriptionId(), name);
            add(type.get(fluid).block.get(), name);
        }
    }

    private void messages() {
        add(MODID + ".message.multitool.save_processor_config", "Saved %s configuration to Multitool");
        add(MODID + ".message.multitool.load_processor_config", "Loaded %s configuration from Multitool");
        add(MODID + ".message.multitool.invalid_processor_config", "%s configuration cannot be loaded to %s");

        add(MODID + ".message.multitool.save_component_info", "Saved %s info to Multitool");
        add(MODID + ".message.multitool.load_component_info", "Loaded %s info from Multitool to %s");
        add(MODID + ".message.multitool.invalid_component_info", "%s info from multiblock cannot be loaded to %s");

        add(MODID + ".message.multitool.start_manager_listener_set", "%s now waiting for set of listeners...");
        add(MODID + ".message.multitool.append_manager_listener_set", "Added listener %s to multitool");
        add(MODID + ".message.multitool.finish_manager_listener_set", "%s now connected to %s listeners");
        add(MODID + ".message.multitool.manager_listener_info", "%s currently connected to %s listeners");

        add(MODID + ".message.multitool.no_connected_component_info", "%s currently has no connected component!");
        add(MODID + ".message.multitool.connected_component_info", "%s currently connected to %s at [%d, %d, %d]");

        add(MODID + ".message.multitool.energy_toggle", "Toggled side to %s");
        add(MODID + ".message.multitool.energy_toggle_opposite", "Toggled opposite side to %s");

        add(MODID + ".message.multitool.fluid_toggle", "Toggled side to %s");
        add(MODID + ".message.multitool.fluid_toggle_opposite", "Toggled opposite side to %s");

        add(MODID + "message.filter", "Set filter to: %s");

        add(MODID + ".multitool.quantum_computer.tool_set_angle", "Saved angle of %s degrees to multitool");
        add(MODID + ".multitool.clear_info", "Cleared stored multitool info");
    }

    private void tooltips() {
        add(MODID + ".tooltip.radiation", "Radiation: %s %sRad/t"); // TODO remove
        add(MODID + ".tooltip.cobblestone_generator_no_req_power", "Produces %s constantly.");
        add(MODID + ".tooltip.cobblestone_generator_req_power", "Produces %s constantly if supplied %s.");
        add(MODID + ".tooltip.water_source", "Produces %s of water constantly.");
        add(MODID + ".tooltip.nitrogen_collector", "Produces %s of nitrogen constantly.");
        add(MODID + ".tooltip.solar", "Produces %s constantly during the daytime.");
        add(MODID + ".tooltip.rtg", "Produces %s constantly.");

        add(MODID + ".tooltip.speed_multiplier", "Speed Multiplier: %s");
        add(MODID + ".tooltip.power_multiplier", "Power Multiplier: %s");

        addTooltip(PROCESSOR_MAP.get("alloy_furnace"), "Combines base metals into alloys.");
        addTooltip(PROCESSOR_MAP.get("assembler"), "Combines components into a complex product.");
        addTooltip(PROCESSOR_MAP.get("centrifuge"), "Separates the isotopes of fluid materials.");
        addTooltip(PROCESSOR_MAP.get("chemical_reactor"), "Houses reactions between fluids.");
        addTooltip(PROCESSOR_MAP.get("crystallizer"), "Precipitates solids from solution.");
        addTooltip(PROCESSOR_MAP.get("decay_hastener"), "Forces radioactive materials to decay.");
        addTooltip(PROCESSOR_MAP.get("electric_furnace"), "Smelts items using energy.");
        addTooltip(PROCESSOR_MAP.get("electrolyzer"), "Splits compounds into their elements.");
        addTooltip(PROCESSOR_MAP.get("fluid_enricher"), "Enriches fluids with materials.");
        addTooltip(PROCESSOR_MAP.get("fluid_extractor"), "Draws fluids from materials.");
        addTooltip(PROCESSOR_MAP.get("fuel_reprocessor"), "Extracts materials from depleted fuel.");
        addTooltip(PROCESSOR_MAP.get("fluid_infuser"), "Enhances materials with fluids.");
        addTooltip(PROCESSOR_MAP.get("ingot_former"), "Forms ingots and gems from molten materials.");
        addTooltip(PROCESSOR_MAP.get("manufactory"), "A handy machine that has many uses.");
        addTooltip(PROCESSOR_MAP.get("melter"), "Melts down materials.");
        addTooltip(PROCESSOR_MAP.get("pressurizer"), "Processes items under immense pressure.");
        addTooltip(PROCESSOR_MAP.get("rock_crusher"), "Smashes up rock to produce mineral dusts.");
        addTooltip(PROCESSOR_MAP.get("fluid_mixer"), "Blends fluids together.");
        addTooltip(PROCESSOR_MAP.get("separator"), "Breaks materials into their constituents.");
        addTooltip(PROCESSOR_MAP.get("supercooler"), "Lowers the temperature of fluids.");

        addTooltip(NUCLEAR_FURNACE, "Smelts items very quickly using uranium ingots and dust as fuel.");
        addTooltip(UNIVERSAL_BIN, "Destroys items, fluids and energy.");
        addTooltip(MACHINE_INTERFACE, "Automation can access the machine directly adjacent to this block. Can only extend one adjacent machine at maximum.");

        add("item." + MODID + ".upgrade.speed.desc", "Increases the processing speed of machines at the cost of additional processing power. These upgrades can be stacked - the speed will %s each additional upgrade while the power use will %s.");
        add("item." + MODID + ".upgrade.energy.desc", "Decreases the processing power of machines. These upgrades can be stacked, but the maximum number that will have an effect is equal to the number of installed speed upgrades. The power use will %s each additional upgrade.");

        add(MODID + ".tooltip.tank", "%s [%s mB / %s mB]");
        add(MODID + ".tooltip.tank.clear", "Shift Click to clear tank");
        add(MODID + ".tooltip.shift_clear_multiblock", "Shift click to clear out ALL components' items and fluids");

        add(MODID + ".tooltip.machine_side_config", "Side Configuration");
        add(MODID + ".tooltip.redstone_control", "Redstone Control");
        add(MODID + ".tooltip.input_item_config", "Input Slot Configuration");
        add(MODID + ".tooltip.input_tank_config", "Input Tank Configuration");
        add(MODID + ".tooltip.output_item_config", "Output Slot Configuration");
        add(MODID + ".tooltip.output_tank_config", "Output Tank Configuration");
        add(MODID + ".tooltip.energy_upgrade_config", "Energy Upgrade Slot Configuration");
        add(MODID + ".tooltip.speed_upgrade_config", "Speed Upgrade Slot Configuration");
        add(MODID + ".tooltip.slot_setting_config", "Slot Setting: %s");
        add(MODID + ".tooltip.tank_setting_config", "Tank Setting: %s");
        add(MODID + ".tooltip.default_setting_config", "DEFAULT");
        add(MODID + ".tooltip.void_excess_setting_config", "VOID EXCESS");
        add(MODID + ".tooltip.void_setting_config", "VOID ALL");

        add(MODID + ".tooltip.bottom_config", "BOTTOM: %s");
        add(MODID + ".tooltip.top_config", "TOP: %s");
        add(MODID + ".tooltip.left_config", "LEFT: %s");
        add(MODID + ".tooltip.right_config", "RIGHT: %s");
        add(MODID + ".tooltip.front_config", "FRONT: %s");
        add(MODID + ".tooltip.back_config", "BACK: %s");
        add(MODID + ".tooltip.in_config", "INPUT");
        add(MODID + ".tooltip.out_config", "OUTPUT");
        add(MODID + ".tooltip.auto_in_config", "AUTO-INPUT");
        add(MODID + ".tooltip.auto_out_config", "AUTO-OUTPUT");
        add(MODID + ".tooltip.both_config", "BOTH");
        add(MODID + ".tooltip.non_config", "DISABLED");

        add(MODID + ".tooltip.exchanger_tube_fluid_side.closed", "CLOSED");
        add(MODID + ".tooltip.exchanger_tube_fluid_side.open", "OPEN");
        add(MODID + ".tooltip.exchanger_tube_fluid_side.closed_baffle", "CLOSED BAFFLE");
        add(MODID + ".tooltip.exchanger_tube_fluid_side.open_baffle", "OPEN BAFFLE");

        add(MODID + ".tooltip.shift_for_info", "Hold Shift for more info");
        add(MODID + ".tooltip.ctrl_for_info", "Hold Ctrl for more info");

        add(MODID + ".tooltip.portable.ender_chest", "Access your Ender Chest on the move.");
        add(MODID + ".tooltip.marshmallow", "Many civilizations would not have fallen if they had these on their side.");
        add(MODID + ".tooltip.dominos", "Paul's favourite - restore 16 hunger points with this beauty.");

        add(MODID + ".tooltip.solidified_corium", "The solidified form of a poisonous mixture of fuel and components produced in fission reactor meltdowns.");

        add(MODID + ".tooltip.energy_stored", "Energy Stored: %s");
        add(MODID + ".tooltip.process_time", "Base Process Time: %s");
        add(MODID + ".tooltip.process_power", "Base Process Power: %s");
        add(MODID + ".tooltip.no_energy", "Does not require energy!");

        add(MODID + ".tooltip.production_rate", "Production Rate: %s");

        add(MODID + ".tooltip.energy_storage.desc", "Right click a side to read the energy level and right click with a multitool to toggle the side's energy connection. Sneaking while placing will maintain the energy connection configuration.");

        add(MODID + ".tooltip.vent_toggle", "Toggled vent to %s mode!");
        add(MODID + ".tooltip.port_toggle", "Toggled port to %s mode!");

        add(MODID + ".tooltip.moderator", "Moderators are the medium through which one fission reactor cell/vessel sends neutron flux to another or itself. A moderator line consists of EITHER at most %s collimated moderators with a cell/vessel at one endpoint and a cell/vessel at the other OR at most %s collimated moderators with a cell/vessel at one endpoint and a neutron reflector at the other. Moderators are only considered 'active' if part of a line AND adjacent to at least one functional endpoint cell/vessel. A primed or functional cell/vessel sends flux down the line, either to be accepted by the other cell/vessel or to be sent back by the reflector. The total flux sent is the sum of flux factors of the moderators in the line. This is doubled in the case of reflection, and is multiplied by the reflector's reflectivity factor. Finally, the total efficiency of the cells at the line endpoints is multiplied by the mean efficiency of the lines it receives flux from - the efficiency of a line is the mean efficiency multiplier of the moderators in the line.");
        add(MODID + ".tooltip.moderator.underline", "Fission Reactor Moderator");
        add(MODID + ".tooltip.moderator.flux_factor", "Flux Factor: %s");
        add(MODID + ".tooltip.moderator.efficiency", "Efficiency Multiplier: %s");
    }

    private void addTooltip(DeferredBlock<Block> block, String string) {
        add(block.get().getDescriptionId() + ".desc", string);
    }

    private void damage_types() {
        add(MODID + ".death.attack.superfluid_freeze", "%swas Bose-Einstein condensated");
        add(MODID + ".death.attack.plasma_burn", "%s was incinerated by plasma");
        add(MODID + ".death.attack.gas_burn", "%s was excruciatingly vaporized");
        add(MODID + ".death.attack.steam_burn", "%s would have fared better in a kettle");
        add(MODID + ".death.attack.molten_burn", "%s went to hell but didn't come back");
        add(MODID + ".death.attack.corium_burn", "%s decided to follow the fate of their fission reactor");
        add(MODID + ".death.attack.hot_coolant_burn", "%s cooled off at the wrong time and place");
        add(MODID + ".death.attack.acid_burn", "%s turned out to be far too alkaline");
        add(MODID + ".death.attack.fluid_burn", "%s was effectively pan-fried");
        add(MODID + ".death.attack.hypothermia", "%s died as a result of extreme hypothermia");
        add(MODID + ".death.attack.fission_burn", "%s was fried by a fission reactor");
        add(MODID + ".death.attack.fatal_rads", "%s died due to fatal radiation poisoning");
    }

    private void menus() {
        add(MODID + ".menu.title.alloy_furnace", "Alloy Furnace");
        add(MODID + ".menu.title.assembler", "Assembler");
        add(MODID + ".menu.title.centrifuge", "Centrifuge");
        add(MODID + ".menu.title.chemical_reactor", "Chemical Reactor");
        add(MODID + ".menu.title.crystallizer", "Crystallizer");
        add(MODID + ".menu.title.decay_hastener", "Decay Hastener");
        add(MODID + ".menu.title.electric_furnace", "Electric Furnace");
        add(MODID + ".menu.title.electrolyzer", "Electrolyzer");
        add(MODID + ".menu.title.fluid_enricher", "Fluid Enricher");
        add(MODID + ".menu.title.fluid_extractor", "Fluid Extractor");
        add(MODID + ".menu.title.fuel_reprocessor", "Fuel Reprocessor");
        add(MODID + ".menu.title.fluid_infuser", "Fluid Infuser");
        add(MODID + ".menu.title.ingot_former", "Ingot Former");
        add(MODID + ".menu.title.manufactory", "Manufactory");
        add(MODID + ".menu.title.melter", "Melter");
        add(MODID + ".menu.title.nuclear_furnace", "Nuclear Furnace");
        add(MODID + ".menu.title.pressurizer", "Pressurizer");
        add(MODID + ".menu.title.rock_crusher", "Rock Crusher");
        add(MODID + ".menu.title.fluid_mixer", "Fluid Mixer");
        add(MODID + ".menu.title.separator", "Separator");
        add(MODID + ".menu.title.supercooler", "Supercooler");

        // Turbine
        add(MODID + ".menu.turbine_controller.title", "%s*%s*%s Turbine");
        add(MODID + ".menu.turbine_controller.power", "Power Output: %s");
        add(MODID + ".menu.turbine_controller.dynamo_efficiency", "Dynamo Efficiency: %s");
        add(MODID + ".menu.turbine_controller.dynamo_coil_count", "Coil Counts: %s");
        add(MODID + ".menu.turbine_controller.expansion_level", "Expansion: %s");
        add(MODID + ".menu.turbine_controller.rotor_efficiency", "Rotor Efficiency: %s");
        add(MODID + ".menu.turbine_controller.fluid_rate", "Input: %s");
        add(MODID + ".menu.turbine_controller.power_bonus", "Rate Power Bonus: %s");

        add(MODID + ".multiblock_validation.invalid_block", "%4$s is not a valid block at [%1$d, %2$d, %3$d]");
        add(MODID + ".multiblock_validation.no_controller", "There must be a controller for the multiblock to form");
        add(MODID + ".multiblock_validation.too_many_controllers", "There must only be one controller for the multiblock to form");
        add(MODID + ".menu.fission.no_cluster", "No cluster!");

        add(MODID + ".menu.fission_heater.title", "Fission Coolant Heater");
        add(MODID + ".menu.fission_heater_port.title", "Fission Coolant Heater Port");
    }

    private void creativeTabs() {
        add(MODID + ".creative_tab.title.materials", "NuclearCraft Materials");
        add(MODID + ".creative_tab.title.machines", "NuclearCraft Machines");
        add(MODID + ".creative_tab.title.multiblocks", "NuclearCraft Multiblocks");
        add(MODID + ".creative_tab.title.radiation", "NuclearCraft Radiation");
        add(MODID + ".creative_tab.title.miscellaneous", "NuclearCraft Miscellaneous");
    }

    private void sounds() {
        for (DeferredItem<Item> disc : MUSIC_DISC_MAP.values()) {
            add(disc.get(), "Music Disc");
        }

        add(MODID + ".music_disc.money_for_nothing", "Dire Straits - Money For Nothing");
        add(MODID + ".music_disc.money_for_nothing.credit", "8-Bit Cover by 'Omnigrad'");
        add(MODID + ".music_disc.wanderer", "Dion - The Wanderer");
        add(MODID + ".music_disc.wanderer.credit", "8-Bit Cover by '8 Bit Universe'");
        add(MODID + ".music_disc.end_of_the_world", "Skeeter Davis - The End of the World");
        add(MODID + ".music_disc.end_of_the_world.credit", "8-Bit Cover by 'GermanPikachuGaming'");
        add(MODID + ".music_disc.hyperspace", "Ur-Quan Masters - Hyperspace");
        add(MODID + ".music_disc.hyperspace.credit", "8-Bit Cover by 'Riku Nuottajärvi'");

        add(MODID + ".sound.block.electrolyzer_run", "Electrolyzer Running");
        add(MODID + ".sound.block.distiller_run", "Distiller Running");
        add(MODID + ".sound.block.infiltrator_run", "Infiltrator Running");
        add(MODID + ".sound.block.turbine_run", "Turbine Running");
        add(MODID + ".sound.block.fusion_run", "Fusion Reactor Running");

        add(MODID + ".sound.player.geiger_tick", "Geiger Tick");
        add(MODID + ".sound.player.radaway", "Use RadAway");
        add(MODID + ".sound.player.rad_x", "Use Rad-X");
        add(MODID + ".sound.player.chems_wear_off", "Chems Wear Off");
        add(MODID + ".sound.player.rad_poisoning", "Rad Poisoning");

    }

    private void config() {
        add(MODID + ".configuration.processor", "Processor Configs");
        add(MODID + ".configuration.processor.tooltip", "Configure processors.");

        add(MODID + ".configuration.processor_time_multiplier", "Processing Time Multiplier");
        add(MODID + ".configuration.processor_time_multiplier.tooltip", "Modifies the base processing time of all processors.");
        add(MODID + ".configuration.processor_power_multiplier", "Processing Power Multiplier");
        add(MODID + ".configuration.processor_power_multiplier.tooltip", "Modifies the base processing power use of all processors.");
        add(MODID + ".configuration.processor_time", "Processing Times");
        add(MODID + ".configuration.processor_time.tooltip", "Base ticks per process. Order: Manufactory, Separator, Decay Hastener, Fuel Reprocessor, Alloy Furnace, Fluid Infuser, Melter, Supercooler, Electrolyzer, Assembler, Ingot Former, Pressurizer, Chemical Reactor, Fluid Mixer, Crystallizer, Fluid Enricher, Fluid Extractor, Centrifuge, Rock Crusher, Electric Furnace.");
        add(MODID + ".configuration.processor_power", "Processing Power Use");
        add(MODID + ".configuration.processor_power.tooltip", "Base RF/t use during processing. Order: Manufactory, Separator, Decay Hastener, Fuel Reprocessor, Alloy Furnace, Fluid Infuser, Melter, Supercooler, Electrolyzer, Assembler, Ingot Former, Pressurizer, Chemical Reactor, Fluid Mixer, Crystallizer, Fluid Enricher, Fluid Extractor, Centrifuge, Rock Crusher, Electric Furnace.");
        add(MODID + ".configuration.speed_upgrade_power_laws_fp", "Speed Upgrade Power Laws");
        add(MODID + ".configuration.speed_upgrade_power_laws_fp.tooltip", "Power laws for speed upgrades. Order: Processor Time, Processor Power, Generator Time, Generator Power.");
        add(MODID + ".configuration.speed_upgrade_multipliers_fp", "Speed Upgrade Multipliers");
        add(MODID + ".configuration.speed_upgrade_multipliers_fp.tooltip", "Base multipliers for speed upgrades. Order: Processor Time, Processor Power, Generator Time, Generator Power.");
        add(MODID + ".configuration.energy_upgrade_power_laws_fp", "Energy Upgrade Power Laws");
        add(MODID + ".configuration.energy_upgrade_power_laws_fp.tooltip", "Power laws for energy upgrades. Order: Processor Power, Generator Time.");
        add(MODID + ".configuration.energy_upgrade_multipliers_fp", "Energy Upgrade Multipliers");
        add(MODID + ".configuration.energy_upgrade_multipliers_fp.tooltip", "Base multipliers for energy upgrades. Order: Processor Power, Generator Time.");
        add(MODID + ".configuration.upgrade_stack_sizes", "Upgrade Max Stack Sizes");
        add(MODID + ".configuration.upgrade_stack_sizes.tooltip", "Max stack sizes for upgrades. Order: Speed, Energy.");


        add(MODID + ".configuration.enable_mek_gas", "Enable Mekanism Gas Support");
        add(MODID + ".configuration.enable_mek_gas.tooltip", "If enabled, gas from Mekanism can be handled by NC machines.");
        add(MODID + ".configuration.machine_update_rate", "Machine Update Rate");
        add(MODID + ".configuration.machine_update_rate.tooltip", "Ticks per machine update - used for various processes such as GUI updates and multiblock structure checks.");
        add(MODID + ".configuration.processor_passive_rate", "Passive Production Rate");
        add(MODID + ".configuration.processor_passive_rate.tooltip", "Rates at which passive machines produce materials. Order: Cobblestone Generator, Infinite Water Source, Nitrogen Collector.");
        add(MODID + ".configuration.passive_push", "Passive Pushing");
        add(MODID + ".configuration.passive_push.tooltip", "Will passive machines that produce materials automatically push to adjacent inventories and fluid handlers?");
        add(MODID + ".configuration.cobble_gen_power", "Cobble Gen Power Use");
        add(MODID + ".configuration.cobble_gen_power.tooltip", "RF/t required for Cobblestone Generator to run.");

        add(MODID + ".configuration.smart_processor_input", "Smart Processor Input");
        add(MODID + ".configuration.smart_processor_input.tooltip", "Will a machine's valid inputs depend on the stacks already present as well as its possible recipes?");

        add(MODID + ".configuration.processor_particles", "Processor Particles");
        add(MODID + ".configuration.processor_particles.tooltip", "Will machines produce particle effects while running?");

        add(MODID + ".configuration.generator", "Generator Configs");
        add(MODID + ".configuration.generator.tooltip", "Configure generators.");

        add(MODID + ".configuration.rtg_power", "RTG Power Gen");
        add(MODID + ".configuration.rtg_power.tooltip", "RF/t generated. Order: Uranium, Plutonium, Americium, Californium.");
        add(MODID + ".configuration.solar_power", "Solar Panel Power Gen");
        add(MODID + ".configuration.solar_power.tooltip", "RF/t generated. Order: Basic, Advanced, DU, Elite.");


        add(MODID + ".configuration.energy_storage", "Energy Storage Configs");
        add(MODID + ".configuration.energy_storage.tooltip", "Configure energy storages.");

        add(MODID + ".configuration.battery_block_capacity", "Battery Block Capacities");
        add(MODID + ".configuration.battery_block_capacity.tooltip", "Maximum RF storable. Order: Voltaic Pile [Basic, Advanced, DU, Elite], Lithium Ion Battery [Basic, Advanced, DU, Elite].");
        add(MODID + ".configuration.battery_block_max_transfer", "Battery Block Transfer Rates");
        add(MODID + ".configuration.battery_block_max_transfer.tooltip", "Maximum RF transferable per tick. Order: Voltaic Pile [Basic, Advanced, DU, Elite], Lithium Ion Battery [Basic, Advanced, DU, Elite].");
        add(MODID + ".configuration.battery_item_capacity", "Battery Item Capacities");
        add(MODID + ".configuration.battery_item_capacity.tooltip", "Maximum RF storable. Order: Lithium Ion Cell.");
        add(MODID + ".configuration.battery_item_max_transfer", "Battery Item Transfer Rates");
        add(MODID + ".configuration.battery_item_max_transfer.tooltip", "Maximum RF transferable per tick. Order: Lithium Ion Cell.");

        add(MODID + ".configuration.machine", "Large Machine Configs"); // TODO check if need after adding machines
        add(MODID + ".configuration.machine.tooltip", "Configure large machines.");

        add(MODID + ".configuration.machine_min_size", "Minimum Structure Length");
        add(MODID + ".configuration.machine_min_size.tooltip", "Minimum side length of large machines.");
        add(MODID + ".configuration.machine_max_size", "Maximum Structure Length");
        add(MODID + ".configuration.machine_max_size.tooltip", "Maximum side length of large machines.");
        add(MODID + ".configuration.machine_diaphragm_efficiency", "Diaphragm Efficiency Multipliers");
        add(MODID + ".configuration.machine_diaphragm_efficiency.tooltip", "Efficiency multipliers for diaphragms of this type. Order: sintered steel, polyethersulfone, zirfon.");
        add(MODID + ".configuration.machine_diaphragm_contact_factor", "Diaphragm Contact Factors");
        add(MODID + ".configuration.machine_diaphragm_contact_factor.tooltip", "Contact factors for diaphragms of this type. Order: sintered steel, polyethersulfone, zirfon.");
        add(MODID + ".configuration.machine_sieve_assembly_efficiency", "Sieve Assembly Efficiency Multipliers");
        add(MODID + ".configuration.machine_sieve_assembly_efficiency.tooltip", "Efficiency multipliers for sieve assemblies of this type. Order: steel, polytetrafluoroethene, hastelloy.");

        add(MODID + ".configuration.machine_electrolyzer_time", "Electrolyzer Process Time");
        add(MODID + ".configuration.machine_electrolyzer_time.tooltip", "Base ticks per electrolyzer process.");
        add(MODID + ".configuration.machine_electrolyzer_power", "Electrolyzer Process Power Use");
        add(MODID + ".configuration.machine_electrolyzer_power.tooltip", "Base RF/t use during electrolysis.");
        add(MODID + ".configuration.machine_cathode_efficiency", "Cathode Efficiency Multipliers");
        add(MODID + ".configuration.machine_cathode_efficiency.tooltip", "List of cathode materials and their efficiencies. Format: 'materialSuffix@efficiency'.");
        add(MODID + ".configuration.machine_anode_efficiency", "Anode Efficiency Multipliers");
        add(MODID + ".configuration.machine_anode_efficiency.tooltip", "List of anode materials and their efficiencies. Format: 'materialSuffix@efficiency'.");
        add(MODID + ".configuration.machine_electrolyzer_sound_volume", "Electrolyzer Sound Volume");
        add(MODID + ".configuration.machine_electrolyzer_sound_volume.tooltip", "Modifier for the volume of electrolyzer sound effects.");

        add(MODID + ".configuration.machine_distiller_time", "Distiller Process Time");
        add(MODID + ".configuration.machine_distiller_time.tooltip", "Base ticks per distiller process.");
        add(MODID + ".configuration.machine_distiller_power", "Distiller Process Power Use");
        add(MODID + ".configuration.machine_distiller_power.tooltip", "Base RF/t use during distilling.");
        add(MODID + ".configuration.machine_distiller_sound_volume", "Distiller Sound Volume");
        add(MODID + ".configuration.machine_distiller_sound_volume.tooltip", "Modifier for the volume of distiller sound effects.");

        add(MODID + ".configuration.machine_infiltrator_time", "Infiltrator Process Time");
        add(MODID + ".configuration.machine_infiltrator_time.tooltip", "Base ticks per infiltrator process.");
        add(MODID + ".configuration.machine_infiltrator_power", "Infiltrator Process Power Use");
        add(MODID + ".configuration.machine_infiltrator_power.tooltip", "Base RF/t use during infiltrating.");
        add(MODID + ".configuration.machine_infiltrator_pressure_fluid_efficiency", "Infiltrator Fluid Efficiency Multipliers");
        add(MODID + ".configuration.machine_infiltrator_pressure_fluid_efficiency.tooltip", "List of infiltrator pressure fluids and their efficiencies. Format: 'fluidName@efficiency'.");
        add(MODID + ".configuration.machine_infiltrator_sound_volume", "Infiltrator Sound Volume");
        add(MODID + ".configuration.machine_infiltrator_sound_volume.tooltip", "Modifier for the volume of infiltrator sound effects.");

        add(MODID + ".configuration.fission", "Fission Configs");
        add(MODID + ".configuration.fission.tooltip", "Configure aspects of nuclear fission.");

        add(MODID + ".configuration.fission_min_size", "Minimum Structure Length");
        add(MODID + ".configuration.fission_min_size.tooltip", "Minimum side length of fission structures.");
        add(MODID + ".configuration.fission_max_size", "Maximum Structure Length");
        add(MODID + ".configuration.fission_max_size.tooltip", "Maximum side length of fission structures.");
        add(MODID + ".configuration.fission_fuel_time_multiplier", "Fuel Time Multiplier");
        add(MODID + ".configuration.fission_fuel_time_multiplier.tooltip", "Modifies the depletion time of fuel in fission reactors.");
        add(MODID + ".configuration.fission_fuel_heat_multiplier", "Heat Gen Multiplier");
        add(MODID + ".configuration.fission_fuel_heat_multiplier.tooltip", "Modifies the heat gen of fuels in fission reactors.");
        add(MODID + ".configuration.fission_fuel_efficiency_multiplier", "Efficiency Multiplier");
        add(MODID + ".configuration.fission_fuel_efficiency_multiplier.tooltip", "Modifies the efficiency of fuels in fission reactors.");
        add(MODID + ".configuration.fission_fuel_radiation_multiplier", "Radiation Multiplier");
        add(MODID + ".configuration.fission_fuel_radiation_multiplier.tooltip", "Modifies the radiation levels of fuels in fission reactors.");
        add(MODID + ".configuration.fission_source_efficiency", "Neutron Source Efficiencies");
        add(MODID + ".configuration.fission_source_efficiency.tooltip", "Efficiency multiplier for neutron sources of this type. Order: Ra-Be, Po-Be, Cf-252.");
        add(MODID + ".configuration.fission_sink_cooling_rate", "Heat Sink Cooling Rates");
        add(MODID + ".configuration.fission_sink_cooling_rate.tooltip", "Heat removed per tick by valid heat sinks of this type. Order: water, iron, redstone, quartz, obsidian, nether brick, glowstone, lapis, gold, prismarine, slime, end stone, purpur, diamond, emerald, copper, tin, lead, boron, lithium, magnesium, manganese, aluminum, silver, fluorite, villiaumite, carobbiite, arsenic, liquid nitrogen, liquid helium, enderium, cryotheum.");
        add(MODID + ".configuration.fission_sink_rule", "Heat Sink Placement Rules");
        add(MODID + ".configuration.fission_sink_rule.tooltip", "Placement rule to be parsed for heat sinks of this type. Order: water, iron, redstone, quartz, obsidian, nether brick, glowstone, lapis, gold, prismarine, slime, end stone, purpur, diamond, emerald, copper, tin, lead, boron, lithium, magnesium, manganese, aluminum, silver, fluorite, villiaumite, carobbiite, arsenic, liquid nitrogen, liquid helium, enderium, cryotheum.");
        add(MODID + ".configuration.fission_heater_rule", "Coolant Heater Placement Rules");
        add(MODID + ".configuration.fission_heater_rule.tooltip", "Placement rule to be parsed for coolant heaters of this type. Order: standard, iron, redstone, quartz, obsidian, nether brick, glowstone, lapis, gold, prismarine, slime, end stone, purpur, diamond, emerald, copper, tin, lead, boron, lithium, magnesium, manganese, aluminum, silver, fluorite, villiaumite, carobbiite, arsenic, liquid nitrogen, liquid helium, enderium, cryotheum.");
        add(MODID + ".configuration.fission_shield_heat_per_flux", "Neutron Shield Heat Per Flux");
        add(MODID + ".configuration.fission_shield_heat_per_flux.tooltip", "Heat generated per unit flux for neutron shields of this type. Order: boron-silver.");
        add(MODID + ".configuration.fission_shield_efficiency", "Neutron Shield Efficiency Multipliers");
        add(MODID + ".configuration.fission_shield_efficiency.tooltip", "Efficiency multipliers for neutron shields of this type. Order: boron-silver.");
        add(MODID + ".configuration.fission_cooling_efficiency_leniency", "Cooling Leniency Factor");
        add(MODID + ".configuration.fission_cooling_efficiency_leniency.tooltip", "Determines the leniency for the difference between the actual and ideal cooling rate at which a cluster will begin to be given an overcooling efficiency penalty.");
        add(MODID + ".configuration.fission_sparsity_penalty_params", "Sparsity Efficiency Parameters");
        add(MODID + ".configuration.fission_sparsity_penalty_params.tooltip", "Parameters used for the sparsity efficiency factor. The first determines the minimum value of the multiplier, while the second determines the fraction of functional components at which there is no penalty.");
        add(MODID + ".configuration.fission_heating_coolant_heat_mult", "SFR NaK Heating Multiplier");
        add(MODID + ".configuration.fission_heating_coolant_heat_mult.tooltip", "Modifies the amount of heat absorbed per mB of NaK coolant when used to cool a solid fuel reactor.");

        add(MODID + ".configuration.fission_decay_mechanics", "Enable Decay Mechanics");
        add(MODID + ".configuration.fission_decay_mechanics.tooltip", "Enable fission reactor decay mechanics?");
        add(MODID + ".configuration.fission_decay_build_up_times", "Decay Build Time Params");
        add(MODID + ".configuration.fission_decay_build_up_times.tooltip", "Parameters controlling the time taken for decay heat, iodine and neutron poison to reach their equilibrium levels.");
        add(MODID + ".configuration.fission_decay_lifetimes", "Decay Lifetime Params");
        add(MODID + ".configuration.fission_decay_lifetimes.tooltip", "Parameters controlling the lifetimes of isotopes responsible for decay heat, iodine and neutron poison.");
        add(MODID + ".configuration.fission_decay_equilibrium_factors", "Decay Equilibrium Factors");
        add(MODID + ".configuration.fission_decay_equilibrium_factors.tooltip", "Factors controlling the equilibrium levels of decay heat, iodine and neutron poison.");
        add(MODID + ".configuration.fission_decay_daughter_multipliers", "Decay Daughter Multipliers");
        add(MODID + ".configuration.fission_decay_daughter_multipliers.tooltip", "Multipliers for the amount by which iodine and neutron poison levels increase due to decreases in decay heat and iodine levels, respectively.");
        add(MODID + ".configuration.fission_decay_term_multipliers", "Decay Term Multipliers");
        add(MODID + ".configuration.fission_decay_term_multipliers.tooltip", "Multipliers for the magnitudes of the terms in the equations determining the rate of change of decay product levels.");
        add(MODID + ".configuration.fission_overheat", "Enable Meltdowns");
        add(MODID + ".configuration.fission_overheat.tooltip", "Can fission reactors overheat?");
//add(MODID + ".configuration.fission_meltdown_radiation_multiplier", "Meltdown Radiation Multiplier");
//add(MODID + ".configuration.fission_meltdown_radiation_multiplier.tooltip", "Modifies the radiation leaked by meltdowns.");
//add(MODID + ".configuration.fission_heat_damage", "Heat Contact Damage");
//add(MODID + ".configuration.fission_heat_damage.tooltip", "Can hot fission components hurt the player? Note that this is disabled by default as the hit boxes of fission reactor components are modified.");
        add(MODID + ".configuration.fission_neutron_reach", "Neutron Radiation Reach");
        add(MODID + ".configuration.fission_neutron_reach.tooltip", "Maximum number of moderator blocks that can be between two cells for neutron radiation to be shared between them.");
        add(MODID + ".configuration.fission_heat_dissipation", "Reactor Heat Dissipation");
        add(MODID + ".configuration.fission_heat_dissipation.tooltip", "Sets whether fission reactors dissipate heat while idle and active, respectively.");
        add(MODID + ".configuration.fission_heat_dissipation_rate", "Reactor Heat Dissipation Rate");
        add(MODID + ".configuration.fission_heat_dissipation_rate.tooltip", "Controls the rate at which fission reactors dissipate heat.");
        add(MODID + ".configuration.fission_sound_volume", "Fission Sound Volume");
        add(MODID + ".configuration.fission_sound_volume.tooltip", "Modifier for the volume of fission sound effects.");

        add(MODID + ".configuration.fusion", "Fusion Configs");
        add(MODID + ".configuration.fusion.tooltip", "Configure aspects of nuclear fusion.");

//add(MODID + ".configuration.fusion_fuel_time_multiplier", "Fuel Use Multiplier");
//add(MODID + ".configuration.fusion_fuel_time_multiplier.tooltip", "Modifies the rate of fuel used by fusion reactors.");
//add(MODID + ".configuration.fusion_fuel_heat_multiplier", "Heat Gen Multiplier");
//add(MODID + ".configuration.fusion_fuel_heat_multiplier.tooltip", "Modifies the heat generated by fusion reactors.");
//add(MODID + ".configuration.fusion_overheat", "Enable Meltdowns");
//add(MODID + ".configuration.fusion_overheat.tooltip", "Can fusion reactors overheat?");
//add(MODID + ".configuration.fusion_meltdown_radiation_multiplier", "Meltdown Radiation Multiplier");
//add(MODID + ".configuration.fusion_meltdown_radiation_multiplier.tooltip", "Modifies the radiation leaked by meltdowns.");
//add(MODID + ".configuration.fusion_active_cooling", "Enable Fusion Active Cooling");
//add(MODID + ".configuration.fusion_active_cooling.tooltip", "Can fusion reactors be actively cooled (disable if suffering from lag)?");
//add(MODID + ".configuration.fusion_active_cooling_rate", "Active Cooling Rates");
//add(MODID + ".configuration.fusion_active_cooling_rate.tooltip", "Base heat removed per tick/mB. Order: water, redstone, quartz, gold, glowstone, lapis, diamond, liquid helium, ender, cryotheum, iron, emerald, copper, tin, magnesium.");
//add(MODID + ".configuration.fusion_min_size", "Minimum Toroid Size");
//add(MODID + ".configuration.fusion_min_size.tooltip", "Minimum size of fusion toroids.");
//add(MODID + ".configuration.fusion_max_size", "Maximum Toroid Size");
//add(MODID + ".configuration.fusion_max_size.tooltip", "Maximum size of fusion toroids.");
//add(MODID + ".configuration.fusion_comparator_max_efficiency", "Max Comparator Signal Efficiency");
//add(MODID + ".configuration.fusion_comparator_max_efficiency.tooltip", "Efficiency of a fusion reactor at which a comparator adjacent to the core will output a full-strength redstone signal.");
//add(MODID + ".configuration.fusion_plasma_craziness", "Fusion Plasma Fires");
//add(MODID + ".configuration.fusion_plasma_craziness.tooltip", "Will fusion plasma start fires? Disabling this will reduce lag somewhat.");
//add(MODID + ".configuration.fusion_sound_volume", "Fusion Sound Volume");
//add(MODID + ".configuration.fusion_sound_volume.tooltip", "Modifier for the volume of fusion sound effects.");

//add(MODID + ".configuration.fusion_fuel_time", "Fusion Fuel Combo Lifetimes");
//add(MODID + ".configuration.fusion_fuel_time.tooltip", "Base ticks the fuel combos last. Order: H-H, H-D, H-T, H-He3, H-Li6, H-Li7, H-B11, D-D, D-T, D-He3, ..., D-B11, T-T, ..., T-B11, ..., B11-B11.");
//add(MODID + ".configuration.fusion_heat_generation", "Fusion Fuel Combo Heat Gen");
//add(MODID + ".configuration.fusion_heat_generation.tooltip", "Base H/t the fuel combos produce. Order: H-H, H-D, H-T, H-He3, H-Li6, H-Li7, H-B11, D-D, D-T, D-He3, ..., D-B11, T-T, ..., T-B11, ..., B11-B11.");
//add(MODID + ".configuration.fusion_optimal_temperature", "Fusion Fuel Combo Optimal Temperatures");
//add(MODID + ".configuration.fusion_optimal_temperature.tooltip", "Optimal temperatures of the fuel combos. Order: H-H, H-D, H-T, H-He3, H-Li6, H-Li7, H-B11, D-D, D-T, D-He3, ..., D-B11, T-T, ..., T-B11, ..., B11-B11.");
//add(MODID + ".configuration.fusion_radiation", "Fusion Fuel Combo Radiation");
//add(MODID + ".configuration.fusion_radiation.tooltip", "Base radiation the fuel combos produce while processing. Order: H-H, H-D, H-T, H-He3, H-Li6, H-Li7, H-B11, D-D, D-T, D-He3, ..., D-B11, T-T, ..., T-B11, ..., B11-B11.");
//add(MODID + ".configuration.fusion_electromagnet_power", "Electromagnet Power");
//add(MODID + ".configuration.fusion_electromagnet_power.tooltip", "RF/t required to keep an electromagnet active.");

        add(MODID + ".configuration.heat_exchanger", "Heat Exchanger Configs");
        add(MODID + ".configuration.heat_exchanger.tooltip", "Configure aspects of heat exchangers.");

        add(MODID + ".configuration.heat_exchanger_min_size", "Minimum Structure Length");
        add(MODID + ".configuration.heat_exchanger_min_size.tooltip", "Minimum side length of heat exchangers.");
        add(MODID + ".configuration.heat_exchanger_max_size", "Maximum Structure Length");
        add(MODID + ".configuration.heat_exchanger_max_size.tooltip", "Maximum side length of heat exchangers.");
        add(MODID + ".configuration.heat_exchanger_lmtd", "Use LMTD");
        add(MODID + ".configuration.heat_exchanger_lmtd.tooltip", "Will LMTD be used instead of an arithmetic mean temperature difference?");

        add(MODID + ".configuration.turbine", "Turbine Configs");
        add(MODID + ".configuration.turbine.tooltip", "Configure aspects of turbines.");

        add(MODID + ".configuration.turbine_min_size", "Minimum Structure Length");
        add(MODID + ".configuration.turbine_min_size.tooltip", "Minimum side length of turbines.");
        add(MODID + ".configuration.turbine_max_size", "Maximum Structure Length");
        add(MODID + ".configuration.turbine_max_size.tooltip", "Maximum side length of turbines.");
        add(MODID + ".configuration.turbine_blade_efficiency", "Rotor Blade Efficiency");
        add(MODID + ".configuration.turbine_blade_efficiency.tooltip", "Multiplier for the energy transferred from the fluid flow to this type of rotor blade. Order: steel, extreme alloy, SiC-SiC CMC.");
        add(MODID + ".configuration.turbine_blade_expansion", "Rotor Blade Expansion Coefficient");
        add(MODID + ".configuration.turbine_blade_expansion.tooltip", "Coefficient by which a set of this type of rotor blade multiplies the volume of the fluid in the oncoming flow. Order: steel, extreme alloy, SiC-SiC CMC.");
        add(MODID + ".configuration.turbine_stator_expansion", "Rotor Stator Expansion Coefficient");
        add(MODID + ".configuration.turbine_stator_expansion.tooltip", "Coefficient by which a set of rotor stators multiplies the volume of the fluid in the oncoming flow.");
        add(MODID + ".configuration.turbine_coil_conductivity", "Dynamo Coil Conductivity");
        add(MODID + ".configuration.turbine_coil_conductivity.tooltip", "Multiplier for the energy generated by this type of dynamo coil. Order: magnesium, beryllium, aluminum, gold, copper, silver.");
        add(MODID + ".configuration.turbine_coil_rule", "Dynamo Coil Placement Rules");
        add(MODID + ".configuration.turbine_coil_rule.tooltip", "Placement rule to be parsed for this type of dynamo coil. Order: magnesium, beryllium, aluminum, gold, copper, silver.");
        add(MODID + ".configuration.turbine_connector_rule", "Coil Connector Placement Rules");
        add(MODID + ".configuration.turbine_connector_rule.tooltip", "Placement rule to be parsed for this type of coil connector. Order: standard.");
        add(MODID + ".configuration.turbine_spin_up_multiplier_global", "Spin-Up Multiplier Global");
        add(MODID + ".configuration.turbine_spin_up_multiplier_global.tooltip", "Global multiplier for the rate at which turbine rotors accelerate up to the equilibrium angular velocity.");
        add(MODID + ".configuration.turbine_spin_down_multiplier", "Spin-Down Multiplier");
        add(MODID + ".configuration.turbine_spin_down_multiplier.tooltip", "Multiplier for the rate at which turbine rotors decelerate.");
        add(MODID + ".configuration.turbine_mb_per_blade", "Rotor Blade Maximum Process Rate");
        add(MODID + ".configuration.turbine_mb_per_blade.tooltip", "Maximum mB of fluid processed per tick per rotor blade.");
        add(MODID + ".configuration.turbine_throughput_leniency_params", "Throughput Leniency Parameters");
        add(MODID + ".configuration.turbine_throughput_leniency_params.tooltip", "Parameters used for the throughput efficiency factor. The first determines the minimum value of the multiplier, while the second determines the ratio of actual throughput to ideal throughput at which there is no penalty.");
        add(MODID + ".configuration.turbine_tension_throughput_factor", "Tension Throughput Factor");
        add(MODID + ".configuration.turbine_tension_throughput_factor.tooltip", "Determines the maximum for the ratio of actual throughput to ideal throughput, and thus the ratio at which the rate of increase of bearing tension is maximal.");
        add(MODID + ".configuration.turbine_tension_leniency", "Tension Leniency Factor");
        add(MODID + ".configuration.turbine_tension_leniency.tooltip", "Determines the leniency for the ratio of actual throughput to ideal throughput at which bearing tension will begin to increase.");
        add(MODID + ".configuration.turbine_power_bonus_multiplier", "Power Bonus Multiplier");
        add(MODID + ".configuration.turbine_power_bonus_multiplier.tooltip", "Multiplier for the input rate power output bonus.");
        add(MODID + ".configuration.turbine_sound_volume", "Turbine Sound Volume");
        add(MODID + ".configuration.turbine_sound_volume.tooltip", "Modifier for the volume of turbine sound effects.");
        add(MODID + ".configuration.turbine_particles", "Particle Spawn Rate");
        add(MODID + ".configuration.turbine_particles.tooltip", "Multiplier for the rate of steam particles spawned in the turbine.");
        add(MODID + ".configuration.turbine_render_blade_width", "Rotor Blade Width");
        add(MODID + ".configuration.turbine_render_blade_width.tooltip", "Multiplier for the width of rendered rotor blades and stators.");
        add(MODID + ".configuration.turbine_render_rotor_expansion", "Rotor Render Expansion");
        add(MODID + ".configuration.turbine_render_rotor_expansion.tooltip", "Parameter determining the rate of expansion of the size of turbine rotors along the direction of steam flow.");
        add(MODID + ".configuration.turbine_render_rotor_speed", "Rotor Speed Multiplier");
        add(MODID + ".configuration.turbine_render_rotor_speed.tooltip", "Multiplier for the maximum rotation speed of rendered turbine rotors.");

        add(MODID + ".configuration.quantum", "Quantum Configs");
        add(MODID + ".configuration.quantum.tooltip", "Configure quantum mechanical systems.");

//add(MODID + ".configuration.quantum_dedicated_server", "Enable on Dedicated Servers");
//add(MODID + ".configuration.quantum_dedicated_server.tooltip", "Will quantum computers form on dedicated servers? Disabled by default to prevent players using up server memory, and because generated code is not sent to clients but instead stored in the server's directory.");
//add(MODID + ".configuration.quantum_max_qubits", "Max Qubits");
//add(MODID + ".configuration.quantum_max_qubits.tooltip", "The maximum number of qubits a single quantum computer multiblock can have.");
//add(MODID + ".configuration.quantum_angle_precision", "Gate Angle Precision");
//add(MODID + ".configuration.quantum_angle_precision.tooltip", "Controls the precision allowed for setting the working angle of gates by defining the number of available angles, uniformly distributed from 0 to 360 degrees.");

        add(MODID + ".configuration.entity", "Entity Configs");
        add(MODID + ".configuration.entity.tooltip", "Configure entities.");

//add(MODID + ".configuration.entity_tracking_range", "Entity Tracking Range");
//add(MODID + ".configuration.entity_tracking_range.tooltip", "The range at which Minecraft will send tracking updates for NC entities.");

        add(MODID + ".configuration.radiation", "Radiation Configs");
        add(MODID + ".configuration.radiation.tooltip", "Configure aspects of nuclear radiation.");

//add(MODID + ".configuration.radiation_enabled", "Enable Radiation");
//add(MODID + ".configuration.radiation_enabled.tooltip", "Will nuclear radiation exist in the world? Note that the game will require a restart for radiation-related items to be shown in JEI after enabling this feature.");

//add(MODID + ".configuration.radiation_immune_players", "Player Radiation Immunity");
//add(MODID + ".configuration.radiation_immune_players.tooltip", "List of UUIDs for players who will be given permanent radiation immunity.");

//add(MODID + ".configuration.radiation_world_chunks_per_tick", "Radiation World Chunks Per Tick");
//add(MODID + ".configuration.radiation_world_chunks_per_tick.tooltip", "The maximum number of chunks in which radiation will be updated per tick.");
//add(MODID + ".configuration.radiation_player_tick_rate", "Radiation Player Tick Rate");
//add(MODID + ".configuration.radiation_player_tick_rate.tooltip", "The number of ticks between each player radiation update.");

//add(MODID + ".configuration.radiation_worlds", "World Background Radiation");
//add(MODID + ".configuration.radiation_worlds.tooltip", "List of dimensions and their background radiation levels. Format: 'dimID_radiationLevel'.");
//add(MODID + ".configuration.radiation_biomes", "Biome Background Radiation");
//add(MODID + ".configuration.radiation_biomes.tooltip", "List of biomes and their background radiation levels. These values stack with the underlying world's background radiation. Format: 'modid:biomeName_radiationLevel'.");
//add(MODID + ".configuration.radiation_structures", "Structure Background Radiation");
//add(MODID + ".configuration.radiation_structures.tooltip", "List of structures and their background radiation levels. These values stack with the underlying world or biome's background radiation. Format: 'structureName_radiationLevel'.");
//add(MODID + ".configuration.radiation_world_limits", "World Radiation Limit");
//add(MODID + ".configuration.radiation_world_limits.tooltip", "List of dimensions and their radiation level limits. Format: 'dimID_radiationLimit'.");
//add(MODID + ".configuration.radiation_biome_limits", "Biome Radiation Limit");
//add(MODID + ".configuration.radiation_biome_limits.tooltip", "List of biomes and their radiation level limits. These values stack with the underlying world's background radiation. Format: 'modid:biomeName_radiationLimit'.");
//add(MODID + ".configuration.radiation_from_biomes_dims_blacklist", "Biome Radiation Dimension Blacklist");
//add(MODID + ".configuration.radiation_from_biomes_dims_blacklist.tooltip", "List of IDs of dimensions to blacklist for biome background radiation.");

//add(MODID + ".configuration.radiation_ores", "Ore Stack Radiation");
//add(MODID + ".configuration.radiation_ores.tooltip", "List of ore dict entries and their stack radiation levels. Can be used to overwrite default values. Can use Unix-style wildcards for the ore name. Format: 'oreName_radiationLevel'.");
//add(MODID + ".configuration.radiation_items", "Item Stack Radiation");
//add(MODID + ".configuration.radiation_items.tooltip", "List of items and their stack radiation levels. Can be used to overwrite default values. Format: 'modid:name:meta_radiationLevel'.");
//add(MODID + ".configuration.radiation_blocks", "Block Stack Radiation");
//add(MODID + ".configuration.radiation_blocks.tooltip", "List of blocks and their radiation levels. Can be used to overwrite default values. Format: 'modid:name:meta_radiationLevel'.");
//add(MODID + ".configuration.radiation_fluids", "Fluid Stack Radiation");
//add(MODID + ".configuration.radiation_fluids.tooltip", "List of fluids and their radiation levels. Can be used to overwrite default values. If the fluid has a corresponding block, then it will also be given a radiation level. Format: 'fluidName_radiationLevel'.");
//add(MODID + ".configuration.radiation_foods", "Food Radiation and Resistance");
//add(MODID + ".configuration.radiation_foods.tooltip", "List of foods and the rads and rad resistance gained on consumption. Negative values mean rads and rad resistance will be lost. Can be used to overwrite default values. Format: 'modid:name:meta_radiationLevel_radiationResistance'.");

//add(MODID + ".configuration.radiation_ores_blacklist", "Ore Stack Radiation Blacklist");
//add(MODID + ".configuration.radiation_ores_blacklist.tooltip", "List of ore dict entries that will be forced not have a radiation level. Can use Unix-style wildcards for the ore name. Format: 'oreName'.");
//add(MODID + ".configuration.radiation_items_blacklist", "Item Stack Radiation Blacklist");
//add(MODID + ".configuration.radiation_items_blacklist.tooltip", "List of items that will be forced not have a radiation level. Format: 'modid:name:meta'.");
//add(MODID + ".configuration.radiation_blocks_blacklist", "Block Stack Radiation Blacklist");
//add(MODID + ".configuration.radiation_blocks_blacklist.tooltip", "List of blocks that will be forced not have a radiation level. Format: 'modid:name:meta'.");
//add(MODID + ".configuration.radiation_fluids_blacklist", "Fluid Radiation Blacklist");
//add(MODID + ".configuration.radiation_fluids_blacklist.tooltip", "List of fluids that will be forced not have a radiation level. Format: 'fluidName'.");

//add(MODID + ".configuration.max_player_rads", "Max Player Rads");
//add(MODID + ".configuration.max_player_rads.tooltip", "The maximum number of rads a player can have before the radiation is fatal.");
//add(MODID + ".configuration.radiation_player_decay_rate", "Player Radiation Decay Rate");
//add(MODID + ".configuration.radiation_player_decay_rate.tooltip", "The rate at which player radiation decreases over time, as a fraction of the player's current radiation level per tick.");
//add(MODID + ".configuration.max_entity_rads", "Max Entity Rads");
//add(MODID + ".configuration.max_entity_rads.tooltip", "List of entities and their maximum rad counts. Used to overwrite default values (100 Rad per heart of health). Format: 'modid:name_maxRads'.");
//add(MODID + ".configuration.radiation_entity_decay_rate", "Entity Radiation Decay Rate");
//add(MODID + ".configuration.radiation_entity_decay_rate.tooltip", "The rate at which entity radiation decreases over time, as a fraction of the entity's current radiation level per tick.");
//add(MODID + ".configuration.radiation_spread_rate", "Chunk Radiation Spread Rate");
//add(MODID + ".configuration.radiation_spread_rate.tooltip", "Controls the rate at which radiation will spread from chunk to chunk.");
//add(MODID + ".configuration.radiation_spread_gradient", "Chunk Radiation Spread Gradient");
//add(MODID + ".configuration.radiation_spread_gradient.tooltip", "Controls how high the ratio of radiation levels between chunks must be for radiation to spread between them.");
//add(MODID + ".configuration.radiation_decay_rate", "Chunk Radiation Decay Rate");
//add(MODID + ".configuration.radiation_decay_rate.tooltip", "Controls the rate at which radiation decreases in a chunk over time.");
//add(MODID + ".configuration.radiation_lowest_rate", "Lowest Radiation Rate");
//add(MODID + ".configuration.radiation_lowest_rate.tooltip", "The lowest rate of player and chunk irradiation possible in Rad/t - amounts below this are ignored.");
//add(MODID + ".configuration.radiation_chunk_limit", "Chunk Radiation Limit");
//add(MODID + ".configuration.radiation_chunk_limit.tooltip", "The maximum chunk radiation level possible in Rad/t. A negative value means there is no limit.");

//add(MODID + ".configuration.radiation_sound_volumes", "Radiation Sound Volumes");
//add(MODID + ".configuration.radiation_sound_volumes.tooltip", "Modifiers for the volumes of radiation sound effects. Order: Geiger counter ticking, RadAway use, Rad-X use, food effects, chems wearing off, radiation badge breaking, radiation level warning, feral ghoul attack.");
//add(MODID + ".configuration.radiation_check_blocks", "Block Radiation Contribution");
//add(MODID + ".configuration.radiation_check_blocks.tooltip", "Should non-tile entity blocks contribute to chunk radiation?");
//add(MODID + ".configuration.radiation_block_effect_max_rate", "Radiation Block Mutation Max Rate");
//add(MODID + ".configuration.radiation_block_effect_max_rate.tooltip", "The maximum number of attempts per chunk radiation update to mutate blocks.");
//add(MODID + ".configuration.radiation_rain_mult", "Radiation Rain Multiplier");
//add(MODID + ".configuration.radiation_rain_mult.tooltip", "Multiplies the rate of irradiation while in rain.");
//add(MODID + ".configuration.radiation_swim_mult", "Radiation Swimming Multiplier");
//add(MODID + ".configuration.radiation_swim_mult.tooltip", "Multiplies the rate of irradiation while swimming.");
//add(MODID + ".configuration.radiation_ic2_reactor_mult", "Radiation IC2 Reactor Multiplier");
//add(MODID + ".configuration.radiation_ic2_reactor_mult.tooltip", "Multiplier for the radiation level of an IC2 reactor based on its EU/t output.");

//add(MODID + ".configuration.radiation_feral_ghoul_attack", "Feral Ghoul Attack Radiation");
//add(MODID + ".configuration.radiation_feral_ghoul_attack.tooltip", "The amount of radiation received from an attack by a feral ghoul.");

//add(MODID + ".configuration.radiation_radaway_amount", "RadAway Rads Removed");
//add(MODID + ".configuration.radiation_radaway_amount.tooltip", "The total number of rads removed when using RadAway.");
//add(MODID + ".configuration.radiation_radaway_slow_amount", "Slow-Acting RadAway Rads Removed");
//add(MODID + ".configuration.radiation_radaway_slow_amount.tooltip", "The total number of rads removed when using Slow-Acting RadAway.");
//add(MODID + ".configuration.radiation_radaway_rate", "RadAway Rad Removal Rate");
//add(MODID + ".configuration.radiation_radaway_rate.tooltip", "The rate at which rads is removed when using RadAway in Rad/t.");
//add(MODID + ".configuration.radiation_radaway_slow_rate", "Slow-Acting RadAway Rad Removal Rate");
//add(MODID + ".configuration.radiation_radaway_slow_rate.tooltip", "The rate at which rads is removed when using Slow-Acting RadAway in Rad/t.");
//add(MODID + ".configuration.radiation_poison_time", "Rad Poisoning Time");
//add(MODID + ".configuration.radiation_poison_time.tooltip", "The time taken for a dose of radiation poisoning to be added to the player's rad count in ticks.");
//add(MODID + ".configuration.radiation_radaway_cooldown", "RadAway Cooldown Time");
//add(MODID + ".configuration.radiation_radaway_cooldown.tooltip", "The time that must be waited between doses of RadAway in ticks.");
//add(MODID + ".configuration.radiation_rad_x_amount", "Rad-X Rad Resistance");
//add(MODID + ".configuration.radiation_rad_x_amount.tooltip", "Controls the amount of rad resistance gained when consuming Rad-X.");
//add(MODID + ".configuration.radiation_rad_x_lifetime", "Rad-X Lifetime");
//add(MODID + ".configuration.radiation_rad_x_lifetime.tooltip", "The time taken for one dose of Rad-X to wear off in ticks.");
//add(MODID + ".configuration.radiation_rad_x_cooldown", "Rad-X Cooldown Time");
//add(MODID + ".configuration.radiation_rad_x_cooldown.tooltip", "The time that must be waited between doses of Rad-X in ticks.");
//add(MODID + ".configuration.radiation_shielding_level", "Radiation Shielding Levels");
//add(MODID + ".configuration.radiation_shielding_level.tooltip", "The rad resistance levels provided by the three levels of shielding. Order: light, medium, heavy.");
//add(MODID + ".configuration.radiation_tile_shielding", "Radiation Container Shielding");
//add(MODID + ".configuration.radiation_tile_shielding.tooltip", "If enabled along with hardcore containers, radiation shielding can be applied to tile entities.");
//add(MODID + ".configuration.radiation_hazmat_shielding", "Radiation Hazmat Shielding");
//add(MODID + ".configuration.radiation_hazmat_shielding.tooltip", "The rad resistance levels provided by each piece of the hazmat suit. Order: headwear, chestpiece, leggings, boots.");
//add(MODID + ".configuration.radiation_scrubber_fraction", "Scrubber Max Removal Rate");
//add(MODID + ".configuration.radiation_scrubber_fraction.tooltip", "The maximum rate at which a 100% efficiency scrubber will remove radiation as a fraction of the radiation level of the chunk it is in. Only applies when using linear scrubbing mechanics.");
//add(MODID + ".configuration.radiation_scrubber_radius", "Scrubber Search Radius");
//add(MODID + ".configuration.radiation_scrubber_radius.tooltip", "The range of scrubbers' search for occluding blocks.");
//add(MODID + ".configuration.radiation_scrubber_non_linear", "Non-linear Scrubber Mechanics");
//add(MODID + ".configuration.radiation_scrubber_non_linear.tooltip", "Use a non-linear scrubbing rate equation instead of the linear behaviour?");
//add(MODID + ".configuration.radiation_scrubber_param", "Non-linear Scrubber Parameters");
//add(MODID + ".configuration.radiation_scrubber_param.tooltip", "The four parameters a,b,c,d in the non-linear scrubbing rate equation 'F = a^[-(S/b)^(1 + S/c)^(1/d)]', where 'F' is the remaining fraction of radiation left in the chunk's update buffer after scrubbing and 'S' is the effective number of scrubbers in the chunk after accounting for their efficiencies. It is recommended that you only modify these if you know what you are doing.");
//add(MODID + ".configuration.radiation_scrubber_time", "Scrubber Recipe Lifetime");
//add(MODID + ".configuration.radiation_scrubber_time.tooltip", "The lifetime in ticks of one item or 250 mB of fluid in an active scrubber. Order: borax dust, RadAway, Slow-Acting RadAway");
//add(MODID + ".configuration.radiation_scrubber_power", "Scrubber Recipe Power Use");
//add(MODID + ".configuration.radiation_scrubber_power.tooltip", "The power in RF/t scrubbers need to be supplied to run with these ingredients. Order: borax dust, RadAway, Slow-Acting RadAway");
//add(MODID + ".configuration.radiation_scrubber_efficiency", "Scrubber Recipe Efficiency");
//add(MODID + ".configuration.radiation_scrubber_efficiency.tooltip", "The efficiency multiplier of an active scrubber using these ingredients. Order: borax dust, RadAway, Slow-Acting RadAway");
//add(MODID + ".configuration.radiation_geiger_block_redstone", "Geiger Block Comparator Scale");
//add(MODID + ".configuration.radiation_geiger_block_redstone.tooltip", "The radiation level, as a power of ten, at which the Geiger counter block will emit a full-strength comparator signal.");

//add(MODID + ".configuration.radiation_shielding_default_recipes", "Radiation Shielding Default Recipes");
//add(MODID + ".configuration.radiation_shielding_default_recipes.tooltip", "If enabled, radiation shielding attachment recipes will be added for all registered armor pieces.");
//add(MODID + ".configuration.radiation_shielding_item_blacklist", "Radiation Shielding ItemStack Blacklist");
//add(MODID + ".configuration.radiation_shielding_item_blacklist.tooltip", "List of armor item stacks for which shielding recipes will not be added by default. Format: 'modid:armorName:meta'.");
//add(MODID + ".configuration.radiation_shielding_custom_stacks", "Radiation Shielding ItemStack Whitelist");
//add(MODID + ".configuration.radiation_shielding_custom_stacks.tooltip", "List of armor item stacks for which shielding recipes will be added. Mostly used when the automated shielding recipes are disabled. Format: 'modid:armorName:meta'.");
//add(MODID + ".configuration.radiation_shielding_default_levels", "Radiation Default Armor Shielding");
//add(MODID + ".configuration.radiation_shielding_default_levels.tooltip", "List of armor item stacks and their default radiation shielding levels. Format: 'modid:armorName:meta_resistance'. Note: this is not the only way to add these values if you have CraftTweaker installed. Rad resistance can also be given to armor by changing their recipe such that an NBT tag called "ncRadiationResistance" with the double value equal to the resistance level is attached to it.");

//add(MODID + ".configuration.radiation_hud_size", "Radiation HUD Size");
//add(MODID + ".configuration.radiation_hud_size.tooltip", "Modifier for the size of radiation info on the HUD.");
//add(MODID + ".configuration.radiation_hud_position", "Radiation HUD Position");
//add(MODID + ".configuration.radiation_hud_position.tooltip", "Angle from the center of the screen at which radiation info appears on the HUD. 0 degrees is the top of the screen, 45 is top-right, etc.");
//add(MODID + ".configuration.radiation_hud_position_cartesian", "HUD Cartesian Position");
//add(MODID + ".configuration.radiation_hud_position_cartesian.tooltip", "Optional config to specify where the rads overlay should appear on the screen if not wanted on a side or at a corner - if used, this config will be used in place of the angle-based one. Two doubles are required [x, y] which specify how far right (x) and how far down (y) from the top-left corner of the screen the overlay is drawn at.");
//add(MODID + ".configuration.radiation_hud_text_outline", "HUD Text Outline");
//add(MODID + ".configuration.radiation_hud_text_outline.tooltip", "If enabled, a black outline will surround the radiation counter text underneath the rad bar.");

//add(MODID + ".configuration.radiation_require_counter", "Counter Required For Info");
//add(MODID + ".configuration.radiation_require_counter.tooltip", "If disabled, the rads overlay will show and the ticking will be heard even when a Geiger counter is not being held.");
//add(MODID + ".configuration.radiation_chunk_boundaries", "Counter/Scrubber Chunk Boundaries");
//add(MODID + ".configuration.radiation_chunk_boundaries.tooltip", "If enabled, chunk boundaries will be shown while holding or looking at a Geiger counter block or radiation scrubber.");
//add(MODID + ".configuration.radiation_unit_prefixes", "Radiation Unit Prefixes");
//add(MODID + ".configuration.radiation_unit_prefixes.tooltip", "If set to a positive integer, radiation levels will be shown without unit prefixes to this number of significant figures.");

//add(MODID + ".configuration.radiation_badge_durability", "Radiation Badge Durability");
//add(MODID + ".configuration.radiation_badge_durability.tooltip", "Determines the total radiation that a Radiation Badge can be exposed to before disintegrating.");
//add(MODID + ".configuration.radiation_badge_info_rate", "Radiation Badge Info Rate");
//add(MODID + ".configuration.radiation_badge_info_rate.tooltip", "Determines the amount of exposed radiation after which the Radiation Badge will inform the player of its total exposure level as a fraction of the durability.");

//add(MODID + ".configuration.radiation_tile_entities", "Tile Entity Radiation");
//add(MODID + ".configuration.radiation_tile_entities.tooltip", "If enabled, tile entities can irradiate the chunk they are in.");
//add(MODID + ".configuration.radiation_hardcore_stacks", "Hardcore Stacks");
//add(MODID + ".configuration.radiation_hardcore_stacks.tooltip", "If enabled, the radioactive stacks in a player's inventory will irradiate the chunk the player is in as well as the player directly, and will also irradiate the chunk if left to despawn as an entity.");
//add(MODID + ".configuration.radiation_hardcore_containers", "Hardcore Containers");
//add(MODID + ".configuration.radiation_hardcore_containers.tooltip", "The radioactive stacks in container blocks will irradiate the chunk the tile entity is in at a rate of this fraction of their base radiation level.");
//add(MODID + ".configuration.radiation_dropped_items", "Radiation From Dropped Items");
//add(MODID + ".configuration.radiation_dropped_items.tooltip", "Radioactive items dropped on the ground will irradiate the chunk they are in.");
//add(MODID + ".configuration.radiation_death_persist", "Radiation Death Persistence");
//add(MODID + ".configuration.radiation_death_persist.tooltip", "If enabled, players' radiation levels will persist on death.");
//add(MODID + ".configuration.radiation_death_persist_fraction", "Death Persistence Fraction");
//add(MODID + ".configuration.radiation_death_persist_fraction.tooltip", "Specifies the fraction of the player's radiation level that persists if Radiation Death Persistence is enabled.");
//add(MODID + ".configuration.radiation_death_immunity_time", "Post-Death Immunity Time");
//add(MODID + ".configuration.radiation_death_immunity_time.tooltip", "Number of seconds players will have radiation immunity after death due to radiation.");

//add(MODID + ".configuration.radiation_player_debuff_lists", "Player Radiation Effects");
//add(MODID + ".configuration.radiation_player_debuff_lists.tooltip", "Lists of effects experienced at various percentages of radiation level by players. Format: 'radPercent_effect,effect,...,effect', where 'effect' is a potion effect 'potionName@amplifier' or an attribute modifier 'attributeName@amount@operation'.");
//add(MODID + ".configuration.radiation_passive_debuff_lists", "Passive Entity Radiation Effects");
//add(MODID + ".configuration.radiation_passive_debuff_lists.tooltip", "Lists of effects experienced at various percentages of radiation level by passive entities. Format: 'radPercent_effect,effect,...,effect', where 'effect' is a potion effect 'potionName@amplifier' or an attribute modifier 'attributeName@amount@operation'.");
//add(MODID + ".configuration.radiation_mob_buff_lists", "Mob Radiation Effects");
//add(MODID + ".configuration.radiation_mob_buff_lists.tooltip", "Lists of effects experienced at various percentages of radiation level by mobs. Format: 'radPercent_effect,effect,...,effect', where 'effect' is a potion effect 'potionName@amplifier' or an attribute modifier 'attributeName@amount@operation'.");
//add(MODID + ".configuration.radiation_player_rads_fatal", "Player Max Radiation Fatality");
//add(MODID + ".configuration.radiation_player_rads_fatal.tooltip", "If enabled, players will be killed when they reach their maximum rad count.");
//add(MODID + ".configuration.radiation_passive_rads_fatal", "Passive Entity Max Radiation Fatality");
//add(MODID + ".configuration.radiation_passive_rads_fatal.tooltip", "If enabled, passive entities will be killed when they reach their maximum rad count.");
//add(MODID + ".configuration.radiation_mob_rads_fatal", "Mob Max Radiation Fatality");
//add(MODID + ".configuration.radiation_mob_rads_fatal.tooltip", "If enabled, mobs will be killed when they reach their maximum rad count.");

//add(MODID + ".configuration.radiation_horse_armor", "Horse Armor
//add(MODID + ".configuration.radiation_horse_armor.tooltip", "If enabled, horses can be equipped with radiation-protective armor.

        add(MODID + ".configuration.misc", "Misc Configs");
        add(MODID + ".configuration.misc.tooltip", "Miscellaneous configurations.");

        add(MODID + ".configuration.give_guidebook", "Give Guidebook");
        add(MODID + ".configuration.give_guidebook.tooltip", "Give new players a copy of the NC guidebook after logging in?");
        add(MODID + ".configuration.ctrl_info", "Ctrl For More Info");
        add(MODID + ".configuration.ctrl_info.tooltip", "Hold down the control key rather than the shift key to show extra tooltip info?");
//add(MODID + ".configuration.rare_drops", "Enable Rare Drops
//add(MODID + ".configuration.rare_drops.tooltip", "Will mobs drop rare items?
//add(MODID + ".configuration.dungeon_loot", "Enable Dungeon Loot
//add(MODID + ".configuration.dungeon_loot.tooltip", "Will NuclearCraft loot generate in chests?
        add(MODID + ".configuration.corium_solidification", "Solid Corium Dimension List");
        add(MODID + ".configuration.corium_solidification.tooltip", "List of IDs of dimensions to whitelist/blacklist for corium solidification.");
        add(MODID + ".configuration.corium_solidification_list_type", "Solid Corium Dim Whitelist/Blacklist");
        add(MODID + ".configuration.corium_solidification_list_type.tooltip", "Is the dimension list a whitelist (false) or a blacklist (true)?");
    }

    private void fuelTypeItems(HashMap<String, DeferredItem<Item>> map, String prepend) {
        for (String name : map.keySet()) {
            add(map.get(name).asItem(), prepend + capitalize(fuelTypes(name, false, false)));
        }
    }

    private void fuelPelletTypeItems(HashMap<String, DeferredItem<Item>> map, String prepend) {
        for (String name : map.keySet()) {
            add(map.get(name).asItem(), prepend + fuelTypes(name, true, true).replace("_", "-"));
        }
    }

    private void fuelPelletItems(HashMap<String, DeferredItem<Item>> map) {
        for (String name : map.keySet()) {
            add(map.get(name).asItem(), pelletTypes(name).replace("_", "-"));
        }
    }

    private String fuelTypes(String name, boolean upperCase, boolean pellet) {
        if (!name.contains("_")) return upperCase ? name.toUpperCase() : name;
        String suffix = switch (name.substring(name.lastIndexOf('_') + 1)) {
            case "c" -> " Carbide";
            case "ni" -> " Nitride" + (pellet ? " Fuel Pellet" : "");
            case "ox" -> " Oxide" + (pellet ? " Fuel Pellet" : "");
            case "za" -> "-Zirconium Alloy" + (pellet ? " Fuel Pellet" : "");
            case "tr" -> " TRISO" + (pellet ? " Fuel Pebble" : "");
            case String val -> {
                try {
                    yield "-" + Integer.parseInt(val);
                } catch (Exception e) {
                    throw new RuntimeException("ERROR, not a valid type");
                }
            }
        };

        if (upperCase) {
            return name.substring(0, name.lastIndexOf('_')).toUpperCase().replaceAll("(?<=[LH]EC)M", "m").replaceAll("(?<=[LH]EC)F", "f") + suffix;
        }
        return name.substring(0, name.lastIndexOf('_')) + suffix;
    }

    private String pelletTypes(String name) {
        if (!name.contains("_")) return name.toUpperCase();
        String suffix = switch (name.substring(name.lastIndexOf('_') + 1)) {
            case "c" -> " Carbide";
            case String val -> {
                try {
                    yield "-" + Integer.parseInt(val);
                } catch (Exception e) {
                    yield "ERROR";
                }
            }
        };

        return name.substring(0, name.lastIndexOf('_')).toUpperCase().replaceAll("(?<=[LH]EC)M", "m").replaceAll("(?<=[LH]EC)F", "f") + suffix;
    }

    private void simpleItems(List<String> list, HashMap<String, DeferredItem<Item>> map, String append) {
        simpleItems(list, map, "", append);
    }

    private void simpleItems(List<String> list, HashMap<String, DeferredItem<Item>> map, String prepend, String append) {
        replaceItems(list, map, prepend, append, Map.of());
    }

    private void simpleItems(HashMap<String, DeferredItem<Item>> map) {
        replaceItems(map.keySet().stream().toList(), map, "", "", Map.of());
    }

    private void simpleItems(HashMap<String, DeferredItem<Item>> map, Map<String, String> replacers) {
        replaceItems(map.keySet().stream().toList(), map, "", "", replacers);
    }

    private void replaceItems(List<String> list, HashMap<String, DeferredItem<Item>> map, String prepend, String append, Map<String, String> replacers) {
        for (String name : list) {
            if (replacers.containsKey(name)) {
                add(map.get(name).asItem(), replacers.get(name));
            } else {
                add(map.get(name).asItem(), prepend + capitalize(name) + append);
            }
        }
    }

    private void simpleBlocks(HashMap<String, DeferredBlock<Block>> map) {
        simpleBlocks(map, Map.of());
    }

    private void simpleBlocks(HashMap<String, DeferredBlock<Block>> map, Map<String, String> replacers) {
        replaceBlocks(map.keySet().stream().toList(), map, "", "", replacers);
    }

    private void replaceBlocks(List<String> list, HashMap<String, DeferredBlock<Block>> map, String prepend, String append, Map<String, String> replacers) {
        for (String name : list) {
            if (replacers.containsKey(name)) {
                add(map.get(name).asItem(), replacers.get(name));
            } else {
                add(map.get(name).asItem(), prepend + capitalize(name) + append);
            }
        }
    }

    private void simpleBlocks(List<String> list, HashMap<String, DeferredBlock<Block>> map, String append) {
        simpleBlocks(list, map, "", append);
    }

    private void simpleBlocks(List<String> list, HashMap<String, DeferredBlock<Block>> map, String prepend, String append) {
        for (String name : list) {
            add(map.get(name).asItem(), prepend + capitalize(name) + append);
        }
    }

    private String capitalize(String input) {
        return String.join(" ", Arrays.stream(input.split("_")).map(StringUtils::capitalize).toList()).trim();
    }
}