package io.github.joemama.jamad.common.register

import io.github.joemama.jamad.common.block.DrawerBlock
import io.github.joemama.jamad.common.util.ObjectRegistrar
import net.fabricmc.fabric.api.`object`.builder.v1.block.FabricBlockSettings
import net.minecraft.block.Block
import net.minecraft.block.Blocks
import net.minecraft.item.BlockItem
import net.minecraft.util.registry.Registry

object ModBlocks : ObjectRegistrar<Block>(Registry.BLOCK) {
    val OakDrawer by this.registerWithItem("oak_drawer") {
        DrawerBlock(FabricBlockSettings.copy(Blocks.OAK_PLANKS))
    }
    val SpruceDrawer by this.registerWithItem("spruce_drawer") {
        DrawerBlock(FabricBlockSettings.copy(Blocks.SPRUCE_PLANKS))
    }

    private fun registerWithItem(path: String, block: () -> Block): Lazy<Block> = this.registerAndThen(path, block) {
        ModItems.register(path) { BlockItem(it, ModItems.createDefaultSettings()) }
    }
}