// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    val kotlinVersion = "1.8.20"
    val androidVersion = "7.4.2"
    id("com.android.application") version androidVersion apply false
    id("com.android.library") version androidVersion apply false
    kotlin("android") version kotlinVersion apply false
    kotlin("plugin.serialization") version kotlinVersion apply false
}