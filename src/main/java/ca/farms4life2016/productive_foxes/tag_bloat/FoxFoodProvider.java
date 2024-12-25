package ca.farms4life2016.productive_foxes.tag_bloat;

import ca.farms4life2016.productive_foxes.ProductiveFoxes;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;

import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

public class FoxFoodProvider extends ItemTagsProvider {

    public FoxFoodProvider(
        PackOutput output,
        CompletableFuture<HolderLookup.Provider> lookupProvider,
        CompletableFuture<TagsProvider.TagLookup<Block>> pBlockTags,
        ExistingFileHelper existingFileHelper) {

        super(output, lookupProvider, pBlockTags, ProductiveFoxes.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider lookupProvider) {
        // add all our custom berries to tag
        ProductiveFoxes.LOGGER.error("adding item tags");
        tag(ItemTags.FOX_FOOD)
                .add(Items.APPLE)
                .add(ProductiveFoxes.DELPHOX_BERRY_ITEM.get());
    }
}
