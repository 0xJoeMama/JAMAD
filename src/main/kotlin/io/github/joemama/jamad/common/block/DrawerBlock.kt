package io.github.joemama.jamad.common.block

import io.github.joemama.jamad.common.blockentity.DrawerBlockEntity
import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant
import net.fabricmc.fabric.api.transfer.v1.storage.Storage
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction
import net.minecraft.block.Block
import net.minecraft.block.BlockEntityProvider
import net.minecraft.block.BlockState
import net.minecraft.block.HorizontalFacingBlock
import net.minecraft.block.entity.BlockEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemPlacementContext
import net.minecraft.item.ItemStack
import net.minecraft.state.StateManager
import net.minecraft.state.property.DirectionProperty
import net.minecraft.state.property.Properties
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.World

class DrawerBlock(settings: Settings) : HorizontalFacingBlock(settings), BlockEntityProvider {
    init {
        this.defaultState = this.stateManager.defaultState.with(FACING, Direction.NORTH)
    }

    companion object {
        @JvmStatic
        val HorizontalFacing: DirectionProperty = Properties.HORIZONTAL_FACING
    }

    override fun getPlacementState(ctx: ItemPlacementContext): BlockState =
        this.defaultState.with(HorizontalFacing, ctx.playerFacing.opposite)

    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
        super.appendProperties(builder)
        builder.add(HorizontalFacing)
    }

    override fun createBlockEntity(pos: BlockPos, state: BlockState): BlockEntity =
        DrawerBlockEntity(pos, state)

    @Deprecated("Deprecated in Java")
    @Suppress("UnstableApiUsage")
    override fun onUse(
        state: BlockState,
        world: World,
        pos: BlockPos?,
        player: PlayerEntity,
        hand: Hand,
        hit: BlockHitResult
    ): ActionResult {
        if (!world.isClient) {
            if (hit.side == state[HorizontalFacing]) {
                ItemStorage.SIDED.find(world, pos, hit.side)?.let {
                    val stackInHand = player.getStackInHand(Hand.MAIN_HAND)
                    if (!stackInHand.isEmpty) {
                        val transaction = Transaction.openOuter()

                        try {
                            val inserted =
                                it.insert(ItemVariant.of(stackInHand), stackInHand.count.toLong(), transaction)
                            stackInHand.decrement(inserted.toInt())
                        } finally {
                            transaction.commit()
                        }
                    }
                }

                return ActionResult.CONSUME
            }
        }

        return ActionResult.SUCCESS
    }

    @Deprecated("Deprecated in Java")
    override fun onStateReplaced(
        state: BlockState,
        world: World,
        pos: BlockPos,
        newState: BlockState,
        moved: Boolean
    ) {
        if (!world.isClient) {
            world.getBlockEntity(pos)?.let {
                (it as DrawerBlockEntity).dropContents()
            }
        }

        @Suppress("DEPRECATION")
        super.onStateReplaced(state, world, pos, newState, moved)
    }

    @Suppress("UnstableApiUsage")
    fun playerClicked(
        state: BlockState,
        world: World,
        player: PlayerEntity,
        pos: BlockPos,
        direction: Direction
    ) {
        if (direction != state.get(HorizontalFacing)) {
            return
        }

        ItemStorage.SIDED.find(world, pos, direction)?.let {
            val transaction = Transaction.openOuter()
            try {
                val handStack = player.mainHandStack
                val handVariant = ItemVariant.of(handStack)

                for (variant in it.iterable(transaction)) {
                    val stored = variant.resource
                    if (!stored.isBlank && (handStack.isEmpty || stored == handVariant)) {
                        extractItem(player, it, handStack, stored, transaction)
                        player.swingHand(Hand.MAIN_HAND)
                    }
                }
            } finally {
                transaction.commit()
            }
        }
    }

    @Suppress("UnstableApiUsage")
    private fun extractItem(
        player: PlayerEntity,
        storage: Storage<ItemVariant>,
        handStack: ItemStack,
        variant: ItemVariant,
        transaction: Transaction
    ) {
        val amount = if (player.isSneaking) handStack.maxCount else 1
        val extracted = storage.extract(variant, amount.toLong(), transaction)

        if (handStack.isEmpty) {
            variant.toStack(extracted.toInt()).also {
                player.setStackInHand(Hand.MAIN_HAND, it)
            }
        } else {
            val newCount = handStack.count + extracted
            val extractedStack = variant.toStack(newCount.toInt())

            if (extractedStack.count > extractedStack.maxCount) {
                val extraStack = extractedStack.copy()
                extraStack.count = extractedStack.count - extractedStack.maxCount

                extractedStack.count = extractedStack.maxCount

                // TODO: Look into using increment instead of this?!
                player.setStackInHand(Hand.MAIN_HAND, extractedStack)

                if (!player.giveItemStack(extraStack)) {
                    player.dropItem(extraStack, false)
                }
            } else {
                handStack.count = newCount.toInt()
            }
        }
    }
}