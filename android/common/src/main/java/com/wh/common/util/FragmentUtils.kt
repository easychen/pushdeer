package com.wh.common.util

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager

object FragmentUtils {

    fun showDialog(
        activity: AppCompatActivity,
        fragment: DialogFragment,
        tag: String? = null
    ) {
        showDialog(activity.supportFragmentManager, fragment, tag)
    }

    fun showDialog(
        fragmentManager: FragmentManager,
        fragment: DialogFragment,
        tag: String? = null
    ) {
        fragment.show(fragmentManager, tag)
    }
}