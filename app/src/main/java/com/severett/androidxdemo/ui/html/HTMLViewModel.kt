package com.severett.androidxdemo.ui.html

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.html.a
import kotlinx.html.div
import kotlinx.html.dom.createHTMLDocument
import kotlinx.html.dom.serialize
import kotlinx.html.p
import kotlinx.html.style

class HTMLViewModel : ViewModel() {
    val name = MutableLiveData<String>()
    val isBold = MutableLiveData(false)
    val isItalics = MutableLiveData(false)
    val isUnderlined = MutableLiveData(false)

    fun generateHTML(helloStr: String, linkStr: String): String {
        val styleStr = buildList {
            if (isBold.value == true) add("font-weight: bold;")
            if (isItalics.value == true) add("font-style: italic;")
            if (isUnderlined.value == true) add("text-decoration-line: underline;")
        }.joinToString(" ")
        return createHTMLDocument().div {
            p {
                if (styleStr.isNotBlank()) style = styleStr
                +String.format(helloStr, name.value)
            }
            a("https://github.com/kotlin/kotlinx.html") {
                +linkStr
            }
        }.documentElement.serialize()
    }
}