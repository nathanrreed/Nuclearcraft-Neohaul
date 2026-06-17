ServerEvents.recipes(event => {
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
        "placementRule": global["test_heater"].toString()
    })

    event.custom({
        "type": "nuclearcraftneohaul:fission_cooler_recipe",
        "fluidIngredient": {
            "amount": 1,
            "ingredient": {
                "fluid": global["test_gas"].toString()
            }
        },
        "fluidProduct": {
            "amount": 1,
            "ingredient": {
                "fluid": global["test_gas_hot"].toString()
            }
        },
        "cooler": {
            "count": 1,
            "id": global["test_cooler"].toString()
        },
        "placementRule": global["test_cooler"].toString()
    })

    event.custom({
        "type": "kubejs:test_generator",
        "itemIngredients": [
            {
                "count": 1,
                "ingredient": {
                    "item": "minecraft:oak_log"
                }
            }
        ],
        "powerModifier": 1.0,
        "radiation": 0.0,
        "timeModifier": 1.0
    })
})

