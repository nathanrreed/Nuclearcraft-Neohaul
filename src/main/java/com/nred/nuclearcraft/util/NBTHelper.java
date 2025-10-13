package com.nred.nuclearcraft.util;

import it.unimi.dsi.fastutil.doubles.DoubleCollection;
import it.unimi.dsi.fastutil.floats.FloatCollection;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntCollection;
import it.unimi.dsi.fastutil.longs.LongCollection;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import org.joml.Vector3f;

import java.util.Map;
import java.util.Map.Entry;

public class NBTHelper {

    // ItemStack

    public static CompoundTag getStackNBT(ItemStack stack, String... subKeys) {
        if (stack.isEmpty()) {
            return null;
        }

        CompoundTag nbt = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
        if (nbt.isEmpty()) {
            stack.set(DataComponents.CUSTOM_DATA, CustomData.of(nbt = new CompoundTag()));
        }

        for (String subKey : subKeys) {
            if (nbt.contains(subKey, 10)) {
                nbt = nbt.getCompound(subKey);
            } else {
                CompoundTag prev = nbt;
                prev.put(subKey, nbt = new CompoundTag());
            }
        }

        return nbt;
    }

    public static void clearStackNBT(ItemStack stack, String... subKeys) {
        if (!stack.has(DataComponents.CUSTOM_DATA)) {
            return;
        }

        if (subKeys.length == 0) {
            stack.set(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
        }

        CompoundTag nbt = stack.get(DataComponents.CUSTOM_DATA).copyTag();

        int lastIndex = subKeys.length - 1;

        for (int i = 0; i < lastIndex; ++i) {
            String subKey = subKeys[i];
            if (nbt.contains(subKey, 10)) {
                nbt = nbt.getCompound(subKey);
            } else {
                return;
            }
        }

        nbt.put(subKeys[lastIndex], new CompoundTag());
    }

    // Inventory

    @SafeVarargs
    public static CompoundTag writeAllItems(CompoundTag tag, HolderLookup.Provider registries, NonNullList<ItemStack>... lists) {
        return writeAllItems(tag, registries, "Items", lists);
    }

    @SafeVarargs
    public static CompoundTag writeAllItems(CompoundTag tag, HolderLookup.Provider registries, String name, NonNullList<ItemStack>... lists) {
        if (lists.length == 0) {
            return tag;
        }
        ListTag nbttaglist = new ListTag();

        for (NonNullList<ItemStack> list : lists) {
            CompoundTag nbttagcompound = new CompoundTag();
            ContainerHelper.saveAllItems(nbttagcompound, list, registries);
            nbttaglist.add(nbttagcompound);
        }

        tag.put(name, nbttaglist);

        return tag;
    }

    @SafeVarargs
    public static void readAllItems(CompoundTag tag, HolderLookup.Provider registries, NonNullList<ItemStack>... lists) {
        readAllItems(tag, registries, "Items", lists);
    }

    @SafeVarargs
    public static void readAllItems(CompoundTag tag, HolderLookup.Provider registries, String name, NonNullList<ItemStack>... lists) {
        if (lists.length == 0) {
            return;
        }
        ListTag nbttaglist = tag.getList(name, 10);

        int n = 0;
        for (int i = 0; i < nbttaglist.size(); ++i) {
            CompoundTag nbttagcompound = nbttaglist.getCompound(i);
            ContainerHelper.loadAllItems(nbttagcompound, lists[i], registries);
        }
    }

    // BlockPos

    public static CompoundTag writeBlockPos(CompoundTag nbt, BlockPos pos, String name) {
        if (pos != null) {
            nbt.putIntArray(name, new int[]{pos.getX(), pos.getY(), pos.getZ()});
        }
        return nbt;
    }

    public static BlockPos readBlockPos(CompoundTag nbt, String name) {
        if (nbt.contains(name, 11)) {
            int[] array = nbt.getIntArray(name);
            return new BlockPos(array[0], array[1], array[2]);
        }
        return PosHelper.DEFAULT_NON;
    }

    // Vector3f

    public static CompoundTag writeVector3f(CompoundTag nbt, Vector3f vector, String name) {
        if (vector != null) {
            CompoundTag tag = new CompoundTag();
            tag.putFloat("floatx", vector.x);
            tag.putFloat("floaty", vector.y);
            tag.putFloat("floatz", vector.z);
            nbt.put(name, tag);
        }
        return nbt;
    }

    public static Vector3f readVector3f(CompoundTag nbt, String name) {
        if (nbt.contains(name, 10)) {
            CompoundTag tag = nbt.getCompound(name);
            return new Vector3f(tag.getFloat("floatx"), tag.getFloat("floaty"), tag.getFloat("floatz"));
        }
        return new Vector3f(0, -1, 0);
    }

    // long[]

    public static CompoundTag writeLongArray(CompoundTag nbt, long[] array, String name) {
        if (array != null) {
            CompoundTag tag = new CompoundTag();
            tag.putInt("length", array.length);
            for (int i = 0; i < array.length; ++i) {
                tag.putLong("long" + i, array[i]);
            }
            nbt.put(name, tag);
        }
        return nbt;
    }

    public static long[] readLongArray(CompoundTag nbt, String name) {
        if (nbt.contains(name, 10)) {
            CompoundTag tag = nbt.getCompound(name);
            long[] array = new long[tag.getInt("length")];
            for (int i = 0; i < array.length; ++i) {
                array[i] = tag.getLong("long" + i);
            }
            return array;
        }
        return new long[0];
    }

    // float[]

    public static CompoundTag writeFloatArray(CompoundTag nbt, float[] array, String name) {
        if (array != null) {
            CompoundTag tag = new CompoundTag();
            tag.putInt("length", array.length);
            for (int i = 0; i < array.length; ++i) {
                tag.putFloat("float" + i, array[i]);
            }
            nbt.put(name, tag);
        }
        return nbt;
    }

    public static float[] readFloatArray(CompoundTag nbt, String name) {
        if (nbt.contains(name, 10)) {
            CompoundTag tag = nbt.getCompound(name);
            float[] array = new float[tag.getInt("length")];
            for (int i = 0; i < array.length; ++i) {
                array[i] = tag.getFloat("float" + i);
            }
            return array;
        }
        return new float[0];
    }

    // double[]

    public static CompoundTag writeDoubleArray(CompoundTag nbt, double[] array, String name) {
        if (array != null) {
            CompoundTag tag = new CompoundTag();
            tag.putInt("length", array.length);
            for (int i = 0; i < array.length; ++i) {
                tag.putDouble("double" + i, array[i]);
            }
            nbt.put(name, tag);
        }
        return nbt;
    }

    public static double[] readDoubleArray(CompoundTag nbt, String name) {
        if (nbt.contains(name, 10)) {
            CompoundTag tag = nbt.getCompound(name);
            double[] array = new double[tag.getInt("length")];
            for (int i = 0; i < array.length; ++i) {
                array[i] = tag.getDouble("double" + i);
            }
            return array;
        }
        return new double[0];
    }

    // BlockPos[]

    public static CompoundTag writeBlockPosArray(CompoundTag nbt, BlockPos[] array, String name) {
        if (array != null) {
            CompoundTag tag = new CompoundTag();
            tag.putInt("length", array.length);
            for (int i = 0; i < array.length; ++i) {
                writeBlockPos(tag, array[i], "pos" + i);
            }
            nbt.put(name, tag);
        }
        return nbt;
    }

    public static BlockPos[] readBlockPosArray(CompoundTag nbt, String name) {
        if (nbt.contains(name, 10)) {
            CompoundTag tag = nbt.getCompound(name);
            BlockPos[] array = new BlockPos[tag.getInt("length")];
            for (int i = 0; i < array.length; ++i) {
                array[i] = readBlockPos(tag, "pos" + i);
            }
            return array;
        }
        return null;
    }

    // Vector3f[]

    public static CompoundTag writeVector3fArray(CompoundTag nbt, Vector3f[] array, String name) {
        if (array != null) {
            CompoundTag tag = new CompoundTag();
            tag.putInt("length", array.length);
            for (int i = 0; i < array.length; ++i) {
                writeVector3f(tag, array[i], "vector" + i);
            }
            nbt.put(name, tag);
        }
        return nbt;
    }

    public static Vector3f[] readVector3fArray(CompoundTag nbt, String name) {
        if (nbt.contains(name, 10)) {
            CompoundTag tag = nbt.getCompound(name);
            Vector3f[] array = new Vector3f[tag.getInt("length")];
            for (int i = 0; i < array.length; ++i) {
                array[i] = readVector3f(tag, "vector" + i);
            }
            return array;
        }
        return null;
    }

    // IntCollection

    public static CompoundTag writeIntCollection(CompoundTag nbt, IntCollection collection, String name) {
        CompoundTag tag = new CompoundTag();
        tag.putIntArray("ints", collection.toIntArray());
        nbt.put(name, tag);
        return nbt;
    }

    public static void readIntCollection(CompoundTag nbt, IntCollection collection, String name) {
        if (nbt.contains(name, 10)) {
            CompoundTag tag = nbt.getCompound(name);
            collection.clear();
            collection.addAll(new IntArrayList(tag.getIntArray("ints")));
        }
    }

    // LongCollection

    public static CompoundTag writeLongCollection(CompoundTag nbt, LongCollection collection, String name) {
        CompoundTag tag = new CompoundTag();
        int i = 0;
        for (long l : collection) {
            tag.putLong("long" + i, l);
            ++i;
        }
        nbt.put(name, tag);
        return nbt;
    }

    public static void readLongCollection(CompoundTag nbt, LongCollection collection, String name) {
        if (nbt.contains(name, 10)) {
            CompoundTag tag = nbt.getCompound(name);
            collection.clear();
            int i = 0;
            while (tag.contains("long" + i)) {
                collection.add(tag.getLong("long" + i));
                ++i;
            }
        }
    }

    // FloatCollection

    public static CompoundTag writeFloatCollection(CompoundTag nbt, FloatCollection collection, String name) {
        CompoundTag tag = new CompoundTag();
        int i = 0;
        for (float f : collection) {
            tag.putFloat("float" + i, f);
            ++i;
        }
        nbt.put(name, tag);
        return nbt;
    }

    public static void readFloatCollection(CompoundTag nbt, FloatCollection collection, String name) {
        if (nbt.contains(name, 10)) {
            CompoundTag tag = nbt.getCompound(name);
            collection.clear();
            int i = 0;
            while (tag.contains("float" + i)) {
                collection.add(tag.getFloat("float" + i));
                ++i;
            }
        }
    }

    // DoubleCollection

    public static CompoundTag writeDoubleCollection(CompoundTag nbt, DoubleCollection collection, String name) {
        CompoundTag tag = new CompoundTag();
        int i = 0;
        for (double d : collection) {
            tag.putDouble("double" + i, d);
            ++i;
        }
        nbt.put(name, tag);
        return nbt;
    }

    public static void readDoubleCollection(CompoundTag nbt, DoubleCollection collection, String name) {
        if (nbt.contains(name, 10)) {
            CompoundTag tag = nbt.getCompound(name);
            collection.clear();
            int i = 0;
            while (tag.contains("double" + i)) {
                collection.add(tag.getDouble("double" + i));
                ++i;
            }
        }
    }

    // Map<BlockPos, Integer>

    public static CompoundTag writeBlockPosToIntegerMap(CompoundTag nbt, Map<BlockPos, Integer> map, String name) {
        CompoundTag tag = new CompoundTag();
        int i = 0;
        for (Entry<BlockPos, Integer> entry : map.entrySet()) {
            writeBlockPos(tag, entry.getKey(), "pos" + i);
            tag.putInt("int" + i, entry.getValue());
            ++i;
        }
        nbt.put(name, tag);
        return nbt;
    }

    public static void readBlockPosToIntegerMap(CompoundTag nbt, Map<BlockPos, Integer> map, String name) {
        if (nbt.contains(name, 10)) {
            CompoundTag tag = nbt.getCompound(name);
            map.clear();
            int i = 0;
            while (tag.contains("pos" + i)) {
                map.put(readBlockPos(tag, "pos" + i), tag.getInt("int" + i));
                ++i;
            }
        }
    }
}