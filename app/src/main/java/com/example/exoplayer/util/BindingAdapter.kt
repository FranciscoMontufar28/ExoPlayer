package com.example.exoplayer.util

import android.net.Uri
import android.widget.Toast
import androidx.databinding.BindingAdapter
import com.example.exoplayer.view.PlayerStateCallback
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory

class VideoPlayerObject {

    companion object {
        // for hold all players generated
        private var playersMap: MutableMap<Int, SimpleExoPlayer> = mutableMapOf()

        // for hold current player
        private var currentPlayingVideo: Pair<Int, SimpleExoPlayer>? = null

        fun playIndexThenPausePreviousPlayer(index: Int, recyclerSize: Int) {
            for (item in 0..playersMap.size) {
                if (item != index) {
                    if (playersMap[item] != null)
                        playersMap[item]!!.playWhenReady = false
                } else {
                    if (playersMap[item] != null)
                        playersMap[item]!!.playWhenReady = true
                }

            }
        }

        @JvmStatic
        @BindingAdapter("video_url", "on_state_change", "item_index")
        fun PlayerView.loadVideo(url: String, callback: PlayerStateCallback, item_index: Int) {
            if (url == null) return
            val player: SimpleExoPlayer = ExoPlayerFactory.newSimpleInstance(
                context,
                DefaultRenderersFactory(context),
                DefaultTrackSelector(),
                DefaultLoadControl()
            )

            player.playWhenReady = true
            player.repeatMode = Player.REPEAT_MODE_ALL
            // When changing track, retain the latest frame instead of showing a black screen
            setKeepContentOnPlayerReset(true)
            // We'll show the controller
            this.useController = true
            // Provide url to load the video from here
            val mediaSource = ExtractorMediaSource.Factory(
                DefaultHttpDataSourceFactory("DemoAgent")
            ).createMediaSource(Uri.parse(url))

            player.prepare(mediaSource)

            this.player = player

            // add player with its index to map
            if (playersMap.containsKey(item_index))
                playersMap.remove(item_index)
            if (item_index != null)
                playersMap[item_index] = player

            this.player!!.addListener(object : Player.EventListener {

                override fun onPlayerError(error: ExoPlaybackException) {
                    super.onPlayerError(error)
                    Toast.makeText(
                        this@loadVideo.context,
                        "Oops! Error occurred while playing media.",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                    super.onPlayerStateChanged(playWhenReady, playbackState)
                    if (playbackState == Player.STATE_BUFFERING) callback.onVideoBuffering(player)
                    if (playbackState == Player.STATE_READY) {
                        callback.onVideoDurationRetrieved(
                            (this@loadVideo.player as SimpleExoPlayer).duration,
                            player
                        )
                    }
                    if (playbackState == Player.STATE_READY && player.playWhenReady) {
                        callback.onStartedPlaying(player)
                    }
                }
            })
        }
    }
}



