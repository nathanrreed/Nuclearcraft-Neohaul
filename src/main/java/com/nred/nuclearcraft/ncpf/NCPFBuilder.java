package com.nred.nuclearcraft.ncpf;

import com.nred.nuclearcraft.block.GenericTooltipDeviceBlock;
import com.nred.nuclearcraft.block.fission.FissionControllerBlock;
import com.nred.nuclearcraft.block.fission.FissionShieldBlock;
import com.nred.nuclearcraft.block.fission.FissionSourceBlock;
import com.nred.nuclearcraft.block.fission.FissionVentBlock;
import com.nred.nuclearcraft.block.fission.port.FissionFluidPortBlock;
import com.nred.nuclearcraft.block.fission.port.FissionIrradiatorPortBlock;
import com.nred.nuclearcraft.block.fission.port.FissionItemPortBlock;
import com.nred.nuclearcraft.block.turbine.TurbineControllerBlock;
import com.nred.nuclearcraft.block.turbine.TurbineDynamoCoilBlock;
import com.nred.nuclearcraft.block.turbine.TurbineRotorBladeBlock;
import com.nred.nuclearcraft.block.turbine.TurbineRotorStatorBlock;
import com.nred.nuclearcraft.handler.BasicRecipeHandler;
import com.nred.nuclearcraft.handler.CoolantHeaterRecipes;
import com.nred.nuclearcraft.handler.SizedChanceFluidIngredient;
import com.nred.nuclearcraft.handler.SizedChanceItemIngredient;
import com.nred.nuclearcraft.multiblock.Multiblock;
import com.nred.nuclearcraft.multiblock.PlacementRule;
import com.nred.nuclearcraft.multiblock.fisson.FissionNeutronShieldType;
import com.nred.nuclearcraft.multiblock.fisson.FissionPlacement;
import com.nred.nuclearcraft.multiblock.fisson.FissionSourceType;
import com.nred.nuclearcraft.multiblock.fisson.molten_salt.FissionCoolantHeaterType;
import com.nred.nuclearcraft.multiblock.fisson.solid.FissionHeatSinkType;
import com.nred.nuclearcraft.multiblock.turbine.TurbineDynamoCoilType;
import com.nred.nuclearcraft.multiblock.turbine.TurbinePlacement;
import com.nred.nuclearcraft.multiblock.turbine.TurbineRotorBladeType;
import com.nred.nuclearcraft.multiblock.turbine.TurbineRotorStatorType;
import com.nred.nuclearcraft.ncpf.element.*;
import com.nred.nuclearcraft.ncpf.module.NCPFEmptyModule;
import com.nred.nuclearcraft.ncpf.value.NCPFPlacementRule;
import com.nred.nuclearcraft.ncpf.value.NCPFPlacementRuleType;
import com.nred.nuclearcraft.recipe.BasicRecipe;
import com.nred.nuclearcraft.recipe.NCRecipes;
import com.nred.nuclearcraft.recipe.fission.*;
import com.nred.nuclearcraft.recipe.turbine.TurbineRecipe;
import it.zerono.mods.zerocore.lib.multiblock.AbstractMultiblockPart;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.crafting.CompoundIngredient;
import net.neoforged.neoforge.fluids.crafting.CompoundFluidIngredient;
import net.neoforged.neoforge.fluids.crafting.FluidIngredient;
import net.neoforged.neoforge.fluids.crafting.SingleFluidIngredient;
import net.neoforged.neoforge.fluids.crafting.TagFluidIngredient;

import java.util.*;
import java.util.function.Predicate;

import static com.nred.nuclearcraft.registration.BlockEntityRegistration.FISSION_ENTITY_TYPE;
import static com.nred.nuclearcraft.registration.BlockEntityRegistration.TURBINE_ENTITY_TYPE;
import static com.nred.nuclearcraft.registration.BlockRegistration.FISSION_REACTOR_MAP;
import static com.nred.nuclearcraft.registration.BlockRegistration.TURBINE_MAP;
import static com.nred.nuclearcraft.registration.DataMapTypeRegistration.FISSION_MODERATOR_DATA;
import static com.nred.nuclearcraft.registration.DataMapTypeRegistration.FISSION_REFLECTOR_DATA;

public class NCPFBuilder {
    public static String configContext = "unknown";

