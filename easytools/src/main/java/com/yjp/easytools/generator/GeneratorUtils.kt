package com.yjp.easytools.generator

import com.yjp.easytools.utils.DateUtils
import com.yjp.easytools.utils.StringUtils
import com.yjp.easytools.utils.Utils
import java.io.File
import java.io.FileOutputStream
import java.io.PrintWriter

/**
 * 项目自动构建器$
 * @author com.yjp
 * @date 2020/3/25 11:30
 */
@Deprecated("2020-06-28弃用，改用QGTools.kt")
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
        private const val PATH_PACKAGE = "com.yjp.mydemo"

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
//            gu.generateUIFile("splash", "Activity")
//            gu.generateUIFile("login", "Activity")
//            gu.generateUIFile("setService", "Activity")
//            gu.generateUIFile("main", "Activity")
//            gu.generateUIFile("home", "Fragment")
//            gu.generateUIFile("message", "Fragment")
//            gu.generateUIFile("my", "Fragment")
//            gu.generateUIFile("goodsType", "Fragment")
//            gu.generateUIFile("forgetPassword", "Activity")
//            gu.generateUIFile("register", "Activity")
//            gu.generateUIFile("bindPhone", UiType.ACTIVITY)
//            gu.generateUIFile("tip", UiType.DIALOG)
//            gu.generateUIFile("goodsDetails",UiType.ACTIVITY)
            //创建Http模块
            gu.generateHttpFile()

            //创建工具模块
