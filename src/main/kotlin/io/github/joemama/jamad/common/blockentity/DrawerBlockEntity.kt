package io.github.joemama.jamad.common.blockentity

import io.github.joemama.jamad.common.register.ModBlockEntityTypes
import io.github.joemama.jamad.common.transfer.DrawerStorage
import io.github.joemama.jamad.common.util.spawnItemEntity
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.nbt.NbtCompound
import net.minecraft.util.math.BlockPos

@Suppress("UnstableApiUsage")
class DrawerBlockEntity(pos: BlockPos, state: BlockState) : BlockEntity(ModBlockEntityTypes.Drawer, pos, state) {
    val storage = DrawerStorage(this)

    @Environment(EnvType.CLIENT)
    @JvmField
    var cachedVariant: ItemVariant = ItemVariant.blank()

    override fun writeNbt(nbt: NbtCompound) {
        super.writeNbt(nbt)
        this.storage.writeNbt(nbt)
    }

    override fun readNbt(nbt: NbtCompound) {
        super.readNbt(nbt)
        this.storage.readNbt(nbt)
    }

    fun dropContents() {
        this.world?.let { world ->
            val maxCount = storage.variant.toStack().maxCount
            val stackCount = storage.amount / maxCount
            val extraCount = storage.amount % maxCount

            val pos = this.pos
            for (i in 0 until stackCount) {
                spawnItemEntity(world, pos, storage.variant.toStack(maxCount))
            }

            spawnItemEntity(world, pos, storage.variant.toStack(extraCount.toInt()))
        }
    }
}