package com.yjp.easytools.generator

import com.yjp.easytools.generator.creater.UiType
import com.yjp.easytools.utils.DataUtils
import com.yjp.easytools.utils.FileUtil
import com.yjp.easytools.utils.StringUtils
import java.io.BufferedReader
import java.io.File
import java.io.FileInputStream
import java.io.InputStreamReader

/**
 * 快速生成工具$
 * @author yjp
 * @date 2020/6/28 11:11
 */
class QGTools {
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

        //模板路径
        const val ACTIVITY =
            "./easytools/src/main/java/com/yjp/easytools/generator/template/TActivity.txt"
        const val FRAGMENT =
            "./easytools/src/main/java/com/yjp/easytools/generator/template/TFragment.txt"
        const val DIALOG =
            "./easytools/src/main/java/com/yjp/easytools/generator/template/TDialog.txt"
        const val LAYOUT =
            "./easytools/src/main/java/com/yjp/easytools/generator/template/TLayout.txt"
        const val DIALOG_LAYOUT =
            "./easytools/src/main/java/com/yjp/easytools/generator/template/TDialogLayout.txt"
        const val VIEW_MODEL =
            "./easytools/src/main/java/com/yjp/easytools/generator/template/TViewModel.txt"

        @JvmStatic
        fun main(args: Array<String>) {
            ui("shopCat", "购物车", UiType.ACTIVITY)
            ui("web", "Web查看", UiType.FRAGMENT)
            ui("tip", "提示弹窗", UiType.DIALOG)
        }

        /**
         * 根据传入的模块名、类型匹配模板
         * 根据模块名生成文件文件名与包名
         * 根据描述生成注释
         * @param model
         * @param desc
         * @param type
         */
        private fun ui(model: String, desc: String, type: UiType) {
            //初始化值
            val bean = QGBean(
                "${PATH_PACKAGE}.ui.${StringUtils.toLowerCaseName(model)}",
                PATH_PACKAGE,
                StringUtils.upperFirstLetter(model) + "",
                model,
                StringUtils.toLowerCaseName(model),
                desc,
                DataUtils.data2Str(System.currentTimeMillis())
            )
            //判断文件类型
            when (type) {
                UiType.ACTIVITY -> {
                    val af = File(ACTIVITY)
                    FileUtil.writeFile(
                        PATH_MAIN_CODE + bean.model_path.replace(".", "/"),
                        "${bean.b_model_name}Activity.kt",
                        create(bean, af)
                    )
                    val xf = File(LAYOUT)
                    FileUtil.writeFile(
                        PATH_MAIN_LAYOUT,
                        "activity_${bean.layout_name}.xml",
                        create(bean, xf)
                    )
                    val vf = File(VIEW_MODEL)
                    FileUtil.writeFile(
                        PATH_MAIN_CODE + bean.model_path.replace(".", "/"),
                        "${bean.b_model_name}ViewModel.kt",
                        create(bean, vf)
                    )
                }
                UiType.FRAGMENT -> {
                    val ff = File(FRAGMENT)
                    FileUtil.writeFile(
                        PATH_MAIN_CODE + bean.model_path.replace(".", "/"),
                        "${bean.b_model_name}Fragment.kt",
                        create(bean, ff)
                    )
                    val xf = File(LAYOUT)
                    FileUtil.writeFile(
                        PATH_MAIN_LAYOUT,
                        "fragment_${bean.layout_name}.xml",
                        create(bean, xf)
                    )
                    val vf = File(VIEW_MODEL)
                    FileUtil.writeFile(
                        PATH_MAIN_CODE + bean.model_path.replace(".", "/"),
                        "${bean.b_model_name}ViewModel.kt",
                        create(bean, vf)
                    )
                }
                UiType.DIALOG -> {
                    bean.model_path = "${PATH_PACKAGE}.dialog"
                    val df = File(DIALOG)
                    FileUtil.writeFile(
                        PATH_MAIN_CODE + bean.model_path.replace(".", "/"),
                        "${bean.b_model_name}Dialog.kt",
                        create(bean, df)
                    )
                    val xf = File(DIALOG_LAYOUT)
                    FileUtil.writeFile(
                        PATH_MAIN_LAYOUT,
                        "dialog_${bean.layout_name}.xml",
                        create(bean, xf)
                    )
                }
            }
        }

        /**
         * 生成文件
         */
        private fun create(bean: QGBean, file: File): String {
            if (file.exists()) {
                val fis = FileInputStream(file)
                val ips = InputStreamReader(fis)
                val br = BufferedReader(ips)
                val bf = StringBuffer()
                var s: String?
                //使用readLine方法，一次读一行
                while (br.readLine().also { s = it } != null) {
                    bf.appendln(format(bean, s!!))
                }
                //关闭资源
                br.close()
                ips.close()
                fis.close()
                return bf.toString()
            } else {
                println("未找模板文件")
            }
            return ""
        }

        /**
         * 模板值替换
         */
        private fun format(bean: QGBean, s: String): String {
            return s.replace("\${model_path}", bean.model_path)
                .replace("\${package_path}", bean.package_path)
                .replace("\${b_model_name}", bean.b_model_name)
                .replace("\${desc}", bean.desc)
                .replace("\${date}", bean.date)
                .replace("\${layout_name}", bean.layout_name)
                .replace("\${s_model_name}", bean.s_model_name)
        }
    }
}

data class QGBean(
    //model路径
    var model_path: String,
    //包路径
    var package_path: String,
    //model名称
    var b_model_name: String,
    //驼峰命名的model名称
    var s_model_name: String,
    //布局文件名称
    var layout_name: String,
    //描述
    var desc: String,
    //时间
    var date: String
)