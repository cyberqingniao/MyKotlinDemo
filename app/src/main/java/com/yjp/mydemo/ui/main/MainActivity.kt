package com.yjp.mydemo.ui.main

import android.os.Bundle
import android.view.KeyEvent
import androidx.lifecycle.Observer
import com.yjp.easytools.base.BaseActivity
import com.yjp.easytools.utils.ActivityManager
import com.yjp.easytools.utils.ToastUtils
import com.yjp.mydemo.BR
import com.yjp.mydemo.R
import com.yjp.mydemo.databinding.ActivityMainBinding
import com.yjp.mydemo.ui.goodsType.GoodsTypeFragment
import com.yjp.mydemo.ui.home.HomeFragment
import com.yjp.mydemo.ui.message.MessageFragment
import com.yjp.mydemo.ui.my.MyFragment

/**
 *
 * @author yjp
 * @date 2020-05-11 23:41:52
 */
class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>() {

    private var mCurrentFragmentIndex = -1;
    private var oldClickTime: Long = 0

    override fun initContentView(saveInstanceState: Bundle?): Int {
        return R.layout.activity_main
    }

    override fun initVariableId(): Int {
        return BR.mainViewModel
    }

    override fun initData() {
        super.initData()
        val bt = supportFragmentManager.beginTransaction();
        bt.add(R.id.fl_main, HomeFragment.instance)
            .add(R.id.fl_main, GoodsTypeFragment.instance)
            .add(R.id.fl_main, MessageFragment.instance)
            .add(R.id.fl_main, MyFragment.instance)
            .hide(GoodsTypeFragment.instance)
            .hide(MessageFragment.instance)
            .hide(MyFragment.instance)
            .commit()
    }

    override fun initViewObservable() {
        super.initViewObservable()
        viewModel!!.ui.observe(this, Observer {
            childFragment(it)
        })
    }

    /**
     * 切换fragment
     */
    private fun childFragment(index: Int) {
        if (mCurrentFragmentIndex == index) {
            return
        }
        mCurrentFragmentIndex = index;
        val bt = supportFragmentManager.beginTransaction()
        when (index) {
            0 -> {
                bt.hide(GoodsTypeFragment.instance)
                    .hide(MessageFragment.instance)
                    .hide(MyFragment.instance)
                    .show(HomeFragment.instance)
                    .commit()
            }
            1 -> {
                bt.hide(HomeFragment.instance)
                    .hide(MessageFragment.instance)
                    .hide(MyFragment.instance)
                    .show(GoodsTypeFragment.instance)
                    .commit()
            }
            2 -> {
                bt.hide(HomeFragment.instance)
                    .hide(GoodsTypeFragment.instance)
                    .hide(MyFragment.instance)
                    .show(MessageFragment.instance)
                    .commit()
            }
            3 -> {
                bt.hide(HomeFragment.instance)
                    .hide(GoodsTypeFragment.instance)
                    .hide(MessageFragment.instance)
                    .show(MyFragment.instance)
                    .commit()
            }
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (System.currentTimeMillis() - oldClickTime < 2000) {
                ActivityManager.instance.AppExit()
            } else {
                ToastUtils.showShort("再次点击退出程序")
                oldClickTime = System.currentTimeMillis();
            }
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

}