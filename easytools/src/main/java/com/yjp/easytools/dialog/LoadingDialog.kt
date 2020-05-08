package com.yjp.easytools.dialog

import android.app.ProgressDialog
import android.content.Context
import android.os.Handler
import android.util.Log

/**
 * 加载弹窗$
 * @author yjp
 * @date 2020/3/26 11:54
 */
object LoadingDialog {
    private var progressDialog: ProgressDialog? = null

    fun showLoading(context: Context, msg: String) {
        try {
            if (progressDialog == null) {
                progressDialog = ProgressDialog(context)
            } else if (progressDialog!!.isShowing) {
                return
            }
            progressDialog!!.isIndeterminate = true
            progressDialog!!.setCanceledOnTouchOutside(false)
            progressDialog!!.setMessage(msg)
            progressDialog!!.show()
            progressDialog!!.window!!.decorView.tag = System.currentTimeMillis()
        } catch (e: Exception) {
            Log.e("LoadingDialog", e.message);
        }
    }

    fun isShowing(): Boolean {
        try {
            if (progressDialog != null) {
                return progressDialog!!.isShowing
            }
        } catch (e: Exception) {
            Log.e("LoadingDialog", e.message);
        }
        return false
    }

    fun dismissLoading() {
        if (progressDialog != null && progressDialog!!.window != null) {
            val createTimeMillis: Long = progressDialog!!.window!!.decorView.tag as Long
            if (System.currentTimeMillis() - createTimeMillis < 1000) {
                Handler().postDelayed(Runnable {
                    try {
                        if (progressDialog != null) {
                            progressDialog!!.dismiss()
                            progressDialog = null
                        }
                    } catch (e: Exception) {
                        Log.e("LoadingDialog", e.message);
                    }
                }, 500)
            } else {
                try {
                    progressDialog!!.dismiss()
                    progressDialog = null
                } catch (e: Exception) {
                    Log.e("LoadingDialog", e.message);
                }
            }
        }
    }
}