    public static void translate(List<NCPFElement> list, Block... blocks) {
        for (Block block : blocks) {
            translate(list, block);
        }
    }

    public static void translate(List<NCPFElement> list, Block block) {
        translate(list, block, true);
    }

    public static void translate(List<NCPFElement> list, Block block, boolean includeModules) {
        List<NCPFElement> newElements = new ArrayList<>();

        if (block instanceof GenericTooltipDeviceBlock<?, ?> mBlock && mBlock.getMultiblockVariant().isPresent()) {
            NCPFBlock ncpfBlock = new NCPFBlock();
            ncpfBlock.name = BuiltInRegistries.BLOCK.getKey(block).toString();
            newElements.add(ncpfBlock);

            if (block instanceof FissionShieldBlock) {
                ncpfBlock.blockstate.put("active", false);

                NCPFBlock closedShield = new NCPFBlock();
                closedShield.name = BuiltInRegistries.BLOCK.getKey(block).toString();
                closedShield.blockstate.put("active", true);
                newElements.add(closedShield);
            }

            if (block instanceof FissionFluidPortBlock) {
                ncpfBlock.blockstate.put("active", false);

                NCPFBlock outputPort = new NCPFBlock();
                outputPort.name = BuiltInRegistries.BLOCK.getKey(block).toString();
                outputPort.blockstate.put("active", true);
                newElements.add(outputPort);
            }
        } else {
            NCPFBlock legacyBlock = new NCPFBlock();

            legacyBlock.name = BuiltInRegistries.BLOCK.getKey(block).toString();
            newElements.add(legacyBlock);

            if (block instanceof FissionVentBlock || block instanceof FissionItemPortBlock || block instanceof FissionFluidPortBlock || block instanceof FissionShieldBlock) {
                legacyBlock.blockstate.put("active", false);

                NCPFBlock outputBlock = new NCPFBlock();
                outputBlock.name = legacyBlock.name;
                outputBlock.blockstate.put("active", true);
                newElements.add(outputBlock);
            }
        }

        list.addAll(newElements);

        if (!includeModules) {
            return;
        }

        // Modules and Blockstates

        for (NCPFElement elem : newElements) {
            elem.modules = new HashMap<>();
            Map<String, Object> blockstate = null;
            boolean wall = false;

            if (elem instanceof NCPFBlock ncpfBlock) {
                blockstate = ncpfBlock.blockstate;
            }

            // Fission

            if (block instanceof FissionControllerBlock || block instanceof TurbineControllerBlock) {
                elem.modules.put("nuclearcraft:" + configContext + ":controller", new NCPFEmptyModule());
                wall = true;
            }

            if (block == FISSION_REACTOR_MAP.get("fission_vent").get()) {
                Map<String, Object> vent = new HashMap<>();
                vent.put("output", blockstate.get("active"));
                elem.modules.put("nuclearcraft:" + configContext + ":coolant_vent", vent);
                wall = true;
            }

            if (block instanceof FissionSourceBlock sourceBlock) {
                Map<String, Object> source = new HashMap<>();
                source.put("efficiency", ((FissionSourceType) sourceBlock.getMultiblockVariant().get()).getEfficiency());
                elem.modules.put("nuclearcraft:" + configContext + ":neutron_source", source);
                wall = true;
            }

            if (block instanceof GenericTooltipDeviceBlock<?, ?> mBlock && mBlock.getMultiblockVariant().orElse(null) instanceof FissionHeatSinkType type) {
                Map<String, Object> sink = new HashMap<>();
                sink.put("cooling", type.getCoolingRate());
                elem.modules.put("nuclearcraft:" + configContext + ":heat_sink", sink);
                translatePlacementRules(sink, type.getRule());
            }

            if (block instanceof GenericTooltipDeviceBlock<?, ?> mBlock && mBlock.getMultiblockVariant().orElse(null) instanceof FissionCoolantHeaterType type) {
                Map<String, Object> heaterModule = new HashMap<>();
                elem.modules.put("nuclearcraft:" + configContext + ":heater", heaterModule);

                Map<String, Object> ports = new HashMap<>();
                List<NCPFElement> portElements = new ArrayList<>();

                translate(portElements, getBlockFromType(type.getName(), FISSION_ENTITY_TYPE.get("coolant_heater_port").getId()));
                ports.put("input", portElements.get(0));
                ports.put("output", portElements.get(1));
                elem.modules.put("nuclearcraft:overhaul_msr:recipe_ports", ports);

                Map<String, Object> recipesModule = new HashMap<>();
                List<NCPFElement> recipes = new ArrayList<>();
                translate(recipes, NCRecipes.coolant_heater, (recipe) -> ((BlockItem) recipe.getHeater().getItem()).getBlock() == block);
                recipesModule.put("recipes", recipes);
                elem.modules.put("ncpf:block_recipes", recipesModule);
                translatePlacementRules(heaterModule, type.getRule());
            }

            if (block == FISSION_REACTOR_MAP.get("fission_fuel_cell").get()) {
                elem.modules.put("nuclearcraft:" + configContext + ":fuel_cell", new NCPFEmptyModule());

                Map<String, Object> ports = new HashMap<>();
                List<NCPFElement> portElements = new ArrayList<>();
                translate(portElements, FISSION_REACTOR_MAP.get("fission_fuel_cell_port").get());
                ports.put("input", portElements.get(0));
                ports.put("output", portElements.get(1));
                elem.modules.put("nuclearcraft:overhaul_sfr:recipe_ports", ports);

                Map<String, Object> recipesModule = new HashMap<>();
                List<NCPFElement> recipes = new ArrayList<>();
                translate(recipes, NCRecipes.solid_fission);
                recipesModule.put("recipes", recipes);
                elem.modules.put("ncpf:block_recipes", recipesModule);
            }

            if (block == FISSION_REACTOR_MAP.get("fission_fuel_vessel").get()) {
                elem.modules.put("nuclearcraft:" + configContext + ":fuel_vessel", new NCPFEmptyModule());

                Map<String, Object> ports = new HashMap<>();
                List<NCPFElement> portElements = new ArrayList<>();
                translate(portElements, FISSION_REACTOR_MAP.get("fission_fuel_vessel_port").get());
                ports.put("input", portElements.get(0));
                ports.put("output", portElements.get(1));
                elem.modules.put("nuclearcraft:overhaul_msr:recipe_ports", ports);

                Map<String, Object> recipesModule = new HashMap<>();
                List<NCPFElement> recipes = new ArrayList<>();
                translate(recipes, NCRecipes.salt_fission);
                recipesModule.put("recipes", recipes);
                elem.modules.put("ncpf:block_recipes", recipesModule);
            }

            if (block == FISSION_REACTOR_MAP.get("fission_irradiator").get()) {
                elem.modules.put("nuclearcraft:" + configContext + ":irradiator", new NCPFEmptyModule());

                Map<String, Object> ports = new HashMap<>();
                List<NCPFElement> portElements = new ArrayList<>();
                translate(portElements, FISSION_REACTOR_MAP.get("fission_irradiator_port").get());
                ports.put("input", portElements.get(0));
                ports.put("output", portElements.get(1));
                elem.modules.put("nuclearcraft:overhaul_sfr:recipe_ports", ports);

                Map<String, Object> recipesModule = new HashMap<>();
                List<NCPFElement> recipes = new ArrayList<>();
                translate(recipes, NCRecipes.fission_irradiator);
                recipesModule.put("recipes", recipes);
                elem.modules.put("ncpf:block_recipes", recipesModule);
            }

            if (block == FISSION_REACTOR_MAP.get("fission_fuel_cell_port").get() || block == FISSION_REACTOR_MAP.get("fission_irradiator_port").get() || block == FISSION_REACTOR_MAP.get("fission_fuel_vessel_port").get() || block instanceof FissionIrradiatorPortBlock) {
                Map<String, Object> port = new HashMap<>();
                port.put("output", blockstate.get("active"));
                elem.modules.put("nuclearcraft:" + configContext + ":port", port);
            }

            if (block == FISSION_REACTOR_MAP.get("fission_conductor").get()) {
                elem.modules.put("nuclearcraft:" + configContext + ":conductor", new NCPFEmptyModule());
            }

            if (block instanceof FissionShieldBlock shieldBlock && shieldBlock.getMultiblockVariant().orElse(null) instanceof FissionNeutronShieldType type) {
                if (Objects.equals(blockstate.get("active"), Boolean.FALSE)) {
                    Map<String, Object> shield = new HashMap<>();
                    shield.put("heat_per_flux", type.getHeatPerFlux());
                    shield.put("efficiency", type.getEfficiency());
                    shield.put("closed", newElements.get(1));
                    elem.modules.put("nuclearcraft:" + configContext + ":neutron_shield", shield);
                }
            }

            // Turbine

            if (block == TURBINE_MAP.get("turbine_inlet").get()) {
                elem.modules.put("nuclearcraft:" + configContext + ":inlet", new NCPFEmptyModule());
            }

            if (block == TURBINE_MAP.get("turbine_outlet").get()) {
                elem.modules.put("nuclearcraft:" + configContext + ":outlet", new NCPFEmptyModule());
            }

            if (block instanceof TurbineRotorBladeBlock bladeBlock && bladeBlock.getMultiblockVariant().orElse(null) instanceof TurbineRotorBladeType type) {
                Map<String, Object> blade = new HashMap<>();
                blade.put("efficiency", type.getEfficiency());
                blade.put("expansion", type.getExpansionCoefficient());
                elem.modules.put("nuclearcraft:" + configContext + ":blade", blade);
            }

            if (block instanceof TurbineRotorStatorBlock statorBlock && statorBlock.getMultiblockVariant().orElse(null) instanceof TurbineRotorStatorType type) {
                Map<String, Object> stator = new HashMap<>();
                stator.put("expansion", type.getExpansionCoefficient());
                elem.modules.put("nuclearcraft:" + configContext + ":stator", stator);
            }

            if (block instanceof TurbineDynamoCoilBlock dynamoCoilBlock && dynamoCoilBlock.getMultiblockVariant().orElse(null) instanceof TurbineDynamoCoilType type) {
                Map<String, Object> coil = new HashMap<>();
                coil.put("efficiency", type.getConductivity());
                elem.modules.put("nuclearcraft:" + configContext + ":coil", coil);
                translatePlacementRules(coil, type.getRule());
            }

            if (block == TURBINE_MAP.get("turbine_coil_connector").get()) {
                Map<String, Object> connector = new HashMap<>();
                elem.modules.put("nuclearcraft:" + configContext + ":connector", connector);
                translatePlacementRules(connector, TurbinePlacement.RULE_MAP.get("connector"));
            }

            if (block == TURBINE_MAP.get("turbine_rotor_bearing").get()) {
                elem.modules.put("nuclearcraft:" + configContext + ":bearing", new NCPFEmptyModule());
            }

            if (block == TURBINE_MAP.get("turbine_rotor_shaft").get()) {
                elem.modules.put("nuclearcraft:" + configContext + ":shaft", new NCPFEmptyModule());
            }

            if (wall || block == FISSION_REACTOR_MAP.get("fission_casing").get() || block == FISSION_REACTOR_MAP.get("fission_glass").get() || block == FISSION_REACTOR_MAP.get("fission_monitor").get() || block == FISSION_REACTOR_MAP.get("fission_source_manager").get() || block == FISSION_REACTOR_MAP.get("fission_shield_manager").get() || block == FISSION_REACTOR_MAP.get("fission_power_port").get() || block == FISSION_REACTOR_MAP.get("fission_computer_port").get() || block == TURBINE_MAP.get("turbine_casing").get() || block == TURBINE_MAP.get("turbine_glass").get() || block == TURBINE_MAP.get("turbine_computer_port").get() || block == TURBINE_MAP.get("turbine_redstone_port").get()) {
                Map<String, Object> casing = new HashMap<>();
                casing.put("edge", block == FISSION_REACTOR_MAP.get("fission_casing").get() || block == TURBINE_MAP.get("turbine_casing").get());
                elem.modules.put("nuclearcraft:" + configContext + ":casing", casing);
            }

            if (elem.modules.isEmpty()) {
                elem.modules = null;
            }
        }
    }

