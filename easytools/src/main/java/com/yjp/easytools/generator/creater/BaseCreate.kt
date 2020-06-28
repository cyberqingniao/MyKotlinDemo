package com.yjp.easytools.generator.creater

import com.yjp.easytools.utils.DataUtils
import com.yjp.easytools.utils.FileUtil
import com.yjp.easytools.utils.StringUtils
import com.yjp.easytools.utils.Utils
import java.io.File

/**
 * 自动生成父类$
 * @author yjp
 * @date 2020/6/23 9:17
 */
open class BaseCreate {

    companion object {

        //项目主路径
        private const val PATH_MAIN = "./app/src/main/"

        //项目代码存放路径
        const val PATH_MAIN_CODE = "${PATH_MAIN}java/"

        //项目资源存放路径
        const val PATH_MAIN_RES = "${PATH_MAIN}res/"

        //项目布局文件存放路径
        const val PATH_MAIN_LAYOUT = "${PATH_MAIN_RES}layout/"

        //项目包名
        const val PATH_PACKAGE = "com.yjp.mydemo"

        //easytools路径
        private const val PATH_EASYTOOLS = "./easytools/src/main"

        //easytools Values 存放路径
        const val PATH_EASYTOOLS_VALUES = "${PATH_EASYTOOLS}/res/values"
    }

    /**
     * 生成View
     */
    open fun createView(
        filePath: File,
        aName: String,
        vmName: String?,
        xmlName: String,
        modelName: String,
        mPackage: String
    ){}

    /**
     * 生成Layout
     */
    fun createLayout(
        vmName: String?,
        xmlName: String,
        modelName: String
    ) {
        //生成XML文件
        val layoutFile = File(PATH_MAIN_LAYOUT, xmlName)
        //写入初始内容
        val layout = StringBuilder()
        layout.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n")
        layout.append("<layout>\n")
        if (!Utils.isEmpty(vmName)) {
            layout.append("\t<data>\n")
            layout.append("\t\t<variable\n")
            layout.append(
                "\t\t\tname=\"${StringUtils.lowerFirstLetter(
                    vmName!!.substring(
                        0,
                        vmName.indexOf(".")
                    )
                )}\"\n"
            )
            layout.append(
                "\t\t\ttype=\"${PATH_PACKAGE}.ui.${modelName}.${vmName.substring(
                    0,
                    vmName.indexOf(".")
                )}\"/>\n"
            )
            layout.append("\t</data>\n")
        }
        layout.append("\t<LinearLayout xmlns:android=\"http://schemas.android.com/apk/res/android\"\n")
        layout.append("\t\tandroid:layout_width=\"match_parent\"\n")
        layout.append("\t\tandroid:layout_height=\"match_parent\"\n")
        layout.append("\t\tandroid:orientation=\"vertical\">\n\n")
        layout.append("\t</LinearLayout>\n")
        layout.append("</layout>")
        if (FileUtil.writeFile(layoutFile, layout.toString())) {
            println("Layout创建成功")
        } else {
            println("Layout创建失败")
        }
    }

    /**
     * 生成ViewModel
     */
    fun createViewModel(
        filePath: File,
        vmName: String,
        modelName: String,
        mPackage: String
    ) {
        //生成文件
        val vmf = File(filePath, vmName)
        //写入文件初始内容
        val vmsb = StringBuilder()
        vmsb.append("package ${PATH_PACKAGE}.ui.${StringUtils.toLowerCaseName(modelName)}\n\n")
        vmsb.append("import android.app.Application\n")
        vmsb.append("import com.yjp.easytools.base.BaseViewModel\n\n")
        vmsb.append("/**\n*\n* @author yjp\n* @date ${DataUtils.data2Str(System.currentTimeMillis())}\n*/\n")
        vmsb.append(
            "class ${vmName.substring(
                0,
                vmName.indexOf(".")
            )}(application: Application) : BaseViewModel(application) {\n}"
        )
        if (FileUtil.writeFile(vmf, vmsb.toString())) {
            println("ViewModel创建成功")
        } else {
            println("ViewModel创建失败")
        }
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
            sb.append(String.format(format, value, f.toString()))
        }
        return sb.toString()
    }

    /**
     * 输出文件
     * @param path: 文件目录
     * @param fileName: 文件名称
     * @param content : 文件内容
     * */
    fun outFile(path: String, fileName: String, content: StringBuilder) {
        val file = File(path, fileName)
        if (FileUtil.writeFile(file, content.toString())) {
            println("创建成功")
        } else {
            println("创建失败")
        }
    }
}

/**
 * 文件创建类型
 * @author yjp
 * @date 2020/6/24
 */
enum class UiType {
    ACTIVITY, FRAGMENT, DIALOG
}