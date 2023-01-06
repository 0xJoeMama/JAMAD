package io.github.joemama.jamad.common.lib

import io.github.joemama.jamad.common.blockentity.DrawerBlockEntity
import io.github.joemama.jamad.common.util.NbtSerializable
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtCompound

interface Upgrade

data class CapacityModifier(val modifier: (Long) -> Long)

interface CapacityUpgrade {
    val capacityModifier: CapacityModifier
}

object NoOpUpgrade : Upgrade

class UpgradeManager(val drawer: DrawerBlockEntity): NbtSerializable {
    private val upgrades = Array<Upgrade>(5) { NoOpUpgrade}
    private val stacks: Array<ItemStack> = Array(5) { ItemStack.EMPTY }
    private var capacityCache: Long = -1L

    fun getCapacity(): Long {
        if (this.capacityCache == -1L) {
            this.capacityCache = this.upgrades
                .filter { it is CapacityUpgrade }
                .map { it as CapacityUpgrade }
                .fold(1024) { a, b -> b.capacityModifier.modifier(a)}
        }

        return this.capacityCache
    }

    private fun invalidateCache() {
        this.capacityCache = -1L
    }

    fun insertUpgrade(upgrade: Upgrade, stack: ItemStack): Boolean {
        for (i in 0 until this.upgrades.size) {
            if (this.upgrades[i] == NoOpUpgrade) {
                this.upgrades[i] = upgrade
                this.stacks[i] = stack
                this.invalidateCache()
                return true
            }
        }

        return false
    }

    fun removeUpgrade(upgrade: Upgrade): ItemStack {
        for (i in 0 until this.upgrades.size) {
            if (this.upgrades[i] == upgrade) {
                this.upgrades[i] = NoOpUpgrade
                this.invalidateCache()
                return this.stacks[i]
            }
        }

        return ItemStack.EMPTY
    }

    override fun writeNbt(nbt: NbtCompound) { }

    override fun readNbt(nbt: NbtCompound) { }
}