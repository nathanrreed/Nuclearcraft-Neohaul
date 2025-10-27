package com.nred.nuclearcraft.registration;

import com.nred.nuclearcraft.block_entity.energy.ITileEnergy;
import com.nred.nuclearcraft.block_entity.fission.*;
import com.nred.nuclearcraft.block_entity.fission.port.*;
import com.nred.nuclearcraft.block_entity.fluid.ITileFluid;
import com.nred.nuclearcraft.block_entity.internal.inventory.ItemHandler;
import com.nred.nuclearcraft.block_entity.inventory.ITileInventory;
import com.nred.nuclearcraft.block_entity.turbine.TurbineCoilConnectorEntity;
import com.nred.nuclearcraft.block_entity.turbine.TurbineDynamoCoilEntity;
import com.nred.nuclearcraft.block_entity.turbine.TurbineInletEntity;
import com.nred.nuclearcraft.block_entity.turbine.TurbineOutletEntity;
import com.nred.nuclearcraft.compat.cct.RegisterPeripherals;
import com.nred.nuclearcraft.item.EnergyItem;
import com.nred.nuclearcraft.util.ModCheck;
import mekanism.api.chemical.IChemicalHandler;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.component.CustomData;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.energy.EnergyStorage;
import net.neoforged.neoforge.items.wrapper.InvWrapper;

import static com.nred.nuclearcraft.NuclearcraftNeohaul.MODID;
import static com.nred.nuclearcraft.config.NCConfig.enable_mek_gas;
import static com.nred.nuclearcraft.registration.BlockEntityRegistration.*;
import static com.nred.nuclearcraft.registration.BlockRegistration.PROCESSOR_MAP;
import static com.nred.nuclearcraft.registration.ItemRegistration.LITHIUM_ION_CELL;

