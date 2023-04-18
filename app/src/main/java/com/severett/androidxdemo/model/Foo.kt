package com.severett.androidxdemo.model

import kotlinx.serialization.Serializable

@Serializable
data class Foo(val fizz: String, val bazz: List<String>, val count: UInt)
