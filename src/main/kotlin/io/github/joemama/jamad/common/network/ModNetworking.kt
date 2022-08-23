package io.github.joemama.jamad.common.network

import io.github.joemama.jamad.Jamad
import io.github.joemama.jamad.common.blockentity.DrawerBlockEntity
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs
import net.fabricmc.fabric.api.networking.v1.PlayerLookup
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant
import net.minecraft.util.Identifier

@Suppress("UnstableApiUsage")
object ModNetworking {
    val VariantPacket: Identifier = Jamad.prefix("sync")

    fun sendVariantUpdate(variant: ItemVariant, drawer: DrawerBlockEntity) {
        val buf = PacketByteBufs.create()
        buf.writeBlockPos(drawer.pos)
        variant.toPacket(buf)
        for (player in PlayerLookup.tracking(drawer)) {
            ServerPlayNetworking.send(player, VariantPacket, buf)
        }
    }
}