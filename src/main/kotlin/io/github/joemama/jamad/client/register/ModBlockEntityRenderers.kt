package io.github.joemama.jamad.client.register

import io.github.joemama.jamad.client.renderer.blockentity.DrawerBlockEntityRenderer
import io.github.joemama.jamad.common.register.ModBlockEntityTypes
import io.github.joemama.jamad.common.util.Registrar
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry
import org.slf4j.Logger

object ModBlockEntityRenderers : Registrar {
    override fun register(modid: String, logger: Logger) {
        logger.info("Registering block entity renderers for '$modid'")
        BlockEntityRendererRegistry.register(ModBlockEntityTypes.DRAWER) { DrawerBlockEntityRenderer() }
    }
}