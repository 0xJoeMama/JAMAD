package io.github.joemama.jamad.client

import io.github.joemama.jamad.Jamad
import io.github.joemama.jamad.client.register.ModBlockEntityRenderers
import io.github.joemama.jamad.common.blockentity.DrawerBlockEntity
import io.github.joemama.jamad.common.network.ModNetworking
import io.github.joemama.jamad.common.util.RegistrarPipeline
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant

@Environment(EnvType.CLIENT)
object JamadClient : ClientModInitializer {
    override fun onInitializeClient() {
        ClientPlayNetworking.registerGlobalReceiver(ModNetworking.VariantPacket) { client, _, buf, _ ->
            val pos = buf.readBlockPos()
            @Suppress("UnstableApiUsage")
            val variant = ItemVariant.fromPacket(buf)

            client.execute {
                client.world?.getBlockEntity(pos)?.let {
                    if (it is DrawerBlockEntity) {
                        it.cachedVariant = variant
                    }
                }
            }
        }

        RegistrarPipeline(Jamad.MODID) {
            add(ModBlockEntityRenderers)
        }

        Jamad.Logger.info("JamadClient has been initialized")
    }
}