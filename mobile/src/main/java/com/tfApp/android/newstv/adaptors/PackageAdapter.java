package com.tfApp.android.newstv.adaptors;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ottapp.android.basemodule.app.GlideApp;
import com.ottapp.android.basemodule.models.PackageModel;
import com.tfApp.android.newstv.R;

import java.util.List;

public class PackageAdapter extends RecyclerView.Adapter<PackageAdapter.ViewHolder> {

    private List<PackageModel> detailsModel;
    private PackageItemSelectionListener onItemSelectionListener;


    public PackageAdapter(List<PackageModel> detailsModel, PackageItemSelectionListener onItemSelectionListener) {
        this.detailsModel = detailsModel;
        this.onItemSelectionListener = onItemSelectionListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.package_adapter, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PackageAdapter.ViewHolder holder, int position) {
        if (detailsModel != null && detailsModel.size() > position) {
            PackageModel media = detailsModel.get(position);
            GlideApp.with(holder.imageView).load(media.getColorCode()).into(holder.imageView);
            String title = media.getPackageName();
            holder.nameTextView.setText(title);
            holder.priceTexView.setText(String.valueOf("$ "+media.getPrice()+ " /Month"));

            if (position == getItemCount() - 1) {
                if (onItemSelectionListener != null)
                    onItemSelectionListener.onMoreItemsNeeded();
            }
        }

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

        private TextView nameTextView,priceTexView,buyTextView;
        private ImageView imageView;
        private CardView cardView;
        private RelativeLayout layout;

        ViewHolder(View itemView) {
            super(itemView);
            priceTexView = itemView.findViewById(R.id.tvrate);
            nameTextView = itemView.findViewById(R.id.tvMessages);
            imageView = itemView.findViewById(R.id.packageImage);
            cardView = itemView.findViewById(R.id.card_view);
            layout = itemView.findViewById(R.id.packageLayout);
            buyTextView = itemView.findViewById(R.id.tvBuy);
            buyTextView.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if (position > -1 && detailsModel != null && detailsModel.size() > position) { {
                if(v == buyTextView){
                    if (onItemSelectionListener != null)
                    onItemSelectionListener.onItemSelect(detailsModel.get(position));
                }
                }
            }
        }
    }
}

