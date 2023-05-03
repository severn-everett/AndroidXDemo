package com.severett.androidxdemo

import java.util.concurrent.atomic.AtomicInteger

class NaiveFoo {
    private var value = 0
    fun incAndGet() = ++value
    fun get() = value
}

class DeadlockFoo {
    private var value = 0
    private val blockA = "BLOCK_A"
    private val blockB = "BLOCK_B"

    fun incAndGet(): Int {
        synchronized(blockA) {
            synchronized(blockB) {
                return ++value
            }
        }
    }

    fun get(): Int {
        synchronized(blockB) {
            synchronized(blockA) {
                return value
            }
        }
    }
}

class AtomicFoo {
    private val value = AtomicInteger(0)
    fun incAndGet() = value.incrementAndGet()
    fun get() = value.get()
}
