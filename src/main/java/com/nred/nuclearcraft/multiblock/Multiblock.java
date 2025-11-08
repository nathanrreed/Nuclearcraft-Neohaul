package com.nred.nuclearcraft.multiblock;

import com.nred.nuclearcraft.block_entity.ITickable;
import com.nred.nuclearcraft.block_entity.fluid.ITileFluid;
import com.nred.nuclearcraft.block_entity.internal.energy.EnergyStorage;
import com.nred.nuclearcraft.block_entity.internal.fluid.Tank;
import com.nred.nuclearcraft.block_entity.inventory.ITileInventory;
import com.nred.nuclearcraft.util.NCMath;
import com.nred.nuclearcraft.util.PosHelper;
import com.nred.nuclearcraft.util.StackHelper;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectLists;
import it.zerono.mods.zerocore.lib.multiblock.IMultiblockController;
import it.zerono.mods.zerocore.lib.multiblock.IMultiblockPart;
import it.zerono.mods.zerocore.lib.multiblock.cuboid.AbstractCuboidMultiblockController;
import it.zerono.mods.zerocore.lib.multiblock.validation.IMultiblockValidator;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.joml.Vector3f;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.nred.nuclearcraft.NuclearcraftNeohaul.MODID;

public abstract class Multiblock<MULTIBLOCK extends Multiblock<MULTIBLOCK>> extends AbstractCuboidMultiblockController<MULTIBLOCK> implements IMultiblockController<MULTIBLOCK> {
    protected Multiblock(Level world) {
        super(world);
    }

    public RandomSource rand = getWorld().random;

    public abstract int getMinimumInteriorLength();

    public abstract int getMaximumInteriorLength();

    public boolean isInInterior(BlockPos pos) {
        int x = pos.getX(), y = pos.getY(), z = pos.getZ();
        BlockPos min = getMinimumCoord().get(), max = getMaximumCoord().get();
        return x >= min.getX() + 1 && x <= max.getX() - 1 && y >= min.getY() + 1 && y <= max.getY() - 1 && z >= min.getZ() + 1 && z <= max.getZ() - 1;
    }

    @Override
    protected int getMinimumNumberOfPartsForAssembledMachine() {
        return NCMath.hollowCube(getMinimumInteriorLength() + 2);
    }

    @Override
    protected int getMinimumXSize() {
        return getMinimumInteriorLength() + 2;
    }

    @Override
    protected int getMinimumYSize() {
        return getMinimumInteriorLength() + 2;
    }

    @Override
    protected int getMinimumZSize() {
        return getMinimumInteriorLength() + 2;
    }

    public Optional<BlockPos> getMinimumCoord() {
//        if (minimumCoord == null) { TODO
//            recalculateCoords();
//        }
        return Optional.of(this.getBoundingBox().getMin());
    }

    public Optional<BlockPos> getMaximumCoord() {
//        if (maximumCoord == null) {
//            recalculateCoords();
//        }
        return Optional.of(this.getBoundingBox().getMax());
    }

    private List<ITickable> _attachedTickables = ObjectLists.emptyList();

    @Override
    protected boolean updateServer() {
        _attachedTickables.forEach(ITickable::update);
        return false;
    }

    @Override
    protected void onPartAdded(IMultiblockPart<MULTIBLOCK> newPart) {
        if (newPart instanceof ITickable && this.calledByLogicalServer()) {
            if (ObjectLists.<ITickable>emptyList() == this._attachedTickables) {
                this._attachedTickables = new ObjectArrayList<>(4);
            }
            this._attachedTickables.add((ITickable) newPart);
        }
    }

    @Override
    protected void onPartRemoved(IMultiblockPart<MULTIBLOCK> oldPart) {
        if (oldPart instanceof ITickable && this.calledByLogicalServer() &&
                ObjectLists.<ITickable>emptyList() != this._attachedTickables) {
            this._attachedTickables.remove(oldPart);
        }
    }

    @Override
    protected void onAssimilated(IMultiblockController<MULTIBLOCK> iMultiblockController) {
        this._attachedTickables.clear();
    }

    public int getInteriorLength(Direction dir) {
        if (dir == null) {
            return getInteriorLengthY();
        }

        return switch (dir) {
            case DOWN, UP -> getInteriorLengthY();
            case NORTH, SOUTH -> getInteriorLengthZ();
            case WEST, EAST -> getInteriorLengthX();
        };
    }

    public <TYPE> Map<Long, TYPE> getPartMap(Class<TYPE> clazz) {
        return (Map<Long, TYPE>) getConnectedParts(clazz::isInstance).collect(Collectors.toMap(el -> el.getWorldPosition().asLong(), el -> el));
    }