    private static Block getBlockFromType(String type, ResourceLocation id) {
        for (Block block : BuiltInRegistries.BLOCK_ENTITY_TYPE.get(id).getValidBlocks()) {
            if (block instanceof GenericTooltipDeviceBlock<?, ?> deviceBlock) {
                if (type.equals(deviceBlock.getMultiblockVariant().get().getName())) {
                    return block;
                }
            }
        }
        return null;
    }

    public static void translate(List<NCPFElement> list, BasicRecipeHandler<?> recipes) {
        translate(list, recipes, x -> true);
    }

    public static void translateModerator(List<NCPFElement> list) {
        for (var moderatorData : BuiltInRegistries.ITEM.getDataMap(FISSION_MODERATOR_DATA).entrySet()) {
            NCPFElement element = translate(new ItemStack(BuiltInRegistries.ITEM.get(moderatorData.getKey())));

            if (element.modules == null) {
                element.modules = new HashMap<>();
            }

            Map<String, Object> moderator = new HashMap<>();
            moderator.put("flux", moderatorData.getValue().fluxFactor());
            moderator.put("efficiency", moderatorData.getValue().efficiency());
            element.modules.put("nuclearcraft:" + configContext + ":moderator", moderator);

            list.add(element);
        }
    }

    public static void translateReflector(List<NCPFElement> list) {
        for (var reflectorData : BuiltInRegistries.ITEM.getDataMap(FISSION_REFLECTOR_DATA).entrySet()) {
            NCPFElement element = translate(new ItemStack(BuiltInRegistries.ITEM.get(reflectorData.getKey())));
            if (element.modules == null) {
                element.modules = new HashMap<>();
            }

            Map<String, Object> reflector = new HashMap<>();
            reflector.put("efficiency", reflectorData.getValue().efficiency());
            reflector.put("reflectivity", reflectorData.getValue().reflectivity());
            element.modules.put("nuclearcraft:" + configContext + ":reflector", reflector);
            list.add(element);
        }
    }

