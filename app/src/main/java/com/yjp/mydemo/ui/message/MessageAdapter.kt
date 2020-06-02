package com.yjp.mydemo.ui.message

import androidx.databinding.ViewDataBinding
import me.tatarka.bindingcollectionadapter2.BindingRecyclerViewAdapter

/**
 * 消息适配器$
 * @author yjp
 * @date 2020/6/2 14:58
 */
class MessageAdapter : BindingRecyclerViewAdapter<MessageItemViewModel>() {
    override fun onBindBinding(
        binding: ViewDataBinding?,
        variableId: Int,
        layoutRes: Int,
        position: Int,
        item: MessageItemViewModel
    ) {
        super.onBindBinding(binding, variableId, layoutRes, position, item)
    }
}