package com.nred.nuclearcraft.compat.guideme;

import guideme.Guide;
import guideme.PageAnchor;
import guideme.compiler.IdUtils;
import guideme.compiler.ParsedGuidePage;
import guideme.indices.UniqueIndex;
import net.minecraft.ResourceLocationException;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.datamaps.DataMapType;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.nred.nuclearcraft.registration.DataMapTypeRegistration.*;

/**
 * An index of Minecraft DataMaps to the main guidebook page describing it.
 * <p/>
 * This index is installed by default on all {@linkplain Guide guides}.
 */
public class ExtendedItemIndex extends UniqueIndex<ResourceLocation, PageAnchor> {
    private static final Logger LOG = LoggerFactory.getLogger(ExtendedItemIndex.class);

    public ExtendedItemIndex() {
        super("Extended Item Index",
                ExtendedItemIndex::getAllItemAnchors,
                (writer, value) -> writer.value(value.toString()),
                (writer, value) -> writer.value(value.toString()));
    }

    private static List<Pair<ResourceLocation, PageAnchor>> getAllItemAnchors(ParsedGuidePage page) {
        return Stream.concat(Stream.concat(getDataMapItemAnchors(page).stream(), getBlockEntityItemAnchors(page).stream()), getItemAnchors(page).stream()).collect(Collectors.toSet()).stream().toList();
    }

    private static List<Pair<ResourceLocation, PageAnchor>> getDataMapItemAnchors(ParsedGuidePage page) {
        var dataMapIdsNode = page.getFrontmatter().additionalProperties().get("data_maps");
        if (dataMapIdsNode == null) {
            return List.of();
        }

        if (!(dataMapIdsNode instanceof List<?> dataMapIdList)) {
            LOG.warn("Page {} contains malformed data_maps frontmatter", page.getId());
            return List.of();
        }

        var itemAnchors = new ArrayList<Pair<ResourceLocation, PageAnchor>>();

        for (var listEntry : dataMapIdList) {
            if (listEntry instanceof String dataMapIdStr) {
                DataMapType<Block, ?> dataMap = switch (dataMapIdStr) {
                    case "fission_moderator_data" -> FISSION_MODERATOR_DATA;
                    case "fission_reflector_data" -> FISSION_REFLECTOR_DATA;
                    case "machine_diaphragm_data" -> MACHINE_DIAPHRAGM_DATA;
                    case "sieve_assembly_data" -> MACHINE_SIEVE_ASSEMBLY_DATA;
                    case "electrolyzer_cathode_data" -> ELECTROLYZER_CATHODE_DATA;
                    case "electrolyzer_anode_data" -> ELECTROLYZER_ANODE_DATA;
                    default -> null;
                };
                if (dataMap != null) {
                    for (ResourceKey<Block> block : BuiltInRegistries.BLOCK.getDataMap(dataMap).keySet()) {
                        itemAnchors.add(Pair.of(block.location(), new PageAnchor(page.getId(), null)));
                    }
                } else {
                    LOG.warn("Page {} contains a malformed data_maps frontmatter entry: {}", page.getId(), listEntry);
                }
            }
        }

        return itemAnchors;
    }

    private static List<Pair<ResourceLocation, PageAnchor>> getBlockEntityItemAnchors(ParsedGuidePage page) {
        var blockEntityIdsNode = page.getFrontmatter().additionalProperties().get("block_entity_types");
        if (blockEntityIdsNode == null) {
            return List.of();
        }

        if (!(blockEntityIdsNode instanceof List<?> dataMapIdList)) {
            LOG.warn("Page {} contains malformed block_entity_types frontmatter", page.getId());
            return List.of();
        }

        var itemAnchors = new ArrayList<Pair<ResourceLocation, PageAnchor>>();

        for (var listEntry : dataMapIdList) {
            if (listEntry instanceof String blockEntityIdStr) {
                ResourceLocation blockEntityId;
                try {
                    blockEntityId = IdUtils.resolveId(blockEntityIdStr, page.getId().getNamespace());
                } catch (ResourceLocationException e) {
                    LOG.warn("Page {} contains a malformed block_entity_types frontmatter entry: {}", page.getId(), listEntry);
                    continue;
                }

                if (BuiltInRegistries.BLOCK_ENTITY_TYPE.containsKey(blockEntityId)) {
                    // add a link to the top of the page
                    for (Block block : BuiltInRegistries.BLOCK_ENTITY_TYPE.get(blockEntityId).getValidBlocks()) {
                        itemAnchors.add(Pair.of(BuiltInRegistries.BLOCK.getKey(block), new PageAnchor(page.getId(), null)));
                    }
                } else {
                    LOG.warn("Page {} references an unknown item {} in its block_entity_types frontmatter", page.getId(), blockEntityId);
                }
            } else {
                LOG.warn("Page {} contains a malformed block_entity_types frontmatter entry: {}", page.getId(), listEntry);
            }
        }

        return itemAnchors;
    }

    private static List<Pair<ResourceLocation, PageAnchor>> getItemAnchors(ParsedGuidePage page) {
        var itemIdsNode = page.getFrontmatter().additionalProperties().get("item_ids");
        if (itemIdsNode == null) {
            return List.of();
        }

        if (!(itemIdsNode instanceof List<?> itemIdList)) {
            LOG.warn("Page {} contains malformed item_ids frontmatter", page.getId());
            return List.of();
        }

        var itemAnchors = new ArrayList<Pair<ResourceLocation, PageAnchor>>();

        for (var listEntry : itemIdList) {
            if (listEntry instanceof String itemIdStr) {
                ResourceLocation itemId;
                try {
                    itemId = IdUtils.resolveId(itemIdStr, page.getId().getNamespace());
                } catch (ResourceLocationException e) {
                    LOG.warn("Page {} contains a malformed item_ids frontmatter entry: {}", page.getId(), listEntry);
                    continue;
                }

                if (BuiltInRegistries.ITEM.containsKey(itemId)) {
                    // add a link to the top of the page
                    itemAnchors.add(Pair.of(itemId, new PageAnchor(page.getId(), null)));
                } else {
                    LOG.warn("Page {} references an unknown item {} in its item_ids frontmatter", page.getId(), itemId);
                }
            } else {
                LOG.warn("Page {} contains a malformed item_ids frontmatter entry: {}", page.getId(), listEntry);
            }
        }

        return itemAnchors;
    }
}
