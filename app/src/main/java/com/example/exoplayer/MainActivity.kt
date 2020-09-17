package com.example.exoplayer

import android.content.Context
import android.content.pm.ActivityInfo
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.dash.DashMediaSource
import com.google.android.exoplayer2.source.dash.DefaultDashChunkSource
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.custom_controller.*

class MainActivity : AppCompatActivity() {

    lateinit var simpleExoPlayer: SimpleExoPlayer
    var flag = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //Make activity full screen
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN
            , WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        initializePlayer(this)
        val videoUrl = Uri.parse("https://i.imgur.com/7bMqysJ.mp4")
        val mediaSource = buildMediaSource(videoUrl)
        releasePlayer(mediaSource)
        onFullScreenAction()
    }

    private fun onFullScreenAction() {
        bt_fullscreen.setOnClickListener {
            if (flag) {
                bt_fullscreen.setImageDrawable(resources.getDrawable(R.drawable.ic_fullscreen))
                requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                flag = false
            } else {
                bt_fullscreen.setImageDrawable(resources.getDrawable(R.drawable.ic_fullscreen_exit))
                requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                flag = true
            }
        }
    }

    private fun initializePlayer(context: Context) {
        val trackSelector =
            DefaultTrackSelector(AdaptiveTrackSelection.Factory(DefaultBandwidthMeter()))
        simpleExoPlayer =
            ExoPlayerFactory.newSimpleInstance(context, trackSelector, DefaultLoadControl())
        player_view.apply {
            player = simpleExoPlayer
            keepScreenOn = true
        }
    }

    private fun buildMediaSource(uri: Uri): MediaSource {
        val userAgent = "exoplayer_video"
        return if (uri.lastPathSegment?.contains("mp3")!! || uri.lastPathSegment?.contains("mp4")!!) {
            ExtractorMediaSource.Factory(DefaultHttpDataSourceFactory(userAgent))
                .createMediaSource(uri)
        } else if (uri.lastPathSegment?.contains("m3u8")!!) {
            HlsMediaSource.Factory(DefaultHttpDataSourceFactory(userAgent))
                .createMediaSource(uri)
        } else {
            val dashChunkSourceFactory = DefaultDashChunkSource.Factory(
                DefaultHttpDataSourceFactory("ua", DefaultBandwidthMeter())
            )
            val manifestDataSourceFactory = DefaultHttpDataSourceFactory(userAgent)
            DashMediaSource.Factory(dashChunkSourceFactory, manifestDataSourceFactory)
                .createMediaSource(uri)
        }
    }

    private fun releasePlayer(mediaSource: MediaSource) {
        simpleExoPlayer.apply {
            this.prepare(mediaSource)
            playWhenReady = true
            this.addListener(object : Player.EventListener {
                override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                    if (playbackState == Player.STATE_BUFFERING) {
                        progress_bar.visibility = View.VISIBLE
                    } else if (playbackState == Player.STATE_READY) {
                        progress_bar.visibility = View.GONE
                    }
                }
            })
        }
    }

    override fun onPause() {
        super.onPause()
        simpleExoPlayer.playWhenReady = false
        simpleExoPlayer.playbackState
    }

    override fun onRestart() {
        super.onRestart()
        simpleExoPlayer.playWhenReady = true
        simpleExoPlayer.playbackState
    }
}