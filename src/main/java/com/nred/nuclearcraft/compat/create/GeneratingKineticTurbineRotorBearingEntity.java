package com.nred.nuclearcraft.compat.create;

import com.nred.nuclearcraft.block_entity.turbine.TurbineRotorBearingEntity;
import com.nred.nuclearcraft.multiblock.turbine.Turbine;
import com.simibubi.create.content.kinetics.base.DirectionalKineticBlock;
import com.simibubi.create.content.kinetics.base.GeneratingKineticBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import static com.nred.nuclearcraft.config.NCServerConfig.create_bearing_ang_vel_to_speed;

public class GeneratingKineticTurbineRotorBearingEntity extends GeneratingKineticBlockEntity {
    float lastSpeed = 0;
    double averagePower = 0;
    int count = 0;

    public GeneratingKineticTurbineRotorBearingEntity(BlockEntityType<?> type, final BlockPos position, final BlockState blockState) {
        super(type, position, blockState);
        setLazyTickRate(20);
    }

    @Override
    public void lazyTick() {
        super.lazyTick();
        updateGeneratedRotation();
    }

    public void addPower(double power) {
        averagePower += power;
        count++;
    }

    public void setTurbineOff() {
        averagePower = 0;
        count = 0;
    }

    @Override
    public float getGeneratedSpeed() {
        Turbine turbine = getTurbine();
        if (turbine == null || turbine.flowDir == null || !turbine.isTurbineOn)
            return 0;

        float speed;
        if (count < 19) {
            speed = lastSpeed;
        } else {
            speed = (float) ((averagePower / count) * create_bearing_ang_vel_to_speed);
            lastSpeed = speed;
            averagePower = 0;
            count = 0;
        }
        return convertToDirection(speed, turbine.flowDir == getBlockState().getValue(DirectionalKineticBlock.FACING) ? turbine.flowDir : turbine.flowDir.getOpposite());
    }

    public Turbine getTurbine() {
        Direction facing = getBlockState().getValue(DirectionalKineticBlock.FACING);
        if (level.getBlockEntity(worldPosition.relative(facing.getOpposite())) instanceof TurbineRotorBearingEntity bearing) {
            return bearing.getMultiblockController().orElse(null);
        }
        return null;
    }
}