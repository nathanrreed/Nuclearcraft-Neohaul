package com.nred.nuclearcraft.compat.jei;

import com.nred.nuclearcraft.compat.recipe_viewer.RecipeViewerImpl.PebbleFissionCoolerRecipeViewer;
import com.nred.nuclearcraft.recipe.fission.PebbleFissionCoolerRecipe;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NonNull;

import static com.nred.nuclearcraft.helpers.Location.ncLoc;
import static com.nred.nuclearcraft.registration.BlockRegistration.FISSION_REACTOR_MAP;

public class JeiGasCoolingCategory extends JeiRecipeViewerCategory<PebbleFissionCoolerRecipe> {
    protected static final ResourceLocation UID = ncLoc("gas_cooling");
    private static final RecipeType<PebbleFissionCoolerRecipe> TYPE = new RecipeType<>(UID, PebbleFissionCoolerRecipe.class);

    public JeiGasCoolingCategory(IGuiHelper helper) {
        super(helper, UID.getPath(), PebbleFissionCoolerRecipeViewer.class);
    }

    @Override
    public @NonNull RecipeType<PebbleFissionCoolerRecipe> getRecipeType() {
        return TYPE;
    }

    @Override
    public @Nullable IDrawable getIcon() {
        return helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(FISSION_REACTOR_MAP.get("oxygen_fission_gas_cooler").get()));
    }
}