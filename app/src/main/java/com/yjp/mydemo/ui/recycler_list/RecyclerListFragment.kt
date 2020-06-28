package com.yjp.mydemo.ui.recycler_list

import android.os.Bundle
import com.yjp.easytools.base.BaseFragment
import com.yjp.mydemo.BR
import com.yjp.mydemo.R
import com.yjp.mydemo.databinding.FragmentRecyclerListBinding

/**
*
* @author yjp
* @date 2020-06-24 13:55:39
*/
class RecyclerListFragment: BaseFragment<FragmentRecyclerListBinding, RecyclerListViewModel>() {
	override fun initContentView(saveInstanceState: Bundle?): Int {
		return R.layout.fragment_recycler_list
		}

	override fun initVariableId(): Int {
		return BR.recyclerListViewModel
	}
}