package com.yjp.easytools.generator.creater

import com.yjp.easytools.utils.DataUtils
import com.yjp.easytools.utils.FileUtil
import com.yjp.easytools.utils.StringUtils
import java.io.File

/**
 * 创建Dialog$
 * @author yjp
 * @date 2020/6/24 11:36
 */
object CreateDialog : BaseCreate() {

    fun init(name: String) {
        val viewName = StringUtils.upperFirstLetter(name) + "Dialog.kt"
        val layoutName = "dialog_${StringUtils.toLowerCaseName(name)}.xml"
        val mPackage = PATH_PACKAGE.replace(".", "/") + "/dialog/"
        val mFilePath = File(PATH_MAIN_CODE, mPackage)
        if (!FileUtil.isFileExists(mFilePath)) {
            println("创建文件路径失败")
            return
        }
        createView(mFilePath, viewName, null, layoutName, name, mPackage)
        createLayout(null, layoutName, name)
    }

    override fun createView(
        filePath: File,
        aName: String,
        vmName: String?,
        xmlName: String,
        modelName: String,
        mPackage: String
    ) {
        //生成文件
        val df = File(PATH_MAIN_CODE + mPackage, aName)
        //写入文件初始内容
        val dsb = StringBuilder()
        dsb.append("package ${PATH_PACKAGE}.dialog\n\n")
        dsb.append("import android.os.Bundle\n")
        dsb.append("import com.yjp.easytools.base.BaseDialog\n")
        dsb.append("import com.yjp.easytools.utils.ActivityManager\n")
        dsb.append("import $PATH_PACKAGE.R\n")
        dsb.append(
            "import $PATH_PACKAGE.databinding.Dialog${StringUtils.upperFirstLetter(
                modelName
            )}Binding\n\n"
        )
        dsb.append("/**\n*$\n* @author yjp\n* @date ${DataUtils.data2Str(System.currentTimeMillis())}\n*/\n")
        dsb.append(
            "class ${aName.substring(
                0,
                aName.indexOf(".")
            )}: BaseDialog<Dialog${StringUtils.upperFirstLetter(
                modelName
            )}Binding>(ActivityManager.instance.currentActivity()) {\n\n"
        )
        dsb.append("\toverride fun initContentView(savedInstanceState: Bundle?): Int {\n")
        dsb.append("\t\treturn R.layout.${xmlName.substring(0, xmlName.indexOf("."))}\n\t}\n\n")
        dsb.append("\toverride fun init() {\n\t}\n}")
        if (FileUtil.writeFile(df, dsb.toString())) {
            println("View创建成功")
        } else {
            println("View创建失败")
        }
    }
}