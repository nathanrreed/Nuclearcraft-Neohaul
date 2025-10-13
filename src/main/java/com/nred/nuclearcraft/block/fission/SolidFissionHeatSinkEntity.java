package com.nred.nuclearcraft.block.fission;

import com.nred.nuclearcraft.multiblock.PlacementRule;
import com.nred.nuclearcraft.multiblock.fisson.FissionCluster;
import com.nred.nuclearcraft.multiblock.fisson.FissionPlacement;
import com.nred.nuclearcraft.multiblock.fisson.FissionReactor;
import com.nred.nuclearcraft.multiblock.fisson.solid.FissionHeatSinkType;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.zerono.mods.zerocore.lib.multiblock.cuboid.PartPosition;
import it.zerono.mods.zerocore.lib.multiblock.validation.IMultiblockValidator;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import java.util.Iterator;

import static com.nred.nuclearcraft.registration.BlockEntityRegistration.FISSION_ENTITY_TYPE;

public class SolidFissionHeatSinkEntity extends AbstractFissionEntity implements IFissionCoolingComponent {
    public FissionHeatSinkType heatSinkType;
    public String ruleID;

    public PlacementRule<FissionReactor, AbstractFissionEntity> placementRule;

    private FissionCluster cluster = null;
    private long heat = 0L;
    public int coolingRate;
    public boolean isInValidPosition = false;

    public SolidFissionHeatSinkEntity(final BlockPos position, final BlockState blockState, FissionHeatSinkType heatSinkType) {
        super(FISSION_ENTITY_TYPE.get("heat_sink").get(), position, blockState);
        this.heatSinkType = heatSinkType;
        this.placementRule = FissionPlacement.RULE_MAP.get(heatSinkType.getName() + "_sink");
        this.coolingRate = heatSinkType.getCoolingRate();
    }

    @Override
    public boolean isGoodForPosition(PartPosition position, IMultiblockValidator validatorCallback) {
        return position == PartPosition.Interior;
    }

    public static class Variant extends SolidFissionHeatSinkEntity {
        protected Variant(final BlockPos position, final BlockState blockState, FissionHeatSinkType dynamoCoilType) {
            super(position, blockState, dynamoCoilType);
        }
    }

    public static class Water extends SolidFissionHeatSinkEntity.Variant {
        public Water(final BlockPos position, final BlockState blockState) {
            super(position, blockState, FissionHeatSinkType.WATER);
        }
    }

    public static class Iron extends SolidFissionHeatSinkEntity.Variant {
        public Iron(final BlockPos position, final BlockState blockState) {
            super(position, blockState, FissionHeatSinkType.IRON);
        }
    }

    public static class Redstone extends SolidFissionHeatSinkEntity.Variant {
        public Redstone(final BlockPos position, final BlockState blockState) {
            super(position, blockState, FissionHeatSinkType.REDSTONE);
        }
    }

    public static class Quartz extends SolidFissionHeatSinkEntity.Variant {
        public Quartz(final BlockPos position, final BlockState blockState) {
            super(position, blockState, FissionHeatSinkType.QUARTZ);
        }
    }

    public static class Obsidian extends SolidFissionHeatSinkEntity.Variant {
        public Obsidian(final BlockPos position, final BlockState blockState) {
            super(position, blockState, FissionHeatSinkType.OBSIDIAN);
        }
    }

    public static class NetherBrick extends SolidFissionHeatSinkEntity.Variant {
        public NetherBrick(final BlockPos position, final BlockState blockState) {
            super(position, blockState, FissionHeatSinkType.NETHER_BRICK);
        }
    }

    public static class Glowstone extends SolidFissionHeatSinkEntity.Variant {
        public Glowstone(final BlockPos position, final BlockState blockState) {
            super(position, blockState, FissionHeatSinkType.GLOWSTONE);
        }
    }

    public static class Lapis extends SolidFissionHeatSinkEntity.Variant {
        public Lapis(final BlockPos position, final BlockState blockState) {
            super(position, blockState, FissionHeatSinkType.LAPIS);
        }
    }

