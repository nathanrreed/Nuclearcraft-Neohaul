### Recipes:
TODO

### Addons:
All of the following are to be added in
```js
StartupEvents.registry('block', event => {
    event.create(...)
})
```

#### Rotor Blades
``` js
/*
setBladeData(efficiency, expansion_coefficient)
*/
event.create("palestium_rotor_blade", "nuclearcraftneohaul:rotor_blade").setBladeData(1.5, 2.0)
```