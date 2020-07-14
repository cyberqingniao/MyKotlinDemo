package com.yjp.easytools.db

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.yjp.easytools.utils.Utils
import java.lang.reflect.InvocationTargetException
import java.util.*

/**
 * 数据库工具类$
 * @author yjp
 * @date 2020/4/9 14:58
 */
class DBHelp private constructor() : SQLiteOpenHelper(Utils.context, DB_NAME, null, DB_VERSION) {

    companion object {
        private const val DB_NAME = "DB_YJP"
        private const val DB_VERSION = 1
        val instance: DBHelp by lazy(LazyThreadSafetyMode.SYNCHRONIZED) { DBHelp() }
    }

    private var db: SQLiteDatabase = writableDatabase

    override fun onCreate(db: SQLiteDatabase?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    /**
     * 根据实体类创建表
     */
    fun createTable(c: Class<*>) {
        val tableName = getColumnName(c.simpleName)
        val sb = StringBuilder()
        sb.append("CREATE TABLE IF NOT EXISTS ")
            .append(tableName)
            .append(" (_ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,")
        val fields = c.declaredFields
        for (field in fields) {
            if (field.name != "mId") {
                val type = field.type.simpleName
                val name = field.name
                sb.append(getColumnName(name))
                when (type) {
                    "String" -> {
                        sb.append(" TEXT,")
                    }
                    "int" -> {
                        sb.append(" INTEGER,")
                    }
                    "boolean" -> {
                        sb.append(" INTEGER,")
                    }
                    "float" -> {
                        sb.append(" REAL,")
                    }
                    else -> {
                        sb.append(" BLOB,")
                    }
                }
            }
        }
        sb.deleteCharAt(sb.length - 1)
        sb.append(");")
        db.execSQL(sb.toString())
    }

    /**
     * 删除表
     */
    fun delTable(c: Class<*>) {
        val tableName = getColumnName(c.simpleName)
        db.execSQL("DROP TABLE $tableName;")
    }

    /**
     * 判断表是否存在
     */
    fun isTable(tableName: String): Boolean {
        val c =
            db.rawQuery("SELECT name FORM 'sqlite_master' WHERE type='table' ORDER BY name;", null)
        var isExist = false
        if (null != c) {
            while (c.moveToNext()) {
                if (c.getString(0) == tableName) {
                    isExist = true
                    break
                }
            }
            c.close()
        }
        return isExist
    }

    /**
     * 插入数据
     *
     * @param o : 盛装数据的实体类
     */
    fun insert(o: Any) {
        val tableName = getColumnName(o.javaClass.simpleName)
        //不存在则创建表
        if (!isTable(tableName)) {
            createTable(o.javaClass)
        }
        val cv: ContentValues = getValues(o)
        if (cv.size() > 0) {
            db.insert(tableName, null, cv)
        } else {
            throw RuntimeException("插入的数据为空")
        }
    }

    /**
     * 删除数据
     *
     * @param tableName : 表名
     * @param del_ID    : 需要删除的_ID集合
     */
    fun delete(tableName: String, vararg del_ID: String) {
        if (isTable(tableName)) {
            db.delete(tableName, "_ID", del_ID)
        }
    }

    /**
     * 更新数据
     *
     * @param o           : 实体类
     * @param whereClause : 条件字段
     * @param whereArgs   : 条件值
     */
    fun updata(
        o: Any,
        whereClause: String,
        vararg whereArgs: String
    ) {
        val tableName = getColumnName(o.javaClass.simpleName)
        if (!isTable(tableName)) {
            createTable(o.javaClass)
        }
        val cv = getValues(o)
        db.update(tableName, cv, whereClause, whereArgs)
    }

    /**
     * 查询一条数据
     *
     * @param o           : 盛装实体类
     * @param whereClause : 条件字段
     * @param whereArgs   : 条件值
     * @param groupBy     : 分组列
     * @param having      : 分组条件
     * @param orderBy     : 排序列
     * @param <T>         : 实体类
     * @return 实体类
    </T> */
    fun <T> query(
        o: Class<T>,
        whereClause: String?,
        whereArgs: Array<String?>?,
        groupBy: String?,
        having: String?,
        orderBy: String?
    ): T? {
        val data = query(o, whereClause, whereArgs, groupBy, having, orderBy, null)
        return if (data != null && data.size == 1) {
            data[0]
        } else null
    }

    /**
     * 分页查询
     *
     * @param o           : 盛装实体类
     * @param whereClause : 条件字段
     * @param whereArgs   : 条件值
     * @param orderBy     : 排序
     * @param page        : 页数，从1开始
     * @param rows        : 每页条数
     * @param <T>         : 实体类
     * @return List<T>
     */
    fun <T> query(
        o: Class<T>?,
        whereClause: String?,
        whereArgs: Array<String?>?,
        orderBy: String?,
        page: Int,
        rows: Int
    ): List<T>? {
        val startIndex = (page - 1) * rows
        return query(
            o!!,
            whereClause,
            whereArgs,
            null,
            null,
            orderBy,
            "$startIndex,$rows"
        )
    }

    /**
     * 查询所有
     *
     * @param o   : 盛装实体类
     * @param <T> : 实体对象
     * @return List<T>
    </T></T> */
    fun <T> query(o: Class<T>): List<T>? {
        return query(o, null, null, null, null, null, null)
    }

    /**
     * 查询多条数据
     *
     * @param o           : 盛装实体类
     * @param whereClause : 条件字段
     * @param whereArgs   : 条件值
     * @param groupBy     : 分组列
     * @param having      : 分组条件
     * @param orderBy     : 排序列
     * @param limit       : 分页
     * @param <T>         : 实体类
     * @return 盛装实体类
    </T> */
    fun <T> query(
        o: Class<T>,
        whereClause: String?,
        whereArgs: Array<String?>?,
        groupBy: String?,
        having: String?,
        orderBy: String?,
        limit: String?
    ): List<T>? {
        val tableName = getColumnName(o.simpleName)
        if (!isTable(tableName)) {
            createTable(o)
            return null
        }
        val c =
            db.query(tableName, null, whereClause, whereArgs, groupBy, having, orderBy, limit)
        return getCursorData(o, c)
    }

    /**
     * 驼峰属性名转列名
     */
    fun getColumnName(name: String): String {
        val sb = StringBuilder()
        val chars = name.toCharArray()
        for (i in chars.indices) {
            if (chars[i] in 'A'..'Z') {
                sb.append("_").append(chars[i])
            } else {
                sb.append(chars[i])
            }
        }
        return sb.toString().toUpperCase(Locale.ROOT)
    }

    /**
     * 列名转驼峰属性名
     *
     * @param name : 列名
     * @return 属性名
     */
    fun getPropertyName(name: String): String? {
        val sb = StringBuilder()
        val chars = name.toCharArray()
        var i = 0
        while (i < chars.size) {
            if (chars[i] != '_') {
                i++
                sb.append(chars[i])
            } else {
                sb.append(chars[i] + 32)
            }
            i++
        }
        return sb.toString()
    }

    /**
     * 获取实体类的属性值,并装入ContentValues
     *
     * @param o : 实体类
     * @return ContentValues
     */
    fun getValues(o: Any): ContentValues {
        val fields = o.javaClass.declaredFields
        val cv = ContentValues()
        for (field in fields) {
            if (field.name != "mId") {
                val type = field.type.simpleName
                val name = field.name
                val value = Utils.getFieldValueByName(name, o) ?: continue
                when (type) {
                    "String" -> cv.put(getColumnName(name), value as String)
                    "int", "Integer" -> cv.put(getColumnName(name), value as Int)
                    "boolean", "Boolean" -> cv.put(
                        getColumnName(name),
                        if (value as Boolean) 1 else 0
                    )
                    "double", "Double" -> cv.put(getColumnName(name), value as Double)
                    "float", "Float" -> cv.put(getColumnName(name), value as Float)
                    "long", "Long" -> cv.put(getColumnName(name), value as Long)
                    "byte", "Byte" -> cv.put(getColumnName(name), value as Byte)
                    "short", "Short" -> cv.put(getColumnName(name), value as Short)
                    "byte[]" -> cv.put(getColumnName(name), value as ByteArray)
                }
            }
        }
        return cv
    }

    /**
     * 读取游标数据
     *
     * @param o   : 盛装实体类
     * @param c   : 游标
     * @param <T> : 实体类
     * @return 查询到的数据
    </T> */
    fun <T> getCursorData(
        o: Class<T>,
        c: Cursor
    ): List<T>? {
        val fields = o.declaredFields
        val data: MutableList<T> = ArrayList()
        while (c.moveToNext()) {
            val cv = ContentValues()
            for (field in fields) {
                val key = getColumnName(field.name)
                val type = field.type.simpleName
                when (type) {
                    "String" -> cv.put(key, c.getString(c.getColumnIndex(key)))
                    "int", "Integer", "byte", "Byte" -> cv.put(key, c.getInt(c.getColumnIndex(key)))
                    "boolean", "Boolean" -> cv.put(key, c.getInt(c.getColumnIndex(key)) == 1)
                    "double", "Double" -> cv.put(key, c.getDouble(c.getColumnIndex(key)))
                    "float", "Float" -> cv.put(key, c.getFloat(c.getColumnIndex(key)))
                    "long", "Long" -> cv.put(key, c.getLong(c.getColumnIndex(key)))
                    "short", "Short" -> cv.put(key, c.getShort(c.getColumnIndex(key)))
                    "byte[]" -> cv.put(key, c.getBlob(c.getColumnIndex(key)))
                }
            }
            val t = setFieldValueByName(o, cv)
            if (t != null) {
                data.add(t)
            }
        }
        return data
    }

    /**
     * 给实体类赋值
     *
     * @param o      : 盛装实体类
     * @param values : 属性值
     * @return 盛装实体类
     */
    fun <T> setFieldValueByName(o: Class<T>, values: ContentValues): T? {
        try {
            val t = o.newInstance()
            val fields = o.declaredFields
            for (field in fields) {
                val name = field.name
                val value = values[getColumnName(name)]
                val methodStr =
                    "set" + name.substring(0, 1).toUpperCase(Locale.ROOT) + name.substring(1)
                val method = o.getMethod(methodStr, field.type)
                method.invoke(t, value)
            }
            return t
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        } catch (e: InvocationTargetException) {
            e.printStackTrace()
        } catch (e: NoSuchMethodException) {
            e.printStackTrace()
        } catch (e: InstantiationException) {
            e.printStackTrace()
        }
        return null
    }
}