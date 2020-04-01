package com.yjp.easytools.adapter.pager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

/**
 * 碎片适配器$
 * @author yjp
 * @date 2020/4/1 10:07
 */
class ModelFragmentAdapter(fm: FragmentManager, var fragments: List<Fragment>) :
    FragmentPagerAdapter(
        fm,
        BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
    ) {

    override fun getItem(position: Int): Fragment {
        return fragments[position]
    }

    override fun getCount(): Int {
        return fragments.size
    }
}