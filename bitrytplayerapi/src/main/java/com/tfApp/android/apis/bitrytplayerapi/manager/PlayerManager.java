/*
 *
 *  * Copyright (c) 2018, Bitryt Solutions Pvt. Ltd. and/or its affiliates. All rights reserved.
 *  *
 *  * Redistribution and use in source and binary forms, with or without
 *  * modification, are permitted provided that the following conditions
 *  * are met:
 *  *
 *  *   - Redistributions of source code must retain the above copyright
 *  *     notice, this list of conditions and the following disclaimer.
 *  *
 *  *   - Redistributions in binary form must reproduce the above copyright
 *  *     notice, this list of conditions and the following disclaimer in the
 *  *     documentation and/or other materials provided with the distribution.
 *  *
 *  *   - Neither the name of Bitrate Global Solutions or the names of its
 *  *     contributors may be used to endorse or promote products derived
 *  *     from this software without specific prior written permission.
 *  *
 *  * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 *  * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 *  * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 *  * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 *  * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 *  * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 *  * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 *  * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 *  * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 *  * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 *  * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 */
package com.tfApp.android.apis.bitrytplayerapi.manager;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.tfApp.android.apis.bitrytplayerapi.R;
import com.tfApp.android.apis.bitrytplayerapi.model.BaseMediaType;
import com.tfApp.android.apis.bitrytplayerapi.ui.BitRytPlayerView;
import com.tfApp.android.apis.bitrytplayerapi.ui.PlaybackControlView;
import com.tfApp.android.apis.bitrytplayerapi.ui.TimeBar;
import com.tfApp.android.apis.bitrytplayerapi.utils.HttpTools;
import com.tfApp.android.apis.bitrytplayerapi.utils.TrackSelectionHelperPopup;
import com.google.ads.interactivemedia.v3.api.ImaSdkSettings;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.C.ContentType;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.ext.ima.ImaAdsLoader;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.MediaSourceEventListener;
import com.google.android.exoplayer2.source.MergingMediaSource;
import com.google.android.exoplayer2.source.SingleSampleMediaSource;
import com.google.android.exoplayer2.source.ads.AdsMediaSource;
import com.google.android.exoplayer2.source.dash.DashMediaSource;
import com.google.android.exoplayer2.source.dash.DefaultDashChunkSource;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.MappingTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DataSpec;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.MimeTypes;
import com.google.android.exoplayer2.util.Util;
import com.ottapp.android.basemodule.models.AssetVideosDataModel;
import com.ottapp.android.basemodule.models.BaseMediaModel;

import java.io.IOException;

import static android.content.ContentValues.TAG;

/**
 * Manages the {@link ExoPlayer}, the IMA plugin and all video playback.
 */
public final class PlayerManager implements AdsMediaSource.MediaSourceFactory, PlaybackControlView.PlayerControlButtonCallBacks {

