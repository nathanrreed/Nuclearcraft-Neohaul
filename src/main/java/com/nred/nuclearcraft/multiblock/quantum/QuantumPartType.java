package com.nred.nuclearcraft.multiblock.quantum;

import com.nred.nuclearcraft.block.GenericTooltipDeviceBlock;
import com.nred.nuclearcraft.block.quantum.QuantumComputerControllerBlock;
import com.nred.nuclearcraft.block.quantum.QuantumComputerGateBlock;
import com.nred.nuclearcraft.block.quantum.QuantumComputerQubitBlock;
import it.zerono.mods.zerocore.lib.block.multiblock.MultiblockPartBlock;
import it.zerono.mods.zerocore.lib.block.multiblock.MultiblockPartTypeProperties;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;
import java.util.function.Supplier;

import static com.nred.nuclearcraft.registration.BlockEntityRegistration.QUANTUM_ENTITY_TYPE;

public enum QuantumPartType implements IQuantumComputerPartType {
    // Basic
    X(() -> QUANTUM_ENTITY_TYPE.get("x")::get, QuantumComputerGateBlock::new),
    Y(() -> QUANTUM_ENTITY_TYPE.get("y")::get, QuantumComputerGateBlock::new),
    Z(() -> QUANTUM_ENTITY_TYPE.get("z")::get, QuantumComputerGateBlock::new),
    H(() -> QUANTUM_ENTITY_TYPE.get("h")::get, QuantumComputerGateBlock::new),
    S(() -> QUANTUM_ENTITY_TYPE.get("s")::get, QuantumComputerGateBlock::new),
    SDG(() -> QUANTUM_ENTITY_TYPE.get("sdg")::get, QuantumComputerGateBlock::new),
    T(() -> QUANTUM_ENTITY_TYPE.get("t")::get, QuantumComputerGateBlock::new),
    TDG(() -> QUANTUM_ENTITY_TYPE.get("tdg")::get, QuantumComputerGateBlock::new),
    P(() -> QUANTUM_ENTITY_TYPE.get("p")::get, QuantumComputerGateBlock::new),
    RX(() -> QUANTUM_ENTITY_TYPE.get("rx")::get, QuantumComputerGateBlock::new),
    RY(() -> QUANTUM_ENTITY_TYPE.get("ry")::get, QuantumComputerGateBlock::new),
    RZ(() -> QUANTUM_ENTITY_TYPE.get("rz")::get, QuantumComputerGateBlock::new),

    // Control
    CX(() -> QUANTUM_ENTITY_TYPE.get("cx")::get, QuantumComputerGateBlock::new),
    CY(() -> QUANTUM_ENTITY_TYPE.get("cy")::get, QuantumComputerGateBlock::new),
    CZ(() -> QUANTUM_ENTITY_TYPE.get("cz")::get, QuantumComputerGateBlock::new),
    CH(() -> QUANTUM_ENTITY_TYPE.get("ch")::get, QuantumComputerGateBlock::new),
    CS(() -> QUANTUM_ENTITY_TYPE.get("cs")::get, QuantumComputerGateBlock::new),
    CSDG(() -> QUANTUM_ENTITY_TYPE.get("csdg")::get, QuantumComputerGateBlock::new),
    CT(() -> QUANTUM_ENTITY_TYPE.get("ct")::get, QuantumComputerGateBlock::new),
    CTDG(() -> QUANTUM_ENTITY_TYPE.get("ctdg")::get, QuantumComputerGateBlock::new),
    CP(() -> QUANTUM_ENTITY_TYPE.get("cp")::get, QuantumComputerGateBlock::new),
    CRX(() -> QUANTUM_ENTITY_TYPE.get("crx")::get, QuantumComputerGateBlock::new),
    CRY(() -> QUANTUM_ENTITY_TYPE.get("cry")::get, QuantumComputerGateBlock::new),
    CRZ(() -> QUANTUM_ENTITY_TYPE.get("crz")::get, QuantumComputerGateBlock::new),

    // Swap
    SWAP(() -> QUANTUM_ENTITY_TYPE.get("swap")::get, QuantumComputerGateBlock::new),
    CSWAP(() -> QUANTUM_ENTITY_TYPE.get("cswap")::get, QuantumComputerGateBlock::new),

    Quantum_Computer_Controller(() -> QUANTUM_ENTITY_TYPE.get("quantum_computer_controller")::get, QuantumComputerControllerBlock::new),
    Quantum_Computer_Qubit(() -> QUANTUM_ENTITY_TYPE.get("quantum_computer_qubit")::get, QuantumComputerQubitBlock::new),
    Quantum_Computer_Connector(() -> QUANTUM_ENTITY_TYPE.get("quantum_computer_connector")::get, GenericTooltipDeviceBlock::new),
    Quantum_Computer_Port(() -> QUANTUM_ENTITY_TYPE.get("quantum_computer_port")::get, GenericTooltipDeviceBlock::new),
    Quantum_Computer_Code_Generator_QASM(() -> QUANTUM_ENTITY_TYPE.get("quantum_computer_code_generator_qasm")::get, GenericTooltipDeviceBlock::new),
    Quantum_Computer_Code_Generator_QISKIT(() -> QUANTUM_ENTITY_TYPE.get("quantum_computer_code_generator_qiskit")::get, GenericTooltipDeviceBlock::new),
    ;

    private final MultiblockPartTypeProperties<QuantumComputer, IQuantumComputerPartType> _properties;

    QuantumPartType(final Supplier<@NotNull Supplier<@NotNull BlockEntityType<?>>> tileTypeSupplier,
                    final Function<MultiblockPartBlock.@NotNull MultiblockPartProperties<IQuantumComputerPartType>, @NotNull MultiblockPartBlock<QuantumComputer, IQuantumComputerPartType>> blockFactory,
                    final Function<Block.@NotNull Properties, Block.@NotNull Properties> blockPropertiesFixer) {
        this._properties = new MultiblockPartTypeProperties<>(tileTypeSupplier, blockFactory, "", blockPropertiesFixer, ppf -> ppf);
    }

    QuantumPartType(final Supplier<@NotNull Supplier<@NotNull BlockEntityType<?>>> tileTypeSupplier,
                    final Function<MultiblockPartBlock.@NotNull MultiblockPartProperties<IQuantumComputerPartType>, @NotNull MultiblockPartBlock<QuantumComputer, IQuantumComputerPartType>> blockFactory) {
        this._properties = new MultiblockPartTypeProperties<>(tileTypeSupplier, blockFactory, "", bp -> bp, ppf -> ppf);
    }

    @Override
    public MultiblockPartTypeProperties<QuantumComputer, IQuantumComputerPartType> getPartTypeProperties() {
        return this._properties;
    }

    @Override
    public String getSerializedName() {
        return this.name();
    }
}
