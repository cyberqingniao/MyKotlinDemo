package com.yjp.easytools.utils

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson

/**
 * SharePreferences存储$
 * @author yjp
 * @date 2020/3/31 10:24
 */
object SPUtils {
    private const val SP_NAME = "YJP_SP_UTILS"
    private var sp: SharedPreferences

    init {
        sp = Utils.context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE)
    }

    fun put(key: String, value: Any) {
        when (value) {
            is String -> {
                sp.edit().putString(key, value).apply()
            }
            is Int -> {
                sp.edit().putInt(key, value).apply()
            }
            is Long -> {
                sp.edit().putLong(key, value).apply()
            }
            is Boolean -> {
                sp.edit().putBoolean(key, value).apply()
            }
            is Float -> {
                sp.edit().putFloat(key, value).apply()
            }
            else -> {
                throw RuntimeException("不能存储该类型的Value")
            }
        }
    }

    fun getString(key: String): String {
        return getString(key, "")
    }

    fun getString(key: String, defaultValue: String): String {
        return sp.getString(key, defaultValue)!!
    }

    fun getInt(key: String): Int {
        return getInt(key, -1)
    }

    fun getInt(key: String, defaultValue: Int): Int {
        return sp.getInt(key, defaultValue)
    }

    fun getLong(key: String): Long {
        return getLong(key, -1)
    }

    fun getLong(key: String, defaultValue: Long): Long {
        return sp.getLong(key, defaultValue)
    }

    fun getFloat(key: String): Float {
        return getFloat(key, -1F)
    }

    fun getFloat(key: String, defaultValue: Float): Float {
        return sp.getFloat(key, defaultValue)
    }

    fun getBoolean(key: String): Boolean {
        return getBoolean(key, false)
    }

    fun getBoolean(key: String, defaultValue: Boolean): Boolean {
        return sp.getBoolean(key, defaultValue)
    }

    fun getAll(): Map<String, Any?>? {
        return sp.all
    }

    fun contains(key: String): Boolean {
        return sp.contains(key)
    }

    fun remove(key: String) {
        sp.edit().remove(key).apply()
    }

    fun clear() {
        sp.edit().clear().apply()
    }

    /**
     * 根据Key读取SP中存储的Json数据
     * @param key
     * @param clazz : 接收实体类
     */
    fun <T> getJson(key: String, clazz: Class<T>): T? {
        if (contains(key)) {
            val json = getString(key)
            if (!StringUtils.isEmpty(json)) {
                return Gson().fromJson(json, clazz)
            }
        }
        return null
    }

}