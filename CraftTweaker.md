## CraftTweaker

Examples: `crafttweaker_examples/nuclearcraft_processors.zs`

### Notes

- All processor managers are exposed as global CraftTweaker objects.
- `?` means the argument is optional / nullable.
- For guaranteed item outputs use `addRecipe(..., output as IItemStack, ...)`.
- For chance item outputs use `addRecipeWithChance(..., output as Percentaged<IItemStack>, ...)`, for example:

```zenscript
<item:minecraft:diamond>.percent(35)
```

- For guaranteed fluid outputs use `CTFluidIngredient`.
- For chance fluid outputs use `IFluidStack` plus a separate `int` chance right after that output, for example:

```zenscript
<fluid:minecraft:water> * 1000, 35
```

## Globals

```zenscript
nuclearAlloyFurnace
nuclearAssembler
nuclearCentrifuge
nuclearChemicalReactor
nuclearCrystallizer
nuclearDecayHastener
nuclearElectricFurnace
nuclearElectrolyzer
nuclearFluidEnricher
nuclearFluidExtractor
nuclearFluidInfuser
nuclearFluidMixer
nuclearFuelReprocessor
nuclearIngotFormer
nuclearManufactory
nuclearMelter
nuclearPressurizer
nuclearRockCrusher
nuclearSeparator
nuclearSupercooler
```

## Signatures

### Alloy Furnace

Global: `nuclearAlloyFurnace`

```zenscript
nuclearAlloyFurnace.addRecipe(name as string, left as IIngredientWithAmount, right as IIngredientWithAmount, output as IItemStack, timeModifier as double, powerModifier as double, radiation as double);
nuclearAlloyFurnace.addRecipeWithChance(name as string, left as IIngredientWithAmount, right as IIngredientWithAmount, output as Percentaged<IItemStack>, timeModifier as double, powerModifier as double, radiation as double);
```

### Assembler

Global: `nuclearAssembler`

```zenscript
nuclearAssembler.addRecipe(name as string, input1 as IIngredientWithAmount, input2 as IIngredientWithAmount?, input3 as IIngredientWithAmount?, input4 as IIngredientWithAmount?, output as IItemStack, timeModifier as double, powerModifier as double, radiation as double);
nuclearAssembler.addRecipeWithChance(name as string, input1 as IIngredientWithAmount, input2 as IIngredientWithAmount?, input3 as IIngredientWithAmount?, input4 as IIngredientWithAmount?, output as Percentaged<IItemStack>, timeModifier as double, powerModifier as double, radiation as double);
```

### Centrifuge

Global: `nuclearCentrifuge`

```zenscript
nuclearCentrifuge.addRecipe(name as string, input as CTFluidIngredient, output1 as CTFluidIngredient, output2 as CTFluidIngredient?, output3 as CTFluidIngredient?, output4 as CTFluidIngredient?, output5 as CTFluidIngredient?, output6 as CTFluidIngredient?, timeModifier as double, powerModifier as double, radiation as double);
nuclearCentrifuge.addRecipe(name as string, input as CTFluidIngredient, output1 as IFluidStack, output1Chance as int, output2 as IFluidStack?, output2Chance as int, output3 as IFluidStack?, output3Chance as int, output4 as IFluidStack?, output4Chance as int, output5 as IFluidStack?, output5Chance as int, output6 as IFluidStack?, output6Chance as int, timeModifier as double, powerModifier as double, radiation as double);
```

### Chemical Reactor

Global: `nuclearChemicalReactor`

```zenscript
nuclearChemicalReactor.addRecipe(name as string, left as CTFluidIngredient, right as CTFluidIngredient, output1 as CTFluidIngredient, output2 as CTFluidIngredient?, timeModifier as double, powerModifier as double, radiation as double);
nuclearChemicalReactor.addRecipe(name as string, left as CTFluidIngredient, right as CTFluidIngredient, output1 as IFluidStack, output1Chance as int, output2 as IFluidStack?, output2Chance as int, timeModifier as double, powerModifier as double, radiation as double);
```

### Crystallizer

Global: `nuclearCrystallizer`

Actual machine format: `1 fluid -> 1 item`

