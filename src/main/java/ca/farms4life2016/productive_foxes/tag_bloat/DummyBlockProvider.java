package ca.farms4life2016.productive_foxes.tag_bloat;

import ca.farms4life2016.productive_foxes.ProductiveFoxes;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

public class DummyBlockProvider extends BlockTagsProvider {
    public DummyBlockProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, ProductiveFoxes.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider lookupProvider) {
        // dafaq
        ProductiveFoxes.LOGGER.error("adding block tags!");
    }
}
