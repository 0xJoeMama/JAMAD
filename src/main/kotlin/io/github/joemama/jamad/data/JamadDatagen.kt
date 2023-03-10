package io.github.joemama.jamad.data

import io.github.joemama.jamad.data.provider.client.ModLanguageProvider
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator

object JamadDatagen : DataGeneratorEntrypoint {
    override fun onInitializeDataGenerator(gen: FabricDataGenerator) {
        gen.addProvider(::ModLanguageProvider)
        // gen.addProvider(::ModModelProvider)
    }
}