package com.wh.common.ui.componment

import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import com.google.android.material.datepicker.MaterialDatePicker
import com.wh.common.util.FragmentUtils

fun showDatePicker(activity: AppCompatActivity, onOK: (Long) -> Unit) {
    val picker = MaterialDatePicker.Builder.datePicker().setTitleText("aaa").build()
    activity.let {
        picker.show(it.supportFragmentManager, picker.toString())
        picker.addOnPositiveButtonClickListener { it ->
            onOK(it)
            Log.d("WH_", "showDatePicker: $it")
        }
    }
}

fun showDatePicker(fragmentManager: FragmentManager,onOK: (Long) -> Unit){
    val picker = MaterialDatePicker.Builder.datePicker().setTitleText("选择日期").build()
    picker.addOnPositiveButtonClickListener(onOK)
    FragmentUtils.showDialog(fragmentManager,picker,"date-picker")
}