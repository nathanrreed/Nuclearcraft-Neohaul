package com.nred.nuclearcraft.block_entity.generator;

import com.nred.nuclearcraft.block_entity.ITickable;
import com.nred.nuclearcraft.block_entity.dummy.IInterfaceable;
import com.nred.nuclearcraft.block_entity.energy.ITileEnergy;
import com.nred.nuclearcraft.block_entity.energy.TileEnergy;
import com.nred.nuclearcraft.block_entity.internal.energy.EnergyConnection;
import com.nred.nuclearcraft.recipe.NCRecipes;
import com.nred.nuclearcraft.recipe.DecayGeneratorRecipe;
import com.nred.nuclearcraft.recipe.RecipeHelper;
import com.nred.nuclearcraft.recipe.RecipeStats;
import com.nred.nuclearcraft.util.NCMath;
import com.nred.nuclearcraft.util.StackHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Random;

import static com.nred.nuclearcraft.config.NCConfig.machine_update_rate;
import static com.nred.nuclearcraft.registration.BlockEntityRegistration.DECAY_GENERATOR_ENTITY_TYPE;

public class DecayGeneratorEntity extends TileEnergy implements ITickable, IInterfaceable {
    Random rand = new Random();

    protected DecayGeneratorRecipe[] recipes = new DecayGeneratorRecipe[6];

    protected int generatorCount;

    public DecayGeneratorEntity(BlockPos pos, BlockState blockState) {
        super(DECAY_GENERATOR_ENTITY_TYPE.get(), pos, blockState, 2L * RecipeStats.getDecayGeneratorMaxPower(), ITileEnergy.energyConnectionAll(EnergyConnection.OUT));
    }

    @Override
    public void onLoad() {
        super.onLoad();
        for (Direction side : Direction.values()) {
            refreshRecipe(side);
        }
    }

    @Override
    public void update() {
        if (!level.isClientSide()) {
            tickGenerator();
            if (generatorCount == 0) {
                getEnergyStorage().changeEnergyStored(getGenerated());
//				getRadiationSource().setRadiationLevel(getRadiation()); TODO
            }
            pushEnergy();
        }
    }

    public void tickGenerator() {
        ++generatorCount;
        generatorCount %= machine_update_rate;
    }

    public int getGenerated() {
        double power = 0D;
        for (Direction side : Direction.values()) {
            power += decayGen(side);
        }
        return NCMath.toInt(machine_update_rate * power);
    }

    public double getRadiation() {
        double radiation = 0D;
        for (Direction side : Direction.values()) {
            if (getDecayRecipe(side) != null) {
                radiation += getDecayRecipe(side).getDecayGeneratorRadiation();
            }
        }
        return machine_update_rate * radiation;
    }

    public double decayGen(Direction side) {
        if (getDecayRecipe(side) == null) {
            return 0D;
        }
        ItemStack stack = getOutput(side);
        if (stack == null || stack.isEmpty()) {
            return 0D;
        }
        if (rand.nextDouble() * getRecipeLifetime(side) / machine_update_rate < 1D) {
            BlockState block = StackHelper.getBlockStateFromStack(stack);
            if (block == null) {
                return 0D;
            }
            level.setBlockAndUpdate(worldPosition.relative(side), block);
            refreshRecipe(side);
        }
        return getRecipePower(side);
    }

    @Override
    public void onBlockNeighborChanged(BlockState state, Level world, BlockPos pos, BlockPos fromPos) {
        super.onBlockNeighborChanged(state, world, pos, fromPos);
        for (Direction side : Direction.values()) {
            refreshRecipe(side);
        }
    }

    public void refreshRecipe(Direction side) {
        recipes[side.ordinal()] = (DecayGeneratorRecipe) RecipeHelper.blockRecipe(NCRecipes.decay_generator, level, worldPosition.relative(side));
    }

    // Recipe from BlockPos

    public DecayGeneratorRecipe getDecayRecipe(Direction side) {
        return recipes[side.ordinal()];
    }

    public double getRecipeLifetime(Direction side) {
        if (getDecayRecipe(side) == null) {
            return 1200D;
        }
        return getDecayRecipe(side).getDecayGeneratorLifetime();
    }

    public double getRecipePower(Direction side) {
        if (getDecayRecipe(side) == null) {
            return 0D;
        }
        return getDecayRecipe(side).getDecayGeneratorPower();
    }

    public ItemStack getOutput(Direction side) {
        if (getDecayRecipe(side) == null) {
            return ItemStack.EMPTY;
        }
        ItemStack output = RecipeHelper.getItemStackFromIngredientList(getDecayRecipe(side).getItemProducts(), 0);
        return output != null ? output : ItemStack.EMPTY;
    }
}