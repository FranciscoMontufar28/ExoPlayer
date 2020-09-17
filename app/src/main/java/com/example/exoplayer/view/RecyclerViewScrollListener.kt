package com.example.exoplayer.view

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

abstract class RecyclerViewScrollListener: RecyclerView.OnScrollListener() {

    private var firstVisibleItem = 0
    private var visibleItemCount = 0

    @Volatile
    private var mEnabled = true
    private var mPreLoadCount = 0
    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
            val manager = recyclerView.layoutManager
            require(manager is LinearLayoutManager) { "Expected recyclerview to have linear layout manager" }
            visibleItemCount = manager.childCount
            firstVisibleItem = manager.findFirstCompletelyVisibleItemPosition()
            onItemIsFirstVisibleItem(firstVisibleItem, recyclerView.childCount)
    }

    /**
     * Called when end of scroll is reached.
     *
     * @param recyclerView - related recycler view.
     */
    abstract fun onItemIsFirstVisibleItem(index: Int, recyclerSize: Int)
}