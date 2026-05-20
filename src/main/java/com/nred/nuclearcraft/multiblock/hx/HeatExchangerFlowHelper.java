package com.nred.nuclearcraft.multiblock.hx;

import it.unimi.dsi.fastutil.longs.*;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;

public class HeatExchangerFlowHelper {

    public static Long2ObjectMap<ObjectSet<Vec3>> getFlowMap(LongSet inletPosLongSet, LongSet outletPosLongSet, Function<BlockPos, HeatExchangerTubeSetting[]> connectionsFunction, Predicate<HeatExchangerTubeSetting> openPredicate, BiPredicate<BlockPos, Direction> spacePredicate, Predicate<BlockPos> outletPredicate) {
        Long2ObjectMap<ObjectSet<Vec3>> flowMap = new Long2ObjectOpenHashMap<>();

        if (inletPosLongSet.isEmpty() || outletPosLongSet.isEmpty()) {
            return flowMap;
        }

        Long2ObjectMap<Long2IntMap> inletDistanceMap = new Long2ObjectOpenHashMap<>();
        Long2ObjectMap<Long2IntMap> outletDistanceMap = new Long2ObjectOpenHashMap<>();

        for (long inletPosLong : inletPosLongSet) {
            inletDistanceMap.put(inletPosLong, getDistanceMap(BlockPos.of(inletPosLong), connectionsFunction, openPredicate, spacePredicate));
        }

        for (long outletPosLong : outletPosLongSet) {
            outletDistanceMap.put(outletPosLong, getReverseDistanceMap(BlockPos.of(outletPosLong), connectionsFunction, openPredicate, spacePredicate));
        }

        for (long inletPosLong : inletPosLongSet) {
            BlockPos inletPos = BlockPos.of(inletPosLong);
            Long2IntMap fromInlet = inletDistanceMap.get(inletPosLong);

            if (fromInlet == null || fromInlet.isEmpty()) {
                continue;
            }

            for (long outletPosLong : outletPosLongSet) {
                BlockPos outletPos = BlockPos.of(outletPosLong);
                Long2IntMap toOutlet = outletDistanceMap.get(outletPosLong);

                if (toOutlet == null || toOutlet.isEmpty()) {
                    continue;
                }

                int shortestPathLength = Integer.MAX_VALUE;
                for (Long2IntMap.Entry entry : fromInlet.long2IntEntrySet()) {
                    long posLong = entry.getLongKey();
                    if (toOutlet.containsKey(posLong)) {
                        shortestPathLength = Math.min(shortestPathLength, entry.getIntValue() + toOutlet.get(posLong));
                    }
                }

                if (shortestPathLength != Integer.MAX_VALUE) {
                    addShortestPathDirs(flowMap, inletPos, outletPos, fromInlet, toOutlet, shortestPathLength, connectionsFunction, openPredicate, spacePredicate);
                }
            }
        }

        return flowMap;
    }

    private static Long2IntMap getDistanceMap(BlockPos inletPos, Function<BlockPos, HeatExchangerTubeSetting[]> connectionsFunction, Predicate<HeatExchangerTubeSetting> openPredicate, BiPredicate<BlockPos, Direction> spacePredicate) {
        Long2IntMap distanceMap = new Long2IntOpenHashMap();
        distanceMap.defaultReturnValue(-1);

        Queue<BlockPos> queue = new ArrayDeque<>();

        for (Direction dir : Direction.values()) {
            if (isTraversableDir(inletPos, dir, connectionsFunction, openPredicate, spacePredicate)) {
                BlockPos offsetPos = inletPos.relative(dir);
                long offsetPosLong = offsetPos.asLong();
                if (!distanceMap.containsKey(offsetPosLong)) {
                    distanceMap.put(offsetPosLong, 1);
                    queue.add(offsetPos);
                }
            }
        }

        while (!queue.isEmpty()) {
            BlockPos pos = queue.poll();
            int dist = distanceMap.get(pos.asLong());

            for (Direction dir : Direction.values()) {
                if (isTraversableDir(pos, dir, connectionsFunction, openPredicate, spacePredicate)) {
                    BlockPos offsetPos = pos.relative(dir);
                    long offsetPosLong = offsetPos.asLong();
                    if (!distanceMap.containsKey(offsetPosLong)) {
                        distanceMap.put(offsetPosLong, dist + 1);
                        queue.add(offsetPos);
                    }
                }
            }
        }

        return distanceMap;
    }

