package com.nred.nuclearcraft.block_entity.hx;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.nred.nuclearcraft.block.hx.HeatExchangerTubeBlock;
import com.nred.nuclearcraft.block_entity.IRayTraceLogic;
import com.nred.nuclearcraft.item.MultitoolItem;
import com.nred.nuclearcraft.multiblock.hx.HeatExchangerTubeSetting;
import com.nred.nuclearcraft.multiblock.hx.HeatExchangerTubeType;
import it.zerono.mods.zerocore.lib.multiblock.cuboid.PartPosition;
import it.zerono.mods.zerocore.lib.multiblock.validation.IMultiblockValidator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static com.nred.nuclearcraft.NuclearcraftNeohaul.MODID;
import static com.nred.nuclearcraft.block.hx.HeatExchangerTubeBlock.SIDE_BAFFLE_SHAPE;
import static com.nred.nuclearcraft.block.hx.HeatExchangerTubeBlock.SIDE_SHAPE;
import static com.nred.nuclearcraft.registration.BlockEntityRegistration.HX_ENTITY_TYPE;

public class HeatExchangerTubeEntity extends AbstractHeatExchangerEntity implements IRayTraceLogic {
    public final HeatExchangerTubeType tubeType;

    public @Nonnull HeatExchangerTubeSetting[] settings = new HeatExchangerTubeSetting[]{HeatExchangerTubeSetting.CLOSED, HeatExchangerTubeSetting.CLOSED, HeatExchangerTubeSetting.CLOSED, HeatExchangerTubeSetting.CLOSED, HeatExchangerTubeSetting.CLOSED, HeatExchangerTubeSetting.CLOSED};

    public @Nullable Vec3 tubeFlow = null;
    public @Nullable Vec3 shellFlow = null;

    public HeatExchangerTubeEntity(final BlockPos position, final BlockState blockState, HeatExchangerTubeType tubeType) {
        super(HX_ENTITY_TYPE.get("tube").get(), position, blockState);
        this.tubeType = tubeType;
    }

    @Override
    public boolean isGoodForPosition(PartPosition position, IMultiblockValidator validatorCallback) {
        return position == PartPosition.Interior;
    }

    public static abstract class Variant extends HeatExchangerTubeEntity {
        protected Variant(final BlockPos position, final BlockState blockState, HeatExchangerTubeType type) {
            super(position, blockState, type);
        }
    }

    public static class Copper extends Variant {
        public Copper(final BlockPos position, final BlockState blockState) {
            super(position, blockState, HeatExchangerTubeType.COPPER);
        }
    }

    public static class HardCarbon extends Variant {
        public HardCarbon(final BlockPos position, final BlockState blockState) {
            super(position, blockState, HeatExchangerTubeType.HARD_CARBON);
        }
    }

    public static class Thermoconducting extends Variant {
        public Thermoconducting(final BlockPos position, final BlockState blockState) {
            super(position, blockState, HeatExchangerTubeType.THERMOCONDUCTING);
        }
    }

    public void setTubeSettings(@Nonnull HeatExchangerTubeSetting[] settings) {
        System.arraycopy(settings, 0, this.settings, 0, 6);
    }

    public HeatExchangerTubeSetting getTubeSetting(@Nonnull Direction side) {
        return settings[side.ordinal()];
    }

    public void setTubeSetting(@Nonnull Direction side, HeatExchangerTubeSetting setting) {
        settings[side.ordinal()] = setting;

        level.setBlock(worldPosition, getBlockState().setValue(switch (side) {
            case DOWN -> HeatExchangerTubeBlock.DOWN;
            case UP -> HeatExchangerTubeBlock.UP;
            case NORTH -> HeatExchangerTubeBlock.NORTH;
            case SOUTH -> HeatExchangerTubeBlock.SOUTH;
            case WEST -> HeatExchangerTubeBlock.WEST;
            case EAST -> HeatExchangerTubeBlock.EAST;
        }, setting), 2);
    }

    public void setTubeSettingOpen(@Nonnull Direction side, boolean open) {
        setTubeSetting(side, HeatExchangerTubeSetting.of(open, settings[side.ordinal()].isBaffle()));
    }

    public void setTubeSettingBaffle(@Nonnull Direction side, boolean baffle) {
        setTubeSetting(side, HeatExchangerTubeSetting.of(settings[side.ordinal()].isOpen(), baffle));
    }

