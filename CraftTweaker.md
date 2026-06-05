## NuclearCraft CraftTweaker Integration

Examples: `crafttweaker_examples/nuclearcraft_processors.zs`

All processor recipes involve five groups of data: item inputs, fluid inputs, item outputs, fluid outputs and extra
recipe info. The first four are the actual ingredients and products. The extra recipe info contains values such as
`timeModifier`, `powerModifier` and `radiation`.

Recipe extras follow the old 1.12.2 style defaults:

```zenscript
timeModifier = 1D
powerModifier = 1D
radiation = 0D
```

If you specify optional extras, they must be passed in order.

## Chance Ingredients

Item outputs can use NuclearCraft's chance ingredient wrapper.

### ChanceItemIngredient

```zenscript
mods.nuclearcraft.ChanceItemIngredient.create(ingredient as IIngredientWithAmount, chancePercent as int, minStackSize as int = 0);
```

Examples:

```zenscript
mods.nuclearcraft.ChanceItemIngredient.create(<item:minecraft:coal> * 2, 25);
mods.nuclearcraft.ChanceItemIngredient.create(<item:minecraft:glowstone_dust> * 3, 60, 2);
```

Extra methods:

```zenscript
getInternalIngredient() as IIngredientWithAmount
getChancePercent() as int
getMinStackSize() as int
```

Fluid outputs can also use chance data. For fluids, the chance is passed as a separate integer argument right after the
fluid output:

```zenscript
<fluid:minecraft:water> * 1000, 35
```

## Recipe Addition

Item inputs can be:

- `IIngredientWithAmount`
- `null` for optional item slots

Fluid inputs can be:

- `CTFluidIngredient`
- `null` for optional fluid slots

Item outputs can be:

- `IItemStack`
- `mods.nuclearcraft.ChanceItemIngredient`
- `null` for optional output slots

Fluid outputs can be:

- `CTFluidIngredient`
- `IFluidStack, chancePercent`
- `null` for optional output slots

Be sure to use a decimal when a `double` is required and you want to pass a whole number, for example `1D` or `1.0`.

## Recipe Removal

Recipe removal methods are not implemented yet in this integration layer.

## Globals

```zenscript
mods.nuclearcraft.AlloyFurnace
mods.nuclearcraft.Assembler
mods.nuclearcraft.Centrifuge
mods.nuclearcraft.ChemicalReactor
mods.nuclearcraft.Crystallizer
mods.nuclearcraft.DecayHastener
mods.nuclearcraft.ElectricFurnace
mods.nuclearcraft.Electrolyzer
mods.nuclearcraft.Enricher
mods.nuclearcraft.Extractor
mods.nuclearcraft.Infuser
mods.nuclearcraft.SaltMixer
mods.nuclearcraft.FuelReprocessor
mods.nuclearcraft.IngotFormer
mods.nuclearcraft.Manufactory
mods.nuclearcraft.Melter
mods.nuclearcraft.Pressurizer
mods.nuclearcraft.RockCrusher
mods.nuclearcraft.Separator
mods.nuclearcraft.Supercooler
```

## Signatures

### Alloy Furnace

Global: `mods.nuclearcraft.AlloyFurnace`

```zenscript
mods.nuclearcraft.AlloyFurnace.addRecipe(name as string, left as IIngredientWithAmount, right as IIngredientWithAmount, output as IItemStack, timeModifier as double = 1D, powerModifier as double = 1D, radiation as double = 0D);
mods.nuclearcraft.AlloyFurnace.addRecipe(name as string, left as IIngredientWithAmount, right as IIngredientWithAmount, output as mods.nuclearcraft.ChanceItemIngredient, timeModifier as double = 1D, powerModifier as double = 1D, radiation as double = 0D);
```

### Assembler

Global: `mods.nuclearcraft.Assembler`

```zenscript
mods.nuclearcraft.Assembler.addRecipe(name as string, input1 as IIngredientWithAmount, input2 as IIngredientWithAmount?, input3 as IIngredientWithAmount?, input4 as IIngredientWithAmount?, output as IItemStack, timeModifier as double = 1D, powerModifier as double = 1D, radiation as double = 0D);
mods.nuclearcraft.Assembler.addRecipe(name as string, input1 as IIngredientWithAmount, input2 as IIngredientWithAmount?, input3 as IIngredientWithAmount?, input4 as IIngredientWithAmount?, output as mods.nuclearcraft.ChanceItemIngredient, timeModifier as double = 1D, powerModifier as double = 1D, radiation as double = 0D);
```