    public <TYPE> List<TYPE> getParts(Class<TYPE> clazz) {
        return (List<TYPE>) getConnectedParts(clazz::isInstance).toList();
    }

    public <TYPE> Iterator<TYPE> getPartIterator(Class<TYPE> clazz) {
        return getPartMap(clazz).values().iterator();
    }

    public int getPartCount(Class<?> clazz) {
        return getPartsCount(clazz::isInstance);
    }

    @Override
    protected int getMaximumXSize() {
        return getMaximumInteriorLength() + 2;
    }

    @Override
    protected int getMaximumYSize() {
        return getMaximumInteriorLength() + 2;
    }

    @Override
    protected int getMaximumZSize() {
        return getMaximumInteriorLength() + 2;
    }

    public boolean hasAxialSymmetry(Direction.Axis axis) {
        if (axis == null) {
            return true;
        }

        Direction normal = PosHelper.getAxisDirectionDir(axis, Direction.AxisDirection.NEGATIVE);
        int interiorLength = getInteriorLength(normal);

        if (interiorLength <= 1) {
            return true;
        }

        Iterable<BlockPos> plane = getInteriorPlane(normal, 0, 0, 0, 0, 0);

        for (BlockPos planePos : plane) {
            MutableBlockPos columnPos = new MutableBlockPos(planePos.getX(), planePos.getY(), planePos.getZ());
            ItemStack stack = StackHelper.blockStateToStack(getWorld().getBlockState(columnPos));

            for (int i = 1; i < interiorLength; ++i) {
                switch (axis) {
                    case X -> columnPos.setX(columnPos.getX() + 1);
                    case Y -> columnPos.setY(columnPos.getY() + 1);
                    case Z -> columnPos.setZ(columnPos.getZ() + 1);
                }

                BlockState state = getWorld().getBlockState(columnPos);
                if ((!stack.isEmpty() || !state.isAir()) && !ItemStack.isSameItem(stack, StackHelper.blockStateToStack(state))) {
                    if (getLastError().isEmpty()) {
                        setLastError(MODID + ".multiblock_validation.invalid_axial_symmetry", columnPos, columnPos.getX(), columnPos.getY(), columnPos.getZ(), axis.getName());
                    }
                    return false;
                }
            }
        }

        return true;
    }

    public boolean hasPlanarSymmetry(Direction.Axis axis) {
        if (axis == null) {
            return true;
        }

        Direction normal = PosHelper.getAxisDirectionDir(axis, Direction.AxisDirection.NEGATIVE);
        int interiorLength = getInteriorLength(normal);

        for (int i = 0; i < interiorLength; ++i) {
            ItemStack stack = null;
            for (BlockPos pos : getInteriorPlane(normal, i, 0, 0, 0, 0)) {
                BlockState state = getWorld().getBlockState(pos);
                if (stack == null) {
                    stack = StackHelper.blockStateToStack(state);
                } else if ((!stack.isEmpty() || !state.isAir()) && !ItemStack.isSameItem(stack, StackHelper.blockStateToStack(state))) {
                    if (getLastError().isEmpty()) {
                        String planeName = switch (axis) {
                            case X -> "YZ";
                            case Y -> "XZ";
                            case Z -> "XY";
                        };
                        setLastError(MODID + ".multiblock_validation.invalid_planar_symmetry", pos, pos.getX(), pos.getY(), pos.getZ(), planeName);
                    }
                    return false;
                }
            }
        }

        return true;
    }

    public int getMinInteriorX() {
        return getMinimumCoord().get().getX() + 1;
    }

    public int getMinInteriorY() {
        return getMinimumCoord().get().getY() + 1;
    }

    public int getMinInteriorZ() {
        return getMinimumCoord().get().getZ() + 1;
    }

    public int getMaxInteriorX() {
        return getMaximumCoord().get().getX() - 1;
    }

    public int getMaxInteriorY() {
        return getMaximumCoord().get().getY() - 1;
    }

    public int getMaxInteriorZ() {
        return getMaximumCoord().get().getZ() - 1;
    }

    public int getMinX() {
        return getMinimumCoord().get().getX();
    }

    public int getMinY() {
        return getMinimumCoord().get().getY();
    }

    public int getMinZ() {
        return getMinimumCoord().get().getZ();
    }

    public int getMaxX() {
        return getMaximumCoord().get().getX();
    }

    public int getMaxY() {
        return getMaximumCoord().get().getY();
    }

    public int getMaxZ() {
        return getMaximumCoord().get().getZ();
    }

