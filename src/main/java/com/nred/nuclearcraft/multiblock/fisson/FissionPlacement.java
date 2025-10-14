package com.nred.nuclearcraft.multiblock.fisson;

import com.nred.nuclearcraft.block.fission.*;
import com.nred.nuclearcraft.handler.NCRecipes;
import com.nred.nuclearcraft.multiblock.PlacementRule;
import com.nred.nuclearcraft.multiblock.PlacementRule.AdjacencyType;
import com.nred.nuclearcraft.multiblock.PlacementRule.CountType;
import com.nred.nuclearcraft.multiblock.PlacementRule.PlacementMap;
import com.nred.nuclearcraft.recipe.RecipeHelper;
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

import static com.nred.nuclearcraft.config.Config2.fission_heater_rule;
import static com.nred.nuclearcraft.config.Config2.fission_sink_rule;
import static com.nred.nuclearcraft.registration.BlockRegistration.FISSION_REACTOR_MAP;

public abstract class FissionPlacement {

    /**
     * List of all defined rule parsers. Earlier entries are prioritised!
     */
    public static final List<PlacementRule.RuleParser<FissionReactor, AbstractFissionEntity>> RULE_PARSER_LIST = new LinkedList<>();

    /**
     * Map of all placement rule IDs to unparsed rule strings, used for ordered iterations.
     */
    public static final Object2ObjectMap<String, String> RULE_MAP_RAW = new Object2ObjectArrayMap<>();

    /**
     * Map of all defined placement rules.
     */
    public static final Object2ObjectMap<String, PlacementRule<FissionReactor, AbstractFissionEntity>> RULE_MAP = new PlacementMap<>();

    /**
     * List of all defined tooltip builders. Earlier entries are prioritised!
     */
    public static final List<PlacementRule.TooltipBuilder<FissionReactor, AbstractFissionEntity>> TOOLTIP_BUILDER_LIST = new LinkedList<>();

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

