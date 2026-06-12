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

Fluid outputs can also use NuclearCraft's fluid chance wrapper.
A ChanceFluidIngredient for an ingredient of size 500, with a increment of 150 and minimum stack size of 50, will produce 50, 200, 350 or 500 mB of the fluid

### ChanceFluidIngredient

```zenscript
mods.nuclearcraft.ChanceFluidIngredient.create(ingredient as IFluidStack, chancePercent as int, minStackSize as int = 0, increment as int = 1);
millibuckets of the fluid.
```

Examples:

```zenscript
mods.nuclearcraft.ChanceFluidIngredient.create(<fluid:minecraft:water> * 1000, 35);
mods.nuclearcraft.ChanceFluidIngredient.create(<fluid:minecraft:lava> * 1000, 80, 250, 16);
```

Extra methods:

```zenscript
getInternalIngredient() as IFluidStack
getChancePercent() as int
getMinStackSize() as int
getIncrement() as int
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
- `mods.nuclearcraft.ChanceFluidIngredient`
- `null` for optional output slots

Be sure to use a decimal when a `double` is required and you want to pass a whole number, for example `1D` or `1.0`.

## Recipe Removal

Recipe removal methods are not implemented yet in this integration layer.

## Access Styles

Processor recipes can be added in two ways.

Old-style 1.12.2 compatible type access:

```zenscript
mods.nuclearcraft.Pressurizer.addRecipe(...);
```

RecipeType bracket access:

```zenscript
<recipetype:nuclearcraftneohaul:pressurizer>.addRecipe(...);
```

Both forms call the same underlying recipe manager methods.

## Old-Style Types

```zenscript
mods.nuclearcraft.AlloyFurnace
mods.nuclearcraft.Assembler
mods.nuclearcraft.Centrifuge
mods.nuclearcraft.ChemicalReactor
mods.nuclearcraft.Crystallizer
mods.nuclearcraft.DecayHastener
mods.nuclearcraft.ElectricFurnace
mods.nuclearcraft.Electrolyzer
mods.nuclearcraft.FluidEnricher
mods.nuclearcraft.FluidExtractor
mods.nuclearcraft.FluidInfuser
mods.nuclearcraft.FluidMixer
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

Old-style: `mods.nuclearcraft.AlloyFurnace`

RecipeType: `<recipetype:nuclearcraftneohaul:alloy_furnace>`

```zenscript
mods.nuclearcraft.AlloyFurnace.addRecipe(name as string, left as IIngredientWithAmount, right as IIngredientWithAmount, output as IItemStack, timeModifier as double = 1D, powerModifier as double = 1D, radiation as double = 0D);
mods.nuclearcraft.AlloyFurnace.addRecipe(name as string, left as IIngredientWithAmount, right as IIngredientWithAmount, output as mods.nuclearcraft.ChanceItemIngredient, timeModifier as double = 1D, powerModifier as double = 1D, radiation as double = 0D);
```

### Assembler

Old-style: `mods.nuclearcraft.Assembler`

RecipeType: `<recipetype:nuclearcraftneohaul:assembler>`

```zenscript
mods.nuclearcraft.Assembler.addRecipe(name as string, input1 as IIngredientWithAmount, input2 as IIngredientWithAmount?, input3 as IIngredientWithAmount?, input4 as IIngredientWithAmount?, output as IItemStack, timeModifier as double = 1D, powerModifier as double = 1D, radiation as double = 0D);
mods.nuclearcraft.Assembler.addRecipe(name as string, input1 as IIngredientWithAmount, input2 as IIngredientWithAmount?, input3 as IIngredientWithAmount?, input4 as IIngredientWithAmount?, output as mods.nuclearcraft.ChanceItemIngredient, timeModifier as double = 1D, powerModifier as double = 1D, radiation as double = 0D);
```

### Centrifuge

Old-style: `mods.nuclearcraft.Centrifuge`

RecipeType: `<recipetype:nuclearcraftneohaul:centrifuge>`

```zenscript
mods.nuclearcraft.Centrifuge.addRecipe(name as string, input as CTFluidIngredient, output1 as CTFluidIngredient, output2 as CTFluidIngredient?, output3 as CTFluidIngredient?, output4 as CTFluidIngredient?, output5 as CTFluidIngredient?, output6 as CTFluidIngredient?, timeModifier as double = 1D, powerModifier as double = 1D, radiation as double = 0D);
mods.nuclearcraft.Centrifuge.addRecipe(name as string, input as CTFluidIngredient, output1 as mods.nuclearcraft.ChanceFluidIngredient, output2 as mods.nuclearcraft.ChanceFluidIngredient?, output3 as mods.nuclearcraft.ChanceFluidIngredient?, output4 as mods.nuclearcraft.ChanceFluidIngredient?, output5 as mods.nuclearcraft.ChanceFluidIngredient?, output6 as mods.nuclearcraft.ChanceFluidIngredient?, timeModifier as double = 1D, powerModifier as double = 1D, radiation as double = 0D);
```