    private static Long2IntMap getReverseDistanceMap(BlockPos outletPos, Function<BlockPos, HeatExchangerTubeSetting[]> connectionsFunction, Predicate<HeatExchangerTubeSetting> openPredicate, BiPredicate<BlockPos, Direction> spacePredicate) {
        Long2IntMap distanceMap = new Long2IntOpenHashMap();
        distanceMap.defaultReturnValue(-1);

        Queue<BlockPos> queue = new ArrayDeque<>();

        for (Direction dirFromOutletToSpace : Direction.values()) {
            BlockPos spacePos = outletPos.relative(dirFromOutletToSpace);
            Direction dirFromSpaceToOutlet = dirFromOutletToSpace.getOpposite();

            if (spacePredicate.test(spacePos, dirFromOutletToSpace) && isOpenDir(spacePos, dirFromSpaceToOutlet, connectionsFunction, openPredicate)) {
                long spacePosLong = spacePos.asLong();
                if (!distanceMap.containsKey(spacePosLong)) {
                    distanceMap.put(spacePosLong, 1);
                    queue.add(spacePos);
                }
            }
        }

        while (!queue.isEmpty()) {
            BlockPos pos = queue.poll();
            int dist = distanceMap.get(pos.asLong());

            for (Direction dirFromPosToPrevious : Direction.values()) {
                BlockPos previousPos = pos.relative(dirFromPosToPrevious);
                Direction dirFromPreviousToPos = dirFromPosToPrevious.getOpposite();

                if (spacePredicate.test(previousPos, dirFromPosToPrevious) && isTraversableDir(previousPos, dirFromPreviousToPos, connectionsFunction, openPredicate, spacePredicate)) {
                    long previousPosLong = previousPos.asLong();
                    if (!distanceMap.containsKey(previousPosLong)) {
                        distanceMap.put(previousPosLong, dist + 1);
                        queue.add(previousPos);
                    }
                }
            }
        }

        return distanceMap;
    }

    private static void addShortestPathDirs(Long2ObjectMap<ObjectSet<Vec3>> flowMap, BlockPos inletPos, BlockPos outletPos, Long2IntMap fromInlet, Long2IntMap toOutlet, int shortestPathLength, Function<BlockPos, HeatExchangerTubeSetting[]> connectionsFunction, Predicate<HeatExchangerTubeSetting> openPredicate, BiPredicate<BlockPos, Direction> spacePredicate) {
        for (Long2IntMap.Entry entry : fromInlet.long2IntEntrySet()) {
            long posLong = entry.getLongKey();
            int inletDist = entry.getIntValue();
            int outletDist = toOutlet.get(posLong);

            if (outletDist == -1 || inletDist + outletDist != shortestPathLength) {
                continue;
            }

            BlockPos pos = BlockPos.of(posLong);
            List<BlockPos> incomingPosList = getIncomingPosList(pos, inletPos, fromInlet, toOutlet, connectionsFunction, openPredicate, spacePredicate);
            List<BlockPos> outgoingPosList = getOutgoingPosList(pos, outletPos, fromInlet, toOutlet, connectionsFunction, openPredicate, spacePredicate);

            if (incomingPosList.isEmpty() || outgoingPosList.isEmpty()) {
                continue;
            }

            ObjectSet<Vec3> vecs = flowMap.get(posLong);
            if (vecs == null) {
                vecs = new ObjectOpenHashSet<>();
                flowMap.put(posLong, vecs);
            }

            for (BlockPos incomingPos : incomingPosList) {
                for (BlockPos outgoingPos : outgoingPosList) {
                    BlockPos flowDir = outgoingPos.subtract(incomingPos);
                    vecs.add(new Vec3(flowDir.getX(), flowDir.getY(), flowDir.getZ()).normalize());
                }
            }
        }
    }

