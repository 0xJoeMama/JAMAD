package io.github.joemama.jamad.common.transfer

import io.github.joemama.jamad.common.blockentity.DrawerBlockEntity
import io.github.joemama.jamad.common.network.ModNetworking
import io.github.joemama.jamad.common.util.NbtSerializable
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleVariantStorage
import net.minecraft.nbt.NbtCompound

@Suppress("UnstableApiUsage")
class DrawerStorage(private val drawer: DrawerBlockEntity) : SingleVariantStorage<ItemVariant>(), NbtSerializable {
    var syncedVariant: ItemVariant = blankVariant

    override fun getCapacity(variant: ItemVariant): Long = this.drawer.upgradeManager.getCapacity()

    override fun getBlankVariant(): ItemVariant = ItemVariant.blank()

    override fun onFinalCommit() {
        super.onFinalCommit()
        drawer.markDirty()
        if (this.variant != syncedVariant) {
            ModNetworking.sendVariantUpdate(this.variant, this.drawer)
            this.syncedVariant = this.variant
        }
    }

    override fun writeNbt(nbt: NbtCompound) {
        val compound = NbtCompound()
        compound.putLong("Amount", this.amount)
        compound.put("Variant", this.variant.toNbt())
        nbt.put("ItemStorage", compound)
    }

    override fun readNbt(nbt: NbtCompound) {
        val storageNbt = nbt.getCompound("ItemStorage")
        this.amount = storageNbt.getLong("Amount")
        this.variant = ItemVariant.fromNbt(storageNbt.getCompound("Variant"))
    }
}