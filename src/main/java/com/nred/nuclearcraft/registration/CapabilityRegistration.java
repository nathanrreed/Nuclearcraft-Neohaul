package com.nred.nuclearcraft.registration;

import com.nred.nuclearcraft.block.collector.MACHINE_LEVEL;
import com.nred.nuclearcraft.block.collector.cobblestone_generator.CobbleGeneratorEntity;
import com.nred.nuclearcraft.block.collector.nitrogen_collector.NitrogenCollectorEntity;
import com.nred.nuclearcraft.block.collector.water_source.WaterSourceEntity;
import com.nred.nuclearcraft.block.fission.FissionVentEntity;
import com.nred.nuclearcraft.block.processor.ProcessorEntity;
import com.nred.nuclearcraft.block.turbine.TurbineCoilConnectorEntity;
import com.nred.nuclearcraft.block.turbine.TurbineDynamoCoilEntity;
import com.nred.nuclearcraft.block.turbine.TurbineInletEntity;
import com.nred.nuclearcraft.block.turbine.TurbineOutletEntity;
import com.nred.nuclearcraft.compat.cct.RegisterPeripherals;
import com.nred.nuclearcraft.config.ProcessorConfig;
import com.nred.nuclearcraft.item.EnergyItem;
import mekanism.api.chemical.IChemicalHandler;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.component.CustomData;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.energy.EnergyStorage;

import static com.nred.nuclearcraft.NuclearcraftNeohaul.MODID;
import static com.nred.nuclearcraft.config.Config.PROCESSOR_CONFIG_MAP;
import static com.nred.nuclearcraft.registration.BlockEntityRegistration.*;
import static com.nred.nuclearcraft.registration.BlockRegistration.PROCESSOR_MAP;
import static com.nred.nuclearcraft.registration.ItemRegistration.LITHIUM_ION_CELL;

