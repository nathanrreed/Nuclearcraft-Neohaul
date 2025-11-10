package com.nred.nuclearcraft.block_entity.quantum;

import com.nred.nuclearcraft.block_entity.ITickable;
import com.nred.nuclearcraft.multiblock.quantum.QuantumComputer;
import com.nred.nuclearcraft.multiblock.quantum.QuantumOperationWrapper;
import com.nred.nuclearcraft.render.BlockHighlightTracker;
import com.nred.nuclearcraft.util.NBTHelper;
import com.nred.nuclearcraft.util.NCMath;
import it.unimi.dsi.fastutil.ints.*;
import net.minecraft.ChatFormatting;
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
import static com.nred.nuclearcraft.multiblock.quantum.QuantumOperationWrapper.intsToString;
import static com.nred.nuclearcraft.registration.BlockEntityRegistration.QUANTUM_ENTITY_TYPE;

public abstract class QuantumComputerGateEntity extends AbstractQuantumComputerEntity implements ITickable {
    protected final String gateID;
    protected String toolMode = "";
    public boolean pulsed = false;

    public static abstract class Basic extends QuantumComputerGateEntity {
        protected final IntSet targets;

        public Basic(final BlockPos position, final BlockState blockState, String gateID) {
            super(position, blockState, gateID);
            targets = new IntRBTreeSet();
            toolMode = "getTarget";
        }

        @Override
        public void sendGateInfo(ServerPlayer player) {
            highlightQubits(player, targets);
            player.sendSystemMessage(Component.translatable(MODID + ".multitool.quantum_computer.single_gate_info", getTileBlockDisplayName(), intsToString(targets)));
        }

        @Override
        public boolean onUseMultitool(ItemStack multitool, ServerPlayer player, Level level, Direction facing, BlockPos hitPos) {
            CompoundTag nbt = NBTHelper.getStackNBT(multitool, "ncMultitool");
            if (nbt != null) {
                if (toolMode.equals("getTarget") && player.isCrouching()) {
                    targets.clear();
                    player.sendSystemMessage(Component.translatable(MODID + ".multitool.quantum_computer.start_target_set", getTileBlockDisplayName()).withStyle(ChatFormatting.ITALIC));
                    nbt.putString("qComputerQubitMode", "set");
                    nbt.putString("qComputerGateMode", "");
                    toolMode = "setTarget";

                    clearMultitoolGateInfo(nbt);
                    return true;
                } else if (toolMode.equals("setTarget") && !player.isCrouching()) {
                    NBTHelper.readIntCollection(nbt, targets, "qubitIDSet");
                    highlightQubits(player, targets);
                    player.sendSystemMessage(Component.translatable(MODID + ".multitool.quantum_computer.finish_target_set", getTileBlockDisplayName(), intsToString(targets)).withStyle(ChatFormatting.BLUE));
                    nbt.putString("qComputerQubitMode", "");
                    nbt.putString("qComputerGateMode", "");
                    toolMode = "getTarget";

                    clearMultitoolGateInfo(nbt);
                    return true;
                }
            }

            return super.onUseMultitool(multitool, player, level, facing, hitPos);
        }

        @Override
        public CompoundTag writeAll(CompoundTag nbt, HolderLookup.Provider registries) {
            super.writeAll(nbt, registries);
            NBTHelper.writeIntCollection(nbt, targets, "nQubits");
            return nbt;
        }

        @Override
        public void readAll(CompoundTag nbt, HolderLookup.Provider registries) {
            super.readAll(nbt, registries);
            NBTHelper.readIntCollection(nbt, targets, "nQubits");
        }
    }

    public static class X extends Basic {
        public X(final BlockPos position, final BlockState blockState) {
            super(position, blockState, "x");
        }

        @Override
        protected QuantumOperationWrapper newGate(QuantumComputer qc) {
            return new QuantumOperationWrapper.X(qc, targets.toIntArray());
        }
    }

    public static class Y extends Basic {
        public Y(final BlockPos position, final BlockState blockState) {
            super(position, blockState, "y");
        }

        @Override
        protected QuantumOperationWrapper newGate(QuantumComputer qc) {
            return new QuantumOperationWrapper.Y(qc, targets.toIntArray());
        }
    }

    public static class Z extends Basic {
        public Z(final BlockPos position, final BlockState blockState) {
            super(position, blockState, "z");
        }

        @Override
        protected QuantumOperationWrapper newGate(QuantumComputer qc) {
            return new QuantumOperationWrapper.Z(qc, targets.toIntArray());
        }
    }

