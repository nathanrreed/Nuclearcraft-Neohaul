package com.nred.nuclearcraft;

import com.google.common.collect.Lists;
import com.nred.nuclearcraft.multiblock.fisson.FissionNeutronShieldType;
import com.nred.nuclearcraft.multiblock.fisson.FissionSourceType;
import com.nred.nuclearcraft.multiblock.fisson.molten_salt.FissionCoolantHeaterType;
import com.nred.nuclearcraft.multiblock.fisson.solid.FissionHeatSinkType;
import com.nred.nuclearcraft.multiblock.hx.HeatExchangerTubeType;
import com.nred.nuclearcraft.multiblock.turbine.TurbineDynamoCoilType;
import com.nred.nuclearcraft.multiblock.turbine.TurbineRotorBladeType;
import com.nred.nuclearcraft.multiblock.turbine.TurbineRotorStatorType;
import com.nred.nuclearcraft.recipe.fission.FissionModeratorRecipe;
import com.nred.nuclearcraft.recipe.fission.FissionReflectorRecipe;
import com.nred.nuclearcraft.recipe.fission.ItemFissionRecipe;
import com.nred.nuclearcraft.util.Lang;
import com.nred.nuclearcraft.util.NCMath;
import com.nred.nuclearcraft.util.UnitHelper;
import net.minecraft.network.chat.Component;

import java.util.List;
import java.util.function.Supplier;

import static com.nred.nuclearcraft.NuclearcraftNeohaul.MODID;
import static com.nred.nuclearcraft.config.NCConfig.*;

public class NCInfo {
//
//	// Diaphragm
//
//	public static String[] machineDiaphragmFixedInfo(BasicRecipe recipe) {
//		return new String[] {Lang.localize("info." + Global.MOD_ID + ".diaphragm.fixd"), Lang.localize("info." + Global.MOD_ID + ".diaphragm.efficiency.fixd", NCMath.pcDecimalPlaces(recipe.getMachineDiaphragmEfficiency(), 1)), Lang.localize("info." + Global.MOD_ID + ".diaphragm.contact.fixd", NCMath.pcDecimalPlaces(recipe.getMachineDiaphragmContactFactor(), 1))};
//	}
//
//	public static String[] machineDiaphragmInfo() {
//		return InfoHelper.formattedInfo(Lang.localize("info." + Global.MOD_ID + ".diaphragm.desc"));
//	}
//
//	// Sieve Assembly
//
//	public static String[] machineSieveAssemblyFixedInfo(BasicRecipe recipe) {
//		return new String[] {Lang.localize("info." + Global.MOD_ID + ".sieve_assembly.fixd"), Lang.localize("info." + Global.MOD_ID + ".sieve_assembly.efficiency.fixd", NCMath.pcDecimalPlaces(recipe.getMachineSieveAssemblyEfficiency(), 1))};
//	}
//
//	public static String[] machineSieveAssemblyInfo() {
//		return InfoHelper.formattedInfo(Lang.localize("info." + Global.MOD_ID + ".sieve_assembly.desc"));
//	}
//
//	// Electrolyzer Electrode
//
//	public static String[] electrodeFixedInfo(BasicRecipe cathodeRecipe, BasicRecipe anodeRecipe) {
//		boolean anyElectrode = cathodeRecipe != null && anodeRecipe != null;
//		List<String> list = Lists.newArrayList(Lang.localize("info." + Global.MOD_ID + ".electrode." + (anyElectrode ? "fixd" : cathodeRecipe != null ? "cathode.fixd" : "anode.fixd")));
//		if (cathodeRecipe != null) {
//			list.add(Lang.localize("info." + Global.MOD_ID + ".electrode.efficiency." + (anyElectrode ? "cathode.fixd" : "fixd"), NCMath.pcDecimalPlaces(cathodeRecipe.getElectrolyzerElectrodeEfficiency(), 1)));
//		}
//		if (anodeRecipe != null) {
//			list.add(Lang.localize("info." + Global.MOD_ID + ".electrode.efficiency." + (anyElectrode ? "anode.fixd" : "fixd"), NCMath.pcDecimalPlaces(anodeRecipe.getElectrolyzerElectrodeEfficiency(), 1)));
//		}
//		return list.toArray(new String[0]);
//	}
//
//	public static String[] electrodeInfo() {
//		return InfoHelper.formattedInfo(Lang.localize("info." + Global.MOD_ID + ".electrode.desc"));
//	}
//
//	// Infiltrator Pressure Fluid
//
//	public static String[] infiltratorPressureFluidFixedInfo(BasicRecipe recipe) {
//		return new String[] {Lang.localize("info." + Global.MOD_ID + ".infiltrator_pressure_fluid.fixd"), Lang.localize("info." + Global.MOD_ID + ".infiltrator_pressure_fluid.efficiency.fixd", NCMath.pcDecimalPlaces(recipe.getInfiltratorPressureFluidEfficiency(), 1))};
//	}
//
//	public static String[] infiltratorPressureFluidInfo() {
//		return InfoHelper.formattedInfo(Lang.localize("info." + Global.MOD_ID + ".infiltrator_pressure_fluid.desc"));
//	}
//
    // RTG

