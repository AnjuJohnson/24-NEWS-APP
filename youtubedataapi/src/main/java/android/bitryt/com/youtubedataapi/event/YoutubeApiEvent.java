package android.bitryt.com.youtubedataapi.event;


import com.ottapp.android.basemodule.models.AssetVideosDataModel;

public class YoutubeApiEvent<T> {

    public YoutubeApiEvent(int eventType, AssetVideosDataModel youtubeVideoModel, T data) {
        this.youtubeVideoModel = youtubeVideoModel;
        this.eventType = eventType;
        this.data = data;
    }

    private AssetVideosDataModel youtubeVideoModel;
    public YoutubeApiEvent(int eventType, AssetVideosDataModel youtubeVideoModel) {
        this.eventType = eventType;
        this.youtubeVideoModel = youtubeVideoModel;
    }

    public YoutubeApiEvent(int eventType) {
        this.eventType = eventType;
    }

    private int eventType;

    public int getEventType() {
        return eventType;
    }
    private T data;

    public void setEventType(int eventType) {
        this.eventType = eventType;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public AssetVideosDataModel getYoutubeVideoModel() {
        return youtubeVideoModel;
    }

    public void setYoutubeVideoModel(AssetVideosDataModel youtubeVideoModel) {
        this.youtubeVideoModel = youtubeVideoModel;
    }

    public static class EventType{
        public static final int YOUTUBE_EVENT_ON_LOADING=1;
        public static final int YOUTUBE_EVENT_ON_LOADED=2;
        public static final int YOUTUBE_EVENT_ON_AD_STARTED = 3;
        public static final int YOUTUBE_EVENT_ON_VIDEO_STARTED = 4;
        public static final int YOUTUBE_EVENT_ON_VIDEO_ENDED = 5;
        public static final int YOUTUBE_EVENT_ON_ERROR = 6;
        public static final int YOUTUBE_EVENT_ON_PLAYING = 7;
        public static final int YOUTUBE_EVENT_ON_PAUSED = 8;
        public static final int YOUTUBE_EVENT_ON_STOPPED = 9;
        public static final int YOUTUBE_EVENT_ON_BUFFERING = 10;
        public static final int YOUTUBE_EVENT_ON_SEEK = 11;
        public static final int YOUTUBE_EVENT_ON_FULL_SCREEN = 12;
        public static final int YOUTUBE_EVENT_ON_DATA_LOADING_COMPLETED = 13;
        public static final int YOUTUBE_EVENT_ON_DATA_LOADING_STARTED = 14;
    }
}
