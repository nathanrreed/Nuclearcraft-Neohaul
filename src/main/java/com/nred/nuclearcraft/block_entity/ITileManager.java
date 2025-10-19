package com.nred.nuclearcraft.block_entity;

import com.nred.nuclearcraft.multiblock.ILogicMultiblock;
import com.nred.nuclearcraft.multiblock.Multiblock;
import com.nred.nuclearcraft.multiblock.MultiblockLogic;
import com.nred.nuclearcraft.render.BlockHighlightTracker;
import com.nred.nuclearcraft.util.NBTHelper;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import it.unimi.dsi.fastutil.longs.LongSet;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Map;
import java.util.Optional;

import static com.nred.nuclearcraft.NuclearcraftNeohaul.MODID;

public interface ITileManager<MULTIBLOCK extends Multiblock<MULTIBLOCK> & ILogicMultiblock<MULTIBLOCK, LOGIC>, LOGIC extends MultiblockLogic<MULTIBLOCK, LOGIC>, MANAGER extends ITileManager<MULTIBLOCK, LOGIC, MANAGER, LISTENER>, LISTENER extends ITileManagerListener<MULTIBLOCK, LOGIC, MANAGER, LISTENER>> extends ITileLogicMultiblockPart<MULTIBLOCK, LOGIC> {

    LongSet getListenerPosSet();

    boolean getRefreshListenersFlag();

    void setRefreshListenersFlag(boolean flag);

    default void refreshManager() {
        refreshListeners();
    }

    @SuppressWarnings("unchecked")
    default void refreshListeners() {
        setRefreshListenersFlag(false);

        Optional<MULTIBLOCK> multiblock = getMultiblockController();
        if (multiblock.isEmpty()) {
            return;
        }

        boolean refresh = false;
        LongSet listenerPosSet = getListenerPosSet(), invalidPosSet = new LongOpenHashSet();
        for (Long listenerPos : listenerPosSet) {
            LISTENER listener = (LISTENER) multiblock.get().getPartMap(getListenerClass()).get(listenerPos);
            if (listener != null) {
                if (listener.onManagerRefresh((MANAGER) this)) {
                    refresh = true;
                }
            } else {
                invalidPosSet.add(listenerPos);
            }
        }

        listenerPosSet.removeAll(invalidPosSet);

        if (refresh) {
            refreshMultiblock();
        }
    }

    void refreshMultiblock();

    String getManagerType();

    Class<?> getListenerClass();

    boolean isManagerActive();

    @Override
    default void onBlockNeighborChanged(BlockState state, Level world, BlockPos pos, BlockPos fromPos) {
        boolean wasActive = isManagerActive();
        ITileLogicMultiblockPart.super.onBlockNeighborChanged(state, world, pos, fromPos);
        setActivity(isManagerActive());
        if (!world.isClientSide && wasActive != isManagerActive()) {
            refreshListeners();
        }
    }

    // IMultitoolLogic


    @Override
    default boolean onUseMultitool(ItemStack multitool, ServerPlayer player, Level level, Direction facing, BlockPos hitPos) {
        CompoundTag nbt = NBTHelper.getStackNBT(multitool, "ncMultitool");
        if (nbt != null) {
            MULTIBLOCK multiblock;
            if (player.isCrouching()) {
                CompoundTag info = new CompoundTag();
                Component displayName = getTileBlockDisplayName();
                info.putString("managerType", getManagerType());
                info.putString("displayName", displayName.getString());
                info.putLong("managerPos", getTilePos().asLong());
                info.putInt("listenerCount", 0);
                player.sendSystemMessage(Component.translatable(MODID + ".message.multitool.start_manager_listener_set", displayName));
                nbt.put("componentManagerInfo", info);
                return true;
            } else if ((multiblock = getMultiblockController().orElse(null)) != null) {
                if (nbt.contains("componentManagerInfo", 10)) {
                    CompoundTag info = nbt.getCompound("componentManagerInfo");
                    if (info.getLong("managerPos") == getTilePos().asLong()) {
                        Component displayName = getTileBlockDisplayName();
                        if (info.getString("managerType").equals(getManagerType())) {
                            LongSet listenerPosSet = getListenerPosSet();
                            listenerPosSet.clear();
                            Map<Long, ?> partMap = multiblock.getPartMap(getListenerClass());
                            int listenerCount = info.getInt("listenerCount");
                            if (listenerCount <= 0) {
                                for (Map.Entry<Long, ?> entry : partMap.entrySet()) {
                                    listenerPosSet.add(entry.getKey());
                                    onAddListener((LISTENER) entry.getValue());
                                }
                            } else {
                                for (int i = 0; i < listenerCount; ++i) {
                                    long listenerPosLong = info.getLong("listenerPos" + i);
                                    if (partMap.containsKey(listenerPosLong)) {
                                        listenerPosSet.add(listenerPosLong);
                                        onAddListener((LISTENER) partMap.get(listenerPosLong));
                                    }
                                }
                            }
                            onSetListeners();
                            for (long posLong : listenerPosSet) {
                                BlockHighlightTracker.sendPacket(player, BlockPos.of(posLong), 5000);
                            }
                            player.sendSystemMessage(Component.translatable(MODID + ".message.multitool.finish_manager_listener_set", displayName, listenerPosSet.size()));
                            nbt.remove("componentManagerInfo");
                            return true;
                        } else {
                            player.sendSystemMessage(Component.translatable(MODID + ".message.multitool.invalid_component_info", info.getString("displayName"), displayName));
                        }
                    }
                } else {
                    LongSet listenerPosSet = getListenerPosSet();
                    for (long posLong : listenerPosSet) {
                        BlockHighlightTracker.sendPacket(player, BlockPos.of(posLong), 5000);
                    }
                    player.sendSystemMessage(Component.translatable(MODID + ".message.multitool.manager_listener_info", getTileBlockDisplayName(), listenerPosSet.size()));
                    return true;
                }
            }
        }
        return ITileLogicMultiblockPart.super.onUseMultitool(multitool, player, level, facing, hitPos);
    }

    default void onAddListener(LISTENER listener) {
        listener.setManagerPos(getTilePos());
        listener.refreshManager();
    }

    default void onSetListeners() {
        markTileDirty();
        refreshMultiblock();
    }
}