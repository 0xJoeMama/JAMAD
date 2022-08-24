package io.github.joemama.jamad.data.provider.client

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

    override fun run(cache: DataCache) {
        this.registerMappings()
        this.mappings.asMap().forEach { (locale, mappings) ->
            val json = JsonObject()

            mappings.forEach { (key, mapping) ->
                json.addProperty(key, mapping)
            }

            DataProvider.writeToPath(GSON, cache, json, gen.output / "assets" / gen.modId / "lang" / "$locale.json")
        }
    }

    private fun registerMappings() {
        this.createLocale("en_us") {
            ModBlocks.map { it.second to spaceSeparatedCamelCase(it.first) }.forEach { (block, name) ->
                map(block, name)
            }

            ModItems.map { it.second to spaceSeparatedCamelCase(it.first) }.forEach { (item, name) ->
                map(item, name)
            }
        }

        // TODO: Actually add translations!
        this.createLocale("el_gr") {
            map(ModItems.DRAWER_TINKERER, "Επεξεργαστής Ντουλαπιών")
            map(ModBlocks.OAK_DRAWER, "Βελανιδένιο Ντουλάπι")
            map(ModBlocks.SPRUCE_DRAWER, "Ντουλάπι Πικέας")
        }
    }

    private fun createLocale(locale: String, init: Mapper.() -> Unit) {
        val adder = Mapper { key, value -> this.mappings.put(locale, Pair(key, value)) }
        adder.init()
    }

    override fun getName(): String = "Language"

    companion object {
        val GSON: Gson = GsonBuilder().setPrettyPrinting().create()
    }

    private fun spaceSeparatedCamelCase(s: String): String =
        s.split("_").joinToString(" ") { word ->
            buildString {
                append(word[0].uppercase())
                append(word.substring(1))
            }
        }
}

class Mapper(val consumer: (String, String) -> Unit) {
    fun map(key: String, mapping: String) {
        consumer(key, mapping)
    }

    fun map(key: TranslatableText, mapping: String) = this.map(key.key, mapping)

    fun map(item: Item, mapping: String) {
        val itemId = Registry.ITEM.getId(item)
        this.map("item.${itemId.namespace}.${itemId.path}", mapping)
    }

    fun map(block: Block, mapping: String, item: Boolean = true) {
        val blockId = Registry.BLOCK.getId(block)
        this.map("block.${blockId.namespace}.${blockId.path}", mapping)

        if (item) {
            this.map(block.asItem(), mapping)
        }
    }
}