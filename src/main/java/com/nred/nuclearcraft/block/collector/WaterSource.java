package com.nred.nuclearcraft.block.collector;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.nred.nuclearcraft.recipe.CollectorRecipe;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
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

import static com.nred.nuclearcraft.registration.BlockEntityRegistration.WATER_SOURCE_TYPES;

public class WaterSource extends Collector<WaterSource> {
    public WaterSource(Properties properties, MACHINE_LEVEL level) {
        super(properties, level);
    }

    MapCodec<WaterSource> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(propertiesCodec(), Codec.STRING.fieldOf("level").xmap(MACHINE_LEVEL::valueOf, Enum::name).forGetter(machine -> machine.level)
            ).apply(instance, WaterSource::new)
    );

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new WaterSourceEntity(pos, state, level);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        if (level.isClientSide) return null;
        return createTickerHelper(blockEntityType, WATER_SOURCE_TYPES.get(this.level).get(), (level1, pos, state1, blockEntity) -> blockEntity.tick(level1, pos, state1, blockEntity));
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        if (context.level() == null) return;

        CollectorRecipe recipe = ((CollectorRecipe) context.level().getRecipeManager().byKey(ResourceLocation.parse(stack.getItem() + "_rate")).get().value());
        tooltipComponents.add(Component.translatable("tooltip.collector", recipe.rate(), recipe.fluidResult().getHoverName()).withColor(ChatFormatting.AQUA.getColor()));
    }
}