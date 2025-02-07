package ca.farms4life2016.productive_foxes.berry_resourceful;

import ca.farms4life2016.productive_foxes.ProductiveFoxes;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;

public class SweetBerryJamItem extends Item {
    private static final int DRINK_DURATION = 40;

    /*
        Most of this code is copied from HoneyBottleItem.java
     */

    public SweetBerryJamItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack pStack, Level pLevel, LivingEntity pEntityLiving) {
        super.finishUsingItem(pStack, pLevel, pEntityLiving);
        if (pEntityLiving instanceof ServerPlayer serverplayer) {
            CriteriaTriggers.CONSUME_ITEM.trigger(serverplayer, pStack);
            serverplayer.awardStat(Stats.ITEM_USED.get(this));
        }

        if (pStack.isEmpty()) {
            return new ItemStack(ProductiveFoxes.EMPTY_JAM_JAR.get());
        } else {
            if (pEntityLiving instanceof Player player && !player.hasInfiniteMaterials()) {
                ItemStack itemstack = new ItemStack(ProductiveFoxes.EMPTY_JAM_JAR.get());
                if (!player.getInventory().add(itemstack)) {
                    player.drop(itemstack, false);
                }
            }

            return pStack;
        }
    }

    @Override
    public int getUseDuration(ItemStack pStack, LivingEntity pEntity) {
        return DRINK_DURATION;
    }

    /**
     * Returns the action that specifies what animation to play when the item is being used.
     */
    @Override
    public UseAnim getUseAnimation(ItemStack pStack) {
        return UseAnim.DRINK;
    }

    @Override // might want to update these drinking and eating sounds
    public SoundEvent getDrinkingSound() {
        return SoundEvents.HONEY_DRINK;
    }

    @Override
    public SoundEvent getEatingSound() {
        return SoundEvents.HONEY_DRINK;
    }

    /**
     * Called to trigger the item's "innate" right click behavior. To handle when this item is used on a Block, see {@link #onItemUse}.
     */
    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pHand) {
        return ItemUtils.startUsingInstantly(pLevel, pPlayer, pHand);
    }
}
