package ca.farms4life2016.productive_foxes;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.entity.animal.Fox;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SweetBerryBushBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;

/**
 * Let's the fox pathfind to and pluck custom berry bushes
 */
public class FoxEatCustomBerriesGoal extends MoveToBlockGoal {
    private final Fox fox;
    private static final int WAIT_TICKS = 40;
    protected int ticksWaited;

    public FoxEatCustomBerriesGoal(Fox fox, double pSpeedModifier, int pSearchRange, int pVerticalSearchRange) {
        super(fox, pSpeedModifier, pSearchRange, pVerticalSearchRange);
        this.fox = fox;
    }

    @Override
    public double acceptedDistance() {
        return 2.0;
    }

    @Override
    public boolean shouldRecalculatePath() {
        return this.tryTicks % 100 == 0;
    }

    @Override
    protected boolean isValidTarget(LevelReader pLevel, BlockPos pPos) {
        BlockState blockstate = pLevel.getBlockState(pPos);
        return blockstate.is(ProductiveFoxes.DELPHOX_BERRY_BUSH) && blockstate.getValue(SweetBerryBushBlock.AGE) >= 2;
    }

    @Override
    public void tick() {
        if (this.isReachedTarget()) {
            if (this.ticksWaited >= WAIT_TICKS) {
                this.onReachedTarget();
            } else {
                this.ticksWaited++;
            }
        } else if (!this.isReachedTarget() && fox.getRandom().nextFloat() < 0.05F) {
            fox.playSound(SoundEvents.FOX_SNIFF, 1.0F, 1.0F);
        }

        super.tick();
    }

    protected void onReachedTarget() {
        if (net.neoforged.neoforge.event.EventHooks.canEntityGrief(fox.level(), fox)) {
            BlockState blockstate = fox.level().getBlockState(this.blockPos);
            if (blockstate.is(ProductiveFoxes.DELPHOX_BERRY_BUSH)) {
                this.pickSweetBerries(blockstate);
            }
        }
    }

    private void pickSweetBerries(BlockState pState) {
        int i = pState.getValue(SweetBerryBushBlock.AGE);
        pState.setValue(SweetBerryBushBlock.AGE, 1);
        int j = 1 + fox.level().random.nextInt(2) + (i == 3 ? 1 : 0);
        ItemStack itemstack = fox.getItemBySlot(EquipmentSlot.MAINHAND);
        if (itemstack.isEmpty()) {
            fox.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(ProductiveFoxes.DELPHOX_BERRY_ITEM.get()));
            j--;
        }

        if (j > 0) {
            Block.popResource(fox.level(), this.blockPos, new ItemStack(ProductiveFoxes.DELPHOX_BERRY_ITEM.get(), j));
        }

        fox.playSound(SoundEvents.SWEET_BERRY_BUSH_PICK_BERRIES, 1.0F, 1.0F);
        fox.level().setBlock(this.blockPos, pState.setValue(SweetBerryBushBlock.AGE, 1), 2);
        fox.level().gameEvent(GameEvent.BLOCK_CHANGE, this.blockPos, GameEvent.Context.of(fox));
    }

    @Override
    public boolean canUse() {
        return !fox.isSleeping() && super.canUse();
    }

    @Override
    public void start() {
        this.ticksWaited = 0;
        fox.setSitting(false);
        super.start();
    }
}
