/**
 * Massive thanks to McJty, maker of RFTools and many other mods, for letting me use this code!
 */

package com.nred.nuclearcraft.util;

import it.unimi.dsi.fastutil.objects.Object2BooleanMap;
import it.unimi.dsi.fastutil.objects.Object2BooleanOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

import javax.annotation.Nonnull;

public class StructureHelper {
    public static final StructureHelper CACHE = new StructureHelper();

    private final Object2BooleanMap<StructureCacheEntry> structureCache = new Object2BooleanOpenHashMap<>();

    public void clear() {
        structureCache.clear();
    }

    public boolean isInStructure(Level level, @Nonnull String structure, BlockPos pos) {
        return false;
//        ResourceKey<Level> dimension = level.dimension(); TODO
//        ChunkPos chunkPos = new ChunkPos(pos);
//        long chunkPosLong = ChunkPos.asLong(chunkPos.x, chunkPos.z);
//        StructureCacheEntry entry = new StructureCacheEntry(structure, dimension, chunkPosLong);
//        if (structureCache.containsKey(entry)) {
//            return structureCache.getBoolean(entry);
//        }
//
//        MapGenStructureData data = (MapGenStructureData) level.getPerWorldStorage().getOrLoadData(MapGenStructureData.class, structure);
//        if (data == null) {
//            structureCache.put(entry, false);
//            return false;
//        }
//
//        LongSet longs = parseStructureData(data);
//        for (Long l : longs) {
//            structureCache.put(new StructureCacheEntry(structure, dimension, l), true);
//        }
//        if (structureCache.containsKey(entry)) {
//            return true;
//        } else {
//            structureCache.put(entry, false);
//            return false;
//        }
    }

//    private static LongSet parseStructureData(MapGenStructureData data) { TODO
//        LongSet chunks = new LongOpenHashSet();
//        CompoundTag nbttagcompound = data.getTagCompound();
//
//        for (String s : nbttagcompound.getKeySet()) {
//            NBTBase nbtbase = nbttagcompound.getTag(s);
//
//            if (nbtbase.getId() == 10) {
//                CompoundTag nbt = (CompoundTag) nbtbase;
//
//                if (nbt.contains("ChunkX") && nbt.contains("ChunkZ")) {
//                    int i = nbt.getInt("ChunkX");
//                    int j = nbt.getInt("ChunkZ");
//                    chunks.add(ChunkPos.asLong(i, j));
//                }
//            }
//        }
//        return chunks;
//    }

    public static class StructureCacheEntry {
        @Nonnull
        private final String structure;
        private final ResourceKey<Level> dimension;
        private final long chunkPos;

        public StructureCacheEntry(@Nonnull String structure, ResourceKey<Level> dimension, long chunkPos) {
            this.structure = structure;
            this.dimension = dimension;
            this.chunkPos = chunkPos;
        }

        @Nonnull
        public String getStructure() {
            return structure;
        }

        public ResourceKey<Level> getDimension() {
            return dimension;
        }

        public long getChunkPos() {
            return chunkPos;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof StructureCacheEntry other)) {
                return false;
            }

            if (dimension != other.dimension) {
                return false;
            }
            if (chunkPos != other.chunkPos) {
                return false;
            }
            if (!structure.equals(other.structure)) {
                return false;
            }

            return true;
        }

        @Override
        public int hashCode() {
            int result = structure.hashCode();
            result = 31 * result + dimension.hashCode();
            result = 31 * result + (int) (chunkPos ^ chunkPos >>> 32);
            return result;
        }
    }
}