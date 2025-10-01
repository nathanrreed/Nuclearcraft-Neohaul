package com.nred.nuclearcraft.block.fission;

import com.nred.nuclearcraft.multiblock.PlacementRule;
import com.nred.nuclearcraft.multiblock.fisson.FissionPlacement;
import com.nred.nuclearcraft.multiblock.fisson.FissionReactor;
import com.nred.nuclearcraft.multiblock.fisson.molten_salt.FissionCoolantHeaterType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

import static com.nred.nuclearcraft.registration.BlockEntityRegistration.FISSION_ENTITY_TYPE;

public class FissionHeaterEntity extends AbstractFissionEntity {
    public FissionCoolantHeaterType heaterType;
    public PlacementRule<FissionReactor, AbstractFissionEntity> placementRule;
    public Double coolingRate;

    public FissionHeaterEntity(final BlockPos position, final BlockState blockState, FissionCoolantHeaterType heaterType) {
        super(FISSION_ENTITY_TYPE.get("coolant_heater").get(), position, blockState);
        this.heaterType = heaterType;
        this.placementRule = FissionPlacement.RULE_MAP.get(heaterType.getName() + "_coil");
        this.coolingRate = heaterType.getCoolingRate();
    }

    public static class Variant extends FissionHeaterEntity {
        protected Variant(final BlockPos position, final BlockState blockState, FissionCoolantHeaterType dynamoCoilType) {
            super(position, blockState, dynamoCoilType);
        }
    }

    public static class Standard extends FissionHeaterEntity.Variant {
        public Standard(final BlockPos position, final BlockState blockState) {
            super(position, blockState, FissionCoolantHeaterType.STANDARD);
        }
    }

    public static class Iron extends FissionHeaterEntity.Variant {
        public Iron(final BlockPos position, final BlockState blockState) {
            super(position, blockState, FissionCoolantHeaterType.IRON);
        }
    }

    public static class Redstone extends FissionHeaterEntity.Variant {
        public Redstone(final BlockPos position, final BlockState blockState) {
            super(position, blockState, FissionCoolantHeaterType.REDSTONE);
        }
    }

    public static class Quartz extends FissionHeaterEntity.Variant {
        public Quartz(final BlockPos position, final BlockState blockState) {
            super(position, blockState, FissionCoolantHeaterType.QUARTZ);
        }
    }

    public static class Obsidian extends FissionHeaterEntity.Variant {
        public Obsidian(final BlockPos position, final BlockState blockState) {
            super(position, blockState, FissionCoolantHeaterType.OBSIDIAN);
        }
    }

    public static class NetherBrick extends FissionHeaterEntity.Variant {
        public NetherBrick(final BlockPos position, final BlockState blockState) {
            super(position, blockState, FissionCoolantHeaterType.NETHER_BRICK);
        }
    }

    public static class Glowstone extends FissionHeaterEntity.Variant {
        public Glowstone(final BlockPos position, final BlockState blockState) {
            super(position, blockState, FissionCoolantHeaterType.GLOWSTONE);
        }
    }

    public static class Lapis extends FissionHeaterEntity.Variant {
        public Lapis(final BlockPos position, final BlockState blockState) {
            super(position, blockState, FissionCoolantHeaterType.LAPIS);
        }
    }

    public static class Gold extends FissionHeaterEntity.Variant {
        public Gold(final BlockPos position, final BlockState blockState) {
            super(position, blockState, FissionCoolantHeaterType.GOLD);
        }
    }

    public static class Prismarine extends FissionHeaterEntity.Variant {
        public Prismarine(final BlockPos position, final BlockState blockState) {
            super(position, blockState, FissionCoolantHeaterType.PRISMARINE);
        }
    }

    public static class Slime extends FissionHeaterEntity.Variant {
        public Slime(final BlockPos position, final BlockState blockState) {
            super(position, blockState, FissionCoolantHeaterType.SLIME);
        }
    }

    public static class EndStone extends FissionHeaterEntity.Variant {
        public EndStone(final BlockPos position, final BlockState blockState) {
            super(position, blockState, FissionCoolantHeaterType.END_STONE);
        }
    }

    public static class Purpur extends FissionHeaterEntity.Variant {
        public Purpur(final BlockPos position, final BlockState blockState) {
            super(position, blockState, FissionCoolantHeaterType.PURPUR);
        }
    }