    public static <T extends BasicRecipe> void translate(List<NCPFElement> list, BasicRecipeHandler<T> recipes, Predicate<T> filter) { // TODO add recipe name
        if (!(recipes instanceof CoolantHeaterRecipes)) {
            if (recipes.getItemInputSize() + recipes.getFluidInputSize() != 1) {
                throw new IllegalArgumentException("Cannot convert recipes to NCPF element unless they have exactly one input!");
            }
        }

        for (T recipe : recipes.getRecipeList(null)) {
            if (!filter.test(recipe)) {
                continue;
            }

            NCPFElement element;
            if (recipes.getFluidInputSize() > 0) {
                element = translateFluidIngredient(recipe.getFluidIngredients().get(0));
            } else {
                element = translateIngredient(recipe.getItemIngredients().get(0));
            }

            if (recipe instanceof FissionHeatingRecipe heatingRecipe) {
                if (element.modules == null) {
                    element.modules = new HashMap<>();
                }

                Map<String, Object> stats = new HashMap<>();
                stats.put("heat", heatingRecipe.getFissionHeatingHeatPerInputMB());
                stats.put("output_ratio", recipe.getFluidProducts().get(0).getStack().getAmount() / (float) recipe.getFluidIngredients().get(0).getStack().getAmount());
                stats.put("output", translateFluidIngredient(recipe.getFluidProducts().get(0)));
                element.modules.put("nuclearcraft:overhaul_sfr:coolant_recipe_stats", stats);
            }

            if (recipe instanceof FissionIrradiatorRecipe irradiatorRecipe) {
                if (element.modules == null) {
                    element.modules = new HashMap<>();
                }

                Map<String, Object> irradiator = new HashMap<>();
                irradiator.put("heat", irradiatorRecipe.getIrradiatorHeatPerFlux());
                irradiator.put("efficiency", irradiatorRecipe.getIrradiatorProcessEfficiency());
                irradiator.put("output", translateIngredient(recipe.getItemProducts().get(0)));
                element.modules.put("nuclearcraft:" + configContext + ":irradiator_stats", irradiator);
            }

            if (recipe instanceof SolidFissionRecipe solidFissionRecipe) {
                if (element.modules == null) {
                    element.modules = new HashMap<>();
                }

                Map<String, Object> fuel = new HashMap<>();
                fuel.put("efficiency", solidFissionRecipe.getFissionFuelEfficiency());
                fuel.put("heat", solidFissionRecipe.getFissionFuelHeat());
                fuel.put("time", solidFissionRecipe.getFissionFuelTime());
                fuel.put("criticality", solidFissionRecipe.getFissionFuelCriticality());
                fuel.put("self_priming", solidFissionRecipe.getFissionFuelSelfPriming());
                fuel.put("output", translateIngredient(recipe.getItemProducts().get(0)));
                element.modules.put("nuclearcraft:overhaul_sfr:fuel_stats", fuel);
            }

            if (recipe instanceof SaltFissionRecipe saltFissionRecipe) {
                if (element.modules == null) {
                    element.modules = new HashMap<>();
                }

                Map<String, Object> fuel = new HashMap<>();
                fuel.put("efficiency", saltFissionRecipe.getFissionFuelEfficiency());
                fuel.put("heat", saltFissionRecipe.getFissionFuelHeat());
                fuel.put("time", saltFissionRecipe.getSaltFissionFuelTime());
                fuel.put("criticality", saltFissionRecipe.getFissionFuelCriticality());
                fuel.put("self_priming", saltFissionRecipe.getFissionFuelSelfPriming());
                fuel.put("output", translateFluidIngredient(recipe.getFluidProducts().get(0)));
                element.modules.put("nuclearcraft:overhaul_msr:fuel_stats", fuel);
            }

            if (recipe instanceof FissionCoolantHeaterRecipe coolantHeaterRecipe) {
                if (element.modules == null) {
                    element.modules = new HashMap<>();
                }

                Map<String, Object> heater = new HashMap<>();
                heater.put("cooling", coolantHeaterRecipe.getCoolantHeaterCoolingRate());
                heater.put("output", translateFluidIngredient(recipe.getFluidProducts().get(0)));
                element.modules.put("nuclearcraft:overhaul_msr:heater_stats", heater);
            }

            if (recipe instanceof TurbineRecipe turbineRecipe) {
                if (element.modules == null) {
                    element.modules = new HashMap<>();
                }

                Map<String, Object> stats = new HashMap<>();
                stats.put("power", turbineRecipe.getTurbinePowerPerMB());
                stats.put("coefficient", turbineRecipe.getTurbineExpansionLevel());
                stats.put("output", translateFluidIngredient(recipe.getFluidProducts().get(0)));
                element.modules.put("nuclearcraft:overhaul_turbine:recipe_stats", stats);
            }

            list.add(element);
        }
    }

