package com.nred.nuclearcraft.block;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.MagmaBlock;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

import static com.nred.nuclearcraft.item.TooltipItem.shiftForDetails;
import static com.nred.nuclearcraft.registration.DamageTypeRegistration.CORIUM_BURN;

public class SolidifiedCorium extends MagmaBlock {
    public SolidifiedCorium() {
        super(Properties.of().isValidSpawn((state, level, pos, value) -> false));
    }

    @Override
    public void stepOn(Level level, BlockPos pos, BlockState state, Entity entity) {
        if (entity instanceof LivingEntity) {
            DamageSource corium_burn = new DamageSource(level.registryAccess().lookupOrThrow(Registries.DAMAGE_TYPE).getOrThrow(CORIUM_BURN)) {
                @Override
                public boolean is(TagKey<DamageType> damageTypeKey) {
                    return damageTypeKey == DamageTypeTags.BURN_FROM_STEPPING || damageTypeKey == DamageTypeTags.NO_KNOCKBACK;
                }

                @Override
                public boolean is(ResourceKey<DamageType> damageTypeKey) {
                    return damageTypeKey == DamageTypes.HOT_FLOOR || damageTypeKey == DamageTypes.IN_FIRE;
                }
            };
            entity.hurt(corium_burn, 1f);
        }
        super.stepOn(level, pos, state, entity);
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        if (Screen.hasShiftDown()) {
            tooltipComponents.add(Component.translatable("tooltip.solidified_corium").withStyle(ChatFormatting.AQUA));
        } else {
            tooltipComponents.add(shiftForDetails);
        }
    }
}