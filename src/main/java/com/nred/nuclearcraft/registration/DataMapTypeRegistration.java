package com.nred.nuclearcraft.registration;

import com.nred.nuclearcraft.datamap.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.datamaps.DataMapType;
import net.neoforged.neoforge.registries.datamaps.RegisterDataMapTypesEvent;

import static com.nred.nuclearcraft.helpers.Location.ncLoc;

@EventBusSubscriber
public class DataMapTypeRegistration {
    public static final DataMapType<Item, MachineDiaphragmData> MACHINE_DIAPHRAGM_DATA = DataMapType.builder(ncLoc("machine_diaphragm_data"), Registries.ITEM, MachineDiaphragmData.CODEC).synced(MachineDiaphragmData.CODEC, true).build();
    public static final DataMapType<Item, MachineSieveAssemblyData> MACHINE_SIEVE_ASSEMBLY_DATA = DataMapType.builder(ncLoc("sieve_assembly_data"), Registries.ITEM, MachineSieveAssemblyData.CODEC).synced(MachineSieveAssemblyData.CODEC, true).build();
    public static final DataMapType<Item, ElectrolyzerCathodeData> ELECTROLYZER_CATHODE_DATA = DataMapType.builder(ncLoc("electrolyzer_cathode_data"), Registries.ITEM, ElectrolyzerCathodeData.CODEC).synced(ElectrolyzerCathodeData.CODEC, true).build();
    public static final DataMapType<Item, ElectrolyzerAnodeData> ELECTROLYZER_ANODE_DATA = DataMapType.builder(ncLoc("electrolyzer_anode_data"), Registries.ITEM, ElectrolyzerAnodeData.CODEC).synced(ElectrolyzerAnodeData.CODEC, true).build();
    public static final DataMapType<Fluid, ElectrolyzerElectrolyteData> ELECTROLYZER_ELECTROLYTE_DATA = DataMapType.builder(ncLoc("electrolyzer_electrolyte_data"), Registries.FLUID, ElectrolyzerElectrolyteData.CODEC).synced(ElectrolyzerElectrolyteData.CODEC, true).build();
    public static final DataMapType<Fluid, InfiltratorPressureData> INFILTRATOR_PRESSURE_DATA = DataMapType.builder(ncLoc("infiltrator_pressure_data"), Registries.FLUID, InfiltratorPressureData.CODEC).synced(InfiltratorPressureData.CODEC, true).build();
    public static final DataMapType<Item, FissionModeratorData> FISSION_MODERATOR_DATA = DataMapType.builder(ncLoc("fission_moderator_data"), Registries.ITEM, FissionModeratorData.CODEC).synced(FissionModeratorData.CODEC, true).build();
    public static final DataMapType<Item, FissionReflectorData> FISSION_REFLECTOR_DATA = DataMapType.builder(ncLoc("fission_reflector_data"), Registries.ITEM, FissionReflectorData.CODEC).synced(FissionReflectorData.CODEC, true).build();

    @SubscribeEvent
    public static void registerDataMapTypes(RegisterDataMapTypesEvent event) {
        event.register(MACHINE_DIAPHRAGM_DATA);
        event.register(MACHINE_SIEVE_ASSEMBLY_DATA);
        event.register(ELECTROLYZER_CATHODE_DATA);
        event.register(ELECTROLYZER_ANODE_DATA);
        event.register(ELECTROLYZER_ELECTROLYTE_DATA);
        event.register(INFILTRATOR_PRESSURE_DATA);
        event.register(FISSION_MODERATOR_DATA);
        event.register(FISSION_REFLECTOR_DATA);
    }
}