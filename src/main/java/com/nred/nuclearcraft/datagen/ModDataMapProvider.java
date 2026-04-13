package com.nred.nuclearcraft.datagen;

import com.nred.nuclearcraft.datamap.*;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.DataMapProvider;

import java.util.concurrent.CompletableFuture;

import static com.nred.nuclearcraft.datagen.ModFluidTagProvider.*;
import static com.nred.nuclearcraft.helpers.RecipeHelpers.tag;
import static com.nred.nuclearcraft.registration.BlockRegistration.*;
import static com.nred.nuclearcraft.registration.DataMapTypeRegistration.*;
import static com.nred.nuclearcraft.registration.FluidRegistration.SALT_SOLUTION_MAP;
import static net.neoforged.neoforge.common.Tags.Items.STORAGE_BLOCKS;

public class ModDataMapProvider extends DataMapProvider {
    protected ModDataMapProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(packOutput, lookupProvider);
    }

    @Override
    protected void gather(HolderLookup.Provider provider) {
        builder(MACHINE_DIAPHRAGM_DATA)
                .add(MACHINE_MAP.get("sintered_steel_diaphragm").getId(), new MachineDiaphragmData(0.8, 1.0), false)
                .add(MACHINE_MAP.get("polyethersulfone_diaphragm").getId(), new MachineDiaphragmData(0.9, 1.5), false)
                .add(MACHINE_MAP.get("zirfon_diaphragm").getId(), new MachineDiaphragmData(1.0, 2.0), false);

        builder(MACHINE_SIEVE_ASSEMBLY_DATA)
                .add(MACHINE_MAP.get("steel_sieve_assembly").getId(), new MachineSieveAssemblyData(0.8), false)
                .add(MACHINE_MAP.get("polytetrafluoroethene_sieve_assembly").getId(), new MachineSieveAssemblyData(0.9), false)
                .add(MACHINE_MAP.get("hastelloy_sieve_assembly").getId(), new MachineSieveAssemblyData(1.0), false);

        builder(ELECTROLYZER_ANODE_DATA)
                .add(MATERIAL_BLOCK_MAP.get("copper_oxide").getId(), new ElectrolyzerAnodeData(0.6), false)
                .add(INGOT_BLOCK_MAP.get("tin_oxide").getId(), new ElectrolyzerAnodeData(0.6), false)
                .add(INGOT_BLOCK_MAP.get("nickel_oxide").getId(), new ElectrolyzerAnodeData(0.7), false)
                .add(INGOT_BLOCK_MAP.get("cobalt_oxide").getId(), new ElectrolyzerAnodeData(0.8), false)
                .add(INGOT_BLOCK_MAP.get("ruthenium_oxide").getId(), new ElectrolyzerAnodeData(0.9), false)
                .add(INGOT_BLOCK_MAP.get("iridium_oxide").getId(), new ElectrolyzerAnodeData(1.0), false);

        builder(ELECTROLYZER_CATHODE_DATA)
                .add(tag(STORAGE_BLOCKS, "iron"), new ElectrolyzerCathodeData(0.6), false)
                .add(tag(STORAGE_BLOCKS, "nickel"), new ElectrolyzerCathodeData(0.7), false)
                .add(tag(STORAGE_BLOCKS, "molybdenum"), new ElectrolyzerCathodeData(0.8), false)
                .add(tag(STORAGE_BLOCKS, "cobalt"), new ElectrolyzerCathodeData(0.9), false)
                .add(tag(STORAGE_BLOCKS, "platinum"), new ElectrolyzerCathodeData(1.0), false)
                .add(tag(STORAGE_BLOCKS, "palladium"), new ElectrolyzerCathodeData(1.0), false);

        builder(ELECTROLYZER_ELECTROLYTE_DATA)
                .add(SALT_SOLUTION_MAP.get("sodium_hydroxide_solution").still.getId(), new ElectrolyzerElectrolyteData(0.9, "hydroxide_solution"), false)
                .add(SALT_SOLUTION_MAP.get("potassium_hydroxide_solution").still.getId(), new ElectrolyzerElectrolyteData(1.0, "hydroxide_solution"), false)
                .add(SALT_SOLUTION_MAP.get("sodium_fluoride_solution").still.getId(), new ElectrolyzerElectrolyteData(0.9, "fluoride_solution"), false)
                .add(SALT_SOLUTION_MAP.get("potassium_fluoride_solution").still.getId(), new ElectrolyzerElectrolyteData(1.0, "fluoride_solution"), false);

        builder(INFILTRATOR_PRESSURE_DATA)
                .add(NITROGEN_TAG, new InfiltratorPressureData(0.8), false)
                .add(ARGON_TAG, new InfiltratorPressureData(0.9), false)
                .add(NEON_TAG, new InfiltratorPressureData(0.9), false)
                .add(HELIUM_TAG, new InfiltratorPressureData(1.0), false);

        builder(FISSION_MODERATOR_DATA)
                .add(INGOT_BLOCK_MAP.get("graphite").getId(), new FissionModeratorData(10, 1.1), false)
                .add(INGOT_BLOCK_MAP.get("beryllium").getId(), new FissionModeratorData(22, 1.05), false)
                .add(HEAVY_WATER_MODERATOR.getId(), new FissionModeratorData(36, 1), false);

        builder(FISSION_REFLECTOR_DATA)
                .add(FISSION_REACTOR_MAP.get("beryllium_carbon_reflector").getId(), new FissionReflectorData(0.5, 1.0), false)
                .add(FISSION_REACTOR_MAP.get("lead_steel_reflector").getId(), new FissionReflectorData(0.25, 0.5), false);
    }
}