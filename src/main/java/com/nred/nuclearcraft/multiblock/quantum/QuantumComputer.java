package com.nred.nuclearcraft.multiblock.quantum;

import com.nred.nuclearcraft.block_entity.quantum.QuantumComputerControllerEntity;
import com.nred.nuclearcraft.block_entity.quantum.QuantumComputerQubitEntity;
import com.nred.nuclearcraft.config.NCConfig;
import com.nred.nuclearcraft.multiblock.Multiblock;
import com.nred.nuclearcraft.util.IOHelper;
import com.nred.nuclearcraft.util.NCUtil;
import it.unimi.dsi.fastutil.ints.Int2IntMap;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;
import it.zerono.mods.zerocore.lib.multiblock.IMultiblockController;
import it.zerono.mods.zerocore.lib.multiblock.IMultiblockPart;
import it.zerono.mods.zerocore.lib.multiblock.validation.IMultiblockValidator;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.loading.FMLEnvironment;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import static com.nred.nuclearcraft.NuclearcraftNeohaul.MODID;

public class QuantumComputer extends Multiblock<QuantumComputer> {
    protected QuantumComputerControllerEntity controller;

    public QuantumState state;

    public final Queue<QuantumOperationWrapper> queue = new ConcurrentLinkedQueue<>();

    public int codeStart = -1, codeType = -1;
    protected StringBuilder codeBuilder;

    public QuantumComputer(Level level) {
        super(level);
        state = new QuantumState(0);
    }

    @Override
    public int getMinimumInteriorLength() {
        return 0;
    }

    @Override
    public int getMaximumInteriorLength() {
        return 0;
    }

    @Override
    protected void onPartAdded(IMultiblockPart<QuantumComputer> newPart) {
        super.onPartAdded(newPart);
        refreshState();
        markQubitsDirty();
    }

    @Override
    protected void onPartRemoved(IMultiblockPart<QuantumComputer> oldPart) {
        super.onPartRemoved(oldPart);
        refreshState();
        markQubitsDirty();
    }

    @Override
    protected void onMachineAssembled() {
        onQuantumComputerFormed();
    }

    @Override
    protected void onMachineRestored() {
        onQuantumComputerFormed();
    }

    protected void onQuantumComputerFormed() {
        if (!getWorld().isClientSide()) {
            IntSet set = new IntOpenHashSet();
            for (QuantumComputerQubitEntity qubit : getQubits()) {
                if (set.contains(qubit.id)) {
                    qubit.id = -1;
                } else if (qubit.id >= 0) {
                    set.add(qubit.id);
                }
            }

            int i = 0;
            for (QuantumComputerQubitEntity qubit : getQubits()) {
                while (set.contains(i)) {
                    ++i;
                }
                if (qubit.id < 0) {
                    qubit.id = i++;
                }
            }
        }
    }

    @Override
    protected void onMachinePaused() {
    }

    @Override
    protected void onMachineDisassembled() {
    }

    @Override
    protected int getMinimumNumberOfPartsForAssembledMachine() {
        return 1;
    }

    @Override
    protected int getMaximumXSize() {
        return Integer.MAX_VALUE;
    }

    @Override
    protected int getMaximumZSize() {
        return Integer.MAX_VALUE;
    }

    @Override
    protected int getMaximumYSize() {
        return Integer.MAX_VALUE;
    }

    @Override
    protected boolean isMachineWhole(IMultiblockValidator validatorCallback) {
        if (!NCConfig.quantum_dedicated_server && FMLEnvironment.dist == Dist.DEDICATED_SERVER) {
            setLastError(MODID + ".multiblock_validation.quantum_computer.server_disabled");
            return false;
        }

        if (getPartMap(QuantumComputerControllerEntity.class).isEmpty()) {
            setLastError(MODID + ".multiblock_validation.no_controller");
            return false;
        }
        if (getPartCount(QuantumComputerControllerEntity.class) > 1) {
            setLastError(MODID + ".multiblock_validation.too_many_controllers");
            return false;
        }

        int qubits = getQubitCount();
        if (qubits > NCConfig.quantum_max_qubits) {
            setLastError(MODID + ".multiblock_validation.quantum_computer.too_many_qubits", qubits, NCConfig.quantum_max_qubits);
            return false;
        }

        for (QuantumComputerControllerEntity contr : getParts(QuantumComputerControllerEntity.class)) {
            controller = contr;
            break;
        }

        return true;
    }

