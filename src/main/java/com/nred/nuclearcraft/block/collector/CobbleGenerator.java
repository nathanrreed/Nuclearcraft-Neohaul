package com.nred.nuclearcraft.block.collector;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.nred.nuclearcraft.recipe.CollectorRecipe;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Containers;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static com.nred.nuclearcraft.registration.BlockEntityRegistration.COBBLE_GENERATOR_TYPES;

public class CobbleGenerator extends Collector<CobbleGenerator> {
    public CobbleGenerator(Properties properties, MACHINE_LEVEL level) {
        super(properties, level);
    }

    MapCodec<CobbleGenerator> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(propertiesCodec(), Codec.STRING.fieldOf("level").xmap(MACHINE_LEVEL::valueOf, Enum::name).forGetter(machine -> machine.level)
            ).apply(instance, CobbleGenerator::new)
    );

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new CobbleGeneratorEntity(pos, state, level);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        if (level.isClientSide) return null;
        return createTickerHelper(blockEntityType, COBBLE_GENERATOR_TYPES.get(this.level).get(), (level1, pos, state1, blockEntity) -> blockEntity.tick(level1, pos, state1, blockEntity));
    }

    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        if (state.getBlock() != newState.getBlock()) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof CobbleGeneratorEntity entity) {
                SimpleContainer inventory = new SimpleContainer((int) Math.ceil(entity.itemStackHandler.getStackInSlot(0).getCount() / 64.0));
                ItemStack stack = entity.itemStackHandler.internalExtractItem(0, 64, false);
                while (!stack.isEmpty()) {
                    inventory.addItem(stack);
                    stack = entity.itemStackHandler.internalExtractItem(0, 64, false);
                }
                Containers.dropContents(level, pos, inventory);
            }
        }
        super.onRemove(state, level, pos, newState, movedByPiston);
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        if (context.level() == null) return;
        tooltipComponents.add(Component.translatable("tooltip.cobblestone_generator", ((CollectorRecipe) context.level().getRecipeManager().byKey(ResourceLocation.parse(stack.getItem() + "_rate")).get().value()).rate()).withColor(ChatFormatting.AQUA.getColor()));
    }
}