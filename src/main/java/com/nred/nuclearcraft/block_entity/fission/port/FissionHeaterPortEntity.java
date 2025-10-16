package com.nred.nuclearcraft.block_entity.fission.port;

import com.nred.nuclearcraft.block_entity.fission.SaltFissionHeaterEntity;
import com.nred.nuclearcraft.handler.NCRecipes;
import com.nred.nuclearcraft.menu.multiblock.port.FissionHeaterPortMenu;
import com.nred.nuclearcraft.multiblock.fisson.molten_salt.FissionCoolantHeaterPortType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;

import static com.nred.nuclearcraft.registration.BlockEntityRegistration.FISSION_ENTITY_TYPE;
import static com.nred.nuclearcraft.util.FluidStackHelper.INGOT_BLOCK_VOLUME;

public class FissionHeaterPortEntity extends FissionFluidPortEntity<FissionHeaterPortEntity, SaltFissionHeaterEntity> {
    private final FissionCoolantHeaterPortType heaterPortType;
    protected ResourceKey<Fluid> coolantName;

    public FissionHeaterPortEntity(final BlockPos pos, final BlockState blockState, FissionCoolantHeaterPortType heaterPortType) {
        super(FISSION_ENTITY_TYPE.get("coolant_heater_port").get(), pos, blockState, "fission_heater_port", FissionHeaterPortEntity.class, INGOT_BLOCK_VOLUME, null, NCRecipes.coolant_heater);
        this.heaterPortType = heaterPortType;
        tanks.get(0).setAllowedFluids(Collections.singleton(heaterPortType.getCoolantId()));
    }

    // MenuProvider

