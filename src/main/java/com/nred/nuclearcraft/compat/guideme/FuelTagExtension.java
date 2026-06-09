package com.nred.nuclearcraft.compat.guideme;

import com.nred.nuclearcraft.recipe.NCRecipes;
import com.nred.nuclearcraft.recipe.SizedChanceFluidIngredient;
import com.nred.nuclearcraft.recipe.SizedChanceItemIngredient;
import com.nred.nuclearcraft.recipe.fission.BasicFissionRecipe;
import guideme.compiler.PageCompiler;
import guideme.compiler.tags.FlowTagCompiler;
import guideme.compiler.tags.MdxAttrs;
import guideme.document.flow.LytFlowParent;
import guideme.libs.mdast.mdx.model.MdxJsxElementFields;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Set;

import static com.nred.nuclearcraft.NuclearcraftNeohaul.MODID;

public class FuelTagExtension extends FlowTagCompiler {
    @Override
    public Set<String> getTagNames() {
        return Set.of(MODID + ":FuelInfo");
    }

    @Override
    protected void compile(PageCompiler compiler, LytFlowParent parent, MdxJsxElementFields el) {
        ResourceLocation id = MdxAttrs.getRequiredId(compiler, parent, el, "id");
        var value = el.getAttributeString("value", "");

        if (value.isEmpty()) {
            parent.appendError(compiler, "value is required", el);
            return;
        } else if (!value.startsWith("get")) {
            parent.appendError(compiler, "value is not a getter", el);
            return;
        }

        BasicFissionRecipe recipe = null;

        Item resultItem = BuiltInRegistries.ITEM.getOptional(id).orElse(null);
        if (resultItem != null) {
            recipe = NCRecipes.solid_fission.getRecipeFromIngredients(Minecraft.getInstance().level, List.of(SizedChanceItemIngredient.of(resultItem, 1)), List.of());
            if (recipe == null) {
                recipe = NCRecipes.pebble_fission.getRecipeFromIngredients(Minecraft.getInstance().level, List.of(SizedChanceItemIngredient.of(resultItem, 1)), List.of());
            }
        }

        var resultFluid = BuiltInRegistries.FLUID.getOptional(id).orElse(null);
        if (resultFluid != null && recipe == null) {
            recipe = NCRecipes.salt_fission.getRecipeFromIngredients(Minecraft.getInstance().level, List.of(), List.of(SizedChanceFluidIngredient.of(resultFluid, 1)));
        }

        if (recipe == null) {
            parent.appendError(compiler, "No recipe found for: " + id, el);
            return;
        }

        try {
            parent.appendText(recipe.getClass().getMethod(value).invoke(recipe).toString());
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            parent.appendError(compiler, "value " + value + " is not a field of Recipe", el);
        }
    }
}