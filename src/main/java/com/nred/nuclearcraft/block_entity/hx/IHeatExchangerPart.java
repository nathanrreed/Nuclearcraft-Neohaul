package com.nred.nuclearcraft.block_entity.hx;

import com.nred.nuclearcraft.multiblock.ITileCuboidalLogicMultiblockPart;
import com.nred.nuclearcraft.multiblock.hx.HeatExchanger;
import com.nred.nuclearcraft.multiblock.hx.HeatExchangerLogic;

public interface IHeatExchangerPart extends ITileCuboidalLogicMultiblockPart<HeatExchanger, HeatExchangerLogic> {
	default void refreshHeatExchangerRecipe() {
		HeatExchangerLogic logic = getLogic();
		if (logic != null) {
			logic.refreshRecipe();
		}
	}
	
	default void refreshHeatExchangerActivity() {
		HeatExchangerLogic logic = getLogic();
		if (logic != null) {
			logic.refreshActivity();
		}
	}
}