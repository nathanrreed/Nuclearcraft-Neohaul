package com.nred.nuclearcraft.block;

import com.nred.nuclearcraft.NuclearcraftNeohaul;
import com.nred.nuclearcraft.block_entity.ITile;
import com.nred.nuclearcraft.multiblock.fisson.FissionPartType;
import com.nred.nuclearcraft.multiblock.fisson.FissionPlacement;
import com.nred.nuclearcraft.multiblock.fisson.molten_salt.FissionCoolantHeaterType;
import com.nred.nuclearcraft.multiblock.fisson.solid.FissionHeatSinkType;
import com.nred.nuclearcraft.multiblock.turbine.*;
import com.nred.nuclearcraft.util.NCMath;
import it.zerono.mods.zerocore.base.multiblock.part.GenericDeviceBlock;
import it.zerono.mods.zerocore.lib.block.multiblock.IMultiblockPartType;
import it.zerono.mods.zerocore.lib.multiblock.IMultiblockController;
import it.zerono.mods.zerocore.lib.multiblock.variant.IMultiblockVariant;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

import static com.nred.nuclearcraft.item.TooltipItem.shiftForDetails;

public class GenericTooltipDeviceBlock<Controller extends IMultiblockController<Controller>, PartType extends IMultiblockPartType> extends GenericDeviceBlock<Controller, PartType> {
    public GenericTooltipDeviceBlock(MultiblockPartProperties<PartType> properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        IMultiblockVariant variant = getMultiblockVariant().orElse(null);
        if (getPartType().getTranslationKey().isEmpty()) {
            return;
        }
        switch (getPartType()) {
            case TurbinePartType partType -> {
                if (tooltipFlag.hasShiftDown()) {
                    if (partType == TurbinePartType.Dynamo) {
                        tooltipComponents.add(Component.literal(TurbinePlacement.TOOLTIP_MAP.get(variant.getName() + "_coil")).withStyle(ChatFormatting.AQUA));
                    } else if (partType == TurbinePartType.DynamoConnector) {
                        tooltipComponents.add(Component.literal(TurbinePlacement.TOOLTIP_MAP.get("connector")).withStyle(ChatFormatting.AQUA));
                    } else {
                        tooltipComponents.add(Component.translatable(NuclearcraftNeohaul.MODID + "." + getPartType().getTranslationKey()).withStyle(ChatFormatting.AQUA));
                    }
                } else {
                    switch (partType) {
                        case Dynamo -> tooltipComponents.add(Component.translatable(NuclearcraftNeohaul.MODID + ".tooltip.turbine_dynamo_coil.conductivity", NCMath.pcDecimalPlaces(((TurbineDynamoCoilType) variant).getConductivity(), 1)).withStyle(ChatFormatting.LIGHT_PURPLE));
                        case RotorBlade -> {
                            tooltipComponents.add(Component.translatable(NuclearcraftNeohaul.MODID + ".tooltip.turbine_rotor_blade_efficiency", NCMath.pcDecimalPlaces(((TurbineRotorBladeType) variant).getEfficiency(), 1)).withStyle(ChatFormatting.LIGHT_PURPLE));
                            tooltipComponents.add(Component.translatable(NuclearcraftNeohaul.MODID + ".tooltip.turbine_rotor_blade_expansion", NCMath.pcDecimalPlaces(((TurbineRotorBladeType) variant).getExpansionCoefficient(), 1)).withStyle(ChatFormatting.GRAY));
                        }
                        case RotorStator -> tooltipComponents.add(Component.translatable(NuclearcraftNeohaul.MODID + ".tooltip.turbine_rotor_stator_expansion", NCMath.pcDecimalPlaces(((TurbineRotorStatorType) variant).getExpansionCoefficient(), 1)).withStyle(ChatFormatting.GRAY));
                        case null, default -> {
                        }
                    }
                    tooltipComponents.add(shiftForDetails);
                }
            }
            case FissionPartType partType -> {
                if (tooltipFlag.hasShiftDown()) {
                    if (partType == FissionPartType.HeatSink) {
                        tooltipComponents.add(Component.literal(FissionPlacement.TOOLTIP_MAP.get(variant.getName() + "_sink")).withStyle(ChatFormatting.AQUA));
                    } else if (partType == FissionPartType.Heater) {
                        tooltipComponents.add(Component.literal(FissionPlacement.TOOLTIP_MAP.get(variant.getName() + "_heater")).withStyle(ChatFormatting.AQUA));
                    }
                } else {
                    switch (partType) {
                        case HeatSink -> tooltipComponents.add(Component.translatable(NuclearcraftNeohaul.MODID + ".tooltip.solid_fission_sink.cooling_rate", ((FissionHeatSinkType) variant).getCoolingRate()).withStyle(ChatFormatting.BLUE));
                        case Heater -> tooltipComponents.add(Component.translatable(NuclearcraftNeohaul.MODID + ".tooltip.salt_fission_heater.cooling_rate", ((FissionCoolantHeaterType) variant).getCoolingRate()).withStyle(ChatFormatting.BLUE));
                        case null, default -> {
                        }
                    }
                    tooltipComponents.add(shiftForDetails);
                }
            }
            default -> {
            }
        }
    }

    @Override
    public void neighborChanged(BlockState state, Level world, BlockPos blockPosition, Block block, BlockPos neighborPosition, boolean isMoving) {
        super.neighborChanged(state, world, blockPosition, block, neighborPosition, isMoving);
        BlockEntity tile = world.getBlockEntity(blockPosition);
        if (tile instanceof ITile t) {
            t.onBlockNeighborChanged(state, tile.getLevel(), blockPosition, neighborPosition);
        }
    }
}