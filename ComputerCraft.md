### NuclearCraft CC: Tweaked Methods

---

#### Processors:

```lua
int getCurrentTime()

int getBaseProcessTime()

int getBaseProcessPower()

Table[] getItemInputs() -> {{stackSize, itemID}, ...}

Table[] getFluidInputs() -> {{fluidAmount, fluidName}, ...}

Table[] getItemOutputs() -> {{stackSize, itemID}, ...}

Table[] getFluidOutputs() -> {{fluidAmount, fluidName}, ...}

modes are OUTPUT, AUTO_OUTPUT, DISABLED, INPUT
void setItemSorption(String direction, int index, String mode) ex. setItemInputSorption("UP", 0, "DISABLED")

void setFluidSorption(String direction, int index, String mode)
```

#### Turbine:

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