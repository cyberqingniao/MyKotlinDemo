package com.yjp.easytools.utils

import android.content.Context
import android.graphics.*
import android.media.Image
import android.net.Uri
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import android.text.TextUtils
import android.util.Base64
import android.util.Log
import android.util.LruCache
import androidx.appcompat.app.AppCompatActivity
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.FileNotFoundException
import java.io.IOException

/**
 * $
 * @author yjp
 * @date 2020/3/31 18:21
 */
object ImageUtils {
    private var mMemoryCache: LruCache<String, Bitmap?>? = null
    private var cacheSize = 0

    /**
     * 根据Uri获取图片
     *
     * @param context
     * @param uri
     * @return Bitmap
     */
    @Throws(FileNotFoundException::class, IOException::class)
    fun getBitmapFormUri(context: Context, uri: Uri?): Bitmap? {
        var input = context.contentResolver.openInputStream(uri!!)
        //这一段代码是不加载文件到内存中也得到bitmap的真是宽高，主要是设置inJustDecodeBounds为true
        val onlyBoundsOptions = BitmapFactory.Options()
        onlyBoundsOptions.inJustDecodeBounds = true //不加载到内存
        onlyBoundsOptions.inDither = true //optional
        onlyBoundsOptions.inPreferredConfig = Bitmap.Config.RGB_565 //optional
        BitmapFactory.decodeStream(input, null, onlyBoundsOptions)
        input!!.close()
        val originalWidth = onlyBoundsOptions.outWidth
        val originalHeight = onlyBoundsOptions.outHeight
        if (originalWidth == -1 || originalHeight == -1) return null
        //图片分辨率以480x800为标准
        val hh = 800f //这里设置高度为800f
        val ww = 480f //这里设置宽度为480f
        //缩放比，由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        var be = 1 //be=1表示不缩放
        if (originalWidth > originalHeight && originalWidth > ww) { //如果宽度大的话根据宽度固定大小缩放
            be = (originalWidth / ww).toInt()
        } else if (originalWidth < originalHeight && originalHeight > hh) { //如果高度高的话根据宽度固定大小缩放
            be = (originalHeight / hh).toInt()
        }
        if (be <= 0) be = 1
        //比例压缩
        val bitmapOptions = BitmapFactory.Options()
        bitmapOptions.inSampleSize = be //设置缩放比例
        bitmapOptions.inDither = true
        bitmapOptions.inPreferredConfig = Bitmap.Config.RGB_565
        input = context.contentResolver.openInputStream(uri)
        val bitmap = BitmapFactory.decodeStream(input, null, bitmapOptions)
        input!!.close()
        return compressImage(bitmap) //再进行质量压缩
    }

    /**
     * 压缩图片
     *
     * @param image
     * @return Bitmap
     */
    fun compressImage(image: Bitmap?): Bitmap? {
        val baos = ByteArrayOutputStream()
        image!!.compress(Bitmap.CompressFormat.JPEG, 100, baos) //质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        var options = 100
        while (baos.toByteArray().size / 1024 > 100) { //循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset() //重置baos即清空baos
            //第一个参数 ：图片格式 ，第二个参数： 图片质量，100为最高，0为最差  ，第三个参数：保存压缩后的数据的流
            image.compress(Bitmap.CompressFormat.JPEG, options, baos) //这里压缩options，把压缩后的数据存放到baos中
            options -= 10 //每次都减少10
            if (options <= 0) break
        }
        val isBm =
            ByteArrayInputStream(baos.toByteArray()) //把压缩后的数据baos存放到ByteArrayInputStream中
        return BitmapFactory.decodeStream(isBm, null, null)
    }

    /**
     * 创建水印文件, 以下是水印上添加的文本信息
     *
     * @param mContext      上下文环境
     * @param path          需要添加水印的图片路径
     * @param watermarkText 水印文字
     * @return
     */
    fun createWatermark(
        path: String,
        watermarkText: String
    ): Bitmap? {
        val bitmap = BitmapFactory.decodeFile(path)
        //        BitmapFactory.decodeResource(mContext.getResources(), resources);
        // 获取图片的宽高
        val bitmapWidth = bitmap.width
        val bitmapHeight = bitmap.height
        // 创建一个和图片一样大的背景图
        val bmp = Bitmap.createBitmap(bitmapWidth, bitmapHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bmp)
        // 画背景图
        canvas.drawBitmap(bitmap, 0f, 0f, null)
        //-------------开始绘制文字--------------
        if (!TextUtils.isEmpty(watermarkText)) {
            val screenWidth = Utils.getScreenWidth()
            val textSize = (Utils.dp2px(16F) * bitmapWidth / screenWidth).toFloat()
            // 创建画笔
            val mPaint = TextPaint()
            // 文字矩阵区域
            val textBounds = Rect()
            // 水印的字体大小
            mPaint.textSize = textSize
            // 文字阴影
            mPaint.setShadowLayer(0.5f, 0f, 1f, Color.YELLOW)
            // 抗锯齿
            mPaint.isAntiAlias = true
            // 水印的区域
            mPaint.getTextBounds(watermarkText, 0, watermarkText.length, textBounds)
            // 水印的颜色
            mPaint.color = Color.BLUE
            val layout = StaticLayout(
                watermarkText, 0, watermarkText.length, mPaint, (bitmapWidth - textSize).toInt(),
                Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.5f, true
            )
            // 文字开始的坐标
            val textX = (Utils.dp2px(8F) * bitmapWidth / screenWidth).toFloat()
            //float textY = bitmapHeight / 2;//图片的中间
            val textY = (Utils.dp2px(8F) * bitmapHeight / screenWidth).toFloat()
            // 画文字
            canvas.translate(textX, textY)
            layout.draw(canvas)
        }
        //保存所有元素
        canvas.save()
        canvas.restore()
        return bmp
    }

