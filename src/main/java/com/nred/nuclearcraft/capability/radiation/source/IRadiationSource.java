package com.nred.nuclearcraft.capability.radiation.source;

import com.nred.nuclearcraft.capability.ICapability;
import com.nred.nuclearcraft.capability.radiation.IRadiation;

public interface IRadiationSource extends IRadiation, ICapability<IRadiationSource> {
    double getRadiationBuffer();

    void setRadiationBuffer(double newBuffer);

    double getScrubbingFraction();

    void setScrubbingFraction(double newFraction);

    double getEffectiveScrubberCount();

    void setEffectiveScrubberCount(double newScrubberCount);
}