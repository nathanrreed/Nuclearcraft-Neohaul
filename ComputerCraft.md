### NuclearCraft CC: Tweaked Methods

---

#### Processors:

```lua
int getCurrentTime()

int getBaseProcessTime()

int getBaseProcessPower()

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