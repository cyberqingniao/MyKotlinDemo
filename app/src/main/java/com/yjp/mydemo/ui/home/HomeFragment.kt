package com.yjp.mydemo.ui.home

import android.os.Bundle
import com.yjp.easytools.base.BaseFragment
import com.yjp.mydemo.BR
import com.yjp.mydemo.R
import com.yjp.mydemo.databinding.FragmentHomeBinding

/**
 *
 * @author yjp
 * @date 2020-05-11 23:41:52
 */
class HomeFragment : BaseFragment<FragmentHomeBinding, HomeViewModel>() {

    companion object {
        val instance by lazy(LazyThreadSafetyMode.SYNCHRONIZED) { HomeFragment() }
    }

    override fun initContentView(saveInstanceState: Bundle?): Int {
        return R.layout.fragment_home
    }

    override fun initVariableId(): Int {
        childFragmentManager
        return BR.homeViewModel
    }
}