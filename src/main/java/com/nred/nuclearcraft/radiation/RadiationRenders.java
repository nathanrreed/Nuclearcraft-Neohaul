package com.nred.nuclearcraft.radiation;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.nred.nuclearcraft.block_entity.radiation.GeigerCounterEntity;
import com.nred.nuclearcraft.block_entity.radiation.RadiationScrubberEntity;
import com.nred.nuclearcraft.capability.radiation.entity.IEntityRads;
import com.nred.nuclearcraft.helpers.GuiHelper;
import com.nred.nuclearcraft.util.NCMath;
import com.nred.nuclearcraft.util.UnitHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.RenderGuiLayerEvent;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;
import org.joml.Matrix4f;

import static com.nred.nuclearcraft.NuclearcraftNeohaul.MODID;
import static com.nred.nuclearcraft.config.NCConfig.*;
import static com.nred.nuclearcraft.helpers.Location.ncLoc;
import static com.nred.nuclearcraft.registration.BlockRegistration.GEIGER_COUNTER_BLOCK;
import static com.nred.nuclearcraft.registration.BlockRegistration.RADIATION_SCRUBBER;
import static com.nred.nuclearcraft.registration.CapabilityRegistration.CAPABILITY_ENTITY_RADS;
import static com.nred.nuclearcraft.registration.ItemRegistration.GEIGER_COUNTER;

@OnlyIn(Dist.CLIENT)
public class RadiationRenders {
    private static final ResourceLocation RADS_BAR = ncLoc("hud/" + "rads_bar");

    private static final Component IMMUNE = Component.translatable("hud." + MODID + ".rad_immune");
    private static final Component NO_RADS = Component.literal("0 Rad/t");

    /**
     * Originally from coolAlias' 'Tutorial-Demo' - tutorial.client.gui.GuiManaBar
     */
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void addRadiationInfo(RenderGuiLayerEvent.Post event) {
        if (!event.getName().equals(VanillaGuiLayers.HOTBAR) || !radiation_enabled_public) {
            return;
        }

        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;
        if (!RadiationHelper.shouldShowHUD(player)) {
            return;
        }

        IEntityRads playerRads = player.getCapability(CAPABILITY_ENTITY_RADS, null);
        if (playerRads == null) {
            return;
        }
        GuiGraphics guiGraphics = event.getGuiGraphics();

        int barWidth = NCMath.toInt(100D * playerRads.getTotalRads() / playerRads.getMaxRads());
        Component info = playerRads.isImmune() ? playerRads.getRadiationImmunityStage() ? IMMUNE : Component.translatable("hud." + MODID + ".rad_immune_for", UnitHelper.applyTimeUnitShort(playerRads.getRadiationImmunityTime(), 3, 1)) : playerRads.isRadiationNegligible() ? NO_RADS : Component.literal(RadiationHelper.radsPrefix(playerRads.getRadiationLevel(), true));
        int infoWidth = mc.font.width(info);
        int overlayWidth = NCMath.toInt(Math.round(Math.max(104, infoWidth) * radiation_hud_size));
        int overlayHeight = NCMath.toInt(Math.round(19 * radiation_hud_size));

        int xPos = NCMath.toInt(Math.round(radiation_hud_position_cartesian.length >= 2 ? radiation_hud_position_cartesian[0] * guiGraphics.guiWidth() : GuiHelper.getRenderPositionXFromAngle(guiGraphics.guiWidth(), radiation_hud_position, overlayWidth, 3) / radiation_hud_size));
        int yPos = NCMath.toInt(Math.round(radiation_hud_position_cartesian.length >= 2 ? radiation_hud_position_cartesian[1] * guiGraphics.guiHeight() : GuiHelper.getRenderPositionYFromAngle(guiGraphics.guiHeight(), radiation_hud_position, overlayHeight, 3) / radiation_hud_size));

        guiGraphics.pose().pushPose();
        guiGraphics.pose().scale((float) radiation_hud_size, (float) radiation_hud_size, 1f);

        guiGraphics.blitSprite(RADS_BAR, 256, 256, 0, 0, xPos, yPos, 104, 10);
        guiGraphics.blitSprite(RADS_BAR, 256, 256, 100 - barWidth, 10, xPos + 2 + 100 - barWidth, yPos + 2, barWidth, 6);
        yPos += 12;
        if (radiation_hud_text_outline) {
            guiGraphics.drawString(mc.font, info, xPos + 1 + (104 - infoWidth) / 2, yPos, 0);
            guiGraphics.drawString(mc.font, info, xPos - 1 + (104 - infoWidth) / 2, yPos, 0);
            guiGraphics.drawString(mc.font, info, xPos + (104 - infoWidth) / 2, yPos + 1, 0);
            guiGraphics.drawString(mc.font, info, xPos + (104 - infoWidth) / 2, yPos - 1, 0);
        }
        guiGraphics.drawString(mc.font, info, xPos + (104 - infoWidth) / 2, yPos, playerRads.isImmune() ? ChatFormatting.GREEN.getColor() : RadiationHelper.getRadiationTextColor(playerRads).getColor());

        guiGraphics.pose().popPose();
    }

