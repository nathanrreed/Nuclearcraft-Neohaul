package com.nred.nuclearcraft.handler;

public class FissionEmergencyCoolingRecipes extends BasicRecipeHandler {

    public FissionEmergencyCoolingRecipes() {
        super("fission_emergency_cooling", 0, 1, 0, 1);
    }

//	@Override TODO
//	public void addRecipes() {
//		addRecipe(fluidStack("emergency_coolant", 1), fluidStack("emergency_coolant_heated", 1), fission_emergency_cooling_multiplier);
//	}
//
//	@Override
//	protected List<Object> fixedExtras(List<Object> extras) {
//		ExtrasFixer fixer = new ExtrasFixer(extras);
//		fixer.add(Double.class, 1D);
//		return fixer.fixed;
//	}
}