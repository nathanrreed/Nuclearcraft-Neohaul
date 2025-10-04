package com.nred.nuclearcraft.block.fission;

import com.nred.nuclearcraft.multiblock.fisson.FissionSourceType;
import it.zerono.mods.zerocore.lib.multiblock.cuboid.PartPosition;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nonnull;

import static com.nred.nuclearcraft.config.Config2.fission_max_size;
import static com.nred.nuclearcraft.registration.BlockEntityRegistration.FISSION_ENTITY_TYPE;
import static com.nred.nuclearcraft.util.PosHelper.DEFAULT_NON;

public class FissionSourceEntity extends AbstractFissionEntity { //implements IFissionManagerListener<FissionSourceManagerEntity, FissionSourceEntity>
    private final FissionSourceType fissionSourceType;

    public boolean isActive = false;
    public Direction facing = Direction.DOWN;

    protected BlockPos managerPos = DEFAULT_NON;
//    protected FissionSourceManagerEntity manager = null;

    public FissionSourceEntity(final BlockPos position, final BlockState blockState, FissionSourceType fissionSourceType) {
        super(FISSION_ENTITY_TYPE.get("source").get(), position, blockState);
        this.fissionSourceType = fissionSourceType;
    }

    public static class Variant extends FissionSourceEntity {
        protected Variant(final BlockPos position, final BlockState blockState, FissionSourceType fissionSourceType) {
            super(position, blockState, fissionSourceType);
        }
    }

    public static class RadiumBeryllium extends Variant {
        public RadiumBeryllium(final BlockPos position, final BlockState blockState) {
            super(position, blockState, FissionSourceType.RADIUM_BERYLLIUM);
        }
    }

    public static class PoloniumBeryllium extends Variant {
        public PoloniumBeryllium(final BlockPos position, final BlockState blockState) {
            super(position, blockState, FissionSourceType.POLONIUM_BERYLLIUM);
        }
    }

    public static class Californium extends Variant {
        public Californium(final BlockPos position, final BlockState blockState) {
            super(position, blockState, FissionSourceType.CALIFORNIUM);
        }
    }

    public boolean isSourceActive() {
        return false; // TODO (manager != null && manager.isManagerActive()) || getIsRedstonePowered();
    }

    @Override
    public @Nonnull PartPosition getPartPosition() {
        PartPosition partPos = super.getPartPosition();
        if (partPos.getDirection().isPresent()) {
            facing = partPos.getDirection().get();
        }
        return partPos;
    }

    public PrimingTargetInfo getPrimingTarget(boolean checkUpdate, boolean simulate) {
        Direction posFacing = getPartPosition().getDirection().orElse(null);
        if (posFacing == null) {
            posFacing = facing;
            if (posFacing == null) {
                return null;
            }
        }
        Direction dir = posFacing.getOpposite();
        for (int i = 1; i <= fission_max_size; ++i) {
            BlockPos offPos = worldPosition.relative(dir, i);
//            BasicRecipe blockRecipe = RecipeHelper.blockRecipe(NCRecipes.fission_reflector, world, offPos); TODO
//            if (blockRecipe != null && blockRecipe.getFissionReflectorReflectivity() >= 1D) {
//                return null;
//            }

            IFissionComponent component = getMultiblockController().get().getPartMap(IFissionComponent.class).get(offPos.asLong());
            // First check if source is blocked by a flux sink
            if (component != null && component.isNullifyingSources(posFacing, simulate)) {
                return null;
            }

            if (component instanceof IFissionFuelComponent fuelComponent && fuelComponent.isAcceptingFlux(posFacing, simulate)) {
                boolean newSourceEfficiency;
                if (checkUpdate) {
                    double oldSourceEfficiency = fuelComponent.getSourceEfficiency();
                    fuelComponent.setSourceEfficiency(fissionSourceType.getEfficiency(), true);
                    newSourceEfficiency = oldSourceEfficiency != fuelComponent.getSourceEfficiency();
                } else {
                    newSourceEfficiency = false;
                }
                return new PrimingTargetInfo(fuelComponent, newSourceEfficiency);
            }
        }
        return null;
    }

