package ca.farms4life2016.productive_foxes.shoulder_mount;

import ca.farms4life2016.productive_foxes.ProductiveFoxes;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

@EventBusSubscriber(modid = ProductiveFoxes.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class FoxLayerHandler {

    @SubscribeEvent
    public static void onRegisterLayers(EntityRenderersEvent.AddLayers event) {

        // no idea what this is
        var modelSet = Minecraft.getInstance().getEntityModels();

        // add shoulder fox to player render
        for (var skin : event.getSkins()) {
            PlayerRenderer renderer = event.getSkin(skin);
            renderer.addLayer(new FoxShoulderLayer(renderer, modelSet));
        }
    }

}
