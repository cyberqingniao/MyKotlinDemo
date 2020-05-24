package com.yjp.easytools.databing.viewadapter

import android.annotation.SuppressLint
import android.content.Context
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.webkit.WebView
import android.widget.*
import androidx.core.widget.NestedScrollView
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableList
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.jakewharton.rxbinding2.view.RxView
import com.yjp.easytools.databing.command.BindingCommand
import com.yjp.easytools.databing.viewadapter.recyclerview.LineManagers
import com.yjp.easytools.databing.viewadapter.recyclerview.OnScrollListener
import com.yjp.easytools.databing.viewadapter.recyclerview.ScrollDataWrapper
import com.yjp.easytools.databing.viewadapter.scrollview.NestScrollDataWrapper
import com.yjp.easytools.databing.viewadapter.spinner.IKeyAndValue
import com.yjp.easytools.databing.viewadapter.viewgroup.IBindingItemViewModel
import com.yjp.easytools.utils.StringUtils
import me.tatarka.bindingcollectionadapter2.ItemBinding
import java.util.concurrent.TimeUnit

/**
 * ViewAdapter$
 *
 * @author yjp
 * @date 2020-05-16 13:09
 */
/**
 * CheckBox 选择状态监听
 */
@BindingAdapter(value = ["onCheckChangedCommand"], requireAll = false)
fun setCheckedChanged(checkBox: CheckBox, bindingCommand: BindingCommand<Boolean>) {
    checkBox.setOnCheckedChangeListener { buttonView: CompoundButton?, isChecked: Boolean ->
        bindingCommand.execute(isChecked)
    }
}

/**
 * EditText 焦点改变监听
 */
@BindingAdapter(value = ["requestFocus"], requireAll = false)
fun requestFocusCommand(editText: EditText, needRequestFocus: Boolean) {
    if (needRequestFocus) {
        editText.setSelection(editText.text.length)
        editText.requestFocus()
        var imm =
            editText.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT)
    }
    editText.isFocusableInTouchMode = needRequestFocus
}

/**
 * EditText 文字输入时监听
 */
@BindingAdapter(value = ["textChanged"], requireAll = false)
fun addTextChangedListener(editText: EditText, textChanged: BindingCommand<String>?) {
    editText.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(
            charSequence: CharSequence,
            i: Int,
            i1: Int,
            i2: Int
        ) {
        }

        override fun onTextChanged(
            text: CharSequence,
            i: Int,
            i1: Int,
            i2: Int
        ) {
            textChanged?.execute(text.toString())
        }

        override fun afterTextChanged(editable: Editable) {
        }
    })
}

/**
 * EditText 文字输入后监听
 */
@BindingAdapter(value = ["afterTextChangedCommand"], requireAll = false)
fun afterTextChangedListener(editText: EditText, textChanged: BindingCommand<String>?) {
    editText.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(
            charSequence: CharSequence,
            i: Int,
            i1: Int,
            i2: Int
        ) {
        }

        override fun onTextChanged(
            text: CharSequence,
            i: Int,
            i1: Int,
            i2: Int
        ) {
        }

        override fun afterTextChanged(editable: Editable) {
            textChanged?.execute(editable.toString())
        }
    })
}

/**
 * ImageView 设置图片
 */
@BindingAdapter(value = ["url", "placeholderRes"], requireAll = false)
fun setImageUri(imageView: ImageView, url: String?, placeholderRes: Int) {
    if (StringUtils.isEmpty(url) && placeholderRes > 0) {
        imageView.setImageResource(placeholderRes)
    } else if (!StringUtils.isEmpty(url)) {
        if (placeholderRes > 0) {
            Glide.with(imageView.context)
                .load(url)
                .apply(RequestOptions().placeholder(placeholderRes))
                .into(imageView)
        } else {
            Glide.with(imageView.context)
                .load(url)
                .into(imageView)
        }
    }
}

/**
 * RadioButton 选择状态
 */
@BindingAdapter(value = ["onCheckChangedCommand"], requireAll = false)
fun setCheckedChanged(radioButton: RadioButton, bindingCommand: BindingCommand<Boolean>) {
    radioButton.setOnCheckedChangeListener { buttonView: CompoundButton?, isChecked: Boolean ->
        bindingCommand.execute(isChecked)
    }
}

/**
 * RadioGroup 选择监听
 */
@BindingAdapter(value = ["onCheckedChangedCommand"], requireAll = false)
fun onCheckedChangedCommand(radioGroup: RadioGroup, bindingCommand: BindingCommand<String>) {
    radioGroup.setOnCheckedChangeListener { group, checkedId ->
        val radioButton = group.findViewById<RadioButton>(checkedId)
        if (radioButton != null && radioButton.text != null) {
            bindingCommand.execute(radioButton.text.toString())
        }
    }
}