    public static class PrimingTargetInfo {
        public final IFissionFuelComponent fuelComponent;
        public final boolean newSourceEfficiency;

        PrimingTargetInfo(IFissionFuelComponent fuelComponent, boolean newSourceEfficiency) {
            this.fuelComponent = fuelComponent;
            this.newSourceEfficiency = newSourceEfficiency;
        }
    }
//    // IFissionManagerListener
//
//    @Override
//    public BlockPos getManagerPos() {
//        return managerPos;
//    }
//
//    @Override
//    public void setManagerPos(BlockPos pos) {
//        managerPos = pos;
//    }
//
//    @Override
//    public FissionSourceManagerEntity getManager() {
//        return manager;
//    }
//
//    @Override
//    public void setManager(FissionSourceManagerEntity manager) {
//        this.manager = manager;
//    }
//
//    @Override
//    public boolean onManagerRefresh(FissionSourceManagerEntity manager) {
//        this.manager = manager;
//        if (manager != null) {
//            managerPos = manager.getPos();
//            boolean wasActive = isActive;
//            isActive = isSourceActive();
//            if (wasActive != isActive) {
////                setActivity(isActive); // TODO
//                level.setBlockAndUpdate(managerPos, level.getBlockState(managerPos).setValue(ACTIVE, isActive));
//                return true;
//            }
//        }
//        else {
//            managerPos = DEFAULT_NON;
//        }
//        return false;
//    }
//
//    @Override
//    public String getManagerType() {
//        return "fissionSourceManager";
//    }
//
//    @Override
//    public Class<FissionSourceManagerEntity> getManagerClass() {
//        return FissionSourceManagerEntity.class;
//    }

//    // IMultitoolLogic
//
//    @Override
//    public boolean onUseMultitool(ItemStack multitool, EntityPlayerMP player, World world, EnumFacing facing, float hitX, float hitY, float hitZ) {
//        if (IFissionManagerListener.super.onUseMultitool(multitool, player, world, facing, hitX, hitY, hitZ)) {
//            return true;
//        }
//        if (!player.isSneaking()) {
//            PrimingTargetInfo targetInfo = getPrimingTarget(false, true);
//            if (targetInfo == null) {
//                player.sendMessage(new TextComponentString(Lang.localize("nuclearcraft.multiblock.fission_reactor_source.no_target")));
//            }
//            else {
//                IFissionFuelComponent fuelComponent = targetInfo.fuelComponent;
//                BlockPos pos = fuelComponent.getTilePos();
//                BlockHighlightTracker.sendPacket(player, pos, 5000);
//                player.sendMessage(new TextComponentString(Lang.localize("nuclearcraft.multiblock.fission_reactor_source.target", pos.getX(), pos.getY(), pos.getZ(), fuelComponent.getTileBlockDisplayName())));
//            }
//            return true;
//        }
//        return super.onUseMultitool(multitool, player, world, facing, hitX, hitY, hitZ);
//    }
//
//    // NBT
//
//    @Override
//    public NBTTagCompound writeAll(NBTTagCompound nbt) {
//        super.writeAll(nbt);
//        nbt.setInteger("facing", facing.getIndex());
//
//        if (sourceType == null) {
//            nbt.setDouble("efficiency", efficiency);
//        }
//        else {
//            nbt.setString("sourceType", sourceType);
//        }
//
//        nbt.setBoolean("isSourceActive", isActive);
//        nbt.setLong("managerPos", managerPos.toLong());
//        return nbt;
//    }
//
//    @Override
//    public void readAll(NBTTagCompound nbt) {
//        super.readAll(nbt);
//        facing = EnumFacing.byIndex(nbt.getInteger("facing"));
//
//        if (nbt.hasKey("efficiency")) {
//            efficiency = nbt.getDouble("efficiency");
//        }
//        else if (nbt.hasKey("sourceType")) {
//            sourceType = nbt.getString("sourceType");
//
//            if (DYN_EFFICIENCY_MAP.containsKey(sourceType)) {
//                efficiency = DYN_EFFICIENCY_MAP.getDouble(sourceType);
//            }
//        }
//
//        isActive = nbt.getBoolean("isSourceActive");
//        managerPos = BlockPos.fromLong(nbt.getLong("managerPos"));
//    }
}