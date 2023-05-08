package com.severett.androidxdemo.ui.sections

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.severett.androidxdemo.R
import com.severett.androidxdemo.model.s
import com.severett.androidxdemo.ui.components.AppButton
import com.severett.androidxdemo.ui.components.InputField
import com.severett.androidxdemo.ui.components.SectionLabel
import kotlinx.html.a
import kotlinx.html.b
import kotlinx.html.div
import kotlinx.html.dom.createHTMLDocument
import kotlinx.html.dom.serialize
import kotlinx.html.p
import kotlinx.html.style

private val inputLabelTopMargin = 20.dp
private val styleLabelSize = 18.sp
private val generateButtonSideMargin = 115.dp

@Composable
fun HTML(modifier: Modifier = Modifier) {
    ConstraintLayout {
        val (
            inputSectionLabel,
            enterNameLabel,
            editTextName,
            stylingLabel,
            checkboxBold,
            checkboxStrikethrough,
            checkboxUnderlined,
            generateHTMLButton,
            generatedHTMLLabel,
            generatedHTMLDisplay,
        ) = createRefs()

        var nameInput by rememberSaveable { mutableStateOf("") }
        var isBoldChecked by rememberSaveable { mutableStateOf(false) }
        var isStrikethroughChecked by rememberSaveable { mutableStateOf(false) }
        var isUnderlinedChecked by rememberSaveable { mutableStateOf(false) }
        var generatedHtmlStr by rememberSaveable { mutableStateOf("") }

        val helloStr = stringResource(id = R.string.content_html_hello, nameInput)
        val linkStr = stringResource(id = R.string.content_html_link)

        SectionLabel(
            modifier = modifier.constrainAs(inputSectionLabel) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            },
            text = stringResource(id = R.string.label_html_input),
            textAlign = TextAlign.Center,
        )
        // Name input section
        Text(
            modifier = modifier.constrainAs(enterNameLabel) {
                top.linkTo(inputSectionLabel.bottom, margin = inputLabelTopMargin)
                start.linkTo(parent.start, margin = 85.dp)
            },
            text = stringResource(id = R.string.input_html_name),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
        )
        InputField(
            modifier = modifier.constrainAs(editTextName) {
                top.linkTo(enterNameLabel.bottom)
                start.linkTo(parent.start, margin = 32.dp)
            },
            textValue = nameInput,
            placeholder = stringResource(id = R.string.default_html_name),
            width = 164.dp,
            onValueChange = { nameInput = it }
        )
        // Style modification section
        Text(
            modifier = modifier.constrainAs(stylingLabel) {
                top.linkTo(inputSectionLabel.bottom, margin = inputLabelTopMargin)
                end.linkTo(parent.end, margin = 53.dp)
            },
            text = stringResource(id = R.string.input_html_styling),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
        )
        Row(
            modifier = modifier.constrainAs(checkboxBold) {
                top.linkTo(stylingLabel.bottom)
                end.linkTo(parent.end, margin = 95.dp)
            },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                onCheckedChange = {
                    isBoldChecked = !isBoldChecked
                },
                checked = isBoldChecked,
            )
            Text(
                text = stringResource(id = R.string.label_html_bold),
                fontSize = styleLabelSize,
                fontWeight = FontWeight.Bold,
            )
        }
        Row(
            modifier = modifier.constrainAs(checkboxUnderlined) {
                top.linkTo(checkboxBold.bottom)
                end.linkTo(parent.end, margin = 43.dp)
            },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                onCheckedChange = {
                    isUnderlinedChecked = !isUnderlinedChecked
                },
                checked = isUnderlinedChecked,
            )
            Text(
                text = stringResource(id = R.string.label_html_underlined),
                fontSize = styleLabelSize,
                textDecoration = TextDecoration.Underline
            )
        }
        Row(
            modifier = modifier.constrainAs(checkboxStrikethrough) {
                top.linkTo(checkboxUnderlined.bottom)
                end.linkTo(parent.end, margin = 20.dp)
            },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                onCheckedChange = {
                    isStrikethroughChecked = !isStrikethroughChecked
                },
                checked = isStrikethroughChecked,
            )
            Text(
                text = stringResource(id = R.string.label_html_strikethrough),
                fontSize = styleLabelSize,
                textDecoration = TextDecoration.LineThrough
            )
        }
        // HTML Generation
        AppButton(
            modifier = modifier
                .constrainAs(generateHTMLButton) {
                    top.linkTo(inputSectionLabel.bottom, margin = 200.dp)
                    start.linkTo(parent.start, margin = generateButtonSideMargin)
                    end.linkTo(parent.end, margin = generateButtonSideMargin)
                }
                .height(60.dp)
                .width(180.dp),
            onClick = {
                generatedHtmlStr = generateHTML(
                    helloStr = helloStr,
                    linkStr = linkStr,
                    isBold = isBoldChecked,
                    isUnderlined = isUnderlinedChecked,
                    isStrikethrough = isStrikethroughChecked,
                )
            },
            text = stringResource(id = R.string.button_html_generate),
            fontSize = 20.sp
        )
        SectionLabel(
            modifier = modifier.constrainAs(generatedHTMLLabel) {
                top.linkTo(generateHTMLButton.bottom, margin = 16.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            },
            text = stringResource(id = R.string.label_html_generated_html),
            textAlign = TextAlign.Center,
        )
        Text(
            modifier = modifier
                .constrainAs(generatedHTMLDisplay) {
                    top.linkTo(generatedHTMLLabel.bottom, margin = 24.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .fillMaxWidth()
                .padding(horizontal = 4.dp),
            text = generatedHtmlStr,
        )
    }
}

private fun generateHTML(
    helloStr: String,
    linkStr: String,
    isBold: Boolean,
    isUnderlined: Boolean,
    isStrikethrough: Boolean
): String {
    val underlineStr =
        if (isUnderlined) "text-decoration-line: underline;" else null
    return createHTMLDocument().div {
        p {
            if (underlineStr != null) style = underlineStr
            when {
                isBold -> b { if (isStrikethrough) s { +helloStr } else +helloStr }
                isStrikethrough -> s { +helloStr }
                else -> +helloStr
            }
        }
        a("https://github.com/kotlin/kotlinx.html") {
            +linkStr
        }
    }.documentElement.serialize()
}
