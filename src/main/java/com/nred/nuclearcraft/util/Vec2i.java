package com.nred.nuclearcraft.util;

import com.google.common.base.MoreObjects;

public class Vec2i {
    public static final Vec2i ZERO = new Vec2i(0, 0);

    public static final Vec2i[] DIRS = {new Vec2i(1, 0), new Vec2i(0, 1), new Vec2i(-1, 0), new Vec2i(0, -1)};
    public static final Vec2i[] DIRS_WITH_ZERO = {ZERO, new Vec2i(1, 0), new Vec2i(0, 1), new Vec2i(-1, 0), new Vec2i(0, -1)};

    public final int u, v;

    public Vec2i(int u, int v) {
        this.u = u;
        this.v = v;
    }

    public Vec2i add(Vec2i other) {
        return new Vec2i(u + other.u, v + other.v);
    }

    public Vec2i add(int u, int v) {
        return new Vec2i(this.u + u, this.v + v);
    }

    public Vec2i subtract(Vec2i other) {
        return new Vec2i(u - other.u, v - other.v);
    }

    public Vec2i subtract(int u, int v) {
        return new Vec2i(this.u - u, this.v - v);
    }

    public double absSq() {
        return (double) u * (double) u + (double) v * (double) v;
    }

    public double abs() {
        return Math.sqrt(absSq());
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Vec2i other && u == other.u && v == other.v;
    }

    @Override
    public int hashCode() {
        return 31 * u + v;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).add("u", u).add("v", v).toString();
    }
}