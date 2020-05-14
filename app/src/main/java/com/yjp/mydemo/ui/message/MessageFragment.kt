package com.yjp.mydemo.ui.message

import android.os.Bundle
import com.yjp.easytools.base.BaseFragment
import com.yjp.mydemo.BR
import com.yjp.mydemo.R
import com.yjp.mydemo.databinding.FragmentMessageBinding

/**
 *
 * @author yjp
 * @date 2020-05-11 23:41:52
 */
class MessageFragment : BaseFragment<FragmentMessageBinding, MessageViewModel>() {

    companion object {
        val instance by lazy(LazyThreadSafetyMode.SYNCHRONIZED) { MessageFragment() }
    }

    override fun initContentView(saveInstanceState: Bundle?): Int {
        return R.layout.fragment_message
    }

    override fun initVariableId(): Int {
        return BR.messageViewModel
    }
}