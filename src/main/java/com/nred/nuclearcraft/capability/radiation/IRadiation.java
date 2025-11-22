package com.nred.nuclearcraft.capability.radiation;

import static com.nred.nuclearcraft.config.NCConfig.radiation_lowest_rate;

public interface IRadiation {
	double getRadiationLevel();
	
	void setRadiationLevel(double newRadiationLevel);
	
	default boolean isRadiationNegligible() {
		return getRadiationLevel() < radiation_lowest_rate;
	}
}