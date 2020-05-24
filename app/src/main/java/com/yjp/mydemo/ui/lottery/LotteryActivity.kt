package com.yjp.mydemo.ui.lottery

import android.os.Bundle
import androidx.lifecycle.Observer
import com.yjp.easytools.base.BaseActivity
import com.yjp.mydemo.BR
import com.yjp.mydemo.R
import com.yjp.mydemo.databinding.ActivityLotteryBinding

/**
 *
 * @author yjp
 * @date 2020-05-16 15:40:05
 */
class LotteryActivity : BaseActivity<ActivityLotteryBinding, LotteryViewModel>() {
    override fun initContentView(saveInstanceState: Bundle?): Int {
        return R.layout.activity_lottery
    }

    override fun initVariableId(): Int {
        return BR.lotteryViewModel
    }

    override fun initViewObservable() {
        super.initViewObservable()
        binding!!.refreshLayout.setOnRefreshListener {
            viewModel!!.page = 1;
            viewModel!!.getLotteryHistory()
        }
        binding!!.refreshLayout.setOnLoadMoreListener {
            viewModel!!.page++;
            viewModel!!.getLotteryHistory()
        }
        viewModel!!.ui.observe(this, Observer {
            binding!!.refreshLayout.finishRefresh()
            binding!!.refreshLayout.finishLoadMore()
            when (it) {
                0 -> {
                    binding!!.refreshLayout.setEnableLoadMore(false)
                }
                1 -> {
                    binding!!.refreshLayout.setEnableLoadMore(true)
                }
                2 -> {
                    binding!!.refreshLayout.setEnableLoadMore(false)
                }
            }
        })
    }
}