    public static class H extends Basic {
        public H(final BlockPos position, final BlockState blockState) {
            super(position, blockState, "h");
        }

        @Override
        protected QuantumOperationWrapper newGate(QuantumComputer qc) {
            return new QuantumOperationWrapper.H(qc, targets.toIntArray());
        }
    }

    public static class S extends Basic {
        public S(final BlockPos position, final BlockState blockState) {
            super(position, blockState, "s");
        }

        @Override
        protected QuantumOperationWrapper newGate(QuantumComputer qc) {
            return new QuantumOperationWrapper.S(qc, targets.toIntArray());
        }
    }

    public static class Sdg extends Basic {
        public Sdg(final BlockPos position, final BlockState blockState) {
            super(position, blockState, "sdg");
        }

        @Override
        protected QuantumOperationWrapper newGate(QuantumComputer qc) {
            return new QuantumOperationWrapper.Sdg(qc, targets.toIntArray());
        }
    }

    public static class T extends Basic {
        public T(final BlockPos position, final BlockState blockState) {
            super(position, blockState, "t");
        }

        @Override
        protected QuantumOperationWrapper newGate(QuantumComputer qc) {
            return new QuantumOperationWrapper.T(qc, targets.toIntArray());
        }
    }

    public static class Tdg extends Basic {
        public Tdg(final BlockPos position, final BlockState blockState) {
            super(position, blockState, "tdg");
        }

        @Override
        protected QuantumOperationWrapper newGate(QuantumComputer qc) {
            return new QuantumOperationWrapper.Tdg(qc, targets.toIntArray());
        }
    }

    public static abstract class BasicAngle extends QuantumComputerGateEntity {
        protected double angle = 0;
        protected final IntSet targets;

        public BasicAngle(final BlockPos position, final BlockState blockState, String gateID) {
            super(position, blockState, gateID);
            targets = new IntRBTreeSet();
        }

        @Override
        public void sendGateInfo(ServerPlayer player) {
            highlightQubits(player, targets);
            player.sendSystemMessage(Component.translatable(MODID + ".multitool.quantum_computer.single_angle_gate_info", getTileBlockDisplayName(), intsToString(targets), NCMath.decimalPlaces(angle, 5)));
        }

        @Override
        public boolean onUseMultitool(ItemStack multitool, ServerPlayer player, Level level, Direction facing, BlockPos hitPos) {
            CompoundTag nbt = NBTHelper.getStackNBT(multitool, "ncMultitool");
            if (nbt != null) {
                if (toolMode.equals("getAngle") && player.isCrouching()) {
                    angle = 0D;
                    player.sendSystemMessage(Component.translatable(MODID + ".multitool.quantum_computer.start_angle", getTileBlockDisplayName()).withStyle(ChatFormatting.ITALIC));
                    nbt.putString("qComputerQubitMode", "");
                    nbt.putString("qComputerGateMode", "angle");
                    toolMode = "setAngle";

                    clearMultitoolGateInfo(nbt);
                    return true;
                } else if (toolMode.equals("setAngle") && !player.isCrouching()) {
                    angle = nbt.getDouble("qGateAngle");
                    player.sendSystemMessage(Component.translatable(MODID + ".multitool.quantum_computer.finish_angle", getTileBlockDisplayName(), NCMath.decimalPlaces(angle, 5)).withStyle(ChatFormatting.GREEN));
                    nbt.putString("qComputerQubitMode", "");
                    nbt.putString("qComputerGateMode", "");
                    toolMode = "getTarget";

                    clearMultitoolGateInfo(nbt);
                    return true;
                } else if (toolMode.equals("getTarget") && player.isCrouching()) {
                    targets.clear();
                    player.sendSystemMessage(Component.translatable(MODID + ".multitool.quantum_computer.start_target_set", getTileBlockDisplayName()).withStyle(ChatFormatting.ITALIC));
                    nbt.putString("qComputerQubitMode", "set");
                    nbt.putString("qComputerGateMode", "");
                    toolMode = "setTarget";

                    clearMultitoolGateInfo(nbt);
                    return true;
                } else if (toolMode.equals("setTarget") && !player.isCrouching()) {
                    NBTHelper.readIntCollection(nbt, targets, "qubitIDSet");
                    highlightQubits(player, targets);
                    player.sendSystemMessage(Component.translatable(MODID + ".multitool.quantum_computer.finish_target_set", getTileBlockDisplayName(), intsToString(targets)).withStyle(ChatFormatting.BLUE));
                    nbt.putString("qComputerQubitMode", "");
                    nbt.putString("qComputerGateMode", "");
                    toolMode = "getAngle";

                    clearMultitoolGateInfo(nbt);
                    return true;
                }
            }

            return super.onUseMultitool(multitool, player, level, facing, hitPos);
        }