@EventBusSubscriber(modid = MODID)
public class CapabilityRegistration {
    @SubscribeEvent
    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        for (MACHINE_LEVEL level : MACHINE_LEVEL.values()) {
            event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, COBBLE_GENERATOR_TYPES.get(level).get(), (collectorEntity, direction) -> ((CobbleGeneratorEntity) collectorEntity).itemStackHandler);
            event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, WATER_SOURCE_TYPES.get(level).get(), (collectorEntity, direction) -> ((WaterSourceEntity) collectorEntity).fluidStackHandler);
            event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, NITROGEN_COLLECTOR_TYPES.get(level).get(), (collectorEntity, direction) -> ((NitrogenCollectorEntity) collectorEntity).fluidStackHandler);
        }

        for (String typeName : PROCESSOR_MAP.keySet()) {
            event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, PROCESSOR_ENTITY_TYPE.get(typeName).get(), ProcessorEntity::getItemHandler);
            ProcessorConfig config = PROCESSOR_CONFIG_MAP.get(typeName);
            if (config.base_power() > 0)
                event.registerBlockEntity(Capabilities.EnergyStorage.BLOCK, PROCESSOR_ENTITY_TYPE.get(typeName).get(), ProcessorEntity::getEnergyHandler);

            if (config.fluid_capacity() > 0)
                event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, PROCESSOR_ENTITY_TYPE.get(typeName).get(), ProcessorEntity::getFluidHandler);
        }

        for (int tier = 0; tier < 4; tier++) {
            event.registerBlockEntity(Capabilities.EnergyStorage.BLOCK, SOLAR_PANEL_ENTITY_TYPE.get(tier).get(), (entity, context) -> entity.energyHandler);
        }

        // Universal Bin
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, UNIVERSAL_BIN_ENTITY_TYPE.get(), (entity, direction) -> entity.itemStackHandler);
        event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, UNIVERSAL_BIN_ENTITY_TYPE.get(), (entity, direction) -> entity.fluidStackHandler);
        event.registerBlockEntity(Capabilities.EnergyStorage.BLOCK, UNIVERSAL_BIN_ENTITY_TYPE.get(), (entity, direction) -> entity.energyHandler);

        // Machine Interface
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, MACHINE_INTERFACE_ENTITY_TYPE.get(), (entity, direction) -> entity.getLevel().isClientSide() || entity.proxyPos == null ? null : entity.getLevel().getCapability(Capabilities.ItemHandler.BLOCK, entity.proxyPos, direction));
        event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, MACHINE_INTERFACE_ENTITY_TYPE.get(), (entity, direction) -> entity.getLevel().isClientSide() || entity.proxyPos == null ? null : entity.getLevel().getCapability(Capabilities.FluidHandler.BLOCK, entity.proxyPos, direction));
        event.registerBlockEntity(Capabilities.EnergyStorage.BLOCK, MACHINE_INTERFACE_ENTITY_TYPE.get(), (entity, direction) -> entity.getLevel().isClientSide() || entity.proxyPos == null ? null : entity.getLevel().getCapability(Capabilities.EnergyStorage.BLOCK, entity.proxyPos, direction));

        // Batteries / Voltaic Piles
        for (int tier : new int[]{0, 1, 2, 3, 10, 11, 12, 13}) {
            event.registerBlockEntity(Capabilities.EnergyStorage.BLOCK, BATTERY_ENTITY_TYPE.get(tier).get(), (entity, context) -> !entity.ignoreSide(context) ? entity.getEnergyStorage() : null);
        }

        // Turbine
        event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, TURBINE_ENTITY_TYPE.get("inlet").get(), (entity, direction) -> ((TurbineInletEntity) entity).getFluidSideCapability((direction)));
        event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, TURBINE_ENTITY_TYPE.get("outlet").get(), (entity, direction) -> ((TurbineOutletEntity) entity).getFluidSideCapability(direction));
        event.registerBlockEntity(Capabilities.EnergyStorage.BLOCK, TURBINE_ENTITY_TYPE.get("dynamo").get(), (entity, direction) -> ((TurbineDynamoCoilEntity) entity).getEnergySideCapability(direction));
        event.registerBlockEntity(Capabilities.EnergyStorage.BLOCK, TURBINE_ENTITY_TYPE.get("coil_connector").get(), (entity, direction) -> ((TurbineCoilConnectorEntity) entity).getEnergySideCapability(direction));

        //TODO
//        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, FISSION_ENTITY_TYPE.get().get(), (entity, direction) -> ((FissionIrradiatorEntity) entity).getItemSideCapability(direction));

        event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, FISSION_ENTITY_TYPE.get("vent").get(), (entity, direction) -> ((FissionVentEntity) entity).getFluidSideCapability(direction));


        items(event);

        if (ModList.get().isLoaded("computercraft")) {
            RegisterPeripherals.registerPeripherals(event);
        }

        if (ModList.get().isLoaded("mekanism")) {
            // Mekanism Chemical Capabilities
            BlockCapability.getAllProxyable().stream().filter(a -> a.name().equals(ResourceLocation.parse("mekanism:chemical_handler"))).findFirst().ifPresent(c -> {
                BlockCapability<IChemicalHandler, Direction> CHEMICAL = (BlockCapability<IChemicalHandler, Direction>) c;

                // Turbine
                event.registerBlockEntity(CHEMICAL, TURBINE_ENTITY_TYPE.get("inlet").get(), (entity, direction) -> ((TurbineInletEntity) entity).getChemicalCapability(direction));
                event.registerBlockEntity(CHEMICAL, TURBINE_ENTITY_TYPE.get("outlet").get(), (entity, direction) -> ((TurbineOutletEntity) entity).getChemicalCapability(direction));

                event.registerBlockEntity(CHEMICAL, FISSION_ENTITY_TYPE.get("vent").get(), (entity, direction) -> ((FissionVentEntity) entity).getChemicalCapability(direction));

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