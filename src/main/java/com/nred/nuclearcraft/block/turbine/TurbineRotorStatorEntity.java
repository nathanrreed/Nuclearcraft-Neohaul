package com.nred.nuclearcraft.block.turbine;

import com.nred.nuclearcraft.multiblock.turbine.Turbine;
import com.nred.nuclearcraft.multiblock.turbine.TurbineRotorBladeUtil;
import com.nred.nuclearcraft.multiblock.turbine.TurbineRotorBladeUtil.*;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.zerono.mods.zerocore.lib.multiblock.cuboid.PartPosition;
import it.zerono.mods.zerocore.lib.multiblock.validation.IMultiblockValidator;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Iterator;

import static com.nred.nuclearcraft.registration.BlockEntityRegistration.TURBINE_ROTOR_BLADE;
import static com.nred.nuclearcraft.registration.BlockEntityRegistration.TURBINE_ROTOR_STATOR;

public class TurbineRotorStatorEntity extends AbstractTurbineEntity implements ITurbineRotorBlade<TurbineRotorStatorEntity> {
    @Override
    public boolean isGoodForPosition(PartPosition position, IMultiblockValidator validatorCallback) {
        return position == PartPosition.Interior;
    }

    public static final Object2ObjectMap<String, IRotorStatorType> DYN_STATOR_TYPE_MAP = new Object2ObjectOpenHashMap<>();

    public IRotorStatorType statorType = null;
    protected TurbinePartDir dir = TurbinePartDir.Y;

    /**
     * Don't use this constructor!
     */
//    public TurbineRotorStatorEntity(final BlockPos position, final BlockState blockState) {
//        super(TURBINE_ROTOR_STATOR.get(), position, blockState);
//    }
//    public TurbineRotorStatorEntity() {
//        super(CuboidalPartPositionType.INTERIOR);
//    }

    public TurbineRotorStatorEntity(final BlockPos position, final BlockState blockState, IRotorStatorType statorType) {
        super(TURBINE_ROTOR_BLADE.get(), position, blockState);
        this.statorType = statorType;
    }

    public static class Variant extends TurbineRotorStatorEntity {
        protected Variant(final BlockPos position, final BlockState blockState, TurbineRotorStatorType statorType) {
            super(position, blockState, statorType);
        }
    }

    public static class Standard extends Variant {
        public Standard(final BlockPos position, final BlockState blockState) {
            super(position, blockState, TurbineRotorStatorType.STANDARD);
        }
    }

//    @Override
//    public void onMachineAssembled(Turbine multiblock) {
//        doStandardNullControllerResponse(multiblock);
//        super.onMachineAssembled(multiblock);
//    }

    @Override
    public BlockPos bladePos() {
        return worldPosition;
    }

    @Override
    public TurbinePartDir getDir() {
        return dir;
    }

    @Override
    public void setDir(TurbinePartDir newDir) {
        dir = newDir;
    }

    @Override
    public IRotorBladeType getBladeType() {
        return statorType;
    }

    @Override
    public BlockState getRenderState() {
        if (getBlockType() instanceof TurbineRotorStatorBlock) {
            return getBlockType().defaultBlockState().setValue(TurbineRotorBladeUtil.DIR, dir);
        }
        return getBlockType().defaultBlockState();
    }

    @Override
    public void onBearingFailure(Iterator<TurbineRotorStatorEntity> statorIterator) {
        Turbine turbine = getMultiblockController().orElse(null);
        if (turbine != null && turbine.rand.nextDouble() < 0.04D) {
            statorIterator.remove();
            level.removeBlockEntity(worldPosition);
            level.setBlock(worldPosition, Blocks.AIR.defaultBlockState(), 3);
        }
    }

    //	// NBT
//
//	@Override
//	public NBTTagCompound writeAll(NBTTagCompound nbt) {
//		super.writeAll(nbt);
//		if (statorType != null) {
//			nbt.setString("statorName", statorType.getName());
//		}
//
//		nbt.setInteger("bladeDir", dir.ordinal());
//		return nbt;
//	}
//
//	@Override
//	public void readAll(NBTTagCompound nbt) {
//		super.readAll(nbt);
//		if (nbt.hasKey("statorName")) {
//			String statorName = nbt.getString("statorName");
//			if (DYN_STATOR_TYPE_MAP.containsKey(statorName)) {
//				statorType = DYN_STATOR_TYPE_MAP.get(statorName);
//			}
//		}
//
//		dir = TurbinePartDir.values()[nbt.getInteger("bladeDir")];
//	}
}