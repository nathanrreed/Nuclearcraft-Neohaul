package com.nred.nuclearcraft.compat.guideme;

import guideme.Guide;
import guideme.GuideItemSettings;
import guideme.compiler.TagCompiler;
import guideme.indices.ItemIndex;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.registries.datamaps.DataMapsUpdatedEvent;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Optional;

import static com.nred.nuclearcraft.NuclearcraftNeohaul.MODID;
import static com.nred.nuclearcraft.helpers.Location.ncLoc;

public class ModGuideMEPlugin {
    private static Guide guide;

    public static void onDataMapChanged(DataMapsUpdatedEvent event) {
        if (event.getCause() == DataMapsUpdatedEvent.UpdateCause.CLIENT_SYNC) {
            try {
                guide.getClass().getDeclaredMethod("rebuildIndices").invoke(guide);
            } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException ignored) {
            }
        }
    }

    public static void createGuidebook() {
        guide = createGuide();
    }

    private static Guide createGuide() {
        return Guide.builder(ncLoc("guide")).index(ItemIndex.class, new ItemIndexWrapper())
                .extension(TagCompiler.EXTENSION_POINT, new DataMapTagExtension())
                .extension(TagCompiler.EXTENSION_POINT, new FuelTagExtension())
                .extension(TagCompiler.EXTENSION_POINT, new FluidLinkCompiler())
                .itemSettings(new GuideItemSettings(Optional.of(Component.translatable(MODID + ".guide_book.name")), List.of(Component.translatable(MODID + ".guide_book.edition")), Optional.empty()))
                .build();
    }
}