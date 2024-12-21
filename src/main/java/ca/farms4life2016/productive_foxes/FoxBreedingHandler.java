package ca.farms4life2016.productive_foxes;

import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.animal.Fox;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

import static ca.farms4life2016.productive_foxes.ProductiveFoxes.MOD_ID;

@EventBusSubscriber(modid = MOD_ID, bus = EventBusSubscriber.Bus.GAME)
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
                    // feed baby to make it grow up faster

                    // reduce growing timer by 10%
                    fox.ageUp(- AgeableMob.getSpeedUpSecondsWhenFeeding(fox.getAge()), true);

                    // decrease stack size if not creative
                    if (!player.isCreative()) {
                        stack.shrink(1);
                    }

                } else if (fox.getAge() == 0 && !fox.isInLove()) {
                    // adult with zero breeding cooldown (age = 0) and not currently horny

                    // make the fox horny. the cause is the player, so the resulting kit will trust that player
                    fox.setInLove(player);
                    fox.level().broadcastEntityEvent(fox, (byte) 18); // this plays love hearts

                    // plays the eating sound sfx (for some reason it doesn't play for adults but it does for kits)
                    fox.playSound(fox.getEatingSound(stack), 1.0F, 1.0F);

                    // decrease stack size if not creative
                    if (!player.isCreative()) {
                        stack.shrink(1);
                    }
                }

            }
        }
    }
}