        @Override
        public CompoundTag writeAll(CompoundTag nbt, HolderLookup.Provider registries) {
            super.writeAll(nbt, registries);
            nbt.putDouble("qGateAngle", angle);
            NBTHelper.writeIntCollection(nbt, targets, "nQubits");
            return nbt;
        }

        @Override
        public void readAll(CompoundTag nbt, HolderLookup.Provider registries) {
            super.readAll(nbt, registries);
            angle = nbt.getDouble("qGateAngle");
            NBTHelper.readIntCollection(nbt, targets, "nQubits");
        }
    }

    public static class P extends BasicAngle {
        public P(final BlockPos position, final BlockState blockState) {
            super(position, blockState, "p");
        }

        @Override
        protected QuantumOperationWrapper newGate(QuantumComputer qc) {
            return new QuantumOperationWrapper.P(qc, angle, targets.toIntArray());
        }
    }

    public static class RX extends BasicAngle {
        public RX(final BlockPos position, final BlockState blockState) {
            super(position, blockState, "rx");
        }

        @Override
        protected QuantumOperationWrapper newGate(QuantumComputer qc) {
            return new QuantumOperationWrapper.RX(qc, angle, targets.toIntArray());
        }
    }

    public static class RY extends BasicAngle {
        public RY(final BlockPos position, final BlockState blockState) {
            super(position, blockState, "ry");
        }

        @Override
        protected QuantumOperationWrapper newGate(QuantumComputer qc) {
            return new QuantumOperationWrapper.RY(qc, angle, targets.toIntArray());
        }
    }

    public static class RZ extends BasicAngle {
        public RZ(final BlockPos position, final BlockState blockState) {
            super(position, blockState, "rz");
        }

        @Override
        protected QuantumOperationWrapper newGate(QuantumComputer qc) {
            return new QuantumOperationWrapper.RZ(qc, angle, targets.toIntArray());
        }
    }

    public static abstract class Control extends QuantumComputerGateEntity {
        protected final IntSet controls, targets;

        public Control(final BlockPos position, final BlockState blockState, String gateID) {
            super(position, blockState, gateID);
            controls = new IntRBTreeSet();
            targets = new IntRBTreeSet();
            toolMode = "getControl";
        }

        @Override
        public void sendGateInfo(ServerPlayer player) {
            highlightQubits(player, controls);
            highlightQubits(player, targets);
            player.sendSystemMessage(Component.translatable(MODID + ".multitool.quantum_computer.control_gate_info", getTileBlockDisplayName(), intsToString(targets), intsToString(controls)));
        }

        @Override
        public boolean onUseMultitool(ItemStack multitool, ServerPlayer player, Level level, Direction facing, BlockPos hitPos) {
            CompoundTag nbt = NBTHelper.getStackNBT(multitool, "ncMultitool");
            if (nbt != null) {
                if (toolMode.equals("getControl") && player.isCrouching()) {
                    controls.clear();
                    player.sendSystemMessage(Component.translatable(MODID + ".multitool.quantum_computer.start_control_set", getTileBlockDisplayName()).withStyle(ChatFormatting.ITALIC));
                    nbt.putString("qComputerQubitMode", "set");
                    nbt.putString("qComputerGateMode", "");
                    toolMode = "setControl";

                    clearMultitoolGateInfo(nbt);
                    return true;
                } else if (toolMode.equals("setControl") && !player.isCrouching()) {
                    NBTHelper.readIntCollection(nbt, controls, "qubitIDSet");
                    highlightQubits(player, controls);
                    player.sendSystemMessage(Component.translatable(MODID + ".multitool.quantum_computer.finish_control_set", getTileBlockDisplayName(), intsToString(controls)).withStyle(ChatFormatting.RED));
                    nbt.putString("qComputerQubitMode", "");
                    nbt.putString("qComputerGateMode", "");
                    toolMode = "getTarget";

                    clearMultitoolGateInfo(nbt);
                    return true;
                } else if (toolMode.equals("getTarget") && player.isCrouching()) {
                    targets.clear();
                    player.sendSystemMessage(Component.translatable(MODID + ".multitool.quantum_computer.start_target_set", getTileBlockDisplayName()).withStyle(ChatFormatting.ITALIC));
                    nbt.putString("qComputerQubitMode", "set");
                    nbt.putString("qComputerGateMode", "");
                    toolMode = "setTarget";

                    clearMultitoolGateInfo(nbt);
                    return true;
                } else if (toolMode.equals("setTarget") && !player.isCrouching()) {
                    NBTHelper.readIntCollection(nbt, targets, "qubitIDSet");
                    highlightQubits(player, targets);
                    player.sendSystemMessage(Component.translatable(MODID + ".multitool.quantum_computer.finish_target_set", getTileBlockDisplayName(), intsToString(targets)).withStyle(ChatFormatting.BLUE));
                    nbt.putString("qComputerQubitMode", "");
                    nbt.putString("qComputerGateMode", "");
                    toolMode = "getControl";

                    clearMultitoolGateInfo(nbt);
                    return true;
                }
            }

            return super.onUseMultitool(multitool, player, level, facing, hitPos);
        }

