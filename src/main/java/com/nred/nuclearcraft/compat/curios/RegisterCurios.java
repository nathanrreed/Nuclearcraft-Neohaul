package com.nred.nuclearcraft.compat.curios;

import com.nred.nuclearcraft.item.curios.RadiationBadgeItem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import top.theillusivec4.curios.api.CuriosCapability;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurio;

import static com.nred.nuclearcraft.registration.ItemRegistration.GEIGER_COUNTER;
import static com.nred.nuclearcraft.registration.ItemRegistration.RADIATION_BADGE;

public class RegisterCurios {
    public static void registerCurios(RegisterCapabilitiesEvent event) {
        event.registerItem(CuriosCapability.ITEM, (stack, context) -> new ICurio() {
            @Override
            public ItemStack getStack() {
                return stack;
            }

            @Override
            public void curioTick(SlotContext slotContext) {
                if (slotContext.entity() instanceof Player player)
                    RadiationBadgeItem.updateBadge(stack, player);
            }
        }, RADIATION_BADGE);

        event.registerItem(CuriosCapability.ITEM, (stack, context) -> (ICurio) () -> stack, GEIGER_COUNTER);
    }
}
