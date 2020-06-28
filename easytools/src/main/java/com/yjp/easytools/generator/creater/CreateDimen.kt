package com.yjp.easytools.generator.creater

/**
 * SP与DP通用版生成$
 * @author yjp
 * @date 2020/6/24 8:54
 */
object CreateDimen : BaseCreate() {

    //生成文件名
    private const val DIMEN_NAME = "dimens.xml"

    //DP格式
    private const val DIMEN_DP = "<dimen name=\"dp_%1s\">%2sdp</dimen>\n"

    //SP格式
    private const val DIMEN_SP = "<dimen name=\"sp_%1s\">%2ssp</dimen>\n"

    /**
     * 初始化生成
     */
    fun init() {
        val sb = StringBuilder()
        sb.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n")
        sb.append("<resources>\n")
        sb.append("    <!--  字号  -->\n")
        sb.append(intervalDimen(DIMEN_SP, 6, 30));
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
            sb.append(String.format(format, key, value))
        }
        return sb.toString()
    }

}