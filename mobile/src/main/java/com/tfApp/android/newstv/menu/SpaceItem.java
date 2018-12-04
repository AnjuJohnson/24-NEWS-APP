package com.tfApp.android.newstv.menu;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;


public class SpaceItem extends DrawerItem<SpaceItem.ViewHolder> {

    private int spaceDp;
    private float spaceSdp;

    public SpaceItem(int spaceDp) {
        this.spaceDp = spaceDp;
    }

    public SpaceItem(float spaceSdp) {
        this.spaceSdp = spaceSdp;
    }

    @Override
    public ViewHolder createViewHolder(ViewGroup parent) {
        Context c = parent.getContext();
        View view = new View(c);

        int height = 0;
        if (spaceDp != 0)
            height = (int) (c.getResources().getDisplayMetrics().density * spaceDp);
        else
            height = (int) spaceSdp;
        view.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                height));
        return new ViewHolder(view);
    }

    @Override
    public void bindViewHolder(ViewHolder holder) {

    }

    @Override
    public String getAction() {
        return null;
    }

    @Override
    public String getActionName() {
        return null;
    }

    @Override
    public boolean isSelectable() {
        return false;
    }

    static class ViewHolder extends DrawerAdapter.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }


}
