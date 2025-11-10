### NuclearCraft CC: Tweaked Methods

---

### Processors:

```lua
boolean getIsProcessing()

double getCurrentTime()

double getBaseProcessTime()
double getBaseProcessPower()

Table[] getItemInputs() -> {{stackSize, itemID}, ...}
Table[] getFluidInputs() -> {{fluidAmount, fluidName}, ...}
Table[] getItemOutputs() -> {{stackSize, itemID}, ...}
Table[] getFluidOutputs() -> {{fluidAmount, fluidName}, ...}

void setItemInputSorption(int side, int index, int sorption)
void setFluidInputSorption(int side, int index, int sorption)
void setItemOutputSorption(int side, int index, int sorption)
void setFluidOutputSorption(int side, int index, int sorption)

void haltProcess()
void resumeProcess()

```

### Large Machines:

```lua
boolean isComplete()
boolean isMachineOn()

int getLengthX()
int getLengthY()
int getLengthZ()

boolean getIsProcessing()

double getCurrentTime()

double getBaseProcessTime()
double getBaseProcessPower()

Table[] getItemInputs() -> {{stackSize, itemID}, ...}
Table[] getFluidInputs() -> {{fluidAmount, fluidName}, ...}
Table[] getItemOutputs() -> {{stackSize, itemID}, ...}
Table[] getFluidOutputs() -> {{fluidAmount, fluidName}, ...}

void haltProcess()
void resumeProcess()

void clearAllMaterial()
```

### Fission Reactor:

```lua
boolean isComplete()
boolean isReactorOn()

int getLengthX()
int getLengthY()
int getLengthZ()

long getHeatStored()
long getHeatCapacity()

double getTemperature()

double getCoolingRate()
double getRawHeatingRate()
double getMeanEfficiency()
double getMeanHeatMultiplier()

int getNumberOfIrradiators()
int getNumberOfCells()
int getNumberOfSinks()
int getNumberOfVessels()
int getNumberOfHeaters()
int getNumberOfShields()

Table[] getIrradiatorStats()
Table[] getCellStats()
Table[] getSinkStats()
Table[] getVesselStats()
Table[] getHeaterStats()
Table[] getShieldStats()

int getNumberOfClusters()

Table[] getClusterInfo(int clusterID)

void clearAllMaterial()
```

### Heat Exchanger / Condenser:

```lua
boolean isComplete()
boolean isExchangerOn()

int getLengthX()
int getLengthY()
int getLengthZ()

int getActiveNetworkCount()
int getActiveTubeCount()

double getShellSpeedMultiplier()
double getShellInputRate()
double getHeatTransferRate()
double getMeanTempDiff()

int getNumberOfNetworks()

Table[] getNetworkStats()

int getNumberOfTubes()

Table[] getTubeStats()

void activate()
void deactivate()

void clearAllMaterial()
```

### Turbine:

``` lua
boolean isComplete()
boolean isTurbineOn()

int getLengthX()
int getLengthY()
int getLengthZ()

boolean isProcessing()

long getEnergyStored()
long getEnergyCapacity()

double getPower()

double getCoilConductivity()

String getFlowDirection()

double getTotalExpansionLevel()
double getIdealTotalExpansionLevel()

double[] getExpansionLevels()
double[] getIdealExpansionLevels()
double[] getBladeEfficiencies()

int getInputRate()

int getNumberOfDynamoParts()

Table[] getDynamoPartStats()

void activate()
void deactivate()

void clearAllMaterial()
```

### Quantum Computer:

```lua
boolean isComplete()

int getNumberOfQubits()

int getStateDim()

Complex[] getStateVector()
double[] getProbs()
```