/**
 * RecyclerView 设置分割线
 */
@BindingAdapter("lineManager")
fun setLineManager(
    recyclerView: RecyclerView,
    lineManagerFactory: LineManagers.LineManagerFactory
) {
    recyclerView.addItemDecoration(lineManagerFactory.create(recyclerView))
}

/**
 * RecyclerView  滑动状态监听
 */
@BindingAdapter(
    value = ["onScrollChangeCommand", "onScrollStateChangedCommand"],
    requireAll = false
)
fun onScrollChangeCommand(
    recyclerView: RecyclerView,
    onScrollChangeCommand: BindingCommand<ScrollDataWrapper>,
    onScrollStateChangedCommand: BindingCommand<Int>
) {
    recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
        private var state: Int = 0
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            onScrollChangeCommand.execute(ScrollDataWrapper(dx.toFloat(), dy.toFloat(), state))
        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            state = newState
            onScrollStateChangedCommand.execute(newState)
        }
    })
}

/**
 * RecyclerView 上拉监听
 */
@BindingAdapter("onLoadMoreCommand")
fun onLoadMoreCommand(recyclerView: RecyclerView, onLoadMoreCommand: BindingCommand<Int>) {
    val listener = OnScrollListener(onLoadMoreCommand)
    recyclerView.addOnScrollListener(listener)
}

/**
 * RecyclerView item动画
 */
@BindingAdapter("itemAnimator")
fun setItemAnimator(recyclerView: RecyclerView, animator: RecyclerView.ItemAnimator) {
    recyclerView.itemAnimator = animator
}

/**
 * ScrollView 滑动监听
 */
@BindingAdapter("onScrollChangeCommand")
fun onScrollChangeCommand(
    nestedScrollView: NestedScrollView,
    onScrollChangeCommand: BindingCommand<NestScrollDataWrapper>
) {
    nestedScrollView.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
        onScrollChangeCommand.execute(
            NestScrollDataWrapper(scrollX, scrollY, oldScrollX, oldScrollY)
        )
    })
}

/**
 * Spinner 下拉选择框
 */
@BindingAdapter(
    value = ["itemDatas", "valueReply", "resource", "dropDownResource", "onItemSelectedCommand"],
    requireAll = false
)
fun onItemSelectedCommand(
    spinner: Spinner,
    itemDatas: List<IKeyAndValue>,
    valueReply: String,
    resource: Int,
    dropDownResource: Int,
    bindingCommand: BindingCommand<IKeyAndValue>
) {
    var lists = mutableListOf<String>()
    for (iKeyAndValue in itemDatas) {
        lists.add(iKeyAndValue.getKey())
    }
    var resId = if (resource == 0) {
        android.R.layout.simple_spinner_item
    } else {
        resource
    }
    var dropResId = if (dropDownResource == 0) {
        android.R.layout.simple_spinner_dropdown_item
    } else {
        dropDownResource
    }
    var adapter = ArrayAdapter<String>(spinner.context, resId, lists)
    adapter.setDropDownViewResource(dropResId)
    spinner.adapter = adapter
    spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
        override fun onNothingSelected(parent: AdapterView<*>?) {

        }

        override fun onItemSelected(
            parent: AdapterView<*>?,
            view: View?,
            position: Int,
            id: Long
        ) {
            var iKAV = itemDatas[position]
            bindingCommand.execute(iKAV)
        }

    }
    if (!TextUtils.isEmpty(valueReply)) {
        for (i in itemDatas.indices) {
            var iKAV = itemDatas[i]
            if (valueReply == iKAV.getValue()) {
                spinner.setSelection(i)
                return
            }
        }
    }
}

/**
 * SwipeRefresh 下拉刷新
 */
@BindingAdapter("onRefreshCommand")
fun onRefreshCommand(swipeRefreshLayout: SwipeRefreshLayout, onRefreshCommand: BindingCommand<*>) {
    swipeRefreshLayout.setOnRefreshListener {
        onRefreshCommand.execute()
    }
}

/**
 * SwipeRefresh 刷新监听
 */
@BindingAdapter("refreshing")
fun setRefreshing(swipeRefreshLayout: SwipeRefreshLayout, refreshing: Boolean) {
    swipeRefreshLayout.isRefreshing = refreshing
}

/**
 * Switch 状态设置
 */
@BindingAdapter("switchState")
fun setSwitchState(mSwitch: Switch, isChecked: Boolean) {
    mSwitch.isChecked = isChecked
}