    @Override
    public @Nullable AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        return new FissionHeaterPortMenu(containerId, playerInventory, this);
    }

    @Override
    public boolean canOpenGui(Level world, BlockPos position, BlockState state) {
        return true;
    }

    public static class Variant extends FissionHeaterPortEntity {
        protected Variant(final BlockPos position, final BlockState blockState, FissionCoolantHeaterPortType dynamoCoilType) {
            super(position, blockState, dynamoCoilType);
        }
    }

    public static class Standard extends FissionHeaterPortEntity.Variant {
        public Standard(final BlockPos position, final BlockState blockState) {
            super(position, blockState, FissionCoolantHeaterPortType.STANDARD);
        }
    }

    public static class Iron extends FissionHeaterPortEntity.Variant {
        public Iron(final BlockPos position, final BlockState blockState) {
            super(position, blockState, FissionCoolantHeaterPortType.IRON);
        }
    }

    public static class Redstone extends FissionHeaterPortEntity.Variant {
        public Redstone(final BlockPos position, final BlockState blockState) {
            super(position, blockState, FissionCoolantHeaterPortType.REDSTONE);
        }
    }

    public static class Quartz extends FissionHeaterPortEntity.Variant {
        public Quartz(final BlockPos position, final BlockState blockState) {
            super(position, blockState, FissionCoolantHeaterPortType.QUARTZ);
        }
    }

    public static class Obsidian extends FissionHeaterPortEntity.Variant {
        public Obsidian(final BlockPos position, final BlockState blockState) {
            super(position, blockState, FissionCoolantHeaterPortType.OBSIDIAN);
        }
    }

    public static class NetherBrick extends FissionHeaterPortEntity.Variant {
        public NetherBrick(final BlockPos position, final BlockState blockState) {
            super(position, blockState, FissionCoolantHeaterPortType.NETHER_BRICK);
        }
    }

    public static class Glowstone extends FissionHeaterPortEntity.Variant {
        public Glowstone(final BlockPos position, final BlockState blockState) {
            super(position, blockState, FissionCoolantHeaterPortType.GLOWSTONE);
        }
    }

    public static class Lapis extends FissionHeaterPortEntity.Variant {
        public Lapis(final BlockPos position, final BlockState blockState) {
            super(position, blockState, FissionCoolantHeaterPortType.LAPIS);
        }
    }

    public static class Gold extends FissionHeaterPortEntity.Variant {
        public Gold(final BlockPos position, final BlockState blockState) {
            super(position, blockState, FissionCoolantHeaterPortType.GOLD);
        }
    }

    public static class Prismarine extends FissionHeaterPortEntity.Variant {
        public Prismarine(final BlockPos position, final BlockState blockState) {
            super(position, blockState, FissionCoolantHeaterPortType.PRISMARINE);
        }
    }

    public static class Slime extends FissionHeaterPortEntity.Variant {
        public Slime(final BlockPos position, final BlockState blockState) {
            super(position, blockState, FissionCoolantHeaterPortType.SLIME);
        }
    }

    public static class EndStone extends FissionHeaterPortEntity.Variant {
        public EndStone(final BlockPos position, final BlockState blockState) {
            super(position, blockState, FissionCoolantHeaterPortType.END_STONE);
        }
    }

    public static class Purpur extends FissionHeaterPortEntity.Variant {
        public Purpur(final BlockPos position, final BlockState blockState) {
            super(position, blockState, FissionCoolantHeaterPortType.PURPUR);
        }
    }

    public static class Diamond extends FissionHeaterPortEntity.Variant {
        public Diamond(final BlockPos position, final BlockState blockState) {
            super(position, blockState, FissionCoolantHeaterPortType.DIAMOND);
        }
    }

    public static class Emerald extends FissionHeaterPortEntity.Variant {
        public Emerald(final BlockPos position, final BlockState blockState) {
            super(position, blockState, FissionCoolantHeaterPortType.EMERALD);
        }
    }

    public static class Copper extends FissionHeaterPortEntity.Variant {
        public Copper(final BlockPos position, final BlockState blockState) {
            super(position, blockState, FissionCoolantHeaterPortType.COPPER);
        }
    }

    public static class Tin extends FissionHeaterPortEntity.Variant {
        public Tin(final BlockPos position, final BlockState blockState) {
            super(position, blockState, FissionCoolantHeaterPortType.TIN);
        }
    }

    public static class Lead extends FissionHeaterPortEntity.Variant {
        public Lead(final BlockPos position, final BlockState blockState) {
            super(position, blockState, FissionCoolantHeaterPortType.LEAD);
        }
    }

    public static class Boron extends FissionHeaterPortEntity.Variant {
        public Boron(final BlockPos position, final BlockState blockState) {
            super(position, blockState, FissionCoolantHeaterPortType.BORON);
        }
    }

    public static class Lithium extends FissionHeaterPortEntity.Variant {
        public Lithium(final BlockPos position, final BlockState blockState) {
            super(position, blockState, FissionCoolantHeaterPortType.LITHIUM);
        }
    }

    public static class Magnesium extends FissionHeaterPortEntity.Variant {
        public Magnesium(final BlockPos position, final BlockState blockState) {
            super(position, blockState, FissionCoolantHeaterPortType.MAGNESIUM);
        }
    }

    public static class Manganese extends FissionHeaterPortEntity.Variant {
        public Manganese(final BlockPos position, final BlockState blockState) {
            super(position, blockState, FissionCoolantHeaterPortType.MANGANESE);
        }
    }

    public static class Aluminum extends FissionHeaterPortEntity.Variant {
        public Aluminum(final BlockPos position, final BlockState blockState) {
            super(position, blockState, FissionCoolantHeaterPortType.ALUMINUM);
        }
    }

    public static class Silver extends FissionHeaterPortEntity.Variant {
        public Silver(final BlockPos position, final BlockState blockState) {
            super(position, blockState, FissionCoolantHeaterPortType.SILVER);
        }
    }

    public static class Fluorite extends FissionHeaterPortEntity.Variant {
        public Fluorite(final BlockPos position, final BlockState blockState) {
            super(position, blockState, FissionCoolantHeaterPortType.FLUORITE);
        }
    }

    public static class Villiaumite extends FissionHeaterPortEntity.Variant {
        public Villiaumite(final BlockPos position, final BlockState blockState) {
            super(position, blockState, FissionCoolantHeaterPortType.VILLIAUMITE);
        }
    }

    public static class Carobbiite extends FissionHeaterPortEntity.Variant {
        public Carobbiite(final BlockPos position, final BlockState blockState) {
            super(position, blockState, FissionCoolantHeaterPortType.CAROBBIITE);
        }
    }

    public static class Arsenic extends FissionHeaterPortEntity.Variant {
        public Arsenic(final BlockPos position, final BlockState blockState) {
            super(position, blockState, FissionCoolantHeaterPortType.ARSENIC);
        }
    }

    public static class LiquidNitrogen extends FissionHeaterPortEntity.Variant {
        public LiquidNitrogen(final BlockPos position, final BlockState blockState) {
            super(position, blockState, FissionCoolantHeaterPortType.LIQUID_NITROGEN);
        }
    }

    public static class LiquidHelium extends FissionHeaterPortEntity.Variant {
        public LiquidHelium(final BlockPos position, final BlockState blockState) {
            super(position, blockState, FissionCoolantHeaterPortType.LIQUID_HELIUM);
        }
    }

    public static class Enderium extends FissionHeaterPortEntity.Variant {
        public Enderium(final BlockPos position, final BlockState blockState) {
            super(position, blockState, FissionCoolantHeaterPortType.ENDERIUM);
        }
    }

    public static class Cryotheum extends FissionHeaterPortEntity.Variant {
        public Cryotheum(final BlockPos position, final BlockState blockState) {
            super(position, blockState, FissionCoolantHeaterPortType.CRYOTHEUM);
        }
    }

    @Override
    public Object getFilterKey() {
        return heaterPortType;
    }

    // NBT

    @Override
    public CompoundTag writeAll(CompoundTag nbt, HolderLookup.Provider registries) {
        return super.writeAll(nbt, registries);
    }

    @Override
    public void readAll(CompoundTag nbt, HolderLookup.Provider registries) {
        super.readAll(nbt, registries);
        if (SaltFissionHeaterEntity.DYN_COOLANT_NAME_MAP.containsKey(heaterPortType.getName())) {
            coolantName = SaltFissionHeaterEntity.DYN_COOLANT_NAME_MAP.get(heaterPortType.getName());
            tanks.get(0).setAllowedFluids(Collections.singleton(coolantName));
        }
    }
}
