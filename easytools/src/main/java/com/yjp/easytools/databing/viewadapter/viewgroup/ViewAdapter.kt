package com.yjp.easytools.databing.viewadapter.viewgroup

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableList
import androidx.databinding.ViewDataBinding
import me.tatarka.bindingcollectionadapter2.ItemBinding

/**
 * $
 *
 * @author yjp
 * @date 2020-03-29 14:59
 */
object ViewAdapter {
    @BindingAdapter("itemView", "observableList")
    fun addViews(
        viewGroup: ViewGroup,
        itemBinding: ItemBinding<*>,
        viewModelList: ObservableList<IBindingItemViewModel<*>>
    ) {
        if (!viewModelList.isEmpty()) {
            viewGroup.removeAllViews()
            for (viewModel in viewModelList) {
                var binding = DataBindingUtil.inflate(
                    LayoutInflater.from(viewGroup.context),
                    itemBinding.layoutRes(),
                    viewGroup,
                    true
                ) as ViewDataBinding
                binding.setVariable(itemBinding.variableId(), viewModel)
                viewModel.injecDataBinding(binding as Nothing)
            }
        }
    }
}