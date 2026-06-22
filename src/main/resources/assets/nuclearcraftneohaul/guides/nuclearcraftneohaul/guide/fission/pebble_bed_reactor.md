---
navigation:
  title: Pebble Bed Fission Reactor
  icon: pebble_bed_fission_controller
  parent: fission/fission.md
  position: 2

item_ids:
  - pebble_bed_fission_controller
---

# Pebble Bed Fission Reactor

The Pebble Bed fission Reactor (PBR) uses solid fission fuel in the form of layered pellets. The generated heat is transferred to a gas coolant and can be extracted in a [Turbine](../multiblocks/turbine.md).

#### Fuel

The PBR use fuel pellets such which are placed in <ItemLink id="fission_fuel_chamber" /> through a <ItemLink id="fission_fuel_chamber_port" />

For Example:
<ItemGrid>
<ItemIcon id="leu_235_tr" />
</ItemGrid>

#### Cooling

PBR are cooled with gas to create hot gas which is can be turned into FE by a [Turbine](../multiblocks/turbine.md).

For Example:

<FluidLink id="hydrogen" /> which becomes <FluidLink id="hydrogen_hot" />