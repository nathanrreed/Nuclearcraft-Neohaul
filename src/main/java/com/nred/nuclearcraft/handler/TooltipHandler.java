package com.nred.nuclearcraft.handler;

import com.nred.nuclearcraft.NCInfo;
import com.nred.nuclearcraft.recipe.BasicRecipeInput;
import com.nred.nuclearcraft.recipe.fission.FissionModeratorRecipe;
import com.nred.nuclearcraft.util.InfoHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.crafting.SizedIngredient;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;

import java.util.List;

import static com.nred.nuclearcraft.registration.RecipeTypeRegistration.FISSION_MODERATOR_RECIPE_TYPE;

public class TooltipHandler {
    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent(priority = EventPriority.HIGH)
    public void addAdditionalTooltips(ItemTooltipEvent event) {
        List<Component> tooltip = event.getToolTip();
        ItemStack stack = event.getItemStack();

//        addPlacementRuleTooltip(tooltip, stack);
        if (event.getContext().level() != null)
            addRecipeTooltip(tooltip, stack, event.getContext().level());

//        if (radiation_enabled_public) {
//            addArmorRadiationTooltip(tooltip, stack);
//            addRadiationTooltip(tooltip, stack);
//            addFoodRadiationTooltip(tooltip, stack);
//        }
    }

    @OnlyIn(Dist.CLIENT)
    private static void addRecipeTooltip(List<Component> tooltips, ItemStack stack, Level level) {
        BasicRecipeInput input = new BasicRecipeInput(List.of(SizedIngredient.of(stack.getItem(), stack.getCount())), List.of());
        RecipeHolder<FissionModeratorRecipe> recipe = level.getRecipeManager().getAllRecipesFor(FISSION_MODERATOR_RECIPE_TYPE.get()).stream().filter(a -> a.value().matches(input, level)).findFirst().orElse(null);

        if (recipe != null)
            InfoHelper.infoFull(tooltips, new ChatFormatting[]{ChatFormatting.UNDERLINE, ChatFormatting.GREEN, ChatFormatting.LIGHT_PURPLE}, NCInfo.fissionModeratorFixedInfo(recipe.value()), ChatFormatting.AQUA, NCInfo.fissionModeratorInfo());
    }
}