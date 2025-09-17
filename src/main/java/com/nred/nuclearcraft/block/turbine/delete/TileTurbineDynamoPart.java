//package com.nred.nuclearcraft.block.turbine;
//
//import com.nred.nuclearcraft.info.energy.EnergyConnection;
//import com.nred.nuclearcraft.multiblock.turbine.Turbine;
//import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
//import it.unimi.dsi.fastutil.objects.*;
//import net.minecraft.core.Direction;
//import net.neoforged.neoforge.energy.EnergyStorage;
//
//import javax.annotation.Nonnull;
//import javax.annotation.Nullable;
//
//
//public abstract class TileTurbineDynamoPart extends TileTurbinePart implements ITileEnergy {
//
//	public static final Object2DoubleMap<String> DYN_CONDUCTIVITY_MAP = new Object2DoubleOpenHashMap<>();
//	public static final Object2ObjectMap<String, String> DYN_RULE_ID_MAP = new Object2ObjectOpenHashMap<>();
//
//	protected final EnergyStorage backupStorage = new EnergyStorage(0);
//
//	protected final EnergyConnection[] energyConnections = ITileEnergy.energyConnectionAll(EnergyConnection.OUT);
//
//	protected final EnergyTileWrapper[] energySides = ITileEnergy.getDefaultEnergySides(this);
//	protected final EnergyTileWrapperGT[] energySidesGT = ITileEnergy.getDefaultEnergySidesGT(this);
//
//	protected boolean ic2reg = false;
//
//	public String partName;
//	public Double conductivity;
//
//	public boolean isSearched = false, isInValidPosition = false;
//
//	public String ruleID;
//	public PlacementRule<Turbine, ITurbinePart> placementRule;
//
//	/**
//	 * Don't use this constructor!
//	 */
//	public TileTurbineDynamoPart() {
//		super(CuboidalPartPositionType.WALL);
//	}
//
//	public TileTurbineDynamoPart(String partName, Double conductivity, String ruleID) {
//		super(CuboidalPartPositionType.WALL);
//		this.partName = partName;
//		this.conductivity = conductivity;
//		this.ruleID = ruleID;
//		this.placementRule = TurbinePlacement.RULE_MAP.get(ruleID);
//	}
//
//	@Override
//	public void onMachineAssembled(Turbine multiblock) {
//		doStandardNullControllerResponse(multiblock);
//		super.onMachineAssembled(multiblock);
//	}
//
//	public void dynamoSearch(final ObjectSet<TileTurbineDynamoPart> validCache, final ObjectSet<TileTurbineDynamoPart> searchCache, final Long2ObjectMap<TileTurbineDynamoPart> partFailCache, final Long2ObjectMap<TileTurbineDynamoPart> assumedValidCache) {
//		if (!isDynamoPartValid(partFailCache, assumedValidCache)) {
//			return;
//		}
//
//		if (isSearched) {
//			return;
//		}
//
//		isSearched = true;
//		validCache.add(this);
//
//		for (Direction dir : Direction.values()) {
//			TileTurbineDynamoPart part = getMultiblock().getPartMap(TileTurbineDynamoPart.class).get(getTilePos().offset(dir).toLong());
//			if (part != null) {
//				searchCache.add(part);
//			}
//		}
//	}
//
//	public boolean isDynamoPartValid(final Long2ObjectMap<TileTurbineDynamoPart> partFailCache, final Long2ObjectMap<TileTurbineDynamoPart> assumedValidCache) {
//		if (partFailCache.containsKey(pos.toLong())) {
//			return isInValidPosition = false;
//		}
//		else if (placementRule.requiresRecheck()) {
//			isInValidPosition = placementRule.satisfied(this, false);
//			if (isInValidPosition) {
//				assumedValidCache.put(pos.toLong(), this);
//			}
//			return isInValidPosition;
//		}
//		else if (isInValidPosition) {
//			return true;
//		}
//		return isInValidPosition = placementRule.satisfied(this, false);
//	}
//
//	public boolean isSearchRoot() {
//		for (String dep : placementRule.getDependencies()) {
//			if (dep.equals("bearing")) {
//				return true;
//			}
//		}
//		return false;
//	}
//
//	@Override
//	public void update() {
//		if (!world.isRemote) {
//			pushEnergy();
//		}
//	}
//
//	@Override
//	public void onLoad() {
//		super.onLoad();
//		if (ModCheck.ic2Loaded()) {
//			addTileToENet();
//		}
//	}
//
//	@Override
//	public void invalidate() {
//		super.invalidate();
//		if (ModCheck.ic2Loaded()) {
//			removeTileFromENet();
//		}
//	}
//
//	@Override
//	public void onChunkUnload() {
//		super.onChunkUnload();
//		if (ModCheck.ic2Loaded()) {
//			removeTileFromENet();
//		}
//	}
//
//	@Override
//	public EnergyStorage getEnergyStorage() {
//		if (!isInValidPosition || !isMultiblockAssembled()) {
//			return backupStorage;
//		}
//		return getMultiblock().energyStorage;
//	}
//
//	@Override
//	public EnergyConnection[] getEnergyConnections() {
//		return energyConnections;
//	}
//
//	@Override
//	public @Nonnull EnergyTileWrapper[] getEnergySides() {
//		return energySides;
//	}
//
//	@Override
//	public @Nonnull EnergyTileWrapperGT[] getEnergySidesGT() {
//		return energySidesGT;
//	}
//
//	// IC2 Energy
//
//	@Override
//	public boolean getIC2Reg() {
//		return ic2reg;
//	}
//
//	@Override
//	public void setIC2Reg(boolean ic2reg) {
//		this.ic2reg = ic2reg;
//	}
//
//	@Override
//	public int getSinkTier() {
//		return 10;
//	}
//
//	@Override
//	public int getSourceTier() {
//		if (!isInValidPosition || !isMultiblockAssembled()) {
//			return 1;
//		}
//		return EnergyHelper.getEUTier(getMultiblock().power);
//	}
//
//	@Override
//	@Optional.Method(modid = "ic2")
//	public boolean emitsEnergyTo(IEnergyAcceptor receiver, EnumFacing side) {
//		return ITileEnergy.super.emitsEnergyTo(receiver, side);
//	}
//
//	@Override
//	@Optional.Method(modid = "ic2")
//	public double getOfferedEnergy() {
//		return ITileEnergy.super.getOfferedEnergy();
//	}
//
//	@Override
//	@Optional.Method(modid = "ic2")
//	public void drawEnergy(double amount) {
//		ITileEnergy.super.drawEnergy(amount);
//	}
//
//	// NBT
//
//	@Override
//	public NBTTagCompound writeAll(NBTTagCompound nbt) {
//		super.writeAll(nbt);
//		nbt.setString("partName", partName);
//
//		writeEnergyConnections(nbt);
//		nbt.setBoolean("isInValidPosition", isInValidPosition);
//		return nbt;
//	}
//
//	@Override
//	public void readAll(NBTTagCompound nbt) {
//		super.readAll(nbt);
//		if (nbt.hasKey("partName")) {
//			partName = nbt.getString("partName");
//		}
//
//		if (DYN_CONDUCTIVITY_MAP.containsKey(partName)) {
//			conductivity = DYN_CONDUCTIVITY_MAP.getDouble(partName);
//		}
//		if (DYN_RULE_ID_MAP.containsKey(partName)) {
//			ruleID = DYN_RULE_ID_MAP.get(partName);
//			placementRule = TurbinePlacement.RULE_MAP.get(ruleID);
//		}
//
//		readEnergyConnections(nbt);
//		isInValidPosition = nbt.getBoolean("isInValidPosition");
//	}
//
//	// Capability
//
//	@Override
//	public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing side) {
//		if (capability == CapabilityEnergy.ENERGY || (ModCheck.gregtechLoaded() && enable_gtce_eu && capability == GregtechCapabilities.CAPABILITY_ENERGY_CONTAINER)) {
//			return hasEnergySideCapability(side);
//		}
//		return super.hasCapability(capability, side);
//	}
//
//	@Override
//	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing side) {
//		if (capability == CapabilityEnergy.ENERGY) {
//			if (hasEnergySideCapability(side)) {
//				return CapabilityEnergy.ENERGY.cast(getEnergySide(nonNullSide(side)));
//			}
//			return null;
//		}
//		else if (ModCheck.gregtechLoaded() && capability == GregtechCapabilities.CAPABILITY_ENERGY_CONTAINER) {
//			if (enable_gtce_eu && hasEnergySideCapability(side)) {
//				return GregtechCapabilities.CAPABILITY_ENERGY_CONTAINER.cast(getEnergySideGT(nonNullSide(side)));
//			}
//			return null;
//		}
//		return super.getCapability(capability, side);
//	}
//}
