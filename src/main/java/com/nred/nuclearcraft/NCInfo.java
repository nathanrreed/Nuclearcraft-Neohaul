package com.nred.nuclearcraft;

import com.google.common.collect.Lists;
import com.nred.nuclearcraft.recipe.fission.FissionModeratorRecipe;
import com.nred.nuclearcraft.recipe.fission.FissionReflectorRecipe;
import com.nred.nuclearcraft.recipe.fission.SolidFissionRecipe;
import com.nred.nuclearcraft.util.NCMath;
import com.nred.nuclearcraft.util.UnitHelper;
import net.minecraft.network.chat.Component;

import java.util.List;
import java.util.function.Supplier;

import static com.nred.nuclearcraft.NuclearcraftNeohaul.MODID;
import static com.nred.nuclearcraft.config.Config2.fission_decay_mechanics;
import static com.nred.nuclearcraft.config.Config2.fission_neutron_reach;

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

    public static Component rtgInfo(long power) {
        return Component.translatable(MODID + ".tooltip.rtg", UnitHelper.prefix(power, 5, "RF/t"));
    }

    // Solar Panel

    public static Component solarPanelInfo(Supplier<Integer> power) {
        return Component.translatable(MODID + ".tooltip.solar", (Supplier<String>) () -> UnitHelper.prefix(power.get(), 5, "RF/t"));
    }

//	// Battery
//
//	public static String[] batteryInfo() {
//		return InfoHelper.formattedInfo("tile." + Global.MOD_ID + ".energy_storage.desc");
//	}

    // Fission Fuel

    public static Component[] fissionFuelInfo(SolidFissionRecipe recipe) {
        List<Component> list = Lists.newArrayList(Component.translatable(MODID + ".info.fission_fuel"), Component.translatable(MODID + ".info.fission_fuel.base_time", UnitHelper.applyTimeUnit(recipe.getFissionFuelTime(), 3)), Component.translatable(MODID + ".info.fission_fuel.base_heat", UnitHelper.prefix(recipe.getFissionFuelHeat(), 5, "H/t")), Component.translatable(MODID + ".info.fission_fuel.base_efficiency", NCMath.pcDecimalPlaces(recipe.getFissionFuelEfficiency(), 1)), Component.translatable(MODID + ".info.fission_fuel.criticality", recipe.getFissionFuelCriticality() + " N/t"));
        if (fission_decay_mechanics) {
            list.add(Component.translatable(MODID + ".info.fission_fuel.decay_factor", NCMath.pcDecimalPlaces(recipe.getFissionFuelDecayFactor(), 1)));
        }
        if (recipe.getFissionFuelSelfPriming()) {
            list.add(Component.translatable(MODID + ".info.fission_fuel.self_priming"));
        }
        return list.toArray(new Component[0]);
    }

