---
navigation:
  title: Molten Salt Fission Reactor
  icon: molten_salt_fission_controller
  parent: fission/fission.md

item_ids:
  - molten_salt_fission_controller
---

# Molten Salt Fission Reactor

The Molten Salt fission Reactor (MSR) uses a liquid fission fuel. The generated heat is transferred to a molten salt coolant, which in turn is used to produce steam in a [Heat Exchanger](../multiblocks/heat_exchanger.md).

#### Fuel

The MSR uses Molten FLiBe Salt as fuel such as through a <ItemLink id="fission_fuel_vessel_port" />

For example:
<FluidLink id="leu_235_fluoride_flibe" /> which becomes <FluidLink id="depleted_leu_235_fluoride_flibe" />

#### Cooling

MSR are cooled with Eutectic NaK such as <FluidLink id="gold_nak" /> which produces <FluidLink id="gold_nak_hot" /> which is used in a [Heat Exchanger](../multiblocks/heat_exchanger.md).