    public static <T extends BasicRecipe> void translateOutputs(List<NCPFElement> list, BasicRecipeHandler<T> recipes) {
        for (T recipe : recipes.getRecipeList(null)) {
            for (SizedChanceItemIngredient item : recipe.getItemProducts()) {
                list.add(translateIngredient(item));
            }

            for (SizedChanceFluidIngredient fluid : recipe.getFluidProducts()) {
                list.add(translateFluidIngredient(fluid));
            }
        }
    }

    private static NCPFElement translateIngredient(SizedChanceItemIngredient ingredient) {
        return translateIngredient(ingredient.ingredient());
    }

    private static NCPFElement translateIngredient(Ingredient ingredient) {
        NCPFListElement list = new NCPFListElement();
        if (ingredient.isCustom() && ingredient.getCustomIngredient() instanceof CompoundIngredient(List<Ingredient> children)) {
            for (Ingredient child : children) {
                list.elements.add(translateIngredient(child));
            }
        } else {
            for (Ingredient.Value value : ingredient.getValues()) {
                if (value instanceof Ingredient.ItemValue(ItemStack item)) {
                    NCPFItem ncpfItem = new NCPFItem();
                    ncpfItem.name = BuiltInRegistries.ITEM.getKey(item.getItem()).toString();
                    list.elements.add(ncpfItem);
                }
                if (value instanceof Ingredient.TagValue(TagKey<Item> tag)) {
                    NCPFItemTag ncpfItemTag = new NCPFItemTag();
                    ncpfItemTag.name = tag.location().toString();
                    list.elements.add(ncpfItemTag);
                }
            }
        }
        return list.elements.size() == 1 ? list.elements.get(0) : list;
    }