//	// Fission Cooling TODO
//
//	public static <T extends Enum<T> & IStringSerializable & ICoolingComponentEnum<?>> String[][] coolingFixedInfo(T[] values, String name) {
//		String[][] info = new String[values.length][];
//		for (int i = 0; i < values.length; ++i) {
//			info[i] = coolingRateInfo(values[i], name);
//		}
//		return info;
//	}
//
//	public static <T extends Enum<T> & ICoolingComponentEnum<?>> String[] coolingRateInfo(T type, String name) {
//		return coolingRateInfo(type.getCooling(), name);
//	}
//
//	public static String[] coolingRateInfo(int cooling, String name) {
//		return new String[] {Lang.localize("tile." + Global.MOD_ID + "." + name + ".cooling_rate") + " " + cooling + " H/t"};
//	}
//
//	public static String[][] heatSinkFixedInfo() {
//		return coolingFixedInfo(MetaEnums.HeatSinkType.values(), "solid_fission_sink");
//	}
//
//	public static String[][] heatSinkFixedInfo2() {
//		return coolingFixedInfo(MetaEnums.HeatSinkType2.values(), "solid_fission_sink");
//	}
//
//	public static String[][] coolantHeaterFixedInfo() {
//		return coolingFixedInfo(MetaEnums.CoolantHeaterType.values(), "salt_fission_heater");
//	}
//
//	public static String[][] coolantHeaterFixedInfo2() {
//		return coolingFixedInfo(MetaEnums.CoolantHeaterType2.values(), "salt_fission_heater");
//	}
//
//	// Neutron Source
//
//	public static String[][] neutronSourceFixedInfo() {
//		MetaEnums.NeutronSourceType[] values = MetaEnums.NeutronSourceType.values();
//		String[][] info = new String[values.length][];
//		for (int i = 0; i < values.length; ++i) {
//			info[i] = neutronSourceEfficiencyInfo(values[i].getEfficiency());
//		}
//		return info;
//	}
//
//	public static String[] neutronSourceEfficiencyInfo(double efficiency) {
//		return new String[] {Lang.localize("info." + Global.MOD_ID + ".fission_source.efficiency.fixd", NCMath.pcDecimalPlaces(efficiency, 1))};
//	}
//
//	public static String[][] neutronSourceInfo() {
//		MetaEnums.NeutronSourceType[] values = MetaEnums.NeutronSourceType.values();
//		String[][] info = new String[values.length][];
//		for (int i = 0; i < values.length; ++i) {
//			info[i] = neutronSourceDescriptionInfo();
//		}
//		return info;
//	}
//
//	public static String[] neutronSourceDescriptionInfo() {
//		return InfoHelper.formattedInfo(Lang.localize("tile." + Global.MOD_ID + ".fission_source.desc"));
//	}
//
//	// Neutron Shield
//
//	public static String[][] neutronShieldFixedInfo() {
//		MetaEnums.NeutronShieldType[] values = MetaEnums.NeutronShieldType.values();
//		String[][] info = new String[values.length][];
//		for (int i = 0; i < values.length; ++i) {
//			info[i] = neutronShieldStatInfo(values[i].getHeatPerFlux(), values[i].getEfficiency());
//		}
//		return info;
//	}
//
//	public static String[] neutronShieldStatInfo(double heatPerFlux, double efficiency) {
//		return new String[] {Lang.localize("info." + Global.MOD_ID + ".fission_shield.heat_per_flux.fixd", UnitHelper.prefix(heatPerFlux, 5, "H/N")), Lang.localize("info." + Global.MOD_ID + ".fission_shield.efficiency.fixd", NCMath.pcDecimalPlaces(efficiency, 1))};
//	}
//
//	public static String[][] neutronShieldInfo() {
//		MetaEnums.NeutronShieldType[] values = MetaEnums.NeutronShieldType.values();
//		String[][] info = new String[values.length][];
//		for (int i = 0; i < values.length; ++i) {
//			info[i] = neutronShieldDescriptionInfo();
//		}
//		return info;
//	}
//
//	public static String[] neutronShieldDescriptionInfo() {
//		return InfoHelper.formattedInfo(Lang.localize("tile." + Global.MOD_ID + ".fission_shield.desc"));
//	}
//
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

    public static Component[] fissionReflectorInfo() {
        return new Component[]{Component.empty()}; // TODO currently not in original translatable("" + MODID + "info.reflector");
    }

