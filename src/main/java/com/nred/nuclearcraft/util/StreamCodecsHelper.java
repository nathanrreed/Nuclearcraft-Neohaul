package com.nred.nuclearcraft.util;

import com.mojang.datafixers.util.Function13;
import com.mojang.datafixers.util.Function16;
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

    public static <B, C, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> StreamCodec<B, C> composite(
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
            final StreamCodec<? super B, T9> codec9,
            final Function<C, T9> getter9,
            final StreamCodec<? super B, T10> codec10,
            final Function<C, T10> getter10,
            final StreamCodec<? super B, T11> codec11,
            final Function<C, T11> getter11,
            final StreamCodec<? super B, T12> codec12,
            final Function<C, T12> getter12,
            final StreamCodec<? super B, T13> codec13,
            final Function<C, T13> getter13,
            final StreamCodec<? super B, T14> codec14,
            final Function<C, T14> getter14,
            final StreamCodec<? super B, T15> codec15,
            final Function<C, T15> getter15,
            final StreamCodec<? super B, T16> codec16,
            final Function<C, T16> getter16,
            final Function16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, C> p_331335_) {
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
                T9 t9 = codec9.decode(p_330310_);
                T10 t10 = codec10.decode(p_330310_);
                T11 t11 = codec11.decode(p_330310_);
                T12 t12 = codec12.decode(p_330310_);
                T13 t13 = codec13.decode(p_330310_);
                T14 t14 = codec14.decode(p_330310_);
                T15 t15 = codec15.decode(p_330310_);
                T16 t16 = codec16.decode(p_330310_);
                return p_331335_.apply(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16);
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
                codec9.encode(p_332052_, getter9.apply(p_331912_));
                codec10.encode(p_332052_, getter10.apply(p_331912_));
                codec11.encode(p_332052_, getter11.apply(p_331912_));
                codec12.encode(p_332052_, getter12.apply(p_331912_));
                codec13.encode(p_332052_, getter13.apply(p_331912_));
                codec14.encode(p_332052_, getter14.apply(p_331912_));
                codec15.encode(p_332052_, getter15.apply(p_331912_));
                codec16.encode(p_332052_, getter16.apply(p_331912_));
            }
        };
    }

    public static <B, C, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> StreamCodec<B, C> composite(
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
            final StreamCodec<? super B, T9> codec9,
            final Function<C, T9> getter9,
            final StreamCodec<? super B, T10> codec10,
            final Function<C, T10> getter10,
            final StreamCodec<? super B, T11> codec11,
            final Function<C, T11> getter11,
            final StreamCodec<? super B, T12> codec12,
            final Function<C, T12> getter12,
            final StreamCodec<? super B, T13> codec13,
            final Function<C, T13> getter13,
            final Function13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, C> p_331335_) {
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
                T9 t9 = codec9.decode(p_330310_);
                T10 t10 = codec10.decode(p_330310_);
                T11 t11 = codec11.decode(p_330310_);
                T12 t12 = codec12.decode(p_330310_);
                T13 t13 = codec13.decode(p_330310_);
                return p_331335_.apply(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13);
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
                codec9.encode(p_332052_, getter9.apply(p_331912_));
                codec10.encode(p_332052_, getter10.apply(p_331912_));
                codec11.encode(p_332052_, getter11.apply(p_331912_));
                codec12.encode(p_332052_, getter12.apply(p_331912_));
            }
        };
    }
}