//            gu.generateTools()
        }
    }

    /**
     * 生成基础工具
     */
    fun generateTools() {
        //文件名
        val spKeyName = "SPKey.kt"
        val constantUtilName = "ConstantUtil.kt"
        val eventKeyName = "EventKey.kt"

        //包名中的“.”替换成“/”
        var mPackage =
            PATH_PACKAGE
        mPackage = mPackage.replace(".", "/")
        //生成文件
        val sFile = File("$PATH_MAIN_CODE${mPackage}/constant/", spKeyName)
        val cFile = File("$PATH_MAIN_CODE${mPackage}/constant/", constantUtilName)
        val ekFile = File("$PATH_MAIN_CODE${mPackage}/constant/", eventKeyName)
        isFileExists(File("$PATH_MAIN_CODE${mPackage}/constant/"))
        fileExists(sFile)
        fileExists(cFile)
        fileExists(ekFile)
        //写入文件初始内容
        val sSB = StringBuilder()
        sSB.append("package $PATH_PACKAGE.constant\n\n")
        sSB.append(
            "/**\n* SharedPreferences存储Key\n* @author yjp\n* @date ${DateUtils.data2Str(
                System.currentTimeMillis()
            )}\n*/\n"
        )
        sSB.append("object ${spKeyName.substring(0, spKeyName.indexOf("."))}{\n")
        sSB.append("\tconst val CURRENT_IP=\"CURRENT_IP\"\n")
        sSB.append("\tconst val CURRENT_PORT=\"CURRENT_PORT\"\n")
        sSB.append("\tconst val TOKEN=\"TOKEN\"\n")
        sSB.append("}")
        writeFile(sFile, sSB)
        val cSB = StringBuilder()
        cSB.append("package $PATH_PACKAGE.constant\n\n")
        cSB.append("/**\n* 网络链接配置\n* @author yjp\n* @date ${DateUtils.data2Str(System.currentTimeMillis())}\n*/\n")
        cSB.append("object ${constantUtilName.substring(0, constantUtilName.indexOf("."))}{\n")
        cSB.append("\tconst val IP=\"11.1.1.80\"\n")
        cSB.append("\tconst val PORT=\"8080\"\n")
        cSB.append("}")
        writeFile(cFile, cSB)
        val eSB = StringBuilder()
        eSB.append("package $PATH_PACKAGE.constant\n\n")
        eSB.append("/**\n* EventBus Key\n* @author yjp\n* @date ${DateUtils.data2Str(System.currentTimeMillis())}\n*/\n")
        eSB.append(
            "data class ${eventKeyName.substring(
                0,
                eventKeyName.indexOf(".")
            )}(var type: EventType, var arg: Any?){\n"
        )
        eSB.append("\tconstructor(type: EventType) : this(type, null)")
        eSB.append("}\n\n")
        eSB.append("enum class EventType {\n")
        eSB.append("\t//token验证失败\n")
        eSB.append("\tTOKEN_FAIL,\n")
        eSB.append("\t//登录成功\n")
        eSB.append("\tLOGIN_SUCCESS,\n")
        eSB.append("\t//退出登录\n")
        eSB.append("\tLOGOUT_SUCCESS,\n")
        eSB.append("}")
        writeFile(ekFile, eSB)
    }


    /**
     * 生成网络请求框架
     */
    fun generateHttpFile() {
        //文件名
        val httpHelpName = "HttpHelp.kt"
        val apiServiceName = "ApiService.kt"
        val commonObserver = "CommonObserver.kt"
        //包名中的“.”替换成“/”
        var mPackage =
            PATH_PACKAGE
        mPackage = mPackage.replace(".", "/") + "/http/"
        commonObserverFile(mPackage, commonObserver)
        apiServiceFile(mPackage, apiServiceName)
        httpHelpFile(mPackage, httpHelpName, apiServiceName)
    }

    /**
     * 生成视图
     */
    fun generateUIFile(modelName: String, type: UiType) {
        val aName: String
        //ViewModel
        val vmName: String = StringUtils.upperFirstLetter(modelName) + "ViewModel.kt"
        //XML
        var xmlName: String
        //包名中的“.”替换成“/”
        var mPackage =
            PATH_PACKAGE
        if (type == UiType.DIALOG) {
            mPackage = mPackage.replace(".", "/") + "/dialog/"
            isFileExists(File(PATH_MAIN_CODE + mPackage))
        } else {
            mPackage = mPackage.replace(".", "/") + "/ui/"
            isFileExists(File(PATH_MAIN_CODE + mPackage, modelName))
        }
        when (type) {
            UiType.ACTIVITY -> {
                //Activity
                aName = StringUtils.upperFirstLetter(modelName) + "Activity.kt"
                xmlName = if (StringUtils.isUpperCase(modelName)) {
                    "activity_${StringUtils.toLowerCaseName(modelName)}.xml"
                } else {
                    "activity_${modelName}.xml"
                }
                //生成Activity
                activityFile(aName, vmName, xmlName, modelName, mPackage)
            }
            UiType.FRAGMENT -> {
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
            UiType.DIALOG -> {
                //Dialog
                aName = StringUtils.upperFirstLetter(modelName) + "Dialog.kt"
                //XML
                xmlName = if (StringUtils.isUpperCase(modelName)) {
                    "dialog_${StringUtils.toLowerCaseName(modelName)}.xml"
                } else {
                    "dialog_${modelName}.xml"
                }
                //生成Dialog
                dialogFile(aName, xmlName, modelName, mPackage)
            }
        }
        if (type != UiType.DIALOG) {
            //生成ViewModel
            viewModelFile(vmName, modelName, mPackage)
        }
        //生成XML
        xmlFile(vmName, xmlName, modelName)
    }

    /**
     * 生成observer数据解析文件
     */
    private fun commonObserverFile(mPackage: String, commonObserver: String) {
        //创建文件
        val cFile = File(PATH_MAIN_CODE + mPackage, commonObserver)
        //生成文件
        fileExists(cFile)
        //写入文件初始内容
        val cSB = StringBuilder()
        cSB.append("package $PATH_PACKAGE.http\n\n")
        cSB.append("import com.yjp.easytools.dialog.LoadingDialog\n")
        cSB.append("import com.yjp.easytools.http.exception.ApiException\n")
        cSB.append("import com.yjp.easytools.http.exception.ErrorType\n")
        cSB.append("import com.yjp.easytools.http.observer.BaseObserver\n")
        cSB.append("import com.yjp.easytools.utils.ActivityManager\n")
        cSB.append("import com.yjp.easytools.utils.StringUtils\n")
        cSB.append("import com.yjp.easytools.utils.ToastUtils\n")
        cSB.append("import io.reactivex.disposables.Disposable\n")
        cSB.append("import org.greenrobot.eventbus.EventBus\n")
        cSB.append("/**\n* 数据解析\n* @author yjp\n* @date ${DateUtils.data2Str(System.currentTimeMillis())}\n*/\n")
        cSB.append(
            "abstract class ${commonObserver.substring(
                0,
                commonObserver.indexOf(".")
            )}<T>(private var isShowLoading: Boolean) : BaseObserver<T?>() {\n"
        )
        cSB.append("\tvar disposable: Disposable? = null\n")
        cSB.append("\toverride fun onSubscribe(d: Disposable) {\n")
        cSB.append("\t\tdisposable = d\n")
        cSB.append("\t\tif (isShowLoading) {\n")
        cSB.append("\t\t\tif (!LoadingDialog.isShowing()) {\n")
        cSB.append("\t\t\t\tLoadingDialog.showLoading(ActivityManager.instance.currentActivity(), \"请稍后...\")\n")
        cSB.append("\t\t\t}\n")
        cSB.append("\t\t}\n")
        cSB.append("\t}\n")
        cSB.append("\toverride fun onError(e: ApiException) {\n\t}\n")
        cSB.append("\toverride fun onComplete() {\n")
        cSB.append("\t\tif (isShowLoading) {\n")
        cSB.append("\t\t\tif (LoadingDialog.isShowing()) {\n")
        cSB.append("\t\t\t\tLoadingDialog.dismissLoading()\n")
        cSB.append("\t\t\t}\n")
        cSB.append("\t\t}\n")
        cSB.append("\t}\n")
        cSB.append("}\n")
        writeFile(cFile, cSB)
    }

    /**
     * 生成API文件
     */
    private fun apiServiceFile(mPackage: String, apiServiceName: String) {
        //生成文件
        val aFile = File(PATH_MAIN_CODE + mPackage, apiServiceName)
        fileExists(aFile)
        //写入初始数据
        val aSB = StringBuilder();
        aSB.append("package $PATH_PACKAGE.http\n\n")
        aSB.append("import com.yjp.easytools.http.BaseApi\n")
        aSB.append("import com.yjp.easytools.http.transformer.BaseResult\n")
        aSB.append("import io.reactivex.Observable\n")
        aSB.append("import retrofit2.http.GET\n")
        aSB.append("/**\n* API$\n* @author yjp\n* @date ${DateUtils.data2Str(System.currentTimeMillis())}\n*/\n")
        aSB.append(
            "interface ${apiServiceName.substring(
                0,
                apiServiceName.indexOf(".")
            )} : BaseApi {\n\n}"
        )
        writeFile(aFile, aSB)
    }

    /**
     * 生成网络请求框架
     */
    private fun httpHelpFile(mPackage: String, httpHelpName: String, apiServiceName: String) {
        //生成文件
        val hFile = File(PATH_MAIN_CODE + mPackage, httpHelpName)
        fileExists(hFile)
        //写入初始数据
        val hSB = StringBuilder()
        hSB.append("package $PATH_PACKAGE.http\n\n")
        hSB.append("import com.yjp.easytools.http.BaseHttp\n")
        hSB.append("import com.yjp.easytools.utils.SPUtils\n")
        hSB.append("import $PATH_PACKAGE.constant.ConstantUtil\n")
        hSB.append("import $PATH_PACKAGE.constant.SPKey\n")
        hSB.append("import okhttp3.Interceptor\n\n")
        hSB.append("/**\n* 网络请求框架\n* @author yjp\n*@date ${DateUtils.data2Str(System.currentTimeMillis())}\n*/\n")
        hSB.append("class ${httpHelpName.substring(0, httpHelpName.indexOf("."))} : BaseHttp(){\n")
        hSB.append("\tcompanion object {\n")
        hSB.append(
            "\t\tprivate val httpHelp by lazy(LazyThreadSafetyMode.SYNCHRONIZED) { ${httpHelpName.substring(
                0,
                httpHelpName.indexOf(".")
            )}() }\n"
        )
        hSB.append("\t\tfun api(): ${apiServiceName.substring(0, apiServiceName.indexOf("."))} {\n")
        hSB.append(
            "\t\t\treturn httpHelp.getAPI(${apiServiceName.substring(
                0,
                apiServiceName.indexOf(".")
            )}::class.java)\n"
        )
        hSB.append("\t\t}\n")
        hSB.append("\t\tfun reset() {\n")
        hSB.append("\t\t\thttpHelp.reset()\n")
        hSB.append("\t\t}\n")
        hSB.append("\t}\n")
        hSB.append("\toverride fun getBaseUrl(): String {\n")
        hSB.append("\t\treturn \"http://\" + SPUtils.getString(SPKey.CURRENT_IP,ConstantUtil.IP)+\":\"+SPUtils.getString(SPKey.CURRENT_PORT,ConstantUtil.PORT)\n")
        hSB.append("\t}\n")
        hSB.append("\toverride fun getHeaderInterceptor(): Interceptor {\n")
        hSB.append("\t\treturn Interceptor.invoke {\n")
        hSB.append("\t\t\tval originalRequest = it.request()\n")
        hSB.append("\t\t\tval request = originalRequest.newBuilder()\n")
        hSB.append("\t\t\t\t.addHeader(\"Authorization\", SPUtils.getString(SPKey.TOKEN, \"\"))\n")
        hSB.append("\t\t\t\t.addHeader(\"Content-Type\", \"application/json;charset=UTF-8\")\n")
        hSB.append("\t\t\t\t.method(originalRequest.method, originalRequest.body)\n")
        hSB.append("\t\t\t\t.build()\n")
        hSB.append("\t\t\tit.proceed(request)\n")
        hSB.append("\t\t}\n")
        hSB.append("\t}\n")
        hSB.append("}")
        writeFile(hFile, hSB)
    }

    /**
     * 生成Dialog
     */
    private fun dialogFile(aName: String, xmlName: String, modelName: String, mPackage: String) {
        //生成文件
        val aFile = File(PATH_MAIN_CODE + mPackage, aName)
        fileExists(aFile)
        //写入文件初始内容
        val dSB = StringBuilder()
        dSB.append("package $PATH_PACKAGE.dialog\n\n")
        dSB.append("import android.os.Bundle\n")
        dSB.append("import com.yjp.easytools.base.BaseDialog\n")
        dSB.append("import com.yjp.mydemo.R\n")
        dSB.append("import com.yjp.mydemo.databinding.DialogTipBinding\n")
        dSB.append("/**\n*$\n* @author yjp\n* @date ${DateUtils.data2Str(System.currentTimeMillis())}\n*/\n")
        dSB.append(
            "class ${aName.substring(
                0,
                aName.indexOf(".")
            )}: BaseDialog<Dialog${StringUtils.upperFirstLetter(
                modelName
            )}Binding>() {\n\n"
        )
        dSB.append("\toverride fun initContentView(savedInstanceState: Bundle?): Int {\n")
        dSB.append("\t\treturn R.layout.${xmlName.substring(0, xmlName.indexOf("."))}\n\t}\n\n")
        dSB.append("\toverride fun init() {\n\t}\n}")
        writeFile(aFile, dSB)
    }

    /**
     * Fragment生成器
     */
    private fun fragmentFile(
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
        aSB.append("package $PATH_PACKAGE.ui.${modelName}\n\n")
        aSB.append("import android.os.Bundle\n")
        aSB.append("import com.yjp.easytools.base.BaseFragment\n")
        aSB.append("import com.yjp.mydemo.BR\n")
        aSB.append("import com.yjp.mydemo.R\n")
        aSB.append(
            "import com.yjp.mydemo.databinding.Fragment${StringUtils.upperFirstLetter(
                modelName
            )}Binding\n\n"
        )
        aSB.append("/**\n*\n* @author yjp\n* @date ${DateUtils.data2Str(System.currentTimeMillis())}\n*/\n")
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
    private fun activityFile(
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
        aSB.append("package $PATH_PACKAGE.ui.${modelName}\n\n")
        aSB.append("import android.os.Bundle\n")
        aSB.append("import com.yjp.easytools.base.BaseActivity\n")
        aSB.append("import com.yjp.mydemo.BR\n")
        aSB.append("import com.yjp.mydemo.R\n")
        aSB.append(
            "import com.yjp.mydemo.databinding.Activity${StringUtils.upperFirstLetter(
                modelName
            )}Binding\n\n"
        )
        aSB.append("/**\n*\n* @author yjp\n* @date ${DateUtils.data2Str(System.currentTimeMillis())}\n*/\n")
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
    private fun viewModelFile(
        vmName: String,
        modelName: String,
        mPackage: String
    ) {
        //生成文件
        val vmFile = File(PATH_MAIN_CODE + mPackage, "/$modelName/$vmName")
        fileExists(vmFile)
        //写入初始内容
        val vmSB = StringBuilder()
        vmSB.append("package $PATH_PACKAGE.ui.${modelName}\n\n")
        vmSB.append("import android.app.Application\n")
        vmSB.append("import com.yjp.easytools.base.BaseViewModel\n\n")
        vmSB.append("/**\n*\n* @author yjp\n* @date ${DateUtils.data2Str(System.currentTimeMillis())}\n*/\n")
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
    private fun xmlFile(vmName: String, xmlName: String, modelName: String) {
        //生成XML文件
        val xmlFile = File(PATH_MAIN_LAYOUT, xmlName)
        fileExists(xmlFile)
        //写入初始内容
        val xmlSB = StringBuilder()
        xmlSB.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n")
        xmlSB.append("<layout>\n")
        if (!Utils.isEmpty(vmName)) {
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
                "\t\t\ttype=\"$PATH_PACKAGE.ui.${modelName}.${vmName.substring(
                    0,
                    vmName.indexOf(".")
                )}\"/>\n"
            )
            xmlSB.append("\t</data>\n")
        }
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
        outFile(
            PATH_EASYTOOLS_VALUES,
            DIMEN_NAME, sb
        )
    }

    /**
     * 根据区间生成
     *
     * @param format : 生成格式
     * @param start  : 起始值
     * @param end    : 结束值
     * @return String 生成后的字符串
     */
    private fun intervalDimen(format: String, start: Int, end: Int): String {
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
    private fun stringFormat(format: String, key: String, value: String): String {
        return String.format(format, key, value)
    }

    /**
     * 输出文件
     * @param path: 文件目录
     * @param fileName: 文件名称
     * @param content : 文件内容
     * */
    private fun outFile(path: String, fileName: String, content: StringBuilder) {
        val file: File?
        if (isFileExists(File(path))) {
            file = File(path, fileName)
            writeFile(file, content)
        } else {
            throw RuntimeException("创建文件目录失败")
        }
    }

    private fun writeFile(file: File, content: StringBuilder) {
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

    private fun isFileExists(file: File): Boolean {
        if (!file.exists()) {
            return file.mkdirs()
        }
        return true
    }

    private fun fileExists(file: File): Boolean {
        //判断文件路径是否存在，不存在则创建
        isFileExists(file.parentFile!!)
        if (!file.exists()) {
            return file.createNewFile()
        }
        return true
    }
}