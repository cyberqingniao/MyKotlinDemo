package com.yjp.easytools.http.cooike.store

import android.content.Context
import android.text.TextUtils
import com.yjp.easytools.utils.SPUtils
import com.yjp.easytools.utils.StringUtils
import com.yjp.easytools.utils.Utils
import okhttp3.Cookie
import okhttp3.HttpUrl
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.util.concurrent.ConcurrentHashMap

/**
 * $
 * @author yjp
 * @date 2020/4/1 11:39
 */
class PersistentCookieStore(var context: Context) : CookieStore {

    companion object {
        private const val LOG_TAG = "PersistentCookieStore"
        private const val COOKIE_PREFS = "habit_cookie" //cookie使用prefs保存
        private const val COOKIE_NAME_PREFIX = "cookie_" //cookie持久化的统一前缀

    }

    private var cookies = mutableMapOf<String, ConcurrentHashMap<String, Cookie>>()

    init {
        //将持久化的cookies缓存到内存中,数据结构为 Map<Url.host, Map<Cookie.name, Cookie>>
        val prefsMap = SPUtils.getAll()
        if (prefsMap != null) {
            for (entry in prefsMap.entries) {
                if (entry.value != null && !entry.key.startsWith(COOKIE_NAME_PREFIX)) {
                    //获取url对应的所有cookie的key,用","分割
                    val cookieNames = TextUtils.split(entry.value as String, ",")
                    for (name in cookieNames) {
                        //根据对应cookie的Key,从xml中获取cookie的真实值
                        val encodedCookie = SPUtils.getString(COOKIE_NAME_PREFIX + name)
                        if (!StringUtils.isEmpty(encodedCookie)) {
                            val decodedCookie = decodeCookie(encodedCookie)
                            if (!cookies.containsKey(entry.key)) {
                                cookies[entry.key] = ConcurrentHashMap<String, Cookie>()
                            }
                            cookies[entry.key]?.set(name, decodedCookie)
                        }
                    }
                }
            }
        }
    }

    private fun getCookieToken(cookie: Cookie): String {
        return cookie.name + "@" + cookie.domain
    }

    /** 当前cookie是否过期 */
    private fun isCookieExpired(cookie: Cookie): Boolean {
        return cookie.expiresAt < System.currentTimeMillis()
    }

    /**
     * cookies 序列化成 string
     *
     * @param cookie 要序列化的cookie
     * @return 序列化之后的string
     */
    private fun encodeCookie(cookie: SerializableHttpCookie): String {
        val os = ByteArrayOutputStream()
        val outputStream = ObjectOutputStream(os)
        outputStream.writeObject(cookie)
        return Utils.byteArrayToHexString(os.toByteArray())
    }

    /**
     * 将字符串反序列化成cookies
     *
     * @param cookieString cookies string
     * @return cookie object
     */
    private fun decodeCookie(cookieString: String): Cookie {
        val bytes = Utils.hexStringToByteArray(cookieString)
        val byteArrayInputStream = ByteArrayInputStream(bytes)
        val objectInputStream = ObjectInputStream(byteArrayInputStream)
        val cookie = (objectInputStream.readObject() as SerializableHttpCookie).getMyCookie()
        return cookie
    }

    /**
     * 保存cookie，并将cookies持久化到本地,数据结构为
     * Url.host -> Cookie1.name,Cookie2.name,Cookie3.name
     * cookie_Cookie1.name -> CookieString
     * cookie_Cookie2.name -> CookieString
     */
    private fun saveCookie(url: HttpUrl, cookie: Cookie, name: String) {
        cookies[url.host]!![name] = cookie
        SPUtils.put(url.host, TextUtils.join(",", cookies[url.host]!!.keys))
        SPUtils.put(COOKIE_NAME_PREFIX + name, encodeCookie(SerializableHttpCookie(cookie)))
    }

    /** 将url的所有Cookie保存在本地 */
    override fun saveCookie(url: HttpUrl, cookie: List<Cookie>) {
        if (!cookies.containsKey(url.host)) {
            cookies[url.host] = ConcurrentHashMap<String, Cookie>()
        }
        for (cookie in cookie) {
            if (isCookieExpired(cookie)) {
                removeCookie(url, cookie)
            } else {
                saveCookie(url, cookie, getCookieToken(cookie))
            }
        }
    }

    override fun saveCookie(url: HttpUrl, cookie: Cookie) {
        if (!cookies.containsKey(url.host)) {
            cookies[url.host] = ConcurrentHashMap<String, Cookie>()
        }
        if (isCookieExpired(cookie)) {
            removeCookie(url, cookie)
        } else {
            saveCookie(url, cookie, getCookieToken(cookie))
        }
    }

    /** 根据当前url获取所有需要的cookie,只返回没有过期的cookie */
    override fun loadCookie(url: HttpUrl): List<Cookie> {
        var ret = mutableListOf<Cookie>()
        if (cookies.containsKey(url.host)) {
            var urlCookie = cookies[url.host]!!.values
            for (cookie in urlCookie) {
                if (isCookieExpired(cookie)) {
                    removeCookie(url, cookie)
                } else {
                    ret.add(cookie)
                }
            }
        }
        return ret
    }

    /** 获取所有的cookie */
    override fun getAllCookie(): List<Cookie> {
        val ret = mutableListOf<Cookie>()
        for (key in cookies.keys) {
            ret.addAll(cookies[key]!!.values)
        }
        return ret
    }

    override fun getCookie(url: HttpUrl): List<Cookie> {
        val ret = mutableListOf<Cookie>()
        val mapCookie = cookies[url.host]
        if (mapCookie != null) {
            ret.addAll(mapCookie.values)
        }
        return ret
    }

    /** 根据url移除当前的cookie */
    override fun removeCookie(url: HttpUrl, cookie: Cookie): Boolean {
        val name = getCookieToken(cookie)
        return if (cookies.containsKey(url.host) && cookies[url.host]!!.contains(name)) {
            cookies[url.host]!!.remove(name)
            if (SPUtils.contains(COOKIE_NAME_PREFIX + name)) {
                SPUtils.remove(COOKIE_NAME_PREFIX + name)
            }
            SPUtils.put(url.host, TextUtils.join(",", cookies[url.host]!!.keys))
            true
        } else {
            false
        }
    }

    override fun removeCookie(url: HttpUrl): Boolean {
        return if (cookies.containsKey(url.host)) {
            val cookieNames = cookies[url.host]!!.keys
            for (cookieName in cookieNames) {
                if (SPUtils.contains(COOKIE_NAME_PREFIX + cookieName)) {
                    SPUtils.remove(COOKIE_NAME_PREFIX + cookieName)
                }
            }
            true
        } else {
            false
        }
    }

    override fun removeAllCookie(): Boolean {
        for (key in cookies.keys) {
            for (cookieName in cookies[key]!!.keys) {
                if (SPUtils.contains(COOKIE_NAME_PREFIX + cookieName)) {
                    SPUtils.remove(COOKIE_NAME_PREFIX + cookieName)
                }
            }
        }
        cookies.clear()
        return true
    }
}