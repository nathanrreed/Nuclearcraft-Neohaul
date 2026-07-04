package com.nred.nuclearcraft.info;

import com.nred.nuclearcraft.block.fluid.NCFluidBlock;
import com.nred.nuclearcraft.fluid.NCSourceFluid;
import com.nred.nuclearcraft.info.NCFluid.TypeInfo;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.BiFunction;
import java.util.function.Function;

public record NCFluidMaker(DeferredRegister<FluidType> fluidTypes, DeferredRegister<Fluid> fluids, DeferredRegister.Blocks blocks, DeferredRegister.Items items) {
    public NCFluid registerFluid(String name, boolean same, int tint, BiFunction<FlowingFluid, BlockBehaviour.Properties, ? extends NCFluidBlock> block) {
        return registerFluid(name, same, tint, NCSourceFluid::new, BaseFlowingFluid.Flowing::new, block);
    }

    public NCFluid registerFluid(String name, boolean same, int tint, BiFunction<BaseFlowingFluid.Properties, Integer, NCSourceFluid> blockSource, Function<BaseFlowingFluid.Properties, ? extends BaseFlowingFluid> blockFlowing, BiFunction<FlowingFluid, BlockBehaviour.Properties, ? extends NCFluidBlock> block) {
        return registerFluid(name, tint, same, false, blockSource, blockFlowing, block);
    }

    public NCFluid registerFluid(String name, int tint, boolean same, boolean gaseous, BiFunction<FlowingFluid, BlockBehaviour.Properties, ? extends NCFluidBlock> block) {
        return registerFluid(name, tint, same, gaseous, NCSourceFluid::new, BaseFlowingFluid.Flowing::new, block);
    }

    public NCFluid registerFluid(String name, int tint, boolean same, boolean gaseous, BiFunction<BaseFlowingFluid.Properties, Integer, NCSourceFluid> blockSource, Function<BaseFlowingFluid.Properties, ? extends BaseFlowingFluid> blockFlowing, BiFunction<FlowingFluid, BlockBehaviour.Properties, ? extends NCFluidBlock> block) {
        return registerFluid(name, name, tint, same, gaseous, blockSource, blockFlowing, block);
    }

    public NCFluid registerFluid(String name, String file, int tint, boolean same, boolean gaseous, BiFunction<FlowingFluid, BlockBehaviour.Properties, ? extends NCFluidBlock> block) {
        return registerFluid(name, file, tint, same, gaseous, NCSourceFluid::new, BaseFlowingFluid.Flowing::new, block);
    }

    public NCFluid registerFluid(String name, String file, int tint, boolean same, boolean gaseous, BiFunction<BaseFlowingFluid.Properties, Integer, NCSourceFluid> blockSource, Function<BaseFlowingFluid.Properties, ? extends BaseFlowingFluid> blockFlowing, BiFunction<FlowingFluid, BlockBehaviour.Properties, ? extends NCFluidBlock> block) {
        return new NCFluid(this, name, tint, new TypeInfo(8, gaseous, file + (same ? "" : "_still"), file + (same ? "" : "_flow"), FluidType.Properties.create(), block), blockSource, blockFlowing);
    }

    public NCFluid registerFluid(String name, boolean same, int tint, int density, int temperature, int viscosity, int light_level, BiFunction<FlowingFluid, BlockBehaviour.Properties, ? extends NCFluidBlock> block) {
        return registerFluid(name, same, false, tint, density, viscosity, temperature, light_level, NCSourceFluid::new, BaseFlowingFluid.Flowing::new, block);
    }

    public NCFluid registerFluid(String name, boolean same, int tint, int density, int temperature, int viscosity, int light_level, BiFunction<BaseFlowingFluid.Properties, Integer, NCSourceFluid> blockSource, Function<BaseFlowingFluid.Properties, ? extends BaseFlowingFluid> blockFlowing, BiFunction<FlowingFluid, BlockBehaviour.Properties, ? extends NCFluidBlock> block) {
        return registerFluid(name, same, false, tint, density, viscosity, temperature, light_level, blockSource, blockFlowing, block);
    }

    public NCFluid registerFluid(String name, boolean same, boolean gaseous, int tint, int density, int temperature, int viscosity, int light_level, BiFunction<FlowingFluid, BlockBehaviour.Properties, ? extends NCFluidBlock> block) {
        return registerFluid(name, same, gaseous, tint, density, temperature, viscosity, light_level, NCSourceFluid::new, BaseFlowingFluid.Flowing::new, block);
    }

    public NCFluid registerFluid(String name, boolean same, boolean gaseous, int tint, int density, int temperature, int viscosity, int light_level, BiFunction<BaseFlowingFluid.Properties, Integer, NCSourceFluid> blockSource, Function<BaseFlowingFluid.Properties, ? extends BaseFlowingFluid> blockFlowing, BiFunction<FlowingFluid, BlockBehaviour.Properties, ? extends NCFluidBlock> block) {
        return registerFluid(name, name, same, gaseous, tint, density, viscosity, temperature, light_level, blockSource, blockFlowing, block);
    }

    public NCFluid registerFluid(String name, String file, boolean same, boolean gaseous, int tint, int density, int temperature, int viscosity, int light_level, BiFunction<FlowingFluid, BlockBehaviour.Properties, ? extends NCFluidBlock> block) {
        return registerFluid(name, file, same, gaseous, tint, density, temperature, viscosity, light_level, NCSourceFluid::new, BaseFlowingFluid.Flowing::new, block);
    }

    public NCFluid registerFluid(String name, String file, boolean same, boolean gaseous, int tint, int density, int temperature, int viscosity, int light_level, BiFunction<BaseFlowingFluid.Properties, Integer, NCSourceFluid> blockSource, Function<BaseFlowingFluid.Properties, ? extends BaseFlowingFluid> blockFlowing, BiFunction<FlowingFluid, BlockBehaviour.Properties, ? extends NCFluidBlock> block) {
        return new NCFluid(this, name, tint, new TypeInfo(8, gaseous, file + (same ? "" : "_still"), file + (same ? "" : "_flow"), FluidType.Properties.create().density(density).viscosity(viscosity).temperature(temperature).lightLevel(light_level), block), blockSource, blockFlowing);
    }

    public NCFluid registerFluid(String name, int tint, TypeInfo type, int temperature) {
        return registerFluid(name, tint, type, temperature, NCSourceFluid::new, BaseFlowingFluid.Flowing::new);
    }

    public NCFluid registerFluid(String name, int tint, TypeInfo type, int temperature, BiFunction<BaseFlowingFluid.Properties, Integer, NCSourceFluid> blockSource, Function<BaseFlowingFluid.Properties, ? extends BaseFlowingFluid> blockFlowing) {
        return new NCFluid(this, name, tint, new TypeInfo(type.level(), type.gaseous(), type.still(), type.flowing(), type.properties().temperature(temperature), type.block()), blockSource, blockFlowing);
    }

    public NCFluid registerFluid(String name, int tint, TypeInfo type) {
        return new NCFluid(this, name, tint, type, NCSourceFluid::new, BaseFlowingFluid.Flowing::new);
    }
}