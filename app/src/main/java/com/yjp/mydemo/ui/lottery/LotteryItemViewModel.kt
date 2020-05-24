package com.yjp.mydemo.ui.lottery

import androidx.databinding.ObservableField
import com.yjp.easytools.base.ItemViewModel

/**
 * $
 *
 * @author yjp
 * @date 2020-05-16 15:49
 */
class LotteryItemViewModel(
    viewModel: LotteryViewModel,
    time: String,
    lotteryRes: String,
    lotteryNo: String
) :
    ItemViewModel<LotteryViewModel>(viewModel) {

    val time = ObservableField<String>(time)
    val lotteryRes = ObservableField<String>(lotteryRes)
    val lotteryNo = ObservableField<String>(lotteryNo)

}