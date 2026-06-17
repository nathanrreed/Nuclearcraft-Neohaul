package com.nred.nuclearcraft.compat.kubejs.custom_processor;

import com.nred.nuclearcraft.recipe.processor.ProcessorRecipeDyn;
import dev.latvian.mods.kubejs.registry.BuilderBase;
import dev.latvian.mods.rhino.util.ReturnsSelf;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.neoforged.neoforge.registries.DeferredHolder;

import static com.nred.nuclearcraft.registration.RecipeSerializerRegistration.PROCESSOR_RECIPE_SERIALIZERS_DYN;

@ReturnsSelf
public class CustomProcessorRecipeSerializerBuilder extends BuilderBase<RecipeSerializer<ProcessorRecipeDyn>> {
    public CustomProcessorRecipeSerializerBuilder(ResourceLocation i) {
        super(i);
    }

    @Override
    public RecipeSerializer<ProcessorRecipeDyn> createObject() {
        String name = id.toString();

        PROCESSOR_RECIPE_SERIALIZERS_DYN.put(name, DeferredHolder.create(Registries.RECIPE_SERIALIZER, id));

        return new ProcessorRecipeDyn.Serializer(name);
    }
}