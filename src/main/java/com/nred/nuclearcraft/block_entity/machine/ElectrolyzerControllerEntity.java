package com.nred.nuclearcraft.block_entity.machine;

import com.nred.nuclearcraft.handler.TileContainerInfo;
import com.nred.nuclearcraft.handler.TileInfoHandler;
import com.nred.nuclearcraft.menu.multiblock.controller.ElectrolyzerControllerMenu;
import com.nred.nuclearcraft.multiblock.machine.Machine;
import it.zerono.mods.zerocore.lib.multiblock.cuboid.PartPosition;
import it.zerono.mods.zerocore.lib.multiblock.validation.IMultiblockValidator;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

import static com.nred.nuclearcraft.registration.BlockEntityRegistration.ELECTROLYZER_ENTITY_TYPE;
import static com.nred.nuclearcraft.registration.BlockRegistration.FACING_ALL;

public class ElectrolyzerControllerEntity extends AbstractMachineEntity implements IMachineController<ElectrolyzerControllerEntity> {
    protected final TileContainerInfo<ElectrolyzerControllerEntity> info = TileInfoHandler.getTileContainerInfo("electrolyzer_controller");

    protected boolean isRenderer = false;

    public ElectrolyzerControllerEntity(BlockPos pos, BlockState blockState) {
        super(ELECTROLYZER_ENTITY_TYPE.get("controller").get(), pos, blockState);
    }

    @Override
    public boolean isGoodForPosition(PartPosition position, IMultiblockValidator validatorCallback) {
        return position.isFace();
    }

    @Override
    public String getLogicID() {
        return "electrolyzer";
    }

    @Override
    public TileContainerInfo<ElectrolyzerControllerEntity> getContainerInfo() {
        return info;
    }

    // MenuProvider

    @Override
    public @Nullable AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        return new ElectrolyzerControllerMenu(containerId, playerInventory, this);
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
    public void onPreMachineAssembled(Machine controller) {
        super.onPreMachineAssembled(controller);
        if (!level.isClientSide()) {
            Optional<Direction> facing = getPartPosition().getDirection();
            facing.ifPresent(direction -> level.setBlock(worldPosition, level.getBlockState(worldPosition).setValue(FACING_ALL, direction), 2));
        }
    }

    @Override
    public int[] weakSidesToCheck(Level level, BlockPos posIn) {
        return new int[]{2, 3, 4, 5};
    }

    @Override
    public boolean isRenderer() {
        return isRenderer;
    }

    @Override
    public void setIsRenderer(boolean isRenderer) {
        this.isRenderer = isRenderer;
    }
}