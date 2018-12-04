package android.bitryt.com.youtubedataapi.helper;

import android.app.Activity;
import android.bitryt.com.youtubedataapi.R;
import android.bitryt.com.youtubedataapi.background.OnLoadingCompletedListener;
import android.bitryt.com.youtubedataapi.event.YoutubeApiEvent;

import android.content.Intent;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.ottapp.android.basemodule.models.AssetVideosDataModel;

import org.greenrobot.eventbus.EventBus;

import java.lang.ref.WeakReference;
import java.security.Key;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

@SuppressWarnings("unused")
public class YoutubeApiHelper implements YouTubePlayer.OnInitializedListener, YouTubePlayer.PlayerStateChangeListener, YouTubePlayer.PlaybackEventListener, YouTubePlayer.OnFullscreenListener, OnLoadingCompletedListener<AssetVideosDataModel> {

    public static final String CHANNEL_ID="UCmAPdImtXLGh5E-lTTH4Qmg";//Flowers Comedy
    /**
     * now uses flower tv app API Key
     * Change youtube key in youtube module - YoutubeApiHelper (Encrypted with DXJavaStringEncryptor PlugIn)
     */
    public static final String YOUTUBE_API_KEY = DXDecrypt.decode("A2R8kM2ndQTDq+4vNL62LljgVODr59VWB+KMSCKf+Tpn303z+iIj")/*"AIzaSyBdfE98TSQu3mG2Ch48xYqpVgckpGhiROo"*/;
    private static final int RECOVERY_REQUEST = 111;
    private YouTubePlayerView youTubePlayer;
    private WeakReference<Activity> activityWeakReference;
    private AssetVideosDataModel mediaModel;

    public YoutubeApiHelper(Activity activity, YouTubePlayerView youTubePlayer) {
        this.youTubePlayer = youTubePlayer;
        activityWeakReference = new WeakReference<>(activity);
    }

    public void preparePlayer(AssetVideosDataModel mediaModel) {
        this.mediaModel = mediaModel;
        youTubePlayer.initialize(YOUTUBE_API_KEY, this);
    }

    public void getPlaylistData(String[] allPlayLists) {
       // new GetPlaylistTitlesAsyncTask(this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, allPlayLists);
    }

