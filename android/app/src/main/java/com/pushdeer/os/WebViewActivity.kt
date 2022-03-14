package com.pushdeer.os

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView

class WebViewActivity : Activity() {

    companion object {
        const val URL_KEY = "url-to-open"

        fun load(context: Context, url: String) {
            context.startActivity(Intent(context, WebViewActivity::class.java).apply {
                putExtra(URL_KEY, url)
            })
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_webview)

        val ibClose = findViewById<ImageButton>(R.id.ib_close)
        val ibBack = findViewById<ImageButton>(R.id.ib_back)

        val webView = findViewById<WebView>(R.id.webview)

        val progressBar = findViewById<ProgressBar>(R.id.progressBar)

        val tvTitle = findViewById<TextView>(R.id.tv_title)

        webView.settings.apply {
            this.javaScriptEnabled = true
//            this.
        }

        ibClose.setOnClickListener {
            this.finish()
        }

        ibBack.setOnClickListener {
            if (webView.canGoBack()) {
                webView.goBack()
            } else {
                this.finish()
            }
        }

        webView.webViewClient = object :WebViewClient(){
            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                return false
            }
        }

        webView.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                super.onProgressChanged(view, newProgress)
                progressBar.progress = newProgress
                if (newProgress == 100){
                    progressBar.visibility = View.GONE
                }else{
                    progressBar.visibility = View.VISIBLE
                }
            }

            override fun onReceivedTitle(view: WebView?, title: String?) {
                super.onReceivedTitle(view, title)
                title?.let {
                    tvTitle.text = it
                }
            }


        }

        intent.getStringExtra(URL_KEY)?.let {
            webView.loadUrl(it)
            tvTitle.text = it
        }

    }
}