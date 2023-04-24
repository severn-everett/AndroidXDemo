package com.severett.androidxdemo

import org.jetbrains.kotlinx.lincheck.paramgen.ParameterGenerator
import kotlin.random.Random

class AnEnumGen(configuration: String) : ParameterGenerator<AnEnum> {
    override fun generate(): AnEnum {
        val values = AnEnum.values()
        return AnEnum.values()[Random.nextInt(0, values.size)]
    }
}
