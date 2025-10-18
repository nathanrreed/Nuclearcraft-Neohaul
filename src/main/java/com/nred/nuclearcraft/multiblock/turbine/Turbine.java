package com.nred.nuclearcraft.multiblock.turbine;

import com.google.common.collect.Lists;
import com.nred.nuclearcraft.block_entity.internal.energy.EnergyStorage;
import com.nred.nuclearcraft.block_entity.internal.fluid.Tank;
import com.nred.nuclearcraft.block_entity.turbine.ITurbineController;
import com.nred.nuclearcraft.block_entity.turbine.TurbineRotorBladeEntity;
import com.nred.nuclearcraft.block_entity.turbine.TurbineRotorStatorEntity;
import com.nred.nuclearcraft.handler.NCRecipes;
import com.nred.nuclearcraft.multiblock.ILogicMultiblock;
import com.nred.nuclearcraft.multiblock.IPacketMultiblock;
import com.nred.nuclearcraft.multiblock.Multiblock;
import com.nred.nuclearcraft.multiblock.turbine.TurbineRotorBladeUtil.ITurbineRotorBlade;
import com.nred.nuclearcraft.multiblock.turbine.TurbineRotorBladeUtil.TurbinePartDir;
import com.nred.nuclearcraft.payload.multiblock.TurbineRenderPacket;
import com.nred.nuclearcraft.payload.multiblock.TurbineUpdatePacket;
import com.nred.nuclearcraft.recipe.RecipeInfo;
import com.nred.nuclearcraft.recipe.turbine.TurbineRecipe;
import com.nred.nuclearcraft.util.NBTHelper;
import com.nred.nuclearcraft.util.NCMath;
import it.unimi.dsi.fastutil.doubles.DoubleArrayList;
import it.unimi.dsi.fastutil.doubles.DoubleList;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import it.zerono.mods.zerocore.lib.multiblock.IMultiblockController;
import it.zerono.mods.zerocore.lib.multiblock.IMultiblockPart;
import it.zerono.mods.zerocore.lib.multiblock.validation.IMultiblockValidator;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.fluids.FluidStack;
import org.joml.Vector3f;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Set;

import static com.nred.nuclearcraft.NuclearcraftNeohaul.MODID;
import static com.nred.nuclearcraft.config.Config2.turbine_max_size;

public class Turbine extends Multiblock<Turbine> implements ILogicMultiblock<Turbine, TurbineLogic>, IPacketMultiblock<Turbine, TurbineUpdatePacket> {
    protected @Nonnull TurbineLogic logic = new TurbineLogic(this);
    public ITurbineController<?> controller;

    public static final int BASE_MAX_ENERGY = 16000, BASE_MAX_INPUT = 1000, BASE_MAX_OUTPUT = 4000;

    public final EnergyStorage energyStorage = new EnergyStorage(BASE_MAX_ENERGY);
    public final List<Tank> tanks = Lists.newArrayList(new Tank(BASE_MAX_INPUT, (Set<ResourceLocation>) NCRecipes.turbine.validFluids.get(0)), new Tank(BASE_MAX_OUTPUT, null));

    public RecipeInfo<TurbineRecipe> recipeInfo;

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
    public Object2ObjectMap<BlockPos, SoundInstance> soundMap;
    public boolean refreshSounds = true;

    public String particleEffect = "cloud";
    public double particleSpeedMult = 5D / 116D;
    public float angVel = 0F, rotorAngle = 0F;

    @SuppressWarnings("unchecked")
    public Iterable<BlockPos>[] inputPlane = new Iterable[4];

    public boolean nbtUpdateRenderDataFlag = false;
    public boolean shouldSpecialRenderRotor = false;

    public BlockPos[] bladePosArray = null;
    public Vector3f[] renderPosArray = null;
    public float[] bladeAngleArray = null;

    public BlockState[] rotorStateArray = null;
    public final IntList bladeDepths = new IntArrayList(), statorDepths = new IntArrayList();

