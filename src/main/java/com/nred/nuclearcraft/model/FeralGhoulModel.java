package com.nred.nuclearcraft.model;

import com.nred.nuclearcraft.entity.FeralGhoul;
import net.minecraft.client.model.AbstractZombieModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;

import static com.nred.nuclearcraft.helpers.Location.ncLoc;

public class FeralGhoulModel extends AbstractZombieModel<FeralGhoul> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ncLoc("feral_ghoul"), "main");

    public FeralGhoulModel(ModelPart root) {
        super(root);
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(-0.1F)), PartPose.offsetAndRotation(0.0F, 3.5F, -2.2F, 0.1134F, 0.0F, 0.0F));

        PartDefinition hat = partdefinition.addOrReplaceChild("hat", CubeListBuilder.create().texOffs(32, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.4F)), PartPose.offsetAndRotation(0.0F, 3.5F, -2.2F, 0.1134F, 0.0F, 0.0F));

        PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(16, 16).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(-0.2F)), PartPose.offsetAndRotation(0.0F, 2.4F, -2.7F, 0.4363F, 0.0F, 0.0F));

        PartDefinition right_arm = partdefinition.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(40, 16).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 16.0F, 4.0F, new CubeDeformation(-0.5F)), PartPose.offsetAndRotation(-4.2F, 4.4F, -1.7F, -0.3491F, -0.1F, 0.1F));

        PartDefinition left_arm = partdefinition.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(40, 16).mirror().addBox(-1.0F, -2.0F, -2.0F, 4.0F, 16.0F, 4.0F, new CubeDeformation(-0.5F)).mirror(false), PartPose.offsetAndRotation(4.2F, 4.4F, -1.7F, -0.3491F, 0.1F, -0.1F));

        PartDefinition left_leg = partdefinition.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(0, 16).mirror().addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(-0.2F)).mirror(false), PartPose.offsetAndRotation(1.9F, 12.0F, 2.1F, -0.0175F, 0.0F, -0.0262F));

        PartDefinition right_leg = partdefinition.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(-0.2F)), PartPose.offsetAndRotation(-1.9F, 12.0F, 2.1F, -0.0175F, 0.0F, 0.0262F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void setupAnim(FeralGhoul entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        boolean flag = entity.getFallFlyingTicks() > 4;

        head.yRot = netHeadYaw * (float) (Math.PI / 180.0);

        if (flag) {
            head.xRot = 0.11344640137963141F - (float) (Math.PI / 4);
        } else {
            head.xRot = 0.11344640137963141F + headPitch * (float) (Math.PI / 180.0);
        }

        body.yRot = 0F;

        float f = 1F;

        if (flag) {
            f = (float) entity.getDeltaMovement().lengthSqr();
            f = f / 0.2F;
            f = f * f * f;
        }

        if (f < 1F) {
            f = 1F;
        }

        rightArm.xRot = -0.3490658503988659F + Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * 2F * limbSwingAmount * 0.5F / f;
        leftArm.xRot = -0.3490658503988659F + Mth.cos(limbSwing * 0.6662F) * 2F * limbSwingAmount * 0.5F / f;
        rightArm.zRot = 0.10000736613927509F;
        leftArm.zRot = -0.10000736613927509F;
        rightLeg.xRot = -0.017453292519943295F + Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount / f;
        leftLeg.xRot = -0.017453292519943295F + Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount / f;
        rightLeg.yRot = 0F;
        leftLeg.yRot = 0F;
        rightLeg.zRot = 0.02617993877991494F;
        leftLeg.zRot = -0.02617993877991494F;

        if (riding) {
            rightArm.xRot += -0.3490658503988659F - (float) Math.PI / 5F;
            leftArm.xRot -= ((float) Math.PI / 5F);
            rightLeg.xRot = -1.4137167F;
            rightLeg.yRot = (float) Math.PI / 10F;
            rightLeg.zRot = 0.07853982F;
            leftLeg.xRot = -1.4137167F;
            leftLeg.yRot = -((float) Math.PI / 10F);
            leftLeg.zRot = -0.07853982F;
        }

        rightArm.yRot = -0.10000736613927509F;
        rightArm.zRot = 0.10000736613927509F;

        boolean handed = entity.getMainArm() == HumanoidArm.RIGHT;
        if (entity.isUsingItem()) {
            boolean mainHand = entity.getUsedItemHand() == InteractionHand.MAIN_HAND;
            if (mainHand == handed) {
                this.poseRightArm(entity);
            } else {
                this.poseLeftArm(entity);
            }
        } else {
            boolean twoHanded = handed ? this.leftArmPose.isTwoHanded() : this.rightArmPose.isTwoHanded();
            if (handed != twoHanded) {
                this.poseLeftArm(entity);
                this.poseRightArm(entity);
            } else {
                this.poseRightArm(entity);
                this.poseLeftArm(entity);
            }
        }

        if (attackTime > 0F) {
            HumanoidArm mainArm = entity.getMainArm();
            ModelPart arm = getArm(mainArm);
            float f1 = attackTime;
            body.yRot = Mth.sin(Mth.sqrt(f1) * ((float) Math.PI * 2F)) * 0.2F;

            if (mainArm == HumanoidArm.LEFT) {
                body.yRot *= -1F;
            }

            rightArm.zRot = Mth.sin(body.yRot) * 5F;
            rightArm.xRot = -Mth.cos(body.yRot) * 5F;
            leftArm.zRot = -Mth.sin(body.yRot) * 5F;
            leftArm.xRot = Mth.cos(body.yRot) * 5F;
            rightArm.yRot += body.yRot;
            leftArm.yRot += body.yRot;
            f1 = 1F - attackTime;
            f1 = f1 * f1;
            f1 = f1 * f1;
            f1 = 1F - f1;
            float f2 = Mth.sin(f1 * (float) Math.PI);
            float f3 = Mth.sin(attackTime * (float) Math.PI) * -(head.xRot - 0.7F) * 0.75F;
            arm.xRot = (float) (arm.xRot - (f2 * 1.2D + f3));
            arm.yRot += body.yRot * 2F;
            arm.zRot += Mth.sin(attackTime * (float) Math.PI) * -0.4F;
        }

        this.setupAttackAnimation(entity, ageInTicks);

        body.xRot = 0.4363323129985824F;

        rightArm.zRot += Mth.cos(ageInTicks * 0.09F) * 0.05F + 0.05F;
        leftArm.zRot -= Mth.cos(ageInTicks * 0.09F) * 0.05F + 0.05F;
        rightArm.xRot += Mth.sin(ageInTicks * 0.067F) * 0.05F;
        leftArm.xRot -= Mth.sin(ageInTicks * 0.067F) * 0.05F;

        this.hat.copyFrom(this.head);

        rightArm.zRot += Mth.cos(ageInTicks * 0.09F) * 0.05F + 0.05F;
        leftArm.zRot -= Mth.cos(ageInTicks * 0.09F) * 0.05F + 0.05F;
        rightArm.xRot += Mth.sin(ageInTicks * 0.067F) * 0.05F;
        leftArm.xRot -= Mth.sin(ageInTicks * 0.067F) * 0.05F;
    }

    private void poseLeftArm(FeralGhoul entity) {
        switch (this.leftArmPose) {
            case EMPTY:
                leftArm.yRot = 0.10000736613927509F;
                break;
            case BLOCK:
                leftArm.xRot = leftArm.xRot * 0.5F - 0.9424779F;
                leftArm.yRot = 0.5235988F;
                break;
            case ITEM:
                leftArm.xRot = leftArm.xRot * 0.5F - (float) Math.PI / 10F;
                leftArm.yRot = 0F;
                break;
            default:
                break;
        }
    }

    private void poseRightArm(FeralGhoul entity) {
        switch (this.rightArmPose) {
            case EMPTY:
                rightArm.yRot = -0.10000736613927509F;
                break;
            case BLOCK:
                rightArm.xRot = rightArm.xRot * 0.5F - 0.9424779F;
                rightArm.yRot = -0.5235988F;
                break;
            case ITEM:
                rightArm.xRot = rightArm.xRot * 0.5F - (float) Math.PI / 10F;
                rightArm.yRot = 0F;
                break;
            default:
                break;
        }
    }

    @Override
    public boolean isAggressive(FeralGhoul entity) {
        return entity.isAggressive();
    }
}