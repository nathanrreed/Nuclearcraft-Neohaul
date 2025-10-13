package com.nred.nuclearcraft.item;

import com.nred.nuclearcraft.config.Config2;
import com.nred.nuclearcraft.block.IMultitoolLogic;
import com.nred.nuclearcraft.util.NBTHelper;
import com.nred.nuclearcraft.util.NCMath;
import com.nred.nuclearcraft.util.SoundHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.HitResult;

import javax.annotation.Nullable;
import java.util.LinkedList;
import java.util.List;

import static com.nred.nuclearcraft.NuclearcraftNeohaul.MODID;

public class MultiToolItem extends Item {
    public MultiToolItem(Properties properties) {
        super(properties.component(DataComponents.CUSTOM_DATA, CustomData.of(newMultitool())));
    }

    public static CompoundTag newMultitool() {
        CompoundTag nbt = new CompoundTag();
        nbt.put("ncMultitool", new CompoundTag());
        return nbt;
    }

    @Override
    public boolean doesSneakBypassUse(ItemStack stack, LevelReader level, BlockPos pos, Player player) {
        return false;
    }

    public static boolean isMultitool(ItemStack stack) {
        return !stack.isEmpty() && stack.getItem() instanceof MultiToolItem;
    }

    @Override
    public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {
        if (isMultitool(stack) && !context.getLevel().isClientSide) {
            BlockEntity tile = context.getLevel().getBlockEntity(context.getClickedPos());
            if (tile instanceof IMultitoolLogic multitoolTile) {
                CompoundTag nbt = NBTHelper.getStackNBT(stack, "ncMultitool");
                if (nbt != null) {
                    boolean multitoolUsed = multitoolTile.onUseMultitool(stack, (ServerPlayer) context.getPlayer(), context.getLevel(), context.getClickedFace(), context.getClickedPos());
                    nbt.putBoolean("multitoolUsed", multitoolUsed);
                    if (multitoolUsed) {
                        multitoolTile.markDirtyAndNotify(true);
                        playUseSound(context.getLevel(), context.getPlayer());
                        return InteractionResult.SUCCESS;
                    }
                }
            }
        }
        return super.onItemUseFirst(stack, context);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        ItemStack stack = player.getItemInHand(usedHand);
        if (isMultitool(stack) && !level.isClientSide) {
            InteractionResultHolder<ItemStack> result = null;
            CompoundTag nbt = NBTHelper.getStackNBT(stack, "ncMultitool");
            if (nbt != null) {
                for (MultitoolRightClickLogic logic : MULTITOOL_RIGHT_CLICK_LOGIC) {
                    result = logic.use(this, level, player, usedHand, stack);
                    if (result != null) {
                        playUseSound(level, player);
                        break;
                    }
                }
                nbt.remove("multitoolUsed");
            }

            if (result != null) {
                return result;
            }
        }
        return super.use(level, player, usedHand);
    }

    public static void playUseSound(Level level, Player player) {
        level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.PLAYERS, 0.5F, player.isCrouching() ? 1F : SoundHelper.getPitch(-1D));
    }

    // Right click logic

    public interface MultitoolRightClickLogic {
        @Nullable
        InteractionResultHolder<ItemStack> use(MultiToolItem itemMultitool, Level level, Player player, InteractionHand usedHand, ItemStack heldItem);
    }

    /**
     * List of all multitool right-click logic. Earlier entries are prioritised!
     */
    public static final List<MultitoolRightClickLogic> MULTITOOL_RIGHT_CLICK_LOGIC = new LinkedList<>();

    public static void registerRightClickLogic() {
        MULTITOOL_RIGHT_CLICK_LOGIC.add((MultiToolItem itemMultitool, Level level, Player player, InteractionHand usedHand, ItemStack heldItem) -> {
            CompoundTag nbt = NBTHelper.getStackNBT(heldItem, "ncMultitool");
            if (nbt != null && !player.isCrouching() && nbt.getString("qComputerGateMode").equals("angle")) {
                double angle = NCMath.roundTo(player.getYRot() + 360D, 360D / Config2.quantum_angle_precision) % 360D;
                nbt.putDouble("qGateAngle", angle);
                player.sendSystemMessage(Component.translatable(MODID + ".multitool.quantum_computer.tool_set_angle", NCMath.decimalPlaces(angle, 5)));
                return new InteractionResultHolder<>(InteractionResult.SUCCESS, heldItem);
            }
            return null;
        });

        MULTITOOL_RIGHT_CLICK_LOGIC.add((MultiToolItem itemMultitool, Level level, Player player, InteractionHand usedHand, ItemStack heldItem) -> {
            CompoundTag nbt = NBTHelper.getStackNBT(heldItem, "ncMultitool");
            if (nbt != null && player.isCrouching() && !nbt.isEmpty() && !nbt.getBoolean("multitoolUsed")) {
                HitResult ray = getPlayerPOVHitResult(level, player, ClipContext.Fluid.NONE);
                if (ray.getType() != HitResult.Type.BLOCK || !(level.getBlockEntity(new BlockPos(new Vec3i(Mth.floor(ray.getLocation().x), Mth.floor(ray.getLocation().y), Mth.floor(ray.getLocation().z)))) instanceof IMultitoolLogic)) {
                    NBTHelper.clearStackNBT(heldItem, "ncMultitool");
                    player.sendSystemMessage(Component.translatable(MODID + ".multitool.clear_info"));
                    return new InteractionResultHolder<>(InteractionResult.SUCCESS, heldItem);
                }
            }
            return null;
        });
    }
}