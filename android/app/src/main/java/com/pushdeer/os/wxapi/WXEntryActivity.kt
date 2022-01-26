package com.pushdeer.os.wxapi

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.pushdeer.os.App
import com.tencent.mm.opensdk.modelbase.BaseReq
import com.tencent.mm.opensdk.modelbase.BaseResp
import com.tencent.mm.opensdk.modelmsg.SendAuth
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler


class WXEntryActivity : Activity(), IWXAPIEventHandler {

    companion object {
        const val CODE_KEY = "code"
        const val ACTION_RETURN_CODE = "return-code"
    }

    private val iwxapi: IWXAPI by lazy { (application as App).iwxapi }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        iwxapi.handleIntent(intent, this);
    }

    // 微信发送的请求
    override fun onReq(p0: BaseReq?) {

    }

    // 发送到微信请求的响应结果
    override fun onResp(p0: BaseResp?) {
        when (p0?.errCode) {
            BaseResp.ErrCode.ERR_AUTH_DENIED, BaseResp.ErrCode.ERR_USER_CANCEL ->
//                if (RETURN_MSG_TYPE_SHARE === resp.getType()) {
//                    Toast.makeText(this, "分享失败", Toast.LENGTH_SHORT).show()
//                } else {
                Toast.makeText(this, "登录失败", Toast.LENGTH_SHORT).show()
//                }
            BaseResp.ErrCode.ERR_OK -> when (p0.type) {
                1 -> {
                    // login
                    val code: String = (p0 as SendAuth.Resp).code
                    code.let {
                        sendBroadcast(Intent().apply {
                            putExtra(CODE_KEY, code)
                            action = ACTION_RETURN_CODE
                        })
                        Log.d("WH_", "onResp: $code")
                        finish()
                    }
                }
//                RETURN_MSG_TYPE_LOGIN ->                     //拿到了微信返回的code,立马再去请求access_token
//                    var  code
//                    : String
//                    ?
//                    = (resp as SendAuth.Resp).code
//                RETURN_MSG_TYPE_SHARE -> {
//                    Toast.makeText(this, "微信分享成功", Toast.LENGTH_SHORT).show()
//                    finish()
//                }
            }
        }
    }
}