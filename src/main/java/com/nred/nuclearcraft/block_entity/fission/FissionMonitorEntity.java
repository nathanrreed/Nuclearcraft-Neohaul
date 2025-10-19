package com.nred.nuclearcraft.block_entity.fission;

import com.nred.nuclearcraft.multiblock.fisson.FissionReactor;
import com.nred.nuclearcraft.render.BlockHighlightTracker;
import com.nred.nuclearcraft.util.NBTHelper;
import it.zerono.mods.zerocore.lib.multiblock.cuboid.PartPosition;
import it.zerono.mods.zerocore.lib.multiblock.validation.IMultiblockValidator;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import static com.nred.nuclearcraft.NuclearcraftNeohaul.MODID;
import static com.nred.nuclearcraft.registration.BlockEntityRegistration.FISSION_ENTITY_TYPE;
import static com.nred.nuclearcraft.registration.BlockRegistration.FACING_ALL;
import static com.nred.nuclearcraft.util.PosHelper.DEFAULT_NON;

public class FissionMonitorEntity extends AbstractFissionEntity {
    protected BlockPos componentPos = DEFAULT_NON;

    public FissionMonitorEntity(final BlockPos position, final BlockState blockState) {
        super(FISSION_ENTITY_TYPE.get("monitor").get(), position, blockState);
    }

    @Override
    public boolean isGoodForPosition(PartPosition position, IMultiblockValidator validatorCallback) {
        return position.isFace();
    }

    @Override
    public void onPreMachineAssembled(FissionReactor controller) {
        super.onPreMachineAssembled(controller);
        if (!level.isClientSide) {
            getPartPosition().getDirection().ifPresent(direction -> level.setBlock(worldPosition, level.getBlockState(worldPosition).setValue(FACING_ALL, direction), 2));
        }
    }

    public BlockPos getComponentPos() {
        return componentPos;
    }

    public IFissionComponent getComponent() {
        FissionReactor reactor = getMultiblockController().orElse(null);
        return reactor == null ? null : reactor.getPartMap(IFissionComponent.class).get(componentPos.asLong());
    }

    // IMultitoolLogic

    @Override
    public boolean onUseMultitool(ItemStack multitool, ServerPlayer player, Level level, Direction facing, BlockPos hitPos) {
        CompoundTag nbt = NBTHelper.getStackNBT(multitool, "ncMultitool");
        if (nbt != null) {
            if (!player.isCrouching()) {
                Component displayName = getTileBlockDisplayName();
                if (nbt.contains("fissionComponentInfo", 10)) {
                    CompoundTag info = nbt.getCompound("fissionComponentInfo");
                    if (info.contains("componentPos", 99)) {
                        componentPos = BlockPos.of(info.getLong("componentPos"));
                        BlockHighlightTracker.sendPacket(player, componentPos, 5000);
                        player.sendSystemMessage(Component.translatable(MODID + ".message.multitool.load_component_info", info.getString("displayName"), displayName));
                        nbt.remove("fissionComponentInfo");
                        return true;
                    } else {
                        player.sendSystemMessage(Component.translatable(MODID + ".message.multitool.invalid_component_info", info.getString("displayName"), displayName));
                    }
                } else {
                    if (!DEFAULT_NON.equals(componentPos)) {
                        IFissionComponent component = getComponent();
                        if (component != null) {
                            BlockHighlightTracker.sendPacket(player, componentPos, 5000);
                            BlockPos pos = component.getTilePos();
                            player.sendSystemMessage(Component.translatable(MODID + ".message.multitool.connected_component_info", displayName, component.getTileBlockDisplayName(), pos.getX(), pos.getY(), pos.getZ()));
                            return true;
                        }
                    }
                    player.sendSystemMessage(Component.translatable(MODID + ".message.multitool.no_connected_component_info", displayName));
                    return true;
                }
            }
        }
        return super.onUseMultitool(multitool, player, level, facing, hitPos);
    }

    // NBT

    @Override
    public CompoundTag writeAll(CompoundTag nbt, HolderLookup.Provider registries) {
        super.writeAll(nbt, registries);
        nbt.putLong("componentPos", componentPos.asLong());
        return nbt;
    }

    @Override
    public void readAll(CompoundTag nbt, HolderLookup.Provider registries) {
        super.readAll(nbt, registries);
        componentPos = BlockPos.of(nbt.getLong("componentPos"));
    }
}