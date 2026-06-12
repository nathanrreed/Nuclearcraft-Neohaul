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
mods.nuclearcraft.DecayHastener.addRecipe("example_decay", coal, charcoal, 1.0, 1.0, 0.0);
mods.nuclearcraft.ElectricFurnace.addRecipe("example_electric_furnace", sand, glass, 1.0, 1.0, 0.0);
mods.nuclearcraft.Manufactory.addRecipe("example_manufactory", gravel, mods.nuclearcraft.ChanceItemIngredient.create(sand, 75), 1.0, 1.0, 0.0);
mods.nuclearcraft.Pressurizer.addRecipe("example_pressurizer", clayBall * 4, clayBlock, 1.0, 1.0, 0.0);

// 2 item -> 1 item
mods.nuclearcraft.AlloyFurnace.addRecipe("example_alloy", ironIngot, goldIngot, mods.nuclearcraft.ChanceItemIngredient.create(copperIngot, 80), 1.0, 1.0, 0.0);

// up to 4 item inputs -> 1 item output
mods.nuclearcraft.Assembler.addRecipe("example_assembler", redstone, quartz, glowstoneDust, slimeBall, obsidian, 1.0, 1.0, 0.0);

// 1 item -> up to many item outputs
mods.nuclearcraft.RockCrusher.addRecipe("example_rock_crusher", ironOre, mods.nuclearcraft.ChanceItemIngredient.create(gravel, 100), mods.nuclearcraft.ChanceItemIngredient.create(sand, 35), mods.nuclearcraft.ChanceItemIngredient.create(clayBall, 10), 1.0, 1.0, 0.0);
mods.nuclearcraft.Separator.addRecipe("example_separator", gravel, mods.nuclearcraft.ChanceItemIngredient.create(sand, 85), mods.nuclearcraft.ChanceItemIngredient.create(clayBall, 20), 1.0, 1.0, 0.0);
mods.nuclearcraft.FuelReprocessor.addRecipe("example_fuel_reprocessor", diamond, mods.nuclearcraft.ChanceItemIngredient.create(quartz, 100), mods.nuclearcraft.ChanceItemIngredient.create(redstone, 80), mods.nuclearcraft.ChanceItemIngredient.create(glowstoneDust, 60), mods.nuclearcraft.ChanceItemIngredient.create(clayBall, 40), mods.nuclearcraft.ChanceItemIngredient.create(gravel, 30), mods.nuclearcraft.ChanceItemIngredient.create(sand, 20), mods.nuclearcraft.ChanceItemIngredient.create(coal, 10), mods.nuclearcraft.ChanceItemIngredient.create(emerald, 5), 1.0, 1.0, 0.0);

// 1 fluid -> 1 item
mods.nuclearcraft.Crystallizer.addRecipe("example_crystallizer", water, mods.nuclearcraft.ChanceItemIngredient.create(snowball, 50), 1.0, 1.0, 0.0);
mods.nuclearcraft.IngotFormer.addRecipe("example_ingot_former", lava, mods.nuclearcraft.ChanceItemIngredient.create(obsidian, 100), 1.0, 1.0, 0.0);

// item + fluid -> item / fluid
mods.nuclearcraft.FluidInfuser.addRecipe("example_fluid_infuser", quartz, water, mods.nuclearcraft.ChanceItemIngredient.create(emerald, 25), 1.0, 1.0, 0.0);
mods.nuclearcraft.FluidEnricher.addRecipe("example_fluid_enricher", redstone, water, mods.nuclearcraft.ChanceFluidIngredient.create(lava, 100), 1.0, 1.0, 0.0);

// item -> item + fluid
mods.nuclearcraft.FluidExtractor.addRecipe("example_fluid_extractor", slimeBall, mods.nuclearcraft.ChanceItemIngredient.create(quartz, 50), mods.nuclearcraft.ChanceFluidIngredient.create(water, 100), 1.0, 1.0, 0.0);

// item -> fluid
mods.nuclearcraft.Melter.addRecipe("example_melter", redstone, mods.nuclearcraft.ChanceFluidIngredient.create(lava, 80), 1.0, 1.0, 0.0);

// fluid -> fluid
mods.nuclearcraft.Supercooler.addRecipe("example_supercooler", water, mods.nuclearcraft.ChanceFluidIngredient.create(lava, 100), 1.0, 1.0, 0.0);

// 2 fluid -> 1 or 2 fluid outputs
mods.nuclearcraft.FluidMixer.addRecipe("example_fluid_mixer", water, lava, mods.nuclearcraft.ChanceFluidIngredient.create(water, 100), 1.0, 1.0, 0.0);
mods.nuclearcraft.ChemicalReactor.addRecipe("example_chemical_reactor", water, lava, mods.nuclearcraft.ChanceFluidIngredient.create(water, 100), mods.nuclearcraft.ChanceFluidIngredient.create(lava, 40), 1.0, 1.0, 0.0);

// 1 fluid -> multiple fluid outputs
mods.nuclearcraft.Centrifuge.addRecipe("example_centrifuge", water, mods.nuclearcraft.ChanceFluidIngredient.create(water, 100), mods.nuclearcraft.ChanceFluidIngredient.create(lava, 25), null, null, null, null, 1.0, 1.0, 0.0);
mods.nuclearcraft.Electrolyzer.addRecipe("example_electrolyzer", water, mods.nuclearcraft.ChanceFluidIngredient.create(water, 100), mods.nuclearcraft.ChanceFluidIngredient.create(lava, 50), null, null, 1.0, 1.0, 0.0);
