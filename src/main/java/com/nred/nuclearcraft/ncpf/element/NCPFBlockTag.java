package com.nred.nuclearcraft.ncpf.element;

import java.util.HashMap;
import java.util.Map;

public class NCPFBlockTag extends NCPFElement {
    public String name;
    public final Map<String, Object> blockstate = new HashMap<>();
    public String nbt;

    public NCPFBlockTag() {
        super("block_tag");
    }
}