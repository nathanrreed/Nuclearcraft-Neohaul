package com.nred.nuclearcraft.block_entity;

import com.nred.nuclearcraft.multiblock.ILogicMultiblock;
import com.nred.nuclearcraft.multiblock.Multiblock;
import com.nred.nuclearcraft.multiblock.MultiblockLogic;
import com.nred.nuclearcraft.util.NBTHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.Optional;

import static com.nred.nuclearcraft.NuclearcraftNeohaul.MODID;
import static com.nred.nuclearcraft.util.PosHelper.DEFAULT_NON;

public interface ITileManagerListener<MULTIBLOCK extends Multiblock<MULTIBLOCK> & ILogicMultiblock<MULTIBLOCK, LOGIC>, LOGIC extends MultiblockLogic<MULTIBLOCK, LOGIC>, MANAGER extends ITileManager<MULTIBLOCK, LOGIC, MANAGER, LISTENER>, LISTENER extends ITileManagerListener<MULTIBLOCK, LOGIC, MANAGER, LISTENER>> extends ITileLogicMultiblockPart<MULTIBLOCK, LOGIC> {

    BlockPos getManagerPos();

    void setManagerPos(BlockPos pos);

    MANAGER getManager();

    void setManager(MANAGER manager);

    default void clearManager() {
        setManagerPos(DEFAULT_NON);
        setManager(null);
    }

    @SuppressWarnings("unchecked")
    default void refreshManager() {
        Optional<MULTIBLOCK> multiblock = getMultiblockController();
        MANAGER manager = multiblock.map(value -> (MANAGER) value.getPartMap(getManagerClass()).get(getManagerPos().asLong())).orElse(null);
        setManager(manager);
        if (manager == null) {
            setManagerPos(DEFAULT_NON);
        }
    }

    boolean onManagerRefresh(MANAGER manager);

    void refreshMultiblock();

    String getManagerType();

    Class<?> getManagerClass();

    @Override
    default boolean onUseMultitool(ItemStack multitool, ServerPlayer player, Level level, Direction facing, BlockPos hitPos) {
        CompoundTag nbt = NBTHelper.getStackNBT(multitool, "ncMultitool");
        if (nbt != null) {
            if (player.isCrouching()) {
                if (nbt.contains("componentManagerInfo", 10)) {
                    CompoundTag info = nbt.getCompound("componentManagerInfo");
                    if (info.getString("managerType").equals(getManagerType())) {
                        int listenerCount = info.getInt("listenerCount");
                        info.putLong("listenerPos" + listenerCount, getTilePos().asLong());
                        info.putInt("listenerCount", listenerCount + 1);
                        player.sendSystemMessage(Component.translatable(MODID + ".message.multitool.append_manager_listener_set", getTileBlockDisplayName()));
                        return true;
                    }
                }
            }
        }
        return ITileLogicMultiblockPart.super.onUseMultitool(multitool, player, level, facing, hitPos);
    }
}