/**
 * Switch 状态监听
 */
@BindingAdapter("onCheckedChangeCommand")
fun onCheckedChangeCommand(mSwitch: Switch, changeListener: BindingCommand<Boolean>) {
    mSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
        changeListener.execute(isChecked)
    }
}

/**
 * View
 */
//防重复点击间隔(秒)
const val CLICK_INTERVAL = 1

/**
 * 点击事件
 */
@SuppressLint("CheckResult")
@BindingAdapter(value = ["onClickCommand", "isThrottleFirst"], requireAll = false)
fun onClickCommand(view: View, clickCommand: BindingCommand<*>, isThrottleFirst: Boolean) {
    if (isThrottleFirst) {
        RxView.clicks(view)
            .subscribe {
                clickCommand.execute()
            }
    } else {
        RxView.clicks(view)
            .throttleFirst(CLICK_INTERVAL.toLong(), TimeUnit.SECONDS)
            .subscribe {
                clickCommand.execute()
            }
    }
}

/**
 * 长按事件
 */
@SuppressLint("CheckResult")
@BindingAdapter(value = ["onLongClickCommand"], requireAll = false)
fun onLongClickCommand(view: View, clickCommand: BindingCommand<*>) {
    RxView.longClicks(view)
        .subscribe {
            clickCommand.execute()
        }
}

/**
 * 当前View
 */
@BindingAdapter(value = ["currentView"], requireAll = false)
fun replyCurrentView(currentView: View, bindingCommand: BindingCommand<View>) {
    bindingCommand.execute(currentView)
}

/**
 * 设置焦点
 */
@BindingAdapter("requestFocus")
fun requestFocusCommand(view: View, needRequestFocus: Boolean) {
    if (needRequestFocus) {
        view.isFocusableInTouchMode = true
        view.requestFocus()
    } else {
        view.clearFocus()
    }
}

/**
 * 焦点状态监听
 */
@BindingAdapter("onFocusChangeCommand")
fun onFocusChangeCommand(view: View, onFocusChangeCommand: BindingCommand<Boolean>) {
    view.setOnFocusChangeListener { v, hasFocus ->
        run {
            onFocusChangeCommand.execute(hasFocus)
        }
    }
}

/**
 * View是否可视
 */
@BindingAdapter("isVisible")
fun isVisible(view: View, visibility: Boolean) {
    view.visibility = if (visibility) {
        View.VISIBLE
    } else {
        View.GONE
    }
}

/**
 * ViewGroup item选择监听
 */
@BindingAdapter("itemView", "observableList")
fun addViews(
    viewGroup: ViewGroup,
    itemBinding: ItemBinding<*>,
    viewModelList: ObservableList<IBindingItemViewModel<ViewDataBinding>>
) {
    if (!viewModelList.isEmpty()) {
        viewGroup.removeAllViews()
        for (viewModel in viewModelList) {
            val binding = DataBindingUtil.inflate(
                LayoutInflater.from(viewGroup.context),
                itemBinding.layoutRes(),
                viewGroup,
                true
            ) as ViewDataBinding
            binding.setVariable(itemBinding.variableId(), viewModel)
            viewModel.injecDataBinding(binding)
        }
    }
}

/**
 * ViewPager 滑动事件监听
 */
@BindingAdapter(
    value = ["onPageScrolledCommand", "onPageSelectedCommand", "onPageScrollStateChangedCommand"],
    requireAll = false
)
fun onScrollChangeCommand(
    viewPager: ViewPager,
    onPageScrolledCommand: BindingCommand<com.yjp.easytools.databing.viewadapter.viewpager.ViewPagerDataWrapper>?,
    onPageSelectedCommand: BindingCommand<Int>?,
    onPageScrollStateChangedCommand: BindingCommand<Int>?
) {
    viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
        private var state: Int = 0
        override fun onPageScrollStateChanged(state: Int) {
            this.state = state
            onPageScrollStateChangedCommand?.execute(state)
        }

        override fun onPageScrolled(
            position: Int,
            positionOffset: Float,
            positionOffsetPixels: Int
        ) {
            onPageScrolledCommand?.execute(
                com.yjp.easytools.databing.viewadapter.viewpager.ViewPagerDataWrapper(
                    position,
                    positionOffset,
                    positionOffsetPixels,
                    state
                )
            )
        }

        override fun onPageSelected(position: Int) {
            onPageSelectedCommand?.execute(position)
        }

    })
}

/**
 * WebView 加载URL
 */
@BindingAdapter("render")
fun loadHtml(webView: WebView, html: String) {
    webView.loadDataWithBaseURL(null, html, "text/html", "UTF-8", null)
}
