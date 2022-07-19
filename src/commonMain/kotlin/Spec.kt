package io.github.rkbalgi.iso4k

import io.github.aakira.napier.Napier
import io.github.rkbalgi.iso4k.io.newBuffer
import kotlinx.serialization.Serializable
import net.mamoe.yamlkt.Yaml

const val specLocationProperty = "io.github.rkbalgi.iso4k.specsLocation"

@kotlinx.serialization.Serializable
data class MTIPair(val requestMTI: String, val responseMTI: String)

/**
 * Spec represents a ISO8583 specification. This class is usually mapped from a yaml definition file
 * (see *sample_spec.yml*)
 */
@Serializable
public class Spec(
    val name: String,
    val id: Int,
    private val requestResponseMTIMapping: List<MTIPair>,
    private val headerFields: List<IsoField>,
    private val messageSegments: List<MessageSegment>
) {

  private var req2responseMap = mutableMapOf<String, String>()

  fun header(): List<IsoField> {
    return headerFields
  }

  fun messageSegments(): List<MessageSegment> {
    return messageSegments
  }

  fun message(msgName: String): MessageSegment? {
    return messageSegments.find { it.name == msgName }
  }

  fun findMessage(msgData: ByteArray): String? {
    val bb = newBuffer(msgData)
    var headerVal = headerFields.map { it.parse(bb) }.reduce { acc, a -> (acc + a) }

    return try {
      messageSegments.first { it.selectorMatch(headerVal) }.name
    } catch (e: Exception) {
      null
    }
  }

  private fun init() {
    Napier.d { "Initializing Spec - $name" }
    requestResponseMTIMapping.forEach { req2responseMap[it.requestMTI] = it.responseMTI }
    messageSegments.forEach { messageSegment ->
      messageSegment.spec(this)
      messageSegment.fields.forEach {
        it.parent = null
        linkChildren(it)
      }
    }
  }

  private fun linkChildren(field: IsoField) {

    if (field.hasChildren()) {
      field.children?.forEach {
        it.parent = field
        linkChildren(it)
      }
    }
  }

  fun isRequest(mti: String): Boolean = req2responseMap.containsKey(mti)

  fun isResponse(mti: String): Boolean {
    return try {
      req2responseMap.values.first { mti == it }
      true
    } catch (e: NoSuchElementException) {
      false
    }
  }

  fun responseMTI(mti: String): String? {

    return try {
      req2responseMap[req2responseMap.keys.first { it == mti }]
    } catch (e: NoSuchElementException) {
      null
    }
  }

  companion object {

    // @Volatile
    private var initialized: Boolean = false
    var specMap = mutableMapOf<String, Spec>()

    fun allSpecs(): List<Spec> {
      var listOfAllSpecs = mutableListOf<Spec>().apply { specMap.values.forEach { this.add(it) } }

      return listOfAllSpecs
    }

    /** spec returns a Spec given its name */
    fun spec(name: String): Spec? {

      if (!initialized) {
        loadSpecs()
        specMap.values.forEach { it.init() }
        Napier.i("Initialized Specs")
        initialized = true
      }

      if (initialized) {
        return specMap[name]
      }
      throw RuntimeException("iso4k not initialized")
    }

    /**
     * Add a new spec given it's YAML definition
     * @param specDefinition The YAML definition of the spec
     */
    fun addSpec(specDefinition: String) {

      var decoded = Yaml.decodeFromString(Spec.serializer(), specDefinition)

      if (specMap.containsKey(decoded.name)) {
        Napier.w { "Spec ${decoded.name} already exists" }
        return
      }

      Napier.i { "Adding spec - ${decoded.name}" }
      specMap[decoded.name] = decoded
    }
  }
}

expect fun loadSpecs(): List<String>
