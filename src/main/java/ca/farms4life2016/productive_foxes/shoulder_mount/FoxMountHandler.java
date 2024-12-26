package ca.farms4life2016.productive_foxes.shoulder_mount;

import ca.farms4life2016.productive_foxes.ProductiveFoxes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Fox;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

import java.util.Objects;

@EventBusSubscriber(modid = ProductiveFoxes.MOD_ID)
public class FoxMountHandler {

    @SubscribeEvent
    public static void onEntityInteract(PlayerInteractEvent.EntityInteractSpecific event) {
        var player = event.getEntity();

        if (event.getLevel().isClientSide() || ! (event.getTarget() instanceof Fox fox)) {
            // no idea why this clientside check is necessary
        } else {

            // check that player is crouching, has an empty hand, and fox is a baby
            if (player.isCrouching() && player.getMainHandItem().isEmpty() && fox.isBaby()) {

                // place fox on shoulder?
                attachFoxToShoulder(player, fox);
                event.setCanceled(true);

            }
        }

    }

    public static void attachFoxToShoulder(Player player, Fox fox) {

        // try to place the fox on the player's shoulder
        if (player.getShoulderEntityLeft().isEmpty() || player.getShoulderEntityRight().isEmpty()) {

            CompoundTag foxData = new CompoundTag();
            fox.saveWithoutId(foxData);
            foxData.putString("id", Objects.requireNonNull(fox.getEncodeId()));

            player.setEntityOnShoulder(foxData);
            fox.discard();

            player.displayClientMessage(Component.literal("You put the baby fox on your shoulder!"), true);

        } else {
            player.displayClientMessage(Component.literal("Your shoulders are occupied!"), true);
        }
    }

}
