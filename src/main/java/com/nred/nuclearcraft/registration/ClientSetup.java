package com.nred.nuclearcraft.registration;

import com.nred.nuclearcraft.block_entity.hx.CondenserControllerEntity;
import com.nred.nuclearcraft.block_entity.hx.HeatExchangerControllerEntity;
import com.nred.nuclearcraft.block_entity.machine.DistillerControllerEntity;
import com.nred.nuclearcraft.block_entity.machine.ElectrolyzerControllerEntity;
import com.nred.nuclearcraft.block_entity.machine.InfiltratorControllerEntity;
import com.nred.nuclearcraft.block_entity.quantum.QuantumComputerQubitEntity;
import com.nred.nuclearcraft.block_entity.turbine.TurbineControllerEntity;
import com.nred.nuclearcraft.entity.FeralGhoul;
import com.nred.nuclearcraft.handler.SoundHandler;
import com.nred.nuclearcraft.handler.TooltipHandler;
import com.nred.nuclearcraft.info.Fluids;
import com.nred.nuclearcraft.model.FeralGhoulModel;
import com.nred.nuclearcraft.radiation.RadiationRenders;
import com.nred.nuclearcraft.render.BlockHighlightHandler;
import com.nred.nuclearcraft.render.block_entity.*;
import com.nred.nuclearcraft.render.entity.FeralGhoulRender;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;

import static com.nred.nuclearcraft.NuclearcraftNeohaul.MODID;
import static com.nred.nuclearcraft.helpers.Concat.fluidValues;
import static com.nred.nuclearcraft.registration.BlockEntityRegistration.*;
import static com.nred.nuclearcraft.registration.EntityRegistration.FERAL_GHOUL;
import static com.nred.nuclearcraft.registration.FluidRegistration.*;

@EventBusSubscriber(modid = MODID, value = Dist.CLIENT)
public class ClientSetup {

    @SubscribeEvent
    public static void init(FMLClientSetupEvent event) {
        NeoForge.EVENT_BUS.register(new SoundHandler());
        NeoForge.EVENT_BUS.register(new TooltipHandler());
        NeoForge.EVENT_BUS.register(new BlockHighlightHandler());
        NeoForge.EVENT_BUS.register(new RadiationRenders());
    }

    @SubscribeEvent
    public static void fluidLoad(RegisterClientExtensionsEvent event) {
        for (Fluids fluid : fluidValues(GAS_MAP, MOLTEN_MAP, CUSTOM_FLUID_MAP, HOT_GAS_MAP, SUGAR_MAP, CHOCOLATE_MAP, FISSION_FLUID_MAP, STEAM_MAP, SALT_SOLUTION_MAP, ACID_MAP, FLAMMABLE_MAP, HOT_COOLANT_MAP, COOLANT_MAP, FISSION_FUEL_MAP)) {
            event.registerFluidType(fluid.client, fluid.type);
        }
    }

    @SubscribeEvent
    public static void fluidColoring(final FMLCommonSetupEvent event) {
        for (Fluids fluid : fluidValues(GAS_MAP, MOLTEN_MAP, CUSTOM_FLUID_MAP, HOT_GAS_MAP, SUGAR_MAP, CHOCOLATE_MAP, FISSION_FLUID_MAP, STEAM_MAP, SALT_SOLUTION_MAP, ACID_MAP, FLAMMABLE_MAP, HOT_COOLANT_MAP, COOLANT_MAP, FISSION_FUEL_MAP)) {
            ItemBlockRenderTypes.setRenderLayer(fluid.still.get(), RenderType.TRANSLUCENT);
            ItemBlockRenderTypes.setRenderLayer(fluid.flowing.get(), RenderType.TRANSLUCENT);
        }
    }

    @SuppressWarnings("unchecked")
    @SubscribeEvent
    public static void entityRenderer(final FMLCommonSetupEvent event) {
        BlockEntityRenderers.register((BlockEntityType<TurbineControllerEntity>) TURBINE_ENTITY_TYPE.get("controller").get(), TurbineRotorRenderer::new);
        BlockEntityRenderers.register((BlockEntityType<HeatExchangerControllerEntity>) HX_ENTITY_TYPE.get("heat_exchanger_controller").get(), MultiblockHeatExchangerRender::new);
        BlockEntityRenderers.register((BlockEntityType<CondenserControllerEntity>) HX_ENTITY_TYPE.get("condenser_controller").get(), MultiblockCondenserRender::new);
        BlockEntityRenderers.register((BlockEntityType<ElectrolyzerControllerEntity>) MACHINE_ENTITY_TYPE.get("electrolyzer_controller").get(), MultiblockElectrolyzerRender::new);
        BlockEntityRenderers.register((BlockEntityType<DistillerControllerEntity>) MACHINE_ENTITY_TYPE.get("distiller_controller").get(), MultiblockDistillerRender::new);
        BlockEntityRenderers.register((BlockEntityType<InfiltratorControllerEntity>) MACHINE_ENTITY_TYPE.get("infiltrator_controller").get(), MultiblockInfiltratorRender::new);
        BlockEntityRenderers.register((BlockEntityType<QuantumComputerQubitEntity>) QUANTUM_ENTITY_TYPE.get("quantum_computer_qubit").get(), QuantumComputerQubitRender::new);

        EntityRenderers.register(FERAL_GHOUL.get(), FeralGhoulRender::new);
    }

    @SubscribeEvent
    public static void bucketColoring(RegisterColorHandlersEvent.Item event) {
        for (Fluids fluid : fluidValues(GAS_MAP, MOLTEN_MAP, CUSTOM_FLUID_MAP, HOT_GAS_MAP, SUGAR_MAP, CHOCOLATE_MAP, FISSION_FLUID_MAP, STEAM_MAP, SALT_SOLUTION_MAP, ACID_MAP, FLAMMABLE_MAP, HOT_COOLANT_MAP, COOLANT_MAP, FISSION_FUEL_MAP)) {
            event.register(((stack, tintIndex) -> tintIndex == 0 ? -1 : fluid.client.getTintColor()), fluid.bucket.asItem());
        }
    }

    @SubscribeEvent
    public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(FeralGhoulModel.LAYER_LOCATION, FeralGhoulModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(FERAL_GHOUL.get(), FeralGhoul.createAttributes().build());
    }
}