    public void toggleTubeSetting(@Nonnull Direction side) {
        setTubeSetting(side, getTubeSetting(side).next());
        markDirtyAndNotify(true);
    }

    // IMultitoolLogic

    @Override
    public boolean onUseMultitool(ItemStack multitool, ServerPlayer player, Level level, Direction facing, BlockPos hitPos) {
        if (!isMachineAssembled()) {
            boolean opposite = player.isCrouching();
            Direction side = opposite ? facing.getOpposite() : facing;
            toggleTubeSetting(side);

            HeatExchangerTubeSetting setting = getTubeSetting(side);
            if (level.getBlockEntity(worldPosition.relative(side)) instanceof HeatExchangerTubeEntity other) {
                other.setTubeSetting(side.getOpposite(), setting);
                other.markDirtyAndNotify(true);
            }
            player.sendSystemMessage(Component.translatable(opposite ? MODID + ".message.multitool.fluid_toggle_opposite" : MODID + ".message.multitool.fluid_toggle", Component.translatable(MODID + ".tooltip.exchanger_tube_fluid_side." + setting)).withStyle(setting.getTextColor()));
            return true;
        }
        return super.onUseMultitool(multitool, player, level, facing, hitPos);
    }

    // IRayTraceLogic

    @Override
    @OnlyIn(Dist.CLIENT)
    public void onPlayerMouseOver(LocalPlayer player, PoseStack poseStack, Vec3 projectedView, Direction side, float partialTicks) {
        if (side == null) {
            return;
        }

        if (!MultitoolItem.isMultitool(player.getMainHandItem())) {
            return;
        }

        poseStack.pushPose();

        Direction dir = (player.isCrouching() ? side.getOpposite() : side);
        VoxelShape shape = this.getTubeSetting(dir).isBaffle() ? SIDE_BAFFLE_SHAPE.get(dir) : SIDE_SHAPE.get(dir);

        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        LevelRenderer.renderVoxelShape(poseStack, Minecraft.getInstance().renderBuffers().bufferSource().getBuffer(RenderType.LINES), shape, worldPosition.getX() - projectedView.x, worldPosition.getY() - projectedView.y, worldPosition.getZ() - projectedView.z, 1f, 0, 0, 1f, false);
        RenderSystem.enableDepthTest();
        RenderSystem.depthMask(true);
        poseStack.popPose();
    }

    // NBT

    @Override
    public CompoundTag writeAll(CompoundTag nbt, HolderLookup.Provider registries) {
        super.writeAll(nbt, registries);

        byte[] byteSettings = new byte[6];
        for (int i = 0; i < 6; ++i) {
            byteSettings[i] = (byte) settings[i].ordinal();
        }
        nbt.putByteArray("settings", byteSettings);

        nbt.putBoolean("nullTubeFlow", tubeFlow == null);
        if (tubeFlow != null) {
            nbt.putDouble("tubeFlowX", tubeFlow.x);
            nbt.putDouble("tubeFlowY", tubeFlow.y);
            nbt.putDouble("tubeFlowZ", tubeFlow.z);
        }

        nbt.putBoolean("nullShellFlow", shellFlow == null);
        if (shellFlow != null) {
            nbt.putDouble("shellFlowX", shellFlow.x);
            nbt.putDouble("shellFlowY", shellFlow.y);
            nbt.putDouble("shellFlowZ", shellFlow.z);
        }

        return nbt;
    }

    @Override
    public void readAll(CompoundTag nbt, HolderLookup.Provider registries) {
        super.readAll(nbt, registries);

        if (nbt.contains("settings")) {
            settings = new HeatExchangerTubeSetting[6];
            byte[] byteSettings = nbt.getByteArray("settings");
            for (int i = 0; i < 6; ++i) {
                settings[i] = HeatExchangerTubeSetting.values()[byteSettings[i]];
            }
        }

        tubeFlow = nbt.getBoolean("nullTubeFlow") ? null : new Vec3(nbt.getDouble("tubeFlowX"), nbt.getDouble("tubeFlowY"), nbt.getDouble("tubeFlowZ"));
        shellFlow = nbt.getBoolean("nullShellFlow") ? null : new Vec3(nbt.getDouble("shellFlowX"), nbt.getDouble("shellFlowY"), nbt.getDouble("shellFlowZ"));
    }
}