### Chemical Reactor

Old-style: `mods.nuclearcraft.ChemicalReactor`

RecipeType: `<recipetype:nuclearcraftneohaul:chemical_reactor>`

```zenscript
mods.nuclearcraft.ChemicalReactor.addRecipe(name as string, left as CTFluidIngredient, right as CTFluidIngredient, output1 as CTFluidIngredient, output2 as CTFluidIngredient?, timeModifier as double = 1D, powerModifier as double = 1D, radiation as double = 0D);
mods.nuclearcraft.ChemicalReactor.addRecipe(name as string, left as CTFluidIngredient, right as CTFluidIngredient, output1 as mods.nuclearcraft.ChanceFluidIngredient, output2 as mods.nuclearcraft.ChanceFluidIngredient?, timeModifier as double = 1D, powerModifier as double = 1D, radiation as double = 0D);
```

### Crystallizer

Old-style: `mods.nuclearcraft.Crystallizer`

RecipeType: `<recipetype:nuclearcraftneohaul:crystallizer>`

Actual machine format: `1 fluid -> 1 item`

```zenscript
mods.nuclearcraft.Crystallizer.addRecipe(name as string, input as CTFluidIngredient, output as IItemStack, timeModifier as double = 1D, powerModifier as double = 1D, radiation as double = 0D);
mods.nuclearcraft.Crystallizer.addRecipe(name as string, input as CTFluidIngredient, output as mods.nuclearcraft.ChanceItemIngredient, timeModifier as double = 1D, powerModifier as double = 1D, radiation as double = 0D);
```

### Decay Hastener

Old-style: `mods.nuclearcraft.DecayHastener`

RecipeType: `<recipetype:nuclearcraftneohaul:decay_hastener>`

```zenscript
mods.nuclearcraft.DecayHastener.addRecipe(name as string, input as IIngredientWithAmount, output as IItemStack, timeModifier as double = 1D, powerModifier as double = 1D, radiation as double = 0D);
mods.nuclearcraft.DecayHastener.addRecipe(name as string, input as IIngredientWithAmount, output as mods.nuclearcraft.ChanceItemIngredient, timeModifier as double = 1D, powerModifier as double = 1D, radiation as double = 0D);
```

### Electric Furnace

Old-style: `mods.nuclearcraft.ElectricFurnace`

RecipeType: `<recipetype:nuclearcraftneohaul:electric_furnace>`

```zenscript
mods.nuclearcraft.ElectricFurnace.addRecipe(name as string, input as IIngredientWithAmount, output as IItemStack, timeModifier as double = 1D, powerModifier as double = 1D, radiation as double = 0D);
mods.nuclearcraft.ElectricFurnace.addRecipe(name as string, input as IIngredientWithAmount, output as mods.nuclearcraft.ChanceItemIngredient, timeModifier as double = 1D, powerModifier as double = 1D, radiation as double = 0D);
```

### Electrolyzer

Old-style: `mods.nuclearcraft.Electrolyzer`

RecipeType: `<recipetype:nuclearcraftneohaul:electrolyzer>`

```zenscript
mods.nuclearcraft.Electrolyzer.addRecipe(name as string, input as CTFluidIngredient, output1 as CTFluidIngredient, output2 as CTFluidIngredient?, output3 as CTFluidIngredient?, output4 as CTFluidIngredient?, timeModifier as double = 1D, powerModifier as double = 1D, radiation as double = 0D);
mods.nuclearcraft.Electrolyzer.addRecipe(name as string, input as CTFluidIngredient, output1 as mods.nuclearcraft.ChanceFluidIngredient, output2 as mods.nuclearcraft.ChanceFluidIngredient?, output3 as mods.nuclearcraft.ChanceFluidIngredient?, output4 as mods.nuclearcraft.ChanceFluidIngredient?, timeModifier as double = 1D, powerModifier as double = 1D, radiation as double = 0D);
```

### Fluid Enricher

Old-style: `mods.nuclearcraft.FluidEnricher`

RecipeType: `<recipetype:nuclearcraftneohaul:fluid_enricher>`

