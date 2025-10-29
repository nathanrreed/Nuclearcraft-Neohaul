package com.nred.nuclearcraft.block_entity.hx;

import com.nred.nuclearcraft.multiblock.hx.HeatExchangerLogic;
import it.zerono.mods.zerocore.lib.multiblock.cuboid.PartPosition;
import it.zerono.mods.zerocore.lib.multiblock.validation.IMultiblockValidator;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import static com.nred.nuclearcraft.registration.BlockEntityRegistration.HX_ENTITY_TYPE;

public class HeatExchangerRedstonePortEntity extends AbstractHeatExchangerEntity {
    public HeatExchangerRedstonePortEntity(final BlockPos position, final BlockState blockState) {
        super(HX_ENTITY_TYPE.get("redstone_port").get(), position, blockState);
    }

    @Override
    public boolean isGoodForPosition(PartPosition position, IMultiblockValidator validatorCallback) {
        return position.isFace();
    }

    @Override
    public void onBlockNeighborChanged(BlockState state, Level worldIn, BlockPos posIn, BlockPos fromPos) {
        super.onBlockNeighborChanged(state, worldIn, posIn, fromPos);
        setActivity(getIsRedstonePowered());

        HeatExchangerLogic logic = getLogic();
        if (logic != null) {
            logic.setIsExchangerOn();
        }
    }
}