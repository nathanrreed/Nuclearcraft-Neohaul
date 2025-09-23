package com.nred.nuclearcraft.multiblock.turbine;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.EnumProperty;

import java.util.Iterator;

public class TurbineRotorBladeUtil {
    public interface IRotorBladeType extends StringRepresentable {
        double getEfficiency();

        double getExpansionCoefficient();

        default boolean eq(IRotorBladeType other) {
            return other != null && getSerializedName().equals(other.getSerializedName()) && getEfficiency() == other.getEfficiency() && getExpansionCoefficient() == other.getExpansionCoefficient();
        }
    }

    public interface IRotorStatorType extends IRotorBladeType {

        @Override
        default double getEfficiency() {
            return -1D;
        }
    }

    public interface ITurbineRotorBlade<BLADE extends ITurbineRotorBlade<?>> {

        BlockPos bladePos();

        TurbinePartDir getDir();

        void setDir(TurbinePartDir newDir);

        IRotorBladeType getBladeType();

        BlockState getRenderState();

        void onBearingFailure(Iterator<BLADE> bladeIterator);
    }

    public interface IBlockRotorBlade {

    }

    public enum TurbinePartDir implements StringRepresentable {

        INVISIBLE("invisible"),
        X("x"),
        Y("y"),
        Z("z");

        private final String name;

        TurbinePartDir(String name) {
            this.name = name;
        }

        @Override
        public String getSerializedName() {
            return name;
        }

        @Override
        public String toString() {
            return getSerializedName();
        }

        public static TurbinePartDir fromFacingAxis(Direction.Axis axis) {
            return switch (axis) {
                case X -> X;
                case Y -> Y;
                case Z -> Z;
            };
        }
    }

    public static final EnumProperty<TurbinePartDir> DIR = EnumProperty.create("dir", TurbinePartDir.class);
}
