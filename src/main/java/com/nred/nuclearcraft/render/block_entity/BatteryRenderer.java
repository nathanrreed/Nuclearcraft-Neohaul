package com.nred.nuclearcraft.render.block_entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.nred.nuclearcraft.NuclearcraftNeohaul;
import com.nred.nuclearcraft.block_entity.battery.TileBattery;
import net.minecraft.client.Minecraft;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.model.lighting.SmoothQuadLighter;

import java.util.Arrays;
import java.util.List;

public class BatteryRenderer implements BlockEntityRenderer<TileBattery> {
    private final BlockEntityRendererProvider.Context context;

    public BatteryRenderer(BlockEntityRendererProvider.Context context) {
        this.context = context;
    }

    @Override
    public void render(TileBattery blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        SmoothQuadLighter quadLighter = new SmoothQuadLighter(BlockColors.createDefault());
        quadLighter.setup(blockEntity.getLevel(), blockEntity.getBlockPos(), blockEntity.getBlockState());
        String block_id = blockEntity.getBlockState().getBlock().getDescriptionId();
        block_id = block_id.substring(block_id.lastIndexOf('.') + 1);

        for (Direction dir : Direction.values()) {
            poseStack.pushPose();

            String location = "block/" + block_id + "/" + (dir.getAxis().isVertical() ? "top" : "side") + "_" + blockEntity.getEnergyConnection(dir).getSerializedName();
            TextureAtlasSprite sprite = Minecraft.getInstance().getTextureAtlas(TextureAtlas.LOCATION_BLOCKS).apply(ResourceLocation.fromNamespaceAndPath(NuclearcraftNeohaul.MODID, location));

            BakedModel blockModel = Minecraft.getInstance().getModelManager().getBlockModelShaper().getBlockModel(blockEntity.getBlockState());
            List<BakedQuad> newFace = blockModel.getQuads(blockEntity.getBlockState(), dir, blockEntity.getLevel().random, blockModel.getModelData(blockEntity.getLevel(), blockEntity.getBlockPos(), blockEntity.getBlockState(), null), RenderType.SOLID);

            for (BakedQuad quad : newFace) {
                int[] vertices = quad.getVertices();

                // Put the UVs of the sprite into the vertices to make them use the correct texture
                vertices[4] = Float.floatToRawIntBits(sprite.getU(0));
                vertices[5] = Float.floatToRawIntBits(sprite.getV(0));

                vertices[12] = Float.floatToRawIntBits(sprite.getU(0));
                vertices[13] = Float.floatToRawIntBits(sprite.getV(1));

                vertices[20] = Float.floatToRawIntBits(sprite.getU(1));
                vertices[21] = Float.floatToRawIntBits(sprite.getV(1));

                vertices[28] = Float.floatToRawIntBits(sprite.getU(1));
                vertices[29] = Float.floatToRawIntBits(sprite.getV(0));

                BakedQuad sideQuad = new BakedQuad(Arrays.copyOf(vertices, vertices.length), quad.getTintIndex(), quad.getDirection(), sprite, true, true);

                quadLighter.computeLightingForQuad(sideQuad);
                bufferSource.getBuffer(RenderType.SOLID).putBulkData(poseStack.last(), sideQuad, quadLighter.getComputedBrightness(), 1f, 1f, 1f, 1f, quadLighter.getComputedLightmap(), packedOverlay, false);
            }

            poseStack.popPose();
        }
    }
}