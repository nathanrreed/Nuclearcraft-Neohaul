package com.nred.nuclearcraft.block_entity.battery;

import com.nred.nuclearcraft.block_entity.ITickable;
import com.nred.nuclearcraft.block_entity.TilePartAbstract;
import com.nred.nuclearcraft.block_entity.dummy.IInterfaceable;
import com.nred.nuclearcraft.block_entity.energy.ITileEnergy;
import com.nred.nuclearcraft.block_entity.internal.energy.EnergyConnection;
import com.nred.nuclearcraft.block_entity.internal.energy.EnergyStorage;
import com.nred.nuclearcraft.block_entity.internal.energy.EnergyTileWrapper;
import com.nred.nuclearcraft.multiblock.battery.BatteryMultiblock;
import com.nred.nuclearcraft.multiblock.battery.BatteryType;
import com.nred.nuclearcraft.util.NCMath;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nonnull;

import static com.nred.nuclearcraft.NuclearcraftNeohaul.MODID;
import static com.nred.nuclearcraft.registration.BlockEntityRegistration.BATTERY_ENTITY_TYPE;

public class TileBattery extends TilePartAbstract<BatteryMultiblock> implements ITickable, ITileEnergy, IInterfaceable {
    public final BatteryType batteryType;

    protected final EnergyStorage backupStorage = new EnergyStorage(0L);

    protected @Nonnull
    final EnergyConnection[] energyConnections;
    protected boolean[] ignoreSide = new boolean[]{false, false, false, false, false, false};

    protected @Nonnull
    final EnergyTileWrapper[] energySides;

    public long waitingEnergy = 0L;

    public TileBattery(BlockPos pos, BlockState state, BatteryType batteryType) {
        super(BATTERY_ENTITY_TYPE.get(), pos, state);
        this.energyConnections = ITileEnergy.energyConnectionAll(EnergyConnection.IN);
        this.energySides = ITileEnergy.getDefaultEnergySides(this);
        this.batteryType = batteryType;
    }

    public boolean ignoreSide(Direction side) {
        return side != null && ignoreSide[side.ordinal()];
    }

    @Override
    public BlockEntity getTile() {
        return this;
    }

    @Override
    public Level getTileWorld() {
        return getCurrentWorld();
    }

    @Override
    public BlockPos getTilePos() {
        return worldPosition;
    }

    @Override
    public Block getTileBlockType() {
        return getBlockType();
    }

    @Override
    public boolean getIsRedstonePowered() {
        return false;
    }

    @Override
    public void setIsRedstonePowered(boolean isRedstonePowered) {
    }

    @Override
    public boolean getAlternateComparator() {
        return false;
    }

    @Override
    public void setAlternateComparator(boolean alternate) {
    }

    @Override
    public boolean getRedstoneControl() {
        return false;
    }

    @Override
    public void setRedstoneControl(boolean redstoneControl) {
    }

    @Override
    public BatteryMultiblock createController() {
        return new BatteryMultiblock(level);
    }

    @Override
    public Class<BatteryMultiblock> getControllerType() {
        return BatteryMultiblock.class;
    }

    @Override
    public void onPreMachineAssembled(BatteryMultiblock controller) {

    }

    @Override
    public void onPostMachineAssembled(BatteryMultiblock controller) {

    }

    @Override
    public void onPreMachineBroken() {

    }

    @Override
    public void onPostMachineBroken() {

    }

    @Override
    public void onMachineActivated() {

    }

    @Override
    public void onMachineDeactivated() {

    }

    @Override
    public void update() {
        if (!level.isClientSide()) {
            if (waitingEnergy != 0L && getMultiblockController().isPresent()) {
                getEnergyStorage().changeEnergyStored(waitingEnergy);
                waitingEnergy = 0L;
            }
            pushEnergy();
        }
    }

    @Override
    public void pushEnergyToSide(@Nonnull Direction side) {
        if (!ignoreSide(side)) {
            ITileEnergy.super.pushEnergyToSide(side);
        }
    }

    public void onMultiblockRefresh() {
        for (Direction side : Direction.values()) {
            ignoreSide[side.ordinal()] = level.getBlockEntity(worldPosition.relative(side)) instanceof TileBattery;
        }
    }

    @Override
    public EnergyStorage getEnergyStorage() {
        BatteryMultiblock multiblock = getMultiblockController().orElse(null);
        return multiblock != null ? multiblock.getEnergyStorage() : backupStorage;
    }

    @Override
    public EnergyConnection[] getEnergyConnections() {
        return energyConnections;
    }

    @Override
    public @Nonnull EnergyTileWrapper[] getEnergySides() {
        return energySides;
    }

    @Override
    public boolean hasConfigurableEnergyConnections() {
        return true;
    }

    // IMultitoolLogic

    @Override
    public boolean onUseMultitool(ItemStack multitool, ServerPlayer player, Level level, Direction facing, BlockPos hitPos) {
        boolean opposite = player.isCrouching();
        Direction side = opposite ? facing.getOpposite() : facing;
        toggleEnergyConnection(side, EnergyConnection.Type.DEFAULT);
        EnergyConnection energyConnection = getEnergyConnection(side);

        setChanged();
        player.sendSystemMessage(Component.translatable(opposite ? MODID + ".message.multitool.energy_toggle_opposite" : MODID + ".message.multitool.energy_toggle", Component.translatable(MODID + ".tooltip." + energyConnection.getSerializedName() + "_config")).withStyle(energyConnection.getTextColor()));
        return true;
    }

    // NBT

    @Override
    protected void saveAdditional(CompoundTag data, HolderLookup.Provider registries) {
        super.saveAdditional(data, registries);
        writeEnergyConnections(data, registries);
        data.putLong("waitingEnergy", waitingEnergy);
        data.putByteArray("ignoreSide", NCMath.booleansToBytes(ignoreSide));
    }

    @Override
    public void loadAdditional(CompoundTag data, HolderLookup.Provider registries) {
        super.loadAdditional(data, registries);
        readEnergyConnections(data, registries);
        waitingEnergy = data.getLong("waitingEnergy");
        boolean[] arr = NCMath.bytesToBooleans(data.getByteArray("ignoreSide"));
        if (arr.length == 6) {
            ignoreSide = arr;
        }
    }

    @Override
    protected void collectImplicitComponents(DataComponentMap.Builder components) {
        CompoundTag tag = new CompoundTag();
        saveAdditional(tag, level.registryAccess());
        components.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
        super.collectImplicitComponents(components);
    }

    @Override
    protected void applyImplicitComponents(DataComponentInput componentInput) {
        CompoundTag tag = componentInput.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
        HolderLookup.Provider registries = level.registryAccess();
        this.waitingEnergy += tag.getLong("waitingEnergy");

        if (Minecraft.getInstance().player.isCrouching()) {
            this.readEnergyConnections(tag, registries);
        }
        super.applyImplicitComponents(componentInput);
    }
}