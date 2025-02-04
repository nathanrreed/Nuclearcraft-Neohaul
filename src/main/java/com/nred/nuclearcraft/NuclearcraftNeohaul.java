package com.nred.nuclearcraft;

import com.mojang.logging.LogUtils;
import com.nred.nuclearcraft.registration.Registration;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import org.slf4j.Logger;

@Mod(NuclearcraftNeohaul.MODID)
public class NuclearcraftNeohaul {
    public static final String MODID = "nuclearcraftneohaul";
    private static final Logger LOGGER = LogUtils.getLogger();

//    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> EXAMPLE_TAB = CREATIVE_MODE_TABS.register("example_tab", () -> CreativeModeTab.builder()
//            .title(Component.translatable("itemGroup.nuclearcraftneohaul")) //The language key for the title of your CreativeModeTab
//            .withTabsBefore(CreativeModeTabs.COMBAT)
//            .icon(() -> EXAMPLE_ITEM.get().getDefaultInstance())
//            .displayItems((parameters, output) -> {
//                output.accept(EXAMPLE_ITEM.get()); // Add the example item to the tab. For your own tabs, this method is preferred over the event
//            }).build());

    public NuclearcraftNeohaul(IEventBus modEventBus, ModContainer modContainer) {
        Registration.register(modEventBus);

//        NeoForge.EVENT_BUS.register(this);

        // Register the item to a creative tab
//        modEventBus.addListener(this::addCreative);

        // Register our mod's ModConfigSpec so that FML can create and load the config file for us
//        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }


//    private void addCreative(BuildCreativeModeTabContentsEvent event) {
//        if (event.getTabKey() == CreativeModeTabs.BUILDING_BLOCKS){
//               event.accept(EXAMPLE_BLOCK_ITEM);
//        }
//    }
}