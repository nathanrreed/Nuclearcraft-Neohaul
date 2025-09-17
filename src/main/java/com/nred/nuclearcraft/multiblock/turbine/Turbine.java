package com.nred.nuclearcraft.multiblock.turbine;

import com.google.common.collect.Lists;
import com.nred.nuclearcraft.NuclearcraftNeohaul;
import com.nred.nuclearcraft.block.DirectionalGenericDeviceBlock;
import com.nred.nuclearcraft.block.turbine.*;
import com.nred.nuclearcraft.handler.SoundHandler;
import com.nred.nuclearcraft.helpers.CustomEnergyHandler;
import com.nred.nuclearcraft.helpers.Tank;
import com.nred.nuclearcraft.multiblock.MachineMultiblock;
import com.nred.nuclearcraft.multiblock.turbine.TurbineRotorBladeUtil.*;
import com.nred.nuclearcraft.payload.TurbineRenderPayload;
import com.nred.nuclearcraft.recipe.turbine.TurbineRecipe;
import com.nred.nuclearcraft.registration.SoundRegistration;
import com.nred.nuclearcraft.util.BlockStateHelper;
import com.nred.nuclearcraft.util.NBTHelper;
import com.nred.nuclearcraft.util.NCMath;
import com.nred.nuclearcraft.util.SoundHelper;
import it.unimi.dsi.fastutil.doubles.DoubleArrayList;
import it.unimi.dsi.fastutil.doubles.DoubleList;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.zerono.mods.zerocore.lib.multiblock.IMultiblockController;
import it.zerono.mods.zerocore.lib.multiblock.IMultiblockPart;
import it.zerono.mods.zerocore.lib.multiblock.registry.MultiblockRegistry;
import it.zerono.mods.zerocore.lib.multiblock.validation.IMultiblockValidator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.Direction.AxisDirection;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.util.Mth;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.TreeSet;

import static com.nred.nuclearcraft.config.Config2.*;
import static com.nred.nuclearcraft.registration.BlockRegistration.TURBINE_MAP;
import static com.nred.nuclearcraft.registration.RecipeTypeRegistration.TURBINE_RECIPE_TYPE;

public class Turbine extends MachineMultiblock<Turbine> implements IPacketMultiblock {
    public ITurbineController<?> controller;

    public static final int BASE_MAX_ENERGY = 16000, BASE_MAX_INPUT = 1000, BASE_MAX_OUTPUT = 4000;

    public final CustomEnergyHandler energyStorage = new CustomEnergyHandler(BASE_MAX_ENERGY, false, true);
    public final List<Tank> tanks = Lists.newArrayList(new Tank(BASE_MAX_INPUT, new TreeSet<>()), new Tank(BASE_MAX_OUTPUT, null)); // NCRecipes.turbine.validFluids.get(0) TODO

//    public RecipeInfo<BasicRecipe> recipeInfo;

    public Optional<RecipeHolder<TurbineRecipe>> recipe;

    public boolean isTurbineOn, computerActivated, isProcessing;
    public double power = 0D, conductivity = 0D, rotorEfficiency = 0D, powerBonus = 0D;
    public double rawPower = 0D;

    public Direction flowDir = null;
    public int shaftWidth = 0, inertia = 0, bladeLength = 0, noBladeSets = 0, recipeInputRate = 0, dynamoCoilCount = 0, dynamoCoilCountOpposite = 0;
    public double totalExpansionLevel = 1D, idealTotalExpansionLevel = 1D, spinUpMultiplier = 1D, basePowerPerMB = 0D, recipeInputRateFP = 0D;

    public double minBladeExpansionCoefficient = Double.MAX_VALUE;
    public double maxBladeExpansionCoefficient = 1D;
    public double minStatorExpansionCoefficient = 1D;
    public double maxStatorExpansionCoefficient = Double.MIN_VALUE;
    public int effectiveMaxLength = turbine_max_size;
    public double bearingTension = 0D;

    public final DoubleList expansionLevels = new DoubleArrayList(), rawBladeEfficiencies = new DoubleArrayList();

    @OnlyIn(Dist.CLIENT)
    protected Object2ObjectMap<BlockPos, SoundInstance> soundMap;
    public boolean refreshSounds = true;

    public ParticleOptions particleEffect = ParticleTypes.CLOUD;
    public double particleSpeedMult = 5D / 116D;
    public float angVel = 0F, rotorAngle = 0F;

    @SuppressWarnings("unchecked")
    public Iterable<BlockPos>[] inputPlane = new Iterable[4];

    public boolean nbtUpdateRenderDataFlag = false;
    public boolean shouldSpecialRenderRotor = true; // TODO make false and only make true if there is turbine glass

    public BlockPos[] bladePosArray = null;
    public Vector3f[] renderPosArray = null;
    public float[] bladeAngleArray = null;

    public BlockState[] rotorStateArray = null;
    public final IntList bladeDepths = new IntArrayList(), statorDepths = new IntArrayList();

    public boolean searchFlag = false;
//    public final ObjectSet<TileTurbineDynamoPart> dynamoPartCache = new ObjectOpenHashSet<>(), dynamoPartCacheOpposite = new ObjectOpenHashSet<>();
//    public final Long2ObjectMap<TileTurbineDynamoPart> componentFailCache = new Long2ObjectOpenHashMap<>(), assumedValidCache = new Long2ObjectOpenHashMap<>();

    public float prevAngVel = 0F;

    public Turbine(Level world) {
        super(world);
//        for (Class<? extends ITurbinePart> clazz : PART_CLASSES) { TODO REMOVE
//            partSuperMap.equip(clazz);
//        }
    }

    @Override
    public CompoundTag syncDataTo(CompoundTag data, HolderLookup.Provider registries, SyncReason syncReason) {
//        writeEnergy(energyStorage, data, "energyStorage"); TODO
//        writeTanks(tanks, data, "tanks");
        data.putBoolean("isTurbineOn", isTurbineOn);
        data.putBoolean("computerActivated", computerActivated);
        data.putBoolean("isProcessing", isProcessing);
        data.putDouble("power", power);
        data.putDouble("rawPower", rawPower);

        data.putInt("flowDir", flowDir == null ? -1 : flowDir.ordinal());

        data.putDouble("conductivity", conductivity);
        data.putDouble("rotorEfficiency", rotorEfficiency);
        data.putDouble("powerBonus", powerBonus);
//        data.putString("particleEffect", particleEffect);
        data.putDouble("particleSpeedMult", particleSpeedMult);
        data.putFloat("angVel", angVel);
        data.putFloat("rotorAngle", rotorAngle);

        NBTHelper.writeBlockPosArray(data, bladePosArray, "bladePosArray");
        NBTHelper.writeVector3fArray(data, renderPosArray, "renderPosArray");
        NBTHelper.writeFloatArray(data, bladeAngleArray, "bladeAngleArray");

        data.putInt("shaftWidth", shaftWidth);
        data.putInt("shaftVolume", inertia);
        data.putInt("bladeLength", bladeLength);
        data.putInt("noBladeSets", noBladeSets);
        data.putInt("recipeInputRate", recipeInputRate);
        data.putInt("dynamoCoilCount", dynamoCoilCount);
        data.putInt("dynamoCoilCountOpposite", dynamoCoilCountOpposite);
        data.putDouble("totalExpansionLevel", totalExpansionLevel);
        data.putDouble("idealTotalExpansionLevel", idealTotalExpansionLevel);
        data.putDouble("spinUpMultiplier", spinUpMultiplier);
        data.putDouble("basePowerPerMB", basePowerPerMB);
        data.putDouble("recipeInputRateFP", recipeInputRateFP);
        data.putDouble("minBladeExpansionCoefficient", minBladeExpansionCoefficient);
        data.putDouble("maxBladeExpansionCoefficient", maxBladeExpansionCoefficient);
        data.putDouble("minStatorExpansionCoefficient", minStatorExpansionCoefficient);
        data.putDouble("maxStatorExpansionCoefficient", maxStatorExpansionCoefficient);
        data.putInt("effectiveMaxLength", effectiveMaxLength);
        data.putDouble("bearingTension", bearingTension);

        NBTHelper.writeDoubleCollection(data, expansionLevels, "expansionLevels");
        NBTHelper.writeDoubleCollection(data, rawBladeEfficiencies, "rawBladeEfficiencies");

//        writeLogicNBT(data, syncReason);

        return data;
    }

