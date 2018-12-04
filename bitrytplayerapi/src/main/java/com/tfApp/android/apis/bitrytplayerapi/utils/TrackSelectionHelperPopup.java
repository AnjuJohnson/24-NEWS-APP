/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.tfApp.android.apis.bitrytplayerapi.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CheckedTextView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.tfApp.android.apis.bitrytplayerapi.R;
import com.google.android.exoplayer2.RendererCapabilities;
import com.google.android.exoplayer2.source.TrackGroup;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.FixedTrackSelection;
import com.google.android.exoplayer2.trackselection.MappingTrackSelector;
import com.google.android.exoplayer2.trackselection.MappingTrackSelector.MappedTrackInfo;
import com.google.android.exoplayer2.trackselection.MappingTrackSelector.SelectionOverride;
import com.google.android.exoplayer2.trackselection.RandomTrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelection;

import java.util.Arrays;

/**
 * Helper class for displaying track selection dialogs.
 */
/* package */ public final class TrackSelectionHelperPopup implements View.OnClickListener {

    private static final TrackSelection.Factory FIXED_FACTORY = new FixedTrackSelection.Factory();
    private static final TrackSelection.Factory RANDOM_FACTORY = new RandomTrackSelection.Factory();

    private final MappingTrackSelector selector;
    private final TrackSelection.Factory adaptiveTrackSelectionFactory;

    private MappedTrackInfo trackInfo;
    private int rendererIndex;
    private TrackGroupArray trackGroups;
    private boolean[] trackGroupsAdaptive;
    private SelectionOverride override;
    private PopupWindow popupWindow;
    //private CheckedTextView disableView;
    private CheckedTextView defaultView;
    //private CheckedTextView enableRandomAdaptationView;
    private CheckedTextView[][] trackViews;
    private TextView qualityTextView;

    /**
     * @param selector                      The track selector.
     * @param adaptiveTrackSelectionFactory A factory for adaptive {@link TrackSelection}s, or null
     *                                      if the selection helper should not support adaptive tracks.
     */
    public TrackSelectionHelperPopup(MappingTrackSelector selector,
                                     TrackSelection.Factory adaptiveTrackSelectionFactory) {
        this.selector = selector;
        this.adaptiveTrackSelectionFactory = adaptiveTrackSelectionFactory;
    }

    /**
     * Shows the selection dialog for a given renderer.
     *
     * @param view      The view anchor.
     * @param trackInfo The current track information.
     */
    public void showSelectionDialog(View view, TextView qualityText, MappedTrackInfo trackInfo) {
        this.trackInfo = trackInfo;
        this.rendererIndex = 0;

        trackGroups = trackInfo.getTrackGroups(rendererIndex);
        trackGroupsAdaptive = new boolean[trackGroups.length];
        for (int i = 0; i < trackGroups.length; i++) {
            trackGroupsAdaptive[i] = adaptiveTrackSelectionFactory != null
                    && trackInfo.getAdaptiveSupport(rendererIndex, i, false)
                    != RendererCapabilities.ADAPTIVE_NOT_SUPPORTED
                    && trackGroups.get(i).length > 1;
        }
        override = selector.getSelectionOverride(rendererIndex, trackGroups);
        popupWindow = new PopupWindow(view.getContext());
        popupWindow.setFocusable(true);
        popupWindow.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindow.setContentView(buildView(view.getContext()));
        popupWindow.showAsDropDown(view);
        this.qualityTextView = qualityText;
    }

    public void hidePopup() {
        if (popupWindow != null) {
            popupWindow.dismiss();
        }
    }

    @SuppressLint("InflateParams")
    private View buildView(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.track_selection_dialog, null);
        ViewGroup root = view.findViewById(R.id.root);

        TypedArray attributeArray = context.getTheme().obtainStyledAttributes(
                new int[]{android.R.attr.selectableItemBackground});
        int selectableItemBackgroundResourceId = attributeArray.getResourceId(0, 0);
        attributeArray.recycle();

        /*// View for disabling the renderer.
        disableView = (CheckedTextView) inflater.inflate(
                android.R.layout.simple_list_item_single_choice, root, false);
        disableView.setBackgroundResource(selectableItemBackgroundResourceId);
        disableView.setText(R.string.selection_disabled);
        disableView.setFocusable(true);
        disableView.setOnClickListener(this);
        root.addView(disableView);*/

        // View for clearing the override to allow the selector to use its default selection logic.
        defaultView = (CheckedTextView) inflater.inflate(
                android.R.layout.simple_list_item_single_choice, root, false);
        defaultView.setBackgroundResource(selectableItemBackgroundResourceId);
        defaultView.setText(R.string.selection_auto);
        defaultView.setFocusable(true);
        defaultView.setOnClickListener(this);
        root.addView(inflater.inflate(R.layout.list_divider, root, false));
        root.addView(defaultView);

        trackViews = new CheckedTextView[trackGroups.length][];
        for (int groupIndex = 0; groupIndex < trackGroups.length; groupIndex++) {
            TrackGroup group = trackGroups.get(groupIndex);
            trackViews[groupIndex] = new CheckedTextView[group.length];
            for (int trackIndex = 0; trackIndex < group.length; trackIndex++) {
                if (trackIndex == 0) {
                    root.addView(inflater.inflate(R.layout.list_divider, root, false));
                }
                int trackViewLayoutId = android.R.layout.simple_list_item_single_choice;
                CheckedTextView trackView = (CheckedTextView) inflater.inflate(
                        trackViewLayoutId, root, false);
                trackView.setBackgroundResource(selectableItemBackgroundResourceId);
                trackView.setText(processTextValue(DemoUtil.buildTrackName(group.getFormat(trackIndex))));
                if (trackInfo.getTrackFormatSupport(rendererIndex, groupIndex, trackIndex)
                        == RendererCapabilities.FORMAT_HANDLED) {
                    trackView.setFocusable(true);
                    trackView.setTag(Pair.create(groupIndex, trackIndex));
                    trackView.setOnClickListener(this);
                } else {
                    trackView.setFocusable(false);
                    trackView.setEnabled(false);
                }
                trackViews[groupIndex][trackIndex] = trackView;
                root.addView(trackView);
            }
        }

      /*  if (haveAdaptiveTracks) {
            // View for using random adaptation.
            enableRandomAdaptationView = (CheckedTextView) inflater.inflate(
                    android.R.layout.simple_list_item_multiple_choice, root, false);
            enableRandomAdaptationView.setBackgroundResource(selectableItemBackgroundResourceId);
            enableRandomAdaptationView.setText(R.string.enable_random_adaptation);
            enableRandomAdaptationView.setOnClickListener(this);
            root.addView(inflater.inflate(R.layout.list_divider, root, false));
            root.addView(enableRandomAdaptationView);
        }*/

        updateViews();
        return view;
    }

    private void updateViews() {
        defaultView.setChecked(override == null);
        for (int i = 0; i < trackViews.length; i++) {
            for (int j = 0; j < trackViews[i].length; j++) {
                trackViews[i][j].setChecked(override != null && override.groupIndex == i
                        && override.containsTrack(j));
            }
        }
       /* if (enableRandomAdaptationView != null) {
            boolean enableView = !isDisabled && override != null && override.length > 1;
            enableRandomAdaptationView.setEnabled(enableView);
            enableRandomAdaptationView.setFocusable(enableView);
            if (enableView) {
                enableRandomAdaptationView.setChecked(!isDisabled
                        && override.factory instanceof RandomTrackSelection.Factory);
            }
        }*/

    }


    // View.OnClickListener

    @Override
    public void onClick(View view) {
        /*if (view == disableView) {
            override = null;
        } else*/
        if (view == defaultView) {
            override = null;
        } else /*if (view == enableRandomAdaptationView) {
            setOverride(override.groupIndex, override.tracks, !enableRandomAdaptationView.isChecked());
        } else*/ {
            @SuppressWarnings("unchecked")
            Pair<Integer, Integer> tag = (Pair<Integer, Integer>) view.getTag();
            int groupIndex = tag.first;
            int trackIndex = tag.second;
            if (!trackGroupsAdaptive[groupIndex] || override == null
                    || override.groupIndex != groupIndex) {
                override = new SelectionOverride(FIXED_FACTORY, groupIndex, trackIndex);
            } else {
                override = new SelectionOverride(FIXED_FACTORY, groupIndex, trackIndex);
            }
        }
        // Update the views with the new state.
        updateViews();
        if (override != null) {
            selector.setSelectionOverride(rendererIndex, trackGroups, override);
        } else {
            selector.clearSelectionOverrides(rendererIndex);
        }
        if (qualityTextView != null)
            qualityTextView.setText(processTextValue(((CheckedTextView) view).getText().toString()));
        hidePopup();
    }

    private String processTextValue(String value) {
        if (value != null && !value.isEmpty()) {
            if (value.contains("x")) {
                String[] vs = value.split("x");
                if (vs.length > 1) {
                    return vs[1] + "p";
                }
            }
            return value;
        }
        return "Auto";
    }

    private void setOverride(int group, int[] tracks, boolean enableRandomAdaptation) {
        TrackSelection.Factory factory = tracks.length == 1 ? FIXED_FACTORY
                : (enableRandomAdaptation ? RANDOM_FACTORY : adaptiveTrackSelectionFactory);
        override = new SelectionOverride(factory, group, tracks);
    }

    // Track array manipulation.

    private static int[] getTracksAdding(SelectionOverride override, int addedTrack) {
        int[] tracks = override.tracks;
        tracks = Arrays.copyOf(tracks, tracks.length + 1);
        tracks[0] = addedTrack;
        //tracks[tracks.length - 1] = addedTrack;
        return tracks;
    }

    private static int[] getTracksRemoving(SelectionOverride override, int removedTrack) {
        int[] tracks = new int[override.length - 1];
        if (override.length == 1) {
            return override.tracks;
        }

        int trackCount = 0;
        for (int i = 0; i < tracks.length + 1; i++) {
            int track = override.tracks[i];
            if (track != removedTrack) {
                tracks[trackCount++] = track;
            }
        }
        return tracks;
    }

}
