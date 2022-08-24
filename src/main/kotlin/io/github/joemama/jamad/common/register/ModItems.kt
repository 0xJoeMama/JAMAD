package io.github.joemama.jamad.common.register

import io.github.joemama.jamad.Jamad
import io.github.joemama.jamad.common.item.DrawerTinkererItem
import io.github.joemama.jamad.common.util.ObjectRegistrar
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder
import net.minecraft.item.Item
import net.minecraft.item.ItemGroup
import net.minecraft.item.ItemStack
import net.minecraft.util.registry.Registry

object ModItems : ObjectRegistrar<Item>(Registry.ITEM) {
    private val MOD_GROUP: ItemGroup =
        FabricItemGroupBuilder.build(Jamad.prefix("group")) { ItemStack(DRAWER_TINKERER) }

    val DRAWER_TINKERER by this.register("drawer_tinkerer") {
        DrawerTinkererItem(this.createDefaultSettings().maxCount(1))
    }

    fun createDefaultSettings(): Item.Settings = Item.Settings().group(MOD_GROUP)
}