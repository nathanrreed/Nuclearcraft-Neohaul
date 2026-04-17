package com.nred.nuclearcraft.ncpf.element;

import java.util.ArrayList;
import java.util.List;

public class NCPFListElement extends NCPFElement {
    public final List<NCPFElement> elements = new ArrayList<>();

    public NCPFListElement() {
        super("list");
    }
}