    protected final Set<Player> updatePacketListeners = new ObjectOpenHashSet<>();

    public Turbine(Level world) {
        super(world);
    }

    @Override
    public @Nonnull TurbineLogic getLogic() {
        return logic;
    }

    @Override
    public void setLogic(String logicID) {
        if (logicID.equals(logic.getID())) {
            return;
        }
        logic = getNewLogic(TurbineLogic::new);
    }

    // Multiblock Size Limits

    @Override
    public int getMinimumInteriorLength() {
        return logic.getMinimumInteriorLength();
    }

    @Override
    public int getMaximumInteriorLength() {
        return logic.getMaximumInteriorLength();
    }

    @Override
    protected void onMachineAssembled() {
        logic.onMachineAssembled();
    }

    @Override
    protected void onMachineRestored() {
        logic.onMachineRestored();
    }

    @Override
    protected void onMachinePaused() {
        logic.onMachinePaused();
    }

    @Override
    protected void onMachineDisassembled() {
        logic.onMachineDisassembled();
    }


    @Override
    protected int getMinimumNumberOfPartsForAssembledMachine() {
        return NCMath.hollowCuboid(Math.max(5, getMinimumInteriorLength() + 2), Math.max(5, getMinimumInteriorLength() + 2), getMinimumInteriorLength() + 2);
    }

    @Override
    protected boolean isMachineWhole(IMultiblockValidator validatorCallback) {
        shouldSpecialRenderRotor = false;

        return setLogic(this) && super.isMachineWhole(validatorCallback) && logic.isMachineWhole();
    }

    public boolean setLogic(Turbine multiblock) {
        if (getPartMap(ITurbineController.class).isEmpty()) {
            multiblock.setLastError(MODID + ".multiblock_validation.no_controller");
            return false;
        }
        if (getPartCount(ITurbineController.class) > 1) {
            multiblock.setLastError(MODID + ".multiblock_validation.too_many_controllers");
            return false;
        }

        for (ITurbineController<?> contr : getParts(ITurbineController.class)) {
            controller = contr;
            break;
        }

        setLogic(controller.getLogicID());

        return true;
    }

    @Override
    protected void onAssimilate(IMultiblockController<Turbine> assimilated) {
        logic.onAssimilate(assimilated);
    }

    @Override
    protected void onAssimilated(IMultiblockController<Turbine> assimilator) {
        super.onAssimilated(assimilator);
        logic.onAssimilate(assimilator);
    }

    public int getFlowLength() {
        return getInteriorLength(flowDir);
    }

    public int getBladeArea() {
        return 4 * shaftWidth * bladeLength;
    }

    public int getBladeVolume() {
        return getBladeArea() * noBladeSets;
    }

    public double getRotorRadius() {
        return bladeLength + shaftWidth / 2D;
    }

    public int getMinimumBladeArea() {
        return 4 * Math.max(1, getMinimumInteriorLength() - 2);
    }

    // Modified Kurtchekov stuff!