        @Override
        public CompoundTag writeAll(CompoundTag nbt, HolderLookup.Provider registries) {
            super.writeAll(nbt, registries);
            NBTHelper.writeIntCollection(nbt, controls, "cQubits");
            NBTHelper.writeIntCollection(nbt, targets, "tQubits");
            return nbt;
        }

        @Override
        public void readAll(CompoundTag nbt, HolderLookup.Provider registries) {
            super.readAll(nbt, registries);
            NBTHelper.readIntCollection(nbt, controls, "cQubits");
            NBTHelper.readIntCollection(nbt, targets, "tQubits");
        }
    }

    public static class CX extends Control {
        public CX(final BlockPos position, final BlockState blockState) {
            super(position, blockState, "cx");
        }

        @Override
        protected QuantumOperationWrapper newGate(QuantumComputer qc) {
            return new QuantumOperationWrapper.CX(qc, controls.toIntArray(), targets.toIntArray());
        }
    }

    public static class CY extends Control {
        public CY(final BlockPos position, final BlockState blockState) {
            super(position, blockState, "cy");
        }

        @Override
        protected QuantumOperationWrapper newGate(QuantumComputer qc) {
            return new QuantumOperationWrapper.CY(qc, controls.toIntArray(), targets.toIntArray());
        }
    }

    public static class CZ extends Control {
        public CZ(final BlockPos position, final BlockState blockState) {
            super(position, blockState, "cz");
        }

        @Override
        protected QuantumOperationWrapper newGate(QuantumComputer qc) {
            return new QuantumOperationWrapper.CZ(qc, controls.toIntArray(), targets.toIntArray());
        }
    }

    public static class CH extends Control {
        public CH(final BlockPos position, final BlockState blockState) {
            super(position, blockState, "ch");
        }

        @Override
        protected QuantumOperationWrapper newGate(QuantumComputer qc) {
            return new QuantumOperationWrapper.CH(qc, controls.toIntArray(), targets.toIntArray());
        }
    }

    public static class CS extends Control {
        public CS(final BlockPos position, final BlockState blockState) {
            super(position, blockState, "cs");
        }

        @Override
        protected QuantumOperationWrapper newGate(QuantumComputer qc) {
            return new QuantumOperationWrapper.CS(qc, controls.toIntArray(), targets.toIntArray());
        }
    }

    public static class CSdg extends Control {
        public CSdg(final BlockPos position, final BlockState blockState) {
            super(position, blockState, "csdg");
        }

        @Override
        protected QuantumOperationWrapper newGate(QuantumComputer qc) {
            return new QuantumOperationWrapper.CSdg(qc, controls.toIntArray(), targets.toIntArray());
        }
    }

    public static class CT extends Control {
        public CT(final BlockPos position, final BlockState blockState) {
            super(position, blockState, "ct");
        }

        @Override
        protected QuantumOperationWrapper newGate(QuantumComputer qc) {
            return new QuantumOperationWrapper.CT(qc, controls.toIntArray(), targets.toIntArray());
        }
    }

    public static class CTdg extends Control {
        public CTdg(final BlockPos position, final BlockState blockState) {
            super(position, blockState, "ctdg");
        }

        @Override
        protected QuantumOperationWrapper newGate(QuantumComputer qc) {
            return new QuantumOperationWrapper.CTdg(qc, controls.toIntArray(), targets.toIntArray());
        }
    }

    public static abstract class ControlAngle extends QuantumComputerGateEntity {
        protected double angle = 0;
        protected final IntSet controls, targets;

