package com.yjp.easytools.generator

import com.yjp.easytools.utils.DateUtils
import com.yjp.easytools.utils.FileUtils
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
        const val HTTP =
            "./easytools/src/main/java/com/yjp/easytools/generator/template/THttpHelp.txt"
        const val API =
            "./easytools/src/main/java/com/yjp/easytools/generator/template/TApi.txt"
        const val COMMON_OBSERVER =
            "./easytools/src/main/java/com/yjp/easytools/generator/template/TCommonObserver.txt"
        const val SP_KEY =
            "./easytools/src/main/java/com/yjp/easytools/generator/template/TSPKey.txt"
        const val CONSTANT =
            "./easytools/src/main/java/com/yjp/easytools/generator/template/TConstantUtils.txt"

        @JvmStatic
        fun main(args: Array<String>) {


        }

        /**
         * Model生成
         * 生成Http请求工具
         * 生成SharedPreferences存储Key的统一管理工具
         * 生成网络链接配置
         */
        private fun model() {
            //主包路径
            val packagePath = "${PATH_MAIN_CODE}${PATH_PACKAGE.replace(".", "/")}"
            //Http工具包路径
            val httpPackagePath = "$packagePath/http/"
            //内部使用工具
            val constantPackagePath = "$packagePath/utils/"
            //HTTP工具生成
            create(httpPackagePath, "HttpHelp.kt", File(HTTP))
            //SharedPreferences存储Key管理工具生成
            create(constantPackagePath, "SPKey.kt", File(SP_KEY))
            //网络链接配置工具生成
            create(constantPackagePath, "ConstantUtils.kt", File(CONSTANT))
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
                DateUtils.data2Str(System.currentTimeMillis())
            )
            //判断文件类型
            when (type) {
                //生成Activity
                UiType.ACTIVITY -> {
                    val af = File(ACTIVITY)
                    FileUtils.writeFile(
                        PATH_MAIN_CODE + bean.model_path.replace(".", "/"),
                        "${bean.b_model_name}Activity.kt",
                        create(bean, af)
                    )
                    val xf = File(LAYOUT)
                    FileUtils.writeFile(
                        PATH_MAIN_LAYOUT,
                        "activity_${bean.layout_name}.xml",
                        create(bean, xf)
                    )
                    val vf = File(VIEW_MODEL)
                    FileUtils.writeFile(
                        PATH_MAIN_CODE + bean.model_path.replace(".", "/"),
                        "${bean.b_model_name}ViewModel.kt",
                        create(bean, vf)
                    )
                }
                //生成Fragemnt
                UiType.FRAGMENT -> {
                    val ff = File(FRAGMENT)
                    FileUtils.writeFile(
                        PATH_MAIN_CODE + bean.model_path.replace(".", "/"),
                        "${bean.b_model_name}Fragment.kt",
                        create(bean, ff)
                    )
                    val xf = File(LAYOUT)
                    FileUtils.writeFile(
                        PATH_MAIN_LAYOUT,
                        "fragment_${bean.layout_name}.xml",
                        create(bean, xf)
                    )
                    val vf = File(VIEW_MODEL)
                    FileUtils.writeFile(
                        PATH_MAIN_CODE + bean.model_path.replace(".", "/"),
                        "${bean.b_model_name}ViewModel.kt",
                        create(bean, vf)
                    )
                }
                //生成Dialog
                UiType.DIALOG -> {
                    bean.model_path = "${PATH_PACKAGE}.dialog"
                    val df = File(DIALOG)
                    FileUtils.writeFile(
                        PATH_MAIN_CODE + bean.model_path.replace(".", "/"),
                        "${bean.b_model_name}Dialog.kt",
                        create(bean, df)
                    )
                    val xf = File(DIALOG_LAYOUT)
                    FileUtils.writeFile(
                        PATH_MAIN_LAYOUT,
                        "dialog_${bean.layout_name}.xml",
                        create(bean, xf)
                    )
                }
                //HTTP
                UiType.HTTP -> {
                    bean.model_path = PATH_PACKAGE
                    //HTTP工具
                    val hf = File(HTTP)
                    FileUtils.writeFile(
                        "${PATH_MAIN_CODE + bean.model_path.replace(".", "/")}/http",
                        "HttpHelp.kt",
                        create(bean, hf)
                    )
                    //返回统一解析
                    val cof = File(COMMON_OBSERVER)
                    FileUtils.writeFile(
                        "${PATH_MAIN_CODE + bean.model_path.replace(".", "/")}/http",
                        "CommonObserver.kt",
                        create(bean, cof)
                    )
                    //API
                    val apif = File(API)
                    FileUtils.writeFile(
                        "${PATH_MAIN_CODE + bean.model_path.replace(".", "/")}/http",
                        "ApiService.kt",
                        create(bean, apif)
                    )
                    //SPKey
                    val kf = File(SP_KEY)
                    FileUtils.writeFile(
                        "${PATH_MAIN_CODE + bean.model_path.replace(".", "/")}/utils",
                        "SPKey.kt",
                        create(bean, kf)
                    )
                    //链接配置
                    val cf = File(CONSTANT)
                    FileUtils.writeFile(
                        "${PATH_MAIN_CODE + bean.model_path.replace(".", "/")}/utils",
                        "ConstantUtils.kt",
                        create(bean, cf)
                    )
                }
            }
        }

        /**
         * 固定工具类创建
         * @param packagePath : 工具类所在包路径
         * @param fileName : 工具类名称
         * @param file ： 工具类文件
         */
        private fun create(packagePath: String, fileName: String, file: File) {
            FileUtils.writeFile(
                packagePath,
                fileName,
                create(
                    QGBean(
                        "",
                        packagePath,
                        "",
                        "",
                        "",
                        "",
                        DateUtils.data2Str(System.currentTimeMillis())
                    ),
                    file
                )
            )
        }

        /**
         * MVVM模板样式生成
         * @param bean
         * @param file
         * @return
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

/**
 * 项目结构生成配置实体类
 * @author yjp
 * @date 2020/6/30
 */
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

/**
 * 文件创建类型
 * @author yjp
 * @date 2020/6/24
 */
enum class UiType {
    ACTIVITY, FRAGMENT, DIALOG, HTTP
}