    @Override
    public void syncDataFrom(CompoundTag data, HolderLookup.Provider registries, SyncReason syncReason) {
//        readEnergy(energyStorage, data, "energyStorage"); TODO
//        readTanks(tanks, data, "tanks");
        isTurbineOn = data.getBoolean("isTurbineOn");
        computerActivated = data.getBoolean("computerActivated");
        isProcessing = data.getBoolean("isProcessing");
        power = data.getDouble("power");
        rawPower = data.getDouble("rawPower");

        flowDir = data.getInt("flowDir") < 0 ? null : Direction.values()[data.getInt("flowDir")];

        conductivity = data.getDouble("conductivity");
        rotorEfficiency = data.getDouble("rotorEfficiency");
        powerBonus = data.getDouble("powerBonus");
//        particleEffect = data.getString("particleEffect"); TODO
        particleSpeedMult = data.getDouble("particleSpeedMult");
        angVel = data.getFloat("angVel");
        rotorAngle = data.getFloat("rotorAngle");

        bladePosArray = NBTHelper.readBlockPosArray(data, "bladePosArray");
        renderPosArray = NBTHelper.readVector3fArray(data, "renderPosArray");
        bladeAngleArray = NBTHelper.readFloatArray(data, "bladeAngleArray");

        shaftWidth = data.getInt("shaftWidth");
        inertia = data.getInt("shaftVolume");
        bladeLength = data.getInt("bladeLength");
        noBladeSets = data.getInt("noBladeSets");
        recipeInputRate = data.getInt("recipeInputRate");
        dynamoCoilCount = data.getInt("dynamoCoilCount");
        dynamoCoilCountOpposite = data.getInt("dynamoCoilCountOpposite");
        totalExpansionLevel = data.getDouble("totalExpansionLevel");
        idealTotalExpansionLevel = data.getDouble("idealTotalExpansionLevel");
        spinUpMultiplier = data.getDouble("spinUpMultiplier");
        basePowerPerMB = data.getDouble("basePowerPerMB");
        recipeInputRateFP = data.getDouble("recipeInputRateFP");
        minBladeExpansionCoefficient = data.getDouble("minBladeExpansionCoefficient");
        maxBladeExpansionCoefficient = data.getDouble("maxBladeExpansionCoefficient");
        minStatorExpansionCoefficient = data.getDouble("minStatorExpansionCoefficient");
        maxStatorExpansionCoefficient = data.getDouble("maxStatorExpansionCoefficient");
        effectiveMaxLength = data.getInt("effectiveMaxLength");
        bearingTension = data.getDouble("bearingTension");

        NBTHelper.readDoubleCollection(data, expansionLevels, "expansionLevels");
        NBTHelper.readDoubleCollection(data, rawBladeEfficiencies, "rawBladeEfficiencies");

        nbtUpdateRenderDataFlag = true;

//        readLogicNBT(data, syncReason);
    }

    @Override
    protected void onPartAdded(IMultiblockPart<Turbine> iMultiblockPart) {

    }

    @Override
    protected void onPartRemoved(IMultiblockPart<Turbine> iMultiblockPart) {
    }

    @Override
    protected void onMachineAssembled() {
        System.out.println("ASSEMBLED");
        onTurbineFormed();
    }

    @Override
    protected void onMachineRestored() {
        onTurbineFormed();
    }

    @Override
    protected void onMachinePaused() {
        setIsTurbineOn();
    }

    @Override
    protected void onMachineDisassembled() {
        onTurbineBroken();
    }

    @Override
    public int getMinimumInteriorLength() {
        return turbine_min_size;
    }

    @Override
    public int getMaximumInteriorLength() {
        return turbine_max_size;
    }

    @Override
    protected void onAssimilate(IMultiblockController assimilated) {
        if (assimilated instanceof Turbine turbine)
            this.energyStorage.mergeEnergyStorage(turbine.energyStorage);
    }

    @Override
    protected void onAssimilated(IMultiblockController iMultiblockController) {
        if (getWorld().isClientSide) {
            clearSounds();
        }
    }

    public boolean setLogic(Turbine multiblock) {
        if (getPartMap(ITurbineController.class).isEmpty()) {
            multiblock.setLastError("multiblock_validation.no_controller");
            return false;
        }
        if (getPartCount(ITurbineController.class) > 1) {
            multiblock.setLastError("multiblock_validation.too_many_controllers");
            return false;
        }

        for (ITurbineController<?> contr : getParts(ITurbineController.class)) {
            controller = contr;
            break;
        }

//        setLogic(controller.getLogicID());

        return true;
    }

