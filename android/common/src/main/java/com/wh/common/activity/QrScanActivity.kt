package com.wh.common.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import cn.bingoogolapple.qrcode.core.QRCodeView
import com.wh.common.R


class QrScanActivity : AppCompatActivity(), QRCodeView.Delegate {

    private val TAG = "WH_" + javaClass.simpleName
    private lateinit var qrCode: QRCodeView

    companion object {
        val RequestCode_get_scan_result = 436
        val DataKey = "qr_scan_result"

        fun forScanResultIntent(context: Context): Intent {
            return Intent(context, QrScanActivity::class.java).apply {
                putExtra(DataKey, 1)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qr_scan)
        qrCode = findViewById(R.id.qrcode1)
        qrCode.setDelegate(this)
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart")
        qrCode.startSpotAndShowRect()
    }

    override fun onStop() {
        Log.d(TAG, "onStop")
        qrCode.stopCamera()
        super.onStop()
    }

    override fun onDestroy() {
        qrCode.onDestroy()
        super.onDestroy()
    }

    override fun onScanQRCodeSuccess(result: String?) {
        Log.d(TAG, "onScanQRCodeSuccess: $result")
        qrCode.stopCamera()
        val intent = Intent()
        intent.putExtra(DataKey, result)
        setResult(RequestCode_get_scan_result, intent)
        finish()
    }

    override fun onScanQRCodeOpenCameraError() {
        Log.e(TAG, "onScanQRCodeOpenCameraError")
        qrCode.startSpotAndShowRect()
    }
}