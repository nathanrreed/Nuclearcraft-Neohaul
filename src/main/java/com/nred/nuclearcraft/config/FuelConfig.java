package com.nred.nuclearcraft.config;

import java.util.List;

public record FuelConfig(List<Integer> fuel_time, List<Integer> heat_generation, List<Double> efficiency, List<Double> criticality, List<Double> decay_factor, List<Boolean> self_priming) {
}
