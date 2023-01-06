package io.github.joemama.jamad.common.util

import net.minecraft.nbt.NbtCompound

interface NbtSerializable {
    fun writeNbt(nbt: NbtCompound)
    fun readNbt(nbt: NbtCompound)
}