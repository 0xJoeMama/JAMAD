package io.github.joemama.jamad.common.util

import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry
import org.slf4j.Logger

open class ObjectRegistrar<T>(private val registry: Registry<T>) : Registrar, Iterable<Pair<String, T>> {
    private val entries = mutableListOf<Pair<String, Lazy<T>>>()

    fun <U> register(path: String, obj: () -> U): Lazy<U> where U : T {
        val created = lazy(obj)
        entries.add(path to created)
        return created
    }

    fun <U> registerAndThen(path: String, obj: () -> U, then: (U) -> Unit): Lazy<U> where U : T = this.register(path) {
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

    override fun iterator(): Iterator<Pair<String, T>> =
        this.entries.map { (key, value) -> Pair(key, value.value) }.iterator()
}