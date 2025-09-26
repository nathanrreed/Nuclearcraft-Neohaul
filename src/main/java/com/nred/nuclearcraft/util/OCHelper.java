package com.nred.nuclearcraft.util;

import com.nred.nuclearcraft.helpers.Tank;
import it.unimi.dsi.fastutil.longs.LongCollection;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.FluidStack;

import java.util.Collection;

public class OCHelper {

    public static Object[] posInfo(BlockPos pos) {
        return pos == null ? new Object[]{0, 0, 0} : new Object[]{pos.getX(), pos.getY(), pos.getZ()};
    }

    public static Object[] posInfoArray(Collection<BlockPos> posCollection) {
        return posCollection.stream().map(OCHelper::posInfo).toArray();
    }

    public static Object[] posInfo(long posLong) {
        return posInfo(BlockPos.of(posLong));
    }

    public static Object[] posInfoArray(LongCollection posLongCollection) {
        return posLongCollection.stream().map(OCHelper::posInfo).toArray();
    }

    public static Object[] vec3dInfo(Vec3i vec) {
        return vec == null ? vec3dInfo(Vec3i.ZERO) : new Object[]{vec.getX(), vec.getY(), vec.getZ()};
    }

    public static Object[] vec3dInfoArray(Collection<Vec3i> vecCollection) {
        return vecCollection.stream().map(OCHelper::vec3dInfo).toArray();
    }

    public static Object[] stackInfo(ItemStack stack) {
        return stack == null || stack.isEmpty() ? new Object[]{0, "null"} : new Object[]{stack.getCount(), StackHelper.stackName(stack)};
    }

    public static Object[] stackInfoArray(Collection<ItemStack> stackCollection) {
        return stackCollection.stream().map(OCHelper::stackInfo).toArray();
    }

    public static Object[] tankInfo(Tank tank) {
        return tank == null || tank.isEmpty() ? new Object[]{0, "null"} : new Object[]{tank.getFluidAmount(), tank.getFluidName()};
    }

    public static Object[] fluidInfo(FluidStack fluidStack) {
        return fluidStack == null || fluidStack.isEmpty() ? new Object[]{0, "null"} : new Object[]{fluidStack.getAmount(), fluidStack.getFluid().getFluidType().getDescriptionId()};
    }

    public static Object[] tankInfoArray(Collection<Tank> tankCollection) {
        return tankCollection.stream().map(OCHelper::tankInfo).toArray();
    }

    public static Object[] fluidInfoArray(Collection<FluidStack> fluidStackCollection) {
        return fluidStackCollection.stream().map(OCHelper::fluidInfo).toArray();
    }
}
