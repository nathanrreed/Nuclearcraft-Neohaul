package com.nred.nuclearcraft.block_entity.turbine;

import com.nred.nuclearcraft.multiblock.turbine.ITurbinePartType;
import com.nred.nuclearcraft.multiblock.turbine.ITurbinePart;
import com.nred.nuclearcraft.block_entity.TilePartAbstract;
import com.nred.nuclearcraft.multiblock.turbine.Turbine;
import it.zerono.mods.zerocore.lib.block.multiblock.IMultiblockPartTypeProvider;
import it.zerono.mods.zerocore.lib.multiblock.cuboid.PartPosition;
import it.zerono.mods.zerocore.lib.multiblock.validation.IMultiblockValidator;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class AbstractTurbineEntity extends TilePartAbstract<Turbine> implements IMultiblockPartTypeProvider<Turbine, ITurbinePartType>, ITurbinePart {
    public AbstractTurbineEntity(final BlockEntityType<?> type, final BlockPos position, final BlockState blockState) {
        super(type, position, blockState);
    }

    @Override
    public @NotNull Turbine createController() {
        final Level myWorld = this.getLevel();

        if (null == myWorld) {
            throw new RuntimeException("Trying to create a Controller from a Part without a Level");
        }

        return new Turbine(this.getLevel());
    }
    public boolean isTransparent() {
        return false;
    }

    @Override
    public boolean isGoodForPosition(PartPosition position, IMultiblockValidator validatorCallback) {
        if (position.isFace()) {
            if (isTransparent() && getMultiblockController().isPresent()) {
                getMultiblockController().get().shouldSpecialRenderRotor = true;
            }
        }
        return super.isGoodForPosition(position, validatorCallback);
    }

    @Override
    public @NotNull Class<Turbine> getControllerType() {
        return Turbine.class;
    }
}