package com.severett.androidxdemo.ui.sections

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstrainScope
import androidx.constraintlayout.compose.ConstrainedLayoutReference
import androidx.constraintlayout.compose.ConstraintLayout
import com.severett.androidxdemo.R
import com.severett.androidxdemo.model.Foo
import com.severett.androidxdemo.model.ThirdPartyFoo
import com.severett.androidxdemo.serde.ThirdPartyFooSerializer
import com.severett.androidxdemo.ui.components.AppButton
import com.severett.androidxdemo.ui.components.InputField
import com.severett.androidxdemo.ui.components.SectionLabel
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

private val inputBottomMargin = 4.dp
private val displayTopMargin = 6.dp
private val displayLabelMargin = 113.dp
private val displayStrSize = 18.sp
private val displayStrPadding = 5.dp
private val buttonFontSize = 20.sp

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun Serializable(modifier: Modifier = Modifier) {
    var fizzText by rememberSaveable { mutableStateOf("") }
    var bazzText by rememberSaveable { mutableStateOf("") }
    var countText by rememberSaveable { mutableStateOf("") }
    var isNormalSerde by rememberSaveable { mutableStateOf(true) }
    var displaySerdeLabels by rememberSaveable { mutableStateOf(false) }
    var serializedDisplay by rememberSaveable { mutableStateOf("") }
    var deserializedDisplay by rememberSaveable { mutableStateOf("") }

    val keyboardController = LocalSoftwareKeyboardController.current

    ConstraintLayout {
        val (
            enterFizz,
            fizzInput,
            enterBazz,
            bazzInput,
            enterCount,
            countInput,
            serdeTypeRow,
            runSerdeButton,
            serializedFooLabel,
            serializedFooDisplay,
            deserializedFooLabel,
            deserializedFooDisplay,
        ) = createRefs()
        SectionLabel(
            modifier = modifier.constrainAs(enterFizz) {
                top.linkTo(parent.top, margin = 0.dp)
            },
            text = stringResource(id = R.string.input_serialization_fizz),
            padding = 26.dp,
        )
        InputField(
            modifier = modifier.constrainAs(fizzInput, setInputConstraints(enterFizz)),
            textValue = fizzText,
            placeholder = stringResource(id = R.string.default_serialization_fizz),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onGo = { keyboardController?.hide() }),
            onValueChange = { fizzText = it }
        )
        SectionLabel(
            modifier = modifier.constrainAs(enterBazz) {
                top.linkTo(fizzInput.bottom, margin = inputBottomMargin)
            },
            text = stringResource(id = R.string.input_serialization_bazz),
            padding = 26.dp,
        )
        InputField(
            modifier = modifier.constrainAs(bazzInput, setInputConstraints(enterBazz)),
            textValue = bazzText,
            placeholder = stringResource(id = R.string.default_serialization_bazz),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onGo = { keyboardController?.hide() }),
            onValueChange = { bazzText = it }
        )
        SectionLabel(
            modifier = modifier.constrainAs(enterCount) {
                top.linkTo(bazzInput.bottom, margin = inputBottomMargin)
            },
            text = stringResource(id = R.string.input_serialization_count),
            padding = 26.dp,
        )
        InputField(
            modifier = modifier.constrainAs(countInput, setInputConstraints(enterCount)),
            textValue = countText,
            placeholder = stringResource(id = R.string.default_serialization_count),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(onGo = { keyboardController?.hide() }),
            onValueChange = { countText = it }
        )
        Row(
            modifier = modifier
                .selectableGroup()
                .constrainAs(serdeTypeRow) {
                    top.linkTo(countInput.bottom)
                    start.linkTo(parent.start, margin = 26.dp)
                    end.linkTo(parent.end, margin = 26.dp)
                }
        ) {
            // "Normal" serde radio button
            RadioButton(selected = isNormalSerde, onClick = { isNormalSerde = true })
            Text(
                modifier = modifier.padding(top = 10.dp),
                text = stringResource(id = R.string.label_serialization_type_radio_normal),
                fontSize = 20.sp,
            )
            // "Third-Party" serde radio button
            RadioButton(selected = !isNormalSerde, onClick = { isNormalSerde = false })
            Text(
                modifier = modifier.padding(top = 10.dp),
                text = stringResource(id = R.string.label_serialization_type_radio_third_party),
                fontSize = 20.sp,
            )
        }
        AppButton(
            modifier = modifier.constrainAs(runSerdeButton) {
                top.linkTo(serdeTypeRow.bottom)
                start.linkTo(parent.start, margin = 158.dp)
                end.linkTo(parent.end, margin = 159.dp)
            },
            onClick = {
                keyboardController?.hide()
                val (serializedStr, deserializedStr) = runSerde(
                    isNormalSerde,
                    fizzText,
                    bazzText,
                    countText
                )
                serializedDisplay = serializedStr
                deserializedDisplay = deserializedStr
                displaySerdeLabels = true
            },
            text = stringResource(id = R.string.button_serialization_execute),
            fontSize = buttonFontSize
        )
        if (displaySerdeLabels) {
            Text(
                modifier = modifier.constrainAs(serializedFooLabel) {
                    top.linkTo(runSerdeButton.bottom, margin = displayTopMargin)
                    start.linkTo(parent.start, margin = displayLabelMargin)
                    end.linkTo(parent.end, margin = displayLabelMargin)
                },
                text = stringResource(id = R.string.label_serialization_serialized),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                modifier = modifier
                    .constrainAs(serializedFooDisplay) {
                        top.linkTo(serializedFooLabel.bottom, margin = displayTopMargin)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                    .fillMaxWidth()
                    .padding(horizontal = displayStrPadding),
                text = serializedDisplay,
                fontSize = displayStrSize
            )
            Text(
                modifier = modifier.constrainAs(deserializedFooLabel) {
                    top.linkTo(serializedFooDisplay.bottom, margin = displayTopMargin)
                    start.linkTo(parent.start, margin = displayLabelMargin)
                    end.linkTo(parent.end, margin = displayLabelMargin)
                },
                text = stringResource(id = R.string.label_serialization_deserialized),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                modifier = modifier
                    .constrainAs(deserializedFooDisplay) {
                        top.linkTo(deserializedFooLabel.bottom, margin = displayTopMargin)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                    .fillMaxWidth()
                    .padding(horizontal = displayStrPadding),
                text = deserializedDisplay,
                fontSize = displayStrSize
            )
        }
    }
}

