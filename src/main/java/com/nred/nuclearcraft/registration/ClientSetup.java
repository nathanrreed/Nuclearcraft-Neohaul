package com.nred.nuclearcraft.registration;

import com.nred.nuclearcraft.block.batteries.BatteryEntity;
import com.nred.nuclearcraft.block.batteries.BatteryRenderer;
import com.nred.nuclearcraft.block.turbine.TurbineControllerEntity;
import com.nred.nuclearcraft.handler.SoundHandler;
import com.nred.nuclearcraft.handler.TooltipHandler;
import com.nred.nuclearcraft.info.Fluids;
import com.nred.nuclearcraft.render.BlockHighlightHandler;
import com.nred.nuclearcraft.render.TurbineRotorRenderer;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.registries.DeferredHolder;

import static com.nred.nuclearcraft.NuclearcraftNeohaul.MODID;
import static com.nred.nuclearcraft.helpers.Concat.fluidValues;
import static com.nred.nuclearcraft.registration.BlockEntityRegistration.BATTERY_ENTITY_TYPE;
import static com.nred.nuclearcraft.registration.BlockEntityRegistration.TURBINE_ENTITY_TYPE;
import static com.nred.nuclearcraft.registration.FluidRegistration.*;

@EventBusSubscriber(modid = MODID, value = Dist.CLIENT)
public class ClientSetup {

    @SubscribeEvent
    public static void init(FMLClientSetupEvent event) {
        NeoForge.EVENT_BUS.register(new SoundHandler());
        NeoForge.EVENT_BUS.register(new TooltipHandler());
        NeoForge.EVENT_BUS.register(new BlockHighlightHandler());
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

    @SubscribeEvent
    public static void blockEntityRenderer(final FMLCommonSetupEvent event) {
        for (DeferredHolder<BlockEntityType<?>, BlockEntityType<? extends BatteryEntity>> type : BATTERY_ENTITY_TYPE.values()) {
            BlockEntityRenderers.register(type.get(), BatteryRenderer::new);
        }
        BlockEntityRenderers.register((BlockEntityType<TurbineControllerEntity>) TURBINE_ENTITY_TYPE.get("controller").get(), TurbineRotorRenderer::new);
    }

    @SubscribeEvent
    public static void bucketColoring(RegisterColorHandlersEvent.Item event) {
        for (Fluids fluid : fluidValues(GAS_MAP, MOLTEN_MAP, CUSTOM_FLUID_MAP, HOT_GAS_MAP, SUGAR_MAP, CHOCOLATE_MAP, FISSION_FLUID_MAP, STEAM_MAP, SALT_SOLUTION_MAP, ACID_MAP, FLAMMABLE_MAP, HOT_COOLANT_MAP, COOLANT_MAP, FISSION_FUEL_MAP)) {
            event.register(((stack, tintIndex) -> tintIndex == 0 ? -1 : fluid.client.getTintColor()), fluid.bucket.asItem());
        }
    }
}