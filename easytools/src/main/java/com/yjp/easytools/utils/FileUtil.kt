package com.yjp.easytools.utils

import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat
import android.os.Environment
import android.util.Log
import java.io.*

/**
 * 文件工具类$
 * @author yjp
 * @date 2020/3/31 18:25
 */
object FileUtil {
    private const val TAG = "FileUtils"
    private var SDPATH = Environment.getExternalStorageDirectory().absoluteFile
    private var path: File? = null
    private const val fileName = "SNsl"

    //Download APK
    private const val DOWNLOAD_DIR = "/$fileName/apk/"
    private const val IMG_PATH = "/$fileName/images/"

    fun init() {
        if (path == null) {
            path = File("$SDPATH/$fileName/")
        }
        if (!path!!.exists()) {
            path!!.mkdirs()
        }
    }

    /**
     * 下载安装包保存路径
     */
    fun getAPKPath(): File {
        val file = File(SDPATH.toString() + DOWNLOAD_DIR)
        if (!file.exists()) {
            file.mkdirs()
        }
        return file
    }

    /**
     * 图片保存路径
     *
     * @return File
     */
    fun getIMGPath(): File {
        val file = File(SDPATH.toString() + IMG_PATH)
        if (!file.exists()) {
            file.mkdirs()
        }
        return file
    }

    /**
     * 根据byte数据保存成文件
     *
     * @return
     */
    fun saveFileByByteData(data: ByteArray, fileName: String) {
        var bufferedOutputStream: BufferedOutputStream? = null
        try {
            val file = File(path, fileName)
            if (file.exists()) {
                file.delete()
            }
            val out = FileOutputStream(file, true)
            bufferedOutputStream = BufferedOutputStream(out)
            bufferedOutputStream.write(data)
            bufferedOutputStream.close()
            out.close()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    //保存图片
    fun saveBitmap(bm: Bitmap, picName: String, format: CompressFormat): Int {
        try {
            val f = File(getIMGPath(), picName)
            if (f.exists()) {
                f.delete()
            }
            val out = FileOutputStream(f)
            bm.compress(format, 90, out)
            out.flush()
            out.close()
            Log.d(TAG, "success")
            return 1
        } catch (e: FileNotFoundException) {
            Log.e(TAG, "FileNotFoundException $e")
        } catch (e: IOException) {
            Log.e(TAG, "IOException $e")
        }
        return 0
    }

    @Throws(IOException::class)
    fun createSDDir(dirName: String): File {
        val dir = File(SDPATH.toString() + DOWNLOAD_DIR + dirName)
        if (Environment.getExternalStorageState() ==
            Environment.MEDIA_MOUNTED
        ) {
            println("createSDDir:" + dir.absolutePath)
            println("createSDDir:" + dir.mkdir())
        }
        return dir
    }


    //删除文件
    fun delFile(fileName: String) {
        val file = File(SDPATH.toString() + DOWNLOAD_DIR + fileName)
        if (file.isFile || file.exists()) {
            file.delete()
        }
    }

    //删除文件夹和文件夹里面的文件
    fun deleteDir() {
        val dir = File(SDPATH.toString() + DOWNLOAD_DIR)
        if (!dir.exists() || !dir.isDirectory) return
        dir.listFiles()?.forEach { file ->
            if (file.isFile) file.delete() // 删除所有文件
            else if (file.isDirectory) deleteDir() // 递规的方式删除文件夹
        }
        dir.delete() // 删除目录本身
    }

    //删除文件夹和文件夹里面的文件
    fun deleteFile(dir: File) {
        if (!dir.exists() || !dir.isDirectory) return
        dir.listFiles()?.forEach { file ->
            if (file.isFile) file.delete() // 删除所有文件
            else if (file.isDirectory) deleteDir() // 递规的方式删除文件夹
        }
    }


    /**
     * 把流写成文件存储
     *
     * @param inputStream
     * @param file
     */
    fun inputStreamToFile(
        inputStream: InputStream,
        file: File
    ) {
        var outputStream: OutputStream? = null
        try {
            outputStream = FileOutputStream(file)
            var read = 0
            val bytes = ByteArray(1024)
            while (inputStream.read(bytes).also { read = it } != -1) {
                outputStream.write(bytes, 0, read)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            try {
                inputStream.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            if (outputStream != null) {
                try {
                    outputStream.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }
}