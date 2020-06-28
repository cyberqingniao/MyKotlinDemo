package com.yjp.easytools.generator.creater

import com.yjp.easytools.utils.DataUtils
import com.yjp.easytools.utils.FileUtil
import com.yjp.easytools.utils.StringUtils
import java.io.File

/**
 * 创建Activity$
 * @author yjp
 * @date 2020/6/23 9:16
 */
object CreateActivity : BaseCreate() {

    /**
     * 初始化生成
     */
    fun init(name: String) {
        //文件路径
        val mFilePath: File
        //View名称
        val viewName: String = StringUtils.upperFirstLetter(name) + "Activity.kt"
        //Layout名称
        val layoutName: String = "activity_${StringUtils.toLowerCaseName(name)}.xml"
        //viewModel名称
        val viewModelName: String = StringUtils.upperFirstLetter(name) + "ViewModel.kt"
        //包路径
        val mPackage: String =
            PATH_PACKAGE.replace(".", "/") + "/ui/${StringUtils.toLowerCaseName(
                name
            )}/"
        mFilePath = File(PATH_MAIN_CODE, mPackage)
        if (!FileUtil.isFileExists(mFilePath)) {
            println("创建文件路径失败")
            return
        }
        createView(
            mFilePath,
            viewName,
            viewModelName,
            layoutName,
            name,
            mPackage
        )
        createViewModel(mFilePath, viewModelName, name, mPackage)
        createLayout(viewModelName, layoutName, name)
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
        val af = File(filePath, aName)
        //写入文件初始内容
        val asb = StringBuilder()
        asb.append("package $PATH_PACKAGE.ui.${StringUtils.toLowerCaseName(modelName)}\n\n")
        asb.append("import android.os.Bundle\n")
        asb.append("import com.yjp.easytools.base.BaseActivity\n")
        asb.append("import $PATH_PACKAGE.BR\n")
        asb.append("import $PATH_PACKAGE.R\n")
        asb.append(
            "import $PATH_PACKAGE.databinding.Activity${StringUtils.upperFirstLetter(
                modelName
            )}Binding\n\n"
        )
        asb.append("/**\n*\n* @author yjp\n* @date ${DataUtils.data2Str(System.currentTimeMillis())}\n*/\n")
        asb.append(
            "class ${aName.substring(
                0,
                aName.indexOf(".")
            )}: BaseActivity<Activity${StringUtils.upperFirstLetter(
                modelName
            )}Binding, ${vmName!!.substring(
                0,
                vmName.indexOf(".")
            )}>() {\n"
        )
        asb.append("\toverride fun initContentView(saveInstanceState: Bundle?): Int {\n")
        asb.append("\t\treturn R.layout.${xmlName.substring(0, xmlName.indexOf("."))}\n")
        asb.append("\t\t}\n\n")
        asb.append("\toverride fun initVariableId(): Int {\n")
        asb.append(
            "\t\treturn BR.${StringUtils.lowerFirstLetter(
                vmName.substring(
                    0,
                    vmName.indexOf(".")
                )
            )}\n"
        )
        asb.append("\t}\n")
        asb.append("}")
        if (FileUtil.writeFile(af, asb.toString())) {
            println("View创建成功")
        } else {
            println("View创建失败")
        }
    }

}