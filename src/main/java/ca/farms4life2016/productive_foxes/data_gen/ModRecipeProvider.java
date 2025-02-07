package ca.farms4life2016.productive_foxes.data_gen;

import ca.farms4life2016.productive_foxes.ProductiveFoxes;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.Tags;

import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends RecipeProvider {
    public ModRecipeProvider(PackOutput pOutput, CompletableFuture<HolderLookup.Provider> pRegistries) {
        super(pOutput, pRegistries);
    }

    @Override
    protected void buildRecipes(RecipeOutput output) {

        // cook sour berries into extra-sour berries
        SimpleCookingRecipeBuilder.smelting(
                Ingredient.of(ProductiveFoxes.SOUR_BERRIES_ITEM),
                RecipeCategory.FOOD,
                ProductiveFoxes.EXTRA_SOUR_BERRIES_ITEM,
                0.1f,
                200
        ).unlockedBy("has_sour_berries_item", has(ProductiveFoxes.SOUR_BERRIES_ITEM)).save(output, "sour_berries_smelting");
        SimpleCookingRecipeBuilder.smoking(
                Ingredient.of(ProductiveFoxes.SOUR_BERRIES_ITEM),
                RecipeCategory.FOOD,
                ProductiveFoxes.EXTRA_SOUR_BERRIES_ITEM,
                0.1f,
                200
        ).unlockedBy("has_sour_berries_item", has(ProductiveFoxes.SOUR_BERRIES_ITEM)).save(output, "sour_berries_smoking");
        SimpleCookingRecipeBuilder.campfireCooking(
                Ingredient.of(ProductiveFoxes.SOUR_BERRIES_ITEM),
                RecipeCategory.FOOD,
                ProductiveFoxes.EXTRA_SOUR_BERRIES_ITEM,
                0.1f,
                200
        ).unlockedBy("has_sour_berries_item", has(ProductiveFoxes.SOUR_BERRIES_ITEM)).save(output, "sour_berries_campfire_cooking");

        // shapeless - craft sugary berry mix
        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, ProductiveFoxes.SUGARY_SWEET_BERRY_MIX, 2)
                .requires(Items.SUGAR, 5)
                .requires(Items.SWEET_BERRIES, 3)
                .requires(ProductiveFoxes.EXTRA_SOUR_BERRIES_ITEM, 1)
                .unlockedBy("has_sugar", has(Items.SUGAR))
                .unlockedBy("has_sweet_berries", has(Items.SWEET_BERRIES))
                .unlockedBy("has_extra_sour_berries", has(ProductiveFoxes.EXTRA_SOUR_BERRIES_ITEM))
                .save(output, "sugary_sweet_berry_mix_shapeless");

        // shaped - craft empty jam jars
        ShapedRecipeBuilder.shaped(RecipeCategory.FOOD, ProductiveFoxes.EMPTY_JAM_JAR, 3)
                .pattern(" I ")
                .pattern("G G")
                .pattern(" G ")
                .define('I', Items.IRON_INGOT)
                .define('G', Ingredient.of(Tags.Items.GLASS_BLOCKS_COLORLESS)) // note: should use glass tag?
                .unlockedBy("has_iron_nugget", has(Items.IRON_INGOT))
                .unlockedBy("has_glass", has(Items.GLASS))
                .save(output, "empty_jam_jar_iron_shaped");
    }
}
