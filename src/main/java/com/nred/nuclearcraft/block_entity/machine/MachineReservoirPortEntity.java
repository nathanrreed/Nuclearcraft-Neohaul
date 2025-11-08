package com.nred.nuclearcraft.block_entity.machine;

import com.nred.nuclearcraft.block_entity.ITickable;
import com.nred.nuclearcraft.block_entity.fluid.ITileFluid;
import com.nred.nuclearcraft.block_entity.internal.fluid.*;
import com.nred.nuclearcraft.block_entity.passive.ITilePassive;
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
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.nred.nuclearcraft.NuclearcraftNeohaul.MODID;
import static com.nred.nuclearcraft.registration.BlockEntityRegistration.MACHINE_ENTITY_TYPE;
import static com.nred.nuclearcraft.registration.BlockRegistration.FACING_ALL;
import static net.neoforged.neoforge.fluids.capability.IFluidHandler.FluidAction.EXECUTE;
import static net.neoforged.neoforge.fluids.capability.IFluidHandler.FluidAction.SIMULATE;

public class MachineReservoirPortEntity extends AbstractMachineEntity implements ITickable, ITileFluid {
    private final @Nonnull List<Tank> backupTanks = Collections.emptyList();

    private @Nonnull FluidConnection[] fluidConnections = ITileFluid.fluidConnectionAll(TankSorption.IN);

    private final @Nonnull FluidTileWrapper[] fluidSides;
    private final @Nonnull ChemicalTileWrapper[] chemicalSides;

    public MachineReservoirPortEntity(final BlockPos position, final BlockState blockState) {
        super(MACHINE_ENTITY_TYPE.get("reservoir_port").get(), position, blockState);
        fluidSides = ITileFluid.getDefaultFluidSides(this);
        chemicalSides = ITileFluid.getDefaultChemicalSides(this);
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
        if (!level.isClientSide()) {
            List<Tank> tanks = getTanks();
            if (!tanks.isEmpty() && !tanks.get(0).isEmpty()) {
                Optional<Direction> facing = getPartPosition().getDirection();
                if (facing.isPresent() && getTankSorption(facing.get(), 0).canDrain()) {
                    pushFluidToSide(facing.get());
                }
            }
        }
    }

    // Fluids

    @Override
    public @Nonnull List<Tank> getTanks() {
        MachineLogic logic = getLogic();
        return logic != null ? logic.getReservoirPortTanks(backupTanks) : backupTanks;
    }

    @Override
    @Nonnull
    public FluidConnection[] getFluidConnections() {
        return fluidConnections;
    }

    @Override
    public void setFluidConnections(@Nonnull FluidConnection[] connections) {
        fluidConnections = connections;
    }

    @Override
    @Nonnull
    public FluidTileWrapper[] getFluidSides() {
        return fluidSides;
    }

    @Nonnull
    @Override
    public ChemicalTileWrapper[] getChemicalSides() {
        return chemicalSides;
    }

    @Override
    public void pushFluidToSide(@Nonnull Direction side) {
        BlockEntity tile = getTileWorld().getBlockEntity(getTilePos().relative(side));
        if (tile == null) {
            return;
        }

        if (tile instanceof ITilePassive tilePassive && !tilePassive.canPushFluidsTo()) {
            return;
        }

        IFluidHandler adjStorage = level.getCapability(Capabilities.FluidHandler.BLOCK, tile.getBlockPos(), side.getOpposite());
        if (adjStorage == null) {
            return;
        }

        List<Tank> tanks = getTanks();
        if (!tanks.isEmpty()) {
            Tank tank = tanks.get(0);
            onWrapperDrain(tank.drain(adjStorage.fill(tank.drain(tank.getCapacity(), SIMULATE), EXECUTE), EXECUTE), EXECUTE);
        }
    }

    @Override
    public boolean getInputTanksSeparated() {
        return false;
    }

    @Override
    public void setInputTanksSeparated(boolean separated) {
    }

    @Override
    public boolean getVoidUnusableFluidInput(int tankNumber) {
        return false;
    }

    @Override
    public void setVoidUnusableFluidInput(int tankNumber, boolean voidUnusableFluidInput) {
    }

    @Override
    public TankOutputSetting getTankOutputSetting(int tankNumber) {
        return TankOutputSetting.DEFAULT;
    }

    @Override
    public void setTankOutputSetting(int tankNumber, TankOutputSetting setting) {
    }

    @Override
    public boolean hasConfigurableFluidConnections() {
        return true;
    }

    // IMultitoolLogic

    @Override
    public boolean onUseMultitool(ItemStack multitool, ServerPlayer player, Level level, Direction facing, BlockPos hitPos) {
        if (!player.isCrouching()) {
            if (getMultiblockController().isPresent()) {
                if (getTankSorption(facing, 0) != TankSorption.IN) {
                    for (Direction side : Direction.values()) {
                        setTankSorption(side, 0, TankSorption.IN);
                    }
                    setActivity(false);
                    player.sendSystemMessage(Component.translatable(MODID + ".tooltip.port_toggle", Component.translatable(MODID + ".tooltip.in_config").withStyle(ChatFormatting.DARK_AQUA)));
                } else {
                    for (Direction side : Direction.values()) {
                        setTankSorption(side, 0, TankSorption.OUT);
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
        writeFluidConnections(nbt, registries);
        return nbt;
    }

    @Override
    public void readAll(CompoundTag nbt, HolderLookup.Provider registries) {
        super.readAll(nbt, registries);
        readFluidConnections(nbt, registries);
    }
}