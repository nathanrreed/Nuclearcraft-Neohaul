package com.nred.nuclearcraft.multiblock.turbine;

import com.nred.nuclearcraft.block_entity.turbine.*;
import com.nred.nuclearcraft.multiblock.PlacementRule;
import com.nred.nuclearcraft.multiblock.PlacementRule.AdjacencyType;
import com.nred.nuclearcraft.multiblock.PlacementRule.CountType;
import com.nred.nuclearcraft.multiblock.PlacementRule.PlacementMap;
import com.nred.nuclearcraft.util.StringHelper;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.zerono.mods.zerocore.lib.multiblock.cuboid.PartPosition;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;

import javax.annotation.Nullable;
import java.util.*;
import java.util.regex.Pattern;

import static com.nred.nuclearcraft.config.Config2.turbine_coil_rule;
import static com.nred.nuclearcraft.config.Config2.turbine_connector_rule;
import static com.nred.nuclearcraft.registration.BlockRegistration.TURBINE_MAP;

public abstract class TurbinePlacement {

    /**
     * List of all defined rule parsers. Earlier entries are prioritised!
     */
    public static final List<PlacementRule.RuleParser<Turbine, AbstractTurbineEntity>> RULE_PARSER_LIST = new LinkedList<>();

    /**
     * Map of all placement rule IDs to unparsed rule strings, used for ordered iterations.
     */
    public static final Object2ObjectMap<String, String> RULE_MAP_RAW = new Object2ObjectArrayMap<>();

    /**
     * Map of all defined placement rules.
     */
    public static final Object2ObjectMap<String, PlacementRule<Turbine, AbstractTurbineEntity>> RULE_MAP = new PlacementMap<>();

    /**
     * List of all defined tooltip builders. Earlier entries are prioritised!
     */
    public static final List<PlacementRule.TooltipBuilder<Turbine, AbstractTurbineEntity>> TOOLTIP_BUILDER_LIST = new LinkedList<>();

//	public static PlacementRule.RecipeHandler recipe_handler; TODO

    /**
     * Map of all localized tooltips.
     */
    public static final Object2ObjectMap<String, String> TOOLTIP_MAP = new Object2ObjectOpenHashMap<>();

    public static void preInit() {
        RULE_PARSER_LIST.add(new DefaultRuleParser());

        TOOLTIP_BUILDER_LIST.add(new DefaultTooltipBuilder());
    }

    public static void init() {
//		recipe_handler = new RecipeHandler();

        RULE_MAP.put("", new PlacementRule.Or<>(new ArrayList<>()));

        addRule("magnesium_coil", turbine_coil_rule[0], new ItemStack(TURBINE_MAP.get("magnesium_turbine_dynamo_coil"), 1));
        addRule("beryllium_coil", turbine_coil_rule[1], new ItemStack(TURBINE_MAP.get("beryllium_turbine_dynamo_coil"), 1));
        addRule("aluminum_coil", turbine_coil_rule[2], new ItemStack(TURBINE_MAP.get("aluminum_turbine_dynamo_coil"), 1));
        addRule("gold_coil", turbine_coil_rule[3], new ItemStack(TURBINE_MAP.get("gold_turbine_dynamo_coil"), 1));
        addRule("copper_coil", turbine_coil_rule[4], new ItemStack(TURBINE_MAP.get("copper_turbine_dynamo_coil"), 1));
        addRule("silver_coil", turbine_coil_rule[5], new ItemStack(TURBINE_MAP.get("silver_turbine_dynamo_coil"), 1));

        addRule("connector", turbine_connector_rule[0], new ItemStack(TURBINE_MAP.get("turbine_coil_connector")));
    }

    public static void addRule(String id, String rule, Object... blocks) {
        RULE_MAP_RAW.put(id, rule);
        RULE_MAP.put(id, parse(rule));
//		for (Object block : blocks) {
//			recipe_handler.addRecipe(block, id);
//		}
    }

    public static void postInit() {
        for (Object2ObjectMap.Entry<String, PlacementRule<Turbine, AbstractTurbineEntity>> entry : RULE_MAP.object2ObjectEntrySet()) {
            for (PlacementRule.TooltipBuilder<Turbine, AbstractTurbineEntity> builder : TOOLTIP_BUILDER_LIST) {
                String tooltip = builder.buildTooltip(entry.getValue());
                if (tooltip != null) {
                    TOOLTIP_MAP.put(entry.getKey(), tooltip);
                }
            }
        }
    }

