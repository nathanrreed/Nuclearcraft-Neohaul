package com.nred.nuclearcraft.handler;

import com.nred.nuclearcraft.recipe.fission.FissionReflectorRecipe;

public class FissionReflectorRecipes extends BasicRecipeHandler<FissionReflectorRecipe> {

    public FissionReflectorRecipes() {
        super("fission_reflector", 1, 0, 0, 0);
    }

//	@Override
//	public void addRecipes() {
//		for (int i = 0; i < MetaEnums.NeutronReflectorType.values().length; ++i) {
//			addRecipe(new ItemStack(NCBlocks.fission_reflector, 1, i), fission_reflector_efficiency[i], fission_reflector_reflectivity[i]);
//		}
//	}
//
//	@Override
//	protected List<Object> fixedExtras(List<Object> extras) {
//		ExtrasFixer fixer = new ExtrasFixer(extras);
//		fixer.add(Double.class, 0D);
//		fixer.add(Double.class, 0D);
//		return fixer.fixed;
//	}
}