    public static class Diamond extends FissionHeaterEntity.Variant {
        public Diamond(final BlockPos position, final BlockState blockState) {
            super(position, blockState, FissionCoolantHeaterType.DIAMOND);
        }
    }

    public static class Emerald extends FissionHeaterEntity.Variant {
        public Emerald(final BlockPos position, final BlockState blockState) {
            super(position, blockState, FissionCoolantHeaterType.EMERALD);
        }
    }

    public static class Copper extends FissionHeaterEntity.Variant {
        public Copper(final BlockPos position, final BlockState blockState) {
            super(position, blockState, FissionCoolantHeaterType.COPPER);
        }
    }

    public static class Tin extends FissionHeaterEntity.Variant {
        public Tin(final BlockPos position, final BlockState blockState) {
            super(position, blockState, FissionCoolantHeaterType.TIN);
        }
    }

    public static class Lead extends FissionHeaterEntity.Variant {
        public Lead(final BlockPos position, final BlockState blockState) {
            super(position, blockState, FissionCoolantHeaterType.LEAD);
        }
    }

    public static class Boron extends FissionHeaterEntity.Variant {
        public Boron(final BlockPos position, final BlockState blockState) {
            super(position, blockState, FissionCoolantHeaterType.BORON);
        }
    }

    public static class Lithium extends FissionHeaterEntity.Variant {
        public Lithium(final BlockPos position, final BlockState blockState) {
            super(position, blockState, FissionCoolantHeaterType.LITHIUM);
        }
    }

    public static class Magnesium extends FissionHeaterEntity.Variant {
        public Magnesium(final BlockPos position, final BlockState blockState) {
            super(position, blockState, FissionCoolantHeaterType.MAGNESIUM);
        }
    }

    public static class Manganese extends FissionHeaterEntity.Variant {
        public Manganese(final BlockPos position, final BlockState blockState) {
            super(position, blockState, FissionCoolantHeaterType.MANGANESE);
        }
    }

    public static class Aluminum extends FissionHeaterEntity.Variant {
        public Aluminum(final BlockPos position, final BlockState blockState) {
            super(position, blockState, FissionCoolantHeaterType.ALUMINUM);
        }
    }

    public static class Silver extends FissionHeaterEntity.Variant {
        public Silver(final BlockPos position, final BlockState blockState) {
            super(position, blockState, FissionCoolantHeaterType.SILVER);
        }
    }

    public static class Fluorite extends FissionHeaterEntity.Variant {
        public Fluorite(final BlockPos position, final BlockState blockState) {
            super(position, blockState, FissionCoolantHeaterType.FLUORITE);
        }
    }

    public static class Villiaumite extends FissionHeaterEntity.Variant {
        public Villiaumite(final BlockPos position, final BlockState blockState) {
            super(position, blockState, FissionCoolantHeaterType.VILLIAUMITE);
        }
    }

    public static class Carobbiite extends FissionHeaterEntity.Variant {
        public Carobbiite(final BlockPos position, final BlockState blockState) {
            super(position, blockState, FissionCoolantHeaterType.CAROBBIITE);
        }
    }

    public static class Arsenic extends FissionHeaterEntity.Variant {
        public Arsenic(final BlockPos position, final BlockState blockState) {
            super(position, blockState, FissionCoolantHeaterType.ARSENIC);
        }
    }

    public static class LiquidNitrogen extends FissionHeaterEntity.Variant {
        public LiquidNitrogen(final BlockPos position, final BlockState blockState) {
            super(position, blockState, FissionCoolantHeaterType.LIQUID_NITROGEN);
        }
    }

    public static class LiquidHelium extends FissionHeaterEntity.Variant {
        public LiquidHelium(final BlockPos position, final BlockState blockState) {
            super(position, blockState, FissionCoolantHeaterType.LIQUID_HELIUM);
        }
    }

    public static class Enderium extends FissionHeaterEntity.Variant {
        public Enderium(final BlockPos position, final BlockState blockState) {
            super(position, blockState, FissionCoolantHeaterType.ENDERIUM);
        }
    }

    public static class Cryotheum extends FissionHeaterEntity.Variant {
        public Cryotheum(final BlockPos position, final BlockState blockState) {
            super(position, blockState, FissionCoolantHeaterType.CRYOTHEUM);
        }
    }
}
