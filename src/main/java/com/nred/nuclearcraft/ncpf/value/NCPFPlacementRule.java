package com.nred.nuclearcraft.ncpf.value;

import com.nred.nuclearcraft.ncpf.element.NCPFElement;

import java.util.ArrayList;
import java.util.List;

public class NCPFPlacementRule {
    public NCPFPlacementRuleType type;
    public NCPFElement block;
    public int min;
    public int max;
    public final List<NCPFPlacementRule> rules = new ArrayList<>();
}