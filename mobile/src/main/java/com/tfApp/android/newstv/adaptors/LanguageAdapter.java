package com.tfApp.android.newstv.adaptors;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.ottapp.android.basemodule.models.LanguageModel;
import com.tfApp.android.newstv.R;

import java.util.List;

public class LanguageAdapter extends RecyclerView.Adapter<LanguageAdapter.ViewHolder> {

    private List<LanguageModel> detailsModel;
    private OnLanguageSelectionListener onItemSelectionListener;


    public LanguageAdapter(List<LanguageModel> detailsModl, OnLanguageSelectionListener onItemSelectionListener) {
        this.detailsModel = detailsModl;
        this.onItemSelectionListener = onItemSelectionListener;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.language_adapter, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull LanguageAdapter.ViewHolder holder, int position) {

        if (detailsModel != null && detailsModel.size() > position) {

            LanguageModel media = detailsModel.get(position);
            String title = media.getName();

            holder.nameTextView.setText(title);

          if(title.equals("Malayalam")){
              holder.languageBox.setChecked(true);
              media.setSelected(1);
          }
            if (media.getSelected() == 1) {
                holder.languageBox.setChecked(true);
            } else {
                holder.languageBox.setChecked(false);
            }

            if (position == getItemCount() - 1 ) {

            }
        }

    }
   public List<LanguageModel> getSelectedLanguages(){
        return detailsModel;
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
        private CheckBox languageBox;

        ViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.lanuage_text);
            languageBox = itemView.findViewById(R.id.language_check);

            nameTextView.setOnClickListener(this);
            languageBox.setOnClickListener(this);

        }


        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if (position > -1 && detailsModel != null && detailsModel.size() > position) {
                if (v == nameTextView) {
                    if (onItemSelectionListener != null) {
                        onItemSelectionListener.onItemSelect(detailsModel.get(position));
                    }
                }else if (v == languageBox) {
                    if (onItemSelectionListener != null) {
                        if(detailsModel.get(position).getSelected() == 1){
                            detailsModel.get(position).setSelected(-1);
                        }
                       else if(detailsModel.get(position).getSelected() == -1) {
                            detailsModel.get(position).setSelected(1);
                        }else{
                            detailsModel.get(position).setSelected(-1);
                        }
                        notifyDataSetChanged();
                    }
                } else {
                    if (onItemSelectionListener != null)
                        onItemSelectionListener.onItemSelect(detailsModel.get(position));
                }
            }
        }
    }
}

