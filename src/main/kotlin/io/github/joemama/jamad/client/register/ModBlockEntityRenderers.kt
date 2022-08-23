package io.github.joemama.jamad.client.register

import io.github.joemama.jamad.client.renderer.blockentity.DrawerBlockEntityRenderer
import io.github.joemama.jamad.common.blockentity.DrawerBlockEntity
import io.github.joemama.jamad.common.register.ModBlockEntityTypes
import io.github.joemama.jamad.common.util.Registrar
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry
import net.minecraft.block.entity.BlockEntityType
import org.slf4j.Logger

object ModBlockEntityRenderers : Registrar {
    override fun register(modid: String, logger: Logger) {
        logger.info("Registering block entity renderers for '$modid'")

        @Suppress("UNCHECKED_CAST") // we need to
        BlockEntityRendererRegistry.register(ModBlockEntityTypes.Drawer as BlockEntityType<DrawerBlockEntity>) {
            DrawerBlockEntityRenderer()
        }
    }
}