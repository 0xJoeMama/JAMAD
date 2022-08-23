package io.github.joemama.jamad.common.util

import net.minecraft.entity.ItemEntity
import net.minecraft.item.ItemStack
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

fun spawnItemEntity(world: World, pos: BlockPos, stack: ItemStack) {
    world.spawnEntity(ItemEntity(world, pos.x + 0.5, pos.y.toDouble(), pos.z.toDouble() + 0.5, stack))
}