package com.nred.nuclearcraft.registration;

import com.nred.nuclearcraft.recipe.collector.CollectorSerializer;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.neoforged.neoforge.registries.DeferredHolder;

import static com.nred.nuclearcraft.registration.Registers.RECIPE_SERIALIZERS;

public class RecipeSerializerRegistration {
public static final DeferredHolder<RecipeSerializer<?>, CollectorSerializer> COLLECTOR_RECIPE_SERIALIZER = RECIPE_SERIALIZERS.register("collector_recipe", CollectorSerializer::new);

    public static void init() {
    }
}