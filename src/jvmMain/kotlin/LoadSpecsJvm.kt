package io.github.rkbalgi.iso4k

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import io.github.rkbalgi.iso4k.Spec.Companion.specMap
import java.io.File
import java.nio.file.Path
import kotlin.collections.set
import kotlin.io.path.readText
import net.mamoe.yamlkt.Yaml

/**
 * Load spec definitions from YAML fields either from a location on the filesystem specified by
 * system propery *io.github.rkbalgi.iso4k.specsLocation* or from classpath (/specs.yml)
 *
 * @return A list of specs available/read
 */
actual fun loadSpecs(): List<String> {

  Napier.base(DebugAntilog())

  val objectMapper = ObjectMapper(YAMLFactory())
  objectMapper.registerKotlinModule()

  var allSpecs: List<String>

  val specLocation = System.getProperty(specLocationProperty)
  if (!specLocation.isNullOrEmpty()) {
    Napier.i("Loading spec definitions from $specLocation")

    val specDir = File(specLocation)

    if (specDir.exists() && specDir.isDirectory) {

      val fileContent = Path.of(specLocation).resolve("specs.yml").readText(Charsets.UTF_8)
      allSpecs = objectMapper.readValue<List<String>>(fileContent)

      allSpecs.forEach {
        val specDef = Path.of(specLocation).resolve(it).readText(Charsets.UTF_8)
        val spec = objectMapper.readValue<Spec>(specDef)
        specMap[spec.name] = spec
      }
    } else {
      throw java.lang.RuntimeException("$specLocationProperty doesn't point to a valid directory")
    }
  } else {

    // try to read from classpath

    val resource = Spec::javaClass.javaClass.getResource("/specs.yml")
    if (resource == null) {
      Napier.w { "specs.yml not found in classpath, No specs loaded" }
      return emptyList()
    }

    Napier.i("Loading spec definitions from classpath")
    allSpecs = Yaml.decodeListFromString(resource.readText(Charsets.UTF_8)) as List<String>

    allSpecs.forEach {
      Napier.i("Reading spec $it from classpath")
      val fileContent = Spec::javaClass.javaClass.getResource(it).readText()
      val spec = Yaml.decodeFromString(Spec.serializer(), fileContent)
      specMap[spec.name] = spec
    }
  }

  return emptyList()
}
