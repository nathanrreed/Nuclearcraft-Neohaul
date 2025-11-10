package com.nred.nuclearcraft.block_entity.quantum;

import com.nred.nuclearcraft.config.NCConfig;
import com.nred.nuclearcraft.multiblock.quantum.QuantumComputer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import static com.nred.nuclearcraft.NuclearcraftNeohaul.MODID;
import static com.nred.nuclearcraft.registration.BlockEntityRegistration.QUANTUM_ENTITY_TYPE;

public abstract class QuantumComputerCodeGeneratorEntity extends AbstractQuantumComputerEntity {
    protected final int codeType;

    protected QuantumComputerCodeGeneratorEntity(final BlockPos position, final BlockState blockState, int codeType, String name) {
        super(QUANTUM_ENTITY_TYPE.get("quantum_computer_code_generator_" + name).get(), position, blockState);
        this.codeType = codeType;
    }

    public static class Qasm extends QuantumComputerCodeGeneratorEntity {
        public Qasm(final BlockPos position, final BlockState blockState) {
            super(position, blockState, 0, "qasm");
        }

        @Override
        protected String getUnlocalizedCodeStartMessage() {
            return MODID + ".multitool.quantum_computer.controller.code_qasm_start";
        }
    }

    public static class Qiskit extends QuantumComputerCodeGeneratorEntity {
        public Qiskit(final BlockPos position, final BlockState blockState) {
            super(position, blockState, 1, "qiskit");
        }

        @Override
        protected String getUnlocalizedCodeStartMessage() {
            return MODID + ".multitool.quantum_computer.controller.code_qiskit_start";
        }
    }

    @Override
    public boolean onUseMultitool(ItemStack multitool, ServerPlayer player, Level level, Direction facing, BlockPos hitPos) {
        if (!player.isCrouching()) {
            QuantumComputer qc = getMultiblockController().orElse(null);
            if (qc != null && qc.isAssembled()) {
                if (qc.codeType >= 0) {
                    qc.printCode(player);
                } else if (qc.getQubitCount() <= NCConfig.quantum_max_qubits) {
                    qc.codeStart = codeType;
                    player.sendSystemMessage(Component.translatable(getUnlocalizedCodeStartMessage()));
                } else {
                    player.sendSystemMessage(Component.translatable(MODID + ".multitool.quantum_computer.controller.code_too_many_qubits"));
                }
                return true;
            }
        }
        return super.onUseMultitool(multitool, player, level, facing, hitPos);
    }

    protected abstract String getUnlocalizedCodeStartMessage();
}