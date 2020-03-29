package com.yjp.easytools.databing.viewadapter.webview

import android.webkit.WebView
import androidx.databinding.BindingAdapter

/**
 * $
 *
 * @author yjp
 * @date 2020-03-29 15:17
 */
object ViewAdapter {
    @BindingAdapter("render")
    fun loadHtml(webView: WebView, html: String) {
        webView.loadDataWithBaseURL(null, html, "text/html", "UTF-8", null)
    }
}