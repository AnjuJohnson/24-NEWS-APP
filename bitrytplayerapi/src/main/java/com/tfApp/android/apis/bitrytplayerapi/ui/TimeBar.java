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

import android.support.annotation.Nullable;
import android.view.View;

/**
 * Interface for time bar views that can display a playback position, buffered position, duration
 * and ad markers, and that have a listener for scrubbing (seeking) events.
 */
public interface TimeBar {

  /**
   * Adds a listener for scrubbing events.
   *
   * @param listener The listener to add.
   */
  void addListener(OnScrubListener listener);

  /**
   * Removes a listener for scrubbing events.
   *
   * @param listener The listener to remove.
   */
  void removeListener(OnScrubListener listener);

  /**
   * @see View#isEnabled()
   */
  void setEnabled(boolean enabled);

  /**
   * Sets the position increment for key presses and accessibility actions, in milliseconds.
   * <p>
   * Clears any increment specified in a preceding call to {@link #setKeyCountIncrement(int)}.
   *
   * @param time The time increment, in milliseconds.
   */
  void setKeyTimeIncrement(long time);

  /**
   * Sets the position increment for key presses and accessibility actions, as a number of
   * increments that divide the duration of the media. For example, passing 20 will cause key
   * presses to increment/decrement the position by 1/20th of the duration (if known).
   * <p>
   * Clears any increment specified in a preceding call to {@link #setKeyTimeIncrement(long)}.
   *
   * @param count The number of increments that divide the duration of the media.
   */
  void setKeyCountIncrement(int count);

  /**
   * Sets the current position.
   *
   * @param position The current position to show, in milliseconds.
   */
  void setPosition(long position);

  /**
   * Sets the buffered position.
   *
   * @param bufferedPosition The current buffered position to show, in milliseconds.
   */
  void setBufferedPosition(long bufferedPosition);

  /**
   * Sets the duration.
   *
   * @param duration The duration to show, in milliseconds.
   */
  void setDuration(long duration);

  /**
   * Sets the times of ad groups and whether each ad group has been played.
   *
   * @param adGroupTimesMs An array where the first {@code adGroupCount} elements are the times of
   *     ad groups in milliseconds. May be {@code null} if there are no ad groups.
   * @param playedAdGroups An array where the first {@code adGroupCount} elements indicate whether
   *     the corresponding ad groups have been played. May be {@code null} if there are no ad
   *     groups.
   * @param adGroupCount The number of ad groups.
   */
  void setAdGroupTimesMs(@Nullable long[] adGroupTimesMs, @Nullable boolean[] playedAdGroups,
                         int adGroupCount);

  /**
   * Listener for scrubbing events.
   */
  interface OnScrubListener {

    /**
     * Called when the user starts moving the scrubber.
     *
     * @param timeBar The time bar.
     * @param position The position of the scrubber, in milliseconds.
     */
    void onScrubStart(TimeBar timeBar, long position);

    /**
     * Called when the user moves the scrubber.
     *
     * @param timeBar The time bar.
     * @param position The position of the scrubber, in milliseconds.
     */
    void onScrubMove(TimeBar timeBar, long position);

    /**
     * Called when the user stops moving the scrubber.
     *
     * @param timeBar The time bar.
     * @param position The position of the scrubber, in milliseconds.
     * @param canceled Whether scrubbing was canceled.
     */
    void onScrubStop(TimeBar timeBar, long position, boolean canceled);

  }

}
