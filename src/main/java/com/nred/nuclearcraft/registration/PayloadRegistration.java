package com.nred.nuclearcraft.registration;

import com.nred.nuclearcraft.payload.gui.*;
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

        // CLIENT -> SERVER

        registrar.playToServer(ClearTankPacket.TYPE, ClearTankPacket.STREAM_CODEC, ClearTankPacket.Handler::handleOnServer);
        registrar.playToServer(ClearFilterTankPacket.TYPE, ClearFilterTankPacket.STREAM_CODEC, ClearFilterTankPacket.Handler::handleOnServer);

        registrar.playToServer(ToggleInputTanksSeparatedPacket.TYPE, ToggleInputTanksSeparatedPacket.STREAM_CODEC, ToggleInputTanksSeparatedPacket.Handler::handleOnServer);
        registrar.playToServer(ToggleVoidUnusableFluidInputPacket.TYPE, ToggleVoidUnusableFluidInputPacket.STREAM_CODEC, ToggleVoidUnusableFluidInputPacket.Handler::handleOnServer);
        registrar.playToServer(ToggleVoidExcessFluidOutputPacket.TYPE, ToggleVoidExcessFluidOutputPacket.STREAM_CODEC, ToggleVoidExcessFluidOutputPacket.Handler::handleOnServer);
        registrar.playToServer(ToggleAlternateComparatorPacket.TYPE, ToggleAlternateComparatorPacket.STREAM_CODEC, ToggleAlternateComparatorPacket.Handler::handleOnServer);
        registrar.playToServer(ToggleRedstoneControlPacket.TYPE, ToggleRedstoneControlPacket.STREAM_CODEC, ToggleRedstoneControlPacket.Handler::handleOnServer);

        registrar.playToServer(ToggleItemSorptionPacket.TYPE, ToggleItemSorptionPacket.STREAM_CODEC, ToggleItemSorptionPacket.Handler::handleOnServer);
        registrar.playToServer(ResetItemSorptionsPacket.TYPE, ResetItemSorptionsPacket.STREAM_CODEC, ResetItemSorptionsPacket.Handler::handleOnServer);
        registrar.playToServer(ToggleItemOutputSettingPacket.TYPE, ToggleItemOutputSettingPacket.STREAM_CODEC, ToggleItemOutputSettingPacket.Handler::handleOnServer);
        registrar.playToServer(ToggleTankSorptionPacket.TYPE, ToggleTankSorptionPacket.STREAM_CODEC, ToggleTankSorptionPacket.Handler::handleOnServer);
        registrar.playToServer(ResetTankSorptionsPacket.TYPE, ResetTankSorptionsPacket.STREAM_CODEC, ResetTankSorptionsPacket.Handler::handleOnServer);
        registrar.playToServer(ToggleTankOutputSettingPacket.TYPE, ToggleTankOutputSettingPacket.STREAM_CODEC, ToggleTankOutputSettingPacket.Handler::handleOnServer);

        registrar.playToServer(ClearAllMaterialPacket.TYPE, ClearAllMaterialPacket.STREAM_CODEC, ClearAllMaterialPacket.Handler::handleOnServer);

        // SERVER -> CLIENT

        registrar.playToClient(BlockHighlightUpdatePacket.TYPE, BlockHighlightUpdatePacket.STREAM_CODEC, BlockHighlightUpdatePacket.Handler::handleOnClient);

        registrar.playToClient(EnergyProcessorUpdatePacket.TYPE, EnergyProcessorUpdatePacket.STREAM_CODEC, EnergyProcessorUpdatePacket.Handler::handleOnClient);

//        registrar.playToClient(ElectrolyzerUpdatePacket.TYPE, ElectrolyzerUpdatePacket.class);
//        registrar.playToClient(ElectrolyzerRenderPacket.TYPE, ElectrolyzerRenderPacket.class);
//        registrar.playToClient(DistillerUpdatePacket.TYPE, DistillerUpdatePacket.class);
//        registrar.playToClient(DistillerRenderPacket.TYPE, DistillerRenderPacket.class);
//        registrar.playToClient(InfiltratorUpdatePacket.TYPE, InfiltratorUpdatePacket.class);
//        registrar.playToClient(InfiltratorRenderPacket.TYPE, InfiltratorRenderPacket.class);

        registrar.playToClient(ItemPortUpdatePacket.TYPE, ItemPortUpdatePacket.STREAM_CODEC, ItemPortUpdatePacket.Handler::handleOnClient);
        registrar.playToClient(FluidPortUpdatePacket.TYPE, FluidPortUpdatePacket.STREAM_CODEC, FluidPortUpdatePacket.Handler::handleOnClient);

        registrar.playToClient(FissionIrradiatorUpdatePacket.TYPE, FissionIrradiatorUpdatePacket.STREAM_CODEC, FissionIrradiatorUpdatePacket.Handler::handleOnClient);
        registrar.playToClient(FissionCoolerUpdatePacket.TYPE, FissionCoolerUpdatePacket.STREAM_CODEC, FissionCoolerUpdatePacket.Handler::handleOnClient);
        registrar.playToClient(SolidFissionCellUpdatePacket.TYPE, SolidFissionCellUpdatePacket.STREAM_CODEC, SolidFissionCellUpdatePacket.Handler::handleOnClient);
        registrar.playToClient(SaltFissionVesselUpdatePacket.TYPE, SaltFissionVesselUpdatePacket.STREAM_CODEC, SaltFissionVesselUpdatePacket.Handler::handleOnClient);
        registrar.playToClient(SaltFissionHeaterUpdatePacket.TYPE, SaltFissionHeaterUpdatePacket.STREAM_CODEC, SaltFissionHeaterUpdatePacket.Handler::handleOnClient);

        registrar.playToClient(SolidFissionUpdatePacket.TYPE, SolidFissionUpdatePacket.STREAM_CODEC, SolidFissionUpdatePacket.Handler::handleOnClient);
        registrar.playToClient(SaltFissionUpdatePacket.TYPE, SaltFissionUpdatePacket.STREAM_CODEC, SaltFissionUpdatePacket.Handler::handleOnClient);


        registrar.playToClient(HeatExchangerUpdatePacket.TYPE, HeatExchangerUpdatePacket.STREAM_CODEC, HeatExchangerUpdatePacket.Handler::handleOnClient);
        registrar.playToClient(HeatExchangerRenderPacket.TYPE, HeatExchangerRenderPacket.STREAM_CODEC, HeatExchangerRenderPacket.Handler::handleOnClient);
        registrar.playToClient(CondenserUpdatePacket.TYPE, CondenserUpdatePacket.STREAM_CODEC, CondenserUpdatePacket.Handler::handleOnClient);
        registrar.playToClient(CondenserRenderPacket.TYPE, CondenserRenderPacket.STREAM_CODEC, CondenserRenderPacket.Handler::handleOnClient);

        registrar.playToClient(TurbineUpdatePacket.TYPE, TurbineUpdatePacket.STREAM_CODEC, TurbineUpdatePacket.Handler::handleOnClient);
        registrar.playToClient(TurbineRenderPacket.TYPE, TurbineRenderPacket.STREAM_CODEC, TurbineRenderPacket.Handler::handleOnClient);

//        registrar.playToClient(QuantumComputerQubitRenderPacket.TYPE, QuantumComputerQubitRenderPacket.class);
//
//        registrar.playToClient(PlayerRadsUpdatePacket.TYPE, PlayerRadsUpdatePacket.class);
    }
}