package com.yjp.mydemo.ui.goods

import android.app.Application
import androidx.databinding.ObservableArrayList
import com.yjp.easytools.base.BaseViewModel
import com.yjp.easytools.bus.event.SingleLiveEvent
import com.yjp.mydemo.BR
import com.yjp.mydemo.R
import me.tatarka.bindingcollectionadapter2.ItemBinding

/**
 * $
 *
 * @author yjp
 * @date 2020-05-15 21:51
 */
class GoodsListViewModel(application: Application) : BaseViewModel(application) {
    val items = ObservableArrayList<GoodItemViewModel<GoodsListViewModel>>()
    val itemBinding: ItemBinding<GoodItemViewModel<GoodsListViewModel>> = ItemBinding.of(
        BR.goodItemViewModel,
        R.layout.item_good
    )

    val ui = SingleLiveEvent<Int>()

    var page = 1
    private val rows = 10

    override fun onCreate() {
        super.onCreate()
        getData()
    }

    fun getData() {
        if (page == 1) {
            items.clear()
        }
        val start = (page - 1) * rows
        for (i in ((page - 1) * rows) until page * rows) {
            items.add(GoodItemViewModel(this))
        }
        ui.value=0
    }

}