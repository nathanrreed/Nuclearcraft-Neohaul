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
        add(MODID + ".tooltip.turbine_computer_port", "Used to access the multiblock via CC: Tweaked.");

        add(MODID + ".recipe_viewer.turbine_energy_density", "Base Energy Density: %s");
        add(MODID + ".recipe_viewer.turbine_expansion", "Fluid Expansion: %s");
        add(MODID + ".recipe_viewer.turbine_spin_up_multiplier", "Spin-up Multiplier: %s");

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

        fuelTypeItems(AMERICIUM_MAP, "Americium-", "");
        fuelTypeItems(BERKELIUM_MAP, "Berkelium-", "");
        fuelTypeItems(BORON_MAP, "Boron-", "");
        fuelTypeItems(CALIFORNIUM_MAP, "Californium-", "");
        fuelTypeItems(CURIUM_MAP, "Curium-", "");
        fuelTypeItems(LITHIUM_MAP, "Lithium-", "");
        fuelTypeItems(NEPTUNIUM_MAP, "Neptunium-", "");
        fuelTypeItems(PLUTONIUM_MAP, "Plutonium-", "");
//        fuelTypeItems(THORIUM_MAP, "Thorium-", "");
        fuelTypeItems(URANIUM_MAP, "Uranium-", "");

        fuelPelletTypeItems(FUEL_AMERICIUM_MAP, "", " Fuel Pellet");
        fuelPelletTypeItems(FUEL_BERKELIUM_MAP, "", " Fuel Pellet");
        fuelPelletTypeItems(FUEL_CALIFORNIUM_MAP, "", " Fuel Pellet");
        fuelPelletTypeItems(FUEL_CURIUM_MAP, "", " Fuel Pellet");
        fuelPelletTypeItems(FUEL_NEPTUNIUM_MAP, "", " Fuel Pellet");
        fuelPelletTypeItems(FUEL_PLUTONIUM_MAP, "", " Fuel Pellet");
        fuelPelletTypeItems(FUEL_THORIUM_MAP, "", " Fuel Pellet");
        fuelPelletTypeItems(FUEL_URANIUM_MAP, "", " Fuel Pellet");

        add(FUEL_MIXED_MAP.get("mix_239").get(), "MIX-239");
        add(FUEL_MIXED_MAP.get("mix_239_c").get(), "MIX-239 Carbide");
        add(FUEL_MIXED_MAP.get("mix_239_ni").get(), "MNI-239 Fuel Pellet");
        add(FUEL_MIXED_MAP.get("mix_239_ox").get(), "MOX-239 Fuel Pellet");
        add(FUEL_MIXED_MAP.get("mix_239_tr").get(), "MTRISO-239 Fuel Pellet");
        add(FUEL_MIXED_MAP.get("mix_239_za").get(), "MZA-239 Fuel Pellet");
        add(FUEL_MIXED_MAP.get("mix_241").get(), "MIX-241");
        add(FUEL_MIXED_MAP.get("mix_241_c").get(), "MIX-241 Carbide");
        add(FUEL_MIXED_MAP.get("mix_241_ni").get(), "MNI-241 Fuel Pellet");
        add(FUEL_MIXED_MAP.get("mix_241_ox").get(), "MOX-241 Fuel Pellet");
        add(FUEL_MIXED_MAP.get("mix_241_tr").get(), "MTRISO-241 Fuel Pellet");
        add(FUEL_MIXED_MAP.get("mix_241_za").get(), "MZA-241 Fuel Pellet");

        fuelPelletTypeItems(DEPLETED_FUEL_AMERICIUM_MAP, "Depleted ", " Fuel Pellet");
        fuelPelletTypeItems(DEPLETED_FUEL_BERKELIUM_MAP, "Depleted ", " Fuel Pellet");
        fuelPelletTypeItems(DEPLETED_FUEL_CALIFORNIUM_MAP, "Depleted ", " Fuel Pellet");
        fuelPelletTypeItems(DEPLETED_FUEL_CURIUM_MAP, "Depleted ", " Fuel Pellet");
        fuelPelletTypeItems(DEPLETED_FUEL_IC2_MAP, "Depleted ", " Fuel Pellet");
        fuelPelletTypeItems(DEPLETED_FUEL_MIXED_MAP, "Depleted ", " Fuel Pellet");
        fuelPelletTypeItems(DEPLETED_FUEL_NEPTUNIUM_MAP, "Depleted ", " Fuel Pellet");
        fuelPelletTypeItems(DEPLETED_FUEL_PLUTONIUM_MAP, "Depleted ", " Fuel Pellet");
        fuelPelletTypeItems(DEPLETED_FUEL_THORIUM_MAP, "Depleted ", " Fuel Pellet");
        fuelPelletTypeItems(DEPLETED_FUEL_URANIUM_MAP, "Depleted ", " Fuel Pellet");

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

        add(MODID + ".message.multitool.energy_toggle", "Toggled side to ");
        add(MODID + ".message.multitool.energy_toggle_opposite", "Toggled opposite side to ");

        add(MODID + "message.filter", "Set filter to: %s");

        add(MODID + ".multitool.quantum_computer.tool_set_angle", "Saved angle of %s degrees to multitool");
        add(MODID + ".multitool.clear_info", "Cleared stored multitool info");
    }

    private void tooltips() {
        turbine();
        fission_reactor();

        add(MODID + ".tooltip.radiation", "Radiation: %s %sRad/t"); // TODO remove
        add(MODID + ".tooltip.cobblestone_generator_no_req_power", "Produces %s constantly.");
        add(MODID + ".tooltip.cobblestone_generator_req_power", "Produces %s constantly if supplied %s.");
        add(MODID + ".tooltip.water_source", "Produces %s of water constantly.");
        add(MODID + ".tooltip.nitrogen_collector", "Produces %s of nitrogen constantly.");
        add(MODID + ".tooltip.solar", "Produces %s constantly during the daytime.");
        add(MODID + ".tooltip.rtg", "Produces %s constantly.");
        add(MODID + ".tooltip.processor.energy.stored", "Energy Stored: %s");
        add(MODID + ".tooltip.processor.energy.using", "§dProcess Power:§r %s/t");
        add(MODID + ".tooltip.processor.energy.speed", "§bSpeed Multiplier:§r x%s");
        add(MODID + ".tooltip.processor.energy.energy", "§bPower Multiplier:§r x%s");
        add(MODID + ".tooltip.processor.energy.not_required", "Does not require energy!");

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

        add(MODID + ".tooltip.redstone_control", "Redstone Control");
        add(MODID + ".tooltip.side_config", "Side Configuration");
        add(MODID + ".tooltip.side_config.energy_upgrade", "Energy Upgrade Slot Configuration");
        add(MODID + ".tooltip.side_config.speed_upgrade", "Speed Upgrade Slot Configuration");
        add(MODID + ".tooltip.side_config.fluid_input", "Input Tank Configuration");
        add(MODID + ".tooltip.side_config.fluid_output", "Output Tank Configuration");
        add(MODID + ".tooltip.side_config.item_input", "Input Slot Configuration");
        add(MODID + ".tooltip.side_config.item_output", "Output Slot Configuration");

        add(MODID + ".tooltip.side_config.up", "TOP: %s");
        add(MODID + ".tooltip.side_config.west", "LEFT: %s");
        add(MODID + ".tooltip.side_config.north", "FRONT: %s");
        add(MODID + ".tooltip.side_config.east", "RIGHT: %s");
        add(MODID + ".tooltip.side_config.down", "BOTTOM: %s");
        add(MODID + ".tooltip.side_config.south", "BACK: %s");
        add(MODID + ".tooltip.side_config.disabled", "DISABLED");
        add(MODID + ".tooltip.side_config.input", "INPUT");
        add(MODID + ".tooltip.side_config.output", "OUTPUT");
        add(MODID + ".tooltip.side_config.auto_output", "AUTO-OUTPUT");
        add(MODID + ".tooltip.side_config.auto_input", "AUTO-INPUT");
        add(MODID + ".tooltip.side_config.both", "BOTH");
        add(MODID + ".tooltip.side_config.slot_setting.slot", "Slot Setting: %s");
        add(MODID + ".tooltip.side_config.slot_setting.tank", "Tank Setting: %s");
        add(MODID + ".tooltip.side_config.slot_setting.default", "DEFAULT");
        add(MODID + ".tooltip.side_config.slot_setting.void_excess", "VOID EXCESS");
        add(MODID + ".tooltip.side_config.slot_setting.void_all", "VOID ALL");

        add(MODID + ".tooltip.shift_for_info", "Hold Shift for more info");
        add(MODID + ".tooltip.ctrl_for_info", "Hold Ctrl for more info");

        add(MODID + ".tooltip.portable.ender_chest", "Access your Ender Chest on the move.");
        add(MODID + ".tooltip.marshmallow", "Many civilizations would not have fallen if they had these on their side.");
        add(MODID + ".tooltip.dominos", "Paul's favourite - restore 16 hunger points with this beauty.");

        add(MODID + ".tooltip.solidified_corium", "The solidified form of a poisonous mixture of fuel and components produced in fission reactor meltdowns.");

        add(MODID + ".tooltip.energy_stored", "Energy Stored: %s");
        add(MODID + ".tooltip.process_time", "§aBase Process Time:§r %s");
        add(MODID + ".tooltip.process_power", "Base Process Power: %s");

        add(MODID + ".tooltip.production_rate", "§aProduction Rate:§r %s/t");

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

    private void fuelTypeItems(HashMap<String, DeferredItem<Item>> map, String prepend, String append) {
        for (String name : map.keySet()) {
            add(map.get(name).asItem(), prepend + capitalize(fuelTypes(name, false)) + append);
        }
    }

    private void fuelPelletTypeItems(HashMap<String, DeferredItem<Item>> map, String prepend, String append) {
        for (String name : map.keySet()) {
            add(map.get(name).asItem(), prepend + fuelTypes(name, true).replace("_", "-") + (name.endsWith("_c") || name.split("_").length == 2 ? "" : append));
        }
    }

    private String fuelTypes(String name, boolean upperCase) {
        if (!name.contains("_")) return upperCase ? name.toUpperCase() : name;
        String suffix = switch (name.substring(name.lastIndexOf('_') + 1)) {
            case "c" -> " Carbide";
            case "ni" -> " Nitride";
            case "ox" -> " Oxide";
            case "za" -> "-Zirconium Alloy";
            case "tr" -> " TRISO";
            case String val -> {
                try {
                    yield "-" + Integer.parseInt(val);
                } catch (Exception e) {
                    yield "ERROR";
                }
            }
        };

        if (upperCase) {
            return name.substring(0, name.lastIndexOf('_')).toUpperCase().replaceAll("(?<=[LH]EC)M", "m").replaceAll("(?<=[LH]EC)F", "f") + suffix;
        }
        return name.substring(0, name.lastIndexOf('_')) + suffix;
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