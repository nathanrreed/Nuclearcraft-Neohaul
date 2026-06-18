package com.nred.nuclearcraft.compat.guideme;

import guideme.compiler.PageCompiler;
import guideme.compiler.tags.MdxAttrs;
import guideme.document.LytErrorSink;
import guideme.libs.mdast.mdx.model.MdxJsxElementFields;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.Fluid;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.Nullable;

public class GuideMEHelpers {
    @Nullable
    public static Pair<ResourceLocation, Fluid> getRequiredFluidAndId(PageCompiler compiler, LytErrorSink errorSink, MdxJsxElementFields el) {
        ResourceLocation fluidId =  MdxAttrs.getRequiredId(compiler, errorSink, el, "id");

        Fluid resultFluid = BuiltInRegistries.FLUID.getOptional(fluidId).orElse(null);
        if (resultFluid == null) {
            errorSink.appendError(compiler, "Missing fluid: " + fluidId, el);
            return null;
        }
        return Pair.of(fluidId, resultFluid);
    }
}