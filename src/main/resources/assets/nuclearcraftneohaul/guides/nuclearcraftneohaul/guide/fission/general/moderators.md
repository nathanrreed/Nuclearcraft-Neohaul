---
navigation:
  title: Moderators
  icon: heavy_water_moderator
  parent: fission/fission.md

data_maps:
  - fission_moderator_data
---

# Moderators

<Color id="dark_purple">Moderators</Color> can slow down the high energy neutrons produced by the [Fuel Component](fuel_component.md)s into ones that will cause more fission in other fuel components. Moderators are placed in a straight line, no more than 4 blocks long. At one end of the moderator
line there must be an *active* fuel component.

The neutron flux generated at the other end will be equal to the sum of the flux factors of each of the moderators in that line. If there are active fuel cells on both ends of the moderator line, then both cells will receive neutron flux equal to the sum of the flux factors of each moderator in the
line. In some design rules, it may refer to "Active" moderators. Active moderators are moderators directly adjacent to an active [Fuel Component](fuel_component.md).

<Row fullWidth={true}>
<Column>

### Requires 1 Source:

<GameScene zoom="2" interactive={true}>
<ImportStructure src="basic_moderator.snbt" />
<BlockAnnotationTemplate id="fission_fuel_cell">
<BlockAnnotation>
Receiving <nuclearcraftneohaul:DataMapInfo datamap="fission_moderator_data" type="heavy_water_moderator" value="fluxFactor" mult="3" /> N/t

<ItemImage id="leu_233_ni" /> It contains a LEU-233 Nitride Fuel Pellet

which needs <nuclearcraftneohaul:FuelInfo id="leu_233_ni" value="getFissionFuelCriticality" /> N/t to work
</BlockAnnotation>
</BlockAnnotationTemplate>

<BlockAnnotationTemplate id="radium_beryllium_source">
<BlockAnnotation color="#22cc00">
Neutron Source sends Neutron Flux (N) along the line
</BlockAnnotation>
</BlockAnnotationTemplate>

<BlockAnnotation x="1" y="0" z="0" color="#22cc00">
This moderator is <Color id="green">active</Color>, because it is beside an <Color id="green">active</Color> Fuel Component

It contributes <nuclearcraftneohaul:DataMapInfo datamap="fission_moderator_data" type="heavy_water_moderator" value="fluxFactor" /> N/t
</BlockAnnotation>
<BlockAnnotation x="2" y="0" z="0" color="#cccccc">
This moderator is <Color id="red">inactive</Color>

It contributes <nuclearcraftneohaul:DataMapInfo datamap="fission_moderator_data" type="heavy_water_moderator" value="fluxFactor" /> N/t
</BlockAnnotation>
<BlockAnnotation x="3" y="0" z="0" color="#22cc00">
This moderator is <Color id="green">active</Color>, because it is beside an <Color id="green">active</Color> Fuel Component

It contributes <nuclearcraftneohaul:DataMapInfo datamap="fission_moderator_data" type="heavy_water_moderator" value="fluxFactor" /> N/t
</BlockAnnotation>

<LineAnnotation from="0.5 0.5 1.5" to="0.5 0.5 0.5" color="#ff0000" alwaysOnTop={true} />
<LineAnnotation from="0.5 0.5 0.5" to="4.5 0.5 0.5" color="#ff0000" alwaysOnTop={true} >
Line shows the path of the Neutron Flux (N) from the Neutron Source to the Fuel Components
</LineAnnotation>
<DiamondAnnotation pos="0.5 0.5 0.5" color="#ff0000" />
<DiamondAnnotation pos="4.5 0.5 0.5" color="#ff0000" />
<IsometricCamera yaw="0" roll="0" pitch="60" />
</GameScene>
</Column>

<Column alignItems="center">
### Requires 2 Sources:

<GameScene zoom="2" interactive={true}>
<ImportStructure src="4_cell_moderator.snbt" />
<BlockAnnotationTemplate id="fission_fuel_cell">
<BlockAnnotation>
Receiving <nuclearcraftneohaul:DataMapInfo datamap="fission_moderator_data" type="heavy_water_moderator" value="fluxFactor" mult="3" /> N/t

