package com.nred.nuclearcraft.block_entity.turbine;

import com.nred.nuclearcraft.multiblock.turbine.TurbineLogic;
import it.zerono.mods.zerocore.lib.multiblock.cuboid.PartPosition;
import it.zerono.mods.zerocore.lib.multiblock.validation.IMultiblockValidator;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import static com.nred.nuclearcraft.registration.BlockEntityRegistration.TURBINE_ENTITY_TYPE;

public class TurbineRedstonePortEntity extends AbstractTurbineEntity {
    public TurbineRedstonePortEntity(final BlockPos position, final BlockState blockState) {
        super(TURBINE_ENTITY_TYPE.get("redstone_port").get(), position, blockState);
    }

    @Override
    public boolean isGoodForPosition(PartPosition position, IMultiblockValidator validatorCallback) {
        return position.isFace();
    }

    @Override
    public void onBlockNeighborChanged(BlockState state, Level worldIn, BlockPos posIn, BlockPos fromPos) {
        super.onBlockNeighborChanged(state, worldIn, posIn, fromPos);
        setActivity(getIsRedstonePowered());

        TurbineLogic logic = getLogic();
        if (logic != null) {
            logic.setIsTurbineOn();
        }
    }
}