```zenscript
nuclearCrystallizer.addRecipe(name as string, input as CTFluidIngredient, output as IItemStack, timeModifier as double, powerModifier as double, radiation as double);
nuclearCrystallizer.addRecipeWithChance(name as string, input as CTFluidIngredient, output as Percentaged<IItemStack>, timeModifier as double, powerModifier as double, radiation as double);
```

### Decay Hastener

Global: `nuclearDecayHastener`

```zenscript
nuclearDecayHastener.addRecipe(name as string, input as IIngredientWithAmount, output as IItemStack, timeModifier as double, powerModifier as double, radiation as double);
nuclearDecayHastener.addRecipeWithChance(name as string, input as IIngredientWithAmount, output as Percentaged<IItemStack>, timeModifier as double, powerModifier as double, radiation as double);
```

### Electric Furnace

Global: `nuclearElectricFurnace`

```zenscript
nuclearElectricFurnace.addRecipe(name as string, input as IIngredientWithAmount, output as IItemStack, timeModifier as double, powerModifier as double, radiation as double);
nuclearElectricFurnace.addRecipeWithChance(name as string, input as IIngredientWithAmount, output as Percentaged<IItemStack>, timeModifier as double, powerModifier as double, radiation as double);
```

### Electrolyzer

Global: `nuclearElectrolyzer`

```zenscript
nuclearElectrolyzer.addRecipe(name as string, input as CTFluidIngredient, output1 as CTFluidIngredient, output2 as CTFluidIngredient?, output3 as CTFluidIngredient?, output4 as CTFluidIngredient?, timeModifier as double, powerModifier as double, radiation as double);
nuclearElectrolyzer.addRecipe(name as string, input as CTFluidIngredient, output1 as IFluidStack, output1Chance as int, output2 as IFluidStack?, output2Chance as int, output3 as IFluidStack?, output3Chance as int, output4 as IFluidStack?, output4Chance as int, timeModifier as double, powerModifier as double, radiation as double);
```

### Fluid Enricher

Global: `nuclearFluidEnricher`

```zenscript
nuclearFluidEnricher.addRecipe(name as string, itemInput as IIngredientWithAmount, fluidInput as CTFluidIngredient, output as CTFluidIngredient, timeModifier as double, powerModifier as double, radiation as double);
nuclearFluidEnricher.addRecipe(name as string, itemInput as IIngredientWithAmount, fluidInput as CTFluidIngredient, output as IFluidStack, outputChance as int, timeModifier as double, powerModifier as double, radiation as double);
```

### Fluid Extractor

Global: `nuclearFluidExtractor`

```zenscript
nuclearFluidExtractor.addRecipe(name as string, input as IIngredientWithAmount, itemOutput as IItemStack, fluidOutput as CTFluidIngredient, timeModifier as double, powerModifier as double, radiation as double);
nuclearFluidExtractor.addRecipeWithChance(name as string, input as IIngredientWithAmount, itemOutput as Percentaged<IItemStack>, fluidOutput as CTFluidIngredient, timeModifier as double, powerModifier as double, radiation as double);
nuclearFluidExtractor.addRecipe(name as string, input as IIngredientWithAmount, itemOutput as IItemStack, fluidOutput as IFluidStack, fluidOutputChance as int, timeModifier as double, powerModifier as double, radiation as double);
nuclearFluidExtractor.addRecipeWithChance(name as string, input as IIngredientWithAmount, itemOutput as Percentaged<IItemStack>, fluidOutput as IFluidStack, fluidOutputChance as int, timeModifier as double, powerModifier as double, radiation as double);
```

### Fluid Infuser

Global: `nuclearFluidInfuser`

```zenscript
nuclearFluidInfuser.addRecipe(name as string, itemInput as IIngredientWithAmount, fluidInput as CTFluidIngredient, output as IItemStack, timeModifier as double, powerModifier as double, radiation as double);
nuclearFluidInfuser.addRecipeWithChance(name as string, itemInput as IIngredientWithAmount, fluidInput as CTFluidIngredient, output as Percentaged<IItemStack>, timeModifier as double, powerModifier as double, radiation as double);
```

### Fluid Mixer

Global: `nuclearFluidMixer`

```zenscript
nuclearFluidMixer.addRecipe(name as string, left as CTFluidIngredient, right as CTFluidIngredient, output as CTFluidIngredient, timeModifier as double, powerModifier as double, radiation as double);
nuclearFluidMixer.addRecipe(name as string, left as CTFluidIngredient, right as CTFluidIngredient, output as IFluidStack, outputChance as int, timeModifier as double, powerModifier as double, radiation as double);
```

