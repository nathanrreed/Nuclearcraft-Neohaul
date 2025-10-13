package com.nred.nuclearcraft.registration;

import com.nred.nuclearcraft.payload.ButtonPressPayload;
import com.nred.nuclearcraft.payload.ClearPayload;
import com.nred.nuclearcraft.payload.RecipeSetPayload;
import com.nred.nuclearcraft.payload.multiblock.*;
import com.nred.nuclearcraft.payload.multiblock.port.FluidPortUpdatePacket;
import com.nred.nuclearcraft.payload.multiblock.port.ItemPortUpdatePacket;
import com.nred.nuclearcraft.payload.processor.EnergyProcessorUpdatePacket;
import com.nred.nuclearcraft.payload.render.BlockHighlightUpdatePacket;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@EventBusSubscriber
public class PayloadRegistration {
    @SubscribeEvent
    public static void registerPayloads(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar("1");

        // Client to Server
        registrar.playToServer(ButtonPressPayload.TYPE, ButtonPressPayload.STREAM_CODEC, ButtonPressPayload::handleOnServer);
        registrar.playToServer(ClearPayload.TYPE, ClearPayload.STREAM_CODEC, ClearPayload::handleOnServer);

        // Server to Client
        registrar.playToClient(RecipeSetPayload.TYPE, RecipeSetPayload.STREAM_CODEC, RecipeSetPayload::handleOnClient);
//   TODO REMOVE     registrar.playToClient(TurbineRenderPayload.TYPE, TurbineRenderPayload.STREAM_CODEC, TurbineRenderPayload::handleOnClient);
//        registrar.playToClient(TurbineUpdatePayload.TYPE, TurbineUpdatePayload.STREAM_CODEC, TurbineUpdatePayload::handleOnClient);
//        registrar.playToClient(SaltFissionPayload.TYPE, SaltFissionPayload.STREAM_CODEC, SaltFissionPayload::handleOnClient);
//        registrar.playToClient(SolidFissionPayload.TYPE, SolidFissionPayload.STREAM_CODEC, SolidFissionPayload::handleOnClient);


        // CLIENT -> SERVER

//        registrar.playToServer(ClearTankPacket.TYPE, ClearTankPacket.class);
//        registrar.playToServer(ClearFilterTankPacket.TYPE, ClearFilterTankPacket.class);
//
//        registrar.playToServer(ToggleInputTanksSeparatedPacket.TYPE, ToggleInputTanksSeparatedPacket.class);
//        registrar.playToServer(ToggleVoidUnusableFluidInputPacket.TYPE, ToggleVoidUnusableFluidInputPacket.class);
//        registrar.playToServer(ToggleVoidExcessFluidOutputPacket.TYPE, ToggleVoidExcessFluidOutputPacket.class);
//        registrar.playToServer(ToggleAlternateComparatorPacket.TYPE, ToggleAlternateComparatorPacket.class);
//        registrar.playToServer(ToggleRedstoneControlPacket.TYPE, ToggleRedstoneControlPacket.class);
//
//        registrar.playToServer(OpenGuiPacket.TYPE, OpenGuiPacket.class);
//        registrar.playToServer(OpenTileGuiPacket.TYPE, OpenTileGuiPacket.class);
//        registrar.playToServer(OpenSideConfigGuiPacket.TYPE, OpenSideConfigGuiPacket.class);
//        registrar.playToServer(ToggleItemSorptionPacket.TYPE, ToggleItemSorptionPacket.class);
//        registrar.playToServer(ResetItemSorptionsPacket.TYPE, ResetItemSorptionsPacket.class);
//        registrar.playToServer(ToggleItemOutputSettingPacket.TYPE, ToggleItemOutputSettingPacket.class);
//        registrar.playToServer(ToggleTankSorptionPacket.TYPE, ToggleTankSorptionPacket.class);
//        registrar.playToServer(ResetTankSorptionsPacket.TYPE, ResetTankSorptionsPacket.class);
//        registrar.playToServer(ToggleTankOutputSettingPacket.TYPE, ToggleTankOutputSettingPacket.class);
//
        registrar.playToServer(ClearAllMaterialPacket.TYPE, ClearAllMaterialPacket.STREAM_CODEC, ClearAllMaterialPacket.Handler::handleOnServer);

        // SERVER -> CLIENT

//        registrar.playToClient(ConfigUpdatePacket.TYPE, ConfigUpdatePacket.class);
//
        registrar.playToClient(BlockHighlightUpdatePacket.TYPE, BlockHighlightUpdatePacket.STREAM_CODEC, BlockHighlightUpdatePacket.Handler::handleOnClient);

        registrar.playToClient(EnergyProcessorUpdatePacket.TYPE, EnergyProcessorUpdatePacket.STREAM_CODEC, EnergyProcessorUpdatePacket.Handler::handleOnClient);

//        registrar.playToClient(ElectrolyzerUpdatePacket.TYPE, ElectrolyzerUpdatePacket.class);
//        registrar.playToClient(ElectrolyzerRenderPacket.TYPE, ElectrolyzerRenderPacket.class);
//        registrar.playToClient(DistillerUpdatePacket.TYPE, DistillerUpdatePacket.class);
//        registrar.playToClient(DistillerRenderPacket.TYPE, DistillerRenderPacket.class);
//        registrar.playToClient(InfiltratorUpdatePacket.TYPE, InfiltratorUpdatePacket.class);
//        registrar.playToClient(InfiltratorRenderPacket.TYPE, InfiltratorRenderPacket.class);
//
        registrar.playToClient(ItemPortUpdatePacket.TYPE, ItemPortUpdatePacket.STREAM_CODEC, ItemPortUpdatePacket.Handler::handleOnClient);
        registrar.playToClient(FluidPortUpdatePacket.TYPE, FluidPortUpdatePacket.STREAM_CODEC, FluidPortUpdatePacket.Handler::handleOnClient);

        registrar.playToClient(FissionIrradiatorUpdatePacket.TYPE, FissionIrradiatorUpdatePacket.STREAM_CODEC, FissionIrradiatorUpdatePacket.Handler::handleOnClient);
        registrar.playToClient(FissionCoolerUpdatePacket.TYPE, FissionCoolerUpdatePacket.STREAM_CODEC, FissionCoolerUpdatePacket.Handler::handleOnClient);
        registrar.playToClient(SolidFissionCellUpdatePacket.TYPE, SolidFissionCellUpdatePacket.STREAM_CODEC, SolidFissionCellUpdatePacket.Handler::handleOnClient);
        registrar.playToClient(SaltFissionVesselUpdatePacket.TYPE, SaltFissionVesselUpdatePacket.STREAM_CODEC, SaltFissionVesselUpdatePacket.Handler::handleOnClient);
        registrar.playToClient(SaltFissionHeaterUpdatePacket.TYPE, SaltFissionHeaterUpdatePacket.STREAM_CODEC, SaltFissionHeaterUpdatePacket.Handler::handleOnClient);

        registrar.playToClient(SolidFissionUpdatePacket.TYPE, SolidFissionUpdatePacket.STREAM_CODEC, SolidFissionUpdatePacket.Handler::handleOnClient);
        registrar.playToClient(SaltFissionUpdatePacket.TYPE, SaltFissionUpdatePacket.STREAM_CODEC, SaltFissionUpdatePacket.Handler::handleOnClient);


//        registrar.playToClient(HeatExchangerUpdatePacket.TYPE, HeatExchangerUpdatePacket.class);
//        registrar.playToClient(HeatExchangerRenderPacket.TYPE, HeatExchangerRenderPacket.class);
//        registrar.playToClient(CondenserUpdatePacket.TYPE, CondenserUpdatePacket.class);
//        registrar.playToClient(CondenserRenderPacket.TYPE, CondenserRenderPacket.class);
//
        registrar.playToClient(TurbineUpdatePacket.TYPE, TurbineUpdatePacket.STREAM_CODEC, TurbineUpdatePacket.Handler::handleOnClient);
        registrar.playToClient(TurbineRenderPacket.TYPE, TurbineRenderPacket.STREAM_CODEC, TurbineRenderPacket.Handler::handleOnClient);
//
//        registrar.playToClient(QuantumComputerQubitRenderPacket.TYPE, QuantumComputerQubitRenderPacket.class);
//
//        registrar.playToClient(PlayerRadsUpdatePacket.TYPE, PlayerRadsUpdatePacket.class);


    }
}