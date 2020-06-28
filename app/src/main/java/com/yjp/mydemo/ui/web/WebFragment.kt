package com.yjp.mydemo.ui.web

import android.os.Bundle
import com.yjp.easytools.base.BaseFragment
import com.yjp.mydemo.BR
import com.yjp.mydemo.R
import com.yjp.mydemo.databinding.FragmentWebBinding

/**
 * Web查看
 * @author yjp
 * @date 2020-06-28 17:23:05
 */
class WebFragment : BaseFragment<FragmentWebBinding, WebViewModel>() {

    companion object {

       private var instance: WebFragment? = null

        @Synchronized
        fun getInstance(): WebFragment {
            if (instance == null) {
                synchronized(WebFragment::class.java) {
                    instance = WebFragment()
                }
            }
            return instance!!
        }
    }

    override fun initContentView(saveInstanceState: Bundle?): Int {
        return R.layout.fragment_web
    }

    override fun initVariableId(): Int {
        return BR.webViewModel
    }

    override fun onDestroyView() {
            instance = null
            super.onDestroyView()
        }
}
