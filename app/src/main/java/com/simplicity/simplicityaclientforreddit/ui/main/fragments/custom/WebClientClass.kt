package com.simplicity.simplicityaclientforreddit.ui.main.fragments.custom

import android.annotation.TargetApi
import android.net.Uri
import android.os.Build
import android.util.Log
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient


class WebClientClass : WebViewClient() {

    override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
        val uri: Uri = Uri.parse(url)
        Log.i("WebClientClass", "We clicked link $uri")
        url?.let{ view?.loadUrl(it) }
        return true
    }

    @TargetApi(Build.VERSION_CODES.N)
    override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest): Boolean {
        val uri: Uri = request.url
        Log.i("WebClientClass", "We clicked link $uri")
        return true
    }
}