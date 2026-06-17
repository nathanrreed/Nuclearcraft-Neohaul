package com.nred.nuclearcraft.compat.kubejs;

import com.nred.nuclearcraft.compat.kubejs.custom_processor.CustomProcessorBuilder;
import com.nred.nuclearcraft.compat.kubejs.custom_processor.CustomProcessorBuilder.NCSlots;
import com.nred.nuclearcraft.compat.kubejs.fission.*;
import com.nred.nuclearcraft.compat.kubejs.fluid.FluidGasBuilder;
import com.nred.nuclearcraft.compat.kubejs.fluid.FluidHotGasBuilder;
import com.nred.nuclearcraft.compat.kubejs.fluid.FluidHotNakBuilder;
import com.nred.nuclearcraft.compat.kubejs.fluid.FluidNakBuilder;
import com.nred.nuclearcraft.compat.kubejs.other.BatteryBuilder;
import com.nred.nuclearcraft.compat.kubejs.other.HXTubeBuilder;
import com.nred.nuclearcraft.compat.kubejs.other.RTGBuilder;
import com.nred.nuclearcraft.compat.kubejs.turbine.DynamoCoilBuilder;
import com.nred.nuclearcraft.compat.kubejs.turbine.RotorBladeBuilder;
import com.nred.nuclearcraft.compat.kubejs.turbine.RotorStatorBuilder;
import dev.latvian.mods.kubejs.plugin.KubeJSPlugin;
import dev.latvian.mods.kubejs.registry.BuilderTypeRegistry;
import dev.latvian.mods.kubejs.script.BindingRegistry;
import net.minecraft.core.registries.Registries;

import static com.nred.nuclearcraft.helpers.Location.ncLoc;

public class ModKubeJSPlugin implements KubeJSPlugin {

    @Override
    public void registerBuilderTypes(BuilderTypeRegistry registry) {
        registry.of(Registries.BLOCK, reg -> {
            reg.add(ncLoc("rotor_blade"), RotorBladeBuilder.class, RotorBladeBuilder::new);
            reg.add(ncLoc("rotor_stator"), RotorStatorBuilder.class, RotorStatorBuilder::new);
            reg.add(ncLoc("dynamo_coil"), DynamoCoilBuilder.class, DynamoCoilBuilder::new);

            reg.add(ncLoc("fission_source"), FissionSourceBuilder.class, FissionSourceBuilder::new);
            reg.add(ncLoc("fission_shield"), FissionShieldBuilder.class, FissionShieldBuilder::new);
            reg.add(ncLoc("fission_heat_sink"), FissionHeatSinkBuilder.class, FissionHeatSinkBuilder::new);
            reg.add(ncLoc("fission_coolant_heater_and_port"), FissionCoolantHeaterAndPortBuilder.class, FissionCoolantHeaterAndPortBuilder::new);
            reg.add(ncLoc("fission_cooler_and_port"), FissionCoolerAndPortBuilder.class, FissionCoolerAndPortBuilder::new);

            reg.add(ncLoc("rtg"), RTGBuilder.class, RTGBuilder::new);
            reg.add(ncLoc("hx_tube"), HXTubeBuilder.class, HXTubeBuilder::new);
            reg.add(ncLoc("battery"), BatteryBuilder.class, BatteryBuilder::new);

            reg.add(ncLoc("processor"), CustomProcessorBuilder.class, i -> new CustomProcessorBuilder(i, false));
            reg.add(ncLoc("upgradable_processor"), CustomProcessorBuilder.class, i -> new CustomProcessorBuilder(i, true));
        });

        registry.of(Registries.FLUID, reg -> {
            reg.add(ncLoc("nak"), FluidNakBuilder.class, FluidNakBuilder::new);
            reg.add(ncLoc("hot_nak"), FluidHotNakBuilder.class, FluidHotNakBuilder::new);

            reg.add(ncLoc("gas"), FluidGasBuilder.class, FluidGasBuilder::new);
            reg.add(ncLoc("hot_gas"), FluidHotGasBuilder.class, FluidHotGasBuilder::new);
        });

        // TODO add Fission Fuels
        registry.of(Registries.ITEM, reg -> {
        });
    }

    @Override
    public void registerBindings(BindingRegistry bindings) {
        bindings.add("NCSlots", NCSlots.class);
    }
}