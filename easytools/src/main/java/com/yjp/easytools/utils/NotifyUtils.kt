package com.yjp.easytools.utils

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.net.Uri

/**
 * 通知栏工具$
 * @author yjp
 * @date 2020/3/31 17:48
 */
object NotifyUtils {
    private var nManager: NotificationManager? = null
    private var notifyId: Int = 0

    /**
     * 带跳转通知
     */
    fun notifyMessage(
        title: String,
        content: String,
        smallIconResId: Int,
        intent: PendingIntent
    ): Int {
        return notifyMessage(title, content, smallIconResId, true, null, intent)
    }

    /**
     * 带震动、铃声的通知
     */
    fun notifyMessage(title: String, content: String, smallIconResId: Int): Int {
        return notifyMessage(title, content, smallIconResId, true, null, null)
    }

    /**
     * 不带提示的通知
     * */
    fun notifyMessageNo(title: String, content: String, smallIconResId: Int): Int {
        return notifyMessage(title, content, smallIconResId, false, null, null)
    }

    /**
     * 不带提示，但有跳转的通知
     */
    fun notifyMessageNo(
        title: String,
        content: String,
        smallIconResId: Int,
        intent: PendingIntent
    ): Int {
        return notifyMessage(title, content, smallIconResId, false, null, intent)
    }

    /**
     * 发送通知消息
     * @param title : 标题
     * @param content : 内容
     * @param smallIconResId : 图标
     * @param isVibration : 是否震动
     * @param voice : 铃声
     * @param intent : 点击跳转
     */
    fun notifyMessage(
        title: String,
        content: String,
        smallIconResId: Int,
        isVibration: Boolean,
        voice: Uri?,
        intent: PendingIntent?
    ): Int {
        val context = init()
        val build = Notification.Builder(context)
        build.setContentTitle(title)
        build.setContentText(content)
        build.setWhen(System.currentTimeMillis())
        build.setSmallIcon(smallIconResId)
        if (isVibration && voice != null) {
            build.setDefaults(Notification.DEFAULT_VIBRATE)
            build.setSound(voice)
        } else if (isVibration && voice == null) {
            build.setDefaults(Notification.DEFAULT_VIBRATE or Notification.DEFAULT_SOUND)
        } else if (voice != null) {
            build.setSound(voice)
        }
        if (intent != null) {
            build.setContentIntent(intent)
        }
        return send(build.build())
    }

    /**
     * 清除所有通知
     */
    fun clear() {
        nManager?.cancelAll()
    }

    /**
     * 删除指定Id的通知
     */
    fun remover(id: Int) {
        nManager?.cancel(id)
    }

    /**
     * 初始化通知栏管理器
     */
    private fun init(): Context {
        val context = Utils.context
        if (nManager == null) {
            nManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        }
        return context
    }

    /**
     * 发送通知
     */
    private fun send(n: Notification): Int {
        notifyId++
        nManager?.notify(notifyId, n)
        return notifyId
    }
}