        addRule("water_sink", fission_sink_rule[0], new ItemStack(FISSION_REACTOR_MAP.get("water_sink"), 1));
        addRule("iron_sink", fission_sink_rule[1], new ItemStack(FISSION_REACTOR_MAP.get("iron_sink"), 1));
        addRule("redstone_sink", fission_sink_rule[2], new ItemStack(FISSION_REACTOR_MAP.get("redstone_sink"), 1));
        addRule("quartz_sink", fission_sink_rule[3], new ItemStack(FISSION_REACTOR_MAP.get("quartz_sink"), 1));
        addRule("obsidian_sink", fission_sink_rule[4], new ItemStack(FISSION_REACTOR_MAP.get("obsidian_sink"), 1));
        addRule("nether_brick_sink", fission_sink_rule[5], new ItemStack(FISSION_REACTOR_MAP.get("nether_brick_sink"), 1));
        addRule("glowstone_sink", fission_sink_rule[6], new ItemStack(FISSION_REACTOR_MAP.get("glowstone_sink"), 1));
        addRule("lapis_sink", fission_sink_rule[7], new ItemStack(FISSION_REACTOR_MAP.get("lapis_sink"), 1));
        addRule("gold_sink", fission_sink_rule[8], new ItemStack(FISSION_REACTOR_MAP.get("gold_sink"), 1));
        addRule("prismarine_sink", fission_sink_rule[9], new ItemStack(FISSION_REACTOR_MAP.get("prismarine_sink"), 1));
        addRule("slime_sink", fission_sink_rule[10], new ItemStack(FISSION_REACTOR_MAP.get("slime_sink"), 1));
        addRule("end_stone_sink", fission_sink_rule[11], new ItemStack(FISSION_REACTOR_MAP.get("end_stone_sink"), 1));
        addRule("purpur_sink", fission_sink_rule[12], new ItemStack(FISSION_REACTOR_MAP.get("purpur_sink"), 1));
        addRule("diamond_sink", fission_sink_rule[13], new ItemStack(FISSION_REACTOR_MAP.get("diamond_sink"), 1));
        addRule("emerald_sink", fission_sink_rule[14], new ItemStack(FISSION_REACTOR_MAP.get("emerald_sink"), 1));
        addRule("copper_sink", fission_sink_rule[15], new ItemStack(FISSION_REACTOR_MAP.get("copper_sink"), 1));
        addRule("tin_sink", fission_sink_rule[16], new ItemStack(FISSION_REACTOR_MAP.get("tin_sink"), 1));
        addRule("lead_sink", fission_sink_rule[17], new ItemStack(FISSION_REACTOR_MAP.get("lead_sink"), 1));
        addRule("boron_sink", fission_sink_rule[18], new ItemStack(FISSION_REACTOR_MAP.get("boron_sink"), 1));
        addRule("lithium_sink", fission_sink_rule[19], new ItemStack(FISSION_REACTOR_MAP.get("lithium_sink"), 1));
        addRule("magnesium_sink", fission_sink_rule[20], new ItemStack(FISSION_REACTOR_MAP.get("magnesium_sink"), 1));
        addRule("manganese_sink", fission_sink_rule[21], new ItemStack(FISSION_REACTOR_MAP.get("manganese_sink"), 1));
        addRule("aluminum_sink", fission_sink_rule[22], new ItemStack(FISSION_REACTOR_MAP.get("aluminum_sink"), 1));
        addRule("silver_sink", fission_sink_rule[23], new ItemStack(FISSION_REACTOR_MAP.get("silver_sink"), 1));
        addRule("fluorite_sink", fission_sink_rule[24], new ItemStack(FISSION_REACTOR_MAP.get("fluorite_sink"), 1));
        addRule("villiaumite_sink", fission_sink_rule[25], new ItemStack(FISSION_REACTOR_MAP.get("villiaumite_sink"), 1));
        addRule("carobbiite_sink", fission_sink_rule[26], new ItemStack(FISSION_REACTOR_MAP.get("carobbiite_sink"), 1));
        addRule("arsenic_sink", fission_sink_rule[27], new ItemStack(FISSION_REACTOR_MAP.get("arsenic_sink"), 1));
        addRule("liquid_nitrogen_sink", fission_sink_rule[28], new ItemStack(FISSION_REACTOR_MAP.get("liquid_nitrogen_sink"), 1));
        addRule("liquid_helium_sink", fission_sink_rule[29], new ItemStack(FISSION_REACTOR_MAP.get("liquid_helium_sink"), 1));
        addRule("enderium_sink", fission_sink_rule[30], new ItemStack(FISSION_REACTOR_MAP.get("enderium_sink"), 1));
        addRule("cryotheum_sink", fission_sink_rule[31], new ItemStack(FISSION_REACTOR_MAP.get("cryotheum_sink"), 1));