@Suppress("UnnecessaryVariable")
private fun runSerde(
    isNormalSerde: Boolean,
    fizzStr: String,
    bazzStr: String,
    countStr: String
): Pair<String, String> {
    val fizz = fizzStr
    val bazz = bazzStr.split(",")
    val count = if (countStr.isNotBlank()) countStr.toUInt() else 0u
    return if (isNormalSerde) {
        val foo = Foo(fizz, bazz, count)
        val serializedFoo = Json.encodeToString(foo)
        val deserializedFoo = Json.decodeFromString<Foo>(serializedFoo)
        serializedFoo to deserializedFoo.toString()
    } else {
        val thirdPartyFoo = ThirdPartyFoo(fizz, bazz, count)
        val serializedTPF = Json.encodeToString(ThirdPartyFooSerializer, thirdPartyFoo)
        val deserializedTPF = Json.decodeFromString(ThirdPartyFooSerializer, serializedTPF)
        serializedTPF to deserializedTPF.toString()
    }
}

private fun setInputConstraints(anchor: ConstrainedLayoutReference): ConstrainScope.() -> Unit = {
    top.linkTo(anchor.bottom, margin = 0.dp)
    start.linkTo(parent.start, margin = 26.dp)
    end.linkTo(parent.end, margin = 26.dp)
}
