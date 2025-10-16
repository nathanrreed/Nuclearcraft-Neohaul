package com.nred.nuclearcraft.block_entity.fission;

import com.nred.nuclearcraft.handler.TileContainerInfo;
import com.nred.nuclearcraft.handler.TileInfoHandler;
import com.nred.nuclearcraft.menu.multiblock.SolidFissionControllerMenu;
import com.nred.nuclearcraft.multiblock.fisson.FissionReactor;
import it.zerono.mods.zerocore.lib.multiblock.cuboid.PartPosition;
import it.zerono.mods.zerocore.lib.multiblock.validation.IMultiblockValidator;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.Optional;

import static com.nred.nuclearcraft.registration.BlockEntityRegistration.FISSION_ENTITY_TYPE;
import static com.nred.nuclearcraft.registration.BlockRegistration.FACING_ALL;
import static com.nred.nuclearcraft.registration.FluidRegistration.CUSTOM_FLUID_MAP;

public class SolidFissionControllerEntity extends AbstractFissionEntity implements IFissionController<SolidFissionControllerEntity>, MenuProvider {
    protected final TileContainerInfo<SolidFissionControllerEntity> info = TileInfoHandler.getTileContainerInfo("solid_fission_controller");

    public SolidFissionControllerEntity(BlockPos pos, BlockState blockState) {
        super(FISSION_ENTITY_TYPE.get("solid_fuel_fission_controller").get(), pos, blockState);
    }

    @Override
    public boolean isGoodForPosition(PartPosition position, IMultiblockValidator validatorCallback) {
        return position.isFace();
    }

    @Override
    public String getLogicID() {
        return "solid_fuel";
    }

    @Override
    public TileContainerInfo getContainerInfo() {
        return info;
    }

    // MenuProvider

    @Override
    public @Nullable AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        return new SolidFissionControllerMenu(containerId, playerInventory, this);
    }

    @Override
    public Component getDisplayName() {
        return getTileBlockDisplayName();
    }

    @Override
    public boolean canOpenGui(Level world, BlockPos position, BlockState state) {
        return this.isMachineAssembled();
    }

    @Override
    public void onPreMachineAssembled(FissionReactor controller) {
        super.onPreMachineAssembled(controller);
        if (!level.isClientSide) {
            Optional<Direction> facing = getPartPosition().getDirection();
            facing.ifPresent(direction -> level.setBlock(worldPosition, level.getBlockState(worldPosition).setValue(FACING_ALL, direction), 2));
        }
    }

    @Override
    public void onBlockNeighborChanged(BlockState state, Level world, BlockPos pos, BlockPos fromPos) {
        super.onBlockNeighborChanged(state, world, pos, fromPos);
        Optional<FissionReactor> multiblock = getMultiblockController();
        multiblock.ifPresent(FissionReactor::updateActivity);
    }

    @Override
    public void doMeltdown(Iterator<IFissionController<?>> controllerIterator) {
        controllerIterator.remove();
        level.removeBlockEntity(worldPosition);

        BlockState corium = CUSTOM_FLUID_MAP.get("corium").block.get().defaultBlockState();
        level.setBlockAndUpdate(worldPosition, corium);
    }
}