---
navigation:
  title: Irradiator
  icon: fission_irradiator
  parent: fission/fission.md
  
categories:
  - General Fission

item_ids:
  - fission_irradiator
---

# Irradiators

When placed at the end of a [Moderator](moderators.md) line, <Color id="dark_purple">Irradiator</Color>s will use the neutron flux for crafting recipes. The higher the neutron flux, the faster the crafting recipe will complete. When activated, irradiators will generate heat and release radiation
into the environment.

<GameScene zoom="2" interactive={true}>
<ImportStructure src="basic_moderator.snbt" />
<Block x="4" id="fission_irradiator" />

<BlockAnnotationTemplate id="fission_fuel_cell">
<BlockAnnotation>
Receiving <nuclearcraftneohaul:DataMapInfo datamap="fission_moderator_data" type="heavy_water_moderator" value="fluxFactor" mult="3" /> N/t

<ItemImage id="leu_233_ni" /> It contains a LEU-233 Nitride Fuel Pellet

which needs <nuclearcraftneohaul:FuelInfo id="leu_233_ni" value="getFissionFuelCriticality" /> N/t to work
</BlockAnnotation>
</BlockAnnotationTemplate>

<BlockAnnotationTemplate id="fission_irradiator">
<BlockAnnotation>
Receiving <nuclearcraftneohaul:DataMapInfo datamap="fission_moderator_data" type="heavy_water_moderator" value="fluxFactor" mult="3" /> N/t
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
This moderator is <Color id="green">active</Color>, when the irradiator is processing a recipe

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