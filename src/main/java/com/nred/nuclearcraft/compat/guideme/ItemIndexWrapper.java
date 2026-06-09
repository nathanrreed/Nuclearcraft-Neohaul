package com.nred.nuclearcraft.compat.guideme;

import com.google.gson.stream.JsonWriter;
import guideme.GuidePageChange;
import guideme.PageAnchor;
import guideme.compiler.ParsedGuidePage;
import guideme.indices.ItemIndex;
import net.minecraft.resources.ResourceLocation;
import org.jspecify.annotations.Nullable;

import java.io.IOException;
import java.util.List;

public class ItemIndexWrapper extends ItemIndex {
    private final ExtendedItemIndex proxy;

    public ItemIndexWrapper() {
        super();
        proxy = new ExtendedItemIndex();
    }

    @Override
    public String getName() {
        return proxy.getName();
    }

    @Override
    public @Nullable PageAnchor get(ResourceLocation key) {
        return proxy.get(key);
    }

    @Override
    public boolean supportsUpdate() {
        return proxy.supportsUpdate();
    }

    @Override
    public void rebuild(List<ParsedGuidePage> pages) {
        proxy.rebuild(pages);
    }

    @Override
    public void update(List<ParsedGuidePage> allPages, List<GuidePageChange> changes) {
        proxy.update(allPages, changes);
    }

    @Override
    public void export(JsonWriter writer) throws IOException {
        proxy.export(writer);
    }
}