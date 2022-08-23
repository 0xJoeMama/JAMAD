package io.github.joemama.jamad.common.util

import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry
import org.slf4j.Logger

open class ObjectRegistrar<T>(private val registry: Registry<T>) : Registrar {
    private val entries = mutableListOf<Pair<String, Lazy<T>>>()

    fun register(path: String, obj: () -> T): Lazy<T> {
        val created = lazy(obj)
        entries.add(path to created)
        return created
    }

    fun registerAndThen(path: String, obj: () -> T, then: (T) -> Unit): Lazy<T> = this.register(path) {
        val created = obj()
        then(created)
        created
    }

    override fun register(modid: String, logger: Logger) {
        logger.info("Registering ${entries.size} ${if (entries.size == 1) "object" else "objects"} to the '${registry.key.value}' registry")

        entries.forEach { (path, obj) ->
            Registry.register(this.registry, Identifier(modid, path), obj.value)
        }
    }
}