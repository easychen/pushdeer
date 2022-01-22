package com.pushdeer.os.util

import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.wh.common.activity.QrScanActivity

object ActivityOpener {

    fun forResult(activity: AppCompatActivity): ActivityResultLauncher<Intent> {
        return activity.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            Toast.makeText(
                activity,
                "${result.data?.getStringExtra(QrScanActivity.DataKey)}",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    fun forPermission(activity: AppCompatActivity): ActivityResultLauncher<Array<String>> {
        return activity.registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
            it.entries.forEach {
                Log.d("WH_", "forPermission:${it.key} ${it.value} ")
            }
        }
    }
}