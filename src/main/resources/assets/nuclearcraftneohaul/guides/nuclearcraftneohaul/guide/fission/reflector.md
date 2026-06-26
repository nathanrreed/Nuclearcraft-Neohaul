---
navigation:
  title: Reflector
  icon: beryllium_carbon_reflector
  parent: fission/fission.md
  position: 7

categories:
  - General Fission

data_maps:
  - fission_reflector_data
---

# Reflectors

When placed at the end of a [Moderator](moderators.md) line reflects neutron flux back into the source. Thus, a reflector with 100% reflectivity will result in double the flux in the fuel cell, since the neutrons double back over the [Moderators](moderators.md) without any loss. Reflectors must be
no farther than two moderators from the active fuel component

<GameScene zoom="2" interactive={true}>
<ImportStructure src="reflector.snbt" />

<LineAnnotation from="0.5 0.5 1.5" to="0.5 0.5 0.6" color="#ff0000" alwaysOnTop={true} />

<LineAnnotation from="0.5 0.5 0.6" to="3.5 0.5 0.6" color="#ff0000" alwaysOnTop={true} >

Line shows the path of the Neutron Flux (N)

</LineAnnotation>
<LineAnnotation from="3.5 0.5 0.4" to="0.5 0.5 0.4" color="#ff0000" alwaysOnTop={true} >

Line shows the path of the Neutron Flux (N)

</LineAnnotation>
<LineAnnotation from="3.5 0.5 0.4" to="3.5 0.5 0.6" color="#ff0000" alwaysOnTop={true} >

Line shows the path of the Neutron Flux (N)

</LineAnnotation>

<IsometricCamera yaw="0" roll="0" pitch="60" />
</GameScene>