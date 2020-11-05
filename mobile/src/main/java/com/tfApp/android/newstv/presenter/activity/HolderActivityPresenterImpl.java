package com.tfApp.android.newstv.presenter.activity;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;

import com.ottapp.android.basemodule.models.MenuDataModel;
import com.ottapp.android.basemodule.repository.responses.BottomBarVisibilityEvent;
import com.ottapp.android.basemodule.services.MenuServices;
import com.ottapp.android.basemodule.utils.ScreenSwitchEvent;
import com.ottapp.android.basemodule.view.base.fragment.BaseFragment;
import com.tfApp.android.newstv.R;
import com.tfApp.android.newstv.adaptors.BottomNavigationAdapter;
import com.tfApp.android.newstv.models.PopupMenuItem;
import com.tfApp.android.newstv.presenter.activity.iview.HolderActivityIView;
import com.tfApp.android.newstv.view.activity.HolderActivity;
import com.tfApp.android.newstv.view.activity.PollWebActivity;
import com.tfApp.android.newstv.view.activity.SearchActivityScreen;
import com.tfApp.android.newstv.view.fragment.FavouriteVideoGridFragment;
import com.tfApp.android.newstv.view.fragment.GenreFragment;
import com.tfApp.android.newstv.view.fragment.HomeFragment;
import com.tfApp.android.newstv.view.fragment.HtmlViewFragment;
import com.tfApp.android.newstv.view.fragment.InfoFragment;
import com.tfApp.android.newstv.view.fragment.LanguageFragmentIn;
import com.tfApp.android.newstv.view.fragment.PackageFragment;
import com.tfApp.android.newstv.view.fragment.ToolbarFragment;
import com.tfApp.android.newstv.view.fragment.YoutubeVideoGridVideoGridFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import static com.tfApp.android.newstv.view.activity.MenuLeftActivity.POS_MORE_SCREE;

public class HolderActivityPresenterImpl<I extends HolderActivityIView> extends HolderActivityPresenter implements BottomNavigationAdapter.OnOptionSelectedListener {
    private BaseFragment fragmentBase = null;
    List<BaseFragment> scrennHistory = new ArrayList<>();
    private static final String ACTION_HOME = "Home";
    private static final String ACTION_FAVOURITES = "Favourites";
    private static final String ACTION_ABOUT = "About Us";
    private static final String ACTION_CONTACT_US = "Contact Us";
    private static final String ACTION_TERMS = "Terms";
    private static final String ACTION_EXIT = "exit";
    private static final String ACTION_INFO = "Settings";
    private static final String ACTION_SEARCH = "Search";
    private static final String ACTION_POLL = "Polls";
    private static final String ACTION_COMING = "Coming Soon";
    private static String POS_DEFAULT = ACTION_HOME;
    private static final String ACTION_LANGUAGE = "Languages";
    private static final String ACTION_SUBSCRIBE = "Subscription";
    private List<PopupMenuItem> menuItemList;
    private String homeAction = "Home";
    private BottomNavigationAdapter navigationAdapter;


    public HolderActivityPresenterImpl(I iView) {
        super(iView);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN_ORDERED)
    public void screenChangeEvent(ScreenSwitchEvent screenSwitchEvent) {

    }

