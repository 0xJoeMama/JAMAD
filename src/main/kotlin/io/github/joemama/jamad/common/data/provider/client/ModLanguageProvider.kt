package io.github.joemama.jamad.common.data.provider.client

import com.google.common.collect.Multimap
import com.google.common.collect.MultimapBuilder
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import io.github.joemama.jamad.common.register.ModBlocks
import io.github.joemama.jamad.common.register.ModItems
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator
import net.minecraft.block.Block
import net.minecraft.data.DataCache
import net.minecraft.data.DataProvider
import net.minecraft.item.Item
import net.minecraft.text.TranslatableText
import net.minecraft.util.registry.Registry
import kotlin.io.path.div

class ModLanguageProvider(private val gen: FabricDataGenerator) : DataProvider {
    private val mappings: Multimap<String, Pair<String, String>> = MultimapBuilder
        .hashKeys()
        .arrayListValues()
        .build()

    companion object {
        @JvmStatic
        val Gson: Gson = GsonBuilder().setPrettyPrinting().create()
    }

    override fun run(cache: DataCache) {
        this.registerMappings()
        this.mappings.asMap().forEach { (locale, mappings) ->
            val json = JsonObject()

            mappings.forEach { (key, mapping) ->
                json.addProperty(key, mapping)
            }

            DataProvider.writeToPath(Gson, cache, json, gen.output / "assets" / gen.modId / "lang" / "$locale.json")
        }
    }

    private fun registerMappings() {
        this.createLocale("en_us") {
            map(ModItems.DrawerTinkerer, "Drawer Tinkerer")
            map(ModBlocks.OakDrawer, "Oak Drawer")
            map(ModBlocks.SpruceDrawer, "Spruce Drawer")
        }

        this.createLocale("el_gr") {
            map(ModItems.DrawerTinkerer, "Επεξεργαστής Ντουλαπιών")
            map(ModBlocks.OakDrawer, "Βελανιδένιο Ντουλάπι")
            map(ModBlocks.SpruceDrawer, "Ντουλάπι Πικέας")
        }
    }

    private fun createLocale(locale: String, init: Mapper.() -> Unit) {
        val adder = Mapper { key, value -> this.mappings.put(locale, Pair(key, value)) }
        adder.init()
    }

    override fun getName(): String = "Language"
}

class Mapper(val consumer: (String, String) -> Unit) {
    fun map(key: String, mapping: String) {
        consumer(key, mapping)
    }

    fun map(key: TranslatableText, value: String) = this.map(key.key, value)

    fun map(item: Item, value: String) {
        val itemId = Registry.ITEM.getId(item)
        this.map("item.${itemId.namespace}.${itemId.path}", value)
    }

    fun map(block: Block, value: String, item: Boolean = true) {
        val blockId = Registry.BLOCK.getId(block)
        this.map("block.${blockId.namespace}.${blockId.path}", value)

        if (item) {
            this.map(block.asItem(), value)
        }
    }
}