    @Override
    protected boolean isMachineWhole(IMultiblockValidator validatorCallback) {
//        shouldSpecialRenderRotor = false; TODO READD

        if (!setLogic(this) || !super.isMachineWhole(validatorCallback))
            return false;

        int minX = getMinX(), minY = getMinY(), minZ = getMinZ();
        int maxX = getMaxX(), maxY = getMaxY(), maxZ = getMaxZ();

        // Bearings -> flow axis

        boolean dirMinX = false, dirMaxX = false, dirMinY = false, dirMaxY = false, dirMinZ = false, dirMaxZ = false;
        Axis axis = null;
        boolean tooManyAxes = false; // Is any of the bearings in more than a single axis?
        boolean notInAWall = false; // Is the bearing somewhere else in the structure other than the wall?

        for (TurbineRotorBearingEntity bearing : getParts(TurbineRotorBearingEntity.class)) {
            BlockPos pos = bearing.getWorldPosition();

            if (pos.getX() == minX) {
                dirMinX = true;
            } else if (pos.getX() == maxX) {
                dirMaxX = true;
            } else if (pos.getY() == minY) {
                dirMinY = true;
            } else if (pos.getY() == maxY) {
                dirMaxY = true;
            } else if (pos.getZ() == minZ) {
                dirMinZ = true;
            } else if (pos.getZ() == maxZ) {
                dirMaxZ = true;
            } else {
                // If the bearing is not at any of those positions, that means our bearing isn't part of the wall at all
                notInAWall = true;
            }
        }

        if (dirMinX && dirMaxX) {
            axis = Axis.X;
        }
        if (dirMinY && dirMaxY) {
            if (axis != null) {
                tooManyAxes = true;
            } else {
                axis = Axis.Y;
            }
        }
        if (dirMinZ && dirMaxZ) {
            if (axis != null) {
                tooManyAxes = true;
            } else {
                axis = Axis.Z;
            }
        }

        if (axis == null) {
            validatorCallback.setLastError(NuclearcraftNeohaul.MODID + ".multiblock_validation.turbine.need_bearings");
            return false;
        }

        if (axis == Axis.X && getInteriorLengthY() != getInteriorLengthZ() || axis == Axis.Y && getInteriorLengthZ() != getInteriorLengthX() || axis == Axis.Z && getInteriorLengthX() != getInteriorLengthY() || tooManyAxes || notInAWall) {
            validatorCallback.setLastError(NuclearcraftNeohaul.MODID + ".multiblock_validation.turbine.bearings_side_square");
            return false;
        }

        // At this point, all bearings are guaranteed to be part of walls in the same axis
        // Also, it can only ever succeed up to this point if we already have at least two bearings, so no need to check for that

        int internalDiameter;
        if (axis == Axis.X) {
            internalDiameter = getInteriorLengthY();
        } else if (axis == Axis.Y) {
            internalDiameter = getInteriorLengthZ();
        } else {
            internalDiameter = getInteriorLengthX();
        }
        boolean isEvenDiameter = (internalDiameter & 1) == 0;
        boolean validAmountOfBearings = false;

        for (shaftWidth = isEvenDiameter ? 2 : 1; shaftWidth <= internalDiameter - 2; shaftWidth += 2) {
            if (getPartCount(TurbineRotorBearingEntity.class) == 2 * shaftWidth * shaftWidth) {
                validAmountOfBearings = true;
                break;
            }
        }

        if (!validAmountOfBearings) {
            validatorCallback.setLastError(NuclearcraftNeohaul.MODID + ".multiblock_validation.turbine.bearings_center_and_square");
            return false;
        }

        // Last thing that needs to be checked concerning bearings is whether they are grouped correctly at the center of their respective walls

        bladeLength = (internalDiameter - shaftWidth) / 2;

        for (BlockPos pos : getInteriorPlane(Direction.get(AxisDirection.NEGATIVE, axis), -1, bladeLength, bladeLength, bladeLength, bladeLength)) {
            if (getPartMap(TurbineRotorBearingEntity.class).containsKey(pos.asLong())) {
                continue;
            }
            validatorCallback.setLastError(NuclearcraftNeohaul.MODID + ".multiblock_validation.turbine.bearings_center_and_square", pos);
            return false;
        }

        for (BlockPos pos : getInteriorPlane(Direction.get(AxisDirection.POSITIVE, axis), -1, bladeLength, bladeLength, bladeLength, bladeLength)) {
            if (getPartMap(TurbineRotorBearingEntity.class).containsKey(pos.asLong())) {
                continue;
            }
            validatorCallback.setLastError(NuclearcraftNeohaul.MODID + ".multiblock_validation.turbine.bearings_center_and_square", pos);
            return false;
        }

        // All bearings should be valid by now!

        // Inlets/outlets -> multiblock.flowDir

        flowDir = null;

        if (getPartMap(TurbineInletEntity.class).isEmpty() || getPartMap(TurbineOutletEntity.class).isEmpty()) {
            validatorCallback.setLastError(NuclearcraftNeohaul.MODID + ".multiblock_validation.turbine.valve_wrong_wall");
            return false;
        }

        for (TurbineInletEntity inlet : getParts(TurbineInletEntity.class)) {
            BlockPos pos = inlet.getWorldPosition();

            if (isInMinWall(axis, pos)) {
                Direction thisFlowDir = Direction.get(AxisDirection.POSITIVE, axis);
                // Make sure that all inlets are in the same wall
                if (flowDir != null && flowDir != thisFlowDir) {
                    validatorCallback.setLastError(NuclearcraftNeohaul.MODID + ".multiblock_validation.turbine.valve_wrong_wall", pos);
                    return false;
                } else {
                    flowDir = thisFlowDir;
                }
            } else if (isInMaxWall(axis, pos)) {
                Direction thisFlowDir = Direction.get(AxisDirection.NEGATIVE, axis);
                // Make sure that all inlets are in the same wall
                if (flowDir != null && flowDir != thisFlowDir) {
                    validatorCallback.setLastError(NuclearcraftNeohaul.MODID + ".multiblock_validation.turbine.valve_wrong_wall", pos);
                    return false;
                } else {
                    flowDir = thisFlowDir;
                }
            } else {
                validatorCallback.setLastError(NuclearcraftNeohaul.MODID + ".multiblock_validation.turbine.valve_wrong_wall", pos);
                return false;
            }
        }

        if (flowDir == null) {
            validatorCallback.setLastError(NuclearcraftNeohaul.MODID + ".multiblock_validation.turbine.valve_wrong_wall");
            return false;
        }

        for (TurbineOutletEntity outlet : getParts(TurbineOutletEntity.class)) {
            BlockPos pos = outlet.getWorldPosition();

            if (!isInWall(flowDir, pos)) {
                validatorCallback.setLastError(NuclearcraftNeohaul.MODID + ".multiblock_validation.turbine.valve_wrong_wall", pos);
                return false;
            }
        }

        // Interior

        int flowLength = getFlowLength();

        for (int depth = 0; depth < flowLength; ++depth) {
            for (BlockPos pos : getInteriorPlane(Direction.get(AxisDirection.POSITIVE, axis), depth, bladeLength, bladeLength, bladeLength, bladeLength)) {
                TurbineRotorShaftEntity shaft = getPartMap(TurbineRotorShaftEntity.class).get(pos.asLong());
                if (shaft == null) {
                    validatorCallback.setLastError(NuclearcraftNeohaul.MODID + ".multiblock_validation.turbine.shaft_center", pos);
                    return false;
                }
            }
        }

        if (!areBladesValid()) {
            return false;
        }

        if (!NCMath.allEqual(getFlowLength(), expansionLevels.size(), rawBladeEfficiencies.size())) {
            validatorCallback.setLastError(NuclearcraftNeohaul.MODID + ".multiblock_validation.turbine.missing_blades");
            return false;
        }

        for (TurbineControllerEntity controller : getParts(TurbineControllerEntity.class)) {
            controller.setIsRenderer(false);
        }
        for (TurbineControllerEntity controller : getParts(TurbineControllerEntity.class)) {
            if (shouldSpecialRenderRotor) {
                controller.setIsRenderer(true);
            }
            break;
        }

        return true;
    }

    public boolean isInMinWall(Axis axis, BlockPos pos) {
        if (axis == null) {
            return false;
        }

        return switch (axis) {
            case X -> pos.getX() == getMinX();
            case Y -> pos.getY() == getMinY();
            case Z -> pos.getZ() == getMinZ();
        };
    }

    public boolean isInMaxWall(Axis axis, BlockPos pos) {
        if (axis == null) {
            return false;
        }

        return switch (axis) {
            case X -> pos.getX() == getMaxX();
            case Y -> pos.getY() == getMaxY();
            case Z -> pos.getZ() == getMaxZ();
        };
    }

    public boolean isInWall(Direction side, BlockPos pos) {
        if (side == null) {
            return false;
        }

        return switch (side) {
            case DOWN -> pos.getY() == getMinY();
            case UP -> pos.getY() == getMaxY();
            case NORTH -> pos.getZ() == getMinZ();
            case SOUTH -> pos.getZ() == getMaxZ();
            case WEST -> pos.getX() == getMinX();
            case EAST -> pos.getX() == getMaxX();
        };
    }

