package com.severett.androidxdemo

import org.jctools.queues.MpscLinkedQueue
import org.jetbrains.kotlinx.lincheck.annotations.OpGroupConfig
import org.jetbrains.kotlinx.lincheck.annotations.Operation
import org.jetbrains.kotlinx.lincheck.check
import org.jetbrains.kotlinx.lincheck.strategy.managed.modelchecking.ModelCheckingOptions
import org.jetbrains.kotlinx.lincheck.strategy.stress.StressOptions
import org.junit.jupiter.api.Test

private const val CONSUMER_GROUP = "consumer"

class OpGroupTest {
    private val queue = MpscLinkedQueue<Int>()
    fun offer() = queue.offer(5)
    @Operation(nonParallelGroup = CONSUMER_GROUP)
    fun poll(): Int? = queue.poll()
    @Operation(nonParallelGroup = CONSUMER_GROUP)
    fun peek(): Int? = queue.peek()
    @Test
    fun stressTest() = StressOptions().check(this::class.java)
    @Test
    fun modelTest() = ModelCheckingOptions().check(this::class)
}
