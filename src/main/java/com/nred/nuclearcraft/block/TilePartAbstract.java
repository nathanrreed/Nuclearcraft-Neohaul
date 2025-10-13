package com.nred.nuclearcraft.block;

import com.nred.nuclearcraft.NuclearcraftNeohaul;
import it.zerono.mods.zerocore.lib.multiblock.cuboid.AbstractCuboidMultiblockController;
import it.zerono.mods.zerocore.lib.multiblock.cuboid.AbstractCuboidMultiblockPart;
import it.zerono.mods.zerocore.lib.multiblock.cuboid.PartPosition;
import it.zerono.mods.zerocore.lib.multiblock.validation.IMultiblockValidator;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public abstract class TilePartAbstract<MULTIBLOCK extends AbstractCuboidMultiblockController<MULTIBLOCK>> extends AbstractCuboidMultiblockPart<MULTIBLOCK> implements ITile {

    private boolean isRedstonePowered = false, alternateComparator = false, redstoneControl = false;

//	private final IRadiationSource radiation; TODO

    public TilePartAbstract(final BlockEntityType<?> type, final BlockPos position, final BlockState blockState) {
        super(type, position, blockState);
//		radiation = new RadiationSource(0D);
    }

    @Override
    public void onLoad() {
        if (level.isClientSide) {
//            level.markBlockRangeForRenderUpdate(pos, pos); TODO
            refreshIsRedstonePowered(level, worldPosition);
            setChanged();
            updateComparatorOutputLevel();
        }
    }

    @Override
    public boolean isGoodForPosition(PartPosition position, IMultiblockValidator validatorCallback) {
        // Most Reactor parts are not allowed on the Frame an inside the Reactor so reject those positions and allow all the other ones

        final BlockPos coordinates = this.getWorldPosition();

        if (position.isFrame()) {
            validatorCallback.setLastError(coordinates, NuclearcraftNeohaul.MODID + ".multiblock.validation.invalid_frame_block", coordinates.getX(), coordinates.getY(), coordinates.getZ());
            return false;
        } else if (PartPosition.Interior == position) {
            validatorCallback.setLastError(coordinates, NuclearcraftNeohaul.MODID + ".multiblock.validation.invalid_part_for_interior", coordinates.getX(), coordinates.getY(), coordinates.getZ());
            return false;
        }

        return true;
    }

    @Override
    public void onMachineActivated() {
    }

    @Override
    public void onMachineDeactivated() {
    }

    @Override
    public BlockEntity getTile() {
        return this;
    }

    @Override
    public Level getTileWorld() {
        return level;
    }

    @Override
    public BlockPos getTilePos() {
        return worldPosition;
    }

    @Override
    public Block getTileBlockType() {
        return getBlockState().getBlock();
    }

//	@Override
//	public IRadiationSource getRadiationSource() {
//		return radiation;
//	}

    public boolean isUseableByPlayer(Player entityplayer) {
        if (level.getBlockEntity(worldPosition) != this) {
            return false;
        }
        return entityplayer.distanceToSqr(worldPosition.getX() + 0.5D, worldPosition.getY() + 0.5D, worldPosition.getZ() + 0.5D) <= 64D;
    }

    @Override
    public final void markTileDirty() {
        setChanged();
    }

    @Override
    public void setChanged() {
        if (level != null) {
            level.blockEntityChanged(worldPosition);
        }
    }

    // Redstone

    @Override
    public boolean getIsRedstonePowered() {
        return isRedstonePowered;
    }

    @Override
    public void setIsRedstonePowered(boolean isRedstonePowered) {
        this.isRedstonePowered = isRedstonePowered;
    }

    @Override
    public boolean getAlternateComparator() {
        return alternateComparator;
    }

    @Override
    public void setAlternateComparator(boolean alternate) {
        alternateComparator = alternate;
    }

    @Override
    public boolean getRedstoneControl() {
        return redstoneControl;
    }

    @Override
    public void setRedstoneControl(boolean redstoneControl) {
        this.redstoneControl = redstoneControl;
    }

//    // Capabilities TODO
//
//    @Override
//    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing side) {
//        if (capability == IRadiationSource.CAPABILITY_RADIATION_SOURCE) {
//            return radiation != null;
//        }
//        return super.hasCapability(capability, side);
//    }
//
//    @Override
//    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing side) {
//        if (capability == IRadiationSource.CAPABILITY_RADIATION_SOURCE) {
//            return IRadiationSource.CAPABILITY_RADIATION_SOURCE.cast(radiation);
//        }
//        return super.getCapability(capability, side);
//    }
//
//    // GUI management
//
//    /**
//     * Open the specified GUI
//     *
//     * @param player the player currently interacting with your block/tile entity
//     * @param guiId  the GUI to open
//     * @return true if the GUI was opened, false otherwise
//     */
//    public boolean openGui(Object mod, Player player, int guiId) {
//        player.openGui(mod, guiId, world, pos.getX(), pos.getY(), pos.getZ());
//        return true;
//    }
//
//    /**
//     * Returns a Server side Container to be displayed to the user.
//     *
//     * @param guiId  the GUI ID number
//     * @param player the player currently interacting with your block/tile entity
//     * @return A GuiScreen/Container to be displayed to the user, null if none.
//     */
//    public Object getServerGuiElement(int guiId, EntityPlayer player) {
//        return null;
//    }
//
//    /**
//     * Returns a Container to be displayed to the user. On the client side, this needs to return an instance of GuiScreen On the server side, this needs to return an instance of Container
//     *
//     * @param guiId  the GUI ID number
//     * @param player the player currently interacting with your block/tile entity
//     * @return A GuiScreen/Container to be displayed to the user, null if none.
//     */
//    public Object getClientGuiElement(int guiId, EntityPlayer player) {
//        return null;
//    }

    // NBT

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        writeAll(tag, registries);
    }

    public CompoundTag writeAll(CompoundTag nbt, HolderLookup.Provider registries) {
        nbt.putBoolean("isRedstonePowered", isRedstonePowered);
        nbt.putBoolean("alternateComparator", alternateComparator);
        nbt.putBoolean("redstoneControl", redstoneControl);
        if (shouldSaveRadiation()) {
            writeRadiation(nbt, registries);
        }
        return nbt;
    }

    public CompoundTag writeRadiation(CompoundTag nbt, HolderLookup.Provider registries) {
//		nbt.putDouble("radiationLevel", getRadiationSource().getRadiationLevel()); TODO
        return nbt;
    }

    @Override
    public void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        readAll(tag, registries);
        super.loadAdditional(tag, registries);
    }

    public void readAll(CompoundTag nbt, HolderLookup.Provider registries) {
        isRedstonePowered = nbt.getBoolean("isRedstonePowered");
        alternateComparator = nbt.getBoolean("alternateComparator");
        redstoneControl = nbt.getBoolean("redstoneControl");
        if (shouldSaveRadiation()) {
            readRadiation(nbt, registries);
        }
    }

    public void readRadiation(CompoundTag nbt, HolderLookup.Provider registries) {
        if (nbt.contains("radiationLevel")) {
//			getRadiationSource().setRadiationLevel(nbt.getDouble("radiationLevel")); TODO
        }
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        CompoundTag tag = super.getUpdateTag(registries);
        saveAdditional(tag, registries);
        return tag;
    }

    @Override
    public void handleUpdateTag(CompoundTag data, HolderLookup.Provider registries) {
        super.handleUpdateTag(data, registries);
        loadAdditional(data, registries);
    }

    //    @Nullable
//    @Override
//    public SPacketUpdateTileEntity getUpdatePacket() {
//        NBTTagCompound data = super.writeToNBT(new NBTTagCompound());
//        writeAll(data);
//        syncDataTo(data, SyncReason.NetworkUpdate);
//        return new SPacketUpdateTileEntity(pos, getBlockMetadata(), data);
//    }
//
//    @Override
//    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity packet) {
//        NBTTagCompound data = packet.getNbtCompound();
//        super.readFromNBT(data);
//        readAll(data);
//        syncDataFrom(data, SyncReason.NetworkUpdate);
//        if (getBlockType() instanceof IDynamicState) {
//            notifyBlockUpdate();
//        }
//    }
}
