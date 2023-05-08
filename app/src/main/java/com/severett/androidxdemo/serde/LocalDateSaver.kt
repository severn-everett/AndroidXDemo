package com.severett.androidxdemo.serde

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.SaverScope
import kotlinx.datetime.LocalDate

object LocalDateSaver : Saver<MutableState<LocalDate>, Int> {
    override fun restore(value: Int): MutableState<LocalDate> {
        return mutableStateOf(LocalDate.fromEpochDays(value))
    }

    override fun SaverScope.save(value: MutableState<LocalDate>): Int {
        return value.value.toEpochDays()
    }
}
