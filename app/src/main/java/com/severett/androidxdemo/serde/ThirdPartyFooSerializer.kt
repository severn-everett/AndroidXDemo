package com.severett.androidxdemo.serde

import com.severett.androidxdemo.model.ThirdPartyFoo
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.decodeStructure
import kotlinx.serialization.encoding.encodeStructure

private const val FIZZ_INDEX = 0
private const val BAZZ_INDEX = 1
private const val COUNT_INDEX = 2

object ThirdPartyFooSerializer : KSerializer<ThirdPartyFoo> {
    private val stringListSerializer = ListSerializer(String.serializer())
    override val descriptor = buildClassSerialDescriptor("ThirdPartyFoo") {
        element<String>("fizz")
        element<List<String>>("bazz")
        element<UInt>("count")
    }

    override fun deserialize(decoder: Decoder): ThirdPartyFoo {
        return decoder.decodeStructure(descriptor) {
            var fizzVal: String? = null
            var bazzVal: List<String>? = null
            var countVal: UInt? = null
            while (true) {
                when (val index = decodeElementIndex(descriptor)) {
                    FIZZ_INDEX -> fizzVal = decodeStringElement(descriptor, index)
                    BAZZ_INDEX -> bazzVal = decodeSerializableElement(descriptor, index, stringListSerializer)
                    COUNT_INDEX -> countVal = decodeLongElement(descriptor, index).toUInt()
                    CompositeDecoder.DECODE_DONE -> break
                    else -> error("Unexpected index: $index")
                }
            }
            require(fizzVal != null && bazzVal != null && countVal != null)
            ThirdPartyFoo(fizzVal, bazzVal, countVal)
        }
    }

    override fun serialize(encoder: Encoder, value: ThirdPartyFoo) {
        encoder.encodeStructure(descriptor) {
            encodeStringElement(descriptor, FIZZ_INDEX, value.fizz)
            encodeSerializableElement(descriptor, BAZZ_INDEX, stringListSerializer, value.bazz)
            encodeLongElement(descriptor, COUNT_INDEX, value.count.toLong())
        }
    }
}