    /**
     * BitMap转Base64
     *
     * @param bitmap
     * @return
     */
    fun bitmapToBase64(bitmap: Bitmap?): String? {
        var baos: ByteArrayOutputStream? = null
        return try {
            if (bitmap != null) {
                baos = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                val bitmapBytes = baos.toByteArray()
                return Base64.encodeToString(bitmapBytes, Base64.DEFAULT)
            }
            ""
        } finally {
            try {
                if (baos != null) {
                    baos.flush()
                    baos.close()
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    /**
     * Base64转Bitmap
     *
     * @param base64Data
     * @return
     */
    fun base64ToBitmap(base64Data: String): Bitmap? {
        if (cacheSize == 0) {
            val maxMemory = Runtime.getRuntime().maxMemory() / 1024
            cacheSize = (maxMemory / 8).toInt()
        }
        if (mMemoryCache == null) {
            mMemoryCache = object : LruCache<String, Bitmap?>(this.cacheSize) {
                override fun sizeOf(key: String, value: Bitmap?): Int {
                    return value?.byteCount?.div(1024) ?: 0
                }
            }
        }
        val imgByte: ByteArray?
        try {
            var bitmap = mMemoryCache!![base64Data]
            if (bitmap == null) {
                imgByte = Base64.decode(base64Data, Base64.DEFAULT)
                bitmap = BitmapFactory.decodeByteArray(imgByte, 0, imgByte.size)
                mMemoryCache!!.put(base64Data, bitmap)
            }
            return bitmap
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            System.gc()
        }
        return null
    }

    /**
     * 屏幕截图
     *
     * @param activity
     * @return
     */
    fun screenShot(activity: AppCompatActivity, fileName: String): Bitmap {
        val view = activity.window.decorView
        //允许当前窗口保存缓存信息
        view.isDrawingCacheEnabled = true
        view.buildDrawingCache()
        //导航栏高度
        val navigationBarHeight = Utils.getNavigationBarHeight()
        //获取屏幕宽和高
        val width = Utils.getScreenWidth()
        val height = Utils.getScreenHeight()
        // 全屏不用考虑状态栏，有导航栏需要加上导航栏高度
        var bitmap: Bitmap? = null
        try {
            bitmap = Bitmap.createBitmap(
                view.drawingCache,
                0,
                0,
                width,
                height + navigationBarHeight
            )
        } catch (e: Exception) { // 这里主要是为了兼容异形屏做的处理，我这里的处理比较仓促，直接靠捕获异常处理
            // 其实vivo oppo等这些异形屏手机官网都有判断方法
            // 正确的做法应该是判断当前手机是否是异形屏，如果是就用下面的代码创建bitmap
            var msg = e.message
            // 部分手机导航栏高度不占窗口高度，不用添加，比如OppoR15这种异形屏
            if (msg!!.contains("<= bitmap.height()")) {
                try {
                    bitmap = Bitmap.createBitmap(
                        view.drawingCache, 0, 0, width,
                        height
                    )
                } catch (e1: Exception) {
                    msg = e1.message
                    // 适配Vivo X21异形屏，状态栏和导航栏都没有填充
                    if (msg!!.contains("<= bitmap.height()")) {
                        try {
                            bitmap = Bitmap.createBitmap(
                                view.drawingCache, 0, 0, width,
                                height - Utils.getStatusBarHeight()
                            )
                        } catch (e2: Exception) {
                            e2.printStackTrace()
                        }
                    } else {
                        e1.printStackTrace()
                    }
                }
            } else {
                e.printStackTrace()
            }
        }
        //销毁缓存信息
        view.destroyDrawingCache()
        view.isDrawingCacheEnabled = false
        if (null != bitmap) {
            FileUtils.writeFile(fileName, bitmap, Bitmap.CompressFormat.JPEG)
        }
        return bitmap!!
    }

    /**
     * 这个方法可以转换，但是得到的图片右边多了一列，比如上面方法得到1080x2160，这个方法得到1088x2160
     * 所以要对得到的Bitmap裁剪一下
     *
     * @param image
     * @param config
     * @return
     */
    fun image2Bitmap(image: Image, config: Bitmap.Config): Bitmap {
        val width = image.width
        val height = image.height
        val bitmap: Bitmap
        val planes = image.planes
        val buffer = planes[0].buffer
        val pixelStride = planes[0].pixelStride
        val rowStride = planes[0].rowStride
        val rowPadding = rowStride - pixelStride * width
        Log.d(
            "WOW",
            "pixelStride:$pixelStride. rowStride:$rowStride. rowPadding$rowPadding"
        )
        bitmap = Bitmap.createBitmap(
            width + rowPadding / pixelStride /*equals: rowStride/pixelStride */
            , height, config
        )
        bitmap.copyPixelsFromBuffer(buffer)
        return Bitmap.createBitmap(bitmap, 0, 0, width, height)
        //        return bitmap;
    }

    /**
     * PNG
     * @return
     */
    fun bitmap2byte(bmp: Bitmap, quality: Int): ByteArray {
        val baos = ByteArrayOutputStream()
        bmp.compress(Bitmap.CompressFormat.PNG, quality, baos)
        return baos.toByteArray()
    }

    /**
     * PNG
     * @return
     */
    fun byte2bitmap(data: ByteArray): Bitmap {
        return BitmapFactory.decodeByteArray(data, 0, data.size)
    }
}