    public static Component rtgInfo(Supplier<Integer> power) {
        return Component.translatable(MODID + ".tooltip.rtg", (Supplier<String>) () -> UnitHelper.prefix(power.get(), 5, "RF/t"));
    }

    // Solar Panel

    public static Component solarPanelInfo(Supplier<Integer> power) {
        return Component.translatable(MODID + ".tooltip.solar", (Supplier<String>) () -> UnitHelper.prefix(power.get(), 5, "RF/t"));
    }

    // Battery

    public static Component batteryInfo() {
        return Component.translatable(MODID + ".tooltip.energy_storage.desc");
    }

    // Fission Fuel

    public static Component[] fissionFuelInfo(ItemFissionRecipe recipe) {
        List<Component> list = Lists.newArrayList(Component.translatable(MODID + ".info.fission_fuel"), Component.translatable(MODID + ".info.fission_fuel.base_time", UnitHelper.applyTimeUnit(recipe.getFissionFuelTime(), 3)), Component.translatable(MODID + ".info.fission_fuel.base_heat", UnitHelper.prefix(recipe.getFissionFuelHeat(), 5, "H/t")), Component.translatable(MODID + ".info.fission_fuel.base_efficiency", NCMath.pcDecimalPlaces(recipe.getFissionFuelEfficiency(), 1)), Component.translatable(MODID + ".info.fission_fuel.criticality", recipe.getFissionFuelCriticality() + " N/t"));
        if (fission_decay_mechanics) {
            list.add(Component.translatable(MODID + ".info.fission_fuel.decay_factor", NCMath.pcDecimalPlaces(recipe.getFissionFuelDecayFactor(), 1)));
        }
        if (recipe.getFissionFuelSelfPriming()) {
            list.add(Component.translatable(MODID + ".info.fission_fuel.self_priming"));
        }
        return list.toArray(new Component[0]);
    }

    // Fission Cooling

    public static Component[] sinkCoolingRateFixedInfo(FissionHeatSinkType type) {
        return new Component[]{Component.translatable(MODID + ".tooltip.solid_fission_sink.cooling_rate", (Supplier<Integer>) type::getCoolingRate)};
    }

    public static Component[] heaterCoolingRateFixedInfo(FissionCoolantHeaterType type) {
        return new Component[]{Component.translatable(MODID + ".tooltip.salt_fission_heater.cooling_rate", (Supplier<Double>) type::getCoolingRate)};
    }

    // Neutron Source

    public static Component[] neutronSourceFixedInfo(FissionSourceType type) {
        return new Component[]{Component.translatable(MODID + ".tooltip.fission_source.efficiency", (Supplier<String>) () -> NCMath.pcDecimalPlaces(type.getEfficiency(), 1))};
    }

    public static Component neutronSourceInfo() {
        return Component.translatable(MODID + ".tooltip.fission_source"); // TODO not in NCO
    }

    // Neutron Shield

    public static Component[] neutronShieldFixedInfo(FissionNeutronShieldType type) {
        return new Component[]{Component.translatable(MODID + ".tooltip.fission_shield.heat_per_flux", (Supplier<String>) () -> UnitHelper.prefix(type.getHeatPerFlux(), 5, "H/N")), Component.translatable(MODID + ".tooltip.fission_shield.efficiency", (Supplier<String>) () -> NCMath.pcDecimalPlaces(type.getEfficiency(), 1))};
    }


    public static Component neutronShieldInfo() {
        return Component.translatable(MODID + ".tooltip.fission_shield"); // TODO not in NCO
    }

    // Fission Moderator

    public static Component[] fissionModeratorFixedInfo(FissionModeratorRecipe moderatorInfo) {
        return new Component[]{
                Component.translatable(MODID + ".tooltip.moderator.underline"),
                Component.translatable(MODID + ".tooltip.moderator.flux_factor", moderatorInfo.getFissionModeratorFluxFactor() + " N/t"),
                Component.translatable(MODID + ".tooltip.moderator.efficiency", NCMath.pcDecimalPlaces(moderatorInfo.getFissionModeratorEfficiency(), 1))};
    }

