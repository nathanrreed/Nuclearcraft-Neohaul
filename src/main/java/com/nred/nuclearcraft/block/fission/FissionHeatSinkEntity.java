package com.nred.nuclearcraft.block.fission;

import com.nred.nuclearcraft.multiblock.PlacementRule;
import com.nred.nuclearcraft.multiblock.fisson.FissionPlacement;
import com.nred.nuclearcraft.multiblock.fisson.FissionReactor;
import com.nred.nuclearcraft.multiblock.fisson.solid.FissionHeatSinkType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

import static com.nred.nuclearcraft.registration.BlockEntityRegistration.FISSION_ENTITY_TYPE;

public class FissionHeatSinkEntity extends AbstractFissionEntity {
    public FissionHeatSinkType heatSinkType;
    public PlacementRule<FissionReactor, AbstractFissionEntity> placementRule;
    public Double coolingRate;

    public FissionHeatSinkEntity(final BlockPos position, final BlockState blockState, FissionHeatSinkType heatSinkType) {
        super(FISSION_ENTITY_TYPE.get("heat_sink").get(), position, blockState);
        this.heatSinkType = heatSinkType;
        this.placementRule = FissionPlacement.RULE_MAP.get(heatSinkType.getName() + "_coil");
        this.coolingRate = heatSinkType.getCoolingRate();
    }

    public static class Variant extends FissionHeatSinkEntity {
        protected Variant(final BlockPos position, final BlockState blockState, FissionHeatSinkType dynamoCoilType) {
            super(position, blockState, dynamoCoilType);
        }
    }

    public static class Water extends FissionHeatSinkEntity.Variant {
        public Water(final BlockPos position, final BlockState blockState) {
            super(position, blockState, FissionHeatSinkType.WATER);
        }
    }

    public static class Iron extends FissionHeatSinkEntity.Variant {
        public Iron(final BlockPos position, final BlockState blockState) {
            super(position, blockState, FissionHeatSinkType.IRON);
        }
    }

    public static class Redstone extends FissionHeatSinkEntity.Variant {
        public Redstone(final BlockPos position, final BlockState blockState) {
            super(position, blockState, FissionHeatSinkType.REDSTONE);
        }
    }

    public static class Quartz extends FissionHeatSinkEntity.Variant {
        public Quartz(final BlockPos position, final BlockState blockState) {
            super(position, blockState, FissionHeatSinkType.QUARTZ);
        }
    }

    public static class Obsidian extends FissionHeatSinkEntity.Variant {
        public Obsidian(final BlockPos position, final BlockState blockState) {
            super(position, blockState, FissionHeatSinkType.OBSIDIAN);
        }
    }

    public static class NetherBrick extends FissionHeatSinkEntity.Variant {
        public NetherBrick(final BlockPos position, final BlockState blockState) {
            super(position, blockState, FissionHeatSinkType.NETHER_BRICK);
        }
    }

    public static class Glowstone extends FissionHeatSinkEntity.Variant {
        public Glowstone(final BlockPos position, final BlockState blockState) {
            super(position, blockState, FissionHeatSinkType.GLOWSTONE);
        }
    }

    public static class Lapis extends FissionHeatSinkEntity.Variant {
        public Lapis(final BlockPos position, final BlockState blockState) {
            super(position, blockState, FissionHeatSinkType.LAPIS);
        }
    }

    public static class Gold extends FissionHeatSinkEntity.Variant {
        public Gold(final BlockPos position, final BlockState blockState) {
            super(position, blockState, FissionHeatSinkType.GOLD);
        }
    }

    public static class Prismarine extends FissionHeatSinkEntity.Variant {
        public Prismarine(final BlockPos position, final BlockState blockState) {
            super(position, blockState, FissionHeatSinkType.PRISMARINE);
        }
    }

    public static class Slime extends FissionHeatSinkEntity.Variant {
        public Slime(final BlockPos position, final BlockState blockState) {
            super(position, blockState, FissionHeatSinkType.SLIME);
        }
    }

    public static class EndStone extends FissionHeatSinkEntity.Variant {
        public EndStone(final BlockPos position, final BlockState blockState) {
            super(position, blockState, FissionHeatSinkType.END_STONE);
        }
    }

    public static class Purpur extends FissionHeatSinkEntity.Variant {
        public Purpur(final BlockPos position, final BlockState blockState) {
            super(position, blockState, FissionHeatSinkType.PURPUR);
        }
    }

