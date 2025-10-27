package com.nred.nuclearcraft.block_entity.fission;

import com.nred.nuclearcraft.block_entity.fission.manager.IFissionManagerListener;
import com.nred.nuclearcraft.block_entity.fission.manager.FissionSourceManagerEntity;
import com.nred.nuclearcraft.handler.NCRecipes;
import com.nred.nuclearcraft.multiblock.fisson.FissionReactorLogic;
import com.nred.nuclearcraft.multiblock.fisson.FissionSourceType;
import com.nred.nuclearcraft.recipe.RecipeHelper;
import com.nred.nuclearcraft.recipe.fission.FissionReflectorRecipe;
import com.nred.nuclearcraft.render.BlockHighlightTracker;
import it.zerono.mods.zerocore.lib.multiblock.cuboid.PartPosition;
import it.zerono.mods.zerocore.lib.multiblock.validation.IMultiblockValidator;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nonnull;

import static com.nred.nuclearcraft.NuclearcraftNeohaul.MODID;
import static com.nred.nuclearcraft.config.NCConfig.fission_max_size;
import static com.nred.nuclearcraft.registration.BlockEntityRegistration.FISSION_ENTITY_TYPE;
import static com.nred.nuclearcraft.util.PosHelper.DEFAULT_NON;

public class FissionSourceEntity extends AbstractFissionEntity implements IFissionManagerListener<FissionSourceManagerEntity, FissionSourceEntity> {
    private final FissionSourceType fissionSourceType;

    public boolean isActive = false;
    public Direction facing = Direction.DOWN;

    protected BlockPos managerPos = DEFAULT_NON;
    protected FissionSourceManagerEntity manager = null;

    public FissionSourceEntity(final BlockPos position, final BlockState blockState, FissionSourceType fissionSourceType) {
        super(FISSION_ENTITY_TYPE.get("source").get(), position, blockState);
        this.fissionSourceType = fissionSourceType;
    }

    @Override
    public boolean isGoodForPosition(PartPosition position, IMultiblockValidator validatorCallback) {
        return position.isFace();
    }

    public static class Variant extends FissionSourceEntity {
        protected Variant(final BlockPos position, final BlockState blockState, FissionSourceType fissionSourceType) {
            super(position, blockState, fissionSourceType);
        }
    }

    public static class RadiumBeryllium extends Variant {
        public RadiumBeryllium(final BlockPos position, final BlockState blockState) {
            super(position, blockState, FissionSourceType.RADIUM_BERYLLIUM);
        }
    }

    public static class PoloniumBeryllium extends Variant {
        public PoloniumBeryllium(final BlockPos position, final BlockState blockState) {
            super(position, blockState, FissionSourceType.POLONIUM_BERYLLIUM);
        }
    }

    public static class Californium extends Variant {
        public Californium(final BlockPos position, final BlockState blockState) {
            super(position, blockState, FissionSourceType.CALIFORNIUM);
        }
    }

    @Override
    public @Nonnull PartPosition getPartPosition() {
        PartPosition partPos = super.getPartPosition();
        if (partPos.getDirection().isPresent()) {
            facing = partPos.getDirection().get();
        }
        return partPos;
    }

    @Override
    public int[] weakSidesToCheck(Level worldIn, BlockPos posIn) {
        return new int[]{2, 3, 4, 5};
    }

    @Override
    public void onBlockNeighborChanged(BlockState state, Level levelIn, BlockPos posIn, BlockPos fromPos) {
        boolean wasActive = isActive;
        super.onBlockNeighborChanged(state, levelIn, posIn, fromPos);
        isActive = isSourceActive();
        setActivity(isActive);
        if (!levelIn.isClientSide && wasActive != isActive) {
            FissionReactorLogic logic = getLogic();
            if (logic != null) {
                logic.onSourceUpdated(this);
            }
        }
    }

    public boolean isSourceActive() {
        return (manager != null && manager.isManagerActive()) || getIsRedstonePowered();
    }