    private void loadMorePageContents(AssetVideosDataModel playListModel, int limit) {
//        if (playListModel != null && playListModel.getNextPageToken() != null)
//            new GetPlayListVideosAsyncTask(this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, playListModel);
//        else
           // onLoadingCompleted(null, false);
    }


    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player, boolean wasRestored) {
        player.setPlayerStateChangeListener(this);
        player.setPlaybackEventListener(this);
        player.setOnFullscreenListener(this);
        //FULLSCREEN_FLAG_FULLSCREEN_WHEN_DEVICE_LANDSCAPE without setting FULLSCREEN_FLAG_CONTROL_ORIENTATION
        /* player.addFullscreenControlFlag(YouTubePlayer.FULLSCREEN_FLAG_CONTROL_ORIENTATION | YouTubePlayer.FULLSCREEN_FLAG_ALWAYS_FULLSCREEN_IN_LANDSCAPE);
        player.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);
        player.setFullscreen(true);*/
        if (!wasRestored && mediaModel != null) {
            player.loadVideo(mediaModel.getVideoId()); // Plays https://www.youtube.com/watch?v=fhWaJi1Hsfo
        }
    }


    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult errorReason) {
        if (errorReason.isUserRecoverableError()) {
            if (activityWeakReference.get() != null)
                errorReason.getErrorDialog(activityWeakReference.get(), RECOVERY_REQUEST).show();
            else
                Log.e("Youtube API Error", "Activity already destroyed.");
        } else {
            String error = String.format(youTubePlayer.getContext().getString(R.string.player_error), errorReason.toString());
            Toast.makeText(youTubePlayer.getContext(), error, Toast.LENGTH_SHORT).show();
        }
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RECOVERY_REQUEST) {
            // Retry initialization if user performed a recovery action
            youTubePlayer.initialize(YOUTUBE_API_KEY, this);
        }
    }

    @Override
    public void onLoading() {
        EventBus.getDefault().post(new YoutubeApiEvent<>(YoutubeApiEvent.EventType.YOUTUBE_EVENT_ON_LOADING, mediaModel));
    }

    @Override
    public void onLoaded(String s) {
        EventBus.getDefault().post(new YoutubeApiEvent<>(YoutubeApiEvent.EventType.YOUTUBE_EVENT_ON_LOADED, mediaModel, s));
    }

    @Override
    public void onAdStarted() {
        EventBus.getDefault().post(new YoutubeApiEvent<>(YoutubeApiEvent.EventType.YOUTUBE_EVENT_ON_AD_STARTED, mediaModel));
    }

    @Override
    public void onVideoStarted() {
        EventBus.getDefault().post(new YoutubeApiEvent<>(YoutubeApiEvent.EventType.YOUTUBE_EVENT_ON_VIDEO_STARTED, mediaModel));
    }

    @Override
    public void onVideoEnded() {
        EventBus.getDefault().post(new YoutubeApiEvent<>(YoutubeApiEvent.EventType.YOUTUBE_EVENT_ON_VIDEO_ENDED, mediaModel));
    }

    @Override
    public void onError(YouTubePlayer.ErrorReason errorReason) {
        EventBus.getDefault().post(new YoutubeApiEvent<>(YoutubeApiEvent.EventType.YOUTUBE_EVENT_ON_ERROR, mediaModel, errorReason));
    }

    @Override
    public void onPlaying() {
        EventBus.getDefault().post(new YoutubeApiEvent<>(YoutubeApiEvent.EventType.YOUTUBE_EVENT_ON_PLAYING, mediaModel));
    }

    @Override
    public void onPaused() {
        EventBus.getDefault().post(new YoutubeApiEvent<>(YoutubeApiEvent.EventType.YOUTUBE_EVENT_ON_PAUSED, mediaModel));
    }

    @Override
    public void onStopped() {
        EventBus.getDefault().post(new YoutubeApiEvent<>(YoutubeApiEvent.EventType.YOUTUBE_EVENT_ON_STOPPED, mediaModel));
    }

    @Override
    public void onBuffering(boolean b) {
        EventBus.getDefault().post(new YoutubeApiEvent<>(YoutubeApiEvent.EventType.YOUTUBE_EVENT_ON_BUFFERING, mediaModel, b));
    }

    @Override
    public void onSeekTo(int i) {
        EventBus.getDefault().post(new YoutubeApiEvent<>(YoutubeApiEvent.EventType.YOUTUBE_EVENT_ON_SEEK, mediaModel, i));
    }

    @Override
    public void onFullscreen(boolean b) {
        EventBus.getDefault().post(new YoutubeApiEvent<>(YoutubeApiEvent.EventType.YOUTUBE_EVENT_ON_FULL_SCREEN, mediaModel, b));
    }

    @Override
    public void onLoadingCompleted(List<AssetVideosDataModel> result, boolean loadMoreResult) {
        EventBus.getDefault().post(new YoutubeApiEvent<>(YoutubeApiEvent.EventType.YOUTUBE_EVENT_ON_DATA_LOADING_COMPLETED, mediaModel, result));
    }

    @Override
    public void onLoadingStarted() {
        EventBus.getDefault().post(new YoutubeApiEvent<>(YoutubeApiEvent.EventType.YOUTUBE_EVENT_ON_DATA_LOADING_STARTED, mediaModel));
    }
}

class DXDecrypt {

    public static String decode(String s) {
        String str;
        String key = "ag2lMNJWYlAEylzPrenGGg==";
        try {
            String ago = "ARCFOUR";
            Cipher rc4 = Cipher.getInstance(ago);
            String kp = "I9vj9uvP5erHlEly";
            Key kpk = new SecretKeySpec(kp.getBytes(), ago);
            rc4.init(Cipher.DECRYPT_MODE, kpk);
            byte[] bck = Base64.decode(key, Base64.DEFAULT);
            byte[] bdk = rc4.doFinal(bck);
            Key dk = new SecretKeySpec(bdk, ago);
            rc4.init(Cipher.DECRYPT_MODE, dk);
            byte[] bcs = Base64.decode(s, Base64.DEFAULT);
            byte[] byteDecryptedString = rc4.doFinal(bcs);
            str = new String(byteDecryptedString);
        } catch (Exception e) {
            str = "";
        }
        return str;
    }

}