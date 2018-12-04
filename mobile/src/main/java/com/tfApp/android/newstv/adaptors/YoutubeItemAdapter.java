package com.tfApp.android.newstv.adaptors;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tfApp.android.newstv.R;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.ottapp.android.basemodule.app.GlideApp;
import com.ottapp.android.basemodule.models.AssetVideosDataModel;

import java.util.List;

public class YoutubeItemAdapter extends RecyclerView.Adapter<YoutubeItemAdapter.ViewHolder> {

    private List<AssetVideosDataModel> detailsModel;
    private OnYoutubeItemSelectionListener onItemSelectionListener;
    private String query;
    private boolean loadMore;
    private RequestOptions ro;


    public YoutubeItemAdapter(List<AssetVideosDataModel> detailsModel, OnYoutubeItemSelectionListener onItemSelectionListener, boolean loadMore) {
        this.query = query;
        this.detailsModel = detailsModel;
        this.onItemSelectionListener = onItemSelectionListener;
        this.loadMore = loadMore;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull YoutubeItemAdapter.ViewHolder holder, int position) {

        if (detailsModel != null && detailsModel.size() > position) {
            ro = new RequestOptions()
                    .centerCrop()
                    .placeholder(R.drawable.thumb_default)
                    .error(R.drawable.thumb_default)
                    .diskCacheStrategy(DiskCacheStrategy.DATA) // because file name is always same
                    .skipMemoryCache(false);
            AssetVideosDataModel media = detailsModel.get(position);
            GlideApp.with(holder.imageView).load(media.getThumbnailUrl()).apply(ro).into(holder.imageView);
            String title = media.getName().replace("Flowers│", "");
            title = title.replace("Flowers │", "");
            title = title.replace("Flowers|", "");
            title = title.replace("Flowers |", "");
            holder.nameTextView.setText(title);

            if (media.getFavourite() == 1) {
                holder.iv_favourite.setBackgroundResource(R.drawable.ic_favorite_active);
                media.setFavourite(1);
            } else {
                holder.iv_favourite.setBackgroundResource(R.drawable.ic_favorite_inactive);
                media.setFavourite(0);
            }
          /*  } else {
                if (media.getFavourite() == 1) {
                    holder.iv_favourite.setBackgroundResource(R.drawable.ic_favorite_active);
                } else {
                    holder.iv_favourite.setBackgroundResource(R.drawable.ic_favorite_inactive);
                }
            }*/


            if (position == getItemCount() - 1 ) {
                if (onItemSelectionListener != null)
                    onItemSelectionListener.onMoreItemsNeeded();
            }
        }

    }

    public void setLiveAssetsData(List<AssetVideosDataModel> data){
        this.detailsModel = data;
        //notifyDataSetChanged();
    }
    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return detailsModel.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView nameTextView;
        private ImageView imageView, iv_favourite;

        ViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            imageView = itemView.findViewById(R.id.imageView);
            iv_favourite = itemView.findViewById(R.id.iv_favourite);
            imageView.setOnClickListener(this);
            nameTextView.setOnClickListener(this);
            iv_favourite.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if (position > -1 && detailsModel != null && detailsModel.size() > position) {
                if (v == iv_favourite) {
                    if (onItemSelectionListener != null) {
                        AssetVideosDataModel videoModel = detailsModel.get(position);

                        if(videoModel.getFavourite() == -1){
                            videoModel.setFavourite(1);
                        }
                        else if (videoModel.getFavourite() == 1) {
                            videoModel.setFavourite(0);
                        } else {
                            videoModel.setFavourite(1);
                        }
                        notifyDataSetChanged();
                        onItemSelectionListener.onFavouriteActionSelected(videoModel, position);
                    }
                } else {
                    if (onItemSelectionListener != null)
                        onItemSelectionListener.onItemSelect(detailsModel.get(position));
                }
            }
        }
    }
}