    @Subscribe(threadMode = ThreadMode.MAIN_ORDERED)
    public void onBottomBarVisibilityChangeRequest(BottomBarVisibilityEvent visibilityEvent) {
        if (visibilityEvent.isShowResquest()) {
            if (getIView().getBottomMenuRL() != null && getIView().getBottomMenuRL().getVisibility() != View.VISIBLE) {
                slideToAbove();
            }
        } else {
            if (getIView().getBottomMenuRL() != null && getIView().getBottomMenuRL().getVisibility() == View.VISIBLE) {
                slideToBottom();
            }
        }
    }
    public void onItemSelected(int position) {
            if (getIView().getActivity().getIntent() != null) {
                if (position == POS_MORE_SCREE) {
                   /* fragmentBase = new HomeFragment();
                    Bundle bundle = new Bundle();

                    bundle.putString(YoutubeVideoGridVideoGridFragment.TITLE_TEXT, "Home");
                    bundle.putSerializable(YoutubeVideoGridVideoGridFragment.KEY_LOADER_TYPE,
                            YoutubeVideoGridVideoGridFragment.LOADER_TYPE_MENU);
                    fragmentBase.setArguments(bundle);
                    if (fragmentBase != null)
                        showFragment(fragmentBase);*/
                    setupMenu();
                    return;
                } else {
                    Intent intent = getIView().getActivity().getIntent();
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
            }
    }
    public void showGenreFragment(){
        GenreFragment genreFragment = new GenreFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(YoutubeVideoGridVideoGridFragment.KEY_LOADER_TYPE,
                YoutubeVideoGridVideoGridFragment.LOADER_TYPE_MENU);
        genreFragment.setArguments(bundle);
        showFragment(genreFragment);

    }


    public void onDestroy(){
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

  //  private BottomNavigationViewPagerAdapter adapter;

    public void setupMenu() {
        menuItemList = new ArrayList<>();
        List<MenuDataModel> menuLists = MenuServices.getServices().getAll();
        for(MenuDataModel menuDataModel : menuLists) {
            PopupMenuItem popupMenuItem = new PopupMenuItem(menuDataModel.getId(), menuDataModel.getName(), menuDataModel.getIconUrl(), R.drawable.ic_action_channel);
            menuItemList.add(popupMenuItem);
        }

        navigationAdapter = new BottomNavigationAdapter(getIView().getContext(), menuItemList, 5);
        getIView().getBottomMenu().setAdapter(navigationAdapter);
        navigationAdapter.setOnOptionSelectedListener(this);

        int position = getMenuPositionByAction(homeAction);


        navigationAdapter.setCurrentSelection(position);
        hideAll();
    }
    private int getMenuPositionByAction(String action) {
        if (action != null)
            for (int i = 0; i < menuItemList.size(); i++) {
                if (menuItemList.get(i).getTitle() != null &&
                        menuItemList.get(i).getTitle().equalsIgnoreCase(action)) {
                    return i;
                }
            }
        return 0;
    }

    @Override
    public boolean onOptionSelected(PopupMenuItem popupMenuItem, int position) {

        String action = popupMenuItem.getTitle();
        if (action != null)
            switch (action) {

                case ACTION_FAVOURITES:
                   fragmentBase = new FavouriteVideoGridFragment();
                    break;
                case ACTION_SEARCH:
                    Intent intent = new Intent(getIView().getActivity(), SearchActivityScreen.class);
                    getIView().getActivity().startActivity(intent);
                    break;
                case ACTION_POLL:
                    intent = new Intent(getIView().getActivity(), PollWebActivity.class);
                    getIView().getActivity().startActivity(intent);
                    break;
                case ACTION_COMING:
                    fragmentBase = new ToolbarFragment();
                    break;
                case ACTION_SUBSCRIBE:
                    fragmentBase = new PackageFragment();

                    break;
                case ACTION_LANGUAGE:
                    fragmentBase = new LanguageFragmentIn();
                    break;
                case ACTION_ABOUT:
                    fragmentBase = new HtmlViewFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString(HtmlViewFragment.WEB_URL, "http://www.flowerstv.in/about-us/");
                    bundle.putString(HtmlViewFragment.PAGE_TITLE, action);
                    fragmentBase.setArguments(bundle);
                    break;
                case ACTION_TERMS:
                    fragmentBase = new HtmlViewFragment();
                    bundle = new Bundle();
                    bundle.putString(HtmlViewFragment.WEB_URL, "http://www.flowerstv.in/terms/");
                    bundle.putString(HtmlViewFragment.PAGE_TITLE, action);
                    fragmentBase.setArguments(bundle);
                    break;
                case ACTION_CONTACT_US:
                    fragmentBase = new HtmlViewFragment();
                    bundle = new Bundle();
                    bundle.putString(HtmlViewFragment.WEB_URL, "http://www.flowerstv.in/contact-us/");
                    bundle.putString(HtmlViewFragment.PAGE_TITLE, action);
                    fragmentBase.setArguments(bundle);
                    break;
                case ACTION_INFO:
                    fragmentBase = new InfoFragment();
                    bundle = new Bundle();
                    bundle.putString(YoutubeVideoGridVideoGridFragment.TITLE_TEXT, action);
                    fragmentBase.setArguments(bundle);
                    break;
                default:
                    if (action.equalsIgnoreCase(ACTION_HOME)) {
                        fragmentBase = new HomeFragment();
                    }else if(action.equals(ACTION_COMING)){
                        fragmentBase = new ToolbarFragment();
                    }
                    else {
                        fragmentBase = new YoutubeVideoGridVideoGridFragment();
                    }
                    scrennHistory.add(fragmentBase);
                    bundle = new Bundle();
                    bundle.putSerializable(YoutubeVideoGridVideoGridFragment.LOAD_DATA, popupMenuItem.getId());
                    bundle.putString(YoutubeVideoGridVideoGridFragment.TITLE_TEXT, popupMenuItem.getTitle());
                    bundle.putSerializable(YoutubeVideoGridVideoGridFragment.KEY_LOADER_TYPE,
                            YoutubeVideoGridVideoGridFragment.LOADER_TYPE_MENU);
                    fragmentBase.setArguments(bundle);
                    scrennHistory.add(0, fragmentBase);
                    ((HolderActivity) getIView().getActivity()).showLogo(popupMenuItem.getTitle());
                    break;
            }
          if (fragmentBase != null)
         showFragment(fragmentBase);

        return true;
    }
    private void showFragment(Fragment fragment) {
        getIView().getActivity().getFragmentManager().beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
    }
    public boolean onBackPressed() {
        String title = ((HolderActivity) getIView().getActivity()).getTitleTop();
        System.out.println("title:"+title);
        if(title.equals("Home".trim())){
            askExitDialog();
        }
        else {
            int position = getMenuPositionByAction(homeAction);
            PopupMenuItem popupMenuItem = menuItemList.get(position);
            navigationAdapter.setCurrentSelection(position);
            //changed
      //     onOptionSelected(popupMenuItem,position);
        }

        return true;
    }

    private AlertDialog dialog;
    public void askExitDialog() {
        if (dialog != null)
            dialog.cancel();

        AlertDialog.Builder builder = new AlertDialog.Builder(getIView().getActivity());
        builder.setTitle("Confirm Exit");
        builder.setMessage("Do you want to exit?");
        builder.setPositiveButton("Yes", (dialog, which) -> {
            getIView().getActivity().finish();
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> {
            dialog.dismiss();
        });
        dialog = builder.create();
        dialog.show();
    }
}
