package ca.farms4life2016.productive_foxes.shoulder_mount;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.FoxModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Fox;
import net.minecraft.world.entity.player.Player;

public class FoxShoulderLayer<T extends Player> extends RenderLayer<T, PlayerModel<T> > {

    private final FoxModel<Fox> babyFoxModel;

    private static final ResourceLocation RED_FOX_TEXTURE = ResourceLocation.withDefaultNamespace("textures/entity/fox/fox.png");
    private static final ResourceLocation SNOW_FOX_TEXTURE = ResourceLocation.withDefaultNamespace("textures/entity/fox/snow_fox.png");

    public static final ResourceLocation BABY_FOX_TEXTURE =
            ResourceLocation.withDefaultNamespace("textures/entity/fox/baby_fox.png");


    public FoxShoulderLayer(RenderLayerParent<T, PlayerModel<T>> pRenderer, EntityModelSet pModelSet) {
        super(pRenderer);
        babyFoxModel = new FoxModel<>(pModelSet.bakeLayer(ModelLayers.FOX));
    }

    @Override
    public void render(PoseStack pPoseStack,
                       MultiBufferSource pBufferSource,
                       int pPackedLight,
                       T player,
                       float pLimbSwing,
                       float pLimbSwingAmount,
                       float pPartialTick,
                       float pAgeInTicks,
                       float pNetHeadYaw,
                       float pHeadPitch
    ) {
        // render left and right foxes
        this.render(pPoseStack, pBufferSource, pPackedLight, player, pLimbSwing, pLimbSwingAmount, pPartialTick, pAgeInTicks, pNetHeadYaw, pHeadPitch, true);
        this.render(pPoseStack, pBufferSource, pPackedLight, player, pLimbSwing, pLimbSwingAmount, pPartialTick, pAgeInTicks, pNetHeadYaw, pHeadPitch, false);
    }

    /**
     * Renders a shoulder fox. set left == true to render the left fox.
     * otherwise, render the right fox
     * @param pPoseStack
     * @param pBufferSource
     * @param pPackedLight
     * @param player
     * @param pLimbSwing
     * @param pLimbSwingAmount
     * @param pPartialTick
     * @param pAgeInTicks
     * @param pNetHeadYaw
     * @param pHeadPitch
     * @param left
     */
    public void render(PoseStack pPoseStack, 
                       MultiBufferSource pBufferSource, 
                       int pPackedLight, 
                       T player, 
                       float pLimbSwing, 
                       float pLimbSwingAmount, 
                       float pPartialTick, 
                       float pAgeInTicks, 
                       float pNetHeadYaw, 
                       float pHeadPitch,
                       boolean left
    ) {
        // fox check?
        CompoundTag foxData = left ? player.getShoulderEntityLeft() : player.getShoulderEntityRight();

        // check if we need to render a fox "minecraft:fox"
        if (!foxData.isEmpty() && foxData.getString("id").equals("minecraft:fox") ) {
            pPoseStack.pushPose();

            // place fox on player's shoulder
            pPoseStack.translate( left ? 0.4f : -0.4f, player.isCrouching() ? -1.3f : -1.5f, 0f);

            // create a temp Fox to base the model off of
            Fox temp = EntityType.FOX.create(player.level());
            if (temp != null) {
                temp.load(foxData);
                temp.stopSleeping(); // wake up the fox
                temp.setSitting(true); // force the fox to sit

                // get fox's variant (red or snow)
                var variant = temp.getVariant();

                // render fox model
                var consumer = pBufferSource.getBuffer(babyFoxModel.renderType(variant.equals(Fox.Type.RED) ? RED_FOX_TEXTURE : SNOW_FOX_TEXTURE));
                babyFoxModel.setupAnim(temp, pLimbSwing, pLimbSwingAmount, pAgeInTicks, pNetHeadYaw, pHeadPitch);
                babyFoxModel.renderToBuffer(pPoseStack, consumer, pPackedLight, OverlayTexture.NO_OVERLAY);
            }

            pPoseStack.popPose();
        }

    }
}
