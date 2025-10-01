package com.nred.nuclearcraft.block.turbine;

import com.nred.nuclearcraft.NuclearcraftNeohaul;
import com.nred.nuclearcraft.multiblock.turbine.Turbine;
import it.zerono.mods.zerocore.lib.block.multiblock.IMultiblockPartTypeProvider;
import it.zerono.mods.zerocore.lib.multiblock.cuboid.AbstractCuboidMultiblockPart;
import it.zerono.mods.zerocore.lib.multiblock.cuboid.PartPosition;
import it.zerono.mods.zerocore.lib.multiblock.validation.IMultiblockValidator;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class AbstractTurbineEntity extends AbstractCuboidMultiblockPart<Turbine> implements IMultiblockPartTypeProvider<Turbine, ITurbinePartType> {

    public AbstractTurbineEntity(final BlockEntityType<?> type, final BlockPos position, final BlockState blockState) {
        super(type, position, blockState);
    }

//    protected boolean isTurbineActive() {
//        return this.getMultiblockController()
//                .filter(TurbineMultiblock::isAssembled)
//                .map(TurbineMultiblock::isMachineActive)
//                .orElse(false);
//    }
//
//    protected void setTurbineActive(boolean active) {
//        this.getMultiblockController()
//                .filter(TurbineMultiblock::isAssembled)
//                .ifPresent(c -> c.setMachineActive(active));
//    }
//
//    public Component getPartDisplayName() {
//        return Component.translatable("gui.bigreactors.multiblock_variant_part_format.title",

    /// /                Component.translatable(this.getMultiblockVariant().map(IMultiblockVariant::getTranslationKey).orElse("unknown")),
//                Component.translatable(this.getPartType().map(ITurbinePartType::getTranslationKey).orElse("unknown")));
//    }

    //region client render support
//
//    @Override
//    protected ModelData getUpdatedModelData() {
//        return ModelData.EMPTY; //CodeHelper.optionalMap(this.getMultiblockVariant(), this.getPartType(), this::getUpdatedModelData).orElse(ModelData.EMPTY);
//    }

//    protected int getUpdatedModelVariantIndex() {
//        return 0;
//    }

    //endregion
    //region IDebuggable

//    @Override
//    public void getDebugMessages(LogicalSide side, IDebugMessages messages) {
//
//        super.getDebugMessages(side, messages);
//        messages.addUnlocalized("Model Variant Index: %d", this.getUpdatedModelVariantIndex());
//    }

    //endregion
    @Override
    public boolean isGoodForPosition(PartPosition position, IMultiblockValidator validatorCallback) {
        // Most Reactor parts are not allowed on the Frame an inside the Reactor so reject those positions and allow all the other ones

        final BlockPos coordinates = this.getWorldPosition();

        if (position.isFrame()) {
            validatorCallback.setLastError(coordinates, NuclearcraftNeohaul.MODID + ".multiblock.validation.reactor.invalid_frame_block");
            return false;
        } else if (PartPosition.Interior == position) {
            validatorCallback.setLastError(coordinates, NuclearcraftNeohaul.MODID + ".multiblock.validation.reactor.invalid_part_for_interior");
            return false;
        }

        return true;
    }

    @Override
    public @NotNull Turbine createController() {
        final Level myWorld = this.getLevel();

        if (null == myWorld) {
            throw new RuntimeException("Trying to create a Controller from a Part without a Level");
        }

        return new Turbine(this.getLevel());
    }

    @Override
    public @NotNull Class<Turbine> getControllerType() {
        return Turbine.class;
    }

    @Override
    public void onMachineActivated() {
    }

    @Override
    public void onMachineDeactivated() {
    }
}