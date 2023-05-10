package com.severett.androidxdemo.ui.sections

import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.severett.androidxdemo.R
import com.severett.androidxdemo.ui.components.AppButton
import com.severett.androidxdemo.ui.components.SectionLabel
import kotlinx.atomicfu.atomic
import kotlinx.atomicfu.locks.reentrantLock
import kotlinx.atomicfu.locks.withLock
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

private const val LIMIT = 10_000
private val buttonTopMargin = 72.dp
private val buttonSideMargin = 50.dp
private val buttonWidth = 120.dp
private val resultsTopMargin = 32.dp
private val raceSideMargin = 12.dp
private val lockDemoSideMargin = 12.dp
private val buttonFontSize = 18.sp

@Composable
fun AtomicFU(modifier: Modifier = Modifier) {
    var displayRaceResults by rememberSaveable { mutableStateOf(false) }
    var safeValue by rememberSaveable { mutableStateOf(0) }
    var unsafeValue by rememberSaveable { mutableStateOf(0) }
    var displayLockDemo by rememberSaveable { mutableStateOf(false) }
    var lockDemoValue by rememberSaveable { mutableStateOf(0) }
    ConstraintLayout {
        val (
            headerLabel,
            runRaceButton,
            lockDemoButton,
            safeValueLabel,
            unsafeValueLabel,
            lockDemoValueLabel,
        ) = createRefs()
        SectionLabel(
            modifier = modifier.constrainAs(headerLabel) {
                top.linkTo(parent.top)
            },
            text = stringResource(R.string.label_atomicfu_header),
            textAlign = TextAlign.Center
        )
        // Atomic Race Section
        AppButton(
            modifier = modifier
                .constrainAs(runRaceButton) {
                    top.linkTo(headerLabel.bottom, margin = buttonTopMargin)
                    start.linkTo(parent.start, margin = buttonSideMargin)
                }
                .width(buttonWidth),
            onClick = {
                val (safeResult, unsafeResult) = runRace()
                safeValue = safeResult
                unsafeValue = unsafeResult
                displayRaceResults = true
            },
            text = stringResource(id = R.string.button_atomicfu_run_race),
            fontSize = buttonFontSize
        )
        if (displayRaceResults) {
            Text(
                modifier = modifier.constrainAs(safeValueLabel) {
                    top.linkTo(runRaceButton.bottom, margin = resultsTopMargin)
                    start.linkTo(parent.start, margin = raceSideMargin)
                },
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                text = stringResource(id = R.string.content_atomicfu_safe_value, safeValue)
            )
            Text(
                modifier = modifier.constrainAs(unsafeValueLabel) {
                    top.linkTo(safeValueLabel.bottom, margin = 18.dp)
                    start.linkTo(parent.start, margin = raceSideMargin)
                },
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                text = stringResource(id = R.string.content_atomicfu_unsafe_value, unsafeValue)
            )
        }
        // Lock Demo Section
        AppButton(
            modifier = modifier
                .constrainAs(lockDemoButton) {
                    top.linkTo(headerLabel.bottom, margin = buttonTopMargin)
                    end.linkTo(parent.end, margin = buttonSideMargin)
                }
                .width(buttonWidth),
            onClick = {
                lockDemoValue = lockDemo()
                displayLockDemo = true
            },
            text = stringResource(id = R.string.button_atomicfu_lock_demo),
            fontSize = buttonFontSize
        )
        if (displayLockDemo) {
            Text(
                modifier = modifier.constrainAs(lockDemoValueLabel) {
                    top.linkTo(lockDemoButton.bottom, margin = resultsTopMargin)
                    end.linkTo(parent.end, margin = lockDemoSideMargin)
                },
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                text = stringResource(id = R.string.content_atomicfu_locked_value, lockDemoValue)
            )
        }
    }
}

private fun runRace(): Pair<Int, Int> {
    val safeCounter = atomic(0)
    var unsafeCounter = 0
    runBlocking {
        (0 until LIMIT).map {
            CoroutineScope(Dispatchers.Default).launch {
                safeCounter += 1
                unsafeCounter += 1
            }
        }.forEach { it.join() }
    }
    return safeCounter.value to unsafeCounter
}

private fun lockDemo(): Int {
    var unsafeValue = 0
    val lock = reentrantLock()
    runBlocking {
        (0 until LIMIT).map {
            CoroutineScope(Dispatchers.Default).launch {
                lock.withLock { unsafeValue += 1 }
            }
        }.forEach { it.join() }
    }
    return unsafeValue
}
