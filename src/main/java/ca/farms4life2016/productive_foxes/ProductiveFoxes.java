package ca.farms4life2016.productive_foxes;

import ca.farms4life2016.productive_foxes.berry_resourceful.DelphoxBerryBush;
import ca.farms4life2016.productive_foxes.berry_resourceful.DelphoxBerryItem;
import ca.farms4life2016.productive_foxes.berry_resourceful.SweetBerryJamItem;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.Foods;
import net.minecraft.world.item.*;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.SoundType;
import net.neoforged.neoforge.event.brewing.RegisterBrewingRecipesEvent;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(ProductiveFoxes.MOD_ID)
public class ProductiveFoxes {

    private static final Logger LOGGER = LogUtils.getLogger();

    public static final String MOD_ID = "productive_foxes";

    // Deferred Registers
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(MOD_ID);
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MOD_ID);
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MOD_ID);

    /*
     * Add (real) blocks and items
     */
    // sour berries (lemons lol), half the hunger compared to sweet berries, same saturation
    private static final int SOUR_DURATION = 5*20, SOUR_AMPLIFIER = 4-1; // duration in game ticks, amplifier is 0-indexed
    public static final DeferredItem<Item> SOUR_BERRIES_ITEM = ITEMS.registerSimpleItem("sour_berries_item",
            new Item.Properties().food(new FoodProperties.Builder().nutrition(1).saturationModifier(0.1f)
                    .effect(()-> new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, SOUR_DURATION, SOUR_AMPLIFIER),1f)
                    .effect(()-> new MobEffectInstance(MobEffects.DIG_SLOWDOWN, SOUR_DURATION, SOUR_AMPLIFIER),1f)
                    .effect(()-> new MobEffectInstance(MobEffects.WEAKNESS, SOUR_DURATION, SOUR_AMPLIFIER),1f)
                    .alwaysEdible().build()));
    // extra sour berries (roasted lemons), more intense sour over less time
    private static final int EXTRA_SOUR_DURATION = 3*20, EXTRA_SOUR_AMPLIFIER = 6-1;
    public static final DeferredItem<Item> EXTRA_SOUR_BERRIES_ITEM = ITEMS.registerSimpleItem("extra_sour_berries_item",
            new Item.Properties().food(new FoodProperties.Builder().nutrition(1).saturationModifier(0.1f)
                    .effect(()-> new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, EXTRA_SOUR_DURATION, EXTRA_SOUR_AMPLIFIER),1f)
                    .effect(()-> new MobEffectInstance(MobEffects.DIG_SLOWDOWN, EXTRA_SOUR_DURATION, EXTRA_SOUR_AMPLIFIER),1f)
                    .effect(()-> new MobEffectInstance(MobEffects.WEAKNESS, EXTRA_SOUR_DURATION, EXTRA_SOUR_AMPLIFIER),1f)
                    .alwaysEdible().build()));

    // empty jam jar, hopefully we can place in the brewing stand
    public static final DeferredItem<Item> EMPTY_JAM_JAR = ITEMS.registerSimpleItem("empty_jam_jar_item");
    // sugary sweet berry sugary mix - intermediate brewing item
    public static final DeferredItem<Item> SUGARY_SWEET_BERRY_MIX = ITEMS.registerSimpleItem("sugary_sweet_berry_mix_item",
            new Item.Properties().food(new FoodProperties.Builder().nutrition(1).saturationModifier(0.1f)
                    .alwaysEdible().build()));
    // sweet berry jam - "brewed" from sugary sweet berry mix  NOTE: need to implement "giving the jar back" after eat
    public static final DeferredItem<Item> SWEET_BERRY_JAM = ITEMS.register("sweet_berry_jam_item",
            () -> new SweetBerryJamItem(new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(1).saturationModifier(0.1f)
                    .alwaysEdible().build())
                    .craftRemainder(EMPTY_JAM_JAR.get())));


    /*
     * Add testing blocks and items
     */
    public static final DeferredBlock<Block> FOX_BLOCK = BLOCKS.registerSimpleBlock("fox_block",
            BlockBehaviour.Properties.of().sound(SoundType.WOOL).lightLevel(s -> 15).strength(0.8F));
    // item for our block above ^
    public static final DeferredItem<BlockItem> FOX_BLOCK_ITEM = ITEMS.registerSimpleBlockItem("fox_block", FOX_BLOCK);

    // Creates a new food item with the id "productive_foxes:fox_item", nutrition 1 and saturation 2
    public static final DeferredItem<Item> FOX_ITEM = ITEMS.registerSimpleItem("fox_item", new Item.Properties().food(new FoodProperties.Builder()
            .alwaysEdible().nutrition(1).saturationModifier(2f).build()));

    // creates the berry bush block
    public static final DeferredBlock<Block> DELPHOX_BERRY_BUSH = BLOCKS.register("delphox_berry_bush", DelphoxBerryBush::new);
    // block-item form of the berry bush (unused)
    // public static final DeferredItem<BlockItem> DELPHOX_BERRY_BUSH_ITEM = ITEMS.registerSimpleBlockItem("delphox_berry_bush", DELPHOX_BERRY_BUSH);
    public static final DeferredItem<BlockItem> DELPHOX_BERRY_ITEM = ITEMS.registerItem("delphox_berry_item", bruh -> new DelphoxBerryItem(DELPHOX_BERRY_BUSH.get(), new Item.Properties().food(Foods.SWEET_BERRIES)));


    // Creates a creative tab with the id "productive_foxes:fox_tab" for the example item, that is placed after the combat tab
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> EXAMPLE_TAB = CREATIVE_MODE_TABS.register("fox_tab", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.productive_foxes")) //The language key for the title of your CreativeModeTab
            .withTabsBefore(CreativeModeTabs.COMBAT)
            .icon(() -> FOX_ITEM.get().getDefaultInstance()) // sets the icon to our fox_item
            .displayItems((parameters, output) -> {
                output.accept(FOX_ITEM.get()); // Add the example item to the tab. For your own tabs, this method is preferred over the event
                output.accept(FOX_BLOCK_ITEM.get());
                // output.accept(DELPHOX_BERRY_BUSH_ITEM.get());
                output.accept(DELPHOX_BERRY_ITEM.get());

                // serious items
                output.accept(SOUR_BERRIES_ITEM.get());
                output.accept(EXTRA_SOUR_BERRIES_ITEM.get());
                output.accept(SUGARY_SWEET_BERRY_MIX.get());
                output.accept(SWEET_BERRY_JAM.get());
                output.accept(EMPTY_JAM_JAR.get());
            }).build());

    // The constructor for the mod class is the first code that is run when your mod is loaded.
    // FML will recognize some parameter types like IEventBus or ModContainer and pass them in automatically.
    public ProductiveFoxes(IEventBus modEventBus, ModContainer modContainer) {
        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        // Register the Deferred Registers
        BLOCKS.register(modEventBus);
        ITEMS.register(modEventBus);
        CREATIVE_MODE_TABS.register(modEventBus);

        // Register ourselves for server and other game events we are interested in.
        // Note that this is necessary if and only if we want *this* class (ExampleMod) to respond directly to events.
        // Do not add this line if there are no @SubscribeEvent-annotated functions in this class, like onServerStarting() below.
        NeoForge.EVENT_BUS.register(this);

        // Register an item to an existing creative tab
        modEventBus.addListener(this::addCreative);

        // Register our mod's ModConfigSpec so that FML can create and load the config file for us
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        // Some common setup code
        LOGGER.info("HELLO FROM COMMON SETUP");

        if (Config.logDirtBlock)
            LOGGER.info("DIRT BLOCK >> {}", BuiltInRegistries.BLOCK.getKey(Blocks.DIRT));

        LOGGER.info("{}{}", Config.magicNumberIntroduction, Config.magicNumber);

        Config.items.forEach((item) -> LOGGER.info("ITEM >> {}", item.toString()));
    }

    // Add the example block item to the building blocks tab
    private void addCreative(BuildCreativeModeTabContentsEvent event)  {
        if (event.getTabKey() == CreativeModeTabs.BUILDING_BLOCKS)
            event.accept(FOX_BLOCK_ITEM);
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        // Do something when the server starts
        LOGGER.info("HELLO from server starting");
    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @EventBusSubscriber(modid = MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            // Some client setup code
            LOGGER.info("HELLO FROM CLIENT SETUP");
            LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
        }
    }

    @SubscribeEvent
    public void registerBrewingRecipes(RegisterBrewingRecipesEvent event) {
        // get builder to add recipes to
        PotionBrewing.Builder builder = event.getBuilder();

        // add the potion...?
        builder.addRecipe(Ingredient.of(ProductiveFoxes.EMPTY_JAM_JAR), Ingredient.of(ProductiveFoxes.SUGARY_SWEET_BERRY_MIX),
                new ItemStack(ProductiveFoxes.SWEET_BERRY_JAM.get()));
    }

}