    public boolean areBladesValid() {
        int flowLength = getFlowLength();

        inertia = shaftWidth * (shaftWidth + 4 * bladeLength) * flowLength;
        noBladeSets = 0;

        totalExpansionLevel = 1D;
        expansionLevels.clear();
        rawBladeEfficiencies.clear();

        bladePosArray = new BlockPos[4 * flowLength];
        bladeAngleArray = new float[4 * flowLength];

        for (int depth = 0; depth < flowLength; ++depth) {

            // Free space

            for (BlockPos pos : getInteriorPlane(flowDir, depth, 0, 0, shaftWidth + bladeLength, shaftWidth + bladeLength)) {
                if (!BlockStateHelper.isReplaceable(getWorld().getBlockState(pos))) {
                    setLastError(NuclearcraftNeohaul.MODID + ".multiblock_validation.turbine.space_between_blades", pos);
                    return false;
                }
                getWorld().setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
            }

            for (BlockPos pos : getInteriorPlane(flowDir, depth, shaftWidth + bladeLength, 0, 0, shaftWidth + bladeLength)) {
                if (!BlockStateHelper.isReplaceable(getWorld().getBlockState(pos))) {
                    setLastError(NuclearcraftNeohaul.MODID + ".multiblock_validation.turbine.space_between_blades", pos);
                    return false;
                }
                getWorld().setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
            }

            for (BlockPos pos : getInteriorPlane(flowDir, depth, 0, shaftWidth + bladeLength, shaftWidth + bladeLength, 0)) {
                if (!BlockStateHelper.isReplaceable(getWorld().getBlockState(pos))) {
                    setLastError(NuclearcraftNeohaul.MODID + ".multiblock_validation.turbine.space_between_blades", pos);
                    return false;
                }
                getWorld().setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
            }

            for (BlockPos pos : getInteriorPlane(flowDir, depth, shaftWidth + bladeLength, shaftWidth + bladeLength, 0, 0)) {
                if (!BlockStateHelper.isReplaceable(getWorld().getBlockState(pos))) {
                    setLastError(NuclearcraftNeohaul.MODID + ".multiblock_validation.turbine.space_between_blades", pos);
                    return false;
                }
                getWorld().setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
            }

            // Blades/stators

            IRotorBladeType currentBladeType = null;

            for (BlockPos pos : getInteriorPlane(flowDir.getOpposite(), depth, bladeLength, 0, bladeLength, shaftWidth + bladeLength)) {
                ITurbineRotorBlade<?> thisBlade = getBlade(pos);
                IRotorBladeType thisBladeType = thisBlade == null ? null : thisBlade.getBladeType();
                if (thisBladeType == null) {
                    setLastError(NuclearcraftNeohaul.MODID + ".multiblock_validation.turbine.missing_blades", pos);
                    return false;
                } else if (currentBladeType == null) {
                    currentBladeType = thisBladeType;
                } else if (!currentBladeType.eq(thisBladeType)) {
                    setLastError(NuclearcraftNeohaul.MODID + ".multiblock_validation.turbine.different_type_blades", pos);
                    return false;
                }
                thisBlade.setDir(getBladeDir(PlaneDir.V));

                bladePosArray[depth] = thisBlade.bladePos();
                bladeAngleArray[depth] = 45F;
            }

            for (BlockPos pos : getInteriorPlane(flowDir.getOpposite(), depth, 0, bladeLength, shaftWidth + bladeLength, bladeLength)) {
                ITurbineRotorBlade<?> thisBlade = getBlade(pos);
                IRotorBladeType thisBladeType = thisBlade == null ? null : thisBlade.getBladeType();
                if (thisBladeType == null) {
                    setLastError(NuclearcraftNeohaul.MODID + ".multiblock_validation.turbine.missing_blades", pos);
                    return false;
                } else if (!currentBladeType.eq(thisBladeType)) {
                    setLastError(NuclearcraftNeohaul.MODID + ".multiblock_validation.turbine.different_type_blades", pos);
                    return false;
                }
                thisBlade.setDir(getBladeDir(PlaneDir.U));

                bladePosArray[depth + flowLength] = thisBlade.bladePos();
                bladeAngleArray[depth + flowLength] = flowDir.getAxis() == Axis.Z ? -45F : 45F;
            }

            for (BlockPos pos : getInteriorPlane(flowDir.getOpposite(), depth, shaftWidth + bladeLength, bladeLength, 0, bladeLength)) {
                ITurbineRotorBlade<?> thisBlade = getBlade(pos);
                IRotorBladeType thisBladeType = thisBlade == null ? null : thisBlade.getBladeType();
                if (thisBladeType == null) {
                    setLastError(NuclearcraftNeohaul.MODID + ".multiblock_validation.turbine.missing_blades", pos);
                    return false;
                } else if (!currentBladeType.eq(thisBladeType)) {
                    setLastError(NuclearcraftNeohaul.MODID + ".multiblock_validation.turbine.different_type_blades", pos);
                    return false;
                }
                thisBlade.setDir(getBladeDir(PlaneDir.U));

                bladePosArray[depth + 2 * flowLength] = thisBlade.bladePos();
                bladeAngleArray[depth + 2 * flowLength] = flowDir.getAxis() == Axis.Z ? 45F : -45F;
            }

            for (BlockPos pos : getInteriorPlane(flowDir.getOpposite(), depth, bladeLength, shaftWidth + bladeLength, bladeLength, 0)) {
                ITurbineRotorBlade<?> thisBlade = getBlade(pos);
                IRotorBladeType thisBladeType = thisBlade == null ? null : thisBlade.getBladeType();
                if (thisBladeType == null) {
                    setLastError(NuclearcraftNeohaul.MODID + ".multiblock_validation.turbine.missing_blades", pos);
                    return false;
                } else if (!currentBladeType.eq(thisBladeType)) {
                    setLastError(NuclearcraftNeohaul.MODID + ".multiblock_validation.turbine.different_type_blades", pos);
                    return false;
                }
                thisBlade.setDir(getBladeDir(PlaneDir.V));

                bladePosArray[depth + 3 * flowLength] = thisBlade.bladePos();
                bladeAngleArray[depth + 3 * flowLength] = -45F;
            }

            if (currentBladeType == null) {
                setLastError(NuclearcraftNeohaul.MODID + ".multiblock_validation.turbine.missing_blades");
                return false;
            }

            expansionLevels.add(totalExpansionLevel * Math.sqrt(currentBladeType.getExpansionCoefficient()));
            totalExpansionLevel *= currentBladeType.getExpansionCoefficient();
            rawBladeEfficiencies.add(currentBladeType.getEfficiency());

            if (currentBladeType instanceof IRotorStatorType) {
                minStatorExpansionCoefficient = Math.min(currentBladeType.getExpansionCoefficient(), minStatorExpansionCoefficient);
                maxStatorExpansionCoefficient = Math.max(currentBladeType.getExpansionCoefficient(), maxStatorExpansionCoefficient);
            } else {
                ++noBladeSets;
                minBladeExpansionCoefficient = Math.min(currentBladeType.getExpansionCoefficient(), minBladeExpansionCoefficient);
                maxBladeExpansionCoefficient = Math.max(currentBladeType.getExpansionCoefficient(), maxBladeExpansionCoefficient);
            }
        }

        return true;
    }

    @Override
    protected boolean updateServer() {
        boolean flag = true, wasProcessing = this.isProcessing;
        refreshRecipe();

        double prevRawPower = this.rawPower;
        int prevInputRate = this.recipeInputRate;

        Tank inputTank = this.tanks.getFirst();
        int maxRecipeRateMultiplier = getMaxRecipeRateMultiplier();
        double throughputMult = 1D + (turbine_tension_throughput_factor - 1D) * (double) inputTank.getFluidAmount() / (double) inputTank.getCapacity();
        this.recipeInputRate = Math.min(inputTank.getFluidAmount(), NCMath.toInt(throughputMult * maxRecipeRateMultiplier));

        double rawLimitPower = getRawLimitProcessPower(this.recipeInputRate);
        double rawMaxPower = getRawLimitProcessPower(maxRecipeRateMultiplier);

        this.isProcessing = canProcessInputs();

        if (this.isProcessing) {
            this.rawPower = getNewRawProcessPower(prevRawPower, rawLimitPower, true);
        } else {
            this.rawPower = getNewRawProcessPower(prevRawPower, 0D, false);
        }

        if (this.isProcessing) {
            produceProducts();
        }

        setRotorEfficiency();
        setEffectiveMaxLength();
        setInputRatePowerBonus();

        this.power = this.rawPower * this.conductivity * this.rotorEfficiency * getExpansionIdealityMultiplier(this.idealTotalExpansionLevel, this.totalExpansionLevel) * getThroughputEfficiency() * this.powerBonus;
        this.angVel = rawMaxPower == 0D ? 0F : (float) (turbine_render_rotor_speed * this.rawPower / rawMaxPower);

        if (!this.isProcessing) {
            this.recipeInputRate = 0;
        }

        this.recipeInputRateFP = NCMath.getNextFP(this.recipeInputRateFP, prevInputRate, this.recipeInputRate);

        if (wasProcessing != this.isProcessing) {
            this.sendMultiblockUpdatePacketToAll();
        }

        double tensionFactor = !this.isProcessing || maxRecipeRateMultiplier <= 0 ? 0D : (this.recipeInputRate - maxRecipeRateMultiplier * (1D + turbine_tension_leniency)) / maxRecipeRateMultiplier;
        if (tensionFactor > 0D) {
            tensionFactor /= (turbine_tension_throughput_factor < 2D ? 1D : turbine_tension_throughput_factor - 1D);
        } else {
            tensionFactor = -Math.sqrt(-tensionFactor);
        }

        this.bearingTension = Math.max(0D, this.bearingTension + Math.min(1D, tensionFactor) / (1200D * getPartCount(TurbineRotorBearingEntity.class)));
        if (this.bearingTension > 1D) {
            bearingFailure();
            return true;
        }

        this.energyStorage.changeEnergyStored((int) this.power);

        if (this.controller != null) {
//            this.sendMultiblockUpdatePacketToListeners();
//            this.sendRenderPacketToAll();
        }

        return flag;
    }

    @Override
    protected void updateClient() {
        if (this.shouldSpecialRenderRotor && this.flowDir != null) {
            if (this.nbtUpdateRenderDataFlag) {
                this.nbtUpdateRenderDataFlag = false;
                updateRenderData();
            }
            updateParticles();
        }
        updateSounds();
    }

    @Override
    protected boolean isBlockGoodForFrame(@NotNull Level level, int x, int y, int z, @NotNull IMultiblockValidator iMultiblockValidator) {
        return level.getBlockEntity(new BlockPos(x, y, z)) instanceof TurbineCasingEntity;
    }

    @Override
    protected boolean isBlockGoodForTop(@NotNull Level level, int x, int y, int z, @NotNull IMultiblockValidator iMultiblockValidator) {
        return switch (level.getBlockEntity(new BlockPos(x, y, z))) {
            case TurbineCasingEntity ignored -> true;
            case TurbineGlassEntity ignored -> true;
            case null, default -> false;
        };
    }

    @Override
    protected boolean isBlockGoodForBottom(@NotNull Level level, int x, int y, int z, @NotNull IMultiblockValidator iMultiblockValidator) {
        return switch (level.getBlockEntity(new BlockPos(x, y, z))) {
            case TurbineCasingEntity ignored -> true;
            case TurbineGlassEntity ignored -> true;
            case null, default -> false;
        };
    }

