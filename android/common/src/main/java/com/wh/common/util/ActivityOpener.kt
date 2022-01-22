package com.wh.common.util

import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.wh.common.activity.QrScanActivity

object ActivityOpener {

    fun forQrScanResult(
        activity: AppCompatActivity,
        onReturn: (String) -> Unit = {}
    ): ActivityResultLauncher<Intent> {
        return activity.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            result.data?.getStringExtra(QrScanActivity.DataKey)?.let {
                onReturn(it)
                Toast.makeText(activity, it, Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun forPermission(
        activity: AppCompatActivity,
        onReturn: () -> Unit = {}
    ): ActivityResultLauncher<Array<String>> {
        return activity.registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
            it.entries.forEach {
                Log.d("WH_", "forPermission:${it.key} ${it.value} ")
            }
            if (it.entries.all { it.value == true }){
                onReturn()
            }
        }
    }
}