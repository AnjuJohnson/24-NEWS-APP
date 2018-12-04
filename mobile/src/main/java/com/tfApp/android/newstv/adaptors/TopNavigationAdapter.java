package com.tfApp.android.newstv.adaptors;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.v7.widget.ListPopupWindow;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tfApp.android.newstv.R;
import com.tfApp.android.newstv.app.FlowersTvApp;
import com.tfApp.android.newstv.models.PopupMenuItem;

import java.util.List;

public class TopNavigationAdapter extends RecyclerView.Adapter<TopNavigationAdapter.ViewHolder> {

    private List<PopupMenuItem> menuModels;

    private int maximumMenuLength = 7;
    private Handler handler;
    private ColorStateList themeColorStateList = new ColorStateList(
            new int[][]{
                    new int[]{android.R.attr.state_pressed},
                    new int[]{android.R.attr.state_selected},
                    new int[]{} // this should be empty to make default color as we want
            },
            new int[]{
                    getColorFromRes(R.color.black),
                    getColorFromRes(R.color.black),
                    getColorFromRes(R.color.white)
            }
    );
    private ListPopupWindow popupWindow;
    private boolean consumed = false;
    private PopupMenuAdapter popupMenuAdapter;
    private OnOptionSelectedListener onOptionSelectedListener;

    public TopNavigationAdapter(Context context, List<PopupMenuItem> menuModels, int maximumMenuLength) {
        if (maximumMenuLength > 1)
            this.maximumMenuLength = maximumMenuLength;
        if (menuModels.size() > maximumMenuLength) {
            popupMenuAdapter = new PopupMenuAdapter(context);
            popupMenuAdapter.addAll(menuModels.subList(maximumMenuLength - 1, menuModels.size()));
            this.menuModels = menuModels.subList(0, maximumMenuLength - 1);
        } else
            this.menuModels = menuModels;
        handler = new Handler();
    }

    public void setCurrentSelection(int position) {
        if (menuModels == null)
            return;
        int max = menuModels.size();
        if (popupMenuAdapter != null)
            max += popupMenuAdapter.getCount();
        int limit = maximumMenuLength - 1;

        if (max > position) {
            if (position >= limit) {
                int temp_pos = position - limit;
                boolean showSelection = false;
                if (onOptionSelectedListener != null) {
                    showSelection = onOptionSelectedListener.onOptionSelected(popupMenuAdapter.getItem(temp_pos));
                }
                if (showSelection) {
                    setSelected(limit);
                    setSelectedInOptions(temp_pos);
                }
            } else {
                boolean showSelection = false;
                if (onOptionSelectedListener != null) {
                    showSelection = onOptionSelectedListener.onOptionSelected(menuModels.get(position));
                }
                if (showSelection) {
                    setSelected(position);
                }
            }
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.top_menu_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        PopupMenuItem item = menuModels.get(position);
        holder.nameTextView.setText(item.getTitle());
        if (item.isSelected()) {
            holder.nameTextView.setTextColor(getColorFromRes(R.color.red));
        } else {
            holder.nameTextView.setTextColor(getColorFromRes(R.color.white));
        }

    }

    private int getColorFromRes(@ColorRes int color) {
        return FlowersTvApp.getApplication().getResources().getColor(color);
    }

    private Drawable getDrawableFromRes(@DrawableRes int drawable) {
        return FlowersTvApp.getApplication().getResources().getDrawable(drawable);
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return menuModels.size();
    }

    public void setOnOptionSelectedListener(OnOptionSelectedListener onOptionSelectedListener) {
        this.onOptionSelectedListener = onOptionSelectedListener;
    }

    private void setSelected(int pos) {
        if (menuModels == null)
            return;
        for (int i = 0; i < menuModels.size(); i++) {
            menuModels.get(i).setSelected(i == pos);
        }
        handler.post(this::notifyDataSetChanged);
    }

    private void setSelectedInOptions(int pos) {
        if (popupMenuAdapter == null)
            return;
        for (int i = 0; i < popupMenuAdapter.getCount(); i++) {
            PopupMenuItem popupMenuItem = popupMenuAdapter.getItem(i);
            if (popupMenuItem != null)
                popupMenuItem.setSelected(i == pos);
        }
    }

    private void showListMenu(final View anchor) {
        if (popupMenuAdapter == null)
            return;
        consumed = false;
        popupWindow = new ListPopupWindow(anchor.getContext());
        popupWindow.setBackgroundDrawable(null);
        popupWindow.setAnchorView(anchor);
        popupWindow.setAdapter(popupMenuAdapter);
        popupWindow.setWidth(anchor.getContext().getResources().getDimensionPixelSize(R.dimen._130sdp));
        popupWindow.setOnItemClickListener((parent, view, position, id) -> {
            consumed = true;
            setSelectedInOptions(position);
            if (onOptionSelectedListener != null) {
                consumed = onOptionSelectedListener.onOptionSelected(popupMenuAdapter.getItem(position));
            }
            popupWindow.dismiss();
        });
        popupWindow.setOnDismissListener(() -> {
            if (consumed) {
                setSelected(maximumMenuLength - 1);
            }
        });

        popupWindow.show();
    }

    public interface OnOptionSelectedListener {
        boolean onOptionSelected(PopupMenuItem popupMenuItem);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView nameTextView;

        ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            //itemView.setBackgroundColor(Color.TRANSPARENT);
            nameTextView = itemView.findViewById(R.id.tv_title);

        }

        @Override
        public void onClick(View v) {
            final int pos = getAdapterPosition();
            if (pos == maximumMenuLength - 1) {
                showListMenu(v);
            } else {
                PopupMenuItem menuItem = menuModels.get(pos);
                setSelected(pos);
                setSelectedInOptions(-1);
                if (onOptionSelectedListener != null) {
                    onOptionSelectedListener.onOptionSelected(menuItem);
                }
            }
        }
    }

}

