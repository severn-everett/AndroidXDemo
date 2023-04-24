package com.severett.androidxdemo

import java.util.concurrent.atomic.AtomicInteger

class NaiveFoo {
    private var value = 0
    fun incAndGet() = ++value
    fun get() = value
}

class AtomicFoo {
    private val value = AtomicInteger(0)
    fun incAndGet() = value.incrementAndGet()
    fun get() = value.get()
}
