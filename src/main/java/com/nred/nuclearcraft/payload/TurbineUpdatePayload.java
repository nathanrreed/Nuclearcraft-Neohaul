//package com.nred.nuclearcraft.payload; TODO DELETE
//
//import com.nred.nuclearcraft.block.turbine.TurbineControllerEntity;
//import com.nred.nuclearcraft.helpers.CustomEnergyHandler;
//import com.nred.nuclearcraft.multiblock.turbine.Turbine;
//import com.nred.nuclearcraft.util.StreamCodecsHelper;
//import net.minecraft.core.BlockPos;
//import net.minecraft.network.RegistryFriendlyByteBuf;
//import net.minecraft.network.codec.ByteBufCodecs;
//import net.minecraft.network.codec.StreamCodec;
//import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
//import net.neoforged.neoforge.network.handling.IPayloadContext;
//
//import java.util.Objects;
//
//import static com.nred.nuclearcraft.helpers.CustomEnergyHandler.ENERGY_HANDLER_STREAM_CODEC;
//import static com.nred.nuclearcraft.helpers.Location.ncLoc;
//
//public record TurbineUpdatePayload(BlockPos pos, boolean isTurbineOn, CustomEnergyHandler energyStorage, double power, double rawPower, double conductivity, double rotorEfficiency, double powerBonus, double totalExpansionLevel,
//                                   double idealTotalExpansionLevel, int shaftWidth, int bladeLength, int noBladeSets, int dynamoCoilCount, int dynamoCoilCountOpposite, double bearingTension) implements CustomPacketPayload {
//    public static final Type<TurbineUpdatePayload> TYPE = new Type<>(ncLoc("turbine_update_server_to_client"));
//    public static final StreamCodec<RegistryFriendlyByteBuf, TurbineUpdatePayload> STREAM_CODEC = StreamCodecsHelper.composite(
//            BlockPos.STREAM_CODEC, TurbineUpdatePayload::pos,
//            ByteBufCodecs.BOOL, TurbineUpdatePayload::isTurbineOn,
//            ENERGY_HANDLER_STREAM_CODEC, TurbineUpdatePayload::energyStorage,
//            ByteBufCodecs.DOUBLE, TurbineUpdatePayload::power,
//            ByteBufCodecs.DOUBLE, TurbineUpdatePayload::rawPower,
//            ByteBufCodecs.DOUBLE, TurbineUpdatePayload::conductivity,
//            ByteBufCodecs.DOUBLE, TurbineUpdatePayload::rotorEfficiency,
//            ByteBufCodecs.DOUBLE, TurbineUpdatePayload::powerBonus,
//            ByteBufCodecs.DOUBLE, TurbineUpdatePayload::totalExpansionLevel,
//            ByteBufCodecs.DOUBLE, TurbineUpdatePayload::idealTotalExpansionLevel,
//            ByteBufCodecs.INT, TurbineUpdatePayload::shaftWidth,
//            ByteBufCodecs.INT, TurbineUpdatePayload::bladeLength,
//            ByteBufCodecs.INT, TurbineUpdatePayload::noBladeSets,
//            ByteBufCodecs.INT, TurbineUpdatePayload::dynamoCoilCount,
//            ByteBufCodecs.INT, TurbineUpdatePayload::dynamoCoilCountOpposite,
//            ByteBufCodecs.DOUBLE, TurbineUpdatePayload::bearingTension,
//            TurbineUpdatePayload::new);
//
//    @Override
//    public Type<TurbineUpdatePayload> type() {
//        return TYPE;
//    }
//
//    public static void handleOnClient(final TurbineUpdatePayload payload, final IPayloadContext context) {
//        Turbine turbine = ((TurbineControllerEntity) Objects.requireNonNull(context.player().level().getBlockEntity(payload.pos))).getMultiblockController().orElse(null);
//        if (turbine != null) {
//            turbine.isTurbineOn = payload.isTurbineOn;
//            turbine.energyStorage.copy(payload.energyStorage);
//            turbine.power = payload.power;
//            turbine.rawPower = payload.rawPower;
//            turbine.conductivity = payload.conductivity;
//            turbine.rotorEfficiency = payload.rotorEfficiency;
//            turbine.powerBonus = payload.powerBonus;
//            turbine.totalExpansionLevel = payload.totalExpansionLevel;
//            turbine.idealTotalExpansionLevel = payload.idealTotalExpansionLevel;
//            turbine.shaftWidth = payload.shaftWidth;
//            turbine.bladeLength = payload.bladeLength;
//            turbine.noBladeSets = payload.noBladeSets;
//            turbine.dynamoCoilCount = payload.dynamoCoilCount;
//            turbine.dynamoCoilCountOpposite = payload.dynamoCoilCountOpposite;
//            turbine.bearingTension = payload.bearingTension;
//        }
//    }
//}