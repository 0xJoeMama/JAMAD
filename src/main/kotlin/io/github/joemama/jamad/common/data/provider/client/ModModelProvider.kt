package io.github.joemama.jamad.common.data.provider.client

import io.github.joemama.jamad.common.register.ModBlocks
import io.github.joemama.jamad.common.register.ModItems
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider
import net.minecraft.data.client.BlockStateModelGenerator
import net.minecraft.data.client.ItemModelGenerator
import net.minecraft.data.client.Models
import net.minecraft.data.client.TextureMap

class ModModelProvider(gen: FabricDataGenerator) : FabricModelProvider(gen) {
    override fun generateBlockStateModels(blockStateModelGenerator: BlockStateModelGenerator) {
        blockStateModelGenerator.registerNorthDefaultHorizontalRotatable(
            ModBlocks.OakDrawer,
            TextureMap.texture(ModBlocks.OakDrawer)
        )
    }

    override fun generateItemModels(itemModelGenerator: ItemModelGenerator) {
        itemModelGenerator.register(ModItems.DrawerTinkerer, Models.GENERATED)
    }
}