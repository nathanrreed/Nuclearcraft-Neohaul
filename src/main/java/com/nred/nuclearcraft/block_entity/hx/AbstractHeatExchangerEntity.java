package com.nred.nuclearcraft.block_entity.hx;

import com.nred.nuclearcraft.block_entity.TilePartAbstract;
import com.nred.nuclearcraft.multiblock.hx.HeatExchanger;
import com.nred.nuclearcraft.multiblock.hx.IHeatExchangerPartType;
import it.zerono.mods.zerocore.lib.block.multiblock.IMultiblockPartTypeProvider;
import it.zerono.mods.zerocore.lib.multiblock.cuboid.PartPosition;
import it.zerono.mods.zerocore.lib.multiblock.validation.IMultiblockValidator;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractHeatExchangerEntity extends TilePartAbstract<HeatExchanger> implements IMultiblockPartTypeProvider<HeatExchanger, IHeatExchangerPartType>, IHeatExchangerPart {
    public AbstractHeatExchangerEntity(final BlockEntityType<?> type, final BlockPos position, final BlockState blockState) {
        super(type, position, blockState);
    }

    @Override
    public @NotNull HeatExchanger createController() {
        final Level myWorld = this.getLevel();

        if (null == myWorld) {
            throw new RuntimeException("Trying to create a Controller from a Part without a Level");
        }

        return new HeatExchanger(this.getLevel());
    }

    public boolean isTransparent() {
        return false;
    }

    @Override
    public boolean isGoodForPosition(PartPosition position, IMultiblockValidator validatorCallback) {
        if (position.isFace()) {
            if (isTransparent() && getMultiblockController().isPresent()) {
                getMultiblockController().get().shouldSpecialRender = true;
            }
        }
        return super.isGoodForPosition(position, validatorCallback);
    }

    @Override
    public @NotNull Class<HeatExchanger> getControllerType() {
        return HeatExchanger.class;
    }
}
