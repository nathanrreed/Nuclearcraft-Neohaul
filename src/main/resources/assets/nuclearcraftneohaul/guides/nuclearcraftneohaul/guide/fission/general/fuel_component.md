---
navigation:
  title: Fuel Component
  icon: fission_fuel_cell
  parent: fission/fission.md

item_ids:
- fission_fuel_cell
- fission_fuel_vessel
- fission_fuel_chamber
---

# Fuel Component

Each type of reactor has an associated <Color id="dark_purple">Fuel Component</Color>, which houses the fission fuel. When the fuel is undergoing fission, it produces a certain amount of heat. <Color id="dark_purple">Fuel Component</Color>s can also be filtered by right-clicking it with a fuel to allow for multi-fuel reactors.

| Reactor Type        | Fuel Component                         | Cooling Type |
|---------------------|----------------------------------------|--------------|
| Solid Fuel Reactor  | <ItemLink id="fission_fuel_cell" />    | Liquids      |
| Pebble Bed Reactor  | <ItemLink id="fission_fuel_chamber" /> | Gases        |
| Molten Salt Reactor | <ItemLink id="fission_fuel_vessel" />  | Molten Salt  |



## Criticality Factor

For fuels to be able to undergo fission, they must receive a certain neutron flux, measured in N/t. When the neutron flux going into a <Color id="dark_purple">Fuel Component</Color> is above the fuel's criticality factor, the fuel will start undergoing fission. This will not only produce heat, but also more neutrons.

If the flux in a <Color id="dark_purple">Fuel Component</Color> goes over two times it's criticality factor, the efficiency of the reactor will be reduced. Neutron flux can also be used for crafting recipes in the [Irradiator](irradiator.md).
