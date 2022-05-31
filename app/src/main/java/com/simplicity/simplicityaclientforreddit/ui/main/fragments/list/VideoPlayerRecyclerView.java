package com.simplicity.simplicityaclientforreddit.ui.main.fragments.list;


import android.content.Context;
import android.graphics.Point;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.RequestManager;
import com.google.android.exoplayer2.DeviceInfo;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.MediaMetadata;
import com.google.android.exoplayer2.PlaybackException;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.TracksInfo;
import com.google.android.exoplayer2.audio.AudioAttributes;
import com.google.android.exoplayer2.metadata.Metadata;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.text.Cue;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelectionParameters;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.video.VideoSize;
import com.simplicity.simplicityaclientforreddit.R;
import com.simplicity.simplicityaclientforreddit.base.BasePostsListViewModel;
import com.simplicity.simplicityaclientforreddit.ui.main.fragments.list.util.PostViewHolder;
import com.simplicity.simplicityaclientforreddit.ui.main.models.external.posts.RedditPost;
import com.simplicity.simplicityaclientforreddit.ui.main.usecases.GetMediaDataUseCase;
import com.simplicity.simplicityaclientforreddit.ui.main.usecases.HasPostVideoUseCase;
import com.simplicity.simplicityaclientforreddit.ui.main.usecases.MediaData;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class VideoPlayerRecyclerView extends RecyclerView {

    private static final String TAG = "VideoPlayerRecyclerView";

    private enum VolumeState {ON, OFF};

    private BasePostsListViewModel _viewModel = null;

    // ui
    private ImageView thumbnail, volumeControl;
    private ProgressBar progressBar;
    private View viewHolderParent;
    private FrameLayout frameLayout;
    private PlayerView videoSurfaceView;
    private ExoPlayer videoPlayer;

    // vars
    private ArrayList<RedditPost> _posts = new ArrayList<>();
    private int videoSurfaceDefaultHeight = 0;
    private int screenDefaultHeight = 0;
    private Context context;
    private int playPosition = -1;
    private boolean isVideoViewAdded;
    private RequestManager _requestManager;

    // controlling playback state
    private VolumeState volumeState;

    // Audio
    private MediaPlayer audioMediaPlayer = null;

    public VideoPlayerRecyclerView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public VideoPlayerRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public void setViewModel(@NotNull BasePostsListViewModel viewModel) {
        _viewModel = viewModel;
    }

    public void setRequestManager(@org.jetbrains.annotations.Nullable RequestManager initGlide) {
        _requestManager = initGlide;
    }

    public void set_posts(ArrayList<RedditPost> posts){
        this._posts = posts;
    }

    public void pauseVideo() {
        videoPlayer.pause();
    }

    public void resumeVideo() {
        try {
            videoPlayer.play();
        } catch (Exception e) {
            Log.e(TAG, "Could not resume video");
        }
    }

    private void init(Context context){
        this.context = context.getApplicationContext();
        Display display = ((WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        videoSurfaceDefaultHeight = point.x;
        screenDefaultHeight = point.y;

        videoSurfaceView = new PlayerView(this.context);
        videoSurfaceView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_ZOOM);

        // 2. Create the player
        videoPlayer = new ExoPlayer.Builder(context).build();
        // Bind the player to the view.
        videoSurfaceView.setUseController(false);
        videoSurfaceView.setPlayer(videoPlayer);
        setVolumeControl(VolumeState.ON);

        addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    Log.d(TAG, "onScrollStateChanged: called.");
                    if(thumbnail != null){ // show the old thumbnail
                        thumbnail.setVisibility(VISIBLE);
                    }

                    // There's a special case when the end of the list has been reached.
                    // Need to handle that with this bit of logic
                    playVideo(!recyclerView.canScrollVertically(1));
                    if (!recyclerView.canScrollVertically(1)) {
//                        Toast.makeText(context, "Loading more posts", Toast.LENGTH_LONG).show()
                        if (_viewModel != null) {
                            _viewModel.fetchPosts();
                        }
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });

        addOnChildAttachStateChangeListener(new OnChildAttachStateChangeListener() {
            @Override
            public void onChildViewAttachedToWindow(View view) {

            }

            @Override
            public void onChildViewDetachedFromWindow(View view) {
                if (viewHolderParent != null && viewHolderParent.equals(view)) {
                    resetVideoView();
                }

            }
        });
        videoPlayer.addListener(new Player.Listener() {
            @Override
            public void onEvents(Player player, Player.Events events) {
                Player.Listener.super.onEvents(player, events);
            }

            @Override
            public void onTimelineChanged(Timeline timeline, int reason) {
                Player.Listener.super.onTimelineChanged(timeline, reason);
            }

            @Override
            public void onMediaItemTransition(@Nullable MediaItem mediaItem, int reason) {
                Player.Listener.super.onMediaItemTransition(mediaItem, reason);
            }

            @Override
            public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
                Player.Listener.super.onTracksChanged(trackGroups, trackSelections);
            }

            @Override
            public void onTracksInfoChanged(TracksInfo tracksInfo) {
                Player.Listener.super.onTracksInfoChanged(tracksInfo);
            }

            @Override
            public void onMediaMetadataChanged(MediaMetadata mediaMetadata) {
                Player.Listener.super.onMediaMetadataChanged(mediaMetadata);
            }

            @Override
            public void onPlaylistMetadataChanged(MediaMetadata mediaMetadata) {
                Player.Listener.super.onPlaylistMetadataChanged(mediaMetadata);
            }

            @Override
            public void onIsLoadingChanged(boolean isLoading) {
                Player.Listener.super.onIsLoadingChanged(isLoading);
            }

            @Override
            public void onLoadingChanged(boolean isLoading) {
                Player.Listener.super.onLoadingChanged(isLoading);
            }

            @Override
            public void onAvailableCommandsChanged(Player.Commands availableCommands) {
                Player.Listener.super.onAvailableCommandsChanged(availableCommands);
            }

            @Override
            public void onTrackSelectionParametersChanged(TrackSelectionParameters parameters) {
                Player.Listener.super.onTrackSelectionParametersChanged(parameters);
            }

            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                Player.Listener.super.onPlayerStateChanged(playWhenReady, playbackState);
            }

            @Override
            public void onPlaybackStateChanged(int playbackState) {
                Player.Listener.super.onPlaybackStateChanged(playbackState);
                switch (playbackState) {

                    case Player.STATE_BUFFERING:
                        Log.e(TAG, "onPlayerStateChanged: Buffering video.");
                        if (progressBar != null) {
                            progressBar.setVisibility(VISIBLE);
                        }

                        break;
                    case Player.STATE_ENDED:
                        Log.d(TAG, "onPlayerStateChanged: Video ended.");
                        videoPlayer.seekTo(0);
                        if(audioMediaPlayer != null){
                            audioMediaPlayer.seekTo(0);
                        }
                        break;
                    case Player.STATE_IDLE:

                        break;
                    case Player.STATE_READY:
                        Log.e(TAG, "onPlayerStateChanged: Ready to play.");
                        if (progressBar != null) {
                            progressBar.setVisibility(GONE);
                        }
                        if (!isVideoViewAdded) {
                            addVideoView();
                        }
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onPlayWhenReadyChanged(boolean playWhenReady, int reason) {
                Player.Listener.super.onPlayWhenReadyChanged(playWhenReady, reason);
            }

            @Override
            public void onPlaybackSuppressionReasonChanged(int playbackSuppressionReason) {
                Player.Listener.super.onPlaybackSuppressionReasonChanged(playbackSuppressionReason);
            }

            @Override
            public void onIsPlayingChanged(boolean isPlaying) {
                Player.Listener.super.onIsPlayingChanged(isPlaying);
                Log.i(TAG, "onIsPlayingChanged " + isPlaying);
                if(audioMediaPlayer != null){
                    if(isPlaying){
                        audioMediaPlayer.start();
                    }else{
                        audioMediaPlayer.stop();
                    }
                }
            }

            @Override
            public void onRepeatModeChanged(int repeatMode) {
                Player.Listener.super.onRepeatModeChanged(repeatMode);
            }

            @Override
            public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {
                Player.Listener.super.onShuffleModeEnabledChanged(shuffleModeEnabled);
            }

            @Override
            public void onPlayerError(PlaybackException error) {
                Player.Listener.super.onPlayerError(error);
            }

            @Override
            public void onPlayerErrorChanged(@Nullable PlaybackException error) {
                Player.Listener.super.onPlayerErrorChanged(error);
            }

            @Override
            public void onPositionDiscontinuity(int reason) {
                Player.Listener.super.onPositionDiscontinuity(reason);
            }

            @Override
            public void onPositionDiscontinuity(Player.PositionInfo oldPosition, Player.PositionInfo newPosition, int reason) {
                Player.Listener.super.onPositionDiscontinuity(oldPosition, newPosition, reason);
            }

            @Override
            public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {
                Player.Listener.super.onPlaybackParametersChanged(playbackParameters);
            }

            @Override
            public void onSeekBackIncrementChanged(long seekBackIncrementMs) {
                Player.Listener.super.onSeekBackIncrementChanged(seekBackIncrementMs);
            }

            @Override
            public void onSeekForwardIncrementChanged(long seekForwardIncrementMs) {
                Player.Listener.super.onSeekForwardIncrementChanged(seekForwardIncrementMs);
            }

            @Override
            public void onMaxSeekToPreviousPositionChanged(long maxSeekToPreviousPositionMs) {
                Player.Listener.super.onMaxSeekToPreviousPositionChanged(maxSeekToPreviousPositionMs);
            }

            @Override
            public void onSeekProcessed() {
                Player.Listener.super.onSeekProcessed();
            }

            @Override
            public void onAudioSessionIdChanged(int audioSessionId) {
                Player.Listener.super.onAudioSessionIdChanged(audioSessionId);
            }

            @Override
            public void onAudioAttributesChanged(AudioAttributes audioAttributes) {
                Player.Listener.super.onAudioAttributesChanged(audioAttributes);
            }

            @Override
            public void onVolumeChanged(float volume) {
                Player.Listener.super.onVolumeChanged(volume);
            }

            @Override
            public void onSkipSilenceEnabledChanged(boolean skipSilenceEnabled) {
                Player.Listener.super.onSkipSilenceEnabledChanged(skipSilenceEnabled);
            }

            @Override
            public void onDeviceInfoChanged(DeviceInfo deviceInfo) {
                Player.Listener.super.onDeviceInfoChanged(deviceInfo);
            }

            @Override
            public void onDeviceVolumeChanged(int volume, boolean muted) {
                Player.Listener.super.onDeviceVolumeChanged(volume, muted);
            }

            @Override
            public void onVideoSizeChanged(VideoSize videoSize) {
                Player.Listener.super.onVideoSizeChanged(videoSize);
            }

            @Override
            public void onSurfaceSizeChanged(int width, int height) {
                Player.Listener.super.onSurfaceSizeChanged(width, height);
            }

            @Override
            public void onRenderedFirstFrame() {
                Player.Listener.super.onRenderedFirstFrame();
            }

            @Override
            public void onCues(List<Cue> cues) {
                Player.Listener.super.onCues(cues);
            }

            @Override
            public void onMetadata(Metadata metadata) {
                Player.Listener.super.onMetadata(metadata);
            }
        });
    }

    public void playVideo(boolean isEndOfList) {
        int targetPosition;
        if(!isEndOfList){
            int startPosition = ((LinearLayoutManager) getLayoutManager()).findFirstVisibleItemPosition();
            int endPosition = ((LinearLayoutManager) getLayoutManager()).findLastVisibleItemPosition();

            // if there is more than 2 list-items on the screen, set the difference to be 1
            if (endPosition - startPosition > 1) {
                endPosition = startPosition + 1;
            }

            // something is wrong. return.
            if (startPosition < 0 || endPosition < 0) {
                return;
            }

            // if there is more than 1 list-item on the screen
            if (startPosition != endPosition) {
                int startPositionVideoHeight = getVisibleVideoSurfaceHeight(startPosition);
                int endPositionVideoHeight = getVisibleVideoSurfaceHeight(endPosition);

                targetPosition = startPositionVideoHeight > endPositionVideoHeight ? startPosition : endPosition;
            }
            else {
                targetPosition = startPosition;
            }
        }
        else{
            targetPosition = _posts.size() - 1;
        }

        Log.d(TAG, "playVideo: target position: " + targetPosition);

        // video is already playing so return
        if (targetPosition == playPosition) {
            return;
        }

        // set the position of the list-item that is to be played
        playPosition = targetPosition;
        if (videoSurfaceView == null) {
            return;
        }

        // remove any old surface views from previously playing videos
        videoSurfaceView.setVisibility(INVISIBLE);
        removeVideoView(videoSurfaceView);

        // Clear audio
        clearAudio();

        int currentPosition = targetPosition - ((LinearLayoutManager) getLayoutManager()).findFirstVisibleItemPosition();

        View child = getChildAt(currentPosition);
        if (child == null) {
            return;
        }

        PostViewHolder holder = (PostViewHolder) child.getTag();
        if (holder == null) {
            playPosition = -1;
            return;
        }
        thumbnail = holder.getThumbnail();
        progressBar = holder.getProgressBar();
        volumeControl = holder.getVolumeControl();
        viewHolderParent = holder.itemView;
//        requestManager = holder.requestManager;
//        frameLayout = holder.itemView.findViewById(R.id.media_container);
        frameLayout = holder.getFrameLayout();

        videoSurfaceView.setPlayer(videoPlayer);

        viewHolderParent.setOnClickListener(videoViewClickListener);

        RedditPost post = _posts.get(targetPosition);
        if(new HasPostVideoUseCase().execute(post.getData())){
            String mediaUrl = new GetMediaDataUseCase().execute(post.getData()).getMediaUrl();
            MediaItem mediaItem = MediaItem.fromUri(mediaUrl);
            videoPlayer.setMediaItem(mediaItem);
            videoPlayer.prepare();

            videoPlayer.setPlayWhenReady(true);

            // Prepare audio
            checkIfVideoHasAudio(post.getData());
        }
    }

    private void clearAudio() {
        if (audioMediaPlayer != null) {
            audioMediaPlayer.stop();
            audioMediaPlayer.reset();
        }
    }

    private void initAudio(RedditPost.Data data) {
        MediaData postMediaData = new GetMediaDataUseCase().execute(data);

        // initializing media player
        if (audioMediaPlayer == null) {
            audioMediaPlayer = new MediaPlayer();
        }

        // below line is use to set the audio
        // stream type for our media player.
        audioMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        // below line is use to set our
        // url to our media player.
        try {
            audioMediaPlayer.setDataSource(postMediaData.getAudioUrl());
            // below line is use to prepare
            // and start our media player.
            audioMediaPlayer.prepare();
            audioMediaPlayer.setOnPreparedListener(mediaPlayer -> {
                Log.i(TAG,"Has prepared audio");
                audioMediaPlayer.start();
            });
        } catch (IOException e) {
            Log.e(TAG, "Could not play audio" + e);
        }
    }

    private void checkIfVideoHasAudio(RedditPost.Data data) {
        MediaData postMediaData = new GetMediaDataUseCase().execute(data);
        RequestQueue queue = Volley.newRequestQueue(context);
        // Request a string response from the provided URL.

        StringRequest stringRequest = new StringRequest(Request.Method.GET, postMediaData.getAudioUrl(), response -> {
            Log.i(TAG, "Volley request success");
            initAudio(data);
        }, error -> {
            Log.i(TAG, "Volley request failed");
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    private final OnClickListener videoViewClickListener = v -> toggleVolume();

    /**
     * Returns the visible region of the video surface on the screen.
     * if some is cut off, it will return less than the @videoSurfaceDefaultHeight
     * @param playPosition
     * @return
     */
    private int getVisibleVideoSurfaceHeight(int playPosition) {
        int at = playPosition - ((LinearLayoutManager) getLayoutManager()).findFirstVisibleItemPosition();
        Log.d(TAG, "getVisibleVideoSurfaceHeight: at: " + at);

        View child = getChildAt(at);
        if (child == null) {
            return 0;
        }

        int[] location = new int[2];
        child.getLocationInWindow(location);

        if (location[1] < 0) {
            return location[1] + videoSurfaceDefaultHeight;
        } else {
            return screenDefaultHeight - location[1];
        }
    }


    // Remove the old player
    private void removeVideoView(PlayerView videoView) {
        ViewGroup parent = (ViewGroup) videoView.getParent();
        if (parent == null) {
            return;
        }

        int index = parent.indexOfChild(videoView);
        if (index >= 0) {
            parent.removeViewAt(index);
            isVideoViewAdded = false;
            viewHolderParent.setOnClickListener(null);
        }

    }

    private void addVideoView(){
        frameLayout.addView(videoSurfaceView);
        isVideoViewAdded = true;
        videoSurfaceView.requestFocus();
        videoSurfaceView.setVisibility(VISIBLE);
        videoSurfaceView.setAlpha(1);
        thumbnail.setVisibility(INVISIBLE);
    }

    private void resetVideoView(){
        if(isVideoViewAdded){
            removeVideoView(videoSurfaceView);
            playPosition = -1;
            videoSurfaceView.setVisibility(INVISIBLE);
            thumbnail.setVisibility(VISIBLE);
        }
    }

    public void releasePlayer() {

        if (videoPlayer != null) {
            videoPlayer.release();
            videoPlayer = null;
        }

        viewHolderParent = null;
    }

    private void toggleVolume() {
        if (videoPlayer != null) {
            if (volumeState == VolumeState.OFF) {
                Log.d(TAG, "togglePlaybackState: enabling volume.");
                setVolumeControl(VolumeState.ON);
            } else if(volumeState == VolumeState.ON) {
                Log.d(TAG, "togglePlaybackState: disabling volume.");
                setVolumeControl(VolumeState.OFF);

            }
        }
    }

    private void setVolumeControl(VolumeState state){
        volumeState = state;
        if(state == VolumeState.OFF){
            videoPlayer.setVolume(0f);
            if (audioMediaPlayer != null) {
                audioMediaPlayer.setVolume(0f, 0f);
            }
            animateVolumeControl();
        }
        else if(state == VolumeState.ON){
            videoPlayer.setVolume(1f);
            if (audioMediaPlayer != null) {
                audioMediaPlayer.setVolume(1f, 1f);
            }
            animateVolumeControl();
        }
    }

    private void animateVolumeControl(){
        if(volumeControl != null){
            volumeControl.bringToFront();
            if(volumeState == VolumeState.OFF){
                _requestManager.load(R.drawable.ic_volume_off_grey_24dp)
                        .into(volumeControl);
//                ImageUtil.load(R.drawable.ic_volume_off_grey_24dp, volumeControl);
            }
            else if(volumeState == VolumeState.ON){
//                ImageUtil.load(R.drawable.ic_volume_up_grey_24dp, volumeControl);
                _requestManager.load(R.drawable.ic_volume_up_grey_24dp)
                        .into(volumeControl);
            }
            volumeControl.animate().cancel();

            volumeControl.setAlpha(1f);

            volumeControl.animate()
                    .alpha(0f)
                    .setDuration(600).setStartDelay(1000);
        }
    }
}