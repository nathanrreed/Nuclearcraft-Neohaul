package com.nred.nuclearcraft.handler;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.nred.nuclearcraft.util.NCMath;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.tags.TagKey;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.common.util.NeoForgeExtraCodecs;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.fluids.crafting.FluidIngredient;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Stream;

public class SizedChanceFluidIngredient {
    public static final Codec<SizedChanceFluidIngredient> FLAT_CODEC = RecordCodecBuilder.create(instance -> instance.group(
                    FluidIngredient.MAP_CODEC_NONEMPTY.forGetter(SizedChanceFluidIngredient::ingredient),
                    NeoForgeExtraCodecs.optionalFieldAlwaysWrite(ExtraCodecs.POSITIVE_INT, "amount", FluidType.BUCKET_VOLUME).forGetter(SizedChanceFluidIngredient::amount),
                    ExtraCodecs.POSITIVE_INT.optionalFieldOf("chancePercent", 100).forGetter(SizedChanceFluidIngredient::chancePercent),
                    ExtraCodecs.POSITIVE_INT.optionalFieldOf("minStackSize", 0).forGetter(SizedChanceFluidIngredient::minStackSize))
            .apply(instance, SizedChanceFluidIngredient::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, SizedChanceFluidIngredient> STREAM_CODEC = StreamCodec.composite(
            FluidIngredient.STREAM_CODEC, SizedChanceFluidIngredient::ingredient,
            ByteBufCodecs.VAR_INT, SizedChanceFluidIngredient::amount,
            ByteBufCodecs.VAR_INT, SizedChanceFluidIngredient::chancePercent,
            ByteBufCodecs.VAR_INT, SizedChanceFluidIngredient::minStackSize,
            SizedChanceFluidIngredient::new);

    private final FluidIngredient ingredient;
    private final int amount;
    private final int chancePercent;
    private final int minStackSize;

    @Nullable
    private FluidStack[] cachedStacks;

    public SizedChanceFluidIngredient(FluidIngredient ingredient, int amount, int chancePercent, int minStackSize) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Size must be positive");
        }
        if (chancePercent <= 0) {
            throw new IllegalArgumentException("Chance must be positive");
        }
        if (minStackSize < 0) {
            throw new IllegalArgumentException("Min size must be positive");
        }

        this.ingredient = ingredient;
        this.amount = amount;
        this.chancePercent = chancePercent;
        this.minStackSize = minStackSize;
    }

    public SizedChanceFluidIngredient(FluidIngredient ingredient, int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Size must be positive");
        }
        this.ingredient = ingredient;
        this.amount = amount;
        this.chancePercent = 100;
        this.minStackSize = 0;
    }

    public static SizedChanceFluidIngredient of(Fluid fluid, int amount) {
        return new SizedChanceFluidIngredient(FluidIngredient.of(fluid), amount, 100, 0);
    }

    public static SizedChanceFluidIngredient of(Fluid fluid, int amount, int chancePercent, int minStackSize) {
        return new SizedChanceFluidIngredient(FluidIngredient.of(fluid), amount, chancePercent, minStackSize);
    }

    public static SizedChanceFluidIngredient of(FluidStack stack) {
        return new SizedChanceFluidIngredient(FluidIngredient.single(stack), stack.getAmount(), 100, 0);
    }

    public static SizedChanceFluidIngredient of(FluidStack stack, int chancePercent, int minStackSize) {
        return new SizedChanceFluidIngredient(FluidIngredient.single(stack), stack.getAmount(), chancePercent, minStackSize);
    }

    public static SizedChanceFluidIngredient of(TagKey<Fluid> tag, int amount) {
        return new SizedChanceFluidIngredient(FluidIngredient.tag(tag), amount, 100, 0);
    }

    public static SizedChanceFluidIngredient of(TagKey<Fluid> tag, int amount, int chancePercent, int minStackSize) {
        return new SizedChanceFluidIngredient(FluidIngredient.tag(tag), amount, chancePercent, minStackSize);
    }

    public FluidIngredient ingredient() {
        return ingredient;
    }

    public SizedFluidIngredient sized() {
        return new SizedFluidIngredient(ingredient, amount);
    }

    public int amount() {
        return amount;
    }

    public int chancePercent() {
        return chancePercent;
    }

    public int minStackSize() {
        return minStackSize;
    }

    public boolean test(FluidStack stack) {
        return ingredient.test(stack) && stack.getAmount() >= amount;
    }

    public FluidStack[] getFluids() {
        getFluidsRaw();
        if (chancePercent < 100) {
            return Arrays.stream(cachedStacks).map(s -> s.copyWithAmount(minStackSize + NCMath.getBinomial(amount, chancePercent))).toArray(FluidStack[]::new);
        }
        return cachedStacks;
    }

    public FluidStack getStack() {
        return Arrays.stream(getFluids()).findFirst().orElse(FluidStack.EMPTY);
    }

    public FluidStack[] getFluidsRaw() {
        if (cachedStacks == null) {
            cachedStacks = Stream.of(ingredient.getStacks())
                    .map(s -> s.copyWithAmount(amount))
                    .toArray(FluidStack[]::new);
        }
        return cachedStacks;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SizedChanceFluidIngredient other)) return false;
        return amount == other.amount && chancePercent == other.chancePercent && minStackSize == other.minStackSize && ingredient.equals(other.ingredient);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ingredient, amount, chancePercent, minStackSize);
    }

    @Override
    public String toString() {
        return amount + "x " + ingredient + " [ " + chancePercent + "%, min: " + minStackSize + " ]";
    }
}