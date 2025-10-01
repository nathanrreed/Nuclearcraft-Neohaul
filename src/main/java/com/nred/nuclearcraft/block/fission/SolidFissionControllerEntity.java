package com.nred.nuclearcraft.block.fission;

import com.nred.nuclearcraft.NuclearcraftNeohaul;
import com.nred.nuclearcraft.menu.multiblock.SolidFissionControllerMenu;
import com.nred.nuclearcraft.multiblock.IMultiblockGuiPart;
import com.nred.nuclearcraft.multiblock.fisson.FissionReactor;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;

import static com.nred.nuclearcraft.registration.BlockEntityRegistration.FISSION_ENTITY_TYPE;

public class SolidFissionControllerEntity extends AbstractFissionEntity implements MenuProvider, IFissionController<SolidFissionControllerEntity>, IMultiblockGuiPart<FissionReactor> {
    public SolidFissionControllerEntity(BlockPos pos, BlockState blockState) {
        super(FISSION_ENTITY_TYPE.get("solid_fuel_fission_controller").get(), pos, blockState);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable(NuclearcraftNeohaul.MODID + ".menu.title.multiblock");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        return new SolidFissionControllerMenu(containerId, playerInventory, ContainerLevelAccess.create(level, worldPosition), this);
    }

    @Override
    public boolean canOpenGui(Level world, BlockPos position, BlockState state) {
        return this.isMachineAssembled();
    }

    @Override
    public void doMeltdown(Iterator<IFissionController<?>> controllerIterator) {

    }

    @Override
    public CustomPacketPayload getMultiblockUpdatePacket() {
//        return new SaltFissionPayload(pos, isReactorOn, heatBuffer, clusterCount, cooling, rawHeating, totalHeatMult, meanHeatMult, fuelComponentCount, usefulPartCount, totalEfficiency, meanEfficiency, sparsityEfficiencyMult, meanHeatingSpeedMultiplier, totalHeatingSpeedMultiplier);
        return null; // TODO
    }
}