        public ControlAngle(final BlockPos position, final BlockState blockState, String gateID) {
            super(position, blockState, gateID);
            controls = new IntRBTreeSet();
            targets = new IntRBTreeSet();
            toolMode = "getAngle";
        }

        @Override
        public void sendGateInfo(ServerPlayer player) {
            highlightQubits(player, controls);
            highlightQubits(player, targets);
            player.sendSystemMessage(Component.translatable(MODID + ".multitool.quantum_computer.control_angle_gate_info", getTileBlockDisplayName(), intsToString(targets), NCMath.decimalPlaces(angle, 5), intsToString(controls)));
        }

        @Override
        public boolean onUseMultitool(ItemStack multitool, ServerPlayer player, Level level, Direction facing, BlockPos hitPos) {
            CompoundTag nbt = NBTHelper.getStackNBT(multitool, "ncMultitool");
            if (nbt != null) {
                if (toolMode.equals("getAngle") && player.isCrouching()) {
                    angle = 0D;
                    player.sendSystemMessage(Component.translatable(MODID + ".multitool.quantum_computer.start_angle", getTileBlockDisplayName()).withStyle(ChatFormatting.ITALIC));
                    nbt.putString("qComputerQubitMode", "");
                    nbt.putString("qComputerGateMode", "angle");
                    toolMode = "setAngle";

                    clearMultitoolGateInfo(nbt);
                    return true;
                } else if (toolMode.equals("setAngle") && !player.isCrouching()) {
                    angle = nbt.getDouble("qGateAngle");
                    player.sendSystemMessage(Component.translatable(MODID + ".multitool.quantum_computer.finish_angle", getTileBlockDisplayName(), NCMath.decimalPlaces(angle, 5)).withStyle(ChatFormatting.GREEN));
                    nbt.putString("qComputerQubitMode", "");
                    nbt.putString("qComputerGateMode", "");
                    toolMode = "getControl";

                    clearMultitoolGateInfo(nbt);
                    return true;
                } else if (toolMode.equals("getControl") && player.isCrouching()) {
                    controls.clear();
                    player.sendSystemMessage(Component.translatable(MODID + ".multitool.quantum_computer.start_control_set", getTileBlockDisplayName()).withStyle(ChatFormatting.ITALIC));
                    nbt.putString("qComputerQubitMode", "set");
                    nbt.putString("qComputerGateMode", "");
                    toolMode = "setControl";

                    clearMultitoolGateInfo(nbt);
                    return true;
                } else if (toolMode.equals("setControl") && !player.isCrouching()) {
                    NBTHelper.readIntCollection(nbt, controls, "qubitIDSet");
                    highlightQubits(player, controls);
                    player.sendSystemMessage(Component.translatable(MODID + ".multitool.quantum_computer.finish_control_set", getTileBlockDisplayName(), intsToString(controls)).withStyle(ChatFormatting.RED));
                    nbt.putString("qComputerQubitMode", "");
                    nbt.putString("qComputerGateMode", "");
                    toolMode = "getTarget";

                    clearMultitoolGateInfo(nbt);
                    return true;
                } else if (toolMode.equals("getTarget") && player.isCrouching()) {
                    targets.clear();
                    player.sendSystemMessage(Component.translatable(MODID + ".multitool.quantum_computer.start_target_set", getTileBlockDisplayName()).withStyle(ChatFormatting.ITALIC));
                    nbt.putString("qComputerQubitMode", "set");
                    nbt.putString("qComputerGateMode", "");
                    toolMode = "setTarget";

                    clearMultitoolGateInfo(nbt);
                    return true;
                } else if (toolMode.equals("setTarget") && !player.isCrouching()) {
                    NBTHelper.readIntCollection(nbt, targets, "qubitIDSet");
                    highlightQubits(player, targets);
                    player.sendSystemMessage(Component.translatable(MODID + ".multitool.quantum_computer.finish_target_set", getTileBlockDisplayName(), intsToString(targets)).withStyle(ChatFormatting.BLUE));
                    nbt.putString("qComputerQubitMode", "");
                    nbt.putString("qComputerGateMode", "");
                    toolMode = "getAngle";

                    clearMultitoolGateInfo(nbt);
                    return true;
                }
            }
            return super.onUseMultitool(multitool, player, level, facing, hitPos);
        }

        @Override
        public CompoundTag writeAll(CompoundTag nbt, HolderLookup.Provider registries) {
            super.writeAll(nbt, registries);
            nbt.putDouble("qGateAngle", angle);
            NBTHelper.writeIntCollection(nbt, controls, "cQubits");
            NBTHelper.writeIntCollection(nbt, targets, "tQubits");
            return nbt;
        }

