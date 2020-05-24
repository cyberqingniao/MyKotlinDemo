package com.yjp.mydemo.ui.lottery

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableField
import com.yjp.easytools.base.BaseViewModel
import com.yjp.easytools.bus.event.SingleLiveEvent
import com.yjp.easytools.utils.ToastUtils
import com.yjp.easytools.utils.Utils
import com.yjp.mydemo.BR
import com.yjp.mydemo.R
import com.yjp.mydemo.constant.ConstantUtil
import com.yjp.mydemo.entity.LotteryBean
import com.yjp.mydemo.entity.LotteryEntity
import com.yjp.mydemo.http.AggregateData
import com.yjp.mydemo.http.CommonObserver
import com.yjp.mydemo.http.HttpHelp
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import me.tatarka.bindingcollectionadapter2.ItemBinding
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

/**
 *
 * @author yjp
 * @date 2020-05-16 15:40:05
 */
class LotteryViewModel(application: Application) : BaseViewModel(application) {

    val items = ObservableArrayList<LotteryItemViewModel>()
    val itemBinding: ItemBinding<LotteryItemViewModel> =
        ItemBinding.of<LotteryItemViewModel>(BR.lotteryItemViewModel, R.layout.item_lottery)

    val maxLottery = ObservableField<String>("")
    val minLottery = ObservableField<String>("")

    val ui = SingleLiveEvent<Int>()

    var page = 1
    var pageSize = 50
    val lotteryResList = ArrayList<LotteryBean>()

    val countValueRed = HashMap<String, Int>()
    val countValueBlue = HashMap<String, Int>()
    val CVR = ArrayList<ValueBean>()
    val CVB = ArrayList<ValueBean>()

    data class ValueBean(var number: String, var count: Int)

    override fun onCreate() {
        super.onCreate()
        getLotteryHistory()
    }

    fun getLotteryHistory() {
        HttpHelp.api().getLotteryHistory(
            ConstantUtil.LOTTERY_HISTORY,
            "ssq",
            ConstantUtil.LOTTERY_KEY,
            pageSize,
            page
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : CommonObserver<AggregateData<LotteryEntity>>(true) {
                @RequiresApi(Build.VERSION_CODES.N)
                override fun onSuccess(result: AggregateData<LotteryEntity>?) {
                    if (page == 1) {
                        lotteryResList.clear()
                    }
                    if (!Utils.isEmpty(result)) {
                        if (result!!.error_code == 0) {
                            if (!Utils.isEmpty(result.result.lotteryResList)) {
                                ui.value =
                                    if (result.result.lotteryResList.size >= pageSize) 1 else 2
                                lotteryResList.addAll(result.result.lotteryResList)
                                for (item in lotteryResList) {
                                    if (item.lottery_date.compareTo("2020-05-14") != 0) {
                                        val nums = item.lottery_res.split(",")
                                        for (n in nums.subList(0, nums.size - 1)) {
                                            countValueRed[n] = if (null == countValueRed[n]) {
                                                1
                                            } else {
                                                countValueRed[n]!! + 1
                                            }
                                        }
                                        val blue = nums[nums.size - 1]
                                        countValueBlue[blue] = if (null == countValueBlue[blue]) {
                                            1
                                        } else {
                                            countValueBlue[blue]!! + 1
                                        }
                                    }
                                    items.add(
                                        LotteryItemViewModel(
                                            this@LotteryViewModel,
                                            item.lottery_date,
                                            item.lottery_res,
                                            item.lottery_no
                                        )
                                    )
                                }
                                val redKey = countValueRed.keys
                                val redSB = StringBuilder()
                                for (rk in redKey) {
                                    CVR.add(ValueBean(rk, countValueRed[rk]!!))
                                    redSB.append("${rk}(${countValueRed[rk]}),")
                                }
                                val blueKey = countValueBlue.keys
                                val blueSB = StringBuilder()
                                for (bk in blueKey) {
                                    CVB.add(ValueBean(bk, countValueBlue[bk]!!))
                                    blueSB.append("${bk}(${countValueBlue[bk]})")
                                }
                                println(redSB)
                                println(blueSB)
                                CVR.sortWith(Comparator { o1, o2 -> o2.count - o1.count })
                                CVB.sortWith(Comparator { o1, o2 -> o2.count - o1.count })
                                val RSB = StringBuilder()
                                for (i in 0..5) {
                                    RSB.append(CVR[i].number).append(",")
                                }
                                RSB.append(CVB[0].number)
                                maxLottery.set(RSB.toString())
                                CVR.sortWith(Comparator { o1, o2 -> o1.count - o2.count })
                                CVB.sortWith(Comparator { o1, o2 -> o1.count - o2.count })
                                val BSB = StringBuilder()
                                for (i in 0..5) {
                                    BSB.append(CVR[i].number).append(",")
                                }
                                BSB.append(CVB[0].number)
                                minLottery.set(BSB.toString())
                                println(CVR)
                                println(CVB)
                            } else {
                                ui.value = 2
                            }
                        } else {
                            if (page == 1) {
                                ui.value = 0
                            } else {
                                ui.value = 2
                            }
                            ToastUtils.showShort(result.reason)
                        }
                    } else {
                        if (page == 1) {
                            ui.value = 0
                        } else {
                            ui.value = 2
                        }
                    }
                }
            })
    }
}