    private static final String EXO_LOG_FORMAT = "EXOPLAYER";
    public static long contentPosition;
    private ImaAdsLoader adsLoader;
    private DataSource.Factory manifestDataSourceFactory;
    private DataSource.Factory mediaDataSourceFactory;
    private Handler handler;
    private DefaultTrackSelector trackSelector;
    private TrackSelectionHelperPopup trackSelectionHelper;
    private SimpleExoPlayer player;
    private BitRytPlayerView bitRytPlayerView;
    private BaseMediaModel mediaModel;
    private Context context;
    private boolean isContinueWatchingRequested = false;
    private PlayerManagerCallbacksListener callbacksListener;
    private MediaSourceEventListener eventListener = new MediaSourceEventListener() {
        @Override
        public void onLoadStarted(DataSpec dataSpec, int dataType, int trackType, Format trackFormat, int trackSelectionReason, Object trackSelectionData, long mediaStartTimeMs, long mediaEndTimeMs, long elapsedRealtimeMs) {
            if (trackFormat != null)
                Log.e(EXO_LOG_FORMAT, "onLoadStarted Lang :" + trackFormat.language + " > bitrate:" + trackFormat.bitrate + " > frameRate" + trackFormat.frameRate);
        }

        @Override
        public void onLoadCompleted(DataSpec dataSpec, int dataType, int trackType, Format trackFormat, int trackSelectionReason, Object trackSelectionData, long mediaStartTimeMs, long mediaEndTimeMs, long elapsedRealtimeMs, long loadDurationMs, long bytesLoaded) {
            if (trackFormat != null)
                Log.e(EXO_LOG_FORMAT, "onLoadCompleted Lang :" + trackFormat.language + " > bitrate:" + trackFormat.bitrate + " > frameRate" + trackFormat.frameRate);
        }

        @Override
        public void onLoadCanceled(DataSpec dataSpec, int dataType, int trackType, Format trackFormat, int trackSelectionReason, Object trackSelectionData, long mediaStartTimeMs, long mediaEndTimeMs, long elapsedRealtimeMs, long loadDurationMs, long bytesLoaded) {
            if (trackFormat != null)
                Log.e(EXO_LOG_FORMAT, "onLoadCanceled Lang :" + trackFormat.language + " > bitrate:" + trackFormat.bitrate + " > frameRate" + trackFormat.frameRate);
        }

        @Override
        public void onLoadError(DataSpec dataSpec, int dataType, int trackType, Format trackFormat, int trackSelectionReason, Object trackSelectionData, long mediaStartTimeMs, long mediaEndTimeMs, long elapsedRealtimeMs, long loadDurationMs, long bytesLoaded, IOException error, boolean wasCanceled) {
            if (trackFormat != null)
                Log.e(EXO_LOG_FORMAT, "onLoadError Lang :" + trackFormat.language + " > bitrate:" + trackFormat.bitrate + " > frameRate" + trackFormat.frameRate);
        }

        @Override
        public void onUpstreamDiscarded(int trackType, long mediaStartTimeMs, long mediaEndTimeMs) {

        }

        @Override
        public void onDownstreamFormatChanged(int trackType, Format trackFormat, int trackSelectionReason, Object trackSelectionData, long mediaTimeMs) {
            if (trackFormat != null)
                Log.e(EXO_LOG_FORMAT, "onDownstreamFormatChanged Lang :" + trackFormat.language + " > bitrate:" + trackFormat.bitrate + " > frameRate" + trackFormat.frameRate);
        }
    };
   // private MediaModel mediaModel;
    private boolean isButtonRequest = false;

    /**
     * Instantiates a new Player manager.
     *
     * @param context the context
     * @param mediaModel
     */
    public PlayerManager(Context context, PlayerManagerCallbacksListener callbacksListener, String app_name, AssetVideosDataModel mediaModel) {

        this.context = context;
        this.callbacksListener = callbacksListener;
        handler = new Handler();
        this.mediaModel = mediaModel;
//        String srtUrl = mediaModel.getSubTitleUrl();
        String adUrl = mediaModel.getVastUrl();
        String t = HttpTools.get(adUrl);
        // adsLoader = new ImaAdsLoader(context, Uri.parse(adTag));
        ImaAdsLoader.Builder builder = new ImaAdsLoader.Builder(context);
        adsLoader = builder.setImaSdkSettings(new ImaSdkSettings()).buildForAdsResponse(t);

        manifestDataSourceFactory =
                new DefaultDataSourceFactory(
                        context, Util.getUserAgent(context, context.getString(R.string.app_name)));
        mediaDataSourceFactory =
                new DefaultDataSourceFactory(
                        context,
                        Util.getUserAgent(context, context.getString(R.string.app_name)),
                        new DefaultBandwidthMeter());
    }