        @Override
        public void readAll(CompoundTag nbt, HolderLookup.Provider registries) {
            super.readAll(nbt, registries);
            angle = nbt.getDouble("qGateAngle");
            NBTHelper.readIntCollection(nbt, controls, "cQubits");
            NBTHelper.readIntCollection(nbt, targets, "tQubits");
        }
    }

    public static class CP extends ControlAngle {
        public CP(final BlockPos position, final BlockState blockState) {
            super(position, blockState, "cp");
        }

        @Override
        protected QuantumOperationWrapper newGate(QuantumComputer qc) {
            return new QuantumOperationWrapper.CP(qc, angle, controls.toIntArray(), targets.toIntArray());
        }
    }

    public static class CRX extends ControlAngle {
        public CRX(final BlockPos position, final BlockState blockState) {
            super(position, blockState, "crx");
        }

        @Override
        protected QuantumOperationWrapper newGate(QuantumComputer qc) {
            return new QuantumOperationWrapper.CRX(qc, angle, controls.toIntArray(), targets.toIntArray());
        }
    }

    public static class CRY extends ControlAngle {
        public CRY(final BlockPos position, final BlockState blockState) {
            super(position, blockState, "cry");
        }

        @Override
        protected QuantumOperationWrapper newGate(QuantumComputer qc) {
            return new QuantumOperationWrapper.CRY(qc, angle, controls.toIntArray(), targets.toIntArray());
        }
    }

    public static class CRZ extends ControlAngle {
        public CRZ(final BlockPos position, final BlockState blockState) {
            super(position, blockState, "crz");
        }

        @Override
        protected QuantumOperationWrapper newGate(QuantumComputer qc) {
            return new QuantumOperationWrapper.CRZ(qc, angle, controls.toIntArray(), targets.toIntArray());
        }
    }

    public static class Swap extends QuantumComputerGateEntity {
        protected final IntList from, to;

        public Swap(final BlockPos position, final BlockState blockState) {
            super(position, blockState, "swap");
            from = new IntArrayList();
            to = new IntArrayList();
            toolMode = "getFirst";
        }

        @Override
        protected QuantumOperationWrapper newGate(QuantumComputer qc) {
            return new QuantumOperationWrapper.Swap(qc, from.toIntArray(), to.toIntArray());
        }

        @Override
        public void sendGateInfo(ServerPlayer player) {
            highlightQubits(player, from);
            highlightQubits(player, to);
            player.sendSystemMessage(Component.translatable(MODID + ".multitool.quantum_computer.swap_gate_info", getTileBlockDisplayName(), intsToString(from), intsToString(to)));
        }

        @Override
        public boolean onUseMultitool(ItemStack multitool, ServerPlayer player, Level level, Direction facing, BlockPos hitPos) {
            CompoundTag nbt = NBTHelper.getStackNBT(multitool, "ncMultitool");
            if (nbt != null) {
                if (toolMode.equals("getFirst") && player.isCrouching()) {
                    from.clear();
                    player.sendSystemMessage(Component.translatable(MODID + ".multitool.quantum_computer.start_first_swap_list", getTileBlockDisplayName()).withStyle(ChatFormatting.ITALIC));
                    nbt.putString("qComputerQubitMode", "list");
                    nbt.putString("qComputerGateMode", "");
                    toolMode = "setFirst";

                    clearMultitoolGateInfo(nbt);
                    return true;
                } else if (toolMode.equals("setFirst") && !player.isCrouching()) {
                    NBTHelper.readIntCollection(nbt, from, "qubitIDList");
                    highlightQubits(player, from);
                    player.sendSystemMessage(Component.translatable(MODID + ".multitool.quantum_computer.finish_first_swap_list", getTileBlockDisplayName(), intsToString(from)).withStyle(ChatFormatting.GOLD));
                    nbt.putString("qComputerQubitMode", "");
                    nbt.putString("qComputerGateMode", "");
                    toolMode = "getSecond";

                    clearMultitoolGateInfo(nbt);
                    return true;
                } else if (toolMode.equals("getSecond") && player.isCrouching()) {
                    to.clear();
                    player.sendSystemMessage(Component.translatable(MODID + ".multitool.quantum_computer.start_second_swap_list", getTileBlockDisplayName()).withStyle(ChatFormatting.ITALIC));
                    nbt.putString("qComputerQubitMode", "list");
                    nbt.putString("qComputerGateMode", "");
                    toolMode = "setSecond";

                    clearMultitoolGateInfo(nbt);
                    return true;
                } else if (toolMode.equals("setSecond") && !player.isCrouching()) {
                    NBTHelper.readIntCollection(nbt, to, "qubitIDList");
                    highlightQubits(player, to);
                    player.sendSystemMessage(Component.translatable(MODID + ".multitool.quantum_computer.finish_second_swap_list", getTileBlockDisplayName(), intsToString(to)).withStyle(ChatFormatting.LIGHT_PURPLE));
                    nbt.putString("qComputerQubitMode", "");
                    nbt.putString("qComputerGateMode", "");
                    toolMode = "getFirst";

                    clearMultitoolGateInfo(nbt);
                    return true;
                }
            }

            return super.onUseMultitool(multitool, player, level, facing, hitPos);
        }