    public int getMiddleX() {
        return NCMath.toInt(((long) getMinX() + (long) getMaxX()) / 2);
    }

    public int getMiddleY() {
        return NCMath.toInt(((long) getMinY() + (long) getMaxY()) / 2);
    }

    public int getMiddleZ() {
        return NCMath.toInt(((long) getMinZ() + (long) getMaxZ()) / 2);
    }

    public BlockPos getMiddleCoord() {
        return new BlockPos(getMiddleX(), getMiddleY(), getMiddleZ());
    }

    public BlockPos getExtremeInteriorCoord(boolean maxX, boolean maxY, boolean maxZ) {
        return new BlockPos(maxX ? getMaxInteriorX() : getMinInteriorX(), maxY ? getMaxInteriorY() : getMinInteriorY(), maxZ ? getMaxInteriorZ() : getMinInteriorZ());
    }

    public int getClampedInteriorX(int x) {
        return Mth.clamp(x, getMinInteriorX(), getMaxInteriorX());
    }

    public int getClampedInteriorY(int y) {
        return Mth.clamp(y, getMinInteriorY(), getMaxInteriorY());
    }

    public int getClampedInteriorZ(int z) {
        return Mth.clamp(z, getMinInteriorZ(), getMaxInteriorZ());
    }

    public BlockPos getClampedInteriorCoord(BlockPos pos) {
        return new BlockPos(getClampedInteriorX(pos.getX()), getClampedInteriorY(pos.getY()), getClampedInteriorZ(pos.getZ()));
    }

    public int getExteriorLengthX() {
        return getMaximumCoord().get().getX() - getMinimumCoord().get().getX() + 1;
    }

    public int getExteriorLengthY() {
        return getMaximumCoord().get().getY() - getMinimumCoord().get().getY() + 1;
    }

    public int getExteriorLengthZ() {
        return getMaximumCoord().get().getZ() - getMinimumCoord().get().getZ() + 1;
    }

    public int getInteriorLengthX() {
        return getMaximumCoord().get().getX() - getMinimumCoord().get().getX() - 1;
    }

    public int getInteriorLengthY() {
        return getMaximumCoord().get().getY() - getMinimumCoord().get().getY() - 1;
    }

    public int getInteriorLengthZ() {
        return getMaximumCoord().get().getZ() - getMinimumCoord().get().getZ() - 1;
    }

    public int getExteriorVolume() {
        return getExteriorLengthX() * getExteriorLengthY() * getExteriorLengthZ();
    }

    public int getInteriorVolume() {
        return getInteriorLengthX() * getInteriorLengthY() * getInteriorLengthZ();
    }

    public int getExteriorSurfaceArea() {
        return 2 * (getExteriorLengthX() * getExteriorLengthY() + getExteriorLengthY() * getExteriorLengthZ() + getExteriorLengthZ() * getExteriorLengthX());
    }

    public boolean isInMinWall(Direction.Axis axis, BlockPos pos) {
        if (axis == null) {
            return false;
        }

        return switch (axis) {
            case X -> pos.getX() == getMinX();
            case Y -> pos.getY() == getMinY();
            case Z -> pos.getZ() == getMinZ();
        };
    }

    public boolean isInMaxWall(Direction.Axis axis, BlockPos pos) {
        if (axis == null) {
            return false;
        }

        return switch (axis) {
            case X -> pos.getX() == getMaxX();
            case Y -> pos.getY() == getMaxY();
            case Z -> pos.getZ() == getMaxZ();
        };
    }

    public boolean isInWall(Direction side, BlockPos pos) {
        if (side == null) {
            return false;
        }

        return switch (side) {
            case DOWN -> pos.getY() == getMinY();
            case UP -> pos.getY() == getMaxY();
            case NORTH -> pos.getZ() == getMinZ();
            case SOUTH -> pos.getZ() == getMaxZ();
            case WEST -> pos.getX() == getMinX();
            case EAST -> pos.getX() == getMaxX();
        };
    }

