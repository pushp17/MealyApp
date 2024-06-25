package com.eat_healthy.tiffin.adapter

import android.os.Handler
import android.os.Looper
import android.os.Message
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.eat_healthy.tiffin.databinding.HomePageViewpagerType1Binding
import com.eat_healthy.tiffin.databinding.HomePageViewpagerType2Binding
import com.eat_healthy.tiffin.models.HomeHighLightedItems
import com.eat_healthy.tiffin.viewHolder.HomePageViewpagerHolderType1
import com.eat_healthy.tiffin.viewHolder.HomePageViewpagerHolderType2
import java.lang.ref.WeakReference

class ViewPagerHomeAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    val VIEW_TYPE_1 = 1
    val VIEW_TYPE_2 = 2
    protected val mutableItemList = arrayListOf<HomeHighLightedItems>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_1 -> HomePageViewpagerHolderType1(
                HomePageViewpagerType1Binding.inflate(
                    LayoutInflater.from(
                        parent.context
                    ), parent, false
                ),
                parent.context
            )

            VIEW_TYPE_2 -> HomePageViewpagerHolderType2(
                HomePageViewpagerType2Binding.inflate(
                    LayoutInflater.from(
                        parent.context
                    ), parent, false
                ),
                parent.context
            )

            else -> HomePageViewpagerHolderType1(
                HomePageViewpagerType1Binding.inflate(
                    LayoutInflater.from(
                        parent.context
                    ), parent, false
                ),
                parent.context
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (mutableItemList.isNotEmpty()) {
            return when (getItemViewType(position)) {
                VIEW_TYPE_1 -> (holder as HomePageViewpagerHolderType1).bind(
                    position,
                    mutableItemList[position] as HomeHighLightedItems
                )

                VIEW_TYPE_2 -> (holder as HomePageViewpagerHolderType2).bind(
                    position,
                    mutableItemList[position] as HomeHighLightedItems
                )

                else -> (holder as HomePageViewpagerHolderType1).bind(
                    position,
                    mutableItemList[position] as HomeHighLightedItems
                )
            }
        }
    }
    fun setItems(listItem: List<HomeHighLightedItems>) {
        mutableItemList.clear()
        mutableItemList.addAll(listItem)
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return if(mutableItemList.get(position).fullImage) VIEW_TYPE_1 else VIEW_TYPE_2
    }

    override fun getItemCount() = mutableItemList.size

    class ScrollingHandler(viewPager: ViewPager2) : Handler(Looper.getMainLooper()) {
        private val viewPagerRef: WeakReference<ViewPager2> = WeakReference(viewPager)
        var interval = DEFAULT_SCROLL_INTERVAL_MILLIS
        var isAutoScrolling: Boolean = false

        override fun handleMessage(msg: Message) {
            val loopingViewPager = viewPagerRef.get()
            if (msg.what == SCROLL_WHAT && loopingViewPager != null) {
                loopingViewPager.currentItem = loopingViewPager.currentItem + 1
            }
            startAutoScroll()
        }

        fun startAutoScroll() {
            isAutoScrolling = true
            removeMessages(SCROLL_WHAT) // to strictly queue only one message
            sendEmptyMessageDelayed(SCROLL_WHAT, interval.toLong())
        }

        fun stopAutoScroll() {
            isAutoScrolling = false
            removeMessages(SCROLL_WHAT)
        }

        companion object {
            private const val DEFAULT_SCROLL_INTERVAL_MILLIS = 4000
            private const val SCROLL_WHAT = 101
        }
    }


    override fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder) {
        super.onViewDetachedFromWindow(holder)
    }
}