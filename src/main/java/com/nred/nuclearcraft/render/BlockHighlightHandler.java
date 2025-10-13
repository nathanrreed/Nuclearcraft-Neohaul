/**
 * All credit goes to McJty: 'highlightBlock' comes from XNet: mcjty.xnet.RenderWorldLastEventHandler.renderHilightedBlock, 'renderBlockOutline' comes from McJtyLib: mcjty.lib.client.BlockOutlineRenderer.renderHighLightedBlocksOutline, the other methods comes from XNet: mcjty.xnet.ClientInfo.
 */

package com.nred.nuclearcraft.render;

import com.nred.nuclearcraft.NuclearcraftNeohaul;
import com.nred.nuclearcraft.util.NCMath;
import it.unimi.dsi.fastutil.longs.Long2LongMap;
import it.unimi.dsi.fastutil.longs.LongArrayList;
import it.unimi.dsi.fastutil.longs.LongList;
import it.zerono.mods.zerocore.internal.client.RenderTypes;
import it.zerono.mods.zerocore.lib.client.render.ModRenderHelper;
import it.zerono.mods.zerocore.lib.data.gfx.Colour;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;

@OnlyIn(Dist.CLIENT)
public class BlockHighlightHandler {
    LongList expiredCache = new LongArrayList();

    private static Long2LongMap getHighlightMap() {
        return NuclearcraftNeohaul.blockOverlayTracker.getHighlightMap();
    }

    @SubscribeEvent
    public void highlightBlocks(RenderLevelStageEvent event) {
        if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_LEVEL) return;
        for (Long2LongMap.Entry entry : getHighlightMap().long2LongEntrySet()) {
            highlightBlock(event, entry);
        }
        for (long expired : expiredCache) {
            getHighlightMap().remove(expired);
        }
        expiredCache.clear();
    }

    public void highlightBlock(RenderLevelStageEvent event, Long2LongMap.Entry entry) {
        BlockPos pos = BlockPos.of(entry.getLongKey());

        long time = System.currentTimeMillis();

        if (time > entry.getLongValue()) {
            expiredCache.add(entry.getLongKey());
            return;
        }

        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) {
            return;
        }

        float r = (float) NCMath.trapezoidalWave(time * 0.18D, 0D);
        float g = (float) NCMath.trapezoidalWave(time * 0.18D, 120D);
        float b = (float) NCMath.trapezoidalWave(time * 0.18D, 240D);

        final Vec3 projectedView = event.getCamera().getPosition();

        // TODO test vs original
        ModRenderHelper.paintVoxelShape(Shapes.block(), event.getPoseStack(), Minecraft.getInstance().renderBuffers().bufferSource(), RenderTypes.ERROR_BLOCK_HIGHLIGHT, pos.getX() - projectedView.x, pos.getY() - projectedView.y, pos.getZ() - projectedView.z, new Colour(r, g, b));
    }
}