    @Override
    protected boolean isBlockGoodForSides(@NotNull Level level, int x, int y, int z, @NotNull IMultiblockValidator iMultiblockValidator) {
        return switch (level.getBlockEntity(new BlockPos(x, y, z))) {
            case TurbineCasingEntity ignored -> true;
            case TurbineGlassEntity ignored -> true;
            case null, default -> false;
        };
    }

    @Override
    protected boolean isBlockGoodForInterior(@NotNull Level level, int x, int y, int z, @NotNull IMultiblockValidator iMultiblockValidator) {
        BlockPos pos = new BlockPos(x, y, z);
        if (BlockStateHelper.isReplaceable(level.getBlockState(pos))) {
            return true;
        }
        return switch (level.getBlockEntity(pos)) {
            case TurbineRotorStatorEntity ignored -> true;
            case TurbineRotorBladeEntity ignored -> true;
            case TurbineRotorShaftEntity ignored -> true;
            case null, default -> false;
        };
    }

    public void setIsTurbineOn() {
        boolean oldIsTurbineOn = this.isTurbineOn;
        this.isTurbineOn = (isRedstonePowered() || this.computerActivated) && this.isAssembled();
        if (this.isTurbineOn != oldIsTurbineOn) {
            if (this.controller != null) {
//                this.setActivity(this.isTurbineOn); TODO
                this.sendMultiblockUpdatePacketToAll();
            }
        }
    }

    protected void onTurbineFormed() {
        for (ITurbineController<?> contr : getParts(ITurbineController.class)) {
            this.controller = contr;
            break;
        }
        setIsTurbineOn();

        if (!getWorld().isClientSide) {
            int mult = this.getExteriorVolume();
            this.energyStorage.setCapacity(Turbine.BASE_MAX_ENERGY * mult);
            this.energyStorage.setMaxTransfer(Turbine.BASE_MAX_ENERGY * mult);
            this.tanks.get(0).setCapacity(Turbine.BASE_MAX_INPUT * mult);
            this.tanks.get(1).setCapacity(Turbine.BASE_MAX_OUTPUT * mult);
        }

        if (this.flowDir == null) {
            return;
        }

        if (!getWorld().isClientSide) {
//            componentFailCache.clear();
//            do {
//                assumedValidCache.clear();
//                refreshDynamos();
//            }
//            while (searchFlag);

            refreshDynamoStats();

            for (TurbineRotorShaftEntity shaft : getParts(TurbineRotorShaftEntity.class)) {
                BlockPos pos = shaft.getBlockPos();
                BlockState state = getWorld().getBlockState(pos);
                if (state.getBlock() instanceof TurbineRotorShaftBlock) {
                    getWorld().setBlock(pos, state.setValue(TurbineRotorBladeUtil.DIR, TurbinePartDir.INVISIBLE), 3);
                }
            }

            for (TurbineRotorBladeEntity blade : getParts(TurbineRotorBladeEntity.class)) {
                BlockPos pos = blade.bladePos();
                BlockState state = getWorld().getBlockState(pos);
                if (state.getBlock() instanceof IBlockRotorBlade) {
                    getWorld().setBlock(pos, state.setValue(TurbineRotorBladeUtil.DIR, TurbinePartDir.INVISIBLE), 3);
                }
            }

            for (TurbineRotorStatorEntity stator : getParts(TurbineRotorStatorEntity.class)) {
                BlockPos pos = stator.bladePos();
                BlockState state = getWorld().getBlockState(pos);
                if (state.getBlock() instanceof IBlockRotorBlade) {
                    getWorld().setBlock(pos, state.setValue(TurbineRotorBladeUtil.DIR, TurbinePartDir.INVISIBLE), 3);
                }
            }

//            for (TileTurbineDynamoPart dynamoPart : getParts(TileTurbineDynamoPart.class)) {
//                for (Direction side : Direction.values()) {
//                    dynamoPart.setEnergyConnection(side == this.flowDir || side == this.flowDir.getOpposite() ? EnergyConnection.OUT : EnergyConnection.NON, side);
//                }
//            }
//
//            for (TileTurbineOutlet outlet : getParts(TileTurbineOutlet.class)) {
//                for (Direction side : Direction.values()) {
//                    outlet.setTankSorption(side, 0, side == this.flowDir ? TankSorption.OUT : TankSorption.NON);
//                }
//            }
        }

        Direction oppositeDir = this.flowDir.getOpposite();
        int flowLength = this.getFlowLength(), bladeLength = this.bladeLength, shaftWidth = this.shaftWidth;

        this.inputPlane[0] = this.getInteriorPlane(oppositeDir, 0, 0, 0, bladeLength, shaftWidth + bladeLength);
        this.inputPlane[1] = this.getInteriorPlane(oppositeDir, 0, shaftWidth + bladeLength, 0, 0, bladeLength);
        this.inputPlane[2] = this.getInteriorPlane(oppositeDir, 0, bladeLength, shaftWidth + bladeLength, 0, 0);
        this.inputPlane[3] = this.getInteriorPlane(oppositeDir, 0, 0, bladeLength, shaftWidth + bladeLength, 0);

        if (!getWorld().isClientSide) {
            this.renderPosArray = new Vector3f[(1 + 4 * shaftWidth) * flowLength];
            BlockPos pos = controller.getPos().relative(flowDir, -1);
            Vector3f position = new Vector3f(pos.getX(),pos.getY(),pos.getZ());

            for (int depth = 0; depth < flowLength; ++depth) {
                for (int w = 0; w < shaftWidth; ++w) {
                    this.renderPosArray[w + depth * shaftWidth] = this.getMiddleInteriorPlaneCoord(oppositeDir, depth, 1 + w + bladeLength, 0, shaftWidth - w + bladeLength, shaftWidth + bladeLength).sub(position);//.add(-0.3f, 1f, 1.85f); // Code was absolute now needs to be relative
                    this.renderPosArray[w + (depth + flowLength) * shaftWidth] = this.getMiddleInteriorPlaneCoord(oppositeDir, depth, 0, shaftWidth - w + bladeLength, shaftWidth + bladeLength, 1 + w + bladeLength);//.sub(position).sub(0.3f, 0, 1f);
                    this.renderPosArray[w + (depth + 2 * flowLength) * shaftWidth] = this.getMiddleInteriorPlaneCoord(oppositeDir, depth, shaftWidth + bladeLength, 1 + w + bladeLength, 0, shaftWidth - w + bladeLength);//.sub(position).add(-0.3f, 0, 1f);
                    this.renderPosArray[w + (depth + 3 * flowLength) * shaftWidth] = this.getMiddleInteriorPlaneCoord(oppositeDir, depth, shaftWidth - w + bladeLength, shaftWidth + bladeLength, 1 + w + bladeLength, 0);//.sub(position).sub(0.3f, 1f, 1.85f);
                }
                this.renderPosArray[depth + 4 * flowLength * shaftWidth] = this.getMiddleInteriorPlaneCoord(oppositeDir, depth, 0, 0, 0, 0);//.sub(position);
            }

            this.sendMultiblockUpdatePacketToAll();
            this.markReferenceCoordForUpdate();
        }
    }

    public int getFlowLength() {
        return getInteriorLength(flowDir);
    }


    protected void refreshDynamos() {
        searchFlag = false;

//        if (getPartMap(TileTurbineDynamoPart.class).isEmpty()) { TODO
//            this.conductivity = 0D;
//            return;
//        }
//
//        for (TileTurbineDynamoPart dynamoPart : getParts(TileTurbineDynamoPart.class)) {
//            dynamoPart.isSearched = dynamoPart.isInValidPosition = false;
//        }
//
//        dynamoPartCache.clear();
//        dynamoPartCacheOpposite.clear();
//
//        for (TileTurbineDynamoPart dynamoPart : getParts(TileTurbineDynamoPart.class)) {
//            if (dynamoPart.isSearchRoot()) {
//                iterateDynamoSearch(dynamoPart, dynamoPart.getPartPosition().getFacing() == this.flowDir ? dynamoPartCache : dynamoPartCacheOpposite);
//            }
//        }
//
//        for (TileTurbineDynamoPart dynamoPart : assumedValidCache.values()) {
//            if (!dynamoPart.isInValidPosition) {
//                componentFailCache.put(dynamoPart.getPos().toLong(), dynamoPart);
//                searchFlag = true;
//            }
//        }
    }

//    protected void iterateDynamoSearch(TileTurbineDynamoPart rootDynamoPart, ObjectSet<TileTurbineDynamoPart> currentDynamoPartCache) { TODO
//        final ObjectSet<TileTurbineDynamoPart> searchCache = new ObjectOpenHashSet<>();
//        rootDynamoPart.dynamoSearch(currentDynamoPartCache, searchCache, componentFailCache, assumedValidCache);
//
//        do {
//            final Iterator<TileTurbineDynamoPart> searchIterator = searchCache.iterator();
//            final ObjectSet<TileTurbineDynamoPart> searchSubCache = new ObjectOpenHashSet<>();
//            while (searchIterator.hasNext()) {
//                TileTurbineDynamoPart component = searchIterator.next();
//                searchIterator.remove();
//                component.dynamoSearch(currentDynamoPartCache, searchSubCache, componentFailCache, assumedValidCache);
//            }
//            searchCache.addAll(searchSubCache);
//        }
//        while (!searchCache.isEmpty());
//    }

