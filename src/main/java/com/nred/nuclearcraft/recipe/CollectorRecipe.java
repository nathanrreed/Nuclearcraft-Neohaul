package com.nred.nuclearcraft.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.nred.nuclearcraft.handler.SizedChanceFluidIngredient;
import com.nred.nuclearcraft.handler.SizedChanceItemIngredient;
import com.nred.nuclearcraft.util.NCMath;
import com.nred.nuclearcraft.util.UnitHelper;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.neoforge.fluids.crafting.FluidIngredient;
import org.jspecify.annotations.NonNull;

import java.util.List;
import java.util.Optional;

import static com.nred.nuclearcraft.config.NCConfig.processor_passive_rate;
import static com.nred.nuclearcraft.registration.BlockRegistration.COLLECTOR_MAP;
import static com.nred.nuclearcraft.registration.RecipeSerializerRegistration.COLLECTOR_RECIPE_SERIALIZER;
import static com.nred.nuclearcraft.registration.RecipeTypeRegistration.*;

public class CollectorRecipe extends BasicRecipe { // TODO should this be a DataMap?
    private final String typeName;

    public CollectorRecipe(String typeName, SizedChanceItemIngredient itemProduct, SizedChanceFluidIngredient fluidProduct) {
        super(List.of(), List.of(), itemProduct == null ? List.of() : List.of(itemProduct), fluidProduct == null ? List.of() : List.of(fluidProduct));
        this.typeName = typeName;
    }

    public CollectorRecipe(String typeName, Ingredient itemProduct) {
        this(typeName, new SizedChanceItemIngredient(itemProduct, 1), null);
    }

    public CollectorRecipe(String typeName, FluidIngredient fluidProduct) {
        this(typeName, null, new SizedChanceFluidIngredient(fluidProduct, 1));
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

    @Override
    public @NonNull ItemStack getToastSymbol() {
        return COLLECTOR_MAP.get(this.typeName).toStack();
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
                SizedChanceItemIngredient.FLAT_CODEC.lenientOptionalFieldOf("itemProduct").forGetter(e -> Optional.ofNullable(e.getItemProducts().isEmpty() ? null : e.getItemProduct())),
                SizedChanceFluidIngredient.FLAT_CODEC.lenientOptionalFieldOf("fluidProduct").forGetter(e -> Optional.ofNullable(e.getFluidProducts().isEmpty() ? null : e.getFluidProduct()))
        ).apply(inst, (typeName, itemProduct, fluidProduct) -> new CollectorRecipe(typeName, itemProduct.orElse(null), fluidProduct.orElse(null))));

        public static StreamCodec<RegistryFriendlyByteBuf, CollectorRecipe> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.STRING_UTF8, CollectorRecipe::getTypeName,
                SizedChanceItemIngredient.STREAM_CODEC, e -> e.getItemProducts().isEmpty() ? SizedChanceItemIngredient.EMPTY : e.getItemProduct(),
                SizedChanceFluidIngredient.STREAM_CODEC, e -> e.getFluidProducts().isEmpty() ? SizedChanceFluidIngredient.EMPTY : e.getFluidProduct(),
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