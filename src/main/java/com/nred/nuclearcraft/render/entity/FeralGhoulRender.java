package com.nred.nuclearcraft.render.entity;

import com.nred.nuclearcraft.entity.FeralGhoul;
import com.nred.nuclearcraft.model.FeralGhoulModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import static com.nred.nuclearcraft.helpers.Location.ncLoc;

@OnlyIn(Dist.CLIENT)
public class FeralGhoulRender extends HumanoidMobRenderer<FeralGhoul, FeralGhoulModel> {
    private static final ResourceLocation FERAL_GHOUL_TEXTURE = ncLoc("textures/entity/feral_ghoul.png");

    public FeralGhoulRender(EntityRendererProvider.Context context) {
        super(context, new FeralGhoulModel(context.bakeLayer(FeralGhoulModel.LAYER_LOCATION)), 0.5f);
    }

    @Override
    public ResourceLocation getTextureLocation(FeralGhoul entity) {
        return FERAL_GHOUL_TEXTURE;
    }
}