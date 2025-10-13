package com.nred.nuclearcraft.block.turbine;

import com.nred.nuclearcraft.handler.TileContainerInfo;
import com.nred.nuclearcraft.handler.TileInfoHandler;
import com.nred.nuclearcraft.multiblock.turbine.TurbineLogic;
import com.nred.nuclearcraft.menu.multiblock.TurbineControllerMenu;
import com.nred.nuclearcraft.multiblock.turbine.Turbine;
import com.nred.nuclearcraft.util.NCMath;
import it.zerono.mods.zerocore.lib.multiblock.cuboid.PartPosition;
import it.zerono.mods.zerocore.lib.multiblock.validation.IMultiblockValidator;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

import static com.nred.nuclearcraft.registration.BlockEntityRegistration.TURBINE_ENTITY_TYPE;
import static com.nred.nuclearcraft.registration.BlockRegistration.ACTIVE;
import static com.nred.nuclearcraft.registration.BlockRegistration.FACING_ALL;

public class TurbineControllerEntity extends AbstractTurbineEntity implements ITurbineController<TurbineControllerEntity>, MenuProvider {
    protected final TileContainerInfo info = TileInfoHandler.getTileContainerInfo("turbine_controller"); // TODO look at use of this

    protected boolean isRenderer = false;

    protected final int[] brightnessArray = new int[]{15728880, 15728880, 15728880, 15728880, 15728880, 15728880, 15728880, 15728880};
    protected int brightnessIndex = 0;

    public long prevRenderTime = 0L;

    public TurbineControllerEntity(BlockPos pos, BlockState blockState) {
        super(TURBINE_ENTITY_TYPE.get("controller").get(), pos, blockState);
    }

    @Override
    public boolean isGoodForPosition(PartPosition position, IMultiblockValidator validatorCallback) {
        return position.isFace();
    }

    @Override
    public String getLogicID() {
        return "turbine";
    }

    @Override
    public TileContainerInfo getContainerInfo() {
        return info;
    }

    // MenuProvider

    @Override
    public @Nullable AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        return new TurbineControllerMenu(containerId, playerInventory, this);
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
    public void onPreMachineAssembled(Turbine controller) {
        super.onPreMachineAssembled(controller);
        if (!level.isClientSide) {
            Optional<Direction> facing = getPartPosition().getDirection();
            facing.ifPresent(direction -> level.setBlock(worldPosition, level.getBlockState(worldPosition).setValue(FACING_ALL, direction), 2));
        }
    }

    @Override
    public int[] weakSidesToCheck(Level worldIn, BlockPos posIn) {
        return new int[]{2, 3, 4, 5};
    }

    @Override
    public void onBlockNeighborChanged(BlockState state, Level world, BlockPos pos, BlockPos fromPos) {
        super.onBlockNeighborChanged(state, world, pos, fromPos);
        TurbineLogic logic = getLogic();
        if (logic != null) {
            logic.setIsTurbineOn();
        }
    }

    @Override
    public boolean isRenderer() {
        return isRenderer;
    }

    @Override
    public void setIsRenderer(boolean isRenderer) {
        this.isRenderer = isRenderer;
    }


    @Override
    public void setActivity(boolean value) {
        BlockState state = getLevel().getBlockState(getBlockPos());
        if (!state.isAir())
            getLevel().setBlock(getBlockPos(), state.setValue(ACTIVE, value), Block.UPDATE_ALL);
    }

    @OnlyIn(Dist.CLIENT)
    public int nextRenderBrightness() {
        Turbine turbine = getMultiblockController().orElse(null);

        if (turbine == null) {
            return 15728880;
        }
        brightnessArray[brightnessIndex] = LevelRenderer.getLightColor(level, (turbine.getExtremeInteriorCoord(NCMath.getBit(brightnessIndex, 0) == 1, NCMath.getBit(brightnessIndex, 1) == 1, NCMath.getBit(brightnessIndex, 2) == 1)));
        brightnessIndex = (brightnessIndex + 1) % 8;

        return (brightnessArray[0] + brightnessArray[1] + brightnessArray[2] + brightnessArray[3] + brightnessArray[4] + brightnessArray[5] + brightnessArray[6] + brightnessArray[7]) / 8;
    }
}