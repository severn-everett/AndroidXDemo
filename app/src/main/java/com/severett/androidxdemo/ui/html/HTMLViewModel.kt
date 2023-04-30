package com.severett.androidxdemo.ui.html

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.html.a
import kotlinx.html.b
import kotlinx.html.div
import kotlinx.html.dom.createHTMLDocument
import kotlinx.html.dom.serialize
import kotlinx.html.i
import kotlinx.html.p
import kotlinx.html.style

class HTMLViewModel : ViewModel() {
    val name = MutableLiveData<String>()
    val isBold = MutableLiveData(false)
    val isItalics = MutableLiveData(false)
    val isUnderlined = MutableLiveData(false)

    fun generateHTML(helloStr: String, linkStr: String): String {
        val underlineStr =
            if (isUnderlined.value == true) "text-decoration-line: underline;" else null
        val useBold = isBold.value == true
        val useItalics = isItalics.value == true
        val textStr = String.format(helloStr, name.value)
        return createHTMLDocument().div {
            p {
                if (underlineStr != null) style = underlineStr
                when {
                    useBold -> b { if (useItalics) i { +textStr} else +textStr }
                    useItalics -> i { +textStr }
                    else -> +textStr
                }
            }
            a("https://github.com/kotlin/kotlinx.html") {
                +linkStr
            }
        }.documentElement.serialize()
    }
}