package com.nred.nuclearcraft.block_entity.radiation;

import com.nred.nuclearcraft.block_entity.ITile;
import com.nred.nuclearcraft.radiation.environment.RadiationEnvironmentInfo;
import com.nred.nuclearcraft.util.FourPos;

public interface ITileRadiationEnvironment extends ITile {
	default FourPos getFourPos() {
		return new FourPos(getTilePos(), getTileWorld().dimension());
	}
	
	void checkRadiationEnvironmentInfo();
	
	void handleRadiationEnvironmentInfo(RadiationEnvironmentInfo info);
	
	double getRadiationContributionFraction();
	
	double getCurrentChunkRadiationLevel();
	
	void setCurrentChunkRadiationLevel(double level);
	
	double getCurrentChunkRadiationBuffer();
	
	void setCurrentChunkRadiationBuffer(double buffer);
}
