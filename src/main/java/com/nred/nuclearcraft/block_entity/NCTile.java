package com.nred.nuclearcraft.block_entity;

import com.nred.nuclearcraft.block.IDynamicState;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public abstract class NCTile extends BlockEntity implements ITile {

    private boolean isRedstonePowered = false, alternateComparator = false, redstoneControl = false;

//	private final IRadiationSource radiation;

    public NCTile(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
//		radiation = new RadiationSource(0D);
    }

    @Override
    public void onLoad() {
        if (level.isClientSide) {
            ClientLevel l;
//            level.markBlockRangeForRenderUpdate(worldPosition, worldPosition); TODO
            refreshIsRedstonePowered(level, worldPosition);
            setChanged();
            updateComparatorOutputLevel();
        }
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

//	@Override TODO
//	public IRadiationSource getRadiationSource() {
//		return radiation;
//	}

//	@Override
//	public Component getDisplayName() {
//		Block block = getBlockType();
//		return block == null ? null : new TextComponentTranslation(block.getLocalizedName());
//	}
//
//	@Override
//	public boolean shouldRefresh(World worldIn, BlockPos posIn, IBlockState oldState, IBlockState newState) {
//		return oldState.getBlock() != newState.getBlock();
//	}

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
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        readAll(tag, registries);
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
        CompoundTag tag = new CompoundTag();
        saveAdditional(tag, registries);
        return tag;
    }

//    @Nullable
//    @Override
//    public SPacketUpdateTileEntity getUpdatePacket() {
//        return new SPacketUpdateTileEntity(pos, getBlockMetadata(), writeToNBT(new NBTTagCompound()));
//    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt, HolderLookup.Provider lookupProvider) {
        loadAdditional(pkt.getTag(), lookupProvider);
        if (getBlockState() instanceof IDynamicState) {
            notifyBlockUpdate();
        }
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
//    // For when the raw TE capabilities need to be reached:
//
//    protected boolean hasCapabilityDefault(Capability<?> capability, @Nullable EnumFacing side) {
//        return super.hasCapability(capability, side);
//    }
//
//    protected <T> T getCapabilityDefault(Capability<T> capability, @Nullable EnumFacing side) {
//        return super.getCapability(capability, side);
//    }
//
//    // TESR
//
//    @Override
//    @SideOnly(Side.CLIENT)
//    public double getMaxRenderDistanceSquared() {
//        return NCMath.sq(16D * FMLClientHandler.instance().getClient().gameSettings.renderDistanceChunks);
//    }
}