        @Override
        public CompoundTag writeAll(CompoundTag nbt, HolderLookup.Provider registries) {
            super.writeAll(nbt, registries);
            NBTHelper.writeIntCollection(nbt, from, "iQubits");
            NBTHelper.writeIntCollection(nbt, to, "jQubits");
            return nbt;
        }

        @Override
        public void readAll(CompoundTag nbt, HolderLookup.Provider registries) {
            super.readAll(nbt, registries);
            NBTHelper.readIntCollection(nbt, from, "iQubits");
            NBTHelper.readIntCollection(nbt, to, "jQubits");
        }
    }

    public static class ControlSwap extends QuantumComputerGateEntity {
        protected final IntSet controls;
        protected final IntList from, to;

        public ControlSwap(final BlockPos position, final BlockState blockState) {
            super(position, blockState, "cswap");
            controls = new IntRBTreeSet();
            from = new IntArrayList();
            to = new IntArrayList();
            toolMode = "getControl";
        }

        @Override
        protected QuantumOperationWrapper newGate(QuantumComputer qc) {
            return new QuantumOperationWrapper.ControlSwap(qc, controls.toIntArray(), from.toIntArray(), to.toIntArray());
        }

        @Override
        public void sendGateInfo(ServerPlayer player) {
            highlightQubits(player, controls);
            highlightQubits(player, from);
            highlightQubits(player, to);
            player.sendSystemMessage(Component.translatable(MODID + ".multitool.quantum_computer.control_swap_gate_info", getTileBlockDisplayName(), intsToString(from), intsToString(to), intsToString(controls)));
        }

        @Override
        public boolean onUseMultitool(ItemStack multitool, ServerPlayer player, Level level, Direction facing, BlockPos hitPos) {
            CompoundTag nbt = NBTHelper.getStackNBT(multitool, "ncMultitool");
            if (nbt != null) {
                if (toolMode.equals("getControl") && player.isCrouching()) {
                    controls.clear();
                    player.sendSystemMessage(Component.translatable(MODID + ".multitool.quantum_computer.start_control_set", getTileBlockDisplayName()).withStyle(ChatFormatting.ITALIC));
                    nbt.putString("qComputerQubitMode", "set");
                    nbt.putString("qComputerGateMode", "");
                    toolMode = "setControl";

                    clearMultitoolGateInfo(nbt);
                    return true;
                } else if (toolMode.equals("setControl") && !player.isCrouching()) {
                    NBTHelper.readIntCollection(nbt, controls, "qubitIDSet");
                    highlightQubits(player, controls);
                    player.sendSystemMessage(Component.translatable(MODID + ".multitool.quantum_computer.finish_control_set", getTileBlockDisplayName(), intsToString(controls)).withStyle(ChatFormatting.RED));
                    nbt.putString("qComputerQubitMode", "");
                    nbt.putString("qComputerGateMode", "");
                    toolMode = "getFirst";

                    clearMultitoolGateInfo(nbt);
                    return true;
                } else if (toolMode.equals("getFirst") && player.isCrouching()) {
                    from.clear();
                    player.sendSystemMessage(Component.translatable(MODID + ".multitool.quantum_computer.start_first_swap_list", getTileBlockDisplayName()).withStyle(ChatFormatting.ITALIC));
                    nbt.putString("qComputerQubitMode", "list");
                    nbt.putString("qComputerGateMode", "");
                    toolMode = "setFirst";

                    clearMultitoolGateInfo(nbt);
                    return true;
                } else if (toolMode.equals("setFirst") && !player.isCrouching()) {
                    NBTHelper.readIntCollection(nbt, from, "qubitIDList");
                    highlightQubits(player, from);
                    player.sendSystemMessage(Component.translatable(MODID + ".multitool.quantum_computer.finish_first_swap_list", getTileBlockDisplayName(), intsToString(from)).withStyle(ChatFormatting.GOLD));
                    nbt.putString("qComputerQubitMode", "");
                    nbt.putString("qComputerGateMode", "");
                    toolMode = "getSecond";

                    clearMultitoolGateInfo(nbt);
                    return true;
                } else if (toolMode.equals("getSecond") && player.isCrouching()) {
                    to.clear();
                    player.sendSystemMessage(Component.translatable(MODID + ".multitool.quantum_computer.start_second_swap_list", getTileBlockDisplayName()).withStyle(ChatFormatting.ITALIC));
                    nbt.putString("qComputerQubitMode", "list");
                    nbt.putString("qComputerGateMode", "");
                    toolMode = "setSecond";

                    clearMultitoolGateInfo(nbt);
                    return true;
                } else if (toolMode.equals("setSecond") && !player.isCrouching()) {
                    NBTHelper.readIntCollection(nbt, to, "qubitIDList");
                    highlightQubits(player, to);
                    player.sendSystemMessage(Component.translatable(MODID + ".multitool.quantum_computer.finish_second_swap_list", getTileBlockDisplayName(), intsToString(to)).withStyle(ChatFormatting.LIGHT_PURPLE));
                    nbt.putString("qComputerQubitMode", "");
                    nbt.putString("qComputerGateMode", "");
                    toolMode = "getControl";

                    clearMultitoolGateInfo(nbt);
                    return true;
                }
            }

            return super.onUseMultitool(multitool, player, level, facing, hitPos);
        }

