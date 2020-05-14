package com.yjp.mydemo.http

import com.yjp.easytools.http.BaseHttp
import com.yjp.easytools.utils.SPUtils
import com.yjp.mydemo.constant.ConstantUtil
import com.yjp.mydemo.constant.SPKey
import okhttp3.Interceptor

/**
* 网络请求框架
* @author yjp
*@date 2020-05-11 23:41:52
*/
class HttpHelp : BaseHttp(){
	companion object {
		private val httpHelp by lazy(LazyThreadSafetyMode.SYNCHRONIZED) { HttpHelp() }
		fun api(): ApiService {
			return httpHelp.getAPI(ApiService::class.java)
		}
		fun reset() {
			httpHelp.reset()
		}
	}
	override fun getBaseUrl(): String {
		return "http://" + SPUtils.getString(SPKey.CURRENT_IP,ConstantUtil.IP)+":"+SPUtils.getString(SPKey.CURRENT_PORT,ConstantUtil.PORT)
	}
	override fun getHeaderInterceptor(): Interceptor {
		return Interceptor.invoke {
			val originalRequest = it.request()
			val request = originalRequest.newBuilder()
				.addHeader("Authorization", SPUtils.getString(SPKey.TOKEN, ""))
				.addHeader("Content-Type", "application/json;charset=UTF-8")
				.method(originalRequest.method, originalRequest.body)
				.build()
			it.proceed(request)
		}
	}
}