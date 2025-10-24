package com.nred.nuclearcraft.helpers;

import net.minecraft.core.Direction;

public interface IProcessorHandlerConfigs { // TODO REMOVE
    void createOutputSettings();

    void createSideConfig(int inputs, int outputs);

    void outputSetting(int index, boolean left);

    void sideConfig(int index, Direction dir, int inputs, boolean left);

    SideConfigEnums.OutputSetting getOutputSetting(int slot);

    SideConfigEnums.SideConfigSetting getSideConfig(int slot, Direction direction);

}
