package io.github.joemama.jamad.common.transfer

import io.github.joemama.jamad.common.blockentity.DrawerBlockEntity
import io.github.joemama.jamad.common.network.ModNetworking
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleVariantStorage
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext
import net.minecraft.nbt.NbtCompound

@Suppress("UnstableApiUsage")
class DrawerStorage(private val drawer: DrawerBlockEntity) : SingleVariantStorage<ItemVariant>() {
    var syncedVariant: ItemVariant = blankVariant

    override fun getCapacity(variant: ItemVariant?): Long = 1024

    override fun getBlankVariant(): ItemVariant = ItemVariant.blank()

    override fun onFinalCommit() {
        super.onFinalCommit()
        drawer.markDirty()
        if (this.variant != syncedVariant) {
            ModNetworking.sendVariantUpdate(this.variant, this.drawer)
            this.syncedVariant = this.variant
        }
    }

    fun writeNbt(nbt: NbtCompound) {
        val compound = NbtCompound()
        compound.putLong("Amount", this.amount)
        compound.put("Variant", this.variant.toNbt())
        nbt.put("ItemStorage", compound)
    }

    fun readNbt(nbt: NbtCompound) {
        val storageNbt = nbt.getCompound("ItemStorage")
        this.amount = storageNbt.getLong("Amount")
        this.variant = ItemVariant.fromNbt(storageNbt.getCompound("Variant"))
    }
}