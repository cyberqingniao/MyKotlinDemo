package com.yjp.easytools.generator

import android.annotation.SuppressLint
import com.google.gson.Gson
import com.yjp.easytools.utils.FileUtils
import com.yjp.easytools.utils.Utils
import java.io.*
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.net.URLEncoder
import java.security.KeyManagementException
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern
import javax.net.ssl.HttpsURLConnection
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager
import kotlin.experimental.and

/**
 * 百度翻译API$
 *
 * @author yjp
 * @date 2020-05-25 22:37
 */
class TransApi(private var appId: String, private var securityKey: String) {

    companion object {

        //翻译URL
        private val TRANS_API_HOST = "http://api.fanyi.baidu.com/api/trans/vip/translate"

        //超时时间
        private const val SOCKET_TIMEOUT = 10000 // 10S

        //请求方式
        private const val GET = "GET"

        //百度翻译AK
        private const val BAIDU_TRANS_APP_ID = "20200525000470494"

        //百度翻译ID
        private const val BAIDU_TRANS_APP_KEY = "9TXUXO6C2xeYW9gSB8Yr"

        private val pChineseChara: Pattern =
            Pattern.compile("[\"][\\u4e00-\\u9fa5,，！!()（）？。.]+[\"]")

        //输出目录
        private const val outPath = "E:/outTrans/"

        //读取目录
//        private val readPath = "E:/project/MyKotlinDemo/app/src/main/java/com/yjp/mydemo"
//        private const val readPath = "C:/Users/22/Desktop/runnet/object/QZry/app/src/main/java/com/runnet/aoc"
//        private const val readPath = "C:/Users/22/Desktop/runnet/object/QZry/app/src/main/res/layout"
        private val readPath = arrayOf(
            "E:/yjp/GZwlw_v3.3.20200601_test/app/src/main/java/com/runnet/gzwlw_new",
            "E:/yjp/GZwlw_v3.3.20200601_test/app/src/main/res/layout"
        )

        //忽略内容
        private val regex = Regex("'Log''System''//''*'")

        @JvmStatic
        fun main(args: Array<String>) {
            val api = TransApi(BAIDU_TRANS_APP_ID, BAIDU_TRANS_APP_KEY)
//            val json=api.getTransResult("这是测试", "auto", "en")
//            val result=Gson().fromJson(json,Trans::class.java)
            val strData = mutableListOf<String>()
            val str = StringBuffer()
            for (path in readPath) {
                val files = FileUtils.readFile(path)
                for (f in files) {
                    println(f.path)
                    try {
                        val `is` = BufferedReader(InputStreamReader(FileInputStream(f)))
                        var s: String? = null
                        //使用readLine方法，一次读一行
                        while (`is`.readLine().also { s = it } != null) {
                            if (s!!.contains(regex)) {
                                continue
                            }
                            val s1 = s!!
                            val m: Matcher = pChineseChara.matcher(s1)
                            while (m.find()) {
                                val s2: String = m.group().replace("\"", "")
                                println(s2)
                                if (s2 == "!" || s2 == "！" || s2 == "?" || s2 == "？" || s2 == "," || s2 == "，" || s2 == "." || s2 == "。"||strData.contains(s2)) {
                                    continue
                                }
                                strData.add(s2)
                                val json = api.getTransResult(s2, "auto", "en")
                                println(json)
                                var yingwen = ""
                                if (!Utils.isEmpty(json)) {
                                    val result = Gson().fromJson(json, Trans::class.java)
                                    if (result != null && !Utils.isEmpty(result.trans_result)) {
                                        val dst = result.trans_result[0].dst
                                        yingwen =
                                            dst.toLowerCase(Locale.ROOT).trim().replace(" ", "_")
                                        yingwen = yingwen.replace("'s", "_is")
                                            .replace("'t", "_it")
                                            .replace(",", "")
                                            .replace("!", "")
                                            .replace(".", "")
                                    }
                                }
                                str.append("<string name=\"${yingwen}\">${s2}</string>")
                                    .append("\n")
                                Thread.sleep(1000)
                            }
                        }
                        `is`.close()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
            if (!Utils.isEmpty(str.toString())) {
                FileUtils.writeFile(outPath, "string.xml", str.toString())
            }
        }

        /**
         * 发送GET请求翻译
         */
        operator fun get(
            host: String,
            params: Map<String, String?>
        ): String? {
            try {
                // 设置SSLContext
                val sslContext =
                    SSLContext.getInstance("TLS")
                sslContext.init(null, arrayOf(myX509TrustManager), null)
                val sendUrl = getUrlWithQueryString(host, params)

                // System.out.println("URL:" + sendUrl);
                val uri = URL(sendUrl) // 创建URL对象
                val conn =
                    uri.openConnection() as HttpURLConnection
                if (conn is HttpsURLConnection) {
                    conn.sslSocketFactory = sslContext.socketFactory
                }
                conn.connectTimeout = SOCKET_TIMEOUT // 设置相应超时
                conn.requestMethod = GET
                val statusCode = conn.responseCode
                if (statusCode != HttpURLConnection.HTTP_OK) {
                    println("Http错误码：$statusCode")
                }

                // 读取服务器的数据
                val `is` = conn.inputStream
                val br = BufferedReader(InputStreamReader(`is`))
                val builder = StringBuilder()
                var line: String? = null
                while (br.readLine().also { line = it } != null) {
                    builder.append(line)
                }
                val text = builder.toString()
                close(br) // 关闭数据流
                close(`is`) // 关闭数据流
                conn.disconnect() // 断开连接
                return text
            } catch (e: MalformedURLException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            } catch (e: KeyManagementException) {
                e.printStackTrace()
            } catch (e: NoSuchAlgorithmException) {
                e.printStackTrace()
            }
            return null
        }

        /**
         * 盛装请求参数
         */
        private fun getUrlWithQueryString(
            url: String,
            params: Map<String, String?>
        ): String {
            val builder = StringBuilder(url)
            if (url.contains("?")) {
                builder.append("&")
            } else {
                builder.append("?")
            }
            var i = 0
            for (key in params.keys) {
                val value = params[key]
                    ?: // 过滤空的key
                    continue
                if (i != 0) {
                    builder.append('&')
                }
                builder.append(key)
                builder.append('=')
                builder.append(encode(value))
                i++
            }
            return builder.toString()
        }

        /**
         * 关闭数据流
         */
        private fun close(closeable: Closeable?) {
            if (closeable != null) {
                try {
                    closeable.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }

        /**
         * 对输入的字符串进行URL编码, 即转换为%20这种形式
         *
         * @param input 原文
         * @return URL编码. 如果编码失败, 则返回原文
         */
        private fun encode(input: String?): String? {
            if (input == null) {
                return ""
            }
            try {
                return URLEncoder.encode(input, "utf-8")
            } catch (e: UnsupportedEncodingException) {
                e.printStackTrace()
            }
            return input
        }

        private val myX509TrustManager: TrustManager =
            object : X509TrustManager {
                override fun getAcceptedIssuers(): Array<X509Certificate>? {
                    return null
                }

                @SuppressLint("TrustAllX509TrustManager")
                @Throws(CertificateException::class)
                override fun checkServerTrusted(
                    chain: Array<X509Certificate>,
                    authType: String
                ) {
                }

                @SuppressLint("TrustAllX509TrustManager")
                @Throws(CertificateException::class)
                override fun checkClientTrusted(
                    chain: Array<X509Certificate>,
                    authType: String
                ) {
                }
            }
    }

    fun getTransResult(
        query: String,
        from: String,
        to: String
    ): String? {
        val params =
            buildParams(query, from, to)
        return get(TRANS_API_HOST, params)
    }

    private fun buildParams(
        query: String,
        from: String,
        to: String
    ): Map<String, String?> {
        val params: MutableMap<String, String?> =
            HashMap()
        params["q"] = query
        params["from"] = from
        params["to"] = to
        params["appid"] = appId

        // 随机数
        val salt = System.currentTimeMillis().toString()
        params["salt"] = salt

        // 签名
        val src = appId + query + salt + securityKey // 加密前的原文
        params["sign"] = MD5.md5(src)
        return params
    }
}

//翻译实体类
data class Trans(var from: String, var to: String, var trans_result: List<TransBean>)

//翻译内容实体类
data class TransBean(var src: String, var dst: String)

object MD5 {
    // 首先初始化一个字符数组，用来存放每个16进制字符
    private val hexDigits = charArrayOf(
        '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd',
        'e', 'f'
    )

    /**
     * 获得一个字符串的MD5值
     *
     * @param input 输入的字符串
     * @return 输入字符串的MD5值
     */
    fun md5(input: String?): String? {
        return if (input == null) null else try {
            // 拿到一个MD5转换器（如果想要SHA1参数换成”SHA1”）
            val messageDigest =
                MessageDigest.getInstance("MD5")
            // 输入的字符串转换成字节数组
            val inputByteArray = input.toByteArray(charset("utf-8"))
            // inputByteArray是输入字符串转换得到的字节数组
            messageDigest.update(inputByteArray)
            // 转换并返回结果，也是字节数组，包含16个元素
            val resultByteArray = messageDigest.digest()
            // 字符数组转换成字符串返回
            byteArrayToHex(resultByteArray)
        } catch (e: NoSuchAlgorithmException) {
            null
        }
    }

    /**
     * 获取文件的MD5值
     *
     * @param file
     * @return
     */
    fun md5(file: File): String? {
        try {
            if (!file.isFile) {
                System.err.println("文件" + file.absolutePath + "不存在或者不是文件")
                return null
            }
            val `in` = FileInputStream(file)
            val result = md5(`in`)
            `in`.close()
            return result
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }

    private fun md5(`in`: InputStream): String? {
        try {
            val messagedigest =
                MessageDigest.getInstance("MD5")
            val buffer = ByteArray(1024)
            var read = 0
            while (`in`.read(buffer).also { read = it } != -1) {
                messagedigest.update(buffer, 0, read)
            }
            `in`.close()
            return byteArrayToHex(messagedigest.digest())
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }

    private fun byteArrayToHex(byteArray: ByteArray): String? {
        // new一个字符数组，这个就是用来组成结果字符串的（解释一下：一个byte是八位二进制，也就是2位十六进制字符（2的8次方等于16的2次方））
        val resultCharArray = CharArray(byteArray.size * 2)
        // 遍历字节数组，通过位运算（位运算效率高），转换成字符放到字符数组中去
        var index = 0
        for (b in byteArray) {
            resultCharArray[index++] = hexDigits[b.toInt() ushr (4) and 0xf]
            resultCharArray[index++] = hexDigits[(b and 0xf).toInt()]
        }

        // 字符数组组合成字符串返回
        return String(resultCharArray)
    }
}