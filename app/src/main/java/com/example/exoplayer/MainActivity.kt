package com.example.exoplayer

import android.content.pm.ActivityInfo
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import com.google.android.exoplayer2.DefaultLoadControl
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory
import com.google.android.exoplayer2.source.ExtractorMediaSource
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
        val videoUrl = Uri.parse("https://i.imgur.com/7bMqysJ.mp4")
        //Initialize load control
        val trackSelector =
            DefaultTrackSelector(AdaptiveTrackSelection.Factory(DefaultBandwidthMeter()))
        //Initialize Player
        simpleExoPlayer =
            ExoPlayerFactory.newSimpleInstance(this, trackSelector, DefaultLoadControl())
        
        val factory = DefaultHttpDataSourceFactory("exoplayer_video")
        val mediaSource =
            ExtractorMediaSource(videoUrl, factory, DefaultExtractorsFactory(), null, null)
        player_view.apply {
            player = simpleExoPlayer
            keepScreenOn = true
        }
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