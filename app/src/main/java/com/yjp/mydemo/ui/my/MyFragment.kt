package com.yjp.mydemo.ui.my

import android.os.Bundle
import com.yjp.easytools.base.BaseFragment
import com.yjp.mydemo.BR
import com.yjp.mydemo.R
import com.yjp.mydemo.databinding.FragmentMyBinding

/**
 *
 * @author yjp
 * @date 2020-05-11 23:41:52
 */
class MyFragment : BaseFragment<FragmentMyBinding, MyViewModel>() {

    companion object {
        val instance by lazy(LazyThreadSafetyMode.SYNCHRONIZED) { MyFragment() }
    }

    override fun initContentView(saveInstanceState: Bundle?): Int {
        return R.layout.fragment_my
    }

    override fun initVariableId(): Int {
        return BR.myViewModel
    }
}