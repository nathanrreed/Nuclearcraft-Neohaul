package com.nred.nuclearcraft.block_entity.fission.port;

import com.nred.nuclearcraft.block_entity.fission.SaltFissionHeaterEntity;
import com.nred.nuclearcraft.menu.multiblock.port.FissionHeaterPortMenu;
import com.nred.nuclearcraft.multiblock.fisson.molten_salt.FissionCoolantHeaterPortType;
import com.nred.nuclearcraft.recipe.NCRecipes;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;

import static com.nred.nuclearcraft.NuclearcraftNeohaul.MODID;
import static com.nred.nuclearcraft.registration.BlockEntityRegistration.FISSION_ENTITY_TYPE;
import static com.nred.nuclearcraft.util.FluidStackHelper.INGOT_BLOCK_VOLUME;

public class FissionHeaterPortEntity extends FissionFluidPortEntity<FissionHeaterPortEntity, SaltFissionHeaterEntity> {
    private final FissionCoolantHeaterPortType heaterPortType;

    public FissionHeaterPortEntity(final BlockPos pos, final BlockState blockState, FissionCoolantHeaterPortType heaterPortType) {
        super(FISSION_ENTITY_TYPE.get("coolant_heater_port").get(), pos, blockState, "fission_heater_port", FissionHeaterPortEntity.class, INGOT_BLOCK_VOLUME, e -> Collections.singleton(heaterPortType.getCoolantId()), NCRecipes.coolant_heater);
        this.heaterPortType = heaterPortType;
    }

    // MenuProvider

    @Override
    public @Nullable AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        return new FissionHeaterPortMenu(containerId, playerInventory, this);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable(MODID + ".menu.fission_heater_port.title");
    }

    @Override
    public boolean canOpenGui(Level world, BlockPos position, BlockState state) {
        return true;
    }

    @Override
    public Object getFilterKey() {
        return heaterPortType.getName();
    }
}