    protected void refreshDynamoStats() {
        this.dynamoCoilCount = this.dynamoCoilCountOpposite = 0;
        double newConductivity = 0D, newConductivityOpposite = 0D;
//        for (TileTurbineDynamoPart dynamoPart : dynamoPartCache) { TODO
//            if (dynamoPart.conductivity != null) {
//                ++this.dynamoCoilCount;
//                newConductivity += dynamoPart.conductivity;
//            }
//        }
//        for (TileTurbineDynamoPart dynamoPart : dynamoPartCacheOpposite) {
//            if (dynamoPart.conductivity != null) {
//                ++this.dynamoCoilCountOpposite;
//                newConductivityOpposite += dynamoPart.conductivity;
//            }
//        }

        int bearingCount = 0; //getPartsCount(TileTurbineRotorBearing.class);
        newConductivity = this.dynamoCoilCount == 0 ? 0D : newConductivity / Math.max(bearingCount / 2D, this.dynamoCoilCount);
        newConductivityOpposite = this.dynamoCoilCountOpposite == 0 ? 0D : newConductivityOpposite / Math.max(bearingCount / 2D, this.dynamoCoilCountOpposite);

        this.conductivity = (newConductivity + newConductivityOpposite) / 2D;
    }

    public void onTurbineBroken() {
        makeRotorVisible();

        setIsTurbineOn();

        this.isProcessing = false;
        if (this.controller != null) {
//            this.controller.setActivity(false); TODO
        }
        this.power = this.rawPower = this.conductivity = this.rotorEfficiency = 0D;
        this.angVel = this.rotorAngle = 0F;
        this.flowDir = null;
        this.shaftWidth = this.inertia = this.bladeLength = this.noBladeSets = this.recipeInputRate = 0;
        this.totalExpansionLevel = 1D;
        this.idealTotalExpansionLevel = 0D;
        this.minBladeExpansionCoefficient = Double.MAX_VALUE;
        this.maxBladeExpansionCoefficient = 1D;
        this.minStatorExpansionCoefficient = 1D;
        this.maxStatorExpansionCoefficient = Double.MIN_VALUE;
        this.particleEffect = ParticleTypes.CLOUD;
        this.particleSpeedMult = 5D / 116D;
        this.basePowerPerMB = this.recipeInputRateFP = 0D;
        this.expansionLevels.clear();
        this.rawBladeEfficiencies.clear();
        this.inputPlane[0] = this.inputPlane[1] = this.inputPlane[2] = this.inputPlane[3] = null;

//        for (TileTurbineDynamoPart dynamoPart : getParts(TileTurbineDynamoPart.class)) {
//            dynamoPart.isSearched = dynamoPart.isInValidPosition = false;
//        }

        if (getWorld().isClientSide) {
            clearSounds();
        }
    }

    protected void makeRotorVisible() {
//        this.shouldSpecialRenderRotor = false;TODO

        if (this.flowDir != null) {
            TurbinePartDir shaftDir = this.getShaftDir();
            for (TurbineRotorShaftEntity shaft : getParts(TurbineRotorShaftEntity.class)) {
                BlockPos pos = shaft.getWorldPosition();
                BlockState state = getWorld().getBlockState(pos);
                if (state.getBlock() instanceof TurbineRotorShaftBlock) {
                    getWorld().setBlock(pos, state.setValue(TurbineRotorBladeUtil.DIR, shaftDir), 3);
                }
            }

            for (TurbineRotorBladeEntity blade : getParts(TurbineRotorBladeEntity.class)) {
                BlockPos pos = blade.bladePos();
                BlockState state = getWorld().getBlockState(pos);
                if (state.getBlock() instanceof IBlockRotorBlade) {
                    getWorld().setBlock(pos, state.setValue(TurbineRotorBladeUtil.DIR, blade.getDir()), 3);
                }
            }

            for (TurbineRotorStatorEntity stator : getParts(TurbineRotorStatorEntity.class)) {
                BlockPos pos = stator.bladePos();
                BlockState state = getWorld().getBlockState(pos);
                if (state.getBlock() instanceof IBlockRotorBlade) {
                    getWorld().setBlock(pos, state.setValue(TurbineRotorBladeUtil.DIR, stator.getDir()), 3);
                }
            }
        }
    }

    protected void bearingFailure() {
        makeRotorVisible();

        this.bearingTension = 0D;

        Iterator<TurbineRotorBearingEntity> bearingIterator = getPartIterator(TurbineRotorBearingEntity.class);
        while (bearingIterator.hasNext()) {
            TurbineRotorBearingEntity bearing = bearingIterator.next();
            bearing.onBearingFailure(bearingIterator);
        }

        Iterator<TurbineRotorBladeEntity> bladeIterator = getPartIterator(TurbineRotorBladeEntity.class);
        while (bladeIterator.hasNext()) {
            TurbineRotorBladeEntity blade = bladeIterator.next();
            blade.onBearingFailure(bladeIterator);
        }

        Iterator<TurbineRotorStatorEntity> statorIterator = getPartIterator(TurbineRotorStatorEntity.class);
        while (statorIterator.hasNext()) {
            TurbineRotorStatorEntity stator = statorIterator.next();
            stator.onBearingFailure(statorIterator);
        }

//        MultiblockRegistry.INSTANCE.addDirtyMultiblock(getWorld(), this);
        MultiblockRegistry.INSTANCE.get().addDirtyController(this);


        if (this.controller != null) {
            this.sendMultiblockUpdatePacketToAll();
        }
    }

    protected boolean isRedstonePowered() {
//        return Stream.concat(Stream.of(this.controller), getParts(TileTurbineRedstonePort.class).stream()).anyMatch(x -> x != null && x.getIsRedstonePowered()); TODO
        return false;
    }

    protected void refreshRecipe() {
//        this.recipeInfo = NCRecipes.turbine.getRecipeInfoFromInputs(Collections.emptyList(), this.tanks.subList(0, 1)); TODO
        this.recipe = this.getWorld().getRecipeManager().getAllRecipesFor(TURBINE_RECIPE_TYPE.get()).stream().filter(it -> it.value().fluidResult().test(this.tanks.getFirst().getFluid())).findFirst();
    }

    protected boolean canProcessInputs() {
        if (!setRecipeStats() || !this.isTurbineOn) {
            return false;
        }
        return canProduceProducts();
    }

    protected boolean setRecipeStats() {
        if (this.recipe.isEmpty()) {
            this.recipeInputRate = 0;
            this.idealTotalExpansionLevel = 0D;
            return false;
        }
        var recipe = this.recipe.get().value();
        this.basePowerPerMB = recipe.power_per_mb();
        this.idealTotalExpansionLevel = recipe.expansion_level();
        this.spinUpMultiplier = recipe.spin_up_multiplier();
        this.particleEffect = recipe.particle();
        this.particleSpeedMult = recipe.particle_speed_mult();
        return true;
    }

    protected boolean canProduceProducts() {
//        FluidIngredient fluidProduct = this.recipeInfo.recipe.getFluidProducts().get(0); TODO
//        if (fluidProduct.getMaxStackSize(0) <= 0 || fluidProduct.getStack() == null) {
//            return false;
//        }

        Tank outputTank = this.tanks.get(1);

        if (!outputTank.isEmpty()) {
//            if (!outputTank.getFluid().isFluidEqual(fluidProduct.getStack())) {
//                return false;
//            } else if (outputTank.getFluidAmount() + fluidProduct.getMaxStackSize(0) * this.recipeInputRate > outputTank.getCapacity()) {
//                return false;
//            }
        }


        return true;
    }

