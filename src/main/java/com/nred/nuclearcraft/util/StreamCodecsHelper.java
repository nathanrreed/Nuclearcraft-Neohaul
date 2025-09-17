package com.nred.nuclearcraft.util;

import com.mojang.datafixers.util.Function8;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.neoforge.common.crafting.SizedIngredient;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;
import net.neoforged.neoforge.network.connection.ConnectionType;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

public class StreamCodecsHelper {
    public static final StreamCodec<RegistryFriendlyByteBuf, List<SizedFluidIngredient>> FLUID_INGREDIENT_LIST_STREAM_CODEC = new StreamCodec<>() {
        @Override
        public @NotNull List<SizedFluidIngredient> decode(RegistryFriendlyByteBuf buffer) {
            return Arrays.stream(buffer.readArray(SizedFluidIngredient[]::new, buf -> SizedFluidIngredient.STREAM_CODEC.decode(new RegistryFriendlyByteBuf(buf, buffer.registryAccess(), ConnectionType.NEOFORGE)))).toList();
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buffer, List<SizedFluidIngredient> value) {
            buffer.writeArray(value.toArray(), (buf, ing) -> SizedFluidIngredient.STREAM_CODEC.encode(new RegistryFriendlyByteBuf(buf, buffer.registryAccess(), ConnectionType.NEOFORGE), ((SizedFluidIngredient) ing)));
        }
    };
    public static final StreamCodec<RegistryFriendlyByteBuf, List<FluidStack>> FLUID_STACK_LIST_STREAM_CODEC = new StreamCodec<>() {
        @Override
        public @NotNull List<FluidStack> decode(RegistryFriendlyByteBuf buffer) {
            return Arrays.stream(buffer.readArray(FluidStack[]::new, buf -> FluidStack.OPTIONAL_STREAM_CODEC.decode(new RegistryFriendlyByteBuf(buf, buffer.registryAccess(), ConnectionType.NEOFORGE)))).toList();
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buffer, List<FluidStack> value) {
            buffer.writeArray(value.toArray(), (buf, ing) -> FluidStack.OPTIONAL_STREAM_CODEC.encode(new RegistryFriendlyByteBuf(buf, buffer.registryAccess(), ConnectionType.NEOFORGE), ((FluidStack) ing)));
        }
    };

    public static final StreamCodec<RegistryFriendlyByteBuf, List<SizedIngredient>> ITEM_INGREDIENT_LIST_STREAM_CODEC = new StreamCodec<>() {
        @Override
        public @NotNull List<SizedIngredient> decode(RegistryFriendlyByteBuf buffer) {
            return Arrays.stream(buffer.readArray(SizedIngredient[]::new, buf -> SizedIngredient.STREAM_CODEC.decode(new RegistryFriendlyByteBuf(buf, buffer.registryAccess(), ConnectionType.NEOFORGE)))).toList();
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buffer, List<SizedIngredient> value) {
            buffer.writeArray(value.toArray(), (buf, ing) -> SizedIngredient.STREAM_CODEC.encode(new RegistryFriendlyByteBuf(buf, buffer.registryAccess(), ConnectionType.NEOFORGE), ((SizedIngredient) ing)));
        }
    };

    public static <B, C, T1, T2, T3, T4, T5, T6, T7, T8> StreamCodec<B, C> composite(
            final StreamCodec<? super B, T1> codec1,
            final Function<C, T1> getter1,
            final StreamCodec<? super B, T2> codec2,
            final Function<C, T2> getter2,
            final StreamCodec<? super B, T3> codec3,
            final Function<C, T3> getter3,
            final StreamCodec<? super B, T4> codec4,
            final Function<C, T4> getter4,
            final StreamCodec<? super B, T5> codec5,
            final Function<C, T5> getter5,
            final StreamCodec<? super B, T6> codec6,
            final Function<C, T6> getter6,
            final StreamCodec<? super B, T7> codec7,
            final Function<C, T7> getter7,
            final StreamCodec<? super B, T8> codec8,
            final Function<C, T8> getter8,
            final Function8<T1, T2, T3, T4, T5, T6, T7, T8, C> p_331335_) {
        return new StreamCodec<>() {
            @Override
            public C decode(B p_330310_) {
                T1 t1 = codec1.decode(p_330310_);
                T2 t2 = codec2.decode(p_330310_);
                T3 t3 = codec3.decode(p_330310_);
                T4 t4 = codec4.decode(p_330310_);
                T5 t5 = codec5.decode(p_330310_);
                T6 t6 = codec6.decode(p_330310_);
                T7 t7 = codec7.decode(p_330310_);
                T8 t8 = codec8.decode(p_330310_);
                return p_331335_.apply(t1, t2, t3, t4, t5, t6, t7, t8);
            }

            @Override
            public void encode(B p_332052_, C p_331912_) {
                codec1.encode(p_332052_, getter1.apply(p_331912_));
                codec2.encode(p_332052_, getter2.apply(p_331912_));
                codec3.encode(p_332052_, getter3.apply(p_331912_));
                codec4.encode(p_332052_, getter4.apply(p_331912_));
                codec5.encode(p_332052_, getter5.apply(p_331912_));
                codec6.encode(p_332052_, getter6.apply(p_331912_));
                codec7.encode(p_332052_, getter7.apply(p_331912_));
                codec8.encode(p_332052_, getter8.apply(p_331912_));
            }
        };
    }
}
