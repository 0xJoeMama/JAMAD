package io.github.joemama.jamad.common.register

import io.github.joemama.jamad.common.block.DrawerBlock
import io.github.joemama.jamad.common.util.ObjectRegistrar
import net.fabricmc.fabric.api.`object`.builder.v1.block.FabricBlockSettings
import net.minecraft.block.Block
import net.minecraft.block.Blocks
import net.minecraft.item.BlockItem
import net.minecraft.util.registry.Registry

object ModBlocks : ObjectRegistrar<Block>(Registry.BLOCK) {
    val OAK_DRAWER by this.registerWithItem("oak_drawer") {
        DrawerBlock(FabricBlockSettings.copy(Blocks.OAK_PLANKS))
    }
    val SPRUCE_DRAWER by this.registerWithItem("spruce_drawer") {
        DrawerBlock(FabricBlockSettings.copy(Blocks.SPRUCE_PLANKS))
    }
    val BIRCH_DRAWER by this.registerWithItem("birch_drawer") {
        DrawerBlock(FabricBlockSettings.copy(Blocks.BIRCH_PLANKS))
    }
    val JUNGLE_DRAWER by this.registerWithItem("jungle_drawer") {
        DrawerBlock(FabricBlockSettings.copy(Blocks.JUNGLE_PLANKS))
    }
    val ACACIA_DRAWER by this.registerWithItem("acacia_drawer") {
        DrawerBlock(FabricBlockSettings.copy(Blocks.ACACIA_PLANKS))
    }
    val DARK_OAK_DRAWER by this.registerWithItem("dark_oak_drawer") {
        DrawerBlock(FabricBlockSettings.copy(Blocks.DARK_OAK_PLANKS))
    }
    val CRIMSON_DRAWER by this.registerWithItem("crimson_drawer") {
        DrawerBlock(FabricBlockSettings.copy(Blocks.CRIMSON_PLANKS))
    }
    val WARPED_DRAWER by this.registerWithItem("warped_drawer") {
        DrawerBlock(FabricBlockSettings.copy(Blocks.WARPED_PLANKS))
    }

    private fun <U> registerWithItem(path: String, block: () -> U): Lazy<U> where U : Block =
        this.registerAndThen(path, block) {
            ModItems.register(path) { BlockItem(it, ModItems.createDefaultSettings()) }
        }
}