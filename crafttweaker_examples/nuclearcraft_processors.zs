// Example CraftTweaker script for NuclearCraft processor managers.
// Copy the parts you need into your own scripts folder.

val ironOre = <item:minecraft:iron_ore>;
val gravel = <item:minecraft:gravel>;
val sand = <item:minecraft:sand>;
val clayBall = <item:minecraft:clay_ball>;
val clayBlock = <item:minecraft:clay>;
val redstone = <item:minecraft:redstone>;
val glowstoneDust = <item:minecraft:glowstone_dust>;
val quartz = <item:minecraft:quartz>;
val diamond = <item:minecraft:diamond>;
val emerald = <item:minecraft:emerald>;
val obsidian = <item:minecraft:obsidian>;
val coal = <item:minecraft:coal>;
val charcoal = <item:minecraft:charcoal>;
val ironIngot = <item:minecraft:iron_ingot>;
val goldIngot = <item:minecraft:gold_ingot>;
val copperIngot = <item:minecraft:copper_ingot>;
val glass = <item:minecraft:glass>;
val slimeBall = <item:minecraft:slime_ball>;
val snowball = <item:minecraft:snowball>;
val water = <fluid:minecraft:water> * 1000;
val lava = <fluid:minecraft:lava> * 250;

// 1 item -> 1 item
nuclearDecayHastener.addRecipe("example_decay", coal, charcoal, 1.0, 1.0, 0.0);
nuclearElectricFurnace.addRecipe("example_electric_furnace", sand, glass, 1.0, 1.0, 0.0);
nuclearManufactory.addRecipeWithChance("example_manufactory", gravel, sand.percent(75), 1.0, 1.0, 0.0);
nuclearPressurizer.addRecipe("example_pressurizer", clayBall * 4, clayBlock, 1.0, 1.0, 0.0);

// 2 item -> 1 item
nuclearAlloyFurnace.addRecipeWithChance("example_alloy", ironIngot, goldIngot, copperIngot.percent(80), 1.0, 1.0, 0.0);

// up to 4 item inputs -> 1 item output
nuclearAssembler.addRecipe("example_assembler", redstone, quartz, glowstoneDust, slimeBall, obsidian, 1.0, 1.0, 0.0);

// 1 item -> up to many item outputs
nuclearRockCrusher.addRecipeWithChance("example_rock_crusher", ironOre, gravel.percent(100), sand.percent(35), clayBall.percent(10), 1.0, 1.0, 0.0);
nuclearSeparator.addRecipeWithChance("example_separator", gravel, sand.percent(85), clayBall.percent(20), 1.0, 1.0, 0.0);
nuclearFuelReprocessor.addRecipeWithChance("example_fuel_reprocessor", diamond, quartz.percent(100), redstone.percent(80), glowstoneDust.percent(60), clayBall.percent(40), gravel.percent(30), sand.percent(20), coal.percent(10), emerald.percent(5), 1.0, 1.0, 0.0);

// 1 fluid -> 1 item
nuclearCrystallizer.addRecipeWithChance("example_crystallizer", water, snowball.percent(50), 1.0, 1.0, 0.0);
nuclearIngotFormer.addRecipeWithChance("example_ingot_former", lava, obsidian.percent(100), 1.0, 1.0, 0.0);

// item + fluid -> item / fluid
nuclearFluidInfuser.addRecipeWithChance("example_fluid_infuser", quartz, water, emerald.percent(25), 1.0, 1.0, 0.0);
nuclearFluidEnricher.addRecipe("example_fluid_enricher", redstone, water, lava, 100, 1.0, 1.0, 0.0);

// item -> item + fluid
nuclearFluidExtractor.addRecipeWithChance("example_fluid_extractor", slimeBall, quartz.percent(50), water, 100, 1.0, 1.0, 0.0);

// item -> fluid
nuclearMelter.addRecipe("example_melter", redstone, lava, 80, 1.0, 1.0, 0.0);

// fluid -> fluid
nuclearSupercooler.addRecipe("example_supercooler", water, lava, 100, 1.0, 1.0, 0.0);

// 2 fluid -> 1 or 2 fluid outputs
nuclearFluidMixer.addRecipe("example_fluid_mixer", water, lava, water, 100, 1.0, 1.0, 0.0);
nuclearChemicalReactor.addRecipe("example_chemical_reactor", water, lava, water, 100, lava, 40, 1.0, 1.0, 0.0);

// 1 fluid -> multiple fluid outputs
nuclearCentrifuge.addRecipe("example_centrifuge", water, water, 100, lava, 25, null, 100, null, 100, null, 100, null, 100, 1.0, 1.0, 0.0);
nuclearElectrolyzer.addRecipe("example_electrolyzer", water, water, 100, lava, 50, null, 100, null, 100, 1.0, 1.0, 0.0);
