package com.yjp.easytools.generator.creater

import com.yjp.easytools.utils.DataUtils
import com.yjp.easytools.utils.FileUtil
import java.io.File

/**
 * 生成HTTP工具$
 * @author yjp
 * @date 2020/6/24 14:00
 */
object CreateHttp : BaseCreate() {
    /**
     * 生成网络请求框架
     */
    fun init() {
        //文件名
        val httpHelpName = "HttpHelp.kt"
        val apiServiceName = "ApiService.kt"
        val commonObserver = "CommonObserver.kt"
        //包名中的“.”替换成“/”
        var mPackage =
            PATH_PACKAGE
        mPackage = mPackage.replace(".", "/") + "/http1/"
        commonObserverFile(mPackage, commonObserver)
        apiServiceFile(mPackage, apiServiceName)
        httpHelpFile(mPackage, httpHelpName, apiServiceName)
    }

    /**
     * 生成observer数据解析文件
     */
    private fun commonObserverFile(mPackage: String, commonObserver: String) {
        //创建文件
        val cFile = File(PATH_MAIN_CODE + mPackage, commonObserver)
        //生成文件
        if (!FileUtil.isFileExists(cFile)) {
            println("创建文件路径失败")
            return
        }
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
        cSB.append("/**\n* 数据解析\n* @author yjp\n* @date ${DataUtils.data2Str(System.currentTimeMillis())}\n*/\n")
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
        if (FileUtil.writeFile(cFile, cSB.toString())) {
            println("commonObserver创建成功")
        } else {
            println("commonObserver创建失败")
        }
    }

    /**
     * 生成API文件
     */
    private fun apiServiceFile(mPackage: String, apiServiceName: String) {
        //生成文件
        val aFile = File(PATH_MAIN_CODE + mPackage, apiServiceName)
        if (!FileUtil.isFileExists(aFile)) {
            println("创建文件路径失败")
            return
        }
        //写入初始数据
        val aSB = StringBuilder();
        aSB.append("package $PATH_PACKAGE.http\n\n")
        aSB.append("import com.yjp.easytools.http.BaseApi\n")
        aSB.append("import com.yjp.easytools.http.transformer.BaseResult\n")
        aSB.append("import io.reactivex.Observable\n")
        aSB.append("import retrofit2.http.GET\n")
        aSB.append("/**\n* API$\n* @author yjp\n* @date ${DataUtils.data2Str(System.currentTimeMillis())}\n*/\n")
        aSB.append(
            "interface ${apiServiceName.substring(
                0,
                apiServiceName.indexOf(".")
            )} : BaseApi {\n\n}"
        )
        if (FileUtil.writeFile(aFile, aSB.toString())) {
            println("commonObserver创建成功")
        } else {
            println("commonObserver创建失败")
        }
    }

    /**
     * 生成网络请求框架
     */
    private fun httpHelpFile(mPackage: String, httpHelpName: String, apiServiceName: String) {
        //生成文件
        val hFile = File(PATH_MAIN_CODE + mPackage, httpHelpName)
        if (!FileUtil.isFileExists(hFile)) {
            println("创建文件路径失败")
            return
        }
        //写入初始数据
        val hSB = StringBuilder()
        hSB.append("package $PATH_PACKAGE.http\n\n")
        hSB.append("import com.yjp.easytools.http.BaseHttp\n")
        hSB.append("import com.yjp.easytools.utils.SPUtils\n")
        hSB.append("import $PATH_PACKAGE.constant.ConstantUtil\n")
        hSB.append("import $PATH_PACKAGE.constant.SPKey\n")
        hSB.append("import okhttp3.Interceptor\n\n")
        hSB.append("/**\n* 网络请求框架\n* @author yjp\n*@date ${DataUtils.data2Str(System.currentTimeMillis())}\n*/\n")
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
        if (FileUtil.writeFile(hFile, hSB.toString())) {
            println("commonObserver创建成功")
        } else {
            println("commonObserver创建失败")
        }
    }
}