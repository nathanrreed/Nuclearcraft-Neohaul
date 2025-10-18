package com.nred.nuclearcraft.util;

import com.google.common.collect.Lists;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;

import javax.annotation.Nonnull;
import java.util.List;

public class PosHelper {

    public static final BlockPos DEFAULT_NON = new BlockPos(0, Integer.MIN_VALUE, 0);

    public static Direction getAxisDirectionDir(@Nonnull Direction.Axis axis, @Nonnull Direction.AxisDirection dir) {
        int index = 1 - getAxisDirIndex(dir);

        return switch (axis) {
            case X -> AXIALS_X[index];
            case Y -> AXIALS_Y[index];
            case Z -> AXIALS_Z[index];
        };
    }

    // Horizontals

    private static final Direction[] HORIZONTALS_X = new Direction[]{Direction.DOWN, Direction.UP, Direction.NORTH, Direction.SOUTH};
    private static final Direction[] HORIZONTALS_Y = new Direction[]{Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST};
    private static final Direction[] HORIZONTALS_Z = new Direction[]{Direction.DOWN, Direction.UP, Direction.WEST, Direction.EAST};

    public static Direction[] getHorizontals(Direction dir) {
        return switch (dir) {
            case DOWN -> HORIZONTALS_Y;
            case UP -> HORIZONTALS_Y;
            case NORTH -> HORIZONTALS_Z;
            case SOUTH -> HORIZONTALS_Z;
            case WEST -> HORIZONTALS_X;
            case EAST -> HORIZONTALS_X;
        };
    }

    // Axials

    private static final Direction[] AXIALS_X = new Direction[]{Direction.WEST, Direction.EAST};
    private static final Direction[] AXIALS_Y = new Direction[]{Direction.DOWN, Direction.UP};
    private static final Direction[] AXIALS_Z = new Direction[]{Direction.NORTH, Direction.SOUTH};

    public static List<Direction[]> axialDirsList() {
        return Lists.newArrayList(AXIALS_X, AXIALS_Y, AXIALS_Z);
    }

    public static Direction[] getAxialDirs(Direction dir) {
        return switch (dir) {
            case DOWN -> AXIALS_Y;
            case UP -> AXIALS_Y;
            case NORTH -> AXIALS_Z;
            case SOUTH -> AXIALS_Z;
            case WEST -> AXIALS_X;
            case EAST -> AXIALS_X;
        };
    }

    // Vertices

    public static final Direction[][] VERTEX_DIRS = new Direction[][]{new Direction[]{Direction.DOWN, Direction.NORTH, Direction.WEST}, new Direction[]{Direction.DOWN, Direction.NORTH, Direction.EAST}, new Direction[]{Direction.DOWN, Direction.SOUTH, Direction.WEST}, new Direction[]{Direction.DOWN, Direction.SOUTH, Direction.EAST}, new Direction[]{Direction.UP, Direction.NORTH, Direction.WEST}, new Direction[]{Direction.UP, Direction.NORTH, Direction.EAST}, new Direction[]{Direction.UP, Direction.SOUTH, Direction.WEST}, new Direction[]{Direction.UP, Direction.SOUTH, Direction.EAST}};

    // Edges

    public static final Direction[][] EDGE_DIRS = new Direction[][]{new Direction[]{Direction.DOWN, Direction.NORTH}, new Direction[]{Direction.DOWN, Direction.SOUTH}, new Direction[]{Direction.DOWN, Direction.WEST}, new Direction[]{Direction.DOWN, Direction.EAST}, new Direction[]{Direction.UP, Direction.NORTH}, new Direction[]{Direction.UP, Direction.SOUTH}, new Direction[]{Direction.UP, Direction.WEST}, new Direction[]{Direction.UP, Direction.EAST}, new Direction[]{Direction.NORTH, Direction.WEST}, new Direction[]{Direction.NORTH, Direction.WEST}, new Direction[]{Direction.SOUTH, Direction.EAST}, new Direction[]{Direction.SOUTH, Direction.EAST}};

    // Planes

    public static final Direction[][] PLANE_DIRS = new Direction[][]{new Direction[]{Direction.DOWN, Direction.UP, Direction.NORTH, Direction.SOUTH}, new Direction[]{Direction.NORTH, Direction.SOUTH, Direction.WEST, Direction.EAST}, new Direction[]{Direction.WEST, Direction.EAST, Direction.DOWN, Direction.UP}};

    // Axes

    public static final Direction.Axis[] AXES = new Direction.Axis[]{Direction.Axis.X, Direction.Axis.Y, Direction.Axis.Z};

    public static int getAxisIndex(@Nonnull Direction.Axis axis) {
        return axis == Direction.Axis.X ? 0 : axis == Direction.Axis.Y ? 1 : 2;
    }

    public static final Direction.AxisDirection[] AXIS_DIRS = new Direction.AxisDirection[]{Direction.AxisDirection.POSITIVE, Direction.AxisDirection.NEGATIVE};

    public static int getAxisDirIndex(@Nonnull Direction.AxisDirection dir) {
        return dir == Direction.AxisDirection.POSITIVE ? 0 : 1;
    }
}