@EventBusSubscriber(modid = MODID)
public class CapabilityRegistration {
    @SubscribeEvent
    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, COBBLESTONE_GENERATOR_ENTITY_TYPE.get(), ItemHandler::new);
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, COBBLESTONE_GENERATOR_COMPACT_ENTITY_TYPE.get(), ItemHandler::new);
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, COBBLESTONE_GENERATOR_DENSE_ENTITY_TYPE.get(), ItemHandler::new);

        event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, WATER_SOURCE_ENTITY_TYPE.get(), ITileFluid::getFluidSideCapability);
        event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, WATER_SOURCE_COMPACT_ENTITY_TYPE.get(), ITileFluid::getFluidSideCapability);
        event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, WATER_SOURCE_DENSE_ENTITY_TYPE.get(), ITileFluid::getFluidSideCapability);

        event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, NITROGEN_COLLECTOR_ENTITY_TYPE.get(), ITileFluid::getFluidSideCapability);
        event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, NITROGEN_COLLECTOR_COMPACT_ENTITY_TYPE.get(), ITileFluid::getFluidSideCapability);
        event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, NITROGEN_COLLECTOR_DENSE_ENTITY_TYPE.get(), ITileFluid::getFluidSideCapability);

        for (String typeName : PROCESSOR_MAP.keySet()) {
            event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, PROCESSOR_ENTITY_TYPE.get(typeName).get(), ITileInventory::getItemSideCapability);
            event.registerBlockEntity(Capabilities.EnergyStorage.BLOCK, PROCESSOR_ENTITY_TYPE.get(typeName).get(), ITileEnergy::getEnergySideCapability);
            event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, PROCESSOR_ENTITY_TYPE.get(typeName).get(), ITileFluid::getFluidSideCapability);
        }

        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, NUCLEAR_FURNACE_ENTITY_TYPE.get(), (entity, d) -> new InvWrapper(entity));

        for (int tier = 0; tier < 4; tier++) {
            event.registerBlockEntity(Capabilities.EnergyStorage.BLOCK, SOLAR_PANEL_ENTITY_TYPE.get(tier).get(), ITileEnergy::getEnergySideCapability);
        }

        // Universal Bin
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, UNIVERSAL_BIN_ENTITY_TYPE.get(), (entity, direction) -> new InvWrapper(entity));
        event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, UNIVERSAL_BIN_ENTITY_TYPE.get(), (entity, direction) -> entity.tank);
        event.registerBlockEntity(Capabilities.EnergyStorage.BLOCK, UNIVERSAL_BIN_ENTITY_TYPE.get(), (entity, direction) -> entity.energyStorage);

        // Machine Interface
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, MACHINE_INTERFACE_ENTITY_TYPE.get(), ITileInventory::getItemSideCapability);
        event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, MACHINE_INTERFACE_ENTITY_TYPE.get(), ITileFluid::getFluidSideCapability);
        event.registerBlockEntity(Capabilities.EnergyStorage.BLOCK, MACHINE_INTERFACE_ENTITY_TYPE.get(), ITileEnergy::getEnergySideCapability);

        // Batteries / Voltaic Piles
        event.registerBlockEntity(Capabilities.EnergyStorage.BLOCK, BATTERY_ENTITY_TYPE.get(), (entity, side) -> !entity.ignoreSide(side) ? entity.getEnergySideCapability(side) : null);

        // Decay Generator
        event.registerBlockEntity(Capabilities.EnergyStorage.BLOCK, DECAY_GENERATOR_ENTITY_TYPE.get(), ITileEnergy::getEnergySideCapability);

        // RTG
        event.registerBlockEntity(Capabilities.EnergyStorage.BLOCK, RTG_ENTITY_TYPE.get(), (entity, side) -> !entity.ignoreSide(side) ? entity.getEnergySideCapability(side) : null);

        // Turbine
        event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, TURBINE_ENTITY_TYPE.get("inlet").get(), (entity, direction) -> ((TurbineInletEntity) entity).getFluidSideCapability((direction)));
        event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, TURBINE_ENTITY_TYPE.get("outlet").get(), (entity, direction) -> ((TurbineOutletEntity) entity).getFluidSideCapability(direction));
        event.registerBlockEntity(Capabilities.EnergyStorage.BLOCK, TURBINE_ENTITY_TYPE.get("dynamo").get(), (entity, direction) -> ((TurbineDynamoCoilEntity) entity).getEnergySideCapability(direction));
        event.registerBlockEntity(Capabilities.EnergyStorage.BLOCK, TURBINE_ENTITY_TYPE.get("coil_connector").get(), (entity, direction) -> ((TurbineCoilConnectorEntity) entity).getEnergySideCapability(direction));
        // Fission
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, FISSION_ENTITY_TYPE.get("cell").get(), (entity, direction) -> ((SolidFissionCellEntity) entity).getItemSideCapability(direction));
        event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, FISSION_ENTITY_TYPE.get("cooler").get(), (entity, direction) -> ((FissionCoolerEntity) entity).getFluidSideCapability(direction));
        event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, FISSION_ENTITY_TYPE.get("coolant_heater").get(), (entity, direction) -> ((SaltFissionHeaterEntity) entity).getFluidSideCapability(direction));
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, FISSION_ENTITY_TYPE.get("irradiator").get(), (entity, direction) -> ((FissionIrradiatorEntity) entity).getItemSideCapability(direction));
        event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, FISSION_ENTITY_TYPE.get("vent").get(), (entity, direction) -> ((FissionVentEntity) entity).getFluidSideCapability(direction));
        event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, FISSION_ENTITY_TYPE.get("vessel").get(), (entity, direction) -> ((SaltFissionVesselEntity) entity).getFluidSideCapability(direction));
        // Fission Port
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, FISSION_ENTITY_TYPE.get("cell_port").get(), (entity, direction) -> ((FissionCellPortEntity) entity).getItemSideCapability(direction));
        event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, FISSION_ENTITY_TYPE.get("cooler_port").get(), (entity, direction) -> ((FissionCoolerPortEntity) entity).getFluidSideCapability(direction));
        event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, FISSION_ENTITY_TYPE.get("coolant_heater_port").get(), (entity, direction) -> ((FissionHeaterPortEntity) entity).getFluidSideCapability(direction));
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, FISSION_ENTITY_TYPE.get("irradiator_port").get(), (entity, direction) -> ((FissionIrradiatorPortEntity) entity).getItemSideCapability(direction));
        event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, FISSION_ENTITY_TYPE.get("vessel_port").get(), (entity, direction) -> ((FissionVesselPortEntity) entity).getFluidSideCapability(direction));

        items(event);

        if (ModCheck.ccLoaded()) {
            RegisterPeripherals.registerPeripherals(event);
        }

        if (ModCheck.mekanismLoaded() && enable_mek_gas) {
            // Mekanism Chemical Capabilities
            BlockCapability.getAllProxyable().stream().filter(a -> a.name().equals(ResourceLocation.parse("mekanism:chemical_handler"))).findFirst().ifPresent(c -> {
                BlockCapability<IChemicalHandler, Direction> CHEMICAL = (BlockCapability<IChemicalHandler, Direction>) c;

                // Turbine
                event.registerBlockEntity(CHEMICAL, TURBINE_ENTITY_TYPE.get("inlet").get(), (entity, direction) -> ((TurbineInletEntity) entity).getChemicalCapability(direction));
                event.registerBlockEntity(CHEMICAL, TURBINE_ENTITY_TYPE.get("outlet").get(), (entity, direction) -> ((TurbineOutletEntity) entity).getChemicalCapability(direction));
                // Fission
                event.registerBlockEntity(CHEMICAL, FISSION_ENTITY_TYPE.get("cooler").get(), (entity, direction) -> ((FissionCoolerEntity) entity).getChemicalCapability(direction));
                event.registerBlockEntity(CHEMICAL, FISSION_ENTITY_TYPE.get("coolant_heater").get(), (entity, direction) -> ((SaltFissionHeaterEntity) entity).getChemicalCapability(direction));
                event.registerBlockEntity(CHEMICAL, FISSION_ENTITY_TYPE.get("vent").get(), (entity, direction) -> ((FissionVentEntity) entity).getChemicalCapability(direction));
                event.registerBlockEntity(CHEMICAL, FISSION_ENTITY_TYPE.get("vessel").get(), (entity, direction) -> ((SaltFissionVesselEntity) entity).getChemicalCapability(direction));
                // Fission Port
                event.registerBlockEntity(CHEMICAL, FISSION_ENTITY_TYPE.get("cooler_port").get(), (entity, direction) -> ((FissionCoolerPortEntity) entity).getChemicalCapability(direction));
                event.registerBlockEntity(CHEMICAL, FISSION_ENTITY_TYPE.get("coolant_heater_port").get(), (entity, direction) -> ((FissionHeaterPortEntity) entity).getChemicalCapability(direction));
                event.registerBlockEntity(CHEMICAL, FISSION_ENTITY_TYPE.get("vessel_port").get(), (entity, direction) -> ((FissionVesselPortEntity) entity).getChemicalCapability(direction));

                // Universal Bin
                event.registerBlockEntity(CHEMICAL, UNIVERSAL_BIN_ENTITY_TYPE.get(), (entity, direction) -> entity.tank);

                // Collectors
                event.registerBlockEntity(CHEMICAL, NITROGEN_COLLECTOR_ENTITY_TYPE.get(), ITileFluid::getChemicalCapability);
                event.registerBlockEntity(CHEMICAL, NITROGEN_COLLECTOR_COMPACT_ENTITY_TYPE.get(), ITileFluid::getChemicalCapability);
                event.registerBlockEntity(CHEMICAL, NITROGEN_COLLECTOR_DENSE_ENTITY_TYPE.get(), ITileFluid::getChemicalCapability);

            });
        }
    }

    private static void items(RegisterCapabilitiesEvent event) {
        event.registerItem(Capabilities.EnergyStorage.ITEM, (stack, context) -> new EnergyStorage(((EnergyItem) stack.getItem()).capacity.get(), ((EnergyItem) stack.getItem()).rate.get(), ((EnergyItem) stack.getItem()).rate.get(), stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getInt("energy")) {
            @Override
            public int receiveEnergy(int toReceive, boolean simulate) {
                int rtn = super.receiveEnergy(toReceive, simulate);
                stack.update(DataComponents.CUSTOM_DATA, CustomData.EMPTY, data -> data.update(compoundTag -> compoundTag.putInt("energy", this.energy)));
                return rtn;
            }

            @Override
            public int extractEnergy(int toExtract, boolean simulate) {
                int rtn = super.extractEnergy(toExtract, simulate);
                stack.update(DataComponents.CUSTOM_DATA, CustomData.EMPTY, data -> data.update(compoundTag -> compoundTag.putInt("energy", this.energy)));
                return rtn;
            }
        }, LITHIUM_ION_CELL);
    }
}