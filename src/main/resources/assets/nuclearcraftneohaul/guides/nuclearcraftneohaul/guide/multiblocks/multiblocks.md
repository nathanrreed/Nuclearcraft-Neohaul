---
navigation:
  title: Multiblocks
  position: 2
---

# Multiblocks

| Multiblock                               | Multiblock Type | Casing Type                              | Glass Type                             |
|------------------------------------------|-----------------|------------------------------------------|----------------------------------------|
| [Fission Reactor](../fission/fission.md) | Cuboidal        | <ItemLink id="fission_reactor_casing" /> | <ItemLink id="fission_glass" />        |
| [Turbine](turbine.md)                    | Cuboidal        | <ItemLink id="turbine_casing" />         | <ItemLink id="turbine_glass" />        |
| [Heat Exchanger](heat_exchanger.md)      | Cuboidal        | <ItemLink id="heat_exchanger_casing" />  | <ItemLink id="heat_exchanger_glass" /> |

### Cuboidal Multiblocks

A Cuboidal multiblock is a consists of a frame and walls.

#### Frame
The frame must always be made of the multiblocks associated casing type (See above)

#### Wall
The walls must be made of the multiblocks associated casing type, glass type (See above) or other multiblock parts such as ports


In the following example the dirt blocks are the frame and the clay blocks are the walls

<GameScene zoom="1" interactive={true}>
<ImportStructure src="cuboidal.snbt" />
</GameScene>