    // Default Rule Parser

    public static PlacementRule<Turbine, AbstractTurbineEntity> parse(String string) {
        return PlacementRule.parse(string, RULE_PARSER_LIST);
    }

    /**
     * Rule parser for all rule types available in base NC.
     */
    public static class DefaultRuleParser extends PlacementRule.DefaultRuleParser<Turbine, AbstractTurbineEntity> {

        @Override
        protected @Nullable PlacementRule<Turbine, AbstractTurbineEntity> partialParse(String s) {
            s = s.toLowerCase(Locale.ROOT);

            s = s.replaceAll("at exactly one vertex", "vertex");

            boolean exact = s.contains("exact"), atMost = s.contains("at most");
            boolean axial = s.contains("axial"), vertex = s.contains("vertex"), edge = s.contains("edge");

            if ((exact && atMost) || (axial && vertex)) {
                return null;
            }

            s = s.replaceAll("at least", "");
            s = s.replaceAll("exactly", "");
            s = s.replaceAll("exact", "");
            s = s.replaceAll("at most", "");
            s = s.replaceAll("axially", "");
            s = s.replaceAll("axial", "");
            s = s.replaceAll("at one vertex", "");
            s = s.replaceAll("at a vertex", "");
            s = s.replaceAll("at vertex", "");
            s = s.replaceAll("vertex", "");
            s = s.replaceAll("at one edge", "");
            s = s.replaceAll("at an edge", "");
            s = s.replaceAll("at edge", "");
            s = s.replaceAll("along one edge", "");
            s = s.replaceAll("along an edge", "");
            s = s.replaceAll("along edge", "");
            s = s.replaceAll("edge", "");

            int amount = -1;
            String rule = null, type = null;

            String[] split = s.split(Pattern.quote(" "));
            for (int i = 0; i < split.length; ++i) {
                if (StringHelper.NUMBER_S2I_MAP.containsKey(split[i])) {
                    amount = StringHelper.NUMBER_S2I_MAP.getInt(split[i]);
                } else if (rule == null) {
                    if (split[i].contains("wall") || split[i].contains("casing")) {
                        rule = "casing";
                    } else if (split[i].contains("bearing")) {
                        rule = "bearing";
                    } else if (split[i].contains("connector")) {
                        rule = "connector";
                    } else if (split[i].contains("coil")) {
                        rule = "coil";
                        if (i > 0) {
                            type = split[i - 1];
                        } else {
                            return null;
                        }
                    }
                }
            }

            if (amount < 0 || rule == null) {
                return null;
            }

            CountType countType = exact ? CountType.EXACTLY : (atMost ? CountType.AT_MOST : CountType.AT_LEAST);
            AdjacencyType adjType = axial ? AdjacencyType.AXIAL : (vertex ? AdjacencyType.VERTEX : (edge ? AdjacencyType.EDGE : AdjacencyType.STANDARD));

            return switch (rule) {
                case "casing" -> new AdjacentCasing(amount, countType, adjType);
                case "bearing" -> new AdjacentBearing(amount, countType, adjType);
                case "connector" -> new AdjacentConnector(amount, countType, adjType);
                case "coil" -> new AdjacentCoil(amount, countType, adjType, type);
                default -> null;
            };

        }
    }

    // Adjacent

    public static abstract class Adjacent extends PlacementRule.Adjacent<Turbine, AbstractTurbineEntity> {

        public Adjacent(String dependency, int amount, CountType countType, AdjacencyType adjType) {
            super(dependency, amount, countType, adjType);
        }

        @Override
        public void checkIsRuleAllowed(String ruleID) {
            if (amount > 4) {
                throw new IllegalArgumentException("Adjacency placement rule with ID \"" + ruleID + "\" can not require more than four adjacencies!");
            }
            if (adjType == AdjacencyType.VERTEX) {
                throw new IllegalArgumentException("Vertex adjacency placement rule with ID \"" + ruleID + "\" is disallowed as multiblock dynamos have no depth!");
            }
            super.checkIsRuleAllowed(ruleID);
        }
    }

    public static class AdjacentCasing extends Adjacent {

