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

    //存储父目录
    private var path: File? = null

    //Download APK path
    private const val DOWNLOAD_DIR = "/apk/"

    //Image Path
    private const val IMG_PATH = "/images/"

    //Other file path
    private const val OTHER_PATH = "/other/"

    /**
     * 初始化存储父目录
     */
    fun init(): File {
        if (path == null) {
            //获取存储父目录
            path = if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
                //外部私有目录，不需要权限，跟随软件卸载而删除
                Utils.context.getExternalFilesDir(null)
            } else {
                //内部私有目录，不需要权限，跟随软件卸载而删除
                Utils.context.filesDir
            }
        }
        return path!!
    }

    /**
     * 获取CaChe目录
     * @return File
     */
    fun getCachePath(): File {
        val file = if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
            //外部私有目录，不需要权限，跟随软件卸载而删除
            Utils.context.getExternalFilesDir(null)
        } else {
            //内部私有目录，不需要权限，跟随软件卸载而删除
            Utils.context.filesDir
        }
        return file!!
    }

    /**
     * 下载安装包保存路径
     * @return File
     */
    fun getAPKPath(): File {
        val file = File(init(), DOWNLOAD_DIR)
        if (!file.exists()) {
            if (file.mkdirs()) {
                println("APK安装包存储目录创建成功")
            } else {
                println("APK安装包存储目录创建失败")
            }
        }
        return file
    }

    /**
     * 图片保存路径
     *
     * @return File
     */
    fun getIMGPath(): File {
        val file = File(init(), IMG_PATH)
        if (!file.exists()) {
            if (file.mkdirs()) {
                println("图片存储目录创建成功")
            } else {
                println("图片存储目录创建失败")
            }
        }
        return file
    }

    /**
     * 其他文件存储目录
     * @return File
     */
    fun getOtherPath(): File {
        val file = File(init(), OTHER_PATH)
        if (!file.exists()) {
            if (file.mkdirs()) {
                println("其他文件存储目录创建成功")
            } else {
                println("其他文件存储目录创建失败")
            }
        }
        return file
    }

    /**
     * 判断文件是否存在
     * 不存在则创建
     * @return true 表示一定存在，false 表示不存在
     */
    fun isFileExists(file: File): Boolean {
        if (!file.exists()) {
            return file.mkdirs()
        }
        return true
    }

    /**
     * 删除指定路径的文件
     * @param path : 文件路径
     * @return true 删除成功，或不存在文件。false 表示文件删除失败
     */
    fun deletePath(path: String): Boolean {
        val file = File(path)
        if (file.isFile || file.exists()) {
            return file.delete()
        }
        return true
    }

    /**
     * 删除存储目录下的文件夹和文件夹里面的文件
     * 递归删除
     * 路径是文件存储目录 path
     */
    //删除文件夹和文件夹里面的文件
    fun deleteCache(): Boolean {
        if (path != null) {
            return deleteFile(path!!)
        }
        return true
    }

    /**
     * 删除文件夹和文件夹里面的文件
     */
    fun deleteFile(dir: File): Boolean {
        var isSuccess = true
        //判断文件是否存在
        if (dir.exists()) {
            //判断文件是否是文件夹
            if (dir.isDirectory) {
                //如果是文件夹则递归删除
                dir.listFiles()?.forEach { file ->
                    //判断是否是文件
                    if (file.isFile) {
                        // 删除文件
                        if (!isSuccess.let { file.delete() }) {
                            println("删除失败，路径->${file.path}")
                        }
                    } else if (file.isDirectory) {
                        // 递规的方式删除文件夹下的文件
                        deleteFile(file)
                    }
                }
            } else {
                isSuccess = dir.delete()
            }
        }
        return isSuccess
    }

    /**
     * 获取文件存储名，存在重复的名的时候则在后面+1
     * @param path
     * @return
     */
    fun getSaveFile(path: File): File {
        //判断文件是否存在
        return if (path.exists()) {
            //存在则重新命名
            var fn = path.name
            val suffix = fn.substring(fn.indexOf("."))
            fn = fn.substring(0, fn.indexOf("."))
            if (fn.endsWith(')')) {
                val num = StringUtils.getNumber(fn[fn.length - 2].toString())
                fn = fn.replace("(${num})", "(${(num + 1)})")
            } else {
                fn += "(1)"
            }
            getSaveFile(File(path.parent, fn + suffix))
        } else {
            //不存在则创建文件
            val parentFile = path.parentFile
            if (parentFile != null) {
                if (parentFile.exists()) {
                    path
                } else {
                    if (parentFile.mkdirs()) {
                        val file = File(parentFile, path.name)
                        if (!file.exists()) {
                            if (!file.createNewFile()) {
                                println("创建文件失败->" + file.absolutePath)
                            }
                        }
                        file
                    } else {
                        throw RuntimeException("创建文件目录失败->" + parentFile.absolutePath)
                    }
                }
            } else {
                throw RuntimeException("文件路径异常，不存在父目录")
            }
        }
    }

    /**
     * 根据byte数据保存成文件
     *
     * @param path
     * @param fileName
     * @param data
     * @return
     */
    fun writeFile(path: String, fileName: String, data: ByteArray): Boolean {
        return writeFile(File(path, fileName), data)
    }

    /**
     * 根据byte数据保存成文件
     *
     * @param filePath
     * @param data
     * @return
     */
    fun writeFile(filePath: File, data: ByteArray): Boolean {
        var bufferedOutputStream: BufferedOutputStream? = null
        var out: FileOutputStream? = null
        return try {
            //得到文件
            val file = getSaveFile(filePath)
            //获取文件流
            out = FileOutputStream(file, true)
            //获取输出流
            bufferedOutputStream = BufferedOutputStream(out)
            //些人ByteArray数据
            bufferedOutputStream.write(data)
            true
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
            false
        } catch (e: IOException) {
            e.printStackTrace()
            false
        } finally {
            //关闭资源
            bufferedOutputStream?.close()
            bufferedOutputStream = null
            out?.close()
            out = null
        }
    }

    /**
     * 把流写成文件存储
     *
     * @param path
     * @param fileName
     * @param inputStream
     */
    fun writeFile(
        path: String,
        fileName: String,
        inputStream: InputStream
    ): Boolean {
        return writeFile(File(path, fileName), inputStream)
    }

    /**
     * 把流写成文件存储
     *
     * @param inputStream
     * @param filePath
     */
    fun writeFile(
        filePath: File,
        inputStream: InputStream
    ): Boolean {
        var outputStream: OutputStream? = null
        return try {
            val file = getSaveFile(filePath)
            outputStream = FileOutputStream(file)
            var read: Int
            val bytes = ByteArray(1024)
            while (inputStream.read(bytes).also { read = it } != -1) {
                outputStream.write(bytes, 0, read)
            }
            true
        } catch (e: IOException) {
            e.printStackTrace()
            false
        } finally {
            inputStream.close()
            outputStream?.close()
            outputStream = null
        }
    }

    /**
     * 写入文件
     * 字符串
     * @param path : 存储路径
     * @param fileName : 存储文件名
     * @param content : 存储内容
     */
    fun writeFile(path: String, fileName: String, content: String): Boolean {
        return writeFile(File(path, fileName), content)
    }

    /**
     * 写入文件
     * @param filePath : 文件存储路径
     * @param content : 存储内容
     */
    fun writeFile(
        filePath: File,
        content: String
    ): Boolean {
        var fos: FileOutputStream? = null
        var osw: OutputStreamWriter? = null
        var bw: BufferedWriter? = null
        return try {
            val file = getSaveFile(filePath)
            fos = FileOutputStream(file)
            osw = OutputStreamWriter(fos)
            bw = BufferedWriter(osw)
            bw.write(content)
            //刷新流
            bw.flush()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        } finally {
            //关闭资源
            bw?.close()
            bw = null
            osw?.close()
            osw = null
            fos?.close()
            fos = null
        }
    }

    /**
     * 保存Bitmap
     * @param path : 文件路径
     * @param fileName : 文件名
     * @param bm：Bitmap资源
     * @param format : 保存格式  Bitmap.CompressFormat.JPEG
     * @param
     */
    fun writeFile(path: String, fileName: String, bm: Bitmap, format: CompressFormat): Boolean {
        return writeFile(File(path, fileName), bm, format)
    }

    /**
     * 保存Bitmap
     * @param picName : 文件路径
     * @param bm：Bitmap资源
     * @param format : 保存格式  Bitmap.CompressFormat.JPEG
     * @param
     */
    fun writeFile(filePath: String, bm: Bitmap, format: CompressFormat): Boolean {
        return writeFile(File(filePath), bm, format)
    }

    /**
     * 保存Bitmap
     * @param picName : 文件路径
     * @param bm：Bitmap资源
     * @param format : 保存格式  Bitmap.CompressFormat.JPEG
     * @param
     */
    fun writeFile(filePath: File, bm: Bitmap, format: CompressFormat): Boolean {
        var out: FileOutputStream? = null
        return try {
            val f = getSaveFile(filePath)
            out = FileOutputStream(f)
            bm.compress(format, 90, out)
            out.flush()
            true
        } catch (e: FileNotFoundException) {
            println("保存Bitmap报错\nFileNotFoundException ${e.message}")
            false
        } catch (e: IOException) {
            println("保存Bitmap报错\nIOException ${e.message}")
            false
        } finally {
            out?.close()
            out = null
        }
    }

    /**
     * 读取目录下所有文件
     */
    fun readFile(path: String): List<File> {
        val files = mutableListOf<File>()
        val file = File(path)
        if (file.exists()) {
            files.addAll(readFile(file))
        }
        return files
    }

    /**
     * 读取目录下所有文件
     */
    fun readFile(file: File): List<File> {
        val files = mutableListOf<File>()
        if (file.isFile) {
            files.add(file)
        } else if (file.isDirectory) {
            val tempFile = file.listFiles()
            tempFile?.forEach {
                if (it.isFile) {
                    files.add(it)
                } else if (it.isDirectory) {
                    files.addAll(readFile(it))
                }
            }
        }
        return files
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
            writeFile(f, mIS)
            return f
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }
}