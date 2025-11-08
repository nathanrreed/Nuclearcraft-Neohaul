package com.nred.nuclearcraft.block_entity.fission;

import com.google.common.collect.Lists;
import com.nred.nuclearcraft.block_entity.fluid.ITileFluid;
import com.nred.nuclearcraft.block_entity.internal.fluid.*;
import com.nred.nuclearcraft.block_entity.passive.ITilePassive;
import com.nred.nuclearcraft.block_entity.ITickable;
import com.nred.nuclearcraft.multiblock.fisson.FissionReactor;
import com.nred.nuclearcraft.multiblock.fisson.FissionReactorLogic;
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
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;

import static com.nred.nuclearcraft.NuclearcraftNeohaul.MODID;
import static com.nred.nuclearcraft.registration.BlockEntityRegistration.FISSION_ENTITY_TYPE;
import static com.nred.nuclearcraft.registration.BlockRegistration.FACING_ALL;
import static net.neoforged.neoforge.fluids.capability.IFluidHandler.FluidAction.EXECUTE;
import static net.neoforged.neoforge.fluids.capability.IFluidHandler.FluidAction.SIMULATE;

public class FissionVentEntity extends AbstractFissionEntity implements ITickable, ITileFluid {
    private final @Nonnull List<Tank> backupTanks = Collections.emptyList();

    private @Nonnull FluidConnection[] fluidConnections = ITileFluid.fluidConnectionAll(Lists.newArrayList(TankSorption.IN, TankSorption.NON));

    private final @Nonnull FluidTileWrapper[] fluidSides;
    private final @Nonnull ChemicalTileWrapper[] chemicalSides;

    public FissionVentEntity(final BlockPos position, final BlockState blockState) {
        super(FISSION_ENTITY_TYPE.get("vent").get(), position, blockState);
        fluidSides = ITileFluid.getDefaultFluidSides(this);
        chemicalSides = ITileFluid.getDefaultChemicalSides(this);
    }

    @Override
    public boolean isGoodForPosition(PartPosition position, IMultiblockValidator validatorCallback) {
        return position.isFace();
    }

    @Override
    public void onPreMachineAssembled(FissionReactor controller) {
        super.onPreMachineAssembled(controller);
        if (!level.isClientSide) {
            getPartPosition().getDirection().ifPresent(facing -> level.setBlock(worldPosition, level.getBlockState(worldPosition).setValue(FACING_ALL, facing), 2));
        }
    }

    @Override
    public void update() {
        if (!level.isClientSide) {
            List<Tank> tanks = getTanks();
            if (tanks.size() >= 2 && !tanks.get(1).isEmpty()) {
                Direction facing = getPartPosition().getDirection().orElse(null);
                if (facing != null && getTankSorption(facing, 1).canDrain()) {
                    pushFluidToSide(facing);
                }
            }
        }
    }

    // Fluids

    @Override
    public @Nonnull List<Tank> getTanks() {
        FissionReactorLogic logic = getLogic();
        return logic != null ? logic.getVentTanks(backupTanks) : backupTanks;
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
    public void pushFluidToSide(@NotNull Direction side) {
        BlockEntity tile = level.getBlockEntity(getTilePos().relative(side));
        if (tile == null) {
            return;
        }

        if (tile instanceof ITilePassive tilePassive && !tilePassive.canPushFluidsTo()) {
            return;
        }

        IFluidHandler adjStorage = level.getCapability(Capabilities.FluidHandler.BLOCK, worldPosition.relative(side), side.getOpposite());
        if (adjStorage == null) {
            return;
        }

        List<Tank> tanks = getTanks();
        if (tanks.size() >= 2) {
            Tank tank = tanks.get(1);
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
                        setTankSorption(side, 1, TankSorption.NON);
                    }
                    setActivity(false);
                    player.sendSystemMessage(Component.translatable(MODID + ".tooltip.vent_toggle", Component.translatable(MODID + ".tooltip.in_config").withStyle(ChatFormatting.DARK_AQUA)));
                } else {
                    for (Direction side : Direction.values()) {
                        setTankSorption(side, 0, TankSorption.NON);
                        setTankSorption(side, 1, TankSorption.OUT);
                    }
                    setActivity(true);
                    player.sendSystemMessage(Component.translatable(MODID + ".tooltip.vent_toggle", Component.translatable(MODID + ".tooltip.out_config").withStyle(ChatFormatting.RED)));
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