package com.yjp.easytools.http.cooike.store

import okhttp3.Cookie
import okhttp3.HttpUrl

/**
 * $
 * @author yjp
 * @date 2020/4/1 11:21
 */
class MemoryCookieStore : CookieStore {

    private val memoryCookies = mutableMapOf<String, MutableList<Cookie>>()

    @Synchronized
    override fun saveCookie(url: HttpUrl, cookie: List<Cookie>) {
        var oldCookies = memoryCookies[url.host]
        if (oldCookies == null) {
            oldCookies = mutableListOf()
            memoryCookies[url.host] = oldCookies
        } else {
            val needRemove = mutableListOf<Cookie>()
            for (newCookie in cookie) {
                for (oldCookie in oldCookies) {
                    if (newCookie.name == oldCookie.name) {
                        needRemove.add(oldCookie)
                    }
                }
            }
            oldCookies.removeAll(needRemove)
        }
        oldCookies.addAll(cookie)
    }

    @Synchronized
    override fun saveCookie(url: HttpUrl, cookie: Cookie) {
        var oldCookies = memoryCookies[url.host]
        if (oldCookies == null) {
            oldCookies = mutableListOf()
            memoryCookies[url.host] = oldCookies
        } else {
            val needRemove = mutableListOf<Cookie>()
            for (oldCookie in oldCookies) {
                if (cookie.name == oldCookie.name) {
                    needRemove.add(oldCookie)
                }
            }
            oldCookies.removeAll(needRemove)
        }
        oldCookies.add(cookie)
    }

    @Synchronized
    override fun loadCookie(url: HttpUrl): List<Cookie> {
        var cookies = memoryCookies[url.host]
        if (cookies == null) {
            cookies = mutableListOf()
            memoryCookies[url.host] = cookies
        }
        return cookies
    }

    @Synchronized
    override fun getAllCookie(): List<Cookie> {
        val cookies = mutableListOf<Cookie>()
        val httpUrls = memoryCookies.keys
        for (url in httpUrls) {
            cookies.addAll(memoryCookies[url]!!)
        }
        return cookies
    }

    @Synchronized
    override fun getCookie(url: HttpUrl): List<Cookie> {
        val cookies = mutableListOf<Cookie>()
        var urlCookies = memoryCookies[url.host]
        if (urlCookies == null) {
            urlCookies = mutableListOf()
            memoryCookies[url.host] = urlCookies
        }
        cookies.addAll(urlCookies)
        return cookies
    }

    @Synchronized
    override fun removeCookie(url: HttpUrl, cookie: Cookie): Boolean {
        val cookies = memoryCookies[url.host]
        return cookies?.remove(cookie) ?: false
    }

    @Synchronized
    override fun removeCookie(url: HttpUrl): Boolean {
        return memoryCookies.remove(url.host) != null
    }

    @Synchronized
    override fun removeAllCookie(): Boolean {
        memoryCookies.clear()
        return true
    }
}