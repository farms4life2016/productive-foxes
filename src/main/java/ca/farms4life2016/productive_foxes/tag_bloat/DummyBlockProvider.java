package ca.farms4life2016.productive_foxes.tag_bloat;

import ca.farms4life2016.productive_foxes.ProductiveFoxes;
import com.mojang.logging.LogUtils;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.slf4j.Logger;

import java.util.concurrent.CompletableFuture;

public class DummyBlockProvider extends BlockTagsProvider {

    private static final Logger LOGGER = LogUtils.getLogger();

    public DummyBlockProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, ProductiveFoxes.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider lookupProvider) {
        // dafaq
        LOGGER.error("adding block tags!");
    }
}
