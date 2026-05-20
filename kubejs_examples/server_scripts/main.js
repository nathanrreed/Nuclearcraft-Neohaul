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
})

