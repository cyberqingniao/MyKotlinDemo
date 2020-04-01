package com.yjp.easytools.adapter.recycler

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView

/**
 * 通用Recycler适配器$
 * @author yjp
 * @date 2020/4/1 9:21
 */
class ModelRecyclerAdapter<T>(context: Context, data: MutableList<T>?, @LayoutRes layoutId: Int) :
    RecyclerView.Adapter<ModelViewHolder>() {

    constructor(context: Context, @LayoutRes layoutId: Int) : this(context, null, layoutId)

    private var mContext: Context = context
    private var mLayoutId: Int = layoutId
    private var data: MutableList<T>? = data
    private var listener: ModelBindViewListener<T>? = null

    fun setData(data: MutableList<T>) {
        this.data = data
        notifyDataSetChanged()
    }

    fun addData(data: T) {
        if (this.data == null) {
            this.data = mutableListOf()
        }
        this.data?.add(data)
        notifyDataSetChanged()
    }

    fun addData(data: List<T>) {
        if (this.data == null) {
            this.data = mutableListOf()
        }
        this.data?.addAll(data)
        notifyDataSetChanged()
    }

    fun getData(): List<T>? {
        return this.data
    }

    fun removeData(position: Int) {
        this.data?.removeAt(position)
        notifyDataSetChanged()
    }

    fun removeAll() {
        this.data?.clear()
        notifyDataSetChanged()
    }

    fun setBindViewListener(listener: ModelBindViewListener<T>) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ModelViewHolder {
        val view = LayoutInflater.from(mContext).inflate(mLayoutId, parent, false)
        return ModelViewHolder(view)
    }

    override fun getItemCount(): Int {
        return this.data?.size ?: 0
    }

    override fun onBindViewHolder(holder: ModelViewHolder, position: Int) {
        listener?.onBindView(holder, data?.get(position)!!, position)
    }

    public interface ModelBindViewListener<T> {
        fun onBindView(@NonNull holder: ModelViewHolder, itme: T, position: Int)
    }
}