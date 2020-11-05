package com.tfApp.android.newstv.menu;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ottapp.android.basemodule.app.GlideApp;
import com.ottapp.android.basemodule.models.MenuDataModel;
import com.tfApp.android.newstv.R;


import java.util.List;


public class SimpleItem extends DrawerItem<SimpleItem.ViewHolder> {

    private int selectedItemIconTint;
    private int selectedItemTextTint;

    private int normalItemIconTint;
    private int normalItemTextTint;

    private String icon;
    private String title;
    private int position;
    private List<MenuDataModel> menuDatumModels;
   // private MenuDataModel menuModel;

    public int getPosition() {
        return position;
    }



    private String action;

    public SimpleItem(String icon, String title,int position,String action) {
        this.icon = icon;
        this.title = title;
        this.position=position;
        this.action=action;

    }

    @Override
    public ViewHolder createViewHolder(ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.item_option, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void bindViewHolder(ViewHolder holder) {
        holder.title.setText(title);
        if (icon != null) {
            GlideApp.with(holder.icon).load(icon).into(holder.icon);
            holder.icon.setVisibility(View.VISIBLE);
        } else {
           // holder.icon.setVisibility(View.INVISIBLE);
        }

        holder.title.setTextColor(isChecked ? selectedItemTextTint : normalItemTextTint);
        holder.icon.setColorFilter(isChecked ? selectedItemIconTint : normalItemIconTint);
    }
    public String getAction() {
        return action;
    }
    @Override
    public String getActionName() {
        return title;
    }

    public SimpleItem withSelectedIconTint(int selectedItemIconTint) {
        this.selectedItemIconTint = selectedItemIconTint;
        return this;
    }


    public void setMenu(String icon, String title,int position,String action){
        this.icon = icon;
        this.title = title;
        this.position=position;
        this.action=action;
    }
    public SimpleItem withSelectedTextTint(int selectedItemTextTint) {
        this.selectedItemTextTint = selectedItemTextTint;
        return this;
    }

    public SimpleItem withIconTint(int normalItemIconTint) {
        this.normalItemIconTint = normalItemIconTint;
        return this;
    }

    public SimpleItem withTextTint(int normalItemTextTint) {
        this.normalItemTextTint = normalItemTextTint;
        return this;
    }

    static class ViewHolder extends DrawerAdapter.ViewHolder {

        private ImageView icon;
        private TextView title;

        ViewHolder(View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.icon);
            title = itemView.findViewById(R.id.title);
        }
    }
}
