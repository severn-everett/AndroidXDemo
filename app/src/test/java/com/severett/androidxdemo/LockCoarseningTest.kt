package com.severett.androidxdemo

import org.jetbrains.kotlinx.lincheck.annotations.Operation
import org.jetbrains.kotlinx.lincheck.annotations.Validate
import org.jetbrains.kotlinx.lincheck.check
import org.jetbrains.kotlinx.lincheck.strategy.managed.modelchecking.ModelCheckingOptions
import org.jetbrains.kotlinx.lincheck.strategy.stress.StressOptions
import org.junit.jupiter.api.Test

private const val UNINITIALIZED = -1

// Test is based on the example of lock coarsening in Aleksey Shipilëv's
// article about the Java memory model:
// https://shipilev.net/blog/2016/close-encounters-of-jmm-kind/#myth-barriers-are-sane
class LockCoarseningTest {
    private var x = 0
    private var y = 0
    private var result: Pair<Int, Int> = UNINITIALIZED to UNINITIALIZED

    @Operation
    fun writeOperation() {
        synchronized(this) {
            x += 1
        }
        synchronized(this) {
            y += 1
        }
    }

    @Operation
    fun readOperation() {
        result = y to x
    }

    @Test
    fun stressTest() = StressOptions().check(this::class)

    @Test
    fun modelTest() = ModelCheckingOptions().check(this::class)

    @Validate
    fun validate() = check(result.first <= result.second) {
        "Lock coarsening occurred - x:${result.second} | y:${result.first}"
    }
}