```zenscript
mods.nuclearcraft.FluidEnricher.addRecipe(name as string, itemInput as IIngredientWithAmount, fluidInput as CTFluidIngredient, output as CTFluidIngredient, timeModifier as double = 1D, powerModifier as double = 1D, radiation as double = 0D);
mods.nuclearcraft.FluidEnricher.addRecipe(name as string, itemInput as IIngredientWithAmount, fluidInput as CTFluidIngredient, output as mods.nuclearcraft.ChanceFluidIngredient, timeModifier as double = 1D, powerModifier as double = 1D, radiation as double = 0D);
```

### Fluid Extractor

Old-style: `mods.nuclearcraft.FluidExtractor`

RecipeType: `<recipetype:nuclearcraftneohaul:fluid_extractor>`

```zenscript
mods.nuclearcraft.FluidExtractor.addRecipe(name as string, input as IIngredientWithAmount, itemOutput as IItemStack, fluidOutput as CTFluidIngredient, timeModifier as double = 1D, powerModifier as double = 1D, radiation as double = 0D);
mods.nuclearcraft.FluidExtractor.addRecipe(name as string, input as IIngredientWithAmount, itemOutput as mods.nuclearcraft.ChanceItemIngredient, fluidOutput as CTFluidIngredient, timeModifier as double = 1D, powerModifier as double = 1D, radiation as double = 0D);
mods.nuclearcraft.FluidExtractor.addRecipe(name as string, input as IIngredientWithAmount, itemOutput as IItemStack, fluidOutput as mods.nuclearcraft.ChanceFluidIngredient, timeModifier as double = 1D, powerModifier as double = 1D, radiation as double = 0D);
mods.nuclearcraft.FluidExtractor.addRecipe(name as string, input as IIngredientWithAmount, itemOutput as mods.nuclearcraft.ChanceItemIngredient, fluidOutput as mods.nuclearcraft.ChanceFluidIngredient, timeModifier as double = 1D, powerModifier as double = 1D, radiation as double = 0D);
```

### Fluid Infuser

Old-style: `mods.nuclearcraft.FluidInfuser`

RecipeType: `<recipetype:nuclearcraftneohaul:fluid_infuser>`

```zenscript
mods.nuclearcraft.FluidInfuser.addRecipe(name as string, itemInput as IIngredientWithAmount, fluidInput as CTFluidIngredient, output as IItemStack, timeModifier as double = 1D, powerModifier as double = 1D, radiation as double = 0D);
mods.nuclearcraft.FluidInfuser.addRecipe(name as string, itemInput as IIngredientWithAmount, fluidInput as CTFluidIngredient, output as mods.nuclearcraft.ChanceItemIngredient, timeModifier as double = 1D, powerModifier as double = 1D, radiation as double = 0D);
```

### Fluid Mixer

Old-style: `mods.nuclearcraft.FluidMixer`

RecipeType: `<recipetype:nuclearcraftneohaul:fluid_mixer>`

```zenscript
mods.nuclearcraft.FluidMixer.addRecipe(name as string, left as CTFluidIngredient, right as CTFluidIngredient, output as CTFluidIngredient, timeModifier as double = 1D, powerModifier as double = 1D, radiation as double = 0D);
mods.nuclearcraft.FluidMixer.addRecipe(name as string, left as CTFluidIngredient, right as CTFluidIngredient, output as mods.nuclearcraft.ChanceFluidIngredient, timeModifier as double = 1D, powerModifier as double = 1D, radiation as double = 0D);
```

### Fuel Reprocessor

Old-style: `mods.nuclearcraft.FuelReprocessor`

RecipeType: `<recipetype:nuclearcraftneohaul:fuel_reprocessor>`

```zenscript
mods.nuclearcraft.FuelReprocessor.addRecipe(name as string, input as IIngredientWithAmount, output1 as IItemStack, output2 as IItemStack?, output3 as IItemStack?, output4 as IItemStack?, output5 as IItemStack?, output6 as IItemStack?, output7 as IItemStack?, output8 as IItemStack?, timeModifier as double = 1D, powerModifier as double = 1D, radiation as double = 0D);
mods.nuclearcraft.FuelReprocessor.addRecipe(name as string, input as IIngredientWithAmount, output1 as mods.nuclearcraft.ChanceItemIngredient, output2 as mods.nuclearcraft.ChanceItemIngredient?, output3 as mods.nuclearcraft.ChanceItemIngredient?, output4 as mods.nuclearcraft.ChanceItemIngredient?, output5 as mods.nuclearcraft.ChanceItemIngredient?, output6 as mods.nuclearcraft.ChanceItemIngredient?, output7 as mods.nuclearcraft.ChanceItemIngredient?, output8 as mods.nuclearcraft.ChanceItemIngredient?, timeModifier as double = 1D, powerModifier as double = 1D, radiation as double = 0D);
```

### Ingot Former

Old-style: `mods.nuclearcraft.IngotFormer`

