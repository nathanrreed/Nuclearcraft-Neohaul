package com.nred.nuclearcraft.multiblock.fisson;

import com.nred.nuclearcraft.block.fission.IFissionController;
import com.nred.nuclearcraft.multiblock.MachineMultiblock;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import it.zerono.mods.zerocore.lib.multiblock.IMultiblockController;
import it.zerono.mods.zerocore.lib.multiblock.IMultiblockPart;
import it.zerono.mods.zerocore.lib.multiblock.validation.IMultiblockValidator;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import java.util.Set;

import static com.nred.nuclearcraft.config.Config2.fission_max_size;
import static com.nred.nuclearcraft.config.Config2.fission_min_size;

public class FissionReactor extends MachineMultiblock<FissionReactor> {
    public IFissionController<?> controller;
    protected final Set<Player> updatePacketListeners = new ObjectOpenHashSet<>();

    public FissionReactor(Level world) {
        super(world);
    }

    @Override
    public int getMinimumInteriorLength() {
        return fission_min_size;
    }

    @Override
    public int getMaximumInteriorLength() {
        return fission_max_size;
    }

    @Override
    public Set<Player> getMultiblockUpdatePacketListeners() {
        return updatePacketListeners;
    }

    @Override
    public CustomPacketPayload getMultiblockUpdatePacket() {
        return controller.getMultiblockUpdatePacket();
    }

    @Override
    public CustomPacketPayload getMultiblockRenderPacket() {
        return null;
    }

    @Override
    protected void onPartAdded(IMultiblockPart<FissionReactor> iMultiblockPart) {
    }

    @Override
    protected void onPartRemoved(IMultiblockPart<FissionReactor> iMultiblockPart) {

    }

    @Override
    protected void onMachineRestored() {
    }

    @Override
    protected void onMachinePaused() {
    }

    @Override
    protected void onMachineDisassembled() {
    }

    @Override
    protected void onAssimilate(IMultiblockController<FissionReactor> iMultiblockController) {
    }

    @Override
    protected void onAssimilated(IMultiblockController<FissionReactor> iMultiblockController) {
    }

    @Override
    protected boolean updateServer() {
        return false;
    }

    @Override
    protected void updateClient() {
    }

    @Override
    protected boolean isBlockGoodForFrame(Level level, int i, int i1, int i2, IMultiblockValidator iMultiblockValidator) {
        return false;
    }

    @Override
    protected boolean isBlockGoodForTop(Level level, int i, int i1, int i2, IMultiblockValidator iMultiblockValidator) {
        return false;
    }

    @Override
    protected boolean isBlockGoodForBottom(Level level, int i, int i1, int i2, IMultiblockValidator iMultiblockValidator) {
        return false;
    }

    @Override
    protected boolean isBlockGoodForSides(Level level, int i, int i1, int i2, IMultiblockValidator iMultiblockValidator) {
        return false;
    }

    @Override
    protected boolean isBlockGoodForInterior(Level level, int i, int i1, int i2, IMultiblockValidator iMultiblockValidator) {
        return false;
    }
}