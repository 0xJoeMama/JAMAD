package io.github.joemama.jamad.common.event

import io.github.joemama.jamad.common.block.DrawerBlock
import io.github.joemama.jamad.common.util.EventRegistrar
import net.fabricmc.fabric.api.event.player.AttackBlockCallback
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.World

object EventHandler : EventRegistrar<EventHandler>, AttackBlockCallback {
    override fun interact(
        player: PlayerEntity,
        world: World,
        hand: Hand,
        pos: BlockPos,
        direction: Direction
    ): ActionResult {
        if (world.isClient && player.isSpectator) {
            return ActionResult.PASS
        }

        val state = world.getBlockState(pos)
        val block = state.block

        if (block is DrawerBlock) {
            block.playerClicked(state, world, player, pos, direction)
        }

        return ActionResult.PASS
    }

    override fun register() {
        this.registerEvent(AttackBlockCallback.EVENT, this)
    }
}