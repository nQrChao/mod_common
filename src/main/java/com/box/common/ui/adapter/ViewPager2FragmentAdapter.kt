package com.box.common.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

/**
 * 为 ViewPager2 设计的、支持泛型的、高性能 Fragment 适配器
 */
// 1. 在类名后添加泛型约束 <F : Fragment>
open class ViewPager2FragmentAdapter<F : Fragment> : FragmentStateAdapter {

    // 2. 将列表的类型从 Fragment 修改为泛型 F
    private val fragmentSet: MutableList<F> = ArrayList()

    private val fragmentTitle: MutableList<CharSequence> = ArrayList()

    constructor(activity: FragmentActivity) : super(activity)
    constructor(fragment: Fragment) : super(fragment.childFragmentManager, fragment.lifecycle)

    /**
     * 添加一个 Fragment 和它的标题
     */
    // 3. addFragment 的参数类型也改为 F
    fun addFragment(fragment: F, title: CharSequence = "") {
        fragmentSet.add(fragment)
        fragmentTitle.add(title)
        // 注意：在这里调用 notifyDataSetChanged() 不是最佳实践
        // 最好在添加完所有 Fragment 后，在外部调用一次
    }

    /**
     * 根据位置获取一个 Fragment
     */
    // 4. 返回值类型改为 F
    fun getFragment(position: Int): F {
        return fragmentSet[position]
    }

    fun getPageTitle(position: Int): CharSequence {
        return fragmentTitle[position]
    }

    override fun getItemCount(): Int {
        return fragmentSet.size
    }

    // 5. 【重要】createFragment 的返回值类型也改为 F (虽然方法签名是 : Fragment)
    override fun createFragment(position: Int): Fragment {
        return fragmentSet[position]
    }
}