    public PrimingTargetInfo getPrimingTarget(boolean checkUpdate, boolean simulate) {
        Direction posFacing = getPartPosition().getDirection().orElse(null);
        if (posFacing == null) {
            posFacing = facing;
            if (posFacing == null) {
                return null;
            }
        }
        Direction dir = posFacing.getOpposite();
        for (int i = 1; i <= fission_max_size; ++i) {
            BlockPos offPos = worldPosition.relative(dir, i);
            FissionReflectorRecipe blockRecipe = (FissionReflectorRecipe) RecipeHelper.blockRecipe(NCRecipes.fission_reflector, level, offPos);
            if (blockRecipe != null && blockRecipe.getFissionReflectorReflectivity() >= 1D) {
                return null;
            }

            IFissionComponent component = getMultiblockController().get().getPartMap(IFissionComponent.class).get(offPos.asLong());
            // First check if source is blocked by a flux sink
            if (component != null && component.isNullifyingSources(posFacing, simulate)) {
                return null;
            }

            if (component instanceof IFissionFuelComponent fuelComponent && fuelComponent.isAcceptingFlux(posFacing, simulate)) {
                boolean newSourceEfficiency;
                if (checkUpdate) {
                    double oldSourceEfficiency = fuelComponent.getSourceEfficiency();
                    fuelComponent.setSourceEfficiency(fissionSourceType.getEfficiency(), true);
                    newSourceEfficiency = oldSourceEfficiency != fuelComponent.getSourceEfficiency();
                } else {
                    newSourceEfficiency = false;
                }
                return new PrimingTargetInfo(fuelComponent, newSourceEfficiency);
            }
        }
        return null;
    }

    public static class PrimingTargetInfo {
        public final IFissionFuelComponent fuelComponent;
        public final boolean newSourceEfficiency;

        PrimingTargetInfo(IFissionFuelComponent fuelComponent, boolean newSourceEfficiency) {
            this.fuelComponent = fuelComponent;
            this.newSourceEfficiency = newSourceEfficiency;
        }
    }
    // IFissionManagerListener

    @Override
    public BlockPos getManagerPos() {
        return managerPos;
    }

    @Override
    public void setManagerPos(BlockPos pos) {
        managerPos = pos;
    }

    @Override
    public FissionSourceManagerEntity getManager() {
        return manager;
    }

    @Override
    public void setManager(FissionSourceManagerEntity manager) {
        this.manager = manager;
    }

    @Override
    public boolean onManagerRefresh(FissionSourceManagerEntity manager) {
        this.manager = manager;
        if (manager != null) {
            managerPos = manager.getBlockPos();
            boolean wasActive = isActive;
            isActive = isSourceActive();
            if (wasActive != isActive) {
                setActivity(isActive);
                return true;
            }
        } else {
            managerPos = DEFAULT_NON;
        }
        return false;
    }

    @Override
    public String getManagerType() {
        return "fissionSourceManager";
    }

    @Override
    public Class<FissionSourceManagerEntity> getManagerClass() {
        return FissionSourceManagerEntity.class;
    }

    // IMultitoolLogic

    @Override
    public boolean onUseMultitool(ItemStack multitool, ServerPlayer player, Level level, Direction facing, BlockPos hitPos) {
        if (IFissionManagerListener.super.onUseMultitool(multitool, player, level, facing, hitPos)) {
            return true;
        }
        if (!player.isCrouching()) {
            PrimingTargetInfo targetInfo = getPrimingTarget(false, true);
            if (targetInfo == null) {
                player.sendSystemMessage(Component.translatable(MODID + ".fission_reactor_source.no_target"));
            } else {
                IFissionFuelComponent fuelComponent = targetInfo.fuelComponent;
                BlockPos pos = fuelComponent.getTilePos();
                BlockHighlightTracker.sendPacket(player, pos, 5000);
                player.sendSystemMessage((Component.translatable(MODID + ".fission_reactor_source.target", pos.getX(), pos.getY(), pos.getZ(), fuelComponent.getTileBlockDisplayName())));
            }
            return true;
        }
        return super.onUseMultitool(multitool, player, level, facing, hitPos);
    }

    // NBT

    @Override
    public CompoundTag writeAll(CompoundTag nbt, HolderLookup.Provider registries) {
        super.writeAll(nbt, registries);
        nbt.putInt("facing", facing.ordinal());

        nbt.putBoolean("isSourceActive", isActive);
        nbt.putLong("managerPos", managerPos.asLong());
        return nbt;
    }

    @Override
    public void readAll(CompoundTag nbt, HolderLookup.Provider registries) {
        super.readAll(nbt, registries);

        facing = Direction.from3DDataValue(nbt.getInt("facing"));

        isActive = nbt.getBoolean("isSourceActive");
        managerPos = BlockPos.of(nbt.getLong("managerPos"));
    }
}