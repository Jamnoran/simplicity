package com.simplicity.simplicityaclientforreddit.utils.media

import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.util.Log
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.simplicity.simplicityaclientforreddit.R
import com.simplicity.simplicityaclientforreddit.databinding.MediaCustomPlayerBinding
import com.simplicity.simplicityaclientforreddit.ui.main.models.external.RedditPost
import com.simplicity.simplicityaclientforreddit.ui.main.usecases.GetMediaHeightUseCase
import com.simplicity.simplicityaclientforreddit.ui.main.usecases.MediaBaseValues
import com.simplicity.simplicityaclientforreddit.ui.main.usecases.MediaData
import java.io.IOException


class CustomPlayer(var binding: MediaCustomPlayerBinding, var data: RedditPost.Data, var context: Context) {
    private val TAG = "CustomPlayer"
    private lateinit var videoMediaPlayer: MediaPlayer
    private var audioMediaPlayer: MediaPlayer? = null
    private var videoUrl: String? = null
    private lateinit var audioUrl: String
    private var hasAudio: Boolean? = null
    private var muted = false
    private var hasPreparedVideo = false
    private var hasPreparedAudio = false
    private var videoHeight: Int = 0
    private var videoWidth: Int = 0

    fun init() {
        parseUrls()
        binding.playButton.visibility = View.GONE
        setUpListeners()
        checkIfVideoHasAudio()
        // initializing video player
        initVideo()
    }

    fun getVideoParams(): ConstraintLayout.LayoutParams{
        val mediaBaseValues = GetMediaHeightUseCase(data, MediaData(videoUrl?: "", 0.0f, MediaBaseValues(videoWidth, videoHeight))).execute()
        return ConstraintLayout.LayoutParams(mediaBaseValues.mediaWidth, mediaBaseValues.mediaHeight)
    }

    private fun checkIfVideoHasAudio() {
        val queue = Volley.newRequestQueue(binding.root.context)
        // Request a string response from the provided URL.
        val stringRequest = StringRequest(Request.Method.GET, audioUrl,
            {
                Log.i(TAG, "Volley request success")
                hasAudio = true
                // initializing media player
                initAudio()
                tryPlayVideo()
            },
            {
                Log.i(TAG, "Volley request failed")
                hasAudio = false
                hasPreparedAudio  = true
                binding.muteButton.visibility = View.GONE
                tryPlayVideo()
            }
        )

        // Add the request to the RequestQueue.
        queue.add(stringRequest)
    }

    private fun tryPlayVideo() {
        if(hasPreparedVideo){
            hasAudio?.let {
                binding.customPlayer.start()
                if(it){
                    audioMediaPlayer?.start()
                }else{
                    audioMediaPlayer?.start()
                }
            }
        }
    }

    private fun muteAudio() {
        muted = true
        binding.muteButton.setImageResource(R.drawable.ic_mute)
        audioMediaPlayer?.setVolume(0.0f, 0.0f)
    }

    private fun unMuteAudio() {
        muted = false
        binding.muteButton.setImageResource(R.drawable.ic_audio)
        audioMediaPlayer?.setVolume(1.0f, 1.0f)
    }

    private fun setUpListeners() {
//        binding.customPlayerControllers.setOnClickListener {
//            pause()
//        }
        binding.playButton.setOnClickListener {
            play()
        }
        binding.muteButton.setOnClickListener {
            if(muted){
                unMuteAudio()
            }else{
                muteAudio()
            }
        }
        binding.restartButton.setOnClickListener {
            restartVideo()
        }
    }

    private fun restartVideo() {
        videoMediaPlayer.seekTo(0)
//        audioMediaPlayer?.seekTo(0)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) audioMediaPlayer?.seekTo(
            0,
            MediaPlayer.SEEK_CLOSEST
        ) else audioMediaPlayer?.seekTo(0)
    }

    private fun parseUrls() {
        data.preview?.reddit_video_preview?.fallback_url?.let{
            videoUrl = it
            videoHeight = data.preview?.reddit_video_preview?.height!!
            videoWidth = data.preview?.reddit_video_preview?.width!!
            Log.i(TAG, "Setting video from reddit_video_preview $videoUrl")
        }
        data.media?.reddit_video?.fallback_url?.let{
            if(videoUrl == null){
                videoUrl = it
                videoHeight = data.media?.reddit_video?.height!!
                videoWidth = data.media?.reddit_video?.width!!
                Log.i(TAG, "Setting video from reddit_video $videoUrl")
            }
        }
        data.secureMediaEmbed?.media_domain_url?.let{
            if(videoUrl == null) {
                videoUrl = it
                videoHeight = data.secureMediaEmbed?.height!!
                videoWidth = data.secureMediaEmbed?.width!!
                Log.i(TAG, "Setting video from secureMediaEmbed $videoUrl")
            }
        }
        videoUrl?.let{
            audioUrl = VideoHelper.getAudioUrl(it)
        }
    }

    private fun initVideo() {
        //specify the location of media file
        val uri = Uri.parse(videoUrl)

        //Setting MediaController and URI, then starting the videoView
        binding.customPlayer.setVideoURI(uri)
        binding.customPlayer.requestFocus()
        binding.customPlayer.setOnPreparedListener {
            videoMediaPlayer = it
            hasPreparedVideo = true
            tryPlayVideo()
        }
        binding.customPlayer.setOnCompletionListener {
//            it.start()
//            audioMediaPlayer?.start()

            binding.playButton.visibility = View.VISIBLE
        }
//        videoView.setOnInfoListener { mediaPlayer, what, extra ->
//            if(what == MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START){
//                Log.i(TAG, "Starting audio")
////                mediaPlayer.start()
//            }
//            false
//        }
    }

    private fun initAudio() {
        // initializing media player
        audioMediaPlayer = MediaPlayer()

        // below line is use to set the audio
        // stream type for our media player.
        audioMediaPlayer?.setAudioStreamType(AudioManager.STREAM_MUSIC)

        // below line is use to set our
        // url to our media player.
        try {
            audioMediaPlayer?.setDataSource(audioUrl)
            // below line is use to prepare
            // and start our media player.
            audioMediaPlayer?.prepare()
            audioMediaPlayer?.setOnPreparedListener {
                hasPreparedAudio = true
//                unMuteAudio()
                tryPlayVideo()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        muteAudio()
    }

    private fun pause(){
        binding.playButton.visibility = View.VISIBLE
        videoMediaPlayer.pause()
        try {
            audioMediaPlayer?.pause()
        } catch (e: Exception) {
        }
    }

    private fun play(){
        binding.playButton.visibility = View.GONE
        videoMediaPlayer.start()
        try {
            audioMediaPlayer?.start()
        } catch (e: Exception) {
        }
    }
}