    /**
     * Thanks to dizzyd for this method!
     */
    @SubscribeEvent
    public void onRenderWorldLastEvent(RenderLevelStageEvent event) {
        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_WEATHER) return; // AFTER_LEVEL causes render to be off compared to F3 + G chunks
        // Overlay renderer for the Geiger counter and radiation scrubber blocks
        boolean chunkBorders = false;

        // Bail fast if rendering is disabled
        if (!radiation_chunk_boundaries) {
            return;
        }

        Minecraft mc = Minecraft.getInstance();
        for (InteractionHand hand : InteractionHand.values()) {
            ItemStack heldStack = mc.player.getItemInHand(hand);
            Item heldItem = heldStack.getItem();
            if (GEIGER_COUNTER.asItem() == heldItem || GEIGER_COUNTER_BLOCK.asItem() == heldItem || RADIATION_SCRUBBER.asItem() == heldItem) {
                chunkBorders = true;
                break;
            }
        }

        if (!chunkBorders) {
            HitResult ray = mc.hitResult;
            if (ray != null && ray.getType() == HitResult.Type.BLOCK) {
                BlockEntity te = mc.level.getBlockEntity(((BlockHitResult) ray).getBlockPos());
                if (te instanceof GeigerCounterEntity || te instanceof RadiationScrubberEntity) {
                    chunkBorders = true;
                }
            }
        }

        if (chunkBorders) {
            // Code from net.minecraft.client.renderer.debug.ChunkBorderRenderer
            Vec3 cam = event.getCamera().getPosition();
            Entity entity = event.getCamera().getEntity();
            float minHeight = (float) ((double) mc.level.getMinBuildHeight() - cam.y);
            float maxHeight = (float) (entity.getY() - cam.y + 10);
            ChunkPos chunkpos = entity.chunkPosition();
            float chunkX = (float) ((double) chunkpos.getMinBlockX() - cam.x);
            float chunkZ = (float) ((double) chunkpos.getMinBlockZ() - cam.z);
            VertexConsumer vertexconsumer = mc.renderBuffers().bufferSource().getBuffer(RenderType.LINES);
            event.getPoseStack().pushPose();
            Matrix4f matrix4f = event.getPoseStack().last().pose();

            RenderSystem.enableDepthTest();
            RenderSystem.depthMask(true);

            for (int k1 = 0; k1 <= 16; k1 += 16) {
                for (int k2 = 0; k2 <= 16; k2 += 16) {
                    vertexconsumer.addVertex(matrix4f, chunkX + (float) k1, minHeight, chunkZ + (float) k2).setColor(1F, 0, 0, 1).setNormal(0, 1, 0);
                    vertexconsumer.addVertex(matrix4f, chunkX + (float) k1, maxHeight, chunkZ + (float) k2).setColor(1F, 0, 0, 1).setNormal(0, 1, 0);
                }
            }

            RenderSystem.disableDepthTest();
            RenderSystem.depthMask(false);
            event.getPoseStack().popPose();
        }
    }
}