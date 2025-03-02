package com.nred.nuclearcraft.block.processor.electric_furnace;

import com.nred.nuclearcraft.block.processor.ProcessorEntity;
import com.nred.nuclearcraft.helpers.HandlerInfo;
import com.nred.nuclearcraft.menu.ElectricFurnaceMenu;
import com.nred.nuclearcraft.menu.ProcessorInfo;
import com.nred.nuclearcraft.recipe.processor.ElectricFurnaceRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.crafting.SizedIngredient;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

import static com.nred.nuclearcraft.menu.ProcessorMenu.ENERGY;

public class ElectricFurnaceEntity extends ProcessorEntity {
    public ElectricFurnaceEntity(BlockPos pos, BlockState blockState) {
        super(pos, blockState, "electric_furnace", new HandlerInfo(2, 0, 1, 0));
    }

    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        return new ElectricFurnaceMenu(containerId, playerInventory, ContainerLevelAccess.create(level, worldPosition), new ProcessorInfo(worldPosition, redstoneMode, itemStackHandler, fluidHandler, "electric_furnace"), this.progressSlot);
    }

    @Override
    public boolean hasRecipe() { // Converts a vanilla furnace recipe
        RecipeHolder<SmeltingRecipe> temp = level.getRecipeManager().getRecipeFor(RecipeType.SMELTING, new SingleRecipeInput(itemStackHandler.getStackInSlot(ENERGY + 1)), level).orElse(null);
        if (temp != null) {
            recipe = new RecipeHolder<>(temp.id(), new ElectricFurnaceRecipe(temp.value().getIngredients().stream().map(input -> new SizedIngredient(input, 1)).toList(), List.of(SizedIngredient.of(temp.value().getResultItem(null).getItem(), temp.value().getResultItem(null).getCount())), List.of(), List.of(), 1, 1));
        } else {
            recipe = null;
        }
        return recipe != null;
    }

    @Override
    public boolean validSlot(@NotNull ItemStack stack) {
        return level.getRecipeManager().getAllRecipesFor(RecipeType.SMELTING).stream().flatMap(holder -> holder.value().getIngredients().stream()).flatMap(a -> Arrays.stream(a.getItems())).parallel().anyMatch(itemStack -> itemStack.is(stack.getItem()));
    }
}