        @Override
        public CompoundTag writeAll(CompoundTag nbt, HolderLookup.Provider registries) {
            super.writeAll(nbt, registries);
            NBTHelper.writeIntCollection(nbt, controls, "cQubits");
            NBTHelper.writeIntCollection(nbt, from, "iQubits");
            NBTHelper.writeIntCollection(nbt, to, "jQubits");
            return nbt;
        }

        @Override
        public void readAll(CompoundTag nbt, HolderLookup.Provider registries) {
            super.readAll(nbt, registries);
            NBTHelper.readIntCollection(nbt, controls, "cQubits");
            NBTHelper.readIntCollection(nbt, from, "iQubits");
            NBTHelper.readIntCollection(nbt, to, "jQubits");
        }
    }

    public QuantumComputerGateEntity(final BlockPos position, final BlockState blockState, String gateID) {
        super(QUANTUM_ENTITY_TYPE.get(gateID).get(), position, blockState);
        this.gateID = gateID;
    }

    @Override
    public int[] weakSidesToCheck(Level worldIn, BlockPos posIn) {
        return new int[]{2, 3, 4, 5};
    }

    @Override
    public void update() {
        if (!pulsed && getIsRedstonePowered()) {
            if (isMachineAssembled()) {
                getMultiblockController().ifPresent(quantumComputer -> quantumComputer.queue.add(newGate(getMultiblockController().get())));
            }
            pulsed = true;
        } else if (pulsed && !getIsRedstonePowered()) {
            pulsed = false;
        }
    }

    protected abstract QuantumOperationWrapper newGate(QuantumComputer qc);

    public abstract void sendGateInfo(ServerPlayer player);

    protected void highlightQubits(ServerPlayer player, IntCollection n) {
        QuantumComputer qc = getMultiblockController().orElse(null);
        if (qc != null) {
            for (QuantumComputerQubitEntity qubit : qc.getQubits()) {
                if (n.contains(qubit.id)) {
                    BlockHighlightTracker.sendPacket(player, qubit.getBlockPos(), 5000);
                }
            }
        }
    }

    public static void clearMultitoolGateInfo(CompoundTag nbt) {
        nbt.putDouble("qGateAngle", 0D);
        NBTHelper.writeIntCollection(nbt, new IntRBTreeSet(), "qubitIDSet");
        NBTHelper.writeIntCollection(nbt, new IntArrayList(), "qubitIDList");
    }

    @Override
    public CompoundTag writeAll(CompoundTag nbt, HolderLookup.Provider registries) {
        super.writeAll(nbt, registries);
        nbt.putBoolean("pulsed", pulsed);
        nbt.putString("toolMode", toolMode);
        return nbt;
    }

    @Override
    public void readAll(CompoundTag nbt, HolderLookup.Provider registries) {
        super.readAll(nbt, registries);
        pulsed = nbt.getBoolean("pulsed");
        toolMode = nbt.getString("toolMode");
    }
}