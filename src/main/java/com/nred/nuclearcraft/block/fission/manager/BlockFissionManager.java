package com.nred.nuclearcraft.block.fission.manager;

import com.nred.nuclearcraft.block.GenericDirectionalTooltipDeviceBlock;
import com.nred.nuclearcraft.block.IActivatable;
import com.nred.nuclearcraft.block_entity.fission.manager.FissionManagerEntity;
import com.nred.nuclearcraft.block_entity.fission.manager.IFissionManagerListener;
import it.zerono.mods.zerocore.lib.block.multiblock.IMultiblockPartType;
import it.zerono.mods.zerocore.lib.multiblock.IMultiblockController;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import org.jetbrains.annotations.Nullable;

import static com.nred.nuclearcraft.registration.BlockRegistration.ACTIVE;
import static com.nred.nuclearcraft.registration.BlockRegistration.FACING_ALL;

public abstract class BlockFissionManager<Controller extends IMultiblockController<Controller>, PartType extends IMultiblockPartType, MANAGER extends FissionManagerEntity<MANAGER, LISTENER>, LISTENER extends IFissionManagerListener<MANAGER, LISTENER>> extends GenericDirectionalTooltipDeviceBlock<Controller, PartType> implements IActivatable {
    protected final Class<MANAGER> managerClass;

    public BlockFissionManager(MultiblockPartProperties<PartType> properties, Class<MANAGER> managerClass) {
        super(properties);
        this.managerClass = managerClass;
        this.registerDefaultState(this.defaultBlockState().setValue(FACING_ALL, Direction.NORTH).setValue(ACTIVE, false));
    }


    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING_ALL, context.getNearestLookingDirection()).setValue(ACTIVE, false);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(ACTIVE, FACING_ALL);
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }
}