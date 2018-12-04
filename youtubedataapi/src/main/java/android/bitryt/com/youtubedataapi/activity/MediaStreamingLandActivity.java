package android.bitryt.com.youtubedataapi.activity;

import android.bitryt.com.youtubedataapi.R;

import com.google.gson.Gson;
import com.ottapp.android.basemodule.models.UserProfileModel;
import com.ottapp.android.basemodule.heartbeat.iView.HeartbeatTimerInterface;
import android.bitryt.com.youtubedataapi.helper.YoutubeApiHelper;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.ottapp.android.basemodule.models.AssetVideosDataModel;
import com.ottapp.android.basemodule.heartbeat.presenter.HeartBeatTimerHelper;


public class MediaStreamingLandActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener, YouTubePlayer.OnFullscreenListener, YouTubePlayer.PlaybackEventListener, YouTubePlayer.PlayerStateChangeListener,HeartbeatTimerInterface {

    public static final String DATA_MEDIA = "data";
    public static final String DATA_USER= "user_data";
    private static final int RECOVERY_REQUEST = 1;
    private final String please_try_again = "please try again.";
    private YouTubePlayerView youtube_view;
    private AssetVideosDataModel mediaModel;
    private UserProfileModel userProfileModel;
    private YouTubePlayer youTubePlayer;
    private HeartBeatTimerHelper beatTimerClass;
    private int active ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);


        try {
            setContentView(R.layout.activity_media_land_streaming);
            youtube_view = findViewById(R.id.youtube_view);
            youtube_view.initialize(YoutubeApiHelper.YOUTUBE_API_KEY, this);
            youtube_view.setFilterTouchesWhenObscured(true);
            this.mediaModel = (AssetVideosDataModel) getIntent().getSerializableExtra(DATA_MEDIA);

            this.userProfileModel = (UserProfileModel) getIntent().getSerializableExtra(DATA_USER);
            if (mediaModel == null || mediaModel.getVideoId() == null) {
                String error = String.format(getString(R.string.player_error), please_try_again);
                Toast.makeText(this, error, Toast.LENGTH_LONG).show();
                finish();
            }
        } catch (Exception e) {
            String error = String.format(getString(R.string.player_error), please_try_again);
            Toast.makeText(this, error, Toast.LENGTH_LONG).show();
            finish();
        }
        beatTimerClass = new HeartBeatTimerHelper(this);

    }


    @Override
    public void onBackPressed() {
        try {
            super.onBackPressed();
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } catch (Exception ignore) {
            Log.e("Exception", ignore.getMessage());
            finish();
        }

    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player, boolean wasRestored) {
        youTubePlayer = player;
        player.setPlayerStateChangeListener(this);
        player.setPlaybackEventListener(this);
        player.setOnFullscreenListener(this);

        player.addFullscreenControlFlag(YouTubePlayer.FULLSCREEN_FLAG_CONTROL_ORIENTATION | YouTubePlayer.FULLSCREEN_FLAG_ALWAYS_FULLSCREEN_IN_LANDSCAPE);
        player.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);
        player.setFullscreen(true);



        if (!wasRestored) {
            // player.loadVideo("qsP3Y4hHyeM");// asianet live url from youtube
            // Plays https://www.youtube.com/watch?v=fhWaJi1Hsfo
            try {
                if (mediaModel.getVideoId() != null && !mediaModel.getVideoId().isEmpty()) {
                    player.loadVideo(mediaModel.getVideoId().trim()); //Plays https://www.youtube.com/watch?v=fhWaJi1Hsfo

                }
                else {
                    String error = String.format(getString(R.string.player_error), please_try_again);
                    Toast.makeText(this, error, Toast.LENGTH_LONG).show();
                    finish();
                }
            } catch (Exception e) {
                String error = String.format(getString(R.string.player_error), please_try_again);
                Toast.makeText(this, error, Toast.LENGTH_LONG).show();
                finish();
            }
        }

    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult errorReason) {
        if (errorReason.isUserRecoverableError()) {
            if (!isFinishing())
                errorReason.getErrorDialog(this, RECOVERY_REQUEST).show();
        } else {
            String error = String.format(getString(R.string.player_error), errorReason.toString());
            Toast.makeText(this, error, Toast.LENGTH_LONG).show();
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RECOVERY_REQUEST) {
            try {
                // Retry initialization if user performed a recovery action
                youtube_view.initialize(YoutubeApiHelper.YOUTUBE_API_KEY, this);
            }catch (Exception  ignore){

            }
        }
    }

    @Override
    public void onFullscreen(boolean b) {
        if (b) {
            Log.e("Full", "on");
        } else {
            Log.e("Full", "off");
        }
    }

    @Override
    public void onPlaying() {
        beatTimerClass.startTimer(); //here starts the timer
        active = 2;
    }

    @Override
    public void onPaused() {
        beatTimerClass.stopTimer(); //here stops the timer
        active = 2;
    }

    @Override
    public void onStopped() {
        beatTimerClass.stopTimer();
        active = 2;
    }

    @Override
    public void onBuffering(boolean b) {
        beatTimerClass.stopTimer();
        active = 2;
    }

    @Override
    public void onSeekTo(int i) {
        beatTimerClass.stopTimer();
        active = 2;
    }

    @Override
    public void onLoading() {
        beatTimerClass.stopTimer();
        active = 2;
    }

    @Override
    public void onLoaded(String s) {

    }

    @Override
    public void onAdStarted() {

    }

    @Override
    public void onVideoStarted() {
        System.out.println("playAssetObj:"+ new Gson().toJson(mediaModel));
//        long seconds = (mediaModel.getPlayed_duration() / 1000) % 60;
        int longTointValue = mediaModel.getPlayed_duration();
//
//        System.out.println("duration:"+ seconds);
        youTubePlayer.seekToMillis(this.mediaModel.getPlayed_duration()); //here the seek time is set into the player
        active = 2;
        beatTimerClass.heartBeatStartServiceRequest(); //when the video starts to playe the start heartbeat service is called with active status 2
    }
    @Override
    public void onVideoEnded() {
        beatTimerClass.stopTimer();
        active = 3;
        beatTimerClass.heartBeatStartServiceRequest(); //start heartbeats service is called with active status 3
    }

    @Override
    public void onError(YouTubePlayer.ErrorReason errorReason) {
        beatTimerClass.stopTimer();
    }

    @Override
    public int getCurrentTime() {  //returns the current player time
        int millis =0;
        if(youTubePlayer != null){
            millis = youTubePlayer.getCurrentTimeMillis();//exception may occur
        }
        return millis;
    }

    @Override
    public AssetVideosDataModel assetData() {
        return mediaModel;
    } //returns the asset data

    @Override
    public int activeStatus() {
        return active;
    } //returns the current active status

    @Override
    public UserProfileModel getUserProfileModel() {
        return userProfileModel;
    } //returns the login users data

    @Override
    public void showProgress() {

    }
}
