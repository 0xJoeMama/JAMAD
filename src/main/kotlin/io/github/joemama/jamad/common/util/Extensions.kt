package io.github.joemama.jamad.common.util

import net.minecraft.client.util.math.MatrixStack

fun MatrixStack.runInContext(block: () -> Unit) {
    this.push()
    block()
    this.pop()
}