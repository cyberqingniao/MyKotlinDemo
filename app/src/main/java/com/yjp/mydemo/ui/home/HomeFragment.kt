package com.yjp.mydemo.ui.home

import android.os.Bundle
import com.yjp.easytools.base.BaseFragment
import com.yjp.easytools.view.MyScrollView
import com.yjp.mydemo.BR
import com.yjp.mydemo.R
import com.yjp.mydemo.databinding.FragmentHomeBinding
import kotlin.math.abs

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
        return BR.homeViewModel
    }

    override fun initData() {
        super.initData()
        binding!!.homeTitle.background.mutate().alpha = 0
        binding!!.homeScrollview.setOnScrollListener(object : MyScrollView.OnScrollListener {
            override fun onScroll(startY: Int, endY: Int) {
                changeAlpha(startY, endY)
            }
        })
    }

    /**
     * 滑动改变标题栏背景透明度
     */
    private fun changeAlpha(statY: Int, endY: Int) {
        val titleHeight = binding!!.homeTitle.measuredHeight
        val backHeight = binding!!.homeBanner.measuredHeight
        var location: IntArray = IntArray(2)
        binding!!.homeBanner.getLocationInWindow(location)
        val currentY = location[1]
        binding!!.homeTitle.background.mutate().alpha = if (currentY >= 0) {
            0
        } else if (currentY < titleHeight && currentY >= -(backHeight - titleHeight)) {
            val y = abs(currentY) * 1.0
            val height = (backHeight - titleHeight) * 1.0
            val changeValue = (y / height * 255)
            changeValue.toInt()
        } else {
            255
        }
    }
}