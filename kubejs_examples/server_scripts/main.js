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
        "irradiatorFluxRequired": 155,
        "placementRule": global["test_heater"].toString()
    })
})

