package com.nred.nuclearcraft.compat.mekanism;

import com.nred.nuclearcraft.block_entity.fission.FissionCoolerEntity;
import com.nred.nuclearcraft.block_entity.fission.FissionVentEntity;
import com.nred.nuclearcraft.block_entity.fission.SaltFissionHeaterEntity;
import com.nred.nuclearcraft.block_entity.fission.SaltFissionVesselEntity;
import com.nred.nuclearcraft.block_entity.fission.port.FissionCoolerPortEntity;
import com.nred.nuclearcraft.block_entity.fission.port.FissionHeaterPortEntity;
import com.nred.nuclearcraft.block_entity.fission.port.FissionVesselPortEntity;
import com.nred.nuclearcraft.block_entity.hx.HeatExchangerInletEntity;
import com.nred.nuclearcraft.block_entity.hx.HeatExchangerOutletEntity;
import com.nred.nuclearcraft.block_entity.machine.MachineProcessPortEntity;
import com.nred.nuclearcraft.block_entity.machine.MachineReservoirPortEntity;
import com.nred.nuclearcraft.block_entity.turbine.TurbineInletEntity;
import com.nred.nuclearcraft.block_entity.turbine.TurbineOutletEntity;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;

import static com.nred.nuclearcraft.registration.BlockEntityRegistration.*;
import static com.nred.nuclearcraft.registration.BlockRegistration.PROCESSOR_MAP;

public class MekanismCapabilities {
    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        // Mekanism Chemical Capabilities
        BlockCapability.getAllProxyable().stream().filter(a -> a.name().equals(ResourceLocation.parse("mekanism:chemical_handler"))).findFirst().ifPresent(c -> {
            BlockCapability<mekanism.api.chemical.IChemicalHandler, Direction> CHEMICAL = (BlockCapability<mekanism.api.chemical.IChemicalHandler, Direction>) c;

            // Turbine
            event.registerBlockEntity(CHEMICAL, TURBINE_ENTITY_TYPE.get("inlet").get(), (entity, direction) -> (com.nred.nuclearcraft.block_entity.internal.fluid.ChemicalTile) ((TurbineInletEntity) entity).getChemicalCapability(direction));
            event.registerBlockEntity(CHEMICAL, TURBINE_ENTITY_TYPE.get("outlet").get(), (entity, direction) -> (com.nred.nuclearcraft.block_entity.internal.fluid.ChemicalTile) ((TurbineOutletEntity) entity).getChemicalCapability(direction));
            // Fission
            event.registerBlockEntity(CHEMICAL, FISSION_ENTITY_TYPE.get("cooler").get(), (entity, direction) -> (com.nred.nuclearcraft.block_entity.internal.fluid.ChemicalTile) ((FissionCoolerEntity) entity).getChemicalCapability(direction));
            event.registerBlockEntity(CHEMICAL, FISSION_ENTITY_TYPE.get("coolant_heater").get(), (entity, direction) -> (com.nred.nuclearcraft.block_entity.internal.fluid.ChemicalTile) ((SaltFissionHeaterEntity) entity).getChemicalCapability(direction));
            event.registerBlockEntity(CHEMICAL, FISSION_ENTITY_TYPE.get("vent").get(), (entity, direction) -> (com.nred.nuclearcraft.block_entity.internal.fluid.ChemicalTile) ((FissionVentEntity) entity).getChemicalCapability(direction));
            event.registerBlockEntity(CHEMICAL, FISSION_ENTITY_TYPE.get("vessel").get(), (entity, direction) -> (com.nred.nuclearcraft.block_entity.internal.fluid.ChemicalTile) ((SaltFissionVesselEntity) entity).getChemicalCapability(direction));
            // Fission Port
            event.registerBlockEntity(CHEMICAL, FISSION_ENTITY_TYPE.get("cooler_port").get(), (entity, direction) -> (com.nred.nuclearcraft.block_entity.internal.fluid.ChemicalTile) ((FissionCoolerPortEntity) entity).getChemicalCapability(direction));
            event.registerBlockEntity(CHEMICAL, FISSION_ENTITY_TYPE.get("coolant_heater_port").get(), (entity, direction) -> (com.nred.nuclearcraft.block_entity.internal.fluid.ChemicalTile) ((FissionHeaterPortEntity) entity).getChemicalCapability(direction));
            event.registerBlockEntity(CHEMICAL, FISSION_ENTITY_TYPE.get("vessel_port").get(), (entity, direction) -> (com.nred.nuclearcraft.block_entity.internal.fluid.ChemicalTile) ((FissionVesselPortEntity) entity).getChemicalCapability(direction));
            // Heat Exchanger
            event.registerBlockEntity(CHEMICAL, HX_ENTITY_TYPE.get("inlet").get(), (entity, direction) -> (com.nred.nuclearcraft.block_entity.internal.fluid.ChemicalTile) ((HeatExchangerInletEntity) entity).getChemicalCapability(direction));
            event.registerBlockEntity(CHEMICAL, HX_ENTITY_TYPE.get("outlet").get(), (entity, direction) -> (com.nred.nuclearcraft.block_entity.internal.fluid.ChemicalTile) ((HeatExchangerOutletEntity) entity).getChemicalCapability(direction));
            // Universal Bin
            event.registerBlockEntity(CHEMICAL, UNIVERSAL_BIN_ENTITY_TYPE.get(), (entity, direction) -> (com.nred.nuclearcraft.block_entity.internal.fluid.ChemicalTankVoid) entity.tank);
            // Collectors
            event.registerBlockEntity(CHEMICAL, NITROGEN_COLLECTOR_ENTITY_TYPE.get(), (entity, direction) -> (com.nred.nuclearcraft.block_entity.internal.fluid.ChemicalTile) entity.getChemicalCapability(direction));
            event.registerBlockEntity(CHEMICAL, NITROGEN_COLLECTOR_COMPACT_ENTITY_TYPE.get(), (entity, direction) -> (com.nred.nuclearcraft.block_entity.internal.fluid.ChemicalTile) entity.getChemicalCapability(direction));
            event.registerBlockEntity(CHEMICAL, NITROGEN_COLLECTOR_DENSE_ENTITY_TYPE.get(), (entity, direction) -> (com.nred.nuclearcraft.block_entity.internal.fluid.ChemicalTile) entity.getChemicalCapability(direction));
            // Machine
            event.registerBlockEntity(CHEMICAL, MACHINE_ENTITY_TYPE.get("process_port").get(), (entity, direction) -> (com.nred.nuclearcraft.block_entity.internal.fluid.ChemicalTile) ((MachineProcessPortEntity) entity).getChemicalCapability(direction));
            event.registerBlockEntity(CHEMICAL, MACHINE_ENTITY_TYPE.get("reservoir_port").get(), (entity, direction) -> (com.nred.nuclearcraft.block_entity.internal.fluid.ChemicalTile) ((MachineReservoirPortEntity) entity).getChemicalCapability(direction));
            // Processors
            for (String typeName : PROCESSOR_MAP.keySet()) {
                event.registerBlockEntity(CHEMICAL, PROCESSOR_ENTITY_TYPE.get(typeName).get(), (entity, direction) -> (com.nred.nuclearcraft.block_entity.internal.fluid.ChemicalTile) entity.getChemicalCapability(direction));
            }
        });
    }
}