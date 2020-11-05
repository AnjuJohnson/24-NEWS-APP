package com.tfApp.android.newstv.presenter.activity;

import android.app.FragmentTransaction;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;

import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.ottapp.android.basemodule.app.GlideApp;
import com.ottapp.android.basemodule.presenters.activity.BaseActivityPresenter;
import com.tfApp.android.newstv.R;
import com.tfApp.android.newstv.app.FlowersTvApp;
import com.tfApp.android.newstv.presenter.activity.iview.SearchScreenActivityIView;
import com.tfApp.android.newstv.view.fragment.SearchVideoGridFragment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class SearchScreenActivityPresenter<I extends SearchScreenActivityIView> extends BaseActivityPresenter<I> {
    private String pathFile = "background";
    private SearchVideoGridFragment fragment;

    public SearchScreenActivityPresenter(I iView) {
        super(iView);
    }

    //to set the fragmentito the activity
    public void setFragment() {
        fragment = new SearchVideoGridFragment();
        android.app.FragmentManager fragmentManager1 = getIView().getActivity().getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager1.beginTransaction();
        fragmentTransaction.replace(R.id.search_fragment, fragment);
        fragmentTransaction.commit();
    }

    public void onCreate() {
      //  loadBackground();
    }

    //for the background color images etc
    private void loadBackground() {
        GlideApp.with(getIView().getContext())
                .asBitmap()
                .placeholder(getDrawable())
                .load(getIView().getContext().getString(R.string.app_background_image))
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, Transition<? super Bitmap> transition) {
                        getIView().getRootView().setImageBitmap(resource);
                        saveBitmap(resource);
                    }
                });
    }

    private Drawable getDrawable() {
        String photoPath = FlowersTvApp.getApplication().getCacheDir() + "/" + pathFile;
        if (new File(photoPath).exists()) {
            return Drawable.createFromPath(photoPath);
        } else {
            return null;
        }
    }

    private void saveBitmap(Bitmap bitmap) {
        if (bitmap != null) {
            String file = FlowersTvApp.getApplication().getCacheDir() + "/" + pathFile;
            try {
                FileOutputStream outputStream = null;
                try {
                    outputStream = new FileOutputStream(file); //here is set your file path where you want to save or also here you can set file object directly

                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream); // bitmap is your Bitmap instance, if you want to compress it you can compress reduce percentage
                    // PNG is a lossless format, the compression factor (100) is ignored
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (outputStream != null) {
                            outputStream.flush();
                            outputStream.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //for passing data to the search fragment
    public void loadResult(String query) {
        fragment.getPresenter().startQuery(query);
    }

    //to clear the quesr data
    public void clearResults() {
        fragment.getPresenter().startQuery(null);
    }
}