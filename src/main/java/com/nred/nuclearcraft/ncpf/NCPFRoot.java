package com.nred.nuclearcraft.ncpf;

import java.util.HashMap;
import java.util.Map;

public class NCPFRoot extends NCPFObject {
    public static final int NCPF_VERSION = 1;

    public int version = NCPF_VERSION;

    public final Map<String, Object> configuration = new HashMap<>();
}