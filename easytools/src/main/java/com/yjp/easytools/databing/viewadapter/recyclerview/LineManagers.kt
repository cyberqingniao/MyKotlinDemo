package com.yjp.easytools.databing.viewadapter.recyclerview

import androidx.recyclerview.widget.RecyclerView

/**
 * $
 * @author yjp
 * @date 2020/3/26 15:56
 */
object LineManagers {
    interface LineManagerFactory {
        fun create(recyclerView: RecyclerView): RecyclerView.ItemDecoration
    }

    fun both(): LineManagerFactory {
        return object : LineManagerFactory {
            override fun create(recyclerView: RecyclerView): RecyclerView.ItemDecoration {
                return DividerLine(recyclerView.context, DividerLine.LineDrawModel.BOTH)
            }
        }
    }

    fun horizontal():LineManagerFactory{
        return object : LineManagerFactory{
            override fun create(recyclerView: RecyclerView): RecyclerView.ItemDecoration {
                return DividerLine(recyclerView.context,DividerLine.LineDrawModel.HORIZONTAL)
            }
        }
    }

    fun vertical():LineManagerFactory{
        return object : LineManagerFactory{
            override fun create(recyclerView: RecyclerView): RecyclerView.ItemDecoration {
                return DividerLine(recyclerView.context,DividerLine.LineDrawModel.VERTICAL)
            }
        }
    }
}