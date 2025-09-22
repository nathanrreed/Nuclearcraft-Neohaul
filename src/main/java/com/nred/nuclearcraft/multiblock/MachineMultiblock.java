package com.nred.nuclearcraft.multiblock;

import com.nred.nuclearcraft.util.NCMath;
import it.zerono.mods.zerocore.lib.multiblock.cuboid.AbstractCuboidMultiblockController;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import org.joml.Vector3f;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public abstract class MachineMultiblock<MULTIBLOCK extends MachineMultiblock<MULTIBLOCK>> extends AbstractCuboidMultiblockController<MULTIBLOCK> implements IMultiblock {
    protected MachineMultiblock(Level world) {
        super(world);
    }

    @Override
    public Level getLevel() {
        return getWorld();
    }

    public RandomSource rand = getWorld().random;

    public abstract int getMinimumInteriorLength();

    public abstract int getMaximumInteriorLength();

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
        return getParts(clazz).iterator();
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
}
