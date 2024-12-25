package ca.farms4life2016.productive_foxes.berry_resourceful;

import ca.farms4life2016.productive_foxes.ProductiveFoxes;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SweetBerryBushBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;

public class DelphoxBerryBush extends SweetBerryBushBlock {

    public DelphoxBerryBush() {
        // creates this block as a copy of the vanilla sweet berry bush?
        super(BlockBehaviour.Properties.ofFullCopy(Blocks.SWEET_BERRY_BUSH));
    }

    @Override
    protected boolean mayPlaceOn(BlockState state, BlockGetter world, BlockPos pos) {
        // Define the blocks this bush can grow on (e.g., dirt, grass)
        return state.is(ProductiveFoxes.FOX_BLOCK);
    }

    @Override
    public @NotNull ItemStack getCloneItemStack(LevelReader world, BlockPos pos, BlockState state) {
        // Middle-click block copy
        // Replace this with your custom berry item
        return new ItemStack(ProductiveFoxes.FOX_ITEM.get());
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, BlockHitResult pHitResult) {
        // get current age
        int currentAge = pState.getValue(AGE);
        boolean isFullyMature = currentAge == 3;

        // age 0 = baby (no berries)
        // age 1 = adult but no berries
        // age 2 = some berries, 1-2 berries
        // age 3 = lots of berries, 2-3 berries
        if (currentAge > 1) {
            // amount of berries per harvest. change the ItemStack item to your custom mod item...
            int amount = 1 + pLevel.random.nextInt(2);          // will get guaranteed +1 if fully mature
            popResource(pLevel, pPos, new ItemStack(ProductiveFoxes.DELPHOX_BERRY_ITEM.get(), amount + (isFullyMature ? 1 : 0)));

            // plays berry harvesting sound
            pLevel.playSound(
                    null, pPos, SoundEvents.SWEET_BERRY_BUSH_PICK_BERRIES, SoundSource.BLOCKS, 1.0F, 0.8F + pLevel.random.nextFloat() * 0.4F
            );

            // sets age back to 1 (adult no-berry stage)
            BlockState blockstate = pState.setValue(AGE, 1);
            pLevel.setBlock(pPos, blockstate, 2);

            // not sure what this does, probably updates some BUD switch lol
            pLevel.gameEvent(GameEvent.BLOCK_CHANGE, pPos, GameEvent.Context.of(pPlayer, blockstate));
            return InteractionResult.sidedSuccess(pLevel.isClientSide);
        } else {
            // for other ages, just right-click as usual (e.g. places down a block)
            return super.useWithoutItem(pState, pLevel, pPos, pPlayer, pHitResult);
        }
    }

}
