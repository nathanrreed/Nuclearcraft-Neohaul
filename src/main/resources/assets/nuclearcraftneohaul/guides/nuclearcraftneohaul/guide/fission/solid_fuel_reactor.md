---
navigation:
  title: Solid Fuel Fission Reactor
  icon: solid_fuel_fission_controller
  parent: fission/fission.md

item_ids:
  - solid_fuel_fission_controller
---

# Solid Fuel Fission Reactor

The Solid Fuel fission Reactor (SFR) uses solid fissio fuel in the form of pellets. The generated heat turns a liquid coolant into a gas for use in a [Turbine](../multiblocks/turbine.md).

#### Fuel

The SFR use fuel pellets such which are placed in <ItemLink id="fission_fuel_cell" /> through a <ItemLink id="fission_fuel_cell_port" />

For example LEU-235:
<Row>
<RecipesFor id="leu_235" />
</Row>

Can be made into the following Fuel Pellets:

<ItemGrid>
<ItemIcon id="leu_235_za" />
<ItemIcon id="leu_235_ox" />
<ItemIcon id="leu_235_ni" />
</ItemGrid>

Each have their own properties requiring different reactors for the same underlying fuel type

#### Cooling

SFR are typically cooled with <FluidLink id="minecraft:water" /> to create <FluidLink id="high_pressure_steam" /> which is can be turned into FE by a [Turbine](../multiblocks/turbine.md).