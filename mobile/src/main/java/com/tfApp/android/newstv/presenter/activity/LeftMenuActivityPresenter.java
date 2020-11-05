package com.tfApp.android.newstv.presenter.activity;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;

import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.ottapp.android.basemodule.app.GlideApp;
import com.ottapp.android.basemodule.models.MenuDataModel;
import com.ottapp.android.basemodule.presenters.activity.BaseActivityPresenter;
import com.ottapp.android.basemodule.services.MenuServices;
import com.ottapp.android.basemodule.view.base.fragment.BaseFragment;
import com.tfApp.android.newstv.R;
import com.tfApp.android.newstv.app.FlowersTvApp;
import com.tfApp.android.newstv.menu.DrawerAdapter;
import com.tfApp.android.newstv.menu.DrawerItem;
import com.tfApp.android.newstv.menu.SimpleItem;
import com.tfApp.android.newstv.presenter.activity.iview.LeftMenuActivityIView;
import com.tfApp.android.newstv.utils.MenuPositionComparator;
import com.tfApp.android.newstv.view.fragment.AboutFragment;
import com.tfApp.android.newstv.view.fragment.HomeFragment;
import com.tfApp.android.newstv.view.fragment.HtmlViewFragment;
import com.tfApp.android.newstv.view.fragment.InfoFragment;
import com.tfApp.android.newstv.view.fragment.LanguageFragment;
import com.tfApp.android.newstv.view.fragment.YoutubeVideoGridVideoGridFragment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.tfApp.android.newstv.view.activity.MenuLeftActivity.POS_MORE_SCREE;
import static com.tfApp.android.newstv.view.activity.MenuLeftActivity.POS_MORE_SCREEN;

public class LeftMenuActivityPresenter<I extends LeftMenuActivityIView> extends BaseActivityPresenter<I> implements DrawerAdapter.OnItemSelectedListener {
    private static final String ACTION_HOME = "home";

    private static final String ACTION_LANGUAGE = "language";
    private static final String ACTION_SETTINGS = "settings";
    private static final String ACTION_FAVOURITES = "favourites";
    private static final String ACTION_ABOUT = "about";
    private static final String ACTION_CONTACT_US = "contact";
    private static final String ACTION_TERMS = "terms";
    private static final String ACTION_EXIT = "exit";
    private static final String ACTION_INFO = "Info";

    private static String POS_DEFAULT = ACTION_HOME;
    private String pathFile = "background";
    private DrawerAdapter adapter;
    private BaseFragment fragmentBase = null;
    private int prev_pos, current;
    private AlertDialog dialog;
    private List<DrawerItem> drawerItems;
    private List<MenuDataModel> menuModels;
    private SimpleItem simpleItem;



    public LeftMenuActivityPresenter(I iView) {
        super(iView);

    }

    public void clear() {
        super.release();
    }

    private int getMenuPositionByAction(String action) {
        if (action != null)
            for (int i = 0; i < drawerItems.size(); i++) {
                if (drawerItems.get(i).getActionName() != null &&
                        drawerItems.get(i).getActionName().equalsIgnoreCase(action)) {
                    return i;
                }
            }
        return 0;
    }

    //setting the data into the adapter
    public void setAdapter() {

        menuModels = MenuServices.getServices().getAll(); //here we get all the menu item stored in room db
        if (menuModels != null) {

            drawerItems = new ArrayList<>(menuModels.size());
            Collections.sort(menuModels, new MenuPositionComparator());
            for (MenuDataModel menuModel : menuModels) {
                simpleItem = new SimpleItem(menuModel.getIconUrl(), menuModel.getName(), menuModel.getSortOrder(), menuModel.getAction());


                simpleItem.withIconTint(Color.WHITE)
                        .withTextTint(Color.WHITE)
                        .withSelectedIconTint(Color.RED)
                        .withSelectedTextTint(Color.RED);
                drawerItems.add(simpleItem);

            }

            if(!drawerItems.isEmpty()) {
                adapter = new DrawerAdapter(drawerItems);
                adapter.setListener(this);

                getIView().getRecyclerView().setNestedScrollingEnabled(false);
                getIView().getRecyclerView().setLayoutManager(new LinearLayoutManager(getIView().getContext()));
                getIView().getRecyclerView().setAdapter(adapter);
                adapter.setSelected(getMenuPositionByAction(POS_DEFAULT));
            }

        }
    }

