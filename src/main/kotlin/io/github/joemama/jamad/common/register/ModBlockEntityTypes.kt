package io.github.joemama.jamad.common.register

import io.github.joemama.jamad.common.blockentity.DrawerBlockEntity
import io.github.joemama.jamad.common.util.ObjectRegistrar
import net.fabricmc.fabric.api.`object`.builder.v1.block.entity.FabricBlockEntityTypeBuilder
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.util.registry.Registry

object ModBlockEntityTypes : ObjectRegistrar<BlockEntityType<*>>(Registry.BLOCK_ENTITY_TYPE) {
    val DRAWER: BlockEntityType<DrawerBlockEntity> by this.register("drawer") {
        FabricBlockEntityTypeBuilder.create(::DrawerBlockEntity)
            .addBlock(ModBlocks.OAK_DRAWER)
            .addBlock(ModBlocks.SPRUCE_DRAWER)
            .build(null)
    }
}