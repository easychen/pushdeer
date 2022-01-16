package com.wh.common.util

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.util.Log
object ClipboardUtils {
    fun getClipboardManager(context:Context):ClipboardManager{
        return context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    }

    fun copy(clipboardManager: ClipboardManager,label:String,text:String){
        val clipData = ClipData(label, arrayOf("text/plain"),ClipData.Item(text))
        clipboardManager.setPrimaryClip(clipData)
    }

    fun copy(context: Context,label:String,text:String){
        val clipData = ClipData(label, arrayOf("text/plain"),ClipData.Item(text))
        getClipboardManager(context = context).setPrimaryClip(clipData)
    }

    fun setupListener(clipboardManager: ClipboardManager,onChange:(String)->Unit){
        clipboardManager.addPrimaryClipChangedListener {
            clipboardManager.primaryClip?.let {
                if(it.itemCount>0){
                    onChange(it.getItemAt(0).text.toString())
                }
            }
        }
    }

    fun setupListener(context: Context,onChange:(String)->Unit): ClipboardManager {
        val clipboardManager = getClipboardManager(context = context)
        clipboardManager.addPrimaryClipChangedListener {
            clipboardManager.primaryClip?.let {
                if(it.description.label == "sbw-clip"){
                    Log.d("WH_", "setupListener: is from sbw-clip, skip listen")
                    return@addPrimaryClipChangedListener
                }
                if(it.itemCount>0){
                    onChange(it.getItemAt(0).text.toString())
                }
            }
        }
        return clipboardManager
    }

    fun setupListener(context: Context,l:ClipboardManager.OnPrimaryClipChangedListener):ClipboardManager{
        val clipboardManager = getClipboardManager(context = context)
        clipboardManager.addPrimaryClipChangedListener(l)
        return clipboardManager
    }


}