RecipeType: `<recipetype:nuclearcraftneohaul:ingot_former>`

```zenscript
mods.nuclearcraft.IngotFormer.addRecipe(name as string, input as CTFluidIngredient, output as IItemStack, timeModifier as double = 1D, powerModifier as double = 1D, radiation as double = 0D);
mods.nuclearcraft.IngotFormer.addRecipe(name as string, input as CTFluidIngredient, output as mods.nuclearcraft.ChanceItemIngredient, timeModifier as double = 1D, powerModifier as double = 1D, radiation as double = 0D);
```

### Manufactory

Old-style: `mods.nuclearcraft.Manufactory`

RecipeType: `<recipetype:nuclearcraftneohaul:manufactory>`

```zenscript
mods.nuclearcraft.Manufactory.addRecipe(name as string, input as IIngredientWithAmount, output as IItemStack, timeModifier as double = 1D, powerModifier as double = 1D, radiation as double = 0D);
mods.nuclearcraft.Manufactory.addRecipe(name as string, input as IIngredientWithAmount, output as mods.nuclearcraft.ChanceItemIngredient, timeModifier as double = 1D, powerModifier as double = 1D, radiation as double = 0D);
```

### Melter

Old-style: `mods.nuclearcraft.Melter`

RecipeType: `<recipetype:nuclearcraftneohaul:melter>`

```zenscript
mods.nuclearcraft.Melter.addRecipe(name as string, input as IIngredientWithAmount, output as CTFluidIngredient, timeModifier as double = 1D, powerModifier as double = 1D, radiation as double = 0D);
mods.nuclearcraft.Melter.addRecipe(name as string, input as IIngredientWithAmount, output as mods.nuclearcraft.ChanceFluidIngredient, timeModifier as double = 1D, powerModifier as double = 1D, radiation as double = 0D);
```

### Pressurizer

Old-style: `mods.nuclearcraft.Pressurizer`

RecipeType: `<recipetype:nuclearcraftneohaul:pressurizer>`

```zenscript
mods.nuclearcraft.Pressurizer.addRecipe(name as string, input as IIngredientWithAmount, output as IItemStack, timeModifier as double = 1D, powerModifier as double = 1D, radiation as double = 0D);
mods.nuclearcraft.Pressurizer.addRecipe(name as string, input as IIngredientWithAmount, output as mods.nuclearcraft.ChanceItemIngredient, timeModifier as double = 1D, powerModifier as double = 1D, radiation as double = 0D);
```

### Rock Crusher

Old-style: `mods.nuclearcraft.RockCrusher`

RecipeType: `<recipetype:nuclearcraftneohaul:rock_crusher>`

```zenscript
mods.nuclearcraft.RockCrusher.addRecipe(name as string, input as IIngredientWithAmount, output1 as IItemStack, output2 as IItemStack?, output3 as IItemStack?, timeModifier as double = 1D, powerModifier as double = 1D, radiation as double = 0D);
mods.nuclearcraft.RockCrusher.addRecipe(name as string, input as IIngredientWithAmount, output1 as mods.nuclearcraft.ChanceItemIngredient, output2 as mods.nuclearcraft.ChanceItemIngredient?, output3 as mods.nuclearcraft.ChanceItemIngredient?, timeModifier as double = 1D, powerModifier as double = 1D, radiation as double = 0D);
```

### Separator

Old-style: `mods.nuclearcraft.Separator`

RecipeType: `<recipetype:nuclearcraftneohaul:separator>`

```zenscript
mods.nuclearcraft.Separator.addRecipe(name as string, input as IIngredientWithAmount, output1 as IItemStack, output2 as IItemStack?, timeModifier as double = 1D, powerModifier as double = 1D, radiation as double = 0D);
mods.nuclearcraft.Separator.addRecipe(name as string, input as IIngredientWithAmount, output1 as mods.nuclearcraft.ChanceItemIngredient, output2 as mods.nuclearcraft.ChanceItemIngredient?, timeModifier as double = 1D, powerModifier as double = 1D, radiation as double = 0D);
```

### Supercooler

Old-style: `mods.nuclearcraft.Supercooler`

RecipeType: `<recipetype:nuclearcraftneohaul:supercooler>`

```zenscript
mods.nuclearcraft.Supercooler.addRecipe(name as string, input as CTFluidIngredient, output as CTFluidIngredient, timeModifier as double = 1D, powerModifier as double = 1D, radiation as double = 0D);
mods.nuclearcraft.Supercooler.addRecipe(name as string, input as CTFluidIngredient, output as mods.nuclearcraft.ChanceFluidIngredient, timeModifier as double = 1D, powerModifier as double = 1D, radiation as double = 0D);
```