    //for the live data
    public void updateLiveData(List<MenuDataModel> menus) {
        menuModels = menus;
        drawerItems = new ArrayList<>(menus.size());
        Collections.sort(menuModels, new MenuPositionComparator());
        for (MenuDataModel menuModel : menus) {
            simpleItem = new SimpleItem(menuModel.getIconUrl(), menuModel.getName(), menuModel.getSortOrder(), menuModel.getAction());

            simpleItem.withIconTint(Color.WHITE)
                    .withTextTint(Color.WHITE)
                    .withSelectedIconTint(Color.RED)
                    .withSelectedTextTint(Color.RED);
            drawerItems.add(simpleItem);

        }
        if(!drawerItems.isEmpty()) {
            adapter.updateLiveData(drawerItems);
            adapter.setSelected(getMenuPositionByAction(POS_DEFAULT));
            adapter.notifyDataSetChanged();
        }
    }

    public void adapterSetSelected(int position) {
        if (adapter != null)
            adapter.setSelected(position);
        else {
            setAdapter();
            if (adapter != null)
                adapter.setSelected(position);
        }
    }

    public int getCurrentPosition() {
        return current;
    }

    @Override
    public void onItemSelected(int position, String actionName, String action) { //here we get the selection
        current = position;
        if (action != null && action.equalsIgnoreCase(ACTION_EXIT)) {
            askExitDialog();
            return;
        }
        if (position == POS_MORE_SCREEN) {
            if (getIView().activity().getIntent() != null) {
                Intent intent = getIView().activity().getIntent();
                int categoryIds = intent.getIntExtra(YoutubeVideoGridVideoGridFragment.LOAD_DATA, 0);
                String titleText = intent.getStringExtra(YoutubeVideoGridVideoGridFragment.TITLE_TEXT);
                Bundle bundle = new Bundle();
                bundle.putSerializable(YoutubeVideoGridVideoGridFragment.LOAD_DATA, categoryIds);
                bundle.putSerializable(YoutubeVideoGridVideoGridFragment.KEY_LOADER_TYPE,
                        YoutubeVideoGridVideoGridFragment.LOADER_TYPE_CATEGORY);
                bundle.putString(YoutubeVideoGridVideoGridFragment.TITLE_TEXT, titleText);
                fragmentBase = new YoutubeVideoGridVideoGridFragment();
                fragmentBase.setArguments(bundle);
                if (fragmentBase != null)
                    showFragment(fragmentBase);
            }
            return;
        }
        if(position == POS_MORE_SCREE ){
            fragmentBase = new HomeFragment();
            Bundle bundle = new Bundle();

            bundle.putString(YoutubeVideoGridVideoGridFragment.TITLE_TEXT, "Home");
            bundle.putSerializable(YoutubeVideoGridVideoGridFragment.KEY_LOADER_TYPE,
                    YoutubeVideoGridVideoGridFragment.LOADER_TYPE_MENU);
            fragmentBase.setArguments(bundle);
            if (fragmentBase != null)
                showFragment(fragmentBase);
        return;
        }

        if (menuModels != null && menuModels.size() > position) {
            prev_pos = position;

            MenuDataModel currentMenu = menuModels.get(position);
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    getIView().slidingRootNav().closeMenu();
                }
            });
