package com.nred.nuclearcraft.block_entity;

import com.nred.nuclearcraft.block_entity.internal.energy.EnergyStorageVoid;
import com.nred.nuclearcraft.block_entity.internal.fluid.TankVoid;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import static com.nred.nuclearcraft.registration.BlockEntityRegistration.UNIVERSAL_BIN_ENTITY_TYPE;

public class UniversalBinEntity extends BlockEntity implements Container {
    public UniversalBinEntity(BlockPos pos, BlockState blockState) {
        super(UNIVERSAL_BIN_ENTITY_TYPE.get(), pos, blockState);
    }

    public final EnergyStorageVoid energyStorage = new EnergyStorageVoid();
    public final TankVoid tank = new TankVoid();

    @Override
    public int getContainerSize() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return true;
    }

    @Override
    public ItemStack getItem(int slot) {
        return ItemStack.EMPTY;
    }

    @Override
    public ItemStack removeItem(int slot, int amount) {
        return ItemStack.EMPTY;
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        return ItemStack.EMPTY;
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
    }

    @Override
    public boolean stillValid(Player player) {
        return false;
    }

    @Override
    public void clearContent() {
    }
}