    public static class Diamond extends FissionHeatSinkEntity.Variant {
        public Diamond(final BlockPos position, final BlockState blockState) {
            super(position, blockState, FissionHeatSinkType.DIAMOND);
        }
    }

    public static class Emerald extends FissionHeatSinkEntity.Variant {
        public Emerald(final BlockPos position, final BlockState blockState) {
            super(position, blockState, FissionHeatSinkType.EMERALD);
        }
    }

    public static class Copper extends FissionHeatSinkEntity.Variant {
        public Copper(final BlockPos position, final BlockState blockState) {
            super(position, blockState, FissionHeatSinkType.COPPER);
        }
    }

    public static class Tin extends FissionHeatSinkEntity.Variant {
        public Tin(final BlockPos position, final BlockState blockState) {
            super(position, blockState, FissionHeatSinkType.TIN);
        }
    }

    public static class Lead extends FissionHeatSinkEntity.Variant {
        public Lead(final BlockPos position, final BlockState blockState) {
            super(position, blockState, FissionHeatSinkType.LEAD);
        }
    }

    public static class Boron extends FissionHeatSinkEntity.Variant {
        public Boron(final BlockPos position, final BlockState blockState) {
            super(position, blockState, FissionHeatSinkType.BORON);
        }
    }

    public static class Lithium extends FissionHeatSinkEntity.Variant {
        public Lithium(final BlockPos position, final BlockState blockState) {
            super(position, blockState, FissionHeatSinkType.LITHIUM);
        }
    }

    public static class Magnesium extends FissionHeatSinkEntity.Variant {
        public Magnesium(final BlockPos position, final BlockState blockState) {
            super(position, blockState, FissionHeatSinkType.MAGNESIUM);
        }
    }

    public static class Manganese extends FissionHeatSinkEntity.Variant {
        public Manganese(final BlockPos position, final BlockState blockState) {
            super(position, blockState, FissionHeatSinkType.MANGANESE);
        }
    }

    public static class Aluminum extends FissionHeatSinkEntity.Variant {
        public Aluminum(final BlockPos position, final BlockState blockState) {
            super(position, blockState, FissionHeatSinkType.ALUMINUM);
        }
    }

    public static class Silver extends FissionHeatSinkEntity.Variant {
        public Silver(final BlockPos position, final BlockState blockState) {
            super(position, blockState, FissionHeatSinkType.SILVER);
        }
    }

    public static class Fluorite extends FissionHeatSinkEntity.Variant {
        public Fluorite(final BlockPos position, final BlockState blockState) {
            super(position, blockState, FissionHeatSinkType.FLUORITE);
        }
    }

    public static class Villiaumite extends FissionHeatSinkEntity.Variant {
        public Villiaumite(final BlockPos position, final BlockState blockState) {
            super(position, blockState, FissionHeatSinkType.VILLIAUMITE);
        }
    }

    public static class Carobbiite extends FissionHeatSinkEntity.Variant {
        public Carobbiite(final BlockPos position, final BlockState blockState) {
            super(position, blockState, FissionHeatSinkType.CAROBBIITE);
        }
    }

    public static class Arsenic extends FissionHeatSinkEntity.Variant {
        public Arsenic(final BlockPos position, final BlockState blockState) {
            super(position, blockState, FissionHeatSinkType.ARSENIC);
        }
    }

    public static class LiquidNitrogen extends FissionHeatSinkEntity.Variant {
        public LiquidNitrogen(final BlockPos position, final BlockState blockState) {
            super(position, blockState, FissionHeatSinkType.LIQUID_NITROGEN);
        }
    }

    public static class LiquidHelium extends FissionHeatSinkEntity.Variant {
        public LiquidHelium(final BlockPos position, final BlockState blockState) {
            super(position, blockState, FissionHeatSinkType.LIQUID_HELIUM);
        }
    }

    public static class Enderium extends FissionHeatSinkEntity.Variant {
        public Enderium(final BlockPos position, final BlockState blockState) {
            super(position, blockState, FissionHeatSinkType.ENDERIUM);
        }
    }

    public static class Cryotheum extends FissionHeatSinkEntity.Variant {
        public Cryotheum(final BlockPos position, final BlockState blockState) {
            super(position, blockState, FissionHeatSinkType.CRYOTHEUM);
        }
    }
}
