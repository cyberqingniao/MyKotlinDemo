package com.yjp.easytools.http.interceptor.logging

/**
 * $
 * @author yjp
 * @date 2020/4/1 14:31
 */
enum class Level {
    /**
     * No logs.
     */
    NONE,
    /**
     *
     * Example:
     * <pre>`- URL
     * - Method
     * - Headers
     * - Body
    `</pre> *
     */
    BASIC,
    /**
     *
     * Example:
     * <pre>`- URL
     * - Method
     * - Headers
    `</pre> *
     */
    HEADERS,
    /**
     *
     * Example:
     * <pre>`- URL
     * - Method
     * - Body
    `</pre> *
     */
    BODY
}
