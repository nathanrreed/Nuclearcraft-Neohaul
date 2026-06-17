package com.nred.nuclearcraft.compat.kubejs.custom_processor;

import com.nred.nuclearcraft.recipe.processor.ProcessorRecipeDyn;
import dev.latvian.mods.kubejs.registry.BuilderBase;
import dev.latvian.mods.rhino.util.ReturnsSelf;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.neoforge.registries.DeferredHolder;

import static com.nred.nuclearcraft.registration.RecipeTypeRegistration.PROCESSOR_RECIPE_DYN_TYPES;

@ReturnsSelf
public class CustomProcessorRecipeBuilder extends BuilderBase<RecipeType<ProcessorRecipeDyn>> {
    public CustomProcessorRecipeBuilder(ResourceLocation i) {
        super(i);
    }

    @Override
    public RecipeType<ProcessorRecipeDyn> createObject() {
        PROCESSOR_RECIPE_DYN_TYPES.put(id.toString(), DeferredHolder.create(Registries.RECIPE_TYPE, id));
        return RecipeType.simple(id);
    }
}