    private static NCPFElement translateFluidIngredient(SizedChanceFluidIngredient ingredient) {
        return translateFluidIngredient(ingredient.ingredient());
    }

    private static NCPFElement translateFluidIngredient(FluidIngredient ingredient) {
        if (ingredient instanceof CompoundFluidIngredient compoundFluidIngredient) {
            NCPFListElement list = new NCPFListElement();
            for (FluidIngredient fluidIngredient : compoundFluidIngredient.children()) {
                list.elements.add(translateFluidIngredient(fluidIngredient));
            }
            return list.elements.size() == 1 ? list.elements.get(0) : list;
        }
        if (ingredient instanceof SingleFluidIngredient fluid) {
            NCPFFluid ncpfFluid = new NCPFFluid();
            ncpfFluid.name = BuiltInRegistries.FLUID.getKey(fluid.fluid().value()).toString();
            return ncpfFluid;
        }
        if (ingredient instanceof TagFluidIngredient fluid) {
            NCPFFluidTag ncpfFluidTag = new NCPFFluidTag();
            ncpfFluidTag.name = fluid.tag().location().toString();
            return ncpfFluidTag;
        }

        throw new UnsupportedOperationException("Could not translate FluidIngredient: " + ingredient.getClass().getName());
    }

    public static NCPFElement translate(ItemStack stack) {
        return translate(stack, true);
    }

