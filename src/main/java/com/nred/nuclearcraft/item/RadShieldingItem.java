package com.nred.nuclearcraft.item;

import com.nred.nuclearcraft.NCInfo;
import com.nred.nuclearcraft.capability.radiation.entity.IEntityRads;
import com.nred.nuclearcraft.capability.radiation.resistance.IRadiationResistance;
import com.nred.nuclearcraft.radiation.RadiationHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.ItemHandlerHelper;
import net.neoforged.neoforge.registries.DeferredItem;

import java.util.List;

import static com.nred.nuclearcraft.NuclearcraftNeohaul.MODID;
import static com.nred.nuclearcraft.config.NCConfig.*;
import static com.nred.nuclearcraft.registration.CapabilityRegistration.CAPABILITY_ENTITY_RADS;
import static com.nred.nuclearcraft.registration.CapabilityRegistration.CAPABILITY_RADIATION_RESISTANCE;
import static com.nred.nuclearcraft.registration.ItemRegistration.*;

public class RadShieldingItem extends TooltipItem {
    private final int id;

    public RadShieldingItem(int id) {
        super(new Properties());
        this.id = id;
    }

    private static final Component NOT_HARDCORE = Component.translatable(MODID + ".tooltip.rad_shielding.not_hardcore");

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!radiation_tile_shielding || !player.isCrouching()) {
            return InteractionResultHolder.fail(stack);
        }

        IEntityRads playerRads = player.getCapability(CAPABILITY_ENTITY_RADS, null);
        if (radiation_hardcore_containers <= 0D) {
            if (!level.isClientSide()) {
                if (playerRads != null) {
                    playerRads.setMessageCooldownTime(20);
                }
                player.sendSystemMessage(NOT_HARDCORE);
            }
            return InteractionResultHolder.fail(stack);
        }

        HitResult ray = Minecraft.getInstance().hitResult;
        if (ray == null || ray.getType() != HitResult.Type.BLOCK) {
            return InteractionResultHolder.fail(stack);
        }

        BlockPos pos = ((BlockHitResult) ray).getBlockPos();
        BlockEntity tile = level.getBlockEntity(pos);
        Direction side = ((BlockHitResult) ray).getDirection();

        if (!level.mayInteract(player, pos) || tile == null || level.getCapability(Capabilities.ItemHandler.BLOCK, pos, side) == null && level.getCapability(Capabilities.FluidHandler.BLOCK, pos, side) == null) {
            return InteractionResultHolder.fail(stack);
        }

        IRadiationResistance resistance = level.getCapability(CAPABILITY_RADIATION_RESISTANCE, pos, side);
        if (resistance == null) {
            return InteractionResultHolder.fail(stack);
        }

        double newResistance = radiation_shielding_level[id];
        if (newResistance <= resistance.getShieldingRadResistance()) {
            if (!level.isClientSide()) {
                if (playerRads != null) {
                    playerRads.setMessageCooldownTime(20);
                }
                player.sendSystemMessage(Component.translatable(MODID + ".tooltip.rad_shielding.install_fail", RadiationHelper.resistanceSigFigs(resistance.getShieldingRadResistance())));
            }
            return InteractionResultHolder.fail(stack);
        }

        if (!player.isCreative()) {
            stack.shrink(1);

            List<DeferredItem<Item>> rad_shielding = List.of(LIGHT_RADIATION_SHIELDING, MEDIUM_RADIATION_SHIELDING, HEAVY_RADIATION_SHIELDING);
            for (int i = 2; i >= 0; i--) {
                if (resistance.getShieldingRadResistance() >= radiation_shielding_level[i]) {
                    ItemHandlerHelper.giveItemToPlayer(player, rad_shielding.get(i).toStack());
                    break;
                }
            }
        }

        resistance.setShieldingRadResistance(newResistance);
        tile.setChanged();

        if (!level.isClientSide()) {
            player.sendSystemMessage(Component.translatable(MODID + ".tooltip.rad_shielding.install_success", RadiationHelper.resistanceSigFigs(resistance.getShieldingRadResistance())));
        } else {
            player.playSound(SoundEvents.ANVIL_PLACE, 0.5F, 1F);
        }

        return InteractionResultHolder.success(stack);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        if (tooltipFlag.hasShiftDown()) {
            tooltipComponents.add(NCInfo.radShieldingInfo(id));
        } else {
            tooltipComponents.add(shiftForDetails);
        }
    }
}