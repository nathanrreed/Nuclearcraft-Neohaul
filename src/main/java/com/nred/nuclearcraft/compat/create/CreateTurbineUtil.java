package com.nred.nuclearcraft.compat.create;

import com.nred.nuclearcraft.block_entity.turbine.TurbineRotorBearingEntity;
import com.nred.nuclearcraft.multiblock.turbine.Turbine;

import java.util.HashSet;
import java.util.Set;

public class CreateTurbineUtil {
    public static boolean distributePower(Turbine turbine) {
        Set<GeneratingKineticTurbineRotorBearingEntity> bearings = getBearingSet(turbine);
        if (bearings.isEmpty()) {
            return false;
        }

        for (GeneratingKineticTurbineRotorBearingEntity bearing : bearings) {
            bearing.addPower(turbine.angVel / bearings.size());
        }

        return true;
    }

    public static Set<GeneratingKineticTurbineRotorBearingEntity> getBearingSet(Turbine turbine) {
        Set<GeneratingKineticTurbineRotorBearingEntity> bearings = new HashSet<>();
        for (TurbineRotorBearingEntity bearing : turbine.getParts(TurbineRotorBearingEntity.class)) {
            if (turbine.getWorld().getBlockEntity(bearing.getBlockPos().relative(turbine.flowDir)) instanceof GeneratingKineticTurbineRotorBearingEntity e) {
                bearings.add(e);
            } else if (turbine.getWorld().getBlockEntity(bearing.getBlockPos().relative(turbine.flowDir.getOpposite())) instanceof GeneratingKineticTurbineRotorBearingEntity e) {
                bearings.add(e);
            }
        }
        return bearings;
    }

    public static void setTurbineOff(Turbine turbine) {
        Set<GeneratingKineticTurbineRotorBearingEntity> bearings = getBearingSet(turbine);
        for (GeneratingKineticTurbineRotorBearingEntity bearing : bearings) {
            bearing.setTurbineOff();
        }
    }
}