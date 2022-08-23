package io.github.joemama.jamad.client.renderer.blockentity

import com.mojang.blaze3d.systems.RenderSystem
import io.github.joemama.jamad.common.blockentity.DrawerBlockEntity
import io.github.joemama.jamad.common.util.runInContext
import net.minecraft.client.MinecraftClient
import net.minecraft.client.render.LightmapTextureManager
import net.minecraft.client.render.OverlayTexture
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.WorldRenderer
import net.minecraft.client.render.block.entity.BlockEntityRenderer
import net.minecraft.client.render.model.json.ModelTransformation
import net.minecraft.client.util.math.MatrixStack

// TODO: Remove this and replace it with a BakedModel
class DrawerBlockEntityRenderer : BlockEntityRenderer<DrawerBlockEntity> {
    override fun render(
        entity: DrawerBlockEntity,
        tickDelta: Float,
        matrices: MatrixStack,
        vertexConsumers: VertexConsumerProvider,
        light: Int,
        overlay: Int
    ) {
        matrices.runInContext {
            matrices.translate(0.0, 2.0, 0.0)
            matrices.scale(1f, 1f, 0f)
            MinecraftClient.getInstance().itemRenderer.renderItem(
                entity.cachedVariant.toStack(),
                ModelTransformation.Mode.GUI,
                LightmapTextureManager.MAX_LIGHT_COORDINATE,
                OverlayTexture.DEFAULT_UV,
                matrices,
                vertexConsumers,
                0
            )
        }
    }
}