### Fuel Reprocessor

Global: `nuclearFuelReprocessor`

```zenscript
nuclearFuelReprocessor.addRecipe(name as string, input as IIngredientWithAmount, output1 as IItemStack, output2 as IItemStack?, output3 as IItemStack?, output4 as IItemStack?, output5 as IItemStack?, output6 as IItemStack?, output7 as IItemStack?, output8 as IItemStack?, timeModifier as double, powerModifier as double, radiation as double);
nuclearFuelReprocessor.addRecipeWithChance(name as string, input as IIngredientWithAmount, output1 as Percentaged<IItemStack>, output2 as Percentaged<IItemStack>?, output3 as Percentaged<IItemStack>?, output4 as Percentaged<IItemStack>?, output5 as Percentaged<IItemStack>?, output6 as Percentaged<IItemStack>?, output7 as Percentaged<IItemStack>?, output8 as Percentaged<IItemStack>?, timeModifier as double, powerModifier as double, radiation as double);
```

### Ingot Former

Global: `nuclearIngotFormer`

```zenscript
nuclearIngotFormer.addRecipe(name as string, input as CTFluidIngredient, output as IItemStack, timeModifier as double, powerModifier as double, radiation as double);
nuclearIngotFormer.addRecipeWithChance(name as string, input as CTFluidIngredient, output as Percentaged<IItemStack>, timeModifier as double, powerModifier as double, radiation as double);
```

### Manufactory

Global: `nuclearManufactory`

```zenscript
nuclearManufactory.addRecipe(name as string, input as IIngredientWithAmount, output as IItemStack, timeModifier as double, powerModifier as double, radiation as double);
nuclearManufactory.addRecipeWithChance(name as string, input as IIngredientWithAmount, output as Percentaged<IItemStack>, timeModifier as double, powerModifier as double, radiation as double);
```

### Melter

Global: `nuclearMelter`

```zenscript
nuclearMelter.addRecipe(name as string, input as IIngredientWithAmount, output as CTFluidIngredient, timeModifier as double, powerModifier as double, radiation as double);
nuclearMelter.addRecipe(name as string, input as IIngredientWithAmount, output as IFluidStack, outputChance as int, timeModifier as double, powerModifier as double, radiation as double);
```

### Pressurizer

Global: `nuclearPressurizer`

```zenscript
nuclearPressurizer.addRecipe(name as string, input as IIngredientWithAmount, output as IItemStack, timeModifier as double, powerModifier as double, radiation as double);
nuclearPressurizer.addRecipeWithChance(name as string, input as IIngredientWithAmount, output as Percentaged<IItemStack>, timeModifier as double, powerModifier as double, radiation as double);
```

### Rock Crusher

Global: `nuclearRockCrusher`

```zenscript
nuclearRockCrusher.addRecipe(name as string, input as IIngredientWithAmount, output1 as IItemStack, output2 as IItemStack?, output3 as IItemStack?, timeModifier as double, powerModifier as double, radiation as double);
nuclearRockCrusher.addRecipeWithChance(name as string, input as IIngredientWithAmount, output1 as Percentaged<IItemStack>, output2 as Percentaged<IItemStack>?, output3 as Percentaged<IItemStack>?, timeModifier as double, powerModifier as double, radiation as double);
```

### Separator

Global: `nuclearSeparator`

```zenscript
nuclearSeparator.addRecipe(name as string, input as IIngredientWithAmount, output1 as IItemStack, output2 as IItemStack?, timeModifier as double, powerModifier as double, radiation as double);
nuclearSeparator.addRecipeWithChance(name as string, input as IIngredientWithAmount, output1 as Percentaged<IItemStack>, output2 as Percentaged<IItemStack>?, timeModifier as double, powerModifier as double, radiation as double);
```

### Supercooler

Global: `nuclearSupercooler`

```zenscript
nuclearSupercooler.addRecipe(name as string, input as CTFluidIngredient, output as CTFluidIngredient, timeModifier as double, powerModifier as double, radiation as double);
nuclearSupercooler.addRecipe(name as string, input as CTFluidIngredient, output as IFluidStack, outputChance as int, timeModifier as double, powerModifier as double, radiation as double);
```