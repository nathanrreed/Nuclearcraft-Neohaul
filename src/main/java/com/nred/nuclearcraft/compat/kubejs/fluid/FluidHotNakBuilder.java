package com.nred.nuclearcraft.compat.kubejs.fluid;

import com.nred.nuclearcraft.fluid.NCSourceFluid;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.FlowingFluid;
import net.neoforged.neoforge.fluids.FluidType;

import static com.nred.nuclearcraft.helpers.Location.ncLoc;
import static com.nred.nuclearcraft.info.Fluids.HOT_COOLANT_TYPE;

public class FluidHotNakBuilder extends FluidNakBuilder {
    public FluidHotNakBuilder(ResourceLocation i) {
        super(i);
        fluidType.stillTexture(ncLoc("block/fluid/molten_still"));
        fluidType.flowingTexture(ncLoc("block/fluid/molten_flow"));
        fluidType.fallDistanceModifier(0F);
        fluidType.properties = HOT_COOLANT_TYPE.properties();

        tickRate(new FluidType(fluidType.properties).getViscosity() / 200);
    }

    @Override
    public FlowingFluid createObject() {
        return new NCSourceFluid(createProperties(), HOT_COOLANT_TYPE.level());
    }
}