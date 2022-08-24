package io.github.joemama.jamad.common.register

import io.github.joemama.jamad.common.blockentity.DrawerBlockEntity
import io.github.joemama.jamad.common.util.Registrar
import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage
import org.slf4j.Logger

object ModApiLookup : Registrar {
    @Suppress("UnstableApiUsage")
    override fun register(modid: String, logger: Logger) {
        ItemStorage.SIDED.registerForBlockEntity(
            { blockEntity, _ -> (blockEntity as DrawerBlockEntity).storage },
            ModBlockEntityTypes.DRAWER
        )

        logger.info("Successfully registered mod APIs!")
    }
}