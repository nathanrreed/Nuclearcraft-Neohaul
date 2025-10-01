package com.nred.nuclearcraft.block.turbine;

import com.nred.nuclearcraft.multiblock.turbine.Turbine;
import com.nred.nuclearcraft.multiblock.turbine.TurbineRotorBladeType;
import com.nred.nuclearcraft.multiblock.turbine.TurbineRotorBladeUtil;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.zerono.mods.zerocore.lib.multiblock.cuboid.PartPosition;
import it.zerono.mods.zerocore.lib.multiblock.validation.IMultiblockValidator;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Iterator;

import static com.nred.nuclearcraft.registration.BlockEntityRegistration.TURBINE_ENTITY_TYPE;

public class TurbineRotorBladeEntity extends AbstractTurbineEntity implements TurbineRotorBladeUtil.ITurbineRotorBlade<TurbineRotorBladeEntity> {

    public static final Object2ObjectMap<String, TurbineRotorBladeUtil.IRotorBladeType> DYN_BLADE_TYPE_MAP = new Object2ObjectOpenHashMap<>();

    public TurbineRotorBladeUtil.IRotorBladeType bladeType;
    protected TurbineRotorBladeUtil.TurbinePartDir dir = TurbineRotorBladeUtil.TurbinePartDir.Y;

    public TurbineRotorBladeEntity(final BlockPos position, final BlockState blockState, TurbineRotorBladeUtil.IRotorBladeType bladeType) {
        super(TURBINE_ENTITY_TYPE.get("rotor_blade").get(), position, blockState);
        this.bladeType = bladeType;
    }

    @Override
    public boolean isGoodForPosition(PartPosition partPosition, IMultiblockValidator iMultiblockValidator) {
        return partPosition == PartPosition.Interior;
    }

    @Override
    public Class<Turbine> getControllerType() {
        return Turbine.class;
    }

    @Override
    public void onMachineActivated() {
    }

    @Override
    public void onMachineDeactivated() {
    }

    @Override
    public void onAssimilated(Turbine multiblock) {
//        doStandardNullControllerResponse(multiblock); TODO
        super.onAssimilated(multiblock);
    }

    public static class Variant extends TurbineRotorBladeEntity {
        protected Variant(final BlockPos position, final BlockState blockState, TurbineRotorBladeType bladeType) {
            super(position, blockState, bladeType);
        }
    }

    public static class Steel extends Variant {
        public Steel(final BlockPos position, final BlockState blockState) {
            super(position, blockState, TurbineRotorBladeType.STEEL);
        }
    }

    public static class Extreme extends Variant {
        public Extreme(final BlockPos position, final BlockState blockState) {
            super(position, blockState, TurbineRotorBladeType.EXTREME);
        }
    }

    public static class SicSicCMC extends Variant {
        public SicSicCMC(final BlockPos position, final BlockState blockState) {
            super(position, blockState, TurbineRotorBladeType.SIC_SIC_CMC);
        }
    }

//    @Override TODO REMOVE
//    public void onMachineAssembled(Turbine multiblock) {
//        doStandardNullControllerResponse(multiblock);
//        super.onMachineAssembled(multiblock);
//    }

    @Override
    public BlockPos bladePos() {
        return worldPosition;
    }

    @Override
    public TurbineRotorBladeUtil.TurbinePartDir getDir() {
        return dir;
    }

    @Override
    public void setDir(TurbineRotorBladeUtil.TurbinePartDir newDir) {
        dir = newDir;
    }

    @Override
    public BlockState getRenderState() {
        if (getBlockType() instanceof TurbineRotorBladeBlock) {
            return getBlockType().defaultBlockState().setValue(TurbineRotorBladeUtil.DIR, dir);
        }
        return getBlockType().defaultBlockState();
    }

    @Override
    public TurbineRotorBladeUtil.IRotorBladeType getBladeType() {
        return bladeType;
    }

    @Override
    public void onBearingFailure(Iterator<TurbineRotorBladeEntity> bladeIterator) {
        Turbine turbine = getMultiblockController().orElse(null);
        if (turbine != null && turbine.rand.nextDouble() < 0.18D) {
            bladeIterator.remove();
            level.removeBlockEntity(worldPosition);
            level.setBlock(worldPosition, Blocks.AIR.defaultBlockState(), 3);
            level.explode(null, worldPosition.getX() + turbine.rand.nextDouble() - 0.5D, worldPosition.getY() + turbine.rand.nextDouble() - 0.5D, worldPosition.getZ() + turbine.rand.nextDouble() - 0.5D, 4F, Level.ExplosionInteraction.NONE);
        }
    }

    // NBT

    @Override
    protected void saveAdditional(CompoundTag data, HolderLookup.Provider registries) {
        super.saveAdditional(data, registries);
        if (bladeType != null) {
            data.putString("bladeName", bladeType.getSerializedName());
        }

        data.putInt("bladeDir", dir.ordinal());
    }

    @Override
    public void loadAdditional(CompoundTag data, HolderLookup.Provider registries) {
        super.loadAdditional(data, registries);

        if (data.contains("bladeName")) {
            String bladeName = data.getString("bladeName");
            if (DYN_BLADE_TYPE_MAP.containsKey(bladeName)) {
                bladeType = DYN_BLADE_TYPE_MAP.get(bladeName);
            }
        }

        dir = TurbineRotorBladeUtil.TurbinePartDir.values()[data.getInt("bladeDir")];
    }
}