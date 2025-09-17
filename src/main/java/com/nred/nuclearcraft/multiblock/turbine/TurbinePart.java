package com.nred.nuclearcraft.multiblock.turbine;

import it.zerono.mods.zerocore.lib.multiblock.cuboid.AbstractCuboidMultiblockPart;
import it.zerono.mods.zerocore.lib.multiblock.cuboid.PartPosition;
import it.zerono.mods.zerocore.lib.multiblock.validation.IMultiblockValidator;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class TurbinePart extends AbstractCuboidMultiblockPart<Turbine> {//TODO implements IMultiblockPartTypeProvider<Turbine, TurbinePartType>, IMultiblockVariantProvider<IMultiblockTurbineVariant> {

    public TurbinePart(BlockEntityType<?> type, BlockPos position, BlockState blockState) {
        super(type, position, blockState);
    }

    @Override
    public boolean isGoodForPosition(@NotNull PartPosition partPosition, @NotNull IMultiblockValidator iMultiblockValidator) {
        return false;
    }

    @Override
    public Turbine createController() {
        return new Turbine(level);
    }

    @Override
    public Class<Turbine> getControllerType() {
        return null;
    }

    @Override
    public void onMachineActivated() {

    }

    @Override
    public void onMachineDeactivated() {

    }
}