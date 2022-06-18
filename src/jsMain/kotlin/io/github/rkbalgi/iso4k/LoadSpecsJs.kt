package io.github.rkbalgi.iso4k

import allSpecs
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import net.mamoe.yamlkt.Yaml
import kotlin.collections.set


/**
 * Loads spec definitions. The JS implementation uses inline YAML spec definitions or in future call
 * an external HTTP API
 *
 * @return List of available specs
 */
actual fun loadSpecs(): List<String>? {

    Napier.base(DebugAntilog())
    Napier.i("Loading Spec definitions .. ")

    var specs = mutableListOf<String>()

    var fromJS = allSpecs()
    for (specDef in fromJS) {
        var decoded = Yaml.decodeFromString(
            Spec.serializer(), specDef
        )
        Napier.i { "Loaded spec - ${decoded.name}" }
        Spec.specMap[decoded.name] = decoded
        specs.add(decoded.name)


    }
    return specs
}

