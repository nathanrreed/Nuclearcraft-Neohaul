StartupEvents.registry('fluid', event => {
    global["test_nak"] = event.create("test_nak", "nuclearcraftneohaul:nak").tint(0xff0000).id
    global["test_hot_nak"] = event.create("test_hot_nak", "nuclearcraftneohaul:hot_nak").tint(0xff1100).id
})

StartupEvents.registry('block', event => {
    event.create("titanium_rotor_blade", "nuclearcraftneohaul:rotor_blade").setBladeData(1.5, 2.0)
    event.create("titanium_rotor_stator", "nuclearcraftneohaul:rotor_stator").setStatorData(9.0)
    event.create("titanium_dynamo_coil", "nuclearcraftneohaul:dynamo_coil").setDynamoCoilData(1.2, "one beryllium coil", "%s valid titanium dynamo coil", "%s valid titanium dynamo coils")
    event.create("test_dynamo_coil", "nuclearcraftneohaul:dynamo_coil").setDynamoCoilData(2.0, "one kubejs:titanium_dynamo_coil coil", "%s valid test dynamo coil", "%s valid test dynamo coils")

    event.create("test_source", "nuclearcraftneohaul:fission_source").setSourceData(0.9)
    event.create("test_shield", "nuclearcraftneohaul:fission_shield").setShieldData(0.9, 1.2)
    event.create("test_sink", "nuclearcraftneohaul:fission_heat_sink").setHeatSinkData(116, "one moderator", "%s valid test sink", "%s valid test sinks")
    global["test_heater"] = event.create("test_coolant_heater", "nuclearcraftneohaul:fission_coolant_heater_and_port").setCoolantHeaterData(116, global["test_nak"], "one moderator", "%s functional test heater", "%s functional test heaters").id

    event.create("test_rtg", "nuclearcraftneohaul:rtg").setRTGData(2400000, 0.09)
    event.create("test_hx_tube", "nuclearcraftneohaul:hx_tube").setHXTubeData(0.9, 0.09)
    event.create("test_battery", "nuclearcraftneohaul:battery").setBatteryData(1000, 5)
})