    public BlockPos getMinimumInteriorPlaneCoord(Direction normal, int depth, int uCushion, int vCushion) {
        if (normal == null) {
            return getExtremeInteriorCoord(false, false, false);
        }

        return switch (normal) {
            case DOWN -> getExtremeInteriorCoord(false, false, false).relative(Direction.UP, depth).relative(Direction.SOUTH, uCushion).relative(Direction.EAST, vCushion);
            case UP -> getExtremeInteriorCoord(false, true, false).relative(Direction.DOWN, depth).relative(Direction.SOUTH, uCushion).relative(Direction.EAST, vCushion);
            case NORTH -> getExtremeInteriorCoord(false, false, false).relative(Direction.SOUTH, depth).relative(Direction.EAST, uCushion).relative(Direction.UP, vCushion);
            case SOUTH -> getExtremeInteriorCoord(false, false, true).relative(Direction.NORTH, depth).relative(Direction.EAST, uCushion).relative(Direction.UP, vCushion);
            case WEST -> getExtremeInteriorCoord(false, false, false).relative(Direction.EAST, depth).relative(Direction.UP, uCushion).relative(Direction.SOUTH, vCushion);
            case EAST -> getExtremeInteriorCoord(true, false, false).relative(Direction.WEST, depth).relative(Direction.UP, uCushion).relative(Direction.SOUTH, vCushion);
        };
    }

    public BlockPos getMaximumInteriorPlaneCoord(Direction normal, int depth, int uCushion, int vCushion) {
        if (normal == null) {
            return getExtremeInteriorCoord(false, false, false);
        }

        return switch (normal) {
            case DOWN -> getExtremeInteriorCoord(true, false, true).relative(Direction.UP, depth).relative(Direction.NORTH, uCushion).relative(Direction.WEST, vCushion);
            case UP -> getExtremeInteriorCoord(true, true, true).relative(Direction.DOWN, depth).relative(Direction.NORTH, uCushion).relative(Direction.WEST, vCushion);
            case NORTH -> getExtremeInteriorCoord(true, true, false).relative(Direction.SOUTH, depth).relative(Direction.WEST, uCushion).relative(Direction.DOWN, vCushion);
            case SOUTH -> getExtremeInteriorCoord(true, true, true).relative(Direction.NORTH, depth).relative(Direction.WEST, uCushion).relative(Direction.DOWN, vCushion);
            case WEST -> getExtremeInteriorCoord(false, true, true).relative(Direction.EAST, depth).relative(Direction.DOWN, uCushion).relative(Direction.NORTH, vCushion);
            case EAST -> getExtremeInteriorCoord(true, true, true).relative(Direction.WEST, depth).relative(Direction.DOWN, uCushion).relative(Direction.NORTH, vCushion);
        };
    }

    public Vector3f getMiddleInteriorPlaneCoord(Direction normal, int depth, int minUCushion, int minVCushion, int maxUCushion, int maxVCushion) {
        BlockPos min = getMinimumInteriorPlaneCoord(normal, depth, minUCushion, minVCushion);
        BlockPos max = getMaximumInteriorPlaneCoord(normal, depth, maxUCushion, maxVCushion);
        return new Vector3f((min.getX() + max.getX()) / 2F, (min.getY() + max.getY()) / 2F, (min.getZ() + max.getZ()) / 2F);
    }

    public Iterable<BlockPos> getInteriorPlaneMinX(int depth, int minUCushion, int minVCushion, int maxUCushion, int maxVCushion) {
        return BlockPos.betweenClosed(getMinimumInteriorPlaneCoord(Direction.WEST, depth, minUCushion, minVCushion), getMaximumInteriorPlaneCoord(Direction.WEST, depth, maxUCushion, maxVCushion));
    }

    public Iterable<BlockPos> getInteriorPlaneMaxX(int depth, int minUCushion, int minVCushion, int maxUCushion, int maxVCushion) {
        return BlockPos.betweenClosed(getMinimumInteriorPlaneCoord(Direction.EAST, depth, minUCushion, minVCushion), getMaximumInteriorPlaneCoord(Direction.EAST, depth, maxUCushion, maxVCushion));
    }

    public Iterable<BlockPos> getInteriorPlaneMinY(int depth, int minUCushion, int minVCushion, int maxUCushion, int maxVCushion) {
        return BlockPos.betweenClosed(getMinimumInteriorPlaneCoord(Direction.DOWN, depth, minUCushion, minVCushion), getMaximumInteriorPlaneCoord(Direction.DOWN, depth, maxUCushion, maxVCushion));
    }

    public Iterable<BlockPos> getInteriorPlaneMaxY(int depth, int minUCushion, int minVCushion, int maxUCushion, int maxVCushion) {
        return BlockPos.betweenClosed(getMinimumInteriorPlaneCoord(Direction.UP, depth, minUCushion, minVCushion), getMaximumInteriorPlaneCoord(Direction.UP, depth, maxUCushion, maxVCushion));
    }

    public Iterable<BlockPos> getInteriorPlaneMinZ(int depth, int minUCushion, int minVCushion, int maxUCushion, int maxVCushion) {
        return BlockPos.betweenClosed(getMinimumInteriorPlaneCoord(Direction.NORTH, depth, minUCushion, minVCushion), getMaximumInteriorPlaneCoord(Direction.NORTH, depth, maxUCushion, maxVCushion));
    }

