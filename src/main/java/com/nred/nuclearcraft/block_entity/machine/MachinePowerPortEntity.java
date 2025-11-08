package com.nred.nuclearcraft.block_entity.machine;

import com.nred.nuclearcraft.block_entity.ITickable;
import com.nred.nuclearcraft.block_entity.energy.ITileEnergy;
import com.nred.nuclearcraft.block_entity.internal.energy.EnergyConnection;
import com.nred.nuclearcraft.block_entity.internal.energy.EnergyStorage;
import com.nred.nuclearcraft.block_entity.internal.energy.EnergyTileWrapper;
import com.nred.nuclearcraft.multiblock.machine.Machine;
import com.nred.nuclearcraft.multiblock.machine.MachineLogic;
import it.zerono.mods.zerocore.lib.multiblock.cuboid.PartPosition;
import it.zerono.mods.zerocore.lib.multiblock.validation.IMultiblockValidator;
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

import javax.annotation.Nonnull;

import java.util.Optional;

import static com.nred.nuclearcraft.NuclearcraftNeohaul.MODID;
import static com.nred.nuclearcraft.registration.BlockEntityRegistration.MACHINE_ENTITY_TYPE;
import static com.nred.nuclearcraft.registration.BlockRegistration.FACING_ALL;


public class MachinePowerPortEntity extends AbstractMachineEntity implements ITickable, ITileEnergy {
    protected final EnergyStorage backupStorage = new EnergyStorage(0L);

    protected final EnergyConnection[] energyConnections = ITileEnergy.energyConnectionAll(EnergyConnection.IN);

    protected final EnergyTileWrapper[] energySides = ITileEnergy.getDefaultEnergySides(this);

    public MachinePowerPortEntity(final BlockPos position, final BlockState blockState) {
        super(MACHINE_ENTITY_TYPE.get("power_port").get(), position, blockState);
    }

    @Override
    public boolean isGoodForPosition(PartPosition position, IMultiblockValidator validatorCallback) {
        return position.isFace();
    }

    @Override
    public void onPreMachineAssembled(Machine controller) {
        super.onPreMachineAssembled(controller);
        if (!level.isClientSide()) {
            Optional<Direction> facing = getPartPosition().getDirection();
            facing.ifPresent(direction -> level.setBlock(worldPosition, level.getBlockState(worldPosition).setValue(FACING_ALL, direction), 2));
        }
    }

    @Override
    public void update() {
        if (!level.isClientSide() && getEnergyStored() > 0) {
            Optional<Direction> facing = getPartPosition().getDirection();
            if (facing.isPresent() && getEnergyConnection(facing.get()).canExtract()) {
                pushEnergyToSide(facing.get());
            }
        }
    }

    @Override
    public EnergyStorage getEnergyStorage() {
        MachineLogic logic = getLogic();
        return logic != null ? logic.getPowerPortEnergyStorage(backupStorage) : backupStorage;
    }

    @Override
    public EnergyConnection[] getEnergyConnections() {
        return energyConnections;
    }

    @Override
    public @Nonnull EnergyTileWrapper[] getEnergySides() {
        return energySides;
    }

    @Override
    public boolean hasConfigurableEnergyConnections() {
        return true;
    }

    // IMultitoolLogic

    @Override
    public boolean onUseMultitool(ItemStack multitool, ServerPlayer player, Level level, Direction facing, BlockPos hitPos) {
        if (!player.isCrouching()) {
            if (getMultiblockController().isPresent()) {
                if (getEnergyConnection(facing) != EnergyConnection.IN) {
                    for (Direction side : Direction.values()) {
                        setEnergyConnection(EnergyConnection.IN, side);
                    }
                    setActivity(false);
                    player.sendSystemMessage(Component.translatable(MODID + ".tooltip.port_toggle", Component.translatable(MODID + ".tooltip.in_config").withStyle(ChatFormatting.BLUE)));
                } else {
                    for (Direction side : Direction.values()) {
                        setEnergyConnection(EnergyConnection.OUT, side);
                    }
                    setActivity(true);
                    player.sendSystemMessage(Component.translatable(MODID + ".tooltip.port_toggle", Component.translatable(MODID + ".tooltip.out_config").withStyle(ChatFormatting.RED)));
                }
                markDirtyAndNotify(true);
                return true;
            }
        }

        return super.onUseMultitool(multitool, player, level, facing, hitPos);
    }

    // NBT

    @Override
    public CompoundTag writeAll(CompoundTag nbt, HolderLookup.Provider registries) {
        super.writeAll(nbt, registries);
        writeEnergyConnections(nbt, registries);
        return nbt;
    }

    @Override
    public void readAll(CompoundTag nbt, HolderLookup.Provider registries) {
        super.readAll(nbt, registries);
        readEnergyConnections(nbt, registries);
    }
}