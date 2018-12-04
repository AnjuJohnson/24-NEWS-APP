package com.tfApp.android.newstv.adaptors;




import com.ottapp.android.basemodule.models.AssetVideosDataModel;

/**
 * Created by George PJ on 23-02-2018.
 */

public interface OnYoutubeItemSelectionListener {
    void onItemSelect(AssetVideosDataModel modal);

    void onMoreItemsClicked(AssetVideosDataModel modal);

    void onMoreItemsNeeded();

    void onFavouriteActionSelected(AssetVideosDataModel youtubeVideoModel, int position);
}