    /**
     * Init.
     *
     * @param simpleExoPlayerView the simple exo player view
     */
    public void init(BitRytPlayerView simpleExoPlayerView, @NonNull BaseMediaModel mediaModel, boolean continueWatch) {
        if (!isButtonRequest) {
            BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
            TrackSelection.Factory videoTrackSelectionFactory =
                    new AdaptiveTrackSelection.Factory(bandwidthMeter);
            trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);
            trackSelectionHelper = new TrackSelectionHelperPopup(trackSelector, videoTrackSelectionFactory);
            // Bind the player to the view.
            //player = simpleExoPlayerView.setupExoplayer(trackSelector);
            bitRytPlayerView = simpleExoPlayerView;
            this.mediaModel = mediaModel;
            player = bitRytPlayerView.setupExoplayer(trackSelector);
            MediaSource mediaSource = createMediaSource(Uri.parse(mediaModel.getContentUrl()), handler, eventListener);
            String srtUrl = mediaModel.getSubTitleUrl();
            String adUrl = mediaModel.getVastUrl();
            String srtLanguage = mediaModel.getSubTitleLanguage();
            if (srtLanguage == null || srtLanguage.trim().isEmpty()) {
                srtLanguage = "en";
            }
            if (srtUrl != null && !srtUrl.trim().isEmpty()) {
                Uri subTitleUri = Uri.parse(srtUrl);
                Format textFormat = Format.createTextSampleFormat(null, MimeTypes.APPLICATION_SUBRIP, Format.NO_VALUE, srtLanguage);
                MediaSource subtitleSource = new SingleSampleMediaSource.Factory(mediaDataSourceFactory).createMediaSource(subTitleUri, textFormat, Format.NO_VALUE);
                mediaSource = new MergingMediaSource(mediaSource, subtitleSource);
            }
            if (adUrl != null && !adUrl.trim().isEmpty()) {
                String t = HttpTools.get(adUrl);
                ImaAdsLoader.Builder builder = new ImaAdsLoader.Builder(context);

//                adsLoader = builder.setImaSdkSettings(new ImaSdkSettings()).buildForAdsResponse(t);
                adsLoader = new ImaAdsLoader(context, Uri.parse("<![CDATA["+adUrl+"]]>"));
                mediaSource = new AdsMediaSource(
                        mediaSource,
                        /* adMediaSourceFactory= */ this,
                        adsLoader,
                        bitRytPlayerView.getOverlayFrameLayout(),
                        /* eventHandler= */ null,
                        /* eventListener= */ null);
            }
//            mediaSource = new AdsMediaSource(
//                    mediaSource,
//                    /* adMediaSourceFactory= */ this,
//                    adsLoader,
//                    bitRytPlayerView.getOverlayFrameLayout(),
//                    /* eventHandler= */ null,
//                    /* prepareViews= */ null);
            player.prepare(mediaSource);
            player.setPlayWhenReady(true);
            if (mediaModel.getMediaType() == BaseMediaType.VIDEO) {
                if (!continueWatch)
                    contentPosition = 0;
                player.seekTo(contentPosition);
                bitRytPlayerView.getController().showOrHideMediaControlBox(true);
            } else {
                bitRytPlayerView.getController().showOrHideMediaControlBox(false);
                contentPosition = 0;
            }
            // bitRytPlayerView.getController().hideFullScreenControls();
            bitRytPlayerView.getController().hideFullScreenControls();
            bitRytPlayerView.getController().addExternalEventListener(this);
            bitRytPlayerView.getController().setMediaTitle(mediaModel.getMediaName());
            bitRytPlayerView.getController().setMediaThumbnail(mediaModel.getDefaultThumbnailUrl());
        }

    }

    /**
     * Reset.
     */
    public void reset() {
        if (player != null && !isButtonRequest) {
            contentPosition = player.getContentPosition();
            player.release();
            player = null;
        }
    }

    /**
     * Release.
     */
    public void release() {
        if (!isButtonRequest) {
            if (player != null) {
                player.release();
                player = null;
            }
            if(adsLoader!=null) {
                adsLoader.release();
            }
        }
    }

    // AdsMediaSource.MediaSourceFactory implementation.

    @Override
    public MediaSource createMediaSource(
            Uri uri, @Nullable Handler handler, @Nullable MediaSourceEventListener listener) {
        @ContentType int type = Util.inferContentType(uri);
        switch (type) {
            case C.TYPE_DASH:
                return new DashMediaSource.Factory(
                        new DefaultDashChunkSource.Factory(mediaDataSourceFactory),
                        manifestDataSourceFactory)
                        .createMediaSource(uri, handler, listener);
            case C.TYPE_HLS:
                return new HlsMediaSource.Factory(mediaDataSourceFactory)
                        .createMediaSource(uri, handler, listener);
            case C.TYPE_OTHER:
                return new ExtractorMediaSource.Factory(mediaDataSourceFactory)
                        .createMediaSource(uri, handler, listener);
            case C.TYPE_SS:
            default:
                throw new IllegalStateException("Unsupported type: " + type);
        }
    }

    @Override
    public int[] getSupportedTypes() {
        return new int[]{C.TYPE_DASH, C.TYPE_HLS, C.TYPE_OTHER};
    }

    @Override
    public boolean onNextPressed() {
        return false;
    }

    @Override
    public boolean onPreviousPressed() {
        return true;
    }


    @Override
    public boolean onForwardPressed() {
        return true;
    }

    @Override
    public boolean onBackwardPressed() {
        return true;
    }

    @Override
    public boolean onFullScreenPressed() {
        //activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        return true;
    }

    @Override
    public boolean onFullScreenExitPressed() {
       // activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        return true;
    }

    @Override
    public boolean onPlayPressed() {
        return true;
    }

    @Override
    public boolean onPausePressed() {

        return true;
    }

    @Override
    public boolean onRepeatTogglePressed() {
        return true;
    }

    @Override
    public boolean onShufflePressed() {
        return true;
    }

    @Override
    public void onControlVisibilityChange(boolean visible) {
        if (!visible)
            trackSelectionHelper.hidePopup();
    }

    @Override
    public void onSettingViewPressed(View view, TextView textView) {
        MappingTrackSelector.MappedTrackInfo mappedTrackInfo = trackSelector.getCurrentMappedTrackInfo();
        if (mappedTrackInfo != null) {
            trackSelectionHelper.showSelectionDialog(
                    view, textView, mappedTrackInfo);
        }
    }

    @Override
    public boolean onPlayStateChanged(boolean playWhenReady, int playbackState) {
        if (playbackState == Player.STATE_ENDED) {
            handleMediaEnd();
        }
        return true;
    }

    private void handleMediaEnd() {
        if (mediaModel != null) {
            if (mediaModel.getMediaType() == BaseMediaType.VIDEO) {
                // openMenu();
                bitRytPlayerView.getController().showReplay();
            } else {
                openMenu();
            }
        }
    }

    @Override
    public boolean onRepeatModeChanged(int repeatMode) {
        return true;
    }

    @Override
    public boolean onScrubStart(TimeBar timeBar, long position) {
        return true;
    }

    @Override
    public boolean onScrubMove(TimeBar timeBar, long position) {
        return true;
    }

    @Override
    public boolean onScrubStop(TimeBar timeBar, long position, boolean canceled) {
        return true;
    }

    @Override
    public boolean onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {
        return true;
    }

    @Override
    public boolean onPositionDiscontinuity(int reason) {
        return true;
    }

    @Override
    public boolean onTimelineChanged(Timeline timeline, Object manifest) {
        return true;
    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {
        switch (error.type) {
            case ExoPlaybackException.TYPE_SOURCE:
                Log.e(TAG, "TYPE_SOURCE: " + error.getSourceException().getMessage());
                break;

            case ExoPlaybackException.TYPE_RENDERER:
                Log.e(TAG, "TYPE_RENDERER: " + error.getRendererException().getMessage());
                break;

            case ExoPlaybackException.TYPE_UNEXPECTED:
                Log.e(TAG, "TYPE_UNEXPECTED: " + error.getUnexpectedException().getMessage());
                break;
        }
        bitRytPlayerView.getController().showPlayError();
        //openMenu();
    }

    @Override
    public void onMediaThumbnailClicked() {
        openMenu();
    }

    @Override
    public void onReplayRequested() {
        isContinueWatchingRequested = false;
        if (callbacksListener != null) {
            callbacksListener.startPrepareMediaView();
        }
    }

    @Override
    public void onRetryRequested() {
        isContinueWatchingRequested = false;
        if (callbacksListener != null) {
            callbacksListener.startPrepareMediaView();
        }
    }

    private void openMenu() {
        // isButtonRequest = true;
        //activity.startActivityForResult(new Intent(context, MenuActivity.class), MediaStreamingActivity.RC_SWITCH_MEDIA);
    }

    public void resetMenuClosed() {
        isButtonRequest = false;
    }
}
