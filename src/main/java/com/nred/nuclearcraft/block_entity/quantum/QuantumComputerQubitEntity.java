package com.nred.nuclearcraft.block_entity.quantum;

import com.nred.nuclearcraft.block_entity.ITickable;
import com.nred.nuclearcraft.block_entity.ITilePacket;
import com.nred.nuclearcraft.multiblock.quantum.QuantumOperationWrapper;
import com.nred.nuclearcraft.payload.multiblock.QuantumComputerQubitRenderPacket;
import com.nred.nuclearcraft.util.NBTHelper;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntCollection;
import it.unimi.dsi.fastutil.ints.IntRBTreeSet;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import static com.nred.nuclearcraft.NuclearcraftNeohaul.MODID;
import static com.nred.nuclearcraft.registration.BlockEntityRegistration.QUANTUM_ENTITY_TYPE;

public class QuantumComputerQubitEntity extends AbstractQuantumComputerEntity implements ITickable, ITilePacket<QuantumComputerQubitRenderPacket> {
    public int id = -1;
    public boolean redstone = false, pulsed = false;
    public float measureColor = 0F;

    public QuantumComputerQubitEntity(final BlockPos position, final BlockState blockState) {
        super(QUANTUM_ENTITY_TYPE.get("quantum_computer_qubit").get(), position, blockState);
    }

    @Override
    public int[] weakSidesToCheck(Level worldIn, BlockPos posIn) {
        return new int[]{2, 3, 4, 5};
    }

    @Override
    public void update() {
        if (!pulsed && getIsRedstonePowered()) {
            queueMeasurement();
            pulsed = true;
        } else if (pulsed && !getIsRedstonePowered()) {
            pulsed = false;
        }
    }

    public final void queueMeasurement() {
        if (isMachineAssembled()) {
            getMultiblockController().ifPresent(quantumComputer -> quantumComputer.queue.add(new QuantumOperationWrapper.Measurement(getMultiblockController().get(), new int[]{id})));
        }
    }

    @Override
    public boolean onUseMultitool(ItemStack multitool, ServerPlayer player, Level level, Direction facing, BlockPos hitPos) {
        CompoundTag nbt = NBTHelper.getStackNBT(multitool, "ncMultitool");
        if (nbt != null) {
            String mode = nbt.getString("qComputerQubitMode");
            boolean s, l;
            if ((s = mode.equals("set")) || (l = mode.equals("list"))) {
                IntCollection idColl = s ? new IntRBTreeSet() : new IntArrayList();
                if (s) {
                    NBTHelper.readIntCollection(nbt, idColl, "qubitIDSet");
                    NBTHelper.writeIntCollection(nbt, new IntArrayList(), "qubitIDList");
                } else {
                    NBTHelper.readIntCollection(nbt, idColl, "qubitIDList");
                    NBTHelper.writeIntCollection(nbt, new IntRBTreeSet(), "qubitIDSet");
                }

                if (!player.isCrouching()) {
                    boolean wasEmpty = idColl.isEmpty();
                    if (idColl.add(id)) {
                        if (wasEmpty) {
                            player.sendSystemMessage(Component.translatable(MODID + ".multitool.quantum_computer.start_qubit_" + mode, id));
                        } else {
                            player.sendSystemMessage(Component.translatable(MODID + ".multitool.quantum_computer.add_qubit", id));
                        }
                    }
                }

                if (s) {
                    NBTHelper.writeIntCollection(nbt, idColl, "qubitIDSet");
                } else {
                    NBTHelper.writeIntCollection(nbt, idColl, "qubitIDList");
                }
                return true;
            }
        }
        return super.onUseMultitool(multitool, player, level, facing, hitPos);
    }

    @Override
    public QuantumComputerQubitRenderPacket getTileUpdatePacket() {
        return new QuantumComputerQubitRenderPacket(worldPosition, measureColor);
    }

    @Override
    public void onTileUpdatePacket(QuantumComputerQubitRenderPacket message) {
        measureColor = message.measureColor;
    }

    @Override
    public CompoundTag writeAll(CompoundTag nbt, HolderLookup.Provider registries) {
        super.writeAll(nbt, registries);
        nbt.putInt("qubitID", id);
        nbt.putBoolean("qubitRedstone", redstone);
        nbt.putBoolean("qubitPulsed", pulsed);
        nbt.putFloat("qubitMeasureColor", measureColor);
        return nbt;
    }

    @Override
    public void readAll(CompoundTag nbt, HolderLookup.Provider registries) {
        super.readAll(nbt, registries);
        id = nbt.getInt("qubitID");
        redstone = nbt.getBoolean("qubitRedstone");
        pulsed = nbt.getBoolean("qubitPulsed");
        measureColor = nbt.getFloat("qubitMeasureColor");
    }
}