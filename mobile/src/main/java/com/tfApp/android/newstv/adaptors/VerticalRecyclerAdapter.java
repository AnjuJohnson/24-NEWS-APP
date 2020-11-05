package com.tfApp.android.newstv.adaptors;


import android.content.Context;
import android.graphics.Color;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ottapp.android.basemodule.models.CategoryListDataModel;
import com.ottapp.android.basemodule.services.CategoryService;
import com.tfApp.android.newstv.R;

import java.util.List;


/**
 * The type Vertical recycler adapter.
 */
public class VerticalRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<YoutubeSnap> mSnaps;
    private SparseIntArray listPosition = new SparseIntArray();
    private OnYoutubeItemSelectionListener onItemSelectionListener;
    private Context mContext;
    private boolean visibleTopView;
    // variable to track event time
    private long mLastClickTime = 0;

    /**
     * Instantiates a new Vertical recycler adapter.
     *
     * @param list                    the list
     * @param visibleTopView          the visible top view
     * @param onItemSelectionListener the on item selection listener
     */
    public VerticalRecyclerAdapter(List<YoutubeSnap> list, boolean visibleTopView, OnYoutubeItemSelectionListener onItemSelectionListener) {

        this.mSnaps = list;
        this.visibleTopView = visibleTopView;
        this.onItemSelectionListener = onItemSelectionListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull final ViewGroup viewGroup, int viewType) {
        mContext = viewGroup.getContext();
        switch (viewType) {
            default: {
                View v1 = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adapter_snap, viewGroup, false);
                return new CellViewHolder(v1);
            }
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int position) {
        switch (viewHolder.getItemViewType()) {
            default: {
                CellViewHolder cellViewHolder = (CellViewHolder) viewHolder;
                YoutubeSnap snap = mSnaps.get(position);
                cellViewHolder.mRecyclerView.setHasFixedSize(true);
                LinearLayoutManager layoutManager = new LinearLayoutManager(mContext) {
                    @Override
                    public boolean canScrollVertically() {
                        return false;
                    }
                };
                if (visibleTopView && position == 0) {
                  //  cellViewHolder.lv_top.setVisibility(View.VISIBLE);
                } else {
                    cellViewHolder.lv_top.setVisibility(View.GONE);
                }
                if (snap.getPlayListModel() != null) {
                    String name = allPlayListNameById(String.valueOf(snap.getPlayListModel().getCategories().getId()));
                    cellViewHolder.snapTextView.setText(name == null ? snap.getPlayListModel().getCategories().getName() : name);
                    layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                    cellViewHolder.mRecyclerView.setLayoutManager(layoutManager);
                    cellViewHolder.mRecyclerView.setNestedScrollingEnabled(false);
                    if (snap.getPlayListModel() != null) {
                        if(position==0){
                            HorizontalNewRecyclerAdapter adapter = new HorizontalNewRecyclerAdapter(snap.getPlayListModel().getAssetVideos(), onItemSelectionListener);
                            if (cellViewHolder.mRecyclerView != null)
                                cellViewHolder.mRecyclerView.setAdapter(adapter);
                        }else {
                            if(snap.getPlayListModel().getAssetVideos().size()>2){
                           ((CellViewHolder) viewHolder).tvMore.setVisibility(View.VISIBLE);
                        }else{
                            ((CellViewHolder) viewHolder).tvMore.setVisibility(View.GONE);
                        }
                            HorizontalRecyclerAdapter adapter = new HorizontalRecyclerAdapter(snap.getPlayListModel().getAssetVideos(), onItemSelectionListener);
                            if (cellViewHolder.mRecyclerView != null)
                                cellViewHolder.mRecyclerView.setAdapter(adapter);
                        }

                    }

                    int lastSeenFirstPosition = listPosition.get(position, 0);
                    if (lastSeenFirstPosition >= 0) {
                        cellViewHolder.mRecyclerView.scrollToPosition(lastSeenFirstPosition);
                    }
                    break;
                }
            }
        }
    }


    private String allPlayListNameById(String id) {

        List<CategoryListDataModel> categoryModels = CategoryService.getServices().getAll();
        if (categoryModels != null)
            for (CategoryListDataModel categoryModel : categoryModels) {
                if (String.valueOf(categoryModel.getId()).equalsIgnoreCase(id)) {
                    return categoryModel.getName();
                }
            }
        return null;
    }

    @Override
    public void onViewRecycled(@NonNull RecyclerView.ViewHolder viewHolder) {
        final int position = viewHolder.getAdapterPosition();
        CellViewHolder cellViewHolder = (CellViewHolder) viewHolder;
        LinearLayoutManager layoutManager = ((LinearLayoutManager) cellViewHolder.mRecyclerView.getLayoutManager());
        int firstVisiblePosition = layoutManager.findFirstVisibleItemPosition();
        listPosition.put(position, firstVisiblePosition);

        super.onViewRecycled(viewHolder);
    }

    @Override
    public int getItemCount() {
        if (mSnaps == null)
            return 0;
        return mSnaps.size();
    }

    /**
     * Sets live data.
     *
     * @param items the items
     */
    public void setLiveData(List<YoutubeSnap> items) {

        if (mSnaps == null)
            mSnaps = items;
        this.mSnaps.clear();
        this.mSnaps.addAll(items);
        notifyDataSetChanged();

    }


    private class CellViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        /**
         * The Snap text view.
         */
        TextView snapTextView, /**
         * The Tv more.
         */
        tvMore;
        private RecyclerView mRecyclerView;
        private View lv_top;

        private CellViewHolder(View itemView) {
            super(itemView);

            mRecyclerView = itemView.findViewById(R.id.recyclerView);
            lv_top = itemView.findViewById(R.id.lv_top);
            snapTextView = itemView.findViewById(R.id.snapTextView);
            snapTextView.setTextColor(Color.WHITE);
            tvMore = itemView.findViewById(R.id.tv_more);
            tvMore.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            // Preventing multiple clicks, using threshold of 1 second
            if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                return;
            }
            mLastClickTime = SystemClock.elapsedRealtime();
            if (v == tvMore) {
                if (onItemSelectionListener != null) {
                    int pos = getAdapterPosition();
                    if (mSnaps != null && mSnaps.size() > 0) {
                        if (mSnaps.get(pos) != null) {
                            if (mSnaps.get(pos).getPlayListModel() != null) {
                                if (mSnaps.get(pos).getPlayListModel().getAssetVideos() != null && mSnaps.get(pos).getPlayListModel().getAssetVideos().size() > 0) {
                                    onItemSelectionListener.onMoreItemsClicked(mSnaps.get(pos).getPlayListModel().getAssetVideos().get(0));
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}