    public static class Gold extends SolidFissionHeatSinkEntity.Variant {
        public Gold(final BlockPos position, final BlockState blockState) {
            super(position, blockState, FissionHeatSinkType.GOLD);
        }
    }

    public static class Prismarine extends SolidFissionHeatSinkEntity.Variant {
        public Prismarine(final BlockPos position, final BlockState blockState) {
            super(position, blockState, FissionHeatSinkType.PRISMARINE);
        }
    }

    public static class Slime extends SolidFissionHeatSinkEntity.Variant {
        public Slime(final BlockPos position, final BlockState blockState) {
            super(position, blockState, FissionHeatSinkType.SLIME);
        }
    }

    public static class EndStone extends SolidFissionHeatSinkEntity.Variant {
        public EndStone(final BlockPos position, final BlockState blockState) {
            super(position, blockState, FissionHeatSinkType.END_STONE);
        }
    }

    public static class Purpur extends SolidFissionHeatSinkEntity.Variant {
        public Purpur(final BlockPos position, final BlockState blockState) {
            super(position, blockState, FissionHeatSinkType.PURPUR);
        }
    }

    public static class Diamond extends SolidFissionHeatSinkEntity.Variant {
        public Diamond(final BlockPos position, final BlockState blockState) {
            super(position, blockState, FissionHeatSinkType.DIAMOND);
        }
    }

    public static class Emerald extends SolidFissionHeatSinkEntity.Variant {
        public Emerald(final BlockPos position, final BlockState blockState) {
            super(position, blockState, FissionHeatSinkType.EMERALD);
        }
    }

    public static class Copper extends SolidFissionHeatSinkEntity.Variant {
        public Copper(final BlockPos position, final BlockState blockState) {
            super(position, blockState, FissionHeatSinkType.COPPER);
        }
    }

    public static class Tin extends SolidFissionHeatSinkEntity.Variant {
        public Tin(final BlockPos position, final BlockState blockState) {
            super(position, blockState, FissionHeatSinkType.TIN);
        }
    }

    public static class Lead extends SolidFissionHeatSinkEntity.Variant {
        public Lead(final BlockPos position, final BlockState blockState) {
            super(position, blockState, FissionHeatSinkType.LEAD);
        }
    }

    public static class Boron extends SolidFissionHeatSinkEntity.Variant {
        public Boron(final BlockPos position, final BlockState blockState) {
            super(position, blockState, FissionHeatSinkType.BORON);
        }
    }

    public static class Lithium extends SolidFissionHeatSinkEntity.Variant {
        public Lithium(final BlockPos position, final BlockState blockState) {
            super(position, blockState, FissionHeatSinkType.LITHIUM);
        }
    }

    public static class Magnesium extends SolidFissionHeatSinkEntity.Variant {
        public Magnesium(final BlockPos position, final BlockState blockState) {
            super(position, blockState, FissionHeatSinkType.MAGNESIUM);
        }
    }

    public static class Manganese extends SolidFissionHeatSinkEntity.Variant {
        public Manganese(final BlockPos position, final BlockState blockState) {
            super(position, blockState, FissionHeatSinkType.MANGANESE);
        }
    }

    public static class Aluminum extends SolidFissionHeatSinkEntity.Variant {
        public Aluminum(final BlockPos position, final BlockState blockState) {
            super(position, blockState, FissionHeatSinkType.ALUMINUM);
        }
    }

    public static class Silver extends SolidFissionHeatSinkEntity.Variant {
        public Silver(final BlockPos position, final BlockState blockState) {
            super(position, blockState, FissionHeatSinkType.SILVER);
        }
    }

    public static class Fluorite extends SolidFissionHeatSinkEntity.Variant {
        public Fluorite(final BlockPos position, final BlockState blockState) {
            super(position, blockState, FissionHeatSinkType.FLUORITE);
        }
    }

    public static class Villiaumite extends SolidFissionHeatSinkEntity.Variant {
        public Villiaumite(final BlockPos position, final BlockState blockState) {
            super(position, blockState, FissionHeatSinkType.VILLIAUMITE);
        }
    }

