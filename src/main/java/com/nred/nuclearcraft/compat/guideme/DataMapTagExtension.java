package com.nred.nuclearcraft.compat.guideme;

import guideme.compiler.IdUtils;
import guideme.compiler.PageCompiler;
import guideme.compiler.tags.FlowTagCompiler;
import guideme.document.flow.LytFlowParent;
import guideme.libs.mdast.mdx.model.MdxJsxElementFields;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.datamaps.DataMapType;

import java.lang.reflect.InvocationTargetException;
import java.util.Set;

import static com.nred.nuclearcraft.NuclearcraftNeohaul.MODID;
import static com.nred.nuclearcraft.registration.DataMapTypeRegistration.*;

public class DataMapTagExtension extends FlowTagCompiler {
    @Override
    public Set<String> getTagNames() {
        return Set.of(MODID + ":DataMapInfo");
    }

    @Override
    protected void compile(PageCompiler compiler, LytFlowParent parent, MdxJsxElementFields el) {
        var datamap = el.getAttributeString("datamap", "");
        var type = el.getAttributeString("type", "");
        var value = el.getAttributeString("value", "");
        var multiple = el.getAttributeString("mult", "");

        if (datamap.isEmpty()) {
            parent.appendError(compiler, "datamap is required", el);
            return;
        }
        if (type.isEmpty()) {
            parent.appendError(compiler, "type is required", el);
            return;
        }
        if (value.isEmpty()) {
            parent.appendError(compiler, "value is required", el);
            return;
        }

        DataMapType<Block, ?> dataMap = switch (datamap) {
            case "fission_moderator_data" -> FISSION_MODERATOR_DATA;
            case "fission_reflector_data" -> FISSION_REFLECTOR_DATA;
            case "machine_diaphragm_data" -> MACHINE_DIAPHRAGM_DATA;
            case "sieve_assembly_data" -> MACHINE_SIEVE_ASSEMBLY_DATA;
            case "electrolyzer_cathode_data" -> ELECTROLYZER_CATHODE_DATA;
            case "electrolyzer_anode_data" -> ELECTROLYZER_ANODE_DATA;
            default -> null;
        };

        if (dataMap == null) {
            parent.appendError(compiler, "datamap " + datamap + " not found", el);
            return;
        }

        ResourceLocation location = IdUtils.resolveId(type, compiler.getPageId().getNamespace());
        if (BuiltInRegistries.BLOCK.get(location).defaultBlockState().isAir()) {
            parent.appendError(compiler, "type " + location + " not found", el);
            return;
        }

        Object data = BuiltInRegistries.BLOCK.getData(dataMap, ResourceKey.create(Registries.BLOCK, location));
        if (data == null) {
            parent.appendError(compiler, "type " + location + " has no DataMap for " + datamap, el);
            return;
        }
        try {
            var val = data.getClass().getDeclaredMethod(value).invoke(data);
            if (!multiple.isEmpty() && val instanceof Integer num){
                val = num * Integer.decode(multiple);
            }
            parent.appendText(val.toString());
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            parent.appendError(compiler, "value " + value + " is not a field of DataMap " + datamap, el);
        }
    }
}