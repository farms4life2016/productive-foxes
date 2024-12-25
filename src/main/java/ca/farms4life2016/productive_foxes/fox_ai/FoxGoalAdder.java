package ca.farms4life2016.productive_foxes.fox_ai;

import ca.farms4life2016.productive_foxes.ProductiveFoxes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Fox;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;

@EventBusSubscriber(modid = ProductiveFoxes.MOD_ID, bus = EventBusSubscriber.Bus.GAME)
public class FoxGoalAdder {

    @SubscribeEvent
    public static void onEntityJoinLevel(EntityJoinLevelEvent event) {
        Entity entity = event.getEntity();

        if (entity.getType() == EntityType.FOX && entity instanceof Fox fox) {
            fox.goalSelector.addGoal(10, new FoxEatCustomBerriesGoal(fox, 1.2F, 12 ,1));
            ProductiveFoxes.LOGGER.info("Custom Berry Goal added to Fox");
        }
    }
}
