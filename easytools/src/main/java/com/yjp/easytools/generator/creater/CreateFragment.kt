package com.yjp.easytools.generator.creater

import com.yjp.easytools.utils.DataUtils
import com.yjp.easytools.utils.FileUtil
import com.yjp.easytools.utils.StringUtils
import java.io.File

/**
 * 创建Fragment$
 * @author yjp
 * @date 2020/6/24 11:20
 */
object CreateFragment : BaseCreate() {

    fun init(name: String) {
        val viewName = StringUtils.upperFirstLetter(name) + "Fragment.kt"
        val layoutName = "fragment_${StringUtils.toLowerCaseName(name)}.xml"
        val viewModelName = StringUtils.upperFirstLetter(name) + "ViewModel.kt"
        val mPackage =
            PATH_PACKAGE.replace(".", "/") + "/ui/${StringUtils.toLowerCaseName(
                name
            )}/"
        val mFilePath = File(PATH_MAIN_CODE, mPackage)
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
        val ff = File(filePath, aName)
        //写入文件初始内容
        val fsb = StringBuilder()
        fsb.append("package ${PATH_PACKAGE}.ui.${StringUtils.toLowerCaseName(modelName)}\n\n")
        fsb.append("import android.os.Bundle\n")
        fsb.append("import com.yjp.easytools.base.BaseFragment\n")
        fsb.append("import $PATH_PACKAGE.BR\n")
        fsb.append("import $PATH_PACKAGE.R\n")
        fsb.append(
            "import $PATH_PACKAGE.databinding.Fragment${StringUtils.upperFirstLetter(
                modelName
            )}Binding\n\n"
        )
        fsb.append("/**\n*\n* @author yjp\n* @date ${DataUtils.data2Str(System.currentTimeMillis())}\n*/\n")
        fsb.append(
            "class ${aName.substring(
                0,
                aName.indexOf(".")
            )}: BaseFragment<Fragment${StringUtils.upperFirstLetter(
                modelName
            )}Binding, ${vmName!!.substring(
                0,
                vmName.indexOf(".")
            )}>() {\n"
        )
        fsb.append("\toverride fun initContentView(saveInstanceState: Bundle?): Int {\n")
        fsb.append("\t\treturn R.layout.${xmlName.substring(0, xmlName.indexOf("."))}\n")
        fsb.append("\t\t}\n\n")
        fsb.append("\toverride fun initVariableId(): Int {\n")
        fsb.append(
            "\t\treturn BR.${StringUtils.lowerFirstLetter(
                vmName.substring(
                    0,
                    vmName.indexOf(".")
                )
            )}\n"
        )
        fsb.append("\t}\n")
        fsb.append("}")
        if (FileUtil.writeFile(ff, fsb.toString())) {
            println("View创建成功")
        } else {
            println("View创建失败")
        }
    }

}