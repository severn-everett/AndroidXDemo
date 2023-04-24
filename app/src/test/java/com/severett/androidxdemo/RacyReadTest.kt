package com.severett.androidxdemo

import org.jetbrains.kotlinx.lincheck.annotations.Operation
import org.jetbrains.kotlinx.lincheck.annotations.Validate
import org.jetbrains.kotlinx.lincheck.check
import org.jetbrains.kotlinx.lincheck.strategy.managed.modelchecking.ModelCheckingOptions
import org.jetbrains.kotlinx.lincheck.strategy.stress.StressOptions
import org.junit.Test

private const val UNINITIALIZED = -1

// Test is based on the example of racy reads in Aleksey Shipilëv's
// article about the Java memory model:
// https://shipilev.net/blog/2016/close-encounters-of-jmm-kind/#wishful-hb-actual
class RacyReadTest {
    // Uncomment to "fix" test and prevent racy reads
    // @Volatile
    private var a = 0
    private var result = UNINITIALIZED to UNINITIALIZED

    @Operation
    fun writer() {
        a += 1
    }

    @Operation
    fun reader() {
        result = a to a
    }

    @Validate
    fun validate() = check(result.first <= result.second) {
        "Racy read occurred - a1:${result.first} | a2:${result.second}"
    }

    @Test
    fun stressTest() = StressOptions().check(this::class.java)
    @Test
    fun modelTest() = ModelCheckingOptions().check(this::class)
}
