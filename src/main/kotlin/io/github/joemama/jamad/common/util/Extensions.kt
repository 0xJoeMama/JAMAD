package io.github.joemama.jamad.common.util

import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction
import net.minecraft.client.util.math.MatrixStack

fun MatrixStack.runInContext(block: () -> Unit) {
    this.push()
    block()
    this.pop()
}

fun commitTransaction(block: Transaction.() -> Unit) {
    val transaction = Transaction.openOuter()
    try {
        transaction.block()
    } finally {
        transaction.commit()
    }
}

fun Transaction.inner(block: Transaction.() -> Unit) {
    val transaction = Transaction.openNested(this)

    try {
        transaction.block()
    } finally {
        transaction.commit()
    }
}