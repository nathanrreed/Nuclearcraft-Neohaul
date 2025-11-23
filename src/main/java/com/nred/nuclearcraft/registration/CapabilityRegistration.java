package com.nred.nuclearcraft.registration;

import com.nred.nuclearcraft.block_entity.energy.ITileEnergy;
import com.nred.nuclearcraft.block_entity.fission.*;
import com.nred.nuclearcraft.block_entity.fission.port.*;
import com.nred.nuclearcraft.block_entity.fluid.ITileFluid;
import com.nred.nuclearcraft.block_entity.hx.HeatExchangerInletEntity;
import com.nred.nuclearcraft.block_entity.hx.HeatExchangerOutletEntity;
import com.nred.nuclearcraft.block_entity.internal.inventory.ItemHandler;
import com.nred.nuclearcraft.block_entity.inventory.ITileInventory;
import com.nred.nuclearcraft.block_entity.machine.MachinePowerPortEntity;
import com.nred.nuclearcraft.block_entity.machine.MachineProcessPortEntity;
import com.nred.nuclearcraft.block_entity.machine.MachineReservoirPortEntity;
import com.nred.nuclearcraft.block_entity.turbine.TurbineCoilConnectorEntity;
import com.nred.nuclearcraft.block_entity.turbine.TurbineDynamoCoilEntity;
import com.nred.nuclearcraft.block_entity.turbine.TurbineInletEntity;
import com.nred.nuclearcraft.block_entity.turbine.TurbineOutletEntity;
import com.nred.nuclearcraft.capability.radiation.entity.EntityRadsCap;
import com.nred.nuclearcraft.capability.radiation.entity.IEntityRads;
import com.nred.nuclearcraft.capability.radiation.entity.PlayerRadsCap;
import com.nred.nuclearcraft.capability.radiation.resistance.IRadiationResistance;
import com.nred.nuclearcraft.capability.radiation.sink.IRadiationSink;
import com.nred.nuclearcraft.capability.radiation.source.IRadiationSource;
import com.nred.nuclearcraft.capability.radiation.source.RadiationSource;
import com.nred.nuclearcraft.compat.cct.RegisterPeripherals;
import com.nred.nuclearcraft.compat.curios.RegisterCurios;
import com.nred.nuclearcraft.item.EnergyItem;
import com.nred.nuclearcraft.radiation.RadSources;
import com.nred.nuclearcraft.recipe.RecipeHelper;
import com.nred.nuclearcraft.util.ModCheck;
import mekanism.api.chemical.IChemicalHandler;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.CustomData;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.*;
import net.neoforged.neoforge.energy.EnergyStorage;
import net.neoforged.neoforge.items.wrapper.InvWrapper;

import static com.nred.nuclearcraft.NuclearcraftNeohaul.MODID;
import static com.nred.nuclearcraft.config.NCConfig.enable_mek_gas;
import static com.nred.nuclearcraft.helpers.Location.ncLoc;
import static com.nred.nuclearcraft.registration.BlockEntityRegistration.*;
import static com.nred.nuclearcraft.registration.BlockRegistration.PROCESSOR_MAP;
import static com.nred.nuclearcraft.registration.ItemRegistration.LITHIUM_ION_CELL;

@EventBusSubscriber(modid = MODID)
public class CapabilityRegistration {
    public static final EntityCapability<IEntityRads, Void> CAPABILITY_ENTITY_RADS = EntityCapability.createVoid(ncLoc("capability_entity_rads"), IEntityRads.class);
    public static final BlockCapability<IRadiationResistance, Direction> CAPABILITY_RADIATION_RESISTANCE = BlockCapability.createSided(ncLoc("capability_default_radiation_resistance"), IRadiationResistance.class);
    public static final ItemCapability<IRadiationResistance, Void> ITEM_CAPABILITY_RADIATION_RESISTANCE = ItemCapability.createVoid(ncLoc("item_capability_radiation_resistance"), IRadiationResistance.class);
    public static final EntityCapability<IRadiationSink, Void> CAPABILITY_RADIATION_SINK = EntityCapability.createVoid(ncLoc("capability_radiation_sink"), IRadiationSink.class);
    public static final ItemCapability<IRadiationSink, Void> ITEM_CAPABILITY_RADIATION_SINK = ItemCapability.createVoid(ncLoc("item_capability_radiation_sink"), IRadiationSink.class);
    public static final ItemCapability<IRadiationSource, Void> ITEM_CAPABILITY_RADIATION_SOURCE = ItemCapability.createVoid(ncLoc("item_capability_radiation_source"), IRadiationSource.class);

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