<ItemImage id="leu_233_ni" /> It contains a LEU-233 Nitride Fuel Pellet

which needs <nuclearcraftneohaul:FuelInfo id="leu_233_ni" value="getFissionFuelCriticality" /> N/t to work
</BlockAnnotation>
</BlockAnnotationTemplate>

<BlockAnnotationTemplate id="radium_beryllium_source">
<BlockAnnotation color="#22cc00">
Neutron Source sends Neutron Flux (N) along the line
</BlockAnnotation>
</BlockAnnotationTemplate>

<BlockAnnotation x="1" y="0" z="0" color="#22cc00">
This moderator is <Color id="green">active</Color>, because it is beside an <Color id="green">active</Color> Fuel Component

It contributes <nuclearcraftneohaul:DataMapInfo datamap="fission_moderator_data" type="heavy_water_moderator" value="fluxFactor" /> N/t
</BlockAnnotation>
<BlockAnnotation x="2" y="0" z="0" color="#cccccc">
This moderator is <Color id="red">inactive</Color>

It contributes <nuclearcraftneohaul:DataMapInfo datamap="fission_moderator_data" type="heavy_water_moderator" value="fluxFactor" /> N/t
</BlockAnnotation>
<BlockAnnotation x="3" y="0" z="0" color="#22cc00">
This moderator is <Color id="green">active</Color>, because it is beside an <Color id="green">active</Color> Fuel Component

It contributes <nuclearcraftneohaul:DataMapInfo datamap="fission_moderator_data" type="heavy_water_moderator" value="fluxFactor" /> N/t
</BlockAnnotation>

<LineAnnotation from="0.5 0.5 5.5" to="0.5 0.5 0.5" color="#ff0000" alwaysOnTop={true} />
<LineAnnotation from="0.5 0.5 4.5" to="4.5 0.5 4.5" color="#ff0000" alwaysOnTop={true} >
Line shows the path of the Neutron Flux (N) from the Neutron Source to the Fuel Components
</LineAnnotation>

<LineAnnotation from="5.5 0.5 0.5" to="0.5 0.5 0.5" color="#ffff00" alwaysOnTop={true} />
<LineAnnotation from="4.5 0.5 4.5" to="4.5 0.5 0.5" color="#ffff00" alwaysOnTop={true} >
Line shows the path of the Neutron Flux (N) from the Neutron Source to the Fuel Components
</LineAnnotation>
<DiamondAnnotation pos="0.5 0.5 0.5" color="#ff0000" />
<DiamondAnnotation pos="0.5 0.5 4.5" color="#ff0000" />
<DiamondAnnotation pos="4.5 0.5 0.5" color="#ffff00" />
<DiamondAnnotation pos="4.5 0.5 4.5" color="#ffff00" />
<IsometricCamera yaw="0" roll="0" pitch="60" />
</GameScene>
</Column>
</Row>


## Broken Examples:
<Row fullWidth={true}>
<Column>
### Moderator line too short:

<GameScene zoom="2" interactive={true}>
<ImportStructure src="basic_moderator.snbt" />
<Block x="1" id="nuclearcraftneohaul:fission_fuel_cell" />
<Block x="2" id="minecraft:air" />
<Block x="3" id="minecraft:air" />
<Block x="4" id="minecraft:air" />

<IsometricCamera yaw="0" roll="0" pitch="60" />
</GameScene>
</Column>
<Column alignItems="center">
### Moderator line too long:

<GameScene zoom="2" interactive={true}>
<ImportStructure src="basic_moderator.snbt" />
<Block x="4" id="nuclearcraftneohaul:heavy_water_moderator" />
<Block x="5" id="nuclearcraftneohaul:heavy_water_moderator" />
<Block x="6" id="nuclearcraftneohaul:fission_fuel_cell" />

<IsometricCamera yaw="0" roll="0" pitch="60" />
</GameScene>
</Column>
</Row>