### Centrifuge

Global: `mods.nuclearcraft.Centrifuge`

```zenscript
mods.nuclearcraft.Centrifuge.addRecipe(name as string, input as CTFluidIngredient, output1 as CTFluidIngredient, output2 as CTFluidIngredient?, output3 as CTFluidIngredient?, output4 as CTFluidIngredient?, output5 as CTFluidIngredient?, output6 as CTFluidIngredient?, timeModifier as double = 1D, powerModifier as double = 1D, radiation as double = 0D);
mods.nuclearcraft.Centrifuge.addRecipe(name as string, input as CTFluidIngredient, output1 as IFluidStack, output1Chance as int, output2 as IFluidStack?, output2Chance as int, output3 as IFluidStack?, output3Chance as int, output4 as IFluidStack?, output4Chance as int, output5 as IFluidStack?, output5Chance as int, output6 as IFluidStack?, output6Chance as int, timeModifier as double = 1D, powerModifier as double = 1D, radiation as double = 0D);
```

### Chemical Reactor

Global: `mods.nuclearcraft.ChemicalReactor`

```zenscript
mods.nuclearcraft.ChemicalReactor.addRecipe(name as string, left as CTFluidIngredient, right as CTFluidIngredient, output1 as CTFluidIngredient, output2 as CTFluidIngredient?, timeModifier as double = 1D, powerModifier as double = 1D, radiation as double = 0D);
mods.nuclearcraft.ChemicalReactor.addRecipe(name as string, left as CTFluidIngredient, right as CTFluidIngredient, output1 as IFluidStack, output1Chance as int, output2 as IFluidStack?, output2Chance as int, timeModifier as double = 1D, powerModifier as double = 1D, radiation as double = 0D);
```

### Crystallizer

Global: `mods.nuclearcraft.Crystallizer`

Actual machine format: `1 fluid -> 1 item`

```zenscript
mods.nuclearcraft.Crystallizer.addRecipe(name as string, input as CTFluidIngredient, output as IItemStack, timeModifier as double = 1D, powerModifier as double = 1D, radiation as double = 0D);
mods.nuclearcraft.Crystallizer.addRecipe(name as string, input as CTFluidIngredient, output as mods.nuclearcraft.ChanceItemIngredient, timeModifier as double = 1D, powerModifier as double = 1D, radiation as double = 0D);
```

### Decay Hastener

Global: `mods.nuclearcraft.DecayHastener`

```zenscript
mods.nuclearcraft.DecayHastener.addRecipe(name as string, input as IIngredientWithAmount, output as IItemStack, timeModifier as double = 1D, powerModifier as double = 1D, radiation as double = 0D);
mods.nuclearcraft.DecayHastener.addRecipe(name as string, input as IIngredientWithAmount, output as mods.nuclearcraft.ChanceItemIngredient, timeModifier as double = 1D, powerModifier as double = 1D, radiation as double = 0D);
```

### Electric Furnace

Global: `mods.nuclearcraft.ElectricFurnace`

```zenscript
mods.nuclearcraft.ElectricFurnace.addRecipe(name as string, input as IIngredientWithAmount, output as IItemStack, timeModifier as double = 1D, powerModifier as double = 1D, radiation as double = 0D);
mods.nuclearcraft.ElectricFurnace.addRecipe(name as string, input as IIngredientWithAmount, output as mods.nuclearcraft.ChanceItemIngredient, timeModifier as double = 1D, powerModifier as double = 1D, radiation as double = 0D);
```

### Electrolyzer

Global: `mods.nuclearcraft.Electrolyzer`

```zenscript
mods.nuclearcraft.Electrolyzer.addRecipe(name as string, input as CTFluidIngredient, output1 as CTFluidIngredient, output2 as CTFluidIngredient?, output3 as CTFluidIngredient?, output4 as CTFluidIngredient?, timeModifier as double = 1D, powerModifier as double = 1D, radiation as double = 0D);
mods.nuclearcraft.Electrolyzer.addRecipe(name as string, input as CTFluidIngredient, output1 as IFluidStack, output1Chance as int, output2 as IFluidStack?, output2Chance as int, output3 as IFluidStack?, output3Chance as int, output4 as IFluidStack?, output4Chance as int, timeModifier as double = 1D, powerModifier as double = 1D, radiation as double = 0D);
```

