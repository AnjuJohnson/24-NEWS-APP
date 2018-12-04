package com.tfApp.android.newstv.view.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.tfApp.android.apis.bitrytplayerapi.manager.PlayerManager;
import com.tfApp.android.apis.bitrytplayerapi.manager.PlayerManagerCallbacksListener;
import com.tfApp.android.apis.bitrytplayerapi.ui.BitRytPlayerView;
import com.tfApp.android.newstv.R;
import com.tfApp.android.newstv.adaptors.OnItemSelectionListener;
import com.ottapp.android.basemodule.models.AssetVideosDataModel;
import com.ottapp.android.basemodule.models.UserProfileModel;
import com.ottapp.android.basemodule.presenters.activity.BaseActivityPresenter;
import com.ottapp.android.basemodule.view.base.activity.BaseActivity;

public class MediaStreamingActivityExoPlayer extends BaseActivity implements OnItemSelectionListener<AssetVideosDataModel>,PlayerManagerCallbacksListener {
    public static final int RC_SWITCH_MEDIA = 12345;
    public static final String DATA_MEDIA = "data";
    public static final String DATA_USER = "user_data";
    private BitRytPlayerView playerView;
    private PlayerManager player;
    private Handler handler;
    private UserProfileModel userProfileModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_media_streaming);
        playerView = findViewById(R.id.player_view);
        handler = new Handler();

        this.mediaModel = (AssetVideosDataModel) getIntent().getSerializableExtra(DATA_MEDIA);
        this.userProfileModel = (UserProfileModel) getIntent().getSerializableExtra(DATA_USER);
        player = new PlayerManager(this, this,getString(R.string.app_name),mediaModel);
    }

/*
    private void setupAdapter() {
        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setHasFixedSize(true);
        SnapAdapter snapAdapter = new SnapAdapter<>(this);
        if (mediaModel.getMediaType() == MediaType.VIDEO)
            snapAdapter.addSnap(new Snap<>(Gravity.START, "Related Videos", DataStore_Mobile.getInstance().getMovieList()));
        else
            snapAdapter.addSnap(new Snap<>(Gravity.START, "Related Channels", DataStore_Mobile.getInstance().getChannels()));
        mRecyclerView.setAdapter(snapAdapter);
    }
*/


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        resetOrientationRequest();
    }

    private void resetOrientationRequest() {

    }


    @Override
    public void openMenuClicked() {

    }

    public void startPrepareMediaView() {
        handler.postDelayed(() -> prepareMediaPlayer(mediaModel, continueWatchingRequested), 200);
    }

    void prepareMediaPlayer(AssetVideosDataModel mediaModel, boolean continueWatch) {
        if (player != null) {
            player.reset();
            player.release();
        }

        player.init(playerView, mediaModel, continueWatch);
        hideSystemUi(playerView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!fromMenuScreen || mediaChangeRequested) {
            startPrepareMediaView();
        }

    }

    @Override
    protected void onPause() {
        continueWatchingRequested = true;
        player.reset();
        super.onPause();
    }

    @Override
    protected BaseActivityPresenter initializePresenter() {
        return null;
    }

    @Override
    protected void onDestroy() {
        player.release();
        super.onDestroy();
    }

    public boolean continueWatchingRequested = true;
    private AssetVideosDataModel mediaModel;
    private boolean fromMenuScreen = false, mediaChangeRequested = false;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        fromMenuScreen = true;
        if (player != null) {
            player.resetMenuClosed();
        }
        if (requestCode == RC_SWITCH_MEDIA) {

            if (resultCode == RESULT_OK) {

                AssetVideosDataModel mediaModel = (AssetVideosDataModel) data.getSerializableExtra(DATA_MEDIA);
                if (mediaModel != null && player != null) {
                    mediaChangeRequested = true;
                    continueWatchingRequested = false;
                    this.mediaModel = mediaModel;
                } else {
                    mediaChangeRequested = false;
                }

            }
        }

    }

    @SuppressLint("InlinedApi")
    private void hideSystemUi(View view) {
        view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }


    @Override
    public void onItemSelect(AssetVideosDataModel modal) {

    }

    @Override
    public void onMoreItemsClicked(AssetVideosDataModel modal) {

    }

    @Override
    public void onMoreItemsNeeded(long lastUpdateDate, int totalItemsLoaded) {

    }

    @Override
    public void onFavouriteActionSelected(AssetVideosDataModel model, int position) {

    }
}
