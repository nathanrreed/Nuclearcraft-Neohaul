package com.nred.nuclearcraft.block.fission;

import com.nred.nuclearcraft.block.fission.IFissionFuelComponent.ModeratorBlockInfo;
import com.nred.nuclearcraft.block.fission.IFissionFuelComponent.ModeratorLine;
import com.nred.nuclearcraft.multiblock.fisson.FissionCluster;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;

import javax.annotation.Nullable;
import java.util.Iterator;

public interface IFissionComponent extends IFissionPart {

    @Nullable
    FissionCluster getCluster();

    default FissionCluster newCluster(int id) {
        return new FissionCluster(getMultiblockController().get(), id);
    }

    default void setCluster(@Nullable FissionCluster cluster) {
        if (cluster == null && getCluster() != null) {
            // getCluster().getComponentMap().remove(pos.toLong());
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

//    // IMultitoolLogic TODO
//
//    @Override
//    default boolean onUseMultitool(ItemStack multitool, EntityPlayerMP player, World world, Direction facing, float hitX, float hitY, float hitZ) {
//        NBTTagCompound nbt = NBTHelper.getStackNBT(multitool, "ncMultitool");
//        if (nbt != null) {
//            if (player.isSneaking()) {
//                NBTTagCompound info = new NBTTagCompound();
//                String displayName = getTileBlockDisplayName();
//                info.setString("displayName", displayName);
//                info.setLong("componentPos", getTilePos().toLong());
//                player.sendMessage(new TextComponentString(Lang.localize("info.nuclearcraft.multitool.save_component_info", displayName)));
//                nbt.setTag("fissionComponentInfo", info);
//                return true;
//            }
//        }
//        return IFissionPart.super.onUseMultitool(multitool, player, world, facing, hitX, hitY, hitZ);
//    }
}