    public ITurbineRotorBlade<?> getBlade(BlockPos pos) {
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

    public enum PlaneDir {
        U,
        V
    }

    // End of modified Kurtchekov stuff!

    // Server

    @Override
    protected boolean updateServer() {
        super.updateServer();
        return logic.onUpdateServer();
    }

    @Override
    protected void onPartAdded(IMultiblockPart<Turbine> iMultiblockPart) {
        super.onPartAdded(iMultiblockPart);
    }

    @Override
    protected void onPartRemoved(IMultiblockPart<Turbine> iMultiblockPart) {
        super.onPartRemoved(iMultiblockPart);
    }

    // Client

    @Override
    protected void updateClient() {
        logic.onUpdateClient();
    }

    // NBT

    @Override
    public CompoundTag syncDataTo(CompoundTag data, HolderLookup.Provider registries, SyncReason syncReason) {
        writeEnergy(energyStorage, data, registries, "energyStorage");
        writeTanks(tanks, data, registries, "tanks");
        data.putBoolean("isTurbineOn", isTurbineOn);
        data.putBoolean("computerActivated", computerActivated);
        data.putBoolean("isProcessing", isProcessing);
        data.putDouble("power", power);
        data.putDouble("rawPower", rawPower);

        data.putInt("flowDir", flowDir == null ? -1 : flowDir.ordinal());

        data.putDouble("conductivity", conductivity);
        data.putDouble("rotorEfficiency", rotorEfficiency);
        data.putDouble("powerBonus", powerBonus);
        data.putString("particleEffect", particleEffect);
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

        writeLogicNBT(data, registries, syncReason);

        return data;
    }

    @Override
    public void syncDataFrom(CompoundTag data, HolderLookup.Provider registries, SyncReason syncReason) {
        readEnergy(energyStorage, data, registries, "energyStorage");
        readTanks(tanks, data, registries, "tanks");
        isTurbineOn = data.getBoolean("isTurbineOn");
        computerActivated = data.getBoolean("computerActivated");
        isProcessing = data.getBoolean("isProcessing");
        power = data.getDouble("power");
        rawPower = data.getDouble("rawPower");

        flowDir = data.getInt("flowDir") < 0 ? null : Direction.from3DDataValue(data.getInt("flowDir"));

        conductivity = data.getDouble("conductivity");
        rotorEfficiency = data.getDouble("rotorEfficiency");
        powerBonus = data.getDouble("powerBonus");
        particleEffect = data.getString("particleEffect");
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

        readLogicNBT(data, registries, syncReason);
    }

    // Packets

    @Override
    public Set<Player> getMultiblockUpdatePacketListeners() {
        return updatePacketListeners;
    }

    @Override
    public TurbineUpdatePacket getMultiblockUpdatePacket() {
        return logic.getMultiblockUpdatePacket();
    }

    @Override
    public void onMultiblockUpdatePacket(TurbineUpdatePacket message) {
        logic.onMultiblockUpdatePacket(message);
    }

    protected TurbineRenderPacket getRenderPacket() {
        return logic.getRenderPacket();
    }

    public void onRenderPacket(TurbineRenderPacket message) {
        logic.onRenderPacket(message);
    }

    public void sendRenderPacketToPlayer(Player player) {
        if (getWorld().isClientSide) {
            return;
        }
        TurbineRenderPacket packet = getRenderPacket();
        if (packet == null) {
            return;
        }
        packet.sendTo(player);
    }

    public void sendRenderPacketToAll() {
        if (getWorld().isClientSide) {
            return;
        }
        TurbineRenderPacket packet = getRenderPacket();
        if (packet == null) {
            return;
        }
        packet.sendToAll();
    }

    // Multiblock Validators

    @Override
    protected boolean isBlockGoodForFrame(Level level, int x, int y, int z, IMultiblockValidator iMultiblockValidator) {
        return false;
    }

    @Override
    protected boolean isBlockGoodForTop(Level level, int x, int y, int z, IMultiblockValidator iMultiblockValidator) {
        return false;
    }

    @Override
    protected boolean isBlockGoodForBottom(Level level, int x, int y, int z, IMultiblockValidator iMultiblockValidator) {
        return false;
    }

    @Override
    protected boolean isBlockGoodForSides(Level level, int x, int y, int z, IMultiblockValidator iMultiblockValidator) {
        return false;
    }

    @Override
    protected boolean isBlockGoodForInterior(Level level, int x, int y, int z, IMultiblockValidator iMultiblockValidator) {
        return logic.isBlockGoodForInterior(level, x, y, z);
    }

    // Clear Material

    @Override
    public void clearAllMaterial() {
        logic.clearAllMaterial();
        for (Tank tank : tanks) {
            tank.setFluidStored(FluidStack.EMPTY);
        }
        super.clearAllMaterial();
    }
}