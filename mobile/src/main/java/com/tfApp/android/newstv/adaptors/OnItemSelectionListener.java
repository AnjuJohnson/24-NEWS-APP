package com.tfApp.android.newstv.adaptors;


/**
 * Created by George PJ on 23-02-2018.
 */

public interface OnItemSelectionListener<T> {
    void onItemSelect(T modal);

    void onMoreItemsClicked(T modal);

    void onMoreItemsNeeded(long lastUpdateDate, int totalItemsLoaded);

    void onFavouriteActionSelected(T model, int position);
}
