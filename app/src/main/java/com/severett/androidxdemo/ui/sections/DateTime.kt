package com.severett.androidxdemo.ui.sections

import android.app.DatePickerDialog
import android.content.Context
import android.widget.DatePicker
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.severett.androidxdemo.R
import com.severett.androidxdemo.serde.LocalDateSaver
import com.severett.androidxdemo.ui.components.AppButton
import com.severett.androidxdemo.ui.components.SectionLabel
import com.severett.androidxdemo.ui.theme.ApiumBlack
import com.severett.androidxdemo.ui.theme.europaFamily
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month
import kotlinx.datetime.TimeZone
import kotlinx.datetime.daysUntil
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.todayIn
import kotlin.math.abs

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateTime(modifier: Modifier = Modifier) {
    var displayDate by rememberSaveable { mutableStateOf(false) }
    var selectedDate by rememberSaveable(saver = LocalDateSaver) {
        mutableStateOf(Clock.System.todayIn(TimeZone.currentSystemDefault()))
    }
    var selectedTimeZone by rememberSaveable { mutableStateOf("") }
    var selectedTime by rememberSaveable { mutableStateOf("") }
    var expanded by rememberSaveable { mutableStateOf(false) }

    val datePickerCallback = { _: DatePicker, y: Int, m: Int, d: Int ->
        selectedDate = LocalDate(year = y, month = Month(m + 1), dayOfMonth = d)
        displayDate = true
    }
    val context = LocalContext.current

    ConstraintLayout {
        val (
            dateDemoSectionLabel,
            pickDateButton,
            daysBetweenDisplay,
            timeDemoSectionLabel,
            selectTimeZoneLabel,
            timeZoneSpinner,
            currentTimeDisplay,
        ) = createRefs()

        SectionLabel(
            modifier = modifier.constrainAs(dateDemoSectionLabel) {
                top.linkTo(parent.top)
            },
            text = stringResource(id = R.string.label_datetime_date_demo),
            textAlign = TextAlign.Center,
        )
        AppButton(
            modifier = modifier.constrainAs(pickDateButton) {
                top.linkTo(dateDemoSectionLabel.bottom, margin = 36.dp)
                start.linkTo(parent.start, margin = 138.dp)
                end.linkTo(parent.end, margin = 138.dp)
            },
            onClick = {
                launchDatePicker(
                    context,
                    datePickerCallback,
                    Clock.System.todayIn(TimeZone.currentSystemDefault())
                )
            },
            text = stringResource(id = R.string.button_datetime_date),
            fontSize = 18.sp
        )
        Text(
            modifier = modifier
                .constrainAs(daysBetweenDisplay) {
                    top.linkTo(pickDateButton.bottom, margin = 36.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .padding(horizontal = 8.dp),
            text = if (displayDate) {
                val currentDate = Clock.System.todayIn(TimeZone.currentSystemDefault())
                val daysBetween = abs(currentDate.daysUntil(selectedDate))
                pluralStringResource(
                    id = R.plurals.content_datetime_dates_between,
                    count = daysBetween,
                    daysBetween,
                    selectedDate,
                )
            } else "",
            fontSize = 20.sp
        )
        SectionLabel(
            modifier = modifier.constrainAs(timeDemoSectionLabel) {
                top.linkTo(daysBetweenDisplay.bottom, margin = 36.dp)
            },
            text = stringResource(id = R.string.label_datetime_time_demo),
            textAlign = TextAlign.Center,
        )
        Text(
            modifier = modifier.constrainAs(selectTimeZoneLabel) {
                top.linkTo(timeDemoSectionLabel.bottom, margin = 46.dp)
                start.linkTo(parent.start, margin = 8.dp)
            },
            text = stringResource(id = R.string.input_datetime_timezone),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
        )
        Box(
            modifier = modifier.constrainAs(timeZoneSpinner) {
                top.linkTo(timeDemoSectionLabel.bottom, margin = 36.dp)
                end.linkTo(parent.end, margin = 6.dp)
            }
        ) {
            Surface(color = Color.White, onClick = { expanded = true }) {
                Row {
                    Text(
                        modifier = modifier.width(165.dp),
                        text = selectedTimeZone,
                        fontSize = 18.sp,
                        color = ApiumBlack,
                        textAlign = TextAlign.Right
                    )
                    Icon(
                        Icons.Default.ArrowDropDown,
                        contentDescription = null,
                        tint = ApiumBlack
                    )
                }
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = {
                    expanded = false
                }
            ) {
                listOf(
                    "",
                    "Africa/Cairo",
                    "America/New_York",
                    "Europe/London",
                    "Asia/Kolkata",
                    "Asia/Tokyo"
                ).forEach { timeZone ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = timeZone,
                                fontFamily = europaFamily,
                                fontSize = 18.sp
                            )
                        },
                        onClick = {
                            selectedTimeZone = timeZone
                            selectedTime = if (timeZone.isNotBlank()) {
                                Clock.System
                                    .now()
                                    .toLocalDateTime(TimeZone.of(selectedTimeZone))
                                    .toString()
                            } else ""
                            expanded = false
                        },
                        trailingIcon = {
                            if (selectedTimeZone == timeZone) Icon(
                                Icons.Default.Check,
                                contentDescription = null
                            )
                        }
                    )
                }
            }
        }
        Text(
            modifier = modifier
                .constrainAs(currentTimeDisplay) {
                    top.linkTo(selectTimeZoneLabel.bottom, margin = 36.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .padding(horizontal = 8.dp),
            text = if (selectedTimeZone.isNotBlank()) {
                stringResource(
                    id = R.string.content_datetime_current_time,
                    selectedTimeZone,
                    selectedTime
                )
            } else "",
            fontSize = 20.sp
        )
    }
}

private fun launchDatePicker(
    context: Context,
    listener: DatePickerDialog.OnDateSetListener,
    currentTime: LocalDate
) {
    DatePickerDialog(
        context,
        R.style.DialogTheme,
        listener,
        currentTime.year,
        currentTime.monthNumber - 1,
        currentTime.dayOfMonth
    ).show()
}
