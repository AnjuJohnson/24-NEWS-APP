package com.tfApp.android.newstv.adaptors;

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

public class ComingSoonItemAdapter extends RecyclerView.Adapter<ComingSoonItemAdapter.ViewHolder> {

    private List<AssetVideosDataModel> detailsModel;
    private OnYoutubeItemSelectionListener onItemSelectionListener;
    private String query;


    public ComingSoonItemAdapter(List<AssetVideosDataModel> detailsModel, OnYoutubeItemSelectionListener onItemSelectionListener) {
        this.query = query;
        this.detailsModel = detailsModel;
        this.onItemSelectionListener = onItemSelectionListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.coming_soon_adapter, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ComingSoonItemAdapter.ViewHolder holder, int position) {
        if (detailsModel != null && detailsModel.size() > position) {
            AssetVideosDataModel media = detailsModel.get(position);
            GlideApp.with(holder.imageView).load(media.getThumbnailUrl()).into(holder.imageView);
            String title = media.getName().replace("Flowers│", "");
            title = title.replace("Flowers │", "");
            title = title.replace("Flowers|", "");
            title = title.replace("Flowers |", "");
            holder.nameTextView.setText(title);

          /*  } else {
                if (media.getFavourite() == 1) {
                    holder.iv_favourite.setBackgroundResource(R.drawable.ic_favorite_active);
                } else {
                    holder.iv_favourite.setBackgroundResource(R.drawable.ic_favorite_inactive);
                }
            }*/
            if (position == getItemCount() - 1) {
                if (onItemSelectionListener != null)
                    onItemSelectionListener.onMoreItemsNeeded();
            }
        }

    }

    public void setLiveAssetsData(List<AssetVideosDataModel> data){
        this.detailsModel = data;
        notifyDataSetChanged();
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
        private ImageView imageView;

        ViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.title);
            imageView = itemView.findViewById(R.id.thumbnail);
            imageView.setOnClickListener(this);
            nameTextView.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
//            int position = getAdapterPosition();
//            if (position > -1 && detailsModel != null && detailsModel.size() > position) { {
//                    if (onItemSelectionListener != null)
//                        onItemSelectionListener.onItemSelect(detailsModel.get(position));
//                }
//            }
        }
    }
}

