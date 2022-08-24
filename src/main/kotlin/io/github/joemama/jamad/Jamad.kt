package io.github.joemama.jamad

import io.github.joemama.jamad.common.event.EventHandler
import io.github.joemama.jamad.common.register.ModApiLookup
import io.github.joemama.jamad.common.register.ModBlockEntityTypes
import io.github.joemama.jamad.common.register.ModBlocks
import io.github.joemama.jamad.common.register.ModItems
import io.github.joemama.jamad.common.util.RegistrarPipeline
import net.fabricmc.api.ModInitializer
import net.minecraft.util.Identifier
import org.slf4j.Logger
import org.slf4j.LoggerFactory

object Jamad : ModInitializer {
    const val MODID: String = "jamad"
    val LOGGER: Logger = LoggerFactory.getLogger(Jamad::class.java)

    override fun onInitialize() {
        RegistrarPipeline(MODID) {
            add(ModBlocks)
            add(ModItems)
            add(ModBlockEntityTypes)
            add(ModApiLookup)
            add(EventHandler)
        }

        LOGGER.info("Jamad has been initialized")
    }

    fun prefix(path: String): Identifier = Identifier(MODID, path)
}