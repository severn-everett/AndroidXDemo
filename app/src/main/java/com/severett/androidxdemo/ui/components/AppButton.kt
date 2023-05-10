package com.severett.androidxdemo.ui.components

import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import com.severett.androidxdemo.ui.theme.europaFamily

@Composable
fun AppButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    text: String,
    fontSize: TextUnit
) {
    FilledTonalButton(
        modifier = modifier,
        shape = MaterialTheme.shapes.extraSmall,
        onClick = onClick
    ) {
        Text(
            text = text,
            fontSize = fontSize,
            fontFamily = europaFamily,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
        )
    }
}
