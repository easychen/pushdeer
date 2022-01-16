//package com.pushdeer.os.sss
//
//import android.content.Context
//import android.content.Intent
//import androidx.activity.result.contract.ActivityResultContract
//import com.pushdeer.os.activity.QrScanActivity
//
//class MyActivityResultContract : ActivityResultContract<String, String>() {
//    override fun createIntent(context: Context, input: String): Intent {
//        return QrScanActivity.forScanResultIntent(context)
//    }
//
//    override fun parseResult(resultCode: Int, intent: Intent?): String {
//        intent?.let {
//            return it.getStringExtra(QrScanActivity.DataKey).toString()
//        }
//        return ""
//    }
//}