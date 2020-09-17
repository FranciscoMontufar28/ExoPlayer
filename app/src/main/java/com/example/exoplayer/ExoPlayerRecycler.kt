package com.example.exoplayer

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.SnapHelper
import com.example.exoplayer.data.model.Media.Companion.getData
import com.example.exoplayer.util.VideoPlayerObject.Companion.playIndexThenPausePreviousPlayer
import com.example.exoplayer.view.RecyclerViewAdapter
import com.example.exoplayer.view.RecyclerViewScrollListener
import kotlinx.android.synthetic.main.activity_exo_player_recycler.*


class ExoPlayerRecycler : AppCompatActivity() {

    private val instaAdapter by lazy { RecyclerViewAdapter() }
    private lateinit var scrollListener: RecyclerViewScrollListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exo_player_recycler)
        initRecyclerView()
    }

    private fun initRecyclerView() {
        rv_video_player.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = instaAdapter
            instaAdapter.setData(getData())
            scrollListener = object : RecyclerViewScrollListener() {
                override fun onItemIsFirstVisibleItem(index: Int, recyclerSize: Int) {
                    Log.d("visible item index", index.toString())
                    playIndexThenPausePreviousPlayer(index, recyclerSize)
                }

            }
            addOnScrollListener(scrollListener)
        }
        LinearSnapHelper().attachToRecyclerView(rv_video_player)
    }
}