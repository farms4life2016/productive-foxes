package ca.farms4life2016.productive_foxes;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.animal.Fox;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

import static ca.farms4life2016.productive_foxes.ProductiveFoxes.MODID;

@EventBusSubscriber(modid = MODID)
public class FoxBreedingHandler {

    /**
     * Activates when the player tries to use an item on a fox
     * @param event
     */
    @SubscribeEvent
    public static void onPlayerInteractWithFox(PlayerInteractEvent.EntityInteract event) {
        // check if player interacts with a fox
        if (event.getTarget() instanceof Fox fox) {
            Player player = event.getEntity();
            ItemStack stack = event.getItemStack();

            // check if item in hand was delphox berries
            if (stack.is(ProductiveFoxes.DELPHOX_BERRY_ITEM.get())) {

                // need this check to prevent accidental heart particle generator
                if (fox.isBaby()) {
                    // reduce growing timer by 10%
                    fox.ageUp(- AgeableMob.getSpeedUpSecondsWhenFeeding(fox.getAge()));

                    // green sparkles
                    fox.level().broadcastEntityEvent(fox, (byte) 7);

                } else if (fox.canFallInLove()) { // adult
                    // make the fox horny. the cause is the player, so the kit will trust that player
                    fox.setInLove(player);
                    fox.level().broadcastEntityEvent(fox, (byte) 18); // this plays love hearts
                }

                // decrease stack size if not creative
                if (!player.isCreative()) {
                    stack.shrink(1);
                }

                // successful interaction? idk why we have to cancel early
                event.setCancellationResult(InteractionResult.SUCCESS);
                event.setCanceled(true);
            }
        }
    }
}
