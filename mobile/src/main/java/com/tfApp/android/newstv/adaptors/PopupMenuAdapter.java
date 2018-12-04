package com.tfApp.android.newstv.adaptors;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tfApp.android.newstv.R;
import com.tfApp.android.newstv.app.FlowersTvApp;
import com.tfApp.android.newstv.models.PopupMenuItem;
import com.ottapp.android.basemodule.app.GlideApp;

public class PopupMenuAdapter extends ArrayAdapter<PopupMenuItem> {

    public PopupMenuAdapter(Context context) {
        super(context, 0);
    }

    @Override
    public View getView(int position, View contentView, ViewGroup parent) {
        ViewHolder holder;

        if (contentView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            contentView = inflater.inflate(R.layout.popup_menu_item, parent, false);
            holder = new ViewHolder(contentView);
            contentView.setTag(holder);
        } else {
            holder = (ViewHolder) contentView.getTag();
        }

        PopupMenuItem page = getItem(position);
        if (page != null)
            if (page.isSelected()) {
                holder.title.setTextColor(getColorFromRes(R.color.bottombar_variant));
                holder.image.setPressed(true);
                holder.image.setSelected(true);
            } else {
                holder.image.setPressed(false);
                holder.image.setSelected(false);
                holder.title.setTextColor(getColorFromRes(R.color.white));
            }
        if (page != null && !page.getTitle().trim().isEmpty()) {
            holder.title.setText(page.getTitle());
        }
        /*if (page != null && page.getImageUrl() != null) {
            GlideApp.with(holder.image.getApplication()).load(page.getImageUrl()).placeholder(page.getDrawable()).into(holder.image);
        } else */if (page != null && page.getDrawable() > 0) {
            GlideApp.with(holder.image.getContext()).load(page.getImageUrl()).into(holder.image);
           // holder.image.setImageDrawable(getDrawableFromRes(page.getDrawable()));
        }
        if (page!=null&&!page.isWashOut())
            holder.image.setImageTintList(null);
        return contentView;
    }

    private int getColorFromRes(@ColorRes int color) {
        return FlowersTvApp.getApplication().getResources().getColor(color);
    }

    private Drawable getDrawableFromRes(@DrawableRes int drawable) {
        return FlowersTvApp.getApplication().getResources().getDrawable(drawable);
    }

    private ColorStateList themeColorStateList = new ColorStateList(
            new int[][]{
                    new int[]{android.R.attr.state_pressed},
                    new int[]{android.R.attr.state_selected},
                    new int[]{} // this should be empty to make default color as we want
            },
            new int[]{
                    getColorFromRes(R.color.bottombar_variant),
                    getColorFromRes(R.color.bottombar_variant),
                    getColorFromRes(R.color.white)
            }
    );

    private class ViewHolder {
        public TextView title;
        public ImageView image;

        ViewHolder(View view) {
            this.title = view.findViewById(R.id.text);
            this.image = view.findViewById(R.id.icon);
            this.image.setImageTintList(themeColorStateList);
        }
    }

}