    public static Component[] fissionModeratorInfo() {
        return new Component[]{Component.translatable(MODID + ".tooltip.moderator", fission_neutron_reach, fission_neutron_reach / 2)};
    }

    // Fission Reflector

    public static Component[] fissionReflectorFixedInfo(FissionReflectorRecipe reflectorInfo) {
        return new Component[]{Component.translatable(MODID + ".info.reflector"), Component.translatable(MODID + ".info.reflector.reflectivity", NCMath.pcDecimalPlaces(reflectorInfo.getFissionReflectorReflectivity(), 1)), Component.translatable(MODID + ".info.reflector.efficiency", NCMath.pcDecimalPlaces(reflectorInfo.getFissionReflectorEfficiency(), 1))};
    }

    public static Component fissionReflectorInfo() {
        return Component.translatable(MODID + ".tooltip.reflector"); // TODO not in NCO
    }

    // HX Tube

    public static Component[] hxTubeFixedInfo(HeatExchangerTubeType type) {
        return new Component[]{Component.translatable(MODID + ".tooltip.heat_exchanger_tube.heat_transfer_coefficient", (Supplier<String>) () -> UnitHelper.prefix(type.getHeatTransferCoefficient(), 5, "H/t/K")), Component.translatable(MODID + ".tooltip.heat_exchanger_tube.heat_retention_mult", (Supplier<String>) () -> NCMath.pcDecimalPlaces(type.getHeatRetentionMultiplier(), 1))};
    }

    public static Component hxTubeInfo() {
        return Component.translatable("block." + MODID + ".heat_exchanger_tube.desc");
    }

    // Dynamo Coil

    public static Component[] dynamoCoilFixedInfo(TurbineDynamoCoilType type) {
        return new Component[]{Component.translatable(MODID + ".tooltip.turbine_dynamo_coil.conductivity", (Supplier<String>) () -> NCMath.pcDecimalPlaces(type.getConductivity(), 1))};
    }

    // Rotor Blade

    public static Component[] rotorBladeFixedInfo(TurbineRotorBladeType type) {
        return new Component[]{Component.translatable(NuclearcraftNeohaul.MODID + ".tooltip.turbine_rotor_blade_efficiency", (Supplier<String>) () -> NCMath.pcDecimalPlaces(type.getEfficiency(), 1)), Component.translatable(NuclearcraftNeohaul.MODID + ".tooltip.turbine_rotor_blade_expansion", (Supplier<String>) () -> NCMath.pcDecimalPlaces(type.getExpansionCoefficient(), 1))};
    }

    public static Component rotorBladeInfo() {
        return Component.translatable("block." + MODID + ".turbine_rotor_blade.desc", (Supplier<String>) () -> UnitHelper.prefix(turbine_mb_per_blade, 5, "B/t", -1));
    }

    // Rotor Stator

    public static Component[] rotorStatorFixedInfo(TurbineRotorStatorType type) {
        return new Component[]{Component.translatable(NuclearcraftNeohaul.MODID + ".tooltip.turbine_rotor_blade_expansion", (Supplier<String>) () -> NCMath.pcDecimalPlaces(type.getExpansionCoefficient(), 1))};

    }

    public static Component rotorStatorInfo() {
        return Component.translatable("block." + MODID + ".turbine_rotor_stator.desc");
    }

    // Upgrade
    public static String powerAdverb(double power, String verb, String preposition) {
        if (power != (long) power) {
            verb += "_approximately";
        }
        verb = Lang.localize("nc.sf." + verb);

        long p = Math.round(power);

        preposition = "nc.sf." + preposition;
        return Lang.canLocalize(preposition) ? Lang.localize("nc.sf.power_adverb_preposition", Lang.localize("nc.sf.power_adverb" + p, verb), Lang.localize(preposition)) : Lang.localize("nc.sf.power_adverb" + p, verb);
    }

    // Rad Shielding
//
//	public static String[][] radShieldingInfo() {
//		String[][] info = new String[MetaEnums.RadShieldingType.values().length][];
//		for (int i = 0; i < MetaEnums.RadShieldingType.values().length; ++i) {
//			info[i] = InfoHelper.formattedInfo(Lang.localize("item.nuclearcraft.rad_shielding.desc" + (radiation_hardcore_containers > 0D ? "_hardcore" : ""), RadiationHelper.resistanceSigFigs(radiation_shielding_level[i])));
//		}
//		return info;
//	}
}
