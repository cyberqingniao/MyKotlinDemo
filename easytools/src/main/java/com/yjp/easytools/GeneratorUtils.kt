package com.yjp.easytools

import com.yjp.easytools.utils.DataUtils
import com.yjp.easytools.utils.StringUtils
import java.io.File
import java.io.FileOutputStream
import java.io.PrintWriter

/**
 * 项目自动构建器$
 * @author com.yjp
 * @date 2020/3/25 11:30
 */
class GeneratorUtils {
    companion object {
        //项目主路径
        private const val PATH_MAIN = "./app/src/main/"

        //项目代码存放路径
        private const val PATH_MAIN_CODE = "${PATH_MAIN}java/"

        //项目资源存放路径
        private const val PATH_MAIN_RES = "${PATH_MAIN}res/"

        //项目布局文件存放路径
        private const val PATH_MAIN_LAYOUT = "${PATH_MAIN_RES}layout/"

        //项目包名
        private const val PATH_PACKAGE = "com.yjp.mydemo.ui"

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
//            gu.isFileExists(File(PATH_EASYTOOLS_CODE, "/http"))
//            gu.isFileExists(File(PATH_EASYTOOLS_CODE, "/base"))
//            gu.isFileExists(File(PATH_EASYTOOLS_CODE, "/bus"))
//            gu.isFileExists(File(PATH_EASYTOOLS_CODE, "/databing"))
//            gu.isFileExists(File(PATH_EASYTOOLS_CODE, "/view"))
//            gu.isFileExists(File(PATH_EASYTOOLS_CODE, "/utils"))
//            gu.isFileExists(File(PATH_EASYTOOLS_CODE, "/adapter"))
//            gu.isFileExists(File(PATH_EASYTOOLS_CODE, "/db"))
//            gu.isFileExists(File(PATH_EASYTOOLS_CODE, "/ui"))

            //创建视图
            gu.generateFile("main", "Activity")
            gu.generateFile("home", "Fragment")
        }
    }

    fun generateFile(modelName: String, type: String) {

        val aName: String
        //ViewModel
        val vmName: String = StringUtils.upperFirstLetter(modelName) + "ViewModel.kt"
        //XML
        var xmlName: String
        //包名中的“.”替换成“/”
        var mPackage = PATH_PACKAGE
        mPackage = mPackage.replace(".", "/")

        isFileExists(File(PATH_MAIN_CODE + mPackage, modelName))
        if (type == "Activity") {
            //Activity
            aName = StringUtils.upperFirstLetter(modelName) + "Activity.kt"
            xmlName = if (StringUtils.isUpperCase(modelName)) {
                "activity_${StringUtils.toLowerCaseName(modelName)}.xml"
            } else {
                "activity_${modelName}.xml"
            }
            //生成Activity
            activityFile(aName, vmName, xmlName, modelName, mPackage)
        } else {
            //Fragment
            aName = StringUtils.upperFirstLetter(modelName) + "Fragment.kt"
            //XML
            xmlName = if (StringUtils.isUpperCase(modelName)) {
                "fragment_${StringUtils.toLowerCaseName(modelName)}.xml"
            } else {
                "fragment_${modelName}.xml"
            }
            //生成Fragment
            fragmentFile(aName, vmName, xmlName, modelName, mPackage)
        }

        //生成ViewModel
        viewModelFile(aName, vmName, modelName, mPackage)
        //生成XML
        xmlFile(vmName, xmlName, modelName)
    }

    /**
     * Fragment生成器
     */
    fun fragmentFile(
        aName: String,
        vmName: String,
        xmlName: String,
        modelName: String,
        mPackage: String
    ) {
        //生成文件
        val aFile = File(PATH_MAIN_CODE + mPackage, "/$modelName/$aName")
        fileExists(aFile)
        //写入文件初始内容
        val aSB = StringBuilder()
        aSB.append("package ${PATH_PACKAGE}.${modelName}\n\n")
        aSB.append("import android.os.Bundle\n")
        aSB.append("import com.yjp.easytools.base.BaseFragment\n")
        aSB.append("import com.yjp.mydemo.BR\n")
        aSB.append("import com.yjp.mydemo.R\n")
        aSB.append(
            "import com.yjp.mydemo.databinding.Fragment${StringUtils.upperFirstLetter(
                modelName
            )}Binding\n\n"
        )
        aSB.append("/**\n*\n* @author yjp\n* @date ${DataUtils.data2Str(System.currentTimeMillis())}\n*/\n")
        aSB.append(
            "class ${aName.substring(
                0,
                aName.indexOf(".")
            )}: BaseFragment<Fragment${StringUtils.upperFirstLetter(
                modelName
            )}Binding, ${vmName.substring(
                0,
                vmName.indexOf(".")
            )}>() {\n"
        )
        aSB.append("\toverride fun initContentView(saveInstanceState: Bundle?): Int {\n")
        aSB.append("\t\treturn R.layout.${xmlName.substring(0, xmlName.indexOf("."))}\n")
        aSB.append("\t\t}\n\n")
        aSB.append("\toverride fun initVariableId(): Int {\n")
        aSB.append(
            "\t\treturn BR.${StringUtils.lowerFirstLetter(
                vmName.substring(
                    0,
                    vmName.indexOf(".")
                )
            )}\n"
        )
        aSB.append("\t}\n")
        aSB.append("}")
        writeFile(aFile, aSB)
    }

    /**
     * Activity生成器
     */
    fun activityFile(
        aName: String,
        vmName: String,
        xmlName: String,
        modelName: String,
        mPackage: String
    ) {
        //生成文件
        val aFile = File(PATH_MAIN_CODE + mPackage, "/$modelName/$aName")
        fileExists(aFile)
        //写入文件初始内容
        val aSB = StringBuilder()
        aSB.append("package ${PATH_PACKAGE}.${modelName}\n\n")
        aSB.append("import android.os.Bundle\n")
        aSB.append("import com.yjp.easytools.base.BaseActivity\n")
        aSB.append("import com.yjp.mydemo.BR\n")
        aSB.append("import com.yjp.mydemo.R\n")
        aSB.append(
            "import com.yjp.mydemo.databinding.Activity${StringUtils.upperFirstLetter(
                modelName
            )}Binding\n\n"
        )
        aSB.append("/**\n*\n* @author yjp\n* @date ${DataUtils.data2Str(System.currentTimeMillis())}\n*/\n")
        aSB.append(
            "class ${aName.substring(
                0,
                aName.indexOf(".")
            )}: BaseActivity<Activity${StringUtils.upperFirstLetter(
                modelName
            )}Binding, ${vmName.substring(
                0,
                vmName.indexOf(".")
            )}>() {\n"
        )
        aSB.append("\toverride fun initContentView(saveInstanceState: Bundle?): Int {\n")
        aSB.append("\t\treturn R.layout.${xmlName.substring(0, xmlName.indexOf("."))}\n")
        aSB.append("\t\t}\n\n")
        aSB.append("\toverride fun initVariableId(): Int {\n")
        aSB.append(
            "\t\treturn BR.${StringUtils.lowerFirstLetter(
                vmName.substring(
                    0,
                    vmName.indexOf(".")
                )
            )}\n"
        )
        aSB.append("\t}\n")
        aSB.append("}")
        writeFile(aFile, aSB)
    }

    /**
     * ViewModel生成器
     */
    fun viewModelFile(aName: String, vmName: String, modelName: String, mPackage: String) {
        //生成文件
        val vmFile = File(PATH_MAIN_CODE + mPackage, "/$modelName/$vmName")
        fileExists(vmFile)
        //写入初始内容
        val vmSB = StringBuilder()
        vmSB.append("package ${PATH_PACKAGE}.${modelName}\n\n")
        vmSB.append("import android.app.Application\n")
        vmSB.append("import com.yjp.easytools.base.BaseViewModel\n\n")
        vmSB.append("/**\n*\n* @author yjp\n* @date ${DataUtils.data2Str(System.currentTimeMillis())}\n*/\n")
        vmSB.append(
            "class ${vmName.substring(
                0,
                vmName.indexOf(".")
            )}(application: Application) : BaseViewModel(application) {\n}"
        )
        writeFile(vmFile, vmSB)
    }

    /**
     * XMl生成器
     */
    fun xmlFile(vmName: String, xmlName: String, modelName: String) {
        //生成XML文件
        val xmlFile = File(PATH_MAIN_LAYOUT, xmlName)
        fileExists(xmlFile)
        //写入初始内容
        val xmlSB = StringBuilder()
        xmlSB.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n")
        xmlSB.append("<layout>\n")
        xmlSB.append("\t<data>\n")
        xmlSB.append("\t\t<variable\n")
        xmlSB.append(
            "\t\t\tname=\"${StringUtils.lowerFirstLetter(
                vmName.substring(
                    0,
                    vmName.indexOf(".")
                )
            )}\"\n"
        )
        xmlSB.append(
            "\t\t\ttype=\"${PATH_PACKAGE}.${modelName}.${vmName.substring(
                0,
                vmName.indexOf(".")
            )}\"/>\n"
        )
        xmlSB.append("\t</data>\n")
        xmlSB.append("\t<LinearLayout xmlns:android=\"http://schemas.android.com/apk/res/android\"\n")
        xmlSB.append("\t\tandroid:layout_width=\"match_parent\"\n")
        xmlSB.append("\t\tandroid:layout_height=\"match_parent\"\n")
        xmlSB.append("\t\tandroid:orientation=\"vertical\">\n\n")
        xmlSB.append("\t</LinearLayout>\n")
        xmlSB.append("</layout>")
        writeFile(xmlFile, xmlSB)
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
        if (isFileExists(File(path))) {
            file = File(path, fileName)
            writeFile(file, content)
        } else {
            throw RuntimeException("创建文件目录失败")
        }
    }

    fun writeFile(file: File, content: StringBuilder) {
        var fos: FileOutputStream? = null
        var pw: PrintWriter? = null
        try {
            fos = FileOutputStream(file)
            pw = PrintWriter(fos)
            pw.print(content)
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

    fun fileExists(file: File): Boolean {
        if (!file.exists()) {
            return file.createNewFile()
        }
        return true
    }
}