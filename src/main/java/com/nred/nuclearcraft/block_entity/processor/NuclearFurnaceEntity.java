package com.nred.nuclearcraft.block_entity.processor;

import com.nred.nuclearcraft.helpers.RecipeHelpers;
import com.nred.nuclearcraft.menu.processor.NuclearFurnaceMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.Tags;

import java.util.Optional;

import static com.nred.nuclearcraft.NuclearcraftNeohaul.MODID;
import static com.nred.nuclearcraft.registration.BlockEntityRegistration.NUCLEAR_FURNACE_ENTITY_TYPE;

public class NuclearFurnaceEntity extends AbstractFurnaceBlockEntity { //ITile
//    private final IRadiationSource radiation; TODO

    public NuclearFurnaceEntity(BlockPos pos, BlockState blockState) {
        super(NUCLEAR_FURNACE_ENTITY_TYPE.get(), pos, blockState, RecipeType.SMELTING);
//        radiation = new RadiationSource(0D);
        this.quickCheck = createCheck(RecipeType.SMELTING);

    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable(MODID + ".menu.title.nuclear_furnace");
    }

    @Override
    protected AbstractContainerMenu createMenu(int containerId, Inventory player) {
        return new NuclearFurnaceMenu(containerId, player, this, this.dataAccess);
    }

    @Override
    protected int getBurnDuration(ItemStack fuel) {
        return burnDuration(fuel);
    }

    public static int burnDuration(ItemStack fuel) {
        if (fuel.isEmpty()) {
            return 0;
        } else if (Ingredient.of(RecipeHelpers.tag(Tags.Items.STORAGE_BLOCKS, "uranium")).test(fuel)) {
            return 3200;
        } else if (Ingredient.of(RecipeHelpers.tag(Tags.Items.INGOTS, "uranium")).test(fuel)) {
            return 320;
        } else if (Ingredient.of(RecipeHelpers.tag(Tags.Items.DUSTS, "uranium")).test(fuel)) {
            return 320;
        } else {
            return 0;
        }
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, NuclearFurnaceEntity blockEntity) {
        if (blockEntity.isLit()) {
//            blockEntity.getRadiationSource().setRadiationLevel(RadSources.LEU_235_FISSION);
        } else {
//            blockEntity.getRadiationSource().setRadiationLevel(0D);
        }
        AbstractFurnaceBlockEntity.serverTick(level, pos, state, blockEntity);
    }

    public static <I extends RecipeInput, T extends Recipe<I>> RecipeManager.CachedCheck<I, T> createCheck(final RecipeType<T> recipeType) {
        return new RecipeManager.CachedCheck<>() {
            final RecipeManager.CachedCheck<I, T> proxy = RecipeManager.createCheck(recipeType);

            @Override
            public Optional<RecipeHolder<T>> getRecipeFor(I input, Level level) {
                Optional<RecipeHolder<T>> holder = proxy.getRecipeFor(input, level);
                if (holder.isPresent()) {
                    SmeltingRecipe smeltingRecipe = (SmeltingRecipe) holder.get().value();
                    return Optional.of(new RecipeHolder<>(holder.get().id(), (T) new SmeltingRecipe(smeltingRecipe.getGroup(), smeltingRecipe.category(), smeltingRecipe.getIngredients().getFirst(), smeltingRecipe.getResultItem(null), smeltingRecipe.getExperience(), 20)));
                }
                return holder;
            }
        };
    }


    @Override
    public boolean canPlaceItem(int index, ItemStack stack) {
        if (index == 2) {
            return false;
        } else if (index != 1) {
            return true;
        } else {
            return getBurnDuration(stack) > 0;
        }
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        readRadiation(tag, registries);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        writeRadiation(tag, registries);
    }

    public void readRadiation(CompoundTag tag, HolderLookup.Provider registries) {
        if (tag.contains("radiationLevel")) {
//            getRadiationSource().setRadiationLevel(nbt.getDouble("radiationLevel")); // TODO
        }
    }

    public CompoundTag writeRadiation(CompoundTag tag, HolderLookup.Provider registries) {
//        tag.putDouble("radiationLevel", getRadiationSource().getRadiationLevel());
        return tag;
    }

//    @Override
//    public IRadiationSource getRadiationSource() {
//        return radiation;
//    }
}