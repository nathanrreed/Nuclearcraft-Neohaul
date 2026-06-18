---
navigation:
  title: Turbine
  icon: turbine_controller
  parent: multiblocks/multiblocks.md

item_ids:
  - turbine_controller
  - turbine_casing
  - turbine_glass
---

# Turbine

Turbines extract energy from pressurized gas, producing an exhaust fluid / gas in the process.

<Row>
<GameScene zoom="2" interactive={true}>
<ImportStructure src="turbine.snbt" />
<LineAnnotation from="1.5 0.5 3.5" to="7.5 0.5 3.5" color="#0000ff" thickness="0.25">
Gas enters through the inlet and exits through the outlet.

The inlet and outlet must be on opposite sides of the turbine and be on the same side as the bearing(s)

</LineAnnotation>
</GameScene>

<GameScene zoom="2" interactive={true}>
<ImportStructure src="turbine_large.snbt" />
<BoxAnnotation min="4 0 2" max="5 4 6" color="#118888" >
All blades on a rotor segment must be the same type.

Each segment can have a different type

</BoxAnnotation>
</GameScene>

<GameScene zoom="2" interactive={true}>
<ImportStructure src="turbine_complete.snbt" />
<Block x="0" y="1" z="2" id="turbine_controller" p:facing="west" />
<BoxAnnotation min="1 1 0" max="4 4 1" color="#118888" >
Dynamos placed according to their placement rules will create FE as gas moves the tubine blades
</BoxAnnotation>

<BoxAnnotation min="1 1 4" max="4 4 5" color="#118888" >
Dynamos placed according to their placement rules will create FE as gas moves the tubine blades
</BoxAnnotation>
</GameScene>
</Row>