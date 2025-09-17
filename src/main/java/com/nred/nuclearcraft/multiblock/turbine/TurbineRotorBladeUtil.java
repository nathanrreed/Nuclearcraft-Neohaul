package com.nred.nuclearcraft.multiblock.turbine;

import com.nred.nuclearcraft.block.turbine.TurbineRotorBladeEntity;
import com.nred.nuclearcraft.block.turbine.TurbineRotorStatorEntity;
import com.nred.nuclearcraft.enumm.ITileEnum;
import it.zerono.mods.zerocore.lib.CodeHelper;
import it.zerono.mods.zerocore.lib.multiblock.variant.IMultiblockVariant;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.EnumProperty;

import java.util.Iterator;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.nred.nuclearcraft.config.Config2.*;

public class TurbineRotorBladeUtil {
    public enum TurbineRotorBladeType implements IRotorBladeType, ITileEnum<TurbineRotorBladeEntity.Variant>, IMultiblockVariant {
        STEEL("steel", () -> turbine_blade_efficiency[0], () -> turbine_blade_expansion[0], TurbineRotorBladeEntity.Steel.class),
        EXTREME("extreme", () -> turbine_blade_efficiency[1], () -> turbine_blade_expansion[1], TurbineRotorBladeEntity.Extreme.class),
        SIC_SIC_CMC("sic_sic_cmc", () -> turbine_blade_efficiency[2], () -> turbine_blade_expansion[2], TurbineRotorBladeEntity.SicSicCMC.class);

        private final String name;
        private final Supplier<Double> efficiency;
        private final Supplier<Double> expansion;
        private final Class<? extends TurbineRotorBladeEntity.Variant> tileClass;
        private final String _translationKey;
        private final Function<Block.Properties, Block.Properties> _blockPropertiesFixer;

        TurbineRotorBladeType(String name, Supplier<Double> efficiency, Supplier<Double> expansion, Class<? extends TurbineRotorBladeEntity.Variant> tileClass) {
            this.name = name;
            this.efficiency = efficiency;
            this.expansion = expansion;
            this.tileClass = tileClass;

            this._translationKey = ""; // TODO ADD
            this._blockPropertiesFixer = null;
        }

        @Override
        public String toString() {
            return getSerializedName();
        }

        @Override
        public double getEfficiency() {
            return efficiency.get();
        }

        @Override
        public double getExpansionCoefficient() {
            return expansion.get();
        }

        @Override
        public Class<? extends TurbineRotorBladeEntity.Variant> getTileClass() {
            return tileClass;
        }

        @Override
        public String getSerializedName() {
            return name;
        }

        @Override
        public int getId() {
            return this.ordinal();
        }

        @Override
        public String getName() {
            return CodeHelper.neutralLowercase(this.name());
        }

        @Override
        public String getTranslationKey() {
            return this._translationKey;
        }

        @Override
        public Block.Properties getBlockProperties() {
            return this._blockPropertiesFixer.apply(this.getDefaultBlockProperties());
        }
    }

    public interface IRotorBladeType extends StringRepresentable {
        double getEfficiency();

        double getExpansionCoefficient();

        default boolean eq(IRotorBladeType other) {
            return other != null && getSerializedName().equals(other.getSerializedName()) && getEfficiency() == other.getEfficiency() && getExpansionCoefficient() == other.getExpansionCoefficient();
        }
    }

    public enum TurbineRotorStatorType implements IRotorStatorType, ITileEnum<TurbineRotorStatorEntity.Variant>, IMultiblockVariant {
        STANDARD("standard", turbine_stator_expansion, TurbineRotorStatorEntity.Standard.class);

        private final String name;
        private final double expansion;
        private final Class<? extends TurbineRotorStatorEntity.Variant> tileClass;
        private final String _translationKey;
        private final Function<Block.Properties, Block.Properties> _blockPropertiesFixer;

        TurbineRotorStatorType(String name, double expansion, Class<? extends TurbineRotorStatorEntity.Variant> tileClass) {
            this.name = name;
            this.expansion = expansion;
            this.tileClass = tileClass;

            this._translationKey = ""; // TODO ADD
            this._blockPropertiesFixer = null;
        }

        @Override
        public String toString() {
            return getSerializedName();
        }

        @Override
        public double getExpansionCoefficient() {
            return expansion;
        }

        @Override
        public Class<? extends TurbineRotorStatorEntity.Variant> getTileClass() {
            return tileClass;
        }

        @Override
        public String getSerializedName() {
            return name;
        }

        @Override
        public int getId() {
            return this.ordinal();
        }

        @Override
        public String getName() {
            return CodeHelper.neutralLowercase(this.name());
        }

        @Override
        public String getTranslationKey() {
            return this._translationKey;
        }

        @Override
        public Block.Properties getBlockProperties() {
            return this._blockPropertiesFixer.apply(this.getDefaultBlockProperties());
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