    private static List<BlockPos> getIncomingPosList(BlockPos pos, BlockPos inletPos, Long2IntMap fromInlet, Long2IntMap toOutlet, Function<BlockPos, HeatExchangerTubeSetting[]> connectionsFunction, Predicate<HeatExchangerTubeSetting> openPredicate, BiPredicate<BlockPos, Direction> spacePredicate) {
        List<BlockPos> incomingPosList = new ArrayList<>();
        long posLong = pos.asLong();
        int inletDist = fromInlet.get(posLong);
        int outletDist = toOutlet.get(posLong);

        for (Direction dirFromPosToIncoming : Direction.values()) {
            BlockPos incomingPos = pos.relative(dirFromPosToIncoming);
            Direction dirFromIncomingToPos = dirFromPosToIncoming.getOpposite();

            if (incomingPos.equals(inletPos)) {
                if (inletDist == 1 && isTraversableDir(incomingPos, dirFromIncomingToPos, connectionsFunction, openPredicate, spacePredicate)) {
                    incomingPosList.add(incomingPos);
                }
                continue;
            }

            long incomingPosLong = incomingPos.asLong();
            if (fromInlet.get(incomingPosLong) == inletDist - 1 && toOutlet.get(incomingPosLong) == outletDist + 1 && isTraversableDir(incomingPos, dirFromIncomingToPos, connectionsFunction, openPredicate, spacePredicate)) {
                incomingPosList.add(incomingPos);
            }
        }

        return incomingPosList;
    }

    private static List<BlockPos> getOutgoingPosList(BlockPos pos, BlockPos outletPos, Long2IntMap fromInlet, Long2IntMap toOutlet, Function<BlockPos, HeatExchangerTubeSetting[]> connectionsFunction, Predicate<HeatExchangerTubeSetting> openPredicate, BiPredicate<BlockPos, Direction> spacePredicate) {
        List<BlockPos> outgoingPosList = new ArrayList<>();
        long posLong = pos.asLong();
        int inletDist = fromInlet.get(posLong);
        int outletDist = toOutlet.get(posLong);

        for (Direction dirFromPosToOutgoing : Direction.values()) {
            BlockPos outgoingPos = pos.relative(dirFromPosToOutgoing);

            if (outgoingPos.equals(outletPos)) {
                if (outletDist == 1 && isOpenDir(pos, dirFromPosToOutgoing, connectionsFunction, openPredicate)) {
                    outgoingPosList.add(outgoingPos);
                }
                continue;
            }

            long outgoingPosLong = outgoingPos.asLong();
            if (fromInlet.get(outgoingPosLong) == inletDist + 1 && toOutlet.get(outgoingPosLong) == outletDist - 1 && isTraversableDir(pos, dirFromPosToOutgoing, connectionsFunction, openPredicate, spacePredicate)) {
                outgoingPosList.add(outgoingPos);
            }
        }

        return outgoingPosList;
    }

    private static boolean isTraversableDir(BlockPos pos, Direction dir, Function<BlockPos, HeatExchangerTubeSetting[]> connectionsFunction, Predicate<HeatExchangerTubeSetting> openPredicate, BiPredicate<BlockPos, Direction> spacePredicate) {
        return isOpenDir(pos, dir, connectionsFunction, openPredicate) && spacePredicate.test(pos.relative(dir), dir);
    }

    private static boolean isOpenDir(BlockPos pos, Direction dir, Function<BlockPos, HeatExchangerTubeSetting[]> connectionsFunction, Predicate<HeatExchangerTubeSetting> openPredicate) {
        HeatExchangerTubeSetting[] connections = connectionsFunction.apply(pos);
        return connections == null || openPredicate.test(connections[dir.ordinal()]);
    }
}
