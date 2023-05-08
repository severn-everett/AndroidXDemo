package com.severett.androidxdemo.ui.util

import com.severett.androidxdemo.R
import com.severett.androidxdemo.ui.model.NavItem
import com.severett.androidxdemo.ui.sections.AtomicFU
import com.severett.androidxdemo.ui.sections.DateTime
import com.severett.androidxdemo.ui.sections.HTML
import com.severett.androidxdemo.ui.sections.Serializable

object Constants {
    private val serializationItem = NavItem(
        labelId = R.string.title_serialization,
        drawableId = R.drawable.ic_json_black_24dp,
        route = "serialization",
        content = { Serializable() }
    )
    private val atomicFUItem = NavItem(
        labelId = R.string.title_atomicfu,
        drawableId = R.drawable.ic_atomicfu_black_24dp,
        route = "atomicfu",
        content = { AtomicFU() }
    )
    private val htmlItem = NavItem(
        labelId = R.string.title_html,
        drawableId = R.drawable.ic_html_black_24dp,
        route = "html",
        content = { HTML() }
    )
    private val dateTimeItem = NavItem(
        labelId = R.string.title_datetime,
        drawableId = R.drawable.ic_calendar_black_24dp,
        route = "datetime",
        content = { DateTime() }
    )
    val NavItems = listOf(serializationItem, atomicFUItem, htmlItem, dateTimeItem)
    val StartingItem = serializationItem.route
}
