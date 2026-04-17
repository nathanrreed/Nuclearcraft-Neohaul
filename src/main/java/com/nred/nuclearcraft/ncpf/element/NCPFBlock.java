package com.nred.nuclearcraft.ncpf.element;

import java.util.HashMap;
import java.util.Map;

public class NCPFBlock extends NCPFElement {
    public String name;
    public final Map<String, Object> blockstate = new HashMap<>();
    public String nbt;

    public NCPFBlock() {
        super("block");
    }
}