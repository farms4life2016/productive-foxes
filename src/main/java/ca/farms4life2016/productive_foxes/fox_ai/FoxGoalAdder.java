package ca.farms4life2016.productive_foxes.fox_ai;

import ca.farms4life2016.productive_foxes.ProductiveFoxes;
import com.mojang.logging.LogUtils;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Fox;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import org.slf4j.Logger;

@EventBusSubscriber(modid = ProductiveFoxes.MOD_ID, bus = EventBusSubscriber.Bus.GAME)
public class FoxGoalAdder {

    private static final Logger LOGGER = LogUtils.getLogger();

    @SubscribeEvent
    public static void onEntityJoinLevel(EntityJoinLevelEvent event) {
        Entity entity = event.getEntity();

        if (entity.getType() == EntityType.FOX && entity instanceof Fox fox) {
            fox.goalSelector.addGoal(10, new FoxEatCustomBerriesGoal(fox, 1.2F, 12 ,1));
            // LOGGER.info("Custom Berry Goal added to Fox");
        }
    }
}
