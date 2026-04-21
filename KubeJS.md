### Examples: [kubejs_examples](kubejs_examples)

### Recipes:

TODO

### Addons:

All of the following are to be added in startup_scripts

```js
StartupEvents.registry('block', event => {
    event.create(...)
})
```

### Turbine

#### Rotor Blade

``` js
/*
setBladeData(efficiency, expansion_coefficient)
*/
event.create("palestium_rotor_blade", "nuclearcraftneohaul:rotor_blade").setBladeData(1.5, 2.0)
```

#### Rotor Stator

``` js
/*
setStatorData(expansion_coefficient)
*/
event.create("palestium_rotor_stator", "nuclearcraftneohaul:rotor_stator").setStatorData(9.0)
```

#### Dynamo Coil

``` js
/*
setDynamoCoilData(conductivity, placement_rule, singular, plural)
*/
event.create("titanium_dynamo_coil", "nuclearcraftneohaul:dynamo_coil")
    .setDynamoCoilData(1.2, "three beryllium coil", "%s valid titanium dynamo coil", "%s valid titanium dynamo coils")

event.create("palestium_dynamo_coil", "nuclearcraftneohaul:dynamo_coil")
    .setDynamoCoilData(2.0, "one kubejs:titanium_dynamo_coil coil", "%s valid palestium dynamo coil", "%s valid palestium dynamo coils")
```

### Battery

``` js
/*
setBatteryData(capacity, max_transfer)
*/
event.create("test_battery", "nuclearcraftneohaul:battery").setBatteryData(1000, 5)
```

### RTG

``` js
/*
setRTGData(power, radiation)
*/
event.create("test_rtg", "nuclearcraftneohaul:rtg").setRTGData(2400000, 0.09)
```

### Heat Exchanger

#### Heat Exchanger Tube

``` js
/*
setHXTubeData(heatTransferCoefficient, heatRetentionMult)
*/
event.create("test_hx_tube", "nuclearcraftneohaul:hx_tube").setHXTubeData(0.9, 0.09)
```

### Fission

#### Fission Source

``` js
/*
setSourceData(efficiency)
*/
event.create("test_source", "nuclearcraftneohaul:fission_source").setSourceData(0.9)
```

#### Fission Shield

``` js
/*
setShieldData(heatPerFlux, efficiency)
*/
event.create("test_shield", "nuclearcraftneohaul:fission_shield").setShieldData(0.9, 1.2)
```

#### Fission Heat Sink

``` js
/*
setHeatSinkData(coolingRate, placement_rule, singular, plural)
*/
event.create("test_sink", "nuclearcraftneohaul:fission_heat_sink").setHeatSinkData(116, "one moderator", "%s valid test sink", "%s valid test sinks")
```

#### Fission Coolant Heater

Creates both the coolant heater and coolant heater port. It requires a nak type to be created and is needed in the coolant heater recipe

``` js
/*
setCoolantHeaterData(coolingRate, fluid_id, placement_rule, singular, plural)
global["test_heater"]: The heater's id to be used in the recipe
global["test_nak"]: The nak's id
*/
event.create("test_coolant_heater", "nuclearcraftneohaul:fission_coolant_heater_and_port").setCoolantHeaterData(116, "kubejs:test_nak", "one moderator", "%s functional test heater", "%s functional test heaters")
// To use the ids created more easily:
global["test_heater"] = event.create("test_coolant_heater", "nuclearcraftneohaul:fission_coolant_heater_and_port").setCoolantHeaterData(116, global["test_nak"], "one moderator", "%s functional test heater", "%s functional test heaters").id
```

The nak can be created as follows:

```js
StartupEvents.registry('fluid', event => {
    global["test_nak"] = event.create("test_nak", "nuclearcraftneohaul:nak").tint(0xff0000).id
    global["test_hot_nak"] = event.create("test_hot_nak", "nuclearcraftneohaul:hot_nak").tint(0xff1100).id
})
```

Finally the recipe:

```js
    event.custom({
    "type": "nuclearcraftneohaul:fission_heater_recipe",
    "fluidIngredient": {
        "amount": 1,
        "ingredient": {
            "fluid": global["test_nak"].toString()
        }
    },
    "fluidProduct": {
        "amount": 1,
        "ingredient": {
            "fluid": global["test_hot_nak"].toString()
        }
    },
    "heater": {
        "count": 1,
        "id": global["test_heater"].toString()
    },
    "irradiatorFluxRequired": 155,
    "placementRule": global["test_heater"].toString()
})
```