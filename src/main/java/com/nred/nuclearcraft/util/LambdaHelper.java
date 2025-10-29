package com.nred.nuclearcraft.util;

import java.util.function.Consumer;
import java.util.function.Function;

public class LambdaHelper {
    public static <A, B> B let(A it, Function<A, B> function) {
        return function.apply(it);
    }

    public static <A> A also(A it, Consumer<A> consumer) {
        consumer.accept(it);
        return it;
    }

    @FunctionalInterface
    public interface ThrowingRunnable {

        void run() throws Exception;
    }

    public static void runThrowing(ThrowingRunnable runnable) {
        try {
            runnable.run();
        } catch (Exception e) {
            throw new UnsupportedOperationException();
        }
    }

    @FunctionalInterface
    public interface ThrowingConsumer<T> {

        void accept(T x) throws Exception;
    }

    public static <T> void acceptThrowing(ThrowingConsumer<T> consumer, T x) {
        try {
            consumer.accept(x);
        } catch (Exception e) {
            throw new UnsupportedOperationException();
        }
    }

    @FunctionalInterface
    public interface ThrowingSupplier<T> {

        T get() throws Exception;
    }

    public static <T> T getThrowing(ThrowingSupplier<T> supplier) {
        try {
            return supplier.get();
        } catch (Exception e) {
            throw new UnsupportedOperationException();
        }
    }

    public static <T> T getThrowingOrDefault(ThrowingSupplier<T> supplier, T defaultValue) {
        try {
            return supplier.get();
        } catch (Exception e) {
            return defaultValue;
        }
    }

    @FunctionalInterface
    public interface ThrowingFunction<A, B> {

        B apply(A x) throws Exception;
    }

    public static <A, B> B applyThrowing(ThrowingFunction<A, B> function, A x) {
        try {
            return function.apply(x);
        } catch (Exception e) {
            throw new UnsupportedOperationException();
        }
    }

    public static <A, B> B applyThrowingOrDefault(ThrowingFunction<A, B> function, A x, B defaultValue) {
        try {
            return function.apply(x);
        } catch (Exception e) {
            return defaultValue;
        }
    }
}