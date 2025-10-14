package com.nred.nuclearcraft.handler;

import com.google.common.collect.Lists;
import com.nred.nuclearcraft.NCInfo;
import com.nred.nuclearcraft.recipe.BasicRecipe;
import com.nred.nuclearcraft.recipe.BasicRecipeInput;
import com.nred.nuclearcraft.recipe.RecipeInfo;
import com.nred.nuclearcraft.recipe.fission.FissionModeratorRecipe;
import com.nred.nuclearcraft.recipe.fission.FissionReflectorRecipe;
import com.nred.nuclearcraft.recipe.fission.SolidFissionRecipe;
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

import java.util.Collections;
import java.util.List;
import java.util.function.Function;

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
        BasicRecipe recipe;
//        BasicRecipeInput input = new BasicRecipeInput(List.of(SizedIngredient.of(stack.getItem(), stack.getCount())), List.of());
//        RecipeHolder<FissionModeratorRecipe> recipe = level.getRecipeManager().getAllRecipesFor(FISSION_MODERATOR_RECIPE_TYPE.get()).stream().filter(a -> a.value().matches(input, level)).findFirst().orElse(null);


        Function<BasicRecipeHandler<?>, BasicRecipe> itemRecipe = x -> {
            RecipeInfo<? extends BasicRecipe> recipeInfo = x.getRecipeInfoFromInputs(level, Collections.singletonList(stack), Collections.emptyList());
            return recipeInfo == null ? null : recipeInfo.recipe;
        };

//        recipe = itemRecipe.apply(NCRecipes.machine_diaphragm); TODO
//        if (recipe != null) {
//            InfoHelper.infoFull(tooltip, new ChatFormatting[] {ChatFormatting.UNDERLINE, ChatFormatting.LIGHT_PURPLE, ChatFormatting.RED}, NCInfo.machineDiaphragmFixedInfo(recipe), ChatFormatting.AQUA, NCInfo.machineDiaphragmInfo());
//        }
//
//        recipe = itemRecipe.apply(NCRecipes.machine_sieve_assembly);
//        if (recipe != null) {
//            InfoHelper.infoFull(tooltip, new ChatFormatting[] {ChatFormatting.UNDERLINE, ChatFormatting.LIGHT_PURPLE}, NCInfo.machineSieveAssemblyFixedInfo(recipe), ChatFormatting.AQUA, NCInfo.machineSieveAssemblyInfo());
//        }
//
//        BasicRecipe cathodeRecipe = itemRecipe.apply(NCRecipes.electrolyzer_cathode), anodeRecipe = itemRecipe.apply(NCRecipes.electrolyzer_anode);
//        if (cathodeRecipe != null || anodeRecipe != null) {
//            List<ChatFormatting> fixedColors = Lists.newArrayList(ChatFormatting.UNDERLINE);
//            if (cathodeRecipe != null) {
//                fixedColors.add(ChatFormatting.LIGHT_PURPLE);
//            }
//            if (anodeRecipe != null) {
//                fixedColors.add(ChatFormatting.BLUE);
//            }
//            InfoHelper.infoFull(tooltip, fixedColors.toArray(new ChatFormatting[0]), NCInfo.electrodeFixedInfo(cathodeRecipe, anodeRecipe), ChatFormatting.AQUA, NCInfo.electrodeInfo());
//        }

        recipe = itemRecipe.apply(NCRecipes.fission_moderator);
        if (recipe != null) {
            InfoHelper.infoFull(tooltips, new ChatFormatting[]{ChatFormatting.UNDERLINE, ChatFormatting.GREEN, ChatFormatting.LIGHT_PURPLE}, NCInfo.fissionModeratorFixedInfo((FissionModeratorRecipe) recipe), ChatFormatting.AQUA, NCInfo.fissionModeratorInfo());
        }

        recipe = itemRecipe.apply(NCRecipes.fission_reflector);
        if (recipe != null) {
            InfoHelper.infoFull(tooltips, new ChatFormatting[] {ChatFormatting.UNDERLINE, ChatFormatting.WHITE, ChatFormatting.LIGHT_PURPLE}, NCInfo.fissionReflectorFixedInfo((FissionReflectorRecipe) recipe), ChatFormatting.AQUA, NCInfo.fissionReflectorInfo());
        }

//        recipe = itemRecipe.apply(NCRecipes.pebble_fission); TODO
//        if (recipe != null) {
//            InfoHelper.infoFull(tooltips, new ChatFormatting[] {ChatFormatting.UNDERLINE, ChatFormatting.GREEN, ChatFormatting.YELLOW, ChatFormatting.LIGHT_PURPLE, ChatFormatting.RED, ChatFormatting.GRAY, ChatFormatting.DARK_AQUA}, NCInfo.fissionFuelInfo(recipe));
//        }

        recipe = itemRecipe.apply(NCRecipes.solid_fission);
        if (recipe != null) {
            InfoHelper.infoFull(tooltips, new ChatFormatting[] {ChatFormatting.UNDERLINE, ChatFormatting.GREEN, ChatFormatting.YELLOW, ChatFormatting.LIGHT_PURPLE, ChatFormatting.RED, ChatFormatting.GRAY, ChatFormatting.DARK_AQUA}, NCInfo.fissionFuelInfo((SolidFissionRecipe) recipe));
        }
    }
}