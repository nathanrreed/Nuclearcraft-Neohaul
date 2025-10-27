package com.nred.nuclearcraft.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.nred.nuclearcraft.util.NCMath;
import com.nred.nuclearcraft.util.UnitHelper;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.neoforge.common.crafting.SizedIngredient;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;

import java.util.List;

import static com.nred.nuclearcraft.config.NCConfig.processor_passive_rate;
import static com.nred.nuclearcraft.registration.RecipeSerializerRegistration.COLLECTOR_RECIPE_SERIALIZER;
import static com.nred.nuclearcraft.registration.RecipeTypeRegistration.*;
import static com.nred.nuclearcraft.util.StreamCodecsHelper.SIZED_FLUID_INGREDIENT_LIST_STREAM_CODEC;
import static com.nred.nuclearcraft.util.StreamCodecsHelper.SIZED_ITEM_INGREDIENT_LIST_STREAM_CODEC;

public class CollectorRecipe extends BasicRecipe {
    private final String typeName;

    public CollectorRecipe(String typeName, List<SizedIngredient> itemProduct, List<SizedFluidIngredient> fluidProduct) {
        super(List.of(), List.of(), itemProduct, fluidProduct);
        this.typeName = typeName;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return COLLECTOR_RECIPE_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return switch (this.typeName) {
            case "cobblestone_generator" -> COBBLE_GENERATOR_RECIPE_TYPE.get();
            case "cobblestone_generator_compact" -> COBBLE_GENERATOR_COMPACT_RECIPE_TYPE.get();
            case "cobblestone_generator_dense" -> COBBLE_GENERATOR_DENSE_RECIPE_TYPE.get();
            case "water_source" -> WATER_SOURCE_RECIPE_TYPE.get();
            case "water_source_compact" -> WATER_SOURCE_COMPACT_RECIPE_TYPE.get();
            case "water_source_dense" -> WATER_SOURCE_DENSE_RECIPE_TYPE.get();
            case "nitrogen_collector" -> NITROGEN_COLLECTOR_RECIPE_TYPE.get();
            case "nitrogen_collector_compact" -> NITROGEN_COLLECTOR_COMPACT_RECIPE_TYPE.get();
            case "nitrogen_collector_dense" -> NITROGEN_COLLECTOR_DENSE_RECIPE_TYPE.get();
            default -> throw new IllegalStateException("Unexpected value: " + this.typeName);
        };
    }

    public String getCollectorProductionRate() {
        return switch (this.typeName) {
            case "cobblestone_generator" -> NCMath.sigFigs(processor_passive_rate[0], 5) + " C/t";
            case "cobblestone_generator_compact" -> NCMath.sigFigs(processor_passive_rate[0] * 8, 5) + " C/t";
            case "cobblestone_generator_dense" -> NCMath.sigFigs(processor_passive_rate[0] * 64, 5) + " C/t";
            case "water_source" -> UnitHelper.prefix(processor_passive_rate[1], 5, "B/t", -1);
            case "water_source_compact" -> UnitHelper.prefix(processor_passive_rate[1] * 8, 5, "B/t", -1);
            case "water_source_dense" -> UnitHelper.prefix(processor_passive_rate[1] * 64, 5, "B/t", -1);
            case "nitrogen_collector" -> UnitHelper.prefix(processor_passive_rate[2], 5, "B/t", -1);
            case "nitrogen_collector_compact" -> UnitHelper.prefix(processor_passive_rate[2] * 8, 5, "B/t", -1);
            case "nitrogen_collector_dense" -> UnitHelper.prefix(processor_passive_rate[2] * 64, 5, "B/t", -1);
            default -> throw new IllegalStateException("Unexpected value: " + this.typeName);
        };
    }

    public int getLevel() {
        if (this.typeName.endsWith("_dense")) {
            return 64;
        } else if (this.typeName.endsWith("_compact")) {
            return 8;
        } else {
            return 1;
        }
    }

    public String getTypeName() {
        return typeName;
    }

    public static class Serializer implements RecipeSerializer<CollectorRecipe> {
        public static MapCodec<CollectorRecipe> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
                Codec.STRING.fieldOf("typeName").forGetter(CollectorRecipe::getTypeName),
                SizedIngredient.FLAT_CODEC.listOf().fieldOf("itemProduct").forGetter(CollectorRecipe::getItemProducts),
                SizedFluidIngredient.FLAT_CODEC.listOf().fieldOf("fluidProduct").forGetter(CollectorRecipe::getFluidProducts)
        ).apply(inst, CollectorRecipe::new));

        public static StreamCodec<RegistryFriendlyByteBuf, CollectorRecipe> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.STRING_UTF8, CollectorRecipe::getTypeName,
                SIZED_ITEM_INGREDIENT_LIST_STREAM_CODEC, CollectorRecipe::getItemProducts,
                SIZED_FLUID_INGREDIENT_LIST_STREAM_CODEC, CollectorRecipe::getFluidProducts,
                CollectorRecipe::new
        );

        @Override
        public MapCodec<CollectorRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, CollectorRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}