    protected void produceProducts() {
        Tank inputTank = this.tanks.get(0), outputTank = this.tanks.get(1);

        int fluidIngredientSize = getFluidIngredientStackSize() * this.recipeInputRate;
        if (fluidIngredientSize > 0) {
            inputTank.changeFluidAmount(-fluidIngredientSize);
        }
        if (inputTank.getFluidAmount() <= 0) {
            inputTank.setFluid(FluidStack.EMPTY);
        }

        SizedFluidIngredient fluidProduct = this.recipe.get().value().fluidResult();
        FluidStack fluid = fluidProduct.getFluids()[0];
        if (fluid.getAmount() <= 0) {
            return;
        }
        if (outputTank.isEmpty()) {
            outputTank.setFluid(fluid);
            outputTank.setFluidAmount(outputTank.getFluidAmount() * this.recipeInputRate);
        } else if (FluidStack.isSameFluidSameComponents(outputTank.getFluid(), fluid)) {
            outputTank.changeFluidAmount(fluid.getAmount() * this.recipeInputRate);
        }
    }

    protected int getFluidIngredientStackSize() {
//        return this.recipe.isEmpty() ? 0 : this.recipe.get().value().fluidInput().getMaxStackSize(this.recipeInfo.getFluidIngredientNumbers().get(0)); TODO
        return this.recipe.isEmpty() ? 0 : this.recipe.get().value().fluidInput().getFluids()[0].getAmount();
    }

    protected int getBladeArea() {
        return 4 * shaftWidth * bladeLength;
    }

    protected int getBladeVolume() {
        return getBladeArea() * noBladeSets;
    }

    public double getRotorRadius() {
        return bladeLength + shaftWidth / 2D;
    }

    public int getMinimumBladeArea() {
        return 4 * Math.max(1, getMinimumInteriorLength() - 2);
    }

    // Modified Kurtchekov stuff!

    protected ITurbineRotorBlade<?> getBlade(BlockPos pos) {
        long posLong = pos.asLong();
        TurbineRotorBladeEntity blade = getPartMap(TurbineRotorBladeEntity.class).get(posLong);
        return blade == null ? getPartMap(TurbineRotorStatorEntity.class).get(posLong) : blade;
    }

    public TurbinePartDir getShaftDir() {
        if (flowDir == null) {
            return TurbinePartDir.Y;
        }
        return switch (flowDir.getAxis()) {
            case Y -> TurbinePartDir.Y;
            case Z -> TurbinePartDir.Z;
            case X -> TurbinePartDir.X;
        };
    }

    public TurbinePartDir getBladeDir(PlaneDir planeDir) {
        if (flowDir == null) {
            return TurbinePartDir.Y;
        }
        switch (flowDir.getAxis()) {
            case Y:
                switch (planeDir) {
                    case U:
                        return TurbinePartDir.Z;
                    case V:
                        return TurbinePartDir.X;
                    default:
                        break;
                }
                break;
            case Z:
                switch (planeDir) {
                    case U:
                        return TurbinePartDir.X;
                    case V:
                        return TurbinePartDir.Y;
                    default:
                        break;
                }
                break;
            case X:
                switch (planeDir) {
                    case U:
                        return TurbinePartDir.Y;
                    case V:
                        return TurbinePartDir.Z;
                    default:
                        break;
                }
                break;
            default:
                return TurbinePartDir.Y;
        }
        return TurbinePartDir.Y;
    }

    @Override
    public CustomPacketPayload getMultiblockUpdatePacket() {
        return new TurbineRenderPayload(controller.getPos(), tanks.stream().map(FluidTank::getFluid).toList(), particleEffect, particleSpeedMult, angVel, isProcessing, recipeInputRate, recipeInputRateFP);
    }

    public enum PlaneDir {
        U,
        V
    }

    // End of modified Kurtchekov stuff!

    public int getMaxRecipeRateMultiplier() {
        return this.getBladeVolume() * turbine_mb_per_blade;
    }

    public double getRawLimitProcessPower(int recipeInputRate) {
        return this.noBladeSets == 0 ? 0D : recipeInputRate * this.basePowerPerMB;
    }

    public double getNewRawProcessPower(double previousRawPower, double maxLimitPower, boolean increasing) {
        double effectiveInertia = getEffectiveInertia(increasing);
        if (increasing) {
            return (effectiveInertia * previousRawPower + maxLimitPower * this.spinUpMultiplier) / (effectiveInertia + this.spinUpMultiplier);
        } else {
            return effectiveInertia * previousRawPower / (turbine_spin_down_multiplier * (effectiveInertia + Math.log1p(effectiveInertia) + 1D));
        }
    }

    public double getEffectiveInertia(boolean increasing) {
        int bearingCount = 0; //getPartsCount(TileTurbineRotorBearing.class); TODO
        double mult = (Math.min(1D, (1D + 2D * this.dynamoCoilCount) / bearingCount) + Math.min(1D, (1D + 2D * this.dynamoCoilCountOpposite) / bearingCount)) / 2D;
        return this.inertia * Math.sqrt(increasing ? mult : 1D / mult);
    }

    public int getMinX() {
        return getMinimumCoord().get().getX();
    }

    public int getMinY() {
        return getMinimumCoord().get().getY();
    }

    public int getMinZ() {
        return getMinimumCoord().get().getZ();
    }

    public int getMaxX() {
        return getMaximumCoord().get().getX();
    }

    public int getMaxY() {
        return getMaximumCoord().get().getY();
    }

    public int getMaxZ() {
        return getMaximumCoord().get().getZ();
    }

    public int getMiddleX() {
        return NCMath.toInt(((long) getMinX() + (long) getMaxX()) / 2);
    }

    public int getMiddleY() {
        return NCMath.toInt(((long) getMinY() + (long) getMaxY()) / 2);
    }

    public int getMiddleZ() {
        return NCMath.toInt(((long) getMinZ() + (long) getMaxZ()) / 2);
    }

    public BlockPos getMiddleCoord() {
        return new BlockPos(getMiddleX(), getMiddleY(), getMiddleZ());
    }

    public void setRotorEfficiency() {
        this.rotorEfficiency = 0D;

        for (int depth = 0; depth < this.getFlowLength(); ++depth) {
            if (this.rawBladeEfficiencies.get(depth) < 0D) {
                continue;
            }
            this.rotorEfficiency += this.rawBladeEfficiencies.get(depth) * getExpansionIdealityMultiplier(getIdealExpansionLevel(depth), this.expansionLevels.get(depth));
        }
        this.rotorEfficiency /= this.noBladeSets;
    }

    public static double getExpansionIdealityMultiplier(double ideal, double actual) {
        if (ideal <= 0D || actual <= 0D) {
            return 0D;
        }
        return ideal < actual ? ideal / actual : actual / ideal;
    }

    public double getIdealExpansionLevel(int depth) {
        return Math.pow(this.idealTotalExpansionLevel, (depth + 0.5D) / this.getFlowLength());
    }

    public DoubleList getIdealExpansionLevels() {
        DoubleList levels = new DoubleArrayList();
        if (this.flowDir == null) {
            return levels;
        }
        for (int depth = 0; depth < this.getFlowLength(); ++depth) {
            levels.add(getIdealExpansionLevel(depth));
        }
        return levels;
    }

    public double getThroughputEfficiency() {
        double effectiveMinLength = this.idealTotalExpansionLevel <= 1D || this.maxBladeExpansionCoefficient <= 1D ? getMaximumInteriorLength() : Math.ceil(Math.log(this.idealTotalExpansionLevel) / Math.log(this.maxBladeExpansionCoefficient));
        double absoluteLeniency = effectiveMinLength * this.getMinimumBladeArea() * turbine_mb_per_blade;
        int maxRecipeRateMultiplier = getMaxRecipeRateMultiplier();
        double throughputRatio = maxRecipeRateMultiplier == 0 ? 1D : Math.min(1D, (this.recipeInputRateFP + absoluteLeniency) / maxRecipeRateMultiplier);
        return throughputRatio >= turbine_throughput_leniency_params[1] ? 1D : (1D - turbine_throughput_leniency_params[0]) * Math.sin(throughputRatio * Math.PI / (2D * turbine_throughput_leniency_params[1])) + turbine_throughput_leniency_params[0];
    }

