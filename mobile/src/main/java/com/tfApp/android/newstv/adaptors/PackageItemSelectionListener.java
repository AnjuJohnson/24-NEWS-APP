package com.tfApp.android.newstv.adaptors;




import com.ottapp.android.basemodule.models.PackageModel;

/**
 * Created by George PJ on 23-02-2018.
 */

public interface PackageItemSelectionListener {
    void onItemSelect(PackageModel modal);

    void onMoreItemsClicked(PackageModel modal);

    void onMoreItemsNeeded();

    void onFavouriteActionSelected(PackageModel youtubeVideoModel, int position);
}
