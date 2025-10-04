package com.nred.nuclearcraft.multiblock;

import it.zerono.mods.zerocore.lib.multiblock.IMultiblockController;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;

import javax.annotation.Nonnull;
import java.util.function.UnaryOperator;

public interface ILogicMultiblock<MULTIBLOCK extends Multiblock<MULTIBLOCK> & ILogicMultiblock<MULTIBLOCK, LOGIC>, LOGIC extends MultiblockLogic<MULTIBLOCK, LOGIC>> extends IMultiblockController<MULTIBLOCK> {

    @Nonnull
    LOGIC getLogic();

    void setLogic(String logicID);

    default @Nonnull LOGIC getNewLogic(UnaryOperator<LOGIC> constructor) {
        return constructor.apply(getLogic());
    }

    default void writeLogicNBT(CompoundTag data, HolderLookup.Provider registries, SyncReason syncReason) {
        data.putString("logicID", getLogic().getID());
        CompoundTag logicTag = new CompoundTag();
        getLogic().writeToLogicTag(logicTag, registries, syncReason);
        data.put("logic", logicTag);
    }

    default void readLogicNBT(CompoundTag data, HolderLookup.Provider registries, SyncReason syncReason) {
        if (syncReason == SyncReason.FullSync && data.contains("logicID")) {
            setLogic(data.getString("logicID"));
        }
        if (data.contains("logic")) {
            getLogic().readFromLogicTag(data.getCompound("logic"), registries, syncReason);
        }
    }
}
