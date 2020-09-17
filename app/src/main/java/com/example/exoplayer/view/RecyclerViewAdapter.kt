package com.example.exoplayer.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.exoplayer.R
import com.example.exoplayer.data.model.Media
import com.example.exoplayer.databinding.ItemVideoBinding
import com.google.android.exoplayer2.Player

class RecyclerViewAdapter : RecyclerView.Adapter<RecyclerViewAdapter.VideoViewHolder>(),
    PlayerStateCallback {

    private var mList: List<Media> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        val binding = DataBindingUtil.inflate<ItemVideoBinding>(
            LayoutInflater.from(parent.context)
            , R.layout.item_video, parent, false
        )
        return VideoViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        val itemVideo = mList[position]
        with(holder.view){
            mediaData = itemVideo
            callback = this@RecyclerViewAdapter
            index = position
            executePendingBindings()
        }
    }

    fun setData(data: List<Media>) {
        mList = data
        notifyDataSetChanged()
    }

    inner class VideoViewHolder(val view: ItemVideoBinding) : RecyclerView.ViewHolder(view.root)

    override fun onVideoDurationRetrieved(duration: Long, player: Player) {
    }

    override fun onVideoBuffering(player: Player) {
    }

    override fun onStartedPlaying(player: Player) {
    }

    override fun onFinishedPlaying(player: Player) {
    }

}