package ca.farms4life2016.productive_foxes.tag_bloat;

import ca.farms4life2016.productive_foxes.ProductiveFoxes;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.concurrent.CompletableFuture;

/**
 * NOTE: to data gen, run the "runData" Gradle task (under "runClient")
 */
@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD, modid = ProductiveFoxes.MOD_ID)
public class DataGeneratorHandler {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        // Data generators may require some of these as constructor parameters.
        // See below for more details on each of these.
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        ProductiveFoxes.LOGGER.error("Adding tags! PLEASE FREAKING WORK!");

        // store references
        var blockProvider = new DummyBlockProvider(output, lookupProvider, existingFileHelper);
        var foxFoodProvider = new FoxFoodProvider(output, lookupProvider, blockProvider.contentsGetter(), existingFileHelper);

        // Register the providers
        generator.addProvider(event.includeServer(), blockProvider);
        generator.addProvider(event.includeServer(), foxFoodProvider);

    }
}