//	// HX Tube
//
//	public static String[] hxTubeFixedInfo(double heatTransferCoefficient, double heatRetentionMult) {
//		return new String[] {Lang.localize("tile." + Global.MOD_ID + ".heat_exchanger_tube_heat_transfer_coefficient.fixd", UnitHelper.prefix(heatTransferCoefficient, 5, "H/t/K")), Lang.localize("tile." + Global.MOD_ID + ".heat_exchanger_tube_heat_retention_mult.fixd", NCMath.pcDecimalPlaces(heatRetentionMult, 1))};
//	}
//
//	public static String[] hxTubeInfo() {
//		return InfoHelper.formattedInfo("tile." + Global.MOD_ID + ".heat_exchanger_tube.desc");
//	}
//
//	// Dynamo Coil
//
//	public static String[][] dynamoCoilFixedInfo() {
//		String[][] info = new String[TurbineDynamoCoilType.values().length][];
//		for (int i = 0; i < TurbineDynamoCoilType.values().length; ++i) {
//			info[i] = coilConductivityInfo(i);
//		}
//		return info;
//	}
//
//	public static String[] coilConductivityInfo(int meta) {
//		return coilConductivityInfo(TurbineDynamoCoilType.values()[meta].getConductivity());
//	}
//
//	public static String[] coilConductivityInfo(double conductivity) {
//		return new String[] {Lang.localize("tile." + Global.MOD_ID + ".turbine_dynamo_coil.conductivity") + " " + NCMath.pcDecimalPlaces(conductivity, 1)};
//	}
//
//	// Rotor Blade
//
//	public static String[] rotorBladeFixedInfo(double efficiency, double expansionCoefficient) {
//		return new String[] {Lang.localize("tile." + Global.MOD_ID + ".turbine_rotor_blade_efficiency.fixd", NCMath.pcDecimalPlaces(efficiency, 1)), Lang.localize("tile." + Global.MOD_ID + ".turbine_rotor_blade_expansion.fixd", NCMath.pcDecimalPlaces(expansionCoefficient, 1))};
//	}
//
//	public static String[] rotorBladeInfo() {
//		return InfoHelper.formattedInfo("tile." + Global.MOD_ID + ".turbine_rotor_blade.desc", UnitHelper.prefix(turbine_mb_per_blade, 5, "B/t", -1));
//	}
//
//	// Rotor Stator
//
//	public static String[] rotorStatorFixedInfo(double expansionCoefficient) {
//		return new String[] {Lang.localize("tile." + Global.MOD_ID + ".turbine_rotor_stator_expansion.fixd", NCMath.pcDecimalPlaces(expansionCoefficient, 1))};
//	}
//
//	public static String[] rotorStatorInfo() {
//		return InfoHelper.formattedInfo("tile." + Global.MOD_ID + ".turbine_rotor_stator.desc");
//	}
//
//	// Speed Upgrade
//
//	public static String[][] upgradeInfo() {
//		String[][] info = new String[MetaEnums.UpgradeType.values().length][];
//		for (int i = 0; i < MetaEnums.UpgradeType.values().length; ++i) {
//			info[i] = InfoHelper.EMPTY_ARRAY;
//		}
//		info[0] = InfoHelper.formattedInfo(Lang.localize("item.nuclearcraft.upgrade.speed_desc", powerAdverb(speed_upgrade_power_laws_fp[0], "increase", "with"), powerAdverb(speed_upgrade_power_laws_fp[1], "increase", "")));
//		info[1] = InfoHelper.formattedInfo(Lang.localize("item.nuclearcraft.upgrade.energy_desc", powerAdverb(energy_upgrade_power_laws_fp[0], "decrease", "with")));
//		return info;
//	}
//
//	public static String powerAdverb(double power, String verb, String preposition) {
//		if (power != (long) power) {
//			verb += "_approximately";
//		}
//		verb = Lang.localize("nc.sf." + verb);
//
//		long p = Math.round(power);
//
//		preposition = "nc.sf." + preposition;
//		return Lang.canLocalize(preposition) ? Lang.localize("nc.sf.power_adverb_preposition", Lang.localize("nc.sf.power_adverb" + p, verb), Lang.localize(preposition)) : Lang.localize("nc.sf.power_adverb" + p, verb);
//	}
//
//	// Rad Shielding
//
//	public static String[][] radShieldingInfo() {
//		String[][] info = new String[MetaEnums.RadShieldingType.values().length][];
//		for (int i = 0; i < MetaEnums.RadShieldingType.values().length; ++i) {
//			info[i] = InfoHelper.formattedInfo(Lang.localize("item.nuclearcraft.rad_shielding.desc" + (radiation_hardcore_containers > 0D ? "_hardcore" : ""), RadiationHelper.resistanceSigFigs(radiation_shielding_level[i])));
//		}
//		return info;
//	}
}
