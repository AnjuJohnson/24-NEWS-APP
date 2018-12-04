package com.tfApp.android.newstv.adaptors;

import android.bitryt.com.youtubedataapi.background.OnLoadingCompletedListener;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tfApp.android.newstv.R;
import com.ottapp.android.basemodule.app.GlideApp;
import com.ottapp.android.basemodule.models.AssetVideosDataModel;

import java.util.List;

/**
 * Created by Jeffrey Liu on 3/21/16.
 */
public class HorizontalNewRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements OnLoadingCompletedListener<AssetVideosDataModel> {
    private List<AssetVideosDataModel> mList;
    private OnYoutubeItemSelectionListener onItemSelectionListener;
    private int position;
    public HorizontalNewRecyclerAdapter(List<AssetVideosDataModel> mSnaps, OnYoutubeItemSelectionListener onItemSelectionListener) {
        this.mList = mSnaps;

        if (mList!=null&&mList.isEmpty()) {
            loadMore();
        }
        this.onItemSelectionListener=onItemSelectionListener;
    }

    @Override
    public void onLoadingCompleted(List<AssetVideosDataModel> result, boolean loadMoreResult) {
        notifyDataSetChanged();
    }

    @Override
    public void onLoadingStarted() {

    }

    private class CellViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView textView;
        ImageView imageView, iv_favourite;
        CellViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.nameTextView);
            imageView = itemView.findViewById(R.id.imageView);
            itemView.setOnClickListener(this);
            iv_favourite = itemView.findViewById(R.id.iv_favourite);
            imageView.setOnClickListener(this);
            iv_favourite.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (view == iv_favourite) {
                if (onItemSelectionListener != null) {
                    int position = getAdapterPosition();
                    AssetVideosDataModel videoModel = mList.get(position);
                    if(videoModel.getFavourite() == -1){
                        videoModel.setFavourite(1);
                    }
                    else if (videoModel.getFavourite() == 1) {
                        videoModel.setFavourite(0);
                    } else {
                        videoModel.setFavourite(1);
                    }
                    notifyItemChanged(position);
                    onItemSelectionListener.onFavouriteActionSelected(videoModel, position);
                }
            } else {
                if (onItemSelectionListener != null)
                    onItemSelectionListener.onItemSelect(mList.get(getAdapterPosition()));
            }
        }


    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull final ViewGroup viewGroup, int viewType) {
        View v1 = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adapter_new_row, viewGroup, false);
        return new CellViewHolder(v1);
    }



    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        this.position = position;
        switch (viewHolder.getItemViewType()) {
            default: {
                if (mList != null) {

                    AssetVideosDataModel media = mList.get(position);
                    if (media != null) {
                        CellViewHolder cellViewHolder = (CellViewHolder) viewHolder;
                        cellViewHolder.textView.setText(media.getName());

//Comedy Utsavam │Ep# 72
                        GlideApp.with(cellViewHolder.imageView).load(media.getThumbnailUrl()).into(cellViewHolder.imageView);
                        String title = media.getName().replace("Flowers│", "");
                        title = title.replace("Flowers │", "");
                        title = title.replace("Flowers|", "");
                        title = title.replace("Flowers |", "");
                        //Mamangam | മാമാങ്കം | Flowers | Ep# 01
                        cellViewHolder.textView.setText(title);
                   //     if (media.getFavourite() == -1) {
                            if (media.getFavourite() == 1) {
                                cellViewHolder.iv_favourite.setBackgroundResource(R.drawable.ic_favorite_active);
                                media.setFavourite(1);
                            } else {
                                cellViewHolder.iv_favourite.setBackgroundResource(R.drawable.ic_favorite_inactive);
                                media.setFavourite(0);
                            }


                        }
                        if (position == getItemCount() - 1 && getItemCount() < 6) {
                            loadMore();
                        }
                    }
                    break;
                }
            }
    }

    private void loadMore() {
//        if (playListModel!=null&&playListModel.getNextPageToken() != null)
//            new GetPlayListVideosAsyncTask(this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, playListModel);
    }

    @Override
    public int getItemCount() {
        if (mList == null)
            return 0;
        return mList.size();
    }

}