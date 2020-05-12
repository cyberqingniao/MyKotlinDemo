package com.yjp.easytools.utils

import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat
import android.os.Environment
import java.io.*

/**
 * 文件工具类$
 * @author yjp
 * @date 2020/3/31 18:25
 */
object FileUtil {
    private const val TAG = "FileUtils"
    private const val fileName = "SNsl"
    private var SDPATH = Environment.getExternalStorageDirectory().absoluteFile
    private var path: File? = null

    //Download APK path
    private const val DOWNLOAD_DIR = "/$fileName/apk/"

    //Image Path
    private const val IMG_PATH = "/$fileName/images/"

    fun init() {
        if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
            path = File("$SDPATH/$fileName/")
            if (!path!!.exists()) {
                if (!path!!.mkdirs()) {
                    println("外部存储-无法创建目录\t" + path!!.path);
                }
            }
        } else {
            path = File(Utils.context.filesDir, "/$fileName/")
            if (!path!!.exists()) {
                if (!path!!.mkdirs()) {
                    println("私有存储-无法创建目录\t" + path!!.path);
                }
            }
        }
        if (!path!!.exists()) {
            path = Utils.context.getExternalFilesDir(fileName)
            if (!path!!.exists()) {
                if (!path!!.mkdirs()) {
                    println("创建内部目录失败\t" + path!!.path);
                }
            }
        }
        if (!path!!.exists()) {
            println("创建文件存储目录失败");
        } else {
            println("创建文件存储目录成功");
        }
    }

    /**
     * 下载安装包保存路径
     */
    fun getAPKPath(): File {
        val file = File(path.toString() + DOWNLOAD_DIR)
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
        val file = File(path.toString() + IMG_PATH)
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

    /**
     * 保存Bitmap
     * @param bm：Bitmap资源
     * @param picName : 文件名
     * @param format : 保存格式  Bitmap.CompressFormat.JPEG
     * @param
     */
    fun saveBitmap(bm: Bitmap, picName: String, format: CompressFormat): Boolean {
        return try {
            val f = File(getIMGPath(), picName)
            if (f.exists()) {
                f.delete()
            }
            val out = FileOutputStream(f)
            bm.compress(format, 90, out)
            out.flush()
            out.close()
            true
        } catch (e: FileNotFoundException) {
            println("保存Bitmap报错\nFileNotFoundException ${e.message}")
            false
        } catch (e: IOException) {
            println("保存Bitmap报错\nIOException ${e.message}")
            false
        }
    }

    /**
     * 创建文件到SD卡中
     */
    @Throws(IOException::class)
    fun createSDDir(dirName: String): File {
        val dir = File(path.toString() + DOWNLOAD_DIR + dirName)
        if (Environment.getExternalStorageState() ==
            Environment.MEDIA_MOUNTED
        ) {
            println("createSDDir:" + dir.absolutePath)
            println("createSDDir:" + dir.mkdir())
        }
        return dir
    }


    /**
     * 删除指定路径的文件
     */
    fun delFile(path: String) {
        val file = File(path)
        if (file.isFile || file.exists()) {
            file.delete()
        }
    }

    //删除文件夹和文件夹里面的文件
    fun deleteDir() {
        val dir = File(path!!.absolutePath + DOWNLOAD_DIR)
        if (!dir.exists() || !dir.isDirectory) return
        dir.listFiles()?.forEach { file ->
            if (file.isFile) file.delete() // 删除所有文件
            else if (file.isDirectory) deleteDir() // 递规的方式删除文件夹
        }
        dir.delete() // 删除目录本身
    }

    /**
     * 删除文件夹和文件夹里面的文件
     */
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
    fun writeFile(
        inputStream: InputStream,
        file: File
    ): Boolean {
        var outputStream: OutputStream? = null
        return try {
            outputStream = FileOutputStream(file)
            var read = 0
            val bytes = ByteArray(1024)
            while (inputStream.read(bytes).also { read = it } != -1) {
                outputStream.write(bytes, 0, read)
            }
            true
        } catch (e: IOException) {
            e.printStackTrace()
            false
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

    /**
     * 读取assets中的文件
     */
    fun readAssetsFile(fileName: String, savePath: String): File? {
        try {
            val f = File(savePath)
            if (!f.exists()) {
                if (!f.createNewFile()) {
                    println("读取Assets文件时创建本地存储文件失败");
                }
            }
            var cmd = "chmod 777" + f.absolutePath
            Runtime.getRuntime().exec(cmd)
            cmd = "chmod 777" + f.parent
            Runtime.getRuntime().exec(cmd)
            cmd = "chmod 777" + File(f.parent!!).parent
            Runtime.getRuntime().exec(cmd)
            val mIS = Utils.context.assets.open(fileName)
            writeFile(mIS, f)
            return f
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }
}