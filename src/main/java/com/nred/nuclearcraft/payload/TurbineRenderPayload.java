//package com.nred.nuclearcraft.payload; TODO DELETE
//
//import com.nred.nuclearcraft.block.turbine.TurbineControllerEntity;
//import com.nred.nuclearcraft.multiblock.turbine.Turbine;
//import com.nred.nuclearcraft.util.StreamCodecsHelper;
//import net.minecraft.core.BlockPos;
//import net.minecraft.core.particles.ParticleOptions;
//import net.minecraft.core.particles.ParticleTypes;
//import net.minecraft.network.RegistryFriendlyByteBuf;
//import net.minecraft.network.codec.ByteBufCodecs;
//import net.minecraft.network.codec.StreamCodec;
//import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
//import net.neoforged.neoforge.fluids.FluidStack;
//import net.neoforged.neoforge.network.handling.IPayloadContext;
//
//import java.util.List;
//
//import static com.nred.nuclearcraft.helpers.Location.ncLoc;
//
//public record TurbineRenderPayload(BlockPos pos, List<FluidStack> tanks, ParticleOptions particleEffect, double particleSpeedMult, float angVel, boolean isProcessing, int recipeInputRate, double recipeInputRateFP) implements CustomPacketPayload {
//    public static final Type<TurbineRenderPayload> TYPE = new Type<>(ncLoc("turbine_render_server_to_client"));
//    public static final StreamCodec<RegistryFriendlyByteBuf, TurbineRenderPayload> STREAM_CODEC = StreamCodecsHelper.composite(
//            BlockPos.STREAM_CODEC, TurbineRenderPayload::pos,
//            StreamCodecsHelper.FLUID_STACK_LIST_STREAM_CODEC, TurbineRenderPayload::tanks,
//            ParticleTypes.STREAM_CODEC, TurbineRenderPayload::particleEffect,
//            ByteBufCodecs.DOUBLE, TurbineRenderPayload::particleSpeedMult,
//            ByteBufCodecs.FLOAT, TurbineRenderPayload::angVel,
//            ByteBufCodecs.BOOL, TurbineRenderPayload::isProcessing,
//            ByteBufCodecs.INT, TurbineRenderPayload::recipeInputRate,
//            ByteBufCodecs.DOUBLE, TurbineRenderPayload::recipeInputRateFP,
//            TurbineRenderPayload::new);
//
//    @Override
//    public Type<TurbineRenderPayload> type() {
//        return TYPE;
//    }
//
//    public static void handleOnClient(final TurbineRenderPayload payload, final IPayloadContext context) {
//        TurbineControllerEntity controllerEntity = (TurbineControllerEntity) context.player().level().getBlockEntity(payload.pos);
//        if (controllerEntity == null) return;
//        Turbine turbine = controllerEntity.getMultiblockController().orElse(null);
//        if (turbine != null) {
//            turbine.setTanks(payload.tanks);
//            turbine.particleEffect = payload.particleEffect;
//            turbine.particleSpeedMult = payload.particleSpeedMult;
//            turbine.angVel = payload.angVel;
//            boolean wasProcessing = turbine.isProcessing;
//            turbine.isProcessing = payload.isProcessing;
//            if (wasProcessing != turbine.isProcessing) {
//                turbine.refreshSounds = true;
//            }
//            turbine.recipeInputRate = payload.recipeInputRate;
//            turbine.recipeInputRateFP = payload.recipeInputRateFP;
//        }
//    }
//}