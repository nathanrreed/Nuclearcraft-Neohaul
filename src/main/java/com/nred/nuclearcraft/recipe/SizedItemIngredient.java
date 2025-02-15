package com.nred.nuclearcraft.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.tags.TagKey;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.common.util.NeoForgeExtraCodecs;
import net.neoforged.neoforge.network.connection.ConnectionType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public class SizedItemIngredient {
    public static final Codec<SizedItemIngredient> FLAT_CODEC = RecordCodecBuilder.create(instance -> instance.group(
                    Ingredient.MAP_CODEC_NONEMPTY.forGetter(SizedItemIngredient::ingredient),
                    NeoForgeExtraCodecs.optionalFieldAlwaysWrite(ExtraCodecs.POSITIVE_INT, "count", 1).forGetter(SizedItemIngredient::count))
            .apply(instance, SizedItemIngredient::new));

    public static final Codec<SizedItemIngredient> NESTED_CODEC = RecordCodecBuilder.create(instance -> instance.group(
                    Ingredient.CODEC_NONEMPTY.fieldOf("ingredient").forGetter(SizedItemIngredient::ingredient),
                    NeoForgeExtraCodecs.optionalFieldAlwaysWrite(ExtraCodecs.POSITIVE_INT, "count", 1).forGetter(SizedItemIngredient::count))
            .apply(instance, SizedItemIngredient::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, SizedItemIngredient> STREAM_CODEC = StreamCodec.composite(
            Ingredient.CONTENTS_STREAM_CODEC,
            SizedItemIngredient::ingredient,
            ByteBufCodecs.VAR_INT,
            SizedItemIngredient::count,
            SizedItemIngredient::new);

    public static final StreamCodec<RegistryFriendlyByteBuf, List<SizedItemIngredient>> LIST_STREAM_CODEC = new StreamCodec<>() {
        @Override
        public @NotNull List<SizedItemIngredient> decode(RegistryFriendlyByteBuf buffer) {
            return Arrays.stream(buffer.readArray(SizedItemIngredient[]::new, buf -> SizedItemIngredient.STREAM_CODEC.decode(new RegistryFriendlyByteBuf(buf, buffer.registryAccess(), ConnectionType.NEOFORGE)))).toList();
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buffer, List<SizedItemIngredient> value) {
            buffer.writeArray(value.toArray(), (buf, ing) -> SizedItemIngredient.STREAM_CODEC.encode(new RegistryFriendlyByteBuf(buf, buffer.registryAccess(), ConnectionType.NEOFORGE), ((SizedItemIngredient) ing)));
        }
    };

    public static SizedItemIngredient of(ItemLike item, int count) {
        return new SizedItemIngredient(Ingredient.of(item), count);
    }

    public static SizedItemIngredient of(ItemStack stack) {
        return new SizedItemIngredient(Ingredient.of(stack), stack.getCount());
    }

    public static SizedItemIngredient of(TagKey<Item> tag, int count) {
        return new SizedItemIngredient(Ingredient.of(tag), count);
    }

    public static SizedItemIngredient tags(List<TagKey<Item>> tags, int count) {
        return new SizedItemIngredient(Ingredient.fromValues(tags.stream().map(Ingredient.TagValue::new)), count);
    }

    private final Ingredient ingredient;
    private final int count;

    @Nullable
    private ItemStack[] cachedStacks;

    public SizedItemIngredient(Ingredient ingredient, int count) {
        if (count <= 0) {
            throw new IllegalArgumentException("Size must be positive");
        }
        this.ingredient = ingredient;
        this.count = count;
    }

    public Ingredient ingredient() {
        return ingredient;
    }

    public int count() {
        return count;
    }

    public boolean test(ItemStack stack) {
        return ingredient.test(stack) && stack.getCount() >= count;
    }

    public boolean test(List<ItemStack> stacks) {
        return stacks.stream().anyMatch(stack -> ingredient.test(stack) && stack.getCount() >= count);
    }

    public ItemStack[] getItems() {
        if (cachedStacks == null) {
            cachedStacks = Stream.of(ingredient.getItems())
                    .map(s -> s.copyWithCount(count))
                    .toArray(ItemStack[]::new);
        }
        return cachedStacks;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SizedItemIngredient other)) return false;
        return count == other.count && ingredient.equals(other.ingredient);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ingredient, count);
    }

    @Override
    public String toString() {
        return count + "x " + ingredient;
    }
}