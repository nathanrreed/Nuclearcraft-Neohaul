package com.nred.nuclearcraft.block_entity.fission;

import com.nred.nuclearcraft.block_entity.fission.IFissionFuelComponent.ModeratorBlockInfo;
import com.nred.nuclearcraft.block_entity.fission.IFissionFuelComponent.ModeratorLine;
import com.nred.nuclearcraft.multiblock.fisson.FissionCluster;
import com.nred.nuclearcraft.util.NBTHelper;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

import javax.annotation.Nullable;
import java.util.Iterator;

import static com.nred.nuclearcraft.NuclearcraftNeohaul.MODID;

public interface IFissionComponent extends IFissionPart {
    @Nullable
    FissionCluster getCluster();

    default FissionCluster newCluster(int id) {
        return new FissionCluster(getMultiblockController().get(), id);
    }

    default void setCluster(@Nullable FissionCluster cluster) {
        if (cluster == null && getCluster() != null) {
            // getCluster().getComponentMap().remove(pos.toLong()); original
        } else if (cluster != null) {
            cluster.getComponentMap().put(getWorldPosition().asLong(), this);
        }
        setClusterInternal(cluster);
    }

    void setClusterInternal(@Nullable FissionCluster cluster);

    default boolean isClusterSearched() {
        return getCluster() != null;
    }

    /**
     * Unlike {@link IFissionComponent#isFunctional}, includes checking logic during clusterSearch if necessary!
     */
    boolean isValidHeatConductor(final Long2ObjectMap<IFissionComponent> componentFailCache, final Long2ObjectMap<IFissionComponent> assumedValidCache, boolean simulate);

    boolean isFunctional(boolean simulate);

    void resetStats();

    boolean isClusterRoot();

    default void clusterSearch(Integer id, final Object2IntMap<IFissionComponent> clusterSearchCache, final Long2ObjectMap<IFissionComponent> componentFailCache, final Long2ObjectMap<IFissionComponent> assumedValidCache, boolean simulate) {
        if (!isValidHeatConductor(componentFailCache, assumedValidCache, simulate)) {
            return;
        }

        if (isClusterSearched()) {
            if (id != null) {
                getMultiblockController().get().mergeClusters(id, getCluster());
            }
            return;
        }

        if (id == null) {
            id = getMultiblockController().get().clusterCount++;
        }
        FissionCluster cluster = getMultiblockController().get().getClusterMap().get(id.intValue());
        if (cluster == null) {
            cluster = newCluster(id);
            getMultiblockController().get().getClusterMap().put(id.intValue(), cluster);
        }
        setCluster(cluster);

        for (Direction dir : Direction.values()) {
            BlockPos offPos = getWorldPosition().relative(dir);
            if (!getCluster().connectedToWall) {
                BlockEntity tile = getPartWorld().get().getBlockEntity(offPos);
                if (tile instanceof AbstractFissionEntity part && part.getPartPosition().isFace()) { // TODO CHECK .isGoodForWall()
                    getCluster().connectedToWall = true;
                    continue;
                }
            }
            IFissionComponent component = getMultiblockController().get().getPartMap(IFissionComponent.class).get(offPos.asLong());
            if (component != null) {
                clusterSearchCache.put(component, id);
            }
        }
    }

    long getHeatStored();

    void setHeatStored(long heat);

    void onClusterMeltdown(Iterator<IFissionComponent> componentIterator);

    boolean isNullifyingSources(Direction side, boolean simulate);

    // Moderator Line

    default ModeratorBlockInfo getModeratorBlockInfo(Direction dir, boolean validActiveModeratorPos) {
        return null;
    }

    /**
     * The moderator line does not necessarily have to be complete!
     */
    default void onAddedToModeratorCache(ModeratorBlockInfo thisInfo) {
    }

    /**
     * Called if and only if the moderator line from the fuel component searching in the dir direction is complete!
     */
    default void onModeratorLineComplete(ModeratorLine line, ModeratorBlockInfo thisInfo, Direction dir) {
    }

    /**
     * Called during cluster searches!
     */
    default boolean isActiveModerator() {
        return false;
    }

    @Override
    default boolean onUseMultitool(ItemStack multitool, ServerPlayer player, Level level, Direction facing, BlockPos hitPos) {
        CompoundTag nbt = NBTHelper.getStackNBT(multitool, "ncMultitool");
        if (nbt != null) {
            if (player.isCrouching()) {
                CompoundTag info = new CompoundTag();
                Component displayName = getTileBlockDisplayName();
                info.putString("displayName", displayName.getString());
                info.putLong("componentPos", getTilePos().asLong());
                player.sendSystemMessage(Component.translatable(MODID + ".message.multitool.save_component_info", displayName));
                nbt.put("fissionComponentInfo", info);
                return true;
            }
        }
        return IFissionPart.super.onUseMultitool(multitool, player, level, facing, hitPos);
    }

    // ComputerCraft

    String getCCKey();

    Object getCCInfo();
}
