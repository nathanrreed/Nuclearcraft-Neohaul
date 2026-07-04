package com.nred.nuclearcraft.compat.kubejs.fluid;

import com.nred.nuclearcraft.fluid.NCSourceFluid;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.FlowingFluid;
import net.neoforged.neoforge.fluids.FluidType;

import static com.nred.nuclearcraft.helpers.Location.ncLoc;
import static com.nred.nuclearcraft.info.NCFluid.HOT_GAS_TYPE;

public class FluidHotGasBuilder extends FluidNakBuilder {
    public FluidHotGasBuilder(ResourceLocation i) {
        super(i);
        fluidType.stillTexture(ncLoc("block/fluid/gas"));
        fluidType.flowingTexture(ncLoc("block/fluid/gas"));
        fluidType.fallDistanceModifier(0F);
        fluidType.properties = HOT_GAS_TYPE.properties();

        tickRate(new FluidType(fluidType.properties).getViscosity() / 200);
    }

    @Override
    public FlowingFluid createObject() {
        return new NCSourceFluid(createProperties(), HOT_GAS_TYPE.level());
    }
}