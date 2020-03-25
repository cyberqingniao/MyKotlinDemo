package com.yjp.easytools

import java.io.File
import java.io.FileOutputStream
import java.io.PrintWriter

/**
 * 项目自动构建器$
 * @author yjp
 * @date 2020/3/25 11:30
 */
class GeneratorUtils {
    companion object {
        //项目主路径
        private const val PATH_MAIN = "./app/src/main"
        //项目代码存放路径
        private const val PATH_MAIN_CODE = "$PATH_MAIN/java"

        //easytools路径
        private const val PATH_EASYTOOLS = "./easytools/src/main"
        //easytools代码路径
        private const val PATH_EASYTOOLS_CODE = "$PATH_EASYTOOLS/java/com/yjp/easytools"
        //easytools资源存放路径
        private const val PATH_EASYTOOLS_VALUES = "$PATH_EASYTOOLS/res/values"

        private const val DIMEN_NAME = "dimens.xml"
        private const val DIMEN_DP = "<dimen name=\"dp_%1s\">%2sdp</dimen>\n"
        private const val DIMEN_SP = "<dimen name=\"sp_%1s\">%2ssp</dimen>\n"

        @JvmStatic
        fun main(args: Array<String>) {
            val gu = GeneratorUtils()
//            gu.generateDimen()
            gu.isFileExists(File(PATH_EASYTOOLS_CODE, "/http"))
            gu.isFileExists(File(PATH_EASYTOOLS_CODE, "/base"))
            gu.isFileExists(File(PATH_EASYTOOLS_CODE, "/bus"))
            gu.isFileExists(File(PATH_EASYTOOLS_CODE, "/databing"))
            gu.isFileExists(File(PATH_EASYTOOLS_CODE, "/view"))
            gu.isFileExists(File(PATH_EASYTOOLS_CODE, "/utils"))
            gu.isFileExists(File(PATH_EASYTOOLS_CODE, "/adapter"))
            gu.isFileExists(File(PATH_EASYTOOLS_CODE, "/db"))
            gu.isFileExists(File(PATH_EASYTOOLS_CODE, "/ui"))
        }

    }

    /**
     * 创建Dimen
     * */
    fun generateDimen() {
        val sb = StringBuilder()
        sb.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n")
        sb.append("<resources>\n")
        sb.append("    <!--  字号  -->\n")
        sb.append(intervalDimen(DIMEN_SP, 8, 100));
        sb.append("    <!--  间距  -->\n")
        sb.append(intervalDimen(DIMEN_DP, 0, 1080));
        sb.append("</resources>")
        outFile(PATH_EASYTOOLS_VALUES, DIMEN_NAME, sb)
    }

    /**
     * 根据区间生成
     *
     * @param format : 生成格式
     * @param start  : 起始值
     * @param end    : 结束值
     * @return String 生成后的字符串
     */
    fun intervalDimen(format: String, start: Int, end: Int): String {
        val sb = StringBuilder()
        for (i in start until end) {
            sb.append("    ")
            val key = if (i == 0) {
                "05"
            } else {
                i.toString()
            }
            val value = if (i == 0) {
                "0.5"
            } else {
                i.toString()
            }
            sb.append(stringFormat(format, key, value))
        }
        return sb.toString()
    }

    /**
     * 根据指定的值生成
     *
     * @param format : 生成格式
     * @param args   : float值组
     * @return String 生成后的字符串
     */
    fun specifyDimen(format: String, vararg args: Float): String {
        val sb = StringBuilder()
        for (f in args) {
            sb.append("    ")
            var value = f.toString()
            val valueH = value.substring(value.indexOf(".") + 1)
            val valueQ = value.substring(0, value.indexOf("."))
            value = if (valueH.toInt() != 0) {
                if (valueQ.toInt() == 0) {
                    value.replace(".", "")
                } else {
                    value.replace(".", "_")
                }
            } else {
                value.substring(0, value.indexOf("."))
            }
            sb.append(stringFormat(format, value, f.toString()))
        }
        return sb.toString()
    }

    /**
     * 根据指定格式生成
     *
     * @param format : 字符串格式
     * @param arg
     * @return String 生成后的字符串
     */
    fun stringFormat(format: String, key: String, value: String): String {
        return String.format(format, key, value)
    }

    /**
     * 输出文件
     * @param path: 文件目录
     * @param fileName: 文件名称
     * @param content : 文件内容
     * */
    fun outFile(path: String, fileName: String, content: StringBuilder) {
        var file: File? = null
        var fos: FileOutputStream? = null
        var pw: PrintWriter? = null
        try {
            if (isFileExists(File(path))) {
                file = File(path, fileName)
                fos = FileOutputStream(file)
                pw = PrintWriter(fos)
                pw.print(content)
            } else {
                throw RuntimeException("创建文件目录失败")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            pw?.close()
            fos?.close()
        }
    }

    fun isFileExists(file: File): Boolean {
        if (!file.exists()) {
            return file.mkdirs()
        }
        return true
    }
}