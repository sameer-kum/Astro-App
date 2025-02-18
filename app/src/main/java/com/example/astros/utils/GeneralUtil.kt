package com.example.astros.utils

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import com.example.astros.R
import com.example.astros.service.SharedPreference
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

object GeneralUtil {

    private lateinit var sharedPreference: SharedPreference

    // Function to open Date Picker
    fun openDatePicker(context: Context, textView: TextView, onDatePicked: (String) -> Unit) {
        val calendar = Calendar.getInstance()
        val datePicker = DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                val selectedDate = Calendar.getInstance()
                selectedDate.set(year, month, dayOfMonth)
                val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                textView.text = dateFormat.format(selectedDate.time)
                onDatePicked(dateFormat.format(selectedDate.time))
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        datePicker.show()
    }

    // Function to open Time Picker
    fun openTimePicker(context: Context, textView: TextView, onTimePicked: (String) -> Unit) {
        val calendar = Calendar.getInstance()
        val timePicker = TimePickerDialog(
            context,
            { _, hourOfDay, minute ->
                val selectedTime = Calendar.getInstance()
                selectedTime.set(Calendar.HOUR_OF_DAY, hourOfDay)
                selectedTime.set(Calendar.MINUTE, minute)
                val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
                textView.text = timeFormat.format(selectedTime.time)
                onTimePicked(timeFormat.format(selectedTime.time))
            },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            true // True for 24-hour format, false for AM/PM
        )

        timePicker.show()
    }

    // Function to show Gender Picker
    fun showGenderPicker(context: Context, textView: TextView, onGenderSelected: (String) -> Unit) {
        val popupMenu = PopupMenu(context, textView)
        popupMenu.menuInflater.inflate(R.menu.gender_menu, popupMenu.menu)

        popupMenu.setOnMenuItemClickListener { menuItem ->
            val selectedGender = menuItem.title.toString()
            textView.text = selectedGender
            onGenderSelected(selectedGender)
            true
        }

        popupMenu.show()
    }

    // Function to show Marital Status Menu
    fun showMaritalStatusMenu(context: Context, textView: TextView, onStatusSelected: (String) -> Unit) {
        val popupMenu = PopupMenu(context, textView)
        popupMenu.menuInflater.inflate(R.menu.marital_menu, popupMenu.menu)

        popupMenu.setOnMenuItemClickListener { item ->
            val selectedStatus = item.title.toString()
            textView.text = selectedStatus
            onStatusSelected(selectedStatus)
            true
        }

        popupMenu.show()
    }

    fun showToast(context: Context, message: String) {
        val toast = Toast.makeText(context, message, Toast.LENGTH_SHORT)
        toast.show()

        // Cancel toast after 1.5 seconds
        Handler(Looper.getMainLooper()).postDelayed({
            toast.cancel()
        }, 1500) // 1.5 sec
    }
}
