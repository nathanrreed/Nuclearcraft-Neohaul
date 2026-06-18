---
navigation:
  title: Heat Exchanger
  icon: heat_exchanger_controller
  parent: multiblocks/multiblocks.md

item_ids:
  - heat_exchanger_controller
  - heat_exchanger_casing
  - heat_exchanger_glass
---

# Heat Exchanger

Heat exchanger tubes have their modes changed by the <ItemLink id="multitool" />

<Row>
<Column alignItems="center">

#### Hot Path: 12 blocks
#### Cold Path: 6 blocks
<GameScene zoom="2">
<ImportStructure src="hx_no_wrap.snbt" />
<LineAnnotation from="-0.25 0.5 1.5" to="4.37 0.5 1.5" color="#ff0000" thickness="0.25" >
Path length: 12
</LineAnnotation>
<LineAnnotation from="4.5 0.5 1.37" to="4.5 0.5 2.37" color="#ff0000" thickness="0.25" >
Path length: 12
</LineAnnotation>
<LineAnnotation from="4.62 0.5 2.5" to="1.63 0.5 2.5" color="#ff0000" thickness="0.25" >
Path length: 12
</LineAnnotation>
<LineAnnotation from="1.5 0.5 2.37" to="1.5 0.5 3.38" color="#ff0000" thickness="0.25" >
Path length: 12
</LineAnnotation>
<LineAnnotation from="1.37 0.5 3.5" to="6.25 0.5 3.5" color="#ff0000" thickness="0.25" >
Path length: 12
</LineAnnotation>

<LineAnnotation from="1.25 0.5 -0.25" to="1.25 0.5 4" color="#0000ff" thickness="0.25"  >
Path length: 6
</LineAnnotation>
<LineAnnotation from="1.38 0.5 3.88" to="4.62 0.5 3.88" color="#0000ff" thickness="0.25"  >
Path length: 6
</LineAnnotation>
<LineAnnotation from="4.5 0.5 4" to="4.5 0.5 5.25" color="#0000ff" thickness="0.25"  >
Path length: 6
</LineAnnotation>

<IsometricCamera yaw="0" pitch="90" />
</GameScene>
</Column>

<Column alignItems="center">

#### Hot Path: 12 blocks
#### Cold Path: 12 blocks
<GameScene zoom="2">
<ImportStructure src="hx_wrap.snbt" />
<LineAnnotation from="-0.25 0.5 1.5" to="1.13 0.5 1.5" color="#ff0000" thickness="0.25" >
Path length: 12
</LineAnnotation>

<LineAnnotation from="1.13 0.5 1.5" to="4.37 0.5 1.5" color="#ff00ff" thickness="0.25" >

Path length: 12

</LineAnnotation>
<LineAnnotation from="4.5 0.5 1.37" to="4.5 0.5 2.37" color="#ff00ff" thickness="0.25" >
Path length: 12
</LineAnnotation>
<LineAnnotation from="4.62 0.5 2.5" to="1.63 0.5 2.5" color="#ff00ff" thickness="0.25" >
Path length: 12
</LineAnnotation>
<LineAnnotation from="1.5 0.5 2.37" to="1.5 0.5 3.38" color="#ff00ff" thickness="0.25" >
Path length: 12
</LineAnnotation>
<LineAnnotation from="1.37 0.5 3.5" to="4.63 0.5 3.5" color="#ff00ff" thickness="0.25" >
Path length: 12
</LineAnnotation>
<LineAnnotation from="4.63 0.5 3.5" to="6.25 0.5 3.5" color="#ff0000" thickness="0.25" >
Path length: 12
</LineAnnotation>

<LineAnnotation from="1.25 0.5 -0.25" to="1.25 0.5 1.37" color="#0000ff" thickness="0.25"  >
Path length: 12
</LineAnnotation>
<LineAnnotation from="4.5 0.5 3.62" to="4.5 0.5 5.25" color="#0000ff" thickness="0.25"  >
Path length: 12
</LineAnnotation>

<IsometricCamera yaw="0" pitch="90" />
</GameScene>
</Column>
</Row>