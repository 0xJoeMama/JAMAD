package io.github.joemama.jamad.common.block

import io.github.joemama.jamad.common.blockentity.DrawerBlockEntity
import io.github.joemama.jamad.common.util.commitTransaction
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
        val HORIZONTAL_FACING: DirectionProperty = Properties.HORIZONTAL_FACING
    }

    override fun getPlacementState(ctx: ItemPlacementContext): BlockState =
        this.defaultState.with(HORIZONTAL_FACING, ctx.playerFacing.opposite)

    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
        super.appendProperties(builder)
        builder.add(HORIZONTAL_FACING)
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
        if (!world.isClient && hit.side == state[HORIZONTAL_FACING]) {
            ItemStorage.SIDED.find(world, pos, hit.side)?.let {
                val stackInHand = player.getStackInHand(Hand.MAIN_HAND)
                if (!stackInHand.isEmpty) {
                    commitTransaction {
                        val inserted = it.insert(ItemVariant.of(stackInHand), stackInHand.count.toLong(), this)
                        stackInHand.decrement(inserted.toInt())
                    }
                }
            }

            return ActionResult.CONSUME
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
        if (direction != state[HORIZONTAL_FACING]) {
            return
        }

        ItemStorage.SIDED.find(world, pos, direction)?.let {
            commitTransaction {
                val handStack = player.mainHandStack

                for (variant in it.iterable(this)) {
                    val stored = variant.resource
                    if (!stored.isBlank) {
                        extractItem(player, it, handStack, stored, this)
                        break
                    }
                }
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
        val extractedStack = variant.toStack()
        val amount = if (player.isSneaking) extractedStack.maxCount else 1
        extractedStack.count = storage.extract(variant, amount.toLong(), transaction).toInt()

        if (handStack.isEmpty) {
            extractedStack.also {
                player.setStackInHand(Hand.MAIN_HAND, it)
            }
        } else {
            player.giveItemStack(extractedStack)
        }
    }
}