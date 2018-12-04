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
package com.tfApp.android.apis.bitrytplayerapi.ui;

import android.annotation.SuppressLint;
import android.widget.TextView;

import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.decoder.DecoderCounters;

import java.util.Locale;

/**
 * A helper class for periodically updating a {@link TextView} with debug information obtained from
 * a {@link SimpleExoPlayer}.
 */
public final class DebugTextViewHelper extends Player.DefaultEventListener implements Runnable {

  private static final int REFRESH_INTERVAL_MS = 1000;

  private final SimpleExoPlayer player;
  private final TextView textView;

  private boolean started;

  /**
   * @param player The {@link SimpleExoPlayer} from which debug information should be obtained.
   * @param textView The {@link TextView} that should be updated to display the information.
   */
  public DebugTextViewHelper(SimpleExoPlayer player, TextView textView) {
    this.player = player;
    this.textView = textView;
  }

  /**
   * Starts periodic updates of the {@link TextView}. Must be called from the application's main
   * thread.
   */
  public void start() {
    if (started) {
      return;
    }
    started = true;
    player.addListener(this);
    updateAndPost();
  }

  /**
   * Stops periodic updates of the {@link TextView}. Must be called from the application's main
   * thread.
   */
  public void stop() {
    if (!started) {
      return;
    }
    started = false;
    player.removeListener(this);
    textView.removeCallbacks(this);
  }

  // Player.EventListener implementation.

  @Override
  public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
    updateAndPost();
  }

  @Override
  public void onPositionDiscontinuity(@Player.DiscontinuityReason int reason) {
    updateAndPost();
  }

  // Runnable implementation.

  @Override
  public void run() {
    updateAndPost();
  }

  // Private methods.

  @SuppressLint("SetTextI18n")
  private void updateAndPost() {
    textView.setText(getPlayerStateString() + getPlayerWindowIndexString() + getVideoString()
        + getAudioString());
    textView.removeCallbacks(this);
    textView.postDelayed(this, REFRESH_INTERVAL_MS);
  }

  private String getPlayerStateString() {
    String text = "playWhenReady:" + player.getPlayWhenReady() + " playbackState:";
    switch (player.getPlaybackState()) {
      case Player.STATE_BUFFERING:
        text += "buffering";
        break;
      case Player.STATE_ENDED:
        text += "ended";
        break;
      case Player.STATE_IDLE:
        text += "idle";
        break;
      case Player.STATE_READY:
        text += "ready";
        break;
      default:
        text += "unknown";
        break;
    }
    return text;
  }

  private String getPlayerWindowIndexString() {
    return " window:" + player.getCurrentWindowIndex();
  }

  private String getVideoString() {
    Format format = player.getVideoFormat();
    if (format == null) {
      return "";
    }
    return "\n" + format.sampleMimeType + "(id:" + format.id + " r:" + format.width + "x"
        + format.height + getPixelAspectRatioString(format.pixelWidthHeightRatio)
        + getDecoderCountersBufferCountString(player.getVideoDecoderCounters()) + ")";
  }

  private String getAudioString() {
    Format format = player.getAudioFormat();
    if (format == null) {
      return "";
    }
    return "\n" + format.sampleMimeType + "(id:" + format.id + " hz:" + format.sampleRate + " ch:"
        + format.channelCount
        + getDecoderCountersBufferCountString(player.getAudioDecoderCounters()) + ")";
  }

  private static String getDecoderCountersBufferCountString(DecoderCounters counters) {
    if (counters == null) {
      return "";
    }
    counters.ensureUpdated();
    return " sib:" + counters.skippedInputBufferCount
        + " sb:" + counters.skippedOutputBufferCount
        + " rb:" + counters.renderedOutputBufferCount
        + " db:" + counters.droppedBufferCount
        + " mcdb:" + counters.maxConsecutiveDroppedBufferCount
        + " dk:" + counters.droppedToKeyframeCount;
  }

  private static String getPixelAspectRatioString(float pixelAspectRatio) {
    return pixelAspectRatio == Format.NO_VALUE || pixelAspectRatio == 1f ? ""
        : (" par:" + String.format(Locale.US, "%.02f", pixelAspectRatio));
  }

}