        addRule("standard_heater", fission_heater_rule[0], new ItemStack(FISSION_REACTOR_MAP.get("standard_heater"), 1));
        addRule("iron_heater", fission_heater_rule[1], new ItemStack(FISSION_REACTOR_MAP.get("iron_heater"), 1));
        addRule("redstone_heater", fission_heater_rule[2], new ItemStack(FISSION_REACTOR_MAP.get("redstone_heater"), 1));
        addRule("quartz_heater", fission_heater_rule[3], new ItemStack(FISSION_REACTOR_MAP.get("quartz_heater"), 1));
        addRule("obsidian_heater", fission_heater_rule[4], new ItemStack(FISSION_REACTOR_MAP.get("obsidian_heater"), 1));
        addRule("nether_brick_heater", fission_heater_rule[5], new ItemStack(FISSION_REACTOR_MAP.get("nether_brick_heater"), 1));
        addRule("glowstone_heater", fission_heater_rule[6], new ItemStack(FISSION_REACTOR_MAP.get("glowstone_heater"), 1));
        addRule("lapis_heater", fission_heater_rule[7], new ItemStack(FISSION_REACTOR_MAP.get("lapis_heater"), 1));
        addRule("gold_heater", fission_heater_rule[8], new ItemStack(FISSION_REACTOR_MAP.get("gold_heater"), 1));
        addRule("prismarine_heater", fission_heater_rule[9], new ItemStack(FISSION_REACTOR_MAP.get("prismarine_heater"), 1));
        addRule("slime_heater", fission_heater_rule[10], new ItemStack(FISSION_REACTOR_MAP.get("slime_heater"), 1));
        addRule("end_stone_heater", fission_heater_rule[11], new ItemStack(FISSION_REACTOR_MAP.get("end_stone_heater"), 1));
        addRule("purpur_heater", fission_heater_rule[12], new ItemStack(FISSION_REACTOR_MAP.get("purpur_heater"), 1));
        addRule("diamond_heater", fission_heater_rule[13], new ItemStack(FISSION_REACTOR_MAP.get("diamond_heater"), 1));
        addRule("emerald_heater", fission_heater_rule[14], new ItemStack(FISSION_REACTOR_MAP.get("emerald_heater"), 1));
        addRule("copper_heater", fission_heater_rule[15], new ItemStack(FISSION_REACTOR_MAP.get("copper_heater"), 1));
        addRule("tin_heater", fission_heater_rule[16], new ItemStack(FISSION_REACTOR_MAP.get("tin_heater"), 1));
        addRule("lead_heater", fission_heater_rule[17], new ItemStack(FISSION_REACTOR_MAP.get("lead_heater"), 1));
        addRule("boron_heater", fission_heater_rule[18], new ItemStack(FISSION_REACTOR_MAP.get("boron_heater"), 1));
        addRule("lithium_heater", fission_heater_rule[19], new ItemStack(FISSION_REACTOR_MAP.get("lithium_heater"), 1));
        addRule("magnesium_heater", fission_heater_rule[20], new ItemStack(FISSION_REACTOR_MAP.get("magnesium_heater"), 1));
        addRule("manganese_heater", fission_heater_rule[21], new ItemStack(FISSION_REACTOR_MAP.get("manganese_heater"), 1));
        addRule("aluminum_heater", fission_heater_rule[22], new ItemStack(FISSION_REACTOR_MAP.get("aluminum_heater"), 1));
        addRule("silver_heater", fission_heater_rule[23], new ItemStack(FISSION_REACTOR_MAP.get("silver_heater"), 1));
        addRule("fluorite_heater", fission_heater_rule[24], new ItemStack(FISSION_REACTOR_MAP.get("fluorite_heater"), 1));
        addRule("villiaumite_heater", fission_heater_rule[25], new ItemStack(FISSION_REACTOR_MAP.get("villiaumite_heater"), 1));
        addRule("carobbiite_heater", fission_heater_rule[26], new ItemStack(FISSION_REACTOR_MAP.get("carobbiite_heater"), 1));
        addRule("arsenic_heater", fission_heater_rule[27], new ItemStack(FISSION_REACTOR_MAP.get("arsenic_heater"), 1));
        addRule("liquid_nitrogen_heater", fission_heater_rule[28], new ItemStack(FISSION_REACTOR_MAP.get("liquid_nitrogen_heater"), 1));
        addRule("liquid_helium_heater", fission_heater_rule[29], new ItemStack(FISSION_REACTOR_MAP.get("liquid_helium_heater"), 1));
        addRule("enderium_heater", fission_heater_rule[30], new ItemStack(FISSION_REACTOR_MAP.get("enderium_heater"), 1));
        addRule("cryotheum_heater", fission_heater_rule[31], new ItemStack(FISSION_REACTOR_MAP.get("cryotheum_heater"), 1));
    }

    public static void addRule(String id, String rule, Object... blocks) {
        RULE_MAP_RAW.put(id, rule);
        RULE_MAP.put(id, parse(rule));
//        for (Object block : blocks) {
//            recipe_handler.addRecipe(block, id);
//        }
    }

    public static void postInit() {
        for (Object2ObjectMap.Entry<String, PlacementRule<FissionReactor, AbstractFissionEntity>> entry : RULE_MAP.object2ObjectEntrySet()) {
            for (PlacementRule.TooltipBuilder<FissionReactor, AbstractFissionEntity> builder : TOOLTIP_BUILDER_LIST) {
                String tooltip = builder.buildTooltip(entry.getValue());
                if (tooltip != null) {
                    TOOLTIP_MAP.put(entry.getKey(), tooltip);
                }
            }
        }
    }

    // Default Rule Parser

    public static PlacementRule<FissionReactor, AbstractFissionEntity> parse(String string) {
        return PlacementRule.parse(string, RULE_PARSER_LIST);
    }

    /**
     * Rule parser for all rule types available in base NC.
     */
    public static class DefaultRuleParser extends PlacementRule.DefaultRuleParser<FissionReactor, AbstractFissionEntity> {
        @Override
        protected @Nullable PlacementRule<FissionReactor, AbstractFissionEntity> partialParse(String s) {
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
                    } else if (split[i].contains("conductor")) {
                        rule = "conductor";
                    } else if (split[i].contains("moderator")) {
                        rule = "moderator";
                    } else if (split[i].contains("reflector")) {
                        rule = "reflector";
                    } else if (split[i].contains("irradiator")) {
                        rule = "irradiator";
                    } else if (split[i].contains("shield")) {
                        rule = "shield";
                    } else if (split[i].contains("cell")) {
                        rule = "cell";
                    } else if (split[i].contains("sink")) {
                        rule = "sink";
                        if (i > 0) {
                            type = split[i - 1];
                        } else {
                            return null;
                        }
                    } else if (split[i].contains("vessel")) {
                        rule = "vessel";
                    } else if (split[i].contains("heater")) {
                        rule = "heater";
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
                case "conductor" -> new AdjacentConductor(amount, countType, adjType);
                case "moderator" -> new AdjacentModerator(amount, countType, adjType);
                case "reflector" -> new AdjacentReflector(amount, countType, adjType);
                case "irradiator" -> new AdjacentIrradiator(amount, countType, adjType);
                case "shield" -> new AdjacentShield(amount, countType, adjType);
                case "cell" -> new AdjacentCell(amount, countType, adjType);
                case "sink" -> new AdjacentSink(amount, countType, adjType, type);
                case "vessel" -> new AdjacentVessel(amount, countType, adjType);
                case "heater" -> new AdjacentHeater(amount, countType, adjType, type);
                default -> null;
            };

        }
    }

    // Adjacent

    public static abstract class Adjacent extends PlacementRule.Adjacent<FissionReactor, AbstractFissionEntity> {
        public Adjacent(String dependency, int amount, CountType countType, AdjacencyType adjType) {
            super(dependency, amount, countType, adjType);
        }
    }

    public static class AdjacentCasing extends Adjacent {
        public AdjacentCasing(int amount, CountType countType, AdjacencyType adjType) {
            super("reactor_casing", amount, countType, adjType);
        }

        @Override
        public boolean satisfied(AbstractFissionEntity part, Direction dir, boolean simulate) {
            return isCasing(part.getMultiblockController(), part.getBlockPos().relative(dir), simulate);
        }
    }

    public static class AdjacentConductor extends Adjacent {
        public AdjacentConductor(int amount, CountType countType, AdjacencyType adjType) {
            super("conductor", amount, countType, adjType);
        }

        @Override
        public boolean satisfied(AbstractFissionEntity part, Direction dir, boolean simulate) {
            return isConductor(part.getMultiblockController(), part.getBlockPos().relative(dir), simulate);
        }
    }

    public static class AdjacentModerator extends Adjacent {
        public AdjacentModerator(int amount, CountType countType, AdjacencyType adjType) {
            super("moderator", amount, countType, adjType);
        }

        @Override
        public boolean satisfied(AbstractFissionEntity part, Direction dir, boolean simulate) {
            return isActiveModerator(part.getMultiblockController(), part.getBlockPos().relative(dir), simulate);
        }
    }

    public static class AdjacentReflector extends Adjacent {
        public AdjacentReflector(int amount, CountType countType, AdjacencyType adjType) {
            super("reflector", amount, countType, adjType);
        }

        @Override
        public boolean satisfied(AbstractFissionEntity part, Direction dir, boolean simulate) {
            return isActiveReflector(part.getMultiblockController(), part.getBlockPos().relative(dir), simulate);
        }
    }

    public static class AdjacentIrradiator extends Adjacent {
        public AdjacentIrradiator(int amount, CountType countType, AdjacencyType adjType) {
            super("irradiator", amount, countType, adjType);
        }

        @Override
        public boolean satisfied(AbstractFissionEntity part, Direction dir, boolean simulate) {
            return isFunctionalIrradiator(part.getMultiblockController(), part.getBlockPos().relative(dir), simulate);
        }
    }

    public static class AdjacentShield extends Adjacent {
        public AdjacentShield(int amount, CountType countType, AdjacencyType adjType) {
            super("shield", amount, countType, adjType);
        }

        @Override
        public boolean satisfied(AbstractFissionEntity part, Direction dir, boolean simulate) {
            return isFunctionalShield(part.getMultiblockController(), part.getBlockPos().relative(dir), simulate);
        }
    }

    public static class AdjacentCell extends Adjacent {
        public AdjacentCell(int amount, CountType countType, AdjacencyType adjType) {
            super("cell", amount, countType, adjType);
        }

        @Override
        public boolean satisfied(AbstractFissionEntity part, Direction dir, boolean simulate) {
            return isFunctionalCell(part.getMultiblockController(), part.getBlockPos().relative(dir), simulate);
        }
    }

    public static class AdjacentSink extends Adjacent {
        public final String sinkType;

        public AdjacentSink(int amount, CountType countType, AdjacencyType adjType, String sinkType) {
            super(sinkType + "_sink", amount, countType, adjType);
            this.sinkType = sinkType;
        }

        @Override
        public void checkIsRuleAllowed(String ruleID) {
            super.checkIsRuleAllowed(ruleID);
            if (countType != CountType.AT_LEAST && sinkType.equals("any")) {
                throw new IllegalArgumentException((countType == CountType.EXACTLY ? "Exact 'any sink'" : "'At most n of any sink'") + " placement rule with ID \"" + ruleID + "\" is disallowed due to potential ambiguity during rule checks!");
            }
        }

        @Override
        public boolean satisfied(AbstractFissionEntity part, Direction dir, boolean simulate) {
            return isValidSink(part.getMultiblockController(), part.getBlockPos().relative(dir), sinkType, simulate);
        }
    }

    public static class AdjacentVessel extends Adjacent {
        public AdjacentVessel(int amount, CountType countType, AdjacencyType adjType) {
            super("vessel", amount, countType, adjType);
        }

        @Override
        public boolean satisfied(AbstractFissionEntity part, Direction dir, boolean simulate) {
            return isFunctionalVessel(part.getMultiblockController(), part.getBlockPos().relative(dir), simulate);
        }
    }

    public static class AdjacentHeater extends Adjacent {
        public final String heaterType;

        public AdjacentHeater(int amount, CountType countType, AdjacencyType adjType, String heaterType) {
            super(heaterType + "_heater", amount, countType, adjType);
            this.heaterType = heaterType;
        }

        @Override
        public void checkIsRuleAllowed(String ruleID) {
            super.checkIsRuleAllowed(ruleID);
            if (countType != CountType.AT_LEAST && heaterType.equals("any")) {
                throw new IllegalArgumentException((countType == CountType.EXACTLY ? "Exact 'any heater'" : "'At most n of any heater'") + " placement rule with ID \"" + ruleID + "\" is disallowed due to potential ambiguity during rule checks!");
            }
        }

        @Override
        public boolean satisfied(AbstractFissionEntity part, Direction dir, boolean simulate) {
            return isValidHeater(part.getMultiblockController(), part.getBlockPos().relative(dir), heaterType, simulate);
        }
    }

    // Helper Methods

    public static boolean isCasing(Optional<FissionReactor> reactor, BlockPos pos, boolean simulate) {
        if (reactor.isEmpty()) return false;
        BlockEntity tile = reactor.get().getWorld().getBlockEntity(pos);
        return tile instanceof AbstractFissionEntity part && !(part.getPartPosition() == PartPosition.Interior);
    }

    public static boolean isConductor(Optional<FissionReactor> reactor, BlockPos pos, boolean simulate) {
        return reactor.filter(fissionReactor -> fissionReactor.getPartMap(FissionConductorEntity.class).get(pos.asLong()) != null).isPresent();
    }

    public static boolean isActiveModerator(Optional<FissionReactor> reactor, BlockPos pos, boolean simulate) {
        if (reactor.isEmpty()) return false;
        IFissionComponent component = reactor.get().getPartMap(IFissionComponent.class).get(pos.asLong());
        return (component != null && component.isActiveModerator()) || (reactor.get().activeModeratorCache.contains(pos.asLong()) && RecipeHelper.blockRecipe(NCRecipes.fission_moderator, reactor.get().getWorld(), pos) != null);
    }

    public static boolean isActiveReflector(Optional<FissionReactor> reactor, BlockPos pos, boolean simulate) {
        if (reactor.isEmpty()) return false;
        return reactor.get().activeReflectorCache.contains(pos.asLong()) && RecipeHelper.blockRecipe(NCRecipes.fission_reflector, reactor.get().getWorld(), pos) != null;
    }

    public static boolean isFunctionalIrradiator(Optional<FissionReactor> reactor, BlockPos pos, boolean simulate) {
        if (reactor.isEmpty()) return false;
        FissionIrradiatorEntity irradiator = reactor.get().getPartMap(FissionIrradiatorEntity.class).get(pos.asLong());
        return irradiator != null && irradiator.isFunctional(simulate);
    }

    public static boolean isFunctionalShield(Optional<FissionReactor> reactor, BlockPos pos, boolean simulate) {
        if (reactor.isEmpty()) return false;
        FissionShieldEntity shield = reactor.get().getPartMap(FissionShieldEntity.class).get(pos.asLong());
        return shield != null && shield.isFunctional(simulate);
    }

    public static boolean isFunctionalCell(Optional<FissionReactor> reactor, BlockPos pos, boolean simulate) {
        if (reactor.isEmpty()) return false;
        SolidFissionCellEntity cell = reactor.get().getPartMap(SolidFissionCellEntity.class).get(pos.asLong());
        return cell != null && cell.isFunctional(simulate);
    }

    public static boolean isValidSink(Optional<FissionReactor> reactor, BlockPos pos, String sinkType, boolean simulate) {
        if (reactor.isEmpty()) return false;
        SolidFissionHeatSinkEntity sink = reactor.get().getPartMap(SolidFissionHeatSinkEntity.class).get(pos.asLong());
        return sink != null && sink.isFunctional(simulate) && (sinkType.equals("any") || sink.heatSinkType.getName().equals(sinkType));
    }

    public static boolean isFunctionalVessel(Optional<FissionReactor> reactor, BlockPos pos, boolean simulate) {
        if (reactor.isEmpty()) return false;
        SaltFissionVesselEntity vessel = reactor.get().getPartMap(SaltFissionVesselEntity.class).get(pos.asLong());
        return vessel != null && vessel.isFunctional(simulate);
    }

    public static boolean isValidHeater(Optional<FissionReactor> reactor, BlockPos pos, String heaterType, boolean simulate) {
        if (reactor.isEmpty()) return false;
        SaltFissionHeaterEntity heater = reactor.get().getPartMap(SaltFissionHeaterEntity.class).get(pos.asLong());
        return heater != null && heater.isFunctional(simulate) && (heaterType.equals("any") || heater.heaterType.getName().equals(heaterType));
    }

    // Default Tooltip Builder

    public static class DefaultTooltipBuilder extends PlacementRule.DefaultTooltipBuilder<FissionReactor, AbstractFissionEntity> {
    }

    // Recipe Handler

//	public static class RecipeHandler extends PlacementRule.RecipeHandler {
//
//		public RecipeHandler() {
//			super("fission");
//		}
//	}
}
