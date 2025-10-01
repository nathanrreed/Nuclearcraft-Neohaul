package com.nred.nuclearcraft.block.turbine;

import com.nred.nuclearcraft.NuclearcraftNeohaul;
import com.nred.nuclearcraft.menu.multiblock.TurbineControllerMenu;
import com.nred.nuclearcraft.multiblock.IMultiblockGuiPart;
import com.nred.nuclearcraft.multiblock.turbine.Turbine;
import com.nred.nuclearcraft.util.NCMath;
import it.zerono.mods.zerocore.lib.network.INetworkTileEntitySyncProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;

import static com.nred.nuclearcraft.registration.BlockEntityRegistration.TURBINE_ENTITY_TYPE;
import static com.nred.nuclearcraft.registration.BlockRegistration.ACTIVE;

public class TurbineControllerEntity extends AbstractTurbineEntity implements MenuProvider, INetworkTileEntitySyncProvider, ITurbineController<TurbineControllerEntity>, IMultiblockGuiPart<Turbine> {
    protected boolean isRenderer = false;

    protected final float[] brightnessArray = new float[]{1F, 1F, 1F, 1F, 1F, 1F, 1F, 1F};
    protected int brightnessIndex = 0;

    public long prevRenderTime = 0L;

    public TurbineControllerEntity(BlockPos pos, BlockState blockState) {
        super(TURBINE_ENTITY_TYPE.get("controller").get(), pos, blockState);
    }

    @Override
    public void enlistForUpdates(ServerPlayer serverPlayer, boolean b) {
    }

    @Override
    public void delistFromUpdates(ServerPlayer serverPlayer) {
    }

    @Override
    public void sendUpdates() {
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable(NuclearcraftNeohaul.MODID + ".menu.title.multiblock");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        return new TurbineControllerMenu(containerId, playerInventory, ContainerLevelAccess.create(level, worldPosition), this);
    }

    @Override
    public boolean canOpenGui(Level world, BlockPos position, BlockState state) {
        return this.isMachineAssembled();
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
    public BlockPos getPos() {
        return worldPosition;
    }

    @Override
    public void setActiveState(boolean value) {
        BlockState state = getLevel().getBlockState(getPos());
        if (!state.isAir())
            getLevel().setBlock(getPos(), state.setValue(ACTIVE, value), Block.UPDATE_ALL);
    }

    @OnlyIn(Dist.CLIENT)
    public float nextRenderBrightness() {
        Turbine turbine = getMultiblockController().orElse(null);
        if (turbine == null) {
            return 1F;
        }
        brightnessArray[brightnessIndex] = level.getRawBrightness((turbine.getExtremeInteriorCoord(NCMath.getBit(brightnessIndex, 0) == 1, NCMath.getBit(brightnessIndex, 1) == 1, NCMath.getBit(brightnessIndex, 2) == 1)), level.getSkyDarken());
        brightnessIndex = (brightnessIndex + 1) % 8;

        return (brightnessArray[0] + brightnessArray[1] + brightnessArray[2] + brightnessArray[3] + brightnessArray[4] + brightnessArray[5] + brightnessArray[6] + brightnessArray[7]) / 8F;
    }
}
