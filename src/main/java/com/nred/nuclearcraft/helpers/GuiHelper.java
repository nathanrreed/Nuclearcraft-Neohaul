package com.nred.nuclearcraft.helpers;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import org.joml.Matrix4f;

import java.util.List;

public class GuiHelper {

    public static List<Float> colorToFloat(int color) {
        return List.of(FastColor.ARGB32.red(color) / 255.0f, FastColor.ARGB32.green(color) / 255.0f, FastColor.ARGB32.blue(color) / 255.0f, FastColor.ARGB32.alpha(color) / 255.0f);
    }

    public static void blitTile(GuiGraphics guiGraphics, TextureAtlasSprite sprite, int x, int y, int width, int height, int tWidth, int tHeight, int color) {
        List<Float> c = colorToFloat(color);

        guiGraphics.setColor(c.get(0), c.get(1), c.get(2), c.get(3));
        blitTiledSprite(guiGraphics, sprite, x, y, width, height, tWidth, tHeight);
        guiGraphics.setColor(1f, 1f, 1f, 1f);
    }


    // Below taken from GuiGraphics
    static void innerBlit(GuiGraphics guiGraphics, ResourceLocation atlasLocation, int x1, int x2, int y1, int y2, float minU, float maxU, float minV, float maxV) {
        RenderSystem.setShaderTexture(0, atlasLocation);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        Matrix4f matrix4f = guiGraphics.pose().last().pose();
        BufferBuilder bufferbuilder = Tesselator.getInstance().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        bufferbuilder.addVertex(matrix4f, (float) x1, (float) y1, (float) 0).setUv(minU, minV);
        bufferbuilder.addVertex(matrix4f, (float) x1, (float) y2, (float) 0).setUv(minU, maxV);
        bufferbuilder.addVertex(matrix4f, (float) x2, (float) y2, (float) 0).setUv(maxU, maxV);
        bufferbuilder.addVertex(matrix4f, (float) x2, (float) y1, (float) 0).setUv(maxU, minV);
        BufferUploader.drawWithShader(bufferbuilder.buildOrThrow());
    }

    private static void blitSprite(GuiGraphics guiGraphics, TextureAtlasSprite sprite, int textureWidth, int textureHeight, int x, int y, int uWidth, int vHeight) {
        if (uWidth != 0 && vHeight != 0) {
            innerBlit(guiGraphics, sprite.atlasLocation(), x, x + uWidth, y, y + vHeight, sprite.getU(0), sprite.getU((float) (uWidth) / (float) textureWidth), sprite.getV(0), sprite.getV((float) (vHeight) / (float) textureHeight));
        }
    }

    private static void blitTiledSprite(GuiGraphics guiGraphics, TextureAtlasSprite sprite, int x, int y, int width, int height, int spriteWidth, int spriteHeight) {
        if (width > 0 && height > 0) {
            if (spriteWidth > 0 && spriteHeight > 0) {
                for (int i = 0; i < width; i += spriteWidth) {
                    int j = Math.min(spriteWidth, width - i);
                    for (int k = 0; k < height; k += spriteHeight) {
                        int l = Math.min(spriteHeight, height - k);
                        blitSprite(guiGraphics, sprite, spriteWidth, spriteHeight, x + i, y + k, j, l);
                    }
                }
            } else {
                throw new IllegalArgumentException("Tiled sprite texture size must be positive, got " + spriteWidth + "x" + spriteHeight);
            }
        }
    }
}