        public AdjacentCasing(int amount, CountType countType, AdjacencyType adjType) {
            super("turbine_casing", amount, countType, adjType);
        }

        @Override
        public boolean satisfied(AbstractTurbineEntity part, Direction dir, boolean simulate) {
            return isCasing(part.getMultiblockController(), part.getBlockPos().relative(dir));
        }
    }

    public static class AdjacentBearing extends Adjacent {

        public AdjacentBearing(int amount, CountType countType, AdjacencyType adjType) {
            super("bearing", amount, countType, adjType);
        }

        @Override
        public void checkIsRuleAllowed(String ruleID) {
            if (amount > 1) {
                throw new IllegalArgumentException("Adjacency placement rule with ID \"" + ruleID + "\" can not require more than one rotor bearing!");
            }
            super.checkIsRuleAllowed(ruleID);
        }

        @Override
        public boolean satisfied(AbstractTurbineEntity part, Direction dir, boolean simulate) {
            return isRotorBearing(part.getMultiblockController(), part.getBlockPos().relative(dir));
        }
    }

    public static class AdjacentConnector extends Adjacent {

        public AdjacentConnector(int amount, CountType countType, AdjacencyType adjType) {
            super("connector", amount, countType, adjType);
        }

        @Override
        public boolean satisfied(AbstractTurbineEntity part, Direction dir, boolean simulate) {
            return isCoilConnector(part.getMultiblockController(), part.getBlockPos().relative(dir));
        }
    }

    public static class AdjacentCoil extends Adjacent {

        public final String coilType;

        public AdjacentCoil(int amount, CountType countType, AdjacencyType adjType, String coilType) {
            super(coilType + "_coil", amount, countType, adjType);
            this.coilType = coilType;
        }

        @Override
        public void checkIsRuleAllowed(String ruleID) {
            super.checkIsRuleAllowed(ruleID);
            if (countType != CountType.AT_LEAST && coilType.equals("any")) {
                throw new IllegalArgumentException((countType == CountType.EXACTLY ? "Exact 'any coil'" : "'At most n of any coil'") + " placement rule with ID \"" + ruleID + "\" is disallowed due to potential ambiguity during rule checks!");
            }
        }

        @Override
        public boolean satisfied(AbstractTurbineEntity part, Direction dir, boolean simulate) {
            return isDynamoCoil(part.getMultiblockController(), part.getBlockPos().relative(dir), coilType);
        }
    }

    // Helper Methods

    public static boolean isCasing(Optional<Turbine> turbine, BlockPos pos) {
        if (turbine.isEmpty()) return false;
        BlockEntity tile = turbine.get().getWorld().getBlockEntity(pos);
        return tile instanceof AbstractTurbineEntity part && !(part.getPartPosition() == PartPosition.Interior);
    }

    public static boolean isRotorBearing(Optional<Turbine> turbine, BlockPos pos) {
        return turbine.filter(value -> value.getPartMap(TurbineRotorBearingEntity.class).get(pos.asLong()) != null).isPresent();
    }

    public static boolean isCoilConnector(Optional<Turbine> turbine, BlockPos pos) {
        if (turbine.isEmpty()) return false;
        TurbineDynamoEntityPart tile = turbine.get().getPartMap(TurbineDynamoEntityPart.class).get(pos.asLong());
        return tile instanceof TurbineCoilConnectorEntity part && part.isInValidPosition;
    }

    public static boolean isDynamoCoil(Optional<Turbine> turbine, BlockPos pos, String coilName) {
        if (turbine.isEmpty()) return false;
        TurbineDynamoEntityPart tile = turbine.get().getPartMap(TurbineDynamoEntityPart.class).get(pos.asLong());
        return tile instanceof TurbineDynamoCoilEntity part && part.isInValidPosition && (coilName.equals("any") || part.dynamoCoilType.getName().equals(coilName));
    }

    // Default Tooltip Builder

    public static class DefaultTooltipBuilder extends PlacementRule.DefaultTooltipBuilder<Turbine, AbstractTurbineEntity> {
    }

//	// Recipe Handler TODO REMOVE
//
//	public static class RecipeHandler extends PlacementRule.RecipeHandler {
//
//		public RecipeHandler() {
//			super("multiblock");
//		}
//	}
}
