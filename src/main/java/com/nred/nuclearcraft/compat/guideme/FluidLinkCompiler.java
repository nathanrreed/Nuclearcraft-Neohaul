package com.nred.nuclearcraft.compat.guideme;

import guideme.color.SymbolicColor;
import guideme.compiler.PageCompiler;
import guideme.compiler.tags.FlowTagCompiler;
import guideme.document.flow.LytFlowParent;
import guideme.document.flow.LytTooltipSpan;
import guideme.libs.mdast.mdx.model.MdxJsxElementFields;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidStack;

import java.util.Set;

public class FluidLinkCompiler extends FlowTagCompiler {
    @Override
    public Set<String> getTagNames() {
        return Set.of("FluidLink");
    }

    @Override
    public void compile(PageCompiler compiler, LytFlowParent parent, MdxJsxElementFields el) {
        var fluidAndId = GuideMEHelpers.getRequiredFluidAndId(compiler, parent, el);
        if (fluidAndId == null) {
            return;
        }
        ResourceLocation id = fluidAndId.getLeft();
        Fluid fluid = fluidAndId.getRight();

        var span = new LytTooltipSpan();
        span.modifyStyle(style -> style.color(SymbolicColor.LIGHT_PURPLE));
        span.appendComponent(fluid.getFluidType().getDescription());
        span.setTooltip(new FluidTooltip(new FluidStack(fluid, 1000)));
        parent.append(span);
    }

}
