package com.severett.androidxdemo.model

import kotlinx.html.FlowOrPhrasingContent
import kotlinx.html.HTMLTag
import kotlinx.html.HtmlBlockInlineTag
import kotlinx.html.TagConsumer
import kotlinx.html.attributesMapOf
import kotlinx.html.visit

open class Strikethrough(
    initialAttributes: Map<String, String>,
    override val consumer: TagConsumer<*>
) :
    HTMLTag(
        "s",
        consumer,
        initialAttributes,
        null,
        true,
        false
    ),
    HtmlBlockInlineTag

inline fun FlowOrPhrasingContent.s(
    classes: String? = null,
    crossinline block: Strikethrough.() -> Unit = {}
): Unit = Strikethrough(attributesMapOf("class", classes), consumer).visit(block)