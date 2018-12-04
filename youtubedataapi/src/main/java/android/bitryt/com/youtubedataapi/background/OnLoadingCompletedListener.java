package android.bitryt.com.youtubedataapi.background;

import java.util.List;

public interface OnLoadingCompletedListener<T> {
    void onLoadingCompleted(List<T> result, boolean loadMoreResult);
    void onLoadingStarted();
}