//            getIView().slidingRootNav().closeMenu();
            //switch fragment here
            if (action != null) {
                switch (action) {

                    case ACTION_FAVOURITES:
                        fragmentBase = new LanguageFragment();
                       // fragmentBase = new FavouriteVideoGridFragment();

                        break;
                    case ACTION_LANGUAGE:
                        fragmentBase = new LanguageFragment();

                        break;
                    case ACTION_ABOUT:
                        fragmentBase = new AboutFragment();
                        Bundle bundle = new Bundle();
                        bundle.putString(AboutFragment.PAGE_ACTION, action);
                        bundle.putString(AboutFragment.PAGE_TITLE, currentMenu.getName());
                        fragmentBase.setArguments(bundle);
                        break;
                    case ACTION_SETTINGS:
                        fragmentBase = new InfoFragment();
                        bundle = new Bundle();
                        bundle.putString(YoutubeVideoGridVideoGridFragment.TITLE_TEXT, currentMenu.getName());
                        fragmentBase.setArguments(bundle);
                        break;
                    case ACTION_TERMS:
                        fragmentBase = new AboutFragment();
                        bundle = new Bundle();
                        bundle.putString(AboutFragment.PAGE_ACTION, action);
                        bundle.putString(AboutFragment.PAGE_TITLE, currentMenu.getName());
                        fragmentBase.setArguments(bundle);
                        break;
                    case ACTION_CONTACT_US:
                        fragmentBase = new HtmlViewFragment();
                        bundle = new Bundle();
                        bundle.putString(HtmlViewFragment.WEB_URL, "http://www.flowerstv.in/contact-us/");
                        bundle.putString(HtmlViewFragment.PAGE_TITLE, currentMenu.getName());
                        fragmentBase.setArguments(bundle);
                        break;
                    case ACTION_INFO:
                        fragmentBase = new InfoFragment();
                        bundle = new Bundle();
                        bundle.putString(YoutubeVideoGridVideoGridFragment.TITLE_TEXT, currentMenu.getName());
                        fragmentBase.setArguments(bundle);
                        break;
                    default:
                        if (action != null) {
                            if (action.equalsIgnoreCase(ACTION_HOME)) {
                                fragmentBase = new HomeFragment();
                            } else {
                                fragmentBase = new YoutubeVideoGridVideoGridFragment();
                            }
                        } else {
                            fragmentBase = new YoutubeVideoGridVideoGridFragment();
                        }
                        bundle = new Bundle();
                        bundle.putSerializable(YoutubeVideoGridVideoGridFragment.LOAD_DATA, currentMenu.getId());
                        bundle.putString(YoutubeVideoGridVideoGridFragment.TITLE_TEXT, currentMenu.getName());
                        bundle.putSerializable(YoutubeVideoGridVideoGridFragment.KEY_LOADER_TYPE,
                                YoutubeVideoGridVideoGridFragment.LOADER_TYPE_MENU);
                        fragmentBase.setArguments(bundle);
                        break;
                }
            }else{
                Bundle bundle = new Bundle();

                    if (actionName.equalsIgnoreCase(ACTION_HOME)) {
                        fragmentBase = new HomeFragment();
                    } else {
                        fragmentBase = new YoutubeVideoGridVideoGridFragment();
                    }

                bundle.putSerializable(YoutubeVideoGridVideoGridFragment.LOAD_DATA, currentMenu.getId());
                bundle.putString(YoutubeVideoGridVideoGridFragment.TITLE_TEXT, currentMenu.getName());
                bundle.putSerializable(YoutubeVideoGridVideoGridFragment.KEY_LOADER_TYPE,
                        YoutubeVideoGridVideoGridFragment.LOADER_TYPE_MENU);
                fragmentBase.setArguments(bundle);

            }
            if (fragmentBase != null)
                showFragment(fragmentBase);
        }
    }

    private void showFragment(Fragment fragment) {
        getIView().activity().getFragmentManager().beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
    }

    private void askExitDialog() { //for showing the exit dialog
        if (dialog != null)
            dialog.cancel();
        if (getIView() != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getIView().getContext());
            builder.setTitle("Confirm Exit");
            builder.setMessage("Do you really want to exit?");
            builder.setPositiveButton("Yes", (dialog, which) -> {
                getIView().finish();
            });
            builder.setNegativeButton("Cancel", (dialog, which) -> {
                dialog = builder.create();
                dialog.dismiss();
            });


            try {
                dialog.show();
            } catch (Exception ignored) {
                //bad window token happened
                dialog = builder.create();
                dialog.dismiss();
            }
        }
    }

    public void onCreate() {
        //loadBackground();
    }

    //for showing the background images color etc
    @SuppressLint("ResourceType")
    private void loadBackground() {
        GlideApp.with(getIView().getContext())
                .asBitmap()
                .placeholder(getDrawable())
                .load(getIView().getContext().getString(R.drawable.app_bg2))
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, Transition<? super Bitmap> transition) {
                        //getIView().getRootView().setBackground(new BitmapDrawable(getIView().getContext().getResources(),Bitmap.createScaledBitmap(resource, getIView().getContext().getResources().getDimensionPixelSize(R.dimen._200sdp), getIView().getContext().getResources().getDimensionPixelSize(R.dimen._600sdp), false)));
                        try {
                            getIView().getRootView().setImageBitmap(resource);
                            getIView().getMenuRootView().setImageBitmap(resource);
                            saveBitmap(resource);
                        } catch (NullPointerException ignored) {
                            ignored.printStackTrace();
                        }
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

    //for the back pressed moements
    public void onBackPressed() {
        if (getIView().getSlidingRootNav().isMenuOpened()) {
            getIView().getSlidingRootNav().closeMenu();
        } else if (fragmentBase != null && (fragmentBase instanceof HomeFragment)) {
            askExitDialog();
        } else if (fragmentBase != null && current == POS_MORE_SCREEN) {
            adapterSetSelected(prev_pos);
        } else {
            adapterSetSelected(getMenuPositionByAction(POS_DEFAULT));
        }
    }
}