    public static class Carobbiite extends SolidFissionHeatSinkEntity.Variant {
        public Carobbiite(final BlockPos position, final BlockState blockState) {
            super(position, blockState, FissionHeatSinkType.CAROBBIITE);
        }
    }

    public static class Arsenic extends SolidFissionHeatSinkEntity.Variant {
        public Arsenic(final BlockPos position, final BlockState blockState) {
            super(position, blockState, FissionHeatSinkType.ARSENIC);
        }
    }

    public static class LiquidNitrogen extends SolidFissionHeatSinkEntity.Variant {
        public LiquidNitrogen(final BlockPos position, final BlockState blockState) {
            super(position, blockState, FissionHeatSinkType.LIQUID_NITROGEN);
        }
    }

    public static class LiquidHelium extends SolidFissionHeatSinkEntity.Variant {
        public LiquidHelium(final BlockPos position, final BlockState blockState) {
            super(position, blockState, FissionHeatSinkType.LIQUID_HELIUM);
        }
    }

    public static class Enderium extends SolidFissionHeatSinkEntity.Variant {
        public Enderium(final BlockPos position, final BlockState blockState) {
            super(position, blockState, FissionHeatSinkType.ENDERIUM);
        }
    }

    public static class Cryotheum extends SolidFissionHeatSinkEntity.Variant {
        public Cryotheum(final BlockPos position, final BlockState blockState) {
            super(position, blockState, FissionHeatSinkType.CRYOTHEUM);
        }
    }

    // IFissionComponent

    @Override
    public @Nullable FissionCluster getCluster() {
        return cluster;
    }

    @Override
    public void setClusterInternal(@Nullable FissionCluster cluster) {
        this.cluster = cluster;
    }

    @Override
    public boolean isValidHeatConductor(final Long2ObjectMap<IFissionComponent> componentFailCache, final Long2ObjectMap<IFissionComponent> assumedValidCache, boolean simulate) {
        if (componentFailCache.containsKey(worldPosition.asLong())) {
            return isInValidPosition = false;
        } else if (placementRule.requiresRecheck()) {
            isInValidPosition = placementRule.satisfied(this, simulate);
            if (isInValidPosition) {
                assumedValidCache.put(worldPosition.asLong(), this);
            }
            return isInValidPosition;
        } else if (isInValidPosition) {
            return true;
        }
        return isInValidPosition = placementRule.satisfied(this, simulate);
    }

    @Override
    public boolean isFunctional(boolean simulate) {
        return isInValidPosition;
    }

    @Override
    public void resetStats() {
        isInValidPosition = false;
    }

    @Override
    public boolean isClusterRoot() {
        return false;
    }

    @Override
    public long getHeatStored() {
        return heat;
    }

    @Override
    public void setHeatStored(long heat) {
        this.heat = heat;
    }

    @Override
    public void onClusterMeltdown(Iterator<IFissionComponent> componentIterator) {

    }

    @Override
    public boolean isNullifyingSources(Direction side, boolean simulate) {
        return false;
    }

    @Override
    public long getCooling(boolean simulate) {
        return coolingRate;
    }

    @Override
    public CompoundTag writeAll(CompoundTag nbt, HolderLookup.Provider registries) {
        super.writeAll(nbt, registries);
        nbt.putLong("clusterHeat", heat);
        return nbt;
    }

    @Override
    public void readAll(CompoundTag nbt, HolderLookup.Provider registries) {
        super.readAll(nbt, registries);
        heat = nbt.getLong("clusterHeat");
    }

    // ComputerCraft

    @Override
    public String getCCKey() {
        return "sink";
    }

    @Override
    public Object getCCInfo() {
        Object2ObjectMap<String, Object> entry = new Object2ObjectLinkedOpenHashMap<>();
        entry.put("type", heatSinkType.getSerializedName());
        entry.put("cooling", getCooling(false));
        entry.put("is_valid", isInValidPosition);
        return entry;
    }
}