    public void setEffectiveMaxLength() {
        if (this.minBladeExpansionCoefficient <= 1D || this.minStatorExpansionCoefficient >= 1D) {
            this.effectiveMaxLength = getMaximumInteriorLength();
        } else {
            this.effectiveMaxLength = NCMath.toInt(Math.ceil(Mth.clamp((Math.log(this.idealTotalExpansionLevel) - getMaximumInteriorLength() * Math.log(this.minStatorExpansionCoefficient)) / (Math.log(this.minBladeExpansionCoefficient) - Math.log(this.minStatorExpansionCoefficient)), 1D, getMaximumInteriorLength())));
        }
    }

    public void setInputRatePowerBonus() {
        double rate = Math.min(this.recipeInputRate, getMaxRecipeRateMultiplier());
        double lengthBonus = rate / (turbine_mb_per_blade * this.getBladeArea() * this.effectiveMaxLength);
        double areaBonus = Math.sqrt(2D * rate / (turbine_mb_per_blade * this.getFlowLength() * getMaximumInteriorLength() * this.effectiveMaxLength));
        this.powerBonus = 1D + turbine_power_bonus_multiplier * Math.pow(lengthBonus * areaBonus, 2D / 3D);
    }

    public void setTanks(List<FluidStack> fluids) {
        assert fluids.size() == tanks.size();
        for (int i = 0; i < fluids.size(); i++) {
            tanks.get(i).setFluid(fluids.get(i));
        }
    }

    // Client
    protected Object2ObjectMap<BlockPos, SoundInstance> getSoundMap() {
        if (this.soundMap == null) {
            this.soundMap = new Object2ObjectOpenHashMap<>();
        }
        return this.soundMap;
    }

    @OnlyIn(Dist.CLIENT)
    protected void updateSounds() {
        if (turbine_sound_volume == 0D) {
            clearSounds();
            return;
        }

        if (this.isProcessing && this.isAssembled()) {
            double ratio = (NCMath.EPSILON + Math.abs(this.angVel)) / (NCMath.EPSILON + Math.abs(prevAngVel));
            this.refreshSounds |= ratio < 0.8D || ratio > 1.25D || getSoundMap().isEmpty();

            if (!this.refreshSounds) {
                return;
            }
            this.refreshSounds = false;

            // Generate sound info if necessary

            clearSounds();

            final BlockPos minPos = this.getMinimumCoord().get();
            final BlockPos midPos = this.getMiddleCoord();
            final BlockPos maxPos = this.getMaximumCoord().get();

            final int lengthX = this.getExteriorLengthX();
            final int lengthY = this.getExteriorLengthY();
            final int lengthZ = this.getExteriorLengthZ();

            final int[] _x, _y, _z;

            if (lengthX > 8) {
                final int powX = NCMath.toInt(Math.pow(lengthX, 0.4D));
                _x = new int[]{minPos.getX() + powX, maxPos.getX() - powX};
            } else {
                _x = new int[]{midPos.getX()};
            }

            if (lengthY > 8) {
                final int powY = NCMath.toInt(Math.pow(lengthY, 0.4D));
                _y = new int[]{minPos.getY() + powY, maxPos.getY() - powY};
            } else {
                _y = new int[]{midPos.getY()};
            }

            if (lengthZ > 8) {
                final int powZ = NCMath.toInt(Math.pow(lengthZ, 0.4D));
                _z = new int[]{minPos.getZ() + powZ, maxPos.getZ() - powZ};
            } else {
                _z = new int[]{midPos.getZ()};
            }

            for (int i : _x) {
                for (int j : _y) {
                    for (int k : _z) {
                        BlockPos pos = new BlockPos(i, j, k);
                        getSoundMap().put(pos, SoundHandler.startBlockSound(SoundRegistration.turbine_run.get(), pos, (float) ((1D + this.angVel * 2D / turbine_render_rotor_speed) * turbine_sound_volume / 32D), SoundHelper.getPitch(4F * this.angVel / turbine_render_rotor_speed - 2F)));
                    }
                }
            }

            prevAngVel = this.angVel;
        } else {
            this.refreshSounds = true;
            clearSounds();
        }
    }

    @OnlyIn(Dist.CLIENT)
    protected void clearSounds() {
        getSoundMap().forEach((k, v) -> SoundHandler.stopBlockSound(k));
        getSoundMap().clear();
    }

    @OnlyIn(Dist.CLIENT)
    protected void updateParticles() {
        if (this.isProcessing && this.isAssembled() && !Minecraft.getInstance().isPaused()) {
            // Particles will just reach the outlets at this speed
            double flowSpeed = this.getFlowLength() * this.particleSpeedMult;
            double offsetX = particleSpeedOffset(), offsetY = particleSpeedOffset(), offsetZ = particleSpeedOffset();

            double speedX = this.flowDir == Direction.WEST ? -flowSpeed : this.flowDir == Direction.EAST ? flowSpeed : offsetX;
            double speedY = this.flowDir == Direction.DOWN ? -flowSpeed : this.flowDir == Direction.UP ? flowSpeed : offsetY;
            double speedZ = this.flowDir == Direction.NORTH ? -flowSpeed : this.flowDir == Direction.SOUTH ? flowSpeed : offsetZ;

            int maxRecipeRateMultiplier = getMaxRecipeRateMultiplier();

            if (maxRecipeRateMultiplier > 0) {
                for (Iterable<BlockPos> iter : this.inputPlane) {
                    if (iter != null) {
                        for (BlockPos pos : iter) {
                            if (rand.nextDouble() < turbine_particles * this.recipeInputRateFP / maxRecipeRateMultiplier) {
                                double[] spawnPos = particleSpawnPos(pos);
                                if (spawnPos != null) {
                                    getWorld().addParticle(this.particleEffect, false, spawnPos[0], spawnPos[1], spawnPos[2], speedX, speedY, speedZ);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    protected double particleSpeedOffset() {
        return (rand.nextDouble() - 0.5D) / (4D * Math.sqrt(this.getFlowLength()));
    }

    @OnlyIn(Dist.CLIENT)
    protected double[] particleSpawnPos(BlockPos pos) {
        double offsetU = 0.5D + (rand.nextDouble() - 0.5D) / 2D;
        double offsetV = 0.5D + (rand.nextDouble() - 0.5D) / 2D;
        return switch (this.flowDir) {
            case DOWN -> new double[]{pos.getX() + offsetV, pos.getY() + 1D, pos.getZ() + offsetU};
            case UP -> new double[]{pos.getX() + offsetV, pos.getY(), pos.getZ() + offsetU};
            case NORTH -> new double[]{pos.getX() + offsetU, pos.getY() + offsetV, pos.getZ() + 1D};
            case SOUTH -> new double[]{pos.getX() + offsetU, pos.getY() + offsetV, pos.getZ()};
            case WEST -> new double[]{pos.getX() + 1D, pos.getY() + offsetU, pos.getZ() + offsetV};
            case EAST -> new double[]{pos.getX(), pos.getY() + offsetU, pos.getZ() + offsetV};
        };
    }

    @OnlyIn(Dist.CLIENT)
    protected void updateRenderData() {
        int flowLength = this.getFlowLength();
        if (flowLength < 1 || this.bladePosArray == null || this.renderPosArray == null || this.bladeAngleArray == null || this.bladePosArray.length < 4 * flowLength) {
            this.bladePosArray = null;
            this.renderPosArray = null;
            this.bladeAngleArray = null;
        } else {
            this.rotorStateArray = new BlockState[1 + 4 * flowLength];
            this.rotorStateArray[4 * flowLength] = TURBINE_MAP.get("turbine_rotor_shaft").get().defaultBlockState().setValue(TurbineRotorBladeUtil.DIR, this.getShaftDir());

            for (int i = 0; i < this.bladePosArray.length; ++i) {
                BlockPos pos = this.bladePosArray[i];
                ITurbineRotorBlade<?> thisBlade = this.getBlade(pos);
                this.rotorStateArray[i] = thisBlade == null ? getWorld().getBlockState(pos).getBlock().defaultBlockState() : thisBlade.getRenderState();
            }

            this.bladeDepths.clear();
            this.statorDepths.clear();

            for (int i = 0; i < flowLength; ++i) {
                if (this.getBlade(this.bladePosArray[i]).getBladeType() instanceof IRotorStatorType) {
                    this.statorDepths.add(i);
                } else {
                    this.bladeDepths.add(i);
                }
            }
        }
    }
}