### Fluid Enricher

Global: `mods.nuclearcraft.Enricher`

```zenscript
mods.nuclearcraft.Enricher.addRecipe(name as string, itemInput as IIngredientWithAmount, fluidInput as CTFluidIngredient, output as CTFluidIngredient, timeModifier as double = 1D, powerModifier as double = 1D, radiation as double = 0D);
mods.nuclearcraft.Enricher.addRecipe(name as string, itemInput as IIngredientWithAmount, fluidInput as CTFluidIngredient, output as IFluidStack, outputChance as int, timeModifier as double = 1D, powerModifier as double = 1D, radiation as double = 0D);
```

### Fluid Extractor

Global: `mods.nuclearcraft.Extractor`

```zenscript
mods.nuclearcraft.Extractor.addRecipe(name as string, input as IIngredientWithAmount, itemOutput as IItemStack, fluidOutput as CTFluidIngredient, timeModifier as double = 1D, powerModifier as double = 1D, radiation as double = 0D);
mods.nuclearcraft.Extractor.addRecipe(name as string, input as IIngredientWithAmount, itemOutput as mods.nuclearcraft.ChanceItemIngredient, fluidOutput as CTFluidIngredient, timeModifier as double = 1D, powerModifier as double = 1D, radiation as double = 0D);
mods.nuclearcraft.Extractor.addRecipe(name as string, input as IIngredientWithAmount, itemOutput as IItemStack, fluidOutput as IFluidStack, fluidOutputChance as int, timeModifier as double = 1D, powerModifier as double = 1D, radiation as double = 0D);
mods.nuclearcraft.Extractor.addRecipe(name as string, input as IIngredientWithAmount, itemOutput as mods.nuclearcraft.ChanceItemIngredient, fluidOutput as IFluidStack, fluidOutputChance as int, timeModifier as double = 1D, powerModifier as double = 1D, radiation as double = 0D);
```

### Fluid Infuser

Global: `mods.nuclearcraft.Infuser`

```zenscript
mods.nuclearcraft.Infuser.addRecipe(name as string, itemInput as IIngredientWithAmount, fluidInput as CTFluidIngredient, output as IItemStack, timeModifier as double = 1D, powerModifier as double = 1D, radiation as double = 0D);
mods.nuclearcraft.Infuser.addRecipe(name as string, itemInput as IIngredientWithAmount, fluidInput as CTFluidIngredient, output as mods.nuclearcraft.ChanceItemIngredient, timeModifier as double = 1D, powerModifier as double = 1D, radiation as double = 0D);
```

### Fluid Mixer

Global: `mods.nuclearcraft.SaltMixer`

```zenscript
mods.nuclearcraft.SaltMixer.addRecipe(name as string, left as CTFluidIngredient, right as CTFluidIngredient, output as CTFluidIngredient, timeModifier as double = 1D, powerModifier as double = 1D, radiation as double = 0D);
mods.nuclearcraft.SaltMixer.addRecipe(name as string, left as CTFluidIngredient, right as CTFluidIngredient, output as IFluidStack, outputChance as int, timeModifier as double = 1D, powerModifier as double = 1D, radiation as double = 0D);
```

### Fuel Reprocessor

Global: `mods.nuclearcraft.FuelReprocessor`

```zenscript
mods.nuclearcraft.FuelReprocessor.addRecipe(name as string, input as IIngredientWithAmount, output1 as IItemStack, output2 as IItemStack?, output3 as IItemStack?, output4 as IItemStack?, output5 as IItemStack?, output6 as IItemStack?, output7 as IItemStack?, output8 as IItemStack?, timeModifier as double = 1D, powerModifier as double = 1D, radiation as double = 0D);
mods.nuclearcraft.FuelReprocessor.addRecipe(name as string, input as IIngredientWithAmount, output1 as mods.nuclearcraft.ChanceItemIngredient, output2 as mods.nuclearcraft.ChanceItemIngredient?, output3 as mods.nuclearcraft.ChanceItemIngredient?, output4 as mods.nuclearcraft.ChanceItemIngredient?, output5 as mods.nuclearcraft.ChanceItemIngredient?, output6 as mods.nuclearcraft.ChanceItemIngredient?, output7 as mods.nuclearcraft.ChanceItemIngredient?, output8 as mods.nuclearcraft.ChanceItemIngredient?, timeModifier as double = 1D, powerModifier as double = 1D, radiation as double = 0D);
```

