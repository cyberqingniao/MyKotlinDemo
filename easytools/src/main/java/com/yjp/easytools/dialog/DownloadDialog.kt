package com.yjp.easytools.dialog

import android.app.ProgressDialog
import android.content.Context
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody
import java.io.*

/**
 * 下载弹窗$
 * @author yjp
 * @date 2020/5/8 16:54
 */
class DownloadDialog(
    //上下文环境
    private var mContext: Context,
    //下载弹窗标题
    private var title: String,
    //文件存储路径
    private var file: File,
    //下载回调
    private var listener: DownloadListener
) {
    //下载弹窗
    private var downloadProgress: ProgressDialog = ProgressDialog(mContext)

    //下载总长度
    private var mTotalLength: Long = 0

    init {
        //设置下载弹窗样式
        downloadProgress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL)
        //设置标题
        downloadProgress.setTitle(title)
    }

    /**
     * 下载文件
     * @param observable : API
     */
    fun download(observable: Observable<ResponseBody>) {
        observable.subscribeOn(Schedulers.io())
            .unsubscribeOn(Schedulers.io())
            .map {
                mTotalLength = it.contentLength()
                downloadProgress.progress = 0
                downloadProgress.show()
                it.byteStream()
            }
            .observeOn(Schedulers.computation())
            .doOnNext {
                write(it, file)
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<InputStream> {
                override fun onComplete() {
                    listener.onSuccess(file)
                    downloadProgress.dismiss()
                }

                override fun onSubscribe(d: Disposable) {

                }

                override fun onNext(t: InputStream) {

                }

                override fun onError(e: Throwable) {
                    listener.onFail(e.message ?: "下载出错")
                    downloadProgress.dismiss()
                }

            })
    }

    /**
     * 写入文件
     */
    private fun write(inputStream: InputStream, file: File) {
        if (file.exists()) {
            file.delete()
        }
        try {
            val fos = FileOutputStream(file)
            val b = ByteArray(1024)
            var len = 0
            var total = 0
            var lastProgress = 0
            var progress = 0
            while (inputStream.read(b).also { len = it } != -1) {
                fos.write(b, 0, len)
                total += len
                progress = (total * 100 / mTotalLength) as Int
                if (progress > 0 && progress != lastProgress) {
                    lastProgress = progress
                    downloadProgress.progress = progress
                }
            }
            inputStream.close()
            fos.close()
        } catch (e: FileNotFoundException) {
            listener.onFail(e.message ?: "FileNotFoundException")
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
            listener.onFail(e.message ?: "IOException")
        }
    }
}

interface DownloadListener {
    fun onFail(errorInfo: String)
    fun onSuccess(file: File)
}