    @Override
    protected void onAssimilate(IMultiblockController<QuantumComputer> iMultiblockController) {
    }

    @Override
    protected void onAssimilated(IMultiblockController<QuantumComputer> iMultiblockController) {
    }

    @Override
    protected boolean updateServer() {
        boolean refresh = false;

        int qubits = getQubitCount();
        if (codeStart >= 0) {
            codeType = codeStart;
            codeStart = -1;
            codeBuilder = new StringBuilder();
        }

        QuantumOperationWrapper gate = queue.poll();
        if (gate != null) {
            if (codeType >= 0) {
                if (qubits <= NCConfig.quantum_max_qubits) {
                    List<String> code = gate.getCode(codeType);
                    if (!code.isEmpty()) {
                        codeBuilder.append(IOHelper.NEW_LINE);
                    }
                    for (String line : code) {
                        codeBuilder.append(line);
                        codeBuilder.append(IOHelper.NEW_LINE);
                    }
                }
            } else if (qubits <= NCConfig.quantum_max_qubits) {
                gate.run();
                refresh = gate.shouldRefresh();
            }
        }

        return refresh;
    }

    @Override
    protected void updateClient() {
    }

    @Override
    protected boolean isBlockGoodForInterior(Level level, int x, int y, int z, IMultiblockValidator iMultiblockValidator) {
        return true;
    }

    @Override
    public void syncDataFrom(CompoundTag data, HolderLookup.Provider registries, SyncReason syncReason) {
        if (data.contains("size")) {
            int size = data.getInt("size");
            if (size <= NCConfig.quantum_max_qubits) {
                state = new QuantumState(size);
                ByteBuffer.wrap(data.getByteArray("vector")).asDoubleBuffer().get(state.vector);
            }
        }
    }

    @Override
    public CompoundTag syncDataTo(CompoundTag data, HolderLookup.Provider registries, SyncReason syncReason) {
        if (getQubitCount() <= NCConfig.quantum_max_qubits) {
            data.putInt("size", state.size);

            ByteBuffer byteBuf = ByteBuffer.allocate(state.dim << 4);
            DoubleBuffer doubleBuf = byteBuf.asDoubleBuffer();
            doubleBuf.put(state.vector);
            data.putByteArray("vector", byteBuf.array());
        }
        return data;
    }

    // Qubit Logic

    public Collection<QuantumComputerQubitEntity> getQubits() {
        return getParts(QuantumComputerQubitEntity.class);
    }

    public int getQubitCount() {
        return getPartCount(QuantumComputerQubitEntity.class);
    }

    protected void setQubitsRedstone(int[] targets, boolean[] results) {
        Int2IntMap idMap = new Int2IntOpenHashMap();
        for (int i = 0, len = targets.length; i < len; ++i) {
            idMap.put(targets[i], i);
        }

        for (QuantumComputerQubitEntity qubit : getQubits()) {
            int id = qubit.id;
            if (idMap.containsKey(id)) {
                boolean result = results[idMap.get(id)];
                qubit.redstone = result;
                qubit.measureColor = result ? 1F : -1F;
                qubit.sendTileUpdatePacketToAll();
            }
        }
    }

    protected void markQubitsDirty() {
        for (QuantumComputerQubitEntity qubit : getQubits()) {
            qubit.setChanged();
            qubit.updateComparatorOutputLevel();
        }
    }

    // Gates

    public void refreshState() {
        int qubits = getQubitCount();
        if (state.size != qubits) {
            state = new QuantumState(qubits);
        }
    }

    public void measure(int[] targets) {
        refreshState();
        setQubitsRedstone(targets, state.measure(targets, true));
        markQubitsDirty();
    }

    public void reset() {
        state = new QuantumState(getQubitCount());
    }

    public void gate(QuantumGate gate) {
        refreshState();
        state.update(gate);
    }

    // Code Generation