    public static NCPFElement translate(ItemStack stack, boolean includeModules) {
        Item item = stack.getItem();
        if (item instanceof BlockItem itemBlock) {
            Block block = itemBlock.getBlock();
            List<NCPFElement> list = new ArrayList<>();
            translate(list, block, includeModules);
            return list.getFirst();
        }

        NCPFItem ncpfItem = new NCPFItem();
        ncpfItem.name = BuiltInRegistries.ITEM.getKey(stack.getItem()).toString();
        return ncpfItem;
    }

    private static <MULTIBLOCK extends Multiblock<MULTIBLOCK>, T extends AbstractMultiblockPart<MULTIBLOCK>> void translatePlacementRules(Map<String, Object> module, PlacementRule<MULTIBLOCK, T> rule) {
        List<NCPFPlacementRule> rules = new ArrayList<>();

        NCPFPlacementRule placementRule = translatePlacementRule(rule);
        rules.add(placementRule);

        module.put("rules", rules);
    }

    private static <MULTIBLOCK extends Multiblock<MULTIBLOCK>, T extends AbstractMultiblockPart<MULTIBLOCK>> NCPFPlacementRule translatePlacementRule(PlacementRule<MULTIBLOCK, T> rule) {
        NCPFPlacementRule placementRule = new NCPFPlacementRule();

        if (rule instanceof PlacementRule.And<?, ?> and) {
            placementRule.type = NCPFPlacementRuleType.and;
            for (PlacementRule<?, ?> subRule : and.subRules) {
                placementRule.rules.add(translatePlacementRule((PlacementRule<MULTIBLOCK, T>) subRule));
            }
        }

        if (rule instanceof PlacementRule.Or<?, ?> or) {
            placementRule.type = NCPFPlacementRuleType.or;
            for (PlacementRule<?, ?> subRule : or.subRules) {
                placementRule.rules.add(translatePlacementRule((PlacementRule<MULTIBLOCK, T>) subRule));
            }
        }

        if (rule instanceof PlacementRule.Adjacent<?, ?> adjacent) {
            if (rule instanceof FissionPlacement.AdjacentCasing) {
                placementRule.block = new NCPFModuleElement("nuclearcraft:" + configContext + ":casing");
            }

            if (rule instanceof FissionPlacement.AdjacentConductor) {
                placementRule.block = new NCPFModuleElement("nuclearcraft:" + configContext + ":conductor");
            }

            if (rule instanceof FissionPlacement.AdjacentModerator) {
                placementRule.block = new NCPFModuleElement("nuclearcraft:" + configContext + ":moderator");
            }

            if (rule instanceof FissionPlacement.AdjacentReflector) {
                placementRule.block = new NCPFModuleElement("nuclearcraft:" + configContext + ":reflector");
            }

            if (rule instanceof FissionPlacement.AdjacentIrradiator) {
                placementRule.block = new NCPFModuleElement("nuclearcraft:" + configContext + ":irradiator");
            }

            if (rule instanceof FissionPlacement.AdjacentShield) {
                placementRule.block = new NCPFModuleElement("nuclearcraft:" + configContext + ":neutron_shield");
            }

            if (rule instanceof FissionPlacement.AdjacentCell) {
                placementRule.block = new NCPFModuleElement("nuclearcraft:" + configContext + ":fuel_cell");
            }

            if (rule instanceof FissionPlacement.AdjacentSink sink) {
                if (sink.sinkType.equals("any")) {
                    placementRule.block = new NCPFModuleElement("nuclearcraft:" + configContext + ":heat_sink");
                } else {
                    Block blockFound = getBlockFromType(sink.sinkType, FISSION_ENTITY_TYPE.get("heat_sink").getId());
                    placementRule.block = blockFound == null ? null : translate(new ItemStack(blockFound.asItem()), false);
                }

                if (placementRule.block == null) {
                    throw new IllegalArgumentException("Could not find target sink: " + sink.sinkType + "!");
                }
            }

            if (rule instanceof FissionPlacement.AdjacentVessel) {
                placementRule.block = new NCPFModuleElement("nuclearcraft:" + configContext + ":fuel_vessel");
            }

            if (rule instanceof FissionPlacement.AdjacentHeater heater) {
                if (heater.heaterType.equals("any")) {
                    placementRule.block = new NCPFModuleElement("nuclearcraft:" + configContext + ":heater");
                } else {
                    Block blockFound = getBlockFromType(heater.heaterType, FISSION_ENTITY_TYPE.get("coolant_heater").getId());
                    placementRule.block = blockFound == null ? null : translate(new ItemStack(blockFound.asItem()), false);
                }

                if (placementRule.block == null) {
                    throw new IllegalArgumentException("Could not find target heater: " + heater.heaterType + "!");
                }
            }

            if (rule instanceof TurbinePlacement.AdjacentCasing) {
                placementRule.block = new NCPFModuleElement("nuclearcraft:" + configContext + ":casing");
            }

            if (rule instanceof TurbinePlacement.AdjacentBearing) {
                placementRule.block = new NCPFModuleElement("nuclearcraft:" + configContext + ":bearing");
            }

            if (rule instanceof TurbinePlacement.AdjacentConnector) {
                placementRule.block = new NCPFModuleElement("nuclearcraft:" + configContext + ":connector");
            }

            if (rule instanceof TurbinePlacement.AdjacentCoil coil) {
                if (coil.coilType.equals("any")) {
                    placementRule.block = new NCPFModuleElement("nuclearcraft:" + configContext + ":coil");
                } else {
                    Block blockFound = getBlockFromType(coil.coilType, TURBINE_ENTITY_TYPE.get("dynamo").getId());
                    placementRule.block = blockFound == null ? null : translate(new ItemStack(blockFound.asItem()), false);
                }

                if (placementRule.block == null) {
                    throw new IllegalArgumentException("Could not find target coil: " + coil.coilType + "!");
                }
            }

            if (placementRule.block == null) {
                throw new IllegalArgumentException("Could not find target for rule: " + rule.getClass().getName() + "!");
            }

            switch (adjacent.countType) {
                case AT_LEAST -> {
                    placementRule.min = adjacent.amount;
                    placementRule.max = 6;
                }
                case AT_MOST -> {
                    placementRule.min = 0;
                    placementRule.max = adjacent.amount;
                }
                case EXACTLY -> placementRule.min = placementRule.max = adjacent.amount;
            }

            switch (adjacent.adjType) {
                case AXIAL -> {
                    placementRule.type = NCPFPlacementRuleType.axial;
                    placementRule.min /= 2;
                    placementRule.max /= 2;
                    if (adjacent.countType == PlacementRule.CountType.EXACTLY) {
                        NCPFPlacementRule and = new NCPFPlacementRule();
                        and.type = NCPFPlacementRuleType.and;

                        NCPFPlacementRule individual = new NCPFPlacementRule();
                        individual.type = placementRule.type;
                        individual.block = placementRule.block;
                        individual.min = placementRule.min * 2;
                        individual.max = placementRule.max * 2;
                        individual.type = NCPFPlacementRuleType.between;

                        and.rules.add(individual);
                        and.rules.add(placementRule);
                        placementRule = and;
                    }
                }
                case EDGE -> placementRule.type = NCPFPlacementRuleType.edge;
                case STANDARD -> placementRule.type = NCPFPlacementRuleType.between;
                case VERTEX -> placementRule.type = NCPFPlacementRuleType.vertex;
            }
        }

        return placementRule;
    }
}