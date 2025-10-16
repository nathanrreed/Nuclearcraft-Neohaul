package com.nred.nuclearcraft.block_entity.fission;

public interface IFissionSpecialPart extends IFissionPart {
	
	/**
	 * Called after all flux and cluster-searching is complete.
	 */
	void postClusterSearch(boolean simulate);
}
