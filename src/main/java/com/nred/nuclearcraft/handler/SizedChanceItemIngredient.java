package com.nred.nuclearcraft.handler;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.nred.nuclearcraft.util.NCMath;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.tags.TagKey;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.common.crafting.SizedIngredient;
import net.neoforged.neoforge.common.util.NeoForgeExtraCodecs;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Stream;

public class SizedChanceItemIngredient {
    public static final Codec<SizedChanceItemIngredient> FLAT_CODEC = RecordCodecBuilder.create(instance -> instance.group(
                    Ingredient.MAP_CODEC_NONEMPTY.forGetter(SizedChanceItemIngredient::ingredient),
                    NeoForgeExtraCodecs.optionalFieldAlwaysWrite(ExtraCodecs.POSITIVE_INT, "count", 1).forGetter(SizedChanceItemIngredient::count),
                    ExtraCodecs.POSITIVE_INT.optionalFieldOf("chancePercent", 100).forGetter(SizedChanceItemIngredient::chancePercent),
                    ExtraCodecs.POSITIVE_INT.optionalFieldOf("minStackSize", 0).forGetter(SizedChanceItemIngredient::minStackSize))
            .apply(instance, SizedChanceItemIngredient::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, SizedChanceItemIngredient> STREAM_CODEC = StreamCodec.composite(
            Ingredient.CONTENTS_STREAM_CODEC, SizedChanceItemIngredient::ingredient,
            ByteBufCodecs.VAR_INT, SizedChanceItemIngredient::count,
            ByteBufCodecs.VAR_INT, SizedChanceItemIngredient::chancePercent,
            ByteBufCodecs.VAR_INT, SizedChanceItemIngredient::minStackSize,
            SizedChanceItemIngredient::new);

    private final Ingredient ingredient;
    private final int count;
    private final int chancePercent;
    private final int minStackSize;

    @Nullable
    private ItemStack[] cachedStacks;

    public SizedChanceItemIngredient(Ingredient ingredient, int count, int chancePercent, int minStackSize) {
        if (count <= 0) {
            throw new IllegalArgumentException("Size must be positive");
        }
        if (chancePercent <= 0) {
            throw new IllegalArgumentException("Chance must be positive");
        }
        if (minStackSize < 0) {
            throw new IllegalArgumentException("Min size must be positive");
        }

        this.ingredient = ingredient;
        this.count = count;
        this.chancePercent = chancePercent;
        this.minStackSize = minStackSize;
    }

    public SizedChanceItemIngredient(Ingredient ingredient, int count) {
        if (count <= 0) {
            throw new IllegalArgumentException("Size must be positive");
        }

        this.ingredient = ingredient;
        this.count = count;
        this.chancePercent = 100;
        this.minStackSize = 0;
    }

    public static SizedChanceItemIngredient of(ItemLike item, int count) {
        return new SizedChanceItemIngredient(Ingredient.of(item), count);
    }

    public static SizedChanceItemIngredient of(ItemLike item, int count, int chancePercent) {
        return new SizedChanceItemIngredient(Ingredient.of(item), count, chancePercent, 0);
    }

    public static SizedChanceItemIngredient of(TagKey<Item> tag, int count) {
        return new SizedChanceItemIngredient(Ingredient.of(tag), count);
    }

    public static SizedChanceItemIngredient of(TagKey<Item> tag, int count, int chancePercent) {
        return new SizedChanceItemIngredient(Ingredient.of(tag), count, chancePercent, 0);
    }

    public Ingredient ingredient() {
        return ingredient;
    }

    public SizedIngredient sized() {
        return new SizedIngredient(ingredient, count);
    }

    public int count() {
        return count;
    }

    public int chancePercent() {
        return chancePercent;
    }

    public int minStackSize() {
        return minStackSize;
    }

    public boolean test(ItemStack stack) {
        return ingredient.test(stack) && stack.getCount() >= count;
    }

    public ItemStack[] getItems() {
        getItemsRaw();
        if (chancePercent < 100) {
            return Arrays.stream(cachedStacks).map(s -> s.copyWithCount(minStackSize + NCMath.getBinomial(count, chancePercent))).toArray(ItemStack[]::new);
        }
        return cachedStacks;
    }

    public ItemStack[] getItemsRaw() {
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
        if (!(o instanceof SizedChanceItemIngredient other)) return false;
        return count == other.count && chancePercent == other.chancePercent && minStackSize == other.minStackSize && ingredient.equals(other.ingredient);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ingredient, count, chancePercent, minStackSize);
    }

    @Override
    public String toString() {
        return count + "x " + ingredient + " [ " + chancePercent + "%, min: " + minStackSize + " ]";
    }
}