    public void printCode(Player player) {
        if (codeType < 0) {
            return;
        }

        int cachedCodeType = codeType;
        codeType = -1;

        int qubits = getQubitCount();
        if (qubits > NCConfig.quantum_max_qubits) {
            player.sendSystemMessage(Component.translatable(MODID + ".multitool.quantum_computer.controller.code_exit_too_many_qubits"));
            return;
        }

        String codeString = codeBuilder.toString();
        String s = IOHelper.NEW_LINE, d = s + s, time = Long.toString(System.currentTimeMillis() / 100L);

        if (cachedCodeType == 0) {
            if (codeString.isEmpty()) {
                player.sendSystemMessage(Component.translatable(MODID + ".multitool.quantum_computer.controller.qasm_exit_empty"));
                return;
            }

            File out = new File("nc_quantum/qasm/" + qubits + "_qubit_" + time + ".qasm");

            codeString = "OPENQASM 2.0;" + s + "include \"qelib1.inc\";" + d + "qreg q[" + qubits + "];" + s + "creg c[" + qubits + "];" + d + codeString;

            try {
                FileUtils.writeStringToFile(out, codeString, Charset.defaultCharset());
                Component link = Component.literal(out.getName()).withStyle(style -> style.withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_FILE, out.getAbsolutePath())).withBold(true).withUnderlined(true));

                player.sendSystemMessage(Component.translatable(MODID + ".multitool.quantum_computer.controller.qasm_print", link));
            } catch (IOException e) {
                NCUtil.getLogger().error("", e);
                player.sendSystemMessage(Component.translatable(MODID + ".multitool.quantum_computer.controller.qasm_error", out.getAbsolutePath()));
            }
        } else if (cachedCodeType == 1) {
            if (codeString.isEmpty()) {
                player.sendSystemMessage(Component.translatable(MODID + ".multitool.quantum_computer.controller.qiskit_exit_empty"));
                return;
            }

            File out = new File("nc_quantum/qiskit/" + qubits + "_qubit_" + time + ".ipynb");

            codeString = "# Jupyter plot output mode" + s + "# %matplotlib inline" + d +

                    "# Imports" + s + "import qiskit" + s + "from qiskit import IBMQ, QuantumCircuit, visualization" + s + "from qiskit.providers import ibmq" + s + "from qiskit.tools import monitor" + d +

                    "# Number of qubits" + s + "qubits = " + qubits + d +

                    "# Load IBMQ account" + s + "provider = IBMQ.load_account()" + d +

                    "# Get backends" + s + "simulator = provider.get_backend('simulator_statevector')" + s + "device = provider.get_backend('ibmq_manila')" + s + "filtered = provider.backends(" + s + "    filters=lambda x:" + s + "    int(x.configuration().num_qubits) >= qubits" + s + "    and not x.configuration().simulator" + s + "    and x.status().operational" + s + ")" + s + "leastbusy = ibmq.least_busy(filtered) if len(filtered) > 0 else device" + d +

                    "# Choice of backend" + s + "qc_backend = " + (qubits > 5 ? "simulator" : "device") + d +

                    "# Construct circuit" + s + "qc = QuantumCircuit(qubits, qubits)" + d +

                    "# Generated code" + codeString + d +

                    "# Helper function" + s + "def run_job(circuit, backend, shots=4096, optimization_level=3):" + s + "    print(f'Using backend {backend}...')" + s + "    job = qiskit.execute(circuit, backend=backend, shots=shots, optimization_level=optimization_level)" + s + "    qiskit.tools.job_monitor(job)" + s + "    return job.result()" + s + d +

                    "# Run circuit" + s + "result = run_job(qc, qc_backend, 4096, 3)" + s + "counts = result.get_counts(qc)" + s + "hist = visualization.plot_histogram(counts)" + s + "print('\\nCounts: ', counts)" + d +

                    "# Save circuit diagram to file" + s + "qc.draw(output='mpl', filename='circuit.png')" + d +

                    "# Save plot to file" + s + "hist.savefig('counts.png')" + d +

                    "# Plot results in output - only works in Jupyter" + s + "# hist" + s;

            try {
                FileUtils.writeStringToFile(out, codeString, Charset.defaultCharset());
                Component link = Component.literal(out.getName()).withStyle(style -> style.withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_FILE, out.getAbsolutePath())).withBold(true).withUnderlined(true));
                player.sendSystemMessage(Component.translatable(MODID + ".multitool.quantum_computer.controller.qiskit_print", link));
            } catch (IOException e) {
                NCUtil.getLogger().error("", e);
                player.sendSystemMessage(Component.translatable(MODID + ".multitool.quantum_computer.controller.qiskit_error", out.getAbsolutePath()));
            }
        } else {
            player.sendSystemMessage(Component.translatable(MODID + ".multitool.quantum_computer.controller.code_exit_empty"));
            return;
        }

        codeString = null;
    }
}