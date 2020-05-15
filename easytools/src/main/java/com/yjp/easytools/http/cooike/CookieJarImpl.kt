package com.yjp.easytools.http.cooike

import com.yjp.easytools.http.cooike.store.CookieStore
import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl

/**
 * $
 * @author yjp
 * @date 2020/4/1 11:19
 */
class CookieJarImpl(var cookieStore: CookieStore) : CookieJar {

    @Synchronized
    override fun loadForRequest(url: HttpUrl): List<Cookie> {
        return cookieStore.loadCookie(url)
    }

    @Synchronized
    override fun saveFromResponse(url: HttpUrl, cookieList: List<Cookie>) {
        cookieStore.saveCookie(url, cookieList)
    }
}