### Ingot Former

Global: `mods.nuclearcraft.IngotFormer`

```zenscript
mods.nuclearcraft.IngotFormer.addRecipe(name as string, input as CTFluidIngredient, output as IItemStack, timeModifier as double = 1D, powerModifier as double = 1D, radiation as double = 0D);
mods.nuclearcraft.IngotFormer.addRecipe(name as string, input as CTFluidIngredient, output as mods.nuclearcraft.ChanceItemIngredient, timeModifier as double = 1D, powerModifier as double = 1D, radiation as double = 0D);
```

### Manufactory

Global: `mods.nuclearcraft.Manufactory`

```zenscript
mods.nuclearcraft.Manufactory.addRecipe(name as string, input as IIngredientWithAmount, output as IItemStack, timeModifier as double = 1D, powerModifier as double = 1D, radiation as double = 0D);
mods.nuclearcraft.Manufactory.addRecipe(name as string, input as IIngredientWithAmount, output as mods.nuclearcraft.ChanceItemIngredient, timeModifier as double = 1D, powerModifier as double = 1D, radiation as double = 0D);
```

### Melter

Global: `mods.nuclearcraft.Melter`

```zenscript
mods.nuclearcraft.Melter.addRecipe(name as string, input as IIngredientWithAmount, output as CTFluidIngredient, timeModifier as double = 1D, powerModifier as double = 1D, radiation as double = 0D);
mods.nuclearcraft.Melter.addRecipe(name as string, input as IIngredientWithAmount, output as IFluidStack, outputChance as int, timeModifier as double = 1D, powerModifier as double = 1D, radiation as double = 0D);
```

### Pressurizer

Global: `mods.nuclearcraft.Pressurizer`

```zenscript
mods.nuclearcraft.Pressurizer.addRecipe(name as string, input as IIngredientWithAmount, output as IItemStack, timeModifier as double = 1D, powerModifier as double = 1D, radiation as double = 0D);
mods.nuclearcraft.Pressurizer.addRecipe(name as string, input as IIngredientWithAmount, output as mods.nuclearcraft.ChanceItemIngredient, timeModifier as double = 1D, powerModifier as double = 1D, radiation as double = 0D);
```

### Rock Crusher

Global: `mods.nuclearcraft.RockCrusher`

```zenscript
mods.nuclearcraft.RockCrusher.addRecipe(name as string, input as IIngredientWithAmount, output1 as IItemStack, output2 as IItemStack?, output3 as IItemStack?, timeModifier as double = 1D, powerModifier as double = 1D, radiation as double = 0D);
mods.nuclearcraft.RockCrusher.addRecipe(name as string, input as IIngredientWithAmount, output1 as mods.nuclearcraft.ChanceItemIngredient, output2 as mods.nuclearcraft.ChanceItemIngredient?, output3 as mods.nuclearcraft.ChanceItemIngredient?, timeModifier as double = 1D, powerModifier as double = 1D, radiation as double = 0D);
```

### Separator

Global: `mods.nuclearcraft.Separator`

```zenscript
mods.nuclearcraft.Separator.addRecipe(name as string, input as IIngredientWithAmount, output1 as IItemStack, output2 as IItemStack?, timeModifier as double = 1D, powerModifier as double = 1D, radiation as double = 0D);
mods.nuclearcraft.Separator.addRecipe(name as string, input as IIngredientWithAmount, output1 as mods.nuclearcraft.ChanceItemIngredient, output2 as mods.nuclearcraft.ChanceItemIngredient?, timeModifier as double = 1D, powerModifier as double = 1D, radiation as double = 0D);
```

### Supercooler

Global: `mods.nuclearcraft.Supercooler`

```zenscript
mods.nuclearcraft.Supercooler.addRecipe(name as string, input as CTFluidIngredient, output as CTFluidIngredient, timeModifier as double = 1D, powerModifier as double = 1D, radiation as double = 0D);
mods.nuclearcraft.Supercooler.addRecipe(name as string, input as CTFluidIngredient, output as IFluidStack, outputChance as int, timeModifier as double = 1D, powerModifier as double = 1D, radiation as double = 0D);
```
