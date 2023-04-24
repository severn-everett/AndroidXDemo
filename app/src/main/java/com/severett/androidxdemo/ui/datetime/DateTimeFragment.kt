package com.severett.androidxdemo.ui.datetime

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import com.google.android.material.datepicker.MaterialDatePicker
import com.severett.androidxdemo.R
import com.severett.androidxdemo.databinding.FragmentDatetimeBinding
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.math.abs
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.time.DurationUnit

class DateTimeFragment : Fragment() {
    private var _binding: FragmentDatetimeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDatetimeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.pickDateButton.setOnClickListener {
            val datePicker = MaterialDatePicker.Builder
                .datePicker()
                .build()
            datePicker.addOnPositiveButtonClickListener { value ->
                val now = Clock.System.now()
                val selectedInstant = Instant.fromEpochMilliseconds(value)
                val elapse = (now - selectedInstant).toDouble(DurationUnit.DAYS)
                val elapsedDays = (if (elapse < 0) ceil(abs(elapse)) else floor(elapse)).toInt()
                binding.daysBetweenDisplay.text = resources.getQuantityString(
                    R.plurals.content_datetime_dates_between,
                    elapsedDays,
                    elapsedDays,
                    selectedInstant.toLocalDateTime(TimeZone.currentSystemDefault())
                )
            }
            datePicker.show(parentFragmentManager, null)
        }

        ArrayAdapter.createFromResource(
            this.requireContext(),
            R.array.content_datetime_timezones,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            val timeZoneSpinner = binding.timeZoneSpinner
            timeZoneSpinner.adapter = adapter
            timeZoneSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View?,
                    pos: Int,
                    id: Long
                ) {
                    val timezone = when (pos) {
                        1 -> "Africa/Cairo"
                        2 -> "America/New_York"
                        3 -> "Europe/London"
                        4 -> "Asia/Kolkata"
                        5 -> "Asia/Tokyo"
                        else -> null
                    }
                    if (timezone == null) {
                        binding.currentTimeDisplay.text = ""
                    } else {
                        val currentTime = Clock.System.now().toLocalDateTime(
                            TimeZone.of(timezone)
                        )
                        binding.currentTimeDisplay.text = resources.getString(
                            R.string.content_datetime_current_time,
                            parent.getItemAtPosition(pos),
                            currentTime
                        )
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // No-Op
                }
            }
        }
        val timeZoneSpinner = binding.timeZoneSpinner
        timeZoneSpinner.adapter = ArrayAdapter.createFromResource(
            this.requireContext(),
            R.array.content_datetime_timezones,
            android.R.layout.simple_spinner_item
        )


        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}