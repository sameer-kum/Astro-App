package com.example.astros.utils

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.widget.TextView
import com.example.astros.service.SharedPreference
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

object GeneralUtil {

    private lateinit var sharedPreference: SharedPreference

    // Function to open Date Picker
    fun openDatePicker(context: Context, textView: TextView) {
        val calendar = Calendar.getInstance()
        val datePicker = DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                val selectedDate = Calendar.getInstance()
                selectedDate.set(year, month, dayOfMonth)
                val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                textView.text = dateFormat.format(selectedDate.time)
                sharedPreference.saveDOB(dateFormat.format(selectedDate.time))

            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        datePicker.show()
    }

    // Function to open Time Picker
    fun openTimePicker(context: Context, textView: TextView) {
        val calendar = Calendar.getInstance()
        val timePicker = TimePickerDialog(
            context,
            { _, hourOfDay, minute ->
                val selectedTime = Calendar.getInstance()
                selectedTime.set(Calendar.HOUR_OF_DAY, hourOfDay)
                selectedTime.set(Calendar.MINUTE, minute)
                val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
                textView.text = timeFormat.format(selectedTime.time)
            },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            true // True for 24-hour format, false for AM/PM
        )

        timePicker.show()
    }
}