        // Radiation Scrubber
        event.registerBlockEntity(Capabilities.EnergyStorage.BLOCK, RADIATION_SCRUBBER_ENTITY_TYPE.get(), ITileEnergy::getEnergySideCapability);
        event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, RADIATION_SCRUBBER_ENTITY_TYPE.get(), ITileFluid::getFluidSideCapability);
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, RADIATION_SCRUBBER_ENTITY_TYPE.get(), ITileInventory::getItemSideCapability);

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
        event.registerBlockEntity(Capabilities.EnergyStorage.BLOCK, FISSION_ENTITY_TYPE.get("power_port").get(), (entity, direction) -> ((FissionPowerPortEntity) entity).getEnergySideCapability(direction));
        // Heat Exchanger
        event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, HX_ENTITY_TYPE.get("inlet").get(), (entity, direction) -> ((HeatExchangerInletEntity) entity).getFluidSideCapability((direction)));
        event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, HX_ENTITY_TYPE.get("outlet").get(), (entity, direction) -> ((HeatExchangerOutletEntity) entity).getFluidSideCapability(direction));
        // Machine
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, MACHINE_ENTITY_TYPE.get("process_port").get(), (entity, direction) -> ((MachineProcessPortEntity) entity).getItemSideCapability(direction));
        event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, MACHINE_ENTITY_TYPE.get("process_port").get(), (entity, direction) -> ((MachineProcessPortEntity) entity).getFluidSideCapability(direction));
        event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, MACHINE_ENTITY_TYPE.get("reservoir_port").get(), (entity, direction) -> ((MachineReservoirPortEntity) entity).getFluidSideCapability(direction));
        event.registerBlockEntity(Capabilities.EnergyStorage.BLOCK, MACHINE_ENTITY_TYPE.get("power_port").get(), (entity, direction) -> ((MachinePowerPortEntity) entity).getEnergySideCapability(direction));

        radiation_capabilities(event);

//        event.registerItem(CAPABILITY_RADIATION_SINK, new RadiationSinkProvider(0D), RADIATION_BADGE); TODO
//
//        @SubscribeEvent
//        public void attachStackRadiationCapability (AttachCapabilitiesEvent < ItemStack > event) {
//            ItemStack stack = event.getObject();
//
//            if (stack.getItem() == NCItems.radiation_badge) {
//                addCapability(event, IRadiationSink.CAPABILITY_RADIATION_SINK_NAME, new RadiationSinkProvider(0D));
//            }
//
//            int packed = RecipeItemHelper.pack(stack);
//            if (RadSources.STACK_MAP.containsKey(packed)) {
//                addCapability(event, IRadiationSource.CAPABILITY_RADIATION_SOURCE_NAME, new RadiationSourceStackProvider(stack));
//            }
//            if (RadArmor.ARMOR_RAD_RESISTANCE_MAP.containsKey(packed)) {
//                addCapability(event, IRadiationResistance.CAPABILITY_RADIATION_RESISTANCE_NAME, new RadiationResistanceStackProvider(stack));
//            }
//        }
//
//        public static <T > void addCapability (AttachCapabilitiesEvent < T > event, ResourceLocation key, ICapabilityProvider capabilityProvider){
//            if (!event.getCapabilities().containsKey(key)) {
//                event.addCapability(key, capabilityProvider);
//            }
//        }

        items(event);

        if (ModCheck.ccLoaded()) {
            RegisterPeripherals.registerPeripherals(event);
        }

        if (ModCheck.curiosLoaded()) {
            RegisterCurios.registerCurios(event);
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
                // Heat Exchanger
                event.registerBlockEntity(CHEMICAL, HX_ENTITY_TYPE.get("inlet").get(), (entity, direction) -> ((HeatExchangerInletEntity) entity).getChemicalCapability(direction));
                event.registerBlockEntity(CHEMICAL, HX_ENTITY_TYPE.get("outlet").get(), (entity, direction) -> ((HeatExchangerOutletEntity) entity).getChemicalCapability(direction));
                // Universal Bin
                event.registerBlockEntity(CHEMICAL, UNIVERSAL_BIN_ENTITY_TYPE.get(), (entity, direction) -> entity.tank);
                // Collectors
                event.registerBlockEntity(CHEMICAL, NITROGEN_COLLECTOR_ENTITY_TYPE.get(), ITileFluid::getChemicalCapability);
                event.registerBlockEntity(CHEMICAL, NITROGEN_COLLECTOR_COMPACT_ENTITY_TYPE.get(), ITileFluid::getChemicalCapability);
                event.registerBlockEntity(CHEMICAL, NITROGEN_COLLECTOR_DENSE_ENTITY_TYPE.get(), ITileFluid::getChemicalCapability);
                // Machine
                event.registerBlockEntity(CHEMICAL, MACHINE_ENTITY_TYPE.get("process_port").get(), (entity, direction) -> ((MachineProcessPortEntity) entity).getChemicalCapability(direction));
                event.registerBlockEntity(CHEMICAL, MACHINE_ENTITY_TYPE.get("reservoir_port").get(), (entity, direction) -> ((MachineReservoirPortEntity) entity).getChemicalCapability(direction));

            });
        }
    }

    private static void radiation_capabilities(RegisterCapabilitiesEvent event) {
        event.registerEntity(CAPABILITY_ENTITY_RADS, EntityType.PLAYER, (entity, _void) -> new PlayerRadsCap(entity));

        for (Item item : BuiltInRegistries.ITEM) {
            event.registerItem(ITEM_CAPABILITY_RADIATION_SOURCE, (itemStack, _void) -> new RadiationSource(RadSources.STACK_MAP.get(RecipeHelper.pack(item))), item);
        }

        for (EntityType<?> entityType : BuiltInRegistries.ENTITY_TYPE) {
            event.registerEntity(CAPABILITY_ENTITY_RADS, entityType, (entity, _void) -> (entity instanceof LivingEntity living && !(entity instanceof Player)) ? new EntityRadsCap(living) : null);
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