    public Iterable<BlockPos> getInteriorPlaneMaxZ(int depth, int minUCushion, int minVCushion, int maxUCushion, int maxVCushion) {
        return BlockPos.betweenClosed(getMinimumInteriorPlaneCoord(Direction.SOUTH, depth, minUCushion, minVCushion), getMaximumInteriorPlaneCoord(Direction.SOUTH, depth, maxUCushion, maxVCushion));
    }

    public Iterable<BlockPos> getInteriorPlane(Direction normal, int depth, int minUCushion, int minVCushion, int maxUCushion, int maxVCushion) {
        if (normal == null) {
            return BlockPos.betweenClosed(getExtremeInteriorCoord(false, false, false), getExtremeInteriorCoord(false, false, false));
        }

        return switch (normal) {
            case DOWN -> getInteriorPlaneMinY(depth, minUCushion, minVCushion, maxUCushion, maxVCushion);
            case UP -> getInteriorPlaneMaxY(depth, minUCushion, minVCushion, maxUCushion, maxVCushion);
            case NORTH -> getInteriorPlaneMinZ(depth, minUCushion, minVCushion, maxUCushion, maxVCushion);
            case SOUTH -> getInteriorPlaneMaxZ(depth, minUCushion, minVCushion, maxUCushion, maxVCushion);
            case WEST -> getInteriorPlaneMinX(depth, minUCushion, minVCushion, maxUCushion, maxVCushion);
            case EAST -> getInteriorPlaneMaxX(depth, minUCushion, minVCushion, maxUCushion, maxVCushion);
        };
    }

    // Validation helpers

    public boolean standardLastError(BlockPos pos) {
        setLastError(pos, MODID + ".multiblock_validation.invalid_block", pos.getX(), pos.getY(), pos.getZ(), getWorld().getBlockState(pos).getBlock().getName());
        return false;
    }

    @Override
    protected boolean isBlockGoodForFrame(Level level, int x, int y, int z, IMultiblockValidator iMultiblockValidator) {
        return standardLastError(new BlockPos(x, y, z));
    }

    @Override
    protected boolean isBlockGoodForTop(Level level, int x, int y, int z, IMultiblockValidator iMultiblockValidator) {
        return standardLastError(new BlockPos(x, y, z));
    }

    @Override
    protected boolean isBlockGoodForBottom(Level level, int x, int y, int z, IMultiblockValidator iMultiblockValidator) {
        return standardLastError(new BlockPos(x, y, z));
    }

    @Override
    protected boolean isBlockGoodForSides(Level level, int x, int y, int z, IMultiblockValidator iMultiblockValidator) {
        return standardLastError(new BlockPos(x, y, z));
    }

    @Override
    public void markReferenceCoordForUpdate() {
        super.markReferenceCoordForUpdate();
    }

    // Clear Material

    public void clearAllMaterial() {
        for (IMultiblockPart<MULTIBLOCK> part : getConnectedParts()) {
            if (part instanceof ITileInventory tileInventory) {
                tileInventory.clearAllSlots();
            }
            if (part instanceof ITileFluid tileFluid) {
                tileFluid.clearAllTanks();
            }
        }
    }

    // Data synchronization

    public CompoundTag writeStacks(NonNullList<ItemStack> stacks, CompoundTag data, HolderLookup.Provider registries) {
        ContainerHelper.saveAllItems(data, stacks, registries);
        return data;
    }

    public void readStacks(NonNullList<ItemStack> stacks, CompoundTag data, HolderLookup.Provider registries) {
        ContainerHelper.loadAllItems(data, stacks, registries);
    }

    public CompoundTag writeTanks(List<Tank> tanks, CompoundTag data, HolderLookup.Provider registries, String name) {
        for (int i = 0; i < tanks.size(); ++i) {
            tanks.get(i).writeToNBT(data, registries, name + i);
        }
        return data;
    }

    public void readTanks(List<Tank> tanks, CompoundTag data, HolderLookup.Provider registries, String name) {
        for (int i = 0; i < tanks.size(); ++i) {
            tanks.get(i).readFromNBT(data, registries, name + i);
        }
    }

    public CompoundTag writeEnergy(EnergyStorage storage, CompoundTag data, HolderLookup.Provider registries, String string) {
        storage.writeToNBT(data, registries, string);
        return data;
    }

    public void readEnergy(EnergyStorage storage, CompoundTag data, HolderLookup.Provider registries, String string) {
        storage.readFromNBT(data, registries, string);
    }
}