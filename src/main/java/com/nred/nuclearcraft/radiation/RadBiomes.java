package com.nred.nuclearcraft.radiation;

import com.nred.nuclearcraft.util.RegistryHelper;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;
import it.unimi.dsi.fastutil.objects.Object2DoubleMap;
import it.unimi.dsi.fastutil.objects.Object2DoubleOpenHashMap;
import net.minecraft.core.Holder;
import net.minecraft.world.level.biome.Biome;

import static com.nred.nuclearcraft.config.NCConfig.*;

public class RadBiomes {
	public static final Object2DoubleMap<Biome> RAD_MAP = new Object2DoubleOpenHashMap<>();
	public static final Object2DoubleMap<Biome> LIMIT_MAP = new Object2DoubleOpenHashMap<>();
	public static final IntSet DIM_BLACKLIST = new IntOpenHashSet();

	public static void init() { // TODO call
		for (String biomeInfo : radiation_biomes) {
			int scorePos = biomeInfo.lastIndexOf('_');
			if (scorePos == -1) {
				continue;
			}
            Holder.Reference<Biome> biome = RegistryHelper.biomeFromRegistry(biomeInfo.substring(0, scorePos));
			if (biome != null) {
				RAD_MAP.put(biome.value(), Double.parseDouble(biomeInfo.substring(scorePos + 1)));
			}
		}

		for (String biomeInfo : radiation_biome_limits) {
			int scorePos = biomeInfo.lastIndexOf('_');
			if (scorePos == -1) {
				continue;
			}
			Holder.Reference<Biome> biome = RegistryHelper.biomeFromRegistry(biomeInfo.substring(0, scorePos));
			if (biome != null) {
				LIMIT_MAP.put(biome.value(), Double.parseDouble(biomeInfo.substring(scorePos + 1)));
			}
		}

		for (int dim : radiation_from_biomes_dims